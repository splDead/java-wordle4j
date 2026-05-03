package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.*;

import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {
    private static final String FILENAME = "words_ru.txt";

    public static final FileLogger systemLogger = new FileLogger("system.log");
    public static final FileLogger gameLogger = new FileLogger("game.log");

    public static void main(String[] args) {
        WordleGame game;

        // пробуем создать игру, ловим ошибку с пустым файлом словаря
        try {
            WordleDictionaryLoader dictionaryLoader = new WordleDictionaryLoader();
            WordleDictionary dictionary = dictionaryLoader.load(FILENAME);
            game = new WordleGame(dictionary);
        } catch (EmptyDictionaryException e) {
            systemLogger.log(e.getMessage());
            System.out.println("Что-то пошло не так и игра не смогла запуститься");
            return;
        }

        gameLogger.log("Игра готова");

        System.out.println("Добро пожаловать, я хочу сыграть с тобой в игру Wordle");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                String answer = scanner.nextLine();
                String hint;

                // проверяем пустой ввод для вывода подсказки
                if (answer.isBlank()) {
                    String possibleAnswer = game.getHint(); // получаем возможный вариант ответа
                    System.out.println(possibleAnswer);
                    hint = game.guess(possibleAnswer); // проверяем подошла ли подсказка
                } else {
                    hint = game.guess(answer); // проверяем угадал ли игрок
                }

                System.out.println(hint);

                // проверяем угадали ли слово
                if (game.isGuessed()) {
                    System.out.println("Поздравляем Вы угадали!");
                    break;
                }

                // проверяем оставшееся количество попыток
                if (game.getSteps() == 0) {
                    System.out.println("Вы проиграли, пу-пу-пу");
                    break;
                }
            } catch (NotFoundException e) {
                gameLogger.log(e.getMessage());
                System.out.println("Введенного слова нет в словаре, вот подсказка: " + game.getHint());
            } catch (InvalidCyrillicContentException e) {
                gameLogger.log(e.getMessage());
                System.out.println("Используйте кириллицу, вот подсказка: " + game.getHint());
            } catch (InvalidStringLengthException e) {
                gameLogger.log(e.getMessage());
                System.out.println("Загаданное слово состоит из " + WordleDictionary.WORD_LENGTH + ", вот подсказка: " + game.getHint());
            }
        }
    }
}
