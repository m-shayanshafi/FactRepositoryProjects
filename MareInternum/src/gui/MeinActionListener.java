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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import entities.KonstantenProtokoll;
import game.VerwaltungClient;

public class MeinActionListener implements ActionListener{
		
	private VerwaltungClient meineVerClient;
	private Abfragbar[] a_abfragbar;
	
	public MeinActionListener(VerwaltungClient ss_verwaltungClient){
		meineVerClient = ss_verwaltungClient;
	}
	
	public void setAbfragbar(Abfragbar[] ss_a_abfragbar){
		a_abfragbar=ss_a_abfragbar;
	}
	
	public void actionPerformed(ActionEvent e){
		String cmd = e.getActionCommand();
		if(cmd.equals("weiter")){
			if(meineVerClient.getAktAktion()==KonstantenProtokoll.KARTENWAEHLEN){
				meineVerClient.weiterButtonGedrueckt(a_abfragbar[KonstantenProtokoll.KARTENWAEHLEN].getData());
			}
			else if(meineVerClient.getAktAktion()==KonstantenProtokoll.ANGREIFEN){
				meineVerClient.weiterButtonGedrueckt("");
			}
			else if(meineVerClient.getAktAktion()==KonstantenProtokoll.VERTEIDIGEN){
				meineVerClient.weiterButtonGedrueckt(a_abfragbar[KonstantenProtokoll.VERTEIDIGEN].getData());
			}
			else if(meineVerClient.getAktAktion()==KonstantenProtokoll.TRAININEREN){
				meineVerClient.weiterButtonGedrueckt(a_abfragbar[KonstantenProtokoll.TRAININEREN].getData());
			}
			else{
				meineVerClient.weiterButtonGedrueckt("");
			}
			
		}
		else if(cmd.equals("bonuseinheiten")){
			meineVerClient.geschenkGewaehlt(KonstantenProtokoll.A_BONUSEINHEITENGEWAEHLT);
		}
		else if(cmd.equals("bonuspunkte")){
			meineVerClient.geschenkGewaehlt(KonstantenProtokoll.A_BONUSPUNKTEGEWAEHLT);
		}
		else if(cmd.equals("reset")){
			meineVerClient.resetGewaehlt();
		}
	}
}

