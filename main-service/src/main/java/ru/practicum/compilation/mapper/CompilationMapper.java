package ru.practicum.compilation.mapper;

import ru.practicum.compilation.dto.CompilationDtoIn;
import ru.practicum.compilation.dto.CompilationDtoOut;
import ru.practicum.compilation.model.Compilation;
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
                                                     final List<EventShortDto> eventShortDtoList) {

        final CompilationDtoOut compilationDtoOutput = new CompilationDtoOut();

        compilationDtoOutput.setId(compilation.getId());
        compilationDtoOutput.setEvents(eventShortDtoList);
        compilationDtoOutput.setPinned(compilation.getPinned());
        compilationDtoOutput.setTitle(compilation.getTitle());

    }
}
