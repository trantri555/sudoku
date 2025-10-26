package sudokugame;

import javax.swing.SwingUtilities;

public class run {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(SudokuHillClimbGUI::new);
	}
}
