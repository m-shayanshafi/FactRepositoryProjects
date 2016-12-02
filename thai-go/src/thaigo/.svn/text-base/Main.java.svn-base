package thaigo;

import thaigo.utility.PropertyManager;
import thaigo.view.GameUI;
import thaigo.view.Intro;

/**
 * Runs the game.
 * 
 * @author TG_Dream_Team
 * @version 9/5/2013
 *
 */
public class Main {
	
	/** Delay of showing introduction page. */
	private final static int INTRO_DELAY = 1500;

	/**
	 * Creates new game introduction page and new GameUI.
	 * @param args array of string that user input
	 */
	public static void main(String[] args) {
		Intro intro = new Intro();
		intro.run();
		
		try {
			Thread.sleep(INTRO_DELAY);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		intro.close();
		
		PropertyManager.setProperty("mode", "server");
		PropertyManager.setProperty("thaigo.pawn.currentmodel", "Classic");
		PropertyManager.setProperty("thaigo.board.currentcolor", "Wood");
		PropertyManager.setProperty("table", "8");
		
		PropertyManager.setProperty("mode", "client");
		PropertyManager.setProperty("thaigo.pawn.currentmodel", "Classic");
		PropertyManager.setProperty("thaigo.board.currentcolor", "Wood");
		PropertyManager.setProperty("table", "8");
		GameUI ui = GameUI.getInstance();
		ui.run();
		
	}
	
}
