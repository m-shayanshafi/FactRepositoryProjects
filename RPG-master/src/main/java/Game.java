package main.java;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Game implements ActionListener {
	Player player = new Player();
	Inventory inventory = new Inventory();
	Map map = new Map();
	ItemMap itemMap = new ItemMap();
	Board board;
	PlayerAction playeraction;
	Timer timer;

	public Game() {
		this.board = new Board(this);
		this.playeraction = new PlayerAction(this);
		Control control = new Control(this);

		Window window = new Window();
		window.addKeyListener(control);
		window.add(this.board);
		window.setVisible(true);

		this.timer = new Timer(25, this);
		this.timer.start();
	}

	public static void main(String[] args) {
		new Game();
	}

	public void actionPerformed(ActionEvent e) {
		this.board.repaint();
		// System.out.println(this.toString());
	}

	/**
	 * Returns the current map of the game.
	 * 
	 * @return the current map of the game.
	 */
	public Map getMap() {
		if (this.map != null) {
			return this.map;
		}
		return null;
	}

	/**
	 * Returns the current player of the game.
	 * 
	 * @return the current player of the game.
	 */
	public Player getPlayer() {
		if (this.player != null) {
			return this.player;
		}
		return null;
	}

	public String toString() {
		int x = this.player.getX();
		int y = this.player.getY();
		return "Player -- Position: " + x + ":" + y;
	}
}