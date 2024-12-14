package ru.practicum.event.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.error.ConflictException;
import ru.practicum.error.NotFoundException;
import ru.practicum.error.ValidationException;
import ru.practicum.event.controller.EventControllerForAll;
import ru.practicum.event.dto.EventAllDto;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventNewDto;
import ru.practicum.event.dto.EventSmallDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.StateEvent;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.RequestStatusDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.event.model.Action;
import ru.practicum.request.model.Request;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class EventServiceImpl implements EventService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final EntityManager entityManager;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional(readOnly = true)
    public List<EventSmallDto> getAllEvents(final Long userId, final int from, final int size) {
        int startPage = from > 0 ? (from / size) : 0;
        Pageable pageable = PageRequest.of(startPage, size);

        final List<Event> events = eventRepository.findByInitiatorId(userId, pageable);

        if (events.isEmpty()) {
            log.info("Списка событий у пользователя с id = {} не найдено.", userId);
            return new ArrayList<>();
        }
        log.info("Получение списка событий пользователя с id = {}.", userId);
        return EventMapper.toEventSmallDtoList(events);

    }

    @Override
    public EventAllDto createEvent(final Long userId, final EventNewDto eventRequestDto) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с id = {} не существует." + userId));
        Long categoryId = eventRequestDto.getCategory();
        final Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ValidationException("Категории с id = {} не существует." + categoryId));

        Location location = eventRequestDto.getLocation();
        locationRepository.save(location);

        if (eventRequestDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Дата и время не соответствует требованиям.");
        }

        final Event event = eventRepository.save(EventMapper.toEvent(eventRequestDto, user, category));
        log.info("Событие с id = {} и со статусом: {} добавлено", user.getId(), event.getState());
        return EventMapper.toEventAllDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public EventAllDto getEventById(final Long userId, final Long eventId) {
        final Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("События с id = {} не существует." + eventId));

        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ValidationException("Пользователь не является инициатором этого события.");
        }

        log.info("Получение события с id = {}", eventId);
        return EventMapper.toEventAllDto(event);
    }

    @Override
    public EventAllDto updateEvent(final Long userId, final Long eventId, final EventDto eventDto) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с id = {} не существует." + userId));
        final Event oldEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("События с id = {} не существует." + eventId));

        if (!Objects.equals(oldEvent.getInitiator().getId(), user.getId())) {
            throw new ValidationException("Редактирование не доступно.");
        }

        if (oldEvent.getState().equals(StateEvent.PUBLISHED)) {
            throw new ConflictException("Редактирование недоступно.");
        }

        if (Objects.nonNull(eventDto.getEventDate()) &&
                eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Дата и время не соответствует требованиям.");
        }

        if (Objects.nonNull(eventDto.getCategory())) {
            Long catId = eventDto.getCategory();
            final Category category = categoryRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new ValidationException("Категории с id = {} не существует." + catId));
            oldEvent.setCategory(category);
        }

        Optional.ofNullable(eventDto.getTitle()).ifPresent(oldEvent::setTitle);
        Optional.ofNullable(eventDto.getAnnotation()).ifPresent(oldEvent::setAnnotation);
        Optional.ofNullable(eventDto.getDescription()).ifPresent(oldEvent::setDescription);
        Optional.ofNullable(eventDto.getEventDate()).ifPresent(oldEvent::setEventDate);
        Optional.ofNullable(eventDto.getLocation()).ifPresent(oldEvent::setLocation);
        Optional.ofNullable(eventDto.getParticipantLimit()).ifPresent(oldEvent::setParticipantLimit);
        Optional.ofNullable(eventDto.getPaid()).ifPresent(oldEvent::setPaid);
        Optional.ofNullable(eventDto.getRequestModeration()).ifPresent(oldEvent::setRequestModeration);

        if (Objects.nonNull(eventDto.getAction())) {
            if (eventDto.getAction().equals(Action.SEND_TO_REVIEW)) {
                oldEvent.setState(StateEvent.PENDING);
            } else if (eventDto.getAction().equals(Action.CANCEL_REVIEW)) {
                oldEvent.setState(StateEvent.CANCELED);
            }
        }

        final Event event = eventRepository.save(oldEvent);
        log.info("Событие с id = {} обновлено.", eventId);
        return EventMapper.toEventAllDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getRequestsByEventId(final Long userId, final Long eventId) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с id = {} не существует." + userId));
        final Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("События с id = {} не существует." + eventId));

        if (!Objects.equals(event.getInitiator(), user)) {
            throw new ConflictException("Пользователь не является инициатором.");
        }

        final List<Request> requests = requestRepository.findByEventId(eventId);
        if (requests.isEmpty()) {
            log.info("Заявок на участие в мероприятии с id = {} нет.", eventId);
            return new ArrayList<>();
        }

        log.info("Получение списка заявок на участие в мероприятии с id {}.", eventId);
        return RequestMapper.toRequestDtoList(requests);
    }

    @Override
    public Map<String, List<RequestDto>> approveRequests(final Long userId, final Long eventId,
                                                         final RequestStatusDto requestDto) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с id = {} не существует." + userId));
        final Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("События с id = {} не существует." + eventId));

        if (!Objects.equals(event.getInitiator(), user)) {
            throw new ConflictException("Пользователь не является инициатором этого события.");
        }

        final List<Request> requests = requestRepository.findRequestByIdIn(requestDto.getRequestIds());

        if (event.getRequestModeration() && event.getParticipantLimit().equals(event.getConfirmedRequests()) &&
                event.getParticipantLimit() != 0 && requestDto.getStatus().equals(RequestStatus.CONFIRMED)) {
            throw new ConflictException("Лимит заявок на участие в событии исчерпан.");
        }

        final boolean verified = requests.stream()
                .allMatch(request -> request.getEvent().getId().longValue() == eventId);
        if (!verified) {
            throw new ConflictException("Список запросов не относятся к одному событию.");
        }

        final Map<String, List<RequestDto>> requestMap = new HashMap<>();

        if (requestDto.getStatus().equals(RequestStatus.REJECTED)) {
            if (requests.stream()
                    .anyMatch(request -> request.getStatus().equals(RequestStatus.CONFIRMED))) {
                throw new ConflictException("Запрос на установление статуса <ОТМЕНЕНА>. Подтвержденые заявки нельзя отменить.");
            }
            log.info("Запрос на отклонение заявки подтвержден.");

            List<RequestDto> rejectedRequests = requests.stream()
                    .peek(request -> request.setStatus(RequestStatus.REJECTED))
                    .map(requestRepository::save)
                    .map(RequestMapper::toRequestDto)
                    .toList();
            requestMap.put("rejectedRequests", rejectedRequests);
        } else {
            if (requests.stream()
                    .anyMatch(request -> !request.getStatus().equals(RequestStatus.PENDING))) {
                throw new ConflictException("Запрос на установление статуса <ПОДТВЕРЖДЕНА>. Заявки должны быть со статусом <В ОЖИДАНИИ>.");
            }

            long limit = event.getParticipantLimit() - event.getConfirmedRequests();
            final List<Request> confirmedList = requests.stream()
                    .limit(limit)
                    .peek(request -> request.setStatus(RequestStatus.CONFIRMED))
                    .map(requestRepository::save).toList();
            log.info("Заявки на участие сохранены со статусом <ПОДТВЕРЖДЕНА>.");

            final List<RequestDto> confirmedRequests = confirmedList.stream()
                    .map(RequestMapper::toRequestDto)
                    .toList();
            requestMap.put("confirmedRequests", confirmedRequests);

            final List<Request> rejectedList = requests.stream()
                    .skip(limit)
                    .peek(request -> request.setStatus(RequestStatus.REJECTED))
                    .map(requestRepository::save).toList();
            log.info("Часть заявок на участие сохранены со статусом <ОТМЕНЕНА>, в связи с превышением лимита.");
            final List<RequestDto> rejectedRequests = rejectedList.stream()
                    .map(RequestMapper::toRequestDto)
                    .toList();
            requestMap.put("rejectedRequests", rejectedRequests);

            event.setConfirmedRequests(confirmedList.size() + event.getConfirmedRequests());
            eventRepository.save(event);
        }
        return requestMap;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventAllDto> getAllByAdmin(final List<Long> usersId, final List<String> states, final List<Long> categoriesId,
                                           final String rangeStart, final String rangeEnd, final int from, final int size) {
        LocalDateTime start = (rangeStart != null) ? LocalDateTime.parse(rangeStart, FORMATTER) : LocalDateTime.now();
        LocalDateTime end = (rangeEnd != null) ? LocalDateTime.parse(rangeEnd, FORMATTER) : LocalDateTime.now().plusYears(20);
        PageRequest pageRequest = PageRequest.of(from / size, size);

        if (start.isAfter(end)) {
            throw new ValidationException("Временной промежуток задан неверно.");
        }
        List<User> users;
        if (Objects.isNull(usersId) || usersId.isEmpty()) {
            users = userRepository.findAll();
            if (users.isEmpty()) {
                log.info("Еще нет ни одного пользователя, а значит и событий нет.");
                return new ArrayList<>();
            }
        } else {
            users = userRepository.findByIdInOrderByIdAsc(usersId, pageRequest);
            if (users.size() != usersId.size()) {
                throw new ValidationException("Список пользователей передан неверно.");
            }
        }
        final List<StateEvent> stateEvents;
        if (Objects.isNull(states) || states.isEmpty()) {
            stateEvents = List.of(StateEvent.PUBLISHED, StateEvent.CANCELED, StateEvent.PENDING);
        } else {
            try {
                stateEvents = states.stream()
                        .map(StateEvent::valueOf)
                        .toList();
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Недопустимое значение статуса: " + e.getMessage());
            }
        }
        List<Category> categories;
        if (categoriesId == null) {
            categories = categoryRepository.findAll();
            if (categories.isEmpty()) {
                log.info("Еще нет ни одной категории, а значит и событий нет");
                return new ArrayList<>();
            }
        } else {
            categories = categoryRepository.findByIdInOrderById(categoriesId, pageRequest);
            if (categories.size() != categoriesId.size()) {
                throw new ValidationException("Список категорий передан неверно неверно");
            }
        }
        final List<Event> events = eventRepository
                .findByInitiatorInAndStateInAndCategoryIn(
                        users, stateEvents, categories, start, end, pageRequest);
        if (events.isEmpty()) {
            log.info("По данным параметрам не нашлось ни одного события");
            return new ArrayList<>();
        }
        log.info("Получен список событий по заданным параметрам");
        return EventMapper.toEventAllDtoList(events);
    }

    @Override
    public EventAllDto approveEventByAdmin(final Long eventId, final EventDto eventDto) {
        final Event oldEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("События с id = {} не существует." + eventId));

        if ((Objects.nonNull(eventDto.getEventDate()) &&
                eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) ||
                oldEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ConflictException("Событие не может начинаться ранее, чем через 1 час после редактирования.");
        }
        if (oldEvent.getPublishedOn() != null && LocalDateTime.now().plusHours(1).isBefore(oldEvent.getPublishedOn())) {
            throw new ConflictException("Дата и время не соответствует требованиям.");
        }
        if (oldEvent.getState().equals(StateEvent.PUBLISHED) ||
                oldEvent.getState().equals(StateEvent.CANCELED)) {
            throw new ConflictException("Редактирование статуса недоступно.");
        }

        if (Objects.nonNull(eventDto.getCategory())) {
            Long categoryId = eventDto.getCategory();
            final Category category = categoryRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new ValidationException("Категории с id = {} не существует." + categoryId));
            oldEvent.setCategory(category);
        }

        Optional.ofNullable(eventDto.getTitle()).ifPresent(oldEvent::setTitle);
        Optional.ofNullable(eventDto.getAnnotation()).ifPresent(oldEvent::setAnnotation);
        Optional.ofNullable(eventDto.getDescription()).ifPresent(oldEvent::setDescription);
        Optional.ofNullable(eventDto.getEventDate()).ifPresent(oldEvent::setEventDate);
        Optional.ofNullable(eventDto.getLocation()).ifPresent(oldEvent::setLocation);
        Optional.ofNullable(eventDto.getParticipantLimit()).ifPresent(oldEvent::setParticipantLimit);
        Optional.ofNullable(eventDto.getPaid()).ifPresent(oldEvent::setPaid);
        Optional.ofNullable(eventDto.getRequestModeration()).ifPresent(oldEvent::setRequestModeration);


        if (Objects.nonNull(eventDto.getAction()) && oldEvent.getState().equals(StateEvent.PENDING) &&
                eventDto.getAction().equals(Action.PUBLISH_EVENT)) {
            oldEvent.setState(StateEvent.PUBLISHED);
            oldEvent.setPublishedOn(LocalDateTime.now());
        }

        if (Objects.nonNull(eventDto.getAction()) && oldEvent.getState().equals(StateEvent.PENDING) &&
                eventDto.getAction().equals(Action.REJECT_EVENT)) {
            oldEvent.setState(StateEvent.CANCELED);
            oldEvent.setPublishedOn(null);
        }

        final Event event = eventRepository.save(oldEvent);
        log.info("Событие с id = {} обновлено администратором.", eventId);
        return EventMapper.toEventAllDto(event);

    }

    @Override
    @Transactional(readOnly = true)
    public List<EventSmallDto> getAllPublic(final String text, final List<Long> categories, final Boolean paid,
                                            final String rangeStart, final String rangeEnd, final boolean onlyAvailable,
                                            final EventControllerForAll.EventSort sort, final int from, final int size,
                                            final HttpServletRequest request) {
        LocalDateTime start = (rangeStart != null) ? LocalDateTime.parse(rangeStart, FORMATTER) : LocalDateTime.now();
        LocalDateTime end = (rangeEnd != null) ? LocalDateTime.parse(rangeEnd, FORMATTER) : LocalDateTime.now().plusYears(20);
        PageRequest pageRequest = PageRequest.of(from / size, size);

        if (start.isAfter(end)) {
            throw new ValidationException("Временной промежуток задан неверно.");
        }

        StringBuilder queryStr = new StringBuilder("""
                SELECT e
                FROM Event e
                JOIN FETCH e.category c
                WHERE e.eventDate >= :start AND e.eventDate <= :end
                """);

        if (Objects.nonNull(text) && !text.isEmpty()) {
            queryStr.append(" AND (LOWER(e.annotation) LIKE LOWER(:text) OR LOWER(e.description) LIKE LOWER(:text))");
        }
        if (Objects.nonNull(categories) && !categories.isEmpty()) {
            queryStr.append(" AND e.category.id IN :categories");
        }
        if (Objects.nonNull(paid)) {
            queryStr.append(" AND e.paid = :paid");
        }

        queryStr.append(" AND e.participantLimit > e.confirmedRequests");
        TypedQuery<Event> query = entityManager.createQuery(queryStr.toString(), Event.class)
                .setParameter("start", start)
                .setParameter("end", end);

        if (Objects.nonNull(text) && !text.isEmpty()) {
            query.setParameter("text", "%" + text + "%");
        }
        if (Objects.nonNull(categories) && !categories.isEmpty()) {
            query.setParameter("categories", categories);
        }
        if (Objects.nonNull(paid)) {
            query.setParameter("paid", paid);
        }

        query.setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize());
        query.setMaxResults(pageRequest.getPageSize());

        List<Event> events = query.getResultList();

        if (Objects.nonNull(sort)) {
            if (sort.equals(EventControllerForAll.EventSort.EVENT_DATE)) {
                events.sort(Comparator.comparing(Event::getEventDate));
            } else if (sort.equals(EventControllerForAll.EventSort.VIEWS)) {
                events.sort(Comparator.comparing(Event::getViews).reversed());
            }
        }
        if (events.stream().noneMatch(e -> e.getState().equals(StateEvent.PUBLISHED))) {
            throw new ValidationException("Нет опубликованных событий.");
        }

        List<Event> paginatedEvents = events.stream().skip(from).toList();

        return paginatedEvents.stream()
                .map(EventMapper::toEventSmallDto)
                .peek(dto -> {
                }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EventAllDto getEventByIdPublic(final Long eventId, final HttpServletRequest request) {


        log.debug("==> Find the event: eventId {}", eventId);
        Event model = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found by id: " + eventId));
        if (!model.getState().equals(StateEvent.PUBLISHED)) {
            throw new NotFoundException("Event is not published");
        }

        EventAllDto result = EventMapper.toEventAllDto(model);
        log.debug("<== Found the event: result {}", result);
        return result;

    }

    private void checkValidFields(final EventDto eventDto, final Event oldEvent) {
        Optional.ofNullable(eventDto.getTitle()).ifPresent(oldEvent::setTitle);
        Optional.ofNullable(eventDto.getAnnotation()).ifPresent(oldEvent::setAnnotation);
        Optional.ofNullable(eventDto.getDescription()).ifPresent(oldEvent::setDescription);
        Optional.ofNullable(eventDto.getEventDate()).ifPresent(oldEvent::setEventDate);
        Optional.ofNullable(eventDto.getLocation()).ifPresent(oldEvent::setLocation);
        Optional.ofNullable(eventDto.getParticipantLimit()).ifPresent(oldEvent::setParticipantLimit);
        Optional.ofNullable(eventDto.getPaid()).ifPresent(oldEvent::setPaid);
        Optional.ofNullable(eventDto.getRequestModeration()).ifPresent(oldEvent::setRequestModeration);

        if (Objects.nonNull(eventDto.getCategory())) {
            Long catId = eventDto.getCategory();
            final Category category = categoryRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new ValidationException("Категории с id = {} не существует." + catId));
            oldEvent.setCategory(category);
        }
    }

}
