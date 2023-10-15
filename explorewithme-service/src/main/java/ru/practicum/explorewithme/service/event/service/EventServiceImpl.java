package ru.practicum.explorewithme.service.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatClient;
import ru.practicum.explorewithme.dto.StatDto;
import ru.practicum.explorewithme.service.category.model.Category;
import ru.practicum.explorewithme.service.category.repository.CategoryRepository;
import ru.practicum.explorewithme.service.event.dto.*;
import ru.practicum.explorewithme.service.event.mapper.EventMapper;
import ru.practicum.explorewithme.service.event.model.Event;
import ru.practicum.explorewithme.service.event.model.enums.EventRequestStatus;
import ru.practicum.explorewithme.service.event.model.enums.EventState;
import ru.practicum.explorewithme.service.event.model.enums.EventStateAction;
import ru.practicum.explorewithme.service.event.model.enums.EventsSort;
import ru.practicum.explorewithme.service.event.repository.EventRepository;
import ru.practicum.explorewithme.service.exception.NotFoundException;
import ru.practicum.explorewithme.service.exception.ValidateDateException;
import ru.practicum.explorewithme.service.exception.ValidateException;
import ru.practicum.explorewithme.service.location.mapper.LocationMapper;
import ru.practicum.explorewithme.service.location.model.Location;
import ru.practicum.explorewithme.service.location.repository.LocationRepository;
import ru.practicum.explorewithme.service.request.model.Request;
import ru.practicum.explorewithme.service.request.repository.RequestRepository;
import ru.practicum.explorewithme.service.user.model.User;
import ru.practicum.explorewithme.service.user.repository.UserRepository;
import ru.practicum.explorewithme.service.util.Pagination;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.explorewithme.service.event.model.enums.EventState.PENDING;
import static ru.practicum.explorewithme.service.event.model.enums.EventState.PUBLISHED;
import static ru.practicum.explorewithme.service.util.Const.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {
    @Value("${app}")
    private String app;

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final StatClient statClient;
    private final LocationRepository locationRepository;

    @Override
    public List<EventFullDto> getAllEventsAdmin(List<Long> users,
                                                List<EventState> states,
                                                List<Long> categories,
                                                LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd,
                                                Integer from,
                                                Integer size) {
        validateDateParameters(rangeStart, rangeEnd);
        PageRequest pageable = new Pagination(from, size, Sort.unsorted());
        List<Event> events = eventRepository.findAllForAdmin(users, states, categories, getRangeStart(rangeStart), pageable);
        confirmedRequestForListEvent(events);

        log.info("Get all events in admin {}", events);
        return events.stream()
                .map(EventMapper::mapToEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventByIdAdmin(Long eventId, EventUpdatedDto eventUpdatedDto) {
        Event event = getEventById(eventId);
        updateEventAdmin(event, eventUpdatedDto);
        event = eventRepository.save(event);
        locationRepository.save(event.getLocation());
        log.info("Update event with id= {} in admin ", eventId);
        return EventMapper.mapToEventFullDto(event);
    }

    @Override
    public EventFullDto createEventPrivate(Long userId, NewEventDto newEventDto) {
        validateEventDate(newEventDto.getEventDate());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " not found"));
        Category category = getCategoryForEvent(newEventDto.getCategory());
        Location savedLocation = locationRepository.save(LocationMapper.mapToLocation(newEventDto.getLocation()));
        Event event = eventRepository.save(EventMapper.mapToNewEvent(newEventDto, savedLocation, user, category));
        confirmedRequestsForOneEvent(event);
        log.info("User id= {} create event in admin", userId);
        return EventMapper.mapToEventFullDto(event);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> getAllEventsByUserIdPrivate(Long userId, int from, int size) {
        log.info("Get all events of user with id= {} in private", userId);
        List<Event> events = eventRepository.findAllWithInitiatorByInitiatorId(userId, new Pagination(from, size, Sort.unsorted()));
        confirmedRequestForListEvent(events);

        return events.stream()
                .map(EventMapper::mapToEventShortDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto getEventByIdPrivate(Long userId, Long eventId) {
        Event event = getEventByIdAndInitiatorId(eventId, userId);
        confirmedRequestsForOneEvent(event);
        log.info("Get event with id={} of user with id= {} in private", eventId, userId);
        return EventMapper.mapToEventFullDto(event);
    }

    @Override
    public EventFullDto updateEventByIdPrivate(Long userId, Long eventId, EventUpdatedDto eventUpdatedDto) {
        Event event = getEventByIdAndInitiatorId(eventId, userId);
        if (PUBLISHED.equals(event.getState()) || event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidateException("Events with CANCELED or PENDING can be updated");
        }
        updateEvent(event, eventUpdatedDto);
        Event eventSaved = eventRepository.save(event);
        locationRepository.save(eventSaved.getLocation());
        log.info("Update event with id={} of user with id= {} in private", eventId, userId);
        return EventMapper.mapToEventFullDto(eventSaved);
    }

    public List<EventShortDto> getEventsPublic(String text,
                                               List<Long> categories,
                                               Boolean paid,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               Boolean onlyAvailable,
                                               EventsSort sort,
                                               Integer from,
                                               Integer size,
                                               HttpServletRequest request) {

        validateDateParameters(rangeStart, rangeEnd);
        Pagination pageable;
        final EventState state = PUBLISHED;
        List<Event> events;

        if (sort.equals(EventsSort.EVENT_DATE)) {
            pageable = new Pagination(from, size, Sort.by("eventDate"));
        } else {
            pageable = new Pagination(from, size, Sort.unsorted());
        }

        if (Boolean.TRUE.equals(onlyAvailable)) {
            events = eventRepository.findAllPublishStateNotAvailable(state, getRangeStart(rangeStart), categories, paid, text, pageable);
        } else {
            events = eventRepository.findAllPublishStateAvailable(state, getRangeStart(rangeStart), categories, paid, text, pageable);
        }

        if (rangeEnd != null) {
            events = getEventsBeforeRangeEnd(events, rangeEnd);
        }

        confirmedRequestForListEvent(events);
        List<EventShortDto> result = events.stream()
                .map(EventMapper::mapToEventShortDto)
                .collect(Collectors.toList());

        saveViewInEvent(result, rangeStart);
        statClient.addHit(app, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());

        if (sort.equals(EventsSort.VIEWS)) {
            return result.stream()
                    .sorted(Comparator.comparingLong(EventShortDto::getViews))
                    .collect(Collectors.toList());
        }

        log.info("Get all event with text {}, category {}", text, categories);
        return result;
    }

    @Override
    public EventFullDto getEventByIdPublic(Long id, HttpServletRequest request) {
        Event event = getEventById(id);
        confirmedRequestsForOneEvent(event);
        if (!PUBLISHED.equals(event.getState())) {
            throw new NotFoundException("Event with id=" + id + " not published");
        }
        EventFullDto fullDto = EventMapper.mapToEventFullDto(event);

        List<String> uris = List.of("/events/" + event.getId());
        List<StatDto> views = statClient.getStats(START_DATE, END_DATE, uris, null).getBody();

        if (views != null) {
            fullDto.setViews(views.size());
        }

        statClient.addHit(app, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());

        log.info("Get event with  id = {}}", id);
        return fullDto;
    }

    private Category getCategoryForEvent(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id=" + id + " not found"));
    }

    private Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event with id=" + id + " not found"));
    }

    private Event getEventByIdAndInitiatorId(Long eventId, Long userId) {
        return eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " not found"));
    }

    private void updateEvent(Event event, EventUpdatedDto eventDto) {
        updateEventCommonFields(event, eventDto);

        if (eventDto.getStateAction() != null) {
            if (EventStateAction.CANCEL_REVIEW.equals(eventDto.getStateAction())) {
                event.setState(EventState.CANCELED);
            }
            if (EventStateAction.SEND_TO_REVIEW.equals(eventDto.getStateAction())) {
                event.setState(PENDING);
            }
        }
    }

    private void updateEventAdmin(Event event, EventUpdatedDto eventDto) {
        updateEventCommonFields(event, eventDto);

        if (eventDto.getStateAction() != null) {
            if (PENDING.equals(event.getState())) {
                if (EventStateAction.REJECT_EVENT.equals(eventDto.getStateAction())) {
                    event.setState(EventState.CANCELED);
                }
                if (EventStateAction.PUBLISH_EVENT.equals(eventDto.getStateAction())) {
                    event.setState(PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                }
            } else {
                throw new ValidateException("Unable to publish or cancel an event because it is in the wrong state: " + event.getState());
            }
        }

        if (eventDto.getEventDate() != null && PUBLISHED.equals(event.getState())) {
            if (eventDto.getEventDate().isAfter(event.getPublishedOn().plusHours(1))) {
                event.setEventDate(eventDto.getEventDate());
            } else {
                throw new ValidateDateException("Event date must be at least 1 hour later than the published date");
            }
        }
    }

    private void updateEventCommonFields(Event event, EventUpdatedDto eventDto) {
        if (eventDto.getAnnotation() != null && !eventDto.getAnnotation().isBlank()) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getDescription() != null && !eventDto.getDescription().isBlank()) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getCategory() != null) {
            Category category = getCategoryForEvent(eventDto.getCategory());
            event.setCategory(category);
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }
        if (eventDto.getTitle() != null && !eventDto.getTitle().isBlank()) {
            event.setTitle(eventDto.getTitle());
        }
        if (eventDto.getLocation() != null) {
            event.setLocation(LocationMapper.mapToLocation(eventDto.getLocation()));
        }
        if (eventDto.getEventDate() != null) {
            validateEventDate(eventDto.getEventDate());
            event.setEventDate(eventDto.getEventDate());
        }
    }

    private void validateDateParameters(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new ValidateDateException("The range start date cannot be is after range end date");
            }
        }
    }

    private Long getId(String url) {
        String[] uri = url.split("/");
        return Long.valueOf(uri[uri.length - 1]);
    }

    private void saveViewInEvent(List<EventShortDto> result, LocalDateTime rangeStart) {
        List<String> uris = result.stream()
                .map(eventShortDto -> "/events/" + eventShortDto.getId())
                .collect(Collectors.toList());

        if (rangeStart != null) {
            List<StatDto> views = statClient.getStats(
                    rangeStart.format(START_DATE_FORMATTER), LocalDateTime.now().format(START_DATE_FORMATTER),
                    uris, true).getBody();

            if (views != null) {
                Map<Long, Long> mapIdHits = views.stream()
                        .collect(Collectors.toMap(viewStats -> getId(viewStats.getUri()), StatDto::getHits));

                result.forEach(eventShortDto -> {
                    Long eventId = eventShortDto.getId();
                    Long viewsCount = mapIdHits.getOrDefault(eventId, 0L);
                    eventShortDto.setViews(viewsCount);
                });
            }
        }
    }

    private void validateEventDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidateDateException("Event date should be after now");
        }
    }

    private LocalDateTime getRangeStart(LocalDateTime rangeStart) {
        if (rangeStart == null) {
            return LocalDateTime.now();
        }
        return rangeStart;
    }

    private List<Event> getEventsBeforeRangeEnd(List<Event> events, LocalDateTime rangeEnd) {
        return events.stream().filter(event -> event.getEventDate().isBefore(rangeEnd)).collect(Collectors.toList());
    }

    private void confirmedRequestsForOneEvent(Event event) {
        event.setConfirmedRequests(requestRepository.countRequestByEventIdAndStatus(event.getId(), EventRequestStatus.CONFIRMED));
    }

    private void confirmedRequestForListEvent(List<Event> events) {
        Map<Event, Long> requestsPerEvent = requestRepository.findAllByEventInAndStatus(events, EventRequestStatus.CONFIRMED)
                .stream()
                .collect(Collectors.groupingBy(Request::getEvent, Collectors.counting()));
        if (!requestsPerEvent.isEmpty()) {
            for (Event event : events) {
                event.setConfirmedRequests(requestsPerEvent.getOrDefault(event, 0L));
            }
        }
    }

}
