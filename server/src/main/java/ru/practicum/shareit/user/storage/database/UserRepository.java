package ru.practicum.shareit.user.storage.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(final String email);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update User u set u.name = :name, u.email = :email where u.id = :id")
    void updateNameAndEmail(@Param("name") final String name,
                            @Param("email") final String email,
                            @Param("id") final long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update User u set u.name = :name where u.id = :id")
    void updateName(@Param("name") final String name, @Param("id") final long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update User u set u.email = :email where u.id = :id")
    void updateEmail(@Param("email") final String email, @Param("id") final long id);
}
