package ChessGameKenai;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * The CapturedPieces class is the panel that our view uses to show the captured
 * pieces on. ChessBoardView class has two such panels one is for displaying
 * white captured pieces and the other one is for black captured pieces This
 * class is also an observer so each time any change happens to the Data which
 * is CHess_Data class it is notified by executing its update method in the
 * update method capturedPieces object asks the data class if any piece was
 * captured if yes it takes it nd adds itself
 * 
 * @author Dimitri Pankov
 * @see Observer
 * @version 1.5
 */
public class CapturedPieces extends JPanel implements Observer {

	private Color color;
	private Board board;
	private static final int FONT_SIZE = 17;
	private static final int X_POSITION = 188;
	private static final int Y_POSITION = 750;

	/**
	 * Overloaded constructor of the class when the object of this type is
	 * created it will contain this information such as Color and reference to
	 * the Board Board holds all the pieces in the array list so each time the
	 * piece is added to the captured piece panel it has to be removed from the
	 * board
	 * 
	 * @param color
	 *            as a Color
	 * @param board
	 *            as a Board
	 */
	public CapturedPieces(Color color, Board board) {
		setParameters(color, board);

		createBorder();
	}

	private void createBorder() {
		TitledBorder border2 = new TitledBorder(this.getTitle());
		border2.setTitleFont(new Font("Times Roman", Font.PLAIN, FONT_SIZE));
		border2.setTitleColor(Color.WHITE);
		this.setBorder(border2);
		this.setOpaque(false);
	}

	private void setParameters(Color color, Board board) {
		this.color = color;
		this.board = board;
		this.setLayout(new FlowLayout());
		this.setPreferredSize(new Dimension(X_POSITION, Y_POSITION));
		this.setOpaque(false);
	}

	/**
	 * This is how our object represents itself literally as a String
	 * 
	 * @return s as a String
	 */
	@Override
	public String toString() {
		return "CapturedPieces{" + "color=" + color + '}';
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * The method getTitle simply returns the title to the caller depending on
	 * its color black or white
	 * 
	 * @return title as a String
	 */
	private String getTitle() {
		String title = "";
		if (color == Color.WHITE) {
			title = "White Captured Pieces";
		} else {
			title = "Black Captured Pieces";
		}
		return title;
	}

	/**
	 * The method painComponent of whiteCapturedPiecesPanel is used here to
	 * paint the JPanel as we want
	 * 
	 * @param graphics
	 *            Graphics object used to paint this object
	 */
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		final int width = this.getWidth();
		final int height = this.getHeight();

		URL url = this.getClass().getResource("Icons/background.jpg");
		Toolkit toolkit = this.getToolkit();
		Image image = toolkit.getImage(url);
		graphics.drawImage(image, 0, 0, width, height, this);
	}

	/**
	 * The method update is inherited from the Observer Interface when this
	 * method is called it checks if there any captured pieces if it finds any
	 * it adds them to the captured pieces panel depending on the color of the
	 * pieces and itself.
	 * 
	 * @param observable
	 *            as an Observable object
	 * @param object
	 *            as an Object any object passed in
	 */
	public void update(Observable observable, Object object) {
		Chess_Data data = (Chess_Data) observable;
		VisualPiece visualPiece = null;
		if (!data.getCapturedPieces().isEmpty()) {
			NonVisualPiece nonVisualPiece = (NonVisualPiece) data
					.getCapturedPieces().get(
							data.getCapturedPieces().size() - 1);
			if (board.getSquares().get(nonVisualPiece.getPosition() - 1)
					.getComponentCount() > 0) {
				visualPiece = (VisualPiece) board.getSquares()
						.get(nonVisualPiece.getPosition() - 1).getComponent(0);
				if (isWhite(visualPiece, nonVisualPiece)) {
					visualPiece.setPreferredSize(new Dimension(64, 64));
					this.add(visualPiece);
				} else if (isBlack(visualPiece, nonVisualPiece)) {
					visualPiece.setPreferredSize(new Dimension(64, 64));
					this.add(visualPiece);
				}
			}
		}
		this.revalidate();
		this.repaint();
	}

	private boolean isBlack(VisualPiece visualPiece,
			NonVisualPiece nonVisualPiece) {
		return this.getColor() == Color.BLACK
				&& nonVisualPiece.getColor() == Color.BLACK
				&& nonVisualPiece.isCaptured()
				&& visualPiece.getColor() == Color.BLACK;
	}

	private boolean isWhite(VisualPiece visualPiece,
			NonVisualPiece nonVisualPiece) {
		return this.getColor() == Color.WHITE
				&& nonVisualPiece.getColor() == Color.WHITE
				&& nonVisualPiece.isCaptured()
				&& visualPiece.getColor() == Color.WHITE;
	}
}
