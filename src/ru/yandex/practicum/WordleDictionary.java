package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.InvalidCyrillicContentException;
import ru.yandex.practicum.exceptions.InvalidStringLengthException;
import ru.yandex.practicum.exceptions.NotFoundException;

import java.util.*;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {
    public static final int WORD_LENGTH = 5;
    private List<String> words;

    public WordleDictionary(List<String> words) {
        this.words = prepareDictionary(words);
    }

    /**
     * Подготовка загруженного списка:
     * <ul>
     *     <li>приведение слов в нижний регистр</li>
     *     <li>замена ё -> е</li>
     *     <li>отсечение пробелов</li>
     * </ul>
    */
    private List<String> prepareDictionary(List<String> list) {
        List<String> res = new ArrayList<>();
        for (String word : list) {
            if (word.length() == WORD_LENGTH) {
                res.add(word.toLowerCase().replace('ё', 'е').trim());
            }
        }
        return res;
    }

    /**
     * Получение случайного слова
     */
    public String getRandomWord() {
        Random random = new Random();
        int index = random.nextInt(words.size());
        return words.get(index);
    }

    /**
     * Проверяет слово на соответствие правилам игры
     * @param word проверяемое слово
     * @throws InvalidCyrillicContentException допустима только кириллица
     * @throws InvalidStringLengthException недопустимая длина слова
     * @throws NotFoundException слова отсутствует в словаре
     */
    public void checkWord(String word) throws InvalidCyrillicContentException, InvalidStringLengthException, NotFoundException {
        // проверяем слово на длину
        if (word.length() != WORD_LENGTH) {
            throw new InvalidStringLengthException("Некорректная длина ввода: " + word.length());
        }

        // проверяем кириллицу
        if (!word.matches("[\\p{IsCyrillic}]+")) {
            throw new InvalidCyrillicContentException("Слово '" + word + "' содержит недопустимые символы!");
        }

        // проверяем наличие слова в словаре
        if (!words.contains(word)) {
            throw new NotFoundException("Слово '" + word + "' не найдено в словаре!", word);
        }
    }

    /**
     * @param word слово на основе которого подбирается список
     * @return список слов, состоящих из букв слова {@code word}
     */
    public List<String> getHints(String word) {
        return findWords(words, getUniqueLetters(word));
    }

    /**
     * @return множество уникальных букв, из которых состоит слово {@code word}
     */
    private Set<Character> getUniqueLetters(String word) {
        Set<Character> letters = new HashSet<>();

        for (char c : word.toCharArray()) {
            letters.add(c);
        }

        return letters;
    }

    /**
     * @param words список слов для анализа
     * @param uniqueLetters множество букв
     * @return список подходящих слов, которые включают в себя все множество букв
     */
    private List<String> findWords(List<String> words, Set<Character> uniqueLetters) {
        List<String> found = new ArrayList<>();

        for (String word : words) {
            // проверяем соответствует слово набору уникальных символов
            if (isMadeOf(word, uniqueLetters)) {
                found.add(word);
            }
        }
        return found;
    }

    /**
     * @param word слово для анализа
     * @param letters множество букв
     * @return флаг, означающий входит ли множество букв целиком в слово
     */
    private boolean isMadeOf(String word, Set<Character> letters) {
        for (char c : word.toCharArray()) {
            // проверяем что в слове содержатся все уникальные символы
            if (!letters.contains(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param word введенный игроком вариант
     * @param answer ответ
     * @return строку, в которой закодирована подсказка
     */
    public static String getHint(String word, String answer) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < answer.length(); i++) {
            if (word.charAt(i) == answer.charAt(i)) { // проверяем соответствие буквы
                sb.append("+");
            } else if (answer.indexOf(word.charAt(i)) != -1) { // проверяем вхождение буквы в слово
                sb.append("^");
            } else {
                sb.append("-");
            }
        }

        return sb.toString();
    }

    /**
     * @param word введенный игроком вариант
     * @param answer ответ
     * @return маску, в которой описано буквы на какой позиции были угаданы
     */
    public static boolean[] analyzeGuessedLetters(String word, String answer) {
        boolean[] res = new boolean[WORD_LENGTH];

        for (int i = 0; i < answer.length(); i++) {
            res[i] = word.charAt(i) == answer.charAt(i);
        }

        return res;
    }

    /**
     * @param word введенный игроком вариант
     * @param answer ответ
     * @param mask описание позиций угаданных букв
     * @return флаг, означающий, что введенный вариант игрока совпадает с ответом в соответствии с маской
     */
    public static boolean matchesMask(String word, String answer, boolean[] mask) {
        for (int i = 0; i < mask.length; i++) {
            // проверяем угадана ли буква на этой позиции
            if (mask[i]) {
                // проверяем букву из возможного ответа с буквой из ответа
                if (word.charAt(i) != answer.charAt(i)) {
                    return false;
                }
            }
        }

        return true;
    }
}
