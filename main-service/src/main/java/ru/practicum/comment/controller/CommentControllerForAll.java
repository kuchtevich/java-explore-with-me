package ru.practicum.comment.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDtoOut;
import ru.practicum.comment.service.CommentService;
import java.util.List;

@Validated
@RestController
@RequestMapping
@RequiredArgsConstructor
public class CommentControllerForAll {
    private final CommentService commentService;

    @GetMapping("events/{eventId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDtoOut> getAllCommentsByEvent(@PathVariable final Long eventId,
                                                     @RequestParam(defaultValue = "0") final int from,
                                                     @RequestParam(defaultValue = "10") final int size) {
        return commentService.getAllCommentsByEvent(eventId, from, size);
    }

    @GetMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDtoOut getCommentById(@PathVariable @Positive final Long commentId) {
        return commentService.getCommentById(commentId);
    }
}
