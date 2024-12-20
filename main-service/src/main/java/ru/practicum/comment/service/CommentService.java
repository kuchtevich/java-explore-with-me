package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentDtoIn;
import ru.practicum.comment.dto.CommentDtoOut;

import java.util.List;

public interface CommentService {
    List<CommentDtoOut> getAllComments(final Long userId, final Long eventId, final int from, final int size);

    CommentDtoOut createComment(final CommentDtoIn commentDtoIn, final Long userId, final Long eventId);

    CommentDtoOut updateComment(final CommentDtoIn commentDtoIn, final Long userId, final Long commentId);

    void deleteComment(final Long userId, final Long commentId);

    List<CommentDtoOut> getAllCommentsByEvent(final Long eventId, final int from, final int size);

    CommentDtoOut getCommentById(final Long commentId);

    void deleteCommentByAdmin(final Long eventId);

    List<CommentDtoOut> searchComments(final Long userId, final Long eventId, final String text, final Integer from,
                                       final Integer size);
}

