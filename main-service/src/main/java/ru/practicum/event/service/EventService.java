package ru.practicum.event.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.event.controller.EventControllerForAll;
import ru.practicum.event.dto.EventAllDto;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventNewDto;
import ru.practicum.event.dto.EventSmallDto;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestStatusDto;
import ru.practicum.request.model.RequestStatus;

import java.util.List;
import java.util.Map;

public interface EventService {
    List<EventSmallDto> getAllEvents(final Long userId, final int from, final int size);

    EventAllDto createEvent(final Long userId, final EventNewDto eventRequestDto);

    EventAllDto getEventById(final Long userId, final Long eventId);

    EventAllDto updateEvent(final Long userId, final Long eventId, final EventDto eventDto);


    List<RequestDto> getRequestsByEventId(final Long userId, final Long eventId);

    Map<String, List<RequestDto>> approveRequests(final Long userId, final Long eventId,
                                                               final RequestStatusDto requestDto);

    List<EventAllDto> getAllByAdmin(final List<Long> users, final List<String> states, final List<Long> categories,
                                     final String rangeStart, final String rangeEnd, final int from, final int size);

    EventAllDto approveEventByAdmin(final Long eventId, final EventDto eventDto);

    List<EventSmallDto> getAllPublic(final String text, final List<Long> categories, final Boolean paid,
                                     final String rangeStart, final String rangeEnd, final boolean onlyAvailable,
                                     final EventControllerForAll.EventSort sort, final int from, final int size,
                                     final HttpServletRequest request);

    EventAllDto getEventByIdPublic(final Long eventId, final HttpServletRequest request);
}
