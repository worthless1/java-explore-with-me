package ru.practicum.explorewithme.service.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationUpdatedDto {

    @Size(min = 1, max = 50)
    private String title;

    private Boolean pinned;

    private Set<Long> eventIds;

}