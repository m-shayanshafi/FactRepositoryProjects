/**
 * PanelEnLigne.java
 * 
 * 
 * @author Si-Mohamed Lamraoui
 * @date 27.05.10
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import network.client.Party;

import joueur.Joueur;
import joueur.JoueurDistant;


public class PanelEnLigne extends JPanel
{

	private static final long serialVersionUID = 1L;
	private InterfaceMenu mainwindow;
	final JTable table;
	private MyTableModel dtm;
	private Object [][] datas;
	private int idTable[];
	private String[] title = {"Pseudo", "Niveau", "Armee", "Attaques"};
	public JoueurDistant player;
	public final JButton joinButton;
	
	public PanelEnLigne(InterfaceMenu w, JoueurDistant p)
	{
		mainwindow = w;
		mainwindow.frame.setTitle(InterfaceMenu.TITLE+" - Partie en ligne");
		player = p;
		
		
		// Panels
		this.setLayout(new BorderLayout());
		JPanel panel = new JPanel(new BorderLayout());
		JPanel dataPanel = new JPanel(new BorderLayout());
		JPanel buttonPanel1 = new JPanel(new BorderLayout());
		JPanel buttonPanel2 = new JPanel(new BorderLayout());
		JPanel buttonPanel3 = new JPanel(new BorderLayout());
		
		// Table
		table = new JTable(); 
		table.setPreferredSize(new Dimension(mainwindow.dim.width-50, mainwindow.dim.height-50));
		dtm = new MyTableModel(datas, title);
		table.setModel(dtm);
		table.setForeground(Color.blue);
		table.setFont(new Font("MS Gothic", Font.BOLD, 10));
		dataPanel.add(new JScrollPane(table), BorderLayout.CENTER);
		dataPanel.setPreferredSize(new Dimension(mainwindow.dim.width-50, mainwindow.dim.height-50));
		panel.add(dataPanel, BorderLayout.CENTER);

		
		// Buttons
		JButton refreshButton = new JButton("Rafraichir");
		refreshButton.addActionListener(new AdaptatorRefresh());
		buttonPanel1.add(refreshButton, BorderLayout.NORTH);
		buttonPanel1.add(new JLabel(" "), BorderLayout.CENTER);
		JButton cancelButton = new JButton("Deconnexion");
		buttonPanel1.add(cancelButton, BorderLayout.SOUTH);
		
		JButton newButton = new JButton("Creer une partie");
		newButton.addActionListener(new AdaptatorNew(mainwindow));
		buttonPanel2.add(newButton, BorderLayout.NORTH);
		buttonPanel2.add(new JLabel(" "), BorderLayout.CENTER);
		cancelButton.addActionListener(new AdaptatorCancel(mainwindow));
		joinButton = new JButton("Rejoindre la partie");
		joinButton.addActionListener(new AdaptatorJoin(mainwindow));
		joinButton.setEnabled(false);
		table.addMouseListener(new MouseAdapter(){
		      public void mouseReleased(MouseEvent e){
		    	  if(table.getSelectedRow()>=0) {
		    		  joinButton.setEnabled(true);
		    		  table.setRowSelectionInterval(table.getSelectedRow(), table.getSelectedRow());
		    	  }
		      } });
		buttonPanel2.add(joinButton, BorderLayout.SOUTH);
		buttonPanel3.add(buttonPanel1, BorderLayout.WEST);
		buttonPanel3.add(buttonPanel2, BorderLayout.EAST);
		panel.add(buttonPanel3, BorderLayout.SOUTH);
		
		// Main panel
		this.add(panel, BorderLayout.CENTER);
		this.add(new JLabel("     "), BorderLayout.NORTH);
		this.add(new JLabel("     "), BorderLayout.SOUTH);
		this.add(new JLabel("     "), BorderLayout.WEST);
		this.add(new JLabel("     "), BorderLayout.EAST);
		this.setPreferredSize(mainwindow.dim);
		refresh();
	}


	class AdaptatorRefresh implements ActionListener {
		public AdaptatorRefresh() {
		}
	    public void actionPerformed(ActionEvent e) {
	    	 refresh();
	    }
	}	
	
	
	class AdaptatorJoin implements ActionListener {
		private InterfaceMenu mainwindow;
		public AdaptatorJoin(InterfaceMenu w) {
			mainwindow = w;
		}
	    public void actionPerformed(ActionEvent e) {
	    	
	    	// TODO lancer partie en ligne ici 
	    	int partyid = Integer.valueOf((Integer)idTable[table.getSelectedRow()]);
	    	boolean error = player.joinPartie(partyid);
	    	/// TODO gestion erreur
	    	String s = (String) datas[table.getSelectedRow()][3]; // recupere l'id de la partie
	    	boolean selected;
	    	if(s.equalsIgnoreCase("suedois"))
				selected = Joueur.MOSCOVITE;
			else
				selected = Joueur.SUEDOIS;
	        /*InterfaceJeu game = new InterfaceJeu("Online game", selected, player.getPseudo(), player);
	        Thread t = new Thread(game);
	        t.start();*/
	    	/*MainWindow m = new MainWindow(selected, player.getPseudo(), player);
    		Thread t = new Thread(m);
            t.start();*/
	    	SwingUtilities.invokeLater(new Chat(player.getPseudo(), player));
	    	mainwindow.dispose();
	    	
	    }
	}	

	
	class AdaptatorCancel implements ActionListener {
		private InterfaceMenu mainwindow;
		public AdaptatorCancel(InterfaceMenu w) {
			mainwindow = w;
		}
	    public void actionPerformed(ActionEvent e) {
	    	mainwindow.frame.remove(mainwindow.panel);
	        mainwindow.panel = new PanelMenu(mainwindow);
	        mainwindow.frame.getContentPane().add(mainwindow.panel);
	        mainwindow.frame.repaint();
	        mainwindow.frame.pack();
	    }
	}	

	
	class AdaptatorNew implements ActionListener {
		private InterfaceMenu mainwindow;
		public AdaptatorNew(InterfaceMenu w) {
			mainwindow = w;
		}
	    public void actionPerformed(ActionEvent e) {
	    	 mainwindow.frame.remove(mainwindow.panel);
	    	 PanelNouvellePartie newPartie = new PanelNouvellePartie(mainwindow, PanelNouvellePartie.MODE_ONLINE);
	    	 mainwindow.panel = newPartie;
	    	 newPartie.player = player;
	    	 mainwindow.frame.getContentPane().add(mainwindow.panel);
	    	 mainwindow.frame.repaint();
	    	 mainwindow.frame.pack();
	    }
	}
	
	
	public class MyTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;
		public MyTableModel(Object[][] data, Object[] names) {
			super(data, names);
		}
		@Override
	    public boolean isCellEditable(int rowIndex, int columnIndex) {
	        return false;
	    }
	}

	
	private void refresh()
	{
		joinButton.setEnabled(false);
		int k = 0;
		Vector<Party> v = player.getParty(); // recupere les parties en cour sur le serveur
		Iterator<Party> i = v.iterator();
		datas = new Object[v.size()][4];
		idTable = new int[v.size()];
		while(i.hasNext())
		{
			Party p = i.next();
			idTable[k] = p.id;
			datas[k][0] = p.user;
			datas[k][1] = String.valueOf(p.lvl);
			if(p.type.equalsIgnoreCase("s"))
				datas[k][2] = "suedois";
			else
				datas[k][2] = "moscovite";
			if(p.skill==true)
				datas[k][3] = "activee";
			else
				datas[k][3] = "desactivee";
			k++;
		}
		DefaultTableModel newDtm = new DefaultTableModel(datas, title);
		table.setModel(newDtm); // met a jour la table

	}
	

	
	
}
