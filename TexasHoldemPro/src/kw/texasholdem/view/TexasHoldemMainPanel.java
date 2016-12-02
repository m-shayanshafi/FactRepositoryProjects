package kw.texasholdem.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import kw.texasholdem.tool.Card;
import kw.texasholdem.tool.Table;
import kw.texasholdem.util.ResourceManager;
import kw.texasholdem.util.RestoreManager;
import kw.texasholdem.view.MenuPanel;
import kw.texasholdem.ai.Client;
import kw.texasholdem.ai.impl.HumanClient;
import kw.texasholdem.ai.impl.Player;
import kw.texasholdem.ai.impl.SmartRobot;
import kw.texasholdem.config.Action;
import kw.texasholdem.config.AppConfig;
import kw.texasholdem.controller.ViewController;

/**
 * The game's main frame.
 * 
 * This is the core class of the Swing UI client application.
 * 
 * @author Ken Wu
 */
public class TexasHoldemMainPanel extends JFrame implements Serializable, WindowListener {
    
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;
    
    /** The starting cash per player. */
    private static final int STARTING_CASH = 100;
    
    /** The size of the big blind. */
    private static final int BIG_BLIND = 2;
    
    /** The table. */
    private Table table = null;
    
    /** The players at the table. */
    private LinkedHashMap<String, Player> players = null;
    
    /** The GridBagConstraints. */
    private GridBagConstraints gc  = null;
    
    /** The board panel. */
    private BoardPanel boardPanel  = null;
    
    /** The control panel. */
    private ControlPanel controlPanel  = null;
    
    /** The view controller */
	private ViewController mViewController;
    
    /** The player panels. */
    private HashMap<String, PlayerPanel> playerPanels  = null;
    
    /** The current dealer's name. */
    private String dealerName; 

    /** The current actor's name. */
    private String actorName; 

    private JPanel gamePanel = null;
    private JPanel gamePanelOuterRight = null;
    private JPanel gamePanelOuterLeft = null;
    private JPanel gamePanelOuter = null;

	private IntroPanel m_ip;
    
    public final static String INTRODUCTIONPANEL = "Introduction Panel";
    public final static String GAMEPANEL = "Game Panel";
    public final static int INTRODUCTION_PANEL = 1;
    public final static int GAME_PANEL = 2;
    private int currentPanel = INTRODUCTION_PANEL;
    private boolean hasTheGameStarted = false;
    JPanel cards; //a panel that uses CardLayout

	private Player playerMe;

	private String playerMeName;
	
	private int numOfPlayers = 4;
	
	private PlayerProfilesLeftPanel mPlayerProfilesLeftPanel;

	private MenuPanel m_menubar;

	/** A variable indicating if the program is needed to be save*/
	private boolean isRequestedToSave = false;
	
	/** A variable indicating if the program is being saved currently*/
	private boolean isBeingSaved = false;

	private boolean thisApplicationExit = false;
	
    /**
     * Constructor.
     */
    public TexasHoldemMainPanel(int state) {
    	super("Texas Hold'em Pro - v1.0");
    	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    	this.addWindowListener( new WindowAdapter()
    	{
    	    public void windowClosing(WindowEvent e)
    	    {
    	    	if(currentPanel == INTRODUCTION_PANEL) {
    	    		issueExitRequest();
    	    		closeAndExitIfNeeded();
    	    	}
    	        JFrame frame = (JFrame)e.getSource();
    	        int result = JOptionPane.showConfirmDialog(
    	            frame,
    	            "Do you want to save the game before exiting?",
    	            "Save it?",
    	            JOptionPane.YES_NO_OPTION);

    	        if (result == JOptionPane.YES_OPTION) {
    	        	issueSaveRequest();
    	        } 
    	        issueExitRequest();
    	        controlPanel.interruptOfTheUserWaitThread();
    	    }
    	});

        init();
        init2();
    	//gameStart("dew");
        
      //Create the panel that contains the "cards".
        cards = new JPanel(new CardLayout());
        cards.add(m_ip, INTRODUCTIONPANEL);
        cards.add(gamePanelOuter, GAMEPANEL);
        
        
        getContentPane().add(cards, BorderLayout.CENTER);
        
        
     // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Determine the new location of the window
        int w = this.getSize().width;
        int h = this.getSize().height;
        int x = (dim.width-w)/9;
        int y = (dim.height-h)/9;
        
        // Move the window
        this.setLocation(x, y);
        
        // Show the frame.
        pack();
        setResizable(false);
        setVisible(true);
        switchToFrame(INTRODUCTION_PANEL);    //hasTheGameStarted = true; 
        
        while(!hasTheGameStarted) {
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
			}
        }
        initPLayersAndTableAndOthersDependcies();
        table.run();
        
    }

    private void issueExitRequest() {
		thisApplicationExit = true;
	}
    
    
    public void closeAndExitIfNeeded() {
    	if(thisApplicationExit) {
    		System.exit(0);
    	}
    }

	public void switchToFrame(int frame) {
		CardLayout cl;
		currentPanel = frame;
    	switch (frame) {
			case INTRODUCTION_PANEL:
				cl = (CardLayout)(cards.getLayout());
				cl.show(cards, INTRODUCTIONPANEL);
		        setSize(AppConfig.APP_WIDTH, AppConfig.APP_HEIGHT);
				break;
			case GAME_PANEL:
				cl = (CardLayout)(cards.getLayout());
				cl.show(cards, GAMEPANEL);
				setSize(AppConfig.APP_WIDTH_2, AppConfig.APP_HEIGHT_2+70);
				
		    	m_menubar = new MenuPanel(this);
		    	this.setMenuBar(m_menubar);
				
			break;
		}
    }
    
    private void init() {
        
        m_ip = new IntroPanel(this);
        //this.add(m_ip);
        //this.setPreferredSize(m_ip.getSize());
        
      
    }
    

	private JScrollPane mScrollProfileLeftPanel;
    
    private void init2() {

	    gamePanelOuterLeft = new JPanel();
	    mScrollProfileLeftPanel = (JScrollPane) createScrollProfileLeftPanel();
    	gamePanelOuterLeft.add(mScrollProfileLeftPanel);
    	
    	gamePanelOuterRight = new JPanel();
    	gamePanel = new JPanel() {
				private static final long serialVersionUID = 3240933967004754920L;
	
				@Override
				public void paintComponent(Graphics page)
				{
					Image img = ResourceManager.getImage(AppConfig.BACKGROUND_PORKER_TABLE_IMG);
				    super.paintComponent(page);
				    page.drawImage(img, 0, 0, null);
				}
			};
		controlPanel = new ControlPanel();
	    boardPanel = new BoardPanel(controlPanel);
	    BoxLayout box = new BoxLayout(gamePanelOuterRight, BoxLayout.Y_AXIS);
	    gamePanelOuterRight.setLayout(box);
	    gamePanel.setPreferredSize(new Dimension(AppConfig.APP_WIDTH_2, AppConfig.APP_HEIGHT_2));
	    
	    //gamePanelOuter.add(profileLeftPanel);
	    gamePanelOuterRight.add(gamePanel);
	    gamePanelOuterRight.add(controlPanel);
	    
	    gamePanelOuter = new JPanel();
	    gamePanelOuter.setLayout(new BoxLayout(gamePanelOuter, BoxLayout.X_AXIS));
	    gamePanelOuter.add(gamePanelOuterLeft);
	    gamePanelOuter.add(gamePanelOuterRight);
	    

    }
    
    private Component createScrollProfileLeftPanel() {
    	mPlayerProfilesLeftPanel = new PlayerProfilesLeftPanel();
    	//mPlayerProfilesLeftPanel.setBackground(Color.black);
    	
    	JScrollPane scroller = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	//scroller.setBounds(0, 0, 200, AppConfig.APP_HEIGHT_2+400);
    	scroller.getViewport().add(mPlayerProfilesLeftPanel);
    	return scroller;
	}

    private void initPlayerPanels() {

        int i = 0;
        for (Player player : players.values()) {
            PlayerPanel panel = new PlayerPanel();
            playerPanels.put(player.getName(), panel);
            addComponent(panel, AppConfig.getMap(i, numOfPlayers).x, AppConfig.getMap(i, numOfPlayers).y);
            i++;
        }
    }
    
	/*
     * This will be called once the game is started
     */
    private void initPLayersAndTableAndOthersDependcies() {
    	
        gc = new GridBagConstraints();
        
        playerPanels = new HashMap<String, PlayerPanel>();
    	
        
        restore();
        
        if(players == null) {
	        playerMe = new Player(this.playerMeName, true, STARTING_CASH, new HumanClient(this, boardPanel, controlPanel, playerPanels));
	        playerMe.setProfileURL("/images/profile.png");
	        
	        players = new LinkedHashMap<String, Player>();
	        //Player 1 - which is me
	        players.put(playerMeName, playerMe);
	        //Player 2
	        players.put("Alice",    new Player("Alice",  false, STARTING_CASH, new SmartRobot()));
	        //Player 3
	        players.put("Bob",   new Player("Bob", true,  STARTING_CASH, new SmartRobot()));
	        //Player 4
	        players.put("Charles",  new Player("Charles", true,  STARTING_CASH, new SmartRobot()));
	
	        if(numOfPlayers >= 5) {
	        	//Player 5
	        	players.put("Edward",  new Player("Edward", true, STARTING_CASH, new SmartRobot()));
	        }
	        
	        if(numOfPlayers >= 6) {
	        	//Player 5
	        	players.put("John",  new Player("John", true,  STARTING_CASH, new SmartRobot()));
	        }
	        
	        if(numOfPlayers >= 7) {
	        	//Player 5
	        	players.put("Nancy",  new Player("Nancy", false, STARTING_CASH, new SmartRobot()));
	        }
	        
	        if(numOfPlayers >= 8) {
	        	//Player 5
	        	players.put("Sam",  new Player("Sam", true,  STARTING_CASH, new SmartRobot()));
	        }
	        
	        initPlayerPanels();
        } else {
        	numOfPlayers = players.size();
        	initPlayerPanels();
        	//Now i still manually need to construct the HumnanClient player and set its dependency appromixatly
        	for(Player p: players.values()) {
        		if(p.getClient() instanceof HumanClient) {
        			HumanClient hc = (HumanClient) p.getClient();
        			hc.setTexasHoldemMainPanel(this);
        			hc.setBoardPanel(boardPanel);
        			hc.setControlPanel(controlPanel);
        			hc.setPlayerPanels(playerPanels);
        		}
        	}
        }

        if(table == null) {
	        table = new Table(BIG_BLIND);
	        for (Player player : players.values()) {
	            table.addPlayer(player);
	        }
        } else {
        	//Now i am constructing its player
        	table.resetAllPlayers(players);
        }
        mViewController = new ViewController(this, m_menubar);
        table.setViewController(mViewController);
        
      	gamePanel.setLayout(null);
       	gamePanel.setSize(AppConfig.APP_WIDTH_2, AppConfig.APP_HEIGHT_2);
       	gamePanel.setBounds(0, 0, AppConfig.APP_WIDTH_2, AppConfig.APP_HEIGHT_2);
       	addComponent(boardPanel, 253, 250);
        
        mPlayerProfilesLeftPanel.addAllPlayers(players);
        
        this.repaint();

    }
    
    /**
	 * @return the dealerName
	 */
	public String getDealerName() {
		return dealerName;
	}


	/**
	 * @param dealerName the dealerName to set
	 */
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}


	/**
	 * @return the actorName
	 */
	public String getActorName() {
		return actorName;
	}


	/**
	 * @param actorName the actorName to set
	 */
	public void setActorName(String actorName) {
		this.actorName = actorName;
	}


    private void addComponent(Component component, int x, int y, int width, int height) {
        gc.gridx = x;
        gc.gridy = y;
        gc.gridwidth = width;
        gc.gridheight = height;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gamePanel.add(component, gc);
    }
    
    private void addComponent(Component component, int x, int y) {
    	Dimension d = component.getPreferredSize();
    	component.setBounds(x, y, d.width, d.height);
        gamePanel.add(component);
    }

    /**
     * Sets whether the actor  is in turn.
     * 
     * @param isInTurn
     *            Whether the actor is in turn.
     * @param actor 
     */
    public void setActorInTurn(boolean isInTurn, Player actor) {
        if (actorName != null) {
            PlayerPanel playerPanel = playerPanels.get(actorName);
            if (playerPanel != null) {
                playerPanel.setInTurn(isInTurn);
            }
            mPlayerProfilesLeftPanel.setInTurn(actorName, isInTurn, actor);
        }
    }

    /**
     * Sets the dealer.
     * 
     * @param isDealer
     *            Whether the player is the dealer.
     */
    public void setDealer(boolean isDealer) {
        if (dealerName != null) {
            PlayerPanel playerPanel = playerPanels.get(dealerName);
            if (playerPanel != null) {
                playerPanel.setDealer(isDealer);
            }
        }
    }
    
    
    public void setPlayerName(String p) {
    	this.playerMeName = p;
    }

	public void gameStart() {
		this.hasTheGameStarted = true;
	}

	public void setNumOfPlayer(int num) {
		this.numOfPlayers = num;
	}

    /**
     * Saves the current state to a file object by sending the save signal internally
     * 
     */
	public void issueSaveRequest() {
		isRequestedToSave = true;
	}
	
	public void saveItNowIfNeeded() {
		if(isRequestedToSave) {
			setBeingSaved(true);
			RestoreManager.save(players, AppConfig.PLAYERS_FILE_PATH);
			RestoreManager.save(table, AppConfig.TABLE_FILE_PATH);
			setBeingSaved(false);
			isRequestedToSave = false;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void restore() {
		players = (LinkedHashMap<String, Player>)RestoreManager.restore(AppConfig.PLAYERS_FILE_PATH);
		table = (Table) RestoreManager.restore(AppConfig.TABLE_FILE_PATH);
		if(table != null) {
			table.SetIsResumeFromLastGame(true);
		}
	}

	/**
	 * @return the isBeingSaved
	 */
	public boolean isBeingSaved() {
		return isBeingSaved;
	}

	/**
	 * @param isBeingSaved the isBeingSaved to set
	 */
	private void setBeingSaved(boolean isBeingSaved) {
		this.isBeingSaved = isBeingSaved;
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void setLeftProfilePanelVisible(String playerName, boolean b) {
		mPlayerProfilesLeftPanel.setLeftProfilePanelVisible(playerName,b);
	}
	
	public void resetAllLeftProfilePanel(boolean b) {
		mPlayerProfilesLeftPanel.resetAllLeftProfilePanel(b);
	}

	public void updateLeftPlayerPanel(Player p) {
		mPlayerProfilesLeftPanel.updateLeftPlayerPanel(p);
	}


    
}
