package ru.practicum.explorewithme.service.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    Long id;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 1, max = 50) String name;
}