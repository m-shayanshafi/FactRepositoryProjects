package ChessGameKenai;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

/**
 * The Board class is the Board of our Chess Game it consists of a JPanel with
 * the 8 by 8 layout it also has set pieces for game This class is an Observer 
 * to the data class it is notified by executing its update method The Board
 * gets the non visual pieces array list from the data class when its update
 * method is called and asks the non visual pieces for their color, type and
 * position and them replaces them with visual pieces that each non visual piece
 * represents it is happened each time the move method of the data class is
 * called thus executing this method The Board also knows how to flip itself it
 * only changes the view nothing is change in the code Also depending on who's
 * turn it is the listeners will be added or removed
 * 
 * @author Dimitri Pankov
 * @see Observer
 * @version 1.6
 */
public abstract class Board extends JPanel implements Observer {

	private Board currentBoard;
	private boolean isWhite = true;
	protected ArrayList<Square> squares;
	protected HashMap<String, String> imageMap = new HashMap<String, String>();
	protected Chess_Data chessData;
	private ArrayList<VisualPiece> visualPieces = new ArrayList<VisualPiece>();
	private boolean isFirstTime = true;
	protected ChessBoardView chessBoardView;
	private HashMap<Integer, String> mapPositions = new HashMap<Integer, String>();
	protected ArrayList<String> positions = new ArrayList<String>();

	/**
	 * The method flipBoard simply flips the board to normal or flip state
	 */
	public abstract void flipBoard();

	public Board() {
	}

	/**
	 * The overloaded constructor of the class sets the layout of the board to 8
	 * by 8 adds the squares the board by calling setSquares method It also
	 * creates a map that we use to map the images when non visual piece is we
	 * use to draw images. Each non visual piece has a unique type which is used
	 * to map its image when for drawing the visual piece it represents Then it
	 * populates the board with visual piece using map draw them
	 * 
	 * @param data
	 *            as Chess_Data
	 * @param view
	 *            as ChessBoardView
	 */
	public Board(Chess_Data data, ChessBoardView view) {

		this.chessData = data;
		this.chessBoardView = view;
		setImageMap();
		setPositions();
	}

	private void setImageMap() {
		imageMap.put("WKing", "ChessPieces/wKing46.gif");
		imageMap.put("BKing", "ChessPieces/bKing46.gif");
		imageMap.put("WQueen", "ChessPieces/wQueen46.gif");
		imageMap.put("BQueen", "ChessPieces/bQueen46.gif");
		imageMap.put("WBishop", "ChessPieces/wBishop46.gif");
		imageMap.put("BBishop", "ChessPieces/bBishop46.gif");
		imageMap.put("WKnight", "ChessPieces/wKnight46.gif");
		imageMap.put("BKnight", "ChessPieces/bKnight46.gif");
		imageMap.put("WRook", "ChessPieces/wRook46.gif");
		imageMap.put("BRook", "ChessPieces/bRook46.gif");
		imageMap.put("WPawn", "ChessPieces/wPawn46.gif");
		imageMap.put("BPawn", "ChessPieces/bPawn46.gif");
	}

	private void setPositions() {
		positions.add("a");
		positions.add("b");
		positions.add("c");
		positions.add("d");
		positions.add("e");
		positions.add("f");
		positions.add("g");
		positions.add("h");
	}

	/**
	 * The populateBoard method creates visualPieces that represent non
	 * visualPieces in the data class this method is executed ones in the
	 * constructor to draw visual pieces the first time it fills the array list
	 * of visual piece for later use
	 */
	public void populateBoard() {
		NonVisualPieceIterator iterator = chessData.getPiecesIterator();
		int k = 0;
		while(iterator.hasNext()){
			NonVisualPiece nvp = iterator.getNext();
			
			if(nvp != null){
				populateVisualPieces(nvp);
			}
			this.mapAllPositions(k + 1);
			k++;
		}
		
		/*for (int i = 0; i < chessData.getActivePieces().size(); i++) {
			if (chessData.getActivePieces().get(i) != null) {
				NonVisualPiece nonVisualPiece = chessData.getActivePieces()
						.get(i);

				populateVisualPieces(nonVisualPiece);
			}
			this.mapAllPositions(i + 1);
		}*/
		for (int i = 0; i < visualPieces.size(); i++) {
			squares.get(visualPieces.get(i).getPosition() - 1).add(
					visualPieces.get(i));
			visualPieces.get(i).setBounds(5, 5, 40, 40);

			visualPieces.get(i).repaint();
		}
		this.notifyView();
	}

	private void populateVisualPieces(NonVisualPiece p) {

		visualPieces.add(new VisualPiece(this, p, imageMap.get(p
				.getColoredType())));

	}

	/**
	 * The method removeAllPieces removes all pieces. It is used to restart game
	 */
	public void removeAllPieces() {
		for (int i = 0; i < squares.size(); i++) {
			if (squares.get(i).getComponentCount() > 0) {
				squares.get(i).remove(0);
			}
		}
		this.revalidate();
		this.repaint();
	}

	public ArrayList<Square> getSquares() {
		return squares;
	}

	/**
	 * used to draw visual pieces on the board
	 * 
	 * @return imageMap as HashMap
	 */
	public HashMap<String, String> getImageMap() {
		return imageMap;
	}

	/**
	 * The method setSquares simply creates the squares and adds them to the board and populate
	 * it with pieces
	 */
	public void setSquares() {
		int yPosition = 0;
		int xPosition = 0;

		for (int i = 0; i < 64; i++) {
			xPosition = i;
			if ((i + 1) > 8 && (i + 1) % 8 != 0) {
				xPosition = ((i + 1) % 8) - 1;
			}
			if ((i + 1) % 8 == 0) {
				xPosition = 7;
			}
			if ((i) % 8 == 0) {
				xPosition = 0;
			}

			if (isWhite) {

				squares.add(new Square(Color.WHITE, (i + 1)));

				squares.get(i).setBackground(Color.WHITE);
				squares.get(i).setBounds(xPosition * (65), yPosition, 65, 65);
				squares.get(i).repaint();
				isWhite = !isWhite;
			} else {

				squares.add(new Square(Color.BLACK, (i + 1)));

				squares.get(i).setBackground(Color.BLACK);
				squares.get(i).setBounds(xPosition * (65), yPosition, 65, 65);
				squares.get(i).repaint();
				isWhite = !isWhite;
			}
			if ((i + 1) % 8 == 0) {
				isWhite = !isWhite;
				yPosition += 65;
			}
			this.add(squares.get(i));
		}
		for (int i = 0; i < visualPieces.size(); i++) {
			squares.get(visualPieces.get(i).getPosition() - 1).add(
					visualPieces.get(i));
		}
	}


	public Board getCurrentBoard() {
		return currentBoard;
	}

	/**
	 * The method setCurrentBoard sets the current board it is either flipped or
	 * normal
	 * 
	 * @param currentBoard
	 *            as an integer
	 */
	public void setCurrentBoard(Board currentBoard) {
		this.currentBoard = currentBoard;
	}

	/**
	 * The method distributeListeners simply distribute listeners depending on
	 * who's turn it is adds or removes listeners
	 */
	public void distributeListeners() {
		if (!chessData.isWhiteTurn()) {
			for (int i = 0; i < visualPieces.size(); i++) {
				if (visualPieces.get(i).getColor() == Color.BLACK) {
					visualPieces.get(i).addListener();
				} else {
					visualPieces.get(i).removeListener();
				}
			}
		} else {
			for (int i = 0; i < visualPieces.size(); i++) {
				if (visualPieces.get(i).isWhite()) {
					visualPieces.get(i).addListener();
				} else {
					visualPieces.get(i).removeListener();
				}
			}
		}
	}

	/**
	 * The method addListeners adds the listeners to the specified color it
	 * loops through the list of visual pieces and adds the listeners to the
	 * specified color pieces
	 * 
	 * @param color
	 *            as a Color
	 */
	public void addListeners(Color color) {
		for (int i = 0; i < visualPieces.size(); i++) {
			VisualPiece visualPiece = visualPieces.get(i);
			if (visualPiece.getColor() == color) {
				visualPiece.addListener();
			}
		}
	}

	/**
	 * The method removeListeners removes the listeners to the specified color
	 * it loops through the list of visual pieces and removes the listeners from
	 * the specified color pieces
	 * 
	 * @param color
	 *            as a Color
	 */
	public void removeListeners(Color color) {
		for (int i = 0; i < visualPieces.size(); i++) {
			VisualPiece visualPiece = visualPieces.get(i);
			if (visualPiece.getColor() == color) {
				visualPiece.removeListener();
			}
		}
	}
	public void clearPieces(){
		this.visualPieces.clear();
	}
	
	public ArrayList<VisualPiece> getPieces() {
		return visualPieces;
	}

	/**
	 * The method update is called each time the data class changes.It redraws 
	 * pieces then it distributes the listeners accordingly and if any pieces
	 *  were captured they are removed from the array list of pieces
	 * 
	 * @param observable
	 *            as an Observable object
	 * @param object
	 *            as an Object any object
	 */
	public void update(Observable observable, Object object) {
		this.redrawPieces();
		if (!chessData.isWinner() && !chessData.isGameOnLine()) {
			this.distributeListeners();
		}
		if (chessData.isGameOnLine()) {
			this.distributeOnLineListeners();
		}
		if (!chessData.isServer() && isFirstTime) {
			this.removeListeners(Color.BLACK);
			this.removeListeners(Color.WHITE);
			isFirstTime = false;
		}
		if (object != null) {
			ArrayList list = (ArrayList) object;
			String turn = "";
			if (squares.get((Integer) list.get(1) - 1).getComponentCount() > 0) {
				VisualPiece visualPiece = ((VisualPiece) squares.get(
						(Integer) list.get(1) - 1).getComponent(0));
				if (visualPiece.getColor() == Color.WHITE) {
					turn = "W" + visualPiece.getType();
				} else {
					turn = "B" + visualPiece.getType();
				}
			}
			chessBoardView.getMoves().append(
					turn + " from: " + mapPositions.get(list.get(0)) + " to "
							+ mapPositions.get(list.get(1)) + "\n");
			chessBoardView.getMoves().append("--------------------------\n");
			chessBoardView.getMoves().setCaretPosition(
					chessBoardView.getMoves().getDocument().getLength());
		}
		this.removeCapturedPieces();
		this.revalidate();
		this.repaint();
	}

	/**
	 * The method isFirstTime simply sets the boolean value to true or false
	 * depending on the user's choice
	 * 
	 * @param isFirstTime
	 *            as a boolean
	 */
	public void setIsFirstTime(boolean isFirstTime) {
		this.isFirstTime = isFirstTime;
	}

	/**
	 * The method redrawPieces is called each time the data notifies the views
	 * because this method is inside the update method the job of this method is
	 * to Check the list of active pieces that is stored in the data class loop
	 * through the array list and check if the which non visual piece was moved
	 * then redraw its view accordingly to do that it needs to know the last
	 * position of the non visual piece and compare it to the current position
	 * of the visual piece if they are equal that means its the same piece so
	 * the visual piece changes its current position to the current position of
	 * the non visual piece then redraws its view as needed
	 */
	public void redrawPieces() {
		NonVisualPieceIterator iterator = chessData.getPiecesIterator();
		int k = 0;
		while(iterator.hasNext()){
			NonVisualPiece nvPiece = iterator.getNext();
			if (nvPiece != null) {
				for (int j = 0; j < visualPieces.size(); j++) {
					VisualPiece peice = visualPieces.get(j);
					if (nvPiece.isQueenFromPawn()
							&& peice.getPosition() == nvPiece
									.getPreviousPosition()) {
						VisualPiece visualPiece = (VisualPiece) squares.get(
								nvPiece.getPreviousPosition() - 1)
								.getComponent(0);
						visualPieces.remove(visualPiece);
						squares.get(nvPiece.getPreviousPosition() - 1)
								.remove(0);
						VisualPiece newVpiece = new VisualPiece(this, nvPiece,
								imageMap.get(nvPiece.getColoredType()));
						visualPieces.add(newVpiece);
						getSquares().get(nvPiece.getPosition() - 1).add(
								newVpiece);
						newVpiece.setBounds(5, 5, 55, 55);
						peice.revalidate();
						peice.repaint();
						peice.removeListener();
						nvPiece.isQueenFromPawn(false);
					} else {
						if (nvPiece.getColor() == peice.getColor()
								&& nvPiece.getPieceTypeOnly().equals(
										peice.getType())
								&& nvPiece.getPreviousPosition() == peice
										.getPosition()) {
							getSquares().get(nvPiece.getPosition() - 1).add(
									(VisualPiece) peice);
							peice.setPosition(nvPiece.getPosition());
						}
						if (nvPiece.getPreviousPosition() > 0) {
							Square currentSquare = squares.get(nvPiece
									.getPreviousPosition() - 1);
							currentSquare.setBackground(currentSquare
									.getColor());
							currentSquare.revalidate();
							currentSquare.repaint();
						}
						peice.revalidate();
						peice.repaint();
						peice.removeListener();
					}
				}
			}
			
			
			
		}
		/*
		for (int i = 0; i < chessData.getActivePieces().size(); i++) {
			if (chessData.getActivePieces().get(i) != null) {
				NonVisualPiece nvPiece = chessData.getActivePieces().get(i);
				for (int j = 0; j < visualPieces.size(); j++) {
					VisualPiece peice = visualPieces.get(j);
					if (nvPiece.isQueenFromPawn()
							&& peice.getPosition() == nvPiece
									.getPreviousPosition()) {
						VisualPiece visualPiece = (VisualPiece) squares.get(
								nvPiece.getPreviousPosition() - 1)
								.getComponent(0);
						visualPieces.remove(visualPiece);
						squares.get(nvPiece.getPreviousPosition() - 1)
								.remove(0);
						VisualPiece newVpiece = new VisualPiece(this, nvPiece,
								imageMap.get(nvPiece.getColoredType()));
						visualPieces.add(newVpiece);
						getSquares().get(nvPiece.getPosition() - 1).add(
								newVpiece);
						newVpiece.setBounds(5, 5, 55, 55);
						peice.revalidate();
						peice.repaint();
						peice.removeListener();
						nvPiece.isQueenFromPawn(false);
					} else {
						if (nvPiece.getColor() == peice.getColor()
								&& nvPiece.getPieceTypeOnly().equals(
										peice.getType())
								&& nvPiece.getPreviousPosition() == peice
										.getPosition()) {
							getSquares().get(nvPiece.getPosition() - 1).add(
									(VisualPiece) peice);
							peice.setPosition(nvPiece.getPosition());
						}
						if (nvPiece.getPreviousPosition() > 0) {
							Square currentSquare = squares.get(nvPiece
									.getPreviousPosition() - 1);
							currentSquare.setBackground(currentSquare
									.getColor());
							currentSquare.revalidate();
							currentSquare.repaint();
						}
						peice.revalidate();
						peice.repaint();
						peice.removeListener();
					}
				}
			}
		} */
	}

	/**
	 * The method removeCapturedPieces simply removes the captured pieces from
	 * the array list of pieces the piece will be added to the captured pieces 
	 * panel
	 */
	public void removeCapturedPieces() {
		if (!chessData.getCapturedPieces().isEmpty()) {
			NonVisualPiece nonVisualPiece = (NonVisualPiece) chessData
					.getCapturedPieces().get(
							chessData.getCapturedPieces().size() - 1);
			for (int i = 0; i < visualPieces.size(); i++) {
				if (visualPieces.get(i).getPiece().equals(nonVisualPiece)) {
					visualPieces.remove(visualPieces.get(i));
				}
			}
		}
	}

	/**
	 * The method distributeOnLineListeners simply distribute listeners
	 * depending on who's turn it is adds or removes listeners 
	 */
	public void distributeOnLineListeners() {
		if (chessData.isServer()) {
			if (chessData.isWhiteTurn()) {
				this.addListeners(Color.WHITE);
				this.removeListeners(Color.BLACK);
			} else {
				this.removeListeners(Color.WHITE);
				this.removeListeners(Color.BLACK);
			}
		} else {
			if (!chessData.isWhiteTurn()) {
				this.addListeners(Color.BLACK);
				this.removeListeners(Color.WHITE);
			} else {
				this.removeListeners(Color.BLACK);
				this.removeListeners(Color.WHITE);
			}
		}
	}

	public void notifyView() {
		chessData.notifyView();
	}

	/**
	 * The startLocalTimer method starts the timer if the game is played locally
	 * no connection
	 */
	public void startLocalTimer() {
		if (ChessBoardView.getConnectionInstance() == null) {
			chessBoardView.startTimer();
		}
	}

	/**
	 * The mapPositions method simply maps the positions to of the squares to
	 * standard chess game positions like e4, f5 etc... are all mapped to
	 * integers from 1 to 64
	 * 
	 * @param position
	 *            as an integer
	 */
	public void mapAllPositions(int position) {
		if (position <= 8) {
			mapPositions.put(position, 8 + positions.get(position - 1));
		} else if (position > 8 && position <= 16) {
			mapPositions.put(position, 7 + positions.get(position - 9));
		} else if (position > 16 && position <= 24) {
			mapPositions.put(position, 6 + positions.get(position - 17));
		} else if (position > 24 && position <= 32) {
			mapPositions.put(position, 5 + positions.get(position - 25));
		} else if (position > 32 && position <= 40) {
			mapPositions.put(position, 4 + positions.get(position - 33));
		} else if (position > 40 && position <= 48) {
			mapPositions.put(position, 3 + positions.get(position - 41));
		} else if (position > 48 && position <= 56) {
			mapPositions.put(position, 2 + positions.get(position - 49));
		} else if (position > 56) {
			mapPositions.put(position, 1 + positions.get(position - 57));
		}
	}

	public HashMap<Integer, String> getmapPositions() {
		return mapPositions;
	}
}
