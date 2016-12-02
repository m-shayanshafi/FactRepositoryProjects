package common.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import common.model.GameBoard;
import common.model.GameBoard.Cell;

/**
 * Classe vue generale des 3 jeux
 * 
 * @author LETOURNEUR Léo
 *
 */
public class GameBoardView extends JDialog implements Observer {
	private static final long serialVersionUID = 7569627007177996281L;

	protected GameBoard modele;
	protected int width;
	protected int length;
	protected int largeurCase;

	public ImageIcon iconEmpty;
	public ImageIcon iconJoueur1;
	public ImageIcon iconJoueur2;
	public ImageIcon iconNext1;
	public ImageIcon iconNext2;

	public JToolBar menuBar;
	public JButton resetButton;
	public JButton undoButton;
	public JButton loadButton;
	public JButton saveButton;
	public JButton exitButton;
	public JCheckBox aloneCheck;

	protected JPanel contentPane;
	public JPanel labelPane;
	public JButton[][] casesLabel;
	
	public JPanel scorePane;
	public JLabel nextPlayer;
	public JLabel scoreJ1;
	public JLabel scoreJ2;

	public GameBoardView(GameBoard game) {

		modele = game;
		modele.addObserver(this);
		//setModal(true);
		//Doesn't work with addObserver
		width = game.WIDTH;
		length = game.LENGTH;
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}

	/**
	 * Methode qui charge les images
	 * 
	 */
	public void loadIcons() {
		iconEmpty = new ImageIcon(ClassLoader.getSystemResource(modele.getNomJeu()+"/empty.png"));
		iconJoueur1 = new ImageIcon(ClassLoader.getSystemResource(modele.getNomJeu()+"/player1.png"));
		iconJoueur2 = new ImageIcon(ClassLoader.getSystemResource(modele.getNomJeu()+"/player2.png"));
		iconNext1 = new ImageIcon(ClassLoader.getSystemResource(modele.getNomJeu()+"/next1.png"));
		iconNext2 = new ImageIcon(ClassLoader.getSystemResource(modele.getNomJeu()+"/next2.png"));
	}
	
	/**
	 * Methode qui met a jour les scores
	 * 
	 */
	public void updateScore()
	{
		scoreJ1.setText("Player 1 : "+modele.getScore().getScorePlayer1());
		scoreJ2.setText("Player 2 : "+modele.getScore().getScorePlayer2());
	}
	
	/**
	 * Methode qui met en place les composants
	 * 
	 */
	public void loadPanels() {

		setBounds(100, 100, largeurCase * length + 184, largeurCase * width + 120);
		setLocationRelativeTo(null);
		
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setLayout(null);
		setContentPane(contentPane);

		labelPane = new JPanel();
		labelPane.setBounds(50, 70, largeurCase * length, largeurCase * width);
		labelPane.setLayout(new GridLayout(width, length));
		contentPane.add(this.labelPane);
		casesLabel = new JButton[width][length];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < length; j++) {
				casesLabel[i][j] = new JButton();
				labelPane.add(casesLabel[i][j]);
				casesLabel[i][j].setIcon(this.iconEmpty);
				casesLabel[i][j].setContentAreaFilled(false);
				casesLabel[i][j].setActionCommand(i+","+j);
			}
		}
		
		scorePane = new JPanel();
		scorePane.setBounds(largeurCase * length + 70, 70, 100, largeurCase * width);
		scorePane.setLayout(null);
		contentPane.add(this.scorePane);
		
		nextPlayer = new JLabel();
		nextPlayer.setBounds(0, 0, 100, 100);
		nextPlayer.setHorizontalTextPosition(JLabel.CENTER);
		if(modele.nextPlayer()==Cell.PLAYER1.value)
			nextPlayer.setIcon(iconNext1);
		else
			nextPlayer.setIcon(iconNext2);
		scorePane.add(this.nextPlayer);
		
		scoreJ1 = new JLabel();
		scoreJ1.setBounds(0, 100, 100, 40);
		scoreJ1.setHorizontalAlignment(JLabel.CENTER);
		scorePane.add(this.scoreJ1);
		
		scoreJ2 = new JLabel();
		scoreJ2.setBounds(0, 140, 100, 40);
		scoreJ2.setHorizontalAlignment(JLabel.CENTER);
		scorePane.add(this.scoreJ2);
		
		updateScore();
	}

	/**
	 * Methode qui charge le menu
	 * 
	 */
	public void loadMenu() {

		menuBar = new JToolBar();
		menuBar.setBounds(0, 0, largeurCase * length + 183, 40);
		menuBar.setVisible(true);
		menuBar.setRollover(true);
		menuBar.setFloatable(false);
		add(menuBar, BorderLayout.NORTH);

		exitButton = new JButton("Exit");
		exitButton.setForeground(Color.red);
		menuBar.addSeparator();
		menuBar.add(exitButton);
		
		resetButton = new JButton("Replay");
		menuBar.addSeparator();
		menuBar.add(resetButton);

		loadButton = new JButton("Load game");
		menuBar.addSeparator();
		menuBar.add(loadButton);

		saveButton = new JButton("Save game");
		menuBar.addSeparator();
		menuBar.add(saveButton);

		menuBar.add(Box.createHorizontalGlue());

		aloneCheck = new JCheckBox("Alone");
		menuBar.add(aloneCheck);
		undoButton = new JButton("Undo");
		menuBar.add(undoButton);
		menuBar.addSeparator();
	}

	/**
	 * Methode qui nettoie la grille
	 * 
	 */
	public void resetGrid() {

		nextPlayer.setText("");
		if(modele.nextPlayer() == Cell.PLAYER1.value)
			nextPlayer.setIcon(iconNext1);
		else
			nextPlayer.setIcon(iconNext2);
		undoButton.setEnabled(true);
		for (int j = 0; j < length; j++)
			for (int i = 0; i < width; i++) {
				casesLabel[i][j].setEnabled(true);
				casesLabel[i][j].setIcon(this.iconEmpty);
			}
	}

	public void update(Observable arg0, Object arg1) {}
}