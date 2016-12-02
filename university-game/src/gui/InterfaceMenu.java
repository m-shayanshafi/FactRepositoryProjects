/**
 * InterfaceMenu.java
 * 
 * Menu principale de l'application.
 * 
 * @author Si-Mohamed Lamraoui
 * @date 27.05.10
 */

package gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import joueur.JoueurDistant;


public class InterfaceMenu extends JComponent implements Runnable
{

	private static final long serialVersionUID = 1L;
	public static String VERSION = "0.7";
	public static String TITLE = "Tablut "+VERSION;
	JFrame frame;
	JPanel panel;
	Dimension dim;
	JoueurDistant networkplayer;
	public boolean partyStarted = false; // Mode online seulement : vrai si deux joueur on rejoin une partie
	
	public void run() 
	{
		frame = new JFrame(TITLE);
		panel = new PanelMenu(this);		frame.add(panel);

		frame.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth()/2;
		int height = (int) screenSize.getHeight()/2;
		Point loc = new Point(width-(frame.getSize().width/2), height-(frame.getSize().height/2));
		frame.setLocation(loc);
		dim = panel.getSize();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void dispose() {
		frame.dispose();
	}
	
	public static void main(String args[])
	{
		SwingUtilities.invokeLater(new InterfaceMenu());
	}
	
}
