package ru.practicum.time;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateAndTime {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public DateTimeFormatter getDefaultDateTimeFormatter() {
        return DateTimeFormatter.ofPattern(DATE_FORMAT);
    }

    public LocalDateTime getMaxDateTime() {
        return LocalDateTime.parse("9999-12-31T23:59:59.999999");
    }

    public LocalDateTime getMinDateTime() {
        return LocalDateTime.parse("2000-01-01T00:00:00.000000");
    }
}
