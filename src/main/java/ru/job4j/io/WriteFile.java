package ru.job4j.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public final class WriteFile {

    private final File file;

    public WriteFile(File file) {
        this.file = file;
    }

    public void saveContent(String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
