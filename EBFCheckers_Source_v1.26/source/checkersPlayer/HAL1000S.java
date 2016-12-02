package checkersPlayer;


/**
 * The HAL1000S is an advanced learning Artificial Intelligence for Checkers. It
 * uses a depth-limited search and a mini-max algorithm with alpha-beta pruning.
 * It uses a neural network to evaluate the board. The HAL1000S is a reduced
 * version of the HAL1000 that moves within a few seconds.
 * 
 * @author Amos Yuen and Louis Wang
 * @version {@value #VERSION}
 */

public class HAL1000S extends HAL1000 {

	public static final String VERSION = HAL1000.VERSION;
	
	public HAL1000S() {
		reduceTimes = new int[] { 1000 };
	}

	@Override
	protected void calcSearchDepth() {
		searchDepth = MIN_SEARCH_DEPTH;
		maxSearchDepth = searchDepth + 2;
	}

	@Override
	public String getDescription() {
		return "A Speed version of the HAL1000 that searches a small constant"
				+ " depth for fast performance. It is An advanced learning"
				+ " Checkers AI that uses a depth-limited search with a mini-max"
				+ " algorithm and alpha-beta pruning. It also uses a unique"
				+ " heuristic in combination with a neural network."
				+ "\n\nAuthor: Amos Yuen and Louis Wang\nVersion: " + VERSION;
	}
}
