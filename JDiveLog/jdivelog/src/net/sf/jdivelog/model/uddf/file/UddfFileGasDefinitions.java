package net.sf.jdivelog.model.uddf.file;

import java.util.ArrayList;
import java.util.List;

/**Represents the Gasdefinitions Uddf tag
 * 
 * @author Levtraru
 *
 */
public class UddfFileGasDefinitions {
	 private List<UddfFileMix> mixs;
	 private List<UddfFileTankdata> tanksdata;
	 
	public List<UddfFileMix> getMixs() {
		if (mixs==null){
			mixs= new ArrayList<UddfFileMix>();
		}
		return mixs;
	}
	public void setMixs(List<UddfFileMix> mixs) {
		this.mixs = mixs;
	}
	public void addMix(UddfFileMix mix){
		getMixs().add(mix);
	}
	public List<UddfFileTankdata> getTanksdata() {
		if (tanksdata==null){
			tanksdata= new ArrayList<UddfFileTankdata>();
		}
		return tanksdata;
	}
	public void setTanksdata(List<UddfFileTankdata> tanksdata) {
		this.tanksdata = tanksdata;
	}
	public void addTankdata(UddfFileTankdata tankdata){
		getTanksdata().add(tankdata);
	}	
}
