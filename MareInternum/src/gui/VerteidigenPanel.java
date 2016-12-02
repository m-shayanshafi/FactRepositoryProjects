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

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import tools.Localization;
import entities.Konstanten;
import game.VerwaltungClient;

public class VerteidigenPanel extends JInternalFrame implements Abfragbar,Observer{

	private JLabel text;

	private JRadioButton rdJa;
	private JRadioButton rdNein;
	
	public VerteidigenPanel(){
		
		ButtonGroup buttonGroup = new ButtonGroup();
		
		JPanel container = new JPanel();
		container.setLayout(new GridLayout(2,1));
		text=new JLabel(Localization.getInstance().getString("DoYouWantToDefend"));
		
		rdJa=new JRadioButton(Localization.getInstance().getString("Yes"));
		rdNein=new JRadioButton(Localization.getInstance().getString("No"));
		rdNein.setSelected(true);
		buttonGroup.add(rdJa);
		buttonGroup.add(rdNein);
		
		JPanel lblText = new JPanel();
		lblText.add(text);
		
		JPanel lblQuestion = new JPanel();
		lblQuestion.add(rdJa);
		lblQuestion.add(new JLabel(" / "));
		lblQuestion.add(rdNein);
		
		container.add(lblText);
		container.add(lblQuestion);
		this.add(container);
		
		this.pack();
	}
	
	public boolean getVerteidigen(){
		boolean isVerteidigend=false;
		if(rdJa.isSelected()){
			isVerteidigend=true;
		}
		return isVerteidigend;
	}
	
	public String getData() {
		return ""+this.getVerteidigen();
	}
	
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void update(Observable obs, Object arg1) {
		VerwaltungClient verClient;
		String msg=(String)arg1;

		if(obs instanceof VerwaltungClient && !msg.equals(Konstanten.CHATMSG)){
			verClient=(VerwaltungClient)obs;
			
			rdJa.setEnabled(verClient.getKannVerteidigen(verClient.getMeinSpielerByID().get_Spielernummer()));
			rdJa.setSelected(false);
			rdNein.setSelected(true);
		}
		
	}
}
