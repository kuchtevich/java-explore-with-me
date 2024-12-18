package ru.practicum.service;

import ru.practicum.StatsDtoIn;
import ru.practicum.StatsDtoOut;
import java.util.List;
import java.time.LocalDateTime;

public interface StatService {

    StatsDtoIn createStats(StatsDtoIn statsDtoIn);

    List<StatsDtoOut> getStats(LocalDateTime start,
                               LocalDateTime end,
                               List<String> uris,
                               Boolean unique);
}
