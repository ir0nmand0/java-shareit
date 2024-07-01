package ru.practicum.shareit.user.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;

@Component
public class UserToUserDtoConverter implements Converter<User, UserDto> {
    @Override
    public UserDto convert(final User src) {
        return UserDto.builder()
                .id(src.getId())
                .email(src.getEmail())
                .name(src.getName())
                .build();
    }
}
