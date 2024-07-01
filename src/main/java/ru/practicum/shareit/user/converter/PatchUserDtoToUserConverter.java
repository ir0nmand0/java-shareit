package ru.practicum.shareit.user.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.PatchUserDto;

@Component
public class PatchUserDtoToUserConverter implements Converter<PatchUserDto, User> {
    @Override
    public User convert(final PatchUserDto src) {
        return User.builder()
                .email(src.email())
                .name(src.name())
                .build();
    }
}
