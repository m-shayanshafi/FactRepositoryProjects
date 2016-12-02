package Puzzle;

/*
 * class PieceHolder stores and controls the data and functions of 
 * a storage system for puzzle Pieces 
 * data: a 2-D array of Pieces, an int default number of rows, and 
 * an int default number of columns
 * default constructor: creates the array of Pieces with the default size of 3 by 3
 * constructor: takes an integer number of rows and an integer number of columns 
 * and creates an array of Pieces with the specified number of rows and columns
 * constructor: takes a Piece[][] and sets pieces to that Piece[][]
 * getters: getPieces(), getOccupant()
 * setter: setPieces()
 * methods: takePiece(), addPiece(), fillWithDefaultPieces(), isValid(), and clear()
 */
public class PieceHolder {
	private Piece[][] pieces;
	private final int numRows = 3;
	private final int numColumns = 3;
	
	/*
	 * creates an array of Pieces with the default size of three by three
	 */
	public PieceHolder(){
		pieces = new Piece[numRows][numColumns];
	}
	
	/*
	 * sets Pieces to a Piece[][] taken as an argument
	 * @param the Piece[][] to set pieces to
	 */
	public PieceHolder(Piece[][] p){
		pieces = p;
	}
	
	/*
	 * @param the number of rows for the array
	 * @param the number of columns for the array
	 * creates an array of Pieces based with the number of rows and columns specified in
	 * the parameter
	 */
	public PieceHolder(int rows, int columns){
		pieces = new Piece[rows][columns];
	}
	
	//@return the array of Pieces being stored
	public Piece[][] getPieces(){return pieces;}
	
	/*
	 * sets Pieces[][] to the array sent in the parameter
	 * @param the array to set Pieces[][] to
	 */
	public void setPieces(Piece[][] p){pieces = p;}
	
	/*
	 * returns a Piece at a specified location
	 * @param the row of the Piece to be returned
	 * @param the column of the Piece to be returned
	 * @return the Piece at the specified location
	 * @return null if the location specified is either empty or invalid
	 */
	public Piece getOccupant(int row, int column){
		if(isValid(row, column))
			return pieces[row][column];
		
		//if the specified location is not valid
		return null;
	}
	
	/*
	 * checks whether the Pieces are correctly placed in the pieces
	 * @return true if all pieces fit where the have been placed 
	 * @return false if a Piece is in a place where it does not fit
	 * @return false if the pieces is not full
	 */
	public boolean isSolved(){
		for(int row = 0; row < pieces.length; row++){
			for(int column = 0; column < pieces[row].length; column++){
				//if the pieces is not full
				if(pieces[row][column] == null)
					return false;
				
				//the Piece being checked
				Piece test = pieces[row][column];
				
				/*
				 * takes Piece out because the spot must be null to be checked correctly
				 * by canFit() (if the spot is not null, canFit() will return false 
				 * immediately)
				 */
				pieces[row][column] = null;
				
				//checks if every Piece that has been placed fits in its location
				if(!(test.canFit(row, column, pieces))){
					//puts the Piece back into pieces before returning
					pieces[row][column] = test;
					return false;
				}
					
				else
					//adds the Piece that was taken out back into pieces
					pieces[row][column] = test;
			}
		}
		return true;
	}
	
	/*
	 * fills the array of Pieces with the default Pieces for a puzzle
	 */
	public void fillWithDefaultPieces(){
		/*
		 * if the array is not big enough to hold the puzzle (number of rows or number
		 * number of columns is less than three
		 */
		if(pieces.length < 3 || pieces[0].length < 3)
			return;
		//the puzzle pieces
		pieces[0][0] = new Piece(0, 0, 4, 3, -3, -4);
		pieces[0][1] = new Piece(0, 1, -1, -2, 4, 1);
		pieces[0][2] = new Piece(0, 2, -2, -2, 4, 3);
		pieces[1][0] = new Piece(1, 0, -1, -4, 2, 4);
		pieces[1][1] = new Piece(1, 1, -1, -4, 1, 3);
		pieces[1][2] = new Piece(1, 2, -4, -2, 1, 1);
		pieces[2][0] = new Piece(2, 0, -4, -3, 1, 3);
		pieces[2][1] = new Piece(2, 1, 2, -2, -3, 3);
		pieces[2][2] = new Piece(2, 2, 4, -3, -2, 2);				
	}
	
	/*
	 * removes a piece at a specified location from the PieceHolder and returns it
	 * @param the row of the piece to be removed
	 * @param the column of the piece to be removed
	 * @return the Piece at the specified location
	 */
	public Piece takePiece(int r, int c){
		if(isValid(r,c)){
			Piece p = pieces[r][c];
			pieces[r][c] = null;
			return p;
		}
		return null;
	}
	
	/*
	 * @param the row in which to set the Piece
	 * @param the column in which to set the Piece
	 * @param the Piece to be set at the specified location
	 */
	public void addPiece(int r, int c, Piece p){
		if(isValid(r, c) && pieces[r][c] == null)
			pieces[r][c] = p;
	}
	
	/*
	 * @return true if the location is within the bounds of pieces[][]
	 * @return false otherwise
	 */
	public boolean isValid(int r, int c){
		return r >= 0 && r < pieces.length && c >= 0 && c < pieces[0].length; 
	}
	
	//clears the PieceHolder
	public void clear(){pieces = new Piece[numRows][numColumns];}
	
	/*
	 * @return a String form of the PieceHolder
	 */
	public String toString(){
		String b =

		"";
		for(int i=0; i<numRows; i++){
		for (int j =0; j<numColumns; j++){
		if(pieces[i][j].getShape(Side.top)>0)
		b = b +

		"+" + pieces[i][j].getShape(Side.top);
		else
		b = b +

		pieces[i][j].getShape(Side.top);
		if(pieces[i][j].getShape(Side.right)>0)
		b = b +

		"+" + pieces[i][j].getShape(Side.right);
		else
		b = b +

		pieces[i][j].getShape(Side.right);
		if(pieces[i][j].getShape(Side.bottom)>0)
		b = b +

		"+" + pieces[i][j].getShape(Side.bottom);
		else
		b = b +

		pieces[i][j].getShape(Side.bottom);
		if(pieces[i][j].getShape(Side.left)>0)
		b = b +

		"+" + pieces[i][j].getShape(Side.left) + " ";
		else
		b = b +

		pieces[i][j].getShape(Side.left)+ " ";
		}

		b = b +

		'\n';
		}

		return b;
	}
}
