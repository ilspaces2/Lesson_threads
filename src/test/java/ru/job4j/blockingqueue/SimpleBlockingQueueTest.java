package ru.job4j.blockingqueue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class SimpleBlockingQueueTest {

    @Test
    public void whenOfferMoreCapacityThenWaitPoll2sec() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(2);
        Thread producer = new Thread(
                () -> {
                    try {
                        queue.offer(1);
                        queue.offer(2);
                        queue.offer(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
        Thread consumer = new Thread(
                () -> {
                    try {
                        Thread.sleep(2000);
                        queue.poll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        Set<Integer> set = new TreeSet<>();
        set.add(queue.poll());
        set.add(queue.poll());
        assertThat(set, is(Set.of(2, 3)));
    }

    @Test
    public void whenQueueEmptyUsePollThenWaitOffer2sec() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(2);
        Thread consumer = new Thread(
                () -> {
                    try {
                        Thread.sleep(2000);
                        queue.poll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
        Thread producer = new Thread(
                () -> {
                    try {
                        Thread.sleep(2000);
                        queue.offer(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
        consumer.start();
        producer.start();
        consumer.join();
        producer.join();
        assertEquals(0, queue.getSize());
    }

    /**
     * <p>
     * Полноценный тест на блокирующую очередь
     * <p>
     * Зачем нужна двойная проверка.(queue.getSize() != 0 || !Thread.currentThread().isInterrupted())
     * Если производитель закончил свою работу и сразу подаст сигнал об отключении потребителя,
     * то мы не сможем прочитать все данные, а может и успеем.
     * С другой стороны, если мы успели прочитать все данные и находимся в режиме wait пришедший
     * сигнал запустит нить и проверит состояние очереди и завершит цикл. Потребитель закончит свою работу.
     *
     * @throws InterruptedException
     */

    @Test
    public void whenFetchAllThenGetIt() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(10);
        Thread producer = new Thread(
                () -> {
                    IntStream.range(0, 5).forEach(el -> {
                                try {
                                    queue.offer(el);
                                } catch (InterruptedException err) {
                                    err.printStackTrace();
                                }
                            }
                    );
                }
        );
        producer.start();
        Thread consumer = new Thread(
                () -> {
                    while (queue.getSize() != 0 || !Thread.currentThread().isInterrupted()) {
                        try {
                            buffer.add(queue.poll());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        consumer.start();
        producer.join();
        consumer.interrupt();
        consumer.join();
        assertThat(buffer, is(Arrays.asList(0, 1, 2, 3, 4)));
    }
}