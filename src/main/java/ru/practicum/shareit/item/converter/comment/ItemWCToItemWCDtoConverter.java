package ru.practicum.shareit.item.converter.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.converter.BookingToBookerIdDtoConverter;
import ru.practicum.shareit.item.model.ItemWithComment;
import ru.practicum.shareit.item.model.dto.ItemWithCommentDto;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ItemWCToItemWCDtoConverter implements Converter<ItemWithComment, ItemWithCommentDto> {
    private final BookingToBookerIdDtoConverter bookerIdDtoConverter;
    private final CommentToCommentDtoConverter commentDtoConverter;

    @Override
    public ItemWithCommentDto convert(final ItemWithComment source) {
        return ItemWithCommentDto.builder()
                .id(source.getItem().getId())
                .name(source.getItem().getName())
                .description(source.getItem().getDescription())
                .available(source.getItem().getAvailable())
                .lastBooking(Objects.isNull(source.getLastBooking()) ? null : bookerIdDtoConverter
                        .convert(source.getLastBooking()))
                .nextBooking(Objects.isNull(source.getNextBooking()) ? null : bookerIdDtoConverter
                        .convert(source.getNextBooking()))
                .comments(Objects.isNull(source.getComments()) ? List.of() : source.getComments().stream()
                        .map(comment -> commentDtoConverter.convert(comment))
                        .toList())
                .build();
    }
}
