
package ru.practicum.explorewithme.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.explorewithme.service.location.dto.LocationDto;
import ru.practicum.explorewithme.service.event.model.enums.EventStateAction;

import javax.validation.constraints.Future;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.explorewithme.service.util.Const.DATETIME_PATTERN;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventUpdatedDto {

    @Size(min = 3, max = 120)
    private String title;

    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @Size(max = 7000, min = 20)
    private String description;

    @Future
    @JsonFormat(pattern = DATETIME_PATTERN)
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;

    private EventStateAction stateAction;

}