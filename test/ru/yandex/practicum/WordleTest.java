package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.InvalidCyrillicContentException;
import ru.yandex.practicum.exceptions.InvalidStringLengthException;
import ru.yandex.practicum.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    private WordleDictionary dictionary;
    private WordleGame game;
    private List<String> list = List.of("арбуз", "багет", "ветка", "экран", "поиск", "герой", "гонец");

    @BeforeEach
    void setUp() {
        dictionary = new WordleDictionary(list);
        game = new WordleGame(dictionary);
    }

    @Test
    @DisplayName("Проверяем, что при вводе правильного ответа игра пройдена")
    void shouldReturnSuccessHintWhenWordIsCorrect() throws InvalidCyrillicContentException, InvalidStringLengthException, NotFoundException {
        game.guess(game.getAnswer());
        assertTrue(game.isGuessed());
    }

    @Test
    @DisplayName("Проверка уменьшения количества оставшихся шагов")
    void shouldDecreaseStepsOnWrongGuess() throws InvalidCyrillicContentException, InvalidStringLengthException, NotFoundException {
        // проверяем, что уменьшение счетчика оставшихся ходов
        int initialSteps = game.getSteps();
        List<String> filteredList = new ArrayList<>();
        String answer = game.getAnswer();

        // фильтруем правильный ответ
        for (String str : game.getDictionary().getWords()) {
            if (!answer.equals(str)) {
                filteredList.add(str);
            }
        }

        Random random = new Random();
        int index = random.nextInt(filteredList.size());
        String wrongAnswer = filteredList.get(index);

        // передаем заведомо ложный ответ
        game.guess(wrongAnswer);

        assertEquals(initialSteps - 1, game.getSteps(), "Количество шагов должно уменьшиться");
    }

    @Test
    @DisplayName("Проверка подсказки")
    void shouldReturnHintWhenRequested() {
        String hint = game.getHint();
        assertNotNull(hint);
        assertFalse(hint.isBlank());
    }
}
