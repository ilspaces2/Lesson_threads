package ru.job4j.pool;

import org.junit.Test;

import static org.junit.Assert.*;

public class ParallelFindTest {

    @Test
    public void whenArrayInteger() {
        Integer[] array = new Integer[]{43, 234, 643, 754, 24, 1123, 54,
                33, 123, 7, 4, 56, 334, 2, 123, 6, 10, 5,
                55, 44, 11, 100, 101, 57, 1000};
        int actual = ParallelFind.find(array, 44);
        int expected = 19;
        assertEquals(expected, actual);
    }

    @Test
    public void whenArrayString() {
        String[] array = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
                "k", "l", "m", "n", "o", "p", "q", "r",
                "s", "t", "u", "v", "w", "x", "y", "z"};
        int actual = ParallelFind.find(array, "m");
        int expected = 12;
        assertEquals(expected, actual);
    }

    @Test
    public void whenArrayObjectUser() {
        User[] array = new User[100];
        for (int i = 0; i < array.length; i++) {
            array[i] = new User(i);
        }
        int actual = ParallelFind.find(array, new User(40));
        int expected = 40;
        assertEquals(expected, actual);
    }

    @Test
    public void whenArrayIntegerAndLineSearch() {
        Integer[] array = new Integer[]{43, 234, 643, 754, 24, 1123, 54, 33};
        int actual = ParallelFind.find(array, 24);
        int expected = 4;
        assertEquals(expected, actual);
    }

    @Test
    public void whenArrayStringAndIndexNotFound() {
        String[] array = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
                "k", "l", "m", "n", "o", "p", "q", "r",
                "s", "t", "u", "v", "w", "x", "y", "z"};
        int actual = ParallelFind.find(array, "Hello");
        int expected = -1;
        assertEquals(expected, actual);
    }
}