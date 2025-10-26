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

public class Cell {
	int row, col;
	int value;
	boolean fixed;

	Cell(int r, int c, int v, boolean fixed) {
		this.row = r;
		this.col = c;
		this.value = v;
		this.fixed = fixed;
	}
}

