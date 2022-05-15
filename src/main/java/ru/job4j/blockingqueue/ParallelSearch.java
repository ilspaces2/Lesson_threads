package ru.job4j.blockingqueue;

/**
 * Прерываем потребителя, после того как он закончил работу
 */

public class ParallelSearch {

    public static void main(String[] args) throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(10);
        final Thread consumer = new Thread(
                () -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            System.out.println(queue.poll());
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        consumer.start();

        new Thread(
                () -> {
                    for (int index = 0; index != 3; index++) {
                        try {
                            queue.offer(index);
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    consumer.interrupt();
                }

        ).start();
    }
}
