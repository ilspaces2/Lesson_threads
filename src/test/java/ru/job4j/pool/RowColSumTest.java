package ru.job4j.pool;

import org.junit.Test;
import java.util.concurrent.ExecutionException;
import static org.junit.Assert.*;

public class RowColSumTest {

    private static final int[][] MATRIX = new int[][]{
            {1, 2, 3, 4, 5},
            {6, 7, 8, 9, 10},
            {11, 12, 13, 14, 15},
            {16, 17, 18, 19, 20},
            {21, 22, 23, 24, 25},
    };

    @Test
    public void whenSuccessivelySum() {
        RowColSum.Sums[] actual = RowColSum.sum(MATRIX);
        RowColSum.Sums[] expected = new RowColSum.Sums[]{
                new RowColSum.Sums(15, 55),
                new RowColSum.Sums(40, 60),
                new RowColSum.Sums(65, 65),
                new RowColSum.Sums(90, 70),
                new RowColSum.Sums(115, 75)
        };
        assertArrayEquals(expected, actual);
    }

    @Test
    public void whenAsyncSum() throws ExecutionException, InterruptedException {
        RowColSum.Sums[] actual = RowColSum.asyncSum(MATRIX);
        RowColSum.Sums[] expected = new RowColSum.Sums[]{
                new RowColSum.Sums(15, 55),
                new RowColSum.Sums(40, 60),
                new RowColSum.Sums(65, 65),
                new RowColSum.Sums(90, 70),
                new RowColSum.Sums(115, 75)
        };
        assertArrayEquals(expected, actual);
    }
}