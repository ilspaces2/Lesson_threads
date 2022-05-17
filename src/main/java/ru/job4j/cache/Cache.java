package ru.job4j.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    public boolean add(Base model) {
        return memory.putIfAbsent(model.getId(), model) == null;
    }

    public boolean update(Base model) {
        return memory.computeIfPresent(model.getId(),
                (key, value) -> {
                    Base stored = memory.get(key);
                    if (stored.getVersion() != model.getVersion()) {
                        throw new OptimisticException("Versions are not equal");
                    }
                    stored = new Base(key, model.getVersion() + 1);
                    stored.setName(model.getName());
                    return stored;
                }) != null;
    }

    public void delete(Base model) {
        memory.remove(model.getId());
    }

    public Map<Integer, Base> getMap() {
        return Map.copyOf(memory);
    }
}