package ru.yandex.practicum.exceptions;

public class InvalidCyrillicContentException extends Exception {
    public InvalidCyrillicContentException(String message) {
        super(message);
    }
}
