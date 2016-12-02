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

import tools.Localization;

import entities.Konstanten;
import entities.KonstantenProtokoll;
import game.VerwaltungClient;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class AktionenAuswahl extends JInternalFrame implements Observer,ActionListener,Abfragbar{

	private final int BTN_ANZAHL=6;
	private JButton[] btn; 
	private JRadioButton[] chk;
	private String[] a_actionCommand ={"Botschafter","Zenturio","Angriff","Anwerben","Lager","Steigern"};
	
	public AktionenAuswahl(VerwaltungClient obs){
		obs.addObserver(this);
		this.setLayout(new BorderLayout());
		this.add(BorderLayout.NORTH,this.getAktionenAuswahlLabel());
		this.pack();
	}
	
	public JPanel getAktionenAuswahlLabel(){
		
		int z=0;
		JPanel label = new JPanel();
		label.setLayout(new GridLayout(6,2));
		btn= new JButton[BTN_ANZAHL];
		chk = new JRadioButton[BTN_ANZAHL];
		
		btn[KonstantenProtokoll.K_BOTSCHAFTER] = new JButton();
		btn[KonstantenProtokoll.K_BOTSCHAFTER].setIcon(new ImageIcon(ClassLoader.getSystemResource(Bilder.BOTSCHAFTER)));
		btn[KonstantenProtokoll.K_BOTSCHAFTER].addActionListener(this);
		btn[KonstantenProtokoll.K_BOTSCHAFTER].setActionCommand(a_actionCommand[0]);
		btn[KonstantenProtokoll.K_BOTSCHAFTER].setToolTipText(Localization.getInstance().getString("SendAmbassador"));
		chk[KonstantenProtokoll.K_BOTSCHAFTER] = new JRadioButton();
		
		btn[KonstantenProtokoll.K_ZENTURIO] = new JButton();
		btn[KonstantenProtokoll.K_ZENTURIO].setIcon(new ImageIcon(ClassLoader.getSystemResource(Bilder.ZENTURIO)));
		btn[KonstantenProtokoll.K_ZENTURIO].addActionListener(this);
		btn[KonstantenProtokoll.K_ZENTURIO].setActionCommand(a_actionCommand[1]);
		btn[KonstantenProtokoll.K_ZENTURIO].setToolTipText(Localization.getInstance().getString("HireWarlord"));
		chk[KonstantenProtokoll.K_ZENTURIO] = new JRadioButton();
		
		btn[KonstantenProtokoll.K_ANGREIFEN] = new JButton();
		btn[KonstantenProtokoll.K_ANGREIFEN].setIcon(new ImageIcon(ClassLoader.getSystemResource(Bilder.ANGREIFEN)));
		btn[KonstantenProtokoll.K_ANGREIFEN].addActionListener(this);
		btn[KonstantenProtokoll.K_ANGREIFEN].setActionCommand(a_actionCommand[2]);
		btn[KonstantenProtokoll.K_ANGREIFEN].setToolTipText(Localization.getInstance().getString("AttackProvince")+" (1Z/1E)");
		chk[KonstantenProtokoll.K_ANGREIFEN] = new JRadioButton();
		
		btn[KonstantenProtokoll.K_EINHEITENKAUFEN] = new JButton();
		btn[KonstantenProtokoll.K_EINHEITENKAUFEN].setIcon(new ImageIcon(ClassLoader.getSystemResource(Bilder.ANWERBEN)));
		btn[KonstantenProtokoll.K_EINHEITENKAUFEN].addActionListener(this);
		btn[KonstantenProtokoll.K_EINHEITENKAUFEN].setActionCommand(a_actionCommand[3]);
		btn[KonstantenProtokoll.K_EINHEITENKAUFEN].setToolTipText(Localization.getInstance().getString("HireTroops")+" (2-3D)");
		chk[KonstantenProtokoll.K_EINHEITENKAUFEN] = new JRadioButton();
	
		btn[KonstantenProtokoll.K_LAGERBAUEN] = new JButton();
		btn[KonstantenProtokoll.K_LAGERBAUEN].setIcon(new ImageIcon(ClassLoader.getSystemResource(Bilder.LAGER)));
		btn[KonstantenProtokoll.K_LAGERBAUEN].addActionListener(this);
		btn[KonstantenProtokoll.K_LAGERBAUEN].setActionCommand(a_actionCommand[4]);
		btn[KonstantenProtokoll.K_LAGERBAUEN].setToolTipText(Localization.getInstance().getString("BuildFortress")+" ("+Konstanten.KOSTENLAGER+"D/1Z)");
		chk[KonstantenProtokoll.K_LAGERBAUEN] = new JRadioButton();
		
		btn[KonstantenProtokoll.K_TRAINIEREN] = new JButton();
		btn[KonstantenProtokoll.K_TRAINIEREN].setIcon(new ImageIcon(ClassLoader.getSystemResource(Bilder.TRAINIEREN)));
		btn[KonstantenProtokoll.K_TRAINIEREN].addActionListener(this);
		btn[KonstantenProtokoll.K_TRAINIEREN].setActionCommand(a_actionCommand[5]);
		btn[KonstantenProtokoll.K_TRAINIEREN].setToolTipText(Localization.getInstance().getString("TrainTroops")+" ("+Konstanten.KOSTENTRAINING+"D)");
		chk[KonstantenProtokoll.K_TRAINIEREN] = new JRadioButton();
		
		while(z<BTN_ANZAHL){
			label.add(btn[z]);	
			label.add(btn[z+1]);
			label.add(chk[z]);
			label.add(chk[z+1]);
			z=z+2;
		}
		return label;
	}
	
	public void actionPerformed(ActionEvent e){
		String cmd = e.getActionCommand();
		if(cmd.equals(a_actionCommand[0])){
			chk[KonstantenProtokoll.K_BOTSCHAFTER].setSelected(!chk[KonstantenProtokoll.K_BOTSCHAFTER].isSelected());
		}
		else if(cmd.equals(a_actionCommand[1])){
			chk[KonstantenProtokoll.K_ZENTURIO].setSelected(!chk[KonstantenProtokoll.K_ZENTURIO].isSelected());
		}
		else if(cmd.equals(a_actionCommand[2])){
			chk[KonstantenProtokoll.K_ANGREIFEN].setSelected(!chk[KonstantenProtokoll.K_ANGREIFEN].isSelected());	
		}
		else if(cmd.equals(a_actionCommand[3])){
			chk[KonstantenProtokoll.K_EINHEITENKAUFEN].setSelected(!chk[KonstantenProtokoll.K_EINHEITENKAUFEN].isSelected());
		}
		else if(cmd.equals(a_actionCommand[4])){
			chk[KonstantenProtokoll.K_LAGERBAUEN].setSelected(!chk[KonstantenProtokoll.K_LAGERBAUEN].isSelected());	
		}
		else if(cmd.equals(a_actionCommand[5])){
			chk[KonstantenProtokoll.K_TRAINIEREN].setSelected(!chk[KonstantenProtokoll.K_TRAINIEREN].isSelected());
		}
	}
	private String getAktionen(){
		String aktionen="";
		for (int i=0;i<BTN_ANZAHL;i++){
			if(chk[i].isSelected()){
				aktionen = aktionen + i +KonstantenProtokoll.SEPARATORSUBJEKT;
			}
		}
		if(aktionen.length()>1){
			aktionen =aktionen.substring(0,aktionen.length()-1);
		}
		return aktionen;
	}

	public String getData() {
		String aktionen=this.getAktionen();
		for(int i=0;i<KonstantenProtokoll.K_MAX;i++){
			chk[i].setSelected(false);
		}
		return aktionen;
	}

	public int getID() {
		return 0;
	}

	public void update(Observable obs, Object arg1) {
		VerwaltungClient verClient;
		String msg=(String)arg1;
		boolean[] moeglichkeiten;
		
		if(obs instanceof VerwaltungClient &&!msg.equals(Konstanten.CHATMSG)){
			verClient=(VerwaltungClient)obs;
			moeglichkeiten=verClient.getAktionsMoeglichkeiten();
			if(moeglichkeiten!=null){
				for(int i=0;i<moeglichkeiten.length;i++){
					btn[i].setEnabled(moeglichkeiten[i]);
				}
			}			
		}
		
	}
}
