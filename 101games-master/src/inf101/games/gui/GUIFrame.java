package inf101.games.gui;

import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JFrame;

public class GUIFrame extends JFrame {
	private static final long serialVersionUID = 7182884604500889054L;

	public GUIFrame(GameGUI gui) {
		setTitle("101 Games");
		setLayout(new FlowLayout());
		add(gui);
		gui.initialize();
		gui.setSize(this.getSize());
		Container pane = getContentPane();
		pane.setBackground(Style.BACKGROUND);
		pane.setForeground(Style.FOREGROUND);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
