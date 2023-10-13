package ru.practicum.explorewithme.service.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.explorewithme.service.event.dto.EventUpdatedDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class EventRequestStatusUpdateResult extends EventUpdatedDto {

    private List<RequestDto> confirmedRequests;

    private List<RequestDto> rejectedRequests;
}