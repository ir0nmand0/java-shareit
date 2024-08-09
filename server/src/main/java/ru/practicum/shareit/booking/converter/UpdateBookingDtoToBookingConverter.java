package ru.practicum.shareit.booking.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.UpdateBookingDto;

@Component
public class UpdateBookingDtoToBookingConverter implements Converter<UpdateBookingDto, Booking> {
    @Override
    public Booking convert(final UpdateBookingDto source) {
        return Booking.builder()
                .id(source.id())
                .start(source.start())
                .end(source.end())
                .status(source.status())
                .build();
    }
}