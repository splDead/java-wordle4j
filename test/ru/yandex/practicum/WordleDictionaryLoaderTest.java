package ru.yandex.practicum;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.EmptyDictionaryException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class WordleDictionaryLoaderTest {
    @Test
    void shouldThrowExceptionWhenFileNotFound() {
        WordleDictionaryLoader loader = new WordleDictionaryLoader();

        assertThrows(EmptyDictionaryException.class, () -> {
            loader.load("empty.txt");
        }, "Должно быть выброшено исключение при пустом или отсутствующем файле");
    }
}
