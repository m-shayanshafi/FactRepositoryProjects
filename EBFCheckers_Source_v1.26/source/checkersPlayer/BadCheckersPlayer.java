package checkersPlayer;

/**
 * @(#)BadCheckersPlayer.java
 *
 * @author Bichen Wang
 * @version 1.00 2008/7/10
 */
import java.util.List;

import checkersMain.CheckersBoard;
import checkersMain.CheckersPlayerInterface;

public class BadCheckersPlayer implements CheckersPlayerInterface {
	private static final float CUT_RATIO = 0.1f;

	List<? extends CheckersBoard> successors;
	private float myPieces;
	private float myKings;
	private float myWeight;
	private long myTimeLeft;
	private float current;
	private long myCutTime;
	private float myTrade;

	public BadCheckersPlayer() {
		myPieces = (float) 1.0;
		myKings = (float) 1.16;
		myWeight = (float) 1.24;
		myTrade = (float) 0.004;
	}

	@Override
	public int choosePlyIndex(CheckersPlayerEvent cpe) {
		int successors = cpe.board.getNumSuccessors();
		if (successors == 1)
			return 0;
		
		myTimeLeft = cpe.remainingPlyTime;
		myCutTime = (int) (cpe.remainingPlyTime * CUT_RATIO);

		int bestIndex = -1;
		int search = 2;
		int newIndex = -1;
		while (myTimeLeft > myCutTime) {
			float best = (float) 1000.0;
			for (int i = 0; i < successors; i++) {
				if (myTimeLeft < myCutTime) {
					break;
				}
				current = 1000;
				if (numPieces1(cpe.board.getSuccessor(i)) < 4
						|| numPieces2(cpe.board.getSuccessor(i)) < 4) {
					if (numPieces1(cpe.board.getSuccessor(i)) > numPieces2(cpe.board
							.getSuccessor(i))) {
						current = ending(cpe.board.getSuccessor(i), search,
								true);
					}
					if (numPieces1(cpe.board.getSuccessor(i)) <= numPieces2(cpe.board
							.getSuccessor(i))) {
						current = ending(cpe.board.getSuccessor(i), search,
								false);
					}
				} else {
					current = heuristic(cpe.board.getSuccessor(i), search);
				}
				if (best > current) {
					best = current;
					newIndex = i;
				} else if (best == current) {
					if (Math.random() < 0.4) {
						newIndex = i;
					}
				}
				if (myTimeLeft < myCutTime) {
					break;
				}
			}
			if (myTimeLeft < myCutTime) {
				break;
			}
			bestIndex = newIndex;
			search++;
			search++;

		}
		System.out.println(search / 2);
		return bestIndex;
	}

	private boolean drawspace1(int row, int col) {
		if (row == 0 && col == 1) {
			// System.out.println(row + " " + col);
			return true;
		}
		if (row == 1 && col == 0) {
			// System.out.println(row + " " + col);
			return true;
		}
		return false;
	}

	private boolean drawspace2(int row, int col) {
		if (row == 6 && col == 7) {
			// System.out.println(row + " " + col);
			return true;
		}
		if (row == 7 && col == 6) {
			// System.out.println(row + " " + col);
			return true;
		}
		return false;
	}

	private float ending(CheckersBoard currB, int num, boolean win) {
		if (myTimeLeft < myCutTime) {
			return 1000;
		}
		if (num == 0) {
			// float heuristicValue = 0;
			int myChecker = 0;
			int myKing = 0;
			int enemyChecker = 0;
			int enemyKing = 0;
			int trapspace = 0;
			int drawspace1 = 0;
			int drawspace2 = 0;
			int lamespace = 0;
			int yawnspace = 0;
			for (int row = 0; row < 8; row++) {
				for (int col = 0; col < 8; col++) {
					byte contentsOfBoardSpace = currB.getPiece(row, col);
					if (contentsOfBoardSpace == CheckersBoard.PLAYER1_CHECKER) {
						myChecker++;
					}
					if (contentsOfBoardSpace == CheckersBoard.PLAYER1_KING) {
						myKing++;
						if (trapspace(row, col)) {
							trapspace++;
						}
						if (drawspace1(row, col)) {
							drawspace1++;
						}
						if (drawspace2(row, col)) {
							drawspace2++;
						}
					}
					if (contentsOfBoardSpace == CheckersBoard.PLAYER2_CHECKER) {
						enemyChecker++;
					}
					if (contentsOfBoardSpace == CheckersBoard.PLAYER2_KING) {
						enemyKing++;
						if (trapspace(row, col)) {
							lamespace++;
						}
						if (drawspace1(row, col)) {
							yawnspace++;
						}
						if (drawspace2(row, col)) {
							yawnspace++;
						}
					}
				}
			}
			// heuristicValue = -(myChecker * myPieces - enemyChecker * myPieces
			// +
			// myKing * myKings - enemyKing * myKings);
			if (win) {
				currB.freeCache();
				return (float) (-((myChecker * myPieces * 0.95)
						- (enemyChecker * myPieces * 0.95)
						+ ((myKing - trapspace) * myKings)
						- ((enemyKing - yawnspace) * myKings)
						+ (trapspace * myWeight) - (yawnspace * myWeight)) + (numPieces2(currB) * myTrade));
			} else {
				if (drawspace1 == 2 || drawspace2 == 2) {
					currB.freeCache();
					return (float) (-((myChecker * myPieces * 0.95)
							- (enemyChecker * myPieces * 0.95)
							+ ((myKing - drawspace1) * myKings)
							+ ((myKing - drawspace2) * myKings)
							- ((enemyKing - lamespace) * myKings)
							+ ((drawspace1 + drawspace2) / 2 * myWeight) - (lamespace * myWeight)));

				} else {
					currB.freeCache();
					return (float) (-((myChecker * myPieces * 0.95)
							- (enemyChecker * myPieces * 0.95)
							+ ((myKing - drawspace1) * myKings)
							+ ((myKing - drawspace2) * myKings)
							- ((enemyKing - lamespace) * myKings)
							+ (drawspace1 * myWeight) + (drawspace2 * myWeight) - (lamespace * myWeight)));
				}
			}
			// return heuristicValue;
		} else {
			if (myTimeLeft < myCutTime) {
				return 1000;
			}
			int nextMoves = currB.invertCheckersBoard().getNumSuccessors();
			if (nextMoves == 0) {
				if (num % 2 == 0) {
					currB.freeCache();
					return -500;
				} else {
					currB.freeCache();
					return 500;
				}

			}
			if (nextMoves == 1) {
				if (myTimeLeft < myCutTime) {
					return 1000;
				}
				return ending(currB.invertCheckersBoard().getSuccessor(0),
						num - 1, win);
			}
			if (myTimeLeft < myCutTime) {
				return 1000;
			}
			float best = (float) 1000.0;
			for (int i = 0; i < nextMoves; i++) {
				if (myTimeLeft < myCutTime) {
					return 1000;
				}
				// if(heuristic(nextMove.get(i), 0) < 3)
				// {
				float current = ending(currB.invertCheckersBoard()
						.getSuccessor(i), num - 1, win);
				if (best > current) {
					best = current;
				}
				// }

			}
			currB.freeCache();
			return best;
		}
	}

	@Override
	public void gameEnded(CheckersPlayerEvent cpe) {
	}

	@Override
	public void gameStarted(CheckersPlayerEvent cpe) {
	}

	@Override
	public String getDescription() {
		return "A time-gobbling AI that uses a simple iterative deepening search with a"
				+ " mini-max algorithm. It also has a special end-game heuristic and it"
				+ " will take up most of its time allotted unless there is only one possible move."
				+ "\n\nAuthor: Bichen Wang" + "\nVersion: 1.00 - 10 July 2008";
	}

	@Override
	public String getName() {
		return "The Bad Checkers Player";
	}

	private float heuristic(CheckersBoard currB, int num) {
		if (myTimeLeft < myCutTime) {
			return 1000;
		}
		if (num == 0) {
			float heuristicValue = 0;
			int myChecker = 0;
			int myKing = 0;
			int enemyChecker = 0;
			int enemyKing = 0;
			for (int row = 0; row < 8; row++) {
				for (int col = 0; col < 8; col++) {
					byte contentsOfBoardSpace = currB.getPiece(row, col);
					if (contentsOfBoardSpace == CheckersBoard.PLAYER1_CHECKER) {
						myChecker++;
					}
					if (contentsOfBoardSpace == CheckersBoard.PLAYER1_KING) {
						myKing++;
					}
					if (contentsOfBoardSpace == CheckersBoard.PLAYER2_CHECKER) {
						enemyChecker++;
					}
					if (contentsOfBoardSpace == CheckersBoard.PLAYER2_KING) {
						enemyKing++;
					}
				}
			}
			heuristicValue = (-(myChecker * myPieces - enemyChecker * myPieces
					+ myKing * myKings - enemyKing * myKings) + (numPieces2(currB) * myTrade));
			currB.freeCache();
			return heuristicValue;
		} else {
			if (myTimeLeft < myCutTime) {
				return 1000;
			}
			int nextMoves = currB.invertCheckersBoard().getNumSuccessors();
			if (nextMoves == 0) {
				if (num % 2 == 0) {
					currB.freeCache();
					return -500;
				} else {
					currB.freeCache();
					return 500;
				}

			}
			if (nextMoves == 1) {
				if (myTimeLeft < myCutTime) {
					return 1000;
				}
				return heuristic(currB.invertCheckersBoard().getSuccessor(0),
						num - 1);
			}
			if (myTimeLeft < myCutTime) {
				return 1000;
			}
			float best = (float) 1000.0;
			for (int i = 0; i < nextMoves; i++) {
				if (myTimeLeft < myCutTime) {
					return 1000;
				}
				// if(heuristic(nextMove.get(i), 0) < 3)
				// {
				float current = heuristic(currB.invertCheckersBoard()
						.getSuccessor(i), num - 1);
				if (best > current) {
					best = current;
				}
				// }

			}
			currB.freeCache();
			return best;
		}
	}

	private int numPieces1(CheckersBoard count) {
		int num = 0;
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				byte contentsOfBoardSpace = count.getPiece(row, col);
				if (contentsOfBoardSpace == CheckersBoard.PLAYER1_CHECKER
						|| contentsOfBoardSpace == CheckersBoard.PLAYER1_KING) {
					num++;
				}
			}
		}
		return num;
	}

	private int numPieces2(CheckersBoard count) {
		int num = 0;
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				byte contentsOfBoardSpace = count.getPiece(row, col);
				if (contentsOfBoardSpace == CheckersBoard.PLAYER2_CHECKER
						|| contentsOfBoardSpace == CheckersBoard.PLAYER2_KING) {
					num++;
				}
			}
		}
		return num;
	}

	@Override
	public void remainingTimeChanged(CheckersPlayerEvent cpe) {
		myTimeLeft = cpe.remainingPlyTime;
	}

	private boolean trapspace(int row, int col) {
		if (row == 3 && col == 2) {
			// System.out.println(row + " " + col);
			return true;
		}
		if (row == 5 && col == 2) {
			// System.out.println(row + " " + col);
			return true;
		}
		if (row == 5 && col == 4) {
			// System.out.println(row + " " + col);
			return true;
		}
		if (row == 4 && col == 5) {
			// System.out.println(row + " " + col);
			return true;
		}
		if (row == 2 && col == 3) {
			// System.out.println(row + " " + col);
			return true;
		}
		if (row == 2 && col == 5) {
			// System.out.println(row + " " + col);
			return true;
		}
		return false;
	}

	/*
	 * private class PlayerMakeMoveThread1 extends Thread { private
	 * CheckersBoard myBoard; private int count;
	 * 
	 * public PlayerMakeMoveThread1(CheckersBoard board, int num) { super();
	 * setDaemon(true); myBoard = board; count = num; }
	 * 
	 * public void run() { try{ float temp = heuristic(myBoard, count);
	 * if(!isInterrupted()) current = temp; }catch(Exception e) {
	 * e.printStackTrace(); System.out.println("Finished"); return; } } }
	 */
}