package ru.practicum.shareit.user.storage.database;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Primary
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InDbUserStorage implements UserStorage {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void update(final User user) {
        existsByIdOrElseThrow(user.getId());
        userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public void patch(final User user) {
        final boolean nameIsEmpty = ObjectUtils.isEmpty(user.getName());
        final boolean emailIsEmpty = ObjectUtils.isEmpty(user.getEmail());

        if (user.getId() <= 0) {
            throw new ConditionsNotMetException("user", "id must be posititve");
        }

        if (nameIsEmpty && emailIsEmpty) {
            throw new ConditionsNotMetException("user", "name or email fields cannot be empty");
        }

        if (!nameIsEmpty && !emailIsEmpty) {
            userRepository.updateNameAndEmail(user.getName(), user.getEmail(), user.getId());
        } else if (!nameIsEmpty) {
            userRepository.updateName(user.getName(), user.getId());
        } else {
            userRepository.updateEmail(user.getEmail(), user.getId());
        }
    }

    @Override
    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findOneById(final long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findOneByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void deleteById(final long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsById(final long id) {
        return userRepository.existsById(id);
    }

    @Override
    public User findOneByIdOrElseThrow(final long id) {
        return findOneById(id).orElseThrow(() -> new EntityNotFoundException("user", "id not found"));
    }

    @Override
    public void existsByIdOrElseThrow(final long id) {
        if (!existsById(id)) {
            throw new EntityNotFoundException("user", "id not found");
        }
    }

}
