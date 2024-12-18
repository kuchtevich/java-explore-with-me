package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.request.dto.ConfirmedRequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Request findByRequesterIdAndEventId(final Long requesterId,
                                        final Long eventId);

    List<Request> findByRequesterId(final Long requesterId);

    List<Request> findByEventId(final Long eventId);

    List<Request> findRequestByIdIn(final List<Long> requestsId);

    @Query(value = "SELECT new ru.practicum.request.dto.ConfirmedRequest(r.event.id, COUNT(r.id)) " +
            "FROM ParticipationRequest r " +
            "WHERE r.event.id IN (:eventIds) AND r.status = :status " +
            "GROUP BY r.id, r.event.id " +
            "ORDER BY r.id, r.event.id")
    List<ConfirmedRequestDto> getConfirmedRequestsByStatus(@Param("eventIds") List<Long> eventIds, @Param("status") RequestStatus status);
}
