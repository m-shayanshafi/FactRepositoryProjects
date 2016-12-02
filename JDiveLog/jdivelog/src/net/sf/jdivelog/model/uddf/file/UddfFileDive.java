package net.sf.jdivelog.model.uddf.file;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.GregorianCalendar;
import net.sf.jdivelog.model.uddf.UddfUtilsConverter;

/**
 * Represents the Dive Uddf tag
 * @author Levtraru
 *
 */
public class UddfFileDive {

    private String airtemperature;
    private String altitude;
    private String buddyref;
    private Date date;
    private String density;
    private String divenumber;
    private UddfFileEquipmentUsed equipmentused;
    private String greatestdepth;
    private String lowesttemperature;    
    /*
    private notes 
    private observations 
    private program 
    */
    private List<UddfFileWayPoint> samples;
    private UddfFileSurfaceInterval surfaceinterval;
    private Time time;
    
    
	public String getAirtemperature() {
		return airtemperature;
	}
	public void setAirtemperature(String airtemperature) {
		this.airtemperature = airtemperature;
	}
	public String getAltitude() {
		return altitude;
	}
	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}
	public String getBuddyref() {
		return buddyref;
	}
	public void setBuddyref(String buddyref) {
		this.buddyref = buddyref;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setDate(String day, String month, String year) {
		this.date = UddfUtilsConverter.getInstance().getDate(year, month, day);
	}	
	
	public String getDensity() {
		return density;
	}
	public void setDensity(String density) {
		this.density = density;
	}
	public String getDivenumber() {
		return divenumber;
	}
	public void setDivenumber(String divenumber) {
		this.divenumber = divenumber;
	}
	public UddfFileEquipmentUsed getEquipmentused() {
		return equipmentused;
	}
	public void setEquipmentused(UddfFileEquipmentUsed equipmentused) {
		this.equipmentused = equipmentused;
	}
	public String getGreatestdepth() {
		return greatestdepth;
	}
	public void setGreatestdepth(String greatestdepth) {
		this.greatestdepth = greatestdepth;
	}
	public String getLowesttemperature() {
		return lowesttemperature;
	}
	public void setLowesttemperature(String lowesttemperature) {
		this.lowesttemperature = lowesttemperature;
	}
	public List<UddfFileWayPoint> getSamples() {
		if (samples==null){
			samples=new ArrayList<UddfFileWayPoint>();
		}
		return samples;
	}
	protected void setSamples(List<UddfFileWayPoint> samples) {
		this.samples = samples;
	}
	public void addSample(UddfFileWayPoint wayPoint){
		getSamples().add(wayPoint);
	}
	public UddfFileSurfaceInterval getSurfaceinterval() {
		return surfaceinterval;
	}
	public void setSurfaceinterval(UddfFileSurfaceInterval surfaceinterval) {
		this.surfaceinterval = surfaceinterval;
	}
	public Time getTime() {
		return time;
	}
	public void setTime(Time time) {
		this.time = time;
	} 
    public void setTime(String hour, String minute) {
        GregorianCalendar gc = new GregorianCalendar();    	
    	this.time=new Time(UddfUtilsConverter.getInstance().getTime(hour, minute).getTime());
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
}
