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
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JInternalFrame;

import entities.Konstanten;
import entities.KonstantenProtokoll;
import game.Spieler;
import game.VerwaltungClient;


public class AnzeigeKartenauswahl extends JInternalFrame implements Observer{

	public final int BREITE_SPIELER=150;
	private static final int HEIGHT = 100;
	
	private Vector<AnzeigeKarten> panels;
	private int spieleranzahl;
	
	public AnzeigeKartenauswahl( VerwaltungClient ss_verClient){
		this.setLayout(new GridLayout(1,1));
		ss_verClient.addObserver(this);
		//this.pack();
	}
	
	private void setAnzeigeKartenauswahl(String[] ss_spielernamen){
		AnzeigeKarten aktPanel;
		panels = new Vector<AnzeigeKarten>();
		spieleranzahl=ss_spielernamen.length;
		for(int i=0;i<spieleranzahl;i++){
			aktPanel = new AnzeigeKarten(SpielerEinheiten.FARBEN[i],ss_spielernamen[i],Bilder.MUENZE,Bilder.MUENZE);
			panels.addElement(aktPanel);
			this.add(aktPanel);
		}
		this.setIndividualDimensions();
	}
	
	public void setIndividualDimensions(){
		this.setSize(BREITE_SPIELER*spieleranzahl, HEIGHT);
	}
	
	/*public void reset(){
		AnzeigeKarten aktPanel;
		for(int i=0;i<panels.size();i++){
			aktPanel = (AnzeigeKarten)panels.elementAt(i);
			aktPanel.setKarte(Bilder.OK,1);
			aktPanel.setKarte(Bilder.OK,2);
		}
	}*/
	
	public void setKarten(int ss_spieler, String[] ss_karten){
		AnzeigeKarten aktPanel;
		
		aktPanel = panels.elementAt(ss_spieler);
		for(int i=0;i<ss_karten.length;i++){
			if(ss_karten[i].equals(Integer.toString(KonstantenProtokoll.K_BOTSCHAFTER))){
				aktPanel.setKarte(Bilder.BOTSCHAFTER_KL,i);
			}
			else if(ss_karten[i].equals(Integer.toString(KonstantenProtokoll.K_ZENTURIO))){
				aktPanel.setKarte(Bilder.ZENTURIO_KL,i);
			}
			else if(ss_karten[i].equals(Integer.toString(KonstantenProtokoll.K_EINHEITENKAUFEN))){
				aktPanel.setKarte(Bilder.ANWERBEN_KL,i);
			}
			else if(ss_karten[i].equals(Integer.toString(KonstantenProtokoll.K_ANGREIFEN))){
				aktPanel.setKarte(Bilder.ANGREIFEN_KL,i);
			}
			else if(ss_karten[i].equals(Integer.toString(KonstantenProtokoll.K_LAGERBAUEN))){
				aktPanel.setKarte(Bilder.LAGER_KL,i);
			}
			else if(ss_karten[i].equals(Integer.toString(KonstantenProtokoll.K_TRAINIEREN))){
				aktPanel.setKarte(Bilder.TRAINIEREN_KL,i);
			}
			else{
				aktPanel.setKarte(Bilder.OK,i);
			}
		}
	}

	public void update(Observable obs, Object ss_msg) {
		String msg =(String)ss_msg;
		String[] karten=new String[2];
		String[] a_spielernamen;
		Spieler[] a_spieler;
		VerwaltungClient verClient;

		if(obs instanceof VerwaltungClient&&(msg.equals(Konstanten.NOMSG)||msg.equals(Konstanten.SPIELSTART))){
			verClient=(VerwaltungClient)obs;
			
			if(msg.equals(Konstanten.SPIELSTART)){
				a_spielernamen=verClient.getA_Spielernamen();
				this.setAnzeigeKartenauswahl(a_spielernamen);
				}
			else if(ss_msg.equals("chat"))
				{}
			else{
				a_spieler=verClient.getA_spieler();
				for(int i=0;i<a_spieler.length;i++){
					karten[0]=a_spieler[i].getA_Karten()[0]+"";
					karten[1]=a_spieler[i].getA_Karten()[1]+"";
					this.setKarten(a_spieler[i].get_Spielernummer(),karten);
				}
				for(int i=0;i<a_spieler.length;i++){
					if(a_spieler[i].isAktuellerSpieler()){
						this.setAktSpieler(a_spieler[i].get_Spielernummer());
					}
				}
			}
		}
			
	}
		
	public void setAktSpieler(int spielernummer){
		AnzeigeKarten aktPanel;
		for(int i=0;i<panels.size();i++){
			aktPanel = panels.elementAt(i);
			if(i==spielernummer){aktPanel.setBackground(new Color(255,255,130));}
			else{aktPanel.setBackground(Color.getHSBColor(100,0,10));}
		}
	}
	
}

