package ru.job4j.pool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Параллельный поиск индекса в массиве по заданному значению.
 *
 * @param <T>
 */

public class ParallelFind<T> extends RecursiveTask<Integer> {
    private static final int SIZE_FOR_LINE_SEARCH = 10;
    private final T[] array;
    private final T value;
    private final int from;
    private final int to;

    public ParallelFind(T[] array, T value, int from, int to) {
        this.array = array;
        this.value = value;
        this.from = from;
        this.to = to;
    }

    /**
     * <p>
     * fork() - организует асинхронное выполнение новой задачи.
     * Это аналогично тому, что мы запустили бы рекурсивный метод еще раз.
     * <p>
     * join() - Этот метод ожидает завершения задачи и возвращает результат её выполнения,
     * но во время ожидания поток не блокируется, а может начать выполнение других задач.
     * <p>
     * Делим на подзадачи с помощью деления, когда условие if удовлетваряется
     * то идет поиск нужного индекса.
     *
     * @return Возвращаем максимальное число, это и будет наш индекс.
     * Если не найдем то возвращается -1.
     */
    @Override
    protected Integer compute() {
        if (to - from <= SIZE_FOR_LINE_SEARCH) {
            return lineSearch(array, value);
        }
        int mid = (from + to) / 2;
        ParallelFind<T> first = new ParallelFind<>(array, value, from, mid);
        ParallelFind<T> second = new ParallelFind<>(array, value, mid + 1, to);
        first.fork();
        second.fork();
        int firstIndex = first.join();
        int secondIndex = second.join();
        return Math.max(firstIndex, secondIndex);
    }

    private int lineSearch(T[] array, T value) {
        int index = -1;
        for (int i = 0; i < array.length; i++) {
            if (value.equals(array[i])) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * invoke() - запуск задачи
     *
     * @param array массив с данными
     * @param value значение для поиска
     * @return возвращает индекс для value
     */
    public Integer find(T[] array, T value) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return forkJoinPool.invoke(new ParallelFind<>(array, value, 0, array.length - 1));
    }
}
