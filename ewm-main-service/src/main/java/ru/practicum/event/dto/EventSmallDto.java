package ru.practicum.event.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.category.dto.CategoryDtoOut;
import ru.practicum.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
public class EventSmallDto {
    private Long id;

    private UserDto initiator;

    private CategoryDtoOut category;

    Integer confirmedRequests;

    private String title;

    private String annotation;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Boolean paid;

    Integer views;
}
