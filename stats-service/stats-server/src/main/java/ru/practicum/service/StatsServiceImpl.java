package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsDtoIn;
import ru.practicum.StatsDtoOut;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.model.Stats;
import ru.practicum.repository.StatsRepository;
import java.util.List;
import java.time.LocalDateTime;
import ru.practicum.exception.ValidationException;

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
    public List<StatsDtoOut> getStats(LocalDateTime start, LocalDateTime end, List<String> URIs, Boolean unique) {

        if (start.isAfter(end)) {
            throw new ValidationException("Дата начала и дата окончания указаны неверно.");
        }

        if (unique) {
            if (URIs != null) {
                return statsRepository.findAllWithIPAndURI(URIs, start, end);
            }
            return statsRepository.findAllWithIPAndWithoutURI(start, end);
        } else {
            if (URIs != null) {
                return statsRepository.findAllWithURIs(URIs, start, end);
            }
            return statsRepository.findAllWithoutURIs(start, end);
        }
    }
}
