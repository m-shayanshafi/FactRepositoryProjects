/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: OSTCProtocol080321.java
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

import java.util.HashMap;
import java.util.Map;

/**
 * OSTC Protocol for Firmware 1.9 and newer.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class OSTCProtocol080321 extends OSTCProtocol080121 {
    
    private static final String PROTOCOL_NAME = "OSTC 1.09 - 1.17";
    private static final String[] FIRMWARE_VERSIONS = new String[] {"1.9", "1.10", "1.11", "1.12", "1.13", "1.14", "1.15", "1.16", "1.17"};
    private static final String[] HASHKEYS = new String[] { "eaecf0b886678e2f6d07314edeb868e3", "6c5fcbd4e4eb1704b1a9860dedbe8c5e", "c2c1e944d7eda6c2dcf8d84c53796b87", "e5ed5b4ecb9b8b71e12ba0b2be2e05e3", "6e9ceb6113d407255b0a1eb3dfc564d6", "8f1f09d4f3d389dd54fdf305f99fd208", "6ede3075814b7b31633fc3f090e4246d" };
    
    protected static final Feature GAS1 = new Feature("ostc_gas1current", ProgrammableMix.class);
    protected static final Feature GAS2 = new Feature("ostc_gas2current", ProgrammableMix.class);
    protected static final Feature GAS3 = new Feature("ostc_gas3current", ProgrammableMix.class);
    protected static final Feature GAS4 = new Feature("ostc_gas4current", ProgrammableMix.class);
    protected static final Feature GAS5 = new Feature("ostc_gas5current", ProgrammableMix.class);
    protected static final Feature CF27 = new Feature("ostc_cf27", EightBitCustomFunction.class);
    protected static final Feature CF28 = new Feature("ostc_cf28", FifteenBitCustomFunction.class);
    protected static final Feature CF29 = new Feature("ostc_cf29", EightBitCustomFunction.class);
    protected static final Feature CF30 = new Feature("ostc_cf30", EightBitCustomFunction.class);
    protected static final Feature CUSTOM_TEXT = new Feature("ostc_customtext", TextValue.class);

    private static final Feature[] ALL_FEATURES = {SERIALNUMBER, TOTALDIVES, GAS1, GAS1DEFAULT, GAS2, GAS2DEFAULT,
        GAS3, GAS3DEFAULT, GAS4, GAS4DEFAULT, GAS5, GAS5DEFAULT, 
        GAS6CURRENT, STARTGAS, DECOTYPE,
        CF00, CF01, CF02, CF03, CF04, CF05, CF06, CF07, CF08, CF09,
        CF10, CF11, CF12, CF13, CF14, CF15, CF16, CF17, CF18, CF19,
        CF20, CF21, CF22, CF23, CF24, CF25, CF26, CF27, CF28, CF29,
        CF30, CUSTOM_TEXT};

    
    @Override
    protected Map<Feature, OSTCValue> parseSettings(byte[] data) {
        Map<Feature, OSTCValue> result = new HashMap<Feature, OSTCValue>();
        int offset = 0;
        int serial = (0xff & data[offset]) + 255 * (0xff&data[offset+1]);
        result.put(SERIALNUMBER, new ReadonlyInteger(serial));
        int numOfDives = (0xff & data[offset+2]) + 255 * (0xff&data[offset+3]);
        result.put(TOTALDIVES, new ReadonlyInteger(numOfDives));
        offset = 4;
        int activeGasRegister = data[27] & 0xff;
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
        result.put(DECOTYPE, new RadioButtonValue(decotype, 0, new int[] { 0, 1, 2, 3 }, new String[] { "ostc_deco_zhl16", "ostc_deco_gauge", "ostc_deco_const_ppo2", "ostc_deco_apnoe" }));
        
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
        
        return result;
    }
    
    @Override
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
        
        buffer[23] = (byte)(activeGasRegister&0xff);
        
        RadioButtonValue rbv = (RadioButtonValue) data.get(STARTGAS);
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
