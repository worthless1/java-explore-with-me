package ru.practicum.explorewithme.service.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentDto {

    @Size(min = 2, max = 2000)
    @NotBlank(message = "Comment cannot be empty")
    private String text;
}