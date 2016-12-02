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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import entities.Konstanten;
import game.VerwaltungClient;

public class MeinMouseListener implements MouseListener{
	
	private String button="no";	
	private Abfragbar meineGrafik;
	private int grafikInhalt;
	private VerwaltungClient meineVerClient;
	
	public MeinMouseListener(Abfragbar ss_grafik,int ss_inhalt,VerwaltungClient ss_verClient){
		grafikInhalt=ss_inhalt;
		meineGrafik = ss_grafik;
		meineVerClient=ss_verClient;
		
	}
	
	public void mouseExited(MouseEvent e){}
	
	public void mousePressed(MouseEvent e){
		
	}
	
	public void mouseEntered(MouseEvent e){
		//meineGrafik.setToolTipText(Integer.toString(meineGrafik.get_ID()));
	}
	public void mouseReleased(MouseEvent e){
		switch(grafikInhalt){
		case Konstanten.PROVINZMARKER:
				meineVerClient.provinzAusgewaehlt(meineGrafik.getID());
			break;
		case Konstanten.EINHEITENMARKER:
			switch(e.getButton()){
			case MouseEvent.BUTTON1:button="links";break;
			case MouseEvent.BUTTON3:button="rechts";break;
			}
				
				meineVerClient.einheitenAusgewaehlt((int)(meineGrafik.getID()/5),button);
			
			break;
		}
	}
	public void mouseClicked(MouseEvent e){
		
		
	}
	public void mouseOver(MouseEvent e){
		
	}

	
}
