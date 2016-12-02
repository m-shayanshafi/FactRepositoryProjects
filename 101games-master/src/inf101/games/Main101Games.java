package inf101.games;

import inf101.games.gui.GameGUI;
import inf101.games.gui.GUIFrame;
import inf101.games.life.Life;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;

public class Main101Games extends JApplet {
	private static final long serialVersionUID = -5830018712727696869L;

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		new GUIFrame(startGame());
	}

	public static GameGUI startGame() {
		IGame life = new Life(15, 17);
		return new GameGUI(Arrays.asList(life)); 
	}
	
	@Override
	public void init() {
		final JApplet applet = this;
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					GameGUI game = startGame();
					applet.add(game);
					game.initialize();
				}
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
