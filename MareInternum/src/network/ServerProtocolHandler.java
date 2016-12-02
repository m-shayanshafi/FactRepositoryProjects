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

package network;

import java.io.UnsupportedEncodingException;
import java.nio.charset.CharacterCodingException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

import tools.Callback;

import entities.Konstanten;
import entities.KonstantenProtokoll;
import game.Spiel;
import game.Spieler;

/**
 * listens for messages from the client on the network
 * @author johannes
 *
 */
public class ServerProtocolHandler extends IoHandlerAdapter{

	private static final String SESSION_ATTR_NAME = "attr-name"; 
	
	private static final String SESSION_ATTR_ID = "attr-id"; 
	
	private Set<IoSession> sessions = Collections.synchronizedSet(new HashSet<IoSession>());
	
	private Set<Spieler> spieler = Collections.synchronizedSet(new HashSet<Spieler>()); 
	 
	private Spiel meinSpiel;
	 
	private boolean closed = false;
	
	private Callback parentObservable;
	
	/**
	 * counts player ids, to have a unique id
	 * starts with 100, to make a clear distinction to the player numbers later on
	 */
	private int idcount = 100;
	
	 /**
	  * constructor
	  * @param obs
	  */
	public ServerProtocolHandler(Callback obs){
		this.parentObservable = obs;
	}
	
	/**
	 * Receives message from client
	 * @throws UnsupportedEncodingException 
	 * @throws CharacterCodingException 
	 */
	public void messageReceived(IoSession session, Object msg) throws UnsupportedEncodingException, CharacterCodingException {
		
		if(msg instanceof java.lang.String){

			String message = (String)msg;

			String[] a_parts = message.split(KonstantenProtokoll.SEPARATORHEADER);
			String[] a_header = a_parts[0].split(KonstantenProtokoll.SEPARATOR1);
			String[] a_infos = a_parts[1].split(KonstantenProtokoll.SEPARATORSUBJEKT);
			
			int msgType = Integer.parseInt(a_header[1]);
			
			switch(msgType){
			
			case KonstantenProtokoll.ANMELDUNG:
				
				if(!closed && spieler.size() < Konstanten.MAX_SPIELER){
					
					Spieler actSpieler = new Spieler(idcount);
					actSpieler.setName(a_infos[0]);
					
					session.setAttribute(SESSION_ATTR_ID, new Integer(idcount));
					session.setAttribute(SESSION_ATTR_NAME, a_infos[0]);
					
					sessions.add(session);
					
					spieler.add(actSpieler);
					
					session.write(KonstantenProtokoll.ID + KonstantenProtokoll.SEPARATORHEADER + actSpieler.get_id());
					
					parentObservable.generalCall();
					
					idcount++;
					
				}				
				break;
				
			case KonstantenProtokoll.CHAT:
				
				broadcast(KonstantenProtokoll.CHAT+KonstantenProtokoll.SEPARATORHEADER+session.getAttribute(SESSION_ATTR_NAME)+": "+a_infos[0]);
				
				break;
			
			case KonstantenProtokoll.EINSPIELER:
				
				if(closed){
					String returnMessage = meinSpiel.verarbeite_Spielzug((Integer)session.getAttribute(SESSION_ATTR_ID),a_infos);
					broadcast(returnMessage);
				}
				break;
				
			case KonstantenProtokoll.ABMELDEN:
				
				this.removePLayerById((Integer) session.getAttribute(SESSION_ATTR_ID));
				sessions.remove(session);
				session.close();
				if(Konstanten.DEBUG > 0){				
					System.out.println("Client "+session.getAttribute(SESSION_ATTR_NAME)+" removed");
				}
				parentObservable.generalCall();
				
				break;
			}
		}
		
	}
	
	/**
	 * removes a player by his id from player collection
	 * @param id
	 */
	private void removePLayerById(int id){
		 synchronized (spieler) {
			 Iterator<Spieler> iter = spieler.iterator();
			while(iter.hasNext()){
				Spieler actSpieler = iter.next();
				if(actSpieler.get_id() == id){
					spieler.remove(actSpieler);	
					return;
				}
			}
		 }
	}
	
	/**
	 * sends a broadcast message
	 * @param message
	 */
    public void broadcast(String message) {
        synchronized (sessions) {
            Iterator<IoSession> iter = sessions.iterator();
            while (iter.hasNext()) {
                IoSession s = iter.next();
                if (s.isConnected()) {
                    s.write(message);
                }
            }
        }
    }

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	
	public int getClientCount(){
		return spieler.size();
		
	}

	public Set<Spieler> getSpieler() {
		return spieler;
	}

	public void setMeinSpiel(Spiel meinSpiel) {
		this.meinSpiel = meinSpiel;
	}
	
}
