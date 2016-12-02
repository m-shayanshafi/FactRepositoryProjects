package model;

import java.util.Observable;

public abstract class BoardGame  extends Observable {
	public static final int UNUSED = -1;
	public static final int EMPTY = 0;
	public static final int READY_TO_ROLL = -1;
//	public static final int PLAYER2 = 2;
	protected  int[][] matrix;
	protected  int[][] bounds;
	protected Location selectedPiece;
//	protected Location[] locations;
	protected  Player currentPlayer;
	protected  Player[] players;
    protected int rollNumber;
	
	//protected  int 
	//protected Player p1,p2;
    

	
	public BoardGame(){
		selectedPiece = null;
		rollNumber = 0;
		players = new Player[2];
	
	}
	public abstract void init(boolean isSingleMode);
	public abstract void checkWon();
	public State getState(){
		if(this.rollNumber == READY_TO_ROLL)
			return State.ROLL;
		else if(this.selectedPiece == null)
			return State.PIECE;
		else
			return State.DESTINATION;
	}
	
	protected void initPlayers(){
        players[0].setFlag(1);
        players[1].setFlag(2);
        players[0].setNextPlayer(players[1]);
        players[1].setNextPlayer(players[0]);
        players[0].setBoardGame(this);
        players[1].setBoardGame(this);
        currentPlayer = players[0];
	}
	
	protected  boolean isValidSelection(Location l){
		if(l == null)
			return false;
		if(this.isOutOfBound(l)){
			System.out.println("OOB");
			return false;
		}
		else if(this.getValue(l) == EMPTY || this.getValue(l) == UNUSED){
			System.out.println("empty");
			return false;
		}
		else
		 return true;
	}
	
	protected  boolean isValidDestination(Location des){
		if(this.selectedPiece == null)
		  return false;
		if(this.isOutOfBound(des)){
			System.out.println("OOB");
			return false;
		}
	
		return true;
	}
	
	protected abstract boolean computerTurn();

	


	public boolean selectPiece(Location l){
      if(this.isValidSelection(l)){
    	  Location old = this.selectedPiece;
    	  this.selectedPiece = l;
    	   System.out.println("selected value"+ this.getValue(l));
		this.setChanged();
		this.notifyObservers(new SelectedNotification(old,l));
		
	    return true;
	}
		System.out.println("invalid selection");
	return false;
 }
	
	public boolean selectDestination(Location des){
		if(this.getValue(des) == currentPlayer.getFlag()){
			this.selectPiece(des);
			return false;
			}
          if(this.isValidDestination(des)){
			this.replace(this.selectedPiece, des);
			return true;
		}
          System.out.println("invalid destination");
		return false;
	}

	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public int getValue(Location l){
		return matrix[l.getX()][l.getY()];
		
	}
	
	protected boolean isOutOfBound(Location l){
		//  System.out.println("oobtest"+l.getX());
      if(l.getY() >= bounds.length || l.getY() < 0){
    	//  System.out.println(l.getY());
    	  return true;
      }

     if(l.getX() < bounds[l.getY()][0] || l.getX() > bounds[l.getY()][1]){
    //	  System.out.println("oob"+l.getX());
		  return true;
      }
      
		return false;
	}
	
/*	protected boolean  isOnBound(Location l){
          if(l.getX() == bounds[l.getY()][0]||l.getX() == bounds[l.getY()][1])
        	  return true;
        return false;
	}	*/
	protected  void setValue(Location l,int val){
		matrix[l.getX()][l.getY()] = val;
		this.setChanged();
		this.notifyObservers(new ValueChangedNotification(l));
		
	}
	

	
	
	

     protected void replace(Location res,Location des){
		   int tmp = this.getValue(des);
		   this.setValue(des, this.getValue(res));
		   this.setValue(res, tmp);
		//   this.currentPlayer.endTurn();
		 //  this.computerTurn();
	   }
     
     
   //  protected
	
    public abstract void end();
     
	public void sendScoreChange(Player p){
		this.checkWon();
		this.setChanged();
		this.notifyObservers(new ScoreChangeNotification(p));
	}
	
	
	public void setCurrentPlayer(Player currentPlayer) {
		 
		this.currentPlayer = currentPlayer;
	}
   

	public Location getSelectedPiece() {
		return selectedPiece;
	}
	public int getRollNumber() {
		return rollNumber;
	}
	public void setRollNumber(int rollNumber) {
		if(this.getState() == State.ROLL || rollNumber == READY_TO_ROLL){
		 this.rollNumber = rollNumber;
		 this.setChanged();
		 this.notifyObservers(new RollNumberChangedNotification(rollNumber));
		}
	}
	protected void win(Player p){
		System.out.println(" you win");
		this.setChanged();
		this.notifyObservers(new WinNotification(p));
	}
	
	
	
/*	public boolean move(Location l){
		if(this.getValue(l) == EMPTY){
			this.setValue(this.selectPieceedPiece, EMPTY);
	        this.setValue(l,currentPlayer == 0 ? PLAYER1:PLAYER2); 	
	        return true;
	}
	return false;
 }
	
   /*private Player getNextPlayer(){
	   if(currentPlayer.equals(players[players.length-1]))
		   return players[0];
	   else
		   return players[1];
		                 
       }*/

}
