package ru.practicum.shareit.user.storage;

import org.apache.logging.log4j.util.InternalException;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import ru.practicum.shareit.exception.EntityNotFoundByIdException;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Optional;

@Repository
public class InMemoryUserStorage implements UserStorage {
    private final LinkedHashMap<Long, User> users = new LinkedHashMap<>();
    private static long freeId = 0L;

    @Override
    public User save(User user) {
        //ID просто увеличивается, так как тест после удаления ожидает следующий id, а не свободный
        user.setId(++freeId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void update(final User user) {
        findById(user.getId()).orElseThrow(() -> new EntityNotFoundByIdException("user", "id not found"));
        users.put(user.getId(), user);
    }

    @Override
    public User patch(final User user) {
        User patchUser = findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundByIdException("user", "id not found"));

        if (!ObjectUtils.isEmpty(user.getName())) {
            patchUser.setName(user.getName());
        }

        if (!ObjectUtils.isEmpty(user.getEmail())) {
            patchUser.setEmail(user.getEmail());
        }

        return patchUser;
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public Optional<User> findById(final long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        return findAll().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public void delete(final long id) {
        users.remove(id);
    }

    private long findFreeId() {
        long id = 0L;

        if (!ObjectUtils.isEmpty(users.lastEntry())) {
            id = users.lastEntry().getKey();
        }

        if (id == Long.MAX_VALUE) {
            throw new InternalException("free ID not found");
        }

        return ++id;
    }
}
