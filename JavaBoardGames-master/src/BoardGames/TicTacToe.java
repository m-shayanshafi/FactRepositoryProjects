package BoardGames;
import java.util.Scanner;

/**
 * Author: Dean Johnson
 * Date: November 7, 2011
 * Description: Class that handles the gameplay for Tic Tac Toe. 
 * It is where everything is executed while playing Tic Tac Toe 
 * as well.
 **/ 
public class TicTacToe{
	// Define all variables
	Scanner input;
	char player1;
	char player2;
	char board[][];
	char players[] = new char [2];
	int position[];
	int player;
	final int bounds;
	boolean inBounds;
	String columnAndRow;
	String[] columnsAndRows;

	// Created: 11/7/2011
	// Last Modified: 11/9/2011
	// Description: Initializing needed variables and makes player1 'X' and player2 'O'.
	public TicTacToe(){
		//init variables
		input = new Scanner(System.in);
		player1='X';
		player2='O';
		players[0]=player1;
		players[1]=player2;
		position = new int[2];
		player = 1;
		inBounds=false;
		bounds = 9;
		initBoard(bounds);
	}
	// Created: 11/7/2011
	// Last Modified: 11/9/2011
	// Description:Allows the sent characters to be assigned to each player while initializing all variables.
	public TicTacToe(char player1, char player2){
		// init variables
		input=new Scanner(System.in);
		this.player1=player1;
		this.player2=player2;
		players[0]=player1;
		players[1]=player2;
		position= new int[2];
		bounds=3;
		initBoard(bounds);
	}
	// Created: 11/7/2011
	// Last Modified: 11/11/2011
	// Description: Method that lets the user play the game.
	public void play(){
		p("Welcome Player 1 ("+player1+") and Player 2 ("+player2+") to Tic Tac Toe!");

		while (!isFull() || !isWinner(players[0]) || !isWinner(players[1])){
			for (int i=0; i<players.length; i++){
				player=i+1;
				inBounds=false;
				p("Here is the board as it stands:");
				printBoard();
				p("");
				p("Player "+player+" which column and row will you play in?(\"2 3\" would be Column 2 Row 3): ");

				// Columns are Horizontal, Rows are Vertical

				while (!inBounds){ // test to see if number is between 1-3
					columnAndRow=input.nextLine();
					columnsAndRows=columnAndRow.split(" ");

					try{
						for (int j=0; j<position.length; j++){
							position[j]=Integer.parseInt(columnsAndRows[j])-1;
						}
					}
					catch(ArrayIndexOutOfBoundsException e){
						p("You forgot to enter the Row or Column... Re-enter with the correct Row and Column: ");
						continue;
					}
					catch(NumberFormatException e){
						p("You did not enter the Row/Column in the right format... Re-enter the Row/Column you want to play in.");
						continue;
					}
					if (inBounds(position, bounds)){
						if(!spaceTaken()){
							board[position[0]][position[1]]=players[i];
							inBounds=true;
						}
						else {p("That space is taken!! Try again. ");}
						if (isWinner(players[i])){
							printBoard();
							p("You've won player "+players[i]+"!");
							return;
						}
						else if(isFull()){
							p("Board is full, tie!");
							return;
						}
					}// end of if to check if input is correct
				}// end of while
			}
		}		
	}
	// Created: 11/7/2011
	// Last Modified: 11/7/2011
	// Description: Checks to see if the position that is trying to be used is taken or not.
	private boolean spaceTaken(){
		if (board[position[0]][position[1]]=='+') // I initialized board to +
			return false;
		else return true;
	}
	// Created: 11/7/2011
	// Last Modified: 11/7/2011
	// Description:Method we were told to create. Lets the players choose their pieces.
	public void determinePlayerChoice(char player){
		p("What piece would Player 1 like to be? (i.e. 'X' or 'O'): ");
		player1 = input.next().charAt(0);
		input.nextLine();
		p("What piece would Player 2 like to be? (i.e. 'X' or 'O'): ");
		player2 = input.next().charAt(0);
		input.nextLine();
		while(player1==player2){
			p("You chose the same piece as Player 1!! Choose again.");
			p("What piece would you like to be? (i.e. 'X' or 'O'): ");
			player2 = input.next().charAt(0);
		}
	}
	// Created: 11/7/2011
	// Last Modified: 11/10/2011
	// Description:Sets size of board, and the board's contents
	private void initBoard(int bounds){
		board = new char[bounds][bounds];

		for(int i=0; i<board.length;i++){
			for (int j=0; j<board[i].length;j++){
				board[i][j]='+';
			}
		}
	}
	// Created: 11/7/2011
	// Last Modified: 11/11/2011
	// Description:Checks to see if there is a winner
	// Extra: This allows the board to be any length to find the winner (Multiple uses instead of only Tic Tac Toe)
	public boolean isWinner(char player){
		boolean diag1Win=true;
		boolean diag2Win=true;
		boolean rowWin=true;
		boolean columnWin=true;
		// Checks diagonal (Like y=-x), straight row, and straight across wins.
		for (int i=0; i<board.length; i++){
			for (int j=0; j<board.length; j++){
				if (board[j][j]==player){}	
				else diag1Win=false;
				if (board[i][j]==player){}
				else columnWin=false;
				if (board[j][i]==player){}
				else rowWin=false;
			}
			if (diag1Win||rowWin||columnWin){
				return true;
			}
			else {rowWin=true; columnWin=true;}
		}
		// Checks for a diagonal win (Like y=x)
		for (int i=0, j=board.length-1; i<board.length;i++, j--){
			if (board[i][j]==player){}
			else {diag2Win=false;}	
		}

		if (diag2Win){
			return true;
		}

		return false;
	}
	// Created: 11/7/2011
	// Last Modified: 11/7/2011
	// Description: Checks to see if the board is full
	private boolean isFull(){	
		for (int i=0; i<board.length; i++){
			for(int j=0; j<board[i].length; j++){
				if(board[i][j]=='+'){// I initialized all empty spaces to +
					return false;
				}
			}
		}
		return true;
	}
	// Created: 11/7/2011
	// Last Modified: 11/9/2011
	// Description: Checks input to make sure it's not out of bounds.
	public boolean inBounds(int[] input, int bounds){
		for (int i=0; i<input.length; i++){
			if(input[i]>=bounds || input[i]<0){
				return false;
			}
		}
		return true;
	}
	// Created: 11/7/2011
	// Last Modified: 11/11/2011
	// Description: Prints the current boards contents to the user
	private void printBoard () {
		// Every square is 3 lines long. Because we display data one LINE at a time, we need to do columns first
		for (int i=0; i<board.length; i++){
			// Top of a square
			for (int j=0; j<board.length; j++){
				if (j!=board.length-1)
					System.out.print("     |");
				else System.out.print("      \n");
			}
			// Middle of a square (with the contents of that square)
			for (int j=0; j<board.length; j++){
				if (j!=board.length-1)
					System.out.print("  "+board[i][j]+"  |");
				else System.out.print("  "+board[i][j]+"   \n");
			}
			// Bottom of the square
			for(int j=0; j<board.length; j++){
				if (i!=board.length-1)
					System.out.print("_____");
				else System.out.print("     ");
				if (j!=board.length-1)
					System.out.print("|");
				else System.out.print(" \n"); // Determines when the board is done.
			}

		}

	}
	// Created: 11/7/2011
	// Last Modified: 11/7/2011
	// Description: Shorthand for printing to the user
	public void p(String s){
		System.out.println(s);
	}
	// Created: 11/14/2011
	// Last Modified: 11/14/2011
	// Description: Shorthand for printing to the user (Can print data without carriage returns)
	public void p(String s, boolean newLine){
		System.out.print(s);
		if(newLine)
			System.out.println();
	}	

}
