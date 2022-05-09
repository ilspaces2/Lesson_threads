package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {

    @Override
    public void run() {
        char[] process = new char[]{'\\', '|', '/'};
        int count = 0;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (count == process.length) {
                    count = 0;
                }
                System.out.print("\rLoading ... " + process[count++]);
                Thread.sleep(500);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        Thread.sleep(5000);
        progress.interrupt();
    }
}
