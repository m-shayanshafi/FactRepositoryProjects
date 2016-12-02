/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: UdcfFileLoader.java
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
package net.sf.jdivelog.model.wlog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.gui.commands.CommandAddDives;
import net.sf.jdivelog.gui.commands.CommandManager;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.DiveSite;
import net.sf.jdivelog.model.Equipment;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.Masterdata;
import net.sf.jdivelog.model.Tank;
import net.sf.jdivelog.model.udcf.Dive;
import net.sf.jdivelog.model.udcf.Gas;

/**
 * Description: Loader for WLOG files
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class WLOGFileLoader {

    private static final Logger LOGGER = Logger.getLogger(WLOGFileLoader.class.getName());
    private JDive dive = null;
    private Dive divedata = null;
    private Gas  divegas  = null;
    private Tank  divetank  = null;
    private Equipment  diveequipment  = null;
    
    private SimpleDateFormat df = new SimpleDateFormat("dd.MM.yy");
    private MainWindow mainWindow = null;
    
    
    public WLOGFileLoader(MainWindow mainWindow, File[] files) {
        this.mainWindow = mainWindow;
        BufferedReader in;
        String line;        
        String divehour = null;
        String diveminute = null;
        Double divesecond = null;
        String divedepth = null;
        String number = null;
        String divesuit = "";
        int hour = 0;
        int minute = 0;
        int pstart = 0;
        int pconsumption = 0;
        try {
            for (int i = 0; i < files.length; i++) {
                dive = new JDive();
                //Dive Data for the Profil
                divedata = new Dive();
                divegas  = new Gas();
                divetank  = new Tank();
                diveequipment  = new Equipment();
                
                in = new BufferedReader(new FileReader(files[i]));
                //generate the air gas
                divegas.setName(Messages.getString("default_mixname"));
                divegas.setOxygen(new Double(0.21));
                divegas.setNitrogen(new Double(0.79));
                divegas.setHelium(new Double(0));
                
                divedata.setSurfaceinterval("");
                divedata.setDensity(new Double(0));
                divedata.setAltitude(new Double(0));
                
                String spot = "";
                String location = "";
                
                while ((line = in.readLine()) != null) {
                    if (line.trim().length() == 0) {
                        continue;
                    }
                    try {
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_date"))) {
                            dive.setDate(df.parse(get_string_after_separator(line,';').trim()));
                            divedata.setDate(df.parse(get_string_after_separator(line,';').trim()));
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_time"))) {
                            divehour = get_string_after_separator(line,';').trim().substring(0,2);
                            diveminute = get_string_after_separator(line,';').trim().substring(3,5);
                            dive.setTime(divehour, diveminute);
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_place"))) {
                            location = get_string_after_separator(line,';').trim();
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_divingplace"))) {
                            spot = get_string_after_separator(line,';').trim();
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_max_depth"))) {
                            number = get_string_before_separator(get_string_after_separator(line,';'),',');
                            number = number + "." + get_string_after_separator(get_string_after_separator(line,';'),',');
                            dive.setDepth(number);
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_average_depth"))) {
                            number = get_string_before_separator(get_string_after_separator(line,';'),',');
                            number = number + "." + get_string_after_separator(get_string_after_separator(line,';'),',');
                            dive.setAverageDepth(number);
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_duration"))) {
                            hour = new Integer(get_string_before_separator(get_string_after_separator(line,';'),':')).intValue() * 60;
                            minute = new Integer(get_string_after_separator(get_string_after_separator(line,';'),':')).intValue();
                            dive.setDuration(new Double(hour + minute));
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_amv"))) {
                            number = get_string_before_separator(get_string_after_separator(line,';'),',');
                            number = number + "." + get_string_after_separator(get_string_after_separator(line,';'),',');
                            dive.setAMV(new Double(number)/1000);
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_altitude"))) {
                            //dive.setPlace(get_string_after_separator(line,';').trim());
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_weather"))) {
                            if (dive.getVisibility() != null) {
                                dive.setVisibility(dive.getVisibility() + " m  " + Messages.getString("weather") + ": " + get_string_after_separator(line,';').trim());
                            } else {
                                dive.setVisibility(Messages.getString("weather") + ": " + get_string_after_separator(line,';').trim());
                            }
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_view"))) {
                            if (dive.getVisibility() != null) {
                                dive.setVisibility(get_string_after_separator(line,';').trim() + " m  " + dive.getVisibility() );
                            } else {
                                dive.setVisibility(get_string_after_separator(line,';').trim() + " m");
                            }
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_suit"))) {
                            divesuit = diveequipment.getSuit();                        
                            if (divesuit != null) {
                                diveequipment.setSuit(divesuit + " " + get_string_after_separator(line,';').trim());
                            } else {
                                diveequipment.setSuit(get_string_after_separator(line,';').trim());
                            }
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_suit_model"))) {
                            divesuit = diveequipment.getSuit(); 
                            if (divesuit != null) {
                                diveequipment.setSuit(divesuit + " " + get_string_after_separator(line,';').trim());
                            } else {
                                diveequipment.setSuit(get_string_after_separator(line,';').trim());
                            }
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_lead"))) {
                            number = get_string_before_separator(get_string_after_separator(line,';'),',');
                            number = number + "." + get_string_after_separator(get_string_after_separator(line,';'),',');
                            diveequipment.setWeight(number);
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_diving_type"))) {
                            dive.setDiveType(get_string_after_separator(line,';').trim());
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_diving_activity"))) {
                            dive.setDiveActivity(get_string_after_separator(line,';').trim());
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_buddy"))) {
                            dive.setBuddy(get_string_after_separator(line,';').trim());
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_bottle"))) {
                            number = get_string_before_separator(get_string_after_separator(line,';'),',');
                            number = number + "." + get_string_after_separator(get_string_after_separator(line,';'),',');
                            divegas.setTankvolume(new Double(number) / 1000);
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_bottle_material"))) {
                            if (get_string_after_separator(line,';').trim().equals(Messages.getString("tanktype_steel"))) {
                                divetank.setType(Messages.getString("tanktype_steel"));
                            }
                            if (get_string_after_separator(line,';').trim().equalsIgnoreCase(Messages.getString("tanktype_aluminium"))) {
                                divetank.setType(Messages.getString("tanktype_aluminium"));
                            }
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_pressure"))) {
                            pstart = new Integer(get_string_after_separator(line,';').trim()).intValue();
                            divegas.setPstart(get_string_after_separator(line,';').trim());
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_air_consumption"))) {
                            pconsumption = new Integer(get_string_after_separator(line,';').trim()).intValue();
                            divegas.setPend(new Double(pstart - pconsumption));
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_water_temperatur"))) {
                            number = get_string_before_separator(get_string_after_separator(line,';'),',');
                            number = number + "." + get_string_after_separator(get_string_after_separator(line,';'),',');
                            dive.setTemperature(number);
                            divedata.setTemperature(number);
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_air_temperatur"))) {
                            //dive.setPlace(get_string_after_separator(line,';').trim());
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase(Messages.getString("wlog_description"))) {
                            dive.setComment(get_string_after_separator(line,';').trim());
                        }
                        if (get_string_before_separator(line,';').trim().equalsIgnoreCase("")) {
                            divesecond = new Double(get_string_before_separator(get_string_after_separator(line,';'),';')) / 60;
                            divedepth = get_string_after_separator(get_string_after_separator(line,';'),';');
                            number = get_string_before_separator(get_string_after_separator(divedepth,';'),',');
                            number = number + "." + get_string_after_separator(get_string_after_separator(divedepth,';'),',');
                            //if negative value extract the minus
                            if (number.substring(0,1).equals("-"))  {
                                number = number.substring(1,number.length());
                            }
                            divedata.addTime(divesecond.toString());
                            divedata.addDepth(number);                    
                            divedata.addGas(divegas);            
                        }
                    } catch (Exception ex) {
                }
            }
            in.close();
            
            dive.setDiveSiteId(getDiveSitePrivateId(mainWindow.getLogBook().getMasterdata(), spot, location));
            
            //Generate the diveprofil
            if (divedata.getSamples() != null) {
                divedata.setTimeDepthMode();
                dive.setDive(divedata);
                dive.setUnits("metric");
            }
            
            //set the Equipment
            divetank.setGas(divegas);
            diveequipment.addTank(divetank);
            dive.setEquipment(diveequipment);
            
            //add the dive
            ArrayList<JDive> diveToAdd = new ArrayList<JDive>();
            diveToAdd.add(dive);
            CommandAddDives cmd = new CommandAddDives(this.mainWindow, diveToAdd);
            CommandManager.getInstance().execute(cmd);
         }
       } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "error parsing wlog file", ex);
       }
    }
    
    
    public String get_string_before_separator(String input, char character) {
        StringBuffer buf = new StringBuffer();
        if (input != null) {
            input = input.trim();
            buf.append(input.substring(0, input.indexOf(character)));
        }        
        return buf.toString();
    }

    public String get_string_after_separator(String input, char character) {
        StringBuffer buf = new StringBuffer();
        if (input != null) {
            input = input.trim();
            buf.append(input.substring(input.indexOf(character)+1, input.length()));
        }        
        return buf.toString();
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
