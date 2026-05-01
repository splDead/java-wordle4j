package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class FileLogger {
    private final String fileName;

    public FileLogger(String fileName) {
        this.fileName = fileName;
    }

    public void log(String message) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(LocalDateTime.now() + " : " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
