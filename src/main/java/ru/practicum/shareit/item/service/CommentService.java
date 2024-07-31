package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.comment.dto.CreateCommentDto;
import ru.practicum.shareit.item.model.dto.ItemWithCommentDto;

import java.util.Collection;

public interface CommentService {
    CommentDto create(CreateCommentDto createCommentDto, final long itemId, final long userId);

    ItemWithCommentDto findOneByItemIdAndUserId(final long itemId, final long userId);

    Collection<ItemWithCommentDto> findAllByUserId(final long userId);
}
