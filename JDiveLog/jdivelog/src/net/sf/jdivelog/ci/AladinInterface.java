/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: AladinInterface.java
 * 
 * @author Volker Holthaus <volker.urlaub@gmx.de>
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
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.jdivelog.comm.CommPortIdentifier;
import net.sf.jdivelog.comm.CommUtil;
import net.sf.jdivelog.comm.PortInUseException;
import net.sf.jdivelog.comm.PortNotFoundException;
import net.sf.jdivelog.comm.SerialPort;
import net.sf.jdivelog.comm.UnsupportedCommOperationException;
import net.sf.jdivelog.comm.SerialPort.DataBits;
import net.sf.jdivelog.comm.SerialPort.Parity;
import net.sf.jdivelog.comm.SerialPort.StopBits;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.statusbar.StatusInterface;
import net.sf.jdivelog.model.AladinAdapter;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.JDiveLog;
import net.sf.jdivelog.model.aladin.AladinData;

/**
 * Description: Information in order to communicate with Aladin computer and
 * displaying the set up window
 * 
 * @author Volker Holthaus <volker.urlaub@gmx.de>
 */

public class AladinInterface implements ComputerInterface {
    
    private static final Logger LOGGER = Logger.getLogger(AladinInterface.class.getName());

    private static final String[] PROPERTY_NAMES = { "aladin.commport", "aladin.computermodel", "aladin.connectionspeed", "aladin.databits", "aladin.stopbits",
            "aladin.parity" };

    private static final String[] COMPUTERMODEL_NAMES = { "unbekannt", "unbekannt", "Aladin Air", "Spiro Monitor 2 Plus", "Aladin Sport", "Aladin Pro",
            "unbekannt", "Aladin Air Z/X", "Aladin Pro", "Mares Genius", "unbekannt", "Aladin Air Z/X", "Spiro Monitor 3 Air", "unbekannt",
            "Aladin Air Z/X O2", "unbekannt", "Aladin Air Z/X Nitrox", "Aladin Pro Nitrox" };

    private static final int TIME_OUT = 15000;

    private static final Integer[] CONNECTION_SPEED = { 19200, 9600, 2400 };

    private static final DataBits[] CONNECTION_DATA_BITS = { DataBits.DataBits_5, DataBits.DataBits_6,
            DataBits.DataBits_7, DataBits.DataBits_8 };

    private static final StopBits[] CONNECTION_STOP_BITS = { StopBits.StopBits_1, StopBits.StopBits_2,
            StopBits.StopBits_1_5 };

    private static final Parity[] CONNECTION_PARITY = { Parity.NONE, Parity.ODD, Parity.EVEN,
            Parity.MARK, Parity.SPACE };

    private static final int[] COMPUTERMODEL_IDS = { 0x00, 0x14, 0x1c, 0x1d, 0x1e, 0x1f, 0x24, 0x34, 0x3f, 0x40, 0x41, 0x44, 0x48, 0x73, 0xa4, 0xbc, 0xf4, 0xff };

    private Properties properties = null;

    private TreeSet<JDive> jdives = null;

    private SerialPort commPort = null;

    private InputStream in = null;

    private AladinConfigurationPanel configurationPanel;

    /**
     * Return the name of the driver used
     * 
     * @return driver name
     */
    public String getDriverName() {
        return "Aladin Computer";
    }

    /**
     * Return the properties to use
     * 
     * @return PROPERTIES
     */
    public String[] getPropertyNames() {
        return PROPERTY_NAMES;
    }

    /**
     * Return the panel to configure the Aladin
     * 
     * @return ConfigurationPanel
     */
    public AladinInterface.AladinConfigurationPanel getConfigurationPanel() {
        if (configurationPanel == null) {
            configurationPanel = new AladinConfigurationPanel();
        }
        return configurationPanel;
    }

    /**
     * Return the configuration of the interface with the computer
     * 
     * @return Properties
     */
    public Properties saveConfiguration() {
        if (properties == null) {
            properties = new Properties();
        }
        properties.setProperty(PROPERTY_NAMES[0], getConfigurationPanel().getCommPort());
        properties.setProperty(PROPERTY_NAMES[1], getConfigurationPanel().getModel());
        properties.setProperty(PROPERTY_NAMES[2], getConfigurationPanel().getConnectionSpeed().toString());
        properties.setProperty(PROPERTY_NAMES[3], getConfigurationPanel().getDataBits().toString());
        properties.setProperty(PROPERTY_NAMES[4], getConfigurationPanel().getStopBits().toString());
        properties.setProperty(PROPERTY_NAMES[5], getConfigurationPanel().getParity().toString());
        return properties;
    }

    /**
     * Initialization of the interface with the configuration properties
     * 
     * @param properties
     */
    public void initialize(Properties properties) {
        this.properties = new Properties();
        if (properties != null) {
            Iterator<Object> nameIt = properties.keySet().iterator();
            while (nameIt.hasNext()) {
                String name = nameIt.next().toString();
                this.properties.setProperty(name, properties.get(name).toString());
            }
        }
        getConfigurationPanel().setCommPort(this.properties.getProperty(PROPERTY_NAMES[0]));
        try {
            getConfigurationPanel().setModel(Integer.parseInt(this.properties.getProperty(PROPERTY_NAMES[1])));
        } catch (NumberFormatException nfe) {
        }
        try {
            getConfigurationPanel().setConnectionSpeed(new Integer(this.properties.getProperty(PROPERTY_NAMES[2])));
        } catch (NumberFormatException nfe) {
        }
        try {
            getConfigurationPanel().setDataBits(DataBits.valueOf(this.properties.getProperty(PROPERTY_NAMES[3])));
        } catch (NullPointerException npe) {
        }
        try {
            getConfigurationPanel().setStopBits(StopBits.valueOf(this.properties.getProperty(PROPERTY_NAMES[4])));
        } catch (NullPointerException npe) {
        }
        try {
            getConfigurationPanel().setParity(Parity.valueOf(this.properties.getProperty(PROPERTY_NAMES[5])));
        } catch (NullPointerException npe) {
        }
    }

    /**
     * Transfer the datas
     * @param status
     * @param logbook
     * @throws TransferException
     * @throws NotInitializedException
     * @throws InvalidConfigurationException
     */
    public void transfer(StatusInterface status, JDiveLog logbook) throws TransferException, NotInitializedException, InvalidConfigurationException {
        byte[] read_byte = new byte[1];
        int[] read_data = new int[2046];
        int i = 0;
        int t = 0;
        int timeout = 0;

        if (properties == null) {
            throw new NotInitializedException();
        }
        String port = (String) properties.get(PROPERTY_NAMES[0]);
        String model = (String) properties.get(PROPERTY_NAMES[1]);
        if (port == null) {
            throw new InvalidConfigurationException(Messages.getString("aladin.comport_not_set"));
        }
        if (model == null) {
            throw new InvalidConfigurationException(Messages.getString("aladin.model_not_set"));
        }
        boolean portFound = false;
        CommPortIdentifier portIdentifier = null;
        Iterator<CommPortIdentifier> it = CommUtil.getInstance().getPortIdentifiers();
        while (it.hasNext()) {
            CommPortIdentifier cpId = it.next();
            if (port.equals(cpId.getName())) {
                portFound = true;
                portIdentifier = cpId;
            }
        }
        if (!portFound) {
            throw new InvalidConfigurationException(Messages.getString("aladin.comport_not_found"));
        }
        try {
            status.messageInfo(Messages.getString("aladin.initializing"));
            status.countingProgressbarStart(read_data.length, false);
            commPort = CommUtil.getInstance().open(portIdentifier);
            int commtimeout = Integer.parseInt(this.properties.getProperty(PROPERTY_NAMES[2]));
            DataBits dataBits = DataBits.valueOf(this.properties.getProperty(PROPERTY_NAMES[3]));
            StopBits stopBits = StopBits.valueOf(this.properties.getProperty(PROPERTY_NAMES[4]));
            Parity parity = Parity.valueOf(this.properties.getProperty(PROPERTY_NAMES[5]));
            commPort.setSerialPortParams(commtimeout, dataBits, parity, stopBits);
            LOGGER.info(commtimeout + " " + dataBits + " " + parity + " " + stopBits);
            commPort.enableReceiveTimeout(500);
            commPort.setDTR(true);
            commPort.setRTS(false);
            status.commSend();
            sleep(1000);
            in = commPort.getInputStream();

            // start reading the input stream
            for (i = 0, t = 0, timeout = 0; i < 4 && timeout < TIME_OUT; t++) {
                // read the start byte sequence
                in.read(read_byte);
                if (read_byte[0] != 0) {
                    LOGGER.fine(" " + read_byte[0]);
                }
                if (i < 3) {
                    if (unsigned_int(read_byte[0]) == 85) {
                        i++;
                    } else {
                        i = 0;
                    }
                } else if (unsigned_int(read_byte[0]) == 0) {
                    i++;
                } else {
                    i = 0;
                    throw new InvalidConfigurationException(Messages.getString("aladin.read_error"));
                }
                timeout++;
            }
            // no connection in timeout
            if (timeout == TIME_OUT) {
                throw new TransferException(Messages.getString("aladin.comm_timeout"));
            }

            status.messageInfo(Messages.getString("aladin.reading"));
            // read the data
            for (int j = 0; j < read_data.length; j++) {
                status.countingProgressbarIncrement();
                in.read(read_byte);
                status.commReceive();
                read_data[j] = unsigned_int(read_byte[0]);
            }
            read_data = reorder_bytes(read_data, read_data.length);

            for (int j = 0; j < read_data.length; j++) {
                LOGGER.fine(" Nr. " + j + " :" + read_data[j]);
            }
            in.close();
            commPort.setDTR(false);
            status.commSend();

            checkChecksum(read_data);

            // check the computer model
            if (readComputerModel(read_data) == -1) {
                throw new TransferException(Messages.getString("aladin.nocomputer"));
            }

            // create the dive objects
            jdives = new AladinAdapter(new AladinData(read_data));

            // save Aladin log file to disk
            java.io.File file = new java.io.File
                (System.getProperty ("java.io.tmpdir"), "aladin.log");
            java.io.FileOutputStream f = new java.io.FileOutputStream (file);

            for (int index = 0; index < read_data.length; index++) {
                f.write (read_data [index]);
            }
            f.close ();
        } catch (PortInUseException piuex) {
            LOGGER.log(Level.SEVERE, "transfer failed, comprt in use", piuex);
            throw new TransferException(Messages.getString("aladin.comport_in_use"));
        } catch (UnsupportedCommOperationException comuex) {
            LOGGER.log(Level.SEVERE, "transfer failed, comprt operation unsupported", comuex);
            throw new TransferException(Messages.getString("aladin.ioexception"));
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, "transfer failed, IOException", ioe);
            throw new TransferException(Messages.getString("aladin.ioexception"));
        } catch (PortNotFoundException e) {
            LOGGER.log(Level.SEVERE, "transfer failed, port not found", e);
            throw new InvalidConfigurationException(Messages.getString("aladin.comport_not_found"));
        } finally {
            status.messageClear();
            if (commPort != null) {
                commPort.close();
            }
        }
    }

    /**
     * @return the transferred dives.
     */
    public TreeSet<JDive> getDives() {
        return jdives;
    }

    /**
     * main method
     * 
     * @param args
     */
    public static void main(String[] args) {
        Properties p = new Properties();
        p.setProperty(PROPERTY_NAMES[0], "/dev/ttyUSB0");
        p.setProperty(PROPERTY_NAMES[1], "1");
        AladinInterface ci = new AladinInterface();
        ci.initialize(p);
        try {
            ci.transfer(null, null);
        } catch (TransferException e) {
            LOGGER.log(Level.SEVERE, "transfer failed", e);
        } catch (NotInitializedException e) {
            LOGGER.log(Level.SEVERE, "transfer failed", e);
        } catch (InvalidConfigurationException e) {
            LOGGER.log(Level.SEVERE, "transfer failed", e);
        }
    }

    //
    // private methods
    //

    /**
     * check the checksum of the datas
     * 
     * @param read_data
     *            the data read
     * @throws TransferException
     *             when checksum check failed
     */
    private void checkChecksum(int[] read_data) throws TransferException {
        int checksum = read_data[0x7fd] * 256 + read_data[0x7fc];
        int sum = 0x1fe;
        for (int i = 0; i <= 0x7fb; i++) {
            sum += read_data[i];
        }
        sum = sum % 65536;
        if (sum != checksum) {
            throw new TransferException(Messages.getString("aladin.checksum_error") + " " + sum + " <-> " + checksum + ")");
        }
    }

    /**
     * Reorder the bytes
     * 
     * @param buf
     * @param len
     * @return the reordered bytes
     */
    private int[] reorder_bytes(int[] buf, int len) {
        int j;

        for (int i = 0; i < len; i++) {
            j = (buf[i] & 0x01) << 7;
            j += (buf[i] & 0x02) << 5;
            j += (buf[i] & 0x04) << 3;
            j += (buf[i] & 0x08) << 1;
            j += (buf[i] & 0x10) >> 1;
            j += (buf[i] & 0x20) >> 3;
            j += (buf[i] & 0x40) >> 5;
            j += (buf[i] & 0x80) >> 7;
            buf[i] = j;
        }
        return buf;
    }

    /**
     * 
     * @param in
     * @return the byte as unsigned int
     */
    private int unsigned_int(byte in) {
        int out = 0xff & in;
        return out;
    }

    /**
     * Check the computer model
     * 
     * @param res
     * @return int, the computer model
     */
    private int readComputerModel(int[] res) {
        for (int i = 0; i < COMPUTERMODEL_IDS.length; i++) {
            if (COMPUTERMODEL_IDS[i] == res[1980]) {
                LOGGER.fine("Ausgabe : " + res[1980]);

                return i;
            }
        }
        LOGGER.fine("Ausgabe : " + res[1980]);
        return -1;
    }

    /**
     * Wait some time
     * 
     * @param millis :
     *            time to wait
     */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {

        }
    }

    //
    // inner classes
    //

    private static class AladinConfigurationPanel extends JPanel {

        private static final long serialVersionUID = 3832621798402569012L;

        private Vector<String> availablePorts;

        private JComboBox modelList;

        private JComboBox portList;

        private JComboBox connectionspeed;

        private JComboBox databits;

        private JComboBox stopbits;

        private JComboBox parity;

        /**
         * Constructor
         * 
         */
        public AladinConfigurationPanel() {
            availablePorts = new Vector<String>();
            Iterator <CommPortIdentifier> it = CommUtil.getInstance().getPortIdentifiers();
            while (it.hasNext()) {
                CommPortIdentifier cpId = (CommPortIdentifier) it.next();
                availablePorts.add(cpId.getName());
            }
            JLabel labelCommport = new JLabel(Messages.getString("aladin.commport"));
            JLabel labelConnectionSpeed = new JLabel(Messages.getString("aladin.connectionspeed"));
            JLabel labelDataBits = new JLabel(Messages.getString("aladin.databits"));
            JLabel labelStopBits = new JLabel(Messages.getString("aladin.stopbits"));
            JLabel labelParity = new JLabel(Messages.getString("aladin.parity"));
            JLabel labelComputermodel = new JLabel(Messages.getString("aladin.computermodel"));
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
            add(labelConnectionSpeed, gc);
            gc.gridx = 1;
            add(getConnectionSpeedList(), gc);
            gc.gridy = 2;
            gc.gridx = 0;
            add(labelDataBits, gc);
            gc.gridx = 1;
            add(getDataBitsList(), gc);
            gc.gridy = 3;
            gc.gridx = 0;
            add(labelStopBits, gc);
            gc.gridx = 1;
            add(getStopBitsList(), gc);
            gc.gridy = 4;
            gc.gridx = 0;
            add(labelParity, gc);
            gc.gridx = 1;
            add(getParityList(), gc);
            gc.gridy = 5;
            gc.gridx = 0;
            add(labelComputermodel, gc);
            gc.gridx = 1;
            add(getModelList(), gc);
        }

        /**
         * Get communication port
         * 
         * @return the port
         */
        public String getCommPort() {
            return String.valueOf(getPortList().getSelectedItem());
        }

        /**
         * Get the communication speed
         * 
         * @return speed
         */
        public Integer getConnectionSpeed() {
            return Integer.valueOf((Integer) getConnectionSpeedList().getSelectedItem());
        }

        public DataBits getDataBits() {
            return (DataBits)getDataBitsList().getSelectedItem();
        }

        public StopBits getStopBits() {
            return (StopBits)getStopBitsList().getSelectedItem();
        }

        public Parity getParity() {
            return (Parity)getParityList().getSelectedItem();
        }

        /**
         * Get the computer model
         * 
         * @return model
         */
        public String getModel() {
            return String.valueOf(getModelList().getSelectedIndex());
        }

        /**
         * Set communication port
         * 
         * @param commPort
         */
        public void setCommPort(String commPort) {
            getPortList().setSelectedItem(commPort);
            revalidate();
        }

        /**
         * Set connection speed
         * 
         * @param connectionspeed
         */
        public void setConnectionSpeed(Integer connectionspeed) {
            getConnectionSpeedList().setSelectedItem(connectionspeed);
            revalidate();
        }

        /**
         * 
         * @param databits
         */
        public void setDataBits(DataBits databits) {
            getDataBitsList().setSelectedItem(databits);
            revalidate();
        }

        /**
         * 
         * @param stopbits
         */
        public void setStopBits(StopBits stopbits) {
            getStopBitsList().setSelectedItem(stopbits);
            revalidate();
        }

        /**
         * 
         * @param parity
         */
        public void setParity(Parity parity) {
            getParityList().setSelectedItem(parity);
            revalidate();
        }

        /**
         * Set the computer model
         * 
         * @param model
         */
        public void setModel(int model) {
            getModelList().setSelectedIndex(model);
            revalidate();
        }

        //
        // private methods
        //

        private JComboBox getModelList() {
            if (modelList == null) {
                modelList = new JComboBox(COMPUTERMODEL_NAMES);
            }
            return modelList;
        }

        private JComboBox getConnectionSpeedList() {
            if (connectionspeed == null) {
                connectionspeed = new JComboBox(CONNECTION_SPEED);
            }
            return connectionspeed;
        }

        private JComboBox getDataBitsList() {
            if (databits == null) {
                databits = new JComboBox(CONNECTION_DATA_BITS);
            }
            return databits;
        }

        private JComboBox getStopBitsList() {
            if (stopbits == null) {
                stopbits = new JComboBox(CONNECTION_STOP_BITS);
            }
            return stopbits;
        }

        private JComboBox getParityList() {
            if (parity == null) {
                parity = new JComboBox(CONNECTION_PARITY);
            }
            return parity;
        }

        private JComboBox getPortList() {
            if (portList == null) {
                portList = new JComboBox(availablePorts);
            }
            return portList;
        }
    }

}
