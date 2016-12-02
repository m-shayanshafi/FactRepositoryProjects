package model;

import java.util.ArrayList;
import java.util.Iterator;

public class ChineseChecker extends BoardGame {
	public static final int ROW = 17;
	public static final int COLOUM = 17;


	@Override
	public void init(boolean isSingleMode) {
		// int[][] sm =
		// {{2},{1},{2},{1},{2},{1},{2},{1},{2},{1},{ROLL_VAL,PASS_VAL,OUT_VAL}};
		//
		// System.out.println("hello");
		int[][] sb = { { 12, 12 },// y=0
				{ 11, 12 },// y=1
				{ 10, 12 },// y=2
				{ 9, 12 },// y=3
				{ 4, 16 },// y=4
				{ 4, 15 },// y=5
				{ 4, 14 },// y=6
				{ 4, 13 },// y=7
				{ 4, 12 },// y=8
				{ 3, 12 },// y=9
				{ 2, 12 },// y=10
				{ 1, 12 },// y=11
				{ 0, 12 },// y=12
				{ 4, 7 },// y=13
				{ 4, 6 },// y=14
				{ 4, 5 },// y=15
				{ 4, 4 },// y=16
		};
		bounds = sb;
		matrix = new int[17][17];
		this.initPieces();
		players[0] = new HumanPlayer();
		if(isSingleMode)
		players[1] = new CCPlayer();
		else
		players[1] = new HumanPlayer();
		this.initPlayers();

	}

	private void initPieces() {

		for (int i = 0; i < 17; i++) {
			for (int j = 0; j < 17; j++) {
				matrix[j][i] = UNUSED;
			}
		}
		for (int i = 0; i < bounds.length; i++) {
			for (int j = bounds[i][0]; j <= bounds[i][1]; j++) {
				System.out.print(j + "," + i + "  ");
				matrix[j][i] = EMPTY;
			}
			System.out.println();
		}

		for (int i = 16; i > 12; i--) {
			for (int j = 0; j <= 16 - i; j++) {
				matrix[4 + j][i] = 1;
				System.out.print(4 + j + "," + i + "  ");
			}
			System.out.println();
		}

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j <= i; j++) {
				matrix[12 - j][i] = 2;
				System.out.print(12 - j + "," + i + "  ");
			}
			System.out.println();
		}

	}
	
	//Use loops instead of recursion to avoid StackOverflow exception
   private boolean jump(Location orig, Location des) {
   
		ArrayList<Location> jps = this.getJumpableLocations(orig);
		ArrayList<Location> tmp = new ArrayList<Location>(50);
		ArrayList<Location> checked = new ArrayList<Location>(100);
	/*	checked.add(new Location(1,2));
		Location l2 = new Location(1,2);
		System.out.println(checked.contains(l2));*/
		/*
		 *  while(i.hasNext()){ Location
		 * cursor = i.next(); if (cursor.equals(des)) return true; else
		 * 
		 * }
		 */
		while (jps.size() != 0 || tmp.size() != 0) {
			if (jps.size() != 0) {
			//	Iterator<Location> i = jps.iterator();
				for( Iterator< Location > it = jps.iterator(); it.hasNext() ; ) {
				Location loc = it.next();
					System.out.println("Location " + loc.getX() + loc.getY());
					if (loc.equals(des))
						
						return true;
	                else if(!checked.contains(loc)){
						checked.add(loc);
						tmp.addAll(this.getJumpableLocations(loc));
					}
				}
					jps.clear();
				}
			 
		   else {
				for ( Iterator< Location > it = tmp.iterator(); it.hasNext() ; ) {
					Location loc = it.next();
					System.out.println("Location " + loc.getX() + loc.getY());
					if (loc.equals(des))
						return true;
					
				    else if(!checked.contains(loc)){
						checked.add(loc);
						jps.addAll(this.getJumpableLocations(loc));
					}
					
				}
				System.out.println("xxx" + jps.size()+"==========");
				tmp.clear();
			}
			System.out.println("all empty");
		}
		

	    	/*
			 * else if (this.isEmpty(loc)&&this.jump(loc, des)) return true;
			 */
		
		return false;
	}

	private boolean move(Location orig, Location des) {
		ArrayList<Location> als = this.getAdjacentLocations(orig);
		for (Location loc : als) {
			if (des.equals(loc)&&this.isEmpty(loc))
				return true;
		}
		return false;
	}

	private ArrayList<Location> getAdjacentLocations(Location l) {
		ArrayList<Location> als = new ArrayList<Location>();
		Location left = new Location(l.getX() - 1, l.getY());
		// if(!this.isOutOfBound(left))
		als.add(left);
		Location right = new Location(l.getX() + 1, l.getY());
		// if(!this.isOutOfBound(right))
		als.add(right);
		Location leftUp = new Location(l.getX(), l.getY() - 1);
		// if(!this.isOutOfBound(leftUp))
		als.add(leftUp);
		Location leftDown = new Location(l.getX() - 1, l.getY() + 1);
		// if(!this.isOutOfBound(leftDown))
		als.add(leftDown);
		Location rightUp = new Location(l.getX() + 1, l.getY() - 1);
		// if(!this.isOutOfBound(rightUp))
		als.add(rightUp);
		Location rightDown = new Location(l.getX(), l.getY() + 1);
		// if(!this.isOutOfBound(rightDown))
		als.add(rightDown);

		return als;

	}

	private ArrayList<Location> getJumpableLocations(Location l) {

		ArrayList<Location> jps = new ArrayList<Location>();
		Location left = new Location(l.getX() - 1, l.getY());
		if (this.isPiece(left)) {
			Location leftJ = new Location(l.getX() - 2, l.getY());
			if (this.isEmpty(leftJ))
				jps.add(leftJ);
		}

		Location right = new Location(l.getX() + 1, l.getY());
		if (this.isPiece(right)) {
			Location rightJ = new Location(l.getX() + 2, l.getY());
			if (this.isEmpty(rightJ))
				jps.add(rightJ);
		}
		Location leftUp = new Location(l.getX(), l.getY() - 1);
		if (this.isPiece(leftUp)) {
			Location leftUpJ = new Location(l.getX(), l.getY() - 2);
			if (this.isEmpty(leftUpJ))
				jps.add(leftUpJ);
		}

		Location leftDown = new Location(l.getX() - 1, l.getY() + 1);
		if (this.isPiece(leftDown)) {
			Location leftDownJ = new Location(l.getX() - 2, l.getY() + 2);
			if (this.isEmpty(leftDownJ))
				jps.add(leftDownJ);
		}

		Location rightUp = new Location(l.getX() + 1, l.getY() - 1);
		if (this.isPiece(rightUp)) {
			Location rightUpJ = new Location(l.getX() + 2, l.getY() - 2);
			if (this.isEmpty(rightUpJ))
				jps.add(rightUpJ);
		}
		Location rightDown = new Location(l.getX(), l.getY() + 1);
		if (this.isPiece(rightDown)) {
			Location rightDownJ = new Location(l.getX(), l.getY() + 2);
			if (this.isEmpty(rightDownJ))
				jps.add(rightDownJ);
		}
		System.out.println("size= " + jps.size());
		return jps;
	}

	private boolean isPiece(Location l) {
		if (!this.isOutOfBound(l)) {
		//	System.out.println("is piece");
			return this.getValue(l) == 1 || this.getValue(l) == 2;

		}
		return false;
	}

	private boolean isEmpty(Location l) {
		if (!this.isOutOfBound(l)) {
			System.out.println("is empty");
			return this.getValue(l) == 0;
		}
		return false;
	}

	@Override
	protected boolean isValidSelection(Location l) {
		if (super.isValidSelection(l)) {
			return this.getValue(l) == currentPlayer.getFlag();
		}
		return false;
	}

	@Override
	protected boolean isValidDestination(Location des) {
		if (!super.isValidDestination(des))
			return false;
		if (this.move(this.selectedPiece, des))
			return true;
		if (this.jump(this.selectedPiece, des))
			return true;
		return false;
	}

	@Override
	public boolean selectDestination(Location des) {

		if (super.selectDestination(des)) {
			this.checkWon();
			this.end();
			return true;
		}
		return false;

	}

	@Override
	public void checkWon() {
		boolean hasWon = true;
		if (currentPlayer.getFlag() == 1) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j <= i; j++) {
					if (matrix[12 - j][i] != 1)
						hasWon = false;
				}
			}
		} else {
			for (int i = 16; i > 12; i--) {
				for (int j = 0; j <= 16 - i; j++) {
					if (matrix[4 + j][i] != 2)
						hasWon = false;
				}

			}

		}
		if (hasWon)
			this.win(currentPlayer);

	}

	@Override
	protected boolean computerTurn() {
		if (this.currentPlayer instanceof CCPlayer) {
			while (this.getState().equals(State.PIECE)) {
				currentPlayer.select(null);
			}
			while (!currentPlayer.select(null));
				
		//	System.out.println("ai move");
			return true;
			
		}

		return false;
	}

	@Override
	public void end() {
		this.currentPlayer.endTurn();
		this.selectedPiece = null;
		this.computerTurn();
		
	}

}
