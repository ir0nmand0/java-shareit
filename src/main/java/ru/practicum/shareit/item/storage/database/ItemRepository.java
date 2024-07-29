package ru.practicum.shareit.item.storage.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Collection<Item> findAllByUserId(final long userId);

    Collection<Item> findByName(final String name);

    boolean existsByIdAndAvailableIsTrue(final long id);

    Collection<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(final String name,
                                                                                                       final String description);

    Collection<Item> findByDescription(final String description);

    @Modifying
    @Query("update Item i set i.name = :name, i.description = :description, i.available = :available where i.id = :id")
    void updateNameAndDescriptionAndAvailable(@Param("name") final String name,
                                              @Param("description") final String description,
                                              @Param("available") final Boolean available,
                                              @Param("id") final long id);

    @Modifying
    @Query("update Item i set i.name = :name, i.description = :description where i.id = :id")
    void updateNameAndDescription(@Param("name") final String name,
                                  @Param("description") final String description,
                                  @Param("id") final long id);

    @Modifying
    @Query("update Item i set i.name = :name, i.available = :available where i.id = :id")
    void updateNameAndAvailable(@Param("name") final String name,
                                @Param("available") final Boolean available,
                                @Param("id") final long id);

    @Modifying
    @Query("update Item i set i.description = :description, i.available = :available where i.id = :id")
    void updateDescriptionAndAvailable(@Param("description") final String description,
                                       @Param("available") final Boolean available,
                                       @Param("id") final long id);

    @Modifying
    @Query("update Item i set i.name = :name where i.id = :id")
    void updateName(@Param("name") final String name, @Param("id") final long id);

    @Modifying
    @Query("update Item i set i.description = :description where i.id = :id")
    void updateDescription(@Param("description") final String description, @Param("id") final long id);

    @Modifying
    @Query("update Item i set i.available = :available where i.id = :id")
    void updateAvailable(@Param("available") final Boolean available, @Param("id") final long id);
}
