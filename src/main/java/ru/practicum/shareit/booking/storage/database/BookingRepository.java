package ru.practicum.shareit.booking.storage.database;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b from Booking b where b.id = :id and (b.booker.id = :userId or b.item.userId = :userId) "
            + "order by b.id")
    Optional<Booking> findOneByIdAndBookerIdOrItemUserId(final long id, final long userId);

    Collection<Booking> findAllByBookerIdOrderByIdDesc(final long id);

    @Modifying
    @Query(value = "update bookings b set b.start_time = :#{#start.toString()}, "
            + "b.end_time = :#{#end.toString()}, b.status = :#{#status.name()} "
            + "where b.id = :id and b.item_id = :itemId and b.booker_id = :userId", nativeQuery = true)
    void updateStartAndEndAndStatus(@Param("start") final LocalDateTime start,
                                    @Param("end") final LocalDateTime end,
                                    @Param("status") final BookingStatus status,
                                    @Param("id") final long id,
                                    @Param("itemId") final long itemId,
                                    @Param("userId") final long userId);

    @Modifying
    @Query(value = "update bookings b set b.start_time = :#{#start.toString()}, b.end_time = :#{#end.toString()} "
            + "where b.id = :id and b.item_id = :itemId and b.booker_id = :userId", nativeQuery = true)
    void updateStartAndEnd(@Param("start") final LocalDateTime start,
                           @Param("end") final LocalDateTime end,
                           @Param("id") final long id,
                           @Param("itemId") final long itemId,
                           @Param("userId") final long userId);

    @Modifying
    @Query(value = "update bookings b set b.start_time = :#{#start.toString()}, b.status = :#{#status.name()} "
            + "where b.id = :id and b.item_id = :itemId and b.booker_id = :userId", nativeQuery = true)
    void updateStartAndStatus(@Param("start") final LocalDateTime start,
                              @Param("status") final BookingStatus status,
                              @Param("id") final long id,
                              @Param("itemId") final long itemId,
                              @Param("userId") final long userId);

    @Modifying
    @Query(value = "update bookings b set b.end_time = :#{#end.toString()}, b.status = :#{#status.name()} "
            + "where b.id = :id and b.item_id = :itemId and b.booker_id = :userId", nativeQuery = true)
    void updateEndAndStatus(@Param("end") final LocalDateTime end,
                            @Param("status") final BookingStatus status,
                            @Param("id") final long id,
                            @Param("itemId") final long itemId,
                            @Param("userId") final long userId);

    @Modifying
    @Query(value = "update bookings b set b.start_time = :#{#start.toString()} "
            + "where b.id = :id and b.item_id = :itemId and b.booker_id = :userId", nativeQuery = true)
    void updateStart(@Param("start") final LocalDateTime start,
                     @Param("id") final long id,
                     @Param("itemId") final long itemId,
                     @Param("userId") final long userId);

    @Modifying
    @Query(value = "update bookings b set b.end_time = :#{#end.toString()} "
            + "where b.id = :id and b.item_id = :itemId and b.booker_id = :userId", nativeQuery = true)
    void updateEnd(@Param("end") final LocalDateTime end,
                   @Param("id") final long id,
                   @Param("itemId") final long itemId,
                   @Param("userId") final long userId);

    @Modifying
    @Query(value = "update bookings b set b.status = :#{#status.name()} "
            + "where b.id = :id and b.item_id = :itemId and b.booker_id = :userId", nativeQuery = true)
    void updateStatus(@Param("status") final BookingStatus status,
                      @Param("id") final long id,
                      @Param("itemId") final long itemId,
                      @Param("userId") final long userId);

    Optional<Booking> findByIdAndItemUserId(final long id, final long userId);

    //Аналог findByBookerIdAndItemIdAndStatusAndEndLessThanEqualOrderByEndDesc
    @Query("select b from Booking b join fetch b.item join fetch b.booker where b.booker.id = :userId "
            + "and b.item.id = :itemId and b.status = 'APPROVED' and b.end <= CURRENT_TIMESTAMP "
            + "order by b.end desc limit 1")
    Optional<Booking> findPastApproved(@Param("userId") final long userId,
                                       @Param("itemId") final long itemId);

    Optional<Booking> findByBookerIdAndItemIdAndStatusAndEndLessThanEqualOrderByEndDesc(final long userId,
                                                                                        final long itemId,
                                                                                        final BookingStatus status,
                                                                                        final LocalDateTime now,
                                                                                        final Limit limit);

    @Query("select b from Booking b where b.id = :id and (b.booker.id = :userId or b.item.userId = :userId)"
            + "order by b.id")
    Optional<Booking> findBookingByBookingIdAndBookerIdOrItemUserId(@Param("id") final long id,
                                                                    @Param("userId") final long userId);

    //Аналог findByBookerIdAndStatusOrderByIdDesc
    @Query("select b from Booking b where b.status = :status and b.booker.id = :userId order by b.id desc")
    Collection<Booking> findByBookerIdAndStatus(@Param("userId") final long userId,
                                                @Param("status") BookingStatus status);

    Collection<Booking> findByBookerIdAndStatusOrderByIdDesc(final long userId, BookingStatus status);

    //Аналог findByBookerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderById
    @Query("select b from Booking b where b.start <= CURRENT_TIMESTAMP " +
            "and b.end >= CURRENT_TIMESTAMP and b.booker.id = :userId order by b.id")
    Collection<Booking> findCurrentBookings(@Param("userId") final long userId);

    Collection<Booking> findByBookerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderById(final long userId,
                                                                                           final LocalDateTime nowStart,
                                                                                           final LocalDateTime nowEnd);

    //Аналог findByBookerIdAndEndLessThanOrderByIdDesc
    @Query(value = "select b from Booking b where b.end < CURRENT_TIMESTAMP and b.booker.id = :userId "
            + "order by b.id desc")
    Collection<Booking> findPastBookings(@Param("userId") final long userId);

    Collection<Booking> findByBookerIdAndEndLessThanOrderByIdDesc(final long userId, final LocalDateTime now);

    //Аналог findByBookerIdAndStartGreaterThanOrderByIdDesc
    @Query("select b from Booking b where b.start > CURRENT_TIMESTAMP and b.booker.id = :userId "
            + "order by b.id desc")
    Collection<Booking> findFutureBookings(@Param("userId") final long userId);

    Collection<Booking> findByBookerIdAndStartGreaterThanOrderByIdDesc(final long userId, final LocalDateTime now);

    Collection<Booking> findBookingsByItemUserIdAndStatusOrderByIdDesc(final long userId, final BookingStatus status);

    @Query("select b from Booking b where b.start <= CURRENT_TIMESTAMP and b.end >= CURRENT_TIMESTAMP and "
            + "b.item.userId = :userId order by b.id desc")
    Collection<Booking> findCurrentForOwner(@Param("userId") final long userId);

    //Алалог findByItemUserIdAndEndLessThanOrderByIdDesc
    @Query("select b from Booking b where b.end < CURRENT_TIMESTAMP and b.item.userId = :userId "
            + "order by b.id desc")
    Collection<Booking> findPastForOwner(@Param("userId") final long userId);

    Collection<Booking> findByItemUserIdAndEndLessThanOrderByIdDesc(final long userId, final LocalDateTime now);

    //Аналог findByItemUserIdAndStartGreaterThanOrderByIdDesc
    @Query("select b from Booking b where b.start > CURRENT_TIMESTAMP and b.item.userId = :userId "
            + "order by b.id desc")
    Collection<Booking> findFutureByUserId(@Param("userId") final long userId);

    Collection<Booking> findByItemUserIdAndStartGreaterThanOrderByIdDesc(final long userId, final LocalDateTime now);

    Collection<Booking> findAllByItemUserIdOrderByIdDesc(final long userId);
}
