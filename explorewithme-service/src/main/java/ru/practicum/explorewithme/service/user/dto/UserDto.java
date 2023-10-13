package ru.practicum.explorewithme.service.user.dto;

import lombok.*;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String email;
    private Long id;
    private String name;
}