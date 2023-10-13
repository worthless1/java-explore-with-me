package ru.practicum.explorewithme.service.user.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class NewUserRequest {

    @Email
    @NotBlank(message = "Email cannot be empty")
    @Size(min = 6, max = 254)
    private String email;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 250)
    private String name;
}