package ru.practicum.explorewithme.service.compilation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.explorewithme.service.event.dto.EventShortDto;

import java.util.Set;

@Getter
@Setter
@Builder
public class CompilationDto {

    private long id;

    private String title;

    private boolean pinned;

    private Set<EventShortDto> events;
}