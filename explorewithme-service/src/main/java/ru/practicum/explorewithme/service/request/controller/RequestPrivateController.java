package ru.practicum.explorewithme.service.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.service.request.dto.RequestDto;
import ru.practicum.explorewithme.service.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@Slf4j
@RequiredArgsConstructor
public class RequestPrivateController {

    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto create(@PathVariable(value = "userId") Long userId,
                             @RequestParam(value = "eventId") Long eventId) {
        log.info("Create request of event id= {} for user with id= {} ", eventId, userId);
        return requestService.createRequest(userId, eventId);
    }

    @GetMapping
    public List<RequestDto> getRequest(@PathVariable(value = "userId") Long userId) {
        log.info("Create requests for with id= {} ", userId);
        return requestService.getRequestByUserId(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto updateRequestStatusToCancel(@PathVariable(value = "userId") Long userId,
                                                  @PathVariable(value = "requestId") Long requestId) {
        log.info("Update request of event id= {} for user with id= {} ", requestId, userId);
        return requestService.updateRequestStatus(userId, requestId);
    }
}