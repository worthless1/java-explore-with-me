package ru.practicum.explorewithme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatDto {
    private String app;
    private String uri;
    private Long hits;
}
