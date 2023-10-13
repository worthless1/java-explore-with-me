package ru.practicum.explorewithme.service.compilation.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.service.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.service.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

import static ru.practicum.explorewithme.service.util.Const.PAGE_FROM;
import static ru.practicum.explorewithme.service.util.Const.PAGE_SIZE;

@RestController
@RequestMapping("/compilations")
@Slf4j
@Validated
@RequiredArgsConstructor
public class CompilationPublicController {

    private final CompilationService serviceCompilation;

    @GetMapping("/{compId}")
    public CompilationDto getByIdPublic(@PathVariable Long compId) {
        log.info("Get compilation with id= {}", compId);
        return serviceCompilation.getCompilationByIdPublic(compId);
    }

    @GetMapping
    public Collection<CompilationDto> get(@RequestParam(required = false) Boolean pinned,
                                          @RequestParam(defaultValue = PAGE_FROM)
                                          @PositiveOrZero Integer from,
                                          @RequestParam(defaultValue = PAGE_SIZE)
                                          @Positive Integer size) {
        log.info("Get compilations pinned {}", pinned);
        return serviceCompilation.getAllCompilationsPublic(pinned, from, size);
    }

}