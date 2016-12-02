package net.sf.jdivelog.model.uddf.file;

/**Represents the Tankdata tag
 * 
 * @author Levtraru
 *
 */
public class UddfFileTankdata {

	private String id;
	private String breathingconsumptionvolume;
	private String mixref;
	private String tankref;
	private String tankpressurebegin;
	private String tankpressureend; 
	private String tankvolume;  
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}	
	public String getBreathingconsumptionvolume() {
		return breathingconsumptionvolume;
	}
	public void setBreathingconsumptionvolume(String breathingconsumptionvolume) {
		this.breathingconsumptionvolume = breathingconsumptionvolume;
	}
	public String getMixref() {
		return mixref;
	}
	public void setMixref(String mixref) {
		this.mixref = mixref;
	}
	public String getTankref() {
		return tankref;
	}
	public void setTankref(String tankref) {
		this.tankref = tankref;
	}
	public String getTankpressurebegin() {
		return tankpressurebegin;
	}
	public void setTankpressurebegin(String tankpressurebegin) {
		this.tankpressurebegin = tankpressurebegin;
	}
	public String getTankpressureend() {
		return tankpressureend;
	}
	public void setTankpressureend(String tankpressureend) {
		this.tankpressureend = tankpressureend;
	}
	public String getTankvolume() {
		return tankvolume;
	}
	public void setTankvolume(String tankvolume) {
		this.tankvolume = tankvolume;
	}
	



}