/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: AladinAdapter.java
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
import net.sf.jdivelog.model.aladin.*;
import net.sf.jdivelog.model.udcf.*;

/**
 * Description: adapts the Aladin data structure to JDiveLog format
 * 
 * @author Andr&eacute; Schenk
 * @version $Revision: 823 $
 */
public class AladinAdapter extends TreeSet<JDive>
{
    private static final long serialVersionUID = 984168960354523589L;

    private int diveTime = 0;
    private boolean deco_stop = false;
    private boolean rbt_alarm = false;

    public AladinAdapter (AladinData aladinData)
    {
        if (aladinData == null) {
            throw new IllegalArgumentException
                ("parameter aladinData is null");
        }

        List<Dive> depthProfiles =
            convertDepthProfiles (aladinData.getDepthProfiles ());
        List<LogEntry> logbook = aladinData.getLogbook ();

        for (int index = 0; index < logbook.size (); index++) {
            LogEntry logEntry = logbook.get (index);
            JDive dive = new JDive ();

            dive.setDate (logEntry.entryTime);
            dive.setDepth (logEntry.maximumDepth.toString ());
            dive.setDiveNumber (new Long (index + 1));
            dive.setDuration (new Double (logEntry.bottomTime));
            dive.setTemperature (logEntry.waterTemperature.toString ());
            dive.setUnits (((aladinData.getSettings ().settings & 1) != 0) ?
                           "imperial" : "metric");

            Gas gas = getGas (logEntry.airConsumption);
            Tank tank = new Tank();
            Equipment equipment = new Equipment();

            tank.setGas (gas);
            equipment.addTank (tank);
            dive.setEquipment (equipment);

            if (logbook.size () - index <= depthProfiles.size ()) {
                Dive depthProfile = depthProfiles.get
                    (depthProfiles.size () + index - logbook.size ());

                depthProfile.addGas (gas);
                depthProfile.setDate (logEntry.entryTime);
                depthProfile.setSurfaceinterval
                    (String.valueOf (logEntry.surfaceTime));
                dive.setDate (logEntry.entryTime);
                dive.setDive (depthProfile);
                dive.setAverageDepth (depthProfile.getAverageDepth ());
            }
            add (dive);
        }
    }

    private void addAlarms (Dive dive, int warnings)
    {
        if (dive != null) {
            if ((warnings & 16) > 0) {
                dive.addAlarm (Messages.getString ("workalarm"));
            }
            if ((warnings & 8) > 0) {
                dive.addAlarm (Messages.getString ("decoceilingalarm"));
            }
            if ((warnings & 4) > 0) {
                dive.addAlarm (Messages.getString ("ascentalarm"));
            }
            if ((warnings & 2) > 0 && !rbt_alarm) {
                dive.addAlarm (Messages.getString ("rbtalarm"));
                rbt_alarm = true;
            }
            if ((warnings & 1) > 0 && !deco_stop) {
                dive.addAlarm (Messages.getString ("decostop"));
                deco_stop = true;
            }
        }
    }

    private List<Dive> convertDepthProfiles (List<DepthProfile> profiles)
    {
        List<Dive> result = new ArrayList<Dive> ();

        for (int index = 0; index < profiles.size (); index++) {
            final DepthProfile profile = profiles.get (index);
            Dive dive = new Dive ();

            resetDiveTime ();
            dive.setSurfaceinterval ("");
            dive.setDensity (new Double (0));
            dive.setAltitude (new Double (0));
            dive.setTemperature (new Double (profile.startTemperature));
            dive.addTime (getNextDiveTime ());
            dive.addDepth ("0");

            List<DepthProfileEntry> profileEntries =
                profile.getProfileEntries ();

            for (int entryIndex = 0; entryIndex < profileEntries.size ();
                 entryIndex++) {
                final DepthProfileEntry profileEntry =
                    profileEntries.get (entryIndex);

                addAlarms (dive, profileEntry.warnings20);
                dive.addDepth (profileEntry.depth20.toString ());
                dive.addTime (getNextDiveTime ());

                addAlarms (dive, profileEntry.warnings40);
                dive.addDepth (profileEntry.depth40.toString ());
                dive.addTime (getNextDiveTime ());

                addAlarms (dive, profileEntry.warnings00);
                dive.addDepth (profileEntry.depth00.toString ());
                dive.addTime (getNextDiveTime ());
            }

            //set the last depth to 0 meters for the profile
            dive.addDepth (new Double (0).toString ());
            dive.addTime (getNextDiveTime ());

            dive.setTimeDepthMode ();
            result.add (dive);
        }
        return result;
    }

    public static Gas getGas (int airConsumption)
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

    private String getNextDiveTime ()
    {
        String result = new Double (diveTime / 60.0).toString ();

        diveTime += 20;
        return result;
    }

    private void resetDiveTime ()
    {
        diveTime = 0;
    }
}
