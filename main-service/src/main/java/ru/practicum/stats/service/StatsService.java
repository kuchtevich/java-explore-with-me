package ru.practicum.stats.service;

import ru.practicum.StatsDtoOut;
import java.util.List;
import java.util.Map;


public interface StatsService {

    void createStats(final String uri, final String ip);

    List<StatsDtoOut> getStats(final List<Long> eventsId, final boolean unique);

    Map<Long, Long> getView(final List<Long> eventsId, final boolean unique);
}
