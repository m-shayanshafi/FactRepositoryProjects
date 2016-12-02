package bo.solitario.position;

import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics; //import javax.microedition.lcdui.Image;

import bo.solitario.Game;
import bo.solitario.card.Card;
import bo.solitario.card.CardDrawer;
import bo.solitario.card.Set;
import bo.solitario.locator.GameLocator;

public class GamePosition extends CardPosition implements RandomPosition {

	public GamePosition(Game game, int posx, int posy) {
		super();
		dimension = game.dimension;
		super.setPositions(posx, posy);

		// Vector with all cards

		card = new Vector();
		locator = new GameLocator();
		this.image = null;
	}

	public void addCloseCard(Card card) {
		// card.setOpen(false);
		this.card.addElement(card);
		reset();
	}

	public void draw(Graphics g, int x, int y, boolean move, boolean enhance) {

		g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN,
				Font.SIZE_SMALL));

		if (x == posX && y == posY) {
			// yellow
			g.setColor(225, 220, 24);
			g.fillRect(dimension.getPositionX(posX), dimension
					.getPositionY(posY), dimension.widthCard + 4,
					dimension.heightCard + 5);
		}

		// black
		g.setColor(225, 225, 225);
		g.fillRect(dimension.getPositionX(posX), dimension.getPositionY(posY),
				dimension.widthCard, dimension.heightCard);

		g.setColor(0, 0, 0);
		g.drawRect(dimension.getPositionX(posX), dimension.getPositionY(posY),
				dimension.widthCard, dimension.heightCard);

		int first = getfirstOpen();

		if (first == -1) {
			if (card.size() != 0) {
				CardDrawer.draw(g, dimension.getPositionX(posX), dimension
						.getPositionY(posY), dimension.widthCard,
						dimension.heightCard);
			}
			/*
			 * else{ g.setColor(255,255,255); g.drawRect(
			 * dimension.getPositionX(posX), dimension.getPositionY(posY),
			 * dimension.widthCard, dimension.heightCard ); }
			 */
		} else {

			boolean dist = (move && x == posX && y == posY)
					|| (move && enhance);

			// System.out.println("X:"+posX+", Y"+posY+", dist:"+dist+",
			// move:"+" poss "+(x==posX && y==posY)+", enhance "+enhance);

			for (int i = first - 1, j = 0; i < card.size(); i++) {

				Card next = (Card) card.elementAt(i);

				int inc = 0;
				if (dist && i == card.size() - 1) {
					inc = 2;
				}

				if (enhance && i >= current - 1) {
					inc = 2;
				}
				g.setColor(0, 0, 0);

				g.drawRect(dimension.getPositionX(posX) + inc, dimension
						.getPositionY(posY)
						+ j + inc, dimension.widthCard, dimension.heightCard);

				if (dist && i == card.size() - 1) {
					// choose the color
					g.setColor(225, 220, 24);

				} else {
					if (i == current - 1) {
						g.setColor(210, 210, 210);// dark gray
					} else {
						g.setColor(243, 243, 243);// light gray
					}
				}

				g.fillRect(dimension.getPositionX(posX) + inc, dimension
						.getPositionY(posY)
						+ j + inc, dimension.widthCard, dimension.heightCard);

				g.setColor(0, 0, 0);

				g.drawRect(dimension.getPositionX(posX) + inc, dimension
						.getPositionY(posY)
						+ j + inc, dimension.widthCard, dimension.heightCard);

				next.drawColor(g);

				if (next.getName().equals(Set.card_10)) {
					g.drawString(next.getName(), dimension.getPositionX(posX)
							+ inc, dimension.getPositionY(posY) + 2 + j + inc,
							Graphics.TOP | Graphics.LEFT);
				} else {
					g.drawString(next.getName(), dimension.getPositionX(posX)
							+ 1 + inc, dimension.getPositionY(posY) + 2 + j
							+ inc, Graphics.TOP | Graphics.LEFT);
				}

				/**
				 * The last contains the image
				 */
				if (i == card.size() - 1) {
					g.drawImage(next.getImage(), dimension.getPositionX(posX)
							+ 1 + inc, dimension.getPositionY(posY)
							+ (dimension.heightCard / 2) + j + 3 + inc,
							Graphics.TOP | Graphics.LEFT);
				}

				j += (dimension.heightCard / 3);
			}

		}
	}

	public boolean isCloseCard() {

		if (card.isEmpty()) {
			return false;
		} else {
			Card last = (Card) card.lastElement();
			return !last.isOpen();
		}

	}

	/**
	 * Card Vector
	 */
	public Object getCard() {

		if (card.isEmpty()) {
			return null;
		} else {

			if (current == card.size()) {
				return card.lastElement();
			} else {
				Vector vector = new Vector();
				for (int i = current - 1; i < card.size(); i++) {
					vector.addElement(card.elementAt(i));
				}
				return vector;
			}

		}
	}

	public boolean removeCard(Object card) {
		boolean res = false;
		if (card instanceof Card) {
			res = this.card.removeElement(card);
		} else {
			Vector vector = (Vector) card;
			for (int i = 0; i < vector.size(); i++) {
				res = this.card.removeElement(vector.elementAt(i));
			}
		}
		reset();
		return res;
	}

	public boolean putCard(Object card) {
		boolean res = false;
		if (card instanceof Card) {
			res = locator.allocate(this, (Card) card);
		} else {

			Vector vector = (Vector) card;
			for (int i = 0; i < vector.size(); i++) {
				res = locator.allocate(this, (Card) vector.elementAt(i));
			}
		}
		reset();
		return res;
	}

	public void openCard() {
		Card last = (Card) card.lastElement();
		last.setOpen(true);
		reset();
	}

	public void reset() {
		current = card.size();
	}

	private int getfirstOpen() {
		int i = 0;
		boolean key = true;
		// int init=0;
		while (i < card.size() && key) {
			Card current = (Card) card.elementAt(i);
			key = !current.isOpen();
			i++;
		}

		if (key == true) {
			i = -1;
		}

		return i;
	}

	public boolean moveInside(int move) {

		boolean response = true;

		int i = getfirstOpen();

		switch (move) {

		case Canvas.UP: {
			if (current == i || i == -1) {
				response = false;
			} else {
				current--;
				response = true;
			}
		}
			break;

		case Canvas.DOWN: {
			if (current == card.size()) {
				response = false;
			} else {
				current++;
				response = true;
			}
		}
			break;
		}

		return response;
	}

	protected void protectedRestart() {
		locator.restart();
	}
}