package bo.solitario.position;

import java.util.Vector;

import bo.solitario.Game;
import bo.solitario.card.Card;
import bo.solitario.card.Set;

public class OpenPosition extends CardPosition {

	private Game game;

	private boolean lock;

	public void setLock(boolean lock) {
		this.lock = lock;
	}

	public boolean isLock() {
		return lock;
	}

	public OpenPosition(Game game) {
		this.game = game;
		dimension = game.dimension;
		card = new Vector();
		super.setPositions(2, 1);
		image = Set.witch;
	}

	public boolean putCard(Object card) {

		if (card instanceof Card) {
			this.card.addElement(card);
			return true;
		} else {
			return false;
		}
	}

	public boolean moveInside(int move) {
		return false;
	}

	public Object getCard() {
		if (card.isEmpty()) {
			return null;
		} else {
			return card.lastElement();
		}
	}

	public boolean removeCard(Object card) {
		if (card instanceof Card) {
			return this.card.removeElement(card);
		} else {
			return false;
		}
	}

	protected void protectedRestart() {
		;
	}
}
