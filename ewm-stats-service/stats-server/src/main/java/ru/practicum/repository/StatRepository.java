package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.StatsDtoOut;
import ru.practicum.model.Stats;
import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Stats, Long> {
    @Query("""
            SELECT new ru.practicum.StatDtoOutput(s.ip, s.uri, COUNT(DISTINCT s.ip))
            FROM Stat AS s
            WHERE s.timestamp BETWEEN :start AND :end AND s.uri IN :uris
            GROUP BY s.ip, s.uri
            ORDER BY COUNT(DISTINCT s.ip) DESC
            """)
    List<StatsDtoOut> findAllWithUniqueIpWithUris(List<String> uris,
                                          LocalDateTime start,
                                          LocalDateTime end);

    @Query("""
            SELECT new ru.practicum.StatDtoOutput(s.ip, s.uri, COUNT(DISTINCT s.ip))
            FROM Stat AS s
            WHERE s.timestamp BETWEEN :start AND :end
            GROUP BY s.ip, s.uri
            ORDER BY COUNT(DISTINCT s.ip) DESC
            """)
    List<StatsDtoOut> findAllWithUniqueIpWithoutUris(LocalDateTime start,
                                                     LocalDateTime end);

    @Query("""
            SELECT new ru.practicum.StatDtoOutput(s.ip, s.uri, COUNT(s.ip))
            FROM Stat AS s
            WHERE s.timestamp BETWEEN :start AND :end AND s.uri IN :uris
            GROUP BY s.ip, s.uri
            ORDER BY COUNT (s.ip) DESC
            """)
    List<StatsDtoOut> findAllWithUris(List<String> uris,
                                        LocalDateTime start,
                                        LocalDateTime end);

    @Query("""
            SELECT new ru.practicum.StatDtoOutput(s.ip, s.uri, COUNT(s.ip))
            FROM Stat AS s
            WHERE s.timestamp BETWEEN :start AND :end
            GROUP BY s.ip, s.uri
            ORDER BY COUNT (s.ip) DESC
            """)
    List<StatsDtoOut> findAllWithoutUris(LocalDateTime start,
                                           LocalDateTime end);
}
