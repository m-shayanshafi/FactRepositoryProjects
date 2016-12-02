/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: AbstractOSTCProtocol.java
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
package net.sf.jdivelog.ci.ostc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Logger;

import net.sf.jdivelog.ci.InvalidConfigurationException;
import net.sf.jdivelog.ci.NotInitializedException;
import net.sf.jdivelog.ci.TransferException;
import net.sf.jdivelog.comm.CommPortIdentifier;
import net.sf.jdivelog.comm.CommUtil;
import net.sf.jdivelog.comm.PortInUseException;
import net.sf.jdivelog.comm.PortNotFoundException;
import net.sf.jdivelog.comm.SerialPort;
import net.sf.jdivelog.comm.UnsupportedCommOperationException;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.statusbar.StatusInterface;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.util.Hexadecimal;

/**
 * Base Class for all OSTC Protocol Drivers.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public abstract class AbstractOSTCProtocol {
    
    private static final Logger LOGGER = Logger.getLogger(AbstractOSTCProtocol.class.getName());
    private CommPortIdentifier portIdentifier;
    private SerialPort commPort;
    private OutputStream out;
    private InputStream in;
    protected Map<Feature, OSTCValue> data;
    private byte[] eeprom;
    protected static Feature FEATURE_PROFILES = new Feature("profiles", Profiles.class);
    
    public AbstractOSTCProtocol() {
        commPort = null;
    }
    
    public void setCommPort(String port) throws InvalidConfigurationException {
        boolean portFound = false;
        Iterator<CommPortIdentifier> it = CommUtil.getInstance().getPortIdentifiers();
        while (it.hasNext()) {
            CommPortIdentifier cpId = it.next();
            if (port.equals(cpId.getName())) {
                portFound = true;
                portIdentifier = cpId;
            }
        }
        if (!portFound) {
            throw new InvalidConfigurationException(Messages.getString("suunto.comport_not_found"));
        }
    }
    
    public void download(StatusInterface status) throws PortNotFoundException, PortInUseException, UnsupportedCommOperationException, IOException, TransferException {
        startCommunication(status);
        try {
            write(status, new byte[] {0x61});
            byte[] buf = new byte[33034];
            int count = read(status, buf, 10000);
            if (count != 33034) {
                throw new TransferException(Messages.getString("suunto.comm_timeout"));
            }
            data = parseAll(buf);
            eeprom = new byte[252];
            System.arraycopy(buf, 10, eeprom, 0, eeprom.length);
            LOGGER.fine("load eeprom: "+Hexadecimal.valueOf(eeprom));
        } finally {
            endCommunication(status);
        }
    }
    
    public void downloadSettings(StatusInterface status) throws PortNotFoundException, PortInUseException, UnsupportedCommOperationException, IOException, TransferException {
        startCommunication(status);
        try {
            write(status, new byte[] {0x67});
            byte[] buf = new byte[256];
            int count = read(status, buf, 1500);
            if (count != 256) {
                throw new TransferException(Messages.getString("suunto.comm_timeout"));
            }
            data = parseSettings(buf);
            eeprom = new byte[252];
            System.arraycopy(buf, 4, eeprom, 0, eeprom.length);
            LOGGER.fine("load eeprom: "+Hexadecimal.valueOf(eeprom));
        } finally {
            endCommunication(status);
        }
    }
    
    public void upload(StatusInterface status) throws NotInitializedException, PortNotFoundException, PortInUseException, UnsupportedCommOperationException, IOException, TransferException {
        store(data, eeprom);
        if (eeprom == null || eeprom.length != 252) {
            throw new NotInitializedException();
        }
        LOGGER.fine("save eeprom: "+Hexadecimal.valueOf(eeprom));
        startCommunication(status);
        try {
            int count = 0;
            write(status, new byte[] {0x64});
            byte[] buf = new byte[1];
            count = read(status, buf, 70);
            if (count != 1 || buf[0] != 0x64) {
                throw new TransferException(Messages.getString("suunto.comm_timeout"));
            }
            for (int i=0; i<eeprom.length; i++) {
                buf[0] = eeprom[i];
                write(status, buf);
                byte[] res = new byte[1];
                long start = System.currentTimeMillis();
                sleep(2);
                int len = read(status, res, 60);
                if (len != 1 || res[0] != buf[0]) {
                    LOGGER.severe("Timeout on byte "+i+" len="+len+" res[0]="+Hexadecimal.valueOf(res[0])+", read-time="+(System.currentTimeMillis() - start));
                    printout(status, in);
                    throw new TransferException(Messages.getString("suunto.comm_timeout"));
                }
            }
        } finally {
            endCommunication(status);
        }
    }
    
    private static void printout(StatusInterface status, InputStream in) {
        long lastrec = System.currentTimeMillis();
        long current = lastrec;
        while (current - lastrec < 400) {
            try {
                byte[] b = new byte[256];
                int len = in.read(b);
                current = System.currentTimeMillis();
                if (len > 0) {
                    status.commReceive();
                    byte[] b2 = new byte[len];
                    System.arraycopy(b, 0, b2, 0, len);
                    LOGGER.fine("< "+Hexadecimal.valueOf(b2));
                    lastrec = current;
                }
            } catch (IOException e) {
                current = System.currentTimeMillis();
            }
        }
    }
    
    public void setTime(StatusInterface status, long millis) throws PortNotFoundException, PortInUseException, UnsupportedCommOperationException, IOException, TransferException {
        status.infiniteProgressbarStart();
        status.messageClear();
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(millis);
        int year = gc.get(Calendar.YEAR) - 2000;
        int month = gc.get(Calendar.MONTH) + 1;
        int day = gc.get(Calendar.DATE);
        int hour = gc.get(Calendar.HOUR_OF_DAY);
        int minute = gc.get(Calendar.MINUTE);
        int second = gc.get(Calendar.SECOND);
        byte[] data = new byte[] {(byte) hour, (byte)minute, (byte)second, (byte)month, (byte)day, (byte)year};
        startCommunication(status);
        try {
            write(status, new byte[] {0x62});
            byte[] res = new byte[1];
            int count = read(status, res, 100);
            if (count != 1 || res[0] != 0x62) {
                throw new TransferException(Messages.getString("suunto.comm_timeout"));
            }
            write(status, data);
        } finally {
            endCommunication(status);
        }
    }
    
    public void resetDecoInformation(StatusInterface status) throws PortNotFoundException, PortInUseException, UnsupportedCommOperationException, IOException, TransferException {
        status.infiniteProgressbarStart();
        status.messageClear();
        commPort = CommUtil.getInstance().open(portIdentifier);
        commPort.setSerialPortParams(115200, SerialPort.DataBits.DataBits_8, SerialPort.Parity.NONE, SerialPort.StopBits.StopBits_1);
        commPort.enableReceiveTimeout(20);
        out = commPort.getOutputStream();
        in = commPort.getInputStream();
        try {
            write(status, new byte[] {0x68});
            byte[] res = new byte[1];
            int count = read(status, res, 100);
            if (count != 1 || res[0] != 0x68) {
                throw new TransferException(Messages.getString("suunto.comm_timeout"));
            }
        } finally {
            endCommunication(status);
        }
    }

    public abstract String[] getFirmwareVersions();

    public abstract String[] getHashkeys();
    
    public abstract String getName();
    
    public abstract Feature[] getFeatures();
    
    public abstract boolean hasDecoInformationResetFunction();
    
    public Object getFeatureValue(Feature feature) throws UnknownFeatureException {
        checkFeature(feature);
        return data.get(feature);
    }
    
    public void setFeatureValue(Feature feature, OSTCValue value) throws UnknownFeatureException {
        checkFeature(feature);
        data.put(feature, value);
    }
    
    public TreeSet<JDive> getDives() {
        Profiles profiles = (Profiles)data.get(FEATURE_PROFILES);
        return profiles == null ? null : profiles.getDives();
    }

    protected abstract Map<Feature, OSTCValue> parseAll(byte[] data);
    
    protected abstract Map<Feature, OSTCValue> parseSettings(byte[] data);
    
    protected abstract void store(Map<Feature, OSTCValue> data, byte[] buffer);

    protected void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {

        }
    }
    
    protected final void write(StatusInterface status, byte[] data) throws IOException {
        status.commSend();
        out.write(data);
        out.flush();
    }

    protected final int read(StatusInterface status, byte[] ret, int timeout) throws IOException {
        long starttime = System.currentTimeMillis();
        int count = 0;
        while (count < ret.length) {
            int read = in.read(ret, count, ret.length - count);
            if (read > 0) {
                status.commReceive();
                count += read;
            }
            if (System.currentTimeMillis() - starttime > timeout) {
                return count;
            }
        }
        return ret.length;
    }
    
    private void checkFeature(Feature feature) throws UnknownFeatureException {
        Feature[] features = getFeatures();
        for (int i=0; i<features.length; i++) {
            if (features[i].equals(feature)) {
                return;
            }
        }
        throw new UnknownFeatureException(feature);
    }

    /**
     * @param status
     * @throws PortNotFoundException
     * @throws PortInUseException
     * @throws UnsupportedCommOperationException
     * @throws IOException
     */
    protected void startCommunication(StatusInterface status) throws PortNotFoundException, PortInUseException, UnsupportedCommOperationException, IOException {
        status.infiniteProgressbarStart();
        commPort = CommUtil.getInstance().open(portIdentifier);
        commPort.setSerialPortParams(115200, SerialPort.DataBits.DataBits_8, SerialPort.Parity.NONE, SerialPort.StopBits.StopBits_1);
        commPort.enableReceiveTimeout(10);
        out = commPort.getOutputStream();
        in = commPort.getInputStream();
    }

    /**
     * @param status
     */
    protected void endCommunication(StatusInterface status) {
        commPort.close();
        status.infiniteProgressbarEnd();
    }

}
