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

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.Observable;
import java.util.Vector;

import network.ServerProtocolHandler;

import org.apache.mina.common.DefaultIoFilterChainBuilder;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoAcceptorConfig;
import org.apache.mina.filter.LoggingFilter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;

import tools.Localization;
import tools.Callback;

import entities.Konstanten;
import entities.KonstantenProtokoll;


public class VerwaltungServer extends Observable implements Callback{
	
	private int anzahlSpieler;
	private Spiel meinSpiel;
	private Spielbrett meinSpielbrett;
	private IoAcceptor acceptor;
	private ServerProtocolHandler pHandler;
	private int port = Konstanten.STANDARDPORT;
	
	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}


	public VerwaltungServer() {

	}
	
	/**
	 * starts a new game,
	 * opens network acceptor
	 * @param port
	 * @throws Exception
	 */
	public void starte_neuesSpiel(int port)
	throws Exception{
		
		this.port = port;

		acceptor = new SocketAcceptor();
		
		IoAcceptorConfig config = new SocketAcceptorConfig();
	    DefaultIoFilterChainBuilder chain = config.getFilterChain();
	    
	    chain.addLast("codec", new ProtocolCodecFilter(
                new TextLineCodecFactory()));
	    
	   if(Konstanten.DEBUG > 0)
		   {
		   chain.addLast("logger", new LoggingFilter());
		   }
	    
		pHandler = new ServerProtocolHandler(this);
		acceptor.bind(new InetSocketAddress(port), pHandler, config);
	}
	
	
	/**
	 * stop game
	 */
	public void stoppeSpiel(){
		
		meinSpiel=null;
		meinSpielbrett=null;

		if(acceptor!=null){
			acceptor.unbindAll();
		}
		
	}
	
	/**
	 * create game environment, and send its information to all clients
	 * start of game
	 */
	public void baueSpielAufServer(){
		String nachricht = "";
		String alleSpielerStartProvinzen="";
		String alleWaren="";
		String spielernamen="";
		String spielerids="";
		Spieler aktSpieler;
		
		pHandler.setClosed(true);
		
		anzahlSpieler = this.getAnzahlSpieler();

		
		Vector<Spieler> v_spieler = new Vector<Spieler>();
		
		Iterator<Spieler> iter = pHandler.getSpieler().iterator();
		while(iter.hasNext()){
			v_spieler.addElement(iter.next());
		}
		
		meinSpielbrett = new Spielbrett();
		meinSpielbrett.setSpieleranzahl(anzahlSpieler);

		meinSpielbrett.waren_setzen(false);
		meinSpielbrett.spieler_setzen();
		
		meinSpiel=new Spiel(v_spieler, meinSpielbrett);

		pHandler.setMeinSpiel(meinSpiel);
				
		for(int i=0;i<v_spieler.size();i++){
			for(int j=0;j<v_spieler.size();j++){
				aktSpieler = v_spieler.elementAt(j);
				if(aktSpieler.get_Spielernummer()==i){
					spielernamen = spielernamen+aktSpieler.getName()+KonstantenProtokoll.SEPARATORSUBJEKT;
					spielerids = spielerids+aktSpieler.get_id()+KonstantenProtokoll.SEPARATORSUBJEKT;
					}
			}
		}	
		
		alleWaren = meinSpielbrett.get_allewaren();
		alleSpielerStartProvinzen=meinSpielbrett.get_alleSpielernummern();
		nachricht = KonstantenProtokoll.INITIALISIERUNG+KonstantenProtokoll.SEPARATORHEADER+
					alleWaren.substring(0,alleWaren.length()-KonstantenProtokoll.SEPARATORSUBJEKT.length())+KonstantenProtokoll.SEPARATOR1+
					spielerids.substring(0,spielerids.length()-KonstantenProtokoll.SEPARATORSUBJEKT.length())+KonstantenProtokoll.SEPARATOR1+
					spielernamen.substring(0,spielernamen.length()-KonstantenProtokoll.SEPARATORSUBJEKT.length())+KonstantenProtokoll.SEPARATOR1+
					alleSpielerStartProvinzen;
		//h & s0					 & s1 (IDs)		 & s2
		//0 & 1,4,2,3,2,4,1,2,3,3,...& 1,2,3,... & alfred, klopp, fred, ...
		this.sendeAnAlle(nachricht);
		//Anstoß !!!
		
		nachricht=KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+
					0+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.PROVINZWAEHLEN+
					KonstantenProtokoll.SEPARATORSUBJEKT+meinSpiel.getStartspieler()+KonstantenProtokoll.SEPARATOR1+
					KonstantenProtokoll.WP_NIX+KonstantenProtokoll.SEPARATORSUBJEKT+Localization.getInstance().getString("StartOfGame");
		
		this.sendeAnAlle(nachricht);
		
	}
	
	/**
	 * sends a broadcast message
	 * @param message
	 */
	public synchronized void sendeAnAlle(String message){
		pHandler.broadcast(message);
	}

	public int getAnzahlSpieler() {
		return pHandler.getClientCount();
	}

	/**
	 * send notification to all observers
	 */
	public void generalCall() {
		this.setChanged();
		this.notifyObservers();	
	}
	
}
