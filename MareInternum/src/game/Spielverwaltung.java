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

package game;

import java.util.Observable;
import java.util.Vector;

import entities.Konstanten;

public class Spielverwaltung extends Observable{
	
	private Main meinMain;	
	private VerwaltungServer meineVerServer;
	private VerwaltungClient meineVerClient;
	private Vector<VerwaltungClientComp> v_VerClientComp;
	private boolean isTeilnehmen=false;
	private boolean isSpielstartend=false;
	private boolean isSpielaktiv=false;
	
	/**
	 * constructor
	 * @param main
	 */
	public Spielverwaltung(Main main){	
		meineVerClient = new VerwaltungClient();
		meineVerServer = new VerwaltungServer();
		v_VerClientComp = new Vector<VerwaltungClientComp>();
		meinMain=main;		
	}

	/**
	 * inits joining a game
	 */
	public void willTeilnehmen(){
		if(!isSpielstartend){
			isTeilnehmen=true;
			this.melde();
		}
	}
	
	/**
	 * joins a game
	 * returns true if client was connected successfully
	 * 
	 * @param ip
	 * @param port
	 * @return
	 * @throws Exception
	 */
	public boolean nehmeAnSpielTeil(String ip,int port)
	throws Exception{
		if(!meineVerClient.nehmeAnSpielTeil(ip,port)){
			return false;
		}
		isSpielaktiv=true;
		isTeilnehmen=false;
		this.melde();
		return true;
	}
	
	/**
	 * cancel joining game
	 */
	public void abbruchTeilehmen(){
		isTeilnehmen=false;
		this.melde();
	}
	
	/**
	 * stops running game
	 */
	public void stoppe_Spiel(){
		
		isSpielstartend=false;
		isSpielaktiv=false;

		meineVerClient.beendeClient();
		if(meineVerServer!=null){
			meineVerServer.stoppeSpiel();
		}
		meinMain.storeGuiPanelLocations();
		meinMain.init();
		this.melde();
	}	
	
	/**
	 * init new game
	 * @param port
	 * @throws Exception
	 */
	public boolean starte_neuesSpiel(int port)
	throws Exception{
		meineVerServer.starte_neuesSpiel(port);
		if(!meineVerClient.nehmeAnSpielTeil("localhost",meineVerServer.getPort())){
			return false;
		}
		isSpielstartend=true;
		this.melde();
		return true;
	}
	
	/**
	 * lets begin the game
	 * ends connecting status
	 * 
	 * @param computergegner
	 * @param isAuthentifikation
	 * @throws Exception
	 */
	public void fangeSpielAn()
	throws Exception{	
		
		isSpielstartend=false;
		isSpielaktiv=true;	
	
		meineVerServer.baueSpielAufServer();
		
		this.melde();
	}

	/**
	 * adds a new computer player
	 */
	public void addNewComputerPlayer(){
		
		int anzahlSpieler = meineVerServer.getAnzahlSpieler();
		VerwaltungClientComp aktVerClientComp;
		
		if(anzahlSpieler < Konstanten.MAX_SPIELER){
			aktVerClientComp=new VerwaltungClientComp();
			if(aktVerClientComp.nehmeAnSpielTeil("localhost",meineVerServer.getPort())){
				v_VerClientComp.addElement(aktVerClientComp);
			}
		}
			
	}
	
	/**
	 * removes one computer player
	 */
	public void removeComputerPlayer(){
		
		VerwaltungClientComp aktVerClientComp;
		
		if(v_VerClientComp.size() > 0){
			aktVerClientComp = v_VerClientComp.elementAt(0);
			aktVerClientComp.beendeClient();
			v_VerClientComp.remove(aktVerClientComp);
		}
			
	}

	public VerwaltungClient getMeineVerClient() {
		return meineVerClient;
	}

	public VerwaltungServer getMeineVerServer() {
		return meineVerServer;
	}
	
	/**
	 * helper method which notifies observers
	 */
	public void melde(){
		this.setChanged();
		this.notifyObservers();
	}

	public boolean isSpielstartend() {
		return isSpielstartend;
	}

	public boolean isTeilnehmen() {
		return isTeilnehmen;
	}
	public boolean isSpielaktiv() {
		return isSpielaktiv;
	}
	
}
