/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: InvalidConfigurationException.java
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

/**
 * Description: Exception raised when the driver configuration is incorrect
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class InvalidConfigurationException extends Exception {

    private static final long serialVersionUID = 3977583610499249969L;

    private String message;

    /**
     * Constructor
     * @param message
     */
    public InvalidConfigurationException(String message) {
        this.message = message;
    }
    
    /**
     * Return the message of the exception
     * @return message exception
     */
    public String getMessage() {
        return message;
    }

}
