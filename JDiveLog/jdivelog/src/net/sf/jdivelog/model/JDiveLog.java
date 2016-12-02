/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: JDiveLog.java
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
package net.sf.jdivelog.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;

import net.sf.jdivelog.model.gasblending.GasBlendingSettings;
import net.sf.jdivelog.model.gasoverflow.GasOverflowSettings;
import net.sf.jdivelog.model.udcf.Gas;
import net.sf.jdivelog.util.UnitConverter;

/**
 * Description: main class
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class JDiveLog {

    private ExportSettings exportSettings;

    private StatisticSettings statisticSettings;

    private Masterdata masterdata;

    private String computerDriver = null;

    private Properties computerSettings = null;
    
    private int computerDownloadInterval = 0;

    private TreeSet<JDive> dives = new TreeSet<JDive>();

    private TreeSet<JDive> ignoredDives = new TreeSet<JDive>();

    private SlideshowSettings slideshowSettings = null;

    /** Settings for Gas Blending */
    private GasBlendingSettings gasBlendingSettings = null;
    private GasOverflowSettings gasOverflowSettings = null;

    private ProfileSettings profileSettings;

    public void addDive(JDive dive) {
        dives.add(dive);
    }

    public void removeDive(JDive dive) {
        dives.remove(dive);
    }

    public void addIgnoredDive(JDive dive) {
        ignoredDives.add(dive);
    }

    public void removeIgnoredDive(JDive dive) {
        ignoredDives.remove(dive);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<JDiveLog>");
        sb.append("<Version>2.13</Version>");
        if (exportSettings != null) {
            sb.append(exportSettings.toString());
        }
        sb.append(getStatisticSettings().toString());
        if (masterdata != null) {
            sb.append(masterdata.toString());
        }
        if (computerDriver != null) {
            sb.append("<ComputerDriver>");
            sb.append(computerDriver);
            sb.append("</ComputerDriver>");
        }
        if (computerSettings != null) {
            sb.append("<ComputerSettings>");
            Enumeration<?> propEnum = computerSettings.propertyNames();
            while (propEnum.hasMoreElements()) {
                String propName = (String) propEnum.nextElement();
                String propValue = computerSettings.getProperty(propName);
                sb.append("<property name=\"");
                sb.append(propName);
                sb.append("\" value=\"");
                if (propValue != null) {
                    sb.append(propValue);
                }
                sb.append("\"/>");
            }
            sb.append("</ComputerSettings>");
        }
        sb.append("<ComputerDownloadInterval>");
        sb.append(computerDownloadInterval);
        sb.append("</ComputerDownloadInterval>");
        Iterator<JDive> it = dives.iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
        }
        if (ignoredDives.size() > 0) {
            sb.append("<IgnoredDives>");
            it = ignoredDives.iterator();
            while (it.hasNext()) {
                sb.append(it.next().toString());
            }
            sb.append("</IgnoredDives>");
        }
        sb.append(getSlideshowSettings().toString());
        if (gasBlendingSettings != null) {
            sb.append(gasBlendingSettings);
        }
        if (gasOverflowSettings != null) {
            sb.append(gasOverflowSettings.toString());
        }
        sb.append("\n");
        sb.append(getProfileSettings());
        sb.append("\n");
        sb.append("</JDiveLog>");
        return sb.toString();
    }

    public TreeSet<JDive> getDives() {
        return dives;
    }

    public TreeSet<JDive> getIgnoredDives() {
        return ignoredDives;
    }

    public String getComplete_Divetime() {
        BigDecimal divetime = new BigDecimal(0);
        Integer days = new Integer(0);
        Integer hours = new Integer(0);
        Integer minutes = new Integer(0);
        Iterator<JDive> it = dives.iterator();
        while (it.hasNext()) {
            JDive dive = it.next();
            if (dive.getDuration() != null) {
                UnitConverter c = new UnitConverter(UnitConverter.getSystem(dive.getUnits()), UnitConverter.getDisplaySystem());
                divetime = divetime.add(new BigDecimal(c.convertTime(dive.getDuration())));
            }
        }

        // change the complete divetime in days, hours and minutes
        if (divetime == null || divetime.equals(new BigDecimal(0))) {
            return "00:00:00";
        }
        days = divetime.intValue() / 1440;
        hours = (divetime.intValue() % 1440) / 60;
        minutes = divetime.intValue() % 60;
        return days.toString() + ":" + hours.toString() + ":" + minutes.toString();
    }

    public BigDecimal getAverageDepth() {
        BigDecimal averageDepth = new BigDecimal(0);
        int i = 0;
        Iterator<JDive> it = dives.iterator();
        while (it.hasNext()) {
            JDive dive = it.next();
            if (dive.getDepth() != null) {
                UnitConverter c = new UnitConverter(UnitConverter.getSystem(dive.getUnits()), UnitConverter.getDisplaySystem());
                averageDepth = averageDepth.add(new BigDecimal(c.convertAltitude(dive.getDepth())));
                i++;
            }
        }
        if (averageDepth == null || averageDepth.equals(new BigDecimal(0))) {
            return new BigDecimal(0);
        }
        return i == 0 ? new BigDecimal(0) : averageDepth.divide(new BigDecimal(i), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getAverageAmv() {
        BigDecimal averageAmv = new BigDecimal(0);
        int i = 0;
        Iterator<JDive> it = dives.iterator();
        while (it.hasNext()) {
            JDive dive = it.next();
            if (dive != null && dive.getAMV() != null && dive.getAMV() != 0) {
                UnitConverter c = new UnitConverter(UnitConverter.getSystem(dive.getUnits()), UnitConverter.getDisplaySystem());
                averageAmv = averageAmv.add(new BigDecimal(c.convertAMV(dive.getAMV())));
                i++;
            }
        }
        if (averageAmv == null || averageAmv.equals(new BigDecimal(0))) {
            return new BigDecimal(0);
        }
        return i == 0 ? new BigDecimal(0) : averageAmv.divide(new BigDecimal(i), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getAverageTemperature() {
        BigDecimal averageTemperature = new BigDecimal(0);
        int i = 0;
        Iterator<JDive> it = dives.iterator();
        while (it.hasNext()) {
            JDive dive = it.next();
            if (dive != null && dive.getTemperature() != null && dive.getTemperature() != 0) {
                UnitConverter uc = new UnitConverter(UnitConverter.getSystem(dive.getUnits()), UnitConverter.getDisplaySystem());
                averageTemperature = averageTemperature.add(new BigDecimal(uc.convertTemperature(dive.getTemperature())));
                i++;
            }
        }
        if (averageTemperature == null || averageTemperature.equals(new BigDecimal(0))) {
            return new BigDecimal(0);
        }
        return i == 0 ? new BigDecimal(0) : averageTemperature.divide(new BigDecimal(i), 2, RoundingMode.HALF_UP);
    }

    public void setDives(TreeSet<JDive> dives) {
        this.dives = dives;
    }

    public void setIgnoredDives(TreeSet<JDive> dives) {
        this.ignoredDives = dives;
    }

    public Long getNextDiveNumber() {
        Iterator<JDive> it = dives.iterator();
        long number = 0;
        while (it.hasNext()) {
            JDive dive = it.next();
            if (dive.getDiveNumber().longValue() > number) {
                number = dive.getDiveNumber().longValue();
            }
        }
        return new Long(number + 1);
    }

    public ExportSettings getExportSettings() {
        if (exportSettings == null) {
            exportSettings = new ExportSettings();
        }
        return exportSettings;
    }

    public void setExportSettings(ExportSettings exportSettings) {
        this.exportSettings = exportSettings;
    }

    public ProfileSettings getProfileSettings() {
        if (profileSettings == null) {
            profileSettings = new ProfileSettings();
        }
        return profileSettings;
    }

    public void setProfileSettings(ProfileSettings profileSettings) {
        this.profileSettings = profileSettings;
    }

    public Masterdata getMasterdata() {
        if (masterdata == null) {
            masterdata = new Masterdata();
        }
        return masterdata;
    }

    public void setMasterdata(Masterdata masterdata) {
        this.masterdata = masterdata;
    }

    public String getComputerDriver() {
        return computerDriver;
    }

    public void setComputerDriver(String computerDriver) {
        this.computerDriver = computerDriver;
    }

    public Properties getComputerSettings() {
        return computerSettings;
    }

    public void setComputerSettings(Properties computerSettings) {
        this.computerSettings = computerSettings;
    }

    public void addComputerProperty(String name, String value) {
        if (getComputerSettings() == null) {
            setComputerSettings(new Properties());
        }
        getComputerSettings().setProperty(name, value);
    }

    public StatisticSettings getStatisticSettings() {
        if (statisticSettings == null) {
            statisticSettings = new StatisticSettings();
        }
        return statisticSettings;
    }

    public void setStatisticSettings(StatisticSettings statisticSettings) {
        this.statisticSettings = statisticSettings;
    }

    public SlideshowSettings getSlideshowSettings() {
        if (slideshowSettings == null) {
            slideshowSettings = new SlideshowSettings();
        }
        return slideshowSettings;
    }

    public void setSlideshowSettings(SlideshowSettings slideshowSettings) {
        this.slideshowSettings = slideshowSettings;
    }

    /**
     * @return Returns the gasBlendingSettings.
     */
    public GasBlendingSettings getGasBlendingSettings() {
        return gasBlendingSettings;
    }

    /**
     * @param gasBlendingSettings
     *            The gasBlendingSettings to set.
     */
    public void setGasBlendingSettings(GasBlendingSettings gasBlendingSettings) {
        this.gasBlendingSettings = gasBlendingSettings;
    }

    /**
     * @return Returns the gasOverflowSettings.
     */
    public GasOverflowSettings getGasOverflowSettings() {
        return gasOverflowSettings;
    }

    /**
     * @param gasOverflowSettings
     *            The gasOverflowSettings to set.
     */
    public void setGasOverflowSettings(GasOverflowSettings gasOverflowSettings) {
        this.gasOverflowSettings = gasOverflowSettings;
    }
    
    /**
     * @return the last (latest) dive in the logbook.
     */
    public JDive getLastDive() {
        if (getDives().isEmpty()) {
            return null;
        }
        return getDives().last();
    }

    public void assemblyDives(TreeSet<JDive> selectedDives) {

        Iterator<JDive> it = selectedDives.iterator();
        // save the first dive for assembly
        JDive firstDive = it.next();
        while (it.hasNext()) {
            JDive dive = it.next();

            // set the lowest temperature
            if (firstDive.getTemperature() != null && dive.getTemperature() != null && (firstDive.getTemperature() > dive.getTemperature())) {
                firstDive.setTemperature(dive.getTemperature());
            }
            // set the lowest surface temperature
            if (firstDive.getSurfaceTemperature() != null && dive.getSurfaceTemperature() != null
                    && (firstDive.getSurfaceTemperature() > dive.getSurfaceTemperature())) {
                firstDive.setSurfaceTemperature(dive.getSurfaceTemperature());
            }

            // add a gas switch
            if (firstDive.getDive().getGases() != null && dive.getDive().getGases() != null) {
                // assembly the gases
                for (Gas g : dive.getDive().getGases()) {
                    boolean found = false;
                    for (Gas g2 : firstDive.getDive().getGases()) {
                        if (g2.getName().equals(g.getName())) {
                            found = true;
                        }
                    }
                    if (!found) {
                        firstDive.getDive().getGases().add(g);
                    }
                }
            }

            // assembly the profiles
            if (firstDive.getDive().getSamples() != null && dive.getDive().getSamples() != null) {
                dive.getDive().addSampleTimeOffset(firstDive.getDive().getDuration());
                firstDive.getDive().getSamples().addAll(dive.getDive().getSamples());
            }

            // set the new average depth
            firstDive.setAverageDepth(firstDive.getDive().getAverageDepth());

            // set the new duration
            firstDive.setDuration(firstDive.getDive().getDuration());

            // add the tanks
            if (firstDive.getEquipment().getTanks() != null && dive.getEquipment().getTanks() != null) {
                firstDive.getEquipment().getTanks().addAll(dive.getEquipment().getTanks());
            }

            // add the pictures
            if (firstDive.getPictures() != null && dive.getPictures() != null) {
                firstDive.getPictures().addAll(dive.getPictures());
            }

            // assembly the comments
            if (firstDive.getComment() != null && dive.getComment() != null) {
                firstDive.setComment(firstDive.getComment() + " " + dive.getComment());
            }

            // set the new amv
            firstDive.setAMV("");
            firstDive.setAMV(firstDive.getAMV());

            // remove this dive after assembly
            this.getDives().remove(dive);
        }

    }

    public void setComputerDownloadInterval(int downloadInterval) {
        this.computerDownloadInterval = downloadInterval;
    }

    public int getComputerDownloadInterval() {
        return computerDownloadInterval;
    }

}
