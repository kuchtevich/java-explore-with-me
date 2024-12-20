package ru.practicum.comment.dto;

import lombok.Data;
import ru.practicum.event.dto.EventSmallDto;
import java.time.LocalDateTime;

@Data
public class CommentDtoOut {
    Long id;

    String text;

    String authorName;

    EventSmallDto event;

    LocalDateTime created;
}
