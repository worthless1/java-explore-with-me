package ru.practicum.explorewithme.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.explorewithme.service.category.dto.CategoryDto;
import ru.practicum.explorewithme.service.comment.dto.CommentDto;
import ru.practicum.explorewithme.service.location.dto.LocationDto;
import ru.practicum.explorewithme.service.user.dto.UserShortDto;
import ru.practicum.explorewithme.service.event.model.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explorewithme.service.util.Const.DATETIME_PATTERN;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {

    private Long id;

    private String title;

    private String description;

    private String annotation;

    private CategoryDto category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_PATTERN)
    private LocalDateTime eventDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_PATTERN)
    private LocalDateTime publishedOn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_PATTERN)
    private LocalDateTime createdOn;

    private LocationDto location;

    private boolean paid;

    private int participantLimit;

    private long confirmedRequests;

    private long views;

    private EventState state;

    private boolean requestModeration;

    private UserShortDto initiator;

    private List<CommentDto> comments;

}