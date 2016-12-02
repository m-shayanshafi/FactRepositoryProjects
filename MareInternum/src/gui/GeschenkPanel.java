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
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tools.Localization;

public class GeschenkPanel extends JInternalFrame{
	
	private JButton btnEinheiten;
	private JButton btnPunkte;
	
	public GeschenkPanel(MeinActionListener meinActionListener){
		JPanel pnlButtons=new JPanel();
		JPanel beschriftung = new JPanel();
		beschriftung.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel label = new JLabel(Localization.getInstance().getString("ChooseGift")+":");
		beschriftung.add(label);
		
		pnlButtons.setLayout(new FlowLayout());
		
		btnEinheiten=new JButton();
		btnEinheiten.setIcon(new ImageIcon(ClassLoader.getSystemResource(Bilder.BONUSEINHEITEN)));
		btnEinheiten.setActionCommand("bonuseinheiten");
		btnEinheiten.addActionListener(meinActionListener);
		btnEinheiten.setToolTipText(Localization.getInstance().getString("Troops"));
		
		btnPunkte = new JButton();
		btnPunkte.setIcon(new ImageIcon(ClassLoader.getSystemResource(Bilder.BONUSPUNKTE)));
		btnPunkte.setActionCommand("bonuspunkte");
		btnPunkte.addActionListener(meinActionListener);
		btnPunkte.setToolTipText(Localization.getInstance().getString("Points"));
		
		this.setLayout(new BorderLayout());
		pnlButtons.add(btnEinheiten);
		pnlButtons.add(btnPunkte);
		this.add(BorderLayout.NORTH,beschriftung);
		this.add(BorderLayout.CENTER,pnlButtons);
		
		this.pack();
	}
}
