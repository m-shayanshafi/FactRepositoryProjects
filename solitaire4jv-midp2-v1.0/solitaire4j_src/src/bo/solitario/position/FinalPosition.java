package bo.solitario.position;

import java.util.Vector;

import javax.microedition.lcdui.Image;

import bo.solitario.Game;
import bo.solitario.card.Card;
import bo.solitario.locator.Locator;

public class FinalPosition extends CardPosition {

	public FinalPosition(Game game, int posx, int posy, Image image, String name) {
		dimension = game.dimension;
		setPositions(posx, posy);
		card = new Vector();
		this.name = name;
		this.image = image;
		locator = new Locator();

	}

	public FinalPosition(Game game, int posx, int posy) {
		dimension = game.dimension;
		setPositions(posx, posy);
		card = new Vector();
		locator = new Locator();
	}

	public boolean moveInside(int move) {
		return false;
	}

	public Object getCard() {
		if (card.isEmpty()) {
			return null;
		} else {
			return (Card) card.lastElement();
		}
	}

	public boolean putCard(Object card) {

		if (card instanceof Card) {
			boolean res = locator.allocate(this, (Card) card);
			reset();
			return res;
		} else {
			return false;
		}

	}

	public boolean removeCard(Object card) {

		if (card instanceof Card) {
			// boolean res= this.card.removeElement(card);
			boolean res = locator.remove(this, (Card) card);
			reset();
			return res;
		} else {
			return false;
		}

	}

	protected void protectedRestart() {
		locator.restart();
	}

}
