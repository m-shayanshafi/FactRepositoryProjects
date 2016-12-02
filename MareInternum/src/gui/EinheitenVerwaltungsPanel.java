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
import game.Spieler;
import game.VerwaltungClient;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EinheitenVerwaltungsPanel extends JInternalFrame implements Observer{

	
	private JLabel[] einheitenCache;
	private JLabel[] label;
	private JLabel text;
	private JLabel kosten;
	private JButton btnReset;
	
	public EinheitenVerwaltungsPanel(Observable verClient,MeinActionListener listener){
		
		verClient.addObserver(this);
		einheitenCache = new JLabel[Konstanten.REGIONENANZAHL];
		
		label=new JLabel[Konstanten.REGIONENANZAHL];
		label[0] = new JLabel("Oceanus Atlanticus: ");
		label[1] = new JLabel("Mare Internum: ");
		label[2] = new JLabel("Pontus Euxinus: ");
		label[3] = new JLabel("Sarmatia: ");
		label[4] = new JLabel("Arabia: ");
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		text=new JLabel(" "+Localization.getInstance().getString("SetTroops")+":");
		text.setForeground(Color.BLUE);
		kosten =new JLabel("0");
		btnReset=new JButton(Localization.getInstance().getString("Reset"));
		btnReset.setActionCommand("reset");
		btnReset.addActionListener(listener);
		
		JPanel pnlTitle = new JPanel();
		pnlTitle.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnlTitle.add(text);
		
		this.add(pnlTitle);
		
		for(int i=0;i<label.length;i++){
			JPanel container=new JPanel();
			container.setLayout(new FlowLayout(FlowLayout.LEFT));
			container.add(label[i]);
			einheitenCache[i]=new JLabel("0000");
			container.add(einheitenCache[i]);
			this.add(container);
		}
		
		JPanel pnlKosten = new JPanel();
		pnlKosten.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnlKosten.add(kosten);
		JPanel pnlReset = new JPanel();
		pnlReset.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnlReset.add(btnReset);
		
		this.add(pnlKosten);
		this.add(pnlReset);
		this.pack();
	}
	
	public void update(Observable obs, Object arg1) {
		VerwaltungClient verClient;
		String msg=(String)arg1;
		int[] einheiten;
		int z_kosten=0;
		int z_anzahl=0;
		Spieler[] a_spieler;
		int maxGebot=0;
		
		if(obs instanceof VerwaltungClient&&!msg.equals(Konstanten.CHATMSG)){
			verClient=(VerwaltungClient)obs;
			
			einheiten=verClient.getEinheitenCache();
			
			for(int i=0;i<einheiten.length;i++){
				einheitenCache[i].setText(""+einheiten[i]);
				z_anzahl+=einheiten[i];
				if(i!=1){z_kosten+=2*einheiten[i];}
				else{z_kosten+=3*einheiten[i];}
			}
			if(verClient.getAktAktion()==KonstantenProtokoll.EINHEITENKAUFEN){
				text.setText(" "+Localization.getInstance().getString("BoughtTroops")+":");
				kosten.setText(" "+Localization.getInstance().getString("Cost")+": "+z_kosten+" Denarii");
				
			}
			if(verClient.getAktAktion()==KonstantenProtokoll.GESCHENKWAEHLEN){
				text.setText(" "+Localization.getInstance().getString("AdditionalTroops")+":");
				kosten.setText(" "+Localization.getInstance().getString("Altogether")+": "+z_anzahl);
				
			}
			
		}
		
	}
}
