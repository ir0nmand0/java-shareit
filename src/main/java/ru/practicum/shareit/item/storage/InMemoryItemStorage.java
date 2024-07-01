package ru.practicum.shareit.item.storage;

import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import ru.practicum.shareit.exception.EntityNotFoundByIdException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

@Repository
public class InMemoryItemStorage implements ItemStorage {
    private final LinkedHashMap<Long, Item> items = new LinkedHashMap<>();
    private static long freeId = 0L;

    @Override
    public Item save(Item item) {
        item.setId(++freeId);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public void update(final Item item) {
        findById(item.getId()).orElseThrow(() -> new EntityNotFoundByIdException("item", "id not found"));
        items.put(item.getId(), item);
    }

    @Override
    public Item patch(final Item item) {
        Item patchItem = findById(item.getId())
                .orElseThrow(() -> new EntityNotFoundByIdException("item", "id not found"));

        if (!ObjectUtils.isEmpty(item.getName())) {
            patchItem.setName(item.getName());
        }

        if (!ObjectUtils.isEmpty(item.getDescription())) {
            patchItem.setDescription(item.getDescription());
        }

        if (!ObjectUtils.isEmpty(item.getAvailable())) {
            patchItem.setAvailable(item.getAvailable());
        }

        return patchItem;
    }

    @Override
    public Collection<Item> findAll() {
        return items.values();
    }

    @Override
    public Optional<Item> findById(final long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Collection<Item> findByName(@NotBlank final String name) {
        return findAll().stream()
                .filter(item -> {
                            if (ObjectUtils.isEmpty(item.getName()) || !isAvailable(item.getAvailable())) {
                                return false;
                            }

                            return item.getName().toLowerCase().contains(name.toLowerCase());
                        }
                )
                .toList();
    }

    @Override
    public Collection<Item> findAllByUserId(final long userId) {
        return findAll().stream()
                .filter(item -> isAvailable(item.getAvailable()) && item.getUserId() == userId).toList();
    }

    @Override
    public Collection<Item> findByText(@NotBlank final String text) {
        final String toLowerCaseTest = text.toLowerCase();

        return findAll().stream()
                .filter(item -> {
                            if (ObjectUtils.isEmpty(item.getName())
                                    || ObjectUtils.isEmpty(item.getDescription())
                                    || !isAvailable(item.getAvailable())
                            ) {
                                return false;
                            }

                            return item.getName().toLowerCase().contains(toLowerCaseTest)
                                    || item.getDescription().toLowerCase().contains(toLowerCaseTest);
                        }
                )
                .toList();
    }

    @Override
    public Collection<Item> findByDescription(@NotBlank final String description) {
        return findAll().stream()
                .filter(item -> {
                            if (ObjectUtils.isEmpty(item.getDescription()) || !isAvailable(item.getAvailable())) {
                                return false;
                            }

                            return item.getDescription().toLowerCase().contains(description.toLowerCase());
                        }
                )
                .toList();
    }

    @Override
    public void delete(final long id) {
        items.remove(id);
    }

    private boolean isAvailable(final Boolean available) {
        return Objects.nonNull(available) && available;
    }
}
