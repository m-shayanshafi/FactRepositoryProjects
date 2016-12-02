package net.sf.jdivelog.model.uddf;

import net.sf.jdivelog.model.uddf.file.UddfFile;
import net.sf.jdivelog.model.uddf.file.UddfFileDive;
import net.sf.jdivelog.model.uddf.file.UddfFileEquipmentUsed;
import net.sf.jdivelog.model.uddf.file.UddfFileGasDefinitions;
import net.sf.jdivelog.model.uddf.file.UddfFileMix;
import net.sf.jdivelog.model.uddf.file.UddfFileSurfaceInterval;
import net.sf.jdivelog.model.uddf.file.UddfFileTankdata;
import net.sf.jdivelog.model.uddf.file.UddfFileWayAltitude;
import net.sf.jdivelog.model.uddf.file.UddfFileWayPoint;

import org.apache.commons.digester.Digester;

/**
 * 
 * Configures the Digester for UDDF v2.2.0
 * 
 * @author Levtraru
 * 
 */
public class Uddf220DigesterConfigurator extends UddfDigesterConfigurator {

	private final String DIVE_PATTERN = UDDF + SEPARATOR + PROFILEDATA + SEPARATOR + REPETITION_GROUP + SEPARATOR + DIVE;
	private final String EQUIPMENT_USED_PATTERN = DIVE_PATTERN + SEPARATOR + EQUIPMENT_USED;
	private final String WAYPOINT_PATTERN = DIVE_PATTERN + SEPARATOR + SAMPLES + SEPARATOR + WAYPOINT;
	private final String GASDEFINITIONS_PATTERN = UDDF + SEPARATOR + GAS_DEFINITIONS;	
	private final String MIX_PATTERN = GASDEFINITIONS_PATTERN + SEPARATOR + MIX;
	private final String TANKDATA_PATTERN = GASDEFINITIONS_PATTERN + SEPARATOR + TANKDATA;

	/**
	 * Configures the Digester for an UDDF File according to 2.2.0 version
	 * 
	 * @param d
	 */
	public void configure(Digester d) {
		d.setValidating(false); // TODO must validate?
		d.addObjectCreate(UDDF, UddfFile.class);
		configureGasDefinition(d);
		configureProfileData(d);
	}

	private void configureGasDefinition(Digester d) {
		//Creates a new Gas Definition
		d.addObjectCreate(GASDEFINITIONS_PATTERN, UddfFileGasDefinitions.class);
		configureMix(d);
		configureTankData(d);
		//Sets the Gas Definition
		d.addSetNext(GASDEFINITIONS_PATTERN, "setGasDefinitions");
	}

	private void configureMix(Digester d) {
		//Creates a new Gas Mix
		d.addObjectCreate(MIX_PATTERN, UddfFileMix.class);
		d.addSetProperties(MIX_PATTERN, "id", "id");
		d.addCallMethod(MIX_PATTERN + SEPARATOR + "ar", "setAr", 1);
		d.addCallParam(MIX_PATTERN + SEPARATOR + "ar", 0);
		d.addCallMethod(MIX_PATTERN + SEPARATOR + "equivalentairdepth", "setEquivalentairdepth", 1);
		d.addCallParam(MIX_PATTERN + SEPARATOR + "equivalentairdepth", 0);
		d.addCallMethod(MIX_PATTERN + SEPARATOR + "h2", "setH2", 1);
		d.addCallParam(MIX_PATTERN + SEPARATOR + "h2", 0);
		d.addCallMethod(MIX_PATTERN + SEPARATOR + "he", "setHe", 1);
		d.addCallParam(MIX_PATTERN + SEPARATOR + "he", 0);
		d.addCallMethod(MIX_PATTERN + SEPARATOR + "n2", "setN2", 1);
		d.addCallParam(MIX_PATTERN + SEPARATOR + "n2", 0);
		d.addCallMethod(MIX_PATTERN + SEPARATOR + "maximumoperationdepth", "setMaximumoperationdepth", 1);
		d.addCallParam(MIX_PATTERN + SEPARATOR + "maximumoperationdepth",0);
		d.addCallMethod(MIX_PATTERN + SEPARATOR + "maximumpo2", "setMaximumpo2", 1);
		d.addCallParam(MIX_PATTERN + SEPARATOR + "maximumpo2", 0);
		d.addCallMethod(MIX_PATTERN + SEPARATOR + "name", "setName", 1);
		d.addCallParam(MIX_PATTERN + SEPARATOR + "name", 0);		
		d.addCallMethod(MIX_PATTERN + SEPARATOR + "o2", "setO2", 1);
		d.addCallParam(MIX_PATTERN + SEPARATOR + "o2", 0);
		d.addCallMethod(MIX_PATTERN + SEPARATOR + "priceperlitre", "setPriceperlitre", 1);
		d.addCallParam(MIX_PATTERN + SEPARATOR + "priceperlitre", 0);
		d.addSetProperties(MIX_PATTERN + SEPARATOR + "priceperlitre", "currency", "priceperlitreCurrency");
		// Adds a Gas Mix to the  List
		d.addSetNext(MIX_PATTERN, "addMix");
	}
	
	private void configureTankData(Digester d){
		 // Creates a new Tank Data
		 d.addObjectCreate(TANKDATA_PATTERN, UddfFileTankdata.class);
		 
		 d.addSetProperties(TANKDATA_PATTERN, "id", "id");		 
		 d.addCallMethod(TANKDATA_PATTERN + SEPARATOR +"breathingconsumptionvolume", "setBreathingconsumptionvolume", 1);
		 d.addCallParam(TANKDATA_PATTERN + SEPARATOR +"breathingconsumptionvolume", 0);
		 d.addSetProperties(TANKDATA_PATTERN + SEPARATOR + "mixref", "ref", "mixref"); 
		 d.addSetProperties(TANKDATA_PATTERN + SEPARATOR + "tankref", "ref", "tankref");
		 d.addCallMethod(TANKDATA_PATTERN + SEPARATOR +"tankpressurebegin", "setTankpressurebegin", 1);
		 d.addCallParam(TANKDATA_PATTERN + SEPARATOR +"tankpressurebegin", 0);
		 d.addCallMethod(TANKDATA_PATTERN + SEPARATOR +"tankpressureend", "setTankpressureend", 1);
		 d.addCallParam(TANKDATA_PATTERN + SEPARATOR +"tankpressureend", 0); 
		 d.addCallMethod(TANKDATA_PATTERN + SEPARATOR +"tankvolume", "setTankvolume", 1);
		 d.addCallParam(TANKDATA_PATTERN + SEPARATOR +"tankvolume", 0);
		 //Adds a Tank Data to the List
		 d.addSetNext(TANKDATA_PATTERN, "addTankdata");
		} 	
	
	private void configureProfileData(Digester d) {
		//Creates a new Dive
		d.addObjectCreate(DIVE_PATTERN, UddfFileDive.class);
		
		d.addCallMethod(DIVE_PATTERN + SEPARATOR + "airtemperature", "setAirtemperature", 1);
		d.addCallParam(DIVE_PATTERN + SEPARATOR + "airtemperature", 0);
		d.addCallMethod(DIVE_PATTERN + SEPARATOR + "altitude", "setAltitude", 1);
		d.addCallParam(DIVE_PATTERN + SEPARATOR + "altitude", 0);
		d.addSetProperties(DIVE_PATTERN + SEPARATOR + "buddyref", "ref", "buddyref");
		d.addCallMethod(DIVE_PATTERN + SEPARATOR + "date", "setDate", 3);
		d.addCallParam(DIVE_PATTERN + SEPARATOR + "date" + SEPARATOR + "day", 0);
		d.addCallParam(DIVE_PATTERN + SEPARATOR + "date" + SEPARATOR + "month", 1);
		d.addCallParam(DIVE_PATTERN + SEPARATOR + "date" + SEPARATOR + "year", 2);
		d.addCallMethod(DIVE_PATTERN + SEPARATOR + "density", "setDensity", 1);
		d.addCallParam(DIVE_PATTERN + SEPARATOR + "density", 0);
		d.addCallMethod(DIVE_PATTERN + SEPARATOR + "divenumber", "setDivenumber", 1);
		d.addCallParam(DIVE_PATTERN + SEPARATOR + "divenumber", 0);
		configureEquipmentUsed(d);
		d.addCallMethod(DIVE_PATTERN + SEPARATOR + "greatestdepth", "setGreatestdepth", 1);
		d.addCallParam(DIVE_PATTERN + SEPARATOR + "greatestdepth",0);
		d.addCallMethod(DIVE_PATTERN + SEPARATOR + "lowesttemperature", "setLowesttemperature", 1);
		d.addCallParam(DIVE_PATTERN + SEPARATOR + "lowesttemperature", 0);
		configureSamples(d);
		configureSurfaceInterval(d);
		d.addCallMethod(DIVE_PATTERN + SEPARATOR + "time", "setTime", 2);
		d.addCallParam(DIVE_PATTERN + SEPARATOR + "time" + SEPARATOR + "hour", 0);
		d.addCallParam(DIVE_PATTERN + SEPARATOR + "time" + SEPARATOR + "minute", 1);
		
		// Adds a Dive to the Dive List (profile data)
		d.addSetNext(DIVE_PATTERN, "addUddfFileDive");
	}

	private void configureSurfaceInterval(Digester d) {
		//Creates a new Surface Interval
		d.addObjectCreate(DIVE_PATTERN + SEPARATOR + "surfaceinterval", UddfFileSurfaceInterval.class);
		
		d.addCallMethod(DIVE_PATTERN + SEPARATOR + "surfaceinterval" + SEPARATOR + "passedtime", "setPassedtime", 1);
		d.addCallParam(DIVE_PATTERN + SEPARATOR + "surfaceinterval" + SEPARATOR + "passedtime", 0);
		d.addCallMethod(DIVE_PATTERN + SEPARATOR + "surfaceinterval" + SEPARATOR + "infinity", "setPassedtimeInfinity");
		configureWayaltitude(d);		
		//Sets the Surface Interval
		d.addSetNext(DIVE_PATTERN + SEPARATOR + "surfaceinterval" , "setSurfaceinterval");		
		
	}

	private void configureWayaltitude(Digester d) {
		//Creates a new Way Altitude
		d.addObjectCreate(DIVE_PATTERN + SEPARATOR + "surfaceinterval" + SEPARATOR + "wayaltitude", UddfFileWayAltitude.class);
		
		d.addSetProperties(DIVE_PATTERN + SEPARATOR + "surfaceinterval" + SEPARATOR + "wayaltitude", "waytime", "waytime");
		d.addCallMethod(DIVE_PATTERN + SEPARATOR + "surfaceinterval" + SEPARATOR + "wayaltitude", "setAltitude", 1);
		d.addCallParam(DIVE_PATTERN + SEPARATOR + "surfaceinterval" + SEPARATOR + "wayaltitude", 0);		
		// Adds the Way Altitude
		d.addSetNext(DIVE_PATTERN + SEPARATOR + "surfaceinterval" + SEPARATOR + "wayaltitude", "addWayaltitude");
	}

	private void configureEquipmentUsed(Digester d) {
		//Creates a new Equipment Used
		d.addObjectCreate(EQUIPMENT_USED_PATTERN, UddfFileEquipmentUsed.class);
		
		d.addSetProperties(EQUIPMENT_USED_PATTERN + SEPARATOR + "bootsref", "ref", "bootsref");
		d.addSetProperties(EQUIPMENT_USED_PATTERN + SEPARATOR + "buoyancycontroldeviceref", "ref", "buoyancycontroldeviceref");
		d.addSetProperties(EQUIPMENT_USED_PATTERN + SEPARATOR + "cameraref", "ref", "cameraref");
		d.addSetProperties(EQUIPMENT_USED_PATTERN + SEPARATOR + "compassref", "ref", "compassref");
		d.addSetProperties(EQUIPMENT_USED_PATTERN + SEPARATOR + "divecomputerref", "ref", "divecomputerref");
		d.addSetProperties(EQUIPMENT_USED_PATTERN + SEPARATOR + "finsref", "ref", "finsref");
		d.addSetProperties(EQUIPMENT_USED_PATTERN + SEPARATOR + "glovesref", "ref", "glovesref");
		d.addSetProperties(EQUIPMENT_USED_PATTERN + SEPARATOR + "kniferef", "ref", "kniferef");
		d.addSetProperties(EQUIPMENT_USED_PATTERN + SEPARATOR + "leadref", "ref", "leadref");
		d.addSetProperties(EQUIPMENT_USED_PATTERN + SEPARATOR + "lightref", "ref", "lightref");
		d.addSetProperties(EQUIPMENT_USED_PATTERN + SEPARATOR + "maskref", "ref", "maskref");
		d.addSetProperties(EQUIPMENT_USED_PATTERN + SEPARATOR + "rebreatherref", "ref", "rebreatherref");
		d.addSetProperties(EQUIPMENT_USED_PATTERN + SEPARATOR + "regulatorref", "ref", "regulatorref");
		d.addSetProperties(EQUIPMENT_USED_PATTERN + SEPARATOR + "scooterref", "ref", "scooterref");
		d.addSetProperties(EQUIPMENT_USED_PATTERN + SEPARATOR + "suitref", "ref", "suitref");
		d.addCallMethod(EQUIPMENT_USED_PATTERN + SEPARATOR + "tankref", "addTankref", 1);		
		d.addCallParam(EQUIPMENT_USED_PATTERN + SEPARATOR + "tankref", 0, "ref");
		d.addCallMethod(EQUIPMENT_USED_PATTERN + SEPARATOR + "tankdataref", "addTankdataref", 1);		
		d.addCallParam(EQUIPMENT_USED_PATTERN + SEPARATOR + "tankdataref", 0, "ref");
		d.addSetProperties(EQUIPMENT_USED_PATTERN + SEPARATOR + "variouspiecesref", "ref", "variouspiecesref");
		d.addSetProperties(EQUIPMENT_USED_PATTERN + SEPARATOR + "videocameraref", "ref", "videocameraref");
		d.addSetProperties(EQUIPMENT_USED_PATTERN + SEPARATOR + "watchref", "ref", "watchref");		
		// Sets the used equipment 
		d.addSetNext(EQUIPMENT_USED_PATTERN, "setEquipmentused");
	}

	private void configureSamples(Digester d) {
		//Creates a new Way Point
		d.addObjectCreate(WAYPOINT_PATTERN, UddfFileWayPoint.class);
		
		d.addCallMethod(WAYPOINT_PATTERN + SEPARATOR + "alarm", "setAlarm", 1);
		d.addCallParam(WAYPOINT_PATTERN + SEPARATOR + "alarm", 0);
		d.addCallMethod(WAYPOINT_PATTERN + SEPARATOR + "cns", "setCns", 1);
		d.addCallParam(WAYPOINT_PATTERN + SEPARATOR + "cns", 0);
		d.addCallMethod(WAYPOINT_PATTERN + SEPARATOR + "depth", "setDepth", 1);
		d.addCallParam(WAYPOINT_PATTERN + SEPARATOR + "depth", 0);
		d.addCallMethod(WAYPOINT_PATTERN + SEPARATOR + "divetime", "setDivetime", 1);
		d.addCallParam(WAYPOINT_PATTERN + SEPARATOR + "divetime",0);
		d.addCallMethod(WAYPOINT_PATTERN + SEPARATOR + "otu", "setOtu", 1);
		d.addCallParam(WAYPOINT_PATTERN + SEPARATOR + "otu", 0);
		d.addCallMethod(WAYPOINT_PATTERN + SEPARATOR + "setpo2", "setSetpo2", 1);
		d.addCallParam(WAYPOINT_PATTERN + SEPARATOR + "setpo2",0);
		d.addSetProperties(WAYPOINT_PATTERN + SEPARATOR + "switchmix", "ref", "switchmix");
		d.addCallMethod(WAYPOINT_PATTERN + SEPARATOR + "temperature", "setTemperature", 1);
		d.addCallParam(WAYPOINT_PATTERN + SEPARATOR + "temperature",0);
		// Adds a WayPoint to the Samples ArrayList
		d.addSetNext(WAYPOINT_PATTERN, "addSample");
	}

}
