package ru.practicum.error;

public class ValidationException extends RuntimeException  {
    public ValidationException(final String message) {
        super(message);
    }
}
