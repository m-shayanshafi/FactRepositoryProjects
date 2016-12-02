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

import game.Spielbrett;
import game.VerwaltungClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class GrafikEinheiten extends JLabel implements Observer,Abfragbar{

	private Icon meineGrafik;
	private JLabel einheitenanzeige;
	private JLabel bild;
	private int offsetY;
	private int offsetX;
	private int id;
	private int z_einheiten;
	
	public GrafikEinheiten(VerwaltungClient ss_verClient,String ss_grafik,Color ss_vordergrund,int ss_einheiten){
		ss_verClient.getMeinSpielbrett().addObserver(this);
		this.setLayout(new BorderLayout());
		id =0;
		offsetX = 0;
		offsetY = 0;
		z_einheiten=ss_einheiten;
		meineGrafik = new ImageIcon(ClassLoader.getSystemResource(ss_grafik));
		einheitenanzeige = new JLabel(""+ss_einheiten);
		einheitenanzeige.setForeground(ss_vordergrund);
		bild = new JLabel(meineGrafik);
		bild.setSize(30,30);
		this.add(BorderLayout.NORTH,bild);
		this.add(BorderLayout.SOUTH,einheitenanzeige);
		this.setIgnoreRepaint(false);
	}
	
	public void set_offset(int ss_x,int ss_y){
		offsetX = ss_x;
		offsetY = ss_y;
	}
	
	public void set_id(int ss_id){
		id =ss_id;
	}
	
	public int get_offsetX(){
		return offsetX;
	}
	
	public int get_offsetY(){
		return offsetY;
	}
	
	
	public void setEinheitenRelative(int ss_einheiten){
		z_einheiten=z_einheiten+ss_einheiten;
		einheitenanzeige.setText(Integer.toString(z_einheiten));
	}
	public void setEinheitenAbsolut(int ss_einheiten){
		z_einheiten=ss_einheiten;
		einheitenanzeige.setText(Integer.toString(z_einheiten));
	}
	

	public void update(Observable obs, Object arg1) {
		Spielbrett spielbrett;

		if(obs instanceof Spielbrett){
			spielbrett=(Spielbrett)obs;
			if(spielbrett.getSpieleranzahl()<=id%5){
				this.setVisible(false);
			}
			else{
				if(this.isVisible()){
					this.setEinheitenAbsolut(spielbrett.getEinheitenRS(id/5,id%5));
				}
			}
		}
	}
	

	public String getData() {
		
		return ""+z_einheiten;
	}

	public int getID() {
		// TODO Auto-generated method stub
		return id;
	}
}
