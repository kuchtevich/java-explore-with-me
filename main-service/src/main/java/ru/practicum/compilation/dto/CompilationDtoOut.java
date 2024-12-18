package ru.practicum.compilation.dto;

import lombok.Data;
import ru.practicum.event.dto.EventSmallDto;

import java.util.List;

@Data
public class CompilationDtoOut {
    private Long id;

    private List<EventSmallDto> events;

    private Boolean pinned;

    private String title;
}
