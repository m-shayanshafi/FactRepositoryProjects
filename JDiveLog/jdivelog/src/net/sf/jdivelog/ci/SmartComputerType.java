/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: SmartComputerType.java
 * 
 * @author Andr&eacute; Schenk <andre_schenk@users.sourceforge.net>
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
 * Description: Enumeration for Aladin Smart computer models.
 * 
 * @author Andr&eacute; Schenk
 * @version $Revision: 548 $
 */
public enum SmartComputerType
{
    SMART_PRO     (16, "SmartPro"),
    ALADIN_TEC    (18, "Aladin Tec, Prime"),
    ALADIN_TEC_2G (19, "Aladin TEC 2G"),
    SMART_COM     (20, "SmartCom"),
    SMART_TEC     (24, "SmartTec"),
    SMART_Z       (28, "SmartZ");

    private final int identifier;
    private final String label;

    SmartComputerType (int identifier, String label)
    {
        this.identifier = identifier;
        this.label      = label;
    }

    public int getIdentifier ()
    {
        return identifier;
    }

    public String toString ()
    {
    	return label;
    }
}
