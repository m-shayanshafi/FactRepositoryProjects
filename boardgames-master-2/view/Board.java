package view;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Observer;

import javax.swing.JPanel;

import model.BoardGame;
import model.Location;

public abstract class Board extends JPanel implements Observer{
	
public Board(){
		
	}
	public Board(boolean mode){
		
	}
	public enum State {
		INIT,
		NUMBER,
		EMPTY,
		PIECE,
		HOUSE,
        NOTHING,
        SELECTED
    }
	
	protected BoardGame model;
	protected BufferedImage offScreenImg;
	protected Graphics2D offScreenG;
	protected State paintState;
	protected Point paintPosition;
	
	/**
	 * Initialize the double buffer for creating images.
	 */
    protected void initImgBuffer(){
    	this.offScreenImg =   new BufferedImage(this.getWidth(), this.getHeight(),
                BufferedImage.TYPE_INT_RGB);
    	this.offScreenG = this.offScreenImg.createGraphics();
    	
    	offScreenG .setColor(new Color(237,237,237));
    	offScreenG.fillRect(0, 0,offScreenImg.getWidth() , offScreenImg.getHeight());
    	offScreenG .setColor(Color.BLACK);
    }
	
	/**
	 * Returns a location from a point. To convert the screen position to a location object.
	 * @param p the point on the screen
	 * @return the location object
	 */
	public abstract Location getLocationFromPonit(Point p);
	/**
	 * Returns a point from a location. To convert the a location to the screen position.
	 * @param l the location which need to convert
	 * @return the screen position in a point object
	 */
    public abstract Point getPosition(Location l);
    /**
     * Abstract method used to draw a filled rectangle to cover original images.
     * @param p the point of the upper left corner of the image.
     */
	protected abstract void fill(Graphics g,Point p);
	/**
	 * Initialize the gaming board for each games.
	 * @param p the point of the upper left corner of the piece to be drawn.
	 */
	protected abstract void initBoard(Graphics g);
	/**
	 * Returns the pointer of the BoardGame object.
	 * @return the BoardGame object
	 */
	public BoardGame getModel() {
		return model;
	}

}
