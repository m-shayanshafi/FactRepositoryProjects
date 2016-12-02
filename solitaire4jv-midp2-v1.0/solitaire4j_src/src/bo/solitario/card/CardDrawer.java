package bo.solitario.card;

import javax.microedition.lcdui.Graphics;

public class CardDrawer {

	public static void draw(Graphics g, int posx, int posy, int width,
			int height) {

		g.drawRect(posx, posy, width, height);

		// g.setColor(171,171,171);
		// g.fillRect(posx,posy,width,height);

		g.setColor(0, 0, 0);

		for (int i = 0; i < height; i += 4) {
			g.drawLine(posx, posy + i, posx + width, posy + i);
		}

	}

}
