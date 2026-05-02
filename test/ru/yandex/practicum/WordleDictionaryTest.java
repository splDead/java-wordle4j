package ru.yandex.practicum;

import com.sun.tools.javac.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WordleDictionaryTest {
    private WordleDictionary dictionary;
    private WordleGame game;
    private List<String> list = List.of("арбуз", "багет", "ветка", "экран", "поиск", "герой", "гонец");

    @BeforeEach
    void setUp() {
        dictionary = new WordleDictionary(list);
        game = new WordleGame(dictionary);
    }

    @Test
    void testHasTrue() {
        assertTrue(WordleDictionary.hasTrue(new boolean[]{false, true, false}), "Есть хотя бы одна true");
        assertFalse(WordleDictionary.hasTrue(new boolean[]{false, false}), "Нет ни одной true");
        assertFalse(WordleDictionary.hasTrue(new boolean[]{}), "Пустой массив не содержит true");
    }

    @Test
    void testIsAllTrue() {
        assertTrue(WordleDictionary.isAllTrue(new boolean[]{true, true, true}), "Все значения true");
        assertFalse(WordleDictionary.isAllTrue(new boolean[]{true, false, true}), "Есть одно false — должно быть false");
        assertTrue(WordleDictionary.isAllTrue(new boolean[]{}), "Пустой массив: условий для false нет, возвращает true");
    }

    @Test
    void testCountTrue() {
        assertEquals(3, WordleDictionary.countTrue(new boolean[]{true, false, true, true}), "Должно быть 3 попадания");
        assertEquals(0, WordleDictionary.countTrue(new boolean[]{false, false}), "Ноль попаданий");
        assertEquals(0, WordleDictionary.countTrue(new boolean[]{}), "В пустом массиве 0 попаданий");
    }

    @Test
    @DisplayName("Проверка подсказки, выводимой под введенном словом")
    void shouldReturnPositiveHint() {
        String word = "гонец";
        String answer = "герой";
        String hint = dictionary.getHint(word, answer);
        assertEquals("+^-^-", hint);
    }

    @Test
    @DisplayName("Проверка слова с ответом, что одна буква угадана")
    void shouldReturnPositiveOneGuessLetter() {
        String word = "гонец";
        String answer = "герой";
        boolean[] guessedLettersPosition = {false, false, false, false, false};
        WordleDictionary.analyzeGuessedLettersPosition(word, answer, guessedLettersPosition);
        assertTrue(WordleDictionary.hasTrue(guessedLettersPosition));
    }

    @Test
    @DisplayName("Проверяем, что слово по маске соответствует ответу")
    void shouldReturnTrueMatchesMask() {
        String word = "гонец";
        String answer = "герой";
        boolean[] guessedLettersPosition = {true, false, false, false, false};
        assertTrue(WordleDictionary.matchesMask(word, answer, guessedLettersPosition));
    }

    @Test
    @DisplayName("Проверяем, что слово по маске соответствует ответу")
    void shouldReturnFalseMatchesMask() {
        String word = "гонец";
        String answer = "герой";
        boolean[] guessedLettersPosition = {false, false, false, true, false};
        assertFalse(WordleDictionary.matchesMask(word, answer, guessedLettersPosition));
    }
}
