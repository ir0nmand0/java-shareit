package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Item {
    @EqualsAndHashCode.Include
    private long id;
    private long userId;
    private String name;
    private String description;
    private Boolean available;
}
