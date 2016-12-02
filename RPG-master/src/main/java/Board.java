package main.java;

import java.awt.Graphics;

import javax.swing.JPanel;

public class Board extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Map map;
	ItemMap itemMap;
	Player player;
	Graphics graphics;

	public Board(Game game) {
		this.player = game.player;
		this.map = game.map;
		this.itemMap = game.itemMap;
		repaint();
	}

	public void paint(Graphics graphics) {
		this.graphics = graphics;
		super.paint(graphics);
		int xp = this.player.getX() - 5;
		int yp = this.player.getY() - 5;
		for (int y = 0; y < 11; y++) {
			for (int x = 0; x < 11; x++) {
				// Map
				if (this.map.getMap(yp, xp) != 0) {
					int ImageID = this.map.getMap(yp, xp);
					graphics.drawImage(this.map.getMapImage(ImageID), x * 32,
							y * 32, null);
				}
				// Item
				if (this.itemMap.getItemMap(yp, xp) != 0) {
					int ItemID = this.itemMap.getItemMap(yp, xp);
					graphics.drawImage(this.itemMap.getItem(ItemID), x * 32,
							y * 32, null);
				}
				xp++;
			}
			xp = this.player.getX() - 5;
			yp++;
		}
		graphics.drawImage(this.player.getPlayer(), 5 * 32, 5 * 32, null);
	}
}
