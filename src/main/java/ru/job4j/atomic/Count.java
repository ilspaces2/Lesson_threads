package ru.job4j.atomic;

public class Count {
    private int value;
    private int valueTwo;

    /**
     * Метод является не атомарным. Тк операция инкремент сочетает в себе :
     * 1. чтение переменной.
     * 2. увеличение локальной оперенной на единицу.
     * 3. запись локальной переменной в общий ресурс.
     * <p> Может возникнуть ситуация, что обе нити выполнят чтение переменной одновременно.
     * В результате этого общий ресурс обновиться на единицу, а не на два.
     * <p> Атомарность - это свойство определяющее группу операций, которые выполняются неразрывно.
     * Операция инкремента счётчика не атомарна. Определить не атомарные операции просто.
     * Все операции, где данные зависят от начального состояния не атомарны.
     * Эти операции можно описать через процесс "проверить и выполнить".
     * <p> Чтобы добиться атомарности не атомарных операций в
     * Java используется механизм синхронизации (synchronized).
     */
    public void increment() {
        value++;
    }

    public int get() {
        return value;
    }

    /**
     * Добавили модификатор synchronized. Теперь одновременно с объектом может
     * работать только одна нить. Если две нити пробуют выполнить один и тот же синхронизированный метод,
     * то одна из нитей переходит в режим блокировки до тех пор пока первая нить не закончить работу с этим методом.
     * Синхронизация делает параллельную программу последовательной.
     */
    public synchronized void incrementTwo() {
        valueTwo++;
    }

    public synchronized int getTwo() {
        return valueTwo;
    }

    public static void main(String[] args) throws InterruptedException {
        Count count = new Count();
        /*
           Работа с не атомарной операцией.
        */
        Thread first = new Thread(count::increment);
        Thread second = new Thread(count::increment);
        first.start();
        second.start();
        first.join();
        second.join();
        System.out.println(count.get());

        /*
            Работа с не атомарной операцией но с синхронизацией
        */
        first = new Thread(count::incrementTwo);
        second = new Thread(count::incrementTwo);
        first.start();
        second.start();
        first.join();
        second.join();
        System.out.println(count.getTwo());
    }
}