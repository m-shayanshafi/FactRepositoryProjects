/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: Masterdata.java
 * 
 * @author Volker Holthaus <v.holthaus@procar.de>
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
package net.sf.jdivelog.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Description: Lists of datas such as dive activities, buddies, equipment,... 
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class Masterdata {
    
    /** Buddys */
    private ArrayList<Buddy> buddys = new ArrayList<Buddy>();
    
    /** Divetypes */
    private ArrayList<DiveType> divetypes = new ArrayList<DiveType>();

    /** dive activities */
    private ArrayList<DiveActivity> diveactivities = new ArrayList<DiveActivity>();

    /** dive suits */
    private ArrayList<Suit> suits = new ArrayList<Suit>();
        
    /** gloves */
    private ArrayList<GloveType> gloveTypes = new ArrayList<GloveType>();
    
    /** equipment sets */
    private ArrayList<EquipmentSet> equipmentSets = new ArrayList<EquipmentSet>();
    
    /** dive sites */
    private TreeSet<DiveSite> diveSites = new TreeSet<DiveSite>();
    
    /** dive sites ordered by privateId */
    private HashMap<String, DiveSite> diveSitesByPrivateId = new HashMap<String, DiveSite>();
    
    /** dive sites ordered by spot and country */
    private HashMap<String, DiveSite> diveSitesBySpotAndCountry = new HashMap<String, DiveSite>();
    
    /** dive sites ordered by spot and city */
    private HashMap<String, DiveSite> diveSitesBySpotAndCity = new HashMap<String, DiveSite>();
    
    /** dive sites ordered by spot */
    private HashMap<String, DiveSite> diveSitesBySpot = new HashMap<String, DiveSite>();
    
    /** dive sites ordered by spot and country */
    private HashMap<String, Buddy> buddyBySurnameAndFirstname = new HashMap<String, Buddy>();

	/** last id of private dive sites */
    private long lastPrivateDiveSiteId = 0;
    
    private MixDatabase favoriteMixes = new FavoriteMixes();

    public ArrayList<Buddy> getBuddys() {
        return buddys;
    }
    public void setBuddys(ArrayList<Buddy> buddys) {
        this.buddys = buddys;
        buddyBySurnameAndFirstname.clear();
        Iterator<Buddy> it = buddys.iterator();
        while (it.hasNext()) {
        	Buddy buddy = it.next();
            buddyBySurnameAndFirstname.put(buddy.getFirstname() + " " + buddy.getLastname(), buddy);
        }                
    }
    public void addBuddy(Buddy buddy) {
        buddyBySurnameAndFirstname.put(buddy.getFirstname()+ " " + buddy.getLastname(), buddy);
        this.buddys.add(buddy);   
    }
    
    public ArrayList<DiveType> getDivetypes() {
        return divetypes;
    }
    public void setDivetypes(ArrayList<DiveType> divetypes) {
        this.divetypes = divetypes;
    }
    public void addDivetype(DiveType divetype) {
        this.divetypes.add(divetype);
    }
    
    public ArrayList<DiveActivity> getDiveactivities() {
        return diveactivities;
    }
    public void setDiveactivities(ArrayList<DiveActivity> diveactivities) {
        this.diveactivities = diveactivities;
    }
    public void addDiveactivity(DiveActivity diveactivity) {
        this.diveactivities.add(diveactivity);
    }
    
    public ArrayList<Suit> getSuits() {
        return suits;
    }
    public void setSuits(ArrayList<Suit> suits) {
        this.suits = suits;
    }
    public void addSuit(Suit suit) {
        suits.add(suit);
    }
    
    public ArrayList<GloveType> getGloveTypes() {
        return gloveTypes;
    }
    public void setGloveTypes(ArrayList<GloveType> gloveTypes) {
        this.gloveTypes = gloveTypes;
    }
    public void addGloveType(GloveType gloveType) {
        gloveTypes.add(gloveType);
    }
    
    public ArrayList<EquipmentSet> getEquipmentSets() {
        return equipmentSets;
    }
    public void setEquipmentSets(ArrayList<EquipmentSet> equipmentSets) {
        this.equipmentSets = equipmentSets;
    }
    public void addEquipmentSet(EquipmentSet equipmentSet) {
        equipmentSets.add(equipmentSet);
    }
    
    public TreeSet<DiveSite> getDiveSites() {
        return diveSites;
    }
    public void setDiveSites(TreeSet<DiveSite> newDiveSites) {
        diveSites = newDiveSites;
        diveSitesByPrivateId.clear();
        diveSitesBySpotAndCountry.clear();
        diveSitesBySpotAndCity.clear();
        lastPrivateDiveSiteId = 0;
        Iterator<DiveSite> it = diveSites.iterator();
        while (it.hasNext()) {
            DiveSite site = it.next();
            long id = Long.parseLong(site.getPrivateId());
            lastPrivateDiveSiteId = id > lastPrivateDiveSiteId ? id : lastPrivateDiveSiteId;
            diveSitesByPrivateId.put(site.getPrivateId(), site);
            diveSitesBySpotAndCountry.put(getSpotAndCountryKey(site.getSpot(), site.getCountry()), site);
            diveSitesBySpotAndCity.put(getSpotAndCountryKey(site.getSpot(), site.getCity()), site);
            diveSitesBySpot.put(site.getSpot(), site);
        }
    }
    public void addDiveSite(DiveSite site) {
        diveSites.add(site);
        String siteId = site.getPrivateId();
        if (siteId.length() > 0) {
            long id = Long.parseLong(site.getPrivateId());
            lastPrivateDiveSiteId = id > lastPrivateDiveSiteId ? id : lastPrivateDiveSiteId;
            diveSitesByPrivateId.put(site.getPrivateId(), site);
            diveSitesBySpotAndCountry.put(getSpotAndCountryKey(site.getSpot(), site.getCountry()), site);
            diveSitesBySpotAndCity.put(getSpotAndCountryKey(site.getSpot(), site.getCity()), site);
            diveSitesBySpot.put(site.getSpot(), site);
        }
    }
    public void deleteDiveSite(DiveSite site) {
        diveSites.remove(site);
        diveSitesByPrivateId.remove(site.getPrivateId());
        diveSitesBySpotAndCountry.remove(getSpotAndCountryKey(site.getSpot(), site.getCountry()));
        diveSitesBySpotAndCity.remove(getSpotAndCountryKey(site.getSpot(), site.getCity()));
        diveSitesBySpot.remove(site.getSpot());
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("<Masterdata>");
        sb.append("<Buddys>");
        Iterator<Buddy> it = getBuddys().iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
        }
        sb.append("</Buddys>");
        sb.append("<Divetypes>");
        Iterator<DiveType> it1 = getDivetypes().iterator();
        while (it1.hasNext()) {
            sb.append(it1.next().toString());
        }
        sb.append("</Divetypes>");
        sb.append("<Diveactivities>");
        Iterator<DiveActivity> it2 = getDiveactivities().iterator();
        while (it2.hasNext()) {
            sb.append(it2.next().toString());
        }
        sb.append("</Diveactivities>");
        sb.append("<Suits>");
        Iterator<Suit> sit = getSuits().iterator();
        while (sit.hasNext()) {
            sb.append(sit.next().toString());
        }
        sb.append("</Suits>");
        sb.append("<GloveTypes>");
        Iterator<GloveType> gtit = getGloveTypes().iterator();
        while (gtit.hasNext()) {
            sb.append(gtit.next().toString());
        }
        sb.append("</GloveTypes>");
        sb.append("<EquipmentSets>");
        Iterator<EquipmentSet> esit = getEquipmentSets().iterator();
        while (esit.hasNext()) {
            sb.append(esit.next().toString());
        }
        sb.append("</EquipmentSets>");
        sb.append("<DiveSites>");
        Iterator<DiveSite> dsit = getDiveSites().iterator();
        while(dsit.hasNext()) {
            sb.append(dsit.next().toString());
        }
        sb.append("</DiveSites>");
        sb.append(getFavoriteMixes());
        sb.append("</Masterdata>");
        return sb.toString();
    }
    
    public synchronized long getNextPrivateDiveSiteId() {
        return ++lastPrivateDiveSiteId;
    }
    
    public Buddy getBuddyByFirstnameAndSurname(String first_and_surname) {
    	return buddyBySurnameAndFirstname.get(first_and_surname);
	}
    
    public DiveSite getDiveSiteByPrivateId(String privateId) {
        return diveSitesByPrivateId.get(privateId);
    }
    
    public DiveSite getDiveSiteBySpotAndCountry(String spot, String country) {
        return diveSitesBySpotAndCountry.get(getSpotAndCountryKey(spot, country));
    }
    
    public DiveSite getDiveSiteBySpotAndCity(String spot, String city) {
        return diveSitesBySpotAndCity.get(getSpotAndCountryKey(spot, city));
    }
    
    public DiveSite getDiveSiteBySpot(String spot) {
        return diveSitesBySpot.get(spot);
    }
    
    public DiveSite getDiveSite(JDive dive) {
        return getDiveSiteByPrivateId(dive.getDiveSiteId());
    }

    public MixDatabase getFavoriteMixes() {
        return favoriteMixes;
    }
    
    public void addFavorite(String name, int oxygen, int helium, double ppO2, int mod, double change) {
        getFavoriteMixes().addFavorite(new Mix(name, oxygen, helium, ppO2, mod, change));
    }

    //
    // private methods
    //
    
    private String getSpotAndCountryKey(String spot, String country) {
        return country+"_"+spot;
    }
}
