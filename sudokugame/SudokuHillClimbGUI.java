package sudokugame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SudokuHillClimbGUI extends JFrame {
    private final JTextField[][] fields = new JTextField[9][9];
    private SudokuBoard board;

    static int[][] sample = {
        {5,3,0,0,7,0,0,0,0},
        {6,0,0,1,9,5,0,0,0},
        {0,9,8,0,0,0,0,6,0},
        {8,0,0,0,6,0,0,0,3},
        {4,0,0,8,0,3,0,0,1},
        {7,0,0,0,2,0,0,0,6},
        {0,6,0,0,0,0,2,8,0},
        {0,0,0,4,1,9,0,0,5},
        {0,0,0,0,8,0,0,7,9}
    };

    public SudokuHillClimbGUI() {
        super("Sudoku Hill Climbing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(9,9));
        Font font = new Font("Monospaced", Font.BOLD, 18);
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                JTextField tf = new JTextField();
                tf.setHorizontalAlignment(JTextField.CENTER);
                tf.setFont(font);
                if (sample[r][c] != 0) {
                    tf.setText(String.valueOf(sample[r][c]));
                    tf.setEditable(false);
                    tf.setBackground(new Color(220,220,220));
                }
                fields[r][c] = tf;
                gridPanel.add(tf);
            }
        }
        add(gridPanel, BorderLayout.CENTER);

        JButton solveBtn = new JButton("Solve with Hill Climb");
        solveBtn.addActionListener(e -> solveSudoku());
        add(solveBtn, BorderLayout.SOUTH);

        setSize(400, 450);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void solveSudoku() {
        int[][] arr = new int[9][9];
        for (int r = 0; r < 9; r++) for (int c = 0; c < 9; c++) {
            String txt = fields[r][c].getText().trim();
            arr[r][c] = txt.isEmpty() ? 0 : Integer.parseInt(txt);
        }
        board = new SudokuBoard(arr);
        HillClimbingSolver solver = new HillClimbingSolver(board);
        boolean solved = solver.solve(200000);
        for (int r = 0; r < 9; r++) for (int c = 0; c < 9; c++) {
            fields[r][c].setText(String.valueOf(board.cells[r][c].value));
            if (!board.cells[r][c].fixed) fields[r][c].setBackground(new Color(200,255,200));
        }
        JOptionPane.showMessageDialog(this, solved ? "Solved!" : "Not fully solved. Conflicts=" + board.computeConflicts());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuHillClimbGUI::new);
    }
}
