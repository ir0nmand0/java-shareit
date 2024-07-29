package ru.practicum.shareit.item.storage.database;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.Collection;
import java.util.Optional;

@Primary
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InDbItemStorage implements ItemStorage {
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public void update(final Item item) {
        existsByIdOrElseThrow(item.getId());
        itemRepository.saveAndFlush(item);
    }

    @Override
    @Transactional
    public void patch(final Item item) {

        final boolean nameIsEmpty = ObjectUtils.isEmpty(item.getName());
        final boolean descriptionIsEmpty = ObjectUtils.isEmpty(item.getDescription());
        final boolean availableIsEmpty = ObjectUtils.isEmpty(item.getAvailable());

        if (item.getId() <= 0 || item.getUserId() <= 0) {
            throw new ConditionsNotMetException("item", "id or userId must be posititve");
        }

        if (nameIsEmpty && descriptionIsEmpty && availableIsEmpty) {
            throw new ConditionsNotMetException("item", "name, description or availableIsEmpty fields cannot be empty");
        }

        if (!nameIsEmpty && !descriptionIsEmpty && !availableIsEmpty) {
            itemRepository.updateNameAndDescriptionAndAvailable(item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    item.getId()
            );
        }

        if (!nameIsEmpty && !descriptionIsEmpty && availableIsEmpty) {
            itemRepository.updateNameAndDescription(item.getName(), item.getDescription(), item.getId());
        }

        if (!nameIsEmpty && descriptionIsEmpty && !availableIsEmpty) {
            itemRepository.updateNameAndAvailable(item.getName(), item.getAvailable(), item.getId());
        }

        if (nameIsEmpty && !descriptionIsEmpty && !availableIsEmpty) {
            itemRepository.updateDescriptionAndAvailable(item.getDescription(),
                    item.getAvailable(),
                    item.getId()
            );
        }

        if (!nameIsEmpty && descriptionIsEmpty && availableIsEmpty) {
            itemRepository.updateName(item.getName(), item.getId());
        }


        if (nameIsEmpty && !descriptionIsEmpty && availableIsEmpty) {
            itemRepository.updateDescription(item.getDescription(), item.getId());
        }

        if (nameIsEmpty && descriptionIsEmpty && !availableIsEmpty) {
            itemRepository.updateAvailable(item.getAvailable(), item.getId());
        }
    }

    @Override
    public Collection<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public Optional<Item> findOneById(final long id) {
        return itemRepository.findById(id);
    }

    @Override
    public Collection<Item> findAllByUserId(final long userId) {
        return itemRepository.findAllByUserId(userId);
    }

    @Override
    public Collection<Item> findAllByName(final String name) {
        return itemRepository.findByName(name);
    }

    @Override
    public Collection<Item> findAllByText(final String text) {
        return itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(text,
                text);
    }

    @Override
    public Collection<Item> findAllByDescription(final String description) {
        return itemRepository.findByDescription(description);
    }

    @Override
    @Transactional
    public void delete(final long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public Item findOneByIdOrElseThrow(final long id) {
        return findOneById(id).orElseThrow(() -> new EntityNotFoundException("item", "id not found"));
    }

    @Override
    public boolean existsById(final long id) {
        return itemRepository.existsById(id);
    }

    @Override
    public boolean existsByIdAndAvailableIsTrue(final long id) {
        return itemRepository.existsByIdAndAvailableIsTrue(id);
    }

    @Override
    public void existsByIdAndAvailableIsTrueOrElseThrow(final long id) {
        Item item = findOneByIdOrElseThrow(id);

        if (item.getAvailable()) {
            return;
        }

        throw new ConditionsNotMetException("item", "not allowed");
    }

    @Override
    public void existsByIdOrElseThrow(final long id) {
        if (!existsById(id)) {
            throw new EntityNotFoundException("item", "id not found");
        }
    }
}
