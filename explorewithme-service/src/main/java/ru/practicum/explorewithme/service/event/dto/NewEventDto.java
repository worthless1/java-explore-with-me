package ru.practicum.explorewithme.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.service.location.dto.LocationDto;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

import static ru.practicum.explorewithme.service.util.Const.DATETIME_PATTERN;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull(message = "Category cannot be empty")
    private Long category;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    @Future
    @JsonFormat(pattern = DATETIME_PATTERN)
    private LocalDateTime eventDate;

    @Valid
    @NotNull
    private LocationDto location;

    @NotNull
    private Boolean paid = false;

    @PositiveOrZero
    private int participantLimit = 0;

    @NotNull
    private Boolean requestModeration = true;

}