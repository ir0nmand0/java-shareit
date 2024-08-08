package ru.practicum.shareit.item.model.comment.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentDto(
        long id,
        String text,
        String authorName,
        LocalDateTime created) {

}
