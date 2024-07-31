package ru.practicum.shareit.item.model.dto;

import lombok.Builder;
import ru.practicum.shareit.booking.model.dto.BookerIdDto;
import ru.practicum.shareit.item.model.comment.dto.CommentDto;

import java.util.Collection;

@Builder
public record ItemWithCommentDto(
        long id,
        String name,
        String description,
        boolean available,
        BookerIdDto lastBooking,
        BookerIdDto nextBooking,
        Collection<CommentDto> comments
) {
}
