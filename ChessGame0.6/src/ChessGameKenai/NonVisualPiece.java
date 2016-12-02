package ChessGameKenai;

import java.awt.Color;
import java.io.Serializable;

import ChessGameKenai.model.CPiece;
import ChessGameKenai.model.Piece;
import ChessGameKenai.model.PieceBishop;
import ChessGameKenai.model.PieceKing;
import ChessGameKenai.model.PieceKnight;
import ChessGameKenai.model.PiecePawn;
import ChessGameKenai.model.PieceQueen;
import ChessGameKenai.model.PieceRook;

/**
 * The Non_Visual_Piece class is the abstract piece that is not visual to the
 * user but it's the piece that our data class(Chess_Data) uses to move the
 * piece then when the move is made the view will adjust the position of the
 * visual piece that represents the non visual piece that was moved it is done
 * by asking the piece what is type, color or position. The non visual piece has
 * the reference to the data class because all the data to move, save load is
 * stored the data class is the data holder class of our chess game
 * 
 * @author Dimitri Pankov
 * @see Serializable
 * @version 1.5
 */
public class NonVisualPiece implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;
	private int position;
	private Color color;
	private int countClicks;
	private int previousPosition;
	private transient Chess_Data data;
	private boolean isCaptured;
	private boolean isMoved;
	private boolean isQueenFromPawn;
	private Piece piece;

	/**
	 * Overloaded constructor of our class it receives all needed references for
	 * its creation during its lifespan this object will know all the
	 * information specified in the constructor.
	 * 
	 * @param data
	 *            as Chess_Data
	 * @param type
	 *            as a String
	 * @param position
	 *            as an integer
	 * @param color
	 *            as Color Refactor -> removed Color
	 */
	private NonVisualPiece(Chess_Data data, String type, int position) {
		this.type = type;
		this.position = position;
		this.color = CPiece.getColor(type);
		this.data = data;
		setPolymorphicPiece();
	}

	public static NonVisualPiece create(Chess_Data data, String type, int position) {
		return new NonVisualPiece(data, type, position);
	}

	/**
	 * The method isMoved simply tells the caller if the current pieces was
	 * moved this method is only used by the rook and king for castling purposes
	 * 
	 * @return isMoved as a boolean
	 */
	public boolean isMoved() {
		return isMoved;
	}

	/**
	 * The isQueenFromPawn method sets the boolean value to true or false to
	 * tell the game that this queen was created from pawn
	 * 
	 * @param isQueenFromPawn
	 *            as a boolean
	 */
	public void isQueenFromPawn(boolean isQueenFromPawn) {
		this.isQueenFromPawn = isQueenFromPawn;
	}

	/**
	 * The method isQueenFromPawn tells the user if the current Queen was
	 * created from pawn or not
	 * 
	 * @return isQueenFromPawn as a boolean
	 */
	public boolean isQueenFromPawn() {
		return isQueenFromPawn;
	}

	/**
	 * The method isMoved simply sets the piece that was moved to true default
	 * is false to tell the game that this piece was moved
	 * 
	 * @param isMoved
	 *            as a boolean
	 */
	public void isMoved(boolean isMoved) {
		this.isMoved = isMoved;
	}

	/**
	 * The method isCaptured tells the game that the piece was captured or not
	 * 
	 * @return isCaptured as a boolean
	 */
	public boolean isCaptured() {
		return isCaptured;
	}

	/**
	 * The method isCaptured simply sets the boolean value of the captured piece
	 * to true to tell the game that a certain piece as captured
	 * 
	 * @param isCaptured
	 *            as a boolean
	 */
	public void isCaptured(boolean isCaptured) {
		this.isCaptured = isCaptured;
	}

	public Color getColor() {
		return color;
	}

	/**
	 * The method canMove simply tells the game if the current piece can move or
	 * not.
	 * 
	 * @return true if the piece is can move false other wise
	 */
	public boolean canMove() {
		if (data.getPieceAtPosition(this.getPosition() - 1) == null)
			return false;

		// if (data.getActivePieces().get(this.getPosition() - 1) == null)
		// return false;

		for (int i = 0; i < data.getPiecesSize(); i++) { // data.getActivePieces().size()
			if (isMoveable(getPosition(), (i + 1))) {
				return true;
			}
		}

		return false;
	}

	/**
	 * The method move simply moves the current piece to its new position by
	 * calling the Chess_Data move method which does all the job for us
	 */
	public void move() {
		if (data.getPiecePosition() > 0) {
			data.move(this.getPosition(), data.getPiecePosition());
		}
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getPreviousPosition() {
		return previousPosition;
	}

	public void setPreviousPosition(int previousPosition) {
		this.previousPosition = previousPosition;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

	/**
	 * The method setType simply sets the type of the piece
	 * 
	 * @param type
	 *            as a String
	 */
	public void setType(String type) {
		this.type = type;
	}

	public String getColoredType() {
		return type;
	}

	/**
	 * The method getClickCount simply returns the clickCount on the current
	 * piece
	 * 
	 * @return countClicks as an integer
	 */
	public int getClickCount() {
		return countClicks;
	}

	/**
	 * The method setClickCount simply sets clickCount of the piece
	 * 
	 * @param countClicks
	 *            as an integer
	 */
	public void setClickCount(int countClicks) {
		this.countClicks = countClicks;
	}

	/**
	 * The method getPieceType returns the type the user user
	 * 
	 * @return as a String
	 */
	public String getPieceTypeOnly() {
		return type.substring(1);
	}

	/**
	 * The toString method of the class this is an overwritten method of the
	 * object super class this is how our object will represent itself as a
	 * String
	 * 
	 * @return s as a String
	 */
	@Override
	public String toString() {
		String s = "";
		s += "Type: " + this.getColoredType() + ", Position:"
				+ this.getPosition();
		return s;
	}

	public boolean isColoredTypeEquals(String pieceType) {
		return this.type.equals(pieceType);
	}

	public boolean isTypeEquals(String pieceType) {
		return this.getPieceTypeOnly().equals(pieceType);
	}

	public boolean isWhite() {
		return color == Color.WHITE;
	}

	public boolean isBlack() {
		return color == Color.BLACK;
	}

	private void setPolymorphicPiece() {

		if (isTypeEquals(CPiece.King)) {
			piece = new PieceKing();

		} else if (isTypeEquals(CPiece.Queen)) {
			piece = new PieceQueen();

		} else if (isTypeEquals(CPiece.Pawn)) {
			piece = new PiecePawn();

		} else if (isTypeEquals(CPiece.Rook)) {
			piece = new PieceRook();

		} else if (isTypeEquals(CPiece.Knight)) {
			piece = new PieceKnight();

		} else if (isTypeEquals(CPiece.Bishop)) {
			piece = new PieceBishop();

		}
	}

	public boolean isMoveable(int pos, int des) {
		return piece.isMoveable(data.getActivePieces(), pos, des);
	}

	public void makeMove(int pos, int des) {
		piece.makeMove(this, data, pos, des);
		isMoved = true;
	}

	public boolean isKill(int pos, int des) {
		return piece.isKill(this, data, pos, des);
	}
}
