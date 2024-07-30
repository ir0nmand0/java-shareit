package ru.practicum.shareit.booking.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.dto.BookerIdDto;
import ru.practicum.shareit.user.model.User;

@Component
public class UserToBookerIdDtoConverter implements Converter<User, BookerIdDto> {
    @Override
    public BookerIdDto convert(final User source) {
        return new BookerIdDto(source.getId());
    }
}
