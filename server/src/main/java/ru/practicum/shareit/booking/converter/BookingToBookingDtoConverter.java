package ru.practicum.shareit.booking.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.item.converter.ItemToItemIdAndNameDtoConverter;

@Component
@RequiredArgsConstructor
public class BookingToBookingDtoConverter implements Converter<Booking, BookingDto> {
    private final UserToBookerIdDtoConverter bookerIdDtoConverter;
    private final ItemToItemIdAndNameDtoConverter itemIdAndNameDtoConverter;

    @Override
    public BookingDto convert(final Booking source) {
        return BookingDto.builder()
                .id(source.getId())
                .booker(bookerIdDtoConverter.convert(source.getBooker()))
                .item(itemIdAndNameDtoConverter.convert(source.getItem()))
                .start(source.getStart())
                .end(source.getEnd())
                .status(source.getStatus())
                .build();

    }
}
