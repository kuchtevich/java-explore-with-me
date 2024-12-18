package ru.practicum.request.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.service.RequestService;

import java.util.List;

@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Validated
@RestController
public class RequestController {
    private final RequestService requestService;

    @GetMapping
    public List<RequestDto> getAllRequests(@PathVariable @Positive final Long userId) {
        return requestService.getAllRequests(userId);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public RequestDto createRequest(@PathVariable final Long userId, @RequestParam final Long eventId) {
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable @Positive final Long userId,
                                    @PathVariable @Positive final Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }
}
