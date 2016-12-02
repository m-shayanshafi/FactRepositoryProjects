package bo.solitario;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;

import bo.solitario.card.Set;
import bo.solitario.position.WinSplash;

public class MainMenu extends Canvas {

	private MIDlet midlet;
	private Game game;
	Display display;

	public MainMenu(Main midlet, Game game) {
		this.midlet = midlet;
		this.game = game;
		display = Display.getDisplay(midlet);
	}

	protected void paint(Graphics g) {

		g.setColor(255, 255, 255);
		g.fillRect(0, 0, getWidth(), getHeight());

		int xsize = super.getWidth();
		int ysize = super.getHeight();

		g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN,
				Font.SIZE_SMALL));

		g.drawImage(Set.mainback, 0, 0, Graphics.TOP | Graphics.LEFT);

		g.setColor(0, 0, 0);

		g.drawString("Play", xsize / 4, ysize / 4, Graphics.HCENTER
				| Graphics.BOTTOM);
		if (x == 0 && y == 0) {
			g.drawString("-><-", xsize / 4, ysize / 4, Graphics.HCENTER
					| Graphics.TOP);
		}

		g.drawString("Options", xsize * 3 / 4, ysize / 4, Graphics.HCENTER
				| Graphics.BOTTOM);
		if (x == 1 && y == 0) {
			g.drawString("-><-", xsize * 3 / 4, ysize / 4, Graphics.HCENTER
					| Graphics.TOP);
		}

		g.drawString("Exit", xsize / 4, ysize / 2, Graphics.BOTTOM
				| Graphics.HCENTER);
		if (x == 0 && y == 1) {
			g.drawString("-><-", xsize / 4, ysize / 2, Graphics.HCENTER
					| Graphics.TOP);
		}

		g.drawString("About", xsize * 3 / 4, ysize / 2, Graphics.BOTTOM
				| Graphics.HCENTER);
		if (x == 1 && y == 1) {
			g.drawString("-><-", xsize * 3 / 4, ysize / 2, Graphics.HCENTER
					| Graphics.TOP);
		}

		g.drawString(" MENU ", xsize / 2, ysize * 3 / 5, Graphics.HCENTER
				| Graphics.TOP);

		g.fillArc(xsize / 2 - 23, ysize * 3 / 4, 15, 15, 0, 360);
		g.fillArc(xsize / 2 - 8, ysize * 3 / 4, 15, 15, 0, 360);
		g.fillArc(xsize / 2 + 7, ysize * 3 / 4, 15, 15, 0, 360);

		g.setColor(255, 255, 255);
		g.fillArc(xsize / 2 - 21, ysize * 3 / 4 + 2, 5, 5, 0, 360);
		g.fillArc(xsize / 2 - 6, ysize * 3 / 4 + 2, 5, 5, 0, 360);
		g.fillArc(xsize / 2 + 9, ysize * 3 / 4 + 2, 5, 5, 0, 360);
		g.setColor(0, 0, 0);

	}

	private int x = 0;
	private int y = 0;

	public void keyPressed(int codigo) {

		int cod = getGameAction(codigo);

		switch (cod) {

		case Canvas.DOWN: {
			y = y == 1 ? y : y + 1;
		}
			break;
		case Canvas.UP: {
			y = y == 0 ? y : y - 1;
		}
			break;
		case Canvas.RIGHT: {
			x = x == 1 ? x : x + 1;
		}
			break;

		case Canvas.LEFT: {
			x = x == 0 ? x : x - 1;
		}
			break;
		case Canvas.FIRE: {
			select();
		}
			break;
		}

		// System.out.println("******* "+x+", "+y);

		repaint();
	}

	private void select() {
		//#debug info
		System.out.println(" * FIRE ");

		if (x == 0) {
			if (y == 0) {
				game.startGame();
				display.setCurrent(game);
			} else {
				midlet.notifyDestroyed();
			}
		} else {
			if (y == 0) {
				preference();
			} else {
				about();
			}
		}
	}

	private void about() {
		display.setCurrent(WinSplash.getSingleton());
	}

	private void preference() {
		display.setCurrent(FormPreference
				.getFormPreference((MenuListener) midlet));
	}

}
