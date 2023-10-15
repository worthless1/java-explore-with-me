package ru.practicum.explorewithme.service.comment.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.service.comment.dto.CommentDto;
import ru.practicum.explorewithme.service.comment.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.explorewithme.service.util.Const.*;

@RestController
@RequestMapping("/comments")
@Slf4j
@Validated
@RequiredArgsConstructor
public class CommentPublicController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> searchComments(@RequestParam(value = "eventId") Long eventId,
                                           @RequestParam(required = false) String text,
                                           @RequestParam(defaultValue = PAGE_FROM)
                                            @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = PAGE_SIZE)
                                            @Positive Integer size) {
        log.info("search comments with text {}", text);
        return commentService.searchComments(eventId, text, from, size);
    }

}