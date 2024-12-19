package ru.practicum.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventAllDto;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventNewDto;
import ru.practicum.event.dto.EventSmallDto;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestStatusDto;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/users/{userId}/events")
public class EventControllerPrivate {
    private final EventService eventService;

    @GetMapping
    public List<EventSmallDto> getAllEvents(@PathVariable @Positive final Long userId,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero final int from,
                                            @RequestParam(defaultValue = "10") @Positive final int size) {
        return eventService.getAllEvents(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventAllDto createEvent(@PathVariable @Positive final Long userId, @RequestBody @Valid final EventNewDto eventNewDto) {
        return eventService.createEvent(userId, eventNewDto);
    }

    @GetMapping("/{eventId}")
    public EventAllDto getEventById(@PathVariable @Positive final Long userId, @PathVariable @Positive final Long eventId) {
        return eventService.getEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventAllDto updateEvent(@PathVariable @Positive final Long userId, @PathVariable @Positive final Long eventId,
                                   @RequestBody @Valid final EventDto eventDto) {
        return eventService.updateEvent(userId, eventId, eventDto);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequestsByEventId(@PathVariable final Long userId, @PathVariable final Long eventId) {
        return eventService.getRequestsByEventId(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public Map<String, List<RequestDto>> approveRequests(@PathVariable final Long userId,
                                                         @PathVariable final Long eventId,
                                                         @RequestBody @Valid final RequestStatusDto requestDto) {
        return eventService.approveRequests(userId, eventId, requestDto);
    }
}
