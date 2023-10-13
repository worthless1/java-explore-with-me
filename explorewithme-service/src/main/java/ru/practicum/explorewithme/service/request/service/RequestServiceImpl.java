package ru.practicum.explorewithme.service.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.service.event.model.Event;
import ru.practicum.explorewithme.service.event.repository.EventRepository;
import ru.practicum.explorewithme.service.exception.NotFoundException;
import ru.practicum.explorewithme.service.exception.ValidateException;
import ru.practicum.explorewithme.service.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.service.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.service.request.dto.RequestDto;
import ru.practicum.explorewithme.service.request.mapper.RequestMapper;
import ru.practicum.explorewithme.service.request.model.Request;
import ru.practicum.explorewithme.service.request.repository.RequestRepository;
import ru.practicum.explorewithme.service.user.model.User;
import ru.practicum.explorewithme.service.user.repository.UserRepository;
import ru.practicum.explorewithme.service.event.model.enums.EventRequestStatus;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.practicum.explorewithme.service.request.mapper.RequestMapper.mapToNewParticipationRequest;
import static ru.practicum.explorewithme.service.request.mapper.RequestMapper.mapToParticipationRequestDto;
import static ru.practicum.explorewithme.service.event.model.enums.EventRequestStatus.*;
import static ru.practicum.explorewithme.service.event.model.enums.EventState.PUBLISHED;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private EventRequestStatusUpdateResult createConfirmedStatus(List<Request> requests, Event event) {
        validateParticipantLimit(event);
        long potentialParticipant = event.getParticipantLimit() - event.getConfirmedRequests();

        List<Request> confirmedRequests;
        List<Request> rejectedRequests;

        if (requests.size() <= potentialParticipant) {
            confirmedRequests = handleRequests(requests.stream(), CONFIRMED);
            rejectedRequests = Collections.emptyList();
        } else {
            confirmedRequests = handleRequests(requests.stream().limit(potentialParticipant), CONFIRMED);
            rejectedRequests = handleRequests(requests.stream().skip(potentialParticipant), REJECTED);
        }

        event.setConfirmedRequests(event.getConfirmedRequests() + confirmedRequests.size());
        requestRepository.saveAll(confirmedRequests);
        requestRepository.saveAll(rejectedRequests);

        List<RequestDto> confirmedRequestsDto = mapToParticipationRequestDtoList(confirmedRequests);
        List<RequestDto> rejectedRequestsDto = mapToParticipationRequestDtoList(rejectedRequests);

        return new EventRequestStatusUpdateResult(confirmedRequestsDto, rejectedRequestsDto);
    }

    private List<Request> handleRequests(Stream<Request> requestStream, EventRequestStatus status) {
        return requestStream.peek(request -> request.setStatus(status)).collect(Collectors.toList());
    }

    private List<RequestDto> mapToParticipationRequestDtoList(List<Request> requests) {
        return requests.stream().map(RequestMapper::mapToParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    public RequestDto createRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id=%d hasn't found", userId)));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id=%d hasn't found", eventId)));
        event.setConfirmedRequests(requestRepository.countRequestByEventIdAndStatus(event.getId(), EventRequestStatus.CONFIRMED));
        validateParticipantLimit(event);

        if (Boolean.TRUE.equals(requestRepository.existsByRequesterIdAndEventId(userId, eventId))) {
            throw new ValidateException("You cannot create the same query twice");
        }

        if (userId.equals(event.getInitiator().getId())) {
            throw new ValidateException("Event initiator cannot participate in their own event");
        }

        if (!event.getState().equals(PUBLISHED)) {
            throw new ValidateException("Cannot participate if the event is not published");
        }

        Request request = requestRepository.save(mapToNewParticipationRequest(event, user));

        log.info("Create participation request {}", request);
        return mapToParticipationRequestDto(request);
    }

    private EventRequestStatusUpdateResult createRejectedStatus(List<Request> requests, Event event) {
        requests.forEach(request -> request.setStatus(REJECTED));
        requestRepository.saveAll(requests);
        List<RequestDto> rejectedRequests = mapToParticipationRequestDtoList(requests);
        return new EventRequestStatusUpdateResult(Collections.emptyList(), rejectedRequests);
    }

    @Transactional(readOnly = true)
    @Override
    public List<RequestDto> getRequestPrivate(Long userId, Long eventId) {
        if (eventRepository.findByIdAndInitiatorId(eventId, userId).isPresent()) {
            return requestRepository.findAllByEventId(eventId).stream()
                    .map(RequestMapper::mapToParticipationRequestDto)
                    .collect(Collectors.toList());
        }
        log.info("Get participation request for event with id = {}", eventId);
        return Collections.emptyList();
    }

    @Override
    public EventRequestStatusUpdateResult updateEventRequestStatusPrivate(Long userId, Long eventId, EventRequestStatusUpdateRequest statusUpdateRequest) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d hasn't found", eventId)));
        event.setConfirmedRequests(requestRepository.countRequestByEventIdAndStatus(event.getId(), EventRequestStatus.CONFIRMED));

        if (Boolean.TRUE.equals(!event.getRequestModeration()) || event.getParticipantLimit() == 0) {
            throw new ValidateException("Unable to update status if application limit = 0");
        }
        List<Request> requests = requestRepository.findAllByEventIdAndIdIn(eventId,
                statusUpdateRequest.getRequestIds());

        validateRequestStatus(requests);

        log.info("Update event request status id= {}", eventId);
        switch (statusUpdateRequest.getStatus()) {
            case CONFIRMED:
                return createConfirmedStatus(requests, event);
            case REJECTED:
                return createRejectedStatus(requests, event);
            default:
                throw new ValidateException("State is not valid: " + statusUpdateRequest.getStatus());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<RequestDto> getRequestByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id=%d hasn't found", userId));
        }
        log.info("Get participation request for user with id= {}", userId);
        return requestRepository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::mapToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto updateRequestStatus(Long userId, Long requestId) {
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Request with id=" + requestId + " hasn't found"));
        request.setStatus(CANCELED);
        log.info("Update status participation request id= {}", requestId);
        return mapToParticipationRequestDto(requestRepository.save(request));
    }

    private void validateParticipantLimit(Event event) {
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new ValidateException("Number of participants in the event has reached the limit of applications for participation");
        }
    }

    private void validateRequestStatus(List<Request> requests) {
        boolean isStatusPending = requests.stream()
                .anyMatch(request -> !request.getStatus().equals(PENDING));
        if (isStatusPending) {
            throw new ValidateException("Status of the request cannot be changed");
        }
    }
}