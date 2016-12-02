package main.java;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Player {
	int x, y;
	private Image player;

	public Player() {
		this.player = new ImageIcon(this.getClass().getResource(
				"../resources/player.png")).getImage();
		this.x = 9;
		this.y = 9;
	}

	public void move(int x, int y) {
		this.x += x;
		this.y += y;
	}

	public Image getPlayer() {
		return player;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	// public void spellQ() {
	// }

	// public void spellW() {
	// }

	// public void spellE() {
	// }

	// public void spellR() {
	// }

	// public void spellD() {
	// }

	// public void spellF() {
	// }
}
