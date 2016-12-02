/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: SmartAdapter.java
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
package net.sf.jdivelog.model;

import java.util.*;

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.smart.*;
import net.sf.jdivelog.model.udcf.*;

/**
 * Description: adapts the Uwatec Smart data structure to JDiveLog format
 *
 * @author Andr&eacute; Schenk
 * @version $Revision: 304 $
 */
public class SmartAdapter extends TreeSet <JDive>
{
    private static final long serialVersionUID = -4083942145998285268L;

    public SmartAdapter (SmartData aladinData)
    {
        if (aladinData == null) {
            throw new IllegalArgumentException ("parameter aladinData is null");
        }

        List<LogEntry> logbook = aladinData.getLogbook ();

        for (int index = 0; index < logbook.size (); index++) {
            LogEntry logEntry = logbook.get (index);
            JDive dive = new JDive ();

            dive.setDate (logEntry.diveStartTime);
            dive.setDepth (logEntry.maximumDepth.toString ());
            dive.setDiveNumber (new Long (index + 1));
            dive.setDuration (new Double (logEntry.duration));
            dive.setSurfaceTemperature (logEntry.airTemperature.toString ());
            dive.setTemperature (logEntry.minimumTemperature.toString ());

            Gas gas = getGas (0);
            Tank tank = new Tank();
            Equipment equipment = new Equipment();

            tank.setGas (gas);
            equipment.addTank (tank);
            dive.setEquipment (equipment);

            Dive depthProfile = convertDepthProfile (logEntry.profile);

            depthProfile.addGas (gas);
            depthProfile.setDate (logEntry.diveStartTime);
            depthProfile.setSurfaceinterval
                (String.valueOf (logEntry.surfaceInterval));

            dive.setAverageDepth (depthProfile.getAverageDepth ());
            dive.setDate (logEntry.diveStartTime);
            dive.setDive (depthProfile);

            add (dive);
        }
    }

    private void addAlarms (Dive dive, int warnings)
    {
        if (dive != null) {
            if ((warnings & 1) > 0) {
                dive.addAlarm ("Yellow");
            }
            if ((warnings & 2) > 0) {
                dive.addAlarm ("Red");
            }
            if ((warnings & 4) > 0) {
                dive.addAlarm (Messages.getString ("workalarm"));
            }
            if ((warnings & 8) > 0) {
                dive.addAlarm ("unknown");
            }
            if ((warnings & 16) > 0) {
                dive.addAlarm ("unknown");
            }
            if ((warnings & 32) > 0) {
                dive.addAlarm (Messages.getString ("rbtalarm"));
            }
            if ((warnings & 64) > 0) {
                dive.addAlarm (Messages.getString ("decostop"));
            }
        }
    }

    private Dive convertDepthProfile (List <DepthProfileEntry> profileEntries)
    {
        Dive result = new Dive ();

        result.setSurfaceinterval ("");
        result.setDensity (new Double (0));
        result.setAltitude (new Double (0));
        result.addDepth ("0");
        result.setTemperature ("0");
        result.addTime ("0");

        for (int entryIndex = 0; entryIndex < profileEntries.size ();
             entryIndex++) {
            final DepthProfileEntry profileEntry =
                profileEntries.get (entryIndex);

            addAlarms (result, profileEntry.alarms);
            result.addDepth (new Double (new Double (profileEntry.depth) / 100).toString ());
            result.addTemperature (new Double (profileEntry.temperature).toString ());
            result.addTime (convertTime (profileEntry.time));
        }

        //set the last depth to 0 meters for the profile
        result.addDepth ("0");
        result.addTemperature (new Double (profileEntries.get (profileEntries.size () - 1).temperature).toString ());
        result.addTime (convertTime (profileEntries.get (profileEntries.size () - 1).time + 4));
        result.setTimeDepthMode ();
        return result;
    }

    private String convertTime (int seconds)
    {
        return new Double (new Double (seconds) / 60).toString ();
    }

    private Gas getGas (int airConsumption)
    {
        Gas result = new Gas ();

        result.setName (Messages.getString ("default_mixname"));
        result.setOxygen (new Double (0.21));
        result.setNitrogen (new Double (0.79));
        result.setHelium (new Double (0));
        result.setPstart
            (new Double (Messages.getString ("standard_pressure")));
        result.setPend (result.getPstart () - airConsumption);
        return result;
    }
}
