package ru.practicum.shareit.user.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UpdateUserDto;

@Component
public class UpdateUserDtoToUserConverter implements Converter<UpdateUserDto, User> {
    @Override
    public User convert(final UpdateUserDto src) {
        return User.builder()
                .id(src.id())
                .email(src.email())
                .name(src.name())
                .build();
    }
}
