package ru.practicum.explorewithme.service.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.explorewithme.service.event.model.enums.EventRequestStatus;

import java.time.LocalDateTime;

import static ru.practicum.explorewithme.service.util.Const.DATETIME_PATTERN;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RequestDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_PATTERN)
    private LocalDateTime created;

    private Long event;

    private Long id;

    private Long requester;

    private EventRequestStatus status;
}