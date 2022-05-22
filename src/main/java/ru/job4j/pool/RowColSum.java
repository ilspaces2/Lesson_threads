package ru.job4j.pool;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Класс для подсчета сумы рядов и колонок в квадратной матрице
 */

public class RowColSum {
    public static class Sums {
        private final int rowSum;
        private final int colSum;

        public Sums(int rowSum, int colSum) {
            this.rowSum = rowSum;
            this.colSum = colSum;
        }

        public int getRowSum() {
            return rowSum;
        }

        public int getColSum() {
            return colSum;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Sums sums = (Sums) o;
            return rowSum == sums.rowSum && colSum == sums.colSum;
        }

        @Override
        public int hashCode() {
            return Objects.hash(rowSum, colSum);
        }
    }

    /**
     * Последовательный подсчет сумм
     */
    public static Sums[] sum(int[][] matrix) {
        Sums[] sums = new Sums[matrix.length];
        for (int row = 0; row < matrix.length; row++) {
            int sumRow = 0, sumColumn = 0;
            for (int column = 0; column < matrix[row].length; column++) {
                sumRow += matrix[row][column];
                sumColumn += matrix[column][row];
            }
            sums[row] = new Sums(sumRow, sumColumn);
        }
        return sums;
    }

    /**
     * Асинхронное выполнение задач подсчета сумм
     */
    public static Sums[] asyncSum(int[][] matrix) throws ExecutionException, InterruptedException {
        Sums[] sums = new Sums[matrix.length];
        ArrayList<CompletableFuture<Sums>> tasks = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            tasks.add(getSumTask(matrix, i));
        }
        for (int i = 0; i < matrix.length; i++) {
            sums[i] = tasks.get(i).get();
        }
        return sums;
    }

    /**
     * Асинхронная задача полсчета сумм
     */
    private static CompletableFuture<Sums> getSumTask(int[][] matrix, int index) {
        return CompletableFuture.supplyAsync(
                () -> {
                    int sumRow = 0;
                    int sumColumn = 0;
                    for (int i = 0; i < matrix.length; i++) {
                        sumRow += matrix[index][i];
                        sumColumn += matrix[i][index];
                    }
                    return new Sums(sumRow, sumColumn);
                }
        );
    }
}