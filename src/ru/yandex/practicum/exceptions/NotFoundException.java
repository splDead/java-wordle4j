package ru.yandex.practicum.exceptions;

public class NotFoundException extends Exception {
    private String word;

    public NotFoundException(String message, String word) {
        super(message);
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}
