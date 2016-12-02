/**
 * This class represents a board in the classic game of Battleship.
 */
package BoardGames.Battleship;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

import javax.swing.JPanel;

public class BattleshipBoard extends JPanel implements ActionListener{

	private final int rows, columns;
	private boolean firedCorrectly = false, isPlayable;
	private final boolean implementsStrategy, usingFilesForBoard;
	private FireButton[][] board;
	private char[][] charBoard;
	private String boardOwner;
	private JLabel owner;
	private final Color buttonBgColor;
	/**
	 * Constructor for the Battleship Board. It allows for boards of arbitrary
	 * rows and columns to be created
	 * 
	 * @param numRows
	 *            The number of rows
	 * @param numCols
	 *            The number of columns
	 */
	public BattleshipBoard(int numRows, int numCols, String boardOwner, boolean implementsStrategy, Color buttonBgColor) {
		this.rows = numRows;
		this.columns = numCols;
		this.implementsStrategy=implementsStrategy;
		this.boardOwner = boardOwner;
		this.usingFilesForBoard=false;
		this.buttonBgColor = buttonBgColor;
		
		this.setSize(450, 490);
		board = new FireButton[columns][rows]; // 30x30 pixel squares.
		owner = new JLabel(boardOwner); 
		owner.setText(boardOwner+"'s Board");
		owner.setSize(450,40);
		owner.setBackground(Color.gray);
		//this.setLayout(g1);
		this.add(owner, BorderLayout.CENTER);
		initDefaultBoard(buttonBgColor);	
	}
	

	/**
	 * Constructor for the Battleship Board.  
	 * File must have 10 lines, with exactly 10 characters per line.
	 * '.' represents EMPTY.  A unique number (0, 1, 2, 3, or 4) will represent each ship.
	 * Example File:
	 * ..........
	 * ..........
	 * ...11111..
	 * ..02......
	 * ..02......
	 * ..02......
	 * ...2......
	 * ...33344..
	 * ..........
	 * ..........
	 * 
	 * @param boardFile a text file with a 10x10 character grid, representing a battleship board
	 * @throws FileNotFoundException if the board file is not found
	 * @author richmaja
	 */
	public BattleshipBoard(File boardFile, String boardOwner, boolean implementsStrategy, Color buttonBgColor) throws FileNotFoundException{
		this.boardOwner = boardOwner;
		this.columns = 10;
		this.rows = 10;
		board = new FireButton[columns][rows];
		this.implementsStrategy = implementsStrategy;
		this.buttonBgColor = buttonBgColor;
		this.usingFilesForBoard=true;
		this.charBoard = new char[columns][rows];
		//Add 5 entries to shipInfo
		

		owner = new JLabel(boardOwner); //TODO: Fix this so it correctly displays the label w/ GridLayout
		owner.setText(boardOwner+"'s Board");
		owner.setSize(450,40);
		owner.setBackground(Color.gray);
		this.setLayout(null);
		this.add(owner, BorderLayout.CENTER);
		
		
		try{
			if(!boardFile.exists()) throw new FileNotFoundException("Error Reading: "+boardFile.getName());

			FileReader fr = new FileReader(boardFile);
			BufferedReader br = new BufferedReader(fr);
			String line;
			int lineCounter = 0;
			while((line = br.readLine())!=null){
				for(int i = 0; i < line.length(); i++){
					char currentCharacter = line.charAt(i);
					charBoard[i][lineCounter] = currentCharacter;
				}
				lineCounter++;
			}
			br.close();
			fr.close();
			for(int c = 0; c < this.columns; c++){
				for(int r = 0; r < this.rows; r++){
					try{
						int shipIdVal = Integer.parseInt(charBoard[c][r]+"");
						if(shipIdVal>=0 && shipIdVal < 10){
							try {
								initBoardSpace(c, r, true, buttonBgColor);
								placeShip(c, r, c, r, shipIdVal);
								this.add(board[c][r]);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						else{
							initBoardSpace(c, r, false, buttonBgColor);
							this.add(board[c][r]);
						}
					}catch(NumberFormatException e){
						initBoardSpace(c, r, false, buttonBgColor);
						this.add(board[c][r]);
					}
				}
			}
		} catch(IOException e){
			System.out.println("Error: "+e.getMessage());
		}

	}
	/**
	 * Creates a default board then adds it to the BattleshipBoard container. Not for use 
	 * when reading a board from a file.
	 * @param buttonBgColor
	 */
	public void initDefaultBoard(Color buttonBgColor){
		JPanel gameBoard= new JPanel();
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[i].length; j++){
				initBoardSpace(i, j, false, buttonBgColor);
				gameBoard.add(board[i][j]);
			}
		}
		this.add(gameBoard);
	}
	
	/**
	 * Initializes a single board space and everything on that board space required for
	 * the game to work correctly.
	 * @param col
	 * @param row
	 * @param isShipSpace
	 * @param buttonBgColor
	 */
	public void initBoardSpace(int col, int row, boolean isShipSpace, Color buttonBgColor){
		board[col][row] = new FireButton(col, row, isShipSpace, buttonBgColor);
		board[col][row].setSize(45, 45);
		board[col][row].setLocation(45*col, 45*row+40);
		board[col][row].setActionCommand(col+" "+row);
		board[col][row].addActionListener(this);
	}

	/**
	 * Getter for the number of rows in the board
	 * 
	 * @return The number of rows in the Battleship board
	 */
	public int getNumRows() {
		// To be filled in
		return rows;
	}

	/**
	 * Getter for the number of columns in the board
	 * 
	 * @return The number of columns in the Battleship board
	 */
	public int getNumCols() {
		// To be filled in
		return columns;
	}

	/**
	 * Places the battleship at the starting coordinates (startCol,startRow) and the
	 * end coordinates (endCol,endRow) inclusive.
	 * 
	 * @param startCol
	 *            The starting Col coordinate
	 * @param startRow
	 *            The starting Row coordinate
	 * @param endCol
	 *            The ending Col coordinate
	 * @param endRow
	 *            The ending Row coordinate
	 * @throws Exception
	 *             If ship is placed diagonally
	 *             If the coordinates are out of bounds. A coordinate is in bounds if 0 <= col < (number of columns - 1) and 0 <= row < (number of rows - 1).  
	 *             If it overlaps with another battleship  
	 *             If startCol > endCol or startRow > endRow. 
	 */
	public void placeShip(int startCol, int startRow, int endCol, int endRow, int shipId) // Has something implemented so a ship can not be more than 5 spaces
		throws Exception{
				
				if(overlapsOtherBattleship(startCol, startRow, endCol, endRow)){
					throw new BattleshipException("There is already a ship where you are trying to place yours!!!");
				}
				else if(!usingFilesForBoard){
					if (startCol >= 0 && endCol < columns && startCol < endCol) {
						
						if(startRow < 0 || startRow >= rows ||
								endRow!= startRow){
							throw new BattleshipException("You tried making a bigger than possible ship!!!");
						}
						else{
							setShip(startCol, startRow, endCol, endRow, shipId);
						}
					}
					else if (startRow >= 0 && endRow < rows && startRow < endRow) {
						if(startCol < 0 || startCol >= columns ||
								endCol!= startCol){
							throw new BattleshipException("You tried making a bigger than possible ship!!!");
						}
						else{
							setShip(startCol, startRow, endCol, endRow, shipId); //(int) (Math.random()*1000000) +1
						}
					}
					else{
						throw new BattleshipException("You entered incorrect coordinates for the ship's placement!!!");
					}
				}
				else{
					setShip(startCol, startRow, endCol, endRow, shipId); //(int) (Math.random()*1000000) +1
				}
	}
	
	/**
	 * Fires a shot at a battleship
	 * 
	 * @param col
	 *            The col coordinate of the shot
	 * @param row
	 *            The row coordinate of the shot
	 * @return true if an enemy battleship is hit, false otherwise
	 * @throws Exception
	 *             if col or row are out of bounds
	 */
	public boolean fireShot(int col, int row) throws BattleshipException {
		if(col >= 0 && col <columns && (row >= 0 && row < rows)){
			if(board[col][row].getNode().isShipSpace()){
				if(!board[col][row].getNode().getShip().isHit()){
					return true;
				}
				else if(board[col][row].getText().equals("")){
					return false;
				}
				else{
					throw new BattleshipException("You've already shot these coordinates!");
				}
			}
			else {
				if(board[col][row].getText().equals("")){
					return false;
				}
				else{
					throw new BattleshipException("You've already shot these coordinates!");
				}
			}
		}
		else{
			throw new BattleshipException("Your shot was not in bounds!");
		}
	}

	/**
	 * Places the ship on a node and turns a node into a ship space.
	 * 
	 * @param startCol
	 * @param startRow
	 * @param endCol
	 * @param endRow
	 * @param shipId
	 */
	public void setShip(int startCol, int startRow, int endCol, int endRow, int shipId){
		
		for(int i=startCol; i<=endCol; i++){
			for(int j=startRow; j<=endRow; j++){
				board[i][j].getNode().setShipSpace(true);
				board[i][j].getNode().setShip(shipId);// tbis will be the shipID
			}
		}	
	}
	
	/**
	 * Main function
	 * 
	 * @param args
	 *            Command line arguments
	 */
	
	
	/*****************************************
	 * Everything below is FOR BONUS ONLY
	 *****************************************/

	/**
	 * FOR BONUS ONLY: Gets the number of Battleships left
	 * 
	 * @return The number of battleships left
	 */
	public int getNumBattleshipsLeft() {
		// To be filled in
		ArrayList<Integer> shipsRemaining = new ArrayList<Integer>();
		
		for(int i=0; i<columns; i++){
			for (int j=0; j<rows; j++){
				if(board[i][j].getNode().isShipSpace()){
					if(!shipsRemaining.contains(board[i][j].getNode().getShip().getShipID()) &&
							!board[i][j].getNode().getShip().isHit()){
						shipsRemaining.add(board[i][j].getNode().getShip().getShipID());
					}
				}
			}
		}
		
		return shipsRemaining.size();
	}
	
	/**
	 * FOR BONUS ONLY: Returns true if game is over, false otherwise
	 * @return true if game is over, false otherwise
	 */
	public boolean isGameOver() {
		return getNumBattleshipsLeft()==0;
	}
	
	/**
	 * Returns if a ship is being placed on top of another ship.
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @return
	 */
	public boolean overlapsOtherBattleship(int startX, int startY, int endX, int endY){
		for(int i=startX; i<endX; i++){
			for(int j=startY; j<endY; j++){
				if(board[i][j].getNode().isShipSpace()){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns whether this board is being operated by an AI.
	 * @return implementsStrategy
	 */
	public boolean implementsStrategy(){
		return this.implementsStrategy;
	}
	
	/**
	 * Plays a type of strategy, will only be used for AI.
	 * @param strategy
	 */
	public void playStrategicMove(String strategy){
		while(!firedCorrectly){
			if(strategy.equals("random")){
				int col = (int) (Math.random() * 10); 
				int row = (int) (Math.random() * 10);
				try{
					if(fireShot(col, row)){
						hitShip(col, row);
					}
					else{
						missShip(col, row);
					}
					firedCorrectly=true;
				} catch(BattleshipException e){
					// Do nothing, need to continue the loop
				}
			}
			else if(strategy.equals("sequential")){
				for(int i = 0; i < board.length; i++){
					for (int j=0; j<board[i].length; j++){
						if(firedCorrectly){
							return;
						}
						try{
							if(fireShot(i,j)){
								hitShip(i, j);
							}
							else{
								missShip(i, j);
							}
							firedCorrectly=true;
							} catch (BattleshipException e){}
					}// end of j for loop
				}// end of i for loop
			}
			else if(strategy.equals("sequential_smart")){
				if(getNumOfShotsFired()<((board.length*board.length)/2)){
					for(int i = 0; i < board.length; i++){
						for (int j=0; j<board[i].length; j++){
							if(firedCorrectly){
								return;
							}
							try{
								if((i+2)%2==0){
									if((j+2)%2==0){
										if(fireShot(i,j)){
											hitShip(i, j);
										}
										else{
											missShip(i, j);
										}
										firedCorrectly=true;
									}
									else{
										continue;
									}
								}
								else{
									if((j+2)%2==1){
										if(fireShot(i,j)){
											hitShip(i, j);
										}
										else{
											missShip(i, j);
										}
										firedCorrectly=true;
									}
									else{
										continue;
									}
								}
							} catch (BattleshipException e){}
						}// end of j for loop
					}// end of i for loop
				}// end of if for playing smart
				else{
					playStrategicMove("sequential");
				}
			}// end of strategy for sequentially playing the game
			if(strategy.equals("smart")){
				// This strategy should incorporate the sequential strategy (randomized) 
			}
		}
	}
	/**
	 * Returns the board
	 * @return board
	 */
	public FireButton[][] getBoard(){
		return board;
	}
	
	// Shorthand for printing
	public void p(String s){
		System.out.println(s);
	}
	public void p(int i){
		System.out.print(i);
	}
	public void p(String s, boolean carriageReturn){
		System.out.print(s);
		if(carriageReturn)
			System.out.println();
	}

	/**
	 * Returns whether a shot was fired correctly -- used for switching between turns on players
	 * @return firedCorrectly
	 */
	public boolean firedCorrectly(){
		return this.firedCorrectly;
	}

	/**
	 * This operates the user end of the game.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(isPlayable && !implementsStrategy){
			this.firedCorrectly=false;
			String[] event = e.getActionCommand().split(" ");
			int col = Integer.parseInt(event[0]);
			int row = Integer.parseInt(event[1]);
			try {
				if(fireShot(col, row)){
					hitShip(col, row);
				}
				else {
					missShip(col, row);
				}
				firedCorrectly=true;
			}catch (Exception e1) {
				// TODO: add in a popup message telling them they did something wrong with the exception's message
			}
		}
		else{
			p("Not your turn "+boardOwner+"!!");
		}
	}
	
	/**
	 * Sets the board as being able to be played on by a user.
	 * @param playable
	 */
	public void setPlayable(boolean playable){
		this.isPlayable = playable;
		this.firedCorrectly=false;
	}
	
	/**
	 * Hits a ship on the designated coordinates. 
	 * All checks have already been done before performing this operation
	 * @param col
	 * @param row
	 */
	public void hitShip(int col, int row){
		board[col][row].setBackground(Color.YELLOW);
		board[col][row].setText("H");
		board[col][row].getNode().getShip().hit();
	}
	
	/**
	 * Misses a ship on the designated coordinates.
	 * All checks have already been done in other methods to confirm this shot was a miss.
	 * @param col
	 * @param row
	 */
	public void missShip(int col, int row){
		board[col][row].setBackground(Color.GREEN);
		board[col][row].setText("M");
	}
	
	/**
	 * Hides all the ships on a board.
	 */
	public void hideShips(){
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board.length; j++){
				if(board[i][j].getNode().isShipSpace()){
					board[i][j].setBackground(buttonBgColor);
					board[i][j].setText("");
				}
			}
		}
	}
	
	/**
	 * Returns the owner of the board
	 * @return boardOwner
	 */
	public String getOwner(){
		return this.boardOwner;
	}
	
	/**
	 * Returns the number of shots that have been fired
	 * @return numberOfShotsFired
	 */
	public int getNumOfShotsFired(){
		int counter=0;
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[i].length; j++){
				if(board[i][j].getText().equals("M")){
					counter++;
				}
				else if(board[i][j].getText().equals("H")){
					counter++;
				}
				else{}
			}
		}
		return counter;
	}

}