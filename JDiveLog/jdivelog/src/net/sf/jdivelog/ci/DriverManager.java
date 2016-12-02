/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DriverManager.java
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

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Description : driver manager class
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 *
 */
public class DriverManager {
    
    private static final Logger LOGGER = Logger.getLogger(DriverManager.class.getName());
    private static String[] DRIVERCLASSES = {"net.sf.jdivelog.ci.SuuntoInterface", "net.sf.jdivelog.ci.AladinInterface", "net.sf.jdivelog.ci.OSTCInterface", "net.sf.jdivelog.ci.SmartInterface", "net.sf.jdivelog.ci.DR5UddfInterface"};
    private static DriverManager instance;
    
    private HashMap<String, ComputerInterface> drivers;
    
    static {
        instance = new DriverManager();
    }

    /**
     * Create an instance of the DriverManager class
     * @return DriverManager instance
     */
    public static DriverManager getInstance() {
        return instance;
    }
    
    
    /**
     * Constructor
     *
     */
    private DriverManager() {
        drivers = new HashMap<String, ComputerInterface>();
        for (int i=0; i<DRIVERCLASSES.length; i++) {
            addDriver(DRIVERCLASSES[i]);
        }
    }
    
    /**
     * Get computer driver names
     * @return the names of the driver
     */
    public Set<String> getDriverNames() {
        return drivers.keySet();
    }
    
    
    /**
     * Get the instance of the driver
     * @param name
     * @return the driver name
     */
    public ComputerInterface getInterface(String name) {
        return drivers.get(name);
    }

    /**
     * Add a driver
     * @param className
     */
    private void addDriver(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (ComputerInterface.class.isAssignableFrom(clazz)) {
                ComputerInterface ci = (ComputerInterface)clazz.newInstance();
                drivers.put(ci.getDriverName(), ci);
            }
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, "Could not instantiate driver '"+className+"'", t);
        }
    }

}
