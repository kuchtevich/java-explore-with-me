package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
    boolean existsByLatAndLon(Float lat, Float lon);

    Location findByLatAndLon(Float lat, Float lon);
}
