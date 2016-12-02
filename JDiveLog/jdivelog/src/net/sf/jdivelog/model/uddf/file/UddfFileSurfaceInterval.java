package net.sf.jdivelog.model.uddf.file;

import java.util.ArrayList;
import java.util.List;

/**Represents the Surfaceinterval Uddf tag
 * 
 * @author Levtraru
 *
 */
public class UddfFileSurfaceInterval{

    public static final String INFINITY="infinity";
    
    private String passedtime;
    private List<UddfFileWayAltitude> wayaltitude;
    
	public String getPassedtime() {
		return passedtime;
	}
	public void setPassedtime(String passedtime) {
		this.passedtime = passedtime;
	}
	public void setPassedtimeInfinity() {
		this.passedtime = INFINITY;
	}
	public List<UddfFileWayAltitude> getWayaltitude() {
		if (wayaltitude==null){
			wayaltitude=new ArrayList<UddfFileWayAltitude>();
		}
		return wayaltitude;
	}
	public void setWayaltitude(List<UddfFileWayAltitude> wayaltitude) {
		this.wayaltitude = wayaltitude;
	}
	public void addWayaltitude(UddfFileWayAltitude wayAltitude){
		getWayaltitude().add(wayAltitude);
	}
}