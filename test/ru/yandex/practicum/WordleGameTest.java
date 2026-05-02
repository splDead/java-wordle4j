package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.InvalidCyrillicContentException;
import ru.yandex.practicum.exceptions.InvalidStringLengthException;
import ru.yandex.practicum.exceptions.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {
    private WordleDictionary dictionary;
    private WordleGame game;
    private final List<String> list = List.of("арбуз", "багет", "ветка", "экран", "поиск", "герой", "гонец");

    @BeforeEach
    void setUp() {
        dictionary = new WordleDictionary(list);
        game = new WordleGame(dictionary);
    }

    @Test
    @DisplayName("Должно выбрасываться исключение в случае не кириллических символов")
    void shouldThrowExceptionOnNotCyrillicSymbols() {
        assertThrows(InvalidCyrillicContentException.class, () -> {
            game.guess("qwert");
        });
    }

    @Test
    @DisplayName("Должно выбрасываться исключение при неверной длине слова")
    void shouldThrowExceptionOnInvalidLength() {
        assertThrows(InvalidStringLengthException.class, () -> {
            game.guess("рот");
        });
    }

    @Test
    @DisplayName("Должно выбрасываться исключение, если слова нет в словаре")
    void shouldThrowExceptionWhenWordNotFound() {
        assertThrows(NotFoundException.class, () -> {
            game.guess("клопы");
        });
    }

    @Test
    @DisplayName("Максимальное количество подсказок, открывающих буквы, ограничено")
    void shouldRespectMaxHintCount() {
        // Вызываем подсказку много раз
        for (int i = 0; i < 5; i++) {
            game.getHint();
        }


        // Проверяем, что осталось ноль подсказок
        assertTrue(game.getMaxHintCount() == 0);
    }
}
