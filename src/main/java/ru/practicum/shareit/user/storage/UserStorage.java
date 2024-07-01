package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    User save(User user);

    void update(final User user);

    User patch(final User user);

    Collection<User> findAll();

    Optional<User> findById(final long id);

    Optional<User> findByEmail(final String email);

    void delete(final long id);
}
