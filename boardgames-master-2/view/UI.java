package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import model.Mancala;
import model.Senet;
import controller.MenuActionListener;


public class UI extends JFrame {
	

	

	private Board gamePanel;
	private ScorePanel scorePanel1;
	private ScorePanel scorePanel2;
	private RollPanel rollPanel;
	private WelcomePanel welcome;
	
	//private JLabel player1;
	//private JLabel player2;

	
//	private final int size = 30;
	public static final int SIDE = 50;
	public static final int TOP = 100;
	public static final int BOTTOM = 20;

	/**
	 * The construct a new UI object. This constructor is the default constructor for a UI context.
	 */
	public UI(){

		launchFrame();
	
	}
	
	/**
	 * Launch a frame with specified elements. 
	 */
	public void launchFrame(){
		setLayout(null);
		setResizable(false);
		welcome = new WelcomePanel();
		add(welcome);
		setSize(SIDE*2+welcome.getWidth(),TOP+BOTTOM+welcome.getHeight());
	//	setSize(SIDE*2+Mancala.COLOUM* MancalaBoard.CELL_WIDTH, TOP+BOTTOM+Mancala.ROW* MancalaBoard.CELL_HEIGHT+50);
	//	setSize(SIDE*2+Senet.COLOUM* SenetBoard.CELL_WIDTH+50, TOP+BOTTOM+Senet.ROW* SenetBoard.CELL_HEIGHT+RollPanel.HEIGHT+100);
	//	setSize((int)Math.round(SIDE*2+12*(2*CCBoard.R+CCBoard.MARGIN)+2*CCBoard.R+Math.sqrt(3)*CCBoard.R), TOP+BOTTOM+(int)Math.round(13.856*(2*CCBoard.R+CCBoard.MARGIN) +4*CCBoard.R)+200);
		JMenu game = new JMenu("Game");
		
		JMenu save = new JMenu("Save/Load");

		JMenuItem ri = new JMenuItem("Restart");

		game.add(ri);
		JMenuBar menu = new JMenuBar();
		menu.add(game);
	
		menu.add(save);
		setJMenuBar(menu);
		MenuActionListener mal =   new MenuActionListener();
		ri.addActionListener(mal);
		scorePanel1 = new ScorePanel(SIDE,TOP-50, "Player1");
		scorePanel1.setVisible(false);
		add(scorePanel1);
		scorePanel2 = new ScorePanel(SIDE*2+Mancala.COLOUM* MancalaBoard.CELL_WIDTH-SIDE-160,TOP-50, "Player2");
		scorePanel2.setVisible(false);
		add(scorePanel2);
		rollPanel = new RollPanel(SIDE+120, TOP+4*SenetBoard.CELL_HEIGHT, 0);
		rollPanel.setVisible(false);
		add(rollPanel);
	

		//	gamePanel = new MancalaBoard();
		//	gamePanel = new SenetBoard();
		//	gamePanel = new CCBoard();
		//	gamePanel.setLayout(null);
		//	add(gamePanel);
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		Dimension   scrSize=Toolkit.getDefaultToolkit().getScreenSize(); 
		setLocation(scrSize.width/2 - this.getWidth()/2 , 100);
		setVisible(true);
	}
	/**
	 * Re-launch welcome panel
	 * 
	 */
	public void launchWelcome(){
		if(gamePanel != null){
		this.remove(gamePanel);
		gamePanel = null;
		}
		if(this.welcome == null){
	      welcome = new WelcomePanel();
			add(welcome);
		}
		setSize(SIDE*2+welcome.getWidth(),TOP+BOTTOM+welcome.getHeight());
      }
	/**
	 * Launch a Mancala panel on the UI frame
	 * @param isSingleMode boolean is single
	 */
	public void launchMancala(boolean isSingleMode){
		if(this.welcome != null){
			this.remove(welcome);
			welcome = null;
		}
		setSize(SIDE*2+Mancala.COLOUM* MancalaBoard.CELL_WIDTH, TOP+BOTTOM+Mancala.ROW* MancalaBoard.CELL_HEIGHT+50);
		gamePanel = new MancalaBoard(isSingleMode);
		gamePanel.setLayout(null);
		add(gamePanel);
	}
	
	/**
	 * Launch a Senet panel on the UI frame
	 * @param isSingleMode boolean is single
	 */
	public void launchSenet(boolean isSingleMode){
		if(this.welcome != null){
			this.remove(welcome);
			welcome = null;
		}
		setSize(SIDE*2+Senet.COLOUM* SenetBoard.CELL_WIDTH+50, TOP+BOTTOM+Senet.ROW* SenetBoard.CELL_HEIGHT+RollPanel.HEIGHT+100);
        gamePanel = new SenetBoard(isSingleMode);
		gamePanel.setLayout(null);
		add(gamePanel);
	}
	
	/**
	 * Launch a Chinese Checkers panel on the UI frame
	 * @param isSingleMode boolean is single
	 */
	public void launchChineseChecker(boolean isSingleMode){
		if(this.welcome != null){
			this.remove(welcome);
			welcome = null;
		}
		setSize((int)Math.round(SIDE*2+12*(2*CCBoard.R+CCBoard.MARGIN)+2*CCBoard.R+Math.sqrt(3)*CCBoard.R), TOP+BOTTOM+(int)Math.round(13.856*(2*CCBoard.R+CCBoard.MARGIN) +4*CCBoard.R)+200);
        gamePanel = new CCBoard(isSingleMode);
		gamePanel.setLayout(null);
		add(gamePanel);
	}
	
	/**
	 * Draw the score for Player 1 on the screen.
	 * @param score The score of player 1 right now.
	 * @param title The title of the score panel.
	 */
	public void drawScoreForPlayer1(int score, String title){
		Graphics g = scorePanel1.getGraphics();
		//g.setColor(Color.GRAY);
		//g.fillRect(SIDE,TOP-50, 160, 30);
		scorePanel1.drawNumber(score, title, g);
	}
	
	/**
	 * Draw the score for Player 2 on the screen.
	 * @param score The score of player 2 right now.
	 * @param title The title of the score panel.
	 */
	public void drawScoreForPlayer2(int score, String title){
		Graphics g = scorePanel2.getGraphics();
		//g.setColor(Color.GRAY);
		//g.fillRect(SIDE*2+Mancala.COLOUM* MancalaBoard.CELL_WIDTH-SIDE-160,TOP-50, 160, 30);
		scorePanel2.drawNumber(score, title, g);
	}
	
	/**
	 * Draw number and cards on the RollPanel on the screen.
	 * @param number Specify the number which used to draw the number and relative cards. Here the range of the number should be 1 to 5.
	 */
	public void drawRollPoints(int number){
		Graphics g = rollPanel.getGraphics();
		rollPanel.drawRollPoints(number, g);
		rollPanel.drawCards(g);
	}
	/**
	 * Set visible for the score panel
	 * @param isVisible boolean value
	 */
	public void setScorePanelVisible(boolean isVisible){
		scorePanel1.setVisible(isVisible);
		scorePanel2.setVisible(isVisible);
	}
	/**
	 * Set visible for the roll panel
	 * @param isVisible boolean value
	 */
	public void setRollPanelVisible(boolean isVisible){
		rollPanel.setVisible(isVisible);
	}
	
}
