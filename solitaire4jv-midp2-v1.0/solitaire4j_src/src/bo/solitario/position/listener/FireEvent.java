package bo.solitario.position.listener;

import bo.solitario.card.Card;
import bo.solitario.position.CardPosition;
import bo.solitario.position.Position;

public class FireEvent {

	public static int MOVE_CARD;
	public static int PUT_CARD;

	private CardPosition position;
	private int status;

	// private Card selectedCard;
	public CardPosition getPosition() {
		return position;
	}

	public void setPosition(CardPosition position) {
		this.position = position;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
