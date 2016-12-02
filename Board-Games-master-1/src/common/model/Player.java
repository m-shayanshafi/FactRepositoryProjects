package common.model;


public class Player {

	public final int NUMBER;
	public final GameBoard BOARD;
	
	Player(int number, GameBoard board) {
		NUMBER = number;
		BOARD = board;
	}
	
	public Turn play(Position p) {
		return new Turn(this.NUMBER,p);
	}
}
