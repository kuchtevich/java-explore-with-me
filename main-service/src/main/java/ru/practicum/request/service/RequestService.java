package ru.practicum.request.service;

import ru.practicum.request.dto.RequestDto;

import java.util.List;
public interface RequestService {
    List<RequestDto> getAllRequests(final Long userId);

    RequestDto createRequest(final Long userId, final Long eventId);

    RequestDto cancelRequest(final Long userId, final Long requestId);
}
