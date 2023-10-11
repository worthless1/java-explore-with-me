package ru.practicum.explorewithme.service.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.explorewithme.service.event.dto.EventUpdatedDto;

import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class EventRequestStatusUpdateRequest extends EventUpdatedDto {

    private Set<Long> requestIds;

    private EventRequestStatus status;

    public enum EventRequestStatus {
        CONFIRMED,
        REJECTED
    }
}