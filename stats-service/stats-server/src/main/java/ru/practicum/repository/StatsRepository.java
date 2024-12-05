package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.StatsDtoOut;
import ru.practicum.model.Stats;
import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Long> {
    @Query("""
            SELECT new ru.practicum.StatsDtoOut(s.IP, s.URI, COUNT(DISTINCT s.IP))
            FROM Stats AS s
            WHERE s.timestamp BETWEEN :start AND :end AND s.URI IN :URIs
            GROUP BY s.IP, s.URI
            ORDER BY COUNT(DISTINCT s.IP) DESC
            """)
    List<StatsDtoOut> findAllWithIPAndURI(List<String> URIs,
                                          LocalDateTime start,
                                          LocalDateTime end); //findAllWithUniqueIpWithUris

    @Query("""
            SELECT new ru.practicum.StatsDtoOut(s.IP, s.URI, COUNT(DISTINCT s.IP))
            FROM Stats AS s
            WHERE s.timestamp BETWEEN :start AND :end
            GROUP BY s.IP, s.URI
            ORDER BY COUNT(DISTINCT s.IP) DESC
            """)
    List<StatsDtoOut> findAllWithIPAndWithoutURI(LocalDateTime start,
                                                     LocalDateTime end);

    @Query("""
            SELECT new ru.practicum.StatDtoOutput(s.IP, s.URI, COUNT(s.ip))
            FROM Stats AS s
            WHERE s.timestamp BETWEEN :start AND :end AND s.URI IN :URIs
            GROUP BY s.ip, s.URI
            ORDER BY COUNT (s.ip) DESC
            """)
    List<StatsDtoOut> findAllWithURIs(List<String> uris,
                                        LocalDateTime start,
                                        LocalDateTime end);

    @Query("""
            SELECT new ru.practicum.StatsDtoOut(s.IP, s.URI, COUNT(s.IP))
            FROM Stats AS s
            WHERE s.timestamp BETWEEN :start AND :end
            GROUP BY s.IP, s.URI
            ORDER BY COUNT (s.IP) DESC
            """)
    List<StatsDtoOut> findAllWithoutURIs(LocalDateTime start,
                                           LocalDateTime end);
}
