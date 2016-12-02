package checkersPlayer;

import checkersMain.CheckersPlayerInterface;

/**
 * The HAL900S is an advanced Artificial Intelligence for Checkers. It uses a
 * depth-limited search and a mini-max algorithm with alpha-beta pruning. The
 * HAL900S is a reduced version of the HAL900 that moves within a few seconds.
 * 
 * @author Amos Yuen and Louis Wang
 * @version {@value #VERSION}
 */

public class HAL900S extends HAL900 implements CheckersPlayerInterface {

	public static final String VERSION = HAL900.VERSION;
	
	public HAL900S() {
		reduceTimes = new int[] { 1000 };
	}

	@Override
	protected void calcSearchDepth() {
		searchDepth = MIN_SEARCH_DEPTH;
		maxSearchDepth = searchDepth + 2;
	}

	@Override
	public String getDescription() {
		return "A Speed version of the HAL900 that searches a small"
				+ " constant depth for fast performance. It is an"
				+ " advanced Checkers AI that uses a depth-limitedt search"
				+ " with a mini-max algorithm and alpha-beta pruning."
				+ "\n\nAuthor: Amos Yuen and Louis Wang\nVersion: " + VERSION;
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}
}
