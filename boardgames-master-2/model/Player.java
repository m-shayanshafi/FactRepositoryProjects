package model;

import java.util.Random;




public abstract class Player {
	
	public static final int PLAYER1_FLAG = 1;
	public static final int PLAYER2_FLAG = 2;
	
	
	private BoardGame boardGame;
    private Player nextPlayer;
	private int flag;
	private int score;
	
	
	public int roll(int max,int min){
		Random random = new Random();
		int rollNumber = random.nextInt(max)%(max-min+1) + min;
		this.getBoardGame().setRollNumber(rollNumber);
		return rollNumber;
	}
	
	public Player getNextPlayer() {
		return nextPlayer;
	}

	public void setNextPlayer(Player nextPlayer) {
		this.nextPlayer = nextPlayer;
	}

   

   public void endTurn(){
	  // System.out.println("end");
	  
	   this.boardGame.setCurrentPlayer(this.nextPlayer);
	   System.out.println("end now is player "+this.boardGame.getCurrentPlayer().getFlag());
   }
	
	public int getScore() {
		return score;
	}

	public void gainScore(int s){
		this.score += s;
		this.boardGame.sendScoreChange(this);
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public BoardGame getBoardGame() {
		
		return boardGame;
	}
	
	public void pass(){
		this.boardGame.end();
	}
	
	public void setBoardGame(BoardGame boardGame) {
		this.boardGame = boardGame;
	}
	
//	public abstract boolean isValid(Location l);
	
	public abstract boolean select(Location location);
	
//	public abstract boolean moveTo(Location location);
	
			
				

}
