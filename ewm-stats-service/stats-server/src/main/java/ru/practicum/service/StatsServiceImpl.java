package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsDtoIn;
import ru.practicum.StatsDtoOut;
import ru.practicum.exception.ValidationException;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.model.Stats;
import ru.practicum.repository.StatsRepository;
import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public StatsDtoIn createStats(StatsDtoIn statDtoIn) {
        final Stats stats = statsRepository.save(StatsMapper.toStats(statDtoIn));
        log.info("Элемент статистики добавлен в БД {}.", stats);
        return StatsMapper.toStatsDto(stats);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatsDtoOut> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        if (start.isAfter(end)) {
            throw new ValidationException("Дата начала и дата окончания не могут быть равны или противоречить друг другу.");
        }

        if (unique) {
            if (uris != null) {
                return statsRepository.findAllWithUniqueIpWithUris(uris, start, end);
            }
            return statsRepository.findAllWithUniqueIpWithoutUris(start, end);
        } else {
            if (uris != null) {
                return statsRepository.findAllWithUris(uris, start, end);
            }
            return statsRepository.findAllWithoutUris(start, end);
        }
    }
}
