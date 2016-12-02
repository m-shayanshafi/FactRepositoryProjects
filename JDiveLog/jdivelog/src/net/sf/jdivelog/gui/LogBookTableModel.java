/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: LogBookTableModel.java
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

import java.awt.Image;
import java.awt.Toolkit;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;

import net.sf.jdivelog.gui.LogbookChangeEvent.EventType;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.gui.util.SortableTableModel;
import net.sf.jdivelog.gui.util.TableSorter;
import net.sf.jdivelog.model.DiveSite;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.JDiveLog;
import net.sf.jdivelog.model.Picture;
import net.sf.jdivelog.model.Tank;
import net.sf.jdivelog.util.DateFormatUtil;
import net.sf.jdivelog.util.UnitConverter;

/**
 * Description: Return a table with the dives
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class LogBookTableModel extends TableSorter implements SortableTableModel, LogbookChangeListener {

    private static final long serialVersionUID = 3834306220185170488L;

    private static final DateFormat TIMEFORMAT = new SimpleDateFormat(Messages.getString("timeformat")); //$NON-NLS-1$

    private static final NumberFormat DECIMALFORMAT = new DecimalFormat(Messages.getString("numberformat")); //$NON-NLS-1$
    
    private static Image PROFILE_ICON = Toolkit.getDefaultToolkit().getImage(LogBookTableModel.class.getResource("/net/sf/jdivelog/gui/resources/icons/profile.gif"));
    private static Image PHOTO_ICON = Toolkit.getDefaultToolkit().getImage(LogBookTableModel.class.getResource("/net/sf/jdivelog/gui/resources/icons/cam.gif"));

    private String[] colnames = { Messages.getString("dive_no"), Messages.getString("date"), Messages.getString("place"), Messages.getString("city"), Messages.getString("country"), Messages.getString("depth") + UnitConverter.getDisplayAltitudeUnit() + "]", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
            Messages.getString("duration") + " ["+UnitConverter.getDisplayTimeUnit() + "]", "", "" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

    private MainWindow mainWindow;
    private JDiveLog logbook;
    private TreeSet<JDive> dives;
    private int columnCount;
    private String searchString_new = null;
    private String searchString_old = "";

    public LogBookTableModel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.mainWindow.getLogbookChangeNotifier().addLogbookChangeListener(this);
        columnCount = 9;
    }
    
    public LogBookTableModel(JDiveLog logbook, TreeSet<JDive> dives) {
        this.logbook = logbook;
        this.dives = dives;
        columnCount = 7;
    }
    
    public int getColumnCount() {
        return columnCount;
    }

    public int getRowCount() {
        return getDives().size();
    }

    
    public void setSearchString(String searchString) {        
        DecimalFormat format = new DecimalFormat("######0.##");
        searchString_old = searchString_new;
        if (searchString.equals("")) {
            searchString_new = null;
        } else {
            searchString_new = searchString;
        }        
        getDives();
        mainWindow.getAverage_amvField().setText(format.format(getAverageAmv()));
        mainWindow.getAverage_temperaturField().setText(format.format(getAverageTemperature()));
        mainWindow.getCount_divesField().setText(new Integer(dives.size()).toString());
        mainWindow.getComplete_divetimeField().setText(getComplete_Divetime());
        mainWindow.getAverage_depthField().setText(format.format(getAverageDepth()));
    }
            
    public Object getValueAt(int rowIndex, int columnIndex) {
        JDive dive = (JDive)dives.toArray()[rowIndex];
        DiveSite site;
        UnitConverter c = new UnitConverter(UnitConverter.getSystem(dive.getUnits()), UnitConverter.getDisplaySystem());
        switch (columnIndex) {
        case 0:
            return dive.getDiveNumber();
        case 1:
            return dive.getDate() == null ? null : DateFormatUtil.getDateTimeFormat().format(dive.getDate());
        case 2:
            site = getLogbook().getMasterdata().getDiveSite(dive);
            if (site != null) {
                return site.getSpot();
            }
            return "";
        case 3:
            site = getLogbook().getMasterdata().getDiveSite(dive);
            if (site != null) {
                return site.getCity();
            }
            return "";
        case 4:
            site = getLogbook().getMasterdata().getDiveSite(dive);
            if (site != null) {
                return site.getCountry();
            }
            return "";
        case 5:
            return dive.getDepth() == null ? null : DECIMALFORMAT.format(c.convertAltitude(dive.getDepth()));
        case 6:
            return dive.getDuration() == null ? null : DECIMALFORMAT.format(c.convertTime(dive.getDuration()));
        case 7:
            return dive.getDive() == null ? null : PROFILE_ICON;
        case 8:
            return dive.getPictures() == null || dive.getPictures().size() == 0 ? null : PHOTO_ICON;
        }
        return null;
    }
    
    public Object getUnformattedValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < dives.size() && rowIndex >= 0) {
            JDive dive = (JDive)dives.toArray()[rowIndex];
            DiveSite site;
            UnitConverter c = new UnitConverter(UnitConverter.getSystem(dive.getUnits()), UnitConverter.getDisplaySystem());
            switch (columnIndex) {
            case 0:
                return dive.getDiveNumber();
            case 1:
                return dive.getDate();
            case 2:
                site = getLogbook().getMasterdata().getDiveSite(dive);
                if (site != null) {
                    return site.getSpot();
                }
                return "";
            case 3:
                site = getLogbook().getMasterdata().getDiveSite(dive);
                if (site != null) {
                    return site.getCity();
                }
                return "";
            case 4:
                site = getLogbook().getMasterdata().getDiveSite(dive);
                if (site != null) {
                    return site.getCountry();
                }
                return "";
            case 5:
                return c.convertAltitude(dive.getDepth());
            case 6:
                return c.convertTime(dive.getDuration());
            case 7:
                return dive.getDive() == null ? null : PROFILE_ICON;
            case 8:
                return dive.getPictures() == null || dive.getPictures().size() == 0 ? null : PHOTO_ICON;
            }
        }
        return null;
    }
    
    private String extractString(JDive dive) {
        UnitConverter c = new UnitConverter(UnitConverter.getSystem(dive.getUnits()), UnitConverter.getDisplaySystem());
        DiveSite site = mainWindow.getLogBook().getMasterdata().getDiveSiteByPrivateId(dive.getDiveSiteId());
        StringBuffer sb = new StringBuffer();
        sb.append(c.convertAMV(dive.getAMV()));
        sb.append(" $ ");
        sb.append(c.convertAltitude(dive.getAverageDepth()));
        sb.append(" $ ");
        sb.append(dive.getBuddy());
        sb.append(" $ ");
        sb.append(dive.getComment());
        sb.append(" $ ");
        sb.append(DateFormatUtil.getDateFormat().format(dive.getDate()));
        sb.append(" $ ");
        sb.append(TIMEFORMAT.format(dive.getDate()));
        sb.append(" $ ");
        sb.append(c.convertAltitude(dive.getDepth()));
        sb.append(" $ ");
        sb.append(dive.getDiveActivity());
        sb.append(" $ ");
        sb.append(dive.getDiveType());
        sb.append(" $ ");
        sb.append(c.convertTime(dive.getDuration()));
        if (dive.getEquipment() != null) {
            sb.append(" $ ");
            sb.append(dive.getEquipment().getComment());
            sb.append(" $ ");
            sb.append(dive.getEquipment().getGloves());
            sb.append(" $ ");
            sb.append(dive.getEquipment().getName());
            sb.append(" $ ");
            sb.append(dive.getEquipment().getSuit());
            Iterator<Tank> tankIt = dive.getEquipment().getTanks().iterator();
            while(tankIt.hasNext()) {
                Tank tank = tankIt.next();
                sb.append(" $ ");
                sb.append(c.convertVolume(tank.getGas().getTankvolume()));
                sb.append(" $ ");
                sb.append(tank.getType());
            }
            sb.append(" $ ");
            sb.append(dive.getEquipment().getWeight());
        }
        if (dive.getPictures() != null) {
            Iterator<Picture> picIt = dive.getPictures().iterator();
            while (picIt.hasNext()) {
                Picture pic = picIt.next();
                sb.append(" $ ");
                sb.append(pic.getName());
                sb.append(" $ ");
                sb.append(pic.getDescription());
            }
        }
        sb.append(" $ ");
        if (site != null) {
            sb.append(site.getCountry());
            sb.append(" $ ");
            sb.append(site.getSpot());
            sb.append(" $ ");
            sb.append(site.getCity());
            sb.append(" $ ");
            sb.append(site.getState());
            sb.append(" $ ");
            sb.append(site.getWaters());
            sb.append(" $ ");
            sb.append(site.getDescription());
            sb.append(" $ ");
            sb.append(site.getDirections());
            sb.append(" $ ");
            sb.append(site.getWarnings());
            sb.append(" $ ");
            sb.append(site.getPrivateRemarks());
            sb.append(" $ ");
        }
        sb.append(c.convertTemperature(dive.getTemperature()));
        sb.append(" $ ");
        sb.append(dive.getVisibility());
        return sb.toString();
    }
    
    private double matchString(String keyword, String value) {
        if (value.contains(keyword.replace('"', ' ').trim())) {
            return 100.0;
        }
        String upperValue = value.toUpperCase();
        if (upperValue.equals(keyword.toUpperCase())) {
            return 99.0;
        }
        ArrayList<String> keywordList = new ArrayList<String>();
        ArrayList<String> mandatoryList = new ArrayList<String>();
        ArrayList<String> forbiddenList = new ArrayList<String>();
        StringBuffer buf = new StringBuffer();
        boolean quoted = false;
        boolean mandatory = false;
        boolean forbidden = false;
        for (int i=0; i<keyword.length(); i++) {
            if (quoted && keyword.charAt(i) == '"') {
                quoted = false;
                String s = buf.toString().trim();
                if (s.length() > 0) {
                    if (mandatory) {
                        mandatoryList.add(s);
                        keywordList.add(s);
                    } else if (forbidden) {
                        forbiddenList.add(s);
                    } else {
                        keywordList.add(s);
                    }
                }
                buf.setLength(0);
                mandatory = false;
                forbidden = false;
            } else if (!quoted && keyword.charAt(i) == '"') {
                quoted = true;
                String s = buf.toString().trim();
                if (s.length() > 0) {
                    mandatory = false;
                    forbidden = false;
                    if (mandatory) {
                        mandatoryList.add(s);
                        keywordList.add(s);
                    } else if (forbidden) {
                        forbiddenList.add(s);
                    } else {
                        keywordList.add(s);
                    }
                }
                buf.setLength(0);
            } else if (!quoted && keyword.charAt(i) == ' ') {
                String s = buf.toString().trim();
                if (s.length() > 0) {
                    if (mandatory) {
                        mandatoryList.add(s);
                        keywordList.add(s);
                    } else if (forbidden) {
                        forbiddenList.add(s);
                    } else {
                        keywordList.add(s);
                    }
                }
                buf.setLength(0);
                mandatory = false;
                forbidden = false;
            } else if (!quoted && keyword.charAt(i) == '+') {
                String s = buf.toString().trim();
                if (s.length() > 0) {
                    if (mandatory) {
                        mandatoryList.add(s);
                        keywordList.add(s);
                    } else if (forbidden) {
                        forbiddenList.add(s);
                    } else {
                        keywordList.add(s);
                    }
                }
                mandatory = true;
                forbidden = false;
                buf.setLength(0);
            } else if (!quoted && keyword.charAt(i) == '-') {
                if (i == 0 || keyword.charAt(i-1) == ' ') {
                    forbidden = true;
                    mandatory = false;
                } else {
                    buf.append(keyword.charAt(i));
                }
            } else {
                buf.append(keyword.charAt(i));
            }
        }
        String s = buf.toString().trim();
        if (s.length() > 0) {
            if (mandatory) {
                mandatoryList.add(s);
                keywordList.add(s);
            } else if (forbidden) {
                forbiddenList.add(s);
            } else {
                keywordList.add(s);
            }
        }
        double caseSensitivePoints = 100.0 / keywordList.size();
        double caseInsensitivePoints = 99.0 / keywordList.size();
        double points = 0.0;
        Iterator<String> forbiddenIt = forbiddenList.iterator();
        while (forbiddenIt.hasNext()) {
            String word = forbiddenIt.next();
            if (upperValue.contains(word.toUpperCase())) {
                return 0;
            }
        }
        Iterator<String> mandatoryIt = mandatoryList.iterator();
        while (mandatoryIt.hasNext()) {
            String word = mandatoryIt.next();
            if (!upperValue.contains(word.toUpperCase())) {
                return 0;
            }
        }
        Iterator<String> wordIt = keywordList.iterator();
        while (wordIt.hasNext()) {
            String word = wordIt.next();
            if (value.contains(word)) {
                points += caseSensitivePoints;
            } else if (upperValue.contains(word.toUpperCase())) {
                points += caseInsensitivePoints;
            }
        }
        return points;
    }
    
    
    
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
        case 0:
            return Long.class;
        case 1:
            return String.class;
        case 2:
            return String.class;
        case 3:
            return String.class;
        case 4:
            return String.class;
        case 5:
            return String.class;
        case 6:
            return String.class;
        }
        return String.class;
    }
    
    public Class<?> getUnformattedColumnClass(int columnIndex) {
        switch (columnIndex) {
        case 0:
            return Long.class;
        case 1:
            return Date.class;
        case 2:
            return String.class;
        case 3:
            return String.class;
        case 4:
            return String.class;
        case 5:
            return Double.class;
        case 6:
            return Double.class;
        }
        return String.class;
    }

    public String[] getColumnNames() {
        return colnames;
    }

    public String getColumnName(int column) {
        return colnames[column];
    }

    /**
     * @param row
     *            The selected row.
     * @return The JDive at the specified row.
     */
    public JDive getDive(int row) {
        return (JDive) dives.toArray()[row];
    }
    
    /**
     * @param row
     *            The selected row.
     * @return The JDive at the specified row.
     */
    public JDive getRow(int row) {
        return (JDive) dives.toArray()[row];
    }
    
    public void logbookChanged(LogbookChangeEvent e) {
        if (EventType.LOGBOOK_LOADED.equals(e.getType())) {
            searchString_new = null;
            searchString_old = "";
        }
        if (!EventType.LOGBOOK_TITLE_CHANGED.equals(e.getType())) {
            if (mainWindow != null) {
                dives = null;
            }
            fireTableDataChanged();
        }        
    }
    
    private JDiveLog getLogbook() {
        if (logbook != null) {
            return logbook;
        }
        return mainWindow.getLogBook();
    }
    
    public TreeSet<JDive> getDives() {
        if (mainWindow != null && (dives == null || searchString_new != searchString_old)) {
            if (searchString_new != null) {
                Iterator<JDive> diveIt = this.mainWindow.getLogBook().getDives().iterator();
                dives = new TreeSet<JDive>();
                while (diveIt.hasNext()) {
                    JDive dive = diveIt.next();
                    String value = extractString(dive);
                    double points = matchString(searchString_new, value);
                    if (points > 0) {
                        dives.add(dive);
                    }
                }
            } else {
               dives =  mainWindow.getLogBook().getDives();
            }
        } 
        return dives;        
    }
    
    public BigDecimal getAverageAmv() {
        int count_dives = 0;
        BigDecimal averageAmv = new BigDecimal(0);
        Iterator<JDive> diveIt = dives.iterator();
        while (diveIt.hasNext()) {            
            JDive dive = diveIt.next();
            if (dive != null && dive.getAMV() != null && dive.getAMV() != 0) {
                UnitConverter c = new UnitConverter(UnitConverter.getSystem(dive.getUnits()), UnitConverter.getDisplaySystem());
                averageAmv = averageAmv.add(new BigDecimal(c.convertAMV(dive.getAMV())));
                count_dives++;
            }
        }
        if (averageAmv == null || averageAmv.equals(new BigDecimal(0))) {
            return new BigDecimal(0);
        }        
        return count_dives==0 ? new BigDecimal(0) : averageAmv.divide(new BigDecimal(count_dives),2 , RoundingMode.HALF_UP);
    }

    public BigDecimal getAverageTemperature() {
        BigDecimal averageTemperature = new BigDecimal(0);
        int count_dives = 0;
        Iterator<JDive> diveIt = dives.iterator();
        while (diveIt.hasNext()) {            
            JDive dive = diveIt.next();
            if (dive != null && dive.getTemperature() != null && dive.getTemperature() != 0) {
                UnitConverter uc = new UnitConverter(UnitConverter.getSystem(dive.getUnits()), UnitConverter.getDisplaySystem());
                averageTemperature = averageTemperature.add(new BigDecimal(uc.convertTemperature(dive.getTemperature())));
                count_dives++;
            }
        }
        if (averageTemperature == null || averageTemperature.equals(new BigDecimal(0))) {
            return new BigDecimal(0);
        }
        return count_dives==0 ? new BigDecimal(0) : averageTemperature.divide(new BigDecimal(count_dives),2 , RoundingMode.HALF_UP);
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
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.printf("%0,2d:%0,2d:%0,2d", days, hours, minutes);
        return sw.toString();
    }
    
    public BigDecimal getAverageDepth() {
        BigDecimal averageDepth = new BigDecimal(0);
        int count_dives = 0;
        Iterator<JDive> diveIt = dives.iterator();
        while (diveIt.hasNext()) {            
            JDive dive = diveIt.next();
            if (dive.getDepth() != null) {
                UnitConverter c = new UnitConverter(UnitConverter.getSystem(dive.getUnits()), UnitConverter.getDisplaySystem());
                averageDepth = averageDepth.add(new BigDecimal(c.convertAltitude(dive.getDepth())));
                count_dives++;
            }
        }
        if (averageDepth == null || averageDepth.equals(new BigDecimal(0))) {
            return new BigDecimal(0);
        }
        return count_dives==0 ? new BigDecimal(0) : averageDepth.divide(new BigDecimal(count_dives),2 , RoundingMode.HALF_UP);
    }
    
    public BigDecimal getAverageDuration() {
        BigDecimal averageDuration = new BigDecimal(0);
        int count_dives = 0;
        Iterator<JDive> diveIt = dives.iterator();
        while (diveIt.hasNext()) {            
            JDive dive = diveIt.next();
            if (dive.getDepth() != null) {
                UnitConverter c = new UnitConverter(UnitConverter.getSystem(dive.getUnits()), UnitConverter.getDisplaySystem());
                averageDuration = averageDuration.add(new BigDecimal(c.convertTime(dive.getDuration())));
                count_dives++;
            }
        }
        if (averageDuration == null || averageDuration.equals(new BigDecimal(0))) {
            return new BigDecimal(0);
        }
        return count_dives==0 ? new BigDecimal(0) : averageDuration.divide(new BigDecimal(count_dives),2 , RoundingMode.HALF_UP);
    }
    
}