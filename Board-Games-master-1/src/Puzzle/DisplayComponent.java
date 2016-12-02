package Puzzle;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import javax.swing.Timer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
/**
 * DisplayComponent puts together the Piece, PieceComponent, PieceHolder, and Side classes in a way so a user can
 * play the jig-saw puzzle game. Inner classes implement EventListeners to do this.
 * 
 *  DisplayComponent is responsible for showing the puzzle pieces and moving them to the specified location in the  
 *  puzzle based on user input. 
 */
public class DisplayComponent extends JComponent implements Runnable{
	private static final long serialVersionUID = 1L;

	//	Size of one piece not including the sides. Each piece is therefore
	//  125px by 125px. 
	private static final int CELL_SIZE = 125;

	//	number or rows and columns in the board
	private static final int numRows = 3;
	private static final int numCols = 3;


	//	rectangle that outlines the board
	private Rectangle2D.Double boardRectangle = new Rectangle2D.Double(50,50,numRows*CELL_SIZE,numCols*CELL_SIZE);

	//	PieceHolder to hold pieces in the board
	private PieceHolder pieceArray = new PieceHolder(); //stores the pieces in the tray
	private PieceHolder puzzleArray = new PieceHolder(); //stores the pieces in the board

	//	This array of PieceComponents is exclusive to DisplayComponent. pieceArray is converted into a 2D array of
	//	PieceComponents so they can be draw onto the JFrame
	private PieceComponent[][] compArray = new PieceComponent[3][3];//stores the component version of the Pieces
	private PieceComponent[][] compArrayPuzzle = new PieceComponent[3][3];//the Pieces in the board

	//	when a puzzle piece is clicked on, its reference will he set equals to this one
	private PieceComponent highlighted = new PieceComponent(0,0,new Piece(0,0,0,0));
	//dummy value; when a piece is not clicked on, highlighted is set equal to this reference
	private PieceComponent notHighlighted = new PieceComponent(0,0,new Piece(0,0,0,0));

	//	stores the rectangles used in drawing the puzzle board
	private Rectangle2D.Double[][] boardArray = new Rectangle2D.Double[3][3];

	private JFrame frame = new JFrame();
	private int numTimesHighlightedClicked = 0;
	private boolean isChangingColor;

	public DisplayComponent(){
		//	fill the tray with pieces
		pieceArray.fillWithDefaultPieces();
		//	update the PieceComponent arrays with the pieces in the PieceHolders
		toComponent();
		toComponentPuzzle();
		//	create the rectangles necessary to draw to puzzle board
		for(int i = 0; i  < numRows; i++){
			for(int j = 0; j < numCols; j++){
				boardArray[i][j] = new Rectangle2D.Double(50+j*CELL_SIZE,50+i*CELL_SIZE,CELL_SIZE,CELL_SIZE);
			}
		}
	}
	/*
	 * signifies the amount of spacing between the pieces in the tray
	 */
	private int hGap = 55, vGap = 80;
	/**
	 * moves all pieces from the board into the tray and 
	 * updates the PieceComponent arrays
	 */
	public void reset(){
		puzzleArray.clear();	
		pieceArray.fillWithDefaultPieces();
		toComponent();
		toComponentPuzzle();
		repaint();
	}
	/**
	 * updates compArray by reassigning all values in the PieceHolder
	 * to those in the array
	 */
	public void toComponent(){
		for(int i = 0; i < numRows; i ++){
			for(int j = 0; j < numCols; j++){
				Piece p = pieceArray.getOccupant(i,j);
				compArray[i][j] = new PieceComponent(509+j*CELL_SIZE+j*vGap-50,100+i*CELL_SIZE+i*hGap-50,p);
			}
		}
	}
	/**
	 * updates compArrayPuzzle but reassigning all values in the PieceHolder
	 * to those in the array
	 */
	public void toComponentPuzzle(){
		for(int i = 0; i < numRows; i ++){
			for(int j = 0; j < numCols; j++){
				Piece p = puzzleArray.getOccupant(i,j);
				compArrayPuzzle[i][j] = new PieceComponent(100+j*CELL_SIZE-50,100+i*CELL_SIZE-50,p);
			}
		}
	}
	/**
	 * converts compArray into a PieceHolder
	 * is used when updating pieceArray and for no other purpose
	 * 
	 * @return PieceHolder updated PieceHolder
	 */
	public PieceHolder fromComponent(){
		PieceHolder piece = new PieceHolder();
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){
				piece.addPiece(i, j, compArray[i][j].getPiece());
			}
		}
		return piece;
	}
	/**
	 * converts compArrayPuzzle into a PieceHolder
	 * is used when updating puzzleArray and for no other purpose
	 * 
	 * @return PieceHolder updated PieceHolder
	 */
	public PieceHolder fromComponentPuzzle(){
		PieceHolder piece = new PieceHolder();
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){
				piece.addPiece(i, j, compArrayPuzzle[i][j].getPiece());
			}
		}
		return piece;
	}

	
	/**
	 * @param Graphics object
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.BLACK);
		g2.fill(new Rectangle(0,0,getWidth(),getHeight()));
		//	draw the board
		paintBoard(g);
		/*
		 * cycle through compArray and draw each piece in the tray
		 * if a piece equals highlighted, fill it in with orange
		 */
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){
				PieceComponent p = compArray[i][j];
				if (p.getPiece() != null) {
					//	when the highlighted piece is landed upon
					if (p.equals(highlighted)) {
						//	paint the interior orange and fill it in
						g2.setColor(Color.cyan);
						g2.draw(p.drawPiece());
						g2.fill(p.drawPiece());
						//	paint the outline black
						g2.setColor(Color.BLACK);
						g2.draw(p.drawPiece());
					} 
					//if the Piece is not highlighted
					else {
						if(isChangingColor){
							g2.setColor(getRandomColor());
						}
						else{
							g2.setColor(p.getFill());
						}
						g2.draw(p.drawPiece());
						g2.fill(p.drawPiece());
						g2.setColor(p.getOutline());
						g2.draw(p.drawPiece());
					}
				}
			}
		}
		/*
		 * cycle through compArrayPuzzle and draw the pieces present on the puzzle board
		 */
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){
				if(compArrayPuzzle[i][j].getPiece() != null){
					PieceComponent p = compArrayPuzzle[i][j];
					//when the highlighted piece is landed upon
					if (p.equals(highlighted)) {
						//paint the interior cyan and fill it in
						g2.setColor(Color.cyan);
						g2.draw(p.drawPiece());
						g2.fill(p.drawPiece());
						//	paint the outline black
						g2.setColor(Color.BLACK);
						g2.draw(p.drawPiece()); //magenta
					} 
					//if the Piece is not highlighted
					else {
						if(isChangingColor){
							g2.setColor(getRandomColor());
						}
						else{
							g2.setColor(p.getFill());
						}
						g2.draw(p.drawPiece());
						g2.fill(p.drawPiece());
						g2.setColor(p.getOutline());
						g2.draw(p.drawPiece());
					}
				}
			}
		}
	}

	/**
	 * @param num, a number representing a color (meant to be a random number)
	 * @return the Color represented by the number
	 */
	public Color getRandomColor(){
		Random numGen = new Random();
		return new Color(numGen.nextFloat(),numGen.nextFloat(),numGen.nextFloat());
	}



	//the original row and column of a Piece that was moved in the Tray
	private int hPositionX,hPositionY; 
	//the original row and column of a Piece that was moved in the board
	private int bPositionR, bPositionC; 

	/*
	 * true if the Piece being moved was originally in the tray (true is default)
	 * false if the Piece being moved was originally in the board
	 */
	private boolean fromTray = true;
	/*
	 * class: a mouse listener, will allow a user to drag and drop pieces from the tray into
	 * 		the puzzle board
	 * MouseInputAdapter implements MouseListener and a MouseMotionListener and inherits methods 
	 * 		from both interfaces
	 */
	private class MyDragAndDrop extends MouseInputAdapter{
		//	used to constantly change the location of a piece when it is dragged
		private Point offset = new Point();

		/*
		 * Constructor: add the listeners to comp
		 */
		public MyDragAndDrop(DisplayComponent c){
			addMouseListener(this);
			addMouseMotionListener(this);
		}
		public void mousePressed(MouseEvent arg0) {
			int x = arg0.getX();
			int y = arg0.getY();
			/*
			 * right-clicked
			 */
			if(arg0.getButton() == MouseEvent.BUTTON3
					&& highlighted.drawPiece().contains(x,y)){
				highlighted.getPiece().rotateClockwise();
				repaint();
			}
			/*
			 * left clicked
			 */
			else{
				/*
				 * update the PieceHolders
				 */
				pieceArray = fromComponent();
				puzzleArray = fromComponentPuzzle();
				
				//if a spot other than the highlighted piece is clicked
				if(!highlighted.drawPiece().contains(x, y)){
					highlighted = notHighlighted;
					numTimesHighlightedClicked = 0;
				}
				//if the highlighted Piece is clicked
				else{
					numTimesHighlightedClicked++;
				}

				//if the area in the board was clicked
				if(boardRectangle.contains(x, y)){
					/*
					 * cycle through compArrayPuzzle to determine which piece (in the board) was clicked on
					 */
					for(int i = 0; i < numRows; i++){
						for(int j = 0; j < numCols; j++){
							PieceComponent p = compArrayPuzzle[i][j];
							if(p.getPiece() != null){
								Area area = p.drawPiece();
								/*
								 * when a piece is clicked on, it will become highlighted
								 */
								if(area.contains(x, y)){
									fromTray = false;
									highlighted = p;
									bPositionR = i;
									bPositionC = j;
									offset.x = x - p.getFrameX();
									offset.y = y - p.getFrameY();
									numTimesHighlightedClicked = 0;
								}
							}
						}
					}
				}
				//if the area outside the board was clicked
				else{
					/*
					 * cycle through compArray to determine which piece (in the tray) was clicked on
					 */
					for(int i = 0; i < numRows; i ++){
						for(int j = 0; j < numCols; j++){
							PieceComponent p = compArray[i][j];
							if (p.getPiece() != null) {
								Area area = p.drawPiece();
								/*
								 * when a piece is clicked on, it will become highlighted
								 */
								if (area.contains(x, y)) {
									//if the highlighted Piece is clicked on
									if (highlighted.equals(p) && numTimesHighlightedClicked == 2) {
										highlighted = notHighlighted;
										numTimesHighlightedClicked = 0;
									} 
									//if a Piece other than the highlighted Piece is clicked on
									else {
										highlighted = p;
										hPositionX = i;
										hPositionY = j;
										offset.x = x - p.getFrameX();
										offset.y = y - p.getFrameY();
										repaint();
									}
								}
							} 
						}
					} 
				}
			}
		}		
		public void mouseDragged(MouseEvent e){
			/*
			 * as long as the mouse is pressed, keep changing the location
			 * of the highlighted piece
			 */
			int x = e.getX() - offset.x;
			int y = e.getY() - offset.y;
			highlighted.setLocation(x,y);
			repaint();

		}
		public void mouseReleased(MouseEvent e){
			int x = e.getX(); //the x coordinate of the location where the mouse was released
			int y = e.getY(); //the y coordinate of the location where the mouse was released

			/*
			 * when a piece is released over a rectangle in the puzzle board,
			 * the location of that rectangle in the board is stored
			 * for now, these values are set to dummy values
			 */
			int boardX = -1;
			int boardY = -1;

			/*
			 * cycle through the boardArray to find which of the spots the highlighted piece is on
			 * the location will be stored as boardX and boardY
			 * will only set values to boardX and boardY if the mouse was released 
			 * in the board area
			 */	
			for(int i = 0; i < numRows; i++){
				for(int j = 0; j < numCols; j++){
					if(boardArray[i][j].contains(x,y)){
						boardX = i;
						boardY = j;
					}
				}
			}

			//if the Piece being moved was originally in the Tray
			if(fromTray){
				/*
				 * if the piece is inside the puzzle board
				 */
				if(boardRectangle.contains(x,y)){
					/*
					 * update the PieceHolders
					 */
					puzzleArray = fromComponentPuzzle();
					pieceArray = fromComponent();

					/*
					 * check is highlighted fits into the given location and if the highlighted
					 * Piece is being dragged
					 */
					if(highlighted.getPiece().canFit(boardX,boardY,puzzleArray)
							&& highlighted.drawPiece().contains(x, y)){

						//	if a piece is highlighted
						if (highlighted.equals(notHighlighted) == false) {
							//	add the new piece into the puzzle board
							puzzleArray.addPiece(boardX, boardY,
									highlighted.getPiece());

							//	remove the piece from the tray
							pieceArray.takePiece(hPositionX, hPositionY);



							//	un-highlight the piece
							highlighted = notHighlighted;

							// update the PieceComponent arrays
							toComponent();
							toComponentPuzzle();

							repaint();
						}

					}
					// if the piece does not fit
					else{
						//	if a piece is highlighted
						if(highlighted.equals(notHighlighted) == false) {
							//	reset the location of highlighted back into the tray
							highlighted.setLocation(700 + CELL_SIZE
									* hPositionX * hGap, 100 + CELL_SIZE
									* hPositionY * vGap);

							//	un-highlight highlighted
							highlighted = notHighlighted;

							//	update the PieceComponent arrays
							toComponent();
							toComponentPuzzle();

							repaint();
						}
					}
				}
				/*
				 * if the piece was not dragged into the board
				 */
				else{		
					//	update the PieceComponent arrays
					toComponent();
					toComponentPuzzle();

					repaint();
				}
			}
			//if the Piece moved was originally in the board
			else{
				fromTray = true; //reset fromTray for next Piece
				//if the mouse was released in the board
				if(boardRectangle.contains(x,y)){
					/*
					 * update the PieceHolders
					 */
					puzzleArray = fromComponentPuzzle();
					pieceArray = fromComponent();

					//the Piece being moved must be removed from puzzleArray, otherwise, canFit may return false
					Piece moved = puzzleArray.takePiece(bPositionR, bPositionC);
					//if the Piece fits in the place it is being moved to 
					if(highlighted.getPiece().canFit(boardX,boardY,puzzleArray)){
						//if the Piece is highlighted
						if(highlighted.equals(notHighlighted) == false){
							//add the Piece to the puzzle
							puzzleArray.addPiece(boardX, boardY, moved);

							//	un-highlight the piece
							highlighted = notHighlighted;

							// update the PieceComponent arrays
							toComponent();
							toComponentPuzzle();

							repaint();
						}
					}
					//if the Piece does not fit in the spot that it was moved to in the board
					else{
						//put the Piece back in its original location if it fits
						if (highlighted.getPiece().canFit(bPositionR, bPositionC, puzzleArray)) {
							puzzleArray.addPiece(bPositionR, bPositionC, moved);
							toComponentPuzzle(); //reset the puzzleArrayComponent
							repaint();
						}
						//if the Piece does not fit in its original location (was rotated), then
						//place it in the first empty spot found in the Tray
						else{
							//pieceArray = fromComponent(); //CHANGE
							//puzzleArray = fromComponentPuzzle();

							//the row and column of a null location in the tray, initially set to dummy values
							int row = -1;
							int col = -1;
							//find the first null location in the tray
							for(int i = 0; i < numRows; i++){
								for(int j = 0; j < numCols; j++){
									if(pieceArray.getPieces()[i][j] == null){
										row = i;
										col = j;
										break;
									}
								}
							}
							//add the Piece being moved to the null location in the tray
							pieceArray.getPieces()[row][col] = highlighted.getPiece();

							//un-highlight the Piece
							highlighted = notHighlighted;

							//remove the Piece from the board
							puzzleArray.takePiece(bPositionR, bPositionC);

							// update the PieceComponent arrays
							toComponent();
							toComponentPuzzle();
							repaint();
						}
						
					}
				}
				//if the mouse was not released in the board
				else{
					
					pieceArray = fromComponent(); 
					puzzleArray = fromComponentPuzzle();

					//the row and column of a null location in the tray, initially set to dummy values
					int row = -1;
					int col = -1;
					//find the first null location in the tray
					for(int i = 0; i < numRows; i++){
						for(int j = 0; j < numCols; j++){
							if(pieceArray.getPieces()[i][j] == null){
								row = i;
								col = j;
								break;
							}
						}
					}
					//add the Piece being moved to the null location in the tray
					pieceArray.getPieces()[row][col] = highlighted.getPiece();

					//un-highlight the Piece
					highlighted = notHighlighted;

					//remove the Piece from the board
					puzzleArray.takePiece(bPositionR, bPositionC);

					// update the PieceComponent arrays
					toComponent();
					toComponentPuzzle();
					repaint();
				}
			}
			if(puzzleArray.isSolved()){
				JOptionPane.showMessageDialog(frame, "CONGRATULATIONS!!!!",
						"YOU WIN!!!!!",JOptionPane.WARNING_MESSAGE );
			}
		}

	}

	/**
	 * class RainbowListener for the actions performed when the Go Rainbow!! button is clicked
	 * contains instructions for the random color changing of the Pieces 
	 */
	private class RainbowListener implements ActionListener{
		boolean colorChange = false; //whether or not the colors are currently changing
		Timer t; //the Timer for the Color changer

		//called when the button is pressed
		public void actionPerformed(ActionEvent event){
			colorChange = !colorChange; //toggles colorChange
			
			//if the colors need to change
			if(colorChange){
				isChangingColor = true;
				rainbowButton.setText("Be Boring");

				/**
				 * class RainbowTimer contains the instructions for the Timer ActionEvent
				 */
				class RainbowTimer implements ActionListener{
					//called when the Timer sends the signal
					public void actionPerformed(ActionEvent event){
						repaint(); //repaint the Pieces with random colors
					}
				}
				//create and start the Timer
				RainbowTimer rainbowListener = new RainbowTimer();
				t = new Timer(100, rainbowListener);
				t.start();
			}
			//if the Colors no longer need to change
			else{
				isChangingColor = false;
				rainbowButton.setText("Color Change!!"); 
				t.stop(); 
				//update the pieceholders
				pieceArray = fromComponent();
				puzzleArray = fromComponentPuzzle();
				toComponent();
				toComponentPuzzle();
				repaint();
			}
		}
	}

	/**
	 * class: button listener for the instructions button
	 */
	private class InstructionsListener implements ActionListener{
		public InstructionsListener(){
		}
		public void actionPerformed(ActionEvent e){
			JOptionPane.showMessageDialog(frame, "Objective: Correctly fit all the pieces into the puzzle" + "\n" + 
					"Rules: Drag pieces onto the board." + "\n" +
					"Use the mouse wheel or right click to rotate the highlighted piece","Instructions",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	/**	
	 * class: a listener for the Reset Button
	 */
	private class MyButtonListener implements ActionListener{
		public MyButtonListener(){
		}
		/**
		 * when clicked, reset the board
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			reset();
			repaint();
		}

	}
	
	/*
	 * controls functions of a MouseWheelListener 
	 * When the mouse wheel is turned up, the Piece is rotated counterclockwise
	 * when the mouse wheel is turned down, the Piece is rotated clockwise
	 */
	private class MyMouseWheelListener implements MouseWheelListener{
		public MyMouseWheelListener(){
			addMouseWheelListener(this);
		}
		
		public void mouseWheelMoved(MouseWheelEvent e){
			if(e.getWheelRotation() == 1){
				highlighted.getPiece().rotateClockwise();
			}
			else if(e.getWheelRotation() == -1){
				highlighted.getPiece().rotateCounterClockwise();
			}
			repaint();
		}
	}
	/**
	 * class: button listener for the solve button
	 *
	 */
	private class SolveButtonListener implements ActionListener{
		public SolveButtonListener(){
		}
		@Override
		/**
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {
			//	reset the board and tray
			reset();

			//	solve the tray
			Solver s = new Solver(pieceArray.getPieces());
			s.solve();

			//	puzzleArray now has the solved set of pieces
			puzzleArray.setPieces(s.getSolution());

			//	update puzzleArray
			toComponentPuzzle();

			// empty the tray
			for(int i = 0; i < numRows; i ++){
				for(int j = 0;j < numRows; j++){
					compArray[i][j].setPiece(null);
				}
			}
			repaint();
			// display when puzzle has been solved
			JOptionPane.showMessageDialog(frame, "Puzzle Solved",
					"Solved",JOptionPane.ERROR_MESSAGE);

		}

	}
	/**
	 * Will draw the board using rectangles
	 * @param g
	 */
	public void paintBoard(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		//	paint the rectangles black
		g2.setColor(Color.WHITE);
		g2.draw(boardRectangle);
		for(int i = 0; i  < numRows; i++){
			for(int j = 0; j < numCols; j++){
				if (boardArray[i][j] != null) {
					/*
					 * draw the rectangle
					 */
					g2.draw(boardArray[i][j]);
				}
			}
		}
	}
	private JButton resetButton = new JButton("Reset");
	private JButton solveButton = new JButton("CHEAT");
	private JButton rainbowButton = new JButton("Color Change!!");
	private JButton instructions = new JButton("Help");

	//	panel where the buttons will be
	private JPanel buttonPanel = new JPanel();
	/**
	 * adds the mouseListeners and/or anything else we will need i.e buttons, other listeners
	 * to a Container object
	 */
	public void init(JFrame f){
		frame = f;
		Container cp = f.getContentPane();
		//	add the drag and drop feature
		new MyDragAndDrop(this);
		//	initialize the listeners, then add then to their respective
		//	counterparts
		ActionListener resetListener = new MyButtonListener();
		ActionListener solveListener = new SolveButtonListener();
		ActionListener helpListener = new InstructionsListener();
		RainbowListener rainbowListener = new RainbowListener();
		new MyMouseWheelListener();

		resetButton.addActionListener(resetListener);
		solveButton.addActionListener(solveListener);
		rainbowButton.addActionListener(rainbowListener);
		instructions.addActionListener(helpListener);
		//	add the buttons to the JPanel
		buttonPanel.add(resetButton);
		buttonPanel.add(solveButton);
		buttonPanel.add(rainbowButton);
		buttonPanel.add(instructions);
		//	add the JPanel to the top of the JFrame
		cp.add(buttonPanel, BorderLayout.NORTH);
	}

	public void run() {
		// TODO Auto-generated method stub
		try {
			JFrame f = new JFrame();
			f.setTitle("Java Jigsaw Puzzle");
			f.setSize(1015,560 + (540 - 486) + (560 - 540));
			f.setResizable(false);
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			f.setBackground(Color.BLACK);
			DisplayComponent board = new DisplayComponent();
			board.init(f);
			f.add(board);
			f.setVisible(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}







