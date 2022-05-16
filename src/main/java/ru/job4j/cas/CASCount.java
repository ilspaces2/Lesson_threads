package ru.job4j.cas;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public class CASCount {
    private final AtomicReference<Integer> count = new AtomicReference<>();

    public CASCount(int initialValue) {
        count.set(initialValue);
    }

    public void increment() {
        Integer current;
        Integer tmp;
        do {
            current = count.get();
            if (current == null) {
                throw new UnsupportedOperationException("Count is not impl.");
            }
            tmp = current;
            tmp++;
        }
        while (!count.compareAndSet(current, tmp));
    }

    public int get() {
        Integer value = count.get();
        if (value == null) {
            throw new UnsupportedOperationException("Count is not impl.");
        }
        return value;
    }
}
