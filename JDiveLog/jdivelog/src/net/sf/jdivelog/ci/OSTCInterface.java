/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: OSTCInterface.java
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
package net.sf.jdivelog.ci;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.jdivelog.ci.ostc.AbstractOSTCProtocol;
import net.sf.jdivelog.ci.ostc.Feature;
import net.sf.jdivelog.ci.ostc.OSTCValue;
import net.sf.jdivelog.ci.ostc.UnknownFeatureException;
import net.sf.jdivelog.comm.CommPortIdentifier;
import net.sf.jdivelog.comm.CommUtil;
import net.sf.jdivelog.comm.PortInUseException;
import net.sf.jdivelog.comm.PortNotFoundException;
import net.sf.jdivelog.comm.SerialPort;
import net.sf.jdivelog.comm.UnsupportedCommOperationException;
import net.sf.jdivelog.gui.ostc.sim.DummyOstcSimulator;
import net.sf.jdivelog.gui.ostc.sim.OstcSimulator;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.statusbar.StatusInterface;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.JDiveLog;
import net.sf.jdivelog.util.Hexadecimal;

/**
 * Driver for the "Open Source Tauchcomputer" of HeinrichsWeikamp.
 * OSTC @link http://www.heinrichsweikamp.net/ostc/en/index.htm
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class OSTCInterface implements ComputerInterface {
    
    public static final String DRIVER_NAME = "OSTC";
    private static final Logger LOGGER = Logger.getLogger(OSTCInterface.class.getName());
    private static final String[] DRIVER_CLASS_NAMES = { "net.sf.jdivelog.ci.ostc.OSTCProtocol070511", "net.sf.jdivelog.ci.ostc.OSTCProtocol080115",
                                                         "net.sf.jdivelog.ci.ostc.OSTCProtocol080121", "net.sf.jdivelog.ci.ostc.OSTCProtocol080321",
                                                         "net.sf.jdivelog.ci.ostc.OSTCProtocol081130", "net.sf.jdivelog.ci.ostc.OSTCProtocol100801",
                                                         "net.sf.jdivelog.ci.ostc.OSTCProtocol100801Mk2", "net.sf.jdivelog.ci.ostc.OSTCProtocol110119Mk2" }; 
    private static final String[] PROPERTY_NAMES = { "ostc.commport", "ostc.protocol" };
    private static final Map<String, AbstractOSTCProtocol> drivers;
    private static final Map<String, String> HASHKEYS;
    private static final Map<String, String> VERSIONS;
    private OSTCConfigurationPanel configurationPanel;
    private Properties properties;
    private AbstractOSTCProtocol driver;
    private TreeSet<JDive> jdives;
    
    static {
        drivers = new HashMap<String, AbstractOSTCProtocol>();
        VERSIONS = new HashMap<String, String>();
        HASHKEYS = new HashMap<String, String>();
        for (int i=0; i<DRIVER_CLASS_NAMES.length; i++) {
            try {
                Class<?> driverClass = Class.forName(DRIVER_CLASS_NAMES[i]);
                Object o = driverClass.newInstance();
                if (o instanceof AbstractOSTCProtocol) {
                    AbstractOSTCProtocol driver = (AbstractOSTCProtocol) o;
                    drivers.put(driver.getName(), driver);
                    String[] versions = driver.getFirmwareVersions();
                    for (int j=0; j<versions.length; j++) {
                        VERSIONS.put(versions[j], driver.getName());
                    }
                    String[] hashkeys = driver.getHashkeys();
                    for (int j=0; j<hashkeys.length; j++) {
                        HASHKEYS.put(hashkeys[j], driver.getName());
                    }
                }
            } catch (ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, "could not instantiate driver '"+DRIVER_CLASS_NAMES[i]+"'", e);
            } catch (InstantiationException e) {
                LOGGER.log(Level.SEVERE, "could not instantiate driver '"+DRIVER_CLASS_NAMES[i]+"'", e);
            } catch (IllegalAccessException e) {
                LOGGER.log(Level.SEVERE, "could not instantiate driver '"+DRIVER_CLASS_NAMES[i]+"'", e);
            }
        }
    }

    /**
     * @see net.sf.jdivelog.ci.ComputerInterface#getDriverName()
     */
    public String getDriverName() {
        return DRIVER_NAME;
    }

    /**
     * @see net.sf.jdivelog.ci.ComputerInterface#getPropertyNames()
     */
    public String[] getPropertyNames() {
        return PROPERTY_NAMES;
    }

    /**
     * @see net.sf.jdivelog.ci.ComputerInterface#getConfigurationPanel()
     */
    public OSTCConfigurationPanel getConfigurationPanel() {
        if (configurationPanel == null) {
            configurationPanel = new OSTCConfigurationPanel();
        }
        return configurationPanel;
    }

    /**
     * @see net.sf.jdivelog.ci.ComputerInterface#saveConfiguration()
     */
    public Properties saveConfiguration() {
        if (properties == null) {
            properties = new Properties();
        }
        properties.setProperty(PROPERTY_NAMES[0], getConfigurationPanel().getCommPort());
        String protocol = getConfigurationPanel().getProtocol();
        if (protocol != null) {
            properties.setProperty(PROPERTY_NAMES[1], protocol);
        } else {
            properties.remove(PROPERTY_NAMES[1]);
        }
        return properties;
    }

    /**
     * @see net.sf.jdivelog.ci.ComputerInterface#initialize(java.util.Properties)
     */
    public void initialize(Properties properties) {
        this.properties = new Properties();
        this.driver = null;
        if (properties != null) {
            Iterator<Object> nameIt = properties.keySet().iterator();
            while (nameIt.hasNext()) {
                String name = nameIt.next().toString();
                this.properties.setProperty(name, properties.get(name).toString());
            }
        }
        getConfigurationPanel().setCommPort(this.properties.getProperty(PROPERTY_NAMES[0]));
        getConfigurationPanel().setProtocol(this.properties.getProperty(PROPERTY_NAMES[1]));
    }
    
    public void transferSettings(StatusInterface statusInterface)  throws TransferException, NotInitializedException, InvalidConfigurationException {
        if (properties == null) {
            throw new NotInitializedException();
        }
        String protocolName = this.properties.getProperty(PROPERTY_NAMES[1]);
        if (protocolName == null || "".equals(protocolName) || drivers.get(protocolName) == null) {
            protocolName = detectDriverName(statusInterface);
            if (protocolName == null) {
                throw new InvalidConfigurationException(Messages.getString("ostc.protocol_not_set"));
            }
        }
        String port = this.properties.getProperty(PROPERTY_NAMES[0]);
        if (port == null || "".equals(port)) {
            throw new InvalidConfigurationException(Messages.getString("suunto.comport_not_set"));
        }
        driver = drivers.get(protocolName);
        driver.setCommPort(port);
        try {
            driver.downloadSettings(statusInterface);
        } catch (PortInUseException piuex) {
            LOGGER.log(Level.SEVERE, "transfer of settings failed", piuex);
            throw new TransferException(Messages.getString("suunto.comport_in_use"));
        } catch (UnsupportedCommOperationException ucoex) {
            LOGGER.log(Level.SEVERE, "transfer of settings failed", ucoex);
            throw new TransferException(Messages.getString("suunto.could_not_set_comparams"));
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, "transfer of settings failed", ioe);
            throw new TransferException(Messages.getString("suunto.ioexception"));
        } catch (PortNotFoundException pnfe) {
            LOGGER.log(Level.SEVERE, "transfer of settings failed", pnfe);
            throw new TransferException(Messages.getString("suunto.comport_not_found"));
        }
    }
    
    /**
     * @see net.sf.jdivelog.ci.ComputerInterface#transfer(net.sf.jdivelog.gui.statusbar.StatusInterface, JDiveLog)
     */
    public void transfer(StatusInterface statusInterface, JDiveLog logbook) throws TransferException, NotInitializedException, InvalidConfigurationException {
        if (properties == null) {
            throw new NotInitializedException();
        }
        String protocolName = this.properties.getProperty(PROPERTY_NAMES[1]);
        if (protocolName == null || "".equals(protocolName) || drivers.get(protocolName) == null) {
            protocolName = detectDriverName(statusInterface);
            if (protocolName == null) {
                throw new InvalidConfigurationException(Messages.getString("ostc.protocol_not_set"));
            }
        }
        String port = this.properties.getProperty(PROPERTY_NAMES[0]);
        if (port == null || "".equals(port)) {
            throw new InvalidConfigurationException(Messages.getString("suunto.comport_not_set"));
        }
        driver = drivers.get(protocolName);
        driver.setCommPort(port);
        try {
            driver.download(statusInterface);
            jdives = driver.getDives();
        } catch (PortInUseException piuex) {
            LOGGER.log(Level.SEVERE, "transfer failed", piuex);
            throw new TransferException(Messages.getString("suunto.comport_in_use"));
        } catch (UnsupportedCommOperationException ucoex) {
            LOGGER.log(Level.SEVERE, "transfer failed", ucoex);
            throw new TransferException(Messages.getString("suunto.could_not_set_comparams"));
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, "transfer failed", ioe);
            throw new TransferException(Messages.getString("suunto.ioexception"));
        } catch (PortNotFoundException pnfe) {
            LOGGER.log(Level.SEVERE, "transfer failed", pnfe);
            throw new TransferException(Messages.getString("suunto.comport_not_found"));
        }
        
    }
    
    public void save(StatusInterface status) throws NotInitializedException, TransferException {
        if (driver == null) {
            throw new NotInitializedException();
        }
        try {
            driver.upload(status);
        } catch (PortInUseException piuex) {
            LOGGER.log(Level.SEVERE, "save failed", piuex);
            throw new TransferException(Messages.getString("suunto.comport_in_use"));
        } catch (UnsupportedCommOperationException ucoex) {
            LOGGER.log(Level.SEVERE, "save failed", ucoex);
            throw new TransferException(Messages.getString("suunto.could_not_set_comparams"));
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, "save failed", ioe);
            throw new TransferException(Messages.getString("suunto.ioexception"));
        } catch (PortNotFoundException pnfe) {
            LOGGER.log(Level.SEVERE, "save failed", pnfe);
            throw new TransferException(Messages.getString("suunto.comport_not_found"));
        }
    }
    
    public Feature[] getFeatures() {
        return driver.getFeatures();
    }
    
    public void setFeature(Feature feature, OSTCValue value) throws UnknownFeatureException {
        driver.setFeatureValue(feature, value);
    }
    
    public Object getFeature(Feature feature) throws UnknownFeatureException {
        return driver.getFeatureValue(feature);
        
    }

    /**
     * @see net.sf.jdivelog.ci.ComputerInterface#getDives()
     */
    public TreeSet<JDive> getDives() {
        return jdives;
    }
    
    public void setTime(StatusInterface status, long millis) throws NotInitializedException, TransferException, InvalidConfigurationException {
        String protocolName = this.properties.getProperty(PROPERTY_NAMES[1]);
        if (protocolName == null || "".equals(protocolName) || drivers.get(protocolName) == null) {
            protocolName = detectDriverName(status);
            if (protocolName == null) {
                throw new InvalidConfigurationException(Messages.getString("ostc.protocol_not_set"));
            }
        }
        String port = this.properties.getProperty(PROPERTY_NAMES[0]);
        if (port == null || "".equals(port)) {
            throw new InvalidConfigurationException(Messages.getString("suunto.comport_not_set"));
        }
        driver = drivers.get(protocolName);
        driver.setCommPort(port);
        if (driver == null) {
            throw new NotInitializedException();
        }
        try {
            driver.setTime(status, millis);
        } catch (PortInUseException piuex) {
            LOGGER.log(Level.SEVERE, "set time failed", piuex);
        } catch (UnsupportedCommOperationException ucoex) {
            LOGGER.log(Level.SEVERE, "set time failed", ucoex);
            throw new TransferException(Messages.getString("suunto.could_not_set_comparams"));
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, "set time failed", ioe);
            throw new TransferException(Messages.getString("suunto.ioexception"));
        } catch (PortNotFoundException pnfe) {
            LOGGER.log(Level.SEVERE, "set time failed", pnfe);
            throw new TransferException(Messages.getString("suunto.comport_not_found"));
        }
    }

    public void resetDecoInformation(StatusInterface status) throws NotInitializedException, TransferException, InvalidConfigurationException {
        String protocolName = this.properties.getProperty(PROPERTY_NAMES[1]);
        if (protocolName == null || "".equals(protocolName) || drivers.get(protocolName) == null) {
            protocolName = detectDriverName(status);
            if (protocolName == null) {
                throw new InvalidConfigurationException(Messages.getString("ostc.protocol_not_set"));
            }
        }
        String port = this.properties.getProperty(PROPERTY_NAMES[0]);
        if (port == null || "".equals(port)) {
            throw new InvalidConfigurationException(Messages.getString("suunto.comport_not_set"));
        }
        driver = drivers.get(protocolName);
        driver.setCommPort(port);
        if (driver == null) {
            throw new NotInitializedException();
        }
        try {
            if (driver.hasDecoInformationResetFunction()) {
                driver.resetDecoInformation(status);
            }
        } catch (PortInUseException piuex) {
            LOGGER.log(Level.SEVERE, "reset decoinformation failed", piuex);
            throw new TransferException(Messages.getString("suunto.comport_in_use"));
        } catch (UnsupportedCommOperationException ucoex) {
            LOGGER.log(Level.SEVERE, "reset decoinformation failed", ucoex);
            throw new TransferException(Messages.getString("suunto.could_not_set_comparams"));
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, "reset decoinformation failed", ioe);
            throw new TransferException(Messages.getString("suunto.ioexception"));
        } catch (PortNotFoundException pnfe) {
            LOGGER.log(Level.SEVERE, "reset decoinformation failed", pnfe);
            throw new TransferException(Messages.getString("suunto.comport_not_found"));
        }
    }
    
    public OstcSimulator getOstcSimulator(StatusInterface status) {
        String port = this.properties.getProperty(PROPERTY_NAMES[0]);
        if (port == null || "".equals(port)) {
            return new DummyOstcSimulator();
        }
        OSTCSimulatorImpl simulator = new OSTCSimulatorImpl(status, port);
        return simulator;
    }
    
    public void stopDiveMode(StatusInterface status) throws InvalidConfigurationException {
        String port = this.properties.getProperty(PROPERTY_NAMES[0]);
        if (port == null || "".equals(port)) {
            throw new InvalidConfigurationException(Messages.getString("suunto.comport_not_set"));
        }
        boolean portFound = false;
        Iterator<CommPortIdentifier> it = CommUtil.getInstance().getPortIdentifiers();
        CommPortIdentifier portIdentifier = null;
        while (!portFound && it.hasNext()) {
            CommPortIdentifier cpId = (CommPortIdentifier) it.next();
            if (port.equals(cpId.getName())) {
                portFound = true;
                portIdentifier  = cpId;
            }
        }
        if (!portFound) {
            throw new InvalidConfigurationException(Messages.getString("suunto.comport_not_found"));
        }

        SerialPort commPort = null;
        try {
            commPort = CommUtil.getInstance().open(portIdentifier);
            commPort.setSerialPortParams(115200, SerialPort.DataBits.DataBits_8, SerialPort.Parity.NONE, SerialPort.StopBits.StopBits_1);
            commPort.enableReceiveTimeout(500);
            OutputStream out = commPort.getOutputStream();
            InputStream in = commPort.getInputStream();
            write(status, out, new byte[] { 10 });
            byte[] res = new byte[1];
            read(status, in, res, 2000);
            sleep(1000);
        } catch (PortNotFoundException e) {
            LOGGER.log(Level.FINE, "driver detection failed", e);
        } catch (PortInUseException e) {
            LOGGER.log(Level.FINE, "driver detection failed", e);
        } catch (UnsupportedCommOperationException e) {
            LOGGER.log(Level.FINE, "driver detection failed", e);
        } catch (IOException e) {
            LOGGER.log(Level.FINE, "driver detection failed", e);
        } finally {
            if (commPort != null) {
                commPort.close();
            }
        }
    }
    
    private String detectDriverName(StatusInterface status) throws InvalidConfigurationException {
        String port = this.properties.getProperty(PROPERTY_NAMES[0]);
        if (port == null || "".equals(port)) {
            throw new InvalidConfigurationException(Messages.getString("suunto.comport_not_set"));
        }
        boolean portFound = false;
        Iterator<CommPortIdentifier> it = CommUtil.getInstance().getPortIdentifiers();
        CommPortIdentifier portIdentifier = null;
        while (!portFound && it.hasNext()) {
            CommPortIdentifier cpId = (CommPortIdentifier) it.next();
            if (port.equals(cpId.getName())) {
                portFound = true;
                portIdentifier  = cpId;
            }
        }
        if (!portFound) {
            throw new InvalidConfigurationException(Messages.getString("suunto.comport_not_found"));
        }
        
        String drivername = null; // "OSTC 0.23, 1.0" ?

        SerialPort commPort = null;
        try {
            commPort = CommUtil.getInstance().open(portIdentifier);
            commPort.setSerialPortParams(115200, SerialPort.DataBits.DataBits_8, SerialPort.Parity.NONE, SerialPort.StopBits.StopBits_1);
            commPort.enableReceiveTimeout(500);
            OutputStream out = commPort.getOutputStream();
            InputStream in = commPort.getInputStream();
            write(status, out, new byte[] {0x65});
            byte[] hashbuf = new byte[18];
            int count = read(status, in, hashbuf, 2000);
            if (count != 18) {
                LOGGER.warning("Could not autodetect driver, result-length of checkFirmware command: "+count);
                return null;
            }
            LOGGER.info("Firmware Version: "+hashbuf[0]+"."+hashbuf[1]);
            byte[] hb2 = new byte[16];
            System.arraycopy(hashbuf, 2, hb2, 0, 16);
            String hashkey = Hexadecimal.valueOf(hb2);
            LOGGER.info("MD2Hash: "+hashkey);
            drivername = getDriverNameByHashkey(hashkey);
            if (drivername == null) {
                String version = (hashbuf[0] & 0xff) + "." + (hashbuf[1] & 0xff);
                drivername = getDriverByFirmwareVersion(version);
            }
            if (drivername == null) {
                LOGGER.warning("Could not detect driver name");
            } else {
                LOGGER.info("Detected driver "+drivername);
            }
            sleep(1000);

        } catch (PortNotFoundException e) {
            LOGGER.log(Level.FINE, "driver detection failed", e);
        } catch (PortInUseException e) {
            LOGGER.log(Level.FINE, "driver detection failed", e);
        } catch (UnsupportedCommOperationException e) {
            LOGGER.log(Level.FINE, "driver detection failed", e);
        } catch (IOException e) {
            LOGGER.log(Level.FINE, "driver detection failed", e);
        } finally {
            if (commPort != null) {
                commPort.close();
            }
        }

        return drivername;
    }
    
    public static String getDriverNameByHashkey(String hashkey) {
        return HASHKEYS.get(hashkey);
    }

    public static String getDriverByFirmwareVersion(String version) {
        return VERSIONS.get(version);
    }

    
    private static void write(StatusInterface status, OutputStream out, byte[] data) throws IOException {
        status.commSend();
        out.write(data);
    }

    private static int read(StatusInterface status, InputStream in, byte[] ret, int timeout) throws IOException {
        int timeelapsed = 0;
        byte[] buf = new byte[ret.length];
        int count = 0;
        while (count < ret.length) {
            if (in.available() > 0) {
                int read = in.read(buf, 0, ret.length - count);
                for (int i = 0; i < read; i++) {
                    ret[count + i] = buf[i];
                }
                if (read > 0) {
                    status.commReceive();
                    count += read;
                }
            }
            if (timeout > 0 && timeelapsed > timeout) {
                return count;
            }
            if (count < ret.length) {
                sleep(100);
                timeelapsed += 100;
            }
        }
        return ret.length;
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {

        }
    }
    
    private class OSTCConfigurationPanel extends JPanel  {

        private static final long serialVersionUID = -3841826987380000963L;

        private Vector<String> availablePorts;

        private JComboBox protocolList;

        private JComboBox portList;

        public OSTCConfigurationPanel() {
            availablePorts = new Vector<String>();
            Iterator<CommPortIdentifier> it = CommUtil.getInstance().getPortIdentifiers();
            while (it.hasNext()) {
                CommPortIdentifier cpId = (CommPortIdentifier) it.next();
                availablePorts.add(cpId.getName());
            }
            JLabel labelCommport = new JLabel(Messages.getString("suunto.commport"));
            JLabel labelComputermodel = new JLabel(Messages.getString("ostc.protocol"));
            setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.anchor = GridBagConstraints.NORTHWEST;
            gc.gridy = 0;
            gc.gridx = 0;
            add(labelCommport, gc);
            gc.gridx = 1;
            add(getPortList(), gc);
            gc.gridy = 1;
            gc.gridx = 0;
            add(labelComputermodel, gc);
            gc.gridx = 1;
            add(getProtocolList(), gc);
        }

        public String getCommPort() {
            return String.valueOf(getPortList().getSelectedItem());
        }

        public String getProtocol() {
            if (getProtocolList().getSelectedIndex() != 0) {
                return String.valueOf(getProtocolList().getSelectedItem());
            }
            return null;
        }

        public void setCommPort(String commPort) {
            getPortList().setSelectedItem(commPort);
            revalidate();
        }

        public void setProtocol(String protocol) {
            if (protocol == null || Messages.getString("auto").equals(protocol)) {
                getProtocolList().setSelectedIndex(0);
            } else {
                getProtocolList().setSelectedItem(protocol);
            }
            revalidate();
        }

        //
        // private methods
        //

        private JComboBox getProtocolList() {
            if (protocolList == null) {
                ArrayList<String> pn = new ArrayList<String>();
                pn.add(Messages.getString("auto"));
                pn.addAll(drivers.keySet());
                String[] protocolNames = new String[pn.size()];
                pn.toArray(protocolNames);
                protocolList = new JComboBox(protocolNames);
            }
            return protocolList;
        }

        private JComboBox getPortList() {
            if (portList == null) {
                portList = new JComboBox(availablePorts);
            }
            return portList;
        }
        
    }
    
    private class OSTCSimulatorImpl implements OstcSimulator {
        
        private final StatusInterface status;
        private final String port;
        private OutputStream out;
        private SerialPort commPort;

        public OSTCSimulatorImpl(StatusInterface status, String port) {
            this.status = status;
            this.port = port;
        }

        public void initialize() throws InvalidConfigurationException, PortNotFoundException, PortInUseException, UnsupportedCommOperationException, IOException, TransferException {
            Iterator<CommPortIdentifier> it = CommUtil.getInstance().getPortIdentifiers();
            CommPortIdentifier portIdentifier = null;
            boolean portFound = false;
            while (!portFound && it.hasNext()) {
                CommPortIdentifier cpId = (CommPortIdentifier) it.next();
                if (port.equals(cpId.getName())) {
                    portFound = true;
                    portIdentifier  = cpId;
                }
            }
            if (!portFound) {
                throw new InvalidConfigurationException(Messages.getString("suunto.comport_not_found"));
            }
            
            commPort = CommUtil.getInstance().open(portIdentifier);
            commPort.setSerialPortParams(115200, SerialPort.DataBits.DataBits_8, SerialPort.Parity.NONE, SerialPort.StopBits.StopBits_1);
            commPort.enableReceiveTimeout(500);
            out = commPort.getOutputStream();
            sleep(200);
            write(status, out, new byte[] {0x63});
        }

        public void setDepth(long millibar) throws IOException {
            LOGGER.finer("setDepth "+millibar+"mbar");
            int hundredmbar = 10 + (int) (millibar/100);
            byte[] data = new byte[1];
            data[0] = (byte)(hundredmbar & 0xff);
            LOGGER.finer("data = "+data[0]);
            write(status, out, data);
        }

        public void cleanup() throws IOException {
            write(status, out, new byte[] { 10 });
            commPort.close();
        }
        
    }

}
