package view;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import model.Senet;

public class RollPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private int value;
	
	public static final int WIDTH = 400;
	public static final int HEIGHT = 100;
	
	/**
	 * Constructor used to create RollPanel object.
	 * @param x specify the x-coordinate of the panel on screen when created.
	 * @param y specify the y-coordinate of the panel on screen when created.
	 * @param value specify the value after rolling. 
	 */
	public RollPanel(int x, int y, int value){
		setSize(WIDTH, HEIGHT);
		setLocation(x, y);
		setBackground(Color.GRAY);
		this.value = value;
	}

	/**
	 * Override the paintComponent method to draw graphics on the panel
	 * @param g the Graphics context in which to paint.
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawRollPoints(value, g);
		drawCards(g);
	}
	
	/**
	 * Draw the images with number on the panel
	 * @param value the number used to display in the bord
	 * @param g the Graphics context in which to paint.
	 */
	public void drawRollPoints(int value, Graphics g){
		this.value = value;
		Color c = g.getColor();
		g.setColor(Color.GRAY);
		g.fillRect(320, 20, 60, 60);
		g.setColor(c);
		switch(value){
		case 0:
			g.drawImage(Images.ROLL_0, 320, 20, 60, 60, null);
			break;
		case 1:
			g.drawImage(Images.ROLL_1, 320, 20, 60, 60, null);
			break;
		case 2:
			g.drawImage(Images.ROLL_2, 320, 20, 60, 60, null);
			break;
		case 3:
			g.drawImage(Images.ROLL_3, 320, 20, 60, 60, null);
			break;
		case 4:
			g.drawImage(Images.ROLL_4, 320, 20, 60, 60, null);
			break;
		case 5:
			g.drawImage(Images.ROLL_5, 320, 20, 60, 60, null);
			break;
		default:
			System.out.println("Roll point out of bound");
			break;
		}
	}
	
	/**
	 * Draw 4 cards for a number in the panel. There are 2 kinds of cards, different combination indicate different numbers.
	 * @param g the Graphics context in which to paint.
	 */
	public void drawCards(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.GRAY);
		g.fillRect(10, 18, 300, 88);
		g.setColor(c);
		switch(value){
		case 0:
			g.drawImage(Images.DOWN, 10, 18, 140, 30, null);
			g.drawImage(Images.DOWN, 10, 58, 140, 30, null);
			g.drawImage(Images.DOWN, 160, 18, 140, 30, null);
			g.drawImage(Images.DOWN, 160, 58, 140, 30, null);
			break;
		case 1:
			g.drawImage(Images.UP, 10, 18, 140, 30, null);
			g.drawImage(Images.DOWN, 10, 58, 140, 30, null);
			g.drawImage(Images.DOWN, 160, 18, 140, 30, null);
			g.drawImage(Images.DOWN, 160, 58, 140, 30, null);
			break;
		case 2:
			g.drawImage(Images.UP, 10, 18, 140, 30, null);
			g.drawImage(Images.UP, 10, 58, 140, 30, null);
			g.drawImage(Images.DOWN, 160, 18, 140, 30, null);
			g.drawImage(Images.DOWN, 160, 58, 140, 30, null);
			break;
		case 3:
			g.drawImage(Images.UP, 10, 18, 140, 30, null);
			g.drawImage(Images.UP, 10, 58, 140, 30, null);
			g.drawImage(Images.UP, 160, 18, 140, 30, null);
			g.drawImage(Images.DOWN, 160, 58, 140, 30, null);
			break;
		case 4:
			g.drawImage(Images.UP, 10, 18, 140, 30, null);
			g.drawImage(Images.UP, 10, 58, 140, 30, null);
			g.drawImage(Images.UP, 160, 18, 140, 30, null);
			g.drawImage(Images.UP, 160, 58, 140, 30, null);
			break;
		case 5:
			g.drawImage(Images.DOWN, 10, 18, 140, 30, null);
			g.drawImage(Images.DOWN, 10, 58, 140, 30, null);
			g.drawImage(Images.DOWN, 160, 18, 140, 30, null);
			g.drawImage(Images.DOWN, 160, 58, 140, 30, null);
			break;
		}
	}

}
