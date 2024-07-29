package ru.practicum.shareit.item.model.dto;

import lombok.Builder;
import ru.practicum.shareit.booking.model.dto.BookerId;
import ru.practicum.shareit.item.model.comment.dto.CommentDto;

import java.util.Collection;

@Builder
public record ItemWithCommentDto(
        long id,
        String name,
        String description,
        boolean available,
        BookerId lastBooking,
        BookerId nextBooking,
        Collection<CommentDto> comments
) {
}
