/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: RxTx21SerialPort.java
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
import java.util.Enumeration;

/**
 * Our RXTX-2.1 Representation of a Serial Port.
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class RxTx21SerialPort implements SerialPort {
    
    private gnu.io.SerialPort delegate; 
    
    public RxTx21SerialPort(gnu.io.SerialPort commPort) {
        delegate = commPort;
    }
    
    public static SerialPort open(CommPortIdentifier identifier, String lock, int timeout) throws PortNotFoundException, PortInUseException {
        Enumeration<?> e = gnu.io.CommPortIdentifier.getPortIdentifiers();
        boolean portFound = false;
        while (e.hasMoreElements() && !portFound) {
            gnu.io.CommPortIdentifier cpId = (gnu.io.CommPortIdentifier) e.nextElement();
            if (identifier.getNativeName().equals(cpId.getName())) {
                portFound = true;
                gnu.io.SerialPort commPort;
                try {
                    commPort = (gnu.io.SerialPort) cpId.open(lock, timeout);
                    return new RxTx21SerialPort(commPort);
                } catch (gnu.io.PortInUseException e1) {
                    throw new PortInUseException(e1);
                }
            }
        }
        throw new PortNotFoundException();
    }

    public void setSerialPortParams(int baudRate, DataBits dataBits, Parity parity, StopBits stopBits) throws UnsupportedCommOperationException {
        try {
            delegate.setSerialPortParams(baudRate, getDatabits(dataBits), getStopBits(stopBits), getParity(parity));
        } catch (gnu.io.UnsupportedCommOperationException e) {
            throw new UnsupportedCommOperationException(e);
        }
    }

    public void enableReceiveTimeout(int timeout) throws UnsupportedCommOperationException {
        try {
            delegate.enableReceiveTimeout(timeout);
        } catch (gnu.io.UnsupportedCommOperationException e) {
            throw new UnsupportedCommOperationException(e);
        }
    }

    public void setDTR(boolean bool) {
        delegate.setDTR(bool);
    }

    public void setRTS(boolean bool) {
        delegate.setRTS(bool);
    }

    public OutputStream getOutputStream() throws IOException {
        return delegate.getOutputStream();
    }

    public InputStream getInputStream() throws IOException {
        return delegate.getInputStream();
    }

    public void close() {
        delegate.close();
    }
    
    private int getDatabits(SerialPort.DataBits databits) {
        if (databits == SerialPort.DataBits.DataBits_8) {
            return gnu.io.SerialPort.DATABITS_8;
        } else if (databits == SerialPort.DataBits.DataBits_7) {
            return gnu.io.SerialPort.DATABITS_7;
        } else if (databits == SerialPort.DataBits.DataBits_6) {
            return gnu.io.SerialPort.DATABITS_6;
        } else if (databits == SerialPort.DataBits.DataBits_5) {
            return gnu.io.SerialPort.DATABITS_5;
        }
        throw new RuntimeException("databits not supported "+databits);
    }
    
    private int getParity(SerialPort.Parity parity) {
        if (parity == SerialPort.Parity.EVEN) {
            return gnu.io.SerialPort.PARITY_EVEN;
        } else if (parity == SerialPort.Parity.MARK) {
            return gnu.io.SerialPort.PARITY_MARK;
        } else if (parity == SerialPort.Parity.NONE) {
            return gnu.io.SerialPort.PARITY_NONE;
        } else if (parity == SerialPort.Parity.ODD) {
            return gnu.io.SerialPort.PARITY_ODD;
        } else if (parity == SerialPort.Parity.SPACE) {
            return gnu.io.SerialPort.PARITY_SPACE;
        }
        throw new RuntimeException("parity not supported "+parity);
    }
    
    private int getStopBits(SerialPort.StopBits stopBits) {
        if (stopBits == SerialPort.StopBits.StopBits_1) {
            return gnu.io.SerialPort.STOPBITS_1;
        } else if (stopBits == SerialPort.StopBits.StopBits_1_5) {
            return gnu.io.SerialPort.STOPBITS_1_5;
        } else if (stopBits == SerialPort.StopBits.StopBits_2) {
            return gnu.io.SerialPort.STOPBITS_2;
        }
        throw new RuntimeException("stopbits not supported "+stopBits);
    }

}
