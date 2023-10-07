package ru.practicum.explorewithme.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.dto.StatDto;
import ru.practicum.explorewithme.server.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explorewithme.server.util.Const.DATETIME_PATTERN;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatService statService;

    @PostMapping("/hit")
    public HitDto saveHit(@RequestBody HitDto hitDto) {
        return statService.saveHitDto(hitDto);
    }

    @GetMapping("/stats")
    public List<StatDto> getAllStats(
            @RequestParam @DateTimeFormat(pattern = DATETIME_PATTERN) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = DATETIME_PATTERN) LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique) {
        return statService.getAllStats(start, end, uris, unique);
    }

}
