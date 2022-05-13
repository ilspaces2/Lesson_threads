package ru.job4j.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.function.Predicate;

public final class ParseFile {
    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    public String getContent() {
        return readFile(el -> true);
    }

    public String getContentWithoutUnicode() {
        return readFile(el -> el < 0x80);
    }

    private String readFile(Predicate<Character> filter) {
        StringBuilder output = new StringBuilder();
        try (BufferedReader input = new BufferedReader(new FileReader(file))) {
            int data;
            while ((data = input.read()) > 0) {
                if (filter.test((char) data)) {
                    output.append((char) data);
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return output.toString();
    }
}