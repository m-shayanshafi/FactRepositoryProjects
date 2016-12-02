package net.sf.jdivelog.model.uddf.file;

/**Represents the Waypoint Uddf tag
 * 
 * @author Levtraru
 *
 */
public class UddfFileWayPoint{

 private String alarm;
 private String cns; 
 private String depth; 
 private String divetime; 
 private String otu; 
 private String setpo2; 
 private String switchmix;
 private String temperature;
public String getAlarm() {
	return alarm;
}
public void setAlarm(String alarm) {
	this.alarm = alarm;
}
public String getCns() {
	return cns;
}
public void setCns(String cns) {
	this.cns = cns;
}
public String getDepth() {
	return depth;
}
public void setDepth(String depth) {
	this.depth = depth;
}
public String getDivetime() {
	return divetime;
}
public void setDivetime(String divetime) {
	this.divetime = divetime;
}
public String getOtu() {
	return otu;
}
public void setOtu(String otu) {
	this.otu = otu;
}
public String getSetpo2() {
	return setpo2;
}
public void setSetpo2(String setpo2) {
	this.setpo2 = setpo2;
}
public String getSwitchmix() {
	return switchmix;
}
public void setSwitchmix(String switchmix) {
	this.switchmix = switchmix;
}
public String getTemperature() {
	return temperature;
}
public void setTemperature(String temperature) {
	this.temperature = temperature;
}
public boolean hasTemperature() {
	return temperature!=null;
}
public boolean hasSwitchmix() {
	return switchmix!=null;
}
public boolean hasPo2() {
	return setpo2!=null;
}
public boolean hasAlarm() {
	return alarm!=null;
} 

}