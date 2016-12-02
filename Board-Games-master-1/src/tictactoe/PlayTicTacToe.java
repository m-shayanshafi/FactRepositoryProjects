package TicTacToe;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


import Chess.Board;


/*
 * class PlayTicTacToe displays and controls the game play of a TicTacToe game
 * inner classes: CPUListener, TwoPlayerListener, RulesListener, and RageQuitListener all help with
 * controlling the flow of the game 
 */
public class PlayTicTacToe extends JComponent implements Runnable{
	private static final long serialVersionUID = 1L; 
	
	private final int numRows = 3;
	private final int numColumns = 3;
	private final int CELL_SIZE = 120; //size of 1 cell in the game board
	private final int shapeGap = 10; //the gap between the edge of the cell and the shape drawn in it
	private JFrame frame; //the frame that displays the game
	//the game board
	private Rectangle2D.Double boardRectangle = new Rectangle2D.Double(50,50,
			numRows*CELL_SIZE,numColumns*CELL_SIZE); 
	private Rectangle2D.Double [][] cells; //the rectangle representation of the cells in the board
	/*
	 * the game board
	 * a 0 means a cell is empty
	 * a 1 means the cell is occupied by an X
	 * a 2 means the cell is occupied by an O
	 */
	private Board board; 
	/*
	 * stores which player's turn it currently is
	 * in 2 player mode, the person who goes first is player 1 and the person who goes second is player 2
	 * also, for a game with 2 players, the person who goes first is X and the person who goes second is
	 * O, therefore, a 1 represents an X and a 2 represents an O
	 * in vs. CPU mode, the computer is always represented by turn 2, regardless of whether or not it 
	 * goes first
	 */
	private int turn; 
	private JPanel componentPanel; //the panel that holds the buttons and turnLabel
	private final int PANEL_SIZE = 30; //the height of componentPanel
	private JLabel turnLabel;  //labels which player's turn it is
	private TwoPlayerListener twoPlayerListener; //mouse listener for a game with 2 players
	private CPUListener cpuListener;  //mouse listener for a game against the computer
	private CPUPlayer cpu; //the class that controls the computer's moves in vs. computer mode
	/*
	 * creates the frame and initiates data that is needed to play Tic-Tac-Toe
	 */
	public PlayTicTacToe(){
		cells = new Rectangle2D.Double[numRows][numColumns];
		board = new Board(3, 3);
		turn = 1; //game starts with player 1
		/*
		 * cells[][] helps make a connection between the cells drawn in the GUI and the cells in
		 * the board, it stores the rectangle objects that are the cells of the board
		 */
		for(int i = 0; i < cells.length; i++){
			for(int j = 0; j < cells[i].length; j++){
				cells[i][j] = new Rectangle2D.Double(50 + j * CELL_SIZE, 50 + i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
			}
		}
	}
	
	/*
	 * paints the board and shapes
	 * checks if a player has won and draws a line to show the winning player's three in a row
	 */
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		paintBoard(g2);
	
		/*
		 * step through the cells and draw and X and an O in the appropriate spots
		 */
		for(int i = 0; i < cells.length; i++){
			for(int j = 0; j < cells[i].length; j++){
				//a 1 represents an X
				if(board.getCell(i, j) == 1){
					drawX(g2, cells[i][j]);
				}
				//a 2 represents an O
				else if(board.getCell(i, j) == 2){
					drawO(g2, cells[i][j]);
				}
			}
		}
		/*
		 * if a player has won, a line is drawn through the winning shape
		 * if no player has won, this call does nothing
		 */
		if(gameOver())
			drawWinningLine(g2); //draws a line through the winning player's three in a row
	}
	
	/*
	 * draws an X in a given cell
	 * @param the Graphics2D object to draw the X
	 * @param the cell (represented by a Rectangle2D.Double) in which to draw the X
	 */
	public void drawX(Graphics2D g, Rectangle2D.Double r){
	
		Line2D.Double l1 = new Line2D.Double(r.getX() + shapeGap, r.getY() + shapeGap,
				r.getX() + CELL_SIZE - shapeGap, r.getY() + CELL_SIZE - shapeGap);
		
		Line2D.Double l2 = new Line2D.Double(r.getX() + shapeGap, r.getY() + CELL_SIZE - shapeGap, 
				r.getX() + CELL_SIZE - shapeGap, r.getY() + shapeGap);
		g.draw(l1);
		g.draw(l2);
	}
	
	/*
	 * draws an O in a given cell
	 * @param the Graphics2D object to draw the O
	 * @param the cell (represented by a Rectangle2D.Double) in which to draw the O
	 */
	public void drawO(Graphics2D g, Rectangle2D.Double r){
		Ellipse2D.Double e = new Ellipse2D.Double(r.getX() + shapeGap, r.getY() + shapeGap, 
				r.getWidth() - 2 * shapeGap, r.getHeight() - 2 * shapeGap);
		g.draw(e);
	}
	
	/*
	 * draws a line through the winning player's three in a row
	 * @param r1 the row of the first cell in the three in a row
	 * @param c1 the column of the first cell in the three in a row
	 * @param r2 the row of the last cell in the three in a row
	 * @param c2 the column of the last cell in the three in a row
	 */
	public void drawLine(int r1, int c1, int r2, int c2, Graphics2D g){
		int x1 = (int) cells[r1][c1].getX() + CELL_SIZE/2;
		int y1 = (int) cells[r1][c1].getY() + CELL_SIZE/2;
		int x2 = (int) cells[r2][c2].getX() + CELL_SIZE/2;
		int y2 = (int) cells[r2][c2].getY() + CELL_SIZE/2;
		Line2D.Double l = new Line2D.Double(x1, y1, x2, y2);
		g.draw(l);
	}
	
	/*
	 * figures out where the winning player's three in a row is located and calls a method to draw a
	 * line through those cells
	 * this is the method that is called by the paintComponent(Graphics g) method, therefore it is able
	 * to receive a graphics object that it then passes on to the drawLine() method
	 * @param the Graphics2D object to sent to the drawLine() method
	 */
	public void drawWinningLine(Graphics2D g){
		//leftmost column
		if(areEqual(board.getCell(0, 0), board.getCell(1, 0), board.getCell(2, 0)))
			drawLine(0, 0, 2, 0, g);
		
		//top row
		else if(areEqual(board.getCell(0, 0), board.getCell(0, 1), board.getCell(0, 2)))
			drawLine(0, 0, 0, 2, g);
		
		//middle row (across)
		else if(areEqual(board.getCell(1, 0), board.getCell(1, 1), board.getCell(1, 2)))
			drawLine(1, 0, 1, 2, g);
		
		//bottom row
		else if(areEqual(board.getCell(2, 0), board.getCell(2, 1), board.getCell(2, 2)))
			drawLine(2, 0, 2, 2, g);
		
		//middle row (down)
		else if(areEqual(board.getCell(0, 1), board.getCell(1, 1), board.getCell(2, 1)))
			drawLine(0, 1, 2, 1, g);
		
		//rightmost column
		else if(areEqual(board.getCell(0, 2), board.getCell(1, 2), board.getCell(2, 2)))
			drawLine(0, 2, 2, 2, g);
		
		//diagonal from top right to bottom left
		else if(areEqual(board.getCell(0, 2), board.getCell(1, 1), board.getCell(2, 0)))
			drawLine(0, 2, 2, 0, g);
		
		//diagonal from top left to bottom right
		else if(areEqual(board.getCell(0, 0), board.getCell(1, 1), board.getCell(2, 2)))
			drawLine(0, 0, 2, 2, g);
		
	}
	
	/*
	 * checks whether or not a player has won
	 * @return true if a player has won
	 * @return false if a player has not won
	 */
	public boolean gameOver(){
		//leftmost column
		if(areEqual(board.getCell(0, 0), board.getCell(1, 0), board.getCell(2, 0)))
			return true;

		//top row
		else if(areEqual(board.getCell(0, 0), board.getCell(0, 1), board.getCell(0, 2)))
			return true;
		
		//middle row (across)
		else if(areEqual(board.getCell(1, 0), board.getCell(1, 1), board.getCell(1, 2)))
			return true;
			
		
		//bottom row
		else if(areEqual(board.getCell(2, 0), board.getCell(2, 1), board.getCell(2, 2)))
			return true;
			
		
		//middle row (down)
		else if(areEqual(board.getCell(0, 1), board.getCell(1, 1), board.getCell(2, 1)))
			return true;
			
		
		//rightmost column
		else if(areEqual(board.getCell(0, 2), board.getCell(1, 2), board.getCell(2, 2)))
			return true;
		
		
		//diagonal from top right to bottom left
		else if(areEqual(board.getCell(0, 2), board.getCell(1, 1), board.getCell(2, 0)))
			return true;
		
		
		//diagonal from top left to bottom right
		else if(areEqual(board.getCell(0, 0), board.getCell(1, 1), board.getCell(2, 2)))
			return true;
		else 
			return false;
	}
	
	/*
	 * draws the board in the JFrame
	 */
	public void paintBoard(Graphics2D g){
		g.draw(boardRectangle); //draw the outer boundary of the board
		//draw the individual cells of the board
		for(int i = 0; i < cells.length; i++){
			for(int j = 0; j < cells[i].length; j++)
				g.draw(cells[i][j]);
		}
	}
	
	/*
	 * makes the game advance to the next player's turn
	 */
	public void toggleTurn(){
		if(turn == 1){
			turn = 2;
		}
		else if(turn == 2){
			turn = 1;
		}
	}
	
	/*
	 * adds the buttons and turnLabel to the frame
	 */
	public void addComponents(JFrame f){
		//create the frame and add the buttons and label
		frame = f;

		componentPanel = new JPanel(new FlowLayout());
		componentPanel.setSize(frame.getWidth(), PANEL_SIZE);
		
		JButton rulesButton = new JButton("Rules");
		JButton rageQuitButton = new JButton("Rage Quit");
		
		RulesListener rules = new RulesListener();
		rulesButton.addActionListener(rules);
		
		RageQuitListener rageQuit = new RageQuitListener();
		rageQuitButton.addActionListener(rageQuit);
	
		turnLabel = new JLabel();
		componentPanel.add(turnLabel);
		componentPanel.add(rageQuitButton);
		componentPanel.add(rulesButton);
		
		frame.add(componentPanel, BorderLayout.CENTER);
	}
	
	/*
	 * initiates the gameplay by finding out if the user wants a player vs. player game or a player vs.
	 * CPU game
	 * for a player vs. CPU game, this method also figures out if the player wants to go first and 
	 * at what difficulty they would like to play at
	 */
	public void startGame(){
		Object [] options = {"2 Player", "vs. CPU"}; //button text for the dialog box
		
		int ans = JOptionPane.showOptionDialog(frame,
				"Choose your game mode:", "Tic-Tac-Toe",
				JOptionPane.YES_NO_OPTION, JOptionPane.DEFAULT_OPTION,
				null, options, options[0]);

		//if the user wants a game against another player
		if (ans == JOptionPane.YES_OPTION) {
			turnLabel.setText("Player " + turn + "'s turn");
			//add the listener for a 2 player game to the frame
			twoPlayerListener = new TwoPlayerListener();
			frame.addMouseListener(twoPlayerListener);
		}
		//if the user wants a game against the CPU
		else if (ans == JOptionPane.NO_OPTION) {
			repaint();
			int difficulty = getDifficultyInput();
			if(difficulty == 0) //if getDifficultyInput returns the dummy value
				return;
			 // figure out whether or not the player wants to go first
			int a = JOptionPane.showConfirmDialog(frame,
					"Would you like to go first?", "Go First?", JOptionPane.YES_NO_OPTION);
			//if the user would like to go first
			if (a == JOptionPane.YES_OPTION) {
				turnLabel.setText("Your turn");
				/*
				 * create a listener for a game against the CPU
				 * the 1 represents the fact that the opponents shape is an X
				 */
				cpuListener = new CPUListener(1);
				//create a CPUPlayer with the appropriate difficulty
				cpu = new CPUPlayer(board, 2, difficulty);
			} 
			//if the user would like to go second
			else if (a == JOptionPane.NO_OPTION) {
				repaint();
				/*
				 * create a listener for a game against the CPU
				 * the 2 represents the fact that the opponents shape is an O
				 */
				cpuListener = new CPUListener(2); 

				/*
				 * if the player decided to go second, the computer makes its move right away
				 * a 1 represents the cpu's shape (an X)
				 * create a CPUPlayer with the appropriate difficulty
				 */
				cpu = new CPUPlayer(board, 1, difficulty);
				cpu.makeMove();
				repaint();
				turnLabel.setText("Your turn");
			}
			else if(a == JOptionPane.CANCEL_OPTION){
				frame.setVisible(false);
			}

			//add the listener for a game against the CPU to the frame
			frame.addMouseListener(cpuListener);
		}
		else if(ans == JOptionPane.CLOSED_OPTION){
			frame.setVisible(false);
		}

	}

	/*
	 * figures out what difficulty the user would like to play at in a vs. CPU game
	 * @return 3 if the player would like to play at the impossible difficulty
	 * @return 2 if the player would like to play at the medium difficulty
	 * @return 1 if the player would like to play at the easy difficulty
	 */
	public int getDifficultyInput(){
		Object [] options = {"Impossible", "Medium", "Easy"}; //button text for the dialog box
		
		int ans = JOptionPane.showOptionDialog(frame,
				"Choose your difficulty:", "Tic-Tac-Toe",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.DEFAULT_OPTION,
				null, options, options[0]);
		if(ans == JOptionPane.YES_OPTION)
			return 3; //integer representation of the impossible difficulty
		else if(ans == JOptionPane.NO_OPTION){
			return 2; //integer representation of the medium difficulty
		}
		else if(ans == JOptionPane.CANCEL_OPTION){
			return 1; //integer representation of the easy difficulty
		}
		else if(ans == JOptionPane.CLOSED_OPTION){
			frame.setVisible(false);
		}
		//dummy value
		return 0;

	}
	
	/*
	 * restarts the game
	 */
	public void restart(){
		board = new Board(3, 3); //reset the board
		turn = 1; //reset the turn
		//remove whatever listeners the frame had (the proper listener will be added back in startGame())
		frame.removeMouseListener(cpuListener);
		frame.removeMouseListener(twoPlayerListener);
		repaint(); //update the frame with the reset status
		startGame(); //start a new game
	}
	
	/*
	 * listener for a 2 player Tic-Tac-Toe game
	 * listens for when a spot in the board is clicked and modifies the board appropriately 
	 * depending on which players turn it is
	 */
	private class TwoPlayerListener implements MouseListener{
		public void mousePressed(MouseEvent e){
			int x = e.getX();
			int y = e.getY();
			
			/*
			 * steps through the cells of the board
			 */
			for(int i = 0; i < cells.length; i++){
				for(int j = 0; j < cells[i].length; j++){
					/*
					 * if one of the cells contains the location clicked and that spot 
					 * in the board is empty
					 */
					if(cells[i][j].contains(x, y) && board.getCell(i, j) == 0){
						//if it is player 1's turn
						if(turn == 1){
							board.setCell(i, j, 1);
							//change to player 2's turn (because player 1 just make his/her move)
							toggleTurn();
							//change the JLabel based on what player's turn it is
							turnLabel.setText("Player " + turn + "'s turn"); 
						}
						else if(turn == 2){
							board.setCell(i, j, 2);
							//change to player 1's turn (because player 2 just make his/her move)
							toggleTurn();
							//change the JLabel based on what player's turn it is
							turnLabel.setText("Player " + turn + "'s turn");
						}
					}
				}
			}
			repaint();
		}

		/*
		 * when the mouse is released, check if a player has won or if the board is full
		 */
		public void mouseReleased(MouseEvent event) {
			//when one of the players has won
			if(gameOver()){
				toggleTurn(); //go back to the player that made the last move
				//announces which player won
				JOptionPane.showMessageDialog(frame, "Player " + turn + " wins!", "Game Over",
						JOptionPane.INFORMATION_MESSAGE);
				
				showRestartPrompt();
			}
			//if the board is full and neither player has won, the game is a tie
			else if(board.isFull()){
				showTiePrompt();
				showRestartPrompt();
			}
		}
		public void mouseClicked(MouseEvent event) {}
		public void mouseEntered(MouseEvent event) {}
		public void mouseExited(MouseEvent event) {}
	}
	
	/*
	 * listener for a Tic-Tac-Toe game against the computer
	 * listens for when a spot in the board is clicked and modifies the board appropriately 
	 * makes the computer's move after the player has gone
	 * the CPU is always represented by turn = 2, even if it goes first
	 */
	private class CPUListener implements MouseListener {
		private int opponent; //the integer representing the user's shape
	
		public CPUListener(int opponent){
			this.opponent = opponent;
		}
		public void mousePressed(MouseEvent e){
			if(turn != 1){return;} //if if is not the player's turn
			int x = e.getX();
			int y = e.getY();
			/*
			 * steps through the cells of the board
			 */
			for(int i = 0; i < cells.length; i++){
				for(int j = 0; j < cells[i].length; j++){
					/*
					 * if one of the cells contains the location clicked and that spot 
					 * in the board is empty
					 */
					if(cells[i][j].contains(x, y) && board.getCell(i, j) == 0){
						if(opponent == 2)
							board.setCell(i, j, 2);
						else if(opponent == 1)
							board.setCell(i, j, 1);
						toggleTurn();
						turnLabel.setText("CPU's turn");
						repaint();
					}
				}
			}
		}
		//the computer makes its move when the mouse is released
		public void mouseReleased(MouseEvent event) {
			if(turn != 2){return;} //if it is not the computer's turn
			//if the player's last move was a winning move
			if(gameOver()){
				JOptionPane.showMessageDialog(frame, "You Win!!!!!", "Game Over",
						JOptionPane.INFORMATION_MESSAGE);
				showRestartPrompt();
				return;
			}
			//if the board is full and neither player has won, the game is a tie
			else if(board.isFull()){
				showTiePrompt();
				showRestartPrompt();
				return;
			}
			//remove the cpuListener so that the player's clicking will not interfere with the CPU's move
			frame.removeMouseListener(cpuListener); 
			//makes the program pause for .5 second before the computer makes its move
			try {
				Thread.sleep(500);
			} 
			catch (InterruptedException e1) {}
			
			cpu.setBoard(board);//update the board that the cpu has
			cpu.makeMove(); //the CPU makes it's move
			toggleTurn(); //after the cpu makes its move, it is the player's turn
			turnLabel.setText("Your turn");
			frame.addMouseListener(cpuListener); //add back the mouse listener for the CPU game
			repaint();
			
			//if the cpu's last move was a winning move
			if(gameOver()){
				JOptionPane.showMessageDialog(frame, "You Lose :(", "Game Over",
						JOptionPane.INFORMATION_MESSAGE);

				showRestartPrompt();
			}
			//if the board is full and neither player has won, the game is a tie
			else if(board.isFull()){
				showTiePrompt();
				showRestartPrompt();
			}
		}
		public void mouseClicked(MouseEvent event) {}
		public void mouseEntered(MouseEvent event) {}
		public void mouseExited(MouseEvent event) {}
		
	}
	
	/*
	 * asks the user whether or not they would like to play another game after one game has ended
	 * if the user would like to play another game, then the game restarts
	 * otherwise, the program ends
	 */
	public void showRestartPrompt(){
		int ans = JOptionPane.showConfirmDialog(frame,
				"Would you like to play again?", "Play Again?",
				JOptionPane.YES_NO_OPTION);
		//if the player wishes to play again
		if (ans == JOptionPane.YES_OPTION) {
			restart();
		}
		//if the player does not wish to play again
		else if(ans == JOptionPane.NO_OPTION || ans == JOptionPane.CLOSED_OPTION) {
			//end the program
			frame.setVisible(false);
		}
		
	}
	
	public void showTiePrompt(){
		JOptionPane.showMessageDialog(frame, "TIE", "Game Over",
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	/*
	 * a listener for the rules button
	 */
	private class RulesListener implements ActionListener{
		//displays a message when the button is clicked
		public void actionPerformed(ActionEvent e){
			JOptionPane.showMessageDialog(frame, "Seriously?", "HAHAHAHAHA",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	/*
	 * if a player decides to rage quit
	 */
	private class RageQuitListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			int ans = JOptionPane.showConfirmDialog(frame, "Are you sure?", "Rage Quit?", JOptionPane.YES_NO_OPTION);
			//if a player decides to rage quit, the program ends
			if(ans == JOptionPane.YES_OPTION){
				frame.setVisible(false); //ends the program
			}
		}
	}
	
	/*
	 * checks if three integers are equal and not equal to 0
	 * used to help determine whether or not a player has won
	 * @return true if all three integers are nonzero and equal
	 * @return false if any of the numbers is 0 or if all three numbers are not equal
	 */
	private boolean areEqual(int a, int b, int c){
		if(a == 0 || b == 0 || c == 0)
			return false;
		return a == b && a == c && b == c;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			JFrame f = new JFrame("Tic-Tac-Toe");
			f.setSize(600, 500); //default frame size
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			f.setResizable(false);
			
			PlayTicTacToe p = new PlayTicTacToe();
			p.addComponents(f); //add the buttons a turnLabel to the frame
			f.add(p); //add the PlayTicTacToe game to the frame
			f.setVisible(true);
			p.startGame(); //initiate the playing of the game
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
