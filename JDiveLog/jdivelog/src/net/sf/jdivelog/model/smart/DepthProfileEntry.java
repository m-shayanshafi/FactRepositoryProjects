/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DepthProfileEntry.java
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
package net.sf.jdivelog.model.smart;

/**
 * Description: container for one depth profile entry of an Uwatec Smart log file.
 * 
 * @author Andr&eacute; Schenk
 * @version $Revision: 156 $
 */
public class DepthProfileEntry
{
    public final byte alarms;       // bit field
    public final int depth;         // cm
    public final Float temperature; // centigrade
    public final int time;          // s

    public DepthProfileEntry (byte alarms, int depth, Float temperature, int time)
    {
        this.alarms      = alarms;
        this.depth       = depth;
        this.temperature = temperature;
        this.time        = time;
    }
}
