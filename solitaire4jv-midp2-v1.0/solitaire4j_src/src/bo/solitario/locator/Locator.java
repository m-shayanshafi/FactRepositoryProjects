package bo.solitario.locator;

import bo.solitario.card.Card;
import bo.solitario.card.Set;
import bo.solitario.position.CardPosition;
import bo.solitario.position.WinSplash;

public class Locator implements AbstractLocator {

	private Set set;

	public Locator() {
		set = new Set();
	}

	public void restart() {
		set.reset();
	}

	public boolean remove(CardPosition position, Card card) {

		boolean answer = false;

		if (!position.getVectorCard().isEmpty()) {

			if (set.isFull()) {
				WinSplash.getSingleton().rest();
			}
			set.backCard();
			position.card.removeElement(card);
			answer = true;
		}
		return answer;
	}

	public boolean allocate(CardPosition position, Card card) {

		boolean answer = false;

		//#debug info
		System.out.println(" * PALO " + position.getName() + " CARD "
				+ card.getName());

		if (position.getVectorCard().isEmpty()) {

			if (card.getName().equals(Set.card_A)) {
				position.setName(card.getPalo());
				answer = true;
			} else {
				answer = false;
			}

		} else {
			answer = (!set.isFull()) && set.getNext().equals(card.getName())
					&& card.getPalo().equals(position.getName());
		}

		if (answer) {

			if (set.jumpNext()) {
				position.card.addElement(card);
				if (set.isFull()) {
					WinSplash.getSingleton().add();
				}
			} else {
				//#debug info
				System.out.println(" test  3");
			}
		}
		return answer;
	}

}
