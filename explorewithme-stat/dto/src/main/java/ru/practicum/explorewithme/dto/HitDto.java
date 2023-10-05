package ru.practicum.explorewithme.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class HitDto {
    private Long id;

    @NotBlank(message = "app name cannot be empty")
    private String app;

    @NotBlank(message = "URI cannot be empty")
    private String uri;

    @NotBlank(message = "user's IP address cannot be empty")
    private String ip;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime timestamp;
}
