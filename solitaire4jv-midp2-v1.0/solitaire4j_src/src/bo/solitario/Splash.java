package bo.solitario;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

import de.enough.polish.util.Locale;

public class Splash extends Canvas {

	protected void paint(Graphics g) {

		int xsize = super.getWidth();
		int ysize = super.getHeight();

		g.setColor(255, 255, 255);
		g.fillRect(0, 0, xsize, ysize);
		g.setColor(0, 0, 0);

		/*
		 * g.drawString(".",xsize/2,1,Graphics.TOP|Graphics.HCENTER);
		 * g.drawString("... ... ...",xsize/2,5,Graphics.TOP|Graphics.HCENTER);
		 * g.drawString(".............",xsize/2,9,Graphics.TOP|Graphics.HCENTER);
		 * g.drawString("... ... ...",xsize/2,13,Graphics.TOP|Graphics.HCENTER);
		 * g.drawString("...",xsize/2,17,Graphics.TOP|Graphics.HCENTER);
		 * g.drawString(".....",xsize/2,21,Graphics.TOP|Graphics.HCENTER);
		 */

		/*
		 * g.drawString(Locale.get("welcome.title"),
		 * xsize/2,2,Graphics.TOP|Graphics.HCENTER);
		 * g.drawString(Locale.get("welcome.name"),
		 * xsize/2,14,Graphics.TOP|Graphics.HCENTER);
		 * g.drawString(Locale.get("welcome.place"),xsize/2,26,Graphics.TOP|Graphics.HCENTER);
		 */

		g.drawString(Locale.get("welcome.title"), xsize / 2, 2, Graphics.TOP
				| Graphics.HCENTER);
		g.drawString(Locale.get("welcome.name"), xsize / 2, 14, Graphics.TOP
				| Graphics.HCENTER);
		g.drawString(Locale.get("welcome.place"), xsize / 2, 26, Graphics.TOP
				| Graphics.HCENTER);

		g.fillArc(xsize / 2 - 23, 42, 15, 15, 0, 360);
		g.fillArc(xsize / 2 - 8, 42, 15, 15, 0, 360);
		g.fillArc(xsize / 2 + 7, 42, 15, 15, 0, 360);

		g.setColor(255, 255, 255);
		g.fillArc(xsize / 2 - 21, 44, 5, 5, 0, 360);
		g.fillArc(xsize / 2 - 6, 44, 5, 5, 0, 360);
		g.fillArc(xsize / 2 + 9, 44, 5, 5, 0, 360);

		/*
		 * g.setColor(255,0,0); g.drawString("..
		 * ..",xsize/4,47,Graphics.TOP|Graphics.HCENTER); g.drawString("....
		 * ....",xsize/4,50,Graphics.TOP|Graphics.HCENTER);
		 * g.drawString(".......",xsize/4,53,Graphics.TOP|Graphics.HCENTER);
		 * g.drawString("...",xsize/4,56,Graphics.TOP|Graphics.HCENTER);
		 * g.drawString(".",xsize/4,59,Graphics.TOP|Graphics.HCENTER);
		 */

		g.setColor(0, 0, 0);

		g.drawString(".", xsize / 4, 47, Graphics.TOP | Graphics.HCENTER);
		g.drawString("...", xsize / 4, 50, Graphics.TOP | Graphics.HCENTER);
		g.drawString(".....", xsize / 4, 53, Graphics.TOP | Graphics.HCENTER);
		g.drawString(".........", xsize / 4, 56, Graphics.TOP
				| Graphics.HCENTER);
		g.drawString("... ...", xsize / 4, 59, Graphics.TOP | Graphics.HCENTER);
		g.drawString(".", xsize / 4, 62, Graphics.TOP | Graphics.HCENTER);
		g.drawString("...", xsize / 4, 65, Graphics.TOP | Graphics.HCENTER);

		g.setColor(255, 0, 0);

		g.drawString(".", xsize * 3 / 4, 47, Graphics.TOP | Graphics.HCENTER);
		g.drawString("...", xsize * 3 / 4, 50, Graphics.TOP | Graphics.HCENTER);
		g.drawString(".......", xsize * 3 / 4, 53, Graphics.TOP
				| Graphics.HCENTER);
		g.drawString(".........", xsize * 3 / 4, 56, Graphics.TOP
				| Graphics.HCENTER);
		g.drawString(".....", xsize * 3 / 4, 59, Graphics.TOP
				| Graphics.HCENTER);
		g.drawString("...", xsize * 3 / 4, 62, Graphics.TOP | Graphics.HCENTER);
		g.drawString(".", xsize * 3 / 4, 65, Graphics.TOP | Graphics.HCENTER);

		g.setColor(0, 0, 0);
	}

}
