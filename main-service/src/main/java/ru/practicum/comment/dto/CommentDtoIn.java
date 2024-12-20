package ru.practicum.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentDtoIn {
    @NotBlank
    private String text;
}
//