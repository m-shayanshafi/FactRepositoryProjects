/* Transceiver.java

   Copyright (C) 2001  Yu Zhang

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the 
   Free Software Foundation, Inc., 
   59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/


package soccer.common;

import java.net.*;
import java.io.*;

/** 
 * This class defines the UDP networking tasks, such as sending and
 * receiving soccer UDP packets.
 *
 * @author Yu Zhang
 */
public class Transceiver 
{
  private  int  size = 1024;
  private DatagramSocket    socket;
	
  /**
   * sets up a transceiver with default settings. If the transceiver
   * is used for server, the default port number is 7777. If the
   * transceiver is used for client, no port number is needed.
   *
   * @param isServer the flag to indicate if this transceiver is used for server or not
   */
  public Transceiver(boolean isServer)
  {
    try
    {
		if(!isServer) {
		  socket = new DatagramSocket();
		}
		else {
		  socket = new DatagramSocket(7777);
		}
    }
    catch(Exception e)
    {
      System.err.println("Transceiver:socket creating error:" + e);
      System.exit(1);
    }
  }

  /**
   * sets up a transceiver for server with specified port number.
   *
   * @param port the server port number
   */
  public Transceiver(int port)
  {
    try
    {
      socket = new DatagramSocket(port);
    }
    catch(Exception e)
    {
      System.err.println("Transceiver:socket creating error:" + e);
      System.exit(1);      
    }
  }

  /**
   * Disconnect the UDP socket, close this transceiver.
   */
  public void disconnect()
  {
    socket.close();
  }

  /**
   * Sends soccer data packet over UDP socket.
   * 
   * @param p the soccer data packet
   * @exception IOException If any UDP errors occured.
   * 
   * @see Packet
   */
  public void send(Packet p) throws IOException
  {

    byte[] buffer = p.writePacket().getBytes();
    DatagramPacket packet = 
    		new DatagramPacket(	buffer, 
    							buffer.length, 
					       		p.address, 
					       		p.port);
    socket.send(packet);

  }

  /**
   * Receives soccer data packet over UDP socket.
   * 
   * @return    the soccer data packet
   * @exception IOException If any UDP errors occured.
   * 
   * @see Packet
   */
		       
  public Packet receive() throws IOException
  {
    byte[] buffer = new byte[size];
    DatagramPacket packet = new DatagramPacket(buffer, size);
    socket.receive(packet);
    
    String message = new String(buffer);
    Packet p = new Packet();
    p.readPacket(message);
    p.address = packet.getAddress();
    p.port = packet.getPort();
    return p;
  }
  
  /**
   * Sets the UDP receiving buffer size.
   *
   * @param size the UDP receiving buffer size
   */
  public void setSize(int size)
  {
    this.size = size;  
  }
  
  /**
   * Gets the UDP receiving buffer size.
   *
   * @return the UDP receiving buffer size
   */
	      
  public int getSize()
  {
    return size;  
  }

  /**
   * Sets the UDP socket block timeout.
   * <p>
   * Enable/disable SO_TIMEOUT with the specified timeout, in milliseconds.
   * With this option set to a non-zero timeout, a call
   * to receive() for this DatagramSocket will block for only this amount of time.
   * If the timeout expires, a <b>java.io.InterruptedIOException</b> 
   * is raised, though the DatagramSocket is still valid. The option must be enabled 
   * prior to entering the blocking operation to have effect.
   * A timeout of zero is interpreted as an infinite timeout.
   *	
   * @param timeout  the specified timeout in milliseconds.
   * @exception IOException If there is an error in the underlying protocol.
   */
  public void setTimeout(int timeout) throws IOException
  {
    socket.setSoTimeout(timeout);
  }
  /**
   * Gets the UDP socket block timeout.
   * <p>
   * Retrive setting for SO_TIMEOUT. 0 returns implies that the option is disabled 
   * (i.e., timeout of infinity).
   *
   * @return the UDP socket block timeout.
   */
  public int getTimeout() throws IOException
  {
    return socket.getSoTimeout();  
  }  
}

