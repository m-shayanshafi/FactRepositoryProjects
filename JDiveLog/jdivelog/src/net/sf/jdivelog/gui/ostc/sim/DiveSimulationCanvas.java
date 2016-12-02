/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: DiveSimulationCanvas.java
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
package net.sf.jdivelog.gui.ostc.sim;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;

import net.sf.jdivelog.util.UnitConverter;

/**
 * Canvas to show a simulated Dive.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class DiveSimulationCanvas extends Canvas implements DiveProfileDisplay {
    
    private static final Image DIVERIMAGE = Toolkit.getDefaultToolkit().getImage(DiveSimulationCanvas.class.getResource("/net/sf/jdivelog/gui/resources/icons/diver.png")); //$NON-NLS-1$
    private static final Color GRID_COLOR = Color.GRAY;
    private static final Color GRID_LABEL_COLOR = Color.BLACK;
    private static final Color PROFILE_COLOR = Color.BLUE;
    private static final Color REAL_PROFILE_COLOR = Color.RED;
    private static final int LABEL_LEFT_WIDTH = 80;
    private static final int LABEL_RIGHT_WIDTH = 20;
    private static final int LABEL_TOP_HEIGHT = 20;
    private static final int LABEL_BOTTOM_HEIGHT = 50;

    private static final long serialVersionUID = -2672018693061457381L;
    
    private Coordinate diverLocation;
    private final ArrayList<Coordinate> profile;
    private final ArrayList<Coordinate> realProfile;
    private long timerange;
    private long depthrange;
    private boolean changed;
    
    public DiveSimulationCanvas() {
        profile = new ArrayList<Coordinate>();
        realProfile = new ArrayList<Coordinate>();
        reset();
        new Refresher().start();
    }

    public void reset() {
        diverLocation = null;
        profile.clear();
        profile.add(new Coordinate(0,0));
        realProfile.clear();
        realProfile.add(new Coordinate(0,0));
        timerange = 12*60*1000; // default 10 minutes
        depthrange = 1000; // default 10 meters
        changed = true;
    }

    public void addProfilePoint(long millis, long millibar) {
        checkTimeRange(millis);
        checkDepthRange(millibar);
        profile.add(new Coordinate(millis, millibar));
        changed = true;
    }

    public void setDiverLocation(long millis, long millibar) {
        checkTimeRange(millis);
        checkDepthRange(millibar);
        diverLocation = new Coordinate(millis, millibar);
        realProfile.add(diverLocation);
        changed = true;
    }
    
    @Override
    public void paint(Graphics g) {
        Dimension d = getPreferredSize();
        if (d.getWidth() > 0 && d.getHeight() > 0) {
            BufferedImage img = new BufferedImage(new Double(d.getWidth()).intValue(), new Double(d.getHeight()).intValue(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = img.createGraphics();
            g2d.setBackground(Color.WHITE);
            g2d.clearRect(0, 0, new Double(d.getWidth()).intValue(), new Double(d.getHeight()).intValue());
            drawProfile(g2d, d);
            drawGrid(g2d, d);
            drawDiver(g2d, d);
            g.drawImage(img, 0, 0, new Double(d.getWidth()).intValue(), new Double(d.getHeight()).intValue(), Color.WHITE, this);
        }
    }
    
    @Override
    public void update(Graphics g) { 
        paint(g); 
    } 
    
    private void drawProfile(Graphics2D g, Dimension d) {
        Rectangle rect = getProfileCoordinates(d);
        if (rect != null) {
            int num = profile.size();
            if (num > 1) {
                int[] x = new int[num];
                int[] y = new int[num];
                for (int i=0; i<num; i++) {
                    Coordinate c = profile.get(i);
                    Point p = calcPoint(rect, c.x, c.y);
                    x[i] = p.x;
                    y[i] = p.y;
                }
                g.setColor(PROFILE_COLOR);
                Stroke defaultStroke = g.getStroke();
                g.setStroke(new BasicStroke(1));
                g.drawPolyline(x, y, num);
                g.setStroke(defaultStroke);
            }
            num = realProfile.size();
            if (num > 1) {
                int[] x = new int[num];
                int[] y = new int[num];
                for (int i=0; i<num; i++) {
                    Coordinate c = realProfile.get(i);
                    Point p = calcPoint(rect, c.x, c.y);
                    x[i] = p.x;
                    y[i] = p.y;
                }
                g.setColor(REAL_PROFILE_COLOR);
                Stroke defaultStroke = g.getStroke();
                g.setStroke(new BasicStroke(2));
                g.drawPolyline(x, y, num);
                g.setStroke(defaultStroke);                
            }
        }
    }
    
    private void drawGrid(Graphics2D g, Dimension d) {
        Rectangle rect = getProfileCoordinates(d);
        if (rect != null) {
            g.setColor(GRID_COLOR);
            g.drawRect(rect.x, rect.y, rect.width, rect.height);
            long timesize = timerange / 6;
            for (int i=0; i<6; i++) {
                Point p1 = calcPoint(rect, i*timesize, 0);
                Point p2 = calcPoint(rect, i*timesize, depthrange);
                g.setColor(GRID_COLOR);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
                String str = millisToText(i*timesize);
                g.setColor(GRID_LABEL_COLOR);
                g.drawString(str, p2.x - 10, p2.y + 15);
            }
            Point p = calcPoint(rect, timerange/2, depthrange);
            g.setColor(GRID_LABEL_COLOR);
            g.drawString("[min:sec]", p.x -10 , p.y + 40);
            long depthsize = depthrange / 5;
            for (int i=0; i<5; i++) {
                Point p1 = calcPoint(rect, 0, i*depthsize);
                Point p2 = calcPoint(rect, timerange, i*depthsize);
                g.setColor(GRID_COLOR);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
                String str = millibarToText(i*depthsize);
                g.setColor(GRID_LABEL_COLOR);
                g.drawString(str, p1.x - 50, p1.y + 5);
            }
            p = calcPoint(rect, 0, depthrange/2);
            g.setColor(GRID_LABEL_COLOR);
            g.drawString("["+UnitConverter.getDisplayAltitudeUnit()+"]", p.x - 70, p.y - 10);
        }
    }
    
    private static String millisToText(long millis) {
        long seconds = millis/1000;
        long minutes = seconds/60;
        seconds = seconds%60;
        StringBuffer sb = new StringBuffer();
        sb.append(minutes);
        sb.append(":");
        if (seconds < 10) {
            sb.append("0");
        }
        sb.append(seconds);
        return sb.toString();
    }
    
    private static String millibarToText(long millibar) {
        double depth = millibar / 100.0;
        UnitConverter c = new UnitConverter(UnitConverter.SYSTEM_SI, UnitConverter.getDisplaySystem());
        depth = c.convertAltitude(depth);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(depth);
    }
    
    private void drawDiver(Graphics2D g, Dimension d) {
        Rectangle rect = getProfileCoordinates(d);
        if (diverLocation != null) {
            int width = DIVERIMAGE.getWidth(null);
            int height = DIVERIMAGE.getHeight(null);
            Point p = calcPoint(rect, diverLocation.x, diverLocation.y);
            int x = p.x - width/2;
            int y = p.y - height/2;
            g.drawImage(DIVERIMAGE, x, y, this);

        }
    }
    
    private Rectangle getProfileCoordinates(Dimension d) {
        int width = (int) (d.getWidth() - LABEL_LEFT_WIDTH - LABEL_RIGHT_WIDTH);
        int height = (int) (d.getHeight() - LABEL_TOP_HEIGHT - LABEL_BOTTOM_HEIGHT);
        if (width < 100 || height < 100) {
            return null;
        }
        Rectangle result = new Rectangle(new Point(LABEL_LEFT_WIDTH, LABEL_TOP_HEIGHT), new Dimension(width, height));
        return result;
    }
    
    private Point calcPoint(Rectangle rect, long millis, long millibar) {
        int x = rect.x + (int)(millis*rect.width/timerange);
        int y = rect.y + (int)(millibar*rect.height/depthrange);
        return new Point(x, y);
    }
    
    private void checkTimeRange(long millis) {
        if (millis > timerange) {
            timerange = (1+(millis/600000))*600000;
        }
    }
    
    private void checkDepthRange(long millibar) {
        if (millibar > depthrange) {
            depthrange = (1+millibar/1000)*1000;
        }
    }
    
    private static class Coordinate {
        private final long x;
        private final long y;
        public Coordinate(long x, long y) {
            this.x = x;
            this.y = y;
        }
        public long getX() {
            return x;
        }
        public long getY() {
            return y;
        }
    }
    
    private class Refresher extends Thread {
        @Override
        public void run() {
            while (true) {
                if (changed) {
                    invalidate();
                    repaint();
                    changed = false;
                }
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                }
            }
        }
    }

}
