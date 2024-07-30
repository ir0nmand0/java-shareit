package ru.practicum.shareit.booking.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.item.model.dto.ItemIdAndNameDto;

@Component
@RequiredArgsConstructor
public class BookingToBookingDtoConverter implements Converter<Booking, BookingDto> {
    private final UserToBookerIdDtoConverter bookerIdDtoConverter;

    @Override
    public BookingDto convert(final Booking source) {
        return BookingDto.builder()
                .id(source.getId())
                .booker(bookerIdDtoConverter.convert(source.getBooker()))
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
