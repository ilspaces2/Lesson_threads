package ru.job4j.cas;

import org.junit.Test;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.Assert.assertEquals;

public class CASCountTest {

    @Test
    public void whenCount() throws InterruptedException {
        CASCount count = new CASCount(0);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int limit = 1000;
        Thread first = new Thread(() -> {
            while (atomicInteger.get() <= limit) {
                count.increment();
                atomicInteger.incrementAndGet();
            }
        });
        Thread second = new Thread(() -> {
            while (atomicInteger.get() <= limit) {
                count.increment();
                atomicInteger.incrementAndGet();
            }
        });
        first.start();
        second.start();
        first.join();
        second.join();
        assertEquals(atomicInteger.get(), count.get());
    }
}