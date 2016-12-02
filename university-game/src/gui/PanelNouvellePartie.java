/**
 * PanelNouvellePartie.java
 * 
 * 
 * 
 * @author Si-Mohamed Lamraoui
 * @date 28.05.10
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import moteur.MoteurMultijoueur;
import joueur.Joueur;
import joueur.JoueurDistant;


public class PanelNouvellePartie extends JPanel
{


	private static final long serialVersionUID = 1L;
	
	static final String DIFFICULTY_EASY 	= "facile";
	static final String DIFFICULTY_NORMAL 	= "normal";
	static final String DIFFICULTY_HARD		= "difficile";
	static final String DIFFICULTY_HEAVY	= "ultime";

	static final int MODE_OFFLINE 	= 0;
	static final int MODE_ONLINE 	= 1;
	
	public String textSuedois = "L'armee suedoise a un role cle dans une partie de tablut. Le roi suedois est menace\npar l'ennemie moscovite. Prennez votre courage a deux mains et escortez\nle roi en lieu sur.";
	public String textMoscovites = "L'armee moscovite n'a peur de rien, elle tentera tout pour capturer le roi suedois.\nSi vous voulez participer au massacre, alors aiguisez vos epees et percer les\ndefences ennemies.";
	private InterfaceMenu mainwindow;
	public int mode;
	private String ai;
	public boolean selected;
	public ImageIcon suedoisIcon;
	public ImageIcon suedoisSelectedIcon;
	public ImageIcon moscovitesIcon;
	public ImageIcon moscovitesSelectedIcon;
	public JButton suedoisButton;
	public JButton moscovitesButton;
	public JTextArea description;
	public JoueurDistant player;
	public JCheckBox checkAttack;


	public PanelNouvellePartie(InterfaceMenu w, int m)
	{
		mainwindow = w;
		mainwindow.frame.setTitle(InterfaceMenu.TITLE+" - Nouvelle partie");
		mode = m;
		selected = Joueur.MOSCOVITE;
		suedoisIcon = createImageIcon("/images/pion-menu-suedois.png", "");
		suedoisSelectedIcon = createImageIcon("/images/pion-menu-selected-suedois.png", "");
		moscovitesIcon = createImageIcon("/images/pion-menu-moscovites.png", "");
		moscovitesSelectedIcon = createImageIcon("/images/pion-menu-selected-moscovites.png", "");
		this.setLayout(new BorderLayout());
		JPanel panel1 = new JPanel(new BorderLayout());
		JPanel crewPanel = new JPanel(new BorderLayout());
		JPanel infoPanel = new JPanel();
		JPanel buttonPanel = new JPanel(new BorderLayout());
        JPanel radioPanel = new JPanel(new GridLayout(1, 0));
		JPanel armyPanel = new JPanel();
		Font font = new Font("MS Gothic", 0, 12);

		// Buttons
		suedoisButton = new JButton();
		suedoisButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		suedoisButton.addActionListener(new AdaptatorSuedois());
		moscovitesButton = new JButton();
		moscovitesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		moscovitesButton.addActionListener(new AdaptatorMoscovites());
		if(selected==Joueur.SUEDOIS) {
			suedoisButton.setIcon(suedoisSelectedIcon);
			moscovitesButton.setIcon(moscovitesIcon);
		} else {
			suedoisButton.setIcon(suedoisIcon);
			moscovitesButton.setIcon(moscovitesSelectedIcon);
		}
		armyPanel.add(suedoisButton);
		armyPanel.add(new JLabel("                                "));
		armyPanel.add(moscovitesButton);
		crewPanel.add(armyPanel, BorderLayout.NORTH);
		if(selected==Joueur.MOSCOVITE)
			description = new JTextArea(textMoscovites);
		else
			description = new JTextArea(textSuedois);
		description.setFont(font);
		description.setBackground(this.getBackground());
		description.setEditable(false);
		crewPanel.add(description, BorderLayout.CENTER);
		panel1.add(crewPanel, BorderLayout.NORTH);
		
		// Radio buttons | Check box
		if(mode==MODE_OFFLINE) {
		    JRadioButton easyButton = new JRadioButton(DIFFICULTY_EASY);
		    easyButton.setActionCommand(DIFFICULTY_EASY);
		    easyButton.addActionListener(new AdaptatorRadio());
		    easyButton.setSelected(true);
		    JRadioButton normalButton = new JRadioButton(DIFFICULTY_NORMAL);
		    normalButton.setActionCommand(DIFFICULTY_NORMAL);
		    normalButton.addActionListener(new AdaptatorRadio());
		    JRadioButton hardButton = new JRadioButton(DIFFICULTY_HARD);
		    hardButton.setActionCommand(DIFFICULTY_HARD);
		    hardButton.addActionListener(new AdaptatorRadio());
		    JRadioButton heavyButton = new JRadioButton(DIFFICULTY_HEAVY);
		    heavyButton.setActionCommand(DIFFICULTY_HEAVY);
		    heavyButton.addActionListener(new AdaptatorRadio());
		    ButtonGroup group = new ButtonGroup();
		    group.add(easyButton);
		    group.add(normalButton);
		    group.add(hardButton);
		    group.add(heavyButton);
		    easyButton.setFont(font);
		    normalButton.setFont(font);
		    hardButton.setFont(font);
		    heavyButton.setFont(font);
	        radioPanel .add(easyButton);
	        radioPanel .add(normalButton);
	        radioPanel .add(hardButton);
	        radioPanel .add(heavyButton);
			panel1.add(new JLabel("                   "), BorderLayout.EAST);
			panel1.add(new JLabel("                   "), BorderLayout.WEST);
			panel1.add(radioPanel, BorderLayout.CENTER);
		} 
		else if(mode==MODE_ONLINE) {
			checkAttack = new JCheckBox();
			infoPanel.add(checkAttack);
			infoPanel.add(new JLabel("Activer les attaques"));
			panel1.add(infoPanel, BorderLayout.CENTER);
		}
		
		// Buttons
		JButton buttonCancel = new JButton("Annuler");
		buttonCancel.addActionListener(new AdaptatorCancel(mainwindow, this));
		buttonPanel.add(buttonCancel, BorderLayout.WEST);
		JButton buttonPlay = new JButton("Creer");
		buttonPlay.addActionListener(new AdaptatorPlay(mainwindow, this));
		buttonPanel.add(buttonPlay, BorderLayout.EAST);
		panel1.add(buttonPanel, BorderLayout.SOUTH);
		
		// Main panel
		this.add(panel1, BorderLayout.CENTER);
		this.add(new JLabel("     "), BorderLayout.SOUTH);
		this.add(new JLabel("     "), BorderLayout.WEST);
		this.add(new JLabel("     "), BorderLayout.EAST);
		this.setPreferredSize(mainwindow.dim);
	}
	

	class AdaptatorSuedois implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	        if(selected==Joueur.MOSCOVITE) {
	        	description.setText(textSuedois);
	        	suedoisButton.setIcon(suedoisSelectedIcon);
	        	moscovitesButton.setIcon(moscovitesIcon);
	        	selected = Joueur.SUEDOIS;
	        }
	    }
	}
	
	
	class AdaptatorMoscovites implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	        if(selected==Joueur.SUEDOIS) {
	        	description.setText(textMoscovites);
	        	moscovitesButton.setIcon(moscovitesSelectedIcon);
	        	suedoisButton.setIcon(suedoisIcon);
	        	selected = Joueur.MOSCOVITE;
	        }
	    }
	}

	
	class AdaptatorCancel implements ActionListener {
		private InterfaceMenu mainwindow;
		private PanelNouvellePartie panel;
		public AdaptatorCancel(InterfaceMenu w, PanelNouvellePartie p) {
			mainwindow = w;
			panel = p;
		}
	    public void actionPerformed(ActionEvent e) {
	    	mainwindow.frame.remove(mainwindow.panel);
	    	if(panel.mode==PanelNouvellePartie.MODE_ONLINE)
	    		mainwindow.panel = new PanelEnLigne(mainwindow, player);
	    	else
	    		mainwindow.panel = new PanelMenu(mainwindow);
	        mainwindow.frame.getContentPane().add(mainwindow.panel);
	        mainwindow.frame.repaint();
	        mainwindow.frame.pack();
	    }
	}	
	

	class AdaptatorPlay implements ActionListener {
		private InterfaceMenu mainwindow;
		private PanelNouvellePartie panel;
		public AdaptatorPlay(InterfaceMenu w, PanelNouvellePartie p) {
			mainwindow = w;
			panel = p;
		}
	    public void actionPerformed(ActionEvent e) {
	    	if(mode==MODE_ONLINE)
	    	{
	    		boolean error;
	    		boolean skillEnable = panel.checkAttack.isSelected();
	    		if(skillEnable==true)
	    			error = player.newPartie(selected, "a");
	    		else
	    			error = player.newPartie(selected, "i");
	    		if(error==false)
	    			 JOptionPane.showMessageDialog(mainwindow,
	 	 	                "Impossible de creer une partie, le serveur est surement surcharge. Veuillez ressayez plus tard.", "Erreur",
	 	 	                JOptionPane.ERROR_MESSAGE);
	    		// TODO fix dialog
	    		new DialogAttente(mainwindow, player);
	    		System.out.println("fin diag");
	    		player = mainwindow.networkplayer;
	    		if(mainwindow.partyStarted==true) { //si la partie a commencee (deux joueurs), on lance le jeu
		    		//InterfaceJeu game = new InterfaceJeu("Online game", selected, player.getPseudo(), player);
		    		/*MainWindow m = new MainWindow(selected, player.getPseudo(), player);
		    		Thread t = new Thread(m);
		            t.start();*/
		    		SwingUtilities.invokeLater(new Chat(player.getPseudo(), player));
		    		mainwindow.dispose();
	    		} else { // sinon on revien au menu
	    			player.abandonPartie();
	    			mainwindow.frame.remove(mainwindow.panel);
	    	    	mainwindow.panel = new PanelEnLigne(mainwindow, player);
	    	        mainwindow.frame.getContentPane().add(mainwindow.panel);
	    	        mainwindow.frame.repaint();
	    	        mainwindow.frame.pack();
	    		}	
	    	}
	    	else { // Mode offline
	            // TODO lancer le jeu ici
		    	mainwindow.dispose();
	           /* InterfaceJeu jeu = new InterfaceJeu("online");
	            Thread t = new Thread(jeu);
            	t.start();*/
	    	}

	    }
	}	
	
	
	class AdaptatorRadio implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	    	ai = e.getActionCommand();
	    	System.out.println("IA : "+ai);
	    }
	}	
	
	
	/** 
	 * Returns an ImageIcon, 
	 * or null if the path was invalid. 
	 * */
	protected ImageIcon createImageIcon(String path, String description) {
	    java.net.URL imgURL = getClass().getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL, description);
	    } else {
	        System.err.println("Couldn't find file: " + path);
	        return null;
	    }
	}
	
	
}
