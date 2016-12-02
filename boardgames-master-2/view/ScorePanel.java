package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

public class ScorePanel extends JPanel{
	
	private String initTitle;
	
	/**
	 * The constructor used to create a ScorePanel object for players. 
	 * @param x the x-coordinate of the location of this panel
	 * @param y the y-coordinate of the location of this panel
	 * @param initTitle initialize the title which will display on screen
	 */
	public ScorePanel(int x, int y, String initTitle){
		setSize(160, 30);
		setLocation(x,y);
		setBackground(Color.GRAY);
		this.initTitle = initTitle;
	}
	
	/**
	 * Draw numbers on the panel to display the score of players.
	 * @param score the score for one player 
	 * @param title the title in front of the score
	 * @param g the Graphics context in which to paint.
	 */
	public void drawNumber(int score, String title, Graphics g){
		Font f = g.getFont();
		Color c = g.getColor();
		g.setColor(Color.GRAY);
		g.fillRect(0,0, 160, 30);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Monospaced", Font.BOLD,20));
		g.drawString(title + ": " + score + "", 10, 22);
		g.setFont(f);
		g.setColor(c);
	}

	/**
	 * Override the paintComponent in the JComponent. Paint graphics on the panel.
	 * @param g the Graphics context in which to paint. 
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawNumber(00, initTitle, g);
		//g.setColor(Color.WHITE);
		//g.setFont(new Font("Monospaced", Font.BOLD,20));
		//g.drawString("Player1: 28", 10, 22);
	}

	
	
}
