package checkersPlayer;

import checkersMain.CheckersBoard;
import checkersMain.CheckersPlayerInterface;

public class EFCheckersPlayer implements CheckersPlayerInterface {

	protected double myCheckerValue = 1.0;
	protected double myKingValue = 1.5;
	// public static int turncount = 0;
	int number = 0;

	/**
	 * See CheckersPlayer.java for a detailed description of takeTurn. Note:
	 * "@Override" confirms that takeTurn is supposed to be an implementation of
	 * an abstract interface method.
	 */
	@Override
	public int choosePlyIndex(CheckersPlayerEvent cpe) {
		double BestHeurPly = Integer.MIN_VALUE; // empty
		double BestHeurPly2 = Integer.MIN_VALUE; // my move
		double BestHeurPly3 = Integer.MIN_VALUE; // opponents move
		double BestHeurPly4 = Integer.MIN_VALUE; // my move
		double BestHeurPly5 = Integer.MIN_VALUE; // my opponents move
		double BestHeurPly6 = Integer.MIN_VALUE; // my move
		double BestHeurPly7 = Integer.MIN_VALUE; // my opponents move
		// double currheuristic1 = 0;
		double currheuristic2 = 0;
		double currheuristic3 = 0;
		double currheuristic4 = 0;
		double currheuristic5 = 0;
		double currheuristic6 = 0;
		double currheuristic7 = 0;

		double totalHeuristicValue = Integer.MIN_VALUE;
		int maxHeuristicIndex = -1;

		for (int i = 0; i < cpe.board.getNumSuccessors(); i++) {

			CheckersBoard board2 = cpe.board.getSuccessor(i);
			currheuristic2 = evaluateBoard(board2, cpe.moveCount);
			if (currheuristic2 > BestHeurPly2)
				BestHeurPly2 = currheuristic2;
			board2 = board2.invertCheckersBoard();

			for (int j = 0; j < board2.getNumSuccessors(); j++) {

				CheckersBoard board3 = board2.getSuccessor(j);
				currheuristic3 = evaluateOppBoard(board3);
				if (currheuristic3 > BestHeurPly3)
					BestHeurPly3 = currheuristic3;
				board3 = board3.invertCheckersBoard();

				for (int k = 0; k < board3.getNumSuccessors(); k++) {
					CheckersBoard board4 = board3.getSuccessor(k);
					currheuristic4 = evaluateBoard(board4, cpe.moveCount);
					if (currheuristic4 > BestHeurPly4) {
						BestHeurPly4 = currheuristic4;
					}
					board4 = board4.invertCheckersBoard();

					for (int l = 0; l < board4.getNumSuccessors(); l++) {
						CheckersBoard board5 = board4.getSuccessor(l);
						currheuristic5 = evaluateOppBoard(board5);
						if (currheuristic5 > BestHeurPly5) {
							BestHeurPly5 = currheuristic5;
						}
						board5 = board5.invertCheckersBoard();

						for (int m = 0; m < board5.getNumSuccessors(); m++) {
							CheckersBoard board6 = board5.getSuccessor(m);
							currheuristic6 = evaluateBoard(board6,
									cpe.moveCount);
							if (currheuristic6 > BestHeurPly6) {
								BestHeurPly6 = currheuristic6;
							}
							board6 = board6.invertCheckersBoard();

							for (int n = 0; n < board6.getNumSuccessors(); n++) {
								currheuristic7 = evaluateOppBoard(board6
										.getSuccessor(n));
								if (currheuristic7 > BestHeurPly7) {
									BestHeurPly7 = currheuristic7;
								}
							}
						}
					}
				}
			}
			BestHeurPly = BestHeurPly2 - BestHeurPly3 + BestHeurPly4
					- BestHeurPly5 + BestHeurPly6 - BestHeurPly7;
			if (BestHeurPly > totalHeuristicValue) {
				totalHeuristicValue = BestHeurPly;
				maxHeuristicIndex = i;
				if (BestHeurPly < totalHeuristicValue) {
					number++;
					if (number > 60) {
						if (maxHeuristicIndex == -1) {
							maxHeuristicIndex = 1;
							break;
						} else {
							maxHeuristicIndex = i;
							break;
						}
					}
				}
			}

			// BestHeurPly =
			// BestHeurPly2-BestHeurPly3+BestHeurPly4-BestHeurPly5+
			// BestHeurPly6-BestHeurPly7;
			/*
			 * if (BestHeurPly >= totalHeuristicValue){ totalHeuristicValue =
			 * BestHeurPly; maxHeuristicIndex = i; }
			 */

		}

		// return the highest-heuristic successor
		number = 0;
		return maxHeuristicIndex;
	}

	protected double evaluateBoard(CheckersBoard theBoard, int turnCount) {
		double heuristicValue = 0;
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				byte contentsOfBoardSpace = theBoard.getPiece(row, col);
				switch (contentsOfBoardSpace) {

				case CheckersBoard.PLAYER1_CHECKER:
					heuristicValue += myCheckerValue;
					break;

				case CheckersBoard.PLAYER1_KING:
					heuristicValue += myKingValue;
					break;

				case CheckersBoard.PLAYER2_CHECKER:
					heuristicValue -= myCheckerValue;
					break;

				case CheckersBoard.PLAYER2_KING:
					heuristicValue -= myKingValue;
					break;

				case CheckersBoard.OFFBOARD: // ignore the off-diagonal spaces
				case CheckersBoard.EMPTY: // ignore the empty spaces
					break;
				}
			}
		}
		if (turnCount == 0) {
			byte BoardSpace1 = theBoard.getPiece(4, 7);
			switch (BoardSpace1) {
			case CheckersBoard.PLAYER1_CHECKER:
				heuristicValue += myCheckerValue * 5000;
				break;
			}
			byte BoardSpace2 = theBoard.getPiece(4, 3);
			switch (BoardSpace2) {
			case CheckersBoard.PLAYER1_CHECKER:
				heuristicValue += myCheckerValue * 5000;
				break;
			}
		}
		if (turnCount == 1) {
			byte BoardSpace1 = theBoard.getPiece(4, 7);
			switch (BoardSpace1) {
			case CheckersBoard.PLAYER1_CHECKER:
				heuristicValue += myCheckerValue * 5000;
				break;
			}
			byte BoardSpace2 = theBoard.getPiece(4, 3);
			switch (BoardSpace2) {
			case CheckersBoard.PLAYER1_CHECKER:
				heuristicValue += myCheckerValue * 5000;
				break;
			}
		}
		if (turnCount == 2) {
			byte BoardSpace1 = theBoard.getPiece(4, 7);
			switch (BoardSpace1) {
			case CheckersBoard.PLAYER1_CHECKER:
				heuristicValue += myCheckerValue * 5000;
				break;
			}
			byte BoardSpace2 = theBoard.getPiece(4, 3);
			switch (BoardSpace2) {
			case CheckersBoard.PLAYER1_CHECKER:
				heuristicValue += myCheckerValue * 5000;
				break;
			/*
			 * byte BoardSpace3 = theBoard.getSpaceRectangularPos(5,4);
			 * switch(BoardSpace3) { case CheckersBoard.PLAYER2_CHECKER:
			 * heuristicValue -= myCheckerValue6000; break; }
			 */
			}
		}
		double Center = getCenter(theBoard, turnCount);
		if (turnCount > 40) {

			if (heuristicValue < -1) {
				byte BoardSpace1 = theBoard.getPiece(0);
				switch (BoardSpace1) {
				case CheckersBoard.PLAYER1_KING:
					heuristicValue += myKingValue * 5000;
					break;
				}
			}
			if (heuristicValue > 1) {
				{
					int i;
					for (i = 0; i < 32; i++) {
						byte BoardSpace1 = theBoard.getPiece(i);
						switch (BoardSpace1) {
						case CheckersBoard.PLAYER1_CHECKER:
							heuristicValue += myCheckerValue;
							break;

						case CheckersBoard.PLAYER1_KING:
							heuristicValue += myKingValue;
							break;

						case CheckersBoard.PLAYER2_CHECKER:
							heuristicValue -= myCheckerValue * 2;
							break;

						case CheckersBoard.PLAYER2_KING:
							heuristicValue -= myKingValue * 2;
							break;

						case CheckersBoard.OFFBOARD: // ignore the off-diagonal
							// spaces
						case CheckersBoard.EMPTY: // ignore the empty spaces
							break;
						}
					}
					heuristicValue += 0.9 * Center;
				}
			}
		}
		double Edge = getEdge(theBoard, turnCount);
		double inverse = 1 / (Edge + Center);
		if (turnCount <= 21 && turnCount != 0 && turnCount != 1) {
			return heuristicValue + 0.3 * inverse * Edge + 0.3 * inverse
					* Center;
		} else
			return heuristicValue;
	}

	protected double evaluateOppBoard(CheckersBoard theBoard) {
		double heuristicValue = 0;
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				byte contentsOfBoardSpace = theBoard.getPiece(row, col);
				switch (contentsOfBoardSpace) {

				case CheckersBoard.PLAYER1_CHECKER:
					heuristicValue += myCheckerValue;
					break;

				case CheckersBoard.PLAYER1_KING:
					heuristicValue += myKingValue;
					break;

				case CheckersBoard.PLAYER2_CHECKER:
					heuristicValue -= myCheckerValue;
					break;

				case CheckersBoard.PLAYER2_KING:
					heuristicValue -= myKingValue;
					break;

				case CheckersBoard.OFFBOARD: // ignore the off-diagonal spaces
				case CheckersBoard.EMPTY: // ignore the empty spaces
					break;
				}
			}
		}
		return heuristicValue;
	}

	@Override
	public void gameEnded(CheckersPlayerEvent cpe) {
	}

	// int turnCount;
	@Override
	public void gameStarted(CheckersPlayerEvent cpe) {
	}

	public double getCenter(CheckersBoard theBoard, int turnCount) {
		double CenterValueIn = 0;
		for (int row = 2; row < 6; row++) {
			for (int col = 2; col < 6; col++) {
				byte contentsOfBoardSpace = theBoard.getPiece(row, col);
				switch (contentsOfBoardSpace) {

				case CheckersBoard.PLAYER1_CHECKER:
					CenterValueIn += myCheckerValue;
					break;

				case CheckersBoard.PLAYER1_KING:
					CenterValueIn += myKingValue;
					break;

				case CheckersBoard.OFFBOARD: // ignore the off-diagonal spaces
				case CheckersBoard.EMPTY: // ignore the empty spaces
					break;
				}
			}

		}
		double heuristicCenterValue;
		heuristicCenterValue = CenterValueIn + 2 - 0.01 * turnCount;
		return heuristicCenterValue;
	}

	@Override
	public String getDescription() {
		return "A lightning-fast Checkers AI that"
				+ " uses a Depth-First Search."
				+ "\n\nAuthors: Eric Xiao and Fred Shen"
				+ "\nVersion: 1.00 - 10 July 2008";
	}

	public double getEdge(CheckersBoard theBoard, int turnCount) {
		double EdgeValueIn = 0;
		int col = 0;
		for (int row = 0; row < 8; row++) {
			byte contentsOfBoardSpace = theBoard.getPiece(row, col);
			switch (contentsOfBoardSpace) {

			case CheckersBoard.PLAYER1_CHECKER:
				EdgeValueIn += myCheckerValue;
				break;

			case CheckersBoard.PLAYER1_KING:
				EdgeValueIn += myKingValue;
				break;

			case CheckersBoard.OFFBOARD: // ignore the off-diagonal spaces
			case CheckersBoard.EMPTY: // ignore the empty spaces
				break;
			}
		}
		int col2 = 7;
		for (int row = 0; row < 8; row++) {
			byte contentsOfBoardSpace = theBoard.getPiece(row, col2);
			switch (contentsOfBoardSpace) {

			case CheckersBoard.PLAYER1_CHECKER:
				EdgeValueIn += myCheckerValue;
				break;

			case CheckersBoard.PLAYER1_KING:
				EdgeValueIn += myKingValue;
				break;

			case CheckersBoard.OFFBOARD: // ignore the off-diagonal spaces
			case CheckersBoard.EMPTY: // ignore the empty spaces
				break;
			}
		}
		double heuristicEdgeValue;
		heuristicEdgeValue = EdgeValueIn + 0.02 * turnCount;
		return heuristicEdgeValue;
	}

	@Override
	public String getName() {
		return "EFCheckers";
	}

	@Override
	public void remainingTimeChanged(CheckersPlayerEvent cpe) {
	}
}
