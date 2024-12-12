package ru.practicum.event.mapper;

import ru.practicum.event.dto.LocationDto;
import ru.practicum.event.model.Location;

public class LocationMapper {
    public static Location toLocation(LocationDto dto){
        return new Location(
                null,
                dto.getLat(),
                dto.getLon()
        );
    }

    public static LocationDto toLocationDto(Location location){
        return new LocationDto(
                location.getLat(),
                location.getLon()
        );
    }
}
