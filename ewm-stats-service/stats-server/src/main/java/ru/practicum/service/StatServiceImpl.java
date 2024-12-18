package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsDtoIn;
import ru.practicum.StatsDtoOut;
import ru.practicum.exception.ValidationException;
import ru.practicum.mapper.StatMapper;
import ru.practicum.model.Stat;
import ru.practicum.repository.StatRepository;

import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StatServiceImpl implements

        StatService {
    private final StatRepository statRepository;

    @Override
    public StatsDtoIn createStats(StatsDtoIn statDtoIn) {
        final Stat stat = statRepository.save(StatMapper.toStats(statDtoIn));
        log.info("Элемент статистики добавлен в БД {}.", stat);
        return StatMapper.toStatsDto(stat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatsDtoOut> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        if (start.isAfter(end)) {
            throw new ValidationException("Дата начала и дата окончания не могут быть равны или противоречить друг другу.");
        }

        if (unique) {
            if (uris != null) {
                return statRepository.findAllWithUniqueIpWithUris(uris, start, end);
            }
            return statRepository.findAllWithUniqueIpWithoutUris(start, end);
        } else {
            if (uris != null) {
                return statRepository.findAllWithUris(uris, start, end);
            }
            return statRepository.findAllWithoutUris(start, end);
        }
    }
}
