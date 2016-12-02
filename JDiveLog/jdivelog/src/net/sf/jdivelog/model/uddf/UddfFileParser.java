/**
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: UddfFileParser.java
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
package net.sf.jdivelog.model.uddf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.uddf.file.UddfFile;
import net.sf.jdivelog.model.uddf.file.UddfFileDive;

import org.apache.commons.digester.Digester;


/**
 * Parses dives from an UDDF File 
 * @author Levtraru
 *
 */
public class UddfFileParser {


	/**
	 * Creates a new UDDF Parser
	 * @param logbook
	 */
	public UddfFileParser() {
    }

    /**
     * Parses dives from an UDDF File
     * 
     * @param file
     * @return
     * @throws UddfException
     */
    public TreeSet<JDive> parse(File file) throws UddfException{
    	TreeSet<JDive> jDives =new TreeSet<JDive>();
		try {
			Digester d = new Digester();			
			
	        this.configureDigesterRules(d, getUddfVersion(file));
	        UddfFile uddfFile =  (UddfFile) d.parse(file);
			jDives =(TreeSet<JDive>)  buildJDivesFromFile(uddfFile);
		} catch (UddfException e) {
			throw e;
		} catch (Exception e) {
			throw new UddfException(e.getMessage(),e);
		} 
		return jDives;
        }
        

    /**
     * Converts the UDDF Dives into JDives
     * @param uddfFile
     * @return
     * @throws UddfException
     */
	private TreeSet<JDive> buildJDivesFromFile(UddfFile uddfFile) throws UddfException {		
		TreeSet<JDive> jDives = new TreeSet<JDive>();
		Iterator<UddfFileDive> it = uddfFile.getProfileData().iterator();
		while (it.hasNext()){
			jDives.add(UddfAdapter.getInstance().buildJDive(it.next(), uddfFile.getGasDefinitions()));
		}
		return jDives;
	}

	/**
	 * Reads the UDDF version from an UDDF File
	 * 
	 * @param file
	 * @return
	 * @throws UddfException
	 */
	private String getUddfVersion(File file) throws UddfException  {
		BufferedReader reader;

		try {
			
			reader = new BufferedReader(new FileReader(file));
			String firstLine = reader.readLine();
			String[] splittedFirstLine = firstLine.split("\"");					
			reader.close();
			return splittedFirstLine[1];			
		} catch (FileNotFoundException e) {
			throw new UddfException("Couldn't find: "+file.getName(), e);
		} catch (IOException e) {
			throw new UddfException("Error reading file: "+file.getName(), e);
		}

	}

	/**
	 * Configures Digester according to UDDF version
	 * @param d
	 * @param versionNumber
	 * @throws UddfException
	 */
	private void configureDigesterRules(Digester d, String versionNumber) throws UddfException {		
		UddfDigesterConfigurator configurator = UddfDigesterConfigurator.getInstanceForVersion(versionNumber); 
		configurator.configure(d);
	}

}
