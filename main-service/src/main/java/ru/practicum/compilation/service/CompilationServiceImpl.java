package ru.practicum.compilation.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDtoOut;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.error.NotFoundException;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.repository.EventRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.compilation.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.compilation.model.Compilation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDtoOut createCompilation(final CompilationDtoIn compilationRequestDto) {
        final List<Event> events = eventRepository.findByIdIn(compilationRequestDto.getEvents());
        final Compilation compilation = CompilationMapper.toCompilation(compilationRequestDto, events);

        if (Objects.isNull(compilationRequestDto.getPinned())) {
            compilation.setPinned(false);
        }

        final Compilation newCompilation = compilationRepository.save(compilation);
        log.info("Подборка с id = {} создана.", compilation.getId());
        return CompilationMapper.toCompilationDto(newCompilation, events.stream()
                .map(EventMapper::toEventSmallDto)
                .toList());
    }

    @Override
    public void deleteCompilation(final Long compId) {
        compilationRepository.deleteById(compId);
        log.info("Удаление подборки с id = {}.", compId);
    }

    @Override
    public CompilationDtoOut updateCompilation(final CompilationDto compilationDto, final Long compId) {
        final Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборки с id = {} не существует." + compId));
        final List<Event> events = eventRepository.findByIdIn(compilationDto.getEvents());
        if (Objects.nonNull(compilationDto.getEvents())) {
            compilation.setEvents(events);
        }
        if (Objects.nonNull(compilationDto.getPinned())) {
            compilation.setPinned(compilationDto.getPinned());
        }
        if (Objects.nonNull(compilationDto.getTitle())) {
            compilation.setTitle(compilationDto.getTitle());
        }

        final Compilation newCompilation = compilationRepository.save(compilation);
        log.info("Обновление данных подборки с id = {}.", compId);
        return CompilationMapper.toCompilationDto(newCompilation, events.stream()
                .map(EventMapper::toEventSmallDto)
                .toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDtoOut> getAllCompilations(final Boolean pinned, final int from, final int size) {
        final PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        final Sort sort = Sort.by(Sort.Direction.ASC, "id");

        List<Compilation> compilationList;
        if (Objects.nonNull(pinned)) {
            compilationList = compilationRepository.findByPinned(pinned, pageRequest);
        } else {
            compilationList = compilationRepository.findAll(pageRequest).toList();
        }
        if (compilationList.isEmpty()) {
            log.info("Подборок событий еще нет.");
            return new ArrayList<>();
        }
        log.info("Получение списка подборок событий.");
        return compilationList.stream()
                .map(c -> CompilationMapper.toCompilationDto(c, c.getEvents().stream()
                        .map(EventMapper::toEventSmallDto).toList())).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDtoOut getCompilationById(final Long compId) {
        final Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборки с id = {} не существует." + compId));
        log.info("Получение данных подборки с id = {}.", compId);
        return CompilationMapper.toCompilationDto(compilation, compilation.getEvents().stream()
                .map(EventMapper::toEventSmallDto)
                .toList());
    }
}
