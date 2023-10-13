package ru.practicum.explorewithme.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.dto.StatDto;
import ru.practicum.explorewithme.server.exception.ValidationException;
import ru.practicum.explorewithme.server.mapper.HitMapper;
import ru.practicum.explorewithme.server.model.Hit;
import ru.practicum.explorewithme.server.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class StatService {
    private final StatRepository statRepository;

    @Transactional
    public HitDto saveHitDto(HitDto hitDto) {
        Hit hit = statRepository.save(HitMapper.INSTANCE.toHit(hitDto));
        return HitMapper.INSTANCE.toHitDto(hit);
    }

    @Transactional(readOnly = true)
    public List<StatDto> getAllStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        validateDateRange(start, end);

        if (unique) {
            log.info("Get all stats with isUnique {} when uris {} ", unique, uris);
            return uris == null ? statRepository.getAllUniqueStats(start, end) : statRepository.getAllUniqueStatsWithUris(start, end, uris);
        } else {
            log.info("Get all stats {} ", unique);
            return uris == null ? statRepository.getAllStats(start, end) : statRepository.getAllStatsWithUris(start, end, uris);
        }
    }

    private void validateDateRange(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start)) {
            log.info("End time cannot be earlier than the start tim");
            throw new ValidationException("End date and time must be after the start date and time");
        }
    }

}
