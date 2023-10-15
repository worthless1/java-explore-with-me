package ru.practicum.explorewithme.service.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.service.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.service.compilation.mapper.CompilationMapper;
import ru.practicum.explorewithme.service.compilation.dto.CompilationUpdatedDto;
import ru.practicum.explorewithme.service.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.service.compilation.model.Compilation;
import ru.practicum.explorewithme.service.compilation.repository.CompilationRepository;
import ru.practicum.explorewithme.service.event.model.Event;
import ru.practicum.explorewithme.service.event.repository.EventRepository;
import ru.practicum.explorewithme.service.exception.NotFoundException;
import ru.practicum.explorewithme.service.util.Pagination;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.explorewithme.service.compilation.mapper.CompilationMapper.mapToCompilationDto;
import static ru.practicum.explorewithme.service.compilation.mapper.CompilationMapper.mapToNewCompilation;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto createCompilationAdmin(NewCompilationDto compilationDto) {
        Compilation compilation = mapToNewCompilation(compilationDto);
        compilation.setPinned(compilationDto.getPinned() != null && compilationDto.getPinned());
        Set<Long> eventsId = compilationDto.getEvents();
        if (eventsId != null) {
            Set<Event> events = new HashSet<>(eventRepository.findAllByIdIn(eventsId));
            compilation.setEvents(events);
        }
        Compilation savedCompilation = compilationRepository.save(compilation);
        return mapToCompilationDto(savedCompilation);
    }

    @Override
    public CompilationDto updateCompilationByIdAdmin(Long compId, CompilationUpdatedDto dto) {
        Compilation toUpdate = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException(String.format("Compilation %s not found", compId)));
        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            toUpdate.setTitle(dto.getTitle());
        }
        toUpdate.setPinned(dto.getPinned() != null && dto.getPinned());
        if (dto.getEvents() != null && !dto.getEvents().isEmpty()) {
            Set<Long> eventsId = dto.getEvents();
            Collection<Event> events = eventRepository.findAllByIdIn(eventsId);
            toUpdate.setEvents(new HashSet<>(events));
        }
        return mapToCompilationDto(toUpdate);
    }

    @Override
    public void deleteCompilationByIdAdmin(Long compId) {
        getCompilation(compId);
        log.info("Delete compilation with id= {} ", compId);
        compilationRepository.deleteById(compId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CompilationDto> getAllCompilationsPublic(Boolean pinned, Integer from, Integer size) {
        log.info("Get all compilations");

        return compilationRepository.findAllByPinnedAndPagination(
                        pinned, new Pagination(from, size, Sort.unsorted()))
                .getContent()
                .stream()
                .map(CompilationMapper::mapToCompilationDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public CompilationDto getCompilationByIdPublic(Long id) {
        Compilation compilation = getCompilation(id);
        log.info("Get compilation with id= {} ", id);
        return mapToCompilationDto(compilation);
    }

    @Transactional(readOnly = true)
    private Compilation getCompilation(Long id) {
        return compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + id + " not found"));
    }
}
