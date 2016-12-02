/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: CommandLoadFile.java
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
package net.sf.jdivelog.gui.commands;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;

import net.sf.jdivelog.gui.JDiveLogException;
import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.gui.MessageDialog;
import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.Buddy;
import net.sf.jdivelog.model.ChartSettings;
import net.sf.jdivelog.model.DiveActivity;
import net.sf.jdivelog.model.DiveSite;
import net.sf.jdivelog.model.DiveType;
import net.sf.jdivelog.model.Equipment;
import net.sf.jdivelog.model.EquipmentSet;
import net.sf.jdivelog.model.ExportSettings;
import net.sf.jdivelog.model.GloveType;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.JDiveLog;
import net.sf.jdivelog.model.Masterdata;
import net.sf.jdivelog.model.Picture;
import net.sf.jdivelog.model.ProfileSettings;
import net.sf.jdivelog.model.SlideshowSettings;
import net.sf.jdivelog.model.StatisticSettings;
import net.sf.jdivelog.model.Suit;
import net.sf.jdivelog.model.Tank;
import net.sf.jdivelog.model.gasblending.GasBlendingSettings;
import net.sf.jdivelog.model.gasblending.GasSource;
import net.sf.jdivelog.model.gasoverflow.GasOverflowSettings;
import net.sf.jdivelog.model.gasoverflow.GasOverflowSource;
import net.sf.jdivelog.model.jdivelog.JDiveLogFileLoader;
import net.sf.jdivelog.model.udcf.Dive;
import net.sf.jdivelog.model.udcf.Gas;
import net.sf.jdivelog.util.UnitConverter;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 * Command for loading .jlb-files.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class CommandLoadFile implements Command {

    private MainWindow mainWindow;

    private File file;

    private HashMap<String, FileLoader> fileLoaders;
    
    private boolean import_logbook = false;
    private JDiveLogFileLoader jdivelogfileloader = null;

    public CommandLoadFile(MainWindow mainWindow, File file) {
    	this(mainWindow, file, false, null);
    }

    public CommandLoadFile(MainWindow mainWindow, File file, boolean import_logbook, JDiveLogFileLoader jdivelogfileloader) {
        this.mainWindow = mainWindow;
        this.file = file;
        this.import_logbook = import_logbook;
        this.jdivelogfileloader = jdivelogfileloader;
        this.fileLoaders = new HashMap<String, FileLoader>();
        this.fileLoaders.put("1.0", new FileLoader1_0()); //$NON-NLS-1$
        this.fileLoaders.put("1.2", new FileLoader1_2()); //$NON-NLS-1$
        this.fileLoaders.put("2.0", new FileLoader2_0()); //$NON-NLS-1$
        this.fileLoaders.put("2.3", new FileLoader2_3()); //$NON-NLS-1$
        this.fileLoaders.put("2.6", new FileLoader2_6()); //$NON-NLS-1$
        this.fileLoaders.put("2.13", new FileLoader2_13()); //$NON-NLS-1$
    }

    /**
     * @see net.sf.jdivelog.gui.commands.Command#execute()
     */
    public void execute() {
        try {
            Digester d = new Digester();
            d.setValidating(false);
            d.addObjectCreate("JDiveLog", VersionBean.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Version", "setVersion", 1);
            d.addCallParam("JDiveLog/Version", 0);

            JDiveLog jdl = null;
            VersionBean vb = (VersionBean) d.parse(file);
            if (vb.getVersion() != null && !"".equals(vb.getVersion())) {
                FileLoader fl = fileLoaders.get(vb.getVersion());
                if (fl != null) {
                    jdl = fl.loadFile(file);
                } else {
                    new MessageDialog(mainWindow, Messages.getString("error.could_not_open_file"), Messages.getString("error.could_not_open_file")+" "+file.getName()+", "+Messages.getString("unknown_version")+": "+vb.getVersion(), null, MessageDialog.MessageType.ERROR);
                }
            } else {
                FileLoader fl = new FileLoaderPre1_0();
                jdl = fl.loadFile(file);
            }

            if (jdl != null && !import_logbook) {
                mainWindow.setFile(file);
                mainWindow.setChanged(false);
                mainWindow.setLogBook(jdl);
            }
            if (jdl != null && import_logbook) {
            	jdivelogfileloader.setImport_logbook(jdl);     
            }
        } catch (SAXException ex) {
            StringWriter sw = new StringWriter();
            sw.write(Messages.getString("error.could_not_open_file")); //$NON-NLS-1$
            sw.write(" "); //$NON-NLS-1$
            sw.write(file.getName());
            sw.write("!"); //$NON-NLS-1$
            throw new JDiveLogException(Messages.getString("error.could_not_open_file"), sw.toString(), ex);
        } catch (IOException ioe) {
            StringWriter sw = new StringWriter();
            sw.write(Messages.getString("error.could_not_open_file")); //$NON-NLS-1$
            sw.write(" "); //$NON-NLS-1$
            sw.write(file.getName());
            sw.write("!"); //$NON-NLS-1$
            throw new JDiveLogException(Messages.getString("error.could_not_open_file"), sw.toString(), ioe);
        }
    }

    @Deprecated
    private static void migrateDiveSites(JDiveLog logbook) {
        Iterator<JDive> dit = logbook.getDives().iterator();
        while(dit.hasNext()) {
            JDive dive = dit.next();
            dive.setDiveSiteId(getDiveSitePrivateId(logbook.getMasterdata(), dive.getPlace(), dive.getCountry()));
        }
    }
    
    public static String getDiveSitePrivateId(Masterdata masterdata, String spot, String country) {
        DiveSite site = masterdata.getDiveSiteBySpotAndCountry(spot, country);
        if (site == null) {
            site = new DiveSite();
            site.setSpot(spot);
            site.setCountry(country);
            site.setPrivateId(String.valueOf(masterdata.getNextPrivateDiveSiteId()));
            masterdata.addDiveSite(site);
        }
        return site.getPrivateId();
    }

    //
    // inner classes
    //
    
    private interface FileLoader {

        public JDiveLog loadFile(File file) throws SAXException, IOException;
    }

    private class FileLoaderPre1_0 implements FileLoader {
        public JDiveLog loadFile(File file) throws SAXException, IOException {
            Digester d = new Digester();
            d.setValidating(false);
            d.addObjectCreate("JDiveLog", JDiveLog.class); //$NON-NLS-1$

            d.addObjectCreate("JDiveLog/Masterdata", Masterdata.class); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/Masterdata/Buddys/Buddy", Buddy.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Buddys/Buddy/Firstname", "setFirstname", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Buddys/Buddy/Firstname", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Buddys/Buddy/Lastname", "setLastname", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Buddys/Buddy/Lastname", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Buddys/Buddy", "addBuddy"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/Divetypes/Divetype", DiveType.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Divetypes/Divetype/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Divetypes/Divetype/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Divetypes/Divetype", "addDivetype"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/Diveactivities/Diveactivity", DiveActivity.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Diveactivities/Diveactivity/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Diveactivities/Diveactivity/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Diveactivities/Diveactivity", "addDiveactivity"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/Masterdata", "setMasterdata"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/ExportSettings", ExportSettings.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexTitle"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexImages"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexDate"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexTime"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexLocation"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexCountry"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexDuration"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailVisible"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailDate"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailTime"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailLocation"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailCountry"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailDuration"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailEquipment"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailBuddy"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailComment"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailVisibility"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailProfile"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailProfileWidth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailProfileHeight"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureVisible"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureImageMaxWidth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureImageMaxHeight"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureThumbnailMaxWidth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureThumbnailMaxHeight"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/skinFile"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/exportDirectory"); //$NON-NLS-1$
            d.addSetNext("JDiveLog/ExportSettings", "setExportSettings"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/ComputerDriver", "setComputerDriver", 1);//$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/ComputerDriver", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/ComputerSettings/property", "addComputerProperty", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/ComputerSettings/property", 0, "name"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/ComputerSettings/property", 1, "value"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/JDive", JDive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DiveNum", "setDiveNumber", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DiveNum", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/UNITS", "setUnits", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/UNITS", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/PLACE", "setPlace", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/PLACE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Country", "setCountry", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Country", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Depth", "setDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Average_Depth", "setAverageDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Average_Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Duration", "setDuration", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Duration", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/amv", "setAMV", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/amv", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DiveType", "setDiveType", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DiveType", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DiveActivity", "setDiveActivity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DiveActivity", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Visibility", "setVisibility", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Visibility", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Buddy", "setBuddy", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Buddy", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Comment", "setComment", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Comment", 0); //$NON-NLS-1$

            d.addObjectCreate("JDiveLog/JDive/Equipment", Equipment.class); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/JDive/Equipment/Tanks/Tank", Tank.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/Type", "setType", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/JDive/Equipment/Tanks/Tank/MIX", Gas.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/MIXNAME", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/MIXNAME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/TANKVOLUME", "setTankvolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/TANKVOLUME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PSTART", "setPstart", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PSTART", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PEND", "setPend", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PEND", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/O2", "setOxygen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/O2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/N2", "setNitrogen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/N2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/HE", "setHelium", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/HE", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/Equipment/Tanks/Tank/MIX", "setGas"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/JDive/Equipment/Tanks/Tank", "addTank"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/Equipment/Weight", "setWeight", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Weight", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Suit", "setSuit", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Suit", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Gloves", "setGloves", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Gloves", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Comment", "setComment", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Comment", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/Equipment", "setEquipment"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/JDive/Pictures/Picture", Picture.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/Filename", "setFilename", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/Filename", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/Name", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/Name", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/Pictures/Picture", "addPicture"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/JDive/DIVE", Dive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DIVE/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DIVE/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DIVE/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SURFACEINTERVAL", "setSurfaceinterval", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SURFACEINTERVAL", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/DENSITY", "setDensity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/DENSITY", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/ALTITUDE", "setAltitude", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/ALTITUDE", 0); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/JDive/DIVE/GASES/MIX", Gas.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/MIXNAME", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/MIXNAME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", "setTankvolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PSTART", "setPstart", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PSTART", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PEND", "setPend", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PEND", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/O2", "setOxygen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/O2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/N2", "setNitrogen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/N2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/HE", "setHelium", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/HE", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/DIVE/GASES/MIX", "addGas"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/DIVE/TIMEDEPTHMODE", "setTimeDepthMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/DIVE/DELTAMODE", "setDeltaMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/DELTA", "addDelta", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/DELTA", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/SWITCH", "addSwitch", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/SWITCH", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/T", "addTime", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/T", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/D", "addDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/D", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/TEMPERATURE", "addTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/ALARM", "addAlarm", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/ALARM", 0); //$NON-NLS-1$

            d.addSetNext("JDiveLog/JDive/DIVE", "setDive"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/JDive", "addDive"); //$NON-NLS-1$ //$NON-NLS-2$

            JDiveLog log = (JDiveLog) d.parse(file);
            Iterator<JDive> diveIt = log.getDives().iterator();
            while(diveIt.hasNext()) {
                JDive dive = diveIt.next();
                if (dive.getAMV() != null) {
                    UnitConverter c = new UnitConverter(UnitConverter.SYSTEM_DISPLAY, UnitConverter.getSystem(dive.getUnits()));
                    dive.setAMV(c.convertAMV(dive.getAMV()));
                }
            }
            migrateDiveSites(log);
            return log;
        }
    }

    private class FileLoader1_0 implements FileLoader {
        public JDiveLog loadFile(File file) throws SAXException, IOException {
            Digester d = new Digester();
            d.setValidating(false);
            d.addObjectCreate("JDiveLog", JDiveLog.class); //$NON-NLS-1$

            d.addObjectCreate("JDiveLog/Masterdata", Masterdata.class); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/Masterdata/Buddys/Buddy", Buddy.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Buddys/Buddy/Firstname", "setFirstname", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Buddys/Buddy/Firstname", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Buddys/Buddy/Lastname", "setLastname", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Buddys/Buddy/Lastname", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Buddys/Buddy", "addBuddy"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/Divetypes/Divetype", DiveType.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Divetypes/Divetype/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Divetypes/Divetype/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Divetypes/Divetype", "addDivetype"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/Diveactivities/Diveactivity", DiveActivity.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Diveactivities/Diveactivity/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Diveactivities/Diveactivity/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Diveactivities/Diveactivity", "addDiveactivity"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/Suits/Suit", Suit.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Suits/Suit/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Suits/Suit/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Suits/Suit", "addSuit"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/GloveTypes/GloveType", GloveType.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/GloveTypes/GloveType/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/GloveTypes/GloveType/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/GloveTypes/GloveType", "addGloveType"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/EquipmentSets/EquipmentSet", EquipmentSet.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Name", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Name", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Suit", "setSuit", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Suit", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Gloves", "setGloves", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Gloves", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Weight", "setWeight", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Weight", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankVolume", "setTankVolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankVolume", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankType", "setTankType", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankType", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/EquipmentSets/EquipmentSet", "addEquipmentSet"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/Masterdata", "setMasterdata"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/ExportSettings", ExportSettings.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexTitle"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexImages"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexDate"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexTime"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexLocation"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexCountry"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexDuration"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexPictureCount"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailVisible"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailDate"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailTime"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailLocation"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailCountry"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailDuration"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailEquipment"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailBuddy"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailComment"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailVisibility"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailProfile"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailProfileWidth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailProfileHeight"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureVisible"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureImageMaxWidth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureImageMaxHeight"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureThumbnailMaxWidth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureThumbnailMaxHeight"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/skinFile"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/exportDirectory"); //$NON-NLS-1$
            d.addSetNext("JDiveLog/ExportSettings", "setExportSettings"); //$NON-NLS-1$ //$NON-NLS-2$
            
            d.addCallMethod("JDiveLog/ComputerDriver", "setComputerDriver", 1);//$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/ComputerDriver", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/ComputerSettings/property", "addComputerProperty", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/ComputerSettings/property", 0, "name"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/ComputerSettings/property", 1, "value"); //$NON-NLS-1$ //$NON-NLS-2$
            
            d.addObjectCreate("JDiveLog/StatisticSettings", StatisticSettings.class); //$NON-NLS-1$
            String chart = "buddyStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setBuddyStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "divePlaceStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setDivePlaceStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "countryStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setCountryStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "diveTypeStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setDiveTypeStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "diveActivityStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setDiveActivityStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings", "setStatisticSettings"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/JDive", JDive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DiveNum", "setDiveNumber", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DiveNum", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/UNITS", "setUnits", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/UNITS", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/PLACE", "setPlace", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/PLACE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Country", "setCountry", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Country", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Depth", "setDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Average_Depth", "setAverageDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Average_Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Duration", "setDuration", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Duration", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/amv", "setAMV", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/amv", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DiveType", "setDiveType", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DiveType", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DiveActivity", "setDiveActivity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DiveActivity", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Visibility", "setVisibility", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Visibility", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Buddy", "setBuddy", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Buddy", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Comment", "setComment", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Comment", 0); //$NON-NLS-1$

            d.addObjectCreate("JDiveLog/JDive/Equipment", Equipment.class); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/JDive/Equipment/Tanks/Tank", Tank.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/Type", "setType", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/JDive/Equipment/Tanks/Tank/MIX", Gas.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/MIXNAME", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/MIXNAME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/TANKVOLUME", "setTankvolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/TANKVOLUME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PSTART", "setPstart", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PSTART", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PEND", "setPend", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PEND", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/O2", "setOxygen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/O2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/N2", "setNitrogen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/N2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/HE", "setHelium", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/HE", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/Equipment/Tanks/Tank/MIX", "setGas"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/JDive/Equipment/Tanks/Tank", "addTank"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/Equipment/Weight", "setWeight", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Weight", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Suit", "setSuit", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Suit", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Gloves", "setGloves", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Gloves", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Comment", "setComment", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Comment", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/Equipment", "setEquipment"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/JDive/Pictures/Picture", Picture.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/Filename", "setFilename", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/Filename", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/Name", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/Name", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/Pictures/Picture", "addPicture"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/JDive/DIVE", Dive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DIVE/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DIVE/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DIVE/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SURFACEINTERVAL", "setSurfaceinterval", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SURFACEINTERVAL", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/DENSITY", "setDensity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/DENSITY", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/ALTITUDE", "setAltitude", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/ALTITUDE", 0); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/JDive/DIVE/GASES/MIX", Gas.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/MIXNAME", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/MIXNAME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", "setTankvolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PSTART", "setPstart", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PSTART", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PEND", "setPend", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PEND", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/O2", "setOxygen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/O2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/N2", "setNitrogen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/N2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/HE", "setHelium", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/HE", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/DIVE/GASES/MIX", "addGas"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/DIVE/TIMEDEPTHMODE", "setTimeDepthMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/DIVE/DELTAMODE", "setDeltaMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/DELTA", "addDelta", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/DELTA", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/SWITCH", "addSwitch", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/SWITCH", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/T", "addTime", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/T", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/D", "addDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/D", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/TEMPERATURE", "addTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/ALARM", "addAlarm", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/ALARM", 0); //$NON-NLS-1$

            d.addSetNext("JDiveLog/JDive/DIVE", "setDive"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/JDive", "addDive"); //$NON-NLS-1$ //$NON-NLS-2$

            JDiveLog log = (JDiveLog) d.parse(file);
            Iterator<JDive> diveIt = log.getDives().iterator();
            while(diveIt.hasNext()) {
                JDive dive = diveIt.next();
                if (dive.getStoredAMV() != null) {
                    UnitConverter c = new UnitConverter(UnitConverter.SYSTEM_DISPLAY, UnitConverter.getSystem(dive.getUnits()));
                    dive.setAMV(c.convertAMV(dive.getStoredAMV()));
                }
            }
            migrateDiveSites(log);
            return log;
        }
    }

    private class FileLoader1_2 implements FileLoader {
        public JDiveLog loadFile(File file) throws SAXException, IOException {
            Digester d = new Digester();
            d.setValidating(false);
            d.addObjectCreate("JDiveLog", JDiveLog.class); //$NON-NLS-1$

            d.addObjectCreate("JDiveLog/Masterdata", Masterdata.class); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/Masterdata/Buddys/Buddy", Buddy.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Buddys/Buddy/Firstname", "setFirstname", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Buddys/Buddy/Firstname", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Buddys/Buddy/Lastname", "setLastname", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Buddys/Buddy/Lastname", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Buddys/Buddy", "addBuddy"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/Divetypes/Divetype", DiveType.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Divetypes/Divetype/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Divetypes/Divetype/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Divetypes/Divetype", "addDivetype"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/Diveactivities/Diveactivity", DiveActivity.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Diveactivities/Diveactivity/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Diveactivities/Diveactivity/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Diveactivities/Diveactivity", "addDiveactivity"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/Suits/Suit", Suit.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Suits/Suit/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Suits/Suit/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Suits/Suit", "addSuit"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/GloveTypes/GloveType", GloveType.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/GloveTypes/GloveType/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/GloveTypes/GloveType/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/GloveTypes/GloveType", "addGloveType"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/EquipmentSets/EquipmentSet", EquipmentSet.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Name", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Name", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Suit", "setSuit", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Suit", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Gloves", "setGloves", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Gloves", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Weight", "setWeight", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Weight", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankVolume", "setTankVolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankVolume", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankType", "setTankType", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankType", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/EquipmentSets/EquipmentSet", "addEquipmentSet"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/Masterdata", "setMasterdata"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/ExportSettings", ExportSettings.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/footerLink"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/footerLinkName"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexTitle"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexImages"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexGroupByYear", "groupByYear"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexDate"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexTime"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexLocation"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexCountry"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexDuration"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexPictureCount"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailVisible"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailDate"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailTime"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailLocation"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailCountry"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailDuration"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailEquipment"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailBuddy"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailComment"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailVisibility"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailProfile"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailProfileWidth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailProfileHeight"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureVisible"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureImageMaxWidth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureImageMaxHeight"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureThumbnailMaxWidth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureThumbnailMaxHeight"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/skinFile"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/exportDirectory"); //$NON-NLS-1$
            d.addSetNext("JDiveLog/ExportSettings", "setExportSettings"); //$NON-NLS-1$ //$NON-NLS-2$
            
            d.addObjectCreate("JDiveLog/SlideshowSettings", SlideshowSettings.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/SlideshowSettings/cycletime"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/SlideshowSettings/repeat"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/SlideshowSettings/displayTitle"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/SlideshowSettings/displayDescription"); //$NON-NLS-1$
            d.addSetNext("JDiveLog/SlideshowSettings", "setSlideshowSettings"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addCallMethod("JDiveLog/ComputerDriver", "setComputerDriver", 1);//$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/ComputerDriver", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/ComputerSettings/property", "addComputerProperty", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/ComputerSettings/property", 0, "name"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/ComputerSettings/property", 1, "value"); //$NON-NLS-1$ //$NON-NLS-2$
            
            d.addObjectCreate("JDiveLog/StatisticSettings", StatisticSettings.class); //$NON-NLS-1$
            String chart = "buddyStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setBuddyStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "divePlaceStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setDivePlaceStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "countryStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setCountryStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "diveTypeStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setDiveTypeStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "diveActivityStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setDiveActivityStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings", "setStatisticSettings"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/JDive", JDive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DiveNum", "setDiveNumber", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DiveNum", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/UNITS", "setUnits", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/UNITS", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/PLACE", "setPlace", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/PLACE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Country", "setCountry", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Country", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Depth", "setDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Average_Depth", "setAverageDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Average_Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Duration", "setDuration", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Duration", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/amv", "setAMV", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/amv", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DiveType", "setDiveType", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DiveType", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DiveActivity", "setDiveActivity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DiveActivity", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Visibility", "setVisibility", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Visibility", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Buddy", "setBuddy", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Buddy", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Comment", "setComment", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Comment", 0); //$NON-NLS-1$

            d.addObjectCreate("JDiveLog/JDive/Equipment", Equipment.class); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/JDive/Equipment/Tanks/Tank", Tank.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/Type", "setType", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/JDive/Equipment/Tanks/Tank/MIX", Gas.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/MIXNAME", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/MIXNAME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/TANKVOLUME", "setTankvolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/TANKVOLUME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PSTART", "setPstart", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PSTART", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PEND", "setPend", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PEND", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/O2", "setOxygen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/O2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/N2", "setNitrogen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/N2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/HE", "setHelium", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/HE", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/Equipment/Tanks/Tank/MIX", "setGas"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/JDive/Equipment/Tanks/Tank", "addTank"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/Equipment/Weight", "setWeight", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Weight", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Suit", "setSuit", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Suit", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Gloves", "setGloves", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Gloves", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Comment", "setComment", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Comment", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/Equipment", "setEquipment"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/JDive/Pictures/Picture", Picture.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/Filename", "setFilename", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/Filename", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/Name", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/Name", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/Description", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/rotation", "setRotation", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/rotation", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/Pictures/Picture", "addPicture"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/JDive/DIVE", Dive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DIVE/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DIVE/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DIVE/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SURFACEINTERVAL", "setSurfaceinterval", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SURFACEINTERVAL", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/DENSITY", "setDensity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/DENSITY", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/ALTITUDE", "setAltitude", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/ALTITUDE", 0); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/JDive/DIVE/GASES/MIX", Gas.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/MIXNAME", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/MIXNAME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", "setTankvolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PSTART", "setPstart", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PSTART", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PEND", "setPend", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PEND", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/O2", "setOxygen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/O2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/N2", "setNitrogen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/N2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/HE", "setHelium", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/HE", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/DIVE/GASES/MIX", "addGas"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/DIVE/TIMEDEPTHMODE", "setTimeDepthMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/DIVE/DELTAMODE", "setDeltaMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/DELTA", "addDelta", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/DELTA", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/SWITCH", "addSwitch", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/SWITCH", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/T", "addTime", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/T", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/D", "addDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/D", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/TEMPERATURE", "addTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/ALARM", "addAlarm", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/ALARM", 0); //$NON-NLS-1$

            d.addSetNext("JDiveLog/JDive/DIVE", "setDive"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/JDive", "addDive"); //$NON-NLS-1$ //$NON-NLS-2$
            
            // ignored dives
            d.addObjectCreate("JDiveLog/IgnoredDives/JDive", JDive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DiveNum", "setDiveNumber", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DiveNum", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/UNITS", "setUnits", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/UNITS", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/PLACE", "setPlace", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/PLACE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Country", "setCountry", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Country", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Depth", "setDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Average_Depth", "setAverageDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Average_Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Duration", "setDuration", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Duration", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/amv", "setAMV", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/amv", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DiveType", "setDiveType", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DiveType", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DiveActivity", "setDiveActivity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DiveActivity", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Visibility", "setVisibility", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Visibility", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Buddy", "setBuddy", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Buddy", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Comment", "setComment", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Comment", 0); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/IgnoredDives/JDive/DIVE", Dive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SURFACEINTERVAL", "setSurfaceinterval", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SURFACEINTERVAL", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/DENSITY", "setDensity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/DENSITY", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/ALTITUDE", "setAltitude", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/ALTITUDE", 0); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX", Gas.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/MIXNAME", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/MIXNAME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", "setTankvolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/PSTART", "setPstart", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/PSTART", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/PEND", "setPend", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/PEND", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/O2", "setOxygen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/O2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/N2", "setNitrogen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/N2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/HE", "setHelium", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/HE", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX", "addGas"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/TIMEDEPTHMODE", "setTimeDepthMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/DELTAMODE", "setDeltaMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/DELTA", "addDelta", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/DELTA", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/SWITCH", "addSwitch", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/SWITCH", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/T", "addTime", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/T", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/D", "addDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/D", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/TEMPERATURE", "addTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/ALARM", "addAlarm", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/ALARM", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/IgnoredDives/JDive/DIVE", "setDive"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/IgnoredDives/JDive", "addIgnoredDive"); //$NON-NLS-1$ //$NON-NLS-2$
            JDiveLog log = (JDiveLog) d.parse(file);
            migrateDiveSites(log);
            return log;
        }
    }

    private class FileLoader2_0 implements FileLoader {
        public JDiveLog loadFile(File file) throws SAXException, IOException {
            Digester d = new Digester();
            d.setValidating(false);
            d.addObjectCreate("JDiveLog", JDiveLog.class); //$NON-NLS-1$

            d.addObjectCreate("JDiveLog/Masterdata", Masterdata.class); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/Masterdata/Buddys/Buddy", Buddy.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Buddys/Buddy/Firstname", "setFirstname", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Buddys/Buddy/Firstname", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Buddys/Buddy/Lastname", "setLastname", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Buddys/Buddy/Lastname", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Buddys/Buddy", "addBuddy"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/Divetypes/Divetype", DiveType.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Divetypes/Divetype/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Divetypes/Divetype/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Divetypes/Divetype", "addDivetype"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/Diveactivities/Diveactivity", DiveActivity.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Diveactivities/Diveactivity/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Diveactivities/Diveactivity/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Diveactivities/Diveactivity", "addDiveactivity"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/Suits/Suit", Suit.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Suits/Suit/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Suits/Suit/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Suits/Suit", "addSuit"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/GloveTypes/GloveType", GloveType.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/GloveTypes/GloveType/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/GloveTypes/GloveType/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/GloveTypes/GloveType", "addGloveType"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/EquipmentSets/EquipmentSet", EquipmentSet.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Name", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Name", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Suit", "setSuit", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Suit", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Gloves", "setGloves", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Gloves", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Weight", "setWeight", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Weight", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankVolume", "setTankVolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankVolume", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankType", "setTankType", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankType", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/EquipmentSets/EquipmentSet", "addEquipmentSet"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/DiveSites/DiveSite", DiveSite.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/privateId"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/publicId"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/publish"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/published"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/ownerId"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/spot"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/city"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/state"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/country"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/longitude"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/latitude"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/minDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/avgDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/maxDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/waters"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/timezone"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/description"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/directions"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/altitude"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/warnings"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/privateRemarks"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/siteType"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/mainDiveActivity"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/evaluation"); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/DiveSites/DiveSite", "addDiveSite"); // $NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/Masterdata", "setMasterdata"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/ExportSettings", ExportSettings.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/footerLink"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/footerLinkName"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexTitle"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexImages"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexGroupByYear", "groupByYear"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexDate"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexTime"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexLocation"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexCity"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexCountry"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexDuration"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexPictureCount"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailVisible"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailDate"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailTime"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailLocation"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailCity"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailCountry"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailWaters"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailDuration"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailEquipment"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailBuddy"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailComment"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailVisibility"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailProfile"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailProfileWidth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailProfileHeight"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureVisible"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureImageMaxWidth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureImageMaxHeight"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureThumbnailMaxWidth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureThumbnailMaxHeight"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/watermarkEnabled"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/watermarkText"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/skinFile"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/fullExport"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/keepImages"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/exportDirectory"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/scpEnabled"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/scpHost"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/scpDirectory"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/scpUser"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/scpPassword"); //$NON-NLS-1$
            d.addSetNext("JDiveLog/ExportSettings", "setExportSettings"); //$NON-NLS-1$ //$NON-NLS-2$
            
            d.addObjectCreate("JDiveLog/SlideshowSettings", SlideshowSettings.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/SlideshowSettings/cycletime"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/SlideshowSettings/repeat"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/SlideshowSettings/displayTitle"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/SlideshowSettings/displayDescription"); //$NON-NLS-1$
            d.addSetNext("JDiveLog/SlideshowSettings", "setSlideshowSettings"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addCallMethod("JDiveLog/ComputerDriver", "setComputerDriver", 1);//$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/ComputerDriver", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/ComputerSettings/property", "addComputerProperty", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/ComputerSettings/property", 0, "name"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/ComputerSettings/property", 1, "value"); //$NON-NLS-1$ //$NON-NLS-2$
            
            d.addObjectCreate("JDiveLog/StatisticSettings", StatisticSettings.class); //$NON-NLS-1$
            String chart = "buddyStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setBuddyStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "divePlaceStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setDivePlaceStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "countryStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setCountryStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "diveTypeStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setDiveTypeStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "diveActivityStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setDiveActivityStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings", "setStatisticSettings"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/JDive", JDive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DiveNum", "setDiveNumber", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DiveNum", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/UNITS", "setUnits", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/UNITS", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/JDive/diveSiteId"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/JDive/htmlExported"); // $NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Depth", "setDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Average_Depth", "setAverageDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Average_Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Duration", "setDuration", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Duration", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/amv", "setAMV", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/amv", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DiveType", "setDiveType", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DiveType", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DiveActivity", "setDiveActivity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DiveActivity", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Visibility", "setVisibility", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Visibility", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Buddy", "setBuddy", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Buddy", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Comment", "setComment", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Comment", 0); //$NON-NLS-1$

            d.addObjectCreate("JDiveLog/JDive/Equipment", Equipment.class); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/JDive/Equipment/Tanks/Tank", Tank.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/Type", "setType", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/JDive/Equipment/Tanks/Tank/MIX", Gas.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/MIXNAME", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/MIXNAME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/TANKVOLUME", "setTankvolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/TANKVOLUME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PSTART", "setPstart", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PSTART", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PEND", "setPend", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PEND", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/O2", "setOxygen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/O2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/N2", "setNitrogen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/N2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/HE", "setHelium", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/HE", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/Equipment/Tanks/Tank/MIX", "setGas"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/JDive/Equipment/Tanks/Tank", "addTank"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/Equipment/Weight", "setWeight", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Weight", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Suit", "setSuit", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Suit", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Gloves", "setGloves", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Gloves", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Comment", "setComment", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Comment", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/Equipment", "setEquipment"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/JDive/Pictures/Picture", Picture.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/Filename", "setFilename", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/Filename", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/Name", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/Name", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/Description", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/rotation", "setRotation", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/rotation", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/rating", "setRating", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/rating", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/Pictures/Picture", "addPicture"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/JDive/DIVE", Dive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DIVE/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DIVE/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DIVE/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SURFACEINTERVAL", "setSurfaceinterval", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SURFACEINTERVAL", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/DENSITY", "setDensity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/DENSITY", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/ALTITUDE", "setAltitude", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/ALTITUDE", 0); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/JDive/DIVE/GASES/MIX", Gas.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/MIXNAME", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/MIXNAME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", "setTankvolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PSTART", "setPstart", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PSTART", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PEND", "setPend", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PEND", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/O2", "setOxygen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/O2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/N2", "setNitrogen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/N2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/HE", "setHelium", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/HE", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/DIVE/GASES/MIX", "addGas"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/DIVE/TIMEDEPTHMODE", "setTimeDepthMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/DIVE/DELTAMODE", "setDeltaMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/DELTA", "addDelta", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/DELTA", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/SWITCH", "addSwitch", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/SWITCH", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/T", "addTime", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/T", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/D", "addDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/D", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/TEMPERATURE", "addTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/ALARM", "addAlarm", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/ALARM", 0); //$NON-NLS-1$

            d.addSetNext("JDiveLog/JDive/DIVE", "setDive"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/JDive", "addDive"); //$NON-NLS-1$ //$NON-NLS-2$
            
            // ignored dives
            d.addObjectCreate("JDiveLog/IgnoredDives/JDive", JDive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DiveNum", "setDiveNumber", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DiveNum", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/UNITS", "setUnits", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/UNITS", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/PLACE", "setPlace", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/PLACE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Country", "setCountry", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Country", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Depth", "setDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Average_Depth", "setAverageDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Average_Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Duration", "setDuration", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Duration", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/amv", "setAMV", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/amv", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DiveType", "setDiveType", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DiveType", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DiveActivity", "setDiveActivity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DiveActivity", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Visibility", "setVisibility", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Visibility", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Buddy", "setBuddy", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Buddy", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Comment", "setComment", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Comment", 0); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/IgnoredDives/JDive/DIVE", Dive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SURFACEINTERVAL", "setSurfaceinterval", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SURFACEINTERVAL", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/DENSITY", "setDensity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/DENSITY", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/ALTITUDE", "setAltitude", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/ALTITUDE", 0); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX", Gas.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/MIXNAME", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/MIXNAME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", "setTankvolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/PSTART", "setPstart", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/PSTART", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/PEND", "setPend", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/PEND", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/O2", "setOxygen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/O2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/N2", "setNitrogen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/N2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/HE", "setHelium", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/HE", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX", "addGas"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/TIMEDEPTHMODE", "setTimeDepthMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/DELTAMODE", "setDeltaMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/DELTA", "addDelta", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/DELTA", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/SWITCH", "addSwitch", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/SWITCH", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/T", "addTime", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/T", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/D", "addDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/D", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/TEMPERATURE", "addTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/ALARM", "addAlarm", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/ALARM", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/IgnoredDives/JDive/DIVE", "setDive"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/IgnoredDives/JDive", "addIgnoredDive"); //$NON-NLS-1$ //$NON-NLS-2$
            JDiveLog log = (JDiveLog) d.parse(file);
            return log;
        }
    }

    private class FileLoader2_3 implements FileLoader {
        public JDiveLog loadFile(File file) throws SAXException, IOException {
            Digester d = new Digester();
            d.setValidating(false);
            d.addObjectCreate("JDiveLog", JDiveLog.class); //$NON-NLS-1$

            d.addObjectCreate("JDiveLog/Masterdata", Masterdata.class); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/Masterdata/Buddys/Buddy", Buddy.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Buddys/Buddy/Firstname", "setFirstname", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Buddys/Buddy/Firstname", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Buddys/Buddy/Lastname", "setLastname", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Buddys/Buddy/Lastname", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Buddys/Buddy", "addBuddy"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/Divetypes/Divetype", DiveType.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Divetypes/Divetype/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Divetypes/Divetype/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Divetypes/Divetype", "addDivetype"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/Diveactivities/Diveactivity", DiveActivity.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Diveactivities/Diveactivity/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Diveactivities/Diveactivity/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Diveactivities/Diveactivity", "addDiveactivity"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/Suits/Suit", Suit.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Suits/Suit/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Suits/Suit/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Suits/Suit", "addSuit"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/GloveTypes/GloveType", GloveType.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/GloveTypes/GloveType/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/GloveTypes/GloveType/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/GloveTypes/GloveType", "addGloveType"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/EquipmentSets/EquipmentSet", EquipmentSet.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Name", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Name", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Suit", "setSuit", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Suit", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Gloves", "setGloves", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Gloves", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Weight", "setWeight", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Weight", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankVolume", "setTankVolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankVolume", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankType", "setTankType", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankType", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/EquipmentSets/EquipmentSet", "addEquipmentSet"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/DiveSites/DiveSite", DiveSite.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/privateId"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/publicId"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/publish"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/published"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/ownerId"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/spot"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/city"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/state"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/country"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/longitude"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/latitude"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/minDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/avgDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/maxDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/waters"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/timezone"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/description"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/directions"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/altitude"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/warnings"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/privateRemarks"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/siteType"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/mainDiveActivity"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/evaluation"); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/DiveSites/DiveSite", "addDiveSite"); // $NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/Masterdata", "setMasterdata"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/ExportSettings", ExportSettings.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/footerLink"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/footerLinkName"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexTitle"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexImages"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexGroupByYear", "groupByYear"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexDate"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexTime"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexLocation"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexCity"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexCountry"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexDuration"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexPictureCount"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailVisible"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailDate"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailTime"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailLocation"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailCity"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailCountry"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailWaters"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailDuration"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailEquipment"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailBuddy"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailComment"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailVisibility"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailProfile"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailProfileWidth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailProfileHeight"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureVisible"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureImageMaxWidth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureImageMaxHeight"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureThumbnailMaxWidth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureThumbnailMaxHeight"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/watermarkEnabled"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/watermarkText"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/skinFile"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/fullExport"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/keepImages"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/exportDirectory"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/scpEnabled"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/scpHost"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/scpDirectory"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/scpUser"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/scpPassword"); //$NON-NLS-1$
            d.addSetNext("JDiveLog/ExportSettings", "setExportSettings"); //$NON-NLS-1$ //$NON-NLS-2$
            
            d.addObjectCreate("JDiveLog/SlideshowSettings", SlideshowSettings.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/SlideshowSettings/cycletime"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/SlideshowSettings/repeat"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/SlideshowSettings/displayTitle"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/SlideshowSettings/displayDescription"); //$NON-NLS-1$
            d.addSetNext("JDiveLog/SlideshowSettings", "setSlideshowSettings"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addCallMethod("JDiveLog/ComputerDriver", "setComputerDriver", 1);//$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/ComputerDriver", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/ComputerSettings/property", "addComputerProperty", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/ComputerSettings/property", 0, "name"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/ComputerSettings/property", 1, "value"); //$NON-NLS-1$ //$NON-NLS-2$
            
            d.addObjectCreate("JDiveLog/StatisticSettings", StatisticSettings.class); //$NON-NLS-1$
            String chart = "buddyStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setBuddyStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "divePlaceStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setDivePlaceStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "countryStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setCountryStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "diveTypeStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setDiveTypeStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "diveActivityStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setDiveActivityStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings", "setStatisticSettings"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/GasBlendingSettings", GasBlendingSettings.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/tankVolume"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/currentPressure"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/currentOxygen"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/currentHelium"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/plannedPressure"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/plannedOxygen"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/plannedHelium"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/tankVolume"); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/GasBlendingSettings/gasSources/GasSource", GasSource.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/gasSources/GasSource/description"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/gasSources/GasSource/pressure"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/gasSources/GasSource/compressor"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/gasSources/GasSource/size"); //$NON-NLS-1$
            
            d.addCallMethod("JDiveLog/GasBlendingSettings/gasSources/GasSource/Mix", "setMix", 2, new Class[] {Double.class, Double.class});
            d.addCallParam("JDiveLog/GasBlendingSettings/gasSources/GasSource/Mix/oxygen", 0);
            d.addCallParam("JDiveLog/GasBlendingSettings/gasSources/GasSource/Mix/helium", 1);

            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/gasSources/GasSource/description"); //$NON-NLS-1$
            d.addSetNext("JDiveLog/GasBlendingSettings/gasSources/GasSource", "addGasSource"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/GasBlendingSettings", "setGasBlendingSettings"); //$NON-NLS-1$ //$NON-NLS-2$
            
            d.addObjectCreate("JDiveLog/JDive", JDive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DiveNum", "setDiveNumber", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DiveNum", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/UNITS", "setUnits", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/UNITS", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/JDive/diveSiteId"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/JDive/htmlExported"); // $NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Depth", "setDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Average_Depth", "setAverageDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Average_Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Duration", "setDuration", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Duration", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/amv", "setAMV", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/amv", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DiveType", "setDiveType", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DiveType", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DiveActivity", "setDiveActivity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DiveActivity", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Visibility", "setVisibility", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Visibility", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/surfaceTemperature", "setSurfaceTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/surfaceTemperature", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Buddy", "setBuddy", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Buddy", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Comment", "setComment", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Comment", 0); //$NON-NLS-1$

            d.addObjectCreate("JDiveLog/JDive/Equipment", Equipment.class); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/JDive/Equipment/Tanks/Tank", Tank.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/Type", "setType", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/JDive/Equipment/Tanks/Tank/MIX", Gas.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/MIXNAME", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/MIXNAME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/TANKVOLUME", "setTankvolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/TANKVOLUME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PSTART", "setPstart", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PSTART", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PEND", "setPend", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PEND", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/O2", "setOxygen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/O2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/N2", "setNitrogen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/N2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/HE", "setHelium", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/HE", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/Equipment/Tanks/Tank/MIX", "setGas"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/JDive/Equipment/Tanks/Tank", "addTank"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/Equipment/Weight", "setWeight", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Weight", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Suit", "setSuit", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Suit", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Gloves", "setGloves", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Gloves", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Comment", "setComment", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Comment", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/Equipment", "setEquipment"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/JDive/Pictures/Picture", Picture.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/Filename", "setFilename", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/Filename", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/Name", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/Name", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/Description", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/rotation", "setRotation", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/rotation", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/rating", "setRating", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/rating", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/Pictures/Picture", "addPicture"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/JDive/DIVE", Dive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DIVE/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DIVE/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DIVE/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SURFACEINTERVAL", "setSurfaceinterval", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SURFACEINTERVAL", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/surfaceTemperature", "setSurfaceTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/surfaceTemperature", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/DENSITY", "setDensity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/DENSITY", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/ALTITUDE", "setAltitude", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/ALTITUDE", 0); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/JDive/DIVE/GASES/MIX", Gas.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/MIXNAME", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/MIXNAME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", "setTankvolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PSTART", "setPstart", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PSTART", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PEND", "setPend", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PEND", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/O2", "setOxygen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/O2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/N2", "setNitrogen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/N2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/HE", "setHelium", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/HE", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/DIVE/GASES/MIX", "addGas"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/DIVE/TIMEDEPTHMODE", "setTimeDepthMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/DIVE/DELTAMODE", "setDeltaMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/DELTA", "addDelta", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/DELTA", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/SWITCH", "addSwitch", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/SWITCH", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/T", "addTime", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/T", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/D", "addDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/D", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/TEMPERATURE", "addTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/ALARM", "addAlarm", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/ALARM", 0); //$NON-NLS-1$

            d.addSetNext("JDiveLog/JDive/DIVE", "setDive"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/JDive", "addDive"); //$NON-NLS-1$ //$NON-NLS-2$
            
            // ignored dives
            d.addObjectCreate("JDiveLog/IgnoredDives/JDive", JDive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DiveNum", "setDiveNumber", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DiveNum", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/UNITS", "setUnits", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/UNITS", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/PLACE", "setPlace", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/PLACE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Country", "setCountry", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Country", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Depth", "setDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Average_Depth", "setAverageDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Average_Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Duration", "setDuration", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Duration", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/amv", "setAMV", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/amv", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DiveType", "setDiveType", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DiveType", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DiveActivity", "setDiveActivity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DiveActivity", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Visibility", "setVisibility", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Visibility", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/surfaceTemperature", "setSurfaceTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/surfaceTemperature", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Buddy", "setBuddy", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Buddy", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Comment", "setComment", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Comment", 0); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/IgnoredDives/JDive/DIVE", Dive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SURFACEINTERVAL", "setSurfaceinterval", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SURFACEINTERVAL", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/surfaceTemperature", "setSurfaceTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/surfaceTemperature", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/DENSITY", "setDensity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/DENSITY", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/ALTITUDE", "setAltitude", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/ALTITUDE", 0); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX", Gas.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/MIXNAME", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/MIXNAME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", "setTankvolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/PSTART", "setPstart", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/PSTART", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/PEND", "setPend", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/PEND", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/O2", "setOxygen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/O2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/N2", "setNitrogen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/N2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/HE", "setHelium", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/HE", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX", "addGas"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/TIMEDEPTHMODE", "setTimeDepthMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/DELTAMODE", "setDeltaMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/DELTA", "addDelta", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/DELTA", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/SWITCH", "addSwitch", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/SWITCH", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/T", "addTime", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/T", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/D", "addDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/D", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/TEMPERATURE", "addTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/ALARM", "addAlarm", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/ALARM", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/IgnoredDives/JDive/DIVE", "setDive"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/IgnoredDives/JDive", "addIgnoredDive"); //$NON-NLS-1$ //$NON-NLS-2$
            JDiveLog log = (JDiveLog) d.parse(file);
            return log;
        }
    }

    private class FileLoader2_6 implements FileLoader {
        public JDiveLog loadFile(File file) throws SAXException, IOException {
            Digester d = new Digester();
            d.setValidating(false);
            d.addObjectCreate("JDiveLog", JDiveLog.class); //$NON-NLS-1$

            d.addObjectCreate("JDiveLog/Masterdata", Masterdata.class); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/Masterdata/Buddys/Buddy", Buddy.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Buddys/Buddy/Firstname", "setFirstname", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Buddys/Buddy/Firstname", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Buddys/Buddy/Lastname", "setLastname", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Buddys/Buddy/Lastname", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Buddys/Buddy", "addBuddy"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/Divetypes/Divetype", DiveType.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Divetypes/Divetype/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Divetypes/Divetype/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Divetypes/Divetype", "addDivetype"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/Diveactivities/Diveactivity", DiveActivity.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Diveactivities/Diveactivity/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Diveactivities/Diveactivity/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Diveactivities/Diveactivity", "addDiveactivity"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/Suits/Suit", Suit.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Suits/Suit/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Suits/Suit/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Suits/Suit", "addSuit"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/GloveTypes/GloveType", GloveType.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/GloveTypes/GloveType/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/GloveTypes/GloveType/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/GloveTypes/GloveType", "addGloveType"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/EquipmentSets/EquipmentSet", EquipmentSet.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Name", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Name", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Suit", "setSuit", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Suit", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Gloves", "setGloves", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Gloves", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Weight", "setWeight", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Weight", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankVolume", "setTankVolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankVolume", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankType", "setTankType", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankType", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/EquipmentSets/EquipmentSet", "addEquipmentSet"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/DiveSites/DiveSite", DiveSite.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/privateId"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/publicId"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/publish"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/published"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/ownerId"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/spot"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/city"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/state"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/country"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/longitude"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/latitude"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/minDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/avgDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/maxDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/waters"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/timezone"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/description"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/directions"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/altitude"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/warnings"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/privateRemarks"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/siteType"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/mainDiveActivity"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/evaluation"); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/DiveSites/DiveSite", "addDiveSite"); // $NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/Masterdata/favoriteMixes/Mix", "addFavorite", 6, new Class[] {String.class, Integer.class, Integer.class, Double.class, Integer.class, Double.class});
            d.addCallParam("JDiveLog/Masterdata/favoriteMixes/Mix", 0, "name");
            d.addCallParam("JDiveLog/Masterdata/favoriteMixes/Mix", 1, "oxygen");
            d.addCallParam("JDiveLog/Masterdata/favoriteMixes/Mix", 2, "helium");
            d.addCallParam("JDiveLog/Masterdata/favoriteMixes/Mix", 3, "ppO2");
            d.addCallParam("JDiveLog/Masterdata/favoriteMixes/Mix", 4, "mod");
            d.addCallParam("JDiveLog/Masterdata/favoriteMixes/Mix", 5, "change");
            d.addSetNext("JDiveLog/Masterdata", "setMasterdata"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/ExportSettings", ExportSettings.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/footerLink"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/footerLinkName"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexTitle"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexImages"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexGroupByYear", "groupByYear"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexDate"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexTime"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexLocation"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexCity"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexCountry"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexDuration"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexPictureCount"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailVisible"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailDate"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailTime"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailLocation"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailCity"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailCountry"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailWaters"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailDuration"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailEquipment"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailBuddy"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailComment"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailVisibility"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailProfile"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailProfileWidth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailProfileHeight"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureVisible"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureImageMaxWidth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureImageMaxHeight"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureThumbnailMaxWidth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureThumbnailMaxHeight"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/imageQuality"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/watermarkEnabled"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/watermarkText"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/skinFile"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/fullExport"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/keepImages"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/exportDirectory"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/scpEnabled"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/scpHost"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/scpDirectory"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/scpUser"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/scpPassword"); //$NON-NLS-1$
            d.addSetNext("JDiveLog/ExportSettings", "setExportSettings"); //$NON-NLS-1$ //$NON-NLS-2$
            
            d.addObjectCreate("JDiveLog/SlideshowSettings", SlideshowSettings.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/SlideshowSettings/cycletime"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/SlideshowSettings/repeat"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/SlideshowSettings/displayTitle"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/SlideshowSettings/displayDescription"); //$NON-NLS-1$
            d.addSetNext("JDiveLog/SlideshowSettings", "setSlideshowSettings"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addCallMethod("JDiveLog/ComputerDriver", "setComputerDriver", 1);//$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/ComputerDriver", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/ComputerSettings/property", "addComputerProperty", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/ComputerSettings/property", 0, "name"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/ComputerSettings/property", 1, "value"); //$NON-NLS-1$ //$NON-NLS-2$
            
            d.addCallMethod("JDiveLog/ComputerDownloadInterval", "setComputerDownloadInterval", 1, new Class[] {Integer.class});
            d.addCallParam("JDiveLog/ComputerDownloadInterval", 0);
            
            d.addObjectCreate("JDiveLog/StatisticSettings", StatisticSettings.class); //$NON-NLS-1$
            String chart = "buddyStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setBuddyStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "divePlaceStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setDivePlaceStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "countryStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setCountryStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "diveTypeStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setDiveTypeStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "diveActivityStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setDiveActivityStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "watersStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setWatersStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings", "setStatisticSettings"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/GasBlendingSettings", GasBlendingSettings.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/tankVolume"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/currentPressure"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/currentOxygen"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/currentHelium"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/plannedPressure"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/plannedOxygen"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/plannedHelium"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/tankVolume"); //$NON-NLS-1$
            
            d.addCallMethod("JDiveLog/GasBlendingSettings/current/Mix", "setCurrentMix", 6, new Class[] {String.class, Integer.class, Integer.class, Double.class, Integer.class, Double.class});
            d.addCallParam("JDiveLog/GasBlendingSettings/current/Mix", 0, "name");
            d.addCallParam("JDiveLog/GasBlendingSettings/current/Mix", 1, "oxygen");
            d.addCallParam("JDiveLog/GasBlendingSettings/current/Mix", 2, "helium");
            d.addCallParam("JDiveLog/GasBlendingSettings/current/Mix", 3, "ppO2");
            d.addCallParam("JDiveLog/GasBlendingSettings/current/Mix", 4, "mod");
            d.addCallParam("JDiveLog/GasBlendingSettings/current/Mix", 5, "change");
            
            d.addCallMethod("JDiveLog/GasBlendingSettings/planned/Mix", "setPlannedMix", 6, new Class[] {String.class, Integer.class, Integer.class, Double.class, Integer.class, Double.class});
            d.addCallParam("JDiveLog/GasBlendingSettings/planned/Mix", 0, "name");
            d.addCallParam("JDiveLog/GasBlendingSettings/planned/Mix", 1, "oxygen");
            d.addCallParam("JDiveLog/GasBlendingSettings/planned/Mix", 2, "helium");
            d.addCallParam("JDiveLog/GasBlendingSettings/planned/Mix", 3, "ppO2");
            d.addCallParam("JDiveLog/GasBlendingSettings/planned/Mix", 4, "mod");
            d.addCallParam("JDiveLog/GasBlendingSettings/planned/Mix", 5, "change");

            d.addObjectCreate("JDiveLog/GasBlendingSettings/gasSources/GasSource", GasSource.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/gasSources/GasSource/enabled"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/gasSources/GasSource/description"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/gasSources/GasSource/pressure"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/gasSources/GasSource/compressor"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/gasSources/GasSource/size"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/gasSources/GasSource/price1"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/gasSources/GasSource/price2"); //$NON-NLS-1$

            // Compatibility 2.6 version
            d.addCallMethod("JDiveLog/GasBlendingSettings/gasSources/GasSource/Mix", "setMix", 2, new Class[] {Double.class, Double.class});
            d.addCallParam("JDiveLog/GasBlendingSettings/gasSources/GasSource/Mix/oxygen", 0);
            d.addCallParam("JDiveLog/GasBlendingSettings/gasSources/GasSource/Mix/helium", 1);

            // newer versions
            d.addCallMethod("JDiveLog/GasBlendingSettings/gasSources/GasSource/Mix", "setMix", 2, new Class[] {Double.class, Double.class});
            d.addCallParam("JDiveLog/GasBlendingSettings/gasSources/GasSource/Mix", 0, "oxygen");
            d.addCallParam("JDiveLog/GasBlendingSettings/gasSources/GasSource/Mix", 1, "helium");

            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/gasSources/GasSource/description"); //$NON-NLS-1$
            d.addSetNext("JDiveLog/GasBlendingSettings/gasSources/GasSource", "addGasSource"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/GasBlendingSettings", "setGasBlendingSettings"); //$NON-NLS-1$ //$NON-NLS-2$
            
            d.addObjectCreate("JDiveLog/ProfileSettings", ProfileSettings.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/backgroundColorCode"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/gridColorCode"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/fillDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/showDeco"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/decoColorCode"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/showTemperature"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/temperatureLabelColorCode"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/temperatureColorCodeBegin"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/temperatureColorCodeEnd"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/showPpo2"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/ppo2Color1Code"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/ppo2Color2Code"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/ppo2Color3Code"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/ppo2Color4Code"); //$NON-NLS-1$
            d.addSetNext("JDiveLog/ProfileSettings", "setProfileSettings"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/JDive", JDive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DiveNum", "setDiveNumber", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DiveNum", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/UNITS", "setUnits", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/UNITS", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/JDive/diveSiteId"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/JDive/htmlExported"); // $NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Depth", "setDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Average_Depth", "setAverageDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Average_Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Duration", "setDuration", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Duration", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/amv", "setAMV", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/amv", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DiveType", "setDiveType", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DiveType", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DiveActivity", "setDiveActivity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DiveActivity", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Visibility", "setVisibility", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Visibility", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/surfaceTemperature", "setSurfaceTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/surfaceTemperature", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Buddy", "setBuddy", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Buddy", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Comment", "setComment", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Comment", 0); //$NON-NLS-1$

            d.addObjectCreate("JDiveLog/JDive/Equipment", Equipment.class); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/JDive/Equipment/Tanks/Tank", Tank.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/Type", "setType", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/JDive/Equipment/Tanks/Tank/MIX", Gas.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/MIXNAME", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/MIXNAME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/TANKVOLUME", "setTankvolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/TANKVOLUME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PSTART", "setPstart", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PSTART", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PEND", "setPend", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PEND", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/O2", "setOxygen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/O2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/N2", "setNitrogen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/N2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/HE", "setHelium", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/HE", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/Equipment/Tanks/Tank/MIX", "setGas"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/JDive/Equipment/Tanks/Tank", "addTank"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/Equipment/Weight", "setWeight", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Weight", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Suit", "setSuit", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Suit", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Gloves", "setGloves", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Gloves", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Comment", "setComment", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Comment", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/Equipment", "setEquipment"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/JDive/Pictures/Picture", Picture.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/Filename", "setFilename", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/Filename", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/Name", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/Name", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/Description", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/rotation", "setRotation", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/rotation", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/rating", "setRating", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/rating", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/Pictures/Picture", "addPicture"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/JDive/DIVE", Dive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DIVE/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DIVE/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DIVE/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SURFACEINTERVAL", "setSurfaceinterval", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SURFACEINTERVAL", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/surfaceTemperature", "setSurfaceTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/surfaceTemperature", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/DENSITY", "setDensity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/DENSITY", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/ALTITUDE", "setAltitude", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/ALTITUDE", 0); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/JDive/DIVE/GASES/MIX", Gas.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/MIXNAME", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/MIXNAME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", "setTankvolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PSTART", "setPstart", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PSTART", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PEND", "setPend", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PEND", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/O2", "setOxygen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/O2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/N2", "setNitrogen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/N2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/HE", "setHelium", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/HE", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/DIVE/GASES/MIX", "addGas"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/DIVE/TIMEDEPTHMODE", "setTimeDepthMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/DIVE/DELTAMODE", "setDeltaMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/DELTA", "addDelta", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/DELTA", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/SWITCH", "addSwitch", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/SWITCH", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/T", "addTime", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/T", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/D", "addDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/D", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/TEMPERATURE", "addTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/ALARM", "addAlarm", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/ALARM", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/DECOINFO", "addDecoInfo", 3);
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/DECOINFO", 0);
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/DECOINFO", 1, "tfs");
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/DECOINFO", 2, "tts");
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/PPO2", "addPPO2", 2);
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/PPO2", 0, "sensor");
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/PPO2", 1);

            d.addSetNext("JDiveLog/JDive/DIVE", "setDive"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/JDive", "addDive"); //$NON-NLS-1$ //$NON-NLS-2$
            
            // ignored dives
            d.addObjectCreate("JDiveLog/IgnoredDives/JDive", JDive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DiveNum", "setDiveNumber", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DiveNum", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/UNITS", "setUnits", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/UNITS", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/PLACE", "setPlace", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/PLACE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Country", "setCountry", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Country", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Depth", "setDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Average_Depth", "setAverageDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Average_Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Duration", "setDuration", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Duration", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/amv", "setAMV", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/amv", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DiveType", "setDiveType", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DiveType", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DiveActivity", "setDiveActivity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DiveActivity", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Visibility", "setVisibility", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Visibility", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/surfaceTemperature", "setSurfaceTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/surfaceTemperature", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Buddy", "setBuddy", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Buddy", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Comment", "setComment", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Comment", 0); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/IgnoredDives/JDive/DIVE", Dive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SURFACEINTERVAL", "setSurfaceinterval", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SURFACEINTERVAL", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/surfaceTemperature", "setSurfaceTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/surfaceTemperature", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/DENSITY", "setDensity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/DENSITY", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/ALTITUDE", "setAltitude", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/ALTITUDE", 0); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX", Gas.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/MIXNAME", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/MIXNAME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", "setTankvolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/PSTART", "setPstart", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/PSTART", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/PEND", "setPend", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/PEND", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/O2", "setOxygen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/O2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/N2", "setNitrogen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/N2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/HE", "setHelium", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/HE", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX", "addGas"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/TIMEDEPTHMODE", "setTimeDepthMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/DELTAMODE", "setDeltaMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/DELTA", "addDelta", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/DELTA", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/SWITCH", "addSwitch", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/SWITCH", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/T", "addTime", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/T", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/D", "addDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/D", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/TEMPERATURE", "addTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/ALARM", "addAlarm", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/ALARM", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/DECOINFO", "addDecoInfo", 3);
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/DECOINFO", 0);
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/DECOINFO", 1, "tfs");
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/DECOINFO", 2, "tts");
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/PPO2", "addPPO2", 2);
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/PPO2", 0, "sensor");
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/PPO2", 1);
            d.addSetNext("JDiveLog/IgnoredDives/JDive/DIVE", "setDive"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/IgnoredDives/JDive", "addIgnoredDive"); //$NON-NLS-1$ //$NON-NLS-2$
            JDiveLog log = (JDiveLog) d.parse(file);
            return log;
        }
    }


    private class FileLoader2_13 implements FileLoader {
        public JDiveLog loadFile(File file) throws SAXException, IOException {
            Digester d = new Digester();
            d.setValidating(false);
            d.addObjectCreate("JDiveLog", JDiveLog.class); //$NON-NLS-1$

            d.addObjectCreate("JDiveLog/Masterdata", Masterdata.class); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/Masterdata/Buddys/Buddy", Buddy.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Buddys/Buddy/Firstname", "setFirstname", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Buddys/Buddy/Firstname", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Buddys/Buddy/Lastname", "setLastname", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Buddys/Buddy/Lastname", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Buddys/Buddy", "addBuddy"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/Divetypes/Divetype", DiveType.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Divetypes/Divetype/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Divetypes/Divetype/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Divetypes/Divetype", "addDivetype"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/Diveactivities/Diveactivity", DiveActivity.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Diveactivities/Diveactivity/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Diveactivities/Diveactivity/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Diveactivities/Diveactivity", "addDiveactivity"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/Suits/Suit", Suit.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/Suits/Suit/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/Suits/Suit/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/Suits/Suit", "addSuit"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/GloveTypes/GloveType", GloveType.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/GloveTypes/GloveType/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/GloveTypes/GloveType/Description", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/GloveTypes/GloveType", "addGloveType"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/EquipmentSets/EquipmentSet", EquipmentSet.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Name", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Name", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Suit", "setSuit", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Suit", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Gloves", "setGloves", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Gloves", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Weight", "setWeight", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/Weight", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankVolume", "setTankVolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankVolume", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankType", "setTankType", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/Masterdata/EquipmentSets/EquipmentSet/TankType", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/EquipmentSets/EquipmentSet", "addEquipmentSet"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/Masterdata/DiveSites/DiveSite", DiveSite.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/privateId"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/publicId"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/publish"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/published"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/ownerId"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/spot"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/city"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/state"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/country"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/longitude"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/latitude"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/minDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/avgDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/maxDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/waters"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/timezone"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/description"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/directions"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/altitude"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/warnings"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/privateRemarks"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/siteType"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/mainDiveActivity"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/Masterdata/DiveSites/DiveSite/evaluation"); //$NON-NLS-1$
            d.addSetNext("JDiveLog/Masterdata/DiveSites/DiveSite", "addDiveSite"); // $NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/Masterdata/favoriteMixes/Mix", "addFavorite", 6, new Class[] {String.class, Integer.class, Integer.class, Double.class, Integer.class, Double.class});
            d.addCallParam("JDiveLog/Masterdata/favoriteMixes/Mix", 0, "name");
            d.addCallParam("JDiveLog/Masterdata/favoriteMixes/Mix", 1, "oxygen");
            d.addCallParam("JDiveLog/Masterdata/favoriteMixes/Mix", 2, "helium");
            d.addCallParam("JDiveLog/Masterdata/favoriteMixes/Mix", 3, "ppO2");
            d.addCallParam("JDiveLog/Masterdata/favoriteMixes/Mix", 4, "mod");
            d.addCallParam("JDiveLog/Masterdata/favoriteMixes/Mix", 5, "change");
            d.addSetNext("JDiveLog/Masterdata", "setMasterdata"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/ExportSettings", ExportSettings.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/footerLink"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/footerLinkName"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexTitle"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexImages"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexGroupByYear", "groupByYear"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexDate"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexTime"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexLocation"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexCity"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexCountry"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexDuration"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/indexPictureCount"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailVisible"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailDate"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailTime"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailLocation"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailCity"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailCountry"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailWaters"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailDuration"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailEquipment"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailBuddy"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailComment"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailVisibility"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailProfile"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailProfileWidth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/detailProfileHeight"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureVisible"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureImageMaxWidth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureImageMaxHeight"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureThumbnailMaxWidth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/pictureThumbnailMaxHeight"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/imageQuality"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/watermarkEnabled"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/watermarkText"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/skinFile"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/fullExport"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/keepImages"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/exportDirectory"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/scpEnabled"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/scpHost"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/scpDirectory"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/scpUser"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ExportSettings/scpPassword"); //$NON-NLS-1$
            d.addSetNext("JDiveLog/ExportSettings", "setExportSettings"); //$NON-NLS-1$ //$NON-NLS-2$
            
            d.addObjectCreate("JDiveLog/SlideshowSettings", SlideshowSettings.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/SlideshowSettings/cycletime"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/SlideshowSettings/repeat"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/SlideshowSettings/displayTitle"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/SlideshowSettings/displayDescription"); //$NON-NLS-1$
            d.addSetNext("JDiveLog/SlideshowSettings", "setSlideshowSettings"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addCallMethod("JDiveLog/ComputerDriver", "setComputerDriver", 1);//$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/ComputerDriver", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/ComputerSettings/property", "addComputerProperty", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/ComputerSettings/property", 0, "name"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/ComputerSettings/property", 1, "value"); //$NON-NLS-1$ //$NON-NLS-2$
            
            d.addCallMethod("JDiveLog/ComputerDownloadInterval", "setComputerDownloadInterval", 1, new Class[] {Integer.class});
            d.addCallParam("JDiveLog/ComputerDownloadInterval", 0);
            
            d.addObjectCreate("JDiveLog/StatisticSettings", StatisticSettings.class); //$NON-NLS-1$
            String chart = "buddyStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setBuddyStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "divePlaceStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setDivePlaceStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "countryStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setCountryStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "diveTypeStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setDiveTypeStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "diveActivityStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setDiveActivityStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            chart = "watersStatistic";
            d.addObjectCreate("JDiveLog/StatisticSettings/"+chart, ChartSettings.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/type", "setType", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/type", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/StatisticSettings/"+chart+"/orientation", "setOrientation", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            d.addCallParam("JDiveLog/StatisticSettings/"+chart+"/orientation", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings/"+chart, "setWatersStatistic"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/StatisticSettings", "setStatisticSettings"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/GasBlendingSettings", GasBlendingSettings.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/tankVolume"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/currentPressure"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/currentOxygen"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/currentHelium"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/plannedPressure"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/plannedOxygen"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/plannedHelium"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/tankVolume"); //$NON-NLS-1$

            d.addObjectCreate("JDiveLog/GasOverflowSettings", GasOverflowSettings.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasOverflowSettings/startTankSize"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasOverflowSettings/startTankPressure"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasOverflowSettings/plannedConsumptionPressure"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasOverflowSettings/minStartPressure"); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/GasOverflowSettings/gasOverflowSources/GasOverflowSource", GasOverflowSource.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasOverflowSettings/gasOverflowSources/GasOverflowSource/tankdescription"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasOverflowSettings/gasOverflowSources/GasOverflowSource/tankpressure"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasOverflowSettings/gasOverflowSources/GasOverflowSource/tanksize"); //$NON-NLS-1$
            d.addSetNext("JDiveLog/GasOverflowSettings/gasOverflowSources/GasOverflowSource", "addGasSource"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/GasOverflowSettings", "setGasOverflowSettings"); //$NON-NLS-1$ //$NON-NLS-2$


            d.addCallMethod("JDiveLog/GasBlendingSettings/current/Mix", "setCurrentMix", 6, new Class[] {String.class, Integer.class, Integer.class, Double.class, Integer.class, Double.class});
            d.addCallParam("JDiveLog/GasBlendingSettings/current/Mix", 0, "name");
            d.addCallParam("JDiveLog/GasBlendingSettings/current/Mix", 1, "oxygen");
            d.addCallParam("JDiveLog/GasBlendingSettings/current/Mix", 2, "helium");
            d.addCallParam("JDiveLog/GasBlendingSettings/current/Mix", 3, "ppO2");
            d.addCallParam("JDiveLog/GasBlendingSettings/current/Mix", 4, "mod");
            d.addCallParam("JDiveLog/GasBlendingSettings/current/Mix", 5, "change");
            
            d.addCallMethod("JDiveLog/GasBlendingSettings/planned/Mix", "setPlannedMix", 6, new Class[] {String.class, Integer.class, Integer.class, Double.class, Integer.class, Double.class});
            d.addCallParam("JDiveLog/GasBlendingSettings/planned/Mix", 0, "name");
            d.addCallParam("JDiveLog/GasBlendingSettings/planned/Mix", 1, "oxygen");
            d.addCallParam("JDiveLog/GasBlendingSettings/planned/Mix", 2, "helium");
            d.addCallParam("JDiveLog/GasBlendingSettings/planned/Mix", 3, "ppO2");
            d.addCallParam("JDiveLog/GasBlendingSettings/planned/Mix", 4, "mod");
            d.addCallParam("JDiveLog/GasBlendingSettings/planned/Mix", 5, "change");

            d.addObjectCreate("JDiveLog/GasBlendingSettings/gasSources/GasSource", GasSource.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/gasSources/GasSource/enabled"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/gasSources/GasSource/description"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/gasSources/GasSource/pressure"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/gasSources/GasSource/compressor"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/gasSources/GasSource/size"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/gasSources/GasSource/price1"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/gasSources/GasSource/price2"); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/GasBlendingSettings/gasSources/GasSource/Mix", "setMix", 6, new Class[] {String.class, Integer.class, Integer.class, Double.class, Integer.class, Double.class});
            d.addCallParam("JDiveLog/GasBlendingSettings/gasSources/GasSource/Mix", 0, "name");
            d.addCallParam("JDiveLog/GasBlendingSettings/gasSources/GasSource/Mix", 1, "oxygen");
            d.addCallParam("JDiveLog/GasBlendingSettings/gasSources/GasSource/Mix", 2, "helium");
            d.addCallParam("JDiveLog/GasBlendingSettings/gasSources/GasSource/Mix", 3, "ppO2");
            d.addCallParam("JDiveLog/GasBlendingSettings/gasSources/GasSource/Mix", 4, "mod");
            d.addCallParam("JDiveLog/GasBlendingSettings/gasSources/GasSource/Mix", 5, "change");
            d.addBeanPropertySetter("JDiveLog/GasBlendingSettings/gasSources/GasSource/description"); //$NON-NLS-1$
            d.addSetNext("JDiveLog/GasBlendingSettings/gasSources/GasSource", "addGasSource"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/GasBlendingSettings", "setGasBlendingSettings"); //$NON-NLS-1$ //$NON-NLS-2$
            
            d.addObjectCreate("JDiveLog/ProfileSettings", ProfileSettings.class); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/backgroundColorCode"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/gridColorCode"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/fillDepth"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/showDeco"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/decoColorCode"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/showTemperature"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/temperatureLabelColorCode"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/temperatureColorCodeBegin"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/temperatureColorCodeEnd"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/showPpo2"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/ppo2Color1Code"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/ppo2Color2Code"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/ppo2Color3Code"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/ProfileSettings/ppo2Color4Code"); //$NON-NLS-1$
            d.addSetNext("JDiveLog/ProfileSettings", "setProfileSettings"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/JDive", JDive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DiveNum", "setDiveNumber", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DiveNum", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/UNITS", "setUnits", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/UNITS", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/JDive/diveSiteId"); //$NON-NLS-1$
            d.addBeanPropertySetter("JDiveLog/JDive/htmlExported"); // $NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Depth", "setDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Average_Depth", "setAverageDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Average_Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Duration", "setDuration", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Duration", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/amv", "setAMV", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/amv", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DiveType", "setDiveType", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DiveType", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DiveActivity", "setDiveActivity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DiveActivity", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Visibility", "setVisibility", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Visibility", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/surfaceTemperature", "setSurfaceTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/surfaceTemperature", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Buddy", "setBuddy", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Buddy", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Comment", "setComment", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Comment", 0); //$NON-NLS-1$

            d.addObjectCreate("JDiveLog/JDive/Equipment", Equipment.class); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/JDive/Equipment/Tanks/Tank", Tank.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/Type", "setType", 0); //$NON-NLS-1$ //$NON-NLS-2$
            d.addObjectCreate("JDiveLog/JDive/Equipment/Tanks/Tank/MIX", Gas.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/MIXNAME", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/MIXNAME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/TANKVOLUME", "setTankvolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/TANKVOLUME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PSTART", "setPstart", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PSTART", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PEND", "setPend", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/TANK/PEND", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/O2", "setOxygen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/O2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/N2", "setNitrogen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/N2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/HE", "setHelium", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Tanks/Tank/MIX/HE", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/Equipment/Tanks/Tank/MIX", "setGas"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/JDive/Equipment/Tanks/Tank", "addTank"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/Equipment/Weight", "setWeight", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Weight", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Suit", "setSuit", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Suit", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Gloves", "setGloves", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Gloves", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Equipment/Comment", "setComment", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Equipment/Comment", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/Equipment", "setEquipment"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/JDive/Pictures/Picture", Picture.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/Filename", "setFilename", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/Filename", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/Name", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/Name", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/Description", "setDescription", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/Description", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/rotation", "setRotation", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/rotation", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/Pictures/Picture/rating", "setRating", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/Pictures/Picture/rating", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/Pictures/Picture", "addPicture"); //$NON-NLS-1$ //$NON-NLS-2$

            d.addObjectCreate("JDiveLog/JDive/DIVE", Dive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DIVE/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DIVE/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/JDive/DIVE/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SURFACEINTERVAL", "setSurfaceinterval", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SURFACEINTERVAL", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/surfaceTemperature", "setSurfaceTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/surfaceTemperature", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/DENSITY", "setDensity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/DENSITY", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/ALTITUDE", "setAltitude", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/ALTITUDE", 0); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/JDive/DIVE/GASES/MIX", Gas.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/MIXNAME", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/MIXNAME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", "setTankvolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PSTART", "setPstart", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PSTART", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PEND", "setPend", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/TANK/PEND", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/O2", "setOxygen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/O2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/N2", "setNitrogen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/N2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/GASES/MIX/HE", "setHelium", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/GASES/MIX/HE", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/JDive/DIVE/GASES/MIX", "addGas"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/DIVE/TIMEDEPTHMODE", "setTimeDepthMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/DIVE/DELTAMODE", "setDeltaMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/DELTA", "addDelta", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/DELTA", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/SWITCH", "addSwitch", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/SWITCH", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/T", "addTime", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/T", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/D", "addDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/D", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/TEMPERATURE", "addTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/ALARM", "addAlarm", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/ALARM", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/DECOINFO", "addDecoInfo", 3);
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/DECOINFO", 0);
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/DECOINFO", 1, "tfs");
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/DECOINFO", 2, "tts");
            d.addCallMethod("JDiveLog/JDive/DIVE/SAMPLES/PPO2", "addPPO2", 2);
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/PPO2", 0, "sensor");
            d.addCallParam("JDiveLog/JDive/DIVE/SAMPLES/PPO2", 1);

            d.addSetNext("JDiveLog/JDive/DIVE", "setDive"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/JDive", "addDive"); //$NON-NLS-1$ //$NON-NLS-2$
            
            // ignored dives
            d.addObjectCreate("JDiveLog/IgnoredDives/JDive", JDive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DiveNum", "setDiveNumber", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DiveNum", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/UNITS", "setUnits", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/UNITS", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/PLACE", "setPlace", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/PLACE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Country", "setCountry", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Country", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Depth", "setDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Average_Depth", "setAverageDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Average_Depth", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Duration", "setDuration", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Duration", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/amv", "setAMV", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/amv", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DiveType", "setDiveType", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DiveType", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DiveActivity", "setDiveActivity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DiveActivity", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Visibility", "setVisibility", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Visibility", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/surfaceTemperature", "setSurfaceTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/surfaceTemperature", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Buddy", "setBuddy", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Buddy", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/Comment", "setComment", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/Comment", 0); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/IgnoredDives/JDive/DIVE", Dive.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/DATE", "setDate", 3); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/DATE/YEAR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/DATE/MONTH", 1); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/DATE/DAY", 2); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/TIME", "setTime", 2); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/TIME/HOUR", 0); //$NON-NLS-1$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/TIME/MINUTE", 1); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SURFACEINTERVAL", "setSurfaceinterval", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SURFACEINTERVAL", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/TEMPERATURE", "setTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/surfaceTemperature", "setSurfaceTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/surfaceTemperature", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/DENSITY", "setDensity", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/DENSITY", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/ALTITUDE", "setAltitude", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/ALTITUDE", 0); //$NON-NLS-1$
            d.addObjectCreate("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX", Gas.class); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/MIXNAME", "setName", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/MIXNAME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", "setTankvolume", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/TANKVOLUME", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/PSTART", "setPstart", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/PSTART", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/PEND", "setPend", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/TANK/PEND", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/O2", "setOxygen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/O2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/N2", "setNitrogen", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/N2", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/HE", "setHelium", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX/HE", 0); //$NON-NLS-1$
            d.addSetNext("JDiveLog/IgnoredDives/JDive/DIVE/GASES/MIX", "addGas"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/TIMEDEPTHMODE", "setTimeDepthMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/DELTAMODE", "setDeltaMode"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/DELTA", "addDelta", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/DELTA", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/SWITCH", "addSwitch", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/SWITCH", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/T", "addTime", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/T", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/D", "addDepth", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/D", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/TEMPERATURE", "addTemperature", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/TEMPERATURE", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/ALARM", "addAlarm", 1); //$NON-NLS-1$ //$NON-NLS-2$
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/ALARM", 0); //$NON-NLS-1$
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/DECOINFO", "addDecoInfo", 3);
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/DECOINFO", 0);
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/DECOINFO", 1, "tfs");
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/DECOINFO", 2, "tts");
            d.addCallMethod("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/PPO2", "addPPO2", 2);
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/PPO2", 0, "sensor");
            d.addCallParam("JDiveLog/IgnoredDives/JDive/DIVE/SAMPLES/PPO2", 1);
            d.addSetNext("JDiveLog/IgnoredDives/JDive/DIVE", "setDive"); //$NON-NLS-1$ //$NON-NLS-2$
            d.addSetNext("JDiveLog/IgnoredDives/JDive", "addIgnoredDive"); //$NON-NLS-1$ //$NON-NLS-2$
            JDiveLog log = (JDiveLog) d.parse(file);
            return log;
        }
    }

}
