package ru.practicum.shareit.booking.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookerIdDto;

@Component
public class BookingToBookerIdDtoConverter implements Converter<Booking, BookerIdDto> {
    @Override
    public BookerIdDto convert(final Booking source) {
        return new BookerIdDto(source.getBooker().getId());
    }
}
