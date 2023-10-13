package ru.practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.dto.StatDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class StatClient {
    private final WebClient client;

    public StatClient(@Value("${stats.server.url}") String serverUrl) {
        client = WebClient.create(serverUrl);
    }

    public ResponseEntity<List<StatDto>> getStats(String start, String end, List<String> uris, Boolean unique) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(StatDto.class)
                .doOnNext(c -> log.info("Get stats with param: start date {}, end date {}, uris {}, unique {}",
                        start, end, uris, unique))
                .block();
    }

    public void addHit(String app, String uri, String ip, LocalDateTime timestamp) {
        client.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new HitDto(app, uri, ip, timestamp))
                .retrieve()
                .toBodilessEntity()
                .doOnNext(c -> log.info("Save stats"))
                .block();
    }

}