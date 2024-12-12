package ru.practicum.compilation.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDtoOut;
import ru.practicum.compilation.service.CompilationServiceImpl;

import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/compilations")
public class CompilationControllerForAll {
    private final CompilationServiceImpl compilationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDtoOut> getAllCompilations(@RequestParam(required = false) final Boolean pinned,
                                                      @RequestParam(defaultValue = "0") @PositiveOrZero final int from,
                                                      @RequestParam(defaultValue = "10") @Positive final int size) {
        return compilationService.getAllCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDtoOut getCompilationById(@PathVariable final Long compId) {
        return compilationService.getCompilationById(compId);
    }
}
