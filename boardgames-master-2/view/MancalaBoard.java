package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Observable;


import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javax.swing.SwingUtilities;

import model.Location;
import model.Mancala;
import model.Player;
import model.ResetNotification;
import model.ScoreChangeNotification;
import model.ValueChangedNotification;
import model.WinNotification;
import controller.MancalaListener;


/**
 * @author mayokaze
 *
 */
public class MancalaBoard extends Board{

	
	/**
	 * Constructor used to create MancalaBoard object
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int CELL_WIDTH = 90;
	public static final int CELL_HEIGHT = 90;




   
/*	private Mancala model;
	BufferedImage offScreenImg;
	Graphics2D offScreenG;
	private State paintState;
	private Point paintPosition;*/
	private int number;
	
	
	public MancalaBoard(boolean mode){

		model = new Mancala();
        model.init(mode);
        model.addObserver(this);
	
		
		this.setSize(Mancala.COLOUM*CELL_WIDTH+2,Mancala.ROW*CELL_HEIGHT+2);
		this.setLocation(UI.SIDE,UI.TOP);
		this.addMouseListener(new MancalaListener());
        this.initImgBuffer();
        this.paintState = State.INIT;
     }
	
	/**
	 * Override the paint method in JComponent, draw different graphics depends on different state
	 * @param g the Graphics context in which to paint
	 */
	public void paint(Graphics g) {
      switch (paintState){
		case INIT :
			super.paintComponent(g);
	        this.initBoard(this.offScreenG);
	        System.out.println("REpaint======");
	        break;
	
		case NUMBER :
			this.paintNumber(this.offScreenG);
			  System.out.println("REpaintNumber======");
			break;

		default :
			System.out.println("Nothing");
			break;
		}
		g.drawImage(offScreenImg,0,0,this); 
	
		
	//	System.out.println("REpaint======");
	}
	
	/**
	 * Initialize the elements on the gaming board which includes the cells and pieces.
	 * @param g the Graphics context in which to paint
	 */
	protected void initBoard(Graphics g){
		/*
		Color old = g.getColor();
		g.setColor(Color.YELLOW);
		g.fillRect(0, 0, CELL_WIDTH*Mancala.COLOUM, CELL_HEIGHT*Mancala.ROW);
		g.setColor(Color.ORANGE);
		for(int i=0;i<3;i++){
			g.fillRect(2*i*CELL_WIDTH, 0, CELL_WIDTH, CELL_HEIGHT);
			g.fillRect((2*i+1)*CELL_WIDTH, 0, CELL_WIDTH, CELL_HEIGHT);
		}
		g.setColor(old);
		*/
		//row
		for(int j=0;j<=Mancala.ROW;j++){
			g.drawLine(0, j*CELL_HEIGHT  , Mancala.COLOUM*CELL_WIDTH, j*CELL_HEIGHT);
			System.out.println("draw y");
		}
		//column
		for(int i=0;i<=Mancala.COLOUM;i++){
			g.drawLine(i*CELL_WIDTH , 0, i*CELL_WIDTH,Mancala.ROW*CELL_HEIGHT);
		}
		/*
		for(int i=0;i<Mancala.COLOUM;i++){
			for(int j=0;j<2;j++){
				g.drawImage(Images.BACKGROUND, i*CELL_WIDTH, j*CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT, null);
			}
		}
		*/
		for(int i=0;i<Mancala.COLOUM;i++){
			for(int j=0;j<Mancala.ROW;j++){
				Location l = new Location(i,j);
				this.fill(g, getPosition(l));
				this.drawNumber(g,getPosition(l), model.getValue(l));
			//	System.out.print(model.getValue(l));
				System.out.println("col = " + i+" row = "+ j);
			}
			//System.out.println();
		}
	}

	/**
	 * Draw numbers and pieces on the board.
	 * @param g the Graphics context in which to paint
	 */
	private void paintNumber(Graphics g){
		
	      this.fill(g, paintPosition);
	      this.drawNumber(g, paintPosition, number);

 }
    
 /*   public void reset(int rows,int cols){
    	this.setSize(cols * CELL_HEIGHT + 25,rows * CELL_WIDTH + 25);
    	matrix = new boolean[rows][cols];
        this.paintState = State.INIT;
        this.initImgBuffer();
        this.repaint();
    }*/
	/**
	 * Fill a rectangle use the current color, used to cover the original graphics. 
	 * @param p the position of the upper left corner of the rectangle
	 * @param g the Graphics context in which to paint
	 */
	protected void fill(Graphics g,Point p){
		Color oldc = g.getColor();
		g.setColor(new Color(237,237,237));
		g.fillRect(p.x+1,p.y+1,CELL_WIDTH-2,CELL_HEIGHT-2);
		g.setColor(oldc);
	}
	
	/**
	 * Draw the combination of numbers and pieces on the cells within the specified number.
	 * @param g the Graphics context in which to paint
	 * @param p the position of the upper left corner of the image
	 * @param number the piece number
	 */
	private void drawNumber(Graphics g,Point p,int number){
        switch(number){
        case 0:
        	g.drawImage(Images.PIECE_0, p.x,p.y, CELL_WIDTH, CELL_HEIGHT, null);
        	break;
        case 1:
        	g.drawImage(Images.PIECE_1, p.x,p.y, CELL_WIDTH, CELL_HEIGHT, null);
        	break;
        case 2:
        	g.drawImage(Images.PIECE_2, p.x,p.y, CELL_WIDTH, CELL_HEIGHT, null);
        	break;
        case 3:
        	g.drawImage(Images.PIECE_3, p.x,p.y, CELL_WIDTH, CELL_HEIGHT, null);
        	break;
        case 4:
        	g.drawImage(Images.PIECE_4, p.x,p.y, CELL_WIDTH, CELL_HEIGHT, null);
        	break;
        case 5:
        	g.drawImage(Images.PIECE_5, p.x,p.y, CELL_WIDTH, CELL_HEIGHT, null);
        	break;
        case 6:
        	g.drawImage(Images.PIECE_6, p.x,p.y, CELL_WIDTH, CELL_HEIGHT, null);
        	break;
        case 7:
        	g.drawImage(Images.PIECE_7, p.x,p.y, CELL_WIDTH, CELL_HEIGHT, null);
        	break;
        case 8:
        	g.drawImage(Images.PIECE_8, p.x,p.y, CELL_WIDTH, CELL_HEIGHT, null);
        	break;
        case 9:
        	g.drawImage(Images.PIECE_9, p.x,p.y, CELL_WIDTH, CELL_HEIGHT, null);
        	break;
        case 10:
        	g.drawImage(Images.PIECE_10, p.x,p.y, CELL_WIDTH, CELL_HEIGHT, null);
        	break;
        case 11:
        	g.drawImage(Images.PIECE_11, p.x,p.y, CELL_WIDTH, CELL_HEIGHT, null);
        	break;
        case 12:
        	g.drawImage(Images.PIECE_12, p.x,p.y, CELL_WIDTH, CELL_HEIGHT, null);
        	break;
        case 13:
        	g.drawImage(Images.PIECE_13, p.x,p.y, CELL_WIDTH, CELL_HEIGHT, null);
        	break;
        case 14:
        	g.drawImage(Images.PIECE_14, p.x,p.y, CELL_WIDTH, CELL_HEIGHT, null);
        	break;
        case 15:
        	g.drawImage(Images.PIECE_15, p.x,p.y, CELL_WIDTH, CELL_HEIGHT, null);
        	break;
        default:
        	System.out.println("Need more pieces.");
        	break;
        }
	}
	
	
	/**
	 * Returns the location after converting a point to a location.
	 * @param p the point indicate a position
	 */
	public Location getLocationFromPonit(Point p){

		  return new Location((p.x)/CELL_WIDTH,(p.y)/CELL_HEIGHT);
		  
		}
	
	/**
	 * Returns the position after converting a location to a point
	 * @param l the location in the model
	 */
	public Point getPosition(Location l){
	   return new Point(l.getX()*CELL_WIDTH,l.getY()*CELL_HEIGHT);
	}
	
	/**
	 * Implements the Observable interface and make responds for notifications.
	 */
	public void update(Observable arg0, Object obj) {
		
		System.out.println("noticed");
		//System.out.println();
		
		if(obj instanceof ValueChangedNotification){
			
			ValueChangedNotification vn =(ValueChangedNotification)obj;
			  this.paintPosition = this.getPosition(vn.getLocation());
			  this.paintState = State.NUMBER;
			  this.number = model.getValue(vn.getLocation());
			  paintImmediately(paintPosition.x, paintPosition.y, CELL_WIDTH,CELL_HEIGHT);
			   repaint();
			//  paintImmediately(0,0,this.getWidth(),this.getHeight());
			 // this.repaint();
		/*	  UI ui =  (UI)SwingUtilities.windowForComponent(this);
			 model mc = (model)model;
			 ui.setTitle(mc.getScore());*/
			  
	     }
		else if(obj instanceof WinNotification){
			WinNotification win = (WinNotification)obj;
			JFrame message = new JFrame();
			JOptionPane.showMessageDialog(message, "Player "+ win.getPlayer().getFlag()+"" + " win");
        }
		else if(obj instanceof ScoreChangeNotification){
			ScoreChangeNotification s = (ScoreChangeNotification)obj;
			int score = s.getPlayer().getScore();
			UI ui = (UI)SwingUtilities.windowForComponent(this);
			if(s.getPlayer().getFlag() == Player.PLAYER1_FLAG){
				ui.drawScoreForPlayer1(score, "Player1");
			}
			else if(s.getPlayer().getFlag() == Player.PLAYER2_FLAG){
				ui.drawScoreForPlayer2(score, "Player2");
			}
		}
		else if(obj instanceof ResetNotification){
	    	 this.paintState = State.INIT;
	    	 repaint();
		}
	}



}
