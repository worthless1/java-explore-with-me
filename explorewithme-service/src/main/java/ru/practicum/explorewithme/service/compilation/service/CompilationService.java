package ru.practicum.explorewithme.service.compilation.service;

import ru.practicum.explorewithme.service.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.service.compilation.dto.CompilationUpdatedDto;
import ru.practicum.explorewithme.service.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {

    CompilationDto createCompilationAdmin(NewCompilationDto newCompilationDto);

    CompilationDto updateCompilationByIdAdmin(Long compId, CompilationUpdatedDto updateCompilationRequest);

    void deleteCompilationByIdAdmin(Long compId);

    List<CompilationDto> getAllCompilationsPublic(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationByIdPublic(Long id);

}