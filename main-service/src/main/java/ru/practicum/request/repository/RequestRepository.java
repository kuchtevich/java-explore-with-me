package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Request findByRequesterIdAndEventId(final Long requesterId,
                                        final Long eventId);

    List<Request> findByRequesterId(final Long requesterId);

    List<Request> findByEventId(final Long eventId);

    List<Request> findRequestByIdIn(final List<Long> requestsId);

    Integer countByEventIdAndStatus(Long eventId, RequestStatus requestStatus);
}
