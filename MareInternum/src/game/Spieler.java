/*	 
*    This file is part of Mare Internum.
*
*	 Copyright (C) 2008,2009  Johannes Hoechstaedter
*
*    Mare Internum is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    Mare Internum is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with Mare Internum.  If not, see <http://www.gnu.org/licenses/>.
*
*/

package game;

import entities.Konstanten;

public class Spieler {

	private int id;
    private int spielerNummer;
    private String name;
    private int geld;
    private int zenturios;
    private int[] a_karten;
    private int botschafter;
    private boolean bietet;
    private int gehalt;
    private boolean isStartspieler;
    private boolean isAktuellerSpieler;
    private String angriff;
    private int boni;
    private float trainingsFaktor;
    
    
    public void setAngriff(String ss_angriff){
    	angriff=ss_angriff;
    }

	public Spieler(int ss_id){
		trainingsFaktor=0;
		id = ss_id;
    	
    	//spielerNummer = Konstanten.NIXXOS;

    	geld = Konstanten.STARTGELD;
    	zenturios = Konstanten.STARTZENTURIOS;
    	botschafter = 0;
    	a_karten = new int[2];
    	for(int i=0;i<a_karten.length;i++){
    		a_karten[i] = Konstanten.NIXXOS;
    	}
    	bietet = true;
    	gehalt = 0;
	}
	
	public Spieler(int ss_id,String ss_name,int ss_geld,int ss_zenturios) {
		trainingsFaktor=0;
		id = ss_id;
    	name = ss_name;
    	//spielerNummer = Konstanten.NIXXOS;

    	geld = ss_geld;
    	zenturios = ss_zenturios;
    	botschafter = 0;
    	a_karten = new int[2];
    	for(int i=0;i<a_karten.length;i++){
    		a_karten[i] = Konstanten.NIXXOS;
    	}
    	bietet = true;
    	gehalt = 0;
    }
    
    public int get_id(){
    	return id;
    }
    public void set_id(int ss_id){
    	id = ss_id;
    }
    
    
    
    public void set_Spielernummer(int ss_nummer){
    	spielerNummer = ss_nummer;
    }
    
    public int get_Spielernummer(){
    	return spielerNummer;
    }
    
    public void set_gelddazu(int ss_geld){
    	gehalt = ss_geld;
    	geld = geld + ss_geld;
    	if(geld<0){
    		geld=0;
    	}
    }
    public void setBotschafter(int ss_wert){
    	botschafter=ss_wert;
    }
   
    public void set_zenturios(int ss_zenturio){
        zenturios =  ss_zenturio;
        }

	public int get_zenturios(){
    	return zenturios;
    }
    protected int get_gehalt(){
    	return gehalt;
    }

    public void set_zenturiodazu(int ss_zenturio){
    	zenturios = zenturios + ss_zenturio;
    	if(zenturios<0){zenturios=0;}
    }
    protected boolean get_is_kartevorhanden(int ss_id){
    boolean is_vorhanden = false;
    for(int i=0;i<a_karten.length;i++){
       if (a_karten[i]==ss_id)
          {is_vorhanden = true;}
    }
    return is_vorhanden;
    }
    public void set_karten(int ss_karte1,int ss_karte2){
    a_karten[0] = ss_karte1;
    a_karten[1] = ss_karte2;
    if(ss_karte1==5 || ss_karte2==5){
      bietet = true;
    }//end if
    else{
      bietet = false;
    }//end else
    }
    protected void set_bietet(boolean ss_bietet){
      bietet = ss_bietet;
    }

    protected boolean get_is_bietend(){
    return bietet;
    }

    protected void setBotschafterdazu(int ss_status){
    	botschafter = botschafter + ss_status;
      if(botschafter<0){
    	  botschafter = 0;
      }
   }
   public int getBotschafter(){
     return botschafter;
   }

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public boolean isAktuellerSpieler() {
	return isAktuellerSpieler;
}

public void setAktuellerSpieler(boolean isAktuellerSpieler) {
	this.isAktuellerSpieler = isAktuellerSpieler;
}

public boolean isStartspieler() {
	return isStartspieler;
}

public void setStartspieler(boolean isStartspieler) {
	this.isStartspieler = isStartspieler;
}
public int[] getA_Karten(){
	return a_karten;
}

public int getGeld() {
	return geld;
}

public void setGeld(int geld) {
	this.geld = geld;
}

public String getAngriff() {
	return angriff;
}
public void setBoniDazu(int ss_boni){
	boni+=ss_boni;
}

public int getBoni() {
	return boni;
}

public void setBoni(int boni) {
	this.boni = boni;
}

public float getTrainingsFaktor() {
	return trainingsFaktor;
}

public void setTrainingsFaktor(float trainingsFaktor) {
	this.trainingsFaktor = (float)(Math.floor(trainingsFaktor*10)/10.0);
}
public void setTrainingsFaktorDazu(float trainingsFaktor) {
	this.trainingsFaktor += trainingsFaktor;
	this.trainingsFaktor=(float)(Math.floor(this.trainingsFaktor*10)/10.0);
}

}
