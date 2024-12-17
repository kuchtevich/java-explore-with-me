package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationDtoIn;
import ru.practicum.compilation.dto.CompilationDtoOut;

import java.util.List;

public interface CompilationService {
    CompilationDtoOut createCompilation(final CompilationDtoIn compilationRequestDto);

    void deleteCompilation(final Long compId);

    CompilationDtoOut updateCompilation(final CompilationDto compilationDto, final Long compId);

    List<CompilationDtoOut> getAllCompilations(final Boolean pinned, final int from, final int size);

    CompilationDtoOut getCompilationById(final Long compId);
}
