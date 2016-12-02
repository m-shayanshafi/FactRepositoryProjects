package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;



import model.Location;
import model.Player;
import model.ResetNotification;
import model.RollNumberChangedNotification;
import model.ScoreChangeNotification;
import model.SelectedNotification;
import model.Senet;
import model.ValueChangedNotification;
import model.WinNotification;
import controller.SenetListener;

public class SenetBoard extends Board {

	private static final long serialVersionUID = 1L;
	public static final int CELL_WIDTH = 60;
	public static final int CELL_HEIGHT = 60;
	private int playerFlag;
	
	/**
	 * The constructor used to create a SenetBoard object. 
	 */
	public SenetBoard(boolean mode){
     
		model = new Senet();
        model.init(mode);
        model.addObserver(this);
	
		
		this.setSize(Senet.COLOUM*CELL_WIDTH+2,Senet.ROW*CELL_HEIGHT+2);
		this.setLocation(UI.SIDE,UI.TOP);
		this.addMouseListener(new SenetListener());
        this.initImgBuffer();
        this.paintState = State.INIT;
     }
	
	/**
	 * Override the paint() in JComponent.
	 * There are 4 states, for each case draw different images on screen.
	 * @param g the Graphics context in which to paint.
	 */
	public void paint(Graphics g) {
	      switch (paintState){
			case INIT :
				super.paintComponent(g);
		        this.initBoard(this.offScreenG);
		        System.out.println("REpaint======");
		        break;
		
			case PIECE:
				this.paintPiece(this.offScreenG);
				  System.out.println("REpaintPiece======");
				break;
			case SELECTED:
				this.paintSelected(this.offScreenG);
				break;
			case HOUSE:
				this.paintHouse(this.offScreenG);
				break;
			default :
				System.out.println("Nothing");
				break;
			}
			g.drawImage(offScreenImg,0,0,this); 
		
			
		//	System.out.println("REpaint======");
		}
	
	/**
	 * Paint a piece on the screen, this will fill a rectangle to cover the original images. 
	 * @param g the Graphics context in which to paint.
	 */
	private void paintPiece(Graphics g){
          this.fill(g, paintPosition);
	      this.drawPiece(g, paintPosition, playerFlag);
     }
	
	/**
	 * Draw a piece which is selected on board, which will case the flag for the different players.
	 * @param g the Graphics context in which to paint.
	 */
	private void paintSelected(Graphics g){
		this.fill(g, paintPosition);
		 switch(playerFlag){
		    case 1:
		    	g.drawImage(Images.SENET_1_CLICKED, paintPosition.x,paintPosition.y , CELL_WIDTH, CELL_HEIGHT, null);
		    	break;
		    case 2:
		       g.drawImage(Images.SENET_2_CLICKED, paintPosition.x,paintPosition.y , CELL_WIDTH, CELL_HEIGHT, null);
		       break;
		    default:	
		    	System.out.println("Invalid player");
		 }
	}
	
	/**
	 * Draw a special cell "Beautiful House" on the board. 
	 * @param g the Graphics context in which to paint.
	 */
	private void paintHouse(Graphics g){
		System.out.println("PaintHouse");
		this.fill(g, paintPosition);
	//	g.drawImage(Images.HOUSE,  paintPosition.x,paintPosition.y, CELL_WIDTH, CELL_HEIGHT, null);
		g.drawImage(Images.HOUSE, paintPosition.x+1, paintPosition.y+1 , CELL_WIDTH-2, CELL_HEIGHT-2, null);
	}
	
	/**
	 * Draw a piece on the board. The piece for which player and the position is defined in the parameters. 
	 * @param g the Graphics context in which to paint..
	 * @param p the point of the upper left corner of the piece to be drawn. 
	 * @param playerFlag the flag for the player.
	 */
	private void drawPiece(Graphics g,Point p,int playerFlag){
	        //Font oldf = g.getFont();
	        //g.setFont(new Font("Monospaced", Font.BOLD,CELL_WIDTH/2));
	        //g.drawString(""+playerFlag,p.x+CELL_WIDTH/3,p.y+CELL_HEIGHT/2);
	        //g.setFont(oldf);
		
		   switch(playerFlag){
		    case 1:
		    	g.drawImage(Images.SENET_1, p.x,p.y , CELL_WIDTH, CELL_HEIGHT, null);
		    	break;
		    case 2:
		       g.drawImage(Images.SENET_2, p.x,p.y , CELL_WIDTH, CELL_HEIGHT, null);
		       break;
		    case 5:
		    	g.drawImage(Images.OUT, p.x,p.y , CELL_WIDTH, CELL_HEIGHT, null);
		    	 break;
		    default:	
		    	System.out.println("Invalid player");
	        
		   }
		
	
		}
	
	/**
	 * Fill a rectangle with the current color. Here used to cover the original images.
	 * @param p the point of the upper left corner of the piece to be drawn.
	 */
	protected void fill(Graphics g,Point p){
		Color oldc = g.getColor();
		g.setColor(new Color(237,237,237));
		g.fillRect(p.x+1,p.y+1,CELL_WIDTH-2,CELL_HEIGHT-2);
		g.setColor(oldc);
	}
	
	/**
	 * Initialize the elements on the gaming board, include the cells, pieces and buttons. 
	 */
	protected void initBoard(Graphics g){
       //row
		for(int j=0;j<=Senet.ROW;j++){
			g.drawLine(0, j*CELL_HEIGHT  , (Senet.COLOUM-1)*CELL_WIDTH, j*CELL_HEIGHT);
		//	System.out.println("draw y");
		}
		//column
		for(int i=0;i<Senet.COLOUM;i++){
			g.drawLine(i*CELL_WIDTH , 0, i*CELL_WIDTH,Senet.ROW*CELL_HEIGHT);
		}
        for(int i=0;i<Senet.COLOUM;i++){
			for(int j=0;j<Senet.ROW;j++){
				Location l = new Location(i,j);
				this.fill(g, getPosition(l));
				System.out.println("col = " + i+" row = "+ j);
				this.drawPiece(g,getPosition(l), model.getValue(l));
			//System.out.print(model.getValue(l));
			}
			//System.out.println();
		}
		Point p;
		p = getPosition(new Location(10,0));
		g.drawImage(Images.GO, p.x+1, p.y+1 , CELL_WIDTH-2, CELL_HEIGHT-2, null);
		p = getPosition(new Location(10,1));
		g.drawImage(Images.PASS, p.x+1, p.y+1 , CELL_WIDTH-2, CELL_HEIGHT-2, null);
		p = getPosition(new Location(10,2));
		g.drawImage(Images.OUT, p.x+1, p.y+1 , CELL_WIDTH-2, CELL_HEIGHT-2, null);
/*		p = getPosition(new Location(9,2));
		g.drawImage(Images.LAST_1, p.x+1, p.y+1 , CELL_WIDTH-2, CELL_HEIGHT-2, null);
		p = getPosition(new Location(8,2));
		g.drawImage(Images.LAST_2, p.x+1, p.y+1 , CELL_WIDTH-2, CELL_HEIGHT-2, null);
		p = getPosition(new Location(7,2));
		g.drawImage(Images.LAST_3, p.x+1, p.y+1 , CELL_WIDTH-2, CELL_HEIGHT-2, null);
		p = getPosition(new Location(6,2));
		g.drawImage(Images.WAVE, p.x+1, p.y+1 , CELL_WIDTH-2, CELL_HEIGHT-2, null);*/
		p = getPosition(new Location(5,2));
		g.drawImage(Images.HOUSE, p.x, p.y , CELL_WIDTH-2, CELL_HEIGHT-2, null);
	}
	
	/**
	 * The implements method for the Observable interface. Receive notifications and make responds. 
	 */
	public void update(Observable arg0, Object obj) {
		
		//System.out.println();
		
		if(obj instanceof RollNumberChangedNotification){
			UI ui = (UI)SwingUtilities.windowForComponent(this);
			//ui.setTitle(((RollNumberChangedNotification) obj).getNumber()+"");
			ui.drawRollPoints(((RollNumberChangedNotification) obj).getNumber());
		}
		else if(obj instanceof ValueChangedNotification){
			System.out.println("noticed");
			ValueChangedNotification vn =(ValueChangedNotification)obj;
			  this.paintPosition = this.getPosition(vn.getLocation());
			  this.playerFlag = model.getValue(vn.getLocation());
			  if(this.playerFlag == 0 && vn.getLocation().equals(Senet.BEATIFUL_HOUSE))
				  this.paintState = State.HOUSE;
			  else
			   this.paintState = State.PIECE;
			  
			  paintImmediately(paintPosition.x, paintPosition.y, CELL_WIDTH,CELL_HEIGHT);
			   repaint();
			//  paintImmediately(0,0,this.getWidth(),this.getHeight());
			 // this.repaint();
		/*	  UI ui =  (UI)SwingUtilities.windowForComponent(this);
			 model mc = (model)model;
			 ui.setTitle(mc.getScore());*/
			  
	     }

		else if(obj instanceof SelectedNotification){
			SelectedNotification sn = 	(SelectedNotification) obj;
			//System.out.println("selected location x = "+ ((SelectedNotification) obj).getNewS().getX()+" y = "+((SelectedNotification) obj).getNewS().getY());
			if( sn.getOldS()!= null){
				System.out.println("unselected.....");
				this.paintPosition = this.getPosition(sn.getOldS());
				  this.playerFlag = model.getValue(sn.getOldS());
				 this.paintState = State.PIECE;
				 paintImmediately(paintPosition.x, paintPosition.y, CELL_WIDTH,CELL_HEIGHT);
				 repaint();
			}
			this.paintPosition = this.getPosition(sn.getNewS());
			this.playerFlag = model.getValue(sn.getNewS());
			this.paintState = State.SELECTED;
			 paintImmediately(paintPosition.x, paintPosition.y, CELL_WIDTH,CELL_HEIGHT);
			 repaint();
			 //System.out.println("unselected location x = "+ ((SelectedNotification) obj).getOldS().getX()+" y = "+((SelectedNotification) obj).getOldS().getY());
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
		else if(obj instanceof WinNotification){
			WinNotification win = (WinNotification)obj;
			JFrame message = new JFrame();
			JOptionPane.showMessageDialog(message, "Player "+ win.getPlayer().getFlag()+"" + " win");
        }
		else if(obj instanceof ResetNotification){
	    	 this.paintState = State.INIT;
	    	 repaint();
		}
	}
	
	/**
	 * Returns the location by converting a point object.
	 * @param p 
	 * @return the location from a specified point
	 */
	public Location getLocationFromPonit(Point p){
        return new Location((p.x)/CELL_WIDTH,(p.y)/CELL_HEIGHT);
    }
	
	/**
	 * Returns the point by converting a location object.
	 * @param l 
	 * @return the point from a specified location.
	 */
   public Point getPosition(Location l){
	   return new Point(l.getX()*CELL_WIDTH,l.getY()*CELL_HEIGHT);
   }
	

}
