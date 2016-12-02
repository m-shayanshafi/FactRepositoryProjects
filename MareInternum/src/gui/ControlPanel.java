/*	 
*    This file is part of Mare Internum.
*
*	 Copyright (C) 2008,2009  Johannes Hoechstaedter
*
*    Mare Internum is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    Mare Internum is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with Mare Internum.  If not, see <http://www.gnu.org/licenses/>.
*
*/

package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import tools.Localization;
import entities.Konstanten;
import entities.KonstantenProtokoll;
import game.Spieler;
import game.VerwaltungClient;

public class ControlPanel extends JInternalFrame implements Observer{
	
	private static final int HEIGHT = 470;
	private static final int WIDTH = 300;
	
	private JTable table;
	private JScrollPane scrollPane;
	private JPanel infoTable;
	
	private JLabel lblMeinGeld;
	private JLabel lblTraining;
	private JLabel lblAktSpieler;
	private JLabel lblAktAktion;
	private JLabel lblStartspieler;
	private JButton btnWeiter;
	private JLabel turn;

	private int meinGeld=0;
	private String[] spalten;
	private String[][] zeilen;
	
	public ControlPanel (VerwaltungClient ss_verClient,MeinActionListener ss_actListener){
			
		ss_verClient.addObserver(this);

		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		
		JPanel north=new JPanel();
		north.setLayout(new GridLayout(1,2));
		btnWeiter = new JButton(Localization.getInstance().getString("NextTurn"));
		btnWeiter.setActionCommand("weiter");
		btnWeiter.addActionListener(ss_actListener);
		turn=new JLabel(new ImageIcon(ClassLoader.getSystemResource(Bilder.TURNSTOP)));
		north.add(btnWeiter);
		north.add(turn);
		spalten =new String[9];
		
		spalten[0]=Localization.getInstance().getString("Player");
		spalten[1]=Localization.getInstance().getString("Points");
		spalten[2]=Localization.getInstance().getString("Warlords");
		spalten[3]=Localization.getInstance().getString("State");
		spalten[4]=Localization.getInstance().getString("Wheat");
		spalten[5]=Localization.getInstance().getString("Wine");
		spalten[6]=Localization.getInstance().getString("Stone");
		spalten[7]=Localization.getInstance().getString("Clay");
		spalten[8]=Localization.getInstance().getString("Wool");
		
		
		this.add(north);
		this.add(this.get_eigeneDatenPanel());
		
		this.setSize(WIDTH, HEIGHT);
	}
	
	public JPanel get_eigeneDatenPanel(){
		JPanel daten = new JPanel();
		daten.setLayout(new GridLayout(5,2));
		
		//Geld,aktionsanzeige,startspieleranzeige,momentaner spieler	
		lblStartspieler = new JLabel("ich");
		lblStartspieler.setForeground(SpielerEinheiten.FARBEN[0]);
		
		lblAktSpieler = new JLabel("ich");
		lblAktSpieler.setForeground(SpielerEinheiten.FARBEN[0]);
		
		lblAktAktion = new JLabel();
		lblAktAktion.setPreferredSize(new Dimension(50, 50));
		lblAktAktion.setMinimumSize(new Dimension(50, 50));
		
		this.setAktAktion(1);
		lblTraining=new JLabel();
		meinGeld=Konstanten.STARTGELD;
		lblMeinGeld = new JLabel(""+Konstanten.STARTGELD);
		
		daten.add(new JLabel("   "+Localization.getInstance().getString("Beginner")+": "));
		daten.add(lblStartspieler);
		daten.add(new JLabel("   "+Localization.getInstance().getString("IndependentProvinces")+": "));
		daten.add(lblAktSpieler);
		daten.add(new JLabel("   "+Localization.getInstance().getString("Action")+": "));
		daten.add(lblAktAktion);
		daten.add(new JLabel("   "+Localization.getInstance().getString("Denarii")+" "));
		daten.add(lblMeinGeld);
		daten.add(new JLabel("   "+Localization.getInstance().getString("ForceLevel")+": "));
		daten.add(lblTraining);
		return daten;
	}
	
	/*public void setStartspieler(int ss_spieler){
		lblStartspieler.setForeground(SpielerEinheiten.FARBEN[ss_spieler]);
		lblStartspieler.setText(a_spielernamen[ss_spieler]);
	}*/
	public void setAktProvinzenzahl(int ss_provinzen){
		lblAktSpieler.setForeground(Color.BLACK);
		lblAktSpieler.setText(""+ss_provinzen);
	}
	public void setMeinGeld(int ss_geld){
		meinGeld =ss_geld;
		lblMeinGeld.setText(""+meinGeld);
	}
	
	public void setAktAktion(int ss_aktion){
		switch(ss_aktion){
		case KonstantenProtokoll.PROVINZWAEHLEN:
			lblAktAktion.setIcon(new ImageIcon(ClassLoader.getSystemResource(Bilder.PROVINZWAEHLEN)));
			lblAktAktion.setToolTipText(Localization.getInstance().getString("ChooseProvinceWhichYouWantToAnnex"));
			break;
		case KonstantenProtokoll.KARTENWAEHLEN:
			lblAktAktion.setIcon(new ImageIcon(ClassLoader.getSystemResource(Bilder.KARTENWAEHLEN)));
			lblAktAktion.setToolTipText(Localization.getInstance().getString("ChooseExactlyTwoActions"));
			break;
		case KonstantenProtokoll.TRAININEREN:
			lblAktAktion.setIcon(new ImageIcon(ClassLoader.getSystemResource(Bilder.TRAINIEREN)));
			lblAktAktion.setToolTipText(Localization.getInstance().getString("TrainTroops"));
			break;
		case KonstantenProtokoll.LAGERBAUEN:
			lblAktAktion.setIcon(new ImageIcon(ClassLoader.getSystemResource(Bilder.LAGER)));
			lblAktAktion.setToolTipText(Localization.getInstance().getString("BuildFortress")+" ("+Konstanten.KOSTENLAGER+"D/1Z)");
			break;
		case KonstantenProtokoll.EINHEITENKAUFEN:
			lblAktAktion.setIcon(new ImageIcon(ClassLoader.getSystemResource(Bilder.ANWERBEN)));
			lblAktAktion.setToolTipText(Localization.getInstance().getString("HireTroops"));
			break;
		case KonstantenProtokoll.ANGREIFEN:
			lblAktAktion.setIcon(new ImageIcon(ClassLoader.getSystemResource(Bilder.ANGREIFEN)));
			lblAktAktion.setToolTipText(Localization.getInstance().getString("ChooseRegionAndProvince"));
			break;
		case KonstantenProtokoll.VERTEIDIGEN:
			lblAktAktion.setIcon(new ImageIcon(ClassLoader.getSystemResource(Bilder.ANGREIFEN)));
			lblAktAktion.setToolTipText(Localization.getInstance().getString("DoYouWantToDefend"));
			break;
		default:
			lblAktAktion.setIcon(new ImageIcon(ClassLoader.getSystemResource(Bilder.SPQR)));
			lblAktAktion.setToolTipText(Localization.getInstance().getString("Wait"));
			break;
		}
		
	}
	
	public void setStartSpieler(String name,int id){
		
		this.lblStartspieler.setText(name);
		
		this.lblStartspieler.setForeground(SpielerEinheiten.FARBEN[id]);
		
	}
	
	public void setAktSpieler(String name,int id){
		
		this.lblAktSpieler.setText(name);
		
		this.lblAktSpieler.setForeground(SpielerEinheiten.FARBEN[id]);
		
	}
	
	public void setAktiv(boolean ss_isAktiv){
		
		if(ss_isAktiv){
			
			turn.setIcon(new ImageIcon(ClassLoader.getSystemResource(Bilder.TURN)));
			
		}
		else{
			
			turn.setIcon(new ImageIcon(ClassLoader.getSystemResource(Bilder.TURNSTOP)));
			
		}
	}


	public void update(Observable obs, Object ss_msg) {
		String msg=(String)ss_msg;
		Spieler aktSpieler;
		VerwaltungClient verClient;
		Spieler[] a_spieler;
		int[] a_waren;

		if(obs instanceof VerwaltungClient&&(msg.equals(Konstanten.NOMSG)||msg.equals(Konstanten.SPIELSTART))){
			verClient=(VerwaltungClient)obs;
			if(msg.equals(Konstanten.SPIELSTART)){				
				zeilen=new String[verClient.getSpieleranzahl()][9];
				for(int i=0;i<zeilen.length;i++){
					for(int j=0;j<zeilen[i].length;j++){
						zeilen[i][j]="X";
					}
				}
				
				table=new JTable(zeilen,spalten);
				table.setEnabled(false);
				
				scrollPane=new JScrollPane(table);
				scrollPane.setPreferredSize(new Dimension(250,120));
				table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				
				if (infoTable !=null){
					this.remove(infoTable);
				}
				
				infoTable=new JPanel();
				infoTable.add(scrollPane);
				this.add(infoTable);
			}
			else if(msg.equals(Konstanten.NOMSG)){
				a_spieler=verClient.getA_spieler();
				zeilen=new String[a_spieler.length][9];
				for(int i=0;i<a_spieler.length;i++){
					for(int j=0;j<a_spieler.length-1;j++){
						if(verClient.getPunkte(a_spieler[j].get_Spielernummer())<verClient.getPunkte(a_spieler[j+1].get_Spielernummer())){
							aktSpieler=a_spieler[j];
							a_spieler[j]=a_spieler[j+1];
							a_spieler[j+1]=aktSpieler;
						}
					}
				}
				CellRenderer aktRenderer =new CellRenderer();
				int[] reihenfolge=new int[a_spieler.length];
				for(int i=0;i<a_spieler.length;i++){
					reihenfolge[i]=a_spieler[i].get_Spielernummer();
				}
				aktRenderer=new CellRenderer();
				aktRenderer.setIdReihenfolge(reihenfolge);
				table.getColumn(spalten[0]).setPreferredWidth(80);
				table.getColumn(spalten[0]).setCellRenderer(aktRenderer);
				
				for(int i=1;i<9;i++){
					aktRenderer=new CellRenderer();
					aktRenderer.setIdReihenfolge(reihenfolge);
					table.getColumn(spalten[i]).setPreferredWidth(55);
					table.getColumn(spalten[i]).setCellRenderer(aktRenderer);
					
				}
				
				
				for(int i=0;i<a_spieler.length;i++){
					
					zeilen[i][0]=""+a_spieler[i].getName();
					zeilen[i][1]=""+verClient.getPunkte(a_spieler[i].get_Spielernummer());
					zeilen[i][2]=""+a_spieler[i].get_zenturios();
					zeilen[i][3]=""+a_spieler[i].getBotschafter();
					
					a_waren=verClient.getA_Waren(a_spieler[i].get_Spielernummer());
					for(int j=0;j<a_waren.length;j++){
						zeilen[i][4+j]=""+a_waren[j];
					}
				}
				
				for(int i=0;i<zeilen.length;i++){
					for(int j=0;j<zeilen[i].length;j++){
						table.getModel().setValueAt(zeilen[i][j],i,j);
					}
				}
				
				
				this.setAktiv(verClient.isSpieleraktiv());
				this.setAktAktion(verClient.getAktAktion());
				for(int i=0;i<a_spieler.length;i++){
					//if(a_spieler[i].isAktuellerSpieler()){this.setAktSpieler(a_spieler[i].getName(),a_spieler[i].get_id());}
					if(a_spieler[i].isStartspieler()){this.setStartSpieler(a_spieler[i].getName(),a_spieler[i].get_Spielernummer());}
						this.setMeinGeld(verClient.getMeinSpielerByID().getGeld());
						lblTraining.setText(""+verClient.getMeinSpielerByID().getTrainingsFaktor());
				}
			
				this.setAktProvinzenzahl(verClient.getZFreieProvinzen());
			
			}
			
		}
		
	}
	
}
