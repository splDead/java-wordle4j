package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.InvalidCyrillicContentException;
import ru.yandex.practicum.exceptions.InvalidStringLengthException;
import ru.yandex.practicum.exceptions.NotFoundException;

import java.util.*;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {
    private String answer;
    private int steps;
    private WordleDictionary dictionary;
    private boolean isGuessed;
    private List<String> possibleAnswers;
    private List<String> wordsUsed;
    private boolean[] guessedLettersPosition;
    private int maxHintCount;

    public WordleGame(WordleDictionary dictionary) {
        this.dictionary = dictionary;
        steps = 6;
        answer = dictionary.getRandomWord();
        isGuessed = false;
        possibleAnswers = new ArrayList<>(dictionary.getWords());
        wordsUsed = new ArrayList<>();
        guessedLettersPosition = new boolean[WordleDictionary.WORD_LENGTH];
        maxHintCount = 3;
    }

    // добавляет слово в список использованных с учетом уникальности
    private void addWordUsed(String word) {
        if (!wordsUsed.contains(word)) {
            wordsUsed.add(word);
        }
    }

    // основной метод игры, проверяющий угадал ли игрок слово
    public String guess(String word) throws InvalidCyrillicContentException, InvalidStringLengthException, NotFoundException {
        // проверяем слово
        dictionary.checkWord(word);

        // логируем введенное слово
        Wordle.gameLogger.log("Игрок ввел: " + word);

        // проверяем совпадение с ответом
        if (word.equals(answer)) {
            Wordle.gameLogger.log("Игрок угадал слово: '" + answer + "', осталось попыток: " + (steps - 1));
            isGuessed = true; // задаем флаг для завершения игры
            return "+++++";
        } else {
            // добавляем слово в список использованных
            addWordUsed(word);

            // проверяем есть ли введенное слово в возможных ответах и если есть удаляем его
            if (possibleAnswers.contains(word)) {
                possibleAnswers.remove(word);
            }

            // анализируем на какой позиции буква угадана
            WordleDictionary.analyzeGuessedLettersPosition(word, answer, guessedLettersPosition);

            // уменьшаем оставшиеся попытки
            steps--;
            Wordle.gameLogger.log("Осталось попыток: " + steps);

            return WordleDictionary.getHint(word, answer);
        }
    }

    public boolean isGuessed() {
        return isGuessed;
    }

    public int getSteps() {
        return steps;
    }

    // метод предлагающий подсказку
    public String getHint() {
        Random random = new Random();
        int index;
        String possibleAnswer;

        // если список возможных ответов пуст отдаем случайный использованный
        if (possibleAnswers.isEmpty()) {
            index = random.nextInt(wordsUsed.size());
            possibleAnswer = wordsUsed.get(index);
            Wordle.gameLogger.log("Предложенный ответ взят из списка ранее использованных слов: " + possibleAnswer);

            // открываем следующую букву
            openNextLetterPosition();

            return possibleAnswer;
        } else {
            // проверяем угадал ли игрок хотя бы одну букву
            if (WordleDictionary.hasTrue(guessedLettersPosition)) {
                List<String> filteredPossibleAnswers = new ArrayList<>();

                // фильтруем слова по угаданным позициям
                for (String str : possibleAnswers) {
                    if (WordleDictionary.matchesMask(str, answer, guessedLettersPosition)) {
                        filteredPossibleAnswers.add(str);
                    }
                }

                possibleAnswers = filteredPossibleAnswers; // обновляем список возможных ответов
            }

            // открываем букву для следующей подсказки
            openNextLetterPosition();

            index = random.nextInt(possibleAnswers.size());
            possibleAnswer = possibleAnswers.remove(index); // извлекаем возможный ответ из всего списка возможных
            Wordle.gameLogger.log("Предложенный ответ взят из списка возможных ответов: " + possibleAnswer);

            addWordUsed(possibleAnswer); // сохраняем его
            return possibleAnswer;
        }
    }

    // задаем случайной позиции значение true, что означает открытую букву
    private void openNextLetterPosition() {
        // закончились подсказки или все буквы открыты или открыто три и более буквы ничего не делаем
        if (maxHintCount == 0 || WordleDictionary.isAllTrue(guessedLettersPosition) || WordleDictionary.countTrue(guessedLettersPosition) >= 3) {
            return;
        }

        Wordle.gameLogger.log("Используем подсказку, открываем случайную букву");
        maxHintCount--;
        Random random = new Random();
        while (true) {
            int index = random.nextInt(guessedLettersPosition.length);
            if (!guessedLettersPosition[index]) {
                guessedLettersPosition[index] = true;
                break;
            }
        }
    }

    // геттеры только для теста
    public String getAnswer() {
        return answer;
    }

    public WordleDictionary getDictionary() {
        return dictionary;
    }

    public int getMaxHintCount() {
        return maxHintCount;
    }
}
