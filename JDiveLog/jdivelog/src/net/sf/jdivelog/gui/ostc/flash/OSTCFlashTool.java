/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: OSTCFlashTool.java
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
package net.sf.jdivelog.gui.ostc.flash;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jdivelog.ci.InvalidConfigurationException;
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
import net.sf.jdivelog.util.Hexadecimal;

/**
 * Tool fro writing Firmware to OSTC of HeinrichsWeikamp
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class OSTCFlashTool {
    
    private static final Logger LOGGER = Logger.getLogger(OSTCFlashTool.class.getName());
    private static final int MEMSIZE = 0x17f40;
    private static final int BLOCKSIZE = 0x40;
    private static final int BLOCKCOUNT = MEMSIZE/BLOCKSIZE;
    
    private final String port;
    private final File firmwareFile;
    private final StatusInterface status;
    private Block[] blocks;
    private InputStream in;
    private OutputStream out;

    public OSTCFlashTool(String port, File firmwareFile, StatusInterface status) {
        this.port = port;
        this.firmwareFile = firmwareFile;
        this.status = status;
        this.blocks = null;
    }
    
    public void writeFlash() throws InvalidConfigurationException, PortNotFoundException, PortInUseException, InvalidAddressException, NoStartException, IOException, ParseException, UnsupportedCommOperationException, HandshakeFailedException {
        CommPortIdentifier portIdentifier = null;
        Iterator<CommPortIdentifier> it = CommUtil.getInstance().getPortIdentifiers();
        boolean portFound = false;
        while (it.hasNext()) {
            CommPortIdentifier cpId = it.next();
            if (port.equals(cpId.getName()) || port.equals(cpId.getNativeName())) {
                portFound = true;
                portIdentifier = cpId;
            }
        }
        if (!portFound) {
            throw new InvalidConfigurationException(Messages.getString("suunto.comport_not_found"));
        }
        parseFile();
        SerialPort serialPort = CommUtil.getInstance().open(portIdentifier);
        try {
            serialPort.setSerialPortParams(19200, DataBits.DataBits_8, Parity.NONE, StopBits.StopBits_1);
            serialPort.setRTS(true);
            serialPort.setDTR(true);
            in = serialPort.getInputStream();
            out = serialPort.getOutputStream();
            if (doHandshake()) {
                write();
            } else {
                throw new HandshakeFailedException();
            }
        } finally {
            try {
                serialPort.setRTS(false);
                serialPort.setDTR(false);
            } catch (Throwable t) {}
            serialPort.close();
        }
    }
    
    private boolean doHandshake() {
        boolean success = false;
        status.infiniteProgressbarStart();
        status.messageInfo(Messages.getString("ostc_press_reset_on_interface_now"));
        try {
            long starttime = System.currentTimeMillis();
            while (!success && starttime+10000 > System.currentTimeMillis()) {
                byte[] cmd = new byte[] { (byte)0xc1 };
                commWrite(status, cmd);
                byte[] result = new byte[2];
                int len = commRead(status, result, 500);
                if (len == 2) {
                    if (result[0] == 0x57 && result[1] == 0x4b) {
                        success = true;
                    }
                }
            }
        } catch (IOException e) {
        }
        status.infiniteProgressbarEnd();
        return success;
    }
    
    private void parseFile() throws InvalidAddressException, NoStartException, IOException, ParseException {
        blocks = new Block[BLOCKCOUNT];
        int offset = 0;
        BufferedReader reader = new BufferedReader(new FileReader(firmwareFile));
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            Object o = parseLine(line);
            if (o instanceof Data) {
                Data d = (Data) o;
                int addr = d.address + offset;
                if (addr < 0x17f00) {
                    int blocknum = addr / BLOCKSIZE; 
                    if (blocks[blocknum] == null) {
                        blocks[blocknum] = new Block(BLOCKSIZE);
                    }
                    int ptr = addr%BLOCKSIZE;
                    if (BLOCKSIZE - (ptr+d.values.length) >= 0) {
                        // all fits in this block
                        System.arraycopy(d.values, 0, blocks[blocknum].data, ptr, d.values.length);
                    } else {
                        throw new InternalError("splitting of data packets into 2 blocks not yet supported!");
                    }
                } else {
                    LOGGER.warning("ignoring data on address "+Hexadecimal.valueOf(addr));
                }
            } else if (o instanceof Offset) {
                Offset off = (Offset) o;
                offset = off.value;
            }
        }

        // initialize last block (will be referenced by bootloader)
        if (blocks[BLOCKCOUNT-1] != null) {
            // there already wrote something here, better stop procedure now!
            throw new InvalidAddressException();
        }
        if (blocks[0] == null) {
            // there is no first block! I expected to have here a pointer to program start!
            throw new NoStartException();
        }
        blocks[BLOCKCOUNT-1] = new Block(BLOCKSIZE);
        byte[] startptr = new byte[] { 0x00, 0x00, (byte)0xa0, (byte)0xef, (byte)0xbf, (byte)0xf0, (byte)0xff, (byte)0xff };
        int startptrsize = startptr.length;
        System.arraycopy(blocks[0].data, 0, blocks[BLOCKCOUNT-1].data,  BLOCKSIZE-startptrsize, startptrsize);
        System.arraycopy(startptr, 0, blocks[0].data, 0, startptrsize);
    }
    
    private void write() throws IOException {
        boolean okay = true;
        status.countingProgressbarStart(BLOCKCOUNT, true);
        status.messageInfo(Messages.getString("ostc_writing_flash"));
        for (int i=0; okay && i<BLOCKCOUNT; i++) {
            status.countingProgressbarIncrement();
            if (blocks[i] != null) {
                int addr = i*BLOCKSIZE;
                byte u = (byte)((addr>>16)&0xFF);
                byte h = (byte)((addr>>8)&0xFF);
                byte l = (byte)(addr&0xFF);
                byte x = (byte)BLOCKSIZE;
                byte[] line = createLine(u, h, l, x, blocks[i].data);
                okay = writeLine(line);
            }
        }
        status.messageInfo(Messages.getString("ostc_flashing_finished"));
        status.countingProgressbarEnd();
    }
    
    private boolean writeLine(byte[] line) throws IOException {
        commWrite(status, line);
        byte[] result = new byte[1];
        int len = commRead(status, result, 1000);
        return len == 1 && result[0] == 0x4b;
    }
    
    private static byte[] createLine(byte u, byte h ,byte l, byte x, byte[] block) {
        byte[] lineNoCrc = new byte[block.length+4];
        lineNoCrc[0] = u;
        lineNoCrc[1] = h;
        lineNoCrc[2] = l;
        lineNoCrc[3] = x;
        System.arraycopy(block, 0, lineNoCrc, 4, block.length);
        byte crc = createCRC(lineNoCrc);
        byte[] line = new byte[block.length+5];
        System.arraycopy(lineNoCrc, 0, line, 0, lineNoCrc.length);
        line[line.length-1] = crc;
        return line;
    }
    
    private static Object parseLine(String line) throws ParseException {
        if (line.length() < 11) {
            LOGGER.severe("Error parsing line '"+line+"', length < 11");
            throw new ParseException(line, 11);
        }
        if (line.charAt(0) != ':') {
            LOGGER.severe("Error parsing line '"+line+"', first char not ':'");
            throw new ParseException(line, 0);
        }
        String countStr = line.substring(1,3);
        int count = Hexadecimal.parseInt(countStr);
        if (line.length() != 11+2*count) {
            LOGGER.severe("Error parsing line '"+line+"', number of bytes ("+count+") and linelength do not match!");
            throw new ParseException(line, 1);
        }
        
        byte[] record = new byte[(line.length()-3)/2];
        for (int i=0; i<record.length; i++) {
            String str = line.substring(1+i*2, 3+i*2);
            record[i] = Hexadecimal.parseByte(str);
        }
        
        String crcString = line.substring(line.length()-2, line.length());
        byte crc = Hexadecimal.parseByte(crcString);
        if (createCRC(record) != crc) {
            LOGGER.severe("Error parsing line '"+line+"', crc ("+crcString+") is wrong (correct="+Hexadecimal.valueOf(createCRC(record))+")");
            throw new ParseException(line,line.length()-2);
        }
        
        String addressString = line.substring(3,7);
        int address = Hexadecimal.parseInt(addressString);
        
        String recTypeString = line.substring(7,9);
        int recType = Hexadecimal.parseInt(recTypeString);
        
        if (recType == 0) {
            // data record
            byte[] vals = new byte[count];
            for (int i=0; i<count; i++) {
                String dataString = line.substring(9+i*2, 11+i*2);
                vals[i] = Hexadecimal.parseByte(dataString);
            }
            Data d = new Data(address, vals);
            return d;
        } else if (recType == 4) {
            // linear offset
            String offsetString = line.substring(9,13);
            int offset = Hexadecimal.parseInt(offsetString)<<16;
            Offset o = new Offset(offset);
            return o;
        }
        return null;
    }
    
    
    private void commWrite(StatusInterface status, byte[] data) throws IOException {
        status.commSend();
        out.write(data);
    }

    private int commRead(StatusInterface status, byte[] ret, int timeout) throws IOException {
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
                sleep(10);
                timeelapsed += 10;
            }
        }
        return ret.length;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {

        }
    }
    
    private static byte createCRC(byte[] data) {
        byte b = 0;
        for (int i=0; i<data.length; i++) {
            b += data[i];
        }
        b = (byte)(0xff^b);
        b = (byte)(b+1);
        return b;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            usage();
        }
        String port = args[0];
        String filename = args[1];
        File file = new File(filename);
        if (!file.exists() || !file.canRead()) {
            System.err.println("Could not read file "+filename);
            usage();
        }
        OSTCFlashTool ft = new OSTCFlashTool(port, file, new DummyStatusInterface());
        try {
            ft.writeFlash();
        } catch (InvalidAddressException e) {
            LOGGER.log(Level.SEVERE, "could not write flash", e);
            System.exit(-1);
        } catch (NoStartException e) {
            LOGGER.log(Level.SEVERE, "could not write flash", e);
            System.exit(-1);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "could not write flash", e);
            System.exit(-1);
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE, "could not write flash", e);
            System.exit(-1);
        } catch (InvalidConfigurationException e) {
            LOGGER.log(Level.SEVERE, "could not write flash", e);
            System.exit(-1);
        } catch (PortNotFoundException e) {
            LOGGER.log(Level.SEVERE, "could not write flash", e);
            System.exit(-1);
        } catch (PortInUseException e) {
            LOGGER.log(Level.SEVERE, "could not write flash", e);
            System.exit(-1);
        } catch (UnsupportedCommOperationException e) {
            LOGGER.log(Level.SEVERE, "could not write flash", e);
            System.exit(-1);
        } catch (HandshakeFailedException e) {
            LOGGER.log(Level.SEVERE, "could not write flash", e);
            System.exit(-1);
        }
    }
    
    private static void usage() {
        System.err.println("usage: ");
        System.err.println("OSTCFlashTool <port> <hex-hile>");
        System.exit(-2);
    }
    
    private static class Block {
        private byte[] data;
        public Block(int blocksize) {
            data = new byte[blocksize];
            Arrays.fill(data, (byte)0xff);
        }
    }
    
    private static class Offset {
        
        private int value;

        public Offset(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
        
    }
    
    private static class Data {
        private int address;
        private byte[] values;
        public Data(int address, byte[] values) {
            this.address = address;
            this.values = values;
        }
        public int getAddress() {
            return address;
        }
        public byte[] getValues() {
            return values;
        }
    }
    
    private static class DummyStatusInterface implements StatusInterface {

        public void commSend() {
        }

        public void commReceive() {
        }

        public void messageError(String message) {
            LOGGER.severe(message);
        }

        public void messageWarn(String message) {
            LOGGER.warning(message);
        }

        public void messageInfo(String message) {
            LOGGER.info(message);
        }

        public void messageClear() {
        }

        public void infiniteProgressbarStart() {
        }

        public void infiniteProgressbarEnd() {
        }

        public void countingProgressbarStart(int maxCount, boolean showInPercent) {
        }

        public void countingProgressbarIncrement() {
        }

        public void countingProgressbarEnd() {
        }
        
    }

}
