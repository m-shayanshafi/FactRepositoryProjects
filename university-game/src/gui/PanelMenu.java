/**
 * PanelMenu.java
 * 
 * 
 * @author Si-Mohamed Lamraoui
 * @date 27.05.10
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PanelMenu extends JPanel
{


	private static final long serialVersionUID = 1L;
	private InterfaceMenu mainwindow;

	public PanelMenu(InterfaceMenu w)
	{
		mainwindow = w;
		mainwindow.frame.setTitle(InterfaceMenu.TITLE);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(6,3));
		this.setLayout(new BorderLayout());
		
		Titre title = new Titre();
		title.setPreferredSize(new Dimension(495, 117));
		this.add(title, BorderLayout.CENTER);
		this.add(new JLabel("          "), BorderLayout.NORTH);
		this.add(new JLabel("          "), BorderLayout.WEST);
		this.add(new JLabel("          "), BorderLayout.EAST);
		
		JButton newButton = new JButton("Nouvelle partie");
		newButton.addActionListener(new AdaptatorNew(mainwindow));
		buttonPanel.add(new JLabel("    "));
		buttonPanel.add(newButton); 
		buttonPanel.add(new JLabel("     "));
		
		JButton loadButton = new JButton("Charger une partie"); // TODO griser et ajouter une liste de partie save (list)
		// TODO Load.partiesSauvegarder() : verifier si il y a des parties sauvegardee
		//loadButton.setEnabled(false);
		loadButton.addActionListener(new AdaptatorLoad(mainwindow));
		buttonPanel.add(new JLabel("    "));
		buttonPanel.add(loadButton); 
		buttonPanel.add(new JLabel("     "));
			
		JButton onlineButton = new JButton("Partie en ligne");
		onlineButton.addActionListener(new AdaptatorOnline(mainwindow));
		buttonPanel.add(new JLabel("     "));
		buttonPanel.add(onlineButton);
		buttonPanel.add(new JLabel("     "));
		
		JButton aboutButton = new JButton("A propos");
		aboutButton.addActionListener(new AdaptatorAbout());
		buttonPanel.add(new JLabel("     "));
		buttonPanel.add(aboutButton);
		buttonPanel.add(new JLabel("     "));
		
		JButton quitButton = new JButton("Quitter");
		quitButton.addActionListener(new AdaptatorQuit());
		buttonPanel.add(new JLabel("     "));
		buttonPanel.add(quitButton);
		buttonPanel.add(new JLabel("     "));
		
		buttonPanel.add(new JLabel("     ")); 
		buttonPanel.add(new JLabel("     ")); 
		buttonPanel.add(new JLabel("     "));
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.setPreferredSize(mainwindow.dim);
	}
	


	class AdaptatorNew implements ActionListener {
		private InterfaceMenu mainwindow;
		public AdaptatorNew(InterfaceMenu w) {
			mainwindow = w;
		}
	    public void actionPerformed(ActionEvent e) {
	        mainwindow.frame.remove(mainwindow.panel);
	        mainwindow.panel = new PanelNouvellePartie(mainwindow, PanelNouvellePartie.MODE_OFFLINE );
	        mainwindow.frame.getContentPane().add(mainwindow.panel);
	        mainwindow.frame.repaint();
	        mainwindow.frame.pack();
	    }
	}
	
	class AdaptatorLoad implements ActionListener {
		private InterfaceMenu mainwindow;
		public AdaptatorLoad(InterfaceMenu w) {
			mainwindow = w;
		}
	    public void actionPerformed(ActionEvent e) {
	    	 mainwindow.frame.remove(mainwindow.panel);
	    	 mainwindow.panel = new PanelCharger(mainwindow);
	    	 mainwindow.frame.getContentPane().add(mainwindow.panel);
	    	 mainwindow.frame.repaint();
	    	 mainwindow.frame.pack();
	    }
	}
	
	class AdaptatorOnline implements ActionListener {
		private InterfaceMenu mainwindow;
		public AdaptatorOnline(InterfaceMenu w) {
			mainwindow = w;
		}
	    public void actionPerformed(ActionEvent e) {
	    	new DialogConnexion(mainwindow); // connexion au serveur
	    }
	}

	
	class AdaptatorAbout implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	    	JOptionPane.showMessageDialog(null, "Tablut\n\nVersion : "+InterfaceMenu.VERSION+"\nAuteurs :\n  Andraws George\n  Casagrande Thierry\n  Lamraoui Si-Mohamed\n  Maurigault Thierry\n  Penkler Alexandre\n  Siclon Gautier", "A propos", JOptionPane.INFORMATION_MESSAGE);
	    }
	}	
	
	
	class AdaptatorQuit implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	        System.exit(0);
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
