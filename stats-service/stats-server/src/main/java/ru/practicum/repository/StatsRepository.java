package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.StatsDtoOut;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Long> {
    @Query("""
            SELECT new ru.practicum.StatsDtoOut(s.ip, s.uri, COUNT(DISTINCT s.ip))
            FROM Stats AS s
            WHERE s.timestamp BETWEEN :start AND :end AND s.uri IN :uris
            GROUP BY s.ip, s.uri
            ORDER BY COUNT(DISTINCT s.ip) DESC
            """)
    List<StatsDtoOut> findAllWithIPAndURI(List<String> uris,
                                          LocalDateTime start,
                                          LocalDateTime end);

    @Query("""
            SELECT new ru.practicum.StatsDtoOut(s.ip, s.uri, COUNT(DISTINCT s.ip))
            FROM Stats AS s
            WHERE s.timestamp BETWEEN :start AND :end
            GROUP BY s.ip, s.uri
            ORDER BY COUNT(DISTINCT s.ip) DESC
            """)
    List<StatsDtoOut> findAllWithIPAndWithoutURI(LocalDateTime start,
                                                 LocalDateTime end);

    @Query("""
            SELECT new ru.practicum.StatsDtoOut(s.ip, s.uri, COUNT(s.ip))
            FROM Stats AS s
            WHERE s.timestamp BETWEEN :start AND :end AND s.uri IN :uris
            GROUP BY s.ip, s.uri
            ORDER BY COUNT (s.ip) DESC
            """)
    List<StatsDtoOut> findAllWithURIs(List<String> uris,
                                      LocalDateTime start,
                                      LocalDateTime end);

    @Query("""
            SELECT new ru.practicum.StatsDtoOut(s.ip, s.uri, COUNT(s.ip))
            FROM Stats AS s
            WHERE s.timestamp BETWEEN :start AND :end
            GROUP BY s.ip, s.uri
            ORDER BY COUNT (s.ip) DESC
            """)
    List<StatsDtoOut> findAllWithoutURIs(LocalDateTime start,
                                         LocalDateTime end);
}
