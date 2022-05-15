package ru.job4j.blockingqueue;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

/**
 * <p>
 * Реализуйте шаблон Producer Consumer.
 * Для этого вам необходимо реализовать собственную версию bounded blocking queue.
 * Это блокирующая очередь, ограниченная по размеру. В данном шаблоне Producer помещает данные в очередь,
 * а Consumer извлекает данные из очереди.
 * <p>
 * Если очередь заполнена полностью, то при попытке добавления поток Producer блокируется,
 * до тех пор пока Consumer не извлечет очередные данные, т.е. в очереди появится свободное место.
 * И наоборот если очередь пуста поток Consumer блокируется, до тех пор пока Producer
 * не поместит в очередь данные.
 *
 * @param <T>
 */

@ThreadSafe
public class SimpleBlockingQueue<T> {

    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();
    private final int capacity;

    public SimpleBlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void offer(T value) throws InterruptedException {
        while (capacity == queue.size()) {
            wait();
        }
        queue.offer(value);
        notify();
    }

    public synchronized T poll() throws InterruptedException {
        while (queue.size() == 0) {
            wait();
        }
        notify();
        return queue.poll();
    }

    public synchronized int getSize() {
        return queue.size();
    }
}
