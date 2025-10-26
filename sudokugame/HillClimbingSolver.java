package sudokugame;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class HillClimbingSolver {
	SudokuBoard board;
	Random rnd = ThreadLocalRandom.current();

	HillClimbingSolver(SudokuBoard b) {
		this.board = b;
	}

	boolean solve(int maxIters) {
		board.fillRowsRandomly();
		for (int i = 0; i < maxIters; i++) {
			if (board.isSolved())
				return true;
			int bestGain = 0;
			int[] bestMove = null;
			for (int r = 0; r < SudokuBoard.N; r++) {
				int[] mv = board.bestSwapInRow(r);
				if (mv != null && mv[3] > bestGain) {
					bestGain = mv[3];
					bestMove = mv;
				}
			}
			if (bestMove != null) {
				board.swap(bestMove[0], bestMove[1], bestMove[0], bestMove[2]);
			} else {
				// small random move
				int r = rnd.nextInt(SudokuBoard.N);
				java.util.List<Integer> idx = new ArrayList<>();
				for (int c = 0; c < SudokuBoard.N; c++)
					if (!board.cells[r][c].fixed)
						idx.add(c);
				if (idx.size() >= 2) {
					int a = idx.get(rnd.nextInt(idx.size()));
					int b = idx.get(rnd.nextInt(idx.size()));
					while (b == a)
						b = idx.get(rnd.nextInt(idx.size()));
					board.swap(r, a, r, b);
				}
			}
		}
		return board.isSolved();
	}
}
