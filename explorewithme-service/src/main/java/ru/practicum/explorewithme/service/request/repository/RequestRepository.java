package ru.practicum.explorewithme.service.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.service.event.model.Event;
import ru.practicum.explorewithme.service.request.model.Request;
import ru.practicum.explorewithme.service.event.model.enums.EventRequestStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Boolean existsByRequesterIdAndEventId(Long userId, Long eventId);

    List<Request> findAllByEventIdAndIdIn(Long eventId, Set<Long> requestIds);

    List<Request> findAllByRequesterId(Long requesterId);

    Optional<Request> findByIdAndRequesterId(Long requestId, Long requesterId);

    List<Request> findAllByEventId(Long eventId);

    Long countRequestByEventIdAndStatus(Long eventId, EventRequestStatus state);

    List<Request> findAllByEventInAndStatus(List<Event> event, EventRequestStatus status);

}