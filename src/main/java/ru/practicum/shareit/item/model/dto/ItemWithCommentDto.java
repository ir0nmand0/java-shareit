package ru.practicum.shareit.item.model.dto;

import lombok.Builder;
import ru.practicum.shareit.booking.model.dto.BookingIdAndBookerIdDto;
import ru.practicum.shareit.item.model.comment.dto.CommentDto;

import java.util.Collection;

@Builder
public record ItemWithCommentDto(
        long id,
        String name,
        String description,
        boolean available,
        BookingIdAndBookerIdDto lastBooking,
        BookingIdAndBookerIdDto nextBooking,
        Collection<CommentDto> comments
) {
}
