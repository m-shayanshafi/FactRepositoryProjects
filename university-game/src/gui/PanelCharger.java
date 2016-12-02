/**
 * PanelCharger.java
 * 
 * 
 * @author Si-Mohamed Lamraoui + Thierrys 
 * @date 27.05.10
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import moteur.MoteurMonojoueur;
import moteur.Plateau;

import joueur.Joueur;



public class PanelCharger extends JPanel
{


	private static final long serialVersionUID = 1L;
	private InterfaceMenu mainwindow;
	final JTable table;


	public PanelCharger(InterfaceMenu w)
	{

		mainwindow = w;
		mainwindow.frame.setTitle(InterfaceMenu.TITLE+" - Charger une partie");
		
		// Panels
		this.setLayout(new BorderLayout());
		JPanel panel = new JPanel(new BorderLayout());
		JPanel dataPanel = new JPanel(new BorderLayout());
		dataPanel.setPreferredSize(new Dimension(mainwindow.dim.width-50, mainwindow.dim.height-50));
		JPanel infoPanel = new JPanel(new GridLayout(1,2));
		final JPanel textPanel = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel(new BorderLayout());
		Font font = new Font("MS Gothic", Font.BOLD, 10);
		
		// Table
		final Object [][] datas1 = creerDatas1();
		final String [][] datas2 = creerDatas2();
		
		String[] titreColonnes = { "Nom",  "Date"};
		table = new JTable(); 
		table.setFont(font);
		table.addMouseListener(new MouseAdapter(){
		      public void mouseReleased(MouseEvent e){
		    	  if(table.getSelectedRow()>=0) {
		    		  table.setRowSelectionInterval(table.getSelectedRow(), table.getSelectedRow());
		    	  }
		      } });
		table.setPreferredSize(new Dimension(mainwindow.dim.width-50, mainwindow.dim.height-50));
		MyTableModel dtm = new MyTableModel(datas1, titreColonnes);
		table.setModel(dtm);
		dataPanel.add(new JScrollPane(table), BorderLayout.CENTER);
		dataPanel.add(new JLabel("     "), BorderLayout.SOUTH);
		final JButton loadButton = new JButton("Charger");
		if(datas1.length>0) {
			table.setRowSelectionInterval(0, 0);
		} else {
			 loadButton.setEnabled(false);
		}
		infoPanel.add(dataPanel);
		
		// Description
		final JTextArea textArea =  new JTextArea();
		if(datas1.length>0) 
			textArea.setText("Le "+datas1[0][1]+" vous avez combattu\nl'ennemie "+datas2[0][0]+" en mode "+datas2[0][1]+".");
		textArea.setBackground(this.getBackground());
		textArea.setEditable(false);
		final ImageIcon suedois = createImageIcon("/images/pion-save-suedois.png", "");
		final ImageIcon moscovite = createImageIcon("/images/pion-save-moscovite.png", "");
		final JLabel img = new JLabel(suedois);
		if(datas1.length>0 && datas2[0][0]=="moscovite") 
			img.setIcon(suedois);
		else 
			img.setIcon(moscovite);
		if(img!=null)
			textPanel.add(img, BorderLayout.NORTH);
		textPanel.add(textArea, BorderLayout.CENTER);
		textPanel.setBorder(BorderFactory.createEmptyBorder(10,30,20,20));
		infoPanel.add(textPanel);
		panel.add(infoPanel, BorderLayout.CENTER);

		JButton cancelButton = new JButton("Annuler");
		buttonPanel.add(cancelButton, BorderLayout.WEST);
		cancelButton.addActionListener(new AdaptatorCancel(mainwindow));
		table.addMouseListener(new MouseAdapter(){
		      public void mouseClicked(MouseEvent e){
		    	  int row =  table.getSelectedRow();
		    	  if(row>=0)
		    		  loadButton.setEnabled(true);
		    	  textArea.setText("Le "+datas1[row][1]+" vous avez combattu\nl'ennemie "+datas2[row][0]+" en mode "+datas2[row][1]+".");
		    	  if(datas2[row][0]=="moscovite") 
		    		  img.setIcon(suedois);
		    	  else
		    		  img.setIcon(moscovite);
		      }  
		});
		buttonPanel.add(loadButton, BorderLayout.EAST);
		loadButton.addActionListener(new AdaptatorLoad(mainwindow));
		panel.add(buttonPanel, BorderLayout.SOUTH);
		
		this.add(panel, BorderLayout.CENTER);
		this.add(new JLabel("     "), BorderLayout.NORTH);
		this.add(new JLabel("     "), BorderLayout.SOUTH);
		this.add(new JLabel("     "), BorderLayout.WEST);
		this.add(new JLabel("     "), BorderLayout.EAST);
		this.setPreferredSize(mainwindow.dim);
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

	class AdaptatorLoad implements ActionListener {
		private InterfaceMenu mainwindow;
		public AdaptatorLoad(InterfaceMenu w) {
			mainwindow = w;
		}
	    public void actionPerformed(ActionEvent e) {
	    	// TODO charger le fichier selectionne
	    	// TODO lancer le jeu ici
	    	// int row = table.getSelectedRow(); // difficultee
	    	// datas1[row][0];
	    	
	    	File file = new File("");
	    	try {
	    		Scanner s = new Scanner(file);
	    		// difficulte
	    		int dif = s.nextInt();
	    		String difficulte = new String("");
	    		if (dif == 1)
	    			difficulte = "facile";
	    		else if (dif == 2)
	    			difficulte = "normal";
	    		else if (dif == 3)
	    			difficulte = "difficile";
	    		else
	    			difficulte = "ultime";
	    		// camp
	    		int c = s.nextInt();
	    		boolean camp;
	    		if (c == 1)
	    			camp = Joueur.SUEDOIS;
	    		else
	    			camp = Joueur.MOSCOVITE;
	    		// date
	    		long d = s.nextLong(); // ne sert pas au chargement mais il faut quand même passer l'entier dans le scanner
	    		//création du moteur
	    		// TODO : crée une interface jeu, quand les constructeurs seront à jour
	    		//InterfaceJeu jeu = new InterfaceJeu("super titre");
	            //Thread t = new Thread(jeu);
	            //t.start();
	    		
	    		// TODO chargement du plateau du moteur à partir du fichier
	    		//for(int i=0;i<9;i++)
	    		//	for(int j=0;j<9;j++)
	    		// 		...
	    		
	    		
	    		mainwindow.dispose();
			} catch (IOException exception) {
				System.out.println("Impossible de sauver dans le fichier selectionne");
			}
	    	
	    	
	    	
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
	
	public Object [][] creerDatas1(){
		File directory = new File("save");
		File [] liste = directory.listFiles();
		Object [][] resultat = new Object[liste.length][2];
		for(int i=0;i<liste.length;i++){
			String path = liste[i].getPath();
			// modification du path pour enlever le save/
			path = path.substring(5);
			String date = new String("");
			Scanner s;
			try {
				s = new Scanner(liste[i]);
				// difficulte
	    		s.nextInt();
	    		// camp
	    		s.nextInt();
	    		// date
	    		long d = s.nextLong();
	    		Date dateTmp = new Date();
	    		dateTmp.setTime(d);
	    		String stringDate = dateTmp.toString();
	    		
	    		Scanner scannerTmp = new Scanner(stringDate);
	    		scannerTmp.next();
	    		System.out.println("" + dateTmp.toString());
	    		// mois
	    		String month = scannerTmp.next();
	    		
	    		System.out.println(month + "dfg");
	    		month = month.trim();
	    		System.out.println(month + "dfg");
	    		if (month.equalsIgnoreCase("jan"))
	    			month = "01";
	    		else if (month.equalsIgnoreCase("feb"))
	    			month = "02";
	    		else if (month.equalsIgnoreCase("mar"))
	    			month = "03";
	    		else if (month.equalsIgnoreCase("apr"))
	    			month = "04";
	    		else if (month.equalsIgnoreCase("may"))
	    			month = "05";
	    		else if (month.equalsIgnoreCase("jun"))
	    			month = "06";
	    		else if (month.equalsIgnoreCase("jul"))
	    			month = "07";
	    		else if (month.equalsIgnoreCase("aug"))
	    			month = "08";
	    		else if (month.equalsIgnoreCase("sep"))
	    			month = "09";
	    		else if (month.equalsIgnoreCase("oct"))
	    			month = "10";
	    		else if (month.equalsIgnoreCase("nov"))
	    			month = "11";
	    		else
	    			month = "12";
	    		// jour
	    		int day = scannerTmp.nextInt();
	    		// année
	    		scannerTmp.next();
	    		scannerTmp.next();
	    		String year = scannerTmp.next();
	    	
	    		date = String.valueOf(day) + ":" + month + ":" + year;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resultat[i][0] = path;
			resultat[i][1] = date;
		}
		return resultat;
	}
	
	public String [][] creerDatas2(){
		File directory = new File("save");
		File [] liste = directory.listFiles();
		String [][] resultat = new String[liste.length][2];
		for(int i=0;i<liste.length;i++){
			Scanner s;
			String difficulte = new String("");
			String camp = new String("");;
			try {
				s = new Scanner(liste[i]);
				// difficulte
				int dif = s.nextInt();
				if (dif == 1)
					difficulte = "facile";
				else if (dif == 2)
					difficulte = "normal";
				else if (dif == 3)
					difficulte = "difficile";
				else
					difficulte = "ultime";
				// camp
				int c = s.nextInt();
				if (c == 1)
					camp = "suedois";
				else
					camp = "moscovites";
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resultat[i][0] = camp;
			resultat[i][1] = difficulte;
		}
		return resultat;
	}


}
