/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: SerialPort.java
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 * 
 * This file is part of JDiveLog.
 * JDiveLog is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * JDiveLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JDiveLog; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sf.jdivelog.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Out way to represent a serial Port
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public interface SerialPort {
    
    public void setSerialPortParams(int baudRate, DataBits dataBits, Parity parity, StopBits stopBits) throws UnsupportedCommOperationException;
    
    public void enableReceiveTimeout(int timeout) throws UnsupportedCommOperationException;
    
    public void setDTR(boolean bool);
    
    public void setRTS(boolean bool);
    
    public OutputStream getOutputStream() throws IOException;
    
    public InputStream getInputStream() throws IOException;
    
    public void close();
    
    //
    // inner classes and types
    //
    
    public static enum DataBits {
        DataBits_5, DataBits_6, DataBits_7, DataBits_8;
    }
    
    public static enum Parity {
        EVEN, MARK, NONE, ODD, SPACE;
    }
    
    public static enum StopBits {
        StopBits_1, StopBits_1_5, StopBits_2;
    }

}
