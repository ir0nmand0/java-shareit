package ru.practicum.shareit.item.converter.comment;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.dto.BookingIdAndBookerIdDto;
import ru.practicum.shareit.item.model.ItemWithComment;
import ru.practicum.shareit.item.model.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.dto.ItemWithCommentDto;

import java.util.List;
import java.util.Objects;

@Component
public class ItemWCToItemWCDtoConverter implements Converter<ItemWithComment, ItemWithCommentDto> {
    @Override
    public ItemWithCommentDto convert(final ItemWithComment source) {
        return ItemWithCommentDto.builder()
                .id(source.getItem().getId())
                .name(source.getItem().getName())
                .description(source.getItem().getDescription())
                .available(source.getItem().getAvailable())
                .lastBooking(Objects.isNull(source.getLastBooking()) ? null : BookingIdAndBookerIdDto.builder()
                        .id(source.getLastBooking().getId())
                        .bookerId(source.getLastBooking().getBooker().getId())
                        .build())
                .nextBooking(Objects.isNull(source.getNextBooking()) ? null : BookingIdAndBookerIdDto.builder()
                        .id(source.getNextBooking().getId())
                        .bookerId(source.getNextBooking().getBooker().getId())
                        .build())
                .comments(Objects.isNull(source.getComments()) ? List.of() : source.getComments().stream()
                        .map(comment -> CommentDto.builder()
                                .id(comment.getId())
                                .text(comment.getText())
                                .authorName(comment.getAuthor().getName())
                                .created(comment.getCreated())
                                .build())
                        .toList())
                .build();
    }
}
