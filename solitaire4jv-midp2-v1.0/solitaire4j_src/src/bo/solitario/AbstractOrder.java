package bo.solitario;

import bo.solitario.position.RandomPosition;

public abstract class AbstractOrder {

	public abstract void setCard(RandomPosition position, int number);

	public abstract void restart();
}
