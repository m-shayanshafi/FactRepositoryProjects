package common.model;


public class Random extends Player {

	Random(int number, GameBoard board) {
		super(number, board);
	}

	public Turn play(){
		
		int maxX = BOARD.WIDTH;
		int maxY = BOARD.LENGTH;
		int x = (int)(Math.random() * (maxX + 1));
		int y = (int)(Math.random() * (maxY + 1));
		
		Position position = new Position(x,y);
		super.play(position);
		
		return new Turn(NUMBER,position);
	}
	
	public static Position playRandom(int width, int length){
		
		int maxX = width;
		int maxY = length;
		int x = (int)(Math.random() * (maxX + 1));
		int y = (int)(Math.random() * (maxY + 1));
		
		Position position = new Position(x,y);
		
		return position;
	}
}
