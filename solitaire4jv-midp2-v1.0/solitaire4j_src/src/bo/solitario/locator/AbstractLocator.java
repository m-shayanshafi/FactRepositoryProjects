package bo.solitario.locator;

import bo.solitario.card.Card;
import bo.solitario.position.CardPosition;

public interface AbstractLocator {

	public boolean allocate(CardPosition position, Card card);

	public boolean remove(CardPosition position, Card card);

	public void restart();
}
