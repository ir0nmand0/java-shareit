package ru.practicum.shareit.item.storage.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.ItemWithComment;
import ru.practicum.shareit.item.model.comment.Comment;

import java.util.Collection;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Collection<Comment> findAllByItemId(final long itemId);

    @Query("select c from Comment c where c.item.id = :itemId and (c.author.id = :userId or c.item.userId = :userId)")
    Collection<Comment> findAllByItemIdAndAuthorIdOrItemUserId(@Param("itemId") final long itemId,
                                                               @Param("userId") final long userId);

    @Query("select new ru.practicum.shareit.item.model.ItemWithComment(i, lb, nb) from Item i "
            + "left join fetch Booking lb on lb.item.id = i.id and lb.end = (select max(b.end) "
            + "from Booking b where b.item.id = i.id and b.end <= CURRENT_TIMESTAMP and b.status = 'APPROVED' "
            + "and b.item.userId = :userId) "
            + "left join fetch Booking nb on nb.item.id = i.id "
            + "and nb.start = (SELECT MIN(b.start) from Booking b where b.item.id = i.id "
            + "and b.start >= CURRENT_TIMESTAMP and b.status = 'APPROVED' and b.item.userId = :userId) "
            + "where i.userId = :userId")
    Collection<ItemWithComment> findAllItemsWithCommentsByUserId(@Param("userId") final long userId);

    @Query("select new ru.practicum.shareit.item.model.ItemWithComment(i, lb, nb) from Item i "
            + "left join fetch Booking lb on lb.item.id = i.id and lb.end = (select max(b.end) "
            + "from Booking b where b.item.id = i.id and (b.start <= CURRENT_TIMESTAMP) "
            + "and b.status = 'APPROVED' and b.item.userId = :userId) "
            + "left join fetch Booking nb on nb.item.id = i.id and nb.start = (select MIN(b.start) from Booking b "
            + "where b.item.id = i.id and b.start >= CURRENT_TIMESTAMP and b.status = 'APPROVED' "
            + "and b.item.userId = :userId) where i.id = :itemId")
    Optional<ItemWithComment> findItemWithCommentByItemIdAndUserId(@Param("itemId") final long itemId,
                                                                   @Param("userId") final long userId);
}
