package ru.practicum.shareit.booking.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.BookerId;
import ru.practicum.shareit.item.model.dto.ItemIdAndNameDto;

@Component
public class BookingToBookingDtoConverter implements Converter<Booking, BookingDto> {
    @Override
    public BookingDto convert(final Booking source) {
        return BookingDto.builder()
                .id(source.getId())
                .booker(BookerId.builder()
                        .id(source.getBooker().getId())
                        .build())
                .item(ItemIdAndNameDto.builder()
                        .id(source.getItem().getId())
                        .name(source.getItem().getName())
                        .build())
                .start(source.getStart())
                .end(source.getEnd())
                .status(source.getStatus())
                .build();

    }
}
