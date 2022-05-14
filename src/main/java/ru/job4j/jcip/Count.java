package ru.job4j.jcip;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * <p> Аннотации из библиотеки jcip-annotations.
 * В этой библиотеке есть аннотации, которыми мы описываем класс.
 * Это нужно делать, чтобы облегчить понимание, где общие ресурсы и как мы их синхронизируем.
 * САМА БИБЛИОТЕКА СИНХРОНИЗАЦИЮ НЕ ОБЕСПЕЧИВАЕТ.
 * Она только информирует программиста, о том что у нас есть общие ресурсы и нам нужно аккуратно с ними работать.
 * Запомните, что аннотации jcip - это как JavaDoc - он нужен, только чтобы описать класс,
 * который будет работать в многопоточной среде. Синхронизацию эти аннотации не добавляют.
 * <p> @ThreadSafe -
 * Говорит пользователям данного класса, что класс можно использовать
 * в многопоточном режиме и он будет работать правильно.
 * <p> @GuardedBy("this") -
 * Выставляется над общим ресурсом. Аннотация имеет входящий параметр.
 * Он указывает на объект монитора, по которому мы будем синхронизироваться.
 */

@ThreadSafe
public class Count {
    @GuardedBy("this")
    private int value;

    public synchronized void increment() {
        this.value++;
    }

    public synchronized int get() {
        return this.value;
    }
}