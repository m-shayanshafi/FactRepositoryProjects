/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DataTrakFileLoader.java
 * 
 * @author Volker Holthaus <v.hotlhaus@procar.de>
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
package net.sf.jdivelog.model.datatrak;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jdivelog.gui.DiveImportDataTrakWindow;
import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.DiveSite;
import net.sf.jdivelog.model.Equipment;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.Masterdata;
import net.sf.jdivelog.model.Tank;
import net.sf.jdivelog.model.udcf.Dive;
import net.sf.jdivelog.model.udcf.Gas;

/**
 * @author Volker Holthaus <v.hotlhaus@procar.de>
 */
public class DataTrakFileLoader {

    private static final Logger LOGGER = Logger.getLogger(DataTrakFileLoader.class.getName());
    private JDive dive = null;
    private Dive divedata = null;
    private Gas  divegas  = null;
    private Tank  divetank  = null;
    private Equipment  diveequipment  = null;
    private ArrayList<JDive> diveToAdd = new ArrayList<JDive>();;
    
    private SimpleDateFormat df = new SimpleDateFormat("dd.MM.yy");
    private MainWindow mainWindow = null;
    
    
    public DataTrakFileLoader(MainWindow mainWindow, File[] files) throws IOException {
        this.mainWindow = mainWindow;
        DataInputStream in = null;
        byte[] line = null;
        Double divesecond = null;
        Double depth = null;
        String help_string = "";
        int count_tg = 0;
        Integer count = new Integer(0);
        GregorianCalendar cal = new GregorianCalendar(Locale.getDefault());
        cal.set(Calendar.DATE, 01);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.YEAR, 1600);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);

            for (int i = 0; i < files.length; i++) {
                try {

                    in = new DataInputStream(new BufferedInputStream(new FileInputStream(files[i])));

                    //jump over the header
                    in.skipBytes(6);
                    
                    //read count of dives
                    line = new byte[2];
                    in.read(line);
                    count_tg = unsigned_int(line[1]) * 0x100 + unsigned_int(line[0]);
                    
                    //jump over the rest of the header
                    in.skipBytes(4);
                    
                    for (int k = 0; k < count_tg; k++) {
                        int profiledataOffset = 24;
                        int profiledataPacketLength = 8;
                        boolean workAlarms = true;

                        dive = new JDive();
                        //Dive Data for the Profil
                        divedata = new Dive();
                        divegas  = new Gas();
                        divetank  = new Tank();
                        diveequipment  = new Equipment();
                        
        
                        //generate the air gas
                        divegas.setName(Messages.getString("default_mixname"));
                        divegas.setOxygen(new Double(0.21));
                        divegas.setNitrogen(new Double(0.79));
                        divegas.setHelium(new Double(0));
                        
                        divedata.setSurfaceinterval("");
                        divedata.setDensity(new Double(0));
                        divedata.setAltitude(new Double(0));

                        //jump over first $A000 from the dive
                        in.skipBytes(2);
                        
                       //read divedate in days since 01.01.1600
                       line = new byte[4];
                       in.read(line);
                       count = unsigned_int(line[3]) * 0x1000000 + unsigned_int(line[2]) * 0x10000 + unsigned_int(line[1]) * 0x100 + unsigned_int(line[0]);
                       cal.add(Calendar.DATE, count);
                       //set the dive date
                       try  {
                           dive.setDate(df.parse(cal.get(Calendar.DATE) + "." + (cal.get(Calendar.MONTH)+1) + "." + cal.get(Calendar.YEAR)));
                           divedata.setDate(df.parse(cal.get(Calendar.DATE) + "." + (cal.get(Calendar.MONTH)+1) + "." + cal.get(Calendar.YEAR)));                                                                                                            
                           cal.add(Calendar.DATE, -1 * count);
                       } catch (Exception ex) {
                           LOGGER.log(Level.WARNING, "error parsing date", ex);
                       }


                       //read divetime in minutes since 00:00
                       line = new byte[2];
                       in.read(line);
                       count = unsigned_int(line[1]) * 0x100 + unsigned_int(line[0]);
                       cal.add(Calendar.MINUTE, count);
                       //set the dive time
                       dive.setTime(new Integer(cal.get(Calendar.HOUR_OF_DAY)).toString(), new Integer(cal.get(Calendar.MINUTE)).toString());
                       cal.add(Calendar.MINUTE, -1 * count);
                       
                       //read country
                       line = new byte[1];
                       in.read(line);
                       count = unsigned_int(line[0]);
                       String country = "";
                       if (count != 0) { 
                           String s = readText(in, count);
                           //set the country
                           country = s;
                       }
                       
                       //read divingplace
                       line = new byte[1];
                       in.read(line);
                       count = unsigned_int(line[0]);
                       String place = "";
                       if (count != 0) {
                           //set the place
                           place = readText(in, count);
                       }
                       
                       dive.setDiveSiteId(getDiveSitePrivateId(mainWindow.getLogBook().getMasterdata(), place, country));
                       
                       //skip the altitude
                       in.skipBytes(1);
                       //skip the surface intervall in minutes
                       in.skipBytes(2);
                       
                       //read weather
                       line = new byte[1];
                       in.read(line);
                       switch (line[0]) {
                           case 1:
                             help_string = Messages.getString("clear");
                           break;
                           case 2:
                            help_string = Messages.getString("misty");
                           break;
                           case 3:
                            help_string = Messages.getString("fog");
                           break;
                           case 4:
                            help_string = Messages.getString("rain");
                           break;
                           case 5:
                            help_string = Messages.getString("storm");
                           break;
                           case 6:
                            help_string = Messages.getString("snow");
                           break;
                       }
                       if (dive.getVisibility() != null) {
                           dive.setVisibility(dive.getVisibility() + " m  " + Messages.getString("weather") + ": " + help_string);
                       } else {
                           dive.setVisibility(Messages.getString("weather") + ": " + help_string);
                       }
                       
                       //skip the air temparature
                       in.skipBytes(2);
                       //read suit
                       line = new byte[1];
                       in.read(line);
                       switch (line[0]) {
                           case 1:
                            help_string = Messages.getString("nosuit");
                           break;
                           case 2:
                            help_string = Messages.getString("shorty");
                           break;
                           case 3:
                            help_string = Messages.getString("combi");
                           break;
                           case 4:
                            help_string = Messages.getString("wet");
                           break;
                           case 5:
                            help_string = Messages.getString("semidry");
                           break;
                           case 6:
                            help_string = Messages.getString("dry");
                           break;
                       }
                       diveequipment.setSuit(help_string);

                       //read tank size
                       line = new byte[2];
                       in.read(line);
                       count = unsigned_int(line[1]) * 0x100 + unsigned_int(line[0]);
                       //set tank size
                       if (count != 32767) {
                           divegas.setTankvolume(new Double(count) / 100000);
                       }

                       //read maximum dive depth
                       line = new byte[2];
                       in.read(line);
                       count = unsigned_int(line[1]) * 0x100 + unsigned_int(line[0]);
                       //set maximum dive depth
                       if (count != 32767) {
                           dive.setDepth(new Double(count) / 100);
                       }
                       
                       //read dive duration
                       line = new byte[2];
                       in.read(line);
                       count = unsigned_int(line[1]) * 0x100 + unsigned_int(line[0]);
                       //set dive duration
                       if (count != 32767) {
                           dive.setDuration(new Double(count));
                       }
                       
                       //read water temperature
                       line = new byte[2];
                       in.read(line);
                       count = unsigned_int(line[1]) * 0x100 + unsigned_int(line[0]);
                       //set the water temperature
                       if (count != 32767) {
                           dive.setTemperature(new Double(count) / 100);
                           divedata.setTemperature(new Double(count) / 100);
                       }
                       
                       //read air consumption
                       line = new byte[2];
                       in.read(line);
                       count = unsigned_int(line[1]) * 0x100 + unsigned_int(line[0]);
                       //set air consumption
                       if (count != 32767) {
                            divegas.setPstart(new Double(Messages.getString("standard_pressure")));
                            divegas.setPend(divegas.getPstart() - (new Double(count)/ 100));
                       }
                       
                       //read divingtype
                       line = new byte[2];
                       in.read(line);
                       //set the divingtype
                       ArrayList<String> types = new ArrayList<String>();
                       if ((line[0] & 4) != 0 ) {
                           types.add(Messages.getString("nostop"));
                       }
                       if ((line[0] & 8) != 0 ) {
                           types.add(Messages.getString("decompression"));
                       }
                       if ((line[0] & 16) != 0 ) {
                           types.add(Messages.getString("single_ascent"));
                       }
                       if ((line[0] & 32) != 0 ) {
                           types.add(Messages.getString("multiple_ascent"));
                       }
                       if ((line[0] & 64) != 0 ) {
                           types.add(Messages.getString("freshwater"));
                       }
                       if ((line[0] & 128) != 0 ) {
                           types.add(Messages.getString("seawater"));
                       }
                       if ((line[1] & 1) != 0 ) {
                           types.add(Messages.getString("nitrox"));
                       }
                       if ((line[1] & 2) != 0 ) {
                           types.add(Messages.getString("rebreather"));
                       }
                       
                       dive.setDiveType(getCommaSeparatedList(types));
                       
                       //read divingactivity
                       line = new byte[2];
                       in.read(line);
                       help_string = "";
                       //set the divingactivity
                       ArrayList<String> activities = new ArrayList<String>();
                       if ((line[0] & 1) != 0 ) {
                           activities.add(Messages.getString("sightseeing"));
                       }
                       if ((line[0] & 2) != 0 ) {
                           activities.add(Messages.getString("clubdive"));
                       }
                       if ((line[0] & 4) != 0 ) {
                           activities.add(Messages.getString("education"));
                       }
                       if ((line[0] & 8) != 0 ) {
                           activities.add(Messages.getString("instruction"));
                       }
                       if ((line[0] & 16) != 0 ) {
                           activities.add(Messages.getString("night"));
                       }
                       if ((line[0] & 32) != 0 ) {
                           activities.add(Messages.getString("cave"));
                       }
                       if ((line[0] & 64) != 0 ) {
                           activities.add(Messages.getString("ice"));
                       }
                       if ((line[0] & 128) != 0 ) {
                           activities.add(Messages.getString("search"));
                       }
                       if ((line[1] & 1) != 0 ) {
                           activities.add(Messages.getString("wreck"));
                       }
                       if ((line[1] & 2) != 0 ) {
                           activities.add(Messages.getString("river"));
                       }
                       if ((line[1] & 4) != 0 ) {
                           activities.add(Messages.getString("drift"));
                       }
                       if ((line[1] & 8) != 0 ) {
                           activities.add(Messages.getString("photo"));
                       }
                       if ((line[1] & 16) != 0 ) {
                           activities.add(Messages.getString("other"));
                       }
                       dive.setDiveActivity(getCommaSeparatedList(activities));

                       //read diving activity
                       line = new byte[1];
                       in.read(line);
                       count = unsigned_int(line[0]);
                       if (count != 0) {
                           //set the diving activity
                           dive.setDiveActivity(dive.getDiveActivity() + "," + readText(in, count));
                       }
                       
                       //read diving Buddy
                       line = new byte[1];
                       in.read(line);
                       count = unsigned_int(line[0]);
                       if (count != 0) {
                           //set the diving Buddy
                           dive.setBuddy(readText(in, count));
                       }
                       
                       //read diving comment
                       line = new byte[1];
                       in.read(line);
                       count = unsigned_int(line[0]);
                       if (count != 0) {                       
                           //set the diving comment
                           dive.setComment(readText(in, count));
                       }

                       //skip the alerts and other
                       in.skipBytes(8);
                       
                       line = new byte[1];
                       in.read(line);
                       int model = unsigned_int(line[0]);
                       
                       if (model == 0x1e || model == 0x1f || model == 0x3f) {
                           // Offset and Packet length for Aladin Pro & Aladin Sport
                           profiledataOffset = 21; // 21 is not bad
                           profiledataPacketLength = 7;
                           workAlarms = false;
                       }
                       
                       if (model == 0xff) {
                           // Offset and Packet length for Aladin Pro Nitrox
                           profiledataOffset = 23;
                           profiledataPacketLength = 7;
                           workAlarms = false;
                       }
                       
                       if (model == 0x44) {
                           // Offset and Packet length for Aladin Air X
                           profiledataOffset = 21;
                           profiledataPacketLength = 7;
                           workAlarms = false;
                       }
                       
                       if (model == 0xf4) {
                           // Offset and Packet length for Aladin Ait Z
                           profiledataOffset = 23;
                           profiledataPacketLength = 7;
                           workAlarms = false;
                       }
                       
                       //skip saturation and air usage
                       in.skipBytes(7);

                       //read diving profile
                       line = new byte[2];
                       in.read(line);
                       count = unsigned_int(line[1]) * 0x100 + unsigned_int(line[0]);
                       if (count != 0) {
                           line = new byte[count];
                           in.read(line);
                           divesecond = new Double(0);
                           divedata.addTime(divesecond.toString());
                           divedata.addDepth("0");
                           divedata.addGas(divegas);            
                           for (int j = profiledataOffset ; j < count - profiledataPacketLength; j = j + profiledataPacketLength) {
                               depth = new Double((unsigned_int(line[j+1]) * 0x100 + unsigned_int(line[j])) & 0xffc0) / 410 ;
                               divedata.addTime(new Double(divesecond / 60).toString());
                               divedata.addDepth(depth.toString());
                               setAlarms(line[j+1], line[j], workAlarms);
                               divesecond = divesecond + 20;

                               depth = new Double((unsigned_int(line[j+3]) * 0x100 + unsigned_int(line[j+2])) & 0xffc0) / 410 ;
                               divedata.addTime(new Double(divesecond / 60).toString());
                               divedata.addDepth(depth.toString());
                               setAlarms(line[j+3], line[j+2], workAlarms);
                               divesecond = divesecond + 20;

                               depth = new Double((unsigned_int(line[j+5]) * 0x100 + unsigned_int(line[j+4])) & 0xffc0) / 410 ;
                               divedata.addTime(new Double(divesecond / 60).toString());
                               divedata.addDepth(depth.toString());
                               setAlarms(line[j+5], line[j+4], workAlarms);
                               divesecond = divesecond + 20;                                                     
                           }
                           divedata.addTime(new Double(divesecond / 60).toString());
                           divedata.addDepth("0");                    
                       }
                                                                                                
                       //Generate the diveprofil
                        if (divedata.getSamples() != null) {
                            divedata.setTimeDepthMode();
                            dive.setDive(divedata);
                            dive.setAverageDepth(divedata.getAverageDepth());
                        }
                        dive.setUnits("metric");
                        
                        //set the Equipment
                        divetank.setGas(divegas);
                        diveequipment.addTank(divetank);
                        dive.setEquipment(diveequipment);
                        
                        //add the dive
                        diveToAdd.add(dive);
                }
            } catch (EOFException ex) {
                LOGGER.log(Level.FINE, "end of file reached", ex);
                //End of File
            }
            in.close();            
         }
            
        //open the diveImportDataTrak window to mark the dives for import
        DiveImportDataTrakWindow daw = new DiveImportDataTrakWindow(this.mainWindow, this.mainWindow, diveToAdd);
        daw.setVisible(true);
    }
    
    private void setAlarms(byte higher, byte lower, boolean workAlarms) {
        //set the RBT Alarm for air integrated computers
        if ((((unsigned_int(higher) * 0x100 + unsigned_int(lower)) & 0x003f) ^ 2) == 0) {
            divedata.addAlarm(Messages.getString("rbtalarm"));
        }
        // ascending alarm
        if ((((unsigned_int(higher) * 0x100 + unsigned_int(lower)) & 0x003f) ^ 4) == 0) {
            divedata.addAlarm(Messages.getString("ascalarm"));
        }
        // descending alarm
        if ((((unsigned_int(higher) * 0x100 + unsigned_int(lower)) & 0x003f) ^ 8) == 0) {
            divedata.addAlarm(Messages.getString("descalarm"));
        }
        //set the work Alarm
        if ((workAlarms) && (((unsigned_int(higher) * 0x100 + unsigned_int(lower)) & 0x3f) ^ 16) == 0) {
            divedata.addAlarm(Messages.getString("workalarm"));
        }
        // phys alarm
        if ((workAlarms) && (((unsigned_int(higher) * 0x100 + unsigned_int(lower)) & 0x003f) ^ 32) == 0) {
            divedata.addAlarm(Messages.getString("physalarm"));
        }
        // depth alarm
        if ((((unsigned_int(higher) * 0x100 + unsigned_int(lower)) & 0x003f) ^ 64) == 0) {
            divedata.addAlarm(Messages.getString("depthalarm"));
        }        
        // depth alarm
        if ((((unsigned_int(higher) * 0x100 + unsigned_int(lower)) & 0x003f) ^ 128) == 0) {
            divedata.addAlarm(Messages.getString("depthalarm"));
        }        
    }
    
    
    private int unsigned_int(byte in) {
        int out = 0xff & in;        
        return out;
    }
    
    private static String readText(DataInputStream in, int size) throws IOException {
        byte[] buf = new byte[size];
        for (int i = 0; i<size; i++) {
            buf[i] = in.readByte();
        }
        return characterGerman(buf);
        
    }
    
    /**
     * convert characters encoded with CP850 character set into a Java (unicode) string
     *
     *@param  input  Description of the Parameter
     *@return        Description of the Return Value
     */
    public static String characterGerman(byte[] input)
        throws UnsupportedEncodingException {
        return new String(input, "IBM850");
    }
    
    private String getCommaSeparatedList(ArrayList<String> list) {
        StringBuffer sb = new StringBuffer();
        Iterator<String> it = list.iterator();
        while(it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
    
    private static String getDiveSitePrivateId(Masterdata masterdata, String spot, String city) {
        DiveSite site = masterdata.getDiveSiteBySpotAndCity(spot, city);
        if (site == null) {
            site = new DiveSite();
            site.setSpot(spot);
            site.setCity(city);
            site.setCountry("");
            site.setPrivateId(String.valueOf(masterdata.getNextPrivateDiveSiteId()));
            masterdata.addDiveSite(site);
        }
        return site.getPrivateId();
    }
}
