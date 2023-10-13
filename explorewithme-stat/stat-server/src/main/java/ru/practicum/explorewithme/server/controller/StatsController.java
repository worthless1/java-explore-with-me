package ru.practicum.explorewithme.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.dto.StatDto;
import ru.practicum.explorewithme.server.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explorewithme.server.util.Const.DATETIME_PATTERN;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsController {
    private final StatService statService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto saveHit(@RequestBody HitDto hitDto) {
        log.info("Save StatsHit {}", hitDto);
        return statService.saveHitDto(hitDto);
    }

    @GetMapping("/stats")
    public List<StatDto> getAllStats(
            @RequestParam @DateTimeFormat(pattern = DATETIME_PATTERN) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = DATETIME_PATTERN) LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Get viewed stats with startDate {} endDate {}, uris {} unique {}", start, end, uris, unique);
        return statService.getAllStats(start, end, uris, unique);
    }

}
