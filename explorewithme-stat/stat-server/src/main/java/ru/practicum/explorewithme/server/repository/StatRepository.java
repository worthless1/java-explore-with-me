package ru.practicum.explorewithme.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.practicum.explorewithme.dto.StatDto;
import ru.practicum.explorewithme.server.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<Hit, Long> {
    @Query("SELECT new ru.practicum.explorewithme.dto.StatDto(h.app, h.uri, COUNT(h.ip) as count_ip) " +
            "FROM Hit as h " +
            "WHERE h.timestamp >= :start " +
            "AND h.timestamp <= :end " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY count_ip DESC ")
    List<StatDto> getAllStatsWithUris(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );

    @Query("SELECT new ru.practicum.explorewithme.dto.StatDto(h.app, h.uri, COUNT(DISTINCT(h.ip)) as count_ip) " +
            "FROM Hit as h " +
            "WHERE h.timestamp >= :start " +
            "AND h.timestamp <= :end " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY count_ip DESC ")
    List<StatDto> getAllUniqueStatsWithUris(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );

    @Query("SELECT new ru.practicum.explorewithme.dto.StatDto(h.app, h.uri, COUNT(h.ip) as count_ip) " +
            "FROM Hit as h " +
            "WHERE h.timestamp >= :start " +
            "AND h.timestamp <= :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY count_ip DESC ")
    List<StatDto> getAllStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("SELECT new ru.practicum.explorewithme.dto.StatDto(h.app, h.uri, COUNT(DISTINCT(h.ip)) as count_ip) " +
            "FROM Hit as h " +
            "WHERE h.timestamp >= :start " +
            "AND h.timestamp <= :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY count_ip DESC ")
    List<StatDto> getAllUniqueStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
