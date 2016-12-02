/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: JDiveLogException.java
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
package net.sf.jdivelog.gui;


/**
 * Generic Exception of JDiveLog
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class JDiveLogException extends RuntimeException {

    private static final long serialVersionUID = 595775789005208107L;
    private final String message;
    private final String details;
    
    /**
     * @param message General Message what has happened, shown in Titlebar and beneath Icon (should already be translated in Users-Language!)
     * @param details Detailed Message, shown a little bit smaller below message (should also already be translated in Users-Language!) 
     * @param cause Exception witch caused this Exception (if any)
     */
    public JDiveLogException(String message, String details, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.details = details;
    }

    /**
     * @return Returns the details.
     */
    public String getDetails() {
        return details;
    }

    /**
     * @return Returns the message.
     */
    public String getMessage() {
        return message;
    }
    
    @Override
    public String toString() {
        return "JDiveLogException: "+message+"\n"+details != null ? details : "";
    }

}
