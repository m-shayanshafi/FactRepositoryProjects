package net.sf.jdivelog.model.uddf;

import org.apache.commons.digester.Digester;

/**
 * Configures the Digester for an UDDF File
 * @author Levtraru
 *
 */
public abstract class UddfDigesterConfigurator {
	
	public static final String DEFAULT_UNITS = "si";
	
	protected final String SEPARATOR = "/";
	protected final String UDDF = "uddf";
	protected final String DATE = "date";
	protected final String DIVE = "dive";
	protected final String REPETITION_GROUP = "repetitiongroup";
	protected final String PROFILEDATA = "profiledata";
	protected final String MIX = "mix";
	protected final String GAS_DEFINITIONS = "gasdefinitions";
	protected final String WAYPOINT = "waypoint";
	protected final String SAMPLES = "samples";
	protected final String EQUIPMENT_USED = "equipmentused";
	protected final String TANKDATA = "tankdata";

	/**
	 * Returns an UDDF Digester Configurator according to the UDDF version number
	 * 
	 * @param versionNumber
	 * @return
	 * @throws UddfException
	 */
	public static UddfDigesterConfigurator getInstanceForVersion(String versionNumber) throws UddfException {
		if (versionNumber.equals("2.2.0")){
			return new Uddf220DigesterConfigurator();
		}
		throw new UddfException("UDDF Version not supported!");			
	}	
	
	
	/**
	 * Configures the Digester for an UDDF File
	 * @param d
	 */
	public abstract void configure(Digester d);

}
