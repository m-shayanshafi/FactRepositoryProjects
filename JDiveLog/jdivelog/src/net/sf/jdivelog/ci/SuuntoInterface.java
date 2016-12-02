/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: SuuntoInterface.java
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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.jdivelog.comm.CommPortIdentifier;
import net.sf.jdivelog.comm.CommUtil;
import net.sf.jdivelog.comm.PortInUseException;
import net.sf.jdivelog.comm.PortNotFoundException;
import net.sf.jdivelog.comm.SerialPort;
import net.sf.jdivelog.comm.UnsupportedCommOperationException;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.statusbar.StatusInterface;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.JDiveLog;
import net.sf.jdivelog.model.udcf.Alarm;
import net.sf.jdivelog.model.udcf.Dive;
import net.sf.jdivelog.model.udcf.Gas;
import net.sf.jdivelog.util.Hexadecimal;
import net.sf.jdivelog.util.UnitConverter;

/**
 * Description: Class for data transfer with Suunto computers
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class SuuntoInterface implements ComputerInterface {

    public static final String DRIVER_NAME = "Suunto VyTec, Vyper, Mosquito, Stinger";
    public static final String[] COMPUTERMODEL_NAMES = { "Stinger", "Mosquito", "new Vyper", "Vyper / Cobra", "VyTec", "Gekko", "Zoop"};
    public static final byte[] COMPUTERMODEL_IDS = { 0x03, 0x04, 0x0a, 0x0c, 0x0b, 0x0d, 0x16 };
    private static final Logger LOGGER = Logger.getLogger(SuuntoInterface.class.getName());
    private static final String[] PROPERTY_NAMES = { "suunto.commport", "suunto.computermodel", "suunto.download_all" };

    private Properties properties = null;
    private TreeSet<JDive> jdives = null;
    private SerialPort commPort = null;
    private OutputStream out = null;
    private InputStream in = null;
    private boolean localEcho;
    private SunntoConfigurationPanel configurationPanel;

    /**
     * Return the name of the Suunto driver
     * 
     * @return the name of the driver
     */
    public String getDriverName() {
        return DRIVER_NAME;
    }

    /**
     * Get the properties for suunto interface
     * 
     * @return the properties
     */
    public String[] getPropertyNames() {
        return PROPERTY_NAMES;
    }

    /**
     * returns th configuration panel for this driver
     * 
     * @return the configuration panel.
     */
    public SuuntoInterface.SunntoConfigurationPanel getConfigurationPanel() {
        if (configurationPanel == null) {
            configurationPanel = new SunntoConfigurationPanel();
        }
        return configurationPanel;
    }

    /**
     * Save the configuration
     * 
     * @return PROPERTIES
     */
    public Properties saveConfiguration() {
        if (properties == null) {
            properties = new Properties();
        }
        properties.setProperty(PROPERTY_NAMES[0], getConfigurationPanel().getCommPort());
        properties.setProperty(PROPERTY_NAMES[1], getConfigurationPanel().getModel());
        properties.setProperty(PROPERTY_NAMES[2], String.valueOf(getConfigurationPanel().isDownloadAllSelected()));
        return properties;
    }

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
        String dlAllProp = this.properties.getProperty(PROPERTY_NAMES[2]);
        if (dlAllProp != null && !"".equals(dlAllProp)) {
            getConfigurationPanel().setDownloadAll(Boolean.valueOf(dlAllProp));
        } else {
            getConfigurationPanel().setDownloadAll(false);
        }
    }

    public void transfer(StatusInterface status, JDiveLog logbook) throws TransferException, NotInitializedException, InvalidConfigurationException {
        status.messageInfo(Messages.getString("suunto.initializing"));
        String model = (String) properties.get(PROPERTY_NAMES[1]);
        if (model == null) {
            throw new InvalidConfigurationException(Messages.getString("suunto.model_not_set"));
        }
        int computermodel;
        try {
            computermodel = Integer.parseInt(model);
        } catch (NumberFormatException nfe) {
            throw new InvalidConfigurationException(Messages.getString("suunto.model_invalid"));
        }
        boolean downloadAll = true;
        if (properties.get(PROPERTY_NAMES[2]) != null) {
            String dlAllStr = properties.getProperty(PROPERTY_NAMES[2]);
            if (!"".equals(dlAllStr)) {
                downloadAll = Boolean.valueOf(dlAllStr);
            }
        }
        try {
            prepareTransfer(status);

            if (computermodel != readComputerModel(status)) {
                throw new InvalidConfigurationException(Messages.getString("suunto.wrong_computer_set"));
            }

            status.messageInfo(Messages.getString("suunto.reading"));

            jdives = new TreeSet<JDive>();
            byte[] data = readDive(status, true);
            if (data.length > 0) {
                int count = 1; 
                do {
                    JDive dive = createDive(data);
                    if (!downloadAll && logbook != null) {
                        JDive lastDive = logbook.getLastDive();
                        if (lastDive != null && !lastDive.before(dive)) {
                            break;
                        }
                    }
                    if (dive.getDepth() > 0) {
                        jdives.add(dive);
                    } else {
                        LOGGER.info("ignoring dive with depth 0: "+dive.getDate());
                    }
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e1) {
                    }
                    data = readDive(status, false);
                    status.messageInfo(Messages.getString("suunto.reading")+" ... "+count++);
                } while (data.length > 0);
            }
            commPort.setDTR(false);
        } catch (PortInUseException piuex) {
            LOGGER.log(Level.SEVERE, "transfer failed", piuex);
            throw new TransferException(Messages.getString("suunto.comport_in_use"));
        } catch (UnsupportedCommOperationException ucoex) {
            LOGGER.log(Level.SEVERE, "transfer failed", ucoex);
            throw new TransferException(Messages.getString("suunto.could_not_set_comparams"));
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, "transfer failed", ioe);
            throw new TransferException(Messages.getString("suunto.ioexception"));
        } catch (ChecksumException cse) {
            LOGGER.log(Level.SEVERE, "transfer failed", cse);
            throw new TransferException(Messages.getString("suunto.checksum_error"));
        } catch (CommunicationTimeoutException cte) {
            LOGGER.log(Level.SEVERE, "transfer failed", cte);
            throw new TransferException(Messages.getString("suunto.comm_timeout"));
        } catch (PortNotFoundException pnfe) {
            LOGGER.log(Level.SEVERE, "transfer failed", pnfe);
            throw new TransferException(Messages.getString("suunto.comport_not_found"));
        } finally {
            cleanup(status);
        }
    }

    public TreeSet<JDive> getDives() {
        return jdives;
    }
    
    public void prepareTransfer(StatusInterface status) throws NotInitializedException, InvalidConfigurationException, PortNotFoundException, PortInUseException, UnsupportedCommOperationException, IOException {
        if (properties == null) {
            throw new NotInitializedException();
        }
        localEcho = true;
        String port = (String) properties.get(PROPERTY_NAMES[0]);
        if (port == null) {
            throw new InvalidConfigurationException(Messages.getString("suunto.comport_not_set"));
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
            throw new InvalidConfigurationException(Messages.getString("suunto.comport_not_found"));
        }
        status.infiniteProgressbarStart();
        commPort = CommUtil.getInstance().open(portIdentifier);
        commPort.setSerialPortParams(2400, SerialPort.DataBits.DataBits_8, SerialPort.Parity.ODD, SerialPort.StopBits.StopBits_1);
        commPort.enableReceiveTimeout(50);
        commPort.setDTR(true);
        commPort.setRTS(false);
        sleep(1000);
        out = commPort.getOutputStream();
        in = commPort.getInputStream();

        boolean ready = false;
        for (int i = 0; i < 2 && !ready; i++) {
            ready = checkInterface(status);
        }

        if (!ready) {
            localEcho = false;
        }

    }
    
    public void cleanup(StatusInterface status) {
        status.infiniteProgressbarEnd();
        if (commPort != null) {
            commPort.close();
        }
    }

    //
    // private methods
    //    

    private JDive createDive(byte[] profile) {
        Dive dive = new Dive();
        int len = profile.length;
        HashMap<Integer, Gas> gases = new HashMap<Integer, Gas>();
        Gas gas = new Gas();
        UnitConverter pc = new UnitConverter(UnitConverter.SYSTEM_METRIC, UnitConverter.SYSTEM_SI);
        gas.setPstart(String.valueOf(pc.convertPressure(new Double(2 * (profile[len - 6]&0xff)))));
        int oxygen = profile[len - 7];
        initializeGas(gas, oxygen);
        gases.put(oxygen, gas);
        dive.addGas(gas);
        int year = profile[len - 10];
        if (year > 90) {
            year += 1900;
        } else {
            year += 2000;
        }
        GregorianCalendar gc = new GregorianCalendar(year, profile[len - 11] - 1, profile[len - 12], profile[len - 13], profile[len - 14]);
        dive.setDate(gc.getTime());
        dive.addDelta(String.valueOf(profile[len - 4]));
        dive.addSwitch(gas.getName());
        dive.addDepth("0.00");
        UnitConverter c = new UnitConverter(UnitConverter.SYSTEM_IMPERIAL, UnitConverter.SYSTEM_SI);
        int depth = 0;
        for (int i = len - 15; i > 4; i--) {
            int sample = profile[i];
            if (sample == 0x7a) {
                dive.addAlarm(Alarm.ALARM_SLOW);
            } else if (sample == 0x7b) {
                dive.addAlarm(Alarm.ALARM_ATTENTION);
            } else if (sample == 0x7c) {
                dive.addAlarm(Alarm.ALARM_BOOKMARK);
            } else if (sample == 0x7d) {
                dive.addAlarm(Alarm.ALARM_SURFACE);
            } else if (sample == 0x7e) {
                dive.addAlarm(Alarm.ALARM_DECO);
            } else if (sample == 0x7f) {
                dive.addAlarm(Alarm.ALARM_DECO_CEILING_PASSED);
            } else if (sample == -128) {
                dive.addAlarm(Alarm.ALARM_END_OF_DIVE);
            } else if (sample == -127) {
                dive.addAlarm(Alarm.ALARM_SAFETY_STOP_CEILING_PASSED);
            } else if (sample == -121) {
                int o2percent = profile[--i];
                Gas g = gases.get(o2percent);
                if (g == null) {
                    g = new Gas();
                    initializeGas(g, o2percent);
                    gases.put(o2percent, g);
                    dive.addGas(g);
                }
                dive.addSwitch(g.getName());
            } else {
                depth += sample;
                dive.addDepth(String.valueOf(c.convertAltitude(new Double(depth))));
            }
        }
        dive.addDepth("0.00");
        c = new UnitConverter(UnitConverter.SYSTEM_METRIC, UnitConverter.SYSTEM_SI);
        gas.setPend(String.valueOf(pc.convertPressure(new Double(2 * (profile[1]&0xff)))));
        dive.setTemperature(c.convertTemperature(new Double(profile[3])));
        dive.setSurfaceTemperature(c.convertTemperature(new Double(profile[2])));
        return new JDive("si", dive);
    }
    
    private void initializeGas(Gas gas, int oxygen) {
        if (oxygen == 0) {
            gas.setName("Air");
            gas.setHelium(0.0);
            gas.setOxygen(0.21);
            gas.setNitrogen(0.79);
        } else {
            gas.setName("EAN" + oxygen);
            gas.setHelium(0.0);
            gas.setOxygen(new Double(oxygen / 100.0));
            gas.setNitrogen((100.0 - oxygen) / 100.0);
        }
    }

    private int readComputerModel(StatusInterface status) throws IOException, ChecksumException, CommunicationTimeoutException {
        byte[] res = readMemory(status, (byte) 0x0, (byte) 0x24, (byte) 1);
        for (int i = 0; i < COMPUTERMODEL_IDS.length; i++) {
            if (COMPUTERMODEL_IDS[i] == res[0]) {
                return i;
            }
        }
        return -1;
    }

    private boolean checkInterface(StatusInterface status) throws IOException {
        commPort.setRTS(true);
        status.commSend();
        out.write(new byte[] { 0x41, 0x54, 0x0d });
        sleep(200);
        commPort.setRTS(false);
        sleep(500);
        byte[] buf = new byte[3];
        int len = read(status, buf, 600);
        if (len == 3 && buf[0] == 0x41 && buf[1] == 0x54 && buf[2] == 0x0d) {
            return true;
        }
        sleep(500);
        return false;
    }

    public byte[] readMemory(StatusInterface status, byte addrHigh, byte addrLow, byte len) throws IOException, ChecksumException, CommunicationTimeoutException {
        byte[] cmd = new byte[5];
        cmd[0] = 0x05;
        cmd[1] = addrHigh;
        cmd[2] = addrLow;
        cmd[3] = len;
        createCRC(cmd);
        sendCmd(status, cmd);
        byte[] ret = new byte[len];
        int retlen = readPacket(status, ret, 300);
        if (retlen == 0) {
            throw new CommunicationTimeoutException();
        }
        byte[] answer = new byte[retlen];
        for (int i = 0; i < retlen; i++) {
            answer[i] = ret[i];
        }
        return answer;
    }

    public void writeMemory(StatusInterface status, byte addrHigh, byte addrLow, byte[] data) throws IOException, ChecksumException, CommunicationTimeoutException {
        prepareWrite(status);
        byte len = (byte)data.length;
        byte[] cmd = new byte[data.length + 5];
        cmd[0] = 0x06;
        cmd[1] = addrHigh;
        cmd[2] = addrLow;
        cmd[3] = len;
        for (int i=0; i<len; i++) {
            cmd[i+4] = data[i];
        }
        createCRC(cmd);
        sendCmd(status, cmd);
        byte[] ret = new byte[len];
        readPacket(status, ret, 300);
    }
    
    private void prepareWrite(StatusInterface status) throws CommunicationTimeoutException, IOException, ChecksumException {
        byte[] cmd = new byte[3];
        cmd[0] = 0x07;
        cmd[1] = (byte)0xa5;
        createCRC(cmd);
        sendCmd(status, cmd);
        byte[] ret = new byte[3];
        readPacket(status, ret, 300);
    }

    private byte[] readDive(StatusInterface status, boolean firstDive) throws IOException, ChecksumException, CommunicationTimeoutException {
        byte[] cmd = new byte[3];
        if (firstDive) {
            cmd[0] = 0x08;
        } else {
            cmd[0] = 0x09;
        }
        cmd[1] = (byte) 0xa5;
        createCRC(cmd);
        sendCmd(status, cmd);
        int len = 0;
        ArrayList<byte[]> packets = new ArrayList<byte[]>(10);
        byte[] buf = new byte[32];
        int read = readPacket(status, buf, 400);
        packets.add(readFromByteArray(buf, read));
        len += read;
        if (read == 0) {
            return new byte[0];
        }
        while (buf[read - 1] != 0x82) {
            read = readPacket(status, buf, 200);
            if (read == 0) {
                break;
            }
            packets.add(readFromByteArray(buf, read));
            len += read;
            for (int i = 0; i < buf.length; i++) {
                buf[i] = (byte) 0xff;
            }
            if (read < 32) {
                break;
            }
        }
        byte[] res = new byte[len];
        int count = 0;
        Iterator<byte[]> it = packets.iterator();
        while (it.hasNext()) {
            byte[] pkt = it.next();
            for (int i = 0; i < pkt.length; i++) {
                res[count++] = pkt[i];
            }
        }
        return res;
    }

    private byte[] readFromByteArray(byte[] buf, int read) {
        byte[] pkt = new byte[read];
        for (int i = 0; i < read; i++) {
            pkt[i] = buf[i];
        }
        return pkt;
    }

    private void sendCmd(StatusInterface status, byte[] cmd) throws IOException, CommunicationTimeoutException {
        commPort.setRTS(true);
        sleep(300);
        status.commSend();
        out.write(cmd);
        out.flush();
        sleep(300);
        commPort.setRTS(false);
        sleep(500);
        if (localEcho) {
            int len = read(status, cmd, 800);
            if (len == 0) {
                throw new CommunicationTimeoutException();
            }
            sleep(300);
        }
    }

    private int readPacket(StatusInterface status, byte[] ret, int timeout) throws IOException, ChecksumException {
        int len = 0;
        byte[] cmd = new byte[1];
        byte[] addr;
        byte[] msg;
        byte[] crc;
        byte[] packet;
        if (read(status, cmd, timeout) == 0)
            return 0;
        switch (cmd[0]) {
        case 0x05:
            // read memory
            addr = new byte[3];
            read(status, addr, 0);
            len = addr[2];
            msg = new byte[len];
            read(status, msg, 0);
            crc = new byte[1];
            read(status, crc, 0);
            packet = new byte[len + 5];
            packet[0] = cmd[0];
            packet[1] = addr[0];
            packet[2] = addr[1];
            packet[3] = addr[2];
            for (int i = 0; i < len; i++) {
                packet[4 + i] = msg[i];
                ret[i] = msg[i];
            }
            packet[4 + len] = crc[0];
            if (!checkCRC(packet)) {
                throw new ChecksumException();
            }
            return len;
        case 0x06:
            // write memory
            addr = new byte[4];
            read(status, addr, 0);
            len = addr[2];
            packet = new byte[5];
            packet[0] = cmd[0];
            packet[1] = addr[0];
            packet[2] = addr[1];
            packet[3] = addr[2];
            packet[4] = addr[3];
            if (!checkCRC(packet)) {
                throw new ChecksumException();
            }
            return len;
        case 0x07:
            // prepare write memory
            addr = new byte[2];
            read(status, addr, 0);
            packet = new byte[3];
            packet[0] = cmd[0];
            packet[1] = addr[0];
            packet[2] = addr[1];
            if (!checkCRC(packet)) {
                throw new ChecksumException();
            }
            return len;
        case 0x08:
            // get first dive profile
            addr = new byte[1];
            read(status, addr, 0);
            len = addr[0];
            msg = new byte[len];
            read(status, msg, 0);
            crc = new byte[1];
            read(status, crc, 0);
            packet = new byte[len + 5];
            packet[0] = cmd[0];
            packet[1] = addr[0];
            for (int i = 0; i < len; i++) {
                packet[2 + i] = msg[i];
                ret[i] = msg[i];
            }
            packet[2 + len] = crc[0];
            if (!checkCRC(packet)) {
                throw new ChecksumException();
            }
            return len;
        case 0x09:
            addr = new byte[1];
            read(status, addr, 0);
            len = addr[0];
            msg = new byte[len];
            read(status, msg, 0);
            crc = new byte[1];
            read(status, crc, 0);
            packet = new byte[len + 5];
            packet[0] = cmd[0];
            packet[1] = addr[0];
            for (int i = 0; i < len; i++) {
                packet[2 + i] = msg[i];
                ret[i] = msg[i];
            }
            packet[2 + len] = crc[0];
            if (!checkCRC(packet)) {
                throw new ChecksumException();
            }
            return len;
        }
        return 0; // ??? could not read. now what ???
    }

    private int read(StatusInterface status, byte[] ret, int timeout) throws IOException {
        int timeelapsed = 0;
        byte[] buf = new byte[ret.length];
        int count = 0;
        while (count < ret.length) {
            int read = in.read(buf, 0, ret.length - count);
            for (int i = 0; i < read; i++) {
                ret[count + i] = buf[i];
            }
            if (read > 0) {
                status.commReceive();
                count += read;
            }
            if (timeout > 0 && timeelapsed > timeout) {
                return count;
            }
            if (count < ret.length) {
                sleep(50);
                timeelapsed += 50;
            }
        }
        return ret.length;
    }

    private void createCRC(byte[] packet) {
        byte crc = 0x00;
        for (int i = 0; i < packet.length - 1; i++) {
            crc ^= packet[i];
        }
        packet[packet.length - 1] = crc;
    }

    private boolean checkCRC(byte[] packet) {
        byte crc = 0x00;
        for (int i = 0; i < packet.length - 1; i++) {
            crc ^= packet[i];
        }
        return crc == packet[packet.length - 1];
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {

        }
    }

    //
    // inner classes
    //

    private static class SunntoConfigurationPanel extends JPanel {

        private static final long serialVersionUID = 3832621798402569012L;

        private Vector<String> availablePorts;

        private JComboBox modelList;

        private JComboBox portList;

        private JCheckBox downloadAllCheckbox;

        public SunntoConfigurationPanel() {
            availablePorts = new Vector<String>();
            Iterator<CommPortIdentifier> it = CommUtil.getInstance().getPortIdentifiers();
            while (it.hasNext()) {
                CommPortIdentifier cpId = it.next();
                availablePorts.add(cpId.getName());
            }
            JLabel labelCommport = new JLabel(Messages.getString("suunto.commport"));
            JLabel labelComputermodel = new JLabel(Messages.getString("suunto.computermodel"));
            JLabel labelDownloadAll = new JLabel(Messages.getString("suunto.download_all"));
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
            add(getModelList(), gc);
            gc.gridy++;
            gc.gridx = 0;
            add(labelDownloadAll, gc);
            gc.gridx = 1;
            add(getDownloadAllCheckbox(), gc);
        }

        public String getCommPort() {
            return String.valueOf(getPortList().getSelectedItem());
        }

        public String getModel() {
            return String.valueOf(getModelList().getSelectedIndex());
        }

        public boolean isDownloadAllSelected() {
            return getDownloadAllCheckbox().isSelected();
        }
        
        public void setCommPort(String commPort) {
            getPortList().setSelectedItem(commPort);
            revalidate();
        }

        public void setModel(int model) {
            getModelList().setSelectedIndex(model);
            revalidate();
        }
        
        public void setDownloadAll(boolean downloadAll) {
            getDownloadAllCheckbox().setSelected(downloadAll);
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

        private JComboBox getPortList() {
            if (portList == null) {
                portList = new JComboBox(availablePorts);
            }
            return portList;
        }
        
        private JCheckBox getDownloadAllCheckbox() {
            if (downloadAllCheckbox == null) {
                downloadAllCheckbox = new JCheckBox();
            }
            return downloadAllCheckbox;
        }
    }
    
    private void test() throws PortNotFoundException, PortInUseException, UnsupportedCommOperationException, IOException, ChecksumException, CommunicationTimeoutException {
        StatusInterface status = new StatusInterface() {

            public void commSend() {
                // TODO Auto-generated method stub
                
            }

            public void commReceive() {
                // TODO Auto-generated method stub
                
            }

            public void messageError(String message) {
                // TODO Auto-generated method stub
                
            }

            public void messageWarn(String message) {
                // TODO Auto-generated method stub
                
            }

            public void messageInfo(String message) {
                // TODO Auto-generated method stub
                
            }

            public void messageClear() {
                // TODO Auto-generated method stub
                
            }

            public void infiniteProgressbarStart() {
                // TODO Auto-generated method stub
                
            }

            public void infiniteProgressbarEnd() {
                // TODO Auto-generated method stub
                
            }

            public void countingProgressbarStart(int maxCount, boolean showInPercent) {
                // TODO Auto-generated method stub
                
            }

            public void countingProgressbarIncrement() {
                // TODO Auto-generated method stub
                
            }

            public void countingProgressbarEnd() {
                // TODO Auto-generated method stub
                
            }
            
        };
        Iterator<CommPortIdentifier> it = CommUtil.getInstance().getPortIdentifiers();
        if (it.hasNext()) {
            CommPortIdentifier cpId = it.next();
            commPort = CommUtil.getInstance().open(cpId);
            commPort.setSerialPortParams(2400, SerialPort.DataBits.DataBits_8, SerialPort.Parity.ODD, SerialPort.StopBits.StopBits_1);
            commPort.enableReceiveTimeout(5);
            commPort.setDTR(true);
            commPort.setRTS(false);
            sleep(1000);
            out = commPort.getOutputStream();
            in = commPort.getInputStream();

            boolean ready = false;
            for (int i = 0; i < 5 && !ready; i++) {
                ready = checkInterface(status);
            }

            localEcho = true;
            if (!ready) {
                localEcho = false;
            }
            
            try {

                byte[] bs = readMemory(status, (byte) 0x0, (byte) 0x63, (byte) 1);
                LOGGER.info("old value = "+Hexadecimal.valueOf(bs));
                LOGGER.info("writing to computer...");
                byte[] data = new byte[] { 0x2 };
                writeMemory(status, (byte)0x0, (byte)0x63, data);
                bs = readMemory(status, (byte) 0x0, (byte) 0x63, (byte) 1);
                LOGGER.info("new value = "+Hexadecimal.valueOf(bs));

            } finally {
                commPort.close();
            }
        }

    }
    
    public static void main(String[] args) {
        SuuntoInterface si = new SuuntoInterface();
        try {
            si.test();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "test failed", e);
        }
    }

}
