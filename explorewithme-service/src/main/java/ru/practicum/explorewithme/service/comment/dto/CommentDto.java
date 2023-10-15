package ru.practicum.explorewithme.service.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.Null;
import java.time.LocalDateTime;

import static ru.practicum.explorewithme.service.util.Const.DATETIME_PATTERN;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    @Null
    private Long id;

    private String text;

    @Null
    private Long eventId;

    @Null
    private Long authorId;

    @Null
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_PATTERN)
    private LocalDateTime created;

}

