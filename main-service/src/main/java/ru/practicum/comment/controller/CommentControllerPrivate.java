package ru.practicum.comment.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDtoIn;
import ru.practicum.comment.dto.CommentDtoOut;
import ru.practicum.comment.service.CommentService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
public class CommentControllerPrivate {
    private final CommentService commentService;

    @GetMapping("/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDtoOut> getAllComments(@PathVariable @Positive final Long userId,
                                              @PathVariable @Positive final Long eventId,
                                              @RequestParam(defaultValue = "0") final int from,
                                              @RequestParam(defaultValue = "10") final int size) {
        return commentService.getAllComments(userId, eventId, from, size);
    }

    @PostMapping("/events/{eventId}/comments")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CommentDtoOut createComment(@RequestBody @Validated final CommentDtoIn commentDtoIn,
                                       @PathVariable @Positive final Long userId,
                                       @PathVariable @Positive final Long eventId) {
        return commentService.createComment(commentDtoIn, userId, eventId);
    }

    @PatchMapping("/comments/{commentId}")
    public CommentDtoOut updateComment(@RequestBody @Validated final CommentDtoIn commentDtoIn,
                                       @PathVariable @Positive final Long userId,
                                       @PathVariable @Positive final Long commentId) {
        return commentService.updateComment(commentDtoIn, userId, commentId);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable @Positive final Long userId, @PathVariable @Positive final Long commentId) {
        commentService.deleteComment(userId, commentId);
    }

    @GetMapping("/events/{eventId}/comments/search")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDtoOut> searchComments(@PathVariable final Long userId, @PathVariable final Long eventId,
                                              @RequestParam @NotBlank final String text,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero final Integer from,
                                              @RequestParam(defaultValue = "10") @Positive final Integer size) {
        return commentService.searchComments(userId, eventId, text, from, size);
    }
}
