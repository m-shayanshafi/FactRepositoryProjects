
package net.sf.jdivelog.model.uddf.file;

import java.util.ArrayList;
import java.util.List;
/**Represents the Equipmentused Uddf tag
 * 
 * @author Levtraru
 *
 */
public class UddfFileEquipmentUsed{
 private String bootsref ;
 private String buoyancycontroldeviceref; 
 private String cameraref; 
 private String compassref; 
 private String divecomputerref; 
 private String finsref; 
 private String glovesref; 
 private String kniferef; 
 private String leadref; 
 private String lightref; 
 private String maskref; 
 private String rebreatherref; 
 private String regulatorref; 
 private String scooterref; 
 private String suitref; 
 private List<String> tankrefs; 
 private List<String> tankdatarefs; 
 private String variouspiecesref; 
 private String videocameraref; 
 private String watchref;
public String getBootsref() {
 return bootsref;
}
public void setBootsref(String bootsref) {
 this.bootsref = bootsref;
}
public String getBuoyancycontroldeviceref() {
 return buoyancycontroldeviceref;
}
public void setBuoyancycontroldeviceref(String buoyancycontroldeviceref) {
 this.buoyancycontroldeviceref = buoyancycontroldeviceref;
}
public String getCameraref() {
 return cameraref;
}
public void setCameraref(String cameraref) {
 this.cameraref = cameraref;
}
public String getCompassref() {
 return compassref;
}
public void setCompassref(String compassref) {
 this.compassref = compassref;
}
public String getDivecomputerref() {
 return divecomputerref;
}
public void setDivecomputerref(String divecomputerref) {
 this.divecomputerref = divecomputerref;
}
public String getFinsref() {
 return finsref;
}
public void setFinsref(String finsref) {
 this.finsref = finsref;
}
public String getGlovesref() {
 return glovesref;
}
public void setGlovesref(String glovesref) {
 this.glovesref = glovesref;
}
public String getKniferef() {
 return kniferef;
}
public void setKniferef(String kniferef) {
 this.kniferef = kniferef;
}
public String getLeadref() {
 return leadref;
}
public void setLeadref(String leadref) {
 this.leadref = leadref;
}
public String getLightref() {
 return lightref;
}
public void setLightref(String lightref) {
 this.lightref = lightref;
}
public String getMaskref() {
 return maskref;
}
public void setMaskref(String maskref) {
 this.maskref = maskref;
}
public String getRebreatherref() {
 return rebreatherref;
}
public void setRebreatherref(String rebreatherref) {
 this.rebreatherref = rebreatherref;
}
public String getRegulatorref() {
 return regulatorref;
}
public void setRegulatorref(String regulatorref) {
 this.regulatorref = regulatorref;
}
public String getScooterref() {
 return scooterref;
}
public void setScooterref(String scooterref) {
 this.scooterref = scooterref;
}
public String getSuitref() {
 return suitref;
}
public void setSuitref(String suitref) {
 this.suitref = suitref;
}
public List<String> getTankrefs() {
 if (tankrefs==null){
  tankrefs= new ArrayList<String>();
 }
 return tankrefs;
}
public void setTankrefs(List<String> tankrefs) {
 this.tankrefs = tankrefs;
}
public void addTankref(String tankref){
 getTankrefs().add(tankref);
}
public List<String> getTankdatarefs() {
 if(tankdatarefs==null){
  tankdatarefs= new ArrayList<String>();
 } 
 return tankdatarefs;
}
public void setTankdatarefs(List<String> tankdataref) {
 this.tankdatarefs = tankdataref;
}
public void addTankdataref(String tankref){
 getTankdatarefs().add(tankref);
}
public String getVariouspiecesref() {
 return variouspiecesref;
}
public void setVariouspiecesref(String variouspiecesref) {
 this.variouspiecesref = variouspiecesref;
}
public String getVideocameraref() {
 return videocameraref;
}
public void setVideocameraref(String videocameraref) {
 this.videocameraref = videocameraref;
}
public String getWatchref() {
 return watchref;
}
public void setWatchref(String watchref) {
 this.watchref = watchref;
} 
}
 