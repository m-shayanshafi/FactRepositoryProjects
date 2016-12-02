package game.penguincards;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import game.penguincards.debug.*;

public class PenguinCards extends JFrame {
	
	class PanelTimerTask extends TimerTask {
		public PanelTimerTask() { 
			super();
		}
		public void run() {
	        currentPanel_1.closeCard();
			currentPanel_2.closeCard();
			lockCards=false;
			changeActivePlayer();
		}
	}
	
	private static PenguinCards theInstance=null;
	
	// set paths of images, pictures
	private String systemImgPath=new String("images/system/");
	private String penguinsImgPath=new String("images/pictures/penguins/");
	
	// path of property file
	private String propFile=new String("PenguinCards.prop");
	
	private JPanel panelCards, panelNorth ,panelSouth;
	private JMenuBar menuBar;
	private  JLabel scorePlayer_1, scorePlayer_2, activeUserLabel;
	
	private Properties properties=new Properties();
	private JLabel player2_lbl;
	private static int oponent;
	private final static int COMPUTER = 0;
	private final static int PLAYER1  = 1; 
	private final static int PLAYER2  = 2;
	private static int ACTIVEPLAYER= PLAYER1;
	
	private int size; // number of cards
	private int foundCards; // number of foundCards till certain time
	
	private int difficulty;
	private final static int LOW    = 0;
	private final static int LOWLEVEL    = 1;
	private final static int MEDIUM = 1;
	private final static int MEDIUMLEVEL = 5;
	private final static int HIGH   = 2;
	private final static int HIGHLEVEL   = 9;
	private int computerLevel   = 0; 
	
	private static boolean lockCards=false; 
	
	private int cardsArray[];  // keeps solution array (places of cards)
	private int computerCardsArray[]; // keeps opened cards for computer_player
	private int computerCardsOpened[] ;// keeps whether card is already opened or not -1:not opened /1:opened
	
	private static int currentImage_1= -1 , currentImage_2= -1;
	private int first_card, second_card;
	private PenguinCardsPanel currentPanel_1, currentPanel_2;
	private PenguinCardsPanel mCardsPanels[]; 
	
	private PenguinCards() {
		initGUI();
	}
	
	public static PenguinCards getInstance() {
		if (theInstance == null) theInstance=new PenguinCards();
		return theInstance;
	}

	private void initGUI() {
		
		panelNorth = new JPanel();
		panelCards = new JPanel();
		panelSouth = new JPanel();

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(panelNorth, BorderLayout.NORTH);
		this.getContentPane().add(panelCards, BorderLayout.CENTER);
		this.getContentPane().add(panelSouth, BorderLayout.SOUTH);

		// add ScoreTable into panelNorth
		panelNorth.setBackground(new Color(192,220,192));
		JLabel player1_lbl=new JLabel("");
		player1_lbl.setIcon(new ImageIcon(systemImgPath+"player1.gif"));
		panelNorth.add(player1_lbl);
		
		scorePlayer_1= new JLabel("0");
		scorePlayer_1.setFont(new Font("Verdana", Font.PLAIN, 25));
		scorePlayer_1.setForeground(new Color(255,255,255));
		panelNorth.add(scorePlayer_1);
		
		JLabel score_lbl= new JLabel();
		score_lbl.setIcon(new ImageIcon(systemImgPath+"score.jpg"));
		panelNorth.add(score_lbl);
		
		scorePlayer_2= new JLabel("0");
		scorePlayer_2.setFont(new Font("Verdana", Font.PLAIN, 25));
		scorePlayer_2.setForeground(new Color(255,255,255));
		panelNorth.add(scorePlayer_2);
		
		player2_lbl=new JLabel("");
		if (oponent == COMPUTER )player2_lbl.setIcon(new ImageIcon(systemImgPath+"computer.gif"));
		else player2_lbl.setIcon(new ImageIcon(systemImgPath+"player2.gif"));
		panelNorth.add(player2_lbl);
		panelNorth.setVisible(false);

		// add Start Button to panelSouth
		panelSouth.setBackground(new Color(192,220,192));
		
		JLabel activeuser_lbl=new JLabel("");
		activeuser_lbl.setIcon(new ImageIcon(systemImgPath+"activeuser.gif"));
		panelSouth.add(activeuser_lbl);
		
		activeUserLabel= new JLabel("Player1");
		activeUserLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
		activeUserLabel.setForeground(new Color(0,0,0));
		panelSouth.add(activeUserLabel);
		panelSouth.setVisible(false);
		
		// adding cards into the panelCards at runtime
		
		PenguinCardsMenu mCardsMenu= new PenguinCardsMenu();
		menuBar=mCardsMenu.composeMenuBar();
		this.setJMenuBar(menuBar);
		mCardsMenu.menuitemNewGame.addActionListener(new NewGameListener());
		
		int x=800;
		int y=600;
		this.setSize(x,y);
		
		// locate frame in the center
		Dimension frame=Toolkit.getDefaultToolkit().getScreenSize();
		x=(int) ((frame.getWidth()-x)/2);
		y=(int) ((frame.getHeight()-y)/2);
		this.setLocation(x,y);
		
		this.setTitle("Penguin Cards v1.0");
		this.dispose();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.show();

	}
	
	/**
	 * gets the sequence number of PenguinCardsPanel which was clicked
	 */
	public void getCardsNumber(int cardSequence, int cardName) {
		
	 	computerCardsArray[cardSequence]=cardName;
		
		if (currentImage_1 == -1) {// first card clicked
			currentImage_1=cardName;
			currentPanel_1=mCardsPanels[cardSequence];
			first_card=cardSequence;
		} 
		else { // second card clicked
			
			lockCards=true;
		   	currentImage_2=cardName;
			second_card=cardSequence;
			currentPanel_2=mCardsPanels[cardSequence];
		   	
		   	if (currentImage_1 == currentImage_2) {
		   		increasePoint(); // increase point of active player
		   		currentPanel_1.disableCard();
				currentPanel_2.disableCard();
				lockCards=false;
				computerCardsOpened[first_card] =1;
				computerCardsOpened[second_card] =1;
				
				foundCards -=2;
				if (foundCards == 0) finishGame();
				else if (ACTIVEPLAYER == COMPUTER) {
					currentImage_1=-1;
					currentImage_2=-1;
					playComputer();
				} 
		   	}
		   	else {
		   		java.util.Timer timer =new java.util.Timer();
				timer.schedule(new PanelTimerTask(),(long)2000);
		   	}
			
			currentImage_1=-1;
			currentImage_2=-1;
			
		}
	}
	
	private void increasePoint() {
		if (ACTIVEPLAYER == PLAYER1) {
			  int score=Integer.parseInt(scorePlayer_1.getText())+10;
			  scorePlayer_1.setText(Integer.toString(score));
		}
		else if (ACTIVEPLAYER == PLAYER2) {
			  int score=Integer.parseInt(scorePlayer_2.getText())+10;
			  scorePlayer_2.setText(Integer.toString(score));
		}
		else if (ACTIVEPLAYER == COMPUTER) {
			  int score=Integer.parseInt(scorePlayer_2.getText())+10;
			  scorePlayer_2.setText(Integer.toString(score));
		}
	}
	
	private void changeActivePlayer() {
		switch(ACTIVEPLAYER) {
			case PLAYER1: 
			     if (oponent==PLAYER2) {
			     	ACTIVEPLAYER=PLAYER2;
					activeUserLabel.setText("Player2");
			     }
			     else {
			     	ACTIVEPLAYER=COMPUTER;
					activeUserLabel.setText("Computer");
					computerLevel=0;
			     	playComputer(); 
			     }
			     break;
			case PLAYER2: ACTIVEPLAYER=PLAYER1;
					showCursor(); 
					activeUserLabel.setText("Player1");
					break;
			case COMPUTER: ACTIVEPLAYER=PLAYER1;
					showCursor();
					activeUserLabel.setText("Player1");
		}
	}
	
	private void playComputer() {
		hideCursor();
		try {
			Thread.sleep(1000); 
		}catch(InterruptedException exc) {
			Debug.debug(exc.getMessage());
		}
		
		CardPair cardPair=null;
		
		if (computerLevel <= difficulty) {
			cardPair= pickupCards("EXACT");
			computerLevel ++;
		} 
		else if (computerLevel > difficulty) cardPair = pickupCards("GUESS");
		
		if (cardPair == null ) cardPair = pickupCards("HARDGUESS");
		
		// open cards
		if (cardsArray[cardPair.x] == cardsArray[cardPair.y]) {
			computerCardsOpened[cardPair.x]= 1;
			computerCardsOpened[cardPair.y]= 1;
		}
			
		mCardsPanels[cardPair.x].setActiveImage();
		getCardsNumber(cardPair.x,cardsArray[cardPair.x]);
		mCardsPanels[cardPair.y].setActiveImage();
		getCardsNumber(cardPair.y,cardsArray[cardPair.y]);
		
	}
	
	private CardPair pickupCards(String type) {
		
		CardPair cardPair=new CardPair();
		
		if (type.equalsIgnoreCase("EXACT")) { // pickup already opened cardpair
			
			boolean s=true;
			int i=0;
			
			while (s && i<computerCardsArray.length-1){
				int j=i+1;
				while (s && j<computerCardsArray.length){
					if (computerCardsArray[i]>-1 && computerCardsArray[i]==computerCardsArray[j] && computerCardsOpened[i] < 1) {
						cardPair= new CardPair(i,j);
						s=false;
					}
					j++;
				}
				i++;
				j=0;
			}
			if (!s) return cardPair; // if opened CardPair exists 
		}

		else if (type.equalsIgnoreCase("HARDGUESS")) { // pickup randomly cardpair
			// firstly randomly choose an unopened card
			// then randomly choose the second card
			
			boolean s=true;
			int rndmCard_1=-1,rndmCard_2=-1;
			Random random= new Random();
			while(s) {
				rndmCard_1=random.nextInt(size);
				if (computerCardsArray[rndmCard_1] == -1 ) s=false;
			}
			cardPair.x=rndmCard_1;
			s=true;
			while(s) {
				rndmCard_2=random.nextInt(size);
				if (cardPair.x != rndmCard_2 && computerCardsOpened[rndmCard_2] < 1) s=false;
			}
			cardPair.y=rndmCard_2;
			return cardPair;
		}
		
		else if (type.equalsIgnoreCase("GUESS")) { // pickup randomly cardpair
			// firstly randomly choose an unopened card
			// then randomly choose the second card
			boolean s=true;
			int rndmCard_1=-1,rndmCard_2=-1;
			Random random= new Random();
			while(s) {
				rndmCard_1=random.nextInt(size);
				if (computerCardsOpened[rndmCard_1] < 1 ) s=false;
			}
			cardPair.x=rndmCard_1;
			s=true;
			while(s) {
				rndmCard_2=random.nextInt(size);
				if (cardPair.x != rndmCard_2 && computerCardsOpened[rndmCard_2] < 1 ) s=false;
			}
			cardPair.y=rndmCard_2;
			return cardPair;
		}
		
		return null;
		
	}
	
	private void hideCursor() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image InvisMouse = tk.getImage("pictures"+System.getProperty("file.separator")+"null.gif");
		setCursor(tk.createCustomCursor(InvisMouse,new Point(0,0),""));
	}
	
	private void showCursor() {
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	private void finishGame() {
		
		showCursor();
		
		int scoreP1= Integer.parseInt(scorePlayer_1.getText());
		int scoreP2= Integer.parseInt(scorePlayer_2.getText());
		
		String player_2;
		if (oponent == COMPUTER) player_2="Computer";
		else  player_2="Player 2";
		
		String score="Player 1: "+scoreP1+" "+player_2+": "+scoreP2+" \n";  
		
		if (scoreP1 < scoreP2)
		   JOptionPane.showMessageDialog(this,score+player_2+" has won!","Result",1,null);
		else if (scoreP1 > scoreP2)
		    JOptionPane.showMessageDialog(this,score+"Player 1 has won!","Result",1,null);
		else if (scoreP1 == scoreP2)
			JOptionPane.showMessageDialog(this,score+"No winner!","Result",1,null);
		
	}
	
    public static void main(String args[]) {
		theInstance=new PenguinCards();
    }
    
    class NewGameListener implements ActionListener {
    	
    	public void actionPerformed(ActionEvent ae) { 
    		// get size, oponent and difficulty
    		getSettings();
    		
    		// create Cards
    		createCards(size);
    		
    		// reset environmentVariables
    		resetVariables();
    		
    		menuBar.validate();
    	}
    	
    	private void getSettings() {
    		
    		String tmp_players=null;
    		String tmp_size=null;
    		String tmp_difficulty=null;
    		try {
    		   properties.load(new FileInputStream(propFile));
			   tmp_players=properties.getProperty("Players");
			   tmp_size=properties.getProperty("Size");
			   tmp_difficulty=properties.getProperty("Difficulty");
    		}catch (IOException exc) {
    			Debug.debug(exc.getMessage());
    		}
    		
    		if (tmp_players.equalsIgnoreCase("Human2Human")) oponent=PenguinCards.PLAYER2;
			else if (tmp_players.equalsIgnoreCase("Human2Computer")) oponent=PenguinCards.COMPUTER;
			
			if (tmp_size.equalsIgnoreCase("3*2 Cards")) size=6;
			else if (tmp_size.equalsIgnoreCase("5*2 Cards")) size=10;
			else if (tmp_size.equalsIgnoreCase("10*2 Cards")) size=20;
			else if (tmp_size.equalsIgnoreCase("15*2 Cards")) size=30;
			else if (tmp_size.equalsIgnoreCase("20*2 Cards")) size=40;
			
			if (tmp_difficulty.equalsIgnoreCase("Low")) difficulty=PenguinCards.LOWLEVEL;
			else if (tmp_difficulty.equalsIgnoreCase("Medium")) difficulty=PenguinCards.MEDIUMLEVEL;
			else if (tmp_difficulty.equalsIgnoreCase("High")) difficulty=PenguinCards.HIGHLEVEL;
    	}
    	
    	private void createCards(int size) {
    		
    		foundCards=size;
    		// set grid layout size
    		switch (size) {
    			case 6: panelCards.setLayout(new GridLayout(2,3));break;
				case 10: panelCards.setLayout(new GridLayout(2,5));break;
				case 20: panelCards.setLayout(new GridLayout(4,5));break;
				case 30: panelCards.setLayout(new GridLayout(3,10));break;
				case 40: panelCards.setLayout(new GridLayout(4,10));break;
    		}
    		
    		// generate cards Array to mix cards
    		generateCardsArray(size);
    		
    		// set visibility of north and south panels
			panelSouth.setVisible(true);
			panelNorth.setVisible(true);
			
    		// create PenguinCardsPanel
			File[] dirList = new File(penguinsImgPath).listFiles();
			
    		panelCards.removeAll();
    		
    		// specify dimension of cards
    		Dimension dimension=null;
    		switch(size) {
    			case  6: dimension= new Dimension(150,150);break;
				case 10: dimension= new Dimension(150,150);break;
				case 20: dimension= new Dimension(100,100);break;
				case 30: dimension= new Dimension(100,100);break;
				case 40: dimension= new Dimension(100,100);
    		}
    		
			mCardsPanels= new PenguinCardsPanel[size];
			for (int i=0;i<size;i++) {
				mCardsPanels[i]=new PenguinCardsPanel(systemImgPath+"default.jpg",penguinsImgPath+dirList[cardsArray[i]].getName(),i,Integer.toString(cardsArray[i]),dimension);
				mCardsPanels[i].addMouseListener(new PanelListener());
				panelCards.add(mCardsPanels[i]);
    		}
				
    		panelCards.validate();
    	}
    	
    	private void resetVariables() {
    		
			scorePlayer_1.setText("0");
			scorePlayer_2.setText("0");
			if (oponent==PLAYER2) player2_lbl.setIcon(new ImageIcon(systemImgPath+"player2.gif"));
			else player2_lbl.setIcon(new ImageIcon(systemImgPath+"computer.gif"));
			activeUserLabel.setText("Player 1");
			ACTIVEPLAYER=PLAYER1;
			currentImage_1=-1;
			currentImage_2=-1;
    	}
   
    //	generate cards Array to mix cards
    private void generateCardsArray(int size) {
    	
    	cardsArray= new int[size];
		computerCardsArray= new int[size]; // computer keeps track of opened cards
		int half=size/2;
		computerCardsOpened= new int[size]; // computer keeps track whether i th card and its pair
    	
    	// inititialize the arrays
    	for (int i=0;i<half;i++) {
    		cardsArray[i]=i;
			cardsArray[i+half]=i;
			computerCardsArray[i]=-1;
			computerCardsArray[i+half]=-1;
			computerCardsOpened[i]=-1;
			computerCardsOpened[i+half]=-1;
    	} 
    	
    	//mix the array
    	int rndm1;
		int rndm2;
		int tmp;
		Random random= new Random();
		for (int i=0;i<size*2;i++) {
			rndm1=random.nextInt(size);
			rndm2=random.nextInt(size);
			tmp=cardsArray[rndm1];
			cardsArray[rndm1]=cardsArray[rndm2];
			cardsArray[rndm2]=tmp;
		}
    }
    }
	
	class CardPair {
    	public int x;
    	public int y;
    	
    	CardPair(int x, int y) {
    		this.x=x;
    		this.y=y;
    	}
		
		CardPair() {
		}
    }
    
	class PanelListener implements MouseListener {
		
		public void mouseClicked(MouseEvent mouseEvent) {
			if (!lockCards || ACTIVEPLAYER==COMPUTER) {
				PenguinCardsPanel mcp= (PenguinCardsPanel)mouseEvent.getSource();
				mcp.clickImage();
			}
		}
		public void mousePressed(MouseEvent mouseEvent) {
		}
		public void mouseReleased(MouseEvent mouseEvent) {
		}
		public void mouseExited(MouseEvent mouseEvent) {
		}
		public void mouseEntered(MouseEvent mouseEvent) {
		}
	}
}