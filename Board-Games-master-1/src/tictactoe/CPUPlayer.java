package TicTacToe;
import Chess.Board;

/*
 * class for a computer that controls the moves in a Tic-Tac-Toe game
 * can play in three difficulties: easy, medium, and impossible
 * All methods return boolean values to signify whether or not they did something. If the method
 * made a change to the board, it will return true, if it did not make any change to the board, 
 * it will return false.
 * constructor: takes a board, the integer representing the computer's shape, and the difficulty
 * setter: setBoard()
 * method: makeMove()
 * helper methods: makeMoveEasy(), makeMoveMedium(), makeMoveImpossible(), blockOpponent(), win(),
 * checkForTwoConsecutive(), checkForTwoNonConsecutive(), enterCornerWithNoNeighbors(), 
 * enterRandomCorner(), enterRandomCell(), enterSideWithNoNeighbors(), cornerWithNoOppositeOpponent(),
 * twoEmptySpaces(), opponentInOppositeCorners(), and enterRandomSide()
 */
public class CPUPlayer {
	private Board board; //the board
	private int opponent; //the integer representing the opponent's shape(1 for X and 2 for O)
	private int cpu;  //the integer representing the cpu's shape(1 for X and 2 for O)
	/*
	 * the integer representing the difficulty (1 for easy, 2 for medium, and 3 for impossible)
	 */
	private int difficulty; 
	
	/*
	 * @param the board
	 * @param the integer representing the computer's shape (1 for X and 2 for O)
	 */
	public CPUPlayer(Board b, int comp, int difficulty){
		board = b;
		cpu = comp;
		this.difficulty = difficulty;
		if(cpu == 1){opponent = 2;}
		else if(cpu == 2){opponent = 1;}
	}
	
	//@param the new Board
	public void setBoard(Board b){board = b;}
	
	/*
	 * makes the computer's next move
	 */
	public void makeMove(){
		if(difficulty == 1)
			makeMoveEasy();
		else if(difficulty == 2)
			makeMoveMedium();
		else if(difficulty == 3)
			makeMoveImpossible();
	}
	
	/*
	 * makes a move at the easy difficulty
	 */
	private void makeMoveEasy(){
		if(win()) //if the computer made a winning move
			return;
		if(board.getCell(1, 1) == 0) //if the middle cell is empty
			board.setCell(1, 1, cpu);
		
		else {
			enterRandomCell(); //otherwise, move in a random cell
		} 
	}
	
	/*
	 * makes a move at the medium difficulty
	 */
	private void makeMoveMedium(){
		if(win()) //if the computer can make a winning move, it does
			return;
		else if(blockOpponent()) //if the user is about to win, he/she is blocked
			return;
		else if(board.getCell(1, 1) == 0) //if the middle cell is empty
			board.setCell(1, 1, cpu);
		//to make the medium mode easier
		else if(board.getNumOccupied() == 1 || board.getNumOccupied() == 2){
			enterSideWithNoNeighbors();
		}
		else
			enterRandomCell();  //otherwise, move in a random cell
		
	}
	
	/*
	 * makes a move at the impossible difficulty
	 */
	private void makeMoveImpossible(){
		//if the cpu is about to win, it wins
		if(win()){
			return;
		}
		//if the opponent is about to win, the computer blocks him/her
		if(blockOpponent()){
			return;
		}
		//if the center is empty
		if(board.getCell(1, 1) == 0){
			board.setCell(1, 1, cpu);
			return;
		}
		if(board.getNumOccupied() == 3 && opponentInOppositeCorners()){ //imp one
			enterRandomSide();
			return;
		}
		//enter a winning position
		if(board.getCell(1, 1) == cpu && board.getNumOccupied() == 4){
			if(enterCornerWithNoNeighbors())
				return;
			else{
				enterRandomCorner();
				return;
			}
		}
		//if there are two empty corners, the computer will move in one of them
		if(cornerWithNoOppositeOpponent()){
			return;
		}
		//if there are 2 non consecutive empty spaces, the computer will move in one of them
		if(twoEmptySpaces()){
			return;
		}
		//if there is a corner with no neighbors, enter it
		if(enterCornerWithNoNeighbors()){
			return;
		}
		//if there is a side cell with no neighbors, then the computer will move in it
		if(enterSideWithNoNeighbors()){
			return;
		}
		//if there isn't a corner with no neighbors, then enter a random corner
		if(enterRandomCorner()){
			return;
		}
		else{
			enterRandomCell(); //if all above conditions fail, then enter a random cell
		}
	}
	
	private boolean opponentInOppositeCorners(){
	 return (board.getCell(0, 0) == opponent && board.getCell(2, 2) == opponent) ||
				(board.getCell(2, 0) == opponent && board.getCell(0, 2) == opponent);
	}
	
	/*
	 * if the player is about to win, the computer blocks him/her
	 * if the player is not about to win, this method does nothing
	 * @return true if the method did something
	 * @return false if it did nothing
	 */
	private boolean blockOpponent(){
		if(checkForTwoConsecutive(opponent))
			return true;
		else if(checkForTwoNonConsecutive(opponent))
			return true;
		else
			return false;
	}
	
	/*
	 * if the computer is about to win, it makes its winning move
	 * if the computer is not 1 move away from winning, this method does nothing
	 * @return true if the method did something
	 * @return false if it did nothing
	 */
	private boolean win(){
		if(checkForTwoConsecutive(cpu))
			return true;
		else if(checkForTwoNonConsecutive(cpu))
			return true;
		else
			return false;
	}
	
	/*
	 * if there is a pair of empty opposite corners, the computer will enter one of them
	 * @return true if the method did something
	 * @return false if it did nothing 
	 */
	private boolean cornerWithNoOppositeOpponent(){
		if(board.getCell(0, 0) == 0 && board.getCell(2, 2) == 0){
			board.setCell(2, 2, cpu);
			return true;
		}
		if(board.getCell(2, 0) == 0 && board.getCell(0, 2) == 0){
			board.setCell(2, 0, cpu);
			return true;
		}
		return false;
	}
	
	/*
	 * checks for 2 consecutive numbers anywhere in the board and places the cpu's shape at the 
	 * end of these numbers
	 * for example, if the top left and top middle cells are contain the number sent in the 
	 * parameter, then the cpu's shape will be placed in the top right cell
	 * @param the number to check for (find 2 consecutive occurrences of the number in the parameter)
	 * @return true if the method did something
	 * @return false if it did nothing
	 */
	private boolean checkForTwoConsecutive(int num){
		// check top left corner
		if(board.getCell(0, 0) == 0){
			//check for consecutive occurrences of num
			
			if(board.getCell(0, 1) == num && board.getCell(0, 2) == num){
				board.setCell(0, 0, cpu);
				return true;
			}
			if(board.getCell(1, 1) == num && board.getCell(2, 2) == num){
				board.setCell(0, 0, cpu);
				return true;
			}
			if(board.getCell(1, 0) == num && board.getCell(2, 0) == num){
				board.setCell(0, 0, cpu);
				return true;
			}
		}
		// check top right corner
		if(board.getCell(0, 2) == 0){
			//check for consecutive occurrences of num
			
			if(board.getCell(0, 1) == num && board.getCell(0, 0) == num){
				board.setCell(0, 2, cpu);
				return true;
			}
			if(board.getCell(1, 1) == num && board.getCell(2, 0) == num){
				board.setCell(0, 2, cpu);
				return true;
			}
			if(board.getCell(1, 2) == num && board.getCell(2, 2) == num){
				board.setCell(0, 2, cpu);
				return true;
			}
		}
		//check bottom left corner
		if(board.getCell(2, 0) == 0){
			//check for consecutive occurrences of num
			
			if(board.getCell(1, 0) == num && board.getCell(0, 0) == num){
				board.setCell(2, 0, cpu);
				return true;
			}
			if(board.getCell(1, 1) == num && board.getCell(0, 2) == num){
				board.setCell(2, 0, cpu);
				return true;
			}
			if(board.getCell(2, 1) == num && board.getCell(2, 2) == num){
				board.setCell(2, 0, cpu);
				return true;
			}
		}
		//check bottom right corner
		if(board.getCell(2, 2) == 0){
			//check for consecutive occurrences of num
			
			if(board.getCell(1, 2) == num && board.getCell(0, 2) == num){
				board.setCell(2, 2, cpu);
				return true;
			}
			if(board.getCell(1, 1) == num && board.getCell(0, 0) == num){
				board.setCell(2, 2, cpu);
				return true;
			}
			if(board.getCell(2, 1) == num && board.getCell(2, 0) == num){
				board.setCell(2, 2, cpu);
				return true;
			}
		}

		/*
		 * if all previous checks failed, it is impossible to have 2 consecutive nums
		 * without the center equaling num 
		 */
		if(board.getCell(1, 1) != num)
			return false;

		/*
		 * the reason why (1, 1) is not checked in the following statements is because it is known
		 * that (1, 1) contains num from the following condition
		 */
		
		//check middle of top row
		if(board.getCell(0,  1) == 0 && board.getCell(2, 1) == num){
			board.setCell(0,  1, cpu);
			return true;
		}
		//check the leftmost cell of the middle row
		if(board.getCell(1, 0) == 0 && board.getCell(1, 2) == num){
			board.setCell(1, 0, cpu);
			return true;
		}
		//check the rightmost cell of the middle row
		if(board.getCell(1, 2) == 0 && board.getCell(1, 0) == num){
			board.setCell(1, 2, cpu);
			return true;
		}
		//check the middle cell of the bottom row
		if(board.getCell(2, 1) == 0 && board.getCell(0, 1) == num){
			board.setCell(2, 1, cpu);
			return true;
		}
		return false; //if the methods does nothing, return false
	}
	
	/*
	 * checks for 2 non consecutive occurrences of a number and places the cpu's shape between 
	 * the two places where num is found
	 * for example, if num is found in spots (0, 0) and (0, 2) then the cpu's shape will be 
	 * placed in spot (0, 1) 
	 * @param the number to be checked for (find two non consecutive occurrences of num)
	 * @return true if the method did something
	 * @return false if it did nothing
	 */
	private boolean checkForTwoNonConsecutive(int num){
		//if the center is surrounded by two occupants being checked for
		if((board.getCell(1, 1) == 0) && ((board.getCell(0, 0) == num && board.getCell(2, 2) == num) ||
				(board.getCell(0, 2) == num && board.getCell(2, 0) == num) ||
				(board.getCell(0, 1) == num && board.getCell(2, 1) == num) ||
				(board.getCell(1, 0) == num && board.getCell(1, 2) == num))){
			board.setCell(1, 1, cpu);
			return true;
		}
		//check the top left and top right cells
		if(board.getCell(0, 0) == num && board.getCell(0, 2) == num && board.getCell(0, 1) == 0){
			board.setCell(0, 1, cpu);
			return true;
		}
		//check the top left and bottom left cells
		if(board.getCell(0, 0) == num && board.getCell(2, 0) == num && board.getCell(1, 0) == 0){
			board.setCell(1, 0, cpu);
			return true;
		}
		//check the bottom left and bottom right cells
		if(board.getCell(2, 0) == num && board.getCell(2, 2) == num && board.getCell(2, 1) == 0){
			board.setCell(2, 1, cpu);
			return true;
		}
		//check the top right and bottom right cells
		if(board.getCell(0, 2) == num && board.getCell(2, 2) == num && board.getCell(1, 2) == 0){
			board.setCell(1, 2, cpu);
			return true;
		}
		return false;
	}
	
	/*
	 * enter a random side
	 */
	private void enterRandomSide(){
		if(board.getCell(0, 1) == 0){
			board.setCell(0, 1, cpu);
			return;
		}
		else if(board.getCell(1, 0) == 0){
			board.setCell(1, 0, cpu);
			return;
		}
		else if(board.getCell(1, 2) == 0){
			board.setCell(1, 2, cpu);
			return;
		}
		else if(board.getCell(2, 1) == 0){
			board.setCell(2, 1, cpu);
			return;
		}
	}
	
	/*
	 * if there are two non consecutive empty spaces, the computer will move in one of them
	 * @return true if the method did something
	 * @return false if it did nothing 
	 */
	private boolean twoEmptySpaces(){
		//check different pairs of cells to determine possible candidates for 2 empty non-consecutive spaces
		
		if(board.getCell(0, 0) == 0 && board.getCell(2, 2) == 0){
			//if one of the corners has no neighbors, then it is the better corner to enter
			if(enterCornerWithNoNeighbors())
				return true;
			//otherwise, it does not matter which corner is entered, so (2, 2) is used as default
			else{
				board.setCell(2, 2, cpu);
				return true;
			}
		}
		else if(board.getCell(2, 0) == 0 && board.getCell(0, 2) == 0){
			//if one of the corners has no neighbors, then it is the better corner to enter
			if(enterCornerWithNoNeighbors())
				return true;
			//otherwise, it does not matter which corner is entered, so (2, 0) is used as a default
			else{
				board.setCell(2, 0, cpu);
				return true;
			}
		}
		else if(board.getCell(1, 0) == 0 && board.getCell(1, 2) == 0){
			//if one of the sides has no neighbors, then it is the better side to enter
			if(enterSideWithNoNeighbors())
				return true;
			//otherwise, it does not matter which side is entered, so (1, 0) is used as a default
			else{
				board.setCell(1, 0, cpu);
				return true;
			}
		}
		else if(board.getCell(0, 1) == 0 && board.getCell(2, 1) == 0){
			//if one of the sides has no neighbors, then it is the better side to enter
			if(enterSideWithNoNeighbors())
				return true;
			//otherwise, it does not matter which side is entered, so (1, 0) is used as a default
			else{
				board.setCell(2, 1, cpu);
				return true;
			}
		}
		return false; //if the method did nothing, return false
	}
	
	/*
	 * finds a random unoccupied corner and places the cpu's shape in it
	 * if there is no empty corner, then the method does nothing
	 * @return true if the method did something
	 * @return false if it did nothing 
	 */
	private boolean enterRandomCorner(){
		int [][] cells = board.getCells();
		//find an empty corner and enter it
		if(cells[0][0] == 0){
			board.setCell(0, 0, cpu);
			return true;
		}
		else if(cells[2][0] == 0){
			board.setCell(2, 0, cpu);
			return true;
		}
		else if(cells[0][2] == 0){
			board.setCell(0, 2, cpu);
			return true;
		}
		else if(cells[2][2] == 0){
			board.setCell(2, 2, cpu);
			return true;
		}
		return false; //if the method could not find an empty corner to enter, return false
			
	}
	
	/*
	 * enters a random corner with no neighbors
	 * if no such corner exists, then the method does nothing
	 * @return true if the method did something
	 * @return false if it did nothing 
	 */
	private boolean enterCornerWithNoNeighbors(){
		/*
		 * checks the four corners for being empty and having neighbors
		 * places the cpu's shape in the first empty corner with no neighbors encountered
		 */
		
		if(board.getCell(0, 0) == 0){
			if(board.getCell(1, 0) == 0 && board.getCell(0, 1) == 0){
				board.setCell(0, 0, cpu);
				return true;
			}
		}
		if(board.getCell(2, 0) == 0){
			if(board.getCell(1, 0) == 0 && board.getCell(2, 1) == 0){
				board.setCell(2, 0, cpu);
				return true;
			}
		}
		if(board.getCell(0, 2) == 0){
			if(board.getCell(0, 1) == 0 && board.getCell(1, 2) == 0){
				board.setCell(0, 2, cpu);
				return true;
			}
		}
		if(board.getCell(2, 2) == 0){
			if(board.getCell(1, 2) == 0 && board.getCell(2, 1) == 0){
				board.setCell(2, 2, cpu);
				return true;
			}
		}
		return false;
	}
	
	/*
	 * finds a side with no neighbors and enters it
	 * @return true if the method did something
	 * @return false if it did nothing 
	 */
	private boolean enterSideWithNoNeighbors(){
		/*
		 * find a side cell with no neighbors and enter it
		 */
		if(board.getCell(1, 0) == 0){
			if(board.getCell(0,  0) == 0 && board.getCell(2, 0) == 0){
				board.setCell(1, 0, cpu);
				return true;
			}
		}
		if(board.getCell(1, 2) == 0){
			if(board.getCell(0,  2) == 0 && board.getCell(2, 2) == 0){
				board.setCell(1, 2, cpu);
				return true;
			}
		}
		if(board.getCell(0, 1) == 0){
			if(board.getCell(0,  0) == 0 && board.getCell(0, 2) == 0){
				board.setCell(0, 1, cpu);
				return true;
			}
		}
		if(board.getCell(2, 1) == 0){
			if(board.getCell(2,  0) == 0 && board.getCell(2, 2) == 0){
				board.setCell(2, 1, cpu);
				return true;
			}
		}
		return false; //if there is no side cell with no neighbors, return false
	}
	
	/*
	 * finds a random cell and places the cpu's shape in it
	 * if there is no empty cell, this method does nothing
	 */
	private void enterRandomCell(){
		int [][] cells = board.getCells();
		for(int row = 0; row < cells.length; row++){
			for(int col = 0; col < cells[row].length; col++){
				if(cells[row][col] == 0){
					board.setCell(row, col, cpu);
					return;
				}
			}
		}
	}
}
