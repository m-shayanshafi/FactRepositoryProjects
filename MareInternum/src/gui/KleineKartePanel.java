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
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;

import entities.Konstanten;
import entities.Provinz;
import entities.RPGrenze;

import game.Spielbrett;
import game.VerwaltungClient;

/**
 * this class represents the small map for quick navigation
 * @author johannes
 *
 */
public class KleineKartePanel extends JInternalFrame implements MouseListener, MouseMotionListener{

	private static final int WIDTH = 185;
	private static final int HEIGHT = 170;
	
	private GrafikRechteck fokus;
	private Hauptfenster mainWindow;
	
	public KleineKartePanel(VerwaltungClient verClient, Hauptfenster mainWindow){
		
		this.mainWindow = mainWindow;
		
		Container cp = this.getContentPane();

		cp.setLayout(null);
		cp.setBackground(Color.GRAY);
		cp.addMouseListener(this);
		cp.addMouseMotionListener(this);
		
		fokus=new GrafikRechteck();
				
		double screen_w=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double screen_h=Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		double fokus_w=(screen_w/10);
		double fokus_h=(screen_h/10);
		
		fokus.setRectangleWidth((int)fokus_w);
		fokus.setRectangleHeight((int)fokus_h);
		fokus.setBounds(0,0,(int)fokus_w,(int)fokus_h);
		fokus.addMouseListener(this);
		fokus.addMouseMotionListener(this);
		
		cp.add(fokus);
		
		Grafik aktGrafik;
		Provinz aktProvinz;
		RPGrenze aktRPGrenze;
		Spielbrett spielbrett = verClient.getMeinSpielbrett();
		
		Vector<Provinz> v_provinzen=spielbrett.getV_Provinzen();
		for(int i = 0; i < v_provinzen.size(); i++){
			aktProvinz = v_provinzen.elementAt(i);
			aktGrafik=new Grafik(Bilder.NOK);
			aktGrafik.setBounds(aktProvinz.getX_pMarker()/10,aktProvinz.getY_pMarker()/10,10,10);
			aktGrafik.set_id(aktProvinz.getOid());
			aktGrafik.setType(Konstanten.PROVINZMARKERKLEIN);
			aktGrafik.setToolTipText(aktProvinz.getName());
			aktGrafik.setObservable(spielbrett);
			cp.add(aktGrafik);
		}
		
		Vector<RPGrenze> grenzen=spielbrett.getV_RPGrenzen();
		for(int i = 0; i < grenzen.size(); i++){
			aktRPGrenze = grenzen.elementAt(i);
			aktGrafik = new Grafik(Bilder.NOK);
			aktGrafik.setBounds(aktRPGrenze.getX()/10,aktRPGrenze.getY()/10,10,10);
			aktGrafik.set_id(aktRPGrenze.getOid());
			aktGrafik.setObservable(verClient);
			aktGrafik.setType(Konstanten.ANGRIFFSMARKERKLEIN);
			cp.add(aktGrafik);
		}
		
		Grafik karte = new Grafik(Bilder.KARTEKLEIN);
		karte.setBounds(1,1,160,124);
		cp.add(karte);
		
		this.setSize(WIDTH, HEIGHT);
		 
	}

	/**
	 * changes size of focus
	 * @param width
	 * @param height
	 */
	public void setFocusSize(int width, int height){
		fokus.setRectangleHeight(height);
		fokus.setRectangleWidth(width);
	}
	
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1){
			int x = e.getX()*10;
			int y = e.getY()*10;
			
			if(e.getSource() instanceof GrafikRechteck){
				x = (e.getX() + fokus.getX()) * 10;
				y = (e.getY() + fokus.getY()) * 10;
			}
			
			this.moveMap(x, y);
		}	
	}
	
	/**
	 * moves map and focus in small map to the new place
	 * @param x
	 * @param y
	 */
	public void moveMap(int x, int y){
		mainWindow.centerLocationMap(x, y);
		JComponent karte = mainWindow.getGuiComponent(Karte.class.getName());
		setRelativeFocusLocation(karte.getX(), karte.getY());
	}
	
	/**
	 * updates the focus of the small map, relative to the map
	 * @param x = x value from the big map
	 * @param y = y value from the big map
	 */
	public void setRelativeFocusLocation(int x, int y){
		fokus.setBounds(x /10*-1, y/10*-1, fokus.getWidth(), fokus.getHeight());
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseDragged(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1){
			int x = e.getX()*10;
			int y = e.getY()*10;
			
			if(e.getSource() instanceof GrafikRechteck){
				x = (e.getX() + fokus.getX()) * 10;
				y = (e.getY() + fokus.getY()) * 10;
			}
			
			this.moveMap(x, y);
		}	
		
	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
