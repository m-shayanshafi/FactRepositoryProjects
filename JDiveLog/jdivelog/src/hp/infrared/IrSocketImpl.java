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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

class IrSocketImpl extends java.net.SocketImpl
{
  
  private native static FileDescriptor socketCreate(boolean isStream)
       throws IOException;

  private native static void socketBind(FileDescriptor fd, byte [] addr, int port)
       throws IOException;
  
  private native static void socketBindObject(FileDescriptor fd, byte[] addr, String IASObject)
       throws IOException;

  private native static void socketListen(FileDescriptor fd, int backlog)
       throws IOException;

  private native static FileDescriptor socketAccept(FileDescriptor fd,
	  int timeout, java.net.SocketImpl s) throws IOException;

  private native static void socketConnect(FileDescriptor fd, String nickname,
					 int port)
       throws IOException;

  private native static void socketConnectObject(FileDescriptor fd, String nickname, String IASObject)
       throws IOException;

  private native static int socketAvailable(FileDescriptor fd)
       throws IOException;

  private native static void socketClose(FileDescriptor fd1, FileDescriptor fd2)
       throws IOException;

  private native static FileDescriptor dupFD(FileDescriptor fd);

  private FileDescriptor outfd;   

  int timeout;         
                        

  protected  void create(boolean isStream)
       throws IOException
  {
    fd = socketCreate(isStream);
  }

  protected  void bind(java.net.InetAddress localAddr, int localPort)
       throws IOException
  {
    socketBind(this.fd, (localAddr != null) ? localAddr.getAddress() : null, 
	     localPort);
  }

  protected  void bind(java.net.InetAddress localAddr, String IASObject)
       throws IOException
  {
    socketBindObject(this.fd, (localAddr != null) ? localAddr.getAddress() : null, 
	    IASObject); 
  }
  
  protected  void listen(int backlog)
       throws IOException
  {
    socketListen(fd, backlog);
  } 

  protected  void accept(java.net.SocketImpl client)
       throws IOException
  {
    synchronized(this.fd){
      FileDescriptor fd = socketAccept(this.fd, timeout, client);
      ((IrSocketImpl)client).fd = fd;
    }
  }

  protected  void connect(String nickname, int remotePort)
       throws IOException
  {
    socketConnect(fd, nickname, remotePort);
  }
 
  protected  void connect(java.net.InetAddress addr, int remotePort)
       throws IOException
  {
	throw new IOException("Method not supported for Infrared medium");
  } 

  protected void connect(java.net.SocketAddress addr, int timeout)
      throws IOException
  {
      throw new IOException("Method not supported for Infrared medium");
  }

  protected  void connect( String nickname, String IASObject)
       throws IOException
  {
    socketConnectObject(fd, nickname, IASObject);
  } 
   
  protected  void close()
       throws IOException
  {
    if (this.fd != null && outfd != null){
	  socketClose(this.fd, outfd);
    }else if (this.fd != null){
	socketClose(this.fd, null);
    }
  } 

  protected  InputStream getInputStream()
       throws IOException
  {
    return new IrSocketInputStream(fd, this);
  }

  protected  OutputStream getOutputStream()
       throws IOException
  {
    if (outfd == null)
      outfd = dupFD(fd); 
    return new IrSocketOutputStream(outfd, this);
  }

  protected  int available() throws IOException
  {
    synchronized(this.fd){
      return socketAvailable(this.fd);
    } 
  } 
  public Object getOption(int i ) { return null;}
  public void setOption(int i,Object o) {}

  protected void sendUrgentData(int data)
       throws IOException
  {
      throw new IOException("Method not supported for Infrared medium");
  }
}


