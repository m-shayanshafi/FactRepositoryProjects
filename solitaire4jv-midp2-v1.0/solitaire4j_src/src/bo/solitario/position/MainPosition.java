package bo.solitario.position;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;

import bo.solitario.Game;
import bo.solitario.Preference;
import bo.solitario.card.Card;
import bo.solitario.card.CardDrawer;

public class MainPosition extends CardPosition implements RandomPosition {

	private Game game;
	private CardPosition move_to;

	public MainPosition(Game game, CardPosition move_to) {

		this.game = game;
		super.dimension = game.dimension;
		card = new Vector();
		super.setPositions(1, 1);
		this.move_to = move_to;
	}

	public boolean isCloseCard() {
		return true;
	}

	public void openCard() {

		if (!card.isEmpty()) {
			Card next = (Card) card.elementAt(0);
			card.removeElementAt(0);
			move_to.putCard(next);
			next.setOpen(true);
		} else {

			boolean key = Preference.continueRound();
			// debug info
			// System.out.println("MAIN IS EMPTY "+key);
			if (key) {
				while (!move_to.isEmpty()) {
					Card card = move_to.removeCard();
					putCard(card);
				}
			}
		}
	}

	public void draw(Graphics g, int x, int y, boolean selected, boolean enhance) {

		if (x == posX && y == posY) {
			g.setColor(225, 220, 24);
			g.fillRect(dimension.getPositionX(posX), dimension
					.getPositionY(posY), dimension.widthCard + 2,
					dimension.heightCard + 2);
		}

		g.setColor(225, 225, 225);
		g.fillRect(dimension.getPositionX(posX), dimension.getPositionY(posY),
				dimension.widthCard, dimension.heightCard);
		g.setColor(0, 0, 0);
		g.drawRect(dimension.getPositionX(posX), dimension.getPositionY(posY),
				dimension.widthCard, dimension.heightCard);

		if (card.isEmpty()) {
			g.drawArc(dimension.getPositionX(posX), dimension
					.getPositionY(posY), dimension.widthCard,
					dimension.heightCard, 0, 360);
		} else {
			CardDrawer.draw(g, dimension.getPositionX(posX), dimension
					.getPositionY(posY), dimension.widthCard,
					dimension.heightCard);
		}

	}

	public boolean putCard(Object card) {

		if (card instanceof Card) {
			this.card.addElement(card);
			return true;
		} else {
			return false;
		}

	}

	public Object getCard() {

		if (card.isEmpty()) {
			return null;
		} else {
			Card next = (Card) card.elementAt(0);
			card.removeElementAt(0);
			return next;
		}
	}

	public boolean removeCard(Object card) {

		if (card instanceof Card) {
			return this.card.removeElement(card);
		}
		return false;

	}

	public void addCloseCard(Card card) {
		// card.setOpen(false);
		this.card.addElement(card);
	}

	public boolean moveInside(int move) {
		return false;
	}

	protected void protectedRestart() {
		;
	}

}
