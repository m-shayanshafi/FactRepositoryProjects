package checkersPlayer;

import checkersMain.CheckersBoard;
import checkersMain.CheckersPlayerInterface;

public class NaqshAndKarenAI implements CheckersPlayerInterface {

	@Override
	public int choosePlyIndex(CheckersPlayerEvent cpe) {
		double currentBest = -500;
		int bestBoard = 0;
		int numSuccessors = cpe.board.getNumSuccessors();
		if (numSuccessors == 1)
			return 0;
		for (int i = 0; i < numSuccessors; i++) {
			double value = evaluateBoard(cpe.board.getSuccessor(i));
			if (value >= currentBest) {
				bestBoard = i;
				currentBest = value;
			}
			// System.out.println("DONE EVALBOARD " +i+ " TIMES");
		}
		// System.out.println("IT FINISHED EVALBOARD");
		return bestBoard;

	}

	protected double evaluateBoard(CheckersBoard theBoard) {
		double eval = 0;
		int l = 0;
		int highest;
		int lowest;

		while ((l < 10)) {
			int numSuccessors = theBoard.getNumSuccessors();
			if (numSuccessors == 0)
				continue;

			int a;
			highest = 0;
			lowest = 0;
			double friendlyVal = helper(theBoard.getSuccessor(0), eval, l,
					theBoard);
			double enemyVal = helper(theBoard.getSuccessor(0), eval, l,
					theBoard);
			for (a = 0; a < numSuccessors; a++) {
				if (((l + 2) % 2 == 0)
						&& (helper(theBoard.getSuccessor(a), eval, l, theBoard) >= friendlyVal)) {
					highest = a; // System.out.println("high L is " +l);
					friendlyVal = helper(theBoard.getSuccessor(a), eval, l,
							theBoard);
				} else if (((l + 2) % 2 == 1)
						&& (helper(theBoard.getSuccessor(a), eval, l, theBoard) >= enemyVal)) {
					lowest = a; // System.out.println("low L is " +l);
					enemyVal = helper(theBoard.getSuccessor(a), eval, l,
							theBoard);
				}
			}

			if ((l + 2) % 2 == 0) {
				eval += helper(theBoard.getSuccessor(highest), eval, l,
						theBoard);
				theBoard = theBoard.getSuccessor(highest);
				if (theBoard.invertCheckersBoard().getNumPlies() <= (l / 2))
					return eval;
				// System.out.println("IT's INVERTED ETC (highest)");
			} else if ((l + 2) % 2 == 1) {
				eval -= helper(theBoard.getSuccessor(lowest), eval, l, theBoard);
				theBoard = theBoard.getSuccessor(lowest);
				if (theBoard.invertCheckersBoard().getNumPlies() <= (l / 2))
					return eval;
				// System.out.println("IT's INVERTED ETC (lowest)");
			}
			l += 1;
			// System.out.println("INCREMENTED L " +l);
		} // argh.

		return eval;
	}

	@Override
	public void gameEnded(CheckersPlayerEvent cpe) {
	}

	@Override
	public void gameStarted(CheckersPlayerEvent cpe) {
	}

	@Override
	public String getDescription() {
		return "A fast and simple AI."
				+ "\n\nAuthors: Naqsh Haque and Karen Wright"
				+ "\nVersion: 1.00 - 10 July 2008";
	}

	@Override
	public String getName() {
		return "Naqsh&Karen AI";
	}

	private double helper(CheckersBoard theBoard, double eval, int l,
			CheckersBoard initialBoard) {
		int numPieces = 0, numIPieces = 0, numKings = 0, numIKings = 0, numEvil = 0, numIEvil = 0, numIEvilKings = 0, numEvilKings = 0;
		for (int row = 0; row < 7; row++) {
			for (int col = 0; col < 7; col++) {
				if ((l + 2) % 2 == 0) {
					if (theBoard.getPiece(row, col) == 2) {
						eval += 1;
						numPieces += 1;
					} else if (theBoard.getPiece(row, col) == 3) {
						eval += 1.7;
						numPieces += 1;
						numKings += 1;
					} else if (theBoard.getPiece(row, col) == 4) {
						eval -= 1;
						numEvil += 1;
					} else if (theBoard.getPiece(row, col) == 5) {
						eval -= 2;
						numEvil += 1;
						numEvilKings += 1;
					}
				} else if ((l + 2) % 2 == 1) {
					if (theBoard.getPiece(row, col) == 2) {
						eval -= 1;
						numEvil += 1;
					} else if (theBoard.getPiece(row, col) == 3) {
						eval -= 1.7;
						numEvil += 1;
						numEvilKings += 1;
					} else if (theBoard.getPiece(row, col) == 4) {
						eval += 1;
						numPieces += 1;
					} else if (theBoard.getPiece(row, col) == 5) {
						eval += 2;
						numPieces += 1;
						numKings += 1;
					}
				}

				if (initialBoard.getPiece(row, col) == 2) {
					numIPieces += 1;
				} else if (initialBoard.getPiece(row, col) == 3) {
					numIPieces += 1;
					numIKings += 1;
				} else if (initialBoard.getPiece(row, col) == 4) {
					numIEvil += 1;
				} else if (initialBoard.getPiece(row, col) == 5) {
					numIEvil += 1;
					numIEvilKings += 1;
				}

			}

		}
		if (numPieces < numIPieces)
			eval -= 30;
		if (numKings < numIKings)
			eval -= 40;
		if (numEvil < numIEvil)
			eval += 20;
		if (numEvilKings < numIEvilKings)
			eval += 25;
		// System.out.println("EVAL IS " +eval);
		return eval;

	}

	@Override
	public void remainingTimeChanged(CheckersPlayerEvent cpe) {
	}
}
