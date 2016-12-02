package model;

public class Senet extends BoardGame {

	
	public static Location BEATIFUL_HOUSE = new Location(5, 2);
	public static Location OUT = new Location(10, 2);
	public static Location ROLL = new Location(10, 0);
	public static Location PASS = new Location(10, 1);
	public static final int ROW = 3;
	public static final int COLOUM = 11;
    public static int ROLL_VAL = 3;
	public static int PASS_VAL = 4;
	public static int OUT_VAL = 5;

	
	@Override
	public void init(boolean isSingleMode) {
		int[][] sm = {{2,0,0},{1,0,0},{2,0,0},{1,0,0},{2,0,0},{1,0,0},{2,0,0},{1,0,0},{2,0,0},{1,0,0},{ROLL_VAL,PASS_VAL,OUT_VAL}};
		matrix = sm;
		int[][] sb = { { 0, 9 }, { 0, 9 }, { 0, 10 } };
		bounds = sb;
		players[0] = new HumanPlayer();
		if(isSingleMode)
		players[1] = new SPlayer();
		else
		players[1] = new HumanPlayer();
		this.initPlayers();
	//	this.rollNumber = READY_TO_ROLL;
		this.setRollNumber(READY_TO_ROLL);
	}

	@Override
	protected boolean isValidSelection(Location l) {
		if (!super.isValidSelection(l))
			return false;
        if(this.getValue(l) != this.currentPlayer.getFlag())
			return false;
	//	return this.getSuitableDestination(l)==null;
        return true;
	}

	@Override
	protected boolean isValidDestination(Location des) {
//		System.out.println("test");

		if (!super.isValidDestination(des))
			return false;

		if (this.isDefended(des))
			//System.out.println("invalid xx");
			return false;
		
		if (des.equals(this.getForwardDestination(this.selectedPiece,
				rollNumber)))
			return true;
		if (!this.isEnd()
				&& des.equals(this.getBackDestination(this.selectedPiece,
						rollNumber)))
			return true;
		System.out.println("invalid");
		return false;
	}
	 public boolean selectPiece(Location l){
		 if(l.equals(PASS)){
			this.end();
			return false;
		 }
        return super.selectPiece(l);
	 }
	 
	 
	public boolean selectDestination(Location des) {
		 if(des.equals(PASS)){ 
				this.end();
				return false;
			 }

         if(super.selectDestination(des)){
        	 if(des.equals(OUT)){
        		 this.currentPlayer.gainScore(1);
        		 this.setValue(OUT, OUT_VAL);
        		 this.setValue(selectedPiece, EMPTY);
        	 }
        	this.end();
        	 return true;
         }
         return false;
    }

	@Override
	protected boolean computerTurn() {
		if (this.currentPlayer instanceof SPlayer) {
			currentPlayer.roll(5, 1);
			Player tmp = currentPlayer;
			while(tmp.equals(currentPlayer)){
				if(currentPlayer.select(null)){
			     if(currentPlayer.select(this.getForwardDestination(selectedPiece, rollNumber))||currentPlayer.select(this.getBackDestination(selectedPiece, rollNumber)))
			    	 return true;
			    }
			}
	      }
		return false;
	}
	
	private boolean isEnd() {
		return this.selectedPiece.getY() == 2 && this.selectedPiece.getX() > 5;
	}

	@Override
	public void end(){
		this.currentPlayer.endTurn();
   	   // this.rollNumber = BoardGame.READY_TO_ROLL;//roll changed
		this.setRollNumber(READY_TO_ROLL);
	    this.selectedPiece = null;//selected
		this.computerTurn();
	}
	
	/*public Location getSuitableDestination(Location l){
		Location tmp = l==null ? this.selectedPiece : l ;
	    Location des = this.getForwardDestination(tmp,rollNumber);
		if(this.isValidDestination(des))
			return des;
		des = this.getBackDestination(tmp,rollNumber);
		if(this.isValidDestination(des))
			return des;
		return null;
	}
	/*
	 * public boolean selectPiece(Location l){ if(super.selectPiece(l)){
	 * this.moveTo(l, this.getForwardDestination(l, this.roll())); } return
	 * false; }
	 */
	  public void checkWon(){
	     for(Player player:players){
	    	 if(player.getScore() == 5)
	    		 this.win(player);
	     }   
		}


	 private Location getForwardDestination(Location l, int rn) {
		Location des = l;
		
		for (int i = 1; i <= rn; i++) {
			des = this.nextLocation(des);
			if (des == null)
				break;
			if (i < rn) {
				if (des.equals(BEATIFUL_HOUSE) || des.equals(OUT)) {
					des = this.getBackDestination(des,  rn-i);
					break;
				}
			}
		}
//		System.out.println(des.getX() + "," + des.getY());
		return des;
	}

	private Location getBackDestination(Location l, int rn) {
		Location des = l;
		for (int i = 1; i <= rn; i++) {
			des = this.preLocation(des);
			if (des == null)
				break;
		}
	//	System.out.println(des.getX() + "," + des.getY());
		return des;
	}
  /*  @Override
    public void roll(){
    	this.rollNumber = this.currentPlayer.roll(5, 1);
    }*/
	


	/*
	 * public boolean moveTo(Location res,Location des){
	 * 
	 * this.replace(res, des); this.endTurn(); //this.roll(); return true; }
	 */



	private Location nextLocation(Location l) {
		Location next = null;
		if (l.getY() % 2 == 0) {
			next = new Location(l.getX() + 1, l.getY());
			if (this.isOutOfBound(next))
				next = new Location(l.getX(), l.getY() + 1);
		} else {
			next = new Location(l.getX() - 1, l.getY());
			if (this.isOutOfBound(next))
				next = new Location(l.getX(), l.getY() + 1);
		}
		if (this.isOutOfBound(next))
			next = null;
		return next;

	}

	private Location preLocation(Location l) {
		Location next = null;
		if (l.getY() % 2 == 0) {
			next = new Location(l.getX() - 1, l.getY());
			if (this.isOutOfBound(next))
				next = new Location(l.getX(), l.getY() - 1);
		} else {

			next = new Location(l.getX() + 1, l.getY());
			if (this.isOutOfBound(next))
				next = new Location(l.getX(), l.getY() - 1);
		}
		if (this.isOutOfBound(next))
			next = null;
		return next;
	}

	public boolean isDefended(Location l) {
		if(this.getValue(l) == EMPTY)
		 return false;
		Location pre = this.nextLocation(l);
		Location next = this.preLocation(l);
		if (pre != null && this.getValue(pre) == this.getValue(l))
			return true;
		else if (next != null && this.getValue(next) == this.getValue(l))
			return true;

		return false;
	}

}
