package ru.practicum.shareit.user.storage;

import org.apache.logging.log4j.util.InternalException;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import ru.practicum.shareit.exception.EntityNotFoundException;
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
        existsByIdOrElseThrow(user.getId());
        users.put(user.getId(), user);
    }

    @Override
    public void patch(final User user) {
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public Optional<User> findOneById(final long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findOneByEmail(final String email) {
        return findAll().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public void deleteById(final long id) {
        users.remove(id);
    }

    @Override
    public boolean existsById(final long id) {
        return users.containsKey(id) && !ObjectUtils.isEmpty(users.get(id));
    }

    @Override
    public void existsByIdOrElseThrow(final long id) {
        if (!existsById(id)) {
            throw new EntityNotFoundException("user", "id not found");
        }
    }

    @Override
    public User findOneByIdOrElseThrow(final long id) {
        return findOneById(id).orElseThrow(() -> new EntityNotFoundException("user", "id not found"));
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
