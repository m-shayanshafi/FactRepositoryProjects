/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: OSTCProtocol081130.java
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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.jdivelog.ci.NotInitializedException;
import net.sf.jdivelog.ci.TransferException;
import net.sf.jdivelog.comm.PortInUseException;
import net.sf.jdivelog.comm.PortNotFoundException;
import net.sf.jdivelog.comm.UnsupportedCommOperationException;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.statusbar.StatusInterface;
import net.sf.jdivelog.util.Hexadecimal;

/**
 * OSTC Protocol for Firmware > 1.17
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class OSTCProtocol081130 extends OSTCProtocol080321 {
    
    private static final Logger LOGGER = Logger.getLogger(OSTCProtocol081130.class.getName());
    
    private byte[] eeprom2;
    
    
    private static final String PROTOCOL_NAME = "OSTC 1.19 - 1.44";
    private static final String[] FIRMWARE_VERSIONS = new String[] {"1.19", "1.20", "1.21", "1.22", "1.23", "1.24", "1.25", "1.26", "1.27", "1.28", "1.29", "1.30", "1.31", "1.32", "1.33", "1.35", "1.38", "1.39", "1.40", "1.41", "1.42", "1.43", "1.44"};
    private static final String[] HASHKEYS = new String[] { "114f881dd49028b941471c48773c6dd3", "5824997748994b3aa4e4e3108b60245f", "58f1997ac2125eadfbb30703c6107e34", "b17229f06430395cc7a90faf2469be42" };

    protected static final Feature CF31 = new Feature("ostc_cf31", RadioButtonValue.class); // Battery Display
    protected static final Feature CF32 = new Feature("ostc_cf32", EightBitCustomFunction.class); // GF-Low
    protected static final Feature CF33 = new Feature("ostc_cf33", EightBitCustomFunction.class); // GF-High
    protected static final Feature CF34 = new Feature("ostc_cf34", EightBitCustomFunction.class); // HUD low PPO2 Warn Level
    protected static final Feature CF35 = new Feature("ostc_cf35", EightBitCustomFunction.class); // 
    protected static final Feature CF36 = new Feature("ostc_cf36", EightBitCustomFunction.class); // 
    protected static final Feature CF37 = new Feature("ostc_cf37", RadioButtonValue.class); // HUD Style
    protected static final Feature CF38 = new Feature("ostc_cf38", RadioButtonValue.class); // Time Format
    protected static final Feature CF39 = new Feature("ostc_cf39", RadioButtonValue.class); // Flip Display
    protected static final Feature CF40 = new Feature("ostc_cf40", RadioButtonValue.class); // Use big ppO2
    protected static final Feature CF41 = new Feature("ostc_cf41", RadioButtonValue.class); // Show Avg Depth
    protected static final Feature SALINITY = new Feature("ostc_salinity", RadioButtonValue.class); // Salinity

    private static final Feature[] ALL_FEATURES = {SERIALNUMBER, TOTALDIVES, GAS1, GAS1DEFAULT, GAS2, GAS2DEFAULT,
        GAS3, GAS3DEFAULT, GAS4, GAS4DEFAULT, GAS5, GAS5DEFAULT, 
        GAS6CURRENT, STARTGAS, DECOTYPE, SALINITY,
        CF00, CF01, CF02, CF03, CF04, CF05, CF06, CF07, CF08, CF09,
        CF10, CF11, CF12, CF13, CF14, CF15, CF16, CF17, CF18, CF19,
        CF20, CF21, CF22, CF23, CF24, CF25, CF26, CF27, CF28, CF29,
        CF30, CF31, CF32, CF33, CF34, CF35, CF36, CF37, CF38, CF39,
        CF40, CF41, CUSTOM_TEXT};


    @Override
    public void downloadSettings(StatusInterface status) throws PortNotFoundException, PortInUseException, UnsupportedCommOperationException, IOException,
            TransferException {
        super.downloadSettings(status);
        startCommunication(status);
        try {
            write(status, new byte[] {0x6a});
            byte[] buf = new byte[256];
            int count = read(status, buf, 1500);
            if (count != 256) {
                throw new TransferException(Messages.getString("suunto.comm_timeout"));
            }
            Map<Feature, OSTCValue> data2 = parseSettings2(buf);
            data.putAll(data2);
            eeprom2 = new byte[252];
            System.arraycopy(buf, 4, eeprom2, 0, eeprom2.length);
            LOGGER.fine("load eeprom2: "+Hexadecimal.valueOf(eeprom2));
        } finally {
            endCommunication(status);
        }
    }
    
    @Override
    public void upload(StatusInterface status) throws NotInitializedException, PortNotFoundException, PortInUseException, UnsupportedCommOperationException,
            IOException, TransferException {
        super.upload(status);
        store2(data, eeprom2);
        if (eeprom2 == null || eeprom2.length != 252) {
            throw new NotInitializedException();
        }
        LOGGER.fine("save eeprom2: "+Hexadecimal.valueOf(eeprom2));
        sleep(5000);
        startCommunication(status);
        try {
            int count = 0;
            write(status, new byte[] {0x69});
            byte[] buf = new byte[1];
            count = read(status, buf, 70);
            if (count != 1 || buf[0] != 0x69) {
                throw new TransferException(Messages.getString("suunto.comm_timeout"));
            }
            for (int i=0; i<eeprom2.length; i++) {
                buf[0] = eeprom2[i];
                write(status, buf);
                byte[] res = new byte[1];
                long start = System.currentTimeMillis();
                sleep(2);
                int len = read(status, res, 60);
                if (len != 1 || res[0] != buf[0]) {
                    LOGGER.severe("Timeout on byte "+i+" len="+len+" res[0]="+Hexadecimal.valueOf(res[0])+", read-time="+(System.currentTimeMillis() - start));
// TODO PP:                    printout(status, in);
                    throw new TransferException(Messages.getString("suunto.comm_timeout"));
                }
            }
        } finally {
            endCommunication(status);
        }

    }
    
    protected Map<Feature, OSTCValue> parseSettings(byte[] data) {
        Map<Feature, OSTCValue> result = new HashMap<Feature, OSTCValue>();
        int offset = 0;
        int serial = (0xff & data[offset]) + 255 * (0xff&data[offset+1]);
        result.put(SERIALNUMBER, new ReadonlyInteger(serial));
        int numOfDives = (0xff & data[offset+2]) + 255 * (0xff&data[offset+3]);
        result.put(TOTALDIVES, new ReadonlyInteger(numOfDives));
        
        offset = 26;
        int salinity = data[offset] & 0xff;
        result.put(SALINITY, new RadioButtonValue(salinity, 100, new int[] { 100, 101, 102, 103, 104 }, new String[] {"ostc_salinity_100", "ostc_salinity_101", "ostc_salinity_102", "ostc_salinity_103", "ostc_salinity_104"} ));

        int activeGasRegister = data[27] & 0xff;

        offset = 4;
        int o2 = data[offset] & 0xff;
        int he = data[offset+1] & 0xff;
        result.put(GAS1DEFAULT, new OSTCMix(o2,he));
        offset += 2;
        o2 = data[offset] & 0xff;
        he = data[offset+1] & 0xff;
        int changedepth = data[28] & 0xff;
        boolean enabled = (activeGasRegister & 0x01) > 0;
        result.put(GAS1, new ProgrammableMix(o2,he,changedepth,enabled));

        offset += 2;
        o2 = data[offset] & 0xff;
        he = data[offset+1] & 0xff;
        result.put(GAS2DEFAULT, new OSTCMix(o2,he));
        offset += 2;
        o2 = data[offset] & 0xff;
        he = data[offset+1] & 0xff;
        changedepth = data[29] & 0xff;
        enabled = (activeGasRegister & 0x02) > 0;
        result.put(GAS2, new ProgrammableMix(o2,he,changedepth,enabled));

        offset += 2;
        o2 = data[offset] & 0xff;
        he = data[offset+1] & 0xff;
        result.put(GAS3DEFAULT, new OSTCMix(o2,he));
        offset += 2;
        o2 = data[offset] & 0xff;
        he = data[offset+1] & 0xff;
        changedepth = data[30] & 0xff;
        enabled = (activeGasRegister & 0x04) > 0;
        result.put(GAS3, new ProgrammableMix(o2,he,changedepth,enabled));

        offset += 2;
        o2 = data[offset] & 0xff;
        he = data[offset+1] & 0xff;
        result.put(GAS4DEFAULT, new OSTCMix(o2,he));
        offset += 2;
        o2 = data[offset] & 0xff;
        he = data[offset+1] & 0xff;
        changedepth = data[31] & 0xff;
        enabled = (activeGasRegister & 0x08) > 0;
        result.put(GAS4, new ProgrammableMix(o2,he,changedepth,enabled));

        offset += 2;
        o2 = data[offset] & 0xff;
        he = data[offset+1] & 0xff;
        result.put(GAS5DEFAULT, new OSTCMix(o2,he));
        offset += 2;
        o2 = data[offset] & 0xff;
        he = data[offset+1] & 0xff;
        changedepth = data[32] & 0xff;
        enabled = (activeGasRegister & 0x10) > 0;
        result.put(GAS5, new ProgrammableMix(o2,he,changedepth,enabled));
        
        offset += 2;
        o2 = data[offset] & 0xff;
        he = data[offset+1] & 0xff;
        result.put(GAS6CURRENT, new OSTCMix(o2,he));
        
        offset = 33;
        int startgas = data[offset] & 0xff;
        result.put(STARTGAS, new RadioButtonValue(startgas, 1, new int[] { 1, 2, 3, 4, 5 }, new String[] { "ostc_gas1", "ostc_gas2", "ostc_gas3", "ostc_gas4", "ostc_gas5"}));
        
        offset += 1;
        int decotype = data[offset] & 0xff;
        result.put(DECOTYPE, new RadioButtonValue(decotype, 0, new int[] { 0, 1, 2, 3, 4, 5 }, new String[] { "ostc_deco_zhl16", "ostc_deco_gauge", "ostc_deco_const_ppo2", "ostc_deco_apnoe", "ostc_deco_zhl16_gf", "ostc_deco_const_ppo2_gf" }));
        
        offset = 64;
        String str = "";
        if (data[offset] == 1) {
            offset += 1;
            byte[] buf = new byte[25];
            System.arraycopy(data, offset, buf, 0, 25);
            str = new String(buf);
            if (str.indexOf('}') > 0) {
                str = str.substring(0, str.indexOf('}'));
            }
        }
        result.put(CUSTOM_TEXT, new TextValue(25, str));
        
        offset = 128;
        int def = ParseUtil.parseCFValue(data, offset);
        int cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF00, new EightBitCustomFunction(def, cur, 100));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF01, new EightBitCustomFunction(def, cur, 30));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF02, new EightBitCustomFunction(def, cur, 240));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF03, new EightBitCustomFunction(def, cur, 120));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF04, new EightBitCustomFunction(def, cur, 5));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF05, new EightBitCustomFunction(def, cur, 7));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF06, new FifteenBitCustomFunction(def, cur, 1250));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF07, new FifteenBitCustomFunction(def, cur, 1100));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF08, new EightBitCustomFunction(def, cur, 20));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF09, new EightBitCustomFunction(def, cur, 0));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF10, new EightBitCustomFunction(def, cur, 10));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF11, new EightBitCustomFunction(def, cur, 110));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF12, new EightBitCustomFunction(def, cur, 90));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF13, new EightBitCustomFunction(def, cur, 60));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF14, new EightBitCustomFunction(def, cur, 100));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF15, new EightBitCustomFunction(def, cur, 120));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF16, new EightBitCustomFunction(def, cur, 10));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF17, new EightBitCustomFunction(def, cur, 19));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF18, new EightBitCustomFunction(def, cur, 160));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF19, new EightBitCustomFunction(def, cur, 150));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF20, new EightBitCustomFunction(def, cur, 10));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF21, new EightBitCustomFunction(def, cur, 2));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF22, new EightBitCustomFunction(def, cur, 6));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF23, new EightBitCustomFunction(def, cur, 0));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF24, new EightBitCustomFunction(def, cur, 0));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF25, new EightBitCustomFunction(def, cur, 0));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF26, new EightBitCustomFunction(def, cur, 0));

        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF27, new EightBitCustomFunction(def, cur, 30));

        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF28, new FifteenBitCustomFunction(def, cur, 0));

        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF29, new EightBitCustomFunction(def, cur, 3));

        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF30, new EightBitCustomFunction(def, cur, 10));
        
        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF31, new RadioButtonValue(cur, new int[] {0,1, 666}, new String[] {"ostc_cf31_symbol", "ostc_cf31_voltage", "ostc_cf31_symbol"}));
        
        return result;
    }
    
    protected void store(Map<Feature, OSTCValue> data, byte[] buffer) {
        if (buffer.length != 252) {
            // TODO PP: throw exception
        }
        int offset = 0;
        int activeGasRegister = 0;

        OSTCMix m = (OSTCMix)data.get(GAS1DEFAULT);
        if (m != null) {
            buffer[offset] = (byte)m.getOxygenPercent();
            buffer[offset+1] = (byte)m.getHeliumPercent();
        }
        ProgrammableMix pm = (ProgrammableMix) data.get(GAS1);
        if (pm != null) {
            offset += 2;
            buffer[offset] = (byte)pm.getOxygenPercent();
            buffer[offset+1] = (byte)pm.getHeliumPercent();
            buffer[24] = (byte)pm.getChangeDepth();
            if (pm.isEnabled()) {
                activeGasRegister = activeGasRegister | 0x01;
            }
        }
        
        m = (OSTCMix)data.get(GAS2DEFAULT);
        if (m != null) {
            offset += 2;
            buffer[offset] = (byte)m.getOxygenPercent();
            buffer[offset+1] = (byte)m.getHeliumPercent();
        }
        pm = (ProgrammableMix) data.get(GAS2);
        if (m != null) {
            offset += 2;
            buffer[offset] = (byte)pm.getOxygenPercent();
            buffer[offset+1] = (byte)pm.getHeliumPercent();
            buffer[25] = (byte)pm.getChangeDepth();
            if (pm.isEnabled()) {
                activeGasRegister = activeGasRegister | 0x02;
            }
        }
        
        m = (OSTCMix)data.get(GAS3DEFAULT);
        if (m != null) {
            offset += 2;
            buffer[offset] = (byte)m.getOxygenPercent();
            buffer[offset+1] = (byte)m.getHeliumPercent();
        }
        pm = (ProgrammableMix) data.get(GAS3);
        if (m != null) {
            offset += 2;
            buffer[offset] = (byte)pm.getOxygenPercent();
            buffer[offset+1] = (byte)pm.getHeliumPercent();
            buffer[26] = (byte)pm.getChangeDepth();
            if (pm.isEnabled()) {
                activeGasRegister = activeGasRegister | 0x04;
            }
        }
        
        m = (OSTCMix)data.get(GAS4DEFAULT);
        if (m != null) {
            offset += 2;
            buffer[offset] = (byte)m.getOxygenPercent();
            buffer[offset+1] = (byte)m.getHeliumPercent();
        }
        pm = (ProgrammableMix) data.get(GAS4);
        if (pm != null) {
            offset += 2;;
            buffer[offset] = (byte)pm.getOxygenPercent();
            buffer[offset+1] = (byte)pm.getHeliumPercent();
            buffer[27] = (byte)pm.getChangeDepth();
            if (pm.isEnabled()) {
                activeGasRegister = activeGasRegister | 0x08;
            }
        }
        
        m = (OSTCMix)data.get(GAS5DEFAULT);
        if (m != null) {
            offset += 2;
            buffer[offset] = (byte)m.getOxygenPercent();
            buffer[offset+1] = (byte)m.getHeliumPercent();
        }
        pm = (ProgrammableMix) data.get(GAS5);
        if (pm != null) {
            offset += 2;
            buffer[offset] = (byte)pm.getOxygenPercent();
            buffer[offset+1] = (byte)pm.getHeliumPercent();
            buffer[28] = (byte)pm.getChangeDepth();
            if (pm.isEnabled()) {
                activeGasRegister = activeGasRegister | 0x10;
            }
        }
        
        m = (OSTCMix)data.get(GAS6CURRENT);
        if (m != null) {
            offset += 2;
            buffer[offset] = (byte)m.getOxygenPercent();
            buffer[offset+1] = (byte)m.getHeliumPercent();
        }
        
        RadioButtonValue rbv = (RadioButtonValue) data.get(SALINITY);
        if (rbv != null) {
            offset = 22;
            buffer[offset] = (byte)rbv.getSelectedValue();
        }
        
        buffer[23] = (byte)(activeGasRegister&0xff);
        
        rbv = (RadioButtonValue) data.get(STARTGAS);
        if (rbv != null) {
            offset = 29;
            buffer[offset] = (byte)rbv.getSelectedValue();
        }
        rbv = (RadioButtonValue) data.get(DECOTYPE);
        if (rbv != null) {
            offset = 30;
            buffer[offset] = (byte)rbv.getSelectedValue();
        }
        
        TextValue tv = (TextValue) data.get(CUSTOM_TEXT);
        if (tv != null) {
            offset = 60;
            String str = tv.stringValue();
            if (str.trim().length() > 0) {
                buffer[offset++] = 1;
                if (str.length() > 25) {
                    str = str.substring(0, 25);
                }
                if (str.length() < 25) {
                    str += "}";
                }
                if (str.length() < 25) {
                    int missing = 25 - str.length();
                    for (int i=0 ; i<missing; i++) {
                        str += " ";
                    }
                }
                byte[] buf = str.getBytes();
                System.arraycopy(buf, 0, buffer, offset, 25);
            } else {
                buffer[offset++] = 0;
            }
        }

        offset = 124;
        EightBitCustomFunction cf8 = (EightBitCustomFunction)data.get(CF00);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF01);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF02);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF03);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF04);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF05);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        FifteenBitCustomFunction cf15 = (FifteenBitCustomFunction)data.get(CF06);
        if (cf15 != null) {
            buffer[offset] = (byte)(cf15.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf15.defaultValue()/256) & 0x7f | 0x80);
            buffer[offset+2] = (byte)(cf15.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf15.currentValue()/256) & 0x7f);
        }
        
        offset += 4;
        cf15 = (FifteenBitCustomFunction)data.get(CF07);
        if (cf15 != null) {
            buffer[offset] = (byte)(cf15.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf15.defaultValue()/256) & 0x7f | 0x80);
            buffer[offset+2] = (byte)(cf15.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf15.currentValue()/256) & 0x7f);
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF08);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF09);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF10);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF11);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF12);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF13);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF14);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF15);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF16);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF17);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF18);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF19);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF20);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF21);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF22);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF23);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF24);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF25);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF26);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF27);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf15 = (FifteenBitCustomFunction)data.get(CF28);
        if (cf15 != null) {
            buffer[offset] = (byte)(cf15.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf15.defaultValue()/256) & 0x7f | 0x80);
            buffer[offset+2] = (byte)(cf15.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf15.currentValue()/256) & 0x7f);
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF29);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF30);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }
        
        offset += 4;
        rbv = (RadioButtonValue)data.get(CF31);
        if (rbv != null) {
            buffer[offset+2] = (byte)(rbv.getSelectedValue() & 0xff);
            buffer[offset+3] = (byte)((rbv.getSelectedValue()/256) & 0xff); //should be 0!
        }
    }

    protected Map<Feature, OSTCValue> parseSettings2(byte[] data) {
        Map<Feature, OSTCValue> result = new HashMap<Feature, OSTCValue>();
        int offset = 4;
        
        offset = 128;
        int def = ParseUtil.parseCFValue(data, offset);
        int cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF32, new EightBitCustomFunction(def, cur, 10));

        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF33, new EightBitCustomFunction(def, cur, 85));

        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF34, new EightBitCustomFunction(def, cur, 70));

        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF35, new EightBitCustomFunction(def, cur, 140));

        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF36, new EightBitCustomFunction(def, cur, 120));

        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF37, new RadioButtonValue(cur, def, new int[] {0,1,2,3}, new String[] {"ostc_cf37_ostc", "ostc_cf37_ds1", "ostc_cf37_rgb", "ostc_cf37_smithers"}));

        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF38, new RadioButtonValue(cur, def, new int[] {0,1}, new String[] {"ostc_cf38_mm", "ostc_cf38_mmss"}));

        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF39, new RadioButtonValue(cur, def, new int[] {0,1}, new String[] {"ostc_cf39_normal", "ostc_cf39_flip"}));

        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF40, new RadioButtonValue(cur, def, new int[] {0,1}, new String[] {"ostc_cf40_normal", "ostc_cf40_big"}));

        offset += 4;
        def = ParseUtil.parseCFValue(data, offset);
        cur = ParseUtil.parseCFValue(data, offset+2);
        result.put(CF41, new RadioButtonValue(cur, def, new int[] {0,1}, new String[] {"ostc_cf41_temperature", "ostc_cf41_avgdepth"}));
        return result;
    }
    
    protected void store2(Map<Feature, OSTCValue> data, byte[] buffer) {
        if (buffer.length != 252) {
            // TODO PP: throw exception
        }        
        int offset = 0;
        
        offset = 124;
        EightBitCustomFunction cf8 = (EightBitCustomFunction)data.get(CF32);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }

        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF33);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }

        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF34);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }

        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF35);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }

        offset += 4;
        cf8 = (EightBitCustomFunction)data.get(CF36);
        if (cf8 != null) {
            buffer[offset] = (byte)(cf8.defaultValue() & 0xff);
            buffer[offset+1] = (byte)((cf8.defaultValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(cf8.currentValue() & 0xff);
            buffer[offset+3] = (byte)((cf8.currentValue()/256) & 0xff); //should be 0!
        }

        offset += 4;
        RadioButtonValue rbv = (RadioButtonValue)data.get(CF37);
        if (rbv != null) {
            buffer[offset] = (byte)(rbv.getSelectedValue() & 0xff);
            buffer[offset+1] = (byte)((rbv.getSelectedValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(rbv.getSelectedValue() & 0xff);
            buffer[offset+3] = (byte)((rbv.getSelectedValue()/256) & 0xff); //should be 0!
        }

        offset += 4;
        rbv = (RadioButtonValue)data.get(CF38);
        if (rbv != null) {
            buffer[offset] = (byte)(rbv.getSelectedValue() & 0xff);
            buffer[offset+1] = (byte)((rbv.getSelectedValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(rbv.getSelectedValue() & 0xff);
            buffer[offset+3] = (byte)((rbv.getSelectedValue()/256) & 0xff); //should be 0!
        }

        offset += 4;
        rbv = (RadioButtonValue)data.get(CF39);
        if (rbv != null) {
            buffer[offset] = (byte)(rbv.getSelectedValue() & 0xff);
            buffer[offset+1] = (byte)((rbv.getSelectedValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(rbv.getSelectedValue() & 0xff);
            buffer[offset+3] = (byte)((rbv.getSelectedValue()/256) & 0xff); //should be 0!
        }

        offset += 4;
        rbv = (RadioButtonValue)data.get(CF40);
        if (rbv != null) {
            buffer[offset] = (byte)(rbv.getSelectedValue() & 0xff);
            buffer[offset+1] = (byte)((rbv.getSelectedValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(rbv.getSelectedValue() & 0xff);
            buffer[offset+3] = (byte)((rbv.getSelectedValue()/256) & 0xff); //should be 0!
        }

        offset += 4;
        rbv = (RadioButtonValue)data.get(CF41);
        if (rbv != null) {
            buffer[offset] = (byte)(rbv.getSelectedValue() & 0xff);
            buffer[offset+1] = (byte)((rbv.getSelectedValue()/256) & 0xff); //should be 0!
            buffer[offset+2] = (byte)(rbv.getSelectedValue() & 0xff);
            buffer[offset+3] = (byte)((rbv.getSelectedValue()/256) & 0xff); //should be 0!
        }

    }


    @Override
    public String[] getFirmwareVersions() {
        return FIRMWARE_VERSIONS;
    }

    @Override
    public String[] getHashkeys() {
        return HASHKEYS;
    }

    @Override
    public Feature[] getFeatures() {
        return ALL_FEATURES;
    }

    @Override
    public String getName() {
        return PROTOCOL_NAME;
    }
    
}
