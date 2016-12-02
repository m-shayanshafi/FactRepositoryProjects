package Chess;
/*
 * This class creates a board and contains methods that:
 * sets values to the board's cells
 * returns values of the board's cells
 * returns number of rows in board
 * returns number of columns in board
 * toggles cells
 * resets board
 * determines is point is in bounds
 * counts neighbors around a point
 */
public class Board{

	private int[][] a;

	//Constructs a default board of size 20 X 20
	public Board(){
		a = new int[3][3];
		reset();
	}

	//receives a 2D array and constructs a board of that size
	public Board(int[][] array){
		a = array;
		reset();
	}

	//constructs a board using the received number of rows and columns
	public Board(int rows, int columns){
		a = new int[rows][columns];
		reset();
	}

	//receives a given position and assigns the given value (if it's 1 or 0) to that position
	public void setCell(int row, int column, int value){
		if (row >= 0 && column >= 0){
			a[row][column] = value;
			
		}
	}

	//receives a position and returns the integer value at that position
	public int getCell(int row, int col){
		int value = a[row][col];
		return value;
	}

	//returns the number of columns in the board
	public int getNumCols(){
		int length = a[0].length;
		return length;
	}

	//returns the number of rows in the board
	public int getNumRows(){
		int height = a.length;
		return height;
	}
	
	//@return the int[][] representing the cells of the board
	public int[][] getCells(){
		return a;
	}

	/*//recieves a given position and toggles the value at that position (ex. if the value is 1, then it's set to 0 and vice versa)
	public void toggle(int row, int col){
		int value = getCell(row,col);
		if(value == 0){
			setCell(row,col,1);
		}
		else
			setCell(row,col,0);
	}*/

	//resets all values on the board to 0
	public void reset(){
		for(int row = 0; row < a.length; row++){
			for(int col = 0; col < a[0].length; col++){
				setCell(row, col, 0);
			}
		}
	}

	/*receives a position and determines if the position is in the array
	 *returns true if the point is in bounds
	 *returns false if it isn't
	 */
	public boolean isInBounds(int row, int col){
		if(row >= 0 && row < a.length && col >= 0 && col < a[0].length){
			return true;
		}
		else
			return false;
	}
	
	//@return the number of occupied cells in the board
	public int getNumOccupied(){
		int num = 0;
		for(int row = 0; row < a.length; row++){
			for(int col = 0; col < a[row].length; col++){
				if(a[row][col] != 0)
					num++;
			}
		}
		return num;
	}
	
	//@return whether or not all the cells in the board are occupied
	public boolean isFull(){
		for(int i = 0; i < a.length; i++){
			for(int j = 0; j < a[i].length; j++){
				if(a[i][j] == 0)
					return false;
			}
		}
		return true;
	}

/*	//receives a position, counts the number of neighbors around it, and returns the number of neighbors
	public int countNeighbors(int row, int col){
		int count = 0;
		for (int i = -1; i < 2; i++){
			for(int j = -1; j < 2; j++){
				if(isInBounds(row + i, col + j)){//<--inInBounds assumes true!!!! OOOHHHHHHHH
					count += getCell(row + i, col + j);
				}
			}
		}
		if (getCell(row,col) == 1){
			count = count - 1;
		}
		return count;
	}*/
	
	
	
	//converts the board(which is a 2D array) into a string
	public String toString(){
		String s = "";
		for(int i = 0; i < a.length; i++){
			s+= "\n";
			for(int j = 0; j < a[0].length; j++){
				s += a[i][j];
			}
		}
		return s;
	}

}