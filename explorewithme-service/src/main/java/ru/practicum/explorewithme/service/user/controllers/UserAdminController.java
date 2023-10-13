package ru.practicum.explorewithme.service.user.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.service.user.dto.NewUserRequest;
import ru.practicum.explorewithme.service.user.dto.UserDto;
import ru.practicum.explorewithme.service.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.explorewithme.service.util.Const.PAGE_FROM;
import static ru.practicum.explorewithme.service.util.Const.PAGE_SIZE;

@RestController
@RequestMapping("/admin/users")
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.info("Create user with id= {}", newUserRequest);
        return userService.createUser(newUserRequest);
    }

    @GetMapping
    public List<UserDto> get(@RequestParam(defaultValue = "") List<Long> ids,
                             @RequestParam(value = "from", defaultValue = PAGE_FROM) @PositiveOrZero Integer from,
                             @RequestParam(value = "size", defaultValue = PAGE_SIZE) @Positive Integer size) {
        log.info("Get all users with ids: {}", ids);
        return userService.getUsers(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(value = "userId") Long userId) {
        log.info("Delete user with id= {}", userId);
        userService.deleteUserById(userId);
    }
}