package sudokugame;

/*
 * SudokuHillClimbGUI.java
 *
 * OOP Java Sudoku game + Hill Climbing solver + GUI using Swing
 * - Displays Sudoku grid with text fields (disabled for clues)
 * - Press "Solve" to run Hill Climbing and see the result
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class Cell {
    int row, col;
    int value;
    boolean fixed;

    Cell(int r, int c, int v, boolean fixed) {
        this.row = r; this.col = c; this.value = v; this.fixed = fixed;
    }
}

class SudokuBoard {
    static final int N = 9;
    Cell[][] cells = new Cell[N][N];

    SudokuBoard(int[][] initial) {
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                int v = initial[r][c];
                cells[r][c] = new Cell(r, c, v, v != 0);
            }
        }
    }

    void fillRowsRandomly() {
        Random rnd = ThreadLocalRandom.current();
        for (int r = 0; r < N; r++) {
            boolean[] present = new boolean[N + 1];
            for (int c = 0; c < N; c++) {
                int v = cells[r][c].value;
                if (v != 0) present[v] = true;
            }
            java.util.List<Integer> missing = new ArrayList<>();
            for (int v = 1; v <= N; v++) if (!present[v]) missing.add(v);
            Collections.shuffle(missing, rnd);
            int i = 0;
            for (int c = 0; c < N; c++) {
                if (cells[r][c].value == 0) cells[r][c].value = missing.get(i++);
            }
        }
    }

    int computeConflicts() {
        int conf = 0;
        // column conflicts
        for (int c = 0; c < N; c++) {
            int[] freq = new int[N + 1];
            for (int r = 0; r < N; r++) freq[cells[r][c].value]++;
            for (int v = 1; v <= N; v++) if (freq[v] > 1) conf += freq[v] - 1;
        }
        // box conflicts
        for (int br = 0; br < 3; br++) for (int bc = 0; bc < 3; bc++) {
            int[] freq = new int[N + 1];
            for (int r = br * 3; r < br * 3 + 3; r++) for (int c = bc * 3; c < bc * 3 + 3; c++) freq[cells[r][c].value]++;
            for (int v = 1; v <= N; v++) if (freq[v] > 1) conf += freq[v] - 1;
        }
        return conf;
    }

    void swap(int r1, int c1, int r2, int c2) {
        int t = cells[r1][c1].value;
        cells[r1][c1].value = cells[r2][c2].value;
        cells[r2][c2].value = t;
    }

    int[] bestSwapInRow(int r) {
        java.util.List<Integer> idx = new ArrayList<>();
        for (int c = 0; c < N; c++) if (!cells[r][c].fixed) idx.add(c);
        if (idx.size() < 2) return null;
        int base = computeConflicts();
        int bestDelta = 0; int bestC1 = -1, bestC2 = -1;
        for (int i = 0; i < idx.size(); i++) for (int j = i + 1; j < idx.size(); j++) {
            int c1 = idx.get(i), c2 = idx.get(j);
            swap(r, c1, r, c2);
            int delta = base - computeConflicts();
            if (delta > bestDelta) {
                bestDelta = delta; bestC1 = c1; bestC2 = c2;
            }
            swap(r, c1, r, c2);
        }
        if (bestDelta > 0) return new int[]{r, bestC1, bestC2, bestDelta};
        return null;
    }

    boolean isSolved() { return computeConflicts() == 0; }
}

class HillClimbingSolver {
    SudokuBoard board;
    Random rnd = ThreadLocalRandom.current();

    HillClimbingSolver(SudokuBoard b) { this.board = b; }

    boolean solve(int maxIters) {
        board.fillRowsRandomly();
        for (int i = 0; i < maxIters; i++) {
            if (board.isSolved()) return true;
            int bestGain = 0; int[] bestMove = null;
            for (int r = 0; r < SudokuBoard.N; r++) {
                int[] mv = board.bestSwapInRow(r);
                if (mv != null && mv[3] > bestGain) { bestGain = mv[3]; bestMove = mv; }
            }
            if (bestMove != null) {
                board.swap(bestMove[0], bestMove[1], bestMove[0], bestMove[2]);
            } else {
                // small random move
                int r = rnd.nextInt(SudokuBoard.N);
                java.util.List<Integer> idx = new ArrayList<>();
                for (int c = 0; c < SudokuBoard.N; c++) if (!board.cells[r][c].fixed) idx.add(c);
                if (idx.size() >= 2) {
                    int a = idx.get(rnd.nextInt(idx.size()));
                    int b = idx.get(rnd.nextInt(idx.size()));
                    while (b == a) b = idx.get(rnd.nextInt(idx.size()));
                    board.swap(r, a, r, b);
                }
            }
        }
        return board.isSolved();
    }
}

