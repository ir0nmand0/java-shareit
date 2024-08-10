package ru.practicum.shareit.item.converter.comment;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.comment.dto.CommentDto;

@Component
public class CommentToCommentDtoConverter implements Converter<Comment, CommentDto> {
    @Override
    public CommentDto convert(final Comment source) {
        return CommentDto.builder()
                .id(source.getId())
                .text(source.getText())
                .authorName(source.getAuthor().getName())
                .created(source.getCreated())
                .build();
    }
}
