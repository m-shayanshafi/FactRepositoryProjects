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


import entities.Konstanten;

import org.apache.mina.common.IoFilter;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.LoggingFilter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;

/**
 * this class listens for packages from the server on the network
 * @author johannes
 *
 */
public class ClientSupport extends IoHandlerAdapter {
	
	private Netzwerkable meinClient;
	
	private static IoFilter LOGGING_FILTER = new LoggingFilter();

    private static IoFilter CODEC_FILTER = new ProtocolCodecFilter(
            new TextLineCodecFactory());
	
	public ClientSupport(Netzwerkable meinClient){
		this.meinClient = meinClient;
	}
	
	 public void messageReceived(IoSession session, Object message)
     throws Exception {
		 if(message instanceof java.lang.String){
			 meinClient.nachrichtVonServer((String)message);
		 }
	 }
	 
	 public void sessionCreated(IoSession session) throws Exception {
	        session.getFilterChain().addLast("codec", CODEC_FILTER);
	       if(Konstanten.DEBUG > 0)
	    	   {
	    	   session.getFilterChain().addLast("logger", LOGGING_FILTER);
	    	   }
	    }

	
}
