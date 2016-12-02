/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DiveProfile.java
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

import java.applet.Applet;
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Map.Entry;

import javax.swing.JComponent;

import net.sf.jdivelog.gui.resources.Messages;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.ProfileSettings;
import net.sf.jdivelog.model.udcf.Alarm;
import net.sf.jdivelog.model.udcf.DecoInfo;
import net.sf.jdivelog.model.udcf.Delta;
import net.sf.jdivelog.model.udcf.Depth;
import net.sf.jdivelog.model.udcf.Dive;
import net.sf.jdivelog.model.udcf.Gas;
import net.sf.jdivelog.model.udcf.PPO2;
import net.sf.jdivelog.model.udcf.Sample;
import net.sf.jdivelog.model.udcf.Switch;
import net.sf.jdivelog.model.udcf.Temperature;
import net.sf.jdivelog.model.udcf.Time;
import net.sf.jdivelog.util.ColorConverter;
import net.sf.jdivelog.util.UnitConverter;

/**
 * Canvas displaying a dive profile.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class DiveProfile extends JComponent {

    private static final BasicStroke PPO2_STROKE = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1, new float[] { 1, 3 }, 0);

    private static final long serialVersionUID = 3257565101040088629L;

    private static final DecimalFormat DEPTH_FORMAT = new DecimalFormat("##0.0");

    private static final DecimalFormat SECOND_FORMAT = new DecimalFormat("00");

    private static final int segments = 20;

    private static Color[] MIX_COLORS;

    {
        String colors = Messages.getString("profile.colors");
        if (colors == null) {
            MIX_COLORS = new Color[] { Color.BLACK, Color.PINK, Color.MAGENTA, Color.ORANGE, Color.CYAN, Color.PINK, Color.RED, Color.DARK_GRAY, Color.YELLOW };
        } else {
            StringTokenizer st = new StringTokenizer(colors, " ,;:");
            ArrayList<Color> colorsList = new ArrayList<Color>();
            while (st.hasMoreTokens()) {
                String colorString = st.nextToken();
                Color c = ColorConverter.convertColor(colorString);
                colorsList.add(c);
            }
            MIX_COLORS = new Color[colorsList.size()];
            colorsList.toArray(MIX_COLORS);
        }
    }

    private Image alarmImage;

    private Image decoImage;

    private Image bookmarkImage;

    private Image surfaceImage;

    private Image attentionImage;

    private Image slowImage;

    private Image decoceilingpassedImage;

    private Image endofImage;

    private Image safetystopceilingpassedImage;

    private final int LEFTLABEL_SIZE = 200;

    private final int RIGHTLABEL_SIZE = 80;

    private final int TOPLABEL_SIZE = 20;

    private final int BOTTOMLABEL_SIZE = 120;

    private final int GRID_X = 12;

    private final int GRID_Y = 8;

    private final double TIME_PRECISION = 0.5;

    private final double PPO2_PRECISION = 0.25;

    private final double ALT_PRECISION = 0.5;

    private JDive dive = null;

    private Double maxTime = null;

    private Double maxDepth = null;

    private Double maxTemp = null;

    private double maxPPO2;

    private ArrayList<Gas> gasList = null;

    private HashMap<Integer, String> gases = null;

    private HashMap<Integer, TreeSet<TimeDepth>> depths = null;

    private HashMap<String, TreeSet<TimeDepth>> ppo2s = null;
    
    private HashMap<Integer, String> time_ppo2s = null;

    private TreeSet<TimeAlarm> alarms = null;

    private HashMap<Rectangle, String> alarmPositions = null;

    private TreeSet<TimeTemperature> temperatures = null;

    private TreeSet<TimeDepth> decoinfos;

    private double timeScale = 1;

    private double ppo2Scale = 1;

    private double altScale = 1;

    private boolean isCrossHairMode;

    private ArrayList<DiveProfile> comparisonProfiles;

    private boolean comparisonProfile = false;

    private boolean drawTemp = false;

    private boolean drawDeco = false;

    private Double[] temperatureSegments;

    private Double temperatureSegmentMax;

    private Double temperatureSegmentMin;

    private final ProfileSettings profileSettings;

    public DiveProfile(ProfileSettings profileSettings) {
        this.profileSettings = profileSettings;
        setSize(10, 10);
        comparisonProfiles = new ArrayList<DiveProfile>();
        isCrossHairMode = false;
    }

    public DiveProfile(ProfileSettings profileSettings, JDive dive) {
        this(profileSettings, dive, false);
    }

    public DiveProfile(ProfileSettings profileSettings, JDive dive, boolean comparisonProfile) {
        this(profileSettings);
        this.comparisonProfile = comparisonProfile;
        setDive(dive);
    }

    public void setDive(JDive dive) {
        this.dive = dive;
        if (getParent() != null) {
            TooltipListener tl = new TooltipListener(this);
            this.addMouseMotionListener(tl);
        }
        initialize();
    }

    public void addComparisonProfile(JDive dive) {
        comparisonProfiles.add(new DiveProfile(profileSettings, dive, true));
        initialize();
        repaint();
    }
    
    public void removeComparisonProfiles() {
        comparisonProfiles.clear();
        initialize();
        repaint();
    }

    public void setCrossHairMode(boolean isCrossHairMode) {
        this.isCrossHairMode = isCrossHairMode;
    }

    public boolean isCrossHairMode() {
        return isCrossHairMode;
    }

    public void paint(Graphics g) {
        Dimension d = getPreferredSize();
        if (d.getWidth() > 0 && d.getHeight() > 0) {
            BufferedImage img = new BufferedImage(new Double(d.getWidth()).intValue(), new Double(d.getHeight()).intValue(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = img.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setBackground(profileSettings.getBackgroundColor());
            g2d.clearRect(0, 0, new Double(d.getWidth()).intValue(), new Double(d.getHeight()).intValue());
            drawTemperatureGraph(g2d);
            drawGrid(g2d);
            drawPPO2Graph(g2d);
            drawDecoGraph(g2d);
            drawDepthGraphs(this, g2d, new PositionColorIndex(), comparisonProfile);
            drawAlarms(g2d);
            if (this.dive.getDive() == null) {
                drawNoProfileData(g2d);
            }
            g.drawImage(img, 0, 0, new Double(d.getWidth()).intValue(), new Double(d.getHeight()).intValue(), profileSettings.getBackgroundColor(), this);
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.getSize().width,this.getSize().height);
    }

    //
    // private methods
    //

    private void initialize() {
        this.maxTime = null;
        this.maxDepth = null;
        this.maxTemp = null;
        this.gasList = null;
        this.gases = null;
        this.depths = null;
        this.ppo2s = null;
        this.alarms = null;
        this.temperatures = null;
        this.decoinfos = null;
        this.maxPPO2 = 2.0;
        // set the icons for alarmposititions
        alarmImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/alarm.png")); //$NON-NLS-1$
        decoImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/deco.png")); //$NON-NLS-1$
        bookmarkImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/bookmark.png")); //$NON-NLS-1$
        surfaceImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/surface.png")); //$NON-NLS-1$
        attentionImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/alarm.png")); //$NON-NLS-1$
        slowImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/slow.png")); //$NON-NLS-1$
        decoceilingpassedImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/decoceilingpassed.png")); //$NON-NLS-1$
        endofImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/alarm.png")); //$NON-NLS-1$
        safetystopceilingpassedImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/net/sf/jdivelog/gui/resources/icons/alarm.png")); //$NON-NLS-1$
        alarmPositions = new HashMap<Rectangle, String>();

        if (this.dive.getDive() != null && this.dive.getDive().getSamples() != null) {
            Iterator<Gas> it = this.dive.getDive().getGases().iterator();
            while (it.hasNext()) {
                Gas gas = it.next();
                addMix(gas);
            }
            if (dive.getDive().getMode() == Dive.MODE_TIME) {
                parseSamplesTimeMode();
            } else {
                parseSamplesDeltaMode();
            }
            Double maxTime = this.maxTime;
            Double maxTemp = this.maxTemp;
            Double maxDepth = this.maxDepth;
            Iterator<DiveProfile> compIt = comparisonProfiles.iterator();
            while (compIt.hasNext()) {
                DiveProfile profile = compIt.next();
                profile.setPreferredSize(getPreferredSize());
                profile.initialize();
                if (profile.maxTime != null && profile.maxTime > maxTime) {
                    maxTime = profile.maxTime;
                }
                if (profile.maxTemp != null && profile.maxTemp > maxTemp) {
                    maxTemp = profile.maxTemp;
                }
                if (profile.maxDepth != null && profile.maxDepth > maxDepth) {
                    maxDepth = profile.maxDepth;
                }
            }
            double maxTimeNorm = Math.ceil(maxTime.doubleValue() / (GRID_X * TIME_PRECISION)) * (GRID_X * TIME_PRECISION);
            timeScale = GRID_X / maxTimeNorm;
            if (ppo2s != null) {
                double maxPPO2Norm = Math.ceil(maxPPO2 / (GRID_Y * PPO2_PRECISION)) * (GRID_Y * PPO2_PRECISION);
                ppo2Scale = GRID_Y / maxPPO2Norm;
            }
            if (maxDepth != null) {
                double maxDepthNorm = Math.ceil(maxDepth.doubleValue() / (GRID_Y * ALT_PRECISION)) * (GRID_Y * ALT_PRECISION);
                altScale = GRID_Y / maxDepthNorm;
            }
            calcTemperatureSegments();
        }
    }

    private void parseSamplesDeltaMode() {
        double time = 0;
        double delta = 0;
        String mixname = Messages.getString("default_mixname"); //$NON-NLS-1$
        int subprofileId = 0;
        addSubprofile(subprofileId, mixname);
        UnitConverter c = new UnitConverter(UnitConverter.getSystem(this.dive.getUnits()), UnitConverter.getDisplaySystem());
        Iterator<Sample> it = this.dive.getDive().getSamples().iterator();
        double depth = 0.0;
        while (it.hasNext()) {
            Sample s = it.next();
            if (s instanceof Depth) {
                time += delta;
                depth = c.convertAltitude((Double) s.getValue()).doubleValue();
                if (this.maxDepth == null || this.maxDepth.doubleValue() < depth) {
                    this.maxDepth = new Double(depth);
                }
                addDepth(c.convertTime(new Double(time)).doubleValue(), depth, subprofileId);
            } else if (s instanceof Delta) {
                delta = ((Double) s.getValue()).doubleValue();
            } else if (s instanceof Switch) {
                if (!comparisonProfile && profileSettings.isFillDepth()) {
                    if (depth != 0.0) {
                        addDepth(c.convertTime(new Double(time)).doubleValue() + 0.01, 0.0, subprofileId);
                    }
                }
                addGasSwitch(c.convertTime(new Double(time)).doubleValue() + 0.02, subprofileId);
                mixname = (String) s.getValue();
                addSubprofile(++subprofileId, mixname);
                if (!comparisonProfile && profileSettings.isFillDepth()) {
                    if (depth != 0.0) {
                        addDepth(c.convertTime(new Double(time)).doubleValue() - 0.01, 0.0, subprofileId);
                    }
                }
                addDepth(c.convertTime(new Double(time)).doubleValue(), depth, subprofileId);
            } else if (s instanceof Temperature) {
                double temp = (c.convertTemperature((Double) s.getValue())).doubleValue();
                if (this.maxTemp == null || this.maxTemp.doubleValue() < temp) {
                    this.maxTemp = new Double(temp);
                }
                addTemperature(c.convertTime(new Double(time)).doubleValue(), temp);
            } else if (s instanceof DecoInfo) {
                double ceilingdepth = c.convertAltitude((Double) s.getValue());
                addDecoInfo(c.convertTime(time), ceilingdepth);
            } else if (s instanceof Alarm) {
                addAlarm(c.convertTime(new Double(time)).doubleValue(), (String) s.getValue());
            } else if (s instanceof PPO2) {
                PPO2 p = (PPO2) s;
                String sensor = p.getSensor();
                addPPO2(c.convertTime(new Double(time)).doubleValue(), p.getValue(), sensor);
            }
        }
        maxTime = c.convertTime(new Double(time));
    }

    private void parseSamplesTimeMode() {
        double time = 0;
        String mixname = Messages.getString("default_mixname"); //$NON-NLS-1$
        UnitConverter c = new UnitConverter(UnitConverter.getSystem(this.dive.getUnits()), UnitConverter.getDisplaySystem());
        int subprofileId = 0;
        addSubprofile(subprofileId, mixname);
        double depth = 0.0;
        Iterator<Sample> it = this.dive.getDive().getSamples().iterator();
        while (it.hasNext()) {
            Sample s = it.next();
            if (s instanceof Depth) {
                depth = c.convertAltitude((Double) s.getValue()).doubleValue();
                if (this.maxDepth == null || this.maxDepth.doubleValue() < depth) {
                    this.maxDepth = new Double(depth);
                }
                addDepth(c.convertTime(new Double(time)).doubleValue(), depth, subprofileId);
            } else if (s instanceof Time) {
                time = ((Double) s.getValue()).doubleValue();
            } else if (s instanceof Switch) {
                if (profileSettings.isFillDepth() && depth != 0.0) {
                    addDepth(c.convertTime(new Double(time)).doubleValue() + 0.001, depth, subprofileId);
                    addDepth(c.convertTime(new Double(time)).doubleValue() + 0.002, 0.0, subprofileId);
                }
                addGasSwitch(c.convertTime(new Double(time)).doubleValue() + 0.002, subprofileId);
                mixname = (String) s.getValue();
                addSubprofile(++subprofileId, mixname);
                if (profileSettings.isFillDepth() && depth != 0.0) {
                    addDepth(c.convertTime(new Double(time)).doubleValue(), 0.0, subprofileId);
                    addDepth(c.convertTime(new Double(time)).doubleValue() + 0.001, depth, subprofileId);
                }

            } else if (s instanceof Temperature) {
                double temp = (c.convertTemperature((Double) s.getValue())).doubleValue();
                if (this.maxTemp == null || this.maxTemp.doubleValue() < temp) {
                    this.maxTemp = new Double(temp);
                }
                addTemperature(c.convertTime(new Double(time)).doubleValue(), temp);
            } else if (s instanceof DecoInfo) {
                double ceilingdepth = c.convertAltitude((Double) s.getValue());
                addDecoInfo(c.convertTime(time), ceilingdepth);
            } else if (s instanceof Alarm) {
                addAlarm(c.convertTime(new Double(time)).doubleValue(), (String) s.getValue());
            } else if (s instanceof PPO2) {
                PPO2 p = (PPO2) s;
                String sensor = p.getSensor();
                addPPO2(c.convertTime(new Double(time)).doubleValue(), p.getValue(), sensor);
            }
        }
        maxTime = c.convertTime(new Double(time));
    }

    private void addMix(Gas gas) {
        if (gasList == null) {
            gasList = new ArrayList<Gas>();
        }
        gasList.add(gas);
    }

    private void addSubprofile(int subprofileId, String gas) {
        if (gases == null) {
            gases = new HashMap<Integer, String>();
        }
        if (depths == null) {
            depths = new HashMap<Integer, TreeSet<TimeDepth>>();
        }
        gases.put(subprofileId, gas);
        depths.put(subprofileId, new TreeSet<TimeDepth>());
    }

    private void addDepth(double time, double depth, int subprofileId) {
        TreeSet<TimeDepth> set = depths.get(subprofileId);
        if (set != null) {
            set.add(new TimeDepth(time, depth));
        }
    }

    private void addPPO2(double time, double ppo2, String sensor) {
        if (ppo2s == null) {
            ppo2s = new HashMap<String, TreeSet<TimeDepth>>();
        }
        TreeSet<TimeDepth> set = ppo2s.get(sensor);
        if (set == null) {
            set = new TreeSet<TimeDepth>();
            ppo2s.put(sensor, set);
        }
        set.add(new TimeDepth(time, ppo2));
        if (ppo2 > maxPPO2) {
            maxPPO2 = ppo2;
        }
    }

    private void addGasSwitch(double time, int subprofileId) {
        TreeSet<TimeDepth> set = depths.get(subprofileId);
        if (set != null) {
            set.add(new TimeDepth(time));
        }
    }

    private void addTemperature(double time, double temp) {
        if (profileSettings.isShowTemperature()) {
            if (temperatures == null) {
                drawTemp = true;
                temperatures = new TreeSet<TimeTemperature>();
            }
            temperatures.add(new TimeTemperature(time, temp));
        }
    }

    private void addDecoInfo(double time, double depth) {
        if (profileSettings.isShowDeco()) {
            if (decoinfos == null) {
                drawDeco = true;
                decoinfos = new TreeSet<TimeDepth>();
            }
            decoinfos.add(new TimeDepth(time, depth));
        }
    }

    private void addAlarm(double time, String alarm) {
        if (alarms == null) {
            alarms = new TreeSet<TimeAlarm>();
        }
        alarms.add(new TimeAlarm(time, alarm));
    }

    private Rectangle getGraphCoords() {
        return new Rectangle(LEFTLABEL_SIZE, TOPLABEL_SIZE, new Double(getPreferredSize().getWidth() - LEFTLABEL_SIZE - RIGHTLABEL_SIZE).intValue(),
                new Double(getPreferredSize().getHeight() - BOTTOMLABEL_SIZE - TOPLABEL_SIZE).intValue());
    }

    private void drawNoProfileData(Graphics2D g2d) {
        Font oldFont = g2d.getFont();
        Color oldColor = g2d.getColor();
        g2d.setFont(new Font(oldFont.getFamily(), Font.BOLD, 20));
        g2d.setColor(Color.RED);
        g2d.rotate(100, getGraphCoords().getCenterX(), getGraphCoords().getCenterY());
        FontMetrics fm = g2d.getFontMetrics();
        String message = Messages.getString("no_profile_data_available"); //$NON-NLS-1$
        Rectangle2D r2d = fm.getStringBounds(message, g2d);
        g2d.drawString(message, (float) (getGraphCoords().getCenterX() - r2d.getCenterX()), (float) (getGraphCoords().getCenterY() - r2d.getCenterY()));
        g2d.rotate(-100, getGraphCoords().getCenterX(), getGraphCoords().getCenterY());
        g2d.setColor(oldColor);
        g2d.setFont(oldFont);
    }

    private void drawGrid(Graphics2D g) {
        DecimalFormat f = new DecimalFormat(Messages.getString("profile_grid_numberformat")); //$NON-NLS-1$
        for (int i = 0; i <= GRID_X; i++) {
            int x = new Double(getGraphCoords().getX() + i * getGraphCoords().getWidth() / GRID_X).intValue();
            int y_from = new Double(getGraphCoords().getY()).intValue();
            int y_to = new Double(getGraphCoords().getY() + getGraphCoords().getHeight()).intValue();
            g.setColor(profileSettings.getGridColor());
            g.drawLine(x, y_from, x, y_to);
            if (i % 2 == 0) {
                g.setColor(profileSettings.getGridLabelColor());
                g.drawString(f.format(i / timeScale), x - 5, y_to + 16);
            }
        }
        for (int i = 0; i <= GRID_Y; i++) {
            int y = new Double(getGraphCoords().getY() + i * getGraphCoords().getHeight() / GRID_Y).intValue();
            int x_from = new Double(getGraphCoords().getX()).intValue();
            int x_to = new Double(getGraphCoords().getX() + getGraphCoords().getWidth()).intValue();
            g.setColor(profileSettings.getGridColor());
            g.drawLine(x_from, y, x_to, y);
            if (i % 2 == 0) {
                g.setColor(profileSettings.getGridLabelColor());
                g.drawString(f.format(i / altScale), x_from - 40, y + 5);
                if (ppo2s != null) {
                    g.setColor(profileSettings.getGridLabelColor());
                    g.drawString(f.format((8 - i) / ppo2Scale), x_to + 10, y + 5);
                }
            }
        }
        g.setColor(profileSettings.getGridLabelColor());
        g.drawString("[" + UnitConverter.getDisplayAltitudeUnit() + "]", (int) (getGraphCoords().getX() - 70),
                (int) (getGraphCoords().getY() + (getGraphCoords().getHeight() / 2)));
        if (ppo2s != null) {
            g.setColor(profileSettings.getGridLabelColor());
            g.drawString("[" + UnitConverter.getDisplayPressureUnit() + "]", (int) (getGraphCoords().getX() + getGraphCoords().getWidth() + 50),
                    (int) (getGraphCoords().getY() + (getGraphCoords().getHeight() / 2)));
        }
        g.setColor(profileSettings.getGridLabelColor());
        g.drawString("[" + UnitConverter.getDisplayTimeUnit() + "]", (int) (getGraphCoords().getX() + (getGraphCoords().getWidth() / 2)),
                (int) (getGraphCoords().getY() + getGraphCoords().getHeight() + 35));
        drawTempColorLabel(g, (int) (getGraphCoords().getX()), (int) (getGraphCoords().getY() + getGraphCoords().getHeight() + 45));
    }

    private void drawTempColorLabel(Graphics2D g, int offsetx, int offsety) {
        if (profileSettings.isShowTemperature() && drawTemp) {
            g.setColor(profileSettings.getTemperatureLabelColor());

            String labelMin = (new Double(getMinTempRounded())).intValue() + " " + UnitConverter.getDisplayTemperatureUnit();
            String labelMax = (new Double(getMaxTempRounded())).intValue() + " " + UnitConverter.getDisplayTemperatureUnit();
            g.drawString(labelMin, offsetx, offsety);
            g.drawString(labelMax, offsetx + 80, offsety);
            g.drawRect(offsetx, offsety + 5, 100, 10);
            double to = getMinTempRounded();
            for (int i = 0; i < 10; i++) {
                g.setColor(calcTempColor(to + (getMaxTempRounded() - to) * new Double(i).doubleValue() / 10));
                g.fillRect(offsetx + i * 10, offsety + 5, 10, 10);
            }
        }
    }

    private void drawAlarms(Graphics2D g) {
        if (alarms != null) {
            alarmPositions.clear();
            Iterator<TimeAlarm> it = alarms.iterator();
            while (it.hasNext()) {
                TimeAlarm alarm = it.next();
                if (alarm.getAlarm().equals(Alarm.ALARM_DECO)) {
                    int width = decoImage.getWidth(null);
                    int height = decoImage.getHeight(null);
                    int x = new Double((getGraphCoords().getX() + (alarm.getTime() * timeScale) * getGraphCoords().getWidth() / GRID_X) - width / 2).intValue();
                    int y = (int) (getGraphCoords().getHeight() + 4);
                    Rectangle r = new Rectangle(x, y, width, height);
                    alarmPositions.put(r, alarm.getAlarm());
                    g.drawImage(decoImage, x, y, this);
                } else if (alarm.getAlarm().equals(Alarm.ALARM_SURFACE)) {
                    int width = surfaceImage.getWidth(null);
                    int height = surfaceImage.getHeight(null);
                    int x = new Double((getGraphCoords().getX() + (alarm.getTime() * timeScale) * getGraphCoords().getWidth() / GRID_X) - width / 2).intValue();
                    int y = (int) (getGraphCoords().getHeight() + 4);
                    Rectangle r = new Rectangle(x, y, width, height);
                    alarmPositions.put(r, alarm.getAlarm());
                    g.drawImage(surfaceImage, x, y, this);
                } else if (alarm.getAlarm().equals(Alarm.ALARM_DECO_CEILING_PASSED)) {
                    int width = decoceilingpassedImage.getWidth(null);
                    int height = decoceilingpassedImage.getHeight(null);
                    int x = new Double((getGraphCoords().getX() + (alarm.getTime() * timeScale) * getGraphCoords().getWidth() / GRID_X) - width / 2).intValue();
                    int y = (int) (getGraphCoords().getHeight() + 4);
                    Rectangle r = new Rectangle(x, y, width, height);
                    alarmPositions.put(r, alarm.getAlarm());
                    g.drawImage(decoceilingpassedImage, x, y, this);
                } else if (alarm.getAlarm().equals(Alarm.ALARM_BOOKMARK)) {
                    int width = bookmarkImage.getWidth(null);
                    int height = bookmarkImage.getHeight(null);
                    int x = new Double((getGraphCoords().getX() + (alarm.getTime() * timeScale) * getGraphCoords().getWidth() / GRID_X) - width / 2).intValue();
                    int y = (int) (getGraphCoords().getHeight() + 4);
                    Rectangle r = new Rectangle(x, y, width, height);
                    alarmPositions.put(r, alarm.getAlarm());
                    g.drawImage(bookmarkImage, x, y, this);
                } else if (alarm.getAlarm().equals(Alarm.ALARM_SLOW)) {
                    int width = slowImage.getWidth(null);
                    int height = slowImage.getHeight(null);
                    int x = new Double((getGraphCoords().getX() + (alarm.getTime() * timeScale) * getGraphCoords().getWidth() / GRID_X) - width / 2).intValue();
                    int y = (int) (getGraphCoords().getHeight() + 4);
                    Rectangle r = new Rectangle(x, y, width, height);
                    alarmPositions.put(r, alarm.getAlarm());
                    g.drawImage(slowImage, x, y, this);
                } else if (alarm.getAlarm().equals(Alarm.ALARM_ATTENTION)) {
                    int width = attentionImage.getWidth(null);
                    int height = attentionImage.getHeight(null);
                    int x = new Double((getGraphCoords().getX() + (alarm.getTime() * timeScale) * getGraphCoords().getWidth() / GRID_X) - width / 2).intValue();
                    int y = (int) (getGraphCoords().getHeight() + 4);
                    Rectangle r = new Rectangle(x, y, width, height);
                    alarmPositions.put(r, alarm.getAlarm());
                    g.drawImage(attentionImage, x, y, this);
                } else if (alarm.getAlarm().equals(Alarm.ALARM_END_OF_DIVE)) {
                    int width = endofImage.getWidth(null);
                    int height = endofImage.getHeight(null);
                    int x = new Double((getGraphCoords().getX() + (alarm.getTime() * timeScale) * getGraphCoords().getWidth() / GRID_X) - width / 2).intValue();
                    int y = (int) (getGraphCoords().getHeight() + 4);
                    Rectangle r = new Rectangle(x, y, width, height);
                    alarmPositions.put(r, alarm.getAlarm());
                    g.drawImage(endofImage, x, y, this);
                } else if (alarm.getAlarm().equals(Alarm.ALARM_SAFETY_STOP_CEILING_PASSED)) {
                    int width = safetystopceilingpassedImage.getWidth(null);
                    int height = safetystopceilingpassedImage.getHeight(null);
                    int x = new Double((getGraphCoords().getX() + (alarm.getTime() * timeScale) * getGraphCoords().getWidth() / GRID_X) - width / 2).intValue();
                    int y = (int) (getGraphCoords().getHeight() + 4);
                    Rectangle r = new Rectangle(x, y, width, height);
                    alarmPositions.put(r, alarm.getAlarm());
                    g.drawImage(safetystopceilingpassedImage, x, y, this);
                } else {
                    int width = alarmImage.getWidth(null);
                    int height = alarmImage.getHeight(null);
                    int x = new Double((getGraphCoords().getX() + (alarm.getTime() * timeScale) * getGraphCoords().getWidth() / GRID_X) - width / 2).intValue();
                    int y = (int) (getGraphCoords().getHeight() + 4);
                    Rectangle r = new Rectangle(x, y, width, height);
                    alarmPositions.put(r, alarm.getAlarm());
                    g.drawImage(alarmImage, x, y, this);
                }
            }
        }
    }

    private void drawLabel(Graphics2D g, int position, String label) {
        int x = 5;
        int y = 20 * (position + 1);
        g.setColor(profileSettings.getGridLabelColor());
        g.drawString(label, x, y + 8);
    }

    private void drawGasLegend(Graphics2D g, int position, String gasname, Color c) {
        int x = 10;
        int y = 20 * (position + 1);
        g.setColor(profileSettings.getGridLabelColor());
        g.drawRect(x, y, 5, 5);
        String gasLabel = gasname;
        g.drawString(gasLabel, x + 10, y + 8);
        g.setColor(c);
        g.fillRect(x, y, 5, 5);
    }

    private PositionColorIndex drawDepthGraphs(DiveProfile profile, Graphics2D g, PositionColorIndex offset, boolean nofill) {
        if (profile.gases != null) {
            drawLabel(g, offset.position++, profile.getLabel());
            HashSet<String> usedGases = new HashSet<String>();
            Iterator<Integer> it = profile.gases.keySet().iterator();
            while (it.hasNext()) {
                int subprofileId = it.next();
                if (profile.depths.get(subprofileId) != null && profile.depths.get(subprofileId).size() > 0) {
                    String gasname = profile.gases.get(subprofileId);
                    usedGases.add(gasname);
                }
            }
            HashMap<String, Color> gasColors = new HashMap<String, Color>();
            Iterator<Gas> gasIt = profile.gasList.iterator();
            while (gasIt.hasNext()) {
                Gas gas = gasIt.next();
                if (usedGases.contains(gas.getName())) {
                    String gasname = gas.getName();
                    Color color = MIX_COLORS[offset.color++ % MIX_COLORS.length];
                    gasColors.put(gasname, color);
                    drawGasLegend(g, offset.position++, gasname, color);
                }
            }
            it = profile.gases.keySet().iterator();
            while (it.hasNext()) {
                int subprofileId = it.next();
                if (profile.depths.get(subprofileId) != null && profile.depths.get(subprofileId).size() > 0) {
                    String gasname = profile.gases.get(subprofileId);
                    Color color = gasColors.get(gasname);
                    drawDepthGraph(profile, g, subprofileId, color, nofill);
                }
            }
            Iterator<DiveProfile> compIt = profile.comparisonProfiles.iterator();
            while (compIt.hasNext()) {
                DiveProfile p = compIt.next();
                offset = drawDepthGraphs(p, g, offset, true);
            }
        }
        return offset;
    }

    private void drawDepthGraph(DiveProfile profile, Graphics2D g, int subprofileId, Color c, boolean nofill) {
        TreeSet<TimeDepth> depths = profile.depths.get(subprofileId);
        if (depths != null) {
            PolyShape p = createNewPoly(nofill);
            Iterator<TimeDepth> it = depths.iterator();
            while (it.hasNext()) {
                TimeDepth td = it.next();
                if (td.isGasSwitch()) {
                    drawPoly(g, c, nofill, p);
                    p = createNewPoly(nofill);
                } else {
                    int x = new Double(getGraphCoords().getX() + (td.getTime() * timeScale) * getGraphCoords().getWidth() / GRID_X).intValue();
                    int y = new Double(getGraphCoords().getY() + (td.getDepth() * altScale) * getGraphCoords().getHeight() / GRID_Y).intValue();
                    p.addPoint(x, y);
                }
            }
            drawPoly(g, c, nofill, p);
        }
    }

    private void drawTemperatureGraph(Graphics2D g) {
        if (!comparisonProfile && profileSettings.isShowTemperature() && (temperatures == null || temperatures.size() > 0)) {
            if (dive.getTemperature() != null && dive.getSurfaceTemperature() != null) {
                drawTemp = true;
                UnitConverter uc = new UnitConverter(UnitConverter.getSystem(dive.getUnits()), UnitConverter.getDisplaySystem());
                double dt = uc.convertTemperature(dive.getTemperature());
                double st = uc.convertTemperature(dive.getSurfaceTemperature());
                temperatureSegments = new Double[segments];
                for(int i=0; i<segments; i++) {
                    temperatureSegments[i] = st + ((dt-st)*i)/(segments-1);
                }
                calcTempSegmentMinAndMax();
            }
        }
        if (!comparisonProfile && profileSettings.isShowTemperature() && drawTemp && temperatureSegments != null) {

            int x0 = new Double(getGraphCoords().getX()).intValue();
            int w = new Double(getGraphCoords().getWidth()).intValue();
            int h = new Double(Math.ceil(getGraphCoords().getHeight() / segments)).intValue();
            for (int i = 0; i < temperatureSegments.length; i++) {
                int y = new Double(getGraphCoords().getY() + i * h).intValue();
                if (y > getGraphCoords().getHeight())
                    h = ((int) getGraphCoords().getHeight() - (i * h));
                g.setColor(calcTempColor(temperatureSegments[i]));
                g.fillRect(x0, y, w, h);
            }
        }
    }

    private void drawPPO2Graph(Graphics2D g) {
        if (!comparisonProfile && profileSettings.isShowPpo2() && ppo2s != null && ppo2s.size() > 0) {
        	time_ppo2s = new HashMap<Integer, String>();
            Color[] colors = new Color[] { profileSettings.getPpo2Color1(), profileSettings.getPpo2Color2(), profileSettings.getPpo2Color3(),
                    profileSettings.getPpo2Color4() };
            int count = 0;
            for (Entry<String, TreeSet<TimeDepth>> entry : ppo2s.entrySet()) {
                TreeSet<TimeDepth> tds = entry.getValue();
                PolyShape p = createNewPoly(true);
                for (TimeDepth td : tds) {
                    int x = new Double(getGraphCoords().getX() + (td.getTime() * timeScale) * getGraphCoords().getWidth() / GRID_X).intValue();
                    int y = new Double(getGraphCoords().getY() + getGraphCoords().getHeight() - (td.getDepth() * ppo2Scale) * getGraphCoords().getHeight()
                            / GRID_Y).intValue();
                    p.addPoint(x, y);
                    if (time_ppo2s.get(x) == null) {
                    	time_ppo2s.put(x, "#" + entry.getKey() + ": " + td.getDepth());
                    } else {
                		time_ppo2s.put(x, time_ppo2s.get(x) + " #" + entry.getKey() + ": " + td.getDepth());
                    }
                }
                Stroke stroke = PPO2_STROKE;
                Color color = colors[count++ % 4];
                drawPoly(g, color, stroke, p);
                drawPPO2Label(g, count, color, entry.getKey());
            }
        }
    }

    private void drawPPO2Label(Graphics2D g, int count, Color color, String key) {
        int x = (int) (getGraphCoords().getX() + getGraphCoords().getWidth()) - 100;
        int y = (int) (getGraphCoords().getY() + getGraphCoords().getHeight() + 45) + (count - 1) * 20;
        Stroke defaultStroke = g.getStroke();
        g.setColor(color);
        g.setStroke(PPO2_STROKE);
        g.drawLine(x - 50, y - 5, x - 10, y - 5);
        g.setStroke(defaultStroke);
        g.setColor(profileSettings.getTemperatureLabelColor());
        g.drawString(key, x, y);

    }

    private void calcTemperatureSegments() {
        if (profileSettings.isShowTemperature() && drawTemp) {
            temperatureSegments = new Double[segments];
            temperatureSegmentMin = null;
            temperatureSegmentMax = null;
            Collection<TreeSet<TimeDepth>> timedepthgraphs = depths.values();
            for (TreeSet<TimeDepth> tdg : timedepthgraphs) {
                for (TimeDepth td : tdg) {
                    int segment = getSegment(segments, td.depth);
                    double t = getTemperature(td.time);
                    if (temperatureSegments[segment] == null) {
                        temperatureSegments[segment] = t;
                    } else {
                        temperatureSegments[segment] = (temperatureSegments[segment] + t) / 2;
                    }
                }
            }
            calcTempSegmentMinAndMax();
        } else {
            temperatureSegments = null;
        }
    }

    private void calcTempSegmentMinAndMax() {
        for (int i = 0; i < temperatureSegments.length; i++) {
            if (i > 0 && temperatureSegments[i] == null) {
                temperatureSegments[i] = temperatureSegments[i - 1];
            }
            if (temperatureSegmentMin == null || temperatureSegments[i] < temperatureSegmentMin) {
                temperatureSegmentMin = temperatureSegments[i];
            }
            if (temperatureSegmentMax == null || temperatureSegments[i] > temperatureSegmentMax) {
                temperatureSegmentMax = temperatureSegments[i];
            }
        }
    }

    private int getSegment(int segments, double depth) {
        double md = GRID_Y / altScale;
        int segment = (int) (depth * segments / md);
        return segment >= segments ? segments - 1 : segment;
    }

    private double getTemperature(double time) {
        TimeTemperature reftt = new TimeTemperature(time, 0.0);
        TimeTemperature tt = temperatures.ceiling(reftt);
        if (tt == null) {
            tt = temperatures.floor(reftt);
        }
        return tt.temperature;
    }

    private double getMinTempRounded() {
        double result = 0.0;
        if (temperatureSegmentMin != null) {
            result = Math.floor(temperatureSegmentMin);
        }
        return result;
    }

    private double getMaxTempRounded() {
        double result = 0.0;
        if (temperatureSegmentMax != null) {
            result = Math.ceil(temperatureSegmentMax);
        }
        return result;
    }

    private Color calcTempColor(double temp) {
        return ColorShadingUtil.calculate(temp, getMinTempRounded(), getMaxTempRounded(), profileSettings.getTemperatureColorBegin(), profileSettings
                .getTemperatureColorEnd());
    }

    private void drawDecoGraph(Graphics2D g) {
        if (!comparisonProfile && drawDeco && decoinfos != null && decoinfos.size() > 0) {
            PolyShape p = createNewPoly(true);
            Iterator<TimeDepth> it = decoinfos.iterator();
            int lasty = new Double(getGraphCoords().getY()).intValue();
            while (it.hasNext()) {
                TimeDepth td = it.next();
                int x = new Double(getGraphCoords().getX() + (td.getTime() * timeScale) * getGraphCoords().getWidth() / GRID_X).intValue();
                p.addPoint(x, lasty);
                int y = new Double(getGraphCoords().getY() + (td.getDepth() * altScale) * getGraphCoords().getHeight() / GRID_Y).intValue();
                p.addPoint(x, y);
                lasty = y;
            }
            Stroke stroke = new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 1, new float[] { 1, 1 }, 0);

            drawPoly(g, profileSettings.getDecoColor(), stroke, p);
            g.fill(p);
        }
    }

    private PolyShape createNewPoly(boolean nofill) {
        PolyShape p;
        if (!nofill && profileSettings.isFillDepth()) {
            p = new PolyShape(new Polygon());
        } else {
            p = new PolyShape(new GeneralPath());
        }
        return p;
    }

    private void drawPoly(Graphics2D g, Color c, boolean nofill, PolyShape p) {
        g.setColor(c);
        if (!nofill && profileSettings.isFillDepth()) {
            g.fill(p);
        } else {
            Stroke defaultStroke = g.getStroke();
            g.setStroke(new BasicStroke(2));
            g.draw(p);
            g.setStroke(defaultStroke);
        }
    }

    private void drawPoly(Graphics2D g, Color c, Stroke stroke, PolyShape p) {
        g.setColor(c);
        Stroke defaultStroke = g.getStroke();
        g.setStroke(stroke);
        g.draw(p);
        g.setStroke(defaultStroke);
    }

    private static class PolyShape implements Shape {

        private final Shape delegate;

        private final boolean polygon;

        private boolean firstPoint;

        public PolyShape(Polygon poly) {
            delegate = poly;
            polygon = true;
        }

        public PolyShape(GeneralPath path) {
            delegate = path;
            firstPoint = true;
            polygon = false;
        }

        public void addPoint(int x, int y) {
            if (polygon) {
                ((Polygon) delegate).addPoint(x, y);
            } else {
                if (firstPoint) {
                    firstPoint = false;
                    ((GeneralPath) delegate).moveTo(x, y);
                } else {
                    ((GeneralPath) delegate).lineTo(x, y);
                }
            }
        }

        public Rectangle getBounds() {
            return delegate.getBounds();
        }

        public Rectangle2D getBounds2D() {
            return delegate.getBounds2D();
        }

        public boolean contains(double x, double y) {
            return delegate.contains(x, y);
        }

        public boolean contains(Point2D p) {
            return delegate.contains(p);
        }

        public boolean intersects(double x, double y, double w, double h) {
            return delegate.intersects(x, y, w, h);
        }

        public boolean intersects(Rectangle2D r) {
            return delegate.intersects(r);
        }

        public boolean contains(double x, double y, double w, double h) {
            return delegate.contains(x, y, w, h);
        }

        public boolean contains(Rectangle2D r) {
            return delegate.contains(r);
        }

        public PathIterator getPathIterator(AffineTransform at) {
            return delegate.getPathIterator(at);
        }

        public PathIterator getPathIterator(AffineTransform at, double flatness) {
            return delegate.getPathIterator(at, flatness);
        }

    }

    private String getLabel() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return format.format(dive.getDate());
    }

    private String getDepth(int y) {
        double depth = (y - getGraphCoords().getY()) * GRID_Y / (altScale * getGraphCoords().getHeight());

        return DEPTH_FORMAT.format(depth);
    }

    private String getTime(int x) {
        double time = (x - getGraphCoords().getX()) * GRID_X / (timeScale * getGraphCoords().getWidth());
        int minutes = (int) time;
        int seconds = (int) ((time - minutes) * 60);
        StringBuffer sb = new StringBuffer();
        sb.append(minutes);
        sb.append(":");
        sb.append(SECOND_FORMAT.format(seconds));
        return sb.toString();
    }

    private String getppO2(int x) {  	
    	if (time_ppo2s.get(x) == null) {
    		return "";
    	}
    	return time_ppo2s.get(x);
    }
    
    
    //
    // inner classes
    //

    private class ToolTip extends Canvas {

        private static final long serialVersionUID = 3257852099328161841L;

        protected String tip;

        public ToolTip(String tip, int x, int y) {
            this.tip = tip;
            setLocation(x, y);
            setBackground(new Color(255, 255, 220));
        }

        public void paint(Graphics g) {
            g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
            g.drawString(tip, 3, getSize().height - 3);
        }

        public String getTip() {
            return tip;
        }
    }

    private class TooltipListener extends MouseMotionAdapter {

        private final int HORIZONTAL_ENLARGE = 10;

        private Component component;

        private Container mainContainer;

        private ToolTip current;

        public TooltipListener(Component c) {
            component = c;
            findMainContainer();
        }

        private void findMainContainer() {
            Container parent = component.getParent();
            while (true) {
                if ((parent instanceof Applet) || (parent instanceof Frame)) {
                    mainContainer = parent;
                    break;
                } else if (parent == null) {
                    break;
                } else {
                    mainContainer = parent;
                    parent = parent.getParent();
                }
            }
        }

        public void mouseMoved(MouseEvent e) {
            Iterator<Rectangle> it = alarmPositions.keySet().iterator();
            boolean found = false;
            while (it.hasNext()) {
                Rectangle r = it.next();
                if (r.contains(e.getX(), e.getY())) {
                    found = true;
                    String text = alarmPositions.get(r);
                    if (current == null || !current.getTip().equals(text)) {
                        addToolTip(new ToolTip(text, e.getX(), e.getY()));
                    }
                }
            }
            if (!found && isCrossHairMode() && getGraphCoords().contains(e.getX(), e.getY())) {
                found = true;
                StringBuffer sb = new StringBuffer();
                sb.append(Messages.getString("depth"));
                sb.append(": ");
                sb.append(getDepth(e.getY()));
                sb.append(", ");
                sb.append(Messages.getString("time"));
                sb.append(": ");
                sb.append(getTime(e.getX()));
                if (profileSettings.isShowPpo2() && ppo2s != null && ppo2s.size() > 0) {
                    sb.append(", ");
                    sb.append(Messages.getString("ppo2"));                                     
                    sb.append(": ");
                    sb.append(getppO2(e.getX()));                	
                }
                addToolTip(new ToolTip(sb.toString(), e.getX(), e.getY()));
            }
            if (!found) {
                removeToolTip();
            }
        }

        private void addToolTip(ToolTip tt) {
            removeToolTip();
            current = tt;
            mainContainer.setLayout(null);

            FontMetrics fm = getFontMetrics(component.getFont());
            tt.setSize(fm.stringWidth(tt.getTip()) + HORIZONTAL_ENLARGE, fm.getHeight());

            int x = tt.getX();
            int y = tt.getY();
            if (tt.getSize().width + x > mainContainer.getSize().width) {
                x = mainContainer.getSize().width - tt.getSize().width;
            }
            if (tt.getSize().height + y > mainContainer.getSize().height) {
                y = mainContainer.getSize().height - tt.getSize().height;
            }
            tt.setLocation(x, y);

            mainContainer.add(current, 0);
            mainContainer.validate();
            tt.repaint();
        }

        private void removeToolTip() {
            if (current != null) {
                mainContainer.remove(current);
                current.repaint();
                current = null;
            }
        }
    }

    private class TimeDepth implements Comparable<TimeDepth> {
        private double time;

        private double depth;

        private boolean gasSwitch;

        public TimeDepth(double time, double depth) {
            this.time = time;
            this.depth = depth;
            this.gasSwitch = false;
        }

        /**
         * Constructor for Breaks (GasSwitches)
         * 
         * @param time
         */
        public TimeDepth(double time) {
            this.time = time;
            this.gasSwitch = true;
        }

        public int compareTo(TimeDepth td) {
            if (this == td)
                return 0;
            return Double.compare(time, td.time);
        }

        public double getTime() {
            return time;
        }

        public double getDepth() {
            return depth;
        }

        public boolean isGasSwitch() {
            return gasSwitch;
        }
    }

    private class TimeTemperature implements Comparable<TimeTemperature> {
        private double time;

        private double temperature;

        public TimeTemperature(double time, double temperature) {
            this.time = time;
            this.temperature = temperature;
        }

        public int compareTo(TimeTemperature tt) {
            if (this == tt)
                return 0;
            return Double.compare(time, tt.time);
        }

        public double getTime() {
            return time;
        }

        public double getTemperature() {
            return temperature;
        }
    }

    private class TimeAlarm implements Comparable<TimeAlarm> {
        private double time;

        private String alarm;

        public TimeAlarm(double time, String alarm) {
            this.time = time;
            this.alarm = alarm;
        }

        public int compareTo(TimeAlarm ta) {
            if (this == ta)
                return 0;
            return Double.compare(time, ta.time);
        }

        public double getTime() {
            return time;
        }

        public String getAlarm() {
            return alarm;
        }
    }

    private class PositionColorIndex {
        public int position = 0;

        public int color = 0;
    }

}