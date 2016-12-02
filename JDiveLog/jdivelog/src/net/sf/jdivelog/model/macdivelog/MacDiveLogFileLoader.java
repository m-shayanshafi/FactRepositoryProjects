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
package net.sf.jdivelog.model.macdivelog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.gui.commands.CommandAddDives;
import net.sf.jdivelog.gui.commands.CommandManager;
import net.sf.jdivelog.model.DiveSite;
import net.sf.jdivelog.model.Equipment;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.Masterdata;
import net.sf.jdivelog.model.Tank;
import net.sf.jdivelog.model.udcf.Alarm;
import net.sf.jdivelog.model.udcf.Dive;
import net.sf.jdivelog.model.udcf.Gas;
import net.sf.jdivelog.util.UnitConverter;
import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;

/**
 * Description: Loader for MacDiveLog files
 * 
 * @author Vladislav Korecky <v.korecky@gmail.com>
 * 
 * Modified by Brian Minnihan (fix several issues to get it to work for a non-trivial # of dives and to get some of the data correct)
 *   
 */
public class MacDiveLogFileLoader {

    private static final Logger LOGGER = Logger.getLogger(MacDiveLogFileLoader.class.getName());
//    private JDive dive = null;
//    private Dive divedata = null;
//    private Gas  divegas  = null;
//    private Tank  divetank  = null;
//    private Equipment  diveequipment  = null;
    
    private File file = null;       
    
    private SimpleDateFormat df = new SimpleDateFormat("dd.MM.yy");
    private MainWindow mainWindow = null;
    
    
    public MacDiveLogFileLoader(MainWindow mainWindow, File file) {
        this.mainWindow = mainWindow;
        this.file = file;
    }
    
    public void Load()
    {
        try {                                                                             
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (this.file);
            
            // normalize text representation
            doc.getDocumentElement ().normalize ();
            Element rootElement = doc.getDocumentElement();            
            if (!rootElement.getNodeName().toLowerCase().equals("logbook")){
                throw new Exception ("Wrong file format");
            }

            String units = LoadPreferences(doc);            
            Equipment diveequipment = LoadEquipments(doc);
            LoadDives(doc,
                    units,
                    diveequipment);
            LoadPersons(doc);
        }catch (SAXParseException err) {        
            LOGGER.log(Level.SEVERE, "** Parsing error" + ", line " 
                + err.getLineNumber () + ", uri " + err.getSystemId ());
            LOGGER.log(Level.SEVERE, " " + err.getMessage ());

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error importing MacDiveLog", ex);
        }                
    }    

    private void LoadDives(Document doc,
            String units,
            Equipment diveequipment) {
        try
        {                        
            // Load Dives     
            TreeSet<JDive> diveToAdd = new TreeSet<JDive>();
            NodeList listOfDives = doc.getElementsByTagName("dive");
            for (int i = 0; i < listOfDives.getLength(); i++) {
                Element diveElement = (Element)listOfDives.item(i);
                
                LOGGER.log(Level.FINE, "Starting import of dive: " + (i + 1));
                
                
                String computer = diveElement.getAttribute("Computer");
                String computerId = diveElement.getAttribute("ComputerID");
                String diver = diveElement.getAttribute("Diver");                
                String sampleIntervalStr = diveElement.getAttribute("SampleInterval");
                String altitudeMode = diveElement.getAttribute("AltitudeMode");
                String personalMode = diveElement.getAttribute("PersonalMode");
                
                String weather = diveElement.getAttribute("Weather");
                
                
                String waterTempEOD = diveElement.getAttribute("WaterTempEOD");
                
                String diveMaster = diveElement.getAttribute("DiveMaster");
                String boat = diveElement.getAttribute("Boat");
                String tank = diveElement.getAttribute("Tank");
                
                String workingPressure = diveElement.getAttribute("WorkingPressure");
                
                String consumptionDisplay = diveElement.getAttribute("ConsumptionDisplay");                
                String lightConditions = diveElement.getAttribute("LightConditions");
                String camera = diveElement.getAttribute("Camera");
                String diveOperator = diveElement.getAttribute("DiveOperator");
                String custom5 = diveElement.getAttribute("Custom5");
                String weight = diveElement.getAttribute("Weight");                
                String consumptionRate = diveElement.getAttribute("ConsumptionRate");
                String cumReset = diveElement.getAttribute("CumReset");
                
                String brand = diveElement.getAttribute("Brand");
                String endSaturation0 = diveElement.getAttribute("EndSaturation0");
                String endSaturation1 = diveElement.getAttribute("EndSaturation1");
                String endSaturation2 = diveElement.getAttribute("EndSaturation2");
                String endSaturation3 = diveElement.getAttribute("EndSaturation3");
                String endSaturation4 = diveElement.getAttribute("EndSaturation4");
                String endSaturation5 = diveElement.getAttribute("EndSaturation5");
                String endSaturation6 = diveElement.getAttribute("EndSaturation6");
                String endSaturation7 = diveElement.getAttribute("EndSaturation7");
                String endSaturation8 = diveElement.getAttribute("EndSaturation8");
                String endSaturation9 = diveElement.getAttribute("EndSaturation9");
                String endSaturation10 = diveElement.getAttribute("EndSaturation10");
                String endSaturation11 = diveElement.getAttribute("EndSaturation11");
                String endSaturation12 = diveElement.getAttribute("EndSaturation12");
                String endSaturation13 = diveElement.getAttribute("EndSaturation13");   

    //                SimpleDateFormat dateFormat = new SimpleDateFormat(dateTime);
    //                divedata.setDate(dateFormat.getDateInstance());
                           
                // unit converter
                UnitConverter unitConverter;
                if (units.toLowerCase().equals("imperial"))
                {
                    // imperial
                    unitConverter = new UnitConverter(UnitConverter.SYSTEM_IMPERIAL, UnitConverter.SYSTEM_SI);
                }
                else
                {
                    // metric
                    unitConverter = new UnitConverter(UnitConverter.SYSTEM_METRIC, UnitConverter.SYSTEM_SI);
                    
                }
                
                // New diveData
                Dive diveData = new Dive();                                         
                // MasterData
                Masterdata masterData = mainWindow.getLogBook().getMasterdata();                                
                                                
                 // Surface interval
                String surfaceIntervalStr = diveElement.getAttribute("SurfaceInterval");
                if (surfaceIntervalStr.length() > 0)
                {
                    Integer surfaceInterval = Integer.parseInt(surfaceIntervalStr);
                    diveData.setSurfaceinterval(String.valueOf(surfaceInterval));
                }
                // Air temperature
                String airTempStr = diveElement.getAttribute("AirTemp");                
                if (airTempStr.length() > 0)
                {
                    Double airTemp = Double.parseDouble(airTempStr);
                    diveData.setSurfaceTemperature(unitConverter.convertTemperature(airTemp));
                }
                // Water temperature
                String waterTempMDStr = diveElement.getAttribute("WaterTempMD");
                if (waterTempMDStr.length() > 0)
                {
                    Double waterTempMD = Double.parseDouble(waterTempMDStr);
                    diveData.setTemperature(unitConverter.convertTemperature(waterTempMD));
                }    
                                                           
                // Set the Equipment
                String tankSizeStr = diveElement.getAttribute("TankSize");
                String gasModel = diveElement.getAttribute("GasModel");
                String startPSIStr = diveElement.getAttribute("StartPSI");
                String endPSIStr = diveElement.getAttribute("EndPSI");
                Gas divegas  = new Gas();    
                divegas.setName(gasModel);
                if (gasModel.toLowerCase().equals("air"))
                {
                    String nitroxEnrichment = diveElement.getAttribute("NitroxEnrichment");
                    if (nitroxEnrichment.length() > 0)
                    {
                        divegas.setOxygen(Double.parseDouble(nitroxEnrichment));                    
                    }
                    else
                    {
                         divegas.setOxygen(new Double(21));                                 
                    }
                    divegas.setNitrogen(new Double(0.79));
                    divegas.setHelium(new Double(0));
                }   
                Double startPSI = 0.0;
                if (startPSIStr.length() > 0)
                {        
                    startPSI = Double.parseDouble(startPSIStr);
                    divegas.setPstart(unitConverter.convertPressure(startPSI));
                    
                }
                if (endPSIStr.length() > 0)
                {  
                    Double endPSI = Double.parseDouble(endPSIStr);
                    divegas.setPend(unitConverter.convertPressure(endPSI));
                }
                if (tankSizeStr.length() > 0)
                {
                    Double tankSize = Double.parseDouble(tankSizeStr);
                    if ((units.toLowerCase().equals("imperial")) && (startPSI > 0))
                    {                        
                        Double convertedTankSize = (tankSize * 31.579) / (unitConverter.convertPressure(startPSI) / 100000);
                        divegas.setTankvolume(convertedTankSize / 1000);
                    }
                    else                        
                    {
                        divegas.setTankvolume(tankSize);
                    }
                }      
                diveData.addGas(divegas);                                
                ArrayList<Tank> tanks = diveequipment.getTanks();                                
                Tank divetank = null;
                if (tanks.isEmpty()) {
                    divetank = new Tank();
                }
                else {
                    divetank = tanks.get(0);
                }                                                
                divetank.setGas(divegas);
                tanks = new ArrayList<Tank>();
                tanks.add(divetank);
                diveequipment.setTanks(tanks);
                
                // Load dive profile
                String durationStr = diveElement.getAttribute("Duration");                
                Double duration = Double.parseDouble(durationStr);                                
                LoadDiveProfile(diveElement, diveData, duration);
                                                
                 // New JDive
                JDive jDive = new JDive("si", diveData);        
                
//                // SetMaxDepth
//                String maxDepthStr = diveElement.getAttribute("MaxDepth");
//                if (maxDepthStr.length() > 0)
//                {
//                    Double maxDepth = Double.parseDouble(maxDepthStr);
//                    jDive.setDepth(maxDepth);
//                }
//                
//                // SetAvarageDepth
//                String averageDepthStr = diveElement.getAttribute("AverageDepth");
//                if (averageDepthStr.length() > 0)
//                {
//                    Double averageDepth = Double.parseDouble(averageDepthStr);
//                    jDive.setAverageDepth(averageDepth);
//                }
                
                String diveNumber = diveElement.getAttribute("Number");
                if (diveNumber != null)
                	jDive.setDiveNumber(Long.parseLong(diveNumber));
                
                // Set DiveType
                String diveType = diveElement.getAttribute("DiveType");
                jDive.setDiveType(diveType);
                jDive.setDiveActivity("");
                
                // Set Duration                
                jDive.setDuration(duration);
                
                // DateTime
                String dateTime = diveElement.getAttribute("DateTime");                
                Date date = new Date(dateTime);
                jDive.setDate(date);                                   
                
                // Set equipment
                jDive.setEquipment(diveequipment); 
                
                // Set Dive site
                String location = diveElement.getAttribute("Location");
                String site = diveElement.getAttribute("Site");
                DiveSite diveSite = masterData.getDiveSiteBySpotAndCountry(site, location);                        
                if (diveSite == null) {
                    diveSite = new DiveSite();
                    diveSite.setSpot(site);
                    diveSite.setCountry(location);                    
                    diveSite.setPrivateId(String.valueOf(masterData.getNextPrivateDiveSiteId()));
                    masterData.addDiveSite(diveSite);
                }                
                jDive.setDiveSiteId(diveSite.getPrivateId());                               
                
                // Visibillity
                String visibilityStr = diveElement.getAttribute("Visibility");
                jDive.setVisibility(visibilityStr);
                
                // Set buddy
                String diveBuddy = diveElement.getAttribute("DiveBuddy");
                jDive.setBuddy(diveBuddy);
                
                //add the dive                
                diveToAdd.add(jDive);
            }
            CommandAddDives cmd = new CommandAddDives(this.mainWindow, diveToAdd);
            CommandManager.getInstance().execute(cmd);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error importing MacDiveLog - LoadDives", ex);
        }
    }
    
    private void LoadDiveProfile(Element diveElement, Dive divedata, Double duration) {
    	long currentTime = System.currentTimeMillis();
                       
        UnitConverter c = new UnitConverter(UnitConverter.SYSTEM_IMPERIAL, UnitConverter.SYSTEM_SI);
        NodeList diveProfiles = diveElement.getElementsByTagName("profile");
        
        Double deltaUnit = (duration) / diveProfiles.getLength();                
        divedata.addDelta(String.valueOf(deltaUnit));
        divedata.setDeltaMode();                                       
        divedata.addDepth("0.00");
        Double depth = 0.0;
        for (int i = 0; i < diveProfiles.getLength(); i++ )
        {
            Element profileElement = (Element)diveProfiles.item(i);
            String depthStr = profileElement.getAttribute("depth");            
            String surfaceStr = profileElement.getAttribute("surface"); 
            String rbtStr = profileElement.getAttribute("rbt"); 
            String compassStr = profileElement.getAttribute("compass");                         
            
            if (depthStr.length() > 0)
            {
                depth = Double.parseDouble(depthStr);
            }
            divedata.addDepth(String.valueOf(c.convertAltitude(depth))); 
            
            if (surfaceStr.length() > 0)
            {
                divedata.addAlarm(Alarm.ALARM_SURFACE);
            }                                                                
        }
        divedata.addDepth("0.00");
        LOGGER.log(Level.FINE, "LoadDiveProfile() time: " + (System.currentTimeMillis() - currentTime) + " msecs");
    }

    private Equipment LoadEquipments(Document doc) {
    	long currentTime = System.currentTimeMillis();
        Equipment diveequipment  = new Equipment();
        
        try
        {        
            // Load equipments (TO-DO)
            NodeList listOfEquipment = doc.getElementsByTagName("gear"); 
            
            String diveequipmentComment = "";
            String diveequipmentName = "";
                        
            for (int i = 0; i < listOfEquipment.getLength(); i++) {
                Element gearElement = (Element)listOfEquipment.item(i);
                
                String equipmentType = gearElement.getAttribute("Icon");  
                String equipmentManufacturer = gearElement.getAttribute("Manufacturer");
                String equipmentName = gearElement.getAttribute("Name");
                String equipmentSN = gearElement.getAttribute("SerialNumber");
                                    
                // Suit
                if ((equipmentType.toLowerCase().equals("wetsuit")) ||
                        (equipmentType.toLowerCase().equals("drysuit"))) {
                    String suit = "";
                    
                    if (equipmentManufacturer.length() > 0) {
                        suit += equipmentManufacturer + " ";
                    }
                    
                    suit += equipmentName;
                    
                    if (equipmentSN.length() > 0) {
                        suit += " [SN:" + equipmentSN + "]";
                    }                                     
                    
                    diveequipment.setSuit(suit);
                }
                else if (equipmentType.toLowerCase().equals("glove")){                    
                    String glove = "";
                    
                    if (equipmentManufacturer.length() > 0) {
                        glove += equipmentManufacturer + " ";
                    }
                    
                    glove += equipmentName;
                    
                    if (equipmentSN.length() > 0) {
                        glove += " [SN:" + equipmentSN + "]";
                    } 
                    
                    diveequipment.setGloves(glove);
                }   
                else if (equipmentType.toLowerCase().equals("tank")){
                    String tank = "";
                    
                    if (equipmentManufacturer.length() > 0) {
                        tank += equipmentManufacturer + " ";
                    }
                    
                    tank += equipmentName;
                    
                    if (equipmentSN.length() > 0) {
                        tank += " [SN:" + equipmentSN + "]";
                    } 
                    
                    Tank divetank = new Tank();                   
                    divetank.setType(tank);    
                    diveequipment.addTank(divetank);
                }
                else
                {
                    if (equipmentType.toLowerCase().equals("bag")){
                        diveequipmentComment += "Bag: ";                                                                
                    }
                    else if (equipmentType.toLowerCase().equals("camera")){
                        diveequipmentComment += "Camera: ";
                    }
                    else if (equipmentType.toLowerCase().equals("mask")){
                        diveequipmentComment += "Mask: ";
                    }
                    else if (equipmentType.toLowerCase().equals("reg")){
                        diveequipmentComment += "Reg: ";
                    }                
                    else if (equipmentType.toLowerCase().equals("bc")){
                        diveequipmentComment += "BC: ";
                    }
                    else if (equipmentType.toLowerCase().equals("misc")){
                        diveequipmentComment += "Misc: ";
                    }                    
                    else if (equipmentType.toLowerCase().equals("fin")){
                        diveequipmentComment += "Fin: ";
                    }
                    else if (equipmentType.toLowerCase().equals("snorkel")){
                        diveequipmentComment += "Snorkel: ";
                    }
                    
                    if (equipmentManufacturer.length() > 0) {
                        diveequipmentComment += equipmentManufacturer + " ";
                    }
                    
                    if (equipmentName.length() > 0) {
                        diveequipmentComment += equipmentName + " ";
                    }
                    
                    if (equipmentSN.length() > 0) {
                        diveequipmentComment += "SN:" + equipmentSN + ", ";
                    }
                                        
                    diveequipmentComment += "; ";
                }                                  
            }
            
            diveequipmentName = diveequipment.getSuit();
            diveequipment.setComment(diveequipmentComment);
            diveequipment.setName(diveequipmentName);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error importing MacDiveLog - LoadEquipments", ex);
        }            
        LOGGER.log(Level.FINE, "LoadEquipment() time: " + (System.currentTimeMillis() - currentTime) + " msecs");
        return diveequipment;
    }

    private void LoadPersons(Document doc) {
        NodeList listOfPersons = doc.getElementsByTagName("person");
        
        try
        {
            int totalPersons = listOfPersons.getLength();
            System.out.println("Total no of people : " + totalPersons);

            for(int s=0; s<listOfPersons.getLength() ; s++){


                Node firstPersonNode = listOfPersons.item(s);
                if(firstPersonNode.getNodeType() == Node.ELEMENT_NODE){


                    Element firstPersonElement = (Element)firstPersonNode;

                    //-------
                    NodeList firstNameList = firstPersonElement.getElementsByTagName("first");
                    Element firstNameElement = (Element)firstNameList.item(0);

                    NodeList textFNList = firstNameElement.getChildNodes();
                    System.out.println("First Name : " + 
                           ((Node)textFNList.item(0)).getNodeValue().trim());

                    //-------
                    NodeList lastNameList = firstPersonElement.getElementsByTagName("last");
                    Element lastNameElement = (Element)lastNameList.item(0);

                    NodeList textLNList = lastNameElement.getChildNodes();
                    System.out.println("Last Name : " + 
                           ((Node)textLNList.item(0)).getNodeValue().trim());

                    //----
                    NodeList ageList = firstPersonElement.getElementsByTagName("age");
                    Element ageElement = (Element)ageList.item(0);

                    NodeList textAgeList = ageElement.getChildNodes();
                    System.out.println("Age : " + 
                           ((Node)textAgeList.item(0)).getNodeValue().trim());

                    //------

                }//end of if clause
            }//end of for loop with s var
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error importing MacDiveLog - LoadPersons", ex);
        }
    }

    private String LoadPreferences(Document doc) {
         // Load preference
        String units = "metric";
        
        try
        {                        
            NodeList listOfPreference = doc.getElementsByTagName("prefs");            
            if (listOfPreference.getLength() > 0) {
                Element preferenceElement = (Element)listOfPreference.item(0);
                units = preferenceElement.getAttribute("units");                                        
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error importing MacDiveLog - LoadPreferences", ex);
        }
        
        return units;
    }
}

