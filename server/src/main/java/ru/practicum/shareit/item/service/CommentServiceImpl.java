package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.ItemWithComment;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.comment.dto.CreateCommentDto;
import ru.practicum.shareit.item.model.dto.ItemWithCommentDto;
import ru.practicum.shareit.item.storage.CommentStorage;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentStorage commentStorage;
    private final BookingStorage bookingStorage;
    @Qualifier("mvcConversionService")
    private final ConversionService cs;

    @Override
    public CommentDto create(CreateCommentDto createCommentDto, final long itemId, final long userId) {
        Booking booking = bookingStorage.findPastApprovedBooking(userId, itemId)
                .orElseThrow(() -> new ConditionsNotMetException("Comment", "Booking not found Past Approved Booking"));

        Comment comment = cs.convert(createCommentDto, Comment.class);
        comment.setAuthor(booking.getBooker());
        comment.setItem(booking.getItem());
        comment.setCreated(LocalDateTime.now());

        return cs.convert(commentStorage.save(comment), CommentDto.class);
    }

    @Override
    public ItemWithCommentDto findOneByItemIdAndUserId(final long itemId, final long userId) {
        ItemWithComment itemWithComment = commentStorage.findOneByItemIdAndUserId(itemId, userId)
                .orElseThrow(() -> new EntityNotFoundException("ItemWithComment",
                                String.format("ItemWithComment not found by id: %d and userId %d", itemId, userId)
                        )
                );

        itemWithComment.setComments(commentStorage.findAllByItemIdAndAuthorIdOrItemUserId(itemId, userId));

        return cs.convert(itemWithComment, ItemWithCommentDto.class);
    }

    @Override
    public Collection<ItemWithCommentDto> findAllByUserId(final long userId) {
        Collection<ItemWithComment> itemWithComments = commentStorage.findAllByUserId(userId);

        if (itemWithComments.isEmpty()) {
            throw new EntityNotFoundException("ItemWithComment", "item with comments not found by userId: " + userId);
        }

        return itemWithComments.stream()
                .map(itemWithComment -> cs.convert(itemWithComment, ItemWithCommentDto.class))
                .toList();
    }
}
