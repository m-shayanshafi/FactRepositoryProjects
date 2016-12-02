package ChessGameKenai;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import javax.swing.JPanel;

/**
 * The VisualPiece class is the piece that is visual to the user it is only
 * displayed in our view class which is a ChessBoardView The Visual Piece knows
 * its color, position, imagePath type and etc.... This calls has two
 * constructors so we can create visual pieces into two different ways. Each
 * Visual Piece has reference to the board so it can call the board methods and
 * each Visual Piece also has the reference of the non visual piece it
 * represents for easy communication We have decided to use visual and non
 * visual pieces so when we serialize the piece only non visual piece will be
 * serialized it foolproof method to save objects or transport objects over the
 * network, because graphical components are not saveable the will be out of
 * date and no use to us after loading them back so the decision was made to
 * have both visual and non visual pieces for the chess game
 * 
 * @author Dimitri Pankov
 * @see JPanel
 * @version 1.5
 */
public class VisualPiece extends JPanel {

	private int position;
	private int clickCount = 0;
	final private String imagePath;
	private NonVisualPiece piece;
	private MouseListener listener;
	private Board board;

	/**
	 * Overloaded constructor of our class receives the path to its image
	 * 
	 * @param imagePath
	 *            as a String
	 */
	public VisualPiece(final String imagePath) {
		this.imagePath = imagePath;
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(64, 64));
	}

	/**
	 * Overloaded Constructor of our class receives all needed references for
	 * its creation The created object will know all information specified
	 * during its creation during its life time
	 * 
	 * @param board
	 *            as a Board
	 * @param piece
	 *            as a Piece
	 * @param imagePath
	 *            as a String
	 * 
	 */
	public VisualPiece(final Board board, final NonVisualPiece piece,
			final String imagePath) {
		this.position = piece.getPosition();
		this.board = board;
		this.imagePath = imagePath;
		this.piece = piece;
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(64, 64));
		createMouseAdaptor();
		this.addMouseListener(listener);
	}

	private void createMouseAdaptor() {
		listener = new MouseAdapter() {

			@Override
			public void mousePressed(final MouseEvent event) {
				if (VisualPiece.this.piece.canMove()) {
					VisualPiece.this.piece.setClickCount(++clickCount);
					final Square currentSquare = ((Square) VisualPiece.this.board
							.getSquares().get(
									VisualPiece.this.getPosition() - 1));
					currentSquare.setBackground(Color.BLUE);
					clickCount = 0;
					if (VisualPiece.this.getColor() == Color.WHITE) {
						VisualPiece.this.board.removeListeners(Color.WHITE);
					} else {
						VisualPiece.this.board.removeListeners(Color.BLACK);
					}
				}
				VisualPiece.this.board.startLocalTimer();
			}
		};
	}

	public void removeListener() {
		this.removeMouseListener(listener);
	}

	public void addListener() {
		this.addMouseListener(listener);
	}

	public NonVisualPiece getPiece() {
		return piece;
	}

	public Color getColor() {
		return piece.getColor();
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(final int position) {
		this.position = position;
	}

	public String getType() {
		return piece.getPieceTypeOnly();
	}

	public String getTextColor() {

		if (this.isWhite()) {
			return "White";
		} else {
			return "Black";
		}
	}

	/**
	 * The method setClickCount sets clickCount
	 * 
	 * @param clickCount
	 *            sets clicks for the object
	 */
	public void setClickCount(final int clickCount) {
		this.clickCount = clickCount;
	}

	/**
	 * The method getClickCount returns the click count on the current object
	 * This method is needed to check if the piece was clicked or not
	 * 
	 * @return clickCount as an integer
	 */
	public int getClickCount() {
		return clickCount;
	}

	/**
	 * The method toString() is overridden by our class which has implementation
	 * in the super class
	 * 
	 * @return s as String text representation of the object
	 */
	@Override
	public String toString() {
		String stringValue = "";
		stringValue += this.getType() + ", " + this.getTextColor() + ", "
				+ this.getPosition();
		return stringValue;
	}

	/**
	 * The method paintComponent of the Pawn_View class
	 * 
	 * @param graphics
	 *            Graphics object used to paint this object
	 */
	@Override
	public void paintComponent(final Graphics graphics) {
		super.paintComponent(graphics);
		final int width = this.getWidth();
		final int height = this.getHeight();
		final URL url = this.getClass().getResource(imagePath);
		final Toolkit toolkit = this.getToolkit();
		final Image image = toolkit.getImage(url);
		graphics.drawImage(image, 0, 0, width, height, this);
	}

	public boolean isWhite() {
		return piece.isWhite();
	}

	public boolean isBlack() {
		return piece.isBlack();
	}
}
