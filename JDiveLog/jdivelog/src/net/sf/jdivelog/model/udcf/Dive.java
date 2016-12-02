/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: Dive.java
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
package net.sf.jdivelog.model.udcf;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

/**
 * Description: Dive informations
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class Dive implements Comparable<Dive> {
    
    public static final int MODE_DELTA = 0;
    public static final int MODE_TIME = 1;
    
    private Date date = null;
    
    private String surfaceinterval;
    
    private Double temperature;

    private Double surfaceTemperature;
    
    private Double density;
    
    private Double altitude;
    
    private ArrayList<Gas> gases = new ArrayList<Gas>();
    
    private int mode;
    
    private ArrayList<Sample> samples;
        
    public void addGas(Gas gas) {
        if (gases == null) {
            gases = new ArrayList<Gas>();
        }
        gases.add(gas);
    }
    
    public void removeGas(Gas gas) {
        if (gases != null) {
            gases.remove(gas);
        }
    }
    
    public void addSample(Sample sample) {
        if (samples == null) {
            samples = new ArrayList<Sample>();
        }
        samples.add(sample);
    }
    
    public void removeSample(Sample sample) {
        if (samples != null) {
            samples.remove(sample);
        }
    }
    
    public void setTimeDepthMode() {
        mode = MODE_TIME;
    }
    
    public void setDeltaMode() {
        mode = MODE_DELTA;
    }
    
    public void addDelta(String value) {
        Delta delta = new Delta();
        delta.setValue(new Double(value));
        addSample(delta);
    }
    
    public void addSwitch(String value) {
        Switch s = new Switch();
        s.setValue(value);
        addSample(s);
    }
    
    public void addAlarm(String value) {
        Alarm a = new Alarm();
        a.setValue(value);
        addSample(a);
    }
    
    public void addTemperature(String value) {
        Temperature t = new Temperature();
        t.setValue(new Double(value));
        addSample(t);
    }
    
    public void addPPO2(String sensor, String value) {
        PPO2 s = new PPO2();
        s.setSensor(sensor);
        s.setValue(new Double(value));
        addSample(s);
    }

    public void addTime(String value) {
        Time t = new Time();
        t.setValue(new Double(value));
        addSample(t);
    }
    
    public void addDepth(String depth) {
        Depth d = new Depth();
        d.setValue(new Double(depth));
        addSample(d);
    }
    
    public void addDecoInfo(String depth, String tfs, String tts) {
        DecoInfo d = new DecoInfo();
        d.setValue(new Double(depth));
        if (tfs != null && !"".equals(tfs)) {
            d.setTfs(new Double(tfs));
        }
        if (tts != null && !"".equals(tts)) {
            d.setTts(new Double(tts));
        }
        addSample(d);
    }
    
    /**
     * get the xml representation of the element.
     * @return java.lang.String The XML representation of the element.
     */
    public String toString() {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(getDate());
        StringBuffer sb = new StringBuffer("<DIVE>");
        sb.append("<DATE><YEAR>");
        sb.append(gc.get(Calendar.YEAR));
        sb.append("</YEAR><MONTH>");
        sb.append(gc.get(Calendar.MONTH)+1);
        sb.append("</MONTH><DAY>");
        sb.append(gc.get(Calendar.DAY_OF_MONTH));
        sb.append("</DAY></DATE>");
        sb.append("<TIME><HOUR>");
        sb.append(gc.get(Calendar.HOUR_OF_DAY));
        sb.append("</HOUR><MINUTE>");
        sb.append(gc.get(Calendar.MINUTE));
        sb.append("</MINUTE></TIME>");
        sb.append("<SURFACEINTERVAL>");
        if (surfaceinterval != null) {
            sb.append(surfaceinterval);
        }
        sb.append("</SURFACEINTERVAL>");
        sb.append("<TEMPERATURE>");
        if (temperature != null) {
            sb.append(temperature);
        }
        sb.append("</TEMPERATURE>");
        sb.append("<surfaceTemperature>");
        if (surfaceTemperature != null) {
            sb.append(surfaceTemperature);
        }
        sb.append("</surfaceTemperature>");
        sb.append("<DENSITY>");
        if (density != null) {
            sb.append(density);
        }
        sb.append("</DENSITY>");
        sb.append("<ALTITUDE>");
        if (altitude != null) {
            sb.append(altitude);
        }
        sb.append("</ALTITUDE>");
        sb.append("<GASES>");
        Iterator<Gas> it = gases.iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
        }
        sb.append("</GASES>");
        if (MODE_TIME == mode) {
            sb.append("<TIMEDEPTHMODE/>");
        } else {
            sb.append("<DELTAMODE/>");
        }
        sb.append("<SAMPLES>");
        Iterator<Sample> sit = samples.iterator();
        while (sit.hasNext()) {
            sb.append(sit.next().toString());
        }
        sb.append("</SAMPLES>");
        sb.append("</DIVE>");
        return sb.toString();
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public void setDate(String y, String m, String d) {
        GregorianCalendar gc = new GregorianCalendar();
        if (getDate() == null) {
            setDate(new Date());
        }
        if (y != null && m != null && d != null && !"".equals(y) && !"".equals(m) && !"".equals(d)) {
	        int year = Integer.parseInt(y);
	        int month = Integer.parseInt(m);
	        int day = Integer.parseInt(d);
	        gc.setTime(getDate());
	        gc.set(Calendar.YEAR, year);
	        gc.set(Calendar.MONTH, month-1);
	        gc.set(Calendar.DAY_OF_MONTH, day);
	        setDate(gc.getTime());
        }
    }
    
    public void setTime(String hour, String minute) {
        GregorianCalendar gc = new GregorianCalendar();
        if (getDate() == null) {
            setDate(new Date());
        }
        if (hour != null && minute != null && !"".equals(hour) && !"".equals(minute)) {
            gc.setTime(getDate());
            gc.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
            gc.set(Calendar.MINUTE, Integer.parseInt(minute));
            setDate(gc.getTime());
        }
    }

    public Double getAltitude() {
        return altitude;
    }
    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }
    public void setAltitude(String altitude) {
        if (altitude == null || "".equals(altitude.trim())) {
            this.altitude = null;
        } else {
            this.altitude = new Double(altitude);
        }
    }
    public Double getDensity() {
        return density;
    }

    public Date getDate() {
        return this.date;
    }
    public void setDensity(Double density) {
        this.density = density;
    }
    public void setDensity(String density) {
        if (density == null || "".equals(density.trim())) {
            this.density = null;
        } else {
            this.density = new Double(density);
        }
    }
    public ArrayList<Gas> getGases() {
        return gases;
    }
    public void setGases(ArrayList<Gas> gases) {
        this.gases = gases;
    }
    public int getMode() {
        return mode;
    }
    public void setMode(int mode) {
        this.mode = mode;
    }
    public ArrayList<Sample> getSamples() {
        return samples;
    }
    public void setSamples(ArrayList<Sample> samples) {
        this.samples = samples;
    }
    public String getSurfaceinterval() {
        return surfaceinterval;
    }
    public void setSurfaceinterval(String surfaceinterval) {
        this.surfaceinterval = surfaceinterval;
    }
    public Double getTemperature() {
        return temperature;
    }
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
    public void setTemperature(String temperature) {
        if (temperature == null || "".equals(temperature.trim())) {
            this.temperature = null;
        } else {
            this.temperature = new Double(temperature);
        }
    }
    public Double getSurfaceTemperature() {
        return surfaceTemperature;
    }
    public void setSurfaceTemperature(Double surfaceTemperature) {
        this.surfaceTemperature = surfaceTemperature;
    }
    public void setSurfaceTemperature(String surfaceTemperature) {
        if (surfaceTemperature == null || "".equals(surfaceTemperature)) {
            this.surfaceTemperature = null;
        } else {
            this.surfaceTemperature = new Double(surfaceTemperature);
        }
    }

    public Double getMaxDepth() {
        Double max = null;
        if (getSamples() == null) {
            return null;
        }
        Iterator<Sample> it = getSamples().iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof Depth) {
                Depth d = (Depth)o;
                Double val = d.getValue();
                if (val != null) {
                    if (max == null || max.longValue() < val.longValue()) {
                        max = val;
                    }
                }
            }
        }
        return max;
    }
    
    public void addSampleTimeOffset(Double offset) {
        Iterator<Sample> it = getSamples().iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof Time) {
                Time d = (Time)o;
                d.setValue(d.getValue() + offset);
            }
        }
    }

    public Double getAverageDepth() {
        double average = 0;
        int i = 0;
        if (getSamples() == null) {
            return null;
        }
        Iterator<Sample> it = getSamples().iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof Depth) {
                Depth d = (Depth)o;
                Double val = d.getValue();
                if (val != null) {
                    average = average + val;
                    i++;
                }
            }
        }
        return (average / i);
    }

    public Double getDuration() {
        Double duration = null;
        if (getSamples() != null) {
            if (getMode() == MODE_DELTA) {
                long units = 0;
                double factor = 0;
                long counter = 0;
                Iterator<Sample> it = getSamples().iterator();
                while (it.hasNext()) {
                    Sample s = it.next();
                    if (s instanceof Delta) {
                        units += factor * counter;
                        counter = 0;
                        Delta d = (Delta)s;
                        factor = (d.getValue()).doubleValue();
                    } else if (s instanceof Depth) {
                        counter++;
                    }
                }
                units += factor*counter;
                duration = new Double(units);
            } else {
                Iterator<Sample> it = getSamples().iterator();
                Double time = null;
                while (it.hasNext()) {
                    Sample s = it.next();
                    if (s instanceof Time) {
                        Time t = (Time)s;
                        time = t.getValue();
                    }
                }
                duration = time;
            }
        }
        return duration;
    }

    public int compareTo(Dive o) {
        if (this == o) {
            return 0;
        }
        if (this.getDate() == null) {
            return -1;
        }
        if (o.getDate() == null) {
            return 1;
        }
        return this.getDate().compareTo(o.getDate());
    }
}
