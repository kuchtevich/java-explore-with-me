package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.StatsDtoIn;
import ru.practicum.StatsDtoOut;
import ru.practicum.stat.client.StatsClient;
import ru.practicum.stat.service.StatsService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private static final String APP_NAME = "main-service";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final StatsClient statsClient;

    @Override
    public void createStats(final String uri, final String ip) {

        final StatsDtoIn statsDtoIn = new StatsDtoIn();
        statsDtoIn.setApp(APP_NAME);
        statsDtoIn.setIp(ip);
        statsDtoIn.setUri(uri);
        statsDtoIn.setTimestamp(LocalDateTime.now());
        final StatsDtoIn stat = statsClient.createStats(statsDtoIn);
        log.info("Информация сохранена {}.", stat);
    }

    @Override
    public List<StatsDtoOut> getStats(final List<Long> eventsId, final boolean unique) {
        log.info("Запрос на получение статистики с сервиса для events {}.", eventsId);
        final String start = LocalDateTime.now().minusYears(20).format(FORMATTER);
        final String end = LocalDateTime.now().plusYears(20).format(FORMATTER);
        final String[] uris = eventsId.stream()
                .map(id -> String.format("/events/%d", id))
                .toArray(String[]::new);
        return statsClient.getStats(start, end, uris, unique);
    }

    @Override
    public Map<Long, Long> getView(List<Long> eventsId, boolean unique) {
        log.info("Запрос на получение просмотров с сервиса статистики для events {}.", eventsId);
        final List<StatsDtoOut> stats = getStats(eventsId, unique);
        final Map<Long, Long> views = new HashMap<>();
        for (StatsDtoOut stat : stats) {
            final Long id = Long.valueOf(stat.getUri().replace("/events/", ""));
            final Long view = stat.getHits();
            views.put(id, view);
        }
        log.info("Получены просмотры с сервиса статистики.");
        return views;
    }
}
