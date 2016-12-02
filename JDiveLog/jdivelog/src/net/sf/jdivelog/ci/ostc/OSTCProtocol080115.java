/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: OSTCProtocol080115.java
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
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Logger;

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.udcf.Alarm;
import net.sf.jdivelog.model.udcf.Dive;
import net.sf.jdivelog.model.udcf.Gas;
import net.sf.jdivelog.util.Hexadecimal;
import net.sf.jdivelog.util.UnitConverter;

/**
 * Same Protocol as 1.0 Versions, but it has a function to reset DecoInformation
 * and a function to read only EEPROM values rather than the whole profile memory.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class OSTCProtocol080115 extends AbstractOSTCProtocol {
    
    private static final Logger LOGGER = Logger.getLogger(OSTCProtocol080115.class.getName());
    private static final String PROTOCOL_NAME = "OSTC 1.01";
    private static final String[] FIRMWARE_VERSIONS = new String[] {"1.1", "1.2"};
    private static final String[] HASHKEYS = new String[] { "2bcb9bbf90c1706f8582ad3dc52356fb", "0bc36972586a4c2e887173ea9f906a02" };
    
    private static final Feature SERIALNUMBER = new Feature("suuntoConfigurationPanel.serialNumber", ReadonlyInteger.class);
    private static final Feature TOTALDIVES = new Feature("suuntoConfigurationPanel.numberOfDives", ReadonlyInteger.class);
    private static final Feature GAS1DEFAULT = new Feature("ostc_gas1default", OSTCMix.class);
    private static final Feature GAS1CURRENT = new Feature("ostc_gas1current", OSTCMix.class);
    private static final Feature GAS2DEFAULT = new Feature("ostc_gas2default", OSTCMix.class);
    private static final Feature GAS2CURRENT = new Feature("ostc_gas2current", OSTCMix.class);
    private static final Feature GAS3DEFAULT = new Feature("ostc_gas3default", OSTCMix.class);
    private static final Feature GAS3CURRENT = new Feature("ostc_gas3current", OSTCMix.class);
    private static final Feature GAS4DEFAULT = new Feature("ostc_gas4default", OSTCMix.class);
    private static final Feature GAS4CURRENT = new Feature("ostc_gas4current", OSTCMix.class);
    private static final Feature GAS5DEFAULT = new Feature("ostc_gas5default", OSTCMix.class);
    private static final Feature GAS5CURRENT = new Feature("ostc_gas5current", OSTCMix.class);
    private static final Feature CF00 = new Feature("ostc_cf00", EightBitCustomFunction.class);
    private static final Feature CF01 = new Feature("ostc_cf01", EightBitCustomFunction.class);
    private static final Feature CF02 = new Feature("ostc_cf02", EightBitCustomFunction.class);
    private static final Feature CF03 = new Feature("ostc_cf03", EightBitCustomFunction.class);
    private static final Feature CF04 = new Feature("ostc_cf04", EightBitCustomFunction.class);
    private static final Feature CF05 = new Feature("ostc_cf05", EightBitCustomFunction.class);
    private static final Feature CF06 = new Feature("ostc_cf06", FifteenBitCustomFunction.class);
    private static final Feature CF07 = new Feature("ostc_cf07", FifteenBitCustomFunction.class);
    private static final Feature CF08 = new Feature("ostc_cf08", EightBitCustomFunction.class);
    private static final Feature CF09 = new Feature("ostc_cf09", EightBitCustomFunction.class);
    private static final Feature CF10 = new Feature("ostc_cf10", EightBitCustomFunction.class);
    private static final Feature CF11 = new Feature("ostc_cf11", EightBitCustomFunction.class);
    private static final Feature CF12 = new Feature("ostc_cf12", EightBitCustomFunction.class);
    private static final Feature CF13 = new Feature("ostc_cf13", EightBitCustomFunction.class);
    private static final Feature CF14 = new Feature("ostc_cf14", EightBitCustomFunction.class);
    private static final Feature CF15 = new Feature("ostc_cf15", EightBitCustomFunction.class);
    private static final Feature CF16 = new Feature("ostc_cf16", EightBitCustomFunction.class);
    private static final Feature CF17 = new Feature("ostc_cf17", EightBitCustomFunction.class);
    private static final Feature CF18 = new Feature("ostc_cf18", EightBitCustomFunction.class);
    private static final Feature CF19 = new Feature("ostc_cf19", EightBitCustomFunction.class);
    
    private static final Feature[] ALL_FEATURES = {SERIALNUMBER, TOTALDIVES, GAS1CURRENT, GAS1DEFAULT, GAS2CURRENT, GAS2DEFAULT, GAS3CURRENT, GAS3DEFAULT, GAS4CURRENT, GAS4DEFAULT, GAS5CURRENT, GAS5DEFAULT, CF00, CF01, CF02, CF03, CF04, CF05, CF06, CF07, CF08, CF09, CF10, CF11, CF12, CF13, CF14, CF15, CF16, CF17, CF18, CF19};
    
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
        return result;
    }

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
    }

    @Override
    public Feature[] getFeatures() {
        return ALL_FEATURES;
    }

    /**
     * @see net.sf.jdivelog.ci.ostc.AbstractOSTCProtocol#getFirmwareVersions()
     */
    @Override
    public String[] getFirmwareVersions() {
        return FIRMWARE_VERSIONS;
    }

    /**
     * @see net.sf.jdivelog.ci.ostc.AbstractOSTCProtocol#getHashkeys()
     */
    @Override
    public String[] getHashkeys() {
        return HASHKEYS;
    }

    /**
     * @see net.sf.jdivelog.ci.ostc.AbstractOSTCProtocol#getName()
     */
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
            JDive dive = createDive(profile);
            if (dive != null) {
                dives.add(dive);
            }
            profile = reader.nextProfile();
        }
        return dives;
    }
    
    static JDive createDive(byte[] data) {
        if (!(data[0] == ParseUtil.toByte(0xfa) && data[1] == ParseUtil.toByte(0xfa))) {
            LOGGER.severe("invalid profile header start "+Hexadecimal.valueOf(data[0])+Hexadecimal.valueOf(data[1]));
            return null;
        }
        if (!(data[18] == ParseUtil.toByte(0xfb) && data[19] == ParseUtil.toByte(0xfb))) {
            LOGGER.severe("invalid profile header end "+Hexadecimal.valueOf(data[18])+Hexadecimal.valueOf(data[19]));
            return null;
        }
        if (!(data[data.length-2] == ParseUtil.toByte(0xfd) && data[data.length-1] == ParseUtil.toByte(0xfd))) {
            LOGGER.severe("invalid profile header end "+Hexadecimal.valueOf(data[data.length-2])+Hexadecimal.valueOf(data[data.length-1]));
            return null;
        }
        String mix = Messages.getString("default_mixname");
        int month = data[2] & 0xff;
        int day = data[3] & 0xff;
        int year = 2000 + (data[4] & 0xff);
        int endHour = data[5] & 0xff;
        int endMinute = data[6] & 0xff;
        int maxDepthMilliBar = data[7] & 0xff + 256*(data[8] & 0xff);
        int diveTimeMinutes = data[9] & 0xff + 256*(data[10] & 0xff);
        int diveTimeSeconds = data[11] & 0xff;
        int minWaterTemp = data[12] & 0xff + 256*(data[13] & 0xff);
        int surfAirPressure = data[14] & 0xff + 256*(data[15] & 0xff);
        int maxAllowedAscend = data[16] & 0xff + 256*(data[17] & 0xff);
        Dive dive = new Dive();
        GregorianCalendar gc = new GregorianCalendar(year, month-1, day, endHour, endMinute);
        gc.add(Calendar.MINUTE, -diveTimeMinutes);
        gc.add(Calendar.SECOND, -diveTimeSeconds);
        dive.setDate(gc.getTime());
        UnitConverter tempConverter = new UnitConverter(UnitConverter.SYSTEM_METRIC, UnitConverter.SYSTEM_SI);
        dive.setTemperature(tempConverter.convertTemperature(0.1*minWaterTemp));
        Gas g = new Gas();
        g.setName(mix);
        dive.addGas(g);
        dive.setTimeDepthMode();
        final int offset = 20;
        int blocksize = 16;
        boolean deco = false;
        dive.addTime("0.0");
        dive.addSwitch(mix);
        dive.addTime("0.01");
        dive.addDepth("0");
        for (int block = 0;  offset + block*blocksize < data.length-2; block++) {
            int idx = offset + block*blocksize;
            if (idx < data.length-3) {
                // depth
                int time = 60*block + 10;
                dive.addTime(String.valueOf(time));
                int lo = 0xff&data[idx];
                int hi = 256*(0xff&data[idx+1]);
                int depthMilliBar = lo+hi;
                double depth = depthMilliBar / 100.0;
                dive.addDepth(String.valueOf(depth));
            } else {
                continue;
            }
            idx += 2;
            if (idx < data.length-3) {
                // depth
                int time = 60*block + 20;
                dive.addTime(String.valueOf(time));
                int lo = 0xff&data[idx];
                int hi = 256*(0xff&data[idx+1]);
                int depthMilliBar = lo+hi;
                double depth = depthMilliBar / 100.0;
                dive.addDepth(String.valueOf(depth));
            } else {
                continue;
            }
            idx += 2;
            if (idx < data.length-3) {
                // depth
                int time = 60*block + 30;
                dive.addTime(String.valueOf(time));
                int lo = 0xff&data[idx];
                int hi = 256*(0xff&data[idx+1]);
                int depthMilliBar = lo+hi;
                double depth = depthMilliBar / 100.0;
                dive.addDepth(String.valueOf(depth));
            } else {
                continue;
            }
            idx += 2;
            if (idx < data.length-3) {
                // depth
                int time = 60*block + 40;
                dive.addTime(String.valueOf(time));
                int lo = 0xff&data[idx];
                int hi = 256*(0xff&data[idx+1]);
                int depthMilliBar = lo+hi;
                double depth = depthMilliBar / 100.0;
                dive.addDepth(String.valueOf(depth));
            } else {
                continue;
            }
            idx += 2;
            if (idx < data.length-3) {
                // depth
                int time = 60*block + 50;
                dive.addTime(String.valueOf(time));
                int lo = 0xff&data[idx];
                int hi = 256*(0xff&data[idx+1]);
                int depthMilliBar = lo+hi;
                double depth = depthMilliBar / 100.0;
                dive.addDepth(String.valueOf(depth));
            } else {
                continue;
            }
            idx += 2;
            if (idx < data.length-7) {
                // depth
                int time = 60*block + 60;
                dive.addTime(String.valueOf(time));
                int lo = 0xff&data[idx];
                int hi = 256*(0xff&data[idx+1]);
                int depthMilliBar = lo+hi;
                double depth = depthMilliBar / 100.0;
                dive.addDepth(String.valueOf(depth));
                int temp = data[idx+2] & 0xff + 256*(data[idx+3] & 0xff);
                double t = tempConverter.convertTemperature(temp*0.1);
                dive.addTemperature(String.valueOf(t));
                int stopDepth = data[idx+4];
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
                continue;
            }
        }
        dive.addDepth("0");
        
        JDive jdive = new JDive("si", dive);
        jdive.setDuration(String.valueOf(diveTimeMinutes*60+diveTimeSeconds));
        return jdive;
    }

}
