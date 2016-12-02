/*
 * Classname		: DC2dGUI
 * Author				: Christophe Hertigers <xof@pandora.be> 
 * Creation Date	: 2002/02/27 
 * Last Updated		: Saturday, October 19 2002, 13:13:09 
 * Description		: Dimensional user interface for DragonChess. 
 * GPL disclaimer 	: 
 * This program is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the   Free Software Foundation; version 2 of the License.   This
 * program is distributed in the hope that it will be useful, but   WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY   or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License   for more
 * details. You should have received a copy of the GNU General   Public License
 * along with this program; if not, write to the Free Software   Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package gui2d;

/* package import */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;
import connectivity.*;
import main.*;

/**
 * The 2-dimensional front-end for DragonChess.
 * <p>It is composed of a JFrame, with 4 JPanels (3 containing the 
 *    boards, 1 for the game controls). The DCBoards are
 *    represented by arrays of DCImageButtons.
 *
 * @author		Christophe Hertigers
 * @version     Saturday, October 19 2002, 13:13:09
 */
public class DC2dGUI implements DCFrontEnd {

	/*
	 * VARIABLES
	 *
	 */
	
	/* CLASS VARIABLES */
	
	/**
	 * Number of files on the board
	 */
	public final static int FILES = DCConstants.FILES;

	/**
	 * Number of ranks on the board
	 */
	public final static int RANKS = DCConstants.RANKS;

	private final static int BOARDS = DCConstants.BOARDS;
	private final static int SCREEN_WIDTH =
		Toolkit.getDefaultToolkit().getScreenSize().width;
	private final static int SCREEN_HEIGHT =
		Toolkit.getDefaultToolkit().getScreenSize().height;

	private final static Color LIGHT_TOP = new Color(224, 244, 251),
		DARK_TOP = new Color(187, 228, 243),
		LIGHT_MIDDLE = new Color(165, 228, 153),
		DARK_MIDDLE = new Color(118, 179, 105),
		LIGHT_BOTTOM = new Color(209, 203, 137),
		DARK_BOTTOM = new Color(170, 164, 92),
		STANDARD_GRAY = new Color(204, 204, 204),
		GOLD = new Color(255, 246, 97),
		SCARLET = new Color(221, 23, 23),
		HIGHLIGHT = new Color(255, 251, 184);

	private final static Color[] LIGHT_COLORS =
		{ LIGHT_BOTTOM, LIGHT_MIDDLE, LIGHT_TOP },
		DARK_COLORS = { DARK_BOTTOM, DARK_MIDDLE, DARK_TOP };

	private final static Font NAME_FONT = new Font("SansSerif", Font.BOLD, 16);

	/* INSTANCE VARIABLES */

	private DCGame refDCGame;
	private DCLocalConnection connection;
	private DCFrontEndEncoder encoder;
	private DCFrontEndDecoder decoder;

	private int moveCountInt;
	private int gameState;
	private int connectionType;

	private String serverString;
	private int port;

	private int activePlayer;
	private int possibleActivePlayers;

	private String lastMessage = "";
	
	private boolean gotConnection = false;

	/* DISPLAY OBJECTS */

	private JFrame mainFrame;

	private JDesktopPane desktop;

	private DCButtonBoard[] panelArray;

	private DCImageSet iconSet;

	private DCOptions userOptions;

	private ActionHandler aHandler;
	private WindowHandler wHandler;
	private MouseHandler mHandler;
	private ItemHandler iHandler;
	private ComponentHandler cHandler;
	private WindowStateHandler wsHandler;

	public DCConnectionTypePopup connPopup;
	public DCPlayerNamePopup playerNamePopup;
	public DCAnotherGamePopup anotherGamePopup;
	public DCPlayerPreferencesPopup playerPreferencesPopup;

	private JSplitPane mainPanel;
	private JPanel mainGamePanel,
		controlPanel,
		wPanel,
		nwPanel,
		swPanel,
		cPanel,
		ncPanel,
		ccPanel,
		nccPanel,
		cccPanel,
		scPanel,
		sPanel;

	private JButton quitGameButton,
		newGameButton,
		resignGameButton,
		saveGameButton,
		loadGameButton,
		helpButton,
		drawButton,
		prefButton,
		undoButton;

	private DCCommunicationPanel commPanel;

	private JLabel goldTimerLabel,
		scarletTimerLabel,
		goldPlayerLabel,
		scarletPlayerLabel,
		messageLabel,
		statusLabel,
		settingTimerLabel,
		goldListTitle,
		scarletListTitle;

	private JCheckBox timerCheckBox;

	private DefaultListModel goldListModel, scarletListModel;

	private JList goldList, scarletList;

	private JScrollPane goldListScrollPane, scarletListScrollPane;
								
	private Border emptyBorder,
		tabEmptyBorder,
		panelEmptyBorder,
		panelCompoundBorder,
		panelRaisedBorder;

	private DCTimer goldTimer, 
		scarletTimer;
	
	/* 
	 * INNER CLASSES
	 *
	 * class WindowHandler 
	 * class	ActionHandler 
	 * class	MouseHandler 
	 * class ItemHandler 
	 * class	ComponentHandler 
	 * class	WindowStateHandler 
	 * class HierarchyBoundsHandler
	 *
	 */

	/**
	 * Inner class. It handles all WindowEvents.
	 * 
     	 */
	class WindowHandler extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			quitGame();
		}
	}

	/**
	 * Inner class. It handles all ActionEvents.
	 * 
     	 */
	class ActionHandler implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == quitGameButton) {
				quitGame();
			} else if (e.getSource() == newGameButton) {
				if (((gameState == DCConstants.OVER) || 
				    (gameState == DCConstants.READY))){
						
					newGame();
					
				} else { /* Do Nothing. */ }
			} else if (e.getSource() == resignGameButton) {
				if ((gameState == DCConstants.OVER) || 
				    (gameState == DCConstants.INITIALISING)){ 
					/* Do Nothing. */
				} else { 
					resignGame(); 
				}
			} else if (e.getSource() == saveGameButton) {
				if ((gameState == DCConstants.OVER) || 
				    (gameState == DCConstants.INITIALISING) ||
					(activePlayer == DCConstants.PLAYER_NONE)) { 
					/* Do Nothing. */
				} else { 
					saveGame();
				}
			} else if (e.getSource() == loadGameButton) {
				if (activePlayer != DCConstants.PLAYER_NONE) {	
					loadGame();
				}
			} else if (e.getSource() == helpButton) {
				displayHelp();
			} else if (e.getSource() == drawButton) {
				if ((gameState == DCConstants.OVER) || 
				    (gameState == DCConstants.INITIALISING) ||
					(activePlayer == DCConstants.PLAYER_NONE)) { 
					/* Do Nothing. */
				} else { 
					offerDraw();
				}
			} else if (e.getSource() == undoButton) {
				if (gameState == DCConstants.INITIALISING) { 
					/* Do Nothing. */
				} else { 
					undoLastMove();
				}
			} else if (e.getSource() == prefButton) {
				playerPreferencesPopup.showPopup();							
			} else {
				if ((gameState == DCConstants.SELECTSTARTFIELD) &&
					(activePlayer != DCConstants.PLAYER_NONE)) {
					performSelectStartField((DCImageButton) e.getSource());
				} else if ((gameState == DCConstants.SELECTTARGETFIELD) &&
						   (activePlayer != DCConstants.PLAYER_NONE)) { 
					performSelectTargetField((DCImageButton) e.getSource());
				} else if (gameState == DCConstants.OVER) {
					/* Do Nothing. Game's Over Man. */
				}
			}
		}
	}
	
	/**
	 * Inner class. It handles all MouseEvents.
	 * 
     	 */
	class MouseHandler extends MouseAdapter {
		public void mouseEntered(MouseEvent e) {
			DCImageButton myButton = (DCImageButton) e.getSource();
			int r = myButton.getRank();
			int f = myButton.getFile();
			//highlight fields
			for (int i = 0; i < BOARDS; i++) {
				panelArray[i].setButtonBackground(r, f, HIGHLIGHT);
			}
		}		 

		public void mouseExited(MouseEvent e) {
			DCImageButton myButton = (DCImageButton) e.getSource();
			int r = myButton.getRank();
			int f = myButton.getFile();
			//unhighlight fields
			for (int i = 0; i < BOARDS; i++) {
				boolean hlBool = panelArray[i].isButtonHighlighted(r, f);
				if (hlBool) { // if highlighted, set GOLD
					panelArray[i].setButtonBackground(r, f, GOLD);
				} else {	// else set orig background
					Color origColor = panelArray[i].getButtonBgColor(r, f);
					panelArray[i].setButtonBackground(r, f, origColor);
				}
			}
		}
	}

	/**
	 * Inner class. It handles all ItemEvents.
	 * 
     */
	class ItemHandler implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == timerCheckBox) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					settingTimerLabel.setText("OFF");
					goldTimerLabel.setVisible(false);
					scarletTimerLabel.setVisible(false);
				} else {
					settingTimerLabel.setText("ON");
					goldTimerLabel.setVisible(true);
					scarletTimerLabel.setVisible(true);
				}
			}
		}
	}

	/**
	 * Inner class. It handles all ComponentEvents.
	 *
	 */
	class ComponentHandler extends ComponentAdapter {
		public void componentResized(ComponentEvent e) {
			if (gameState != DCConstants.INITIALISING) { 
				//set standard size of images
				Rectangle butBounds = panelArray[0].getButtonBounds();
				double bWidth = (double) butBounds.getWidth() - 6;
				double bHeight = (double) butBounds.getHeight() - 6;
				iconSet.setBounds(3,3, bWidth, bHeight);
			}
		}
	}

	/**
	 * Inner class. It handles all WindowEvents (like maximize, ...).
	 *
	 */
	class WindowStateHandler implements WindowStateListener {
		public void windowStateChanged(WindowEvent e) {
			if (getPreferences().getDebug2dGUI()) {
				System.out.println("windowStateChanged invoked");
			}
			resizeComponents();
			setIconBounds();
		}
	}

	/**
	 * Inner class. It handles the HierarchyBoundsEvents of mainPanel.
	 * Triggered by resizing the JFrame.
	 */
	class HierarchyBoundsHandler extends HierarchyBoundsAdapter {
		public void ancestorResized(HierarchyEvent e) {
			if (getPreferences().getDebug2dGUI()) {
				System.out.println("ancestorResized invoked.");
			}
			resizeComponents();
		}
	}
	
	/*
	 * CONSTRUCTORS
	 *
	 * public	DC2dGUI	(			)
	 *
	 */

	/**
	 * Class constructor. Creates the DC2dGUI, and initializes all its
	 * components.
	 */
	public DC2dGUI() {

		//initializations
		moveCountInt = 1;
		
		//create Event Handlers
		aHandler = new ActionHandler();
		wHandler = new WindowHandler();
		mHandler = new MouseHandler();
		iHandler = new ItemHandler();
		cHandler = new ComponentHandler();
		wsHandler = new WindowStateHandler();
		
		//create message encoder & decoder
		encoder = new DCFrontEndEncoder(this);
		decoder = new DCFrontEndDecoder(this);

		//create the users options class
		userOptions = new DCOptions();
		
		//initialize mainFrames
		mainFrame = new JFrame("Dragon Chess");
		
		//set size of mainFrame according to player preferences
		// + check for errors
		int sizeX = userOptions.getDefaultFrameX();
		int sizeY = userOptions.getDefaultFrameY();
		int sizeW = userOptions.getDefaultFrameWidth();
		int sizeH = userOptions.getDefaultFrameHeight();
		if ((sizeX < 0) || (sizeX > SCREEN_WIDTH)) { sizeX = 0; }
		if ((sizeY < 0) || (sizeY > SCREEN_HEIGHT)) { sizeY = 0; }
		if ((sizeW < 0) || (sizeW > SCREEN_WIDTH)) { sizeW = SCREEN_WIDTH; }
		if ((sizeH < 0) || (sizeH > SCREEN_HEIGHT)) { sizeH = SCREEN_HEIGHT; }
		mainFrame.setBounds(sizeX, sizeY, sizeW, sizeH);
		
		//initialize contentPane desktop
		desktop = new JDesktopPane();

		//initialize mainPanel
		mainGamePanel = new JPanel(new GridLayout(2,2));
		mainGamePanel.addHierarchyBoundsListener(new HierarchyBoundsHandler());
		mainGamePanel.setMinimumSize(new Dimension(100,100));
		
		// create CommunicationPanel
		commPanel = new DCCommunicationPanel(this);
				
		//create DCButtonBoards (Array)
		panelArray = new DCButtonBoard[BOARDS];

		//create borders
		panelEmptyBorder = BorderFactory.createEmptyBorder(5,5,5,5);
		panelRaisedBorder = BorderFactory.createRaisedBevelBorder();
		Border tempBorder = BorderFactory.createCompoundBorder(
														panelEmptyBorder, 
														panelRaisedBorder);
		panelCompoundBorder = BorderFactory.createCompoundBorder(
														tempBorder, 
														panelEmptyBorder);
		
		//create individual DCButtonBoards
		for (int i = 0; i < BOARDS; i++) {
			panelArray[i] = new DCButtonBoard(this,
						i,
						DARK_COLORS[i],
						LIGHT_COLORS[i]);
		}
		
		//create controlPanel
		createControlPanel();

		//create the iconSet (images of the pieces)
		try {
			String sep = File.separator;
			iconSet = new DCImageSet("gui2d" + sep + "svg" + sep 
															+ "piecelist.txt");
			iconSet.setColors(DCConstants.PLAYER_GOLD, GOLD, Color.black);
			iconSet.setColors(DCConstants.PLAYER_SCARLET, SCARLET, Color.black);
			iconSet.setAntiAlias(userOptions.getAntialiased());
		} catch(Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		// create the DCTimers
		goldTimer = new DCTimer(goldTimerLabel);
		scarletTimer = new DCTimer(scarletTimerLabel);

		//add DCButtonBoards & controlPanel to mainFrame
		mainGamePanel.add(panelArray[DCConstants.BOARD_TOP]);
		mainGamePanel.add(controlPanel);
		mainGamePanel.add(panelArray[DCConstants.BOARD_MIDDLE]);
		mainGamePanel.add(panelArray[DCConstants.BOARD_BOTTOM]);
		
		//add WindowListener to mainFrame
		mainFrame.addWindowListener(wHandler);
		mainFrame.addComponentListener(cHandler);
		mainFrame.addWindowStateListener(wsHandler);
		
		//set desktop as contentpane
		mainFrame.setContentPane(desktop);
		
		//create popup windows
		connPopup = new DCConnectionTypePopup(this);
		playerNamePopup = new DCPlayerNamePopup(this);
		anotherGamePopup = new DCAnotherGamePopup(this);
		playerPreferencesPopup = new DCPlayerPreferencesPopup(this);

		//add mainPanel and popup windows to desktop
		mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainGamePanel, commPanel);
		desktop.add(mainPanel);
		desktop.add(connPopup, JLayeredPane.POPUP_LAYER);
		desktop.add(playerNamePopup, JLayeredPane.POPUP_LAYER);
		desktop.add(anotherGamePopup, JLayeredPane.POPUP_LAYER);
		desktop.add(playerPreferencesPopup, JLayeredPane.POPUP_LAYER);
		
		//resize mainPanel to fill the background of the frame1
		mainPanel.setBounds(0,0,
							(int)desktop.getWidth(),
							(int)desktop.getHeight());
		
		mainPanel.setDividerLocation(0.90);

		//show frame
		mainFrame.show();

		//Show ConnectionTypePopup
		connPopup.showPopup();
		
	} 

	/*
	 * METHODS
	 *
	 * private	void			createControlPanel			(				)
	 * public	void			quitGame					(				)
	 * public 	void			newGame						(				)
	 * public	void			newGameStarted				(				)
	 * private	void			resignGame					(				)
	 * private	void			saveGame					(				)
	 * private	void			loadGame					(				)
	 * private	void			displayHelp					(				)
	 * private	void			offerDraw					(				)
	 * private	void			undoLastMove				(				)
	 * public	void			moveUndone					(DCMove			)
	 * public	void			moveNotUndone				(int			)
	 * public	void			pieceRestored				(DCExtCoord		)
	 * private	void			performSelectStartField		(DCImageButton	)
	 * public	void			pieceSelected				(DCCoord		)
	 * public	void			pieceNotSelected			(DCCoord, int	)
	 * public	void			showValidTargets			(DCMoveList		)
	 * private	void			performSelectTargetField	(DCImageButton	)
	 * public	void			pieceDeselected				(DCCoord,DCMoveList)
	 * public	void			pieceMoved					(DCMove			)
	 * public	void			pieceNotMoved				(DCCoord, int	)
	 * public	void			piecePromoted				(DCExtCoord		)
	 * public	void			pieceDemoted				(DCExtCoord		)
	 * public	void			refresh						(				)
	 * public	void			resizeComponents			(				)
	 * public	ActionHandler	getActionHandler			(				)
	 * public	MouseHandler	getMouseHandler				(				)
	 * public	void			sendMessage					(DCMessage		)
	 * public	void			registerConnection			(DCLocalConnection)
	 * public	void			sendOut						(DCMessage		)
	 * public	void			setupConnection				(int,String,int )
	 * private	void			displayMessage				(String			)
	 * private	String			getStatusMessage			(				)
	 * private	void			displayStatus				(String			)
	 * private	void			clearStatusbar				(				)
	 * private	void			clearStatusLabel			(				)
	 * public	void			registerPlayer				(String, int	)
	 * public	void			registerSuccess				(int,String,String)
	 * public	void			registerFailure				(				)
	 * public 	void			unregisterPlayer			(int			)
	 * public	void			playerUnregistered			(int,String		)
	 * public	void			newPlayerRegistered			(int,String,String)
	 * public	void			setPlayerInfo				(int,String,String)
	 * public	void			setGameState				(int			)
	 * public	void			loadDCFrontEndDump			(DCFrontEndDump	)
	 * public	void			setActivePlayer				(int			)
	 * private	void			clearBoards					(				)
	 * public	void			setIconBounds				(				)
	 * public	void			setFreezeStatus				(DCCoord, boolean)
	 * public 	void			setCheck					(int			)
	 * public	void			gameOver					(int,int		)
	 * public	void			addHistoryString			(String,int		)
	 * public	void			removeHistoryString			(int			)
	 * public	String			getGoldPlayerName			(				)
	 * public	String			getScarletPlayerName		(				)
	 * public	int				getPossibleActivePlayers	(				)
	 * public	int				getConnectionType			(				)
	 * public	int				getHeight					(				)
	 * public	int				getWidth					(				)
	 *
	 */

	/**
	 * Creates the control panel. This is taken out of the constructor to 
	 * simplify the code, and make the constructor clearer and easier to
	 * understand. This method is meant only to be called by the contructor.
	 * 
	 */
	private void createControlPanel() {
	
		//create controlPanel
		controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());
		controlPanel.setBorder(panelCompoundBorder);
		
		//initialize controlPanel contents
				
		//create cPanel
		cPanel = new JPanel(new BorderLayout());		
		
		//create + fill ncPanel
		ncPanel = new JPanel(new GridLayout(1,1));
		tabEmptyBorder = BorderFactory.createEmptyBorder(0,20,0,0);
		ncPanel.setBorder(tabEmptyBorder);
		scarletPlayerLabel = new JLabel("Scarlet Player", JLabel.CENTER);
		scarletPlayerLabel.setOpaque(true);
		scarletPlayerLabel.setForeground(SCARLET);
		ncPanel.add(scarletPlayerLabel);
		scarletPlayerLabel.setFont(NAME_FONT);

		//create ccPanel
		ccPanel = new JPanel(new BorderLayout());
		emptyBorder = BorderFactory.createEmptyBorder(10, 20, 10, 0);
		ccPanel.setBorder(emptyBorder);
		
		//create + fill nccPanel
		nccPanel = new JPanel(new GridLayout(1, 4));
		undoButton = new JButton("Undo Last Move");
		undoButton.addActionListener(aHandler);
		nccPanel.add(undoButton);
		
		//create + fill cccPanel
		cccPanel = new JPanel(new GridLayout(1, 2));
		goldListModel = new DefaultListModel();
		goldList = new JList(goldListModel);
		goldListScrollPane = new JScrollPane(goldList);
		goldListTitle = new JLabel("Gold Player History");
		goldListTitle.setHorizontalAlignment(JLabel.CENTER);
		goldListScrollPane.setColumnHeaderView(goldListTitle);
		goldList.setBackground(STANDARD_GRAY);
		scarletListModel = new DefaultListModel();
		scarletList = new JList(scarletListModel);
		scarletListScrollPane = new JScrollPane(scarletList);
		scarletListTitle = new JLabel("Scarlet Player History");
		scarletListTitle.setHorizontalAlignment(JLabel.CENTER);
		scarletListScrollPane.setColumnHeaderView(scarletListTitle);
		scarletList.setBackground(STANDARD_GRAY);
		cccPanel.add(goldListScrollPane);
		cccPanel.add(scarletListScrollPane);

		//fill ccPanel
		ccPanel.add(nccPanel, BorderLayout.SOUTH);
		ccPanel.add(cccPanel, BorderLayout.CENTER);
		
		//create + fill scPanel
		scPanel = new JPanel(new GridLayout(1,1));
		scPanel.setBorder(tabEmptyBorder);
		goldPlayerLabel = new JLabel("Gold Player");
		goldPlayerLabel.setHorizontalAlignment(JLabel.CENTER);
		goldPlayerLabel.setOpaque(true);
		goldPlayerLabel.setBackground(GOLD);
		scPanel.add(goldPlayerLabel);
		goldPlayerLabel.setFont(NAME_FONT);
		
		//fill cPanel
		cPanel.add(ncPanel, BorderLayout.NORTH);
		cPanel.add(ccPanel, BorderLayout.CENTER);
		cPanel.add(scPanel, BorderLayout.SOUTH);
		
		//create wPanel
		wPanel = new JPanel(new BorderLayout());		

		//create + fill nwPanel
		nwPanel = new JPanel(new GridLayout(10, 1));
		newGameButton = new JButton("New Game");
		newGameButton.addActionListener(aHandler);
		resignGameButton = new JButton("Resign Game");
		resignGameButton.addActionListener(aHandler);
		saveGameButton = new JButton("Save Game");
		saveGameButton.addActionListener(aHandler);
		loadGameButton = new JButton("Load Game");
		loadGameButton.addActionListener(aHandler);
		drawButton = new JButton("Offer Draw");
		drawButton.addActionListener(aHandler);
		prefButton = new JButton("Preferences");
		prefButton.addActionListener(aHandler);
		timerCheckBox = new JCheckBox("Set Timer");
		timerCheckBox.addItemListener(iHandler);
		settingTimerLabel = new JLabel();
		settingTimerLabel.setHorizontalAlignment(JLabel.CENTER);
		goldTimerLabel = new JLabel("", JLabel.CENTER);
		goldTimerLabel.setForeground(GOLD);
		goldTimerLabel.setVisible(false);
		scarletTimerLabel = new JLabel("", JLabel.CENTER);
		scarletTimerLabel.setForeground(SCARLET);
		scarletTimerLabel.setVisible(false);
		statusLabel = new JLabel("", JLabel.CENTER);
		nwPanel.add(newGameButton);
		nwPanel.add(resignGameButton);
		nwPanel.add(saveGameButton);
		nwPanel.add(loadGameButton);
		nwPanel.add(drawButton);
		nwPanel.add(prefButton);
		nwPanel.add(timerCheckBox);
		nwPanel.add(settingTimerLabel);
		//nwPanel.add(goldTimerLabel);
		//nwPanel.add(scarletTimerLabel);
		nwPanel.add(statusLabel);
		goldTimerLabel.setFont(goldTimerLabel.getFont().deriveFont(Font.BOLD));
		scarletTimerLabel.setFont(scarletTimerLabel.getFont().deriveFont(
																	Font.BOLD));
		statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD));
		
		//create + fill swPanel
		swPanel = new JPanel(new GridLayout(1, 2));
		quitGameButton = new JButton("Quit");
		quitGameButton.addActionListener(aHandler);
		helpButton = new JButton("Help");
		helpButton.addActionListener(aHandler);
		swPanel.add(quitGameButton);
		swPanel.add(helpButton);

		//fill wPanel
		wPanel.add(nwPanel, BorderLayout.CENTER);
		wPanel.add(swPanel, BorderLayout.SOUTH);

		//fill controlPanel
		controlPanel.add(wPanel, BorderLayout.WEST);
		controlPanel.add(cPanel, BorderLayout.CENTER);
	}

	/**
	 * Quits Dragonchess.
	 * 
	 */
	public void quitGame() {
		// If a game has been started, send out a resign game message
		if ((gameState != DCConstants.OVER) &&
			(gameState != DCConstants.INITIALISING)){
			resignGame();
		}
		
		// save the user preferences
		userOptions.save();
		
		System.exit(0);
	}

	/**
	 * Starts a new Dragonchess game.
	 * 
	 */
	public void newGame() {
		//send MSG_START_GAME to backend
		sendOut(encoder.startGame(activePlayer));
	}

	/**
	 * Clears the board, empties the Historylist, and sets the icon bounds.
	 *
	 */
	public void newGameStarted() {
		//clear history
		goldListModel.clear();
		scarletListModel.clear();
		
		//clear boards
		clearBoards();
		
		//set moves to one, because 1/2=0, 2/2=1, 3/2=1
		moveCountInt = 1;
					
		//resize icons
		setIconBounds();
		
		//disable timer switch
		//timerCheckBox.setEnabled(false);
		
		//start goldTimer
		//goldTimer.start();
	}

	/**
	 * Resigns current Dragonchess game.
	 * 
	 */
	private void resignGame() {
		sendOut(encoder.resignGame(activePlayer));
	}

	/**
	 * Saves current Dragonchess game.
	 * 
	 */
	private void saveGame() {
		//refDCGame.saveDCGame();
	}

	/**
	 * Loads a previous Dragonchess game.
	 * 
	 */
	private void loadGame() {
		//refDCGame.loadDCGame();
	}

	/**
	 * Displays help.
	 * 
	 */
	private void displayHelp() {
		DCHelpFrame myHelpFrame = new DCHelpFrame(SCREEN_WIDTH, SCREEN_HEIGHT);
	}

	/**
	 * Offer draw.
	 * 
	 */
	private void offerDraw() {
		/* Not Implemented Yet */
	}

	/**
	 * Tries to undo the last move.
	 * 
	 */
	private void undoLastMove() {
		switch(connectionType) {
		case DCConstants.CONN_LOCAL:
			sendOut(encoder.requestUndoMove(activePlayer));
			break;
		case DCConstants.CONN_SERVER:
		case DCConstants.CONN_CLIENT:
			sendOut(encoder.requestUndoMove(possibleActivePlayers));
			break;
		default:
			System.err.println("ERROR [undoLastMove()] - unknown connection " +
												"type");
		}
	} 

	/**
	 * The previous move is undone.
	 * @param move	the move to be undone
	 *
	 */
	public void moveUndone(DCMove move) {
		int moveType, sourceBoard, sourceFile, sourceRank,
		    targetBoard, targetFile, targetRank;
		char pieceType;
		DCImage2D image;
		String tooltip;
		
		moveType = move.getMoveType();
		pieceType = move.getPieceType();
		sourceBoard = move.getSource().getBoard();
		sourceFile = move.getSource().getFile();
		sourceRank = move.getSource().getRank();
		targetBoard = move.getTarget().getBoard();
		targetFile = move.getTarget().getFile();
		targetRank = move.getTarget().getRank();
			
		//check type of move
		switch (moveType) {
		case DCConstants.MOVE:
		case DCConstants.CAPT:
			image = panelArray[targetBoard].getButtonOccupyingPiece(
										targetRank, targetFile);
		 	tooltip = DCConstants.pieceName(pieceType);
			
			//remove piece from original position
			panelArray[targetBoard].clearButton(targetRank, targetFile);

			//set image + tooltip on new position
			panelArray[sourceBoard].setButtonOccupyingPiece(sourceRank,
										sourceFile, image);
			panelArray[sourceBoard].setButtonToolTip(sourceRank, sourceFile,
										tooltip);
			panelArray[sourceBoard].repaintButton(sourceRank, sourceFile);
			break;
		case DCConstants.CAFAR:
			/* Nothing to do */
			break;
		default:
			System.err.println("ERROR [pieceMoved()] - Unknown moveType...");
		}

	}

	/**
	 * The previous move is not undone.
	 * @param reason	why it is not undone
	 *
	 */
	public void moveNotUndone(int reason) {
		switch (reason) {
		case DCConstants.UNDO_HISTORY_EMPTY:
			displayMessage("Could not undo move: History is empty !");
			break;
		case DCConstants.UNDO_REFUSED:
			displayMessage("Could not undo move: Opponent refused it !");
			break;
		default:
			System.err.println("ERROR [moveNotUndone()] - Unknown reason...");
		}	
	}
	
	/**
	 * The piece is restored to the board after being captured.
	 * @param location	the location at which the piece is restored
	 *
	 */
	public void pieceRestored(DCExtCoord location) {
		int board = location.getBoard();
		int file = location.getFile();
		int rank = location.getRank();
		int player = location.getPlayer();
		char type = location.getPieceType();
		DCImage2D image = iconSet.getImage(player, type);
		String name = DCConstants.pieceName(type);
		
		/* DEBUG
		System.err.println("--(" + board + "," + file + "," + rank + 
											")-- " + type + ": " + name); */
			
		//set button image
		panelArray[board].setButtonOccupyingPiece(rank, file, image);
		//set button tooltip
		panelArray[board].setButtonToolTip(rank, file, name);

		//repaint button				
		panelArray[board].repaintButton(rank, file);

		//FIXME the restored piece should be moved from a captboard instead
		
	}
	
	/**
	 * Sends out a MSG_SELECT_PIECE with the coordinates of the selected piece
	 * @param myButton	the DCImageButton that was clicked on by the user
	 *
	 */
	private void performSelectStartField(DCImageButton myButton) {

		try {
			DCCoord selectedField = new DCCoord(myButton.getBoard(),
											myButton.getFile(),
											myButton.getRank());
			sendOut(encoder.selectPiece(activePlayer, selectedField));
		} catch (DCLocationException e) {
			System.err.println("DCLocationExeption in " + 
							"performSelectStartField() : " + e.getMessage());
			displayMessage("ERROR [performSelectStartField()] - " + 
													"Invalid startfield!");
		}
	}

	/**
	 * Selection of the piece has succeeded, update highlighting of the 
	 * location.
	 * @param	location	the DCCoord containing the location of the 
	 * 						selected button.
	 */
	public void pieceSelected(DCCoord location) {
		int board = location.getBoard();
		int file = location.getFile();
		int rank = location.getRank();
		panelArray[board].setButtonHighlighted(rank, file, true);
	}

	/**
	 * Selection of the piece has failed, update highlighting of the location.
	 * @param	location	the DCCoord containing the location of the 
	 * 						selected button.
	 * @param	reason		why the piece hasn't been selected
	 */
	public void pieceNotSelected(DCCoord location, int reason) {
		int board = location.getBoard();
		int file = location.getFile();
		int rank = location.getRank();
		panelArray[board].setButtonHighlighted(rank, file, false);

		switch (reason) {
		case DCConstants.SUB_INVALID_PLAYER:
			displayMessage("Piece could not be selected: invalid player !");
			break;
		case DCConstants.SUB_INVALID_GAMESTATE:
			displayMessage("Piece could not be selected: invalid gamestate !");
			break;
		case DCConstants.SUB_INVALID_LOCATION:
			displayMessage("Piece could not be selected: invalid location !");
			break;
		case DCConstants.SUB_KING_IN_CHECK:
			displayMessage("Piece could not be selected: king is in check !");
			break;
		case DCConstants.SUB_PIECE_FROZEN:
			displayMessage("Piece could not be selected: it is frozen !");
			break;
		default:
		}
	}

	/**
	 * Shows the valid targets to which the selected piece can move, captured or
	 * capture from afar.
	 *
	 * @param	targetList	the list of valid targets
	 */
	public void showValidTargets(DCMoveList targetList) {

		for (int i = 0; i < targetList.size(); i++) {
			DCMove element = targetList.get(i);

			int board = (element.getTarget()).getBoard();
			int file = (element.getTarget()).getFile();
			int rank = (element.getTarget()).getRank();
			int type = element.getMoveType();

			
			/* DEBUG 
			System.err.println("--(" + board + "," + file + "," + rank + 
												")-- " + type); */
			
			panelArray[board].highlightButton(rank, file, type);
		}
	
	}
	
	/**
	 * Tries to select a target field to move to.
	 * @param myButton	the DCImageButton that was clicked on by the user
	 * 
	 */
	private void performSelectTargetField(DCImageButton myButton) {

		try {
			DCCoord selectedField = new DCCoord(myButton.getBoard(),
											myButton.getFile(),
											myButton.getRank());
			sendOut(encoder.movePiece(activePlayer, selectedField));
		} catch (DCLocationException e) {
			System.err.println("DCLocationExeption in " + 
							"performSelectStartField() : " + e.getMessage());
			displayMessage("ERROR [performSelectTargetField()] - " + 
														"Invalid startfield!");
		}
	}

	/**
	 * The piece is deselected, the highlights are undone.
	 *
	 * @param	location	the location of the deselected piece.
	 * @param	list		the list of highlights to be undone.
	 */
	public void pieceDeselected(DCCoord location, DCMoveList list) {
		int board, file, rank;
			
		// Unhighlight piece
		board = location.getBoard();
		file = location.getFile();
		rank = location.getRank();
		panelArray[board].setButtonHighlighted(rank, file, false);

		// Unhighlight targets
		for (int i = 0; i < list.size(); i++) {
			DCMove element = list.get(i);

			board = (element.getTarget()).getBoard();
			file = (element.getTarget()).getFile();
			rank = (element.getTarget()).getRank();
			
			/* DEBUG 
			System.err.println("--(" + board + "," + file + "," + rank + 
												")-- " + type); */
			
			panelArray[board].unHighlightButton(rank, file);
		}
	
		
	}

	/**
	 * The piece is moved, possibly capturing another piece.
	 *
	 * @param	move		the DCMove describing the action
	 */
	public void pieceMoved(DCMove move) {
		int player, moveType, sourceBoard, sourceFile, sourceRank,
		    targetBoard, targetFile, targetRank;
		char pieceType;
		DCImage2D image;
		String tooltip;
		
		player = move.getPlayer();
		moveType = move.getMoveType();
		pieceType = move.getPieceType();
		sourceBoard = move.getSource().getBoard();
		sourceFile = move.getSource().getFile();
		sourceRank = move.getSource().getRank();
		targetBoard = move.getTarget().getBoard();
		targetFile = move.getTarget().getFile();
		targetRank = move.getTarget().getRank();
			
		//check type of move
		switch (moveType) {
		case DCConstants.MOVE:
			image = panelArray[sourceBoard].getButtonOccupyingPiece(
										sourceRank, sourceFile);
		 	tooltip = DCConstants.pieceName(pieceType);
			
			//remove piece from original position
			panelArray[sourceBoard].clearButton(sourceRank, sourceFile);

			//set image + tooltip on new position
			panelArray[targetBoard].setButtonOccupyingPiece(targetRank,
										targetFile, image);
			panelArray[targetBoard].setButtonToolTip(targetRank, targetFile,
										tooltip);
			panelArray[targetBoard].repaintButton(targetRank,targetFile);			
			break;
		case DCConstants.CAPT:
			image = panelArray[sourceBoard].getButtonOccupyingPiece(
										sourceRank, sourceFile);
		 	tooltip = DCConstants.pieceName(pieceType);
			
			//remove piece from original position
			panelArray[sourceBoard].clearButton(sourceRank, sourceFile);

			//remove piece from target position (the captured piece)
			//FIXME: the piece should be moved to a captboard
			panelArray[targetBoard].clearButton(targetRank, targetFile);
			
			//set image + tooltip on new position
			panelArray[targetBoard].setButtonOccupyingPiece(targetRank,
										targetFile, image);
			panelArray[targetBoard].setButtonToolTip(targetRank, targetFile,
										tooltip);
			panelArray[targetBoard].repaintButton(targetRank,targetFile);
			break;
		case DCConstants.CAFAR:
			//remove piece from target position (the captured piece)
			//FIXME: the piece should be moved to a captboard
			panelArray[targetBoard].clearButton(targetRank, targetFile);
			panelArray[targetBoard].repaintButton(targetRank, targetFile);			
			break;
		default:
			System.err.println("ERROR [pieceMoved()] - Unknown moveType...");
			break;
		}
	}

	/**
	 * The piece is not moved, the gamestate stays the same.
	 *
	 * @param	location	the location of the piece
	 * @param	reason		why the button hasn't been moved
	 */
	public void pieceNotMoved(DCCoord location, int reason) {
		
		switch(reason) {
		case DCConstants.SUB_INVALID_PLAYER:
			displayMessage("Could not move piece: Invalid Player!");
			break;
		case DCConstants.SUB_INVALID_GAMESTATE:
			displayMessage("Could not move piece: Invalid Gamestate!");
			break;
		case DCConstants.SUB_INVALID_LOCATION:
			displayMessage("Could not move piece: Invalid Location!");
			break;
		case DCConstants.SUB_KING_IN_CHECK:
			displayMessage("Could not move piece: King in check!");
			break;
		default:
			System.err.println("ERROR [pieceNotMoved()] - Unknown reason...");
		}
	}

	/**
	 * The piece is promoted to another type.
	 * @param	location	the location at which the piece is promoted
	 *
	 */
	public void piecePromoted(DCExtCoord location) {
		int board = location.getBoard();
		int file = location.getFile();
		int rank = location.getRank();
		int player = location.getPlayer();
		char type = location.getPieceType();
		DCImage2D image = iconSet.getImage(player, type);
		String name = DCConstants.pieceName(type);
		
		/* DEBUG
		System.err.println("--(" + board + "," + file + "," + rank + 
											")-- " + type + ": " + name); */
			
		//set button image
		panelArray[board].setButtonOccupyingPiece(rank, file, image);
		//set button tooltip
		panelArray[board].setButtonToolTip(rank, file, name);

		//repaint button				
		panelArray[board].repaintButton(rank, file);	
	}

	/**
	 * The piece is demoted after the move is undone.
	 * @param	location	the location at which the piece is demoted
	 *
	 */
	public void pieceDemoted(DCExtCoord location) {
		piecePromoted(location);
	}
	
	/**
	 * Paints the Gui.
	 *
	 */
	public void refresh() {

		if (getPreferences().getDebug2dGUI()) {
			System.out.println("DC2dGUI.refresh invoked.");
		}

		for (int i=0; i < BOARDS; i++) {
			//panelArray[i].repaint();
			panelArray[i].refresh();
		}
	}

	/**
	 * Resizes all components on the JDesktopPane.
	 *
	 */
	public void resizeComponents() {
			if (getPreferences().getDebug2dGUI()) {
				System.out.println("resizeComponents invoked.");
			}
			mainPanel.validate();
			//resize mainPanel to fill the background of the frame
			mainPanel.setBounds(0,0,
							(int)desktop.getWidth(),
							(int)desktop.getHeight());
			mainPanel.setDividerLocation(0.90);
			//move the popup's to the center
            connPopup.setLocation((desktop.getWidth() - connPopup.getWidth())/2,
							(desktop.getHeight() - connPopup.getHeight())/2);
			
            playerNamePopup.setLocation((desktop.getWidth() - 
											playerNamePopup.getWidth())/2,
										(desktop.getHeight() - 
								 			playerNamePopup.getHeight())/2);
			anotherGamePopup.setLocation((desktop.getWidth() - 
											anotherGamePopup.getWidth())/2,
										 (desktop.getHeight() -
									  		anotherGamePopup.getHeight())/2);
			playerPreferencesPopup.setLocation((desktop.getWidth() -
										playerPreferencesPopup.getWidth())/2,
											   (desktop.getHeight() - 
										playerPreferencesPopup.getHeight())/2);
			
	}

	/**
	 * Returns the reference of the ActionHandler defined in DC2dGUI.
	 * @return	the ActionHandler of the DC2dGUI
	 *
	 */
	public ActionHandler getActionHandler() {
		return aHandler;
	}

	/**
	 * Returns the reference of the MouseHandler defined in DC2dGUI.
	 * @return	the MouseHandler of the DC2dGUI
	 *
	 */
	public MouseHandler getMouseHandler() {
		return mHandler;
	}
	
	/**
	 * Accepts a message coming from the backend.
	 *
	 * @param msg	the message that is sent.
	 */
	public void sendMessage(DCMessage msg) {
		final DCMessage message = msg;
		Runnable decodeMessage = new Runnable() {
			public void run() {
				decoder.decodeFrontEndMessage(message);	
			}
		};
		try {
			SwingUtilities.invokeLater(decodeMessage);
		} catch (Exception e) {}
	}

	/**
	 * Registers a DCLocalConnection, to send the outgoing messages to
	 *
	 * @param connection	the DCLocalConnection
	 */
	public void registerConnection(DCLocalConnection connection) {
		this.connection = connection;	
	}

	/**
	 * Sends a message to the DCGame. Takes an allready encoded message.
	 *
	 * @param message	message to send out
	 */
	public void sendOut(DCMessage message) {
		if (connection != null) {
			connection.sendMessage(message);
		} else {
			System.err.println("ERROR [sendOut()] - Sending DCMessage " + 
											"over non-existing connection!");
			displayMessage("ERROR [sendOut()] - Sending DCMessage over " + 
												"non-existing connection!");
		}
	}

	/**
	 * Sets the connection information and creates the DCGame. It then proceeds
	 * by displaying a popup to enter the player information.
	 *
	 * @param connType		the type of connection.
	 * @param serverString	the string specifying the server information.
	 * @param port			integer specifying the port number to be used.
	 */
	public void setupConnection(int connType, String serverString, int port) {
		boolean generatedError = false;
		
		this.connectionType = connType;
		this.serverString = serverString;
		this.port = port;
		switch (connectionType) {
		case DCConstants.CONN_LOCAL:
			/* DEBUG OUTPUT */
			if (getPreferences().getDebug2dGUI()) {
				System.err.println("*** LOCAL CONNECTION ***");
				displayMessage("*** LOCAL CONNECTION ***");
			}
			refDCGame = new DCGameLocal(this);

			/* disable the chat system */
			commPanel.setChatEnabled(false);
			break;
		case DCConstants.CONN_SERVER:
			/* DEBUG OUTPUT */
			if (getPreferences().getDebug2dGUI()) {
				System.err.println("*** NETWORK CONNECTION: SERVER ***");
				System.err.println("Description: " + serverString);
				System.err.println("Port: " + port);
				displayMessage("*** NETWORK CONNECTION: SERVER[Desc=" + serverString
							+ ";Port=" + port + "] ***");
			}
			
			try {
				refDCGame = new DCGameServer(this, port);
			} catch (DCNetworkException e) {
				generatedError = true;
				//check which message it is
				switch (e.getReason()) {
					case DCConstants.NW_SERVERSOCKET_NOT_CREATED :
						displayMessage("Error creating server on port " + port +
								". Is there another service using this port ?");
						break;
					default :
						System.err.println("Error creating server : Unknown error");
				}
			}


			
			break;
		case DCConstants.CONN_CLIENT:
			/* DEBUG OUTPUT */
			if (getPreferences().getDebug2dGUI()) {
				System.err.println("*** NETWORK CONNECTION: CLIENT ***");
				System.err.println("Server: " + serverString);
				System.err.println("Port: " + port);
				displayMessage("*** NETWORK CONNECTION: CLIENT[Desc=" + serverString
							+ ";Port=" + port + "] ***");
			}
			
			// handle exceptions if connection to DCGameServer
			// can't be created (see DCConstants for reasons)
			try {
				refDCGame = new DCGameClient(this, serverString, port);
			} catch (DCNetworkException e) {
				generatedError = true;
				switch (e.getReason()) {
					case DCConstants.NW_HOST_NOT_FOUND :
						displayMessage("Error connecting to " +
								e.getMessage() + " : Host not found ");
						break;
					case DCConstants.NW_SOCKET_NOT_CREATED :
						displayMessage("Error connecting to " +
								e.getMessage() + " : Connection refused ");
						break;
					case DCConstants.NW_STREAMS_NOT_CREATED :
						displayMessage("Error connecting to " +
								e.getMessage() + " : Streams couldn't be created ");
						break;
					default :
						System.err.println("Error connecting to backend : unknown error");
				}
			}
				
			break;
		}

		/* show player name popup if there were no errors, else reshow connectionPopup */
		if (!generatedError) {
			playerNamePopup.setConnectionType(connectionType);
			playerNamePopup.showPopup();
			gotConnection = true;
		} else {
			connPopup.showPopup();
		}
	}
	
	/**
	 * Displays a message on the statusbar
	 *
	 * @param msgString		the message to be displayed
	 */
	private void displayMessage(String msgString) {
		lastMessage = msgString;
		commPanel.systemMessage(msgString);
		//messageLabel.setText(msgString);
	}

	/**
	 * Displays a message an the status label
	 *
	 * @param msgString		the message to be displayed
	 */
	private void displayStatus(String msgString) {
		statusLabel.setText(msgString);
	}
	
	/**
	 * Clears the statusbar
	 *
	 */
	private void clearStatusbar() {
		displayMessage("");
	}

	/**
	 * Clears the status label
	 *
	 */
	private void clearStatusLabel() {
		displayStatus("");
	}

	/**
	 * Sets the player information and sends the register messages to the
	 * backend.
	 *
	 * @param nameString	the name of the player
	 * @param prefPlayerInt	the player he wants to be (GOLD or SCARLET)
	 */
	public void registerPlayer(String nameString, int prefPlayerInt) {
		
		if (getPreferences().getDebug2dGUI()) {
			System.err.println("*** REGISTER PLAYER[" + nameString
							+ "] ADDRESS: " + serverString + 
							" PREFERRED PLAYERINT: " + prefPlayerInt + " ***");
		}
				
		displayMessage("REGISTER PLAYER[" + nameString
						+ "] ADDRESS: " + serverString + 
						" PREFERRED PLAYERINT: " + prefPlayerInt);
						
		sendOut(encoder.registerPlayer(nameString, serverString, 
														prefPlayerInt));
	}
	
	/**
	 * Player registration has been successful
	 *
	 */
	public void registerSuccess(int playerInt, String name, String address) {
		
		/* Set which players can become active */
		switch(connectionType) {
		case DCConstants.CONN_LOCAL:
			possibleActivePlayers = DCConstants.PLAYER_BOTH;
			
			break;
		default:
			possibleActivePlayers = playerInt;
		}
		
		/* DEBUG OUTPUT */
		//System.err.println("Possible Active Players: " + possibleActivePlayers);
	}

	/**
	 * Player registration has failed
	 *
	 */
	public void registerFailure() {
		displayMessage("Registration has failed!");
	}

	/**
	 * Unregister de player.
	 * @param playerInt		which player to unregister.
	 *
	 */
	public void unregisterPlayer(int playerInt) {
		//System.err.println("****** UNREGISTER PLAYER " + playerInt + " ***");
		displayMessage("*** UNREGISTER PLAYER " + playerInt + " ***");
		sendOut(encoder.unregisterPlayer(playerInt));
	}
	
	/**
	 * Player unregistered.
	 * @param player		which player is unregistered
	 * @param reason		why he is unregistered
	 */
	public void playerUnregistered(int player, String reason) {
		switch(player) {
		case DCConstants.PLAYER_GOLD:
			displayMessage(goldPlayerLabel.getText() + " has unregistered.");
			goldPlayerLabel.setText("Gold Player");
			break;
		case DCConstants.PLAYER_SCARLET:
			displayMessage(scarletPlayerLabel.getText() + " has unregistered.");
			scarletPlayerLabel.setText("Scarlet Player");
			break;
		default:
			System.err.println("ERROR [playerUnregistered] - unknown player");
		}
	}
	
	/**
	 * Set player info for newly registered player
	 *
	 */
	public void newPlayerRegistered(int playerInt, String name, 
														String address) {
		/* update the player info on screen */
		switch(playerInt) {
		case DCConstants.PLAYER_GOLD:	
			goldPlayerLabel.setText(name);
			break;
		case DCConstants.PLAYER_SCARLET:
			scarletPlayerLabel.setText(name);
			break;
		}
	}
	
	/**
	 * Set player info for previously registered player
	 *
	 */
	public void setPlayerInfo(int playerInt, String name, String address) {
		newPlayerRegistered(playerInt, name, address);
	}

	/**
	 * Set the game state
	 *
	 */
	public void setGameState(int state) {
		this.gameState = state;
		/* DEBUG OUTPUT */
		if (getPreferences().getDebug2dGUI()) {
			System.err.println("New Gamestate: " + gameState);
		}
	}

	/**
	 * Load a DCFrontEndDump. This dump contains list of the location of pieces
	 * on the board and in the garbage bin
	 *
	 */
	public void loadDCFrontEndDump(DCFrontEndDump dump) {

		/* DEBUG */
		if (getPreferences().getDebug2dGUI()) {
			dump.debugPrint();
		}

		//set icon bounds
		setIconBounds();
		
		/* show the images on the board */
		DCExtCoordList boardList = dump.getBoardPieces();
		for (int i = 0; i < boardList.size(); i++) {
			DCExtCoord element = boardList.get(i);

			int board = element.getBoard();
			int file = element.getFile();
			int rank = element.getRank();
			int player = element.getPlayer();
			char type = element.getPieceType();
			DCImage2D image = iconSet.getImage(player, type);
			boolean frozen = element.isFrozen();
			String name = DCConstants.pieceName(type);
		
			/* DEBUG
			System.err.println("--(" + board + "," + file + "," + rank + 
												")-- " + type + ": " + name); */
			
			//set button image
			panelArray[board].setButtonOccupyingPiece(rank, file, image);
			//set button tooltip
			panelArray[board].setButtonToolTip(rank, file, name);
			//set if button is frozen
			panelArray[board].setButtonFrozen(rank, file, frozen);

			//repaint button				
			panelArray[board].repaintButton(rank, file);
		}

		// Garbage List isn't used at the moment
	
	}

	/**
	 * Gets the active player
	 */
	public int getActivePlayer() {
			return activePlayer;
	}
	
	/**
	 * Sets the active player
	 *
	 */
	public void setActivePlayer(int player) {
		switch (possibleActivePlayers) {
		case DCConstants.PLAYER_BOTH:
			activePlayer = player;
			break;
		default:
			if (possibleActivePlayers == player) {
				activePlayer = player;
			} else {
				activePlayer = DCConstants.PLAYER_NONE;
			}
		}

		switch (player) {
		case DCConstants.PLAYER_SCARLET:
			//change colors of player labels
			goldPlayerLabel.setBackground(STANDARD_GRAY);
			goldPlayerLabel.setForeground(GOLD);
			scarletPlayerLabel.setForeground(Color.white);
			scarletPlayerLabel.setBackground(SCARLET);
			
			//stop gold timer, start scarlet timer
			//goldTimer.stop();
			//scarletTimer.start();
			break;
		case DCConstants.PLAYER_GOLD:
			//change colors of player labels
			goldPlayerLabel.setBackground(GOLD);
			goldPlayerLabel.setForeground(Color.black);
			scarletPlayerLabel.setForeground(SCARLET);
			scarletPlayerLabel.setBackground(STANDARD_GRAY);
			
			//stop scarlet timer, start gold timer
			//scarletTimer.stop();
			//goldTimer.start();
			break;
		default:
			System.err.println("ERROR [setActivePlayer()] - Invalid player");
		} 

		/* DEBUG OUTPUT */
		//System.err.println("New Active Player: " + activePlayer);
	}

	/**
	 * Clears all boards.
	 * 
	 */
	private void clearBoards() {
		for (int i=0; i < DCConstants.BOARDS; i++) {
			panelArray[i].clearBoard();
		}
	}

	/**
	 * Matches the size of the icons to the size of the buttons
	 * 
	 */
	public void setIconBounds() {
			
		//set standard size of images
		Rectangle butBounds = panelArray[0].getButtonBounds();
		double bWidth = (double) butBounds.getWidth() - 6;
		double bHeight = (double) butBounds.getHeight() - 6;
		iconSet.setBounds(3,3, bWidth, bHeight);
		
	}

	/**
	 * Sets the freeze status of a specified location
	 * @param	location	DCCoord with piece to set status for
	 * @param	frozen		boolean with frozen status
	 */
	public void setFreezeStatus(DCCoord location, boolean frozen) {
		int board = location.getBoard();
		int rank = location.getRank();
		int file = location.getFile();
		panelArray[board].setButtonFrozen(rank,file,frozen);
	}

	/**
	 * The previous move has caused a check condition.
	 * @param	player		the player that is in check condition
	 *
	 */
	public void setCheck(int player) {
		displayStatus("CHECK !");

		switch(player) {
		case DCConstants.PLAYER_GOLD:
			displayMessage(goldPlayerLabel.getText() + 
											" is in check condition!");
			break;
		case DCConstants.PLAYER_SCARLET:
			displayMessage(scarletPlayerLabel.getText() + 
											" is in check condition!");
			break;
		default:
			System.err.println("ERROR : invalid player in setCheck()");
		}
	}

	/**
	 * The game is over, due to a checkmate, mate or draw.
	 * @param	reason		why the game is over (checkmate,mate,...)
	 * @param	winner		which player wins
	 */
	public void gameOver(int reason, int winner) {
		String msg = new String("");

		switch (winner) {
		case DCConstants.PLAYER_GOLD:
			msg = goldPlayerLabel.getText() + " wins ! " + 
											scarletPlayerLabel.getText();
			break;
		case DCConstants.PLAYER_SCARLET:
			msg = scarletPlayerLabel.getText() + " wins ! " + 
											goldPlayerLabel.getText();
			break;
		case DCConstants.PLAYER_BOTH:
			msg = "Game over. It's a draw!";
		default:
			System.err.println("ERROR : invalid player in gameOver()");
		}
		
		switch (reason) {
		case DCConstants.GAMEOVER_RESIGNED:
			msg += " resigned.";
			break;
		case DCConstants.GAMEOVER_CHECKMATE:
			displayStatus("CHECKMATE !");
			msg += " is in checkmate condition.";
			break;
		case DCConstants.GAMEOVER_MATE:
			displayStatus("MATE !");	
			msg += " There's a mate condition.";
			break;
		case DCConstants.GAMEOVER_CONNECTION_BROKEN:
			msg = "Connection with " + getPlayerName(winner) + " broken.";
			gotConnection = false;
			clearBoards();
		}

		displayMessage(msg);
		
		//show anotherGamePopup
		anotherGamePopup.setMessage(msg);
		anotherGamePopup.setVisible(true);
	}

	/**
	 * Add a line in the history list.
	 * @param	moveString	the string representation of the previous move
	 * @param	player		the player who executed the move
	 */
	public void addHistoryString(String moveString, int player) {
		
		moveCountInt += 1;
		switch (player) {
		case DCConstants.PLAYER_GOLD:
			goldListModel.addElement(((int) (moveCountInt / 2)) 
											+ ". " 
											+ moveString);
					
			goldList.setSelectedIndex(goldListModel.getSize() - 1);
			goldList.ensureIndexIsVisible(goldListModel.getSize() - 1);
			break;
		case DCConstants.PLAYER_SCARLET:
			scarletListModel.addElement(((int) (moveCountInt / 2)) 
											+ ". " 
											+ moveString);
			
			scarletList.setSelectedIndex(scarletListModel.getSize() -1);
			scarletList.ensureIndexIsVisible(
										scarletListModel.getSize() - 1);
			break;
		default:
			System.err.println("ERROR [addHistoryString()] - Invalid player");
		}

	}

	/**
	 * Remove the last line of a given player in the history list.
	 * @param	player		the player whose historyline should be removed
	 *
	 */
	public void removeHistoryString(int player) {
		
		moveCountInt -= 1;
		switch (player) {
		case DCConstants.PLAYER_GOLD:
			goldListModel.removeElementAt(goldListModel.getSize() - 1);
					
			goldList.setSelectedIndex(goldListModel.getSize() - 1);
			goldList.ensureIndexIsVisible(goldListModel.getSize() - 1);
			break;
		case DCConstants.PLAYER_SCARLET:
			scarletListModel.removeElementAt(scarletListModel.getSize() - 1);
			
			scarletList.setSelectedIndex(scarletListModel.getSize() -1);
			scarletList.ensureIndexIsVisible(
										scarletListModel.getSize() - 1);
			break;
		default:
			System.err.println("ERROR [addHistoryString()] - Invalid player");
		}

	}

	/**
	 * Gets the gold player name
	 * @return	gold player name
	 */
	public String getGoldPlayerName() {
		return goldPlayerLabel.getText();
	}

	/**
	 * Get the scarlet player name
	 * @return scarlet player name
	 */
	public String getScarletPlayerName() {
		return scarletPlayerLabel.getText();
	}
	
	/**
	 * Returns the name corresponding with the given playerInt
	 * @param player	the playerInt of the player whose name you want
	 * @return String	the name of that player
	 */
	public String getPlayerName(int player) {
		String value = new String("");
		switch(player) {
			case DCConstants.PLAYER_GOLD:
				value = getGoldPlayerName();
				break;
			case DCConstants.PLAYER_SCARLET:
				value = getScarletPlayerName();
				break;
			default:
				System.out.println("ERROR[getPlayerName()] - Unknown player!");
				value = "*UNKNOWN*";
		}
		return value;
	}

	/**
	 * Get the possible active players
	 * @return	the possible active players
	 */
	public int getPossibleActivePlayers() {
		return possibleActivePlayers;
	}

	/**
	 * Get the connection type
	 * @return	the connection type
	 */
	public int getConnectionType() {
		return connectionType;
	}

	/**
	 * Get the height of the main frame
	 * @return	height
	 */
	public int getHeight() {
		return mainFrame.getHeight();
	}

	/**
	 * Get the width of the main frame
	 * @return 	width
	 */
	public int getWidth() {
		return mainFrame.getWidth();
	}

	/**
	 * Get the current user preferences
	 * @return	the current user preferences
	 */
	public DCOptions getPreferences() {
		return userOptions;
	}

	/**
	 * indicates that there has been a failed connection attempt to the DCGame
	 * this gui has set up. This method is only invoked for gui's that have set
	 * up a DCServerGame.
	 *
	 * <p>A failed connection attempt like the one that triggers this method
	 * has no impact on the game. It is for informational purposes only.
	 *
	 * @param reason	int with the reason for the failure, as defined in
	 * {@link DCConstants}
	 * @param address	String representing the hostname of the address from
	 * which the connection attempt was made
	 */
	public void connectionFailed(final int reason, final String address) {
		final String reasonString;
		switch (reason) {
			case DCConstants.NW_STREAMS_NOT_CREATED :
				reasonString = "Streams not created";
				break;
			case DCConstants.NW_SOCKET_NOT_CREATED :
				reasonString = "Error creating socket";
				break;
			default :
				reasonString = "Unknown error";
		}

		Runnable display = new Runnable() {
			public void run() {
				displayMessage("Failed connection attempt from address [" + address +
						"] : " + reasonString);
			}
		};
		try {
			SwingUtilities.invokeLater(display);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * indicates that the connection with the backend has been broken. This
	 * method is only called by DCGame instances that use a networked backend
	 * (local backends should never be disconnected).
	 * @param	reason	int with reason for disconnection (defined in {@link
	 * DCConstants})
	 */
	public void backendConnectionBroken(int reason) {
		final String reasonString;
		
		//notify the gui that there's no longer a connection
		gotConnection = false;
		
		switch (reason) {
			case DCConstants.NW_INPUT_STREAM_ERROR :
				reasonString = "Error reading from input stream";
				break;
			default : 
				reasonString = "Unknown error";
		}

		Runnable handleError = new Runnable() {
			public void run() {
				displayMessage("Connection to server broken : " + reasonString);
		
				if (gameState != DCConstants.OVER) {
					// if the server has intentionally broken the connection (ie he pressed 
					// the quit button), a resignGame message is sent and the gameState
					// is set to OVER. Then the AnotherGame popup is shown, so we don't
					// have to show the connection type popup (yet).
					//sendOut(encoder.resignGame(possibleActivePlayers));
					clearBoards();
					anotherGamePopup.setMessage("Connection to server lost.");
					anotherGamePopup.setVisible(true);
				}
			}
		};
		try {
			SwingUtilities.invokeLater(handleError);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Get encoder.
	 *
	 * @return encoder as DCFrontEndEncoder.
	 */
	public DCFrontEndEncoder getEncoder()
	{
	    return encoder;
	}
	
	/** 
	 * Receive the chat msg and update the window.
	 *
	 * @param privateBoolean	whether or not this is a private message
	 * @param playerName		name of the player sending this message
	 * @param msg 				String with the message 
	 */
	public void communicationReceive(boolean privateBoolean, String playerName,
			String msg ) {
		commPanel.playerMessage(privateBoolean, playerName, msg);
	}
	
	/**
	 * Executed after the user preferences are changed. This function
	 * dynamically sets the options in the respective classes.
	 */
	public void executeUserOptions() {
		iconSet.setAntiAlias(userOptions.getAntialiased());
	}
	
	/**
	 * Returns if the gui still has a valid connection to the server or a client
	 * @return boolean	
	 */
	public boolean hasConnection() {
		return gotConnection;
	}	
}
