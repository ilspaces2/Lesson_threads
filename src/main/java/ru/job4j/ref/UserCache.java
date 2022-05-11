package ru.job4j.ref;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UserCache {
    private final ConcurrentHashMap<Integer, User> users = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger();

    /*
    Если использовать метод в таком виде, то будет не потокобезопасно
    тк при изменении оригинала у нас в коллекции тоже объект изменится
    Например если другой поток начнет работать с юзером и его изменит,
    то результат может быть отличным от ожидаемого.
    public void add(User user) {users.put(id.incrementAndGet(), user);}

    В таком варианте мы добавляем копию объекта. И при изменении оригинала
    у нас коллекция не изменится
     */
    public void add(User user) {
        users.put(id.incrementAndGet(), User.of(user.getName()));
    }

    /*
    Возвращать нужно копию
     */
    public User findById(int id) {
        return User.of(users.get(id).getName());
    }

    public List<User> findAll() {
        return users.values()
                .stream()
                .map(user -> User.of(user.getName()))
                .toList();
    }
}