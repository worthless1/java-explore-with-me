package ru.practicum.explorewithme.service.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.service.event.model.Event;
import ru.practicum.explorewithme.service.request.dto.RequestDto;
import ru.practicum.explorewithme.service.request.model.Request;
import ru.practicum.explorewithme.service.user.model.User;

import static ru.practicum.explorewithme.service.event.model.enums.EventRequestStatus.CONFIRMED;


@UtilityClass
public class RequestMapper {

    public static RequestDto mapToParticipationRequestDto(Request request) {
        return RequestDto.builder()
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }

    public static Request mapToNewParticipationRequest(Event event, User user) {
        Request request = new Request();
        request.setEvent(event);
        request.setRequester(user);

        if (event.getParticipantLimit() == 0 || Boolean.TRUE.equals(!event.getRequestModeration())) {
            request.setStatus(CONFIRMED);
        }
        return request;
    }

}
