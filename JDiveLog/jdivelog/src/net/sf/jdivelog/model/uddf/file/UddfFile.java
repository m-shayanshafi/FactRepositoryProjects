package net.sf.jdivelog.model.uddf.file;

import java.util.ArrayList;
import java.util.List;
/**
 * Represents an Uddf File
 * @author Levtraru
 *
 */
public class UddfFile{
	
	private UddfFileGasDefinitions gasDefinitions;
	private List<UddfFileDive> profileData;
	
	
	public UddfFileGasDefinitions getGasDefinitions() {
		return gasDefinitions;
	}
	public void setGasDefinitions(UddfFileGasDefinitions gasDefinitions) {
		this.gasDefinitions = gasDefinitions;
	}
	public List<UddfFileDive> getProfileData() {
		if (profileData==null){
			profileData= new ArrayList<UddfFileDive>();
		}
		return profileData;
	}
	protected void setProfileData(List<UddfFileDive> profileData) {
		this.profileData = profileData;
	}
	
	
	public void addUddfFileDive(UddfFileDive dive){
		getProfileData().add(dive);
	}
	

}