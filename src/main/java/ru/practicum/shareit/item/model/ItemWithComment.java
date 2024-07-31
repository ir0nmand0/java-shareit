package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.comment.Comment;

import java.util.Collection;

@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ItemWithComment {
    private final Item item;
    private final Booking lastBooking;
    private final Booking nextBooking;
    private Collection<Comment> comments;
}
