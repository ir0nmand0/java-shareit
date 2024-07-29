package ru.practicum.shareit.booking.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.dto.CreateBookingDto;

@Component
public class CreateBookingDtoToBookingConverter implements Converter<CreateBookingDto, Booking> {
    @Override
    public Booking convert(final CreateBookingDto source) {
        return Booking.builder()
                .start(source.start())
                .end(source.end())
                .status(BookingStatus.WAITING)
                .build();
    }
}
