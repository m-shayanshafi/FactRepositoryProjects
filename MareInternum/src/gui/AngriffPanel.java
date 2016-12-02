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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tools.Localization;

import entities.Konstanten;
import entities.KonstantenProtokoll;
import game.VerwaltungClient;

/**
 * This panel contains the dialog where the player can choose,
 * from where he or her wants to attack which province
 * @author johannes
 *
 */
public class AngriffPanel extends JInternalFrame implements Observer{

	private static final int WIDTH = 200;
	
	private boolean isOk=false;
	private JLabel label1,label2;
	private JLabel lblVon;
	private JLabel lblNach;
	private JLabel lblOK;
	private JLabel lblText;
	private JPanel[] meinPanel;
	private JPanel meinContainer;

	public AngriffPanel( VerwaltungClient ss_verClient){
		
		this.setLayout(new BorderLayout());
		ss_verClient.addObserver(this);
		
		label1 = new JLabel(Localization.getInstance().getString("From")+": ");
		label2 = new JLabel(Localization.getInstance().getString("To")+": ");
		
		lblVon = new JLabel("--");
		lblNach = new JLabel("--");
		lblOK = new JLabel("?");
		
		lblText=new JLabel(Localization.getInstance().getString("Attack")+":");
		lblText.setForeground(Color.BLUE);
		
		meinPanel=new JPanel[3];
		meinPanel[0]=new JPanel();
		meinPanel[0].setLayout(new FlowLayout(FlowLayout.LEFT));
		meinPanel[0].add(label1);
		meinPanel[0].add(lblVon);
		meinPanel[1]=new JPanel();
		meinPanel[1].setLayout(new FlowLayout(FlowLayout.LEFT));
		meinPanel[1].add(label2);
		meinPanel[1].add(lblNach);
		meinPanel[2]=new JPanel();
		meinPanel[2].setLayout(new FlowLayout(FlowLayout.LEFT));
		meinPanel[2].add(lblOK);
		
		this.resetValues();
		meinContainer= new JPanel();
		meinContainer.setLayout(new GridLayout(4,1));
		meinContainer.add(lblText);
		for(int i=0;i<meinPanel.length;i++){
			meinContainer.add(meinPanel[i]);
		}
		this.add(BorderLayout.NORTH,meinContainer);
		this.pack();
		this.setSize(WIDTH, this.getHeight());
	}
	
	public void setVon(String ss_region){
		String name;
		name=ss_region;
		lblVon.setText(name);
		
	}
	
	public void setNach(String ss_provinz){
		String name;
		name=ss_provinz;
		lblNach.setText(name);
		
	}
	
	
	public void resetValues(){
		lblVon.setText("");
		lblNach.setText("");
	}
	
	public boolean getIsOk(){
		return isOk;
	}
	
	public void setOk(boolean ss_Ok){
		isOk=ss_Ok;
		if(isOk){
			lblOK.setForeground(Color.BLACK);
			lblOK.setText(Localization.getInstance().getString("OK"));
		}
		else{
			lblOK.setForeground(Color.RED);
			lblOK.setText(Localization.getInstance().getString("NotPossible"));
		}
	}
	
	public void update(Observable obs, Object ss_msg) {
		VerwaltungClient verClient;
		String infos;
		String msg=(String)ss_msg;
		String[] a_infos;

		if(obs instanceof VerwaltungClient&&(msg.equals(Konstanten.NOMSG)||msg.equals(Konstanten.SPIELSTART))){
			verClient=(VerwaltungClient)obs;
			infos=verClient.getAngriffsInfo();
			a_infos=infos.split(KonstantenProtokoll.SEPARATORSUBJEKT);
			setVon(a_infos[0]);
			setNach(a_infos[1]);
			this.setOk(Boolean.parseBoolean(a_infos[2]));
		}
	}
	
}
	
