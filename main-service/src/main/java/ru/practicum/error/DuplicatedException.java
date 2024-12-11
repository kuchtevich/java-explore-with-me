package ru.practicum.error;

public class DuplicatedException extends RuntimeException {
    public DuplicatedException(final String message) {
        super(message);
    }
}
