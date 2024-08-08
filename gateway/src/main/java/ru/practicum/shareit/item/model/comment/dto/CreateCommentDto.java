package ru.practicum.shareit.item.model.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCommentDto(
        @Size(max = 5000)
        @NotBlank
        String text
) {
}
