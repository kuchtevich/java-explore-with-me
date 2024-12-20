package ru.practicum.comment.mapper;

import ru.practicum.comment.dto.CommentDtoIn;
import ru.practicum.comment.dto.CommentDtoOut;
import ru.practicum.comment.model.Comment;

import java.time.LocalDateTime;

import ru.practicum.user.model.User;
import ru.practicum.event.model.Event;

import java.util.List;

import ru.practicum.event.mapper.EventMapper;

import java.util.stream.Collectors;

public class CommentMapper {
    public static Comment toComment(final CommentDtoIn commentDtoIn,
                                    final User user,
                                    final Event event) {

        final Comment comment = new Comment();

        comment.setText(commentDtoIn.getText());
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(user);
        comment.setEvent(event);

        return comment;
    }

    public static CommentDtoOut toCommentDtoOut(final Comment comment) {

        final CommentDtoOut commentDtoOut = new CommentDtoOut();

        commentDtoOut.setId(comment.getId());
        commentDtoOut.setText(comment.getText());
        commentDtoOut.setAuthorName(comment.getAuthor().getName());
        commentDtoOut.setEvent(EventMapper.toEventSmallDto(comment.getEvent()));
        commentDtoOut.setCreated(comment.getCreated());

        return commentDtoOut;
    }

    public static List<CommentDtoOut> toCommentDtoList(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toCommentDtoOut)
                .collect(Collectors.toList());
    }
}
