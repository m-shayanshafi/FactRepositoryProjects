/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: CressiFileLoader.java
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
package net.sf.jdivelog.model.cressi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.gui.commands.CommandAddDives;
import net.sf.jdivelog.gui.commands.CommandManager;
import net.sf.jdivelog.model.Equipment;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.Tank;
import net.sf.jdivelog.model.udcf.Delta;
import net.sf.jdivelog.model.udcf.Dive;
import net.sf.jdivelog.model.udcf.Gas;

/**
 * Description: Loader for Cressi PC-Logbook exported files (CSV)
 *
 * @author Uri Kogan <urkheh@gmail.com>
 *
 * Thanks for help and updates to Adam Gorecki
 */

public class CressiFileLoader {

    private static final String FIELD_PRODUCT = "Product";

    private static final String FIELD_DIVE_NUMBER = "Dive No.";

    private static final String FIELD_DATE = "Date";

    private static final String FIELD_ALTITUDE_RANK = "Altitude Rank";

    private static final String FIELD_PGT = "PGT";

    private static final String FIELD_OLI = "OLI";

    private static final String FIELD_MAX_DEPTH = "Max. Depth";

    private static final String FIELD_AVG_DEPTH = "Ave. Depth";

    private static final String FIELD_TEMPERATURE = "Temparature";

    private static final String FIELD_DIVE_TIME = "Dive Tim";

    private static final String FIELD_ENTRY_TIME = "Entry";

    private static final String FIELD_EXIT_TIME = "Exit";

    private static final String FIELD_SURFACE_INTERVAL = "Surface Interval";

    private static final String FIELD_SAFETY_FACTOR = "Safety Factor";

    private static final String FIELD_SAMPLING = "Sampling";

    private static final String FIELD_FO2 = "FO2(%)";

    private static final String FIELD_FO2_MIX1 = "FO2Mix1(%)";

    private static final String FIELD_FO2_MIX2 = "FO2Mix2(%)";

    private static final String FIELD_DECO_DIVE = "DECO Dive";

    private static final String FIELD_DECO_STOP_VIOLATION =
        "DECO Stop Violation";

    private static final String FIELD_PO2_WARNING = "PO2 Warning";

    private static final String FIELD_OLI_WARNING = "OLI Warning";

    private static final String FIELD_OVER_RANGE_WARNING =
        "Over Range Warning";

    private static final String FIELD_ASCENT_RATE_WARNING =
        "Ascent Rate Warning";

    private static final String FIELD_PROFILE = "Profile ";

    /** system independent lines separator */
    private static final String NEWLINE_SEPARATOR =
        System.getProperty("line.separator");

    /** value set by PC-Logbook to mark values not filled in */
    private static final String VALUE_EMPTY = "- ";

    /** value used to split lines */
    private final String SPLIT_VALUE = ",";

    /** reader of input file */
    BufferedReader reader = null;

    /** parent window of this importer */
    MainWindow wnd = null;

    /** Reader initialization
     * @param mainWindow parent window of this loader
     * @param file file selected for loading
     * @throws FileNotFoundException if the file specified does not exist */
    public CressiFileLoader(MainWindow mainWindow, File file)
    throws FileNotFoundException
    {
        if (!file.exists())
        {
            throw new FileNotFoundException();
        }

        reader = new BufferedReader(new FileReader(file));

        wnd = mainWindow;
    }

    /** load data file
     * @throws IOException in case of any read errors
     * @throws IllegalArgumentException if file format is invalid */
    public void Load() throws IOException
    {
        /* single analyzed dive */
        ArrayList<JDive> dives = null;

        /* single line of CSV file */
        String line = null;

        /* single line split into values (comma separated) */
        String[] split = null;

        /* number of dives appearing in input file */
        int diveCount = 0;

        /* number of lines in the file */
        int lineCount = 0;

        /* read all the lines one by one */
        while ((line = reader.readLine()) != null)
        {
            /* split input line */
            split = line.split(SPLIT_VALUE, -1);

            lineCount++;

            /* create new dives array, ensure all the lines in CSV file    */
            /* have the same number of fields                              */
            if (diveCount == 0)
            {
                diveCount = split.length - 1;
                dives = new ArrayList<JDive>(diveCount);
                for (int i = 0; i < diveCount; i++)
                {
                    /* new dive */
                    JDive newDive = new JDive();

                    /* new dive information */
                    Dive diveInfo = new Dive();

                    newDive.setDive(diveInfo);
                    newDive.setComment("");
                    dives.add(newDive);
                }
            }
            else if (diveCount != (split.length - 1))
            {
                throw new IllegalArgumentException(
                    "Invalid file format: number of fileds changed at line" +
                    lineCount);

            }

            /* go over all the fields of current line */
            for (int i = 0; i < diveCount; i++)
            {
                try {
                    setField(dives.get(i), split[0], split[i + 1]);
                }
                catch (Exception ex) {
                    throw new IllegalArgumentException("cannot parse field " +
                            split[0] + "(value: " + split[i + 1] + "): " +
                            ex.getClass().toString() + " (" + ex.getMessage() +
                            ")");

                }
            }
        }

        /* add all the created dives */
        CommandAddDives cmd = new CommandAddDives(wnd, dives);
        CommandManager.getInstance().execute(cmd);
    }

    /** sets single field of specified dive according to field name
     *
     * @param dive dive to be modified
     * @param fieldName name of the field to modify
     * @param value value of the field
     * @throws ParseException if date could not have been parsed
     */
    private void setField(JDive dive, String fieldName, String value)
    throws ParseException {
        /* input sanity check do not process fields with empty name or value */
        if (fieldName == null || fieldName.length() <= 0 ||
                value == null || value.length() <= 0 ||
                value.equals(VALUE_EMPTY))
        {
            return;
        }

        if (fieldName.startsWith(FIELD_PRODUCT))
        {
            dive.setComment(dive.getComment() + "Product: " + value +
                    NEWLINE_SEPARATOR);
        }
        else if (fieldName.startsWith(FIELD_DIVE_NUMBER))
        {
            dive.setDiveNumber(value);
        }
        else if (fieldName.startsWith(FIELD_DATE))
        {
            DateFormat df = new SimpleDateFormat("MM/dd/yy");
            Date dt = null;
            try
            {
                dt = df.parse(value);
            }
            catch (ParseException e)
            {
                dt = new SimpleDateFormat("dd/MM/yy").parse(value);
            }
            dive.setDate(dt);
            dive.getDive().setDate(dt);
        }
        else if (fieldName.startsWith(FIELD_ALTITUDE_RANK))
        {
            dive.getDive().setAltitude(value);
            dive.setComment(dive.getComment() + "Altitude Rank: " + value +
                    NEWLINE_SEPARATOR);
        }
        else if (fieldName.startsWith(FIELD_PGT))
        {
            dive.setComment(dive.getComment() + "PGT: " + value +
                    NEWLINE_SEPARATOR);
        }
        else if (fieldName.startsWith(FIELD_OLI))
        {
            dive.setComment(dive.getComment() + "OLI: " + value +
                    NEWLINE_SEPARATOR);
        }
        else if (fieldName.startsWith(FIELD_MAX_DEPTH))
        {
            dive.setDepth(value);
        }
        else if (fieldName.startsWith(FIELD_AVG_DEPTH))
        {
            dive.setAverageDepth(value);
        }
        else if (fieldName.startsWith(FIELD_TEMPERATURE))
        {
            dive.setTemperature(value);
        }
        else if (fieldName.startsWith(FIELD_DIVE_TIME))
        {
            dive.setDuration(value);
        }
        else if (fieldName.startsWith(FIELD_ENTRY_TIME))
        {
            if (value != null && value.length() > 0)
            {
                String[] sTime = value.split(":");
                if (sTime.length > 1)
                {
                    dive.setTime(sTime[0], sTime[1]);
                }
            }
        }
        else if (fieldName.startsWith(FIELD_EXIT_TIME))
        {
            dive.setComment(dive.getComment() + "Exit time: " + value +
                    NEWLINE_SEPARATOR);
        }
        else if (fieldName.startsWith(FIELD_SURFACE_INTERVAL))
        {
            dive.getDive().setSurfaceinterval(value);
            dive.setComment(dive.getComment() + "Surface interval: " + value +
                    NEWLINE_SEPARATOR);
        }
        else if (fieldName.startsWith(FIELD_SAFETY_FACTOR))
        {
            dive.setComment(dive.getComment() + "Safety factor: " + value +
                    NEWLINE_SEPARATOR);
        }
        else if (fieldName.startsWith(FIELD_SAMPLING))
        {
            Delta delta = new Delta();
            delta.setValue(Double.parseDouble(value) / 60);
            dive.getDive().addSample(delta);
            dive.setComment(dive.getComment() + "Sampling time: " + value +
                    NEWLINE_SEPARATOR);
        }
        else if (fieldName.startsWith(FIELD_FO2_MIX1) ||
                fieldName.startsWith(FIELD_FO2_MIX2) ||
                fieldName.startsWith(FIELD_FO2))
        {
            addMix(dive, Double.parseDouble(value));
        }
        else if (fieldName.startsWith(FIELD_DECO_DIVE))
        {
            dive.getDive().addAlarm("Deco dive");
            dive.setComment(dive.getComment() + "Deco dive: " + value +
                    NEWLINE_SEPARATOR);
        }
        else if (fieldName.startsWith(FIELD_DECO_STOP_VIOLATION))
        {
            dive.getDive().addAlarm("Deco stop violation");
            dive.setComment(dive.getComment() + "Deco stop violation: " +
                    value + NEWLINE_SEPARATOR);
        }
        else if (fieldName.startsWith(FIELD_PO2_WARNING))
        {
            dive.getDive().addAlarm("PO2");
            dive.setComment(dive.getComment() +
                    "Partial oxygen pressure warning: " + value +
                    NEWLINE_SEPARATOR);
        }
        else if (fieldName.startsWith(FIELD_OLI_WARNING))
        {
            dive.getDive().addAlarm("OLI");
            dive.setComment(dive.getComment() + "OLI warning: " + value +
                    NEWLINE_SEPARATOR);
        }
        else if (fieldName.startsWith(FIELD_OVER_RANGE_WARNING))
        {
            dive.getDive().addAlarm("Over range");
            dive.setComment(dive.getComment() + "Over range warning: " +
                    value + NEWLINE_SEPARATOR);
        }
        else if (fieldName.startsWith(FIELD_ASCENT_RATE_WARNING))
        {
            dive.getDive().addAlarm("Ascent rate");
            dive.setComment(dive.getComment() + "Ascent rate warning: " +
                    value + NEWLINE_SEPARATOR);
        }
        else if (fieldName.startsWith(FIELD_PROFILE))
        {
            if (value != null && value.length() > 0)
            {
                dive.getDive().addDepth(value);
            }
        }
        else
        {
            throw new IllegalArgumentException(
                    "Invalid file format: invalid field: " + fieldName);
        }
    }

    /**
     * Adds single mix to the dive
     * @param dive dive to add the mix to
     * @param oxy oxygen percentage in range [0..1]
     */
    private void addMix(JDive dive, double oxy) {
        /* list of equipment of this dive */
        Equipment eq = dive.getEquipment();

        /* new tank with a mix */
        Tank tank = new Tank();

        /* gas of new tank */
        Gas gas = new Gas();

        if (oxy == 21)
        {
            gas.setName("Air");
        }
        else
        {
            gas.setName("EAN" + oxy);
        }
        gas.setOxygen(oxy);
        gas.setHelium(0.0);
        gas.setNitrogen(1 - oxy);
        gas.setTankvolume(12.0 / 1000);
        tank.setGas(gas);

        dive.getDive().addGas(gas);
        dive.getDive().addSwitch(gas.getName());

        if (eq == null)
        {
            eq = new Equipment();
            dive.setEquipment(eq);
        }

        eq.addTank(tank);
    }
}
