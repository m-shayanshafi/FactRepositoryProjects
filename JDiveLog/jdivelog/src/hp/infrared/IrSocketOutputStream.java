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

import java.io.OutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

class IrSocketOutputStream extends OutputStream
{

  private IrSocketImpl impl;   
  private FileDescriptor  fd;

  private native void nativeWrite(FileDescriptor fd, byte[] b, int off, int len)
    throws IOException;

  private native void nativeWrite(FileDescriptor fd, int data)
    throws IOException;

  public IrSocketOutputStream(FileDescriptor the_fd, IrSocketImpl the_impl)
  {
    fd = the_fd;
    impl = the_impl;
  }

   public void close() throws IOException
  {
    impl.close();
  }

   public void write(byte[] b, int off, int len)
      throws IOException, NullPointerException,
	     IndexOutOfBoundsException
   {
     if (b == null)
       throw new NullPointerException();
     if (off < 0 || len < 0)
       throw new IndexOutOfBoundsException();
     if (b.length - off < len)
       throw new IndexOutOfBoundsException();
		      
     synchronized(fd){
       nativeWrite(fd, b, off, len);
     }
   }

  public void write(int b) throws IOException
  {
    synchronized (fd){
      nativeWrite(fd, b);
    }
  }
}
