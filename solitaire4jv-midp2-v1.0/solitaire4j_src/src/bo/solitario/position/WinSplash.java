package bo.solitario.position;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import bo.solitario.MenuListener;

public class WinSplash extends Canvas
// implements CommandListener
{

	private int count;
	private Display display;

	private static WinSplash singleton;

	public static WinSplash getSingleton() {
		if (singleton == null) {
			singleton = new WinSplash();
		}
		return singleton;
	}

	public static WinSplash getSingleton(Display display) {
		if (singleton == null) {
			singleton = new WinSplash();
			singleton.display = display;
		}
		return singleton;
	}

	private WinSplash() {
		count = 0;
		this.addCommand(new Command("Continue", Command.OK, 1));

	}

	public void setMenuListener(MenuListener listener) {
		this.setCommandListener(listener);
	}

	public void add() {

		count++;
		//#debug info
		System.out.println("IT WAS ADD " + count);

		if (count == 4) {
			// it is complete, so end game
			display.setCurrent(this);
		}
	}

	public void rest() {
		count--;
	}

	public void reset() {
		count = 0;
	}

	protected void paint(Graphics g) {

		int xsize = super.getWidth();
		int ysize = super.getHeight();

		g.setColor(255, 255, 255);
		g.fillRect(0, 0, xsize, ysize);

		g.setColor(0, 0, 0);
		g.drawString("YOU WIN", xsize / 2, 3, Graphics.TOP | Graphics.HCENTER);
		g.drawString("Press continue", xsize / 2, 17, Graphics.TOP
				| Graphics.HCENTER);

		g.setColor(0, 0, 0);

		g.drawString(".", xsize / 4, 27, Graphics.TOP | Graphics.HCENTER);
		g.drawString("...", xsize / 4, 30, Graphics.TOP | Graphics.HCENTER);
		g.drawString(".....", xsize / 4, 33, Graphics.TOP | Graphics.HCENTER);
		g.drawString(".........", xsize / 4, 36, Graphics.TOP
				| Graphics.HCENTER);
		g.drawString("... ...", xsize / 4, 39, Graphics.TOP | Graphics.HCENTER);
		g.drawString(".", xsize / 4, 42, Graphics.TOP | Graphics.HCENTER);
		g.drawString("...", xsize / 4, 45, Graphics.TOP | Graphics.HCENTER);

		g.setColor(255, 0, 0);

		g.drawString(".", xsize * 3 / 4, 27, Graphics.TOP | Graphics.HCENTER);
		g.drawString("...", xsize * 3 / 4, 30, Graphics.TOP | Graphics.HCENTER);
		g.drawString(".......", xsize * 3 / 4, 33, Graphics.TOP
				| Graphics.HCENTER);
		g.drawString(".........", xsize * 3 / 4, 36, Graphics.TOP
				| Graphics.HCENTER);
		g.drawString(".....", xsize * 3 / 4, 39, Graphics.TOP
				| Graphics.HCENTER);
		g.drawString("...", xsize * 3 / 4, 42, Graphics.TOP | Graphics.HCENTER);
		g.drawString(".", xsize * 3 / 4, 45, Graphics.TOP | Graphics.HCENTER);
		g.setColor(0, 0, 0);
		
		g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN,Font.SIZE_SMALL));
		g.drawString("By Jose H Valdes Murguia", xsize / 2, 53, Graphics.TOP | Graphics.HCENTER);
		g.drawString("Cochabamba - Bolivia", xsize / 2, 65, Graphics.TOP | Graphics.HCENTER);
		
		

	}

}
