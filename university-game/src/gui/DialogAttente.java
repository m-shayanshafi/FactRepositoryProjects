/**
 * DialogAttente.java
 * 
 *
 * 
 * @author Si-Mohamed Lamraoui
 * @date 28.05.10
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import javax.swing.*;

import joueur.JoueurDistant;
import network.ProtocolTablut;


public class DialogAttente extends JDialog implements Runnable
{
	
	private static final long serialVersionUID = 1L;
	private InterfaceMenu mainwindow;
	public JoueurDistant player;
	

	public DialogAttente(InterfaceMenu w, JoueurDistant p)
	{
		mainwindow = w;
		player = p;
		Thread t = new Thread(this);
		t.start();

       	this.setResizable(false);
    	this.setModal(true);

    	// Panels
    	JPanel panel = new JPanel(new BorderLayout());

    	// Contents
		JLabel label = new JLabel("Attente d'un autre joueur.");
		panel.add(label, BorderLayout.NORTH);
		AnimationAttente wait = new AnimationAttente();
		wait.setPreferredSize(new Dimension(60, 57));
		panel.add(wait, BorderLayout.CENTER);
		//info.add(wait, BorderLayout.CENTER);
		JButton cancel = new JButton("Annuler");
		cancel.addActionListener(new AdaptatorCancel(this));
		panel.add(cancel, BorderLayout.SOUTH);
		
		// Main panel
		panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		this.add(panel);
		this.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth()/2;
		int height = (int) screenSize.getHeight()/2;
		Point loc = new Point(width-(this.getSize().width/2), height-(this.getSize().height/2));
		this.setLocation(loc);
		this.setVisible(true);
	}
	
	
	/**
	 * Attente d'un autre joueur
	 * 
	 */
	public void run()
	{
		player.waitServer();
		Scanner s = new Scanner(player.getReceive().getBuffer());
    	if(s.next().equalsIgnoreCase(ProtocolTablut.JOIN)) { // un autre joueur a rejoin la partie
    		//timer.stop();
    		mainwindow.partyStarted = true;
    		this.dispose();
    	}
    	else
    		System.out.println("HAHAHA");
    	this.dispose();
	}
	
	
	
	
	class AdaptatorCancel implements ActionListener {
		private DialogAttente dial;
		public AdaptatorCancel(DialogAttente  d) {
			dial = d;
		}
	    public void actionPerformed(ActionEvent e) {
	    	dial.dispose();
	    }
	}	
	

	public class AnimationAttente extends JComponent implements AdaptateurDessin {
		private static final long serialVersionUID = 1L;
		private Animation anim;
		public AnimationAttente() {
			anim = new Animation(this, "/images/anim_wait/anim_wait", 4, 500, true);
			anim.start();
		}
		public void paintComponent(Graphics g) {
			Graphics2D draw = (Graphics2D) g;
			draw.drawImage(anim.get(), 0, 0, null);
		}  
	}

	
}
