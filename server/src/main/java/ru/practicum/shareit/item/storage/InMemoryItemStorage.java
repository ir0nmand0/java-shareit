package ru.practicum.shareit.item.storage;

import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

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
        existsByIdOrElseThrow(item.getId());
        items.put(item.getId(), item);
    }

    @Override
    public void patch(final Item item) {
    }

    @Override
    public Collection<Item> findAll() {
        return items.values();
    }

    @Override
    public Optional<Item> findOneById(final long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Optional<Item> findAllByUserIdAndRequestId(final long userId, final long requestId) {
        return findAll().stream()
                .filter(item -> item.getUserId() == userId && item.getRequest().getId() == requestId)
                .findFirst();
    }

    @Override
    public Optional<Item> findOneByRequestId(final long id) {
        return items.entrySet().stream()
                .filter(longItemEntry -> longItemEntry.getValue().getRequest().getId() == id)
                .findFirst()
                .map(Map.Entry::getValue);
    }

    @Override
    public Collection<Item> findAllByName(@NotBlank final String name) {
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
    public Collection<Item> findAllByText(@NotBlank final String text) {
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
    public Collection<Item> findAllByDescription(@NotBlank final String description) {
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

    @Override
    public Item findOneByIdOrElseThrow(final long id) {
        return findOneById(id).orElseThrow(() -> new EntityNotFoundException("item", "id not found"));
    }

    @Override
    public boolean existsById(final long id) {
        return items.containsKey(id) && !ObjectUtils.isEmpty(items.get(id));
    }

    @Override
    public boolean existsByIdAndAvailableIsTrue(final long id) {
        return existsById(id) && items.get(id).getAvailable();
    }

    @Override
    public void existsByIdAndAvailableIsTrueOrElseThrow(final long id) {
        Item item = findOneByIdOrElseThrow(id);

        if (item.getAvailable()) {
            return;
        }

        throw new ConditionsNotMetException("item", "not allowed");
    }

    private boolean isAvailable(final Boolean available) {
        return Objects.nonNull(available) && available;
    }

    @Override
    public void existsByIdOrElseThrow(final long id) {
        if (!existsById(id)) {
            throw new EntityNotFoundException("item", "id not found");
        }
    }
}
