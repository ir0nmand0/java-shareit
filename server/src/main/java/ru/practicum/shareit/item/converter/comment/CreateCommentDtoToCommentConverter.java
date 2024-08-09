package ru.practicum.shareit.item.converter.comment;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.comment.dto.CreateCommentDto;

@Component
public class CreateCommentDtoToCommentConverter implements Converter<CreateCommentDto, Comment> {
    @Override
    public Comment convert(final CreateCommentDto source) {
        return Comment.builder().text(source.text()).build();
    }
}
