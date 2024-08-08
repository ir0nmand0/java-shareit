package ru.practicum.shareit.booking.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.PatchBookingDto;

@Component
public class PatchBookingDtoToBookingConverter implements Converter<PatchBookingDto, Booking> {
    @Override
    public Booking convert(final PatchBookingDto source) {
        return Booking.builder()
                .start(source.start())
                .end(source.end())
                .status(source.status())
                .build();
    }
}
