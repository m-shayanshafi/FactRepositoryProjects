package checkersPlayer;

import checkersMain.CheckersBoard;
import checkersMain.CheckersPlayerInterface;

public class Human implements CheckersPlayerInterface {
	private CheckersBoard board;
	private int moveIndex;
	private int remainingTime;

	@Override
	public int choosePlyIndex(CheckersPlayerEvent cpe) {
		remainingTime = cpe.remainingPlyTime;
		this.board = cpe.board;
		moveIndex = -1;
		while (moveIndex == -1) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				System.out.println("Human Player Ply Thread Interrupted");
				return -1;
			}
		}
		return moveIndex;
	}

	@Override
	public void gameEnded(CheckersPlayerEvent cpe) {
	}

	@Override
	public void gameStarted(CheckersPlayerEvent cpe) {
	}

	public CheckersBoard getCurrBoard() {
		return board;
	}

	@Override
	public String getDescription() {
		return "A Human Player";
	}

	public String getName() {
		return "Human";
	}

	public int getRemainingPlyTime() {
		return remainingTime;
	}

	@Override
	public void remainingTimeChanged(CheckersPlayerEvent cpe) {
		remainingTime = cpe.remainingPlyTime;
	}

	public void setMove(int index) {
		moveIndex = index;
	}
}
