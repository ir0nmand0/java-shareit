package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    User save(User user);

    void update(final User user);

    void patch(final User user);

    Collection<User> findAll();

    Optional<User> findOneById(final long id);

    Optional<User> findOneByEmail(final String email);

    void deleteById(final long id);

    boolean existsById(final long id);

    void existsByIdOrElseThrow(final long id);

    User findOneByIdOrElseThrow(final long id);
}
