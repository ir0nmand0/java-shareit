package ru.practicum.shareit.item.storage.database;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.ItemWithComment;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.storage.CommentStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InDbCommentStorage implements CommentStorage {
    private final CommentRepository commentRepository;

    @Override
    public Collection<Comment> findAllByItemId(final long itemId) {
        return commentRepository.findAllByItemId(itemId);
    }

    @Override
    @Transactional
    public Comment save(final Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Optional<ItemWithComment> findOneByItemIdAndUserId(final long itemId, final long userId) {
        return commentRepository.findItemWithCommentByItemIdAndUserId(itemId, userId);
    }

    @Override
    public Collection<Comment> findAllByItemIdAndAuthorIdOrItemUserId(final long itemId, final long userId) {
        return commentRepository.findAllByItemIdAndAuthorIdOrItemUserId(itemId, userId);
    }

    @Override
    public Collection<ItemWithComment> findAllByUserId(final long userId) {
        return commentRepository.findAllItemsWithCommentsByUserId(userId);
    }

    @Override
    public Optional<Comment> findOneById(final long id) {
        return commentRepository.findById(id);
    }

    @Override
    public Comment findOneByIdOrElseThrow(final long id) {
        return findOneById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment", "Comment not found by id: " + id));
    }
}
