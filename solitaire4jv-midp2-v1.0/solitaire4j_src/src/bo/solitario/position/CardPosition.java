package bo.solitario.position;

/**
 * This class represents cards which are going to have all cards in order.
 */

import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import bo.solitario.card.Card;
import bo.solitario.card.CardDimension;
import bo.solitario.card.CardDrawer;
import bo.solitario.card.Set;
import bo.solitario.locator.AbstractLocator;

public abstract class CardPosition implements Position {

	// public abstract void draw(Graphics g,int x, int y, boolean selected);

	// protected Set set;

	// Posicion de las cartas 1-7
	public int posX;
	public int posY;

	protected CardDimension dimension;

	protected String name;

	protected Image image;

	// Vector Card
	public Vector card;

	public Vector getVectorCard() {
		return card;
	}

	protected AbstractLocator locator;

	protected CardPosition() {
		;
	}

	public void draw(Graphics g, int x, int y, boolean move, boolean enhance) {
		g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN,
				Font.SIZE_SMALL));

		if (x == posX && y == posY) {
			g.setColor(225, 220, 24);
			g.fillRect(dimension.getPositionX(posX), dimension
					.getPositionY(posY), dimension.widthCard + 4,
					dimension.heightCard + 4);
		}

		g.setColor(225, 225, 225);
		g.fillRect(dimension.getPositionX(posX), dimension.getPositionY(posY),
				dimension.widthCard, dimension.heightCard);

		// Rest
		g.setColor(0, 0, 0);
		g.drawRect(dimension.getPositionX(posX), dimension.getPositionY(posY),
				dimension.widthCard, dimension.heightCard);

		if (!card.isEmpty()) {

			if (move && x == posX && y == posY) {

				if (card.size() - 2 >= 0) {
					Card antTop = (Card) card.elementAt(card.size() - 2);

					g.drawString(antTop.getName(),
							dimension.getPositionX(posX) + 2, dimension
									.getPositionY(posY) + 2, Graphics.TOP
									| Graphics.LEFT);

					g.drawImage(antTop.getImage(),
							dimension.getPositionX(posX) + 2, dimension
									.getPositionY(posY)
									+ (dimension.heightCard / 2) + 2,
							Graphics.TOP | Graphics.LEFT);
				}

				g.setColor(225, 225, 0);
				g.fillRect(dimension.getPositionX(posX) + 2, dimension
						.getPositionY(posY) + 2, dimension.widthCard,
						dimension.heightCard);

				g.setColor(0, 0, 0);
				// g.setColor(255, 255, 255);
				g.drawRect(dimension.getPositionX(posX) + 2, dimension
						.getPositionY(posY) + 2, dimension.widthCard,
						dimension.heightCard);

				Card top = (Card) card.lastElement();

				top.drawColor(g);

				g.drawString(top.getName(), dimension.getPositionX(posX) + 4,
						dimension.getPositionY(posY) + 4, Graphics.TOP
								| Graphics.LEFT);

				g.drawImage(top.getImage(), dimension.getPositionX(posX) + 4,
						dimension.getPositionY(posY)
								+ (dimension.heightCard / 2) + 4, Graphics.TOP
								| Graphics.LEFT);

				g.setColor(0, 0, 0);

			} else {

				Card top = (Card) card.lastElement();

				top.drawColor(g);

				int back = 0;
				if (top.getName().equals(Set.card_10)) {
					back = 2;
				}

				g.drawString(top.getName(), dimension.getPositionX(posX) + 2
						- back, dimension.getPositionY(posY) + 2, Graphics.TOP
						| Graphics.LEFT);

				g.drawImage(top.getImage(), dimension.getPositionX(posX) + 2,
						dimension.getPositionY(posY)
								+ (dimension.heightCard / 2) + 2, Graphics.TOP
								| Graphics.LEFT);
			}
		}
	}

	public abstract boolean putCard(Object card);

	public abstract Object getCard();

	public abstract boolean removeCard(Object card);

	public boolean isPosition(int x, int y) {
		return (x == posX && y == posY);
	}

	public Card removeCard() {
		Card response = (Card) card.firstElement();
		card.removeElementAt(0);
		reset();
		return response;
	}

	public boolean isCloseCard() {
		return false;
	}

	public void openCard() {
		;
	}

	public void setCard(Vector card) {
		this.card = card;
	}

	public int getPositionX(int num) {
		return (4 * num - 3);
	}

	public int getPositionY(int num) {
		return (5 * (num - 1) + 1);
	}

	public void setPositions(int x, int y) {
		posX = x;
		posY = y;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public boolean equals(CardPosition card) {
		return this.posX == card.posX && this.posY == card.posY;
	}

	public boolean isEmpty() {
		return card.isEmpty();
	}

	public int current = 0;

	public void reset() {
		current = card.size() - 1;
	}

	public abstract boolean moveInside(int move);

	public void restart() {
		card.removeAllElements();
		current = 0;
		protectedRestart();
	}

	protected abstract void protectedRestart();

}
