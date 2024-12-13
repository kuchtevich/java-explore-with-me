package ru.practicum.event.mapper;

import ru.practicum.category.dto.CategoryDtoOut;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.EventAllDto;
import ru.practicum.event.dto.EventNewDto;
import ru.practicum.event.dto.EventSmallDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.StateEvent;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class EventMapper {
    public static Event toEvent(final EventNewDto eventRequestDto,
                                final User initiator,
                                final Category category) {

        final Event event = new Event();

        event.setInitiator(initiator);
        event.setAnnotation(eventRequestDto.getAnnotation());
        event.setCategory(category);
        event.setDescription(eventRequestDto.getDescription());
        event.setEventDate(eventRequestDto.getEventDate());
        event.setLocation(eventRequestDto.getLocation());
        event.setPaid(eventRequestDto.getPaid());
        event.setParticipantLimit(eventRequestDto.getParticipantLimit());
        event.setRequestModeration(eventRequestDto.getRequestModeration());
        event.setTitle(eventRequestDto.getTitle());
        event.setCreatedOn(LocalDateTime.now());
        event.setState(StateEvent.PENDING);
        event.setConfirmedRequests(0);

        return event;
    }

    public static EventSmallDto toEventSmallDto(final Event event) {

        final EventSmallDto eventSmallDto = new EventSmallDto();

        eventSmallDto.setAnnotation(event.getAnnotation());
        eventSmallDto.setCategory(
                new CategoryDtoOut(event.getCategory().getId(), event.getCategory().getName()));
        eventSmallDto.setConfirmedRequests(event.getConfirmedRequests());
        eventSmallDto.setEventDate(event.getEventDate());
        eventSmallDto.setId(event.getId());
        eventSmallDto.setInitiator(
                new UserDto(event.getInitiator().getId(), event.getInitiator().getName()));
        eventSmallDto.setPaid(event.getPaid());
        eventSmallDto.setTitle(event.getTitle());
        eventSmallDto.setViews(0);

        return eventSmallDto;
    }

    public static EventAllDto toEventAllDto(final Event event) {

        final EventAllDto eventAllDto = new EventAllDto();

        eventAllDto.setAnnotation(event.getAnnotation());
        eventAllDto.setCategory(
                new CategoryDtoOut(event.getCategory().getId(), event.getCategory().getName()));
        eventAllDto.setConfirmedRequests(event.getConfirmedRequests());
        eventAllDto.setCreatedOn(event.getCreatedOn());
        eventAllDto.setDescription(event.getDescription());
        eventAllDto.setEventDate(event.getEventDate());
        eventAllDto.setId(event.getId());
        eventAllDto.setInitiator(
                new UserDto(event.getInitiator().getId(), event.getInitiator().getName()));
        eventAllDto.setLocation(event.getLocation());
        eventAllDto.setPaid(event.getPaid());
        eventAllDto.setParticipantLimit(event.getParticipantLimit());
        eventAllDto.setPublishedOn(event.getPublishedOn());
        eventAllDto.setRequestModeration(event.getRequestModeration());
        eventAllDto.setState(event.getState());
        eventAllDto.setTitle(event.getTitle());
        eventAllDto.setViews(0);

        return eventAllDto;
    }

    public static List<EventSmallDto> toEventSmallDtoList(Iterable<Event> events) {
        List<EventSmallDto> result = new ArrayList<>();

        for (Event event : events) {
            result.add(toEventSmallDto(event));
        }
        return result;
    }

    public static List<EventAllDto> toEventAllDtoList(Iterable<Event> events) {
        List<EventAllDto> result = new ArrayList<>();

        for (Event event : events) {
            result.add(toEventAllDto(event));
        }
        return result;
    }
}
