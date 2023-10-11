package ru.practicum.explorewithme.service.request.service;

import ru.practicum.explorewithme.service.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.service.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.service.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto createRequest(Long userId, Long eventId);

    List<RequestDto> getRequestByUserId(Long userId);

    RequestDto updateRequestStatus(Long userId, Long requestId);

    List<RequestDto> getRequestPrivate(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateEventRequestStatusPrivate(Long userId, Long eventId,
                                                                   EventRequestStatusUpdateRequest dtoRequest);

}