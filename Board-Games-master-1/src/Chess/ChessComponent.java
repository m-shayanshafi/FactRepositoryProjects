package Chess;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JFrame;

import javax.swing.event.MouseInputAdapter;


public class ChessComponent extends JComponent implements Runnable{

	private static final long serialVersionUID = 1L;
	// # of rows and cols in the board
	private int numRows = 8, numCols = 8;
	// size of one cell in the board
	private final int CELL_SIZE = 55;
	//	board to hold the chess pieces
	private ChessBoard chessBoard = new ChessBoard();
	//	array of rectangles to draw the board
	private Rectangle2D.Double[][] rectangleArray = new Rectangle2D.Double[8][8];
	// outline of the board rectangle
	private Rectangle2D.Double boardRect = new Rectangle2D.Double(10,10,CELL_SIZE*numRows,CELL_SIZE*numCols);
	//	reference to the highlighted piece
	private ChessPiece highlighted = null;
	private ChessPiece notHighlighted = null;
	//	store frameX and Y coordinate of the highlighted piece
	int frameX = -1, frameY = -1;
	private boolean isHighlighted = false;
	/*
	 * the current color's turen to move
	 */
	private int currentColor = ChessPiece.WHITE;
	/*
	 * Constructor: fill the chess board with pieces
	 * 				fill the rectangle array
	 */
	public ChessComponent(){
		/*
		 * fill rectangle array
		 */
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){
				rectangleArray[i][j] = new Rectangle2D.Double(10 + i*CELL_SIZE,10 + j*CELL_SIZE, CELL_SIZE, CELL_SIZE);
			}
		}
		/*
		 * fill the board
		 */
		chessBoard.fillWithDefaultPieces();
	}
	public void switchColor(){
		if(currentColor == ChessPiece.BLACK)
			currentColor = ChessPiece.WHITE;
		else currentColor = ChessPiece.BLACK;
	}
	/*
	 * paints the board with rectangles
	 */
	public void paintBoard(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){
				g2.draw(rectangleArray[i][j]);
			}
		}
	}
	/*
	 *	paint component
	 */
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		// paint the background white
		g2.setColor(Color.WHITE);
		g2.fill(new Rectangle2D.Double(0,0,getWidth(),getHeight()));
		g2.setColor(Color.BLACK);
		// paint the grid lines
		paintBoard(g);
		/*
		 * paint the pieces
		 */
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){
				if(chessBoard.getOccupant(i,j) != null){
					// paints the highlighted piece
					if(highlighted != null && highlighted.equals(chessBoard.getOccupant(i,j)) && isHighlighted
							&& frameX != -1 && frameY != -1){
						g2.drawImage(chessBoard.getOccupant(i,j).getImage(),frameX,frameY,null);
					}
					// paints everything else
					else{
						int xCoord = 10 + chessBoard.getOccupant(i,j).getCol()*CELL_SIZE;
						int yCoord = 10 + chessBoard.getOccupant(i,j).getRow()*CELL_SIZE;
						int offset = 7;
						g2.drawImage(chessBoard.getOccupant(i,j).getImage(),xCoord+offset,yCoord+offset,null);
					}
						
				}
				
			}
		}
	}
	public int initialR = 0;
	public int initialC = 0;
	/*
	 * listener class for dragging pieces
	 */
	private class MyDragAndDrop extends MouseInputAdapter{
		private Point offset = new Point();
		/*
		 * Constructor: add Listeners
		 */
		public MyDragAndDrop(){
			addMouseListener(this);
			addMouseMotionListener(this);
		}
		public void mousePressed(MouseEvent arg0){
			int x = arg0.getX();
			int y = arg0.getY();
			for(int i = 0; i < numRows; i++){
				for(int j = 0; j < numCols; j++){
					if(chessBoard.getOccupant(i, j) != null && rectangleArray[j][i].contains(x,y) && chessBoard.getOccupant(i, j).getColor() == currentColor){
						highlighted = chessBoard.getOccupant(i, j);
						initialR = i;
						initialC = j;
						isHighlighted = true;
					}
				}
			}
			repaint();
		}
		public void mouseDragged(MouseEvent arg0){
			int x = arg0.getX() - offset.x;
			int y = arg0.getY() - offset.y;
			frameX = x;
			frameY = y;
			repaint();
		}
		public void mouseReleased(MouseEvent arg0){
			int x = arg0.getX();
			int y = arg0.getY();
			// if the piece was dropped in the chess board
			if(boardRect.contains(x,y)){
				// cycle through the rectangles to see which rectangle hold the moved piece
				for(int i = 0; i < numRows; i++){
					for(int j = 0; j < numCols; j++){
						if(rectangleArray[i][j].contains(x,y)){
							if (highlighted != notHighlighted) {
								if ((i != initialC || j != initialR) && highlighted.canMove(chessBoard, j, i)) {
									// move the piece to the new location
									chessBoard.movePiece(highlighted.getRow(),
											highlighted.getCol(), j, i);
									switchColor();
								}
							}
						}
					}
				}
				repaint();
				isHighlighted = false;
				frameX = -1;
				frameY = -1;
				highlighted = notHighlighted;
			}
			else{
				isHighlighted = false;
				frameX = -1;
				frameY = -1;
				highlighted = notHighlighted;
				repaint();
			}
			
		}
		
	}
	public String getWinner(){
		if(currentColor == ChessPiece.WHITE)
			return "White";
		else return "Black";
	}
	public void init(){
		new MyDragAndDrop();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			JFrame f = new JFrame();
			f.setBackground(Color.BLACK);
			f.setTitle("Java Chess");
			f.setSize(470,490);
			f.setResizable(false);
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			ChessComponent board = new ChessComponent();
			board.init();
			f.add(board);
			f.setVisible(true);
		}catch(Exception e){
			
		}
	}

}
