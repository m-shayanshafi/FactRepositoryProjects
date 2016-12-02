/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: OSTCProtocol070511.java
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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Logger;

import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.udcf.Alarm;
import net.sf.jdivelog.model.udcf.Dive;
import net.sf.jdivelog.model.udcf.Gas;
import net.sf.jdivelog.util.Hexadecimal;
import net.sf.jdivelog.util.UnitConverter;

/**
 * Protocol for the first version of the OSTC
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class OSTCProtocol080121 extends AbstractOSTCProtocol {
    
    private static final Logger LOGGER = Logger.getLogger(OSTCProtocol080121.class.getName());
    private static final String PROTOCOL_NAME = "OSTC 1.03 - 1.08";
    private static final String[] FIRMWARE_VERSIONS = new String[] {"1.3", "1.4", "1.5", "1.6", "1.7", "1.8"};
    private static final String[] HASHKEYS = new String[] { "3e7fa6ac43caa4649ddc19655d01eb01", "4f89e827a6cc7a1c3a8cc2c4ad412d9d", "fdbe6a77f7f986817bc61e82adf819a3", "c819c078ee9cd914bd29337e10c09711" };
    private static final HashMap<Integer, String> ALARMS;
    
    static {
        ALARMS = new HashMap<Integer, String>();
        ALARMS.put(1, Alarm.ALARM_SLOW);
        ALARMS.put(2, Alarm.ALARM_DECO_CEILING_PASSED);
        ALARMS.put(3, Alarm.ALARM_DEEP_STOP_MISSED);
        ALARMS.put(4, Alarm.ALARM_PPO_LOW);
        ALARMS.put(5, Alarm.ALARM_PPO_HIGH);
        ALARMS.put(6, Alarm.ALARM_BOOKMARK);
    }

    private enum ProfileVersion {
        V100, V200;
    }
    
    protected static final Feature SERIALNUMBER = new Feature("suuntoConfigurationPanel.serialNumber", ReadonlyInteger.class);
    protected static final Feature TOTALDIVES = new Feature("suuntoConfigurationPanel.numberOfDives", ReadonlyInteger.class);
    protected static final Feature GAS1DEFAULT = new Feature("ostc_gas1default", OSTCMix.class);
    protected static final Feature GAS1CURRENT = new Feature("ostc_gas1current", OSTCMix.class);
    protected static final Feature GAS2DEFAULT = new Feature("ostc_gas2default", OSTCMix.class);
    protected static final Feature GAS2CURRENT = new Feature("ostc_gas2current", OSTCMix.class);
    protected static final Feature GAS3DEFAULT = new Feature("ostc_gas3default", OSTCMix.class);
    protected static final Feature GAS3CURRENT = new Feature("ostc_gas3current", OSTCMix.class);
    protected static final Feature GAS4DEFAULT = new Feature("ostc_gas4default", OSTCMix.class);
    protected static final Feature GAS4CURRENT = new Feature("ostc_gas4current", OSTCMix.class);
    protected static final Feature GAS5DEFAULT = new Feature("ostc_gas5default", OSTCMix.class);
    protected static final Feature GAS5CURRENT = new Feature("ostc_gas5current", OSTCMix.class);
    protected static final Feature GAS6CURRENT = new Feature("ostc_gas6current", OSTCMix.class);
    protected static final Feature STARTGAS = new Feature("ostc_startgas", RadioButtonValue.class);
    protected static final Feature DECOTYPE = new Feature("ostc_decotype", RadioButtonValue.class);
    protected static final Feature CF00 = new Feature("ostc_cf00", EightBitCustomFunction.class);
    protected static final Feature CF01 = new Feature("ostc_cf01", EightBitCustomFunction.class);
    protected static final Feature CF02 = new Feature("ostc_cf02", EightBitCustomFunction.class);
    protected static final Feature CF03 = new Feature("ostc_cf03", EightBitCustomFunction.class);
    protected static final Feature CF04 = new Feature("ostc_cf04", EightBitCustomFunction.class);
    protected static final Feature CF05 = new Feature("ostc_cf05", EightBitCustomFunction.class);
    protected static final Feature CF06 = new Feature("ostc_cf06", FifteenBitCustomFunction.class);
    protected static final Feature CF07 = new Feature("ostc_cf07", FifteenBitCustomFunction.class);
    protected static final Feature CF08 = new Feature("ostc_cf08", EightBitCustomFunction.class);
    protected static final Feature CF09 = new Feature("ostc_cf09", EightBitCustomFunction.class);
    protected static final Feature CF10 = new Feature("ostc_cf10", EightBitCustomFunction.class);
    protected static final Feature CF11 = new Feature("ostc_cf11", EightBitCustomFunction.class);
    protected static final Feature CF12 = new Feature("ostc_cf12", EightBitCustomFunction.class);
    protected static final Feature CF13 = new Feature("ostc_cf13", EightBitCustomFunction.class);
    protected static final Feature CF14 = new Feature("ostc_cf14", EightBitCustomFunction.class);
    protected static final Feature CF15 = new Feature("ostc_cf15", EightBitCustomFunction.class);
    protected static final Feature CF16 = new Feature("ostc_cf16", EightBitCustomFunction.class);
    protected static final Feature CF17 = new Feature("ostc_cf17", EightBitCustomFunction.class);
    protected static final Feature CF18 = new Feature("ostc_cf18", EightBitCustomFunction.class);
    protected static final Feature CF19 = new Feature("ostc_cf19", EightBitCustomFunction.class);
    protected static final Feature CF20 = new Feature("ostc_cf20", EightBitCustomFunction.class);
    protected static final Feature CF21 = new Feature("ostc_cf21", EightBitCustomFunction.class);
    protected static final Feature CF22 = new Feature("ostc_cf22", EightBitCustomFunction.class);
    protected static final Feature CF23 = new Feature("ostc_cf23", EightBitCustomFunction.class);
    protected static final Feature CF24 = new Feature("ostc_cf24", EightBitCustomFunction.class);
    protected static final Feature CF25 = new Feature("ostc_cf25", EightBitCustomFunction.class);
    protected static final Feature CF26 = new Feature("ostc_cf26", EightBitCustomFunction.class);
    
    private static final Feature[] ALL_FEATURES = {SERIALNUMBER, TOTALDIVES, GAS1CURRENT, GAS1DEFAULT, GAS2CURRENT, GAS2DEFAULT,
        GAS3CURRENT, GAS3DEFAULT, GAS4CURRENT, GAS4DEFAULT, GAS5CURRENT, GAS5DEFAULT, GAS6CURRENT, STARTGAS, DECOTYPE,
        CF00, CF01, CF02, CF03, CF04, CF05, CF06, CF07, CF08, CF09,
        CF10, CF11, CF12, CF13, CF14, CF15, CF16, CF17, CF18, CF19,
        CF20, CF21, CF22, CF23, CF24, CF25, CF26};

    @Override
    protected Map<Feature, OSTCValue> parseAll(byte[] data) {
        Map<Feature, OSTCValue> result = new HashMap<Feature, OSTCValue>();
        TreeSet<JDive> dives = extractDives(data);
        result.put(FEATURE_PROFILES, new Profiles(dives));
        byte[] settingsData = new byte[256];
        System.arraycopy(data, 6, settingsData, 0, 256);
        Map<Feature, OSTCValue> settings = parseSettings(settingsData);
        result.putAll(settings);
        return result;
    }
    
    @Override
    protected Map<Feature, OSTCValue> parseSettings(byte[] data) {
        Map<Feature, OSTCValue> result = new HashMap<Feature, OSTCValue>();
        int offset = 0;
        int serial = (0xff & data[offset]) + 255 * (0xff&data[offset+1]);
        result.put(SERIALNUMBER, new ReadonlyInteger(serial));
        int numOfDives = (0xff & data[offset+2]) + 255 * (0xff&data[offset+3]);
        result.put(TOTALDIVES, new ReadonlyInteger(numOfDives));
        offset = 4;
        int o2 = data[offset] & 0xff;
        int he = data[offset+1] & 0xff;
        result.put(GAS1DEFAULT, new OSTCMix(o2,he));
        offset += 2;
        o2 = data[offset] & 0xff;
        he = data[offset+1] & 0xff;
        result.put(GAS1CURRENT, new OSTCMix(o2,he));

        offset += 2;
        o2 = data[offset] & 0xff;
        he = data[offset+1] & 0xff;
        result.put(GAS2DEFAULT, new OSTCMix(o2,he));
        offset += 2;
        o2 = data[offset] & 0xff;
        he = data[offset+1] & 0xff;
        result.put(GAS2CURRENT, new OSTCMix(o2,he));

        offset += 2;
        o2 = data[offset] & 0xff;
        he = data[offset+1] & 0xff;
        result.put(GAS3DEFAULT, new OSTCMix(o2,he));
        offset += 2;
        o2 = data[offset] & 0xff;
        he = data[offset+1] & 0xff;
        result.put(GAS3CURRENT, new OSTCMix(o2,he));

        offset += 2;
        o2 = data[offset] & 0xff;
        he = data[offset+1] & 0xff;
        result.put(GAS4DEFAULT, new OSTCMix(o2,he));
        offset += 2;
        o2 = data[offset] & 0xff;
        he = data[offset+1] & 0xff;
        result.put(GAS4CURRENT, new OSTCMix(o2,he));

        offset += 2;
        o2 = data[offset] & 0xff;
        he = data[offset+1] & 0xff;
        result.put(GAS5DEFAULT, new OSTCMix(o2,he));
        offset += 2;
        o2 = data[offset] & 0xff;
        he = data[offset+1] & 0xff;
        result.put(GAS5CURRENT, new OSTCMix(o2,he));
        
        offset += 2;
        o2 = data[offset] & 0xff;
        he = data[offset+1] & 0xff;
        result.put(GAS6CURRENT, new OSTCMix(o2,he));
        
        offset = 33;
        int startgas = data[offset] & 0xff;
        result.put(STARTGAS, new RadioButtonValue(startgas, 1, new int[] { 1, 2, 3, 4, 5 }, new String[] { "ostc_gas1", "ostc_gas2", "ostc_gas3", "ostc_gas4", "ostc_gas5"}));
        
        offset += 1;
        int decotype = data[offset] & 0xff;
        result.put(DECOTYPE, new RadioButtonValue(decotype, 0, new int[] { 0, 1 }, new String[] { "ostc_deco_zhl16", "ostc_deco_gauge" }));
        
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
        return result;
    }
    
    @Override
    protected void store(Map<Feature, OSTCValue> data, byte[] buffer) {
        if (buffer.length != 252) {
            // TODO PP: throw exception
        }
        OSTCMix m = (OSTCMix) data.get(GAS1DEFAULT);
        if (m != null) {
            int offset = 0;
            buffer[offset] = (byte)m.getOxygenPercent();
            buffer[offset+1] = (byte)m.getHeliumPercent();
        }
        m = (OSTCMix)data.get(GAS1CURRENT);
        if (m != null) {
            int offset = 2;
            buffer[offset] = (byte)m.getOxygenPercent();
            buffer[offset+1] = (byte)m.getHeliumPercent();
        }
        
        m = (OSTCMix) data.get(GAS2DEFAULT);
        if (m != null) {
            int offset = 4;
            buffer[offset] = (byte)m.getOxygenPercent();
            buffer[offset+1] = (byte)m.getHeliumPercent();
        }
        m = (OSTCMix)data.get(GAS2CURRENT);
        if (m != null) {
            int offset = 6;
            buffer[offset] = (byte)m.getOxygenPercent();
            buffer[offset+1] = (byte)m.getHeliumPercent();
        }
        
        m = (OSTCMix) data.get(GAS3DEFAULT);
        if (m != null) {
            int offset = 8;
            buffer[offset] = (byte)m.getOxygenPercent();
            buffer[offset+1] = (byte)m.getHeliumPercent();
        }
        m = (OSTCMix)data.get(GAS3CURRENT);
        if (m != null) {
            int offset = 10;
            buffer[offset] = (byte)m.getOxygenPercent();
            buffer[offset+1] = (byte)m.getHeliumPercent();
        }
        
        m = (OSTCMix) data.get(GAS4DEFAULT);
        if (m != null) {
            int offset = 12;
            buffer[offset] = (byte)m.getOxygenPercent();
            buffer[offset+1] = (byte)m.getHeliumPercent();
        }
        m = (OSTCMix)data.get(GAS4CURRENT);
        if (m != null) {
            int offset = 14;
            buffer[offset] = (byte)m.getOxygenPercent();
            buffer[offset+1] = (byte)m.getHeliumPercent();
        }
        
        m = (OSTCMix) data.get(GAS5DEFAULT);
        if (m != null) {
            int offset = 16;
            buffer[offset] = (byte)m.getOxygenPercent();
            buffer[offset+1] = (byte)m.getHeliumPercent();
        }
        m = (OSTCMix)data.get(GAS5CURRENT);
        if (m != null) {
            int offset = 18;
            buffer[offset] = (byte)m.getOxygenPercent();
            buffer[offset+1] = (byte)m.getHeliumPercent();
        }
        m = (OSTCMix)data.get(GAS6CURRENT);
        if (m != null) {
            int offset = 20;
            buffer[offset] = (byte)m.getOxygenPercent();
            buffer[offset+1] = (byte)m.getHeliumPercent();
        }
        RadioButtonValue rbv = (RadioButtonValue) data.get(STARTGAS);
        if (rbv != null) {
            int offset = 29;
            buffer[offset] = (byte)rbv.getSelectedValue();
        }
        rbv = (RadioButtonValue) data.get(DECOTYPE);
        if (rbv != null) {
            int offset = 30;
            buffer[offset] = (byte)rbv.getSelectedValue();
        }

        int offset = 124;
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
    
    @Override
    public boolean hasDecoInformationResetFunction() {
        return true;
    }
    
    private TreeSet<JDive> extractDives(byte[] data) {
        byte[] profiles = ParseUtil.extractAndSortProfiles(data);
        TreeSet<JDive> dives = new TreeSet<JDive>();
        ProfileReader reader = new ProfileReader(profiles);
        byte[] profile = reader.nextProfile();
        while (profile != null) {
            try {
                ProfileVersion version = detectProfileVersion(profile);
                JDive dive = null;
                if (version == ProfileVersion.V100) {
                    dive = OSTCProtocol070511.createDive(profile);
                } else if (version == ProfileVersion.V200) {
                    dive = createDive200(profile);
                } else {
                    // unknown profile version?!
                    // TODO PP: what to do?!
                }
                if (dive != null) {
                    dives.add(dive);
                }
            } catch (Throwable t){
                System.err.println("error reading profile "+Hexadecimal.valueOf(profile));
            }
            profile = reader.nextProfile();
        }
        return dives;
    }
    
    static JDive createDive200(byte[] data) {
        if (!(data[0] == ParseUtil.toByte(0xfa) && data[1] == ParseUtil.toByte(0xfa))) {
            LOGGER.severe("invalid profile header start "+Hexadecimal.valueOf(data[0])+Hexadecimal.valueOf(data[1]));
            return null;
        }
        if (!(data[45] == ParseUtil.toByte(0xfb) && data[46] == ParseUtil.toByte(0xfb))) {
            LOGGER.severe("invalid profile header end "+Hexadecimal.valueOf(data[53])+Hexadecimal.valueOf(data[54]));
            return null;
        }
        if (!(data[data.length-2] == ParseUtil.toByte(0xfd) && data[data.length-1] == ParseUtil.toByte(0xfd))) {
            LOGGER.severe("invalid profile end "+Hexadecimal.valueOf(data[data.length-2])+Hexadecimal.valueOf(data[data.length-1]));
            return null;
        }
        if (!(data[2] == ParseUtil.toByte(0x20))) {
            LOGGER.severe("invalid profile version "+Hexadecimal.valueOf(data[2]));
            return null;
        }
        int month = data[3] & 0xff;
        int day = data[4] & 0xff;
        int year = 2000 + (data[5] & 0xff);
        int endHour = data[6] & 0xff;
        int endMinute = data[7] & 0xff;
        int maxDepthMilliBar = (data[8] & 0xff) + 256*(data[9] & 0xff);
        int diveTimeMinutes = (data[10] & 0xff) + 256*(data[11] & 0xff);
        int diveTimeSeconds = data[12] & 0xff;
        int minWaterTemp = (data[13] & 0xff) + 256*(data[14] & 0xff);
        int surfAirPressure = (data[15] & 0xff) + 256*(data[16] & 0xff);
        int maxAllowedAscend = (data[17] & 0xff) + 256*(data[18] & 0xff);
        
        Dive dive = new Dive();

        HashSet<Gas> usedGases = new HashSet<Gas>();
        Gas[] gases = new Gas[6];
        int offset = 19;
        for (int i=0; i<gases.length; i++) {
            gases[i] = new Gas();
            int o2 = data[offset++] & 0xff;
            int he = data[offset++] & 0xff;
            double oxygen = o2 / 100.0;
            double helium = he / 100.0;
            double nitrogen = 1.0 - oxygen - helium;
            gases[i].setOxygen(oxygen);
            gases[i].setHelium(helium);
            gases[i].setNitrogen(nitrogen);
            gases[i].calculateName();
            String nameCandidate = gases[i].getName();
            boolean existing = false;
            for (int j=0; j<i && !existing; j++) {
                if (nameCandidate.equals(gases[j].getName())) {
                    existing = true;
                }
            }
            if (existing) {
                gases[i].setName(nameCandidate+" ("+i+")");
            }
        }
        
        int currentGas = (data[31] & 0xff) - 1;
        
        offset = 36;
        int samplingRate = data[offset++] & 0xff;
        
        int divisorTemp = data[offset] & 0x0f;
        int lengthTemp = (data[offset++] & 0xf0) >> 4;
        
        int divisorDeco = data[offset] & 0x0f;
        int lengthDeco = (data[offset++] & 0xf0) >> 4;
        
        int divisorPressure = data[offset] & 0x0f;
        int lengthPressure = (data[offset++] & 0xf0) >> 4;
        
        int divisorPpO2 = data[offset] & 0x0f;
        int lengthPpO2 = (data[offset++] & 0xf0) >> 4;
        
        int divisorUnused1 = data[offset] & 0x0f;
        int lengthUnused1 = (data[offset++] & 0xf0) >> 4;
        
        int divisorUnused2 = data[offset] & 0x0f;
        int lengthUnused2 = (data[offset++] & 0xf0) >> 4;
        
        
        GregorianCalendar gc = new GregorianCalendar(year, month-1, day, endHour, endMinute);
        gc.add(Calendar.MINUTE, -diveTimeMinutes);
        gc.add(Calendar.SECOND, -diveTimeSeconds);
        dive.setDate(gc.getTime());
        UnitConverter tempConverter = new UnitConverter(UnitConverter.SYSTEM_METRIC, UnitConverter.SYSTEM_SI);
        dive.setTemperature(tempConverter.convertTemperature(0.1*minWaterTemp));
        dive.setTimeDepthMode();
        offset = 47;
        boolean deco = false;
        dive.addTime("0.0");
        dive.addSwitch(gases[currentGas].getName());
        usedGases.add(gases[currentGas]);
        dive.addTime("0.01");
        dive.addDepth("0");
        for (int sample = 1;  offset < data.length-3; sample++) {
            int idx = offset;

            // fix values (time & depth)
            int time = samplingRate*sample;
            dive.addTime(String.valueOf(time));
            int lo = 0xff&data[idx++];
            int hi = 256*(0xff&data[idx++]);
            int depthMilliBar = lo+hi;
            double depth = depthMilliBar / 100.0;
            dive.addDepth(String.valueOf(depth));

            // dynamic data, check what's available
            int sampleLength = data[idx] & 0x7f;
            boolean eventAvailable = (data[idx++] & 0x80) != 0;
            boolean gassetAvailable = eventAvailable && (data[idx] & 0x10) != 0;
            boolean gaschangeAvailable = eventAvailable && (data[idx] & 0x20) != 0;
            boolean tempAvailable = divisorTemp == 0 ? false : sample%divisorTemp == 0;
            boolean decoAvailable = divisorDeco == 0 ? false : sample%divisorDeco == 0;
            boolean pressureAvailable = divisorPressure == 0 ? false : sample%divisorPressure == 0;
            boolean ppo2Available = divisorPpO2 == 0 ? false : sample%divisorPpO2 == 0;
            boolean unused1Available = divisorUnused1 == 0 ? false : sample%divisorUnused1 == 0;
            boolean unused2Available = divisorUnused2 == 0 ? false : sample%divisorUnused2 == 0;
            
            if (eventAvailable) {
                // set Alarm (if the event isn't only used to signal gas-change or gas-set)
                int alarm = data[idx++] & 0x0f;
                if (alarm > 0) {
                    String str = ALARMS.get(alarm);
                    if (str == null) {
                        str = Alarm.ALARM_UNKNOWN;
                    }
                    dive.addAlarm(str);
                }
            }
            if (gassetAvailable) {
                // add new gas as gas #6
                int o2 = data[idx++];
                int he = data[idx++];
                double oxygen = o2 / 100;
                double helium = he / 100;
                double nitrogen = 1.0 - oxygen - helium;
                gases[5] = new Gas();
                gases[5].setOxygen(oxygen);
                gases[5].setHelium(helium);
                gases[5].setNitrogen(nitrogen);
                gases[5].calculateName();
                dive.addGas(gases[5]);
            }
            if (gaschangeAvailable) {
                currentGas = data[idx++] & 0xff;
                dive.addSwitch(gases[currentGas-1].getName());
                usedGases.add(gases[currentGas-1]);
            }
            if (tempAvailable) {
                if (lengthTemp == 2) {
                    int low = 0xff&data[idx++];
                    int high = 256*(0xff&data[idx++]);
                    int temp = low+high;
                    double t = tempConverter.convertTemperature(temp*0.1);
                    dive.addTemperature(String.valueOf(t));
                } else {
                    LOGGER.severe("Expected length of temperature to be 2, ignoring '"+lengthTemp+"' bytes");
                    idx += lengthTemp;
                }
            }
            if (decoAvailable) {
                if (lengthDeco == 2) {
                    int stopDepth = data[idx++] & 0xff;
                    int stopTime = data[idx++] & 0xff;
                    dive.addDecoInfo(String.valueOf(stopDepth), String.valueOf(stopTime), null);
                    if (stopDepth > 0) {
                        if (!deco) {
                            deco = true;
                            dive.addAlarm(Alarm.ALARM_DECO);
                        }
                    } else {
                        if (deco) {
                            deco = false;
                            // alarm no deco / deco finished?
                        }
                    }
                } else {
                    LOGGER.severe("Expected length of deco information to be 2, ignoring '"+lengthDeco+"' bytes");
                    idx += lengthDeco;
                }
            }
            if (pressureAvailable) {
                LOGGER.severe("Tank Pressure monitoring not yet supported, ignoring '"+lengthPressure+"' bytes");
                idx += lengthPressure;
            }
            if (ppo2Available) {
                if (lengthPpO2 == 3) {
                    double ppO2 = (data[idx++] & 0xff)/100.0;
                    dive.addPPO2("1", String.valueOf(ppO2));
                    ppO2 = (data[idx++] & 0xff)/100.0;
                    dive.addPPO2("2", String.valueOf(ppO2));
                    ppO2 = (data[idx++] & 0xff)/100.0;
                    dive.addPPO2("3", String.valueOf(ppO2));
                } else {
                    LOGGER.severe("Expected length of ppO2 information to be 3, ignoring '"+lengthPpO2+"' bytes");
                    idx += lengthPpO2;
                }
            }
            if (unused1Available) {
                LOGGER.severe("unused1 not yet supported, ignoring '"+lengthUnused1+"' bytes");
                idx += lengthUnused1;
            }
            if (unused2Available) {
                LOGGER.severe("unused1 not yet supported, ignoring '"+lengthUnused2+"' bytes");
                idx += lengthUnused2;
            }
            if (idx != offset + sampleLength + 3) {
                // blocksize doesn't match the expected size!
                LOGGER.severe("blocksize doesn't match the expected size! sample="+sample+", idx="+idx+", offset+sampleLength+3="+(offset+sampleLength+3)+".\nIgnoring dive...");
                return null;
            }
            offset = idx;
            
        }
        dive.addDepth("0");
        
        for (Gas gas : usedGases) {
            dive.addGas(gas);
        }
        
        JDive jdive = new JDive("si", dive);
        jdive.setDuration(String.valueOf(diveTimeMinutes*60+diveTimeSeconds));
        return jdive;
    }

    private ProfileVersion detectProfileVersion(byte[] profile) {
        ProfileVersion result = null;
        int headerEnd = ParseUtil.seekSequence(profile, ParseUtil.SEQ_END_DIVE_HEADER, 0);
        if (headerEnd == 18) {
            // Version 1.0 Profile: no Protocol Version available
            result = ProfileVersion.V100;
        } else {
            // Newer profile, check Protocol Version
            int protocolVersion = profile[2] & 0xff;
            if (protocolVersion == 0x20) {
                result = ProfileVersion.V200;
            }
        }
        return result;
    }
}
