package ru.practicum.explorewithme.service.category.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.service.category.dto.CategoryDto;
import ru.practicum.explorewithme.service.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.explorewithme.service.util.Const.PAGE_FROM;
import static ru.practicum.explorewithme.service.util.Const.PAGE_SIZE;

@RestController
@RequestMapping("/categories")
@Slf4j
@Validated
@RequiredArgsConstructor
public class CategoryPublicController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> get(@RequestParam(value = "from", defaultValue = PAGE_FROM) @PositiveOrZero Integer from,
                                 @RequestParam(value = "size", defaultValue = PAGE_SIZE) @Positive Integer size) {
        log.info("Get category, parameters: from= {} size= {}", from, size);
        return categoryService.getCategory(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getById(@PathVariable Long catId) {
        log.info("Get category with id {}", catId);
        return categoryService.getCategoryById(catId);
    }

}