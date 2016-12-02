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

import java.net.InetSocketAddress;

import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.IoSession;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.apache.mina.transport.socket.nio.SocketConnectorConfig;

import entities.Konstanten;
import entities.KonstantenProtokoll;


/**
 * This class manages the connection to the server
 */
public class Client {
	
	/**
	 * client socket for sending messages
	 */
	private IoSession session;
	
	/**
	 * business class which has to be notified
	 */
	private Netzwerkable meineVerwaltung;
	
	private SocketConnector connector;

	public Client(Netzwerkable ss_verwaltung, SocketConnector connector){
		meineVerwaltung = ss_verwaltung;
		this.connector = connector;
		
	}
	
	public boolean connect(String host, int port){
		
		if (session != null && session.isConnected()) {
	            return false;
		}
		
		SocketConnectorConfig config = new SocketConnectorConfig();
	
        ConnectFuture future1 = connector.connect(new InetSocketAddress(host,  port), new ClientSupport(meineVerwaltung), config);
        future1.join();
        
        if (!future1.isConnected()) {
            return false;
        }
        
        session = future1.getSession();
        
        return true;
	}
	
	/**
	 * sends a message to the server
	 * @param message
	 */
	public void sendeNachricht(String message)
	throws Exception{
		if (session != null && session.isConnected()) {
           message = Konstanten.NOMSG + KonstantenProtokoll.SEPARATOR1 + message;
			session.write(message);
		}
	}
	
	/**
	 * closes connection
	 * @throws Exception 
	 */
	public void stoppeClient() throws Exception{
		if (session != null) {
            if (session.isConnected()) {
                // Wait until session ends
               sendeNachricht(KonstantenProtokoll.ABMELDEN +KonstantenProtokoll.SEPARATORHEADER + Konstanten.NOMSG);
               session.getCloseFuture().join();
            }
            session.close();
        }
	}
	
	/**
	 * passes a message to the business layer
	 * @param nachricht
	 */
	public void nachrichtVonServer(String nachricht){
		meineVerwaltung.nachrichtVonServer(nachricht);
	}
}
