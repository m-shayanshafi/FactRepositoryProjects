package thaigo.view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import thaigo.object.Board;
import thaigo.object.BoardModel;
import thaigo.object.PawnModel;
import thaigo.property.AbstractRuler;
import thaigo.property.ThaiGORuler;
import thaigo.utility.AudioPlayer;
import thaigo.utility.CenterDeterminer;
import thaigo.utility.ImageLoader;
import thaigo.utility.PropertyManager;
/**
 * User interface of game window.
 * 
 * @author Nol Pasurapunya & TG_Dream_Team
 * @version 2013.4.21
 *
 */
public class GameUI extends JFrame {
	public static final int HOST = 0;
	public static final int CLIENT = 1;
	
	public static final int PLAY = 10;
	public static final int STOP = 20;
	
	private JMenuBar menuBar;
	private JMenu game, help;
	private JMenu colorSet, modelSet;
	private JMenuItem rule, about, secret, connect;
	private JPanel west, board, east, gameInfo;
	private PlayerPanel yourPanel, foePanel;
	private ChatBoard chatboard;
	private Board gameboard;
	private JLabel foeName, yourName;
	
	private ColorTextPane logs;
	private JScrollPane logPane;
	
	private ButtonGroup colorGroup, modelGroup;

	private AbstractRuler thaiGORuler;
	private static GameUI instance;
	private AudioPlayer moveSound;
	private AudioPlayer slideSound;
	
	
	/** Get the instance of this object if it's already declared. Otherwise creates a new one.
	 * 
	 * @return instance of this object
	 */
	public static GameUI getInstance(){
		if(instance == null)
			instance = new GameUI();
		return instance;
	}

	/** Initializes the GameUI. */
	private GameUI() {
		super("Thai GO!");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		ImageLoader icon = new ImageLoader("images/icon.png");
		this.setIconImage(icon.getImage());
		this.addKeyListener(new SecretCode());
		this.setResizable(false);
		
		initComponent();
	}
	
	/** Initializes the components. */
	private void initComponent() {
		{ // menu
			MyMenuListener menuListener = new MyMenuListener();
			colorGroup = new ButtonGroup();
			modelGroup = new ButtonGroup();
			colorSet = new JMenu("Board Color");
			modelSet = new JMenu("Pawn Model");
			initColorSet();
			initModelSet();
			
			connect = new JMenuItem("Connect");
			connect.setMnemonic('C');
			connect.addActionListener( new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					SetupUI setupUi = SetupUI.getInstance(getUI());
					setupUi.run();
				}
			});
			
			game = new JMenu("Game");
			game.setMnemonic('G');
			game.add( connect );
			game.addSeparator();
			game.add( colorSet );
			game.add( modelSet );
			secret = new JMenuItem();
			secret.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					requestFocus();
				}
			});
			game.addSeparator();
			game.add( secret );

			help = new JMenu("Help");
			help.setMnemonic('H');
			rule = new JMenuItem("How To Play?");
			rule.addActionListener(menuListener);
			about = new JMenuItem("About");
			about.addActionListener(menuListener);
			help.add(rule);
			help.addSeparator();
			help.add(about);

			menuBar = new JMenuBar();
			menuBar.add(game);
			menuBar.add(help);
			this.setJMenuBar(menuBar);
		}

		{ // west
			west = BoxLayoutPanelFactory();
			board = new JPanel(new BorderLayout());
			foeName = new JLabel();
			foeName.setFont( new Font( Font.SANS_SERIF, Font.BOLD , 15) );
			board.add(foeName, BorderLayout.NORTH);
			// game board
			thaiGORuler = ThaiGORuler.getInstance();
			gameboard = new Board(thaiGORuler);
			gameboard.setBorder(new BevelBorder(0));
			board.add(gameboard, BorderLayout.CENTER);
			yourName = new JLabel();
			yourName.setFont( new Font( Font.SANS_SERIF, Font.BOLD , 15) );
			board.add(yourName, BorderLayout.SOUTH);
			west.add(board);
			chatboard = new ChatBoard();
			west.add(chatboard);
			this.add(west, BorderLayout.WEST);
			loadBoardColor();
			loadPawnModel();
		}
		
		{ // east
			east = BoxLayoutPanelFactory();
			gameInfo = new JPanel( new GridLayout(2,1));
			yourPanel = new PlayerPanel("YOUR TURN");
			gameInfo.add(yourPanel);
			foePanel = new PlayerPanel("FOE's TURN");
			gameInfo.add(foePanel);
			
			east.add(gameInfo);
			logs = new ColorTextPane();
			logs.setBackground(Color.GRAY);
			logPane = new JScrollPane(logs);
			logPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			logPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			logPane.setPreferredSize(new Dimension(120,400));
			logs.setBorder(BorderFactory.createEtchedBorder());
			logs.setEditable(false);
			east.add(logPane);
			this.add(east, BorderLayout.EAST);
			stopConnection();
		}
		{ // Sounds
			moveSound = new AudioPlayer("sounds/game_piece_movement_13.wav");
			slideSound = new AudioPlayer("sounds/game_piece_movement_slide_06.wav");
		}
	}
	
	/** Sets UI to be ready for users to play. */
	public void startConnection(){
		modelSet.setEnabled(true);
		foeName.setText(PropertyManager.getProperty("foe"));
		yourName.setText(PropertyManager.getProperty("player"));
		chatboard.setChatable(true);
		logs.clear();
		logs.addText("Game Start !", Color.GREEN);
	}
	
	/** Sets UI to standby mode and wait for connection. */
	public void stopConnection(){
		modelSet.setEnabled(false);
		foeName.setText("Opponent");
		yourName.setText("You");
		chatboard.setChatable(false);
		logs.clear();
		logs.addText("Waiting for connection ...", Color.GREEN);
	}

	/** Initializes the available pawn model to the menu so user can change the model. */
	private void initModelSet() {
		ActionListener listener = new ModelChangeListener();
		for( PawnModel model : PawnModel.values() ){
			JRadioButtonMenuItem item = new JRadioButtonMenuItem( model.name() );
			item.setActionCommand( model.name() );
			item.addActionListener(listener);
			modelSet.add(item);
			modelGroup.add(item);
		}
	}

	/** Adds status of game to logs panel.
	 * 
	 * @param log Game Status
	 * @param textType User's mode to set the text color
	 */
	public void addLog(String log, int textType) {
		Color color = null;
		if(textType == HOST){
			color = Color.RED;
		}
		else if(textType == CLIENT){
			color = Color.BLUE;
		}
		logs.addText(log, color);
		logs.setCaretPosition(logs.getDocument().getLength());
	}

	/** Loads the board color form property. */
	private void loadBoardColor(){
		String currentColor = PropertyManager.getProperty("thaigo.board.currentcolor");
		try{
			for(BoardModel color : BoardModel.values()){
				if( currentColor.equalsIgnoreCase(color.name()) ){
					changeBoardColor(color.getColor1(), color.getColor2());
					Enumeration<AbstractButton> buttons = colorGroup.getElements();
					while(buttons.hasMoreElements()){
						AbstractButton btn = buttons.nextElement();
						if(btn.getActionCommand().equalsIgnoreCase(color.name())){
							colorGroup.clearSelection();
							btn.setSelected(true);
						}
					}
				}
			}

		} catch(Exception e) {
			PropertyManager.setProperty("thaigo.board.currentcolor","Classic");
			changeBoardColor(Color.BLACK, Color.WHITE);
		}
	}

	/** Loads the pawn model form property. */
	private void loadPawnModel() {
		String path = PropertyManager.getProperty("thaigo.pawn.currentmodel");
		try {
			for (PawnModel model : PawnModel.values()){
				if (path.equalsIgnoreCase(model.name())) {
					Enumeration<AbstractButton> buttons = modelGroup.getElements();
					while(buttons.hasMoreElements()){
						AbstractButton btn = buttons.nextElement();
						if(btn.getActionCommand().equalsIgnoreCase(model.name())){
							modelGroup.clearSelection();
							btn.setSelected(true);
						}
					}
				}
			}
		} catch(Exception e) {
		}
	}
	
	/** Initializes the available board color to the menu so user can change the board color. */
	private void initColorSet() {
		ColorChangeListener listener = new ColorChangeListener();
		for( BoardModel b : BoardModel.values()){
			JRadioButtonMenuItem color = new JRadioButtonMenuItem( b.name() );
			color.setActionCommand( b.name() );
			color.addActionListener(listener);
			colorGroup.add(color);
			colorSet.add(color);
		}
	}
	
	/** Creates panel to hold components.
	 * 
	 * @return new panel with BoxLayout
	 */
	private JPanel BoxLayoutPanelFactory() {
		JPanel panel = new JPanel();
		BoxLayout box = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(box);
		return panel;
	}
	
	/** Runs the GameUI and set its location to the center of the screen. */
	public void run(){
		this.pack();
		this.setLocation( CenterDeterminer.determineWithScreen(this) );
		this.setFocusable(true);
		this.setVisible(true);
	}

	/** Changes board color.
	 * 
	 * @param c1 Color 1  
	 * @param c2 Color 2
	 */
	private void changeBoardColor(Color c1, Color c2){
		gameboard.changeColor(c1,c2);
	}

	/** Gets the rule of the game.
	 * 
	 * @return Game rule
	 */
	public AbstractRuler getRuler() {
		return this.thaiGORuler;
	}

	/** Gets chat board that used in this UI.
	 * 
	 * @return chat board
	 */
	public ChatBoard getChatBoard() {
		return this.chatboard;
	}

	/** Adds message to chat board.
	 * 
	 * @param message chat message
	 * @param c Color of message
	 */
	public void addChatMessage(String message , Color c) {
		chatboard.addChatMessage(message, c);
	}

	/** Highlight your turn. */
	public void highlightYourPanel() {
		yourPanel.setBorder(new LineBorder(Color.RED));
		foePanel.setBorder(new LineBorder(null));
	}

	/** Highlight for turn. */
	public void highlightFoePanel() {
		yourPanel.setBorder(new LineBorder(null));
		foePanel.setBorder(new LineBorder(Color.RED));
	}
	
	/** Show time in your panel. */
	public void setYourTime(int second) {
		yourPanel.setTime(second);
	}
	
	/** Show time in foe panel. */
	public void setFoeTime(int second) {
		foePanel.setTime(second);
	}
	
	/** Shows the win image. */
	public void win(){
		WinGame.getInstance(this).run();
	}
	
	/** Shows the lose image. */
	public void lose(){
		LoseGame.getInstance(this).run();
	}
	
	/** Shows the draw image. */
	public void draw(){
		DrawGame.getInstance(this).run();
	}
	
	/** Unavailable. */
	public void newGame(){
		//OPEN FOR IMPROVEMENT.
	/*
		int i = JOptionPane.showConfirmDialog(this, "Do you want to play a new game?", "New Game", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if( i == 0 ){
			thaiGORuler.newGame();
		}
		if( i == 1 ){
			// DISCONNECT
		}*/
	}

	/** Listener for color change. To change the board color. */
	class ColorChangeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			Color c1 = null; 
			Color c2 = null;

			try{
				for(BoardModel color : BoardModel.values()){
					if( e.getActionCommand().equals(color.name())){
						PropertyManager.setProperty("thaigo.board.currentcolor",color.name());
						c1 = color.getColor1();
						c2 = color.getColor2();
					}
				}
			}
			catch(Exception exception){
				PropertyManager.setProperty("thaigo.board.currentcolor","Classic");
				c1 = Color.BLACK;
				c2 = Color.white;
			}
			changeBoardColor(c1,c2);
		}

	}

	/** Listener for pawn model change. */
	class ModelChangeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			ImageIcon you = null;
			ImageIcon foe = null;
			try {
				for(PawnModel model : PawnModel.values()){
					if(arg0.getActionCommand().equalsIgnoreCase(model.name())){
						you = (new ImageLoader(model.getFirstPawn())).getImageIcon();
						foe = (new ImageLoader(model.getSecondPawn())).getImageIcon();
						PropertyManager.setProperty("thaigo.pawn.currentmodel",model.name());
					}
				}
			}
			catch(Exception ex){
				PropertyManager.setProperty("thaigo.pawn.currentmodel","Classic");
				you = (new ImageLoader(PawnModel.Classic.getFirstPawn())).getImageIcon();
				foe = (new ImageLoader(PawnModel.Classic.getSecondPawn())).getImageIcon();
			}
			gameboard.setPawnModel(you, foe);
		}

	}
	
	/** Plays or stops the moving sound.
	 * 
	 * @param status Status of sound (play/stop)
	 */
	public void movingSound(int status){
		switch(status){
		case PLAY:
			moveSound.play();
			break;
		case STOP:
			moveSound.stop();
			break;
		}
	}
	
	/** Plays or stops the eating sound.
	 * 
	 * @param status Status of sound (play/stop)
	 */
	public void eatingSound(int status){
		switch(status){
		case PLAY:
			slideSound.play();
			break;
		case STOP:
			slideSound.stop();
			break;
		}
	}

	/** Gets this UI.
	 * 
	 * @return this GameUI
	 */
	public GameUI getUI(){
		return this;
	}
	
	/** Gets the game board.
	 * 
	 * @return Board
	 */
	public Board getGameBoard() {
		return gameboard;
	}
	
	/** Listener for menu items. */
	class MyMenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if( e.getSource() == rule ){
				HowToPlay rule = HowToPlay.getInstance(getUI());
				rule.run();
			}
			else if( e.getSource() == about ){
				About aboutUs = About.getInstance(getUI());
				aboutUs.run();
			}
		}

	}

	/** Key listener, listening for secret code. */
	class SecretCode implements KeyListener {

		StringBuffer typed = new StringBuffer();
		ImageIcon foe;

		@Override
		public void keyPressed(KeyEvent e) {

		}

		@Override
		public void keyReleased(KeyEvent e) {

		}

		@Override
		public void keyTyped(KeyEvent e) {
			foe = (new ImageLoader("images/"+PropertyManager.getProperty("thaigo.pawn.currentmodel").toLowerCase()+"2.png")).getImageIcon();
			typed.append( Character.toChars(e.getKeyChar()) );
			if(typed.toString().contains("nolmode")){
				typed.setLength(0);
				ImageIcon you = (new ImageLoader("images/crown.png")).getImageIcon();
				gameboard.setPawnModel(you,foe);
			}
			if(typed.toString().contains("batmode")){
				typed.setLength(0);
				ImageIcon you = (new ImageLoader("images/icon.png")).getImageIcon();
				gameboard.setPawnModel(you,foe);
			}
			if(typed.toString().contains("icemode")){
				typed.setLength(0);
				ImageIcon you = (new ImageLoader("images/help.png")).getImageIcon();
				gameboard.setPawnModel(you,foe);
			}
			if(typed.toString().contains("godmode")){
				typed.setLength(0);
				JOptionPane.showMessageDialog(null, "No you are not a GOD !");
			}
			if(typed.toString().contains("iwin")){
				typed.setLength(0);
				win();
			}
			if(typed.toString().contains("ilose")){
				typed.setLength(0);
				lose();
			}
			if(typed.toString().contains("idraw")){
				typed.setLength(0);
				draw();
			}
		}
	}
}