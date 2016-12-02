package checkersPlayer;

import checkersMain.CheckersPlayerInterface;

/**
 * This checkers player picks one of its available moves at random.
 */
public class RandomAI implements CheckersPlayerInterface {

	@Override
	public int choosePlyIndex(CheckersPlayerEvent cpe) {
		// choose a random move to make
		return (int) (cpe.board.getNumPlies() * Math.random());
	}

	@Override
	public void gameEnded(CheckersPlayerEvent cpe) {
	}

	@Override
	public void gameStarted(CheckersPlayerEvent cpe) {
	}

	@Override
	public String getDescription() {
		return "A stupid AI that plays randomly."
				+ "\n\nAuthors: Jeremy Hoffman"
				+ "\nVersion: 1.00 - 10 July 2008";
	}

	public String getName() {
		return "Random AI";
	}

	@Override
	public void remainingTimeChanged(CheckersPlayerEvent cpe) {
	}
}
