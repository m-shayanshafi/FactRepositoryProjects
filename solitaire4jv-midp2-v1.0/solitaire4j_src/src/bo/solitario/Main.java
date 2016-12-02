package bo.solitario;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import bo.solitario.position.WinSplash;

public class Main extends MIDlet implements Runnable, MenuListener {

	private Game game;
	public Display display;
	private MainMenu mainmenu;

	protected void startApp() throws MIDletStateChangeException {

		display = Display.getDisplay(this);

		/*
		 * Form form=new Form("FORM"); form.append("Welcome to solitaries
		 * Game");
		 */

		Splash splash = new Splash();
		display.setCurrent(splash);

		Thread main = new Thread(this);
		main.start();
	}

	protected void pauseApp() {

	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		this.notifyDestroyed();
	}

	public void run() {
		long time1 = System.currentTimeMillis();

		WinSplash.getSingleton(display).setMenuListener(this);

		Menu menu = new Menu(display, this);
		game = new Game(menu);

		// #debug info
		System.out.println("Init Thread");
		mainmenu = new MainMenu(this, game);

		long time2 = System.currentTimeMillis();

		if ((time2 - time1) < 3000) {
			try {
				Thread.sleep(3000 - ((time2 - time1)));
			} catch (Exception e) {
			}
		}

		// game.startGame();
		display.setCurrent(mainmenu);

	}

	/*
	 * public void showMenu(){ display.setCurrent(mainmenu); }
	 */

	public void commandAction(Command arg0, Displayable arg1) {
		display.setCurrent(mainmenu);
	}

}
