package Puzzle;
import java.util.ArrayList;

/*
 * class Solver solves a three by three puzzle given the puzzle Pieces
 * data: an ArrayList<Piece> puzzle, a Piece[][] solution, int spot1Loc, int spot2Loc, 
 * int spot3Loc, int spot4Loc, int spot5Loc, int spot6Loc, int spot7Loc, int spot8Loc,
 * and int spot9Loc
 * constructor: takes a Piece[][] as an argument, creates the ArrayList<Piece> 
 * puzzle using the Pieces from the Piece[][] in the argument, and creates the 
 * Piece[][] solution with the same dimensions as the Piece[][] in the argument
 * getter: getSolution()
 * method: solve(), solved()
 * helper methods: solve(int), resetZeros(int), redo(int),
 * and addPiece(int, int, Piece),     
 */
public class Solver {
	private ArrayList<Piece> puzzle; //stores the unsolved puzzle pieces
	
	/*
	 * stores the solved puzzle pieces
	 * arbitrary numbering system for spots in the 2-D array:
	 * spot 1 is [1][1]
	 * spot 2 is [1][2]
	 * spot 3 is [0][2]
	 * spot 4 is [0][1]
	 * spot 5 is [0][0]
	 * spot 6 is [1][0]
	 * spot 7 is [2][0]
	 * spot 8 is [2][1]
	 * spot 9 is [2][2]
	 */
	private Piece[][] solution;
	
	/*
	 * store the index in puzzle where the given spot in the array left off in its search
	 * for Pieces that fit in that spot
	 * these values are reset to 0 every time the Piece in the previous spot 
	 * of the solution is changed because if the Piece in the previous spot in the 
	 * solution is different, then the current spot would need to restart its search 
	 * in puzzle from index 0
	 */
	private int spot1Loc = 0;
	private int spot2Loc = 0;
	private int spot3Loc = 0;
	private int spot4Loc = 0;
	private int spot5Loc = 0;
	private int spot6Loc = 0;
	private int spot7Loc = 0;
	private int spot8Loc = 0;
	private int spot9Loc = 0;
	
	/*
	 * @param the Piece[][] storing the puzzle pieces
	 * adds the Pieces in the Piece[][] in the parameter to the ArrayList<Piece> puzzle
	 * creates the Piece[][] solution with the same dimensions as the Piece[][] in
	 * the parameter
	 */
	public Solver(Piece [][] p){
		puzzle = new ArrayList<Piece>();
		
		for(int row = 0; row < p.length; row++){
			for(int column = 0; column < p[row].length; column++){
				puzzle.add(p[row][column]);
			}
		}
		solution = new Piece[p.length][p[0].length];
	}
	
	/*
	 * @return the solution to the puzzle stored in a Piece[][]
	 * in order for the correct solution to be returned, the solve() method must be 
	 * called first, otherwise, this method will return an empty or incorrect 
	 * solution
	 */
	public Piece[][] getSolution(){return solution;}
	
	/*
	 * checks whether the Pieces are correctly placed in the solution
	 * @return true if all pieces fit where the have been placed 
	 * @return false if a Piece is in a place where it does not fit
	 * @return false if the solution is not full
	 */
	public boolean solved(){
		for(int row = 0; row < solution.length; row++){
			for(int column = 0; column < solution[row].length; column++){
				//if the solution is not full
				if(solution[row][column] == null)
					return false;
				
				//the Piece being checked
				Piece test = solution[row][column];
				
				/*
				 * takes Piece out because the spot must be null to be checked correctly
				 * by canFit() (if the spot is not null, canFit() will return false 
				 * immediately)
				 */
				solution[row][column] = null;
				
				//checks if every Piece that has been placed fits in its location
				if(!(test.canFit(row, column, solution))){
					//puts the Piece back into solution before returning
					solution[row][column] = test;
					return false;
				}
					
				else
					//adds the Piece that was taken out back into solution
					solution[row][column] = test;
			}
		}
		return true;
	}
	
	/*
	 * resets the locations for the data storing the index of the array that a spot 
	 * in the solution left off checking to 0
	 * @param the location to start setting zeros at
	 */
	private void resetZeros(int start){
		/*
		 * starts with 2 because spotLoc1 should never need to be reset to 0;
		 * it is the only spot that runs through puzzle sequentially
		 * sets the spotLocs to 0 starting from the specified value
		 */
		switch(start){
			case 2:
				spot2Loc = 0;
				spot3Loc = 0;
				spot4Loc = 0;
				spot5Loc = 0;
				spot6Loc = 0;
				spot7Loc = 0;
				spot8Loc = 0;
				spot9Loc = 0;
			case 3:
				spot3Loc = 0;
			 	spot4Loc = 0;
			 	spot5Loc = 0;
			 	spot6Loc = 0;
			 	spot7Loc = 0;
			 	spot8Loc = 0;
			 	spot9Loc = 0;
			case 4:
				spot4Loc = 0;
				spot5Loc = 0;
				spot6Loc = 0;
				spot7Loc = 0;
				spot8Loc = 0;
				spot9Loc = 0;
			case 5:
				spot5Loc = 0;
				spot6Loc = 0;
				spot7Loc = 0;
				spot8Loc = 0;
				spot9Loc = 0;
			case 6:
				spot6Loc = 0;
				spot7Loc = 0;
				spot8Loc = 0;
				spot9Loc = 0;
			case 7:
				spot7Loc = 0;
				spot8Loc = 0;
				spot9Loc = 0;
			case 8:
				spot8Loc = 0;
				spot9Loc = 0;
			case 9:
				spot9Loc = 0;
		}
	}
	
	/*
	 * this is the method called to solve the puzzle
	 * it calls the solve(int spot) method in this class, which solves the puzzle
	 * and throws a SolverDoneException when the puzzle is solved
	 */
	public void solve(){
		try{
			solve(1); //will throw a SolverDoneException when the puzzle is solved
		}
		//catches the SolverDoneException so that the method can end
		catch(SolverDoneException exception){}
	}
	
	/*
	 * called by the solve() method to solve the puzzle
	 * throws a SolverDoneException (used to end the recursion)
	 * when the puzzle is solved
	 * @param the spot in the solution to attempt to fill
	 * This method steps through puzzle to find a Piece that fits in the spot of 
	 * the solution specified in the parameter, if there is a Piece that fits, then
	 * it is added to the solution and the method calls itself to fill the next spot.
	 * If there is no Piece that fits in the spot, then the redo() method is called
	 * on the previous spot.
	 */
	private void solve(int spot) throws SolverDoneException{
		/*
		 * different cases depending on which spot in the solution needs to be 
		 * solved
		 */
		switch(spot){
			case 1:
				for(int i = spot1Loc; i < puzzle.size(); i++){
					/* 
					 * if the Piece in puzzle fits this spot in the solution in 
					 * any orientation
					 */
					if(puzzle.get(i).fits(1, 1, solution)){
						addPiece(1, 1, puzzle.get(i)); 
						puzzle.remove(i);
						
						/*
						 * next time this case is called, it will pick up where it
						 * left off in puzzle
						 */
						spot1Loc = i + 1; 
					
						//sets the index in puzzle of the subsequent spots to 0
						resetZeros(2);
						//solve the next spot
						solve(2); 
					}
				}
				break;
		
			case 2:
				for(int i = spot2Loc; i < puzzle.size(); i++){
					/* 
					 * if the Piece in puzzle fits this spot in the solution in 
					 * any orientation
					 */
					if(puzzle.get(i).fits(1, 2, solution)){
						addPiece(1, 2, puzzle.get(i)); 
						puzzle.remove(i);
						
						/*
						 * next time this case is called, it will pick up where it
						 * left off in puzzle
						 */
						spot2Loc = i + 1;
					
						//sets the index in puzzle of the subsequent spots to 0
						resetZeros(3);
						//solve the next spot
						solve(3);
					}
				}
				//if there is no Piece that fits in this spot, redo the spot before it
				redo(1);
				break;
		
			case 3:
				for(int i = spot3Loc; i < puzzle.size(); i++){
					/* 
					 * if the Piece in puzzle fits this spot in the solution in 
					 * any orientation
					 */
					if(puzzle.get(i).fits(0, 2, solution)){
						addPiece(0, 2, puzzle.get(i)); 
						puzzle.remove(i);
						
						/*
						 * next time this case is called, it will pick up where it
						 * left off in puzzle
						 */
						spot3Loc = i + 1;
					
						//sets the index in puzzle of the subsequent spots to 0
						resetZeros(4);
						//solve the next spot
						solve(4);
					}
				}
				//if there is no Piece that fits in this spot, redo the spot before it
				redo(2);
				break;
		
			case 4:
				for(int i = spot4Loc; i < puzzle.size(); i++){
					/* 
					 * if the Piece in puzzle fits this spot in the solution in 
					 * any orientation
					 */
					if(puzzle.get(i).fits(0, 1, solution)){
						addPiece(0, 1, puzzle.get(i)); 
						puzzle.remove(i);
						
						/*
						 * next time this case is called, it will pick up where it
						 * left off in puzzle
						 */
						spot4Loc = i + 1;
					
						//sets the index in puzzle of the subsequent spots to 0
						resetZeros(5);
						//solve the next spot
						solve(5);
					}
				}
				//if there is no Piece that fits in this spot, redo the spot before it
				redo(3);
				break;
		
			case 5:
				for(int i = spot5Loc; i < puzzle.size(); i++){
					/* 
					 * if the Piece in puzzle fits this spot in the solution in 
					 * any orientation
					 */
					if(puzzle.get(i).fits(0, 0, solution)){
						addPiece(0, 0, puzzle.get(i)); 
						puzzle.remove(i);
						
						/*
						 * next time this case is called, it will pick up where it
						 * left off in puzzle
						 */
						spot5Loc = i + 1;
					
						//sets the index in puzzle of the subsequent spots to 0
						resetZeros(6);
						//solve the next spot
						solve(6);
					}
				}
				//if there is no Piece that fits in this spot, redo the spot before it
				redo(4);
				break;
		
			case 6:
				for(int i = spot6Loc; i < puzzle.size(); i++){
					/* 
					 * if the Piece in puzzle fits this spot in the solution in 
					 * any orientation
					 */
					if(puzzle.get(i).fits(1, 0, solution)){
						addPiece(1, 0, puzzle.get(i)); 
						puzzle.remove(i);
						
						/*
						 * next time this case is called, it will pick up where it
						 * left off in puzzle
						 */
						spot6Loc = i + 1;
					
						//sets the index in puzzle of the subsequent spots to 0
						resetZeros(7);
						//solve the next spot
						solve(7);
					}
				}
				//if there is no Piece that fits in this spot, redo the spot before it
				redo(5);
				break;
		
			case 7:
				for(int i = spot7Loc; i < puzzle.size(); i++){
					/* 
					 * if the Piece in puzzle fits this spot in the solution in 
					 * any orientation
					 */
					if(puzzle.get(i).fits(2, 0, solution)){
						addPiece(2, 0, puzzle.get(i)); 
						puzzle.remove(i);
						
						/*
						 * next time this case is called, it will pick up where it
						 * left off in puzzle
						 */
						spot7Loc = i + 1;
					
						//sets the index in puzzle of the subsequent spots to 0
						resetZeros(8);
						//solve the next spot
						solve(8);
					}
				}
				//if there is no Piece that fits in this spot, redo the spot before it
				redo(6);
				break;
		
			case 8:
				for(int i = spot8Loc; i < puzzle.size(); i++){
					/* 
					 * if the Piece in puzzle fits this spot in the solution in 
					 * any orientation
					 */
					if(puzzle.get(i).fits(2, 1, solution)){
						addPiece(2, 1, puzzle.get(i)); 
						puzzle.remove(i);
						
						/*
						 * next time this case is called, it will pick up where it
						 * left off in puzzle
						 */
						spot8Loc = i + 1;
					
						//sets the index in puzzle of the subsequent spots to 0
						resetZeros(9);
						//solve the next spot
						solve(9);
					}
				}
				//if there is no Piece that fits in this spot, redo the spot before it
				redo(7);
				break;
		
			case 9:
				for(int i = spot9Loc; i < puzzle.size(); i++){
					/* 
					 * if the Piece in puzzle fits this spot in the solution in 
					 * any orientation
					 */
					if(puzzle.get(i).fits(2, 2, solution)){
						addPiece(2, 2, puzzle.get(i)); 
						puzzle.remove(i);
						
						/*
						 * next time this case is called, it will pick up where it
						 * left off in puzzle
						 */
						spot9Loc = i + 1;
						
						//throw a SolverDoneException to end the entire recursion
						throw new SolverDoneException();
					}
				}
				//if there is no Piece that fits in this spot, redo the spot before it
				redo(8);
				break;
		}
	}
	
	/*
	 * removes a Piece from the solution at a specified spot and adds it back into 
	 * puzzle in the same position that it was originally in relative to the other Pieces
	 * calls the solve(int spot) method for that specified spot
	 * @param the spot in the solution to redo 
	 */
	private void redo(int spot){
		 //different cases depending on which spot in the solution must be redone
		 switch(spot){
			case 1:
				//if the Piece currently in solution must be added to the end of puzzle
				if(spot1Loc - 1 >= puzzle.size())
					puzzle.add(solution[1][1]);
				//if the Piece must be added to puzzle at a place other than the end
				else
					puzzle.add(spot1Loc - 1, solution[1][1]);
				
				solution[1][1] = null; //empty the specified spot in the solution
				//try adding another Piece to the specified spot in the solution
				solve(1); 
				break;
			case 2:
				//if the Piece currently in solution must be added to the end of puzzle
				if(spot2Loc - 1 >= puzzle.size())
					puzzle.add(solution[1][2]);
				//if the Piece must be added to puzzle at a place other than the end
				else
					puzzle.add(spot2Loc - 1, solution[1][2]);
				
				solution[1][2] = null; //empty the specified spot in the solution
				//try adding another Piece to the specified spot in the solution
				solve(2);
				break;
			case 3:
				//if the Piece currently in solution must be added to the end of puzzle
				if(spot3Loc - 1 >= puzzle.size())
					puzzle.add(solution[0][2]);
				//if the Piece must be added to puzzle at a place other than the end
				else
					puzzle.add(spot3Loc - 1, solution[0][2]);
				
				solution[0][2] = null; //empty the specified spot in the solution
				//try adding another Piece to the specified spot in the solution
				solve(3);
				break;
			case 4:
				//if the Piece currently in solution must be added to the end of puzzle
				if(spot4Loc - 1 >= puzzle.size())
					puzzle.add(solution[0][1]);
				//if the Piece must be added to puzzle at a place other than the end
				else
					puzzle.add(spot4Loc - 1, solution[0][1]);
				
				solution[0][1] = null; //empty the specified spot in the solution
				//try adding another Piece to the specified spot in the solution
				solve(4);
				break;
			case 5:
				//if the Piece currently in solution must be added to the end of puzzle
				if(spot5Loc - 1 >= puzzle.size())
					puzzle.add(solution[0][0]);
				//if the Piece must be added to puzzle at a place other than the end
				else
					puzzle.add(spot5Loc - 1, solution[0][0]);
				
				solution[0][0] = null; //empty the specified spot in the solution
				//try adding another Piece to the specified spot in the solution
				solve(5);
				break;
			case 6:
				//if the Piece currently in solution must be added to the end of puzzle
				if(spot6Loc - 1 >= puzzle.size())
					puzzle.add(solution[1][0]);
				//if the Piece must be added to puzzle at a place other than the end
				else
					puzzle.add(spot6Loc - 1, solution[1][0]);
				
				solution[1][0] = null; //empty the specified spot in the solution
				//try adding another Piece to the specified spot in the solution
				solve(6);
				break;
			case 7:
				//if the Piece currently in solution must be added to the end of puzzle
				if(spot7Loc - 1 >= puzzle.size())
					puzzle.add(solution[2][0]);
				//if the Piece must be added to puzzle at a place other than the end
				else
					puzzle.add(spot7Loc - 1, solution[2][0]);
				
				solution[2][0] = null; //empty the specified spot in the solution
				//try adding another Piece to the specified spot in the solution
				solve(7);
				break;
			case 8:
				//if the Piece currently in solution must be added to the end of puzzle
				if(spot8Loc - 1 >= puzzle.size())
					puzzle.add(solution[2][1]);
				//if the Piece must be added to puzzle at a place other than the end
				else
					puzzle.add(spot8Loc - 1, solution[2][1]);
				
				solution[2][1] = null; //empty the specified spot in the solution
				//try adding another Piece to the specified spot in the solution
				solve(8);
				break;
		}
	}
	
	/*
	 * adds a Piece to the Piece[][] that stores the solution if the Piece fits in the
	 * Piece[][] in any orientation according to the rules of the puzzle
	 * @param the row in which to add the Piece
	 * @param the column in which to add the Piece
	 * @param the Piece to be added to the Piece[][] that stores the solution
	 */
	private void addPiece(int row, int column, Piece p){
		/*
		 * checks all orientations of the Piece in the solution, adds the Piece the
		 * first time it fits
		 */
		for(int i = 1; i <= 4; i++){
			if(p.canFit(row, column, solution)){
				solution[row][column] = p;
				return;
			}
			//if the Piece does not fit in its current orientation, it is rotated
			else
				p.rotateClockwise();
		}
	}

}
