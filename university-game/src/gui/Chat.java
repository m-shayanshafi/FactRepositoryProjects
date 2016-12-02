package gui;
import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;
import java.util.Scanner;

import javax.swing.*;

import joueur.JoueurDistant;


public class Chat extends JComponent implements Runnable
{

	public static int MAX_MSG_LENGTH = 100;
	public static String CHAT_INIT = "\n\n\n\n\n\n\n\n\n\n\n";
	public static int CHAT_MAX_LINE_COUNT = 9;
	
	private String pseudo;
	private JoueurDistant player;
	public JTextArea discuss;
	public JTextField tip;
	public String messageDistant = ""; // message du joueur distant (voir Recieve.java)
	public String pseudoDistant; // pseudo du joueur distant
	
	public Chat(String s, JoueurDistant p)
	{
		pseudo = s;
		player = p;
		pseudoDistant = player.getPlayerPseudo();
		player.getReceive().setChat(this);
	}
	
	
	public void run() 
	{
		JFrame frame = new JFrame("Test chat");
		JPanel panel = new JPanel(new BorderLayout());
		Font font = new Font("MS Gothic", Font.BOLD, 12);
		
		discuss = new JTextArea(CHAT_INIT);
		discuss.setBackground(Color.black);
		discuss.setForeground(Color.white);
		discuss.setPreferredSize(new Dimension(300,155));
		discuss.setFont(font);
		discuss.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		discuss.setEditable(false);
		tip = new JTextField();
		tip.setBackground(Color.black);
		tip.setForeground(Color.white);
		tip.setFont(font);
		tip.addActionListener(new AdaptatorField());
		panel.add(new JScrollPane(discuss), BorderLayout.CENTER); // TODO fix scroll
		panel.add(tip, BorderLayout.SOUTH);
		frame.add(panel);

		frame.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth()/2;
		int height = (int) screenSize.getHeight()/2;
		Point loc = new Point(width-(frame.getSize().width/2), height-(frame.getSize().height/2));
		frame.setLocation(loc);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	

	
	public static void main(String args[])
	{
		SwingUtilities.invokeLater(new Chat("Simo", null));
	}
	
	
	
	class AdaptatorField implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	    	String message = e.getActionCommand(); // recupere le text du Field
	    	addMessage(player.getPseudo(), message);
	    	player.sendMessage(message);
	    	
	    }
	}
	

	/**
	 * Ajoute un message au chat
	 * 
	 * @param msg
	 */
	public void addMessage(String pseudo, String msg) {
		if(!msg.isEmpty() || msg.length()>MAX_MSG_LENGTH) {
    		tip.setText(""); // efface le champ texte
    		String nnew = "";
    		String old = discuss.getText();
    		Scanner s = new Scanner(old);
    		s.useDelimiter("\n");
    		s.next(); // efface la premiere ligne
    		while(s.hasNext()) {
    			nnew = nnew + "\n" + s.next() ;
    		}
    		discuss.setText(nnew+"\n"+pseudo+": "+msg);
    	}
	}
	
	
	
	
}
