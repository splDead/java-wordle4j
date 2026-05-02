package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.EmptyDictionaryException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    public WordleDictionary load(String fileName) throws EmptyDictionaryException {
        List<String> dictionary = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            while (br.ready()) {
                dictionary.add(br.readLine());
            }
        } catch (FileNotFoundException e) {
            Wordle.systemLogger.log("Файл не найден: " + fileName);
            for (StackTraceElement elem : e.getStackTrace()) {
                Wordle.systemLogger.log(elem.toString());
            }
        } catch (IOException e) {
            Wordle.systemLogger.log("Ошибка при чтении файла: " + e.getMessage());
        }

        if (dictionary.isEmpty()) {
            throw new EmptyDictionaryException("Файл словаря (" + fileName + ") пуст!");
        }

        return new WordleDictionary(dictionary);
    }
}
