package ru.practicum.explorewithme.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.server.model.Hit;

import static ru.practicum.explorewithme.server.util.Const.DATETIME_PATTERN;

@Mapper
public interface HitMapper {
    HitMapper INSTANCE = Mappers.getMapper(HitMapper.class);

    @Mapping(target = "timestamp", source = "hit.timestamp", dateFormat = DATETIME_PATTERN)
    HitDto toHitDto(Hit hit);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamp", source = "hitDto.timestamp", dateFormat = DATETIME_PATTERN)
    Hit toHit(HitDto hitDto);
}
