package bo.solitario.position;

import javax.microedition.lcdui.Graphics;

import bo.solitario.card.Card;

public interface Position {

	public boolean isCloseCard();

	public void openCard();

	public boolean putCard(Object card);

	public boolean removeCard(Object card);

	public Object getCard();

	public void draw(Graphics g, int x, int y, boolean move, boolean enhance);

	public boolean isPosition(int x, int y);

	public Card removeCard();

	public boolean isEmpty();

}
