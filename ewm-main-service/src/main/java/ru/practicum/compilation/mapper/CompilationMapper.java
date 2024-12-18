package ru.practicum.compilation.mapper;

import ru.practicum.compilation.dto.CompilationDtoIn;
import ru.practicum.compilation.dto.CompilationDtoOut;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventSmallDto;
import ru.practicum.event.model.Event;

import java.util.List;

public class CompilationMapper {
    public static Compilation toCompilation(final CompilationDtoIn compilationRequestDto,
                                            final List<Event> evens) {

        final Compilation compilation = new Compilation();

        compilation.setEvents(evens);
        compilation.setPinned(compilationRequestDto.getPinned());
        compilation.setTitle(compilationRequestDto.getTitle());

        return compilation;
    }

    public static CompilationDtoOut toCompilationDto(final Compilation compilation,
                                                     final List<EventSmallDto> eventSmallDtoList) {

        final CompilationDtoOut compilationDtoOut = new CompilationDtoOut();

        compilationDtoOut.setId(compilation.getId());
        compilationDtoOut.setEvents(eventSmallDtoList);
        compilationDtoOut.setPinned(compilation.getPinned());
        compilationDtoOut.setTitle(compilation.getTitle());

        return compilationDtoOut;
    }
}
