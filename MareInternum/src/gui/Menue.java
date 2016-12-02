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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import tools.Localization;

import entities.Konstanten;
import game.Main;
import game.Spielverwaltung;

public class Menue implements ActionListener,Observer{

	private Hauptfenster parent;
	private Vector[] menueItems;
	private Spielverwaltung meineVerwaltung;
	private final String SEXIT=Localization.getInstance().getString("Quit");
	private final String SBEENDEN=Localization.getInstance().getString("QuitGame");
	private final String STEILNEHMEN=Localization.getInstance().getString("ParticipateGame");
	private final String SSTARTEN=Localization.getInstance().getString("NewGame");
	private final String SCHATON=Localization.getInstance().getString("Chat");
	private final String SINFO=Localization.getInstance().getString("Information");
	private final String SKLKARTE=Localization.getInstance().getString("SmallMap");
	private final String SPREFERENCES=Localization.getInstance().getString("Preferences");
	
	public Menue(Spielverwaltung ss_verwaltung,Hauptfenster ss_parent){
		
		meineVerwaltung = ss_verwaltung;
		meineVerwaltung.addObserver(this);
		parent=ss_parent;
		menueItems=new Vector[2];
		for(int i=0;i<menueItems.length;i++){
			menueItems[i]=new Vector();
		}
		JMenuBar menueBar = new JMenuBar();
		this.add_MenuBarItems(menueBar);
		parent.setJMenuBar(menueBar);
	}
	
	private void add_MenuBarItems(JMenuBar ss_menueBar){
		JMenu itemSpiel,itemInfo,itemFenster;
		itemSpiel = new JMenu(Localization.getInstance().getString("Game"));
		this.add_MenuItem(itemSpiel,SSTARTEN,this,0);
		this.add_MenuItem(itemSpiel,STEILNEHMEN,this,0);
		this.add_MenuItem(itemSpiel,SPREFERENCES,this,0);
		this.add_MenuItem(itemSpiel,SBEENDEN,this,0);
		this.add_MenuItem(itemSpiel,SEXIT,this,0);
		
		itemFenster=new JMenu(Localization.getInstance().getString("Window"));
		this.add_CheckableMenuItem(itemFenster,SKLKARTE,this,0,false);
		this.add_CheckableMenuItem(itemFenster,SCHATON,this,0,true);
		
		itemInfo=new JMenu(Localization.getInstance().getString("?"));
		this.add_MenuItem(itemInfo,SINFO,this,0);
		ss_menueBar.add(itemSpiel);
		ss_menueBar.add(itemFenster);
		ss_menueBar.add(itemInfo);
	}
	
	private void add_MenuItem(JMenu ss_menue,String ss_name, ActionListener ss_al,int nr){
		JMenuItem item;
		item = new JMenuItem(ss_name);
		item.addActionListener(ss_al);
		ss_menue.add(item);
		if(ss_name.equals(SBEENDEN)||ss_name.equals(SCHATON)||ss_name.equals(SKLKARTE)){
			item.setEnabled(false);
		}
		menueItems[nr].addElement(item);
	}
	
	private void add_CheckableMenuItem(JMenu ss_menue,String ss_name, ActionListener ss_al,int nr, boolean isChecked){
		JMenuItem item;
		item = new JCheckBoxMenuItem(ss_name);
		item.setSelected(isChecked);
		item.addActionListener(ss_al);
		ss_menue.add(item);
		if(ss_name.equals(SBEENDEN)||ss_name.equals(SCHATON)||ss_name.equals(SKLKARTE)){
			item.setEnabled(false);
		}
		menueItems[nr].addElement(item);
	}
	
	public void actionPerformed(ActionEvent e){
		String cmd = e.getActionCommand();
		if(cmd.equals(SEXIT)){
			meineVerwaltung.stoppe_Spiel();
			System.out.println("Spiel beendet");
			System.exit(0);
		}
		if(cmd.equals(SBEENDEN)){
			meineVerwaltung.stoppe_Spiel();
		}
		if(cmd.equals(SSTARTEN)){
			
			String s = Main.showInputDialog(Localization.getInstance().getString("ChoosePort"), ""+Konstanten.STANDARDPORT);

			if(s!=null){
				try {
					int port=Integer.parseInt(s);
					meineVerwaltung.starte_neuesSpiel(port);
				} catch (Exception e1) {
					
					if(Konstanten.DEBUG>0){
						e1.printStackTrace();
					}
					
					Main.showErrorMessage(Localization.getInstance().getString("WrongPort"));
				}
			}
			
		}
		if(cmd.equals(STEILNEHMEN)){
			meineVerwaltung.willTeilnehmen();
		}
		if(cmd.equals(SCHATON)){
			parent.setChatVisible(((JCheckBoxMenuItem)e.getSource()).getState());
		}
		if(cmd.equals(SKLKARTE)){
			parent.setGuiComponentVisible(KleineKartePanel.class.getName(), ((JCheckBoxMenuItem)e.getSource()).getState());
		}
		if(cmd.equals(SINFO)){
			Main.showInfoDialog();
		}
		if(cmd.equals(SPREFERENCES)){
			Main.showDlgPreferences();
		}
	}

	public void update(Observable obs, Object arg1) {
		JMenuItem aktItem;

		if(obs instanceof Spielverwaltung){
			for(int i=0;i<menueItems[0].size();i++){
				aktItem=(JMenuItem)menueItems[0].elementAt(i);
				if(aktItem.getText().equals(SSTARTEN)){
					if(meineVerwaltung.isSpielaktiv()||meineVerwaltung.isSpielstartend()||meineVerwaltung.isTeilnehmen()){
						aktItem.setEnabled(false);
					}
					else{
						aktItem.setEnabled(true);
					}
				}
				if(aktItem.getText().equals(STEILNEHMEN)){
					if(meineVerwaltung.isSpielaktiv()||meineVerwaltung.isSpielstartend()||meineVerwaltung.isTeilnehmen()){
						aktItem.setEnabled(false);
					}
					else{
						aktItem.setEnabled(true);
					}
					
				}
				if(aktItem.getText().equals(SPREFERENCES)){
					if(meineVerwaltung.isSpielaktiv()||meineVerwaltung.isSpielstartend()||meineVerwaltung.isTeilnehmen()){
						aktItem.setEnabled(false);
					}
					else{
						aktItem.setEnabled(true);
					}
				}
				if(aktItem.getText().equals(SBEENDEN)||aktItem.getText().equals(SCHATON)||aktItem.getText().equals(SKLKARTE)){
					aktItem.setEnabled(meineVerwaltung.isSpielaktiv());
				}
			}
		}
		
	}
}
