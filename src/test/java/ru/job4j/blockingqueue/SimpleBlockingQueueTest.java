package ru.job4j.blockingqueue;

import org.junit.Test;

import java.util.Set;
import java.util.TreeSet;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class SimpleBlockingQueueTest {

    @Test
    public void whenOfferMoreCapacityThenWaitPoll2sec() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(2);
        Thread producer = new Thread(
                () -> {
                    queue.offer(1);
                    queue.offer(2);
                    queue.offer(3);
                }
        );
        Thread consumer = new Thread(
                () -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    queue.poll();
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
        Thread consumer = new Thread(queue::poll);
        Thread producer = new Thread(
                () -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    queue.offer(1);
                }
        );
        consumer.start();
        producer.start();
        consumer.join();
        producer.join();
        assertEquals(0, queue.getSize());
    }
}