package ru.practicum.explorewithme.service.event.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.service.event.dto.EventFullDto;
import ru.practicum.explorewithme.service.event.dto.EventShortDto;
import ru.practicum.explorewithme.service.event.dto.EventUpdatedDto;
import ru.practicum.explorewithme.service.event.dto.NewEventDto;
import ru.practicum.explorewithme.service.event.service.EventService;
import ru.practicum.explorewithme.service.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.service.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.service.request.dto.RequestDto;
import ru.practicum.explorewithme.service.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

import static ru.practicum.explorewithme.service.util.Const.PAGE_FROM;
import static ru.practicum.explorewithme.service.util.Const.PAGE_SIZE;

@RestController
@RequestMapping("/users/{userId}/events")
@Slf4j
@Validated
@RequiredArgsConstructor
public class EventPrivateController {

    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@PathVariable(value = "userId") Long userId,
                               @Valid @RequestBody NewEventDto eventDto) {
        log.info("Create event {} of user with id= {}", eventDto, userId);
        return eventService.createEventPrivate(userId, eventDto);
    }


    @GetMapping
    public Collection<EventShortDto> getEventsByUserId(@PathVariable(value = "userId") Long userId,
                                                       @RequestParam(value = "from", defaultValue = PAGE_FROM)
                                                       @PositiveOrZero Integer from,
                                                       @RequestParam(value = "size", defaultValue = PAGE_SIZE)
                                                       @Positive Integer size) {
        log.info("Get events of user with id= {}", userId);
        return eventService.getAllEventsByUserIdPrivate(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable(value = "userId") Long userId,
                                     @PathVariable(value = "eventId") Long eventId) {
        log.info("Get event with id= {} of user with id= {}", eventId, userId);
        return eventService.getEventByIdPrivate(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable(value = "userId") Long userId,
                                    @PathVariable(value = "eventId") Long eventId,
                                    @Valid @RequestBody EventUpdatedDto eventDto) {
        log.info("Updating event {} with id= {} of user with id= {}", eventDto, eventId, userId);
        return eventService.updateEventByIdPrivate(userId, eventId, eventDto);
    }

    @GetMapping("/{eventId}/requests")
    public Collection<RequestDto> getParticipationRequest(@PathVariable(value = "userId") Long userId,
                                                          @PathVariable(value = "eventId") Long eventId) {
        log.info("Get request for event with id= {} for participation for user with id{}", eventId, userId);
        return requestService.getRequestPrivate(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventRequestStatus(@PathVariable(value = "userId") Long userId,
                                                                   @PathVariable(value = "eventId") Long eventId,
                                                                   @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        log.info("Update request for event with id= {} for participation for user with id{}", eventId, userId);
        return requestService.updateEventRequestStatusPrivate(userId, eventId, updateRequest);
    }
}