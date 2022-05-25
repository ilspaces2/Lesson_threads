package ru.job4j.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Синхронность – это когда действия выполняются последовательно.
 * К примеру, вы сталкивались со словом synchronized, что значит,
 * в данном месте потоки выполняют последовательно, а не одновременно.
 *
 * <p>
 * Асинхронность – это наоборот, когда действие выполняется отдельно от другого действия
 *
 * <p>
 * Асинхронность хорошо подходит, когда основной поток сильно загружен,
 * а нам нужно выполнить что-то отдельно. В этом случае удобно использовать асинхронность,
 * при этом не требуется управление потоками напрямую
 *
 * <p>
 * Для написания асинхронного кода в Java существует замечательный класс CompletableFuture
 * Ниже примеры некоторых методов.
 *
 * <p>
 * Для получения результата, нужно использовать метод get(). Для объекта, созданного через supplyAsync(),
 * этот метод вернет T; для объекта, созданного через runAsync() - null. Стоит обратить внимание,
 * что этот метод является блокирующим, т.е. блокирует поток выполнения, в котором он будет вызван.
 */

public class ComletableFuture {

    private static void iWork() throws InterruptedException {
        int count = 0;
        while (count < 10) {
            System.out.println("Вы: Я работаю");
            TimeUnit.SECONDS.sleep(1);
            count++;
        }
    }

    /**
     * runAsync - просто выполняет задачу
     */
    public static CompletableFuture<Void> goToTrash() {
        return CompletableFuture.runAsync(
                () -> {
                    System.out.println("Сын: Мам/Пам, я пошел выносить мусор");
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Сын: Мам/Пап, я вернулся!");
                }
        );
    }

    public static CompletableFuture<Void> washHands(String name) {
        return CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(name + ", моет руки");
        });
    }

    /**
     * supplyAsync - выполняет задачу и при этом возвращает результат <Тип>
     */
    public static CompletableFuture<String> buyProduct(String product) {
        return CompletableFuture.supplyAsync(
                () -> {
                    System.out.println("Сын: Мам/Пам, я пошел в магазин");
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Сын: Мам/Пап, я купил " + product);
                    return product;
                }
        );
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String product = "Хлеб";

        System.out.println("===========================thenRun()=====================================");
        /**
         после завершения выполняем следующее действие
         */
        goToTrash().thenRun(() -> System.out.println("thenRun следующее действие"));


        System.out.println("============================thenApply()====================================");
        /**
         имеем доступ к результату, но чтобы в итоге получить результат
         необходимо вызвать метод гет s.get()
         */
        CompletableFuture<String> s = buyProduct(product)
                .thenApply(
                        (rzl) -> "thenApply получаем результат но нужно вызвать гет " + rzl);
        System.out.println(s.get());


        System.out.println("==========================thenAccept()======================================");
        /**
         здесь тоже имеем доступ к результату, и можем его получить сразу
         */
        buyProduct(product).thenAccept(
                (rzl) -> System.out.println(" thenAccept Получили результат и вывели " + rzl));


        System.out.println("=========================thenCompose()=======================================");
        /**
         если действия зависимы, сначало идем за сыром а потом мусор
         */
        buyProduct("СЫР").thenCompose(a -> goToTrash());


        System.out.println("===========================thenCombine()=====================================");
        /**
         если действия не зависимы и можно сделать их одновременно.
         для получения результата вызвать гет
         */
        CompletableFuture<String> s1 = buyProduct("Meet").thenCombine(buyProduct("Water"),
                (r1, r2) -> "Купили " + r1 + " и " + r2);
        iWork();
        System.out.println(s1.get());


        System.out.println("============================allOf()====================================");
        /**
         allOf- выполняет сразу несколько задач
         */
        CompletableFuture.allOf(
                washHands("1"), washHands("2"),
                washHands("3"), washHands("4")
        );


        System.out.println("===========================anyOf()=====================================");
        /**
         anyOf- выполняет сразу несколько задач при возвращает
         результат первой завершившийся задачи
         */
        CompletableFuture<Object> s2 = CompletableFuture.anyOf(
                washHands("1"), washHands("2"),
                washHands("3"), washHands("4")
        );
        TimeUnit.SECONDS.sleep(1);
        System.out.println(s2.get());
    }
}


