/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ComputerInterface.java
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
package net.sf.jdivelog.ci;

import java.util.Properties;
import java.util.TreeSet;

import javax.swing.JPanel;

import net.sf.jdivelog.gui.statusbar.StatusInterface;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.JDiveLog;

/**
 * This interface describes all necessary functions to implement a computer interface.
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public interface ComputerInterface {
    
    /**
     * gets the name of the driver.
     * @return the name of the driver that should appear in the list.
     */
    public String getDriverName();
        
    /**
     * gets the name of the properties that can be set in the configuration dialog.
     * the name of the properties should start with the name of the computer interface, e.g. suunto.xxx
     * @return a String array containing the property names.
     */
    public String[] getPropertyNames();
    
    /**
     * gets the configuration panel for the computer interface.
     * @return the component to set the configuration property.
     */
    public JPanel getConfigurationPanel();
        
    /**
     * gets the configuration from the configuration components.
     * @return the Properties containing the configuration values.
     */
    public Properties saveConfiguration();

    /**
     * initializes the computer interface and loads the properties to the configuration panel.
     * @param properties the properties from the configuration
     */
    public void initialize(Properties properties);
    
    /**
     * reads the data out of the divecomputer.
     * @param statusInterface The interface to show progress information to the user.
     * @param logbook The actual opened logbook
     * @throws TransferException if an error occured during transfer of the data from the dive computer.
     * @throws NotInitializedException if initialized has not been called before.
     * @throws InvalidConfigurationException if the configuration has an error, e.g. the specified interface doesn't exist, or the wrong computer is attached. 
     */
    public void transfer(StatusInterface statusInterface, JDiveLog logbook) throws TransferException, NotInitializedException, InvalidConfigurationException;
    
    /**
     * gets the transferred dives.
     * @return a TreeSet containing the JDives. Returns an empty TreeSet if no dives have been transferred. 
     */
    public TreeSet<JDive> getDives();
}
