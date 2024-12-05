package ru.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stats")
public class Stats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "App", nullable = false)
    private String app;

    @Column(name = "URI", nullable = false)
    private String uri;

    @Column(name = "IP", nullable = false)
    private String ip;

    @Column(name = "time_stamp", nullable = false)
    private LocalDateTime timestamp;
}
