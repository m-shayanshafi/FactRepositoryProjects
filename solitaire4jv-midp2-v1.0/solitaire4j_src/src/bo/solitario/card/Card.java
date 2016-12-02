package bo.solitario.card;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Card {

	private String name;
	private Image image;

	private String palo;
	private boolean isOpen;

	public Card(Image image, String name, String palo) {
		this.image = image;
		this.name = name;
		this.palo = palo;
		isOpen = false;

		if (palo.equals(Set.HEART_CARD) || palo.equals(Set.DIAMOND_CARD)) {
			setColor(255, 0, 0);
		} else {
			setColor(0, 0, 0);
		}

	}

	private int red;
	private int green;
	private int blue;

	public void setColor(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public void drawColor(Graphics g) {
		g.setColor(red, green, blue);
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPalo() {
		return palo;
	}

	public void setPalo(String palo) {
		this.palo = palo;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

}
