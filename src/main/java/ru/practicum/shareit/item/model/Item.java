package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;
    @Column(nullable = false)
    private long userId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Boolean available;
}
