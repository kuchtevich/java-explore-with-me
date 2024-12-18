package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.error.ConflictException;
import ru.practicum.error.NotFoundException;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.event.model.StateEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Transactional
@RequiredArgsConstructor
@Slf4j
@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getAllRequests(final Long userId) {
        final List<Request> requests = requestRepository.findByRequesterId(userId);

        if (requests.isEmpty()) {
            log.info("Заявок у пользователя с id {} нет.", userId);
            return new ArrayList<>();
        }
        log.info("Получение списка всех заявок пользователя с id {}.", userId);
        return requests.stream().map(RequestMapper::toRequestDto).toList();
    }

    @Override
    public RequestDto createRequest(final Long userId, final Long eventId) {
        final Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("События с id = {} не существует." + eventId));
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с id = {} не существует." + userId));
        final Request requestValid = requestRepository.findByRequesterIdAndEventId(userId, eventId);

        if (Objects.nonNull(requestValid) || event.getInitiator().getId().equals(user.getId())) {
            throw new ConflictException("Пользователь является инициатором события или уже подал заявку на участие в событии.");
        }

        if (event.getParticipantLimit().equals(event.getConfirmedRequests()) && event.getParticipantLimit() != 0) {
            throw new ConflictException("На данное мероприятие больше нет мест");
        }

        if (!event.getState().equals(StateEvent.PUBLISHED)) {
            throw new ConflictException("Событие еще не было опубликовано.");
        }

        final Request request = new Request();
        request.setEvent(event);
        request.setRequester(user);
        request.setCreated(LocalDateTime.now());

        if (event.getParticipantLimit() == 0 ||
                (!event.getRequestModeration() && event.getParticipantLimit() > event.getConfirmedRequests())) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
            final Request newRequest = requestRepository.save(request);
            log.info("Сохранение заявки на участие со статусом <ПОДТВЕРЖДЕНА>.");
            return RequestMapper.toRequestDto(newRequest);
        }

        if (!event.getRequestModeration() && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            request.setStatus(RequestStatus.REJECTED);
            final Request newRequest = requestRepository.save(request);
            log.info("Сохранение заявки на участие со статусом <ОТМЕНЕНА>, в связи с превышением лимита.");
            return RequestMapper.toRequestDto(newRequest);
        }

        request.setStatus(RequestStatus.PENDING);
        final Request newRequest = requestRepository.save(request);
        log.info("Сохранение заявки на участие со статусом <В ОЖИДАНИИ>.");
        return RequestMapper.toRequestDto(newRequest);
    }

    @Override
    public RequestDto cancelRequest(final Long userId, final Long requestId) {

        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с id = {} не существует." + userId));
        final Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Заявки с id = {} не существует." + requestId));

        if (!request.getRequester().equals(user)) {
            throw new ConflictException("Отменить заявку может только пользователь иницировавший её.");
        }

        request.setStatus(RequestStatus.CANCELED);
        final Request requestCancel = requestRepository.save(request);
        log.info("Заявка на участие с id = {} отменена.", requestId);

        final Event event = request.getEvent();
        if (event.getRequestModeration()) {
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
            log.info("Появилоась свободное место у события с id = {}.", event.getId());
        }

        return RequestMapper.toRequestDto(requestCancel);
    }
}
