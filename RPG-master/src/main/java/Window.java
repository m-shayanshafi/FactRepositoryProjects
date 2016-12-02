package main.java;

import javax.swing.JFrame;

public class Window extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Window() {
		setTitle("Game");
		setBounds(100, 100, 352, 352);
		setUndecorated(true);

		setFocusTraversalKeysEnabled(false);
		setFocusable(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}