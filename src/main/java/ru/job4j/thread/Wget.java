package ru.job4j.thread;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Класс скачивает файлы по ссылке url с заданным ограничением по скорости speed.
 * Скорость указываем в байтах.
 */

public class Wget implements Runnable {
    private static final int DELAY_MS = 1000;
    private static final int BUFFER_BYTE = 1024;
    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    /**
     * readBytes - прочитанные байты.
     * writeBytes -записанные байты(суммируются в цикле). Если размер превышает ограничение по скорости,
     * то проверяем время и если оно меньше DELAY_MS то усыпляем поток на оставшиеся время чтобы
     * в общей сложности задержка получилась равной DELAY_MS.
     * (например потратили на операцию 200мс и нужно добавить еще 800мс чтобы в сумме получилось 1000мс)
      */
    @Override
    public void run() {
        try (BufferedInputStream input = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream output = new FileOutputStream(
                     (Paths.get(new URL(url).getPath()).getFileName().toString()))) {
            byte[] buffer = new byte[BUFFER_BYTE];
            int readBytes;
            int writeBytes = 0;
            long timeStart = System.currentTimeMillis();
            while ((readBytes = input.read(buffer, 0, BUFFER_BYTE)) != -1) {
                output.write(buffer, 0, readBytes);
                writeBytes += readBytes;
                if (writeBytes >= speed) {
                    long timeEnd = System.currentTimeMillis() - timeStart;
                    if (timeEnd < DELAY_MS) {
                        Thread.sleep(DELAY_MS - timeEnd);
                    }
                    writeBytes = 0;
                    timeStart = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void checkArgs(String... args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Arguments should be 2");
        } else if (args[0].isBlank() || !args[0].startsWith("https://")) {
            throw new IllegalArgumentException("Wrong URL");
        } else if (Integer.parseInt(args[1]) < 0) {
            throw new IllegalArgumentException("Speed should be more zero");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        checkArgs(args);
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }
}