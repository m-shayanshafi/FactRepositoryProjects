package com.mgstudios.rpg.game;

import java.awt.Container;

import javax.swing.JFrame;

public class GameRun extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private Game game;
	private JFrame frame;
	private Container contentPane;
	
	public GameRun() {
		game = new Game();
		frame = new JFrame();
		contentPane = getContentPane();
		
		frame.setUndecorated(false);
		frame.setSize((game.width * Game.scale), (game.height * Game.scale));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		
		contentPane.add(game);
		frame.add(contentPane);
		
		game.start(frame);
	}
}
