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
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class AnzeigeKarten extends JPanel{

	private JLabel karte1,karte2;
	private String name;
	private Color color;
	
	public AnzeigeKarten(Color ss_c,String ss_name,String ss_grafik1,String ss_grafik2){
		this.setLayout(new GridLayout(1,2));
		
		name = ss_name;
		
		karte1 = new JLabel();
		karte1.setIcon(new ImageIcon(ss_grafik1));
		karte2 = new JLabel();
		karte2.setIcon(new ImageIcon(ss_grafik2));
		
		Border etchedBdr = BorderFactory.createEtchedBorder(ss_c,ss_c);
		Border titledBdr = BorderFactory.createTitledBorder(etchedBdr, name);
		//Border emptyBdr  = BorderFactory.createEmptyBorder(0,0,0,0);
		Border compoundBdr=BorderFactory.createCompoundBorder(titledBdr, null);
		
		//this.add(name);
		this.add(karte1);
		this.add(karte2);
		this.setBorder(compoundBdr);
		
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(color);
		
	}
	
	public void setKarte(String ss_karte,int ss_kartennummer){
		switch(ss_kartennummer){
		case 0:
			karte1.setIcon(new ImageIcon(ClassLoader.getSystemResource(ss_karte)));
			break;
		case 1:
			karte2.setIcon(new ImageIcon(ClassLoader.getSystemResource(ss_karte)));
			break;
		}
		this.repaint();
	}
	
}
