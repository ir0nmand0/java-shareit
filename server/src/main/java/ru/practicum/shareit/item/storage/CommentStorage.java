package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.ItemWithComment;
import ru.practicum.shareit.item.model.comment.Comment;

import java.util.Collection;
import java.util.Optional;

public interface CommentStorage {

    Collection<Comment> findAllByItemId(final long itemId);

    Comment save(final Comment comment);

    Optional<ItemWithComment> findOneByItemIdAndUserId(final long itemId, final long userId);

    Collection<Comment> findAllByItemIdAndAuthorIdOrItemUserId(final long itemId, final long userId);

    Collection<ItemWithComment> findAllByUserId(final long userId);

    Optional<Comment> findOneById(final long id);

    Comment findOneByIdOrElseThrow(final long id);
}
