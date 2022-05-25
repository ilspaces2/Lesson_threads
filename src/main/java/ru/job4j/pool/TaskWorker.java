package ru.job4j.pool;

import ru.job4j.blockingqueue.SimpleBlockingQueue;

public class TaskWorker extends Thread {
    private final SimpleBlockingQueue<Runnable> tasks;

    public TaskWorker(SimpleBlockingQueue<Runnable> tasks) {
        this.tasks = tasks;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                tasks.poll().run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
