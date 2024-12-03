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
    private Long Id;

    @Column(name = "app", nullable = false)
    private String App;

    @Column(name = "uri", nullable = false)
    private String URI;

    @Column(name = "IP", nullable = false)
    private String IP;

    @Column(name = "time_stamp", nullable = false)
    private LocalDateTime timestamp;
}
