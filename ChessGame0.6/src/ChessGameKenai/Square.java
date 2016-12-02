package ChessGameKenai;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

/**
 * Public Square class which is a template for making Square objects when called
 * upon Some Square objects will have Piece on them some will be empty Square
 * objects will carry either white or black color depending on their position
 * and they will know their position as well in the ChessBoardView class they
 * also listen for click events to register their position at the source and at
 * destination
 * 
 * @author Dimitri Pankov
 * @see JPanel Class
 * @see ChessBoardView Class
 * @version 1.0
 */
public class Square extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int position;
	private Color currentColor;
	private static Chess_Data data;
	private Color previousColor;

	/**
	 * OverLoaded Constructor for creating square objects
	 * 
	 * @param color
	 *            Square object color
	 * @param position
	 *            Square object position on the ChessBoardView's MainPanel
	 * @see ChessBoardView Class
	 */
	public Square(Color color, int position) {

		this.currentColor = color;
		this.position = position;
		this.setBackground(color);
		this.setLayout(null);

		this.addMouseListener(new SendData(data));
	}

	public Square() {
	}

	public Color getColor() {
		return currentColor;
	}

	public void setColor(Color color) {
		this.currentColor = color;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

	public void setPreviousColor(Color previousColor) {
		this.previousColor = previousColor;
	}

	public Color getPreviousColor() {
		return previousColor;
	}

	/**
	 * The SendData Class is a nested inner class of our object It does as its
	 * name suggests sends data to the model after receiving the data and the
	 * address of the model from its outer class it completes the job by sending
	 * the request to the model
	 * 
	 * @author Dimitri Pankov
	 * @see MouseAdapter Class
	 * @see Square Class
	 * @version 1.0
	 */
	protected class SendData extends MouseAdapter {

		/**
		 * Overloaded Constructor of the inner class
		 * 
		 * @param data
		 *            the address of the model for later communication purposes
		 */
		protected SendData(Chess_Data data) {
			Square.data = data;

		}

		@Override
		public void mousePressed(MouseEvent e) {
			int x = Square.this.getPosition();

			System.out.print("mousePressed = " + x + " - ");

			if (Square.data.getPiecePosition() == x
					|| data.getPiecePosition() == 0) {
				return;
			} else if (Square.data.getPiecePosition() > 0) {
				Square.data.move(Square.data.getPiecePosition(), x);
			}
		}
	}
}
