/********************************************************************
 *     Copyright (c) 2000 by Hewlett-Packard, All Rights Reserved.
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public 
 *     License along with this program; if not, write to the Free Software
 *     Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 *     MA 02111-1307 USA
 *
 ********************************************************************/

package hp.infrared;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class implements client sockets over Infrared medium
 *
 * @see IrServerSocket
 */

public class IrSocket 
{

  private IrSocketImpl impl=null;
  IrSocketImpl getImpl() { return impl;}

  static {
	System.loadLibrary("irsock");
  }

  /**
   * Creates an unconnected socket.
   */

  protected IrSocket()  {
	impl = new IrSocketImpl(); 
	}		

  /**
   * Creates a socket and connects it to the specified port number on the
   * infrared device with the specified nickname. If the nickname is 
   * specified as "ANY" then it connects to any device in sight.
   * 
   * @param nickname nickname of the peer device
   * @param port the port number
   * 
   * @exception IOException
   * 	if I/O error occurs while creating the socket.
   */
   
  public IrSocket(String nickname, int port)
       throws IOException
  {
    impl = new IrSocketImpl();

    impl.create(true);

    try{
      impl.connect(nickname, port);
    }catch (IOException e){
      impl.close();
      throw e;
    }
  }

  /**
   * Creates a socket and connects it to the specified service on the
   * infrared device with the specified nickname.
   * 
   * @param nickname nickname of the peer device
   * @param servicename the name of the service
   * 
   * @exception IOException
   * 	if I/O error occurs while creating the socket.
   */

  public IrSocket(String nickname, String servicename)
       throws IOException
  {
    impl = new IrSocketImpl();

    impl.create(true);

    try{
      impl.connect(nickname, servicename);
    }catch (IOException e){
      impl.close();
      throw e;
    }
  }

  /**
   * Creates a socket and connects it to the specified port number on the
   * infrared device with the first mathcing nickname from the list of nicknames.
   * 
   * @param nicknames list of possible nicknames of the peer device
   * @param port the port number
   * 
   * @exception IOException
   * 	if I/O error occurs while creating the socket.
   */

  public IrSocket(String nicknames[], int remotePort)
      throws IOException
  {
    impl = new IrSocketImpl();

    impl.create(true);

    boolean connected = false;
    IOException exception = null;
    int index = 0;

    do {
      if(index >= nicknames.length)
        break;
      try {
        impl.connect(nicknames[index], remotePort);
        connected = true;
        break;
      }
      catch(IOException e) {
        exception = e;
        index++;
      }
    } while(true);
    if(!connected) {
      if(exception != null) {
        throw exception;
      }
      else {
        throw new IOException("connection failed");
      }
    }
  }

  /**
   * Closes this socket.
   *
   * @exception IOException
   * 	if I/O error occurs while closing.
   */

  public synchronized void close()
       throws IOException
  {
    impl.close();
  }
  
  /**
   * Returns an input stream for this socket.
   * 
   * @return an input stream for reading bytes from this socket.
   * @exception IOException
   * 	if I/O error occurs while creating the input stream.
   */
 
  public InputStream getInputStream()
       throws IOException
  {
    return impl.getInputStream();
  }

  /**
   * Returns an output stream for this socket.
   * 
   * @return an output stream for writing bytes to this socket.
   * @exception IOException
   * 	if I/O error occurs while creating the input stream.
   */

  public OutputStream getOutputStream()
       throws IOException
  {
    return impl.getOutputStream();
  }
}

