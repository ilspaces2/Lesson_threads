package ru.job4j.cache;

import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class CacheTest {

    @Test
    public void whenAddToCache() {
        Cache cache = new Cache();
        Base user1 = new Base(1, 1);
        Base user2 = new Base(2, 1);
        Base user3 = new Base(3, 1);
        user1.setName("1");
        user2.setName("2");
        user3.setName("3");
        cache.add(user1);
        cache.add(user2);
        cache.add(user3);
        assertThat(Map.of(1, user1, 2, user2, 3, user3), is(cache.getMap()));
    }

    @Test
    public void whenUpdateCache() {
        Cache cache = new Cache();
        Base user1 = new Base(1, 1);
        Base user2 = new Base(1, 1);
        user1.setName("old name");
        cache.add(user1);
        user2.setName("new name");
        cache.update(user2);
        assertEquals(2, cache.getMap().get(1).getVersion());
        assertThat(user2.getName(), is(cache.getMap().get(1).getName()));
    }

    @Test
    public void whenDeleteCache() {
        Cache cache = new Cache();
        Base user1 = new Base(1, 1);
        user1.setName("1");
        cache.add(user1);
        cache.delete(user1);
        assertTrue(cache.getMap().isEmpty());
    }

    @Test(expected = OptimisticException.class)
    public void whenUpdateCacheAndDifferentVersions() {
        Cache cache = new Cache();
        Base user1 = new Base(1, 1);
        Base user2 = new Base(1, 2);
        cache.add(user1);
        cache.update(user2);
    }

    @Test
    public void whenAddThenTrue() {
        Cache cache = new Cache();
        assertTrue(cache.add(new Base(0, 0)));
    }

    @Test
    public void whenUpdateThenTrue() {
        Cache cache = new Cache();
        cache.add(new Base(0, 0));
        assertTrue(cache.update(new Base(0, 0)));
    }
}