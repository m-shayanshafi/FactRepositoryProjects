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

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tools.Localization;

import entities.Konstanten;

import game.VerwaltungClient;

/**
 * This panel contains the dialog where the player can choose,
 * how hard he or she wants to train the troops
 * @author johannes
 *
 */
public class TrainingPanel extends JInternalFrame implements Abfragbar,Observer,ChangeListener{

	private JSlider slide;
	private JLabel lblInfo;
	
	public TrainingPanel(Observable obs){
		
		obs.addObserver(this);

		this.setLayout(new GridLayout(3,1));
		JPanel pnl0=new JPanel();
		JPanel pnl1=new JPanel();
		JPanel pnl2=new JPanel();
		slide=new JSlider();
		slide.addChangeListener(this);
		slide.setSnapToTicks(true);
		lblInfo=new JLabel();
		pnl0.add(new JLabel(Localization.getInstance().getString("HowMuchDoYouWantToTrain")));
		pnl1.add(slide);
		pnl2.add(lblInfo);
		lblInfo.setText(Localization.getInstance().getString("FactorOfTraining")+": "+slide.getValue());
		this.add(pnl0);
		this.add(pnl1);
		this.add(pnl2);
		
		this.pack();
	}

	public String getData() {
		return slide.getValue()+"";
	}

	public int getID() {
		
		return 0;
	}

	public void update(Observable obs, Object arg1) {
		VerwaltungClient verClient;
		String msg=(String)arg1;

		if(obs instanceof VerwaltungClient&&!msg.equals(Konstanten.CHATMSG)
				&&!msg.equals(Konstanten.SPIELENDE)){
			verClient=(VerwaltungClient)obs;
			slide.setMaximum(verClient.getLegionslager());
			slide.setMinimum(0);
			slide.setValue(verClient.getLegionslager());
			
		}
		
	}

	/**
	 * event on slider state changed
	 */
	public void stateChanged(ChangeEvent arg0) {
		lblInfo.setText(Localization.getInstance().getString("FactorOfTraining")+": "+slide.getValue());
		
	}
}
