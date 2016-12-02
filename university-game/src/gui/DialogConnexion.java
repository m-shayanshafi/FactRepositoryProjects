/**
 * PanelConnexion.java
 * 
 *
 * 
 * @author Si-Mohamed Lamraoui
 * @date 21.05.10
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import joueur.JoueurDistant;


public class DialogConnexion extends JDialog 
{
	
	private static final long serialVersionUID = 1L;
	public InterfaceMenu mainwindow;
	JTextField idField;
	JPasswordField pwdField;
	public JoueurDistant player;
	
	public DialogConnexion(InterfaceMenu w)
	{

		mainwindow = w;
		this.setTitle("Connexion au serveur de jeu");
       	this.setResizable(false);
    	this.setModal(true);
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		this.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		JPanel userPanel = new JPanel();
		userPanel.setLayout(new GridLayout(3,2));
		
		JLabel idLabel = new JLabel("Identifiant : ");
		userPanel.add(idLabel);
		idField = new JTextField();
		userPanel.add(idField);
		JLabel pwdLabel = new JLabel("Mot de passe : ");
		userPanel.add(pwdLabel);
		pwdField = new JPasswordField();
		userPanel.add(pwdField);
		panel1.add(userPanel, BorderLayout.NORTH);
		panel1.add(new JLabel("     "), BorderLayout.WEST);
		panel1.add(new JLabel("     "), BorderLayout.EAST);
		
		final String msg = "Creer un compte";
		final String underlineMsg = "<html><u>Creer un compte</u></html>";
		final JLabel msgLabel = new JLabel(msg);
		msgLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		msgLabel.setForeground(Color.blue);
		msgLabel.addMouseListener(new MouseAdapter(){
		      public void mouseEntered(MouseEvent e){
		    	  msgLabel.setText(underlineMsg);
		      }
		      public void mouseExited(MouseEvent e){
		    	  msgLabel.setText(msg);
		      }
		      public void mouseClicked(MouseEvent e){
		    	  openWeb("www.tablut-online.fr");
		      }
		      
		});
		userPanel.add(msgLabel, BorderLayout.CENTER);
		
		JButton buttonCancel = new JButton("Annuler");
		buttonCancel.addActionListener(new AdaptatorCancel(this));
		buttonPanel.add(buttonCancel, BorderLayout.WEST);
		JButton buttonPlay = new JButton("Connexion");
		buttonPlay.addActionListener(new AdaptatorConnect(this, mainwindow));
		buttonPanel.add(buttonPlay, BorderLayout.EAST);
		panel1.add(buttonPanel, BorderLayout.SOUTH);
		
		this.add(panel1, BorderLayout.CENTER);
		this.add(new JLabel("     "), BorderLayout.NORTH);
		this.add(new JLabel("     "), BorderLayout.SOUTH);
		this.add(new JLabel("     "), BorderLayout.WEST);
		this.add(new JLabel("     "), BorderLayout.EAST);

		pack();
		/** TEMPORAIRE **/
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth()/2;
		int height = (int) screenSize.getHeight()/2;
		Point loc = new Point(width-(getSize().width/2), height-(getSize().height/2));
		setLocation(loc);
		/** TEMPORAIRE **/
		setVisible(true);
	}
	
	

	class AdaptatorCancel implements ActionListener {
		private DialogConnexion dial;
		public AdaptatorCancel(DialogConnexion d) {
			dial = d;
		}
	    public void actionPerformed(ActionEvent e) {
	    	dial.dispose(); // retour au menu principale
	    }
	}	

	class AdaptatorConnect implements ActionListener {
		private InterfaceMenu mainwindow;
		private DialogConnexion dial;
		public AdaptatorConnect(DialogConnexion d, InterfaceMenu w) {
			dial = d;
			mainwindow = w;
		}
	    public void actionPerformed(ActionEvent e) {
	    	
	    	
	    	//////////////////////////////////
	    	mainwindow.networkplayer = new JoueurDistant(idField.getText(), mainwindow);
	    	player = mainwindow.networkplayer;
	    	/////////////////////////////////
	    	
	    	char[] pwd = pwdField.getPassword();
	    	if(idField.getText().length()==0) {
	    		 JOptionPane.showMessageDialog(mainwindow,
	 	                "Veulliez entrer un identifiant!", "Erreur",
	 	                JOptionPane.ERROR_MESSAGE);
	    		 return;
	    	}
	    	if(pwd.length==0) {
	    		 JOptionPane.showMessageDialog(mainwindow,
	 	                "Veulliez entrer un mot de passe!", "Erreur",
	 	                JOptionPane.ERROR_MESSAGE);
	    		 return;
	    	}
	    	
	    	// TODO gerer la destruction de la fenetre si server en maintenance
	    		
	        //if(joueurDistants.isPasswordCorrect(pwd)) {
			if(player.connect(idField.getText(), pwd.toString())) {
	    	//if(!idField.getText().equalsIgnoreCase("tablut")) { // TODO test
	            //JOptionPane.showMessageDialog(mainwindow, "Connexion etablie!");
	    	 	mainwindow.frame.remove(mainwindow.panel);
		        mainwindow.panel = new PanelEnLigne(mainwindow,  player);
		        mainwindow.frame.getContentPane().add(mainwindow.panel);
		        mainwindow.frame.repaint();
		        mainwindow.frame.pack();
	    		dial.dispose();
	        } else {
	            JOptionPane.showMessageDialog(mainwindow,
	                "Mot de passe ou identifiants incorrectes, essayez encore!", "Erreur",
	                JOptionPane.ERROR_MESSAGE);
	        }
	        // par securite on efface le mot de passe en memoire
		    //Arrays.fill(pwd, '0');
	        //pwdField.selectAll();
	        //pwdField.resetFocus();
	    }
	}	

	
	/**
	 * Ouvre un navigateur web et affiche le site web pointe par url. 
	 * 
	 */
	private void openWeb(String url) // throws IOException
	{
		//String browserDir = "C:\\Program Files\\Internet Explorer\\IExplore";
		String browserDir = "/usr/bin/firefox";
		try
		{
			Runtime r = Runtime.getRuntime();
			r.exec(browserDir + " " + url); // le lien est dans urlName
		}
		catch(FileNotFoundException fnfe)
		{
			String info = browserDir + "(fnfe) non trouve !!!";
			JOptionPane.showMessageDialog(null,info);
		}
		catch(IOException ioe)
		{
			String info = browserDir + "\n" + ioe;
			JOptionPane.showMessageDialog(null,info);
		}
		catch(Exception e)
		{
			String info = "catch(Exception e) " + e + url;
			javax.swing.JOptionPane.showMessageDialog(null,info);
		}
	}
	
	
	
	
	
	
	
	
}
