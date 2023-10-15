package ru.practicum.explorewithme.service.comment.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.service.comment.dto.CommentDto;
import ru.practicum.explorewithme.service.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.service.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.explorewithme.service.util.Const.PAGE_FROM;
import static ru.practicum.explorewithme.service.util.Const.PAGE_SIZE;

@RestController
@RequestMapping("/users/{userId}")
@Slf4j
@Validated
@RequiredArgsConstructor
public class CommentPrivateController {

    private final CommentService commentService;

    @PostMapping("/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createdComment(@PathVariable(value = "userId") Long userId,
                                     @RequestParam(value = "eventId") Long eventId,
                                     @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("Create comment {} of user with id= {} and event with id = {}", newCommentDto, userId, eventId);
        return commentService.createCommentPrivate(userId, eventId, newCommentDto);
    }

    @PatchMapping("/comments/{commentId}")
    public CommentDto updateComment(@PathVariable(value = "userId") Long userId,
                                    @PathVariable(value = "commentId") Long commentId,
                                    @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("Update comment with id={} and userId={}", commentId, userId);
        return commentService.updateCommentByIdPrivate(userId, commentId, newCommentDto);
    }

    @GetMapping("/comments/{commentId}")
    public CommentDto getCommentById(@PathVariable(value = "userId") Long userId,
                                     @PathVariable(value = "commentId") Long commentId) {
        log.info("Get comments with id={} and userId={}", commentId, userId);
        return commentService.getCommentByIdPrivate(userId, commentId);
    }

    @GetMapping("/comments")
    public List<CommentDto> getCommentByUser(@PathVariable(value = "userId") Long userId,
                                             @RequestParam(defaultValue = PAGE_FROM)
                                             @PositiveOrZero Integer from,
                                             @RequestParam(defaultValue = PAGE_SIZE)
                                             @Positive Integer size) {
        log.info("Get all comments with user id= {}", userId);
        return commentService.getCommentsByAuthorIdPrivate(userId, from, size);
    }

    @GetMapping("/events/{eventId}/comments")
    public List<CommentDto> getCommentsByEventId(@PathVariable(value = "userId") Long userId,
                                                 @PathVariable(value = "eventId") Long eventId,
                                                 @RequestParam(defaultValue = PAGE_FROM)
                                                 @PositiveOrZero Integer from,
                                                 @RequestParam(defaultValue = PAGE_SIZE)
                                                 @Positive Integer size) {
        log.info("Get comments by user Id {} and event id {}", userId, eventId);
        return commentService.getCommentsByEventIdPrivate(userId, eventId, from, size);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable(value = "userId") Long userId,
                              @PathVariable(value = "commentId") Long commentId) {
        log.info("Delete comment with id= {} of user with id= {}", commentId, userId);
        commentService.deleteCommentByIdPrivate(userId, commentId);
    }

}