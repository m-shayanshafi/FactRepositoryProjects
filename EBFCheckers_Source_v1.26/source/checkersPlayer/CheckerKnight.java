package checkersPlayer;

import java.util.ArrayList;
import java.util.List;

import checkersMain.CheckersBoard;
import checkersMain.CheckersPlayerInterface;

public class CheckerKnight implements CheckersPlayerInterface {

	class BoardNode extends MinimaxTree.MinimaxNode {

		public boolean true_red;

		public CheckersBoard myBoard;

		public BoardNode(MinimaxTree.MinimaxNode par, CheckersBoard board,
				boolean red) {
			super(par);
			myBoard = board;
			true_red = red;
		}

		protected void deepenTree(int depth, int maxdepth) {
			// CheckersBoard tempboard = new CheckersBoard(myBoard);
			// if(depth % 2 == 0)
			// tempboard.invertCheckersBoard();
			// System.out.println(utility_func());
			// System.out.println(tempboard.toString());
			// if(depth % 2 == 0)
			// tempboard.invertCheckersBoard();
			if (depth <= maxdepth) {
				BoardNode temp;
				// tempboard.invertCheckersBoard();
				myBoard = myBoard.invertCheckersBoard();

				for (int x = 0; x < myBoard.getNumSuccessors(); x++) {
					temp = new BoardNode(this, myBoard.getSuccessor(x),
							!true_red);
					//System.out.println("Potential move "+x+" of depth "+(depth
					// +1));

					// temp.myBoard.invertCheckersBoard();
					addChild(temp);
					temp.deepenTree(depth + 1, maxdepth);
				}
			}
		}

		protected float dynamic_minimax(int depth, int maxdepth, boolean max) {
			if (depth <= maxdepth) {
				BoardNode temp;
				CheckersBoard tempboard = myBoard.invertCheckersBoard();
				int nextmoves = tempboard.getNumSuccessors();
				boolean prune_remainder = false;
				float bestval = 0, tempval;
				int index = 0;

				bestval = (max) ? Float.NEGATIVE_INFINITY
						: Float.POSITIVE_INFINITY;

				prune_remainder = false;
				for (int x = 0; x < nextmoves; x++) {
					temp = new BoardNode(this, tempboard.getSuccessor(x),
							!true_red);
					addChild(temp);
					tempval = temp.dynamic_minimax(depth + 1, maxdepth, !max);
					remChild(temp);
					temp.myBoard.freeCache();
					temp = null;
					/*
					 * if(index == 0) { bestval = tempval; }
					 */
					// System.out.print("val, depth ");
					// System.out.println(tempval + " " + depth);
					if (max) {
						if (tempval > bestval) {
							bestval = tempval;
							preferred_child_index = index;
							if (tempval > alpha)
								alpha = tempval;
							if ((parent != null) && parent.beta < alpha) {
								prune_remainder = true;
							}
						}
					} else {
						if (tempval < bestval) {
							bestval = tempval;
							preferred_child_index = index;
							if (tempval < beta)
								beta = tempval;
							if ((parent != null) && (parent.alpha > beta)) {
								prune_remainder = true;
							}
						}
					}
					if (tempval == bestval && Math.random() < choose_diff_prob) {
						bestval = tempval;
						preferred_child_index = x;
					}
					if (prune_remainder) {
						break;
					}
					index++;
					if (Timeleft < 1000) {
						System.out.println("Timeout!");
						break;
					}
				}
				// nextmoves = tempboard.getNextSuccessorBoards(found_jump);

				if (parent != null) {
					if (bestval > parent.alpha && !max) {
						parent.alpha = bestval;
					}
					if (bestval < parent.beta && max) {
						parent.beta = bestval;
					}
				}

				value = bestval;
				// if(depth==1)
				// System.out.println(value);
				return value;
			} else {
				value = utility_func();
				if (parent != null) {
					if (value > parent.alpha && !max) {
						parent.alpha = value;
					}
					if (value < parent.beta && max) {
						parent.beta = value;
					}
				}

				// if(depth==1)
				// System.out.println(value);
				return value;
			}
		}

		@Override
		public void finalize() {
			myBoard.freeCache();
		}

		@Override
		protected float utility_func() {
			float util = ((true_red) ? (float) evaluateBoard(myBoard)
					: -(float) evaluateBoard(myBoard));
			if (util != 0) {/*
							 * System.out.print(util);
							 * System.out.println(true_red);
							 * System.out.println(myBoard.toString());
							 */
			}
			return util;
		}
	}

	public static class MinimaxTree {
		public static class MinimaxNode {
			protected float choose_diff_prob = (float) 0.5;

			protected float value;

			protected float alpha;

			protected float beta;

			protected MinimaxNode parent;

			protected ArrayList<MinimaxNode> children;

			/*
			 * public float dyn_calc_value(boolean max, int depth, int maxdepth)
			 * { MinimaxNode temp; if(depth<maxdepth) {
			 * while(prolif_condition()) {
			 * 
			 * } } return value; }
			 * 
			 * / yet more to be overridden
			 *//*
				 * protected boolean prolif_condition() { return true; }
				 */

			public int preferred_child_index;

			public MinimaxNode(MinimaxNode par) {
				parent = par;
				children = new ArrayList<MinimaxNode>();
				alpha = Float.NEGATIVE_INFINITY;
				beta = Float.POSITIVE_INFINITY;
			}

			public void addAllChildren(List<MinimaxNode> chils) {
				children.clear();
				children.addAll(chils);
			}

			/*
			 * protected void deepenTree(int depth, int maxdepth) { return; }
			 */

			public void addChild(MinimaxNode chil) {
				children.add(chil);
			}

			public MinimaxNode addNewChild() {
				MinimaxNode temp = new MinimaxNode(this);
				children.add(temp);
				return temp;
			}

			public void addSomeChildren(ArrayList<MinimaxNode> chils) {
				children.addAll(chils);
			}

			public MinimaxNode best_child() {
				if (preferred_child_index < -1
						|| preferred_child_index >= children.size())
					return null;
				return children.get(preferred_child_index);
			}

			public float calculate_value(boolean max) {
				MinimaxNode temp;
				int x;
				float bestval = 0, tempval;
				boolean prune_remainder = false;

				if (!children.isEmpty()) {
					for (x = 0; x < children.size(); x++) {
						temp = children.get(x);
						tempval = temp.calculate_value(!max);
						if (x == 0) {
							bestval = tempval;
						} else {
							if (max) {
								if (tempval > bestval) {
									bestval = tempval;
									preferred_child_index = x;
									if (tempval > alpha)
										alpha = tempval;
									if ((parent != null) && parent.beta < alpha) {
										prune_remainder = true;
									}
								}
							} else {
								if (tempval < bestval) {
									bestval = tempval;
									preferred_child_index = x;
									if (tempval < beta)
										beta = tempval;
									if ((parent != null)
											&& (parent.alpha > beta)) {
										prune_remainder = true;
									}
								}
							}
							if (tempval == bestval
									&& Math.random() < choose_diff_prob) {
								bestval = tempval;
								preferred_child_index = x;
							}
						}
						if (prune_remainder) {
							System.out.print(children.size() - x);
							System.out.println(" nodes pruned.");
							break;
						}
					}
					if (parent != null) {
						if (bestval > parent.alpha && !max) {
							parent.alpha = bestval;
						}
						if (bestval < parent.beta && max) {
							parent.beta = bestval;
						}
					}

					value = bestval;
					// System.out.println(value);
					return value;
				} else {
					value = utility_func();
					if (parent != null) {
						if (value > parent.alpha && !max) {
							parent.alpha = value;
						}
						if (value < parent.beta && max) {
							parent.beta = value;
						}
					}
					// System.out.println(value);
					return value;
				}
			}

			public void remChild(MinimaxNode chil) {
				children.remove(chil);
			}

			/**
			 * please override!
			 * 
			 * @return 0
			 */
			protected float utility_func() {
				// please override
				return 0;
			}
		}

		public MinimaxNode root;

		public MinimaxTree() {
			root = new MinimaxNode(null);
		}

		protected void depthfirstdelete(MinimaxNode node) {
			int x;
			for (x = 0; x < node.children.size(); x++) {
				depthfirstdelete(node.children.get(x));
			}

			node = null;
		}

		@Override
		protected void finalize() {
			depthfirstdelete(root);
			System.gc();
		}

		public MinimaxNode getroot() {
			return root;
		}

		public float getrootval() {
			return root.calculate_value(true);
		}
	}

	/**
	 * in my heuristic function evaluateBoard, I treat a promoted king piece as
	 * worth three unpromoted checker pieces.
	 */
	protected double myCheckerValue = 1.0;

	protected double myKingValue = 3.0;

	protected int maxdepth;

	long Timeleft;

	public CheckerKnight() {
		maxdepth = 9;
	}

	/**
	 * BasicCheckersPlayer constructor does nothing, but if you extend this
	 * class, you might need your constructor to do something.
	 */
	public CheckerKnight(int difficulty) {
		maxdepth = difficulty;
	}

	public int choosePlyIndex(CheckersPlayerEvent cpe) {
		Timeleft = cpe.remainingPlyTime;

		// this should be the first line of any takeTurn implementsation
		// List<CheckersBoard> successors =
		// currentBoard.getAllSuccessorBoards();

		MinimaxTree tree = new MinimaxTree();
		// CheckersBoard curboardcopy = new CheckersBoard(currentBoard);
		// curboardcopy.invertCheckersBoard();
		tree.root = new BoardNode(null, cpe.board.invertCheckersBoard(), false);
		// CheckersBoard inver_currentBoard = new CheckersBoard(currentBoard);
		// inver_currentBoard.invertCheckersBoard();
		// currentBoard.invertCheckersBoard();
		float res = 0;
		int best_index_yet = -1;
		System.out.println();

		// tree.getrootval();
		// System.out.println(tree.getrootval());
		//System.out.println(((BoardNode)((tree.getroot()).best_child())).myBoard
		// .toString());
		// System.out.println(((BoardNode)tree.root).preferred_child_index);
		/*
		 * System.out.println("Current board\n"+currentBoard.toString());
		 */
		// Activate iterative deepening search!
		for (int id = 1; id < maxdepth; id++) {
			res = ((BoardNode) tree.root).dynamic_minimax(0, id, true);
			if (Timeleft < 1000) {
				System.out.println("At max depth " + id);
				break;
			} else {
				best_index_yet = tree.root.preferred_child_index;
			}
		}
		System.out.println("Best Path: " + res);/*
												 * for(BoardNode x =
												 * (BoardNode)tree
												 * .root.best_child
												 * ();x!=null;x=(
												 * BoardNode)x.best_child()) {
												 * if(x.true_red)
												 * System.out.print
												 * (x.myBoard.toString()); else
												 * System.out.print(x.myBoard.
												 * invertCheckersBoard
												 * ().toString()); }
												 * System.out.println("\n");
												 * for(int
												 * x=0;x<successors.size();x++)
												 * {
												 * System.out.println(successors
												 * .get(x).toString());
												 * if(((BoardNode
												 * )tree.root.best_child
												 * ()).myBoard
												 * .equals(successors.get(x))) {
												 * System.out.println(x); return
												 * x; } }
												 * System.out.println("NOT FOUND"
												 * ); return 0;
												 */

		return best_index_yet;

		/*
		 * // evaluate heuristic value of each successor board to find the
		 * highest double maxHeuristicValue = Integer.MIN_VALUE; int
		 * maxHeuristicIndex = -1; for (int i=0; i < successors.size(); i++) {
		 * double currHeuristicValue = evaluateBoard(successors.get(i)); //
		 * Store this value as maxHeuristicValue if it's the highest value seen
		 * so far, // or, if it's equal to the highest seen so far, replace the
		 * old one with this // one randomly with probability 0.2. if
		 * (currHeuristicValue > maxHeuristicValue || (currHeuristicValue ==
		 * maxHeuristicValue && Math.random() > 0.2)) { maxHeuristicValue =
		 * currHeuristicValue; maxHeuristicIndex = i; } }
		 * 
		 * // return the highest-heuristic successor return maxHeuristicIndex;
		 */
	}

	/**
	 * Generate a heuristic value for how good theBoard is for red player.
	 * Jeremy's simple function here just compares the number of pieces each
	 * player has, with kings counting for three checkers.
	 */
	protected double evaluateBoard(CheckersBoard theBoard) {
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

	@Override
	public void gameStarted(CheckersPlayerEvent cpe) {
	}

	@Override
	public String getDescription() {
		return "A Checkers AI that uses a Depth-First"
				+ " Search with a mini-max algorithm and Alpha-Beta Pruning."
				+ "\n\nAuthor: Matt" + "\nVersion: 1.00 - 10 July 2008";
	}

	@Override
	public String getName() {
		return "Checker Knight";
	}

	@Override
	public void remainingTimeChanged(CheckersPlayerEvent cpe) {
		Timeleft = cpe.remainingPlyTime;
	}
}
