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

import java.util.Arrays;
import java.util.Observable;
import java.util.Vector;

import entities.Konstanten;
import entities.KonstantenProtokoll;
import entities.PPGrenze;
import entities.Provinz;
import entities.RPGrenze;
import entities.RRGrenze;
import entities.Region;

import tools.ArrayUtils;
import tools.DB;


public class Spielbrett extends Observable{
    
    public int spieleranzahl;
    public Vector<Provinz> v_Provinzen;
    public Vector<Region> v_Regionen;
    public Vector<RRGrenze> v_RRGrenzen;
    public Vector<RPGrenze> v_RPGrenzen;
    public Vector<PPGrenze> v_PPGrenzen;
    
    
    public Spielbrett() {
     
    	spieleranzahl=0;
    	 DB dbBroker =new DB(); 
        v_Regionen=dbBroker.getRegionen();
       
        for(int i=0;i<v_Regionen.size();i++){
    		((Region)v_Regionen.elementAt(i)).init(dbBroker);
    	}
        v_Provinzen=dbBroker.getProvinzen();
        v_RRGrenzen=dbBroker.getRRGrenzen();
        v_RPGrenzen=dbBroker.getRPGrenzen();
        v_PPGrenzen=dbBroker.getPPGrenzen();
        
        
    	/*try {
			dbBroker.shutdown();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}*/
      //this.waren_setzen(false);// true = zufällig Waren setzen, false = nach dem Lager Waren setzen
     }
    
    public void waren_setzen(String[] ss_a_waren){
    	Provinz aktProvinz;
    	for(int i=0;i<ss_a_waren.length;i++){
    		aktProvinz =(Provinz)v_Provinzen.elementAt(i);
    		aktProvinz.setWare(Integer.parseInt(ss_a_waren[i]));
    	}
    	this.setChanged();
    	this.notifyObservers(Konstanten.NOMSG);
    }
    
    public void spieler_setzen(){
    	int provinz=0;
    	int z_spieler=spieleranzahl;
    	while(z_spieler>0){
    		provinz=((int)(Math.random()*100))%Konstanten.PROVINZENANZAHL;
    		if(this.getProvinzByIndex(provinz).isStadt){
    			provinz=(provinz+1)%Konstanten.PROVINZENANZAHL;
    		}
    		if(!this.getProvinzByIndex(provinz).isStadt&&this.getProvinzByOid(provinz).getSpielernummer()==Konstanten.NIXXOS){
    			z_spieler--;
    			this.getProvinzByOid(provinz).setSpielernummer(z_spieler);
    		}
    	}
    }
    
    public int[] getA_Provinzen(){
    	int[] provinzen=new int[Konstanten.PROVINZENANZAHL];
    	for(int i=0;i<Konstanten.PROVINZENANZAHL;i++){
    		provinzen[i]=this.getProvinzByOid(i).getSpielernummer();
    	}
    	return provinzen;
    }
    
    public void spieler_setzen(String[] provinzen){
    	
    	for(int i=0;i<Konstanten.PROVINZENANZAHL;i++){
    		this.getProvinzByOid(i).setSpielernummer(Integer.parseInt(provinzen[i]));
    	}
    	this.setChanged();
    	this.notifyObservers(Konstanten.NOMSG);
    }
    
    public void waren_setzen(boolean ss_zufall){
       int[] a_warenmenge;
       int verteiltewaren = 0;
       int warenzufallszahl,provinzenzufallszahl;
       boolean is_gesetzt=false;
       int rom = 1;//1 = Rom wird ausgelassen beim verteilen der Waren, 0 = Rom wird mitgezählt
       a_warenmenge = new int[Konstanten.MAX_WAREN];
       a_warenmenge[Konstanten.WEIZEN] = 7;//Getreide
       a_warenmenge[Konstanten.WEIN]= 7;//Wein
       a_warenmenge[Konstanten.STEIN] = 7;//Stein
       a_warenmenge[Konstanten.TOEPFERWARE] = 7;//Töpferware
       a_warenmenge[Konstanten.SCHAAF] = 7-rom;//Schaaf
       
       Provinz aktProvinz;
       
       for (int i=0;i<v_Provinzen.size();i++){
         aktProvinz=(Provinz)v_Provinzen.elementAt(i);
         aktProvinz.setWare(Konstanten.NIXXOS);
       }
       while(verteiltewaren < Konstanten.PROVINZENANZAHL-rom){
          is_gesetzt=false;
          warenzufallszahl =((int)(Math.random()*10)) % 5;
          provinzenzufallszahl = ((int)(Math.random()*100))%Konstanten.PROVINZENANZAHL;
          aktProvinz=this.getProvinzByIndex(provinzenzufallszahl);
          while(!is_gesetzt){
            //prüfen ob Provinz schon besetzt und es sich nicht um Rom Handelt bei rom = 1
            if(aktProvinz.getWare() == Konstanten.NIXXOS && ((provinzenzufallszahl != Konstanten.ROM) || (rom == 0))){
              //prüfen ob Ware noch im Lager oder ob per Zufall gesetzt wird
              if(a_warenmenge[warenzufallszahl] > 0 || ss_zufall){
                //setzen der Ware
                aktProvinz.setWare(warenzufallszahl);
                a_warenmenge[warenzufallszahl]--;
                verteiltewaren++;
                is_gesetzt = true;
              }//end if
              else{
                //Ware war nichtmehr im Lager, also nächste Ware auswählen
                warenzufallszahl = (warenzufallszahl+1)%5;
              }//end else
            }// end if
            else{
              //Provinz war besetzt, also nächste Provinz auswählen
              provinzenzufallszahl = (provinzenzufallszahl+1)%Konstanten.PROVINZENANZAHL;
              aktProvinz=this.getProvinzByIndex(provinzenzufallszahl);
            }//end else

           }// end while
        }//end while
       
    
   }
   
  public String get_allewaren(){
    String allewaren = "";
    Provinz aktProvinz;
    for(int i=0;i<v_Provinzen.size();i++){
        aktProvinz=(Provinz)v_Provinzen.elementAt(i);
    	allewaren = allewaren + aktProvinz.getWare() +KonstantenProtokoll.SEPARATORSUBJEKT;
    }
    return allewaren;
  }
  
  public String get_alleSpielernummern(){
	  String alleSpielernummern = "";
	    Provinz aktProvinz;
	    for(int i=0;i<v_Provinzen.size();i++){
	        aktProvinz=(Provinz)v_Provinzen.elementAt(i);
	        alleSpielernummern = alleSpielernummern + aktProvinz.getSpielernummer() +KonstantenProtokoll.SEPARATORSUBJEKT;
	    }
	    return alleSpielernummern;
  }

  public int get_provinzWare(int ss_Provinz){
    int ware = Konstanten.NIXXOS;
    Provinz aktProvinz=this.getProvinzByIndex(ss_Provinz);
    if(aktProvinz!=null){
    	ware =aktProvinz.getWare();
    }
    
    return ware;
    }

  public int getSpielernummerVonProvinz(int ss_Provinz){
  	int spieler = Konstanten.NIXXOS;
    if(ss_Provinz!=Konstanten.NIXXOS){
    	Provinz aktProvinz=this.getProvinzByIndex(ss_Provinz);
        spieler = aktProvinz.getSpielernummer();
    }
  	
    return spieler;
  }
  
  public Provinz getProvinzByIndex(int id){
	  Provinz aktProvinz=(Provinz)v_Provinzen.elementAt(id);
	  return aktProvinz;
  }
  
  public int get_legionslagerP(int ss_Provinz){
    int lager = Konstanten.NIXXOS;
    Provinz aktProvinz=this.getProvinzByOid(ss_Provinz);
    lager = aktProvinz.getZ_lager();
    return lager;
  }
  
  public int get_legionslagerS(int spielernummer){
	    int lager = 0;
	    Provinz aktProvinz;
	    for(int i=0;i<v_Provinzen.size();i++){
	    	aktProvinz=this.getProvinzByIndex(i);
	    	if(aktProvinz.getZ_lager()>0&&aktProvinz.getSpielernummer()==spielernummer){
	    		lager++;
	    	}
	    }
	    return lager;
	  }
  
  public int get_einheitenRS(int ss_Region,int spielernummer){
    int einheiten = Konstanten.NIXXOS;
    Region aktRegion=this.getRegionByIndex(ss_Region);
    einheiten = aktRegion.getEinheitenS(spielernummer);
    return einheiten;
  }
 
  public int get_einheitenKosten(int ss_Region){
    Region aktRegion=this.getRegionByIndex(ss_Region);
	int kosten = aktRegion.getKosten();
    return kosten;
  }
  
  public void set_einheitendazuRSE(int ss_region,int spielernummer,int ss_einheiten){
	Region aktRegion=this.getRegionByIndex(ss_region);
	
	aktRegion.setEinheitenSDazu(spielernummer,ss_einheiten);
    if(aktRegion.getEinheitenS(spielernummer)<0){
    	aktRegion.setEinheitenS(spielernummer,0);
    }
	this.setChanged();
    this.notifyObservers(Konstanten.NOMSG);
    
}
  
  public void set_einheitenArrayDazuES(int[] ss_einheiten,int ss_spieler,int faktor){
	  Region aktRegion;
	  for(int i=0;i<ss_einheiten.length;i++){
		  aktRegion=this.getRegionByIndex(i);
		  aktRegion.setEinheitenSDazu(ss_spieler,ss_einheiten[i]*faktor);
	  }  
	    this.setChanged();
	    this.notifyObservers(Konstanten.NOMSG);
	    
	}
  
  public void set_einheitenAbsolutRSE(int ss_region,int spielernummer,int ss_einheiten){
	  Region aktRegion=this.getRegionByOid(ss_region);
	  aktRegion.setEinheitenS(spielernummer,ss_einheiten);
     this.setChanged();
     this.notifyObservers(Konstanten.NOMSG);
  }
  
  public void set_einheitenAbsolutES(int[] ss_einheiten,int spielernummer){
	  Region aktRegion;
	  for(int i=0;i<ss_einheiten.length;i++){
		  aktRegion=this.getRegionByOid(i);
		  aktRegion.setEinheitenS(spielernummer,ss_einheiten[i]);
	  }  
	    
	    this.setChanged();
	    this.notifyObservers(Konstanten.NOMSG);
	    
	}
      
public void set_LagerAbsolutPSL(int ss_provinz,int spielernummer,int ss_lager){
    this.resetLegionsLager(); 
    Provinz aktProvinz=this.getProvinzByIndex(ss_provinz);
    if(aktProvinz.getSpielernummer()==spielernummer){
    	aktProvinz.setZ_lager(ss_lager);
    }
    this.setChanged();
    this.notifyObservers();
    
 }

public int getEinheitenRS(int ss_region,int spielernummer){
	Region aktRegion=null;
	int z_einheiten=0;;
	if(ss_region!=Konstanten.NIXXOS){
		aktRegion=this.getRegionByIndex(ss_region);
		z_einheiten=aktRegion.getEinheitenS(spielernummer);
	}
	return z_einheiten;
}

public void resetLegionsLager(){
   Provinz aktProvinz;
	for(int i=0;i<v_Provinzen.size();i++){
    	aktProvinz=this.getProvinzByIndex(i);
		if(aktProvinz.getZ_lager()<0){
			aktProvinz.setZ_lager(0);
    	}
    }
    this.setChanged();
    this.notifyObservers(Konstanten.NOMSG);
}

 public boolean is_regionenGrenze(int ss_region1, int ss_region2){
    //ist eine Grenze von Regionen eine Grenze
    boolean is_grenze = false;
    RRGrenze aktGrenze;
    for(int i=0;i<v_RRGrenzen.size();i++){
       aktGrenze=(RRGrenze)v_RRGrenzen.elementAt(i);
    	if ((aktGrenze.regionID1==ss_region1&&aktGrenze.regionID2==ss_region2)||
    			(aktGrenze.regionID1==ss_region2&&aktGrenze.regionID2==ss_region1)){
          is_grenze = true;
       }// end if
    }//end for
    
    return is_grenze;
  }
 
  public boolean is_provinzenGrenze(int ss_provinz1,int ss_provinz2){
  //ist Grenze von Provinzen eine Grenze
	boolean is_grenze = false;
    PPGrenze aktGrenze;
    for(int i=0;i<v_PPGrenzen.size();i++){
       aktGrenze=(PPGrenze)v_PPGrenzen.elementAt(i);
    	if ((aktGrenze.provinzID1==ss_provinz1&&aktGrenze.provinzID2==ss_provinz2)||
    			(aktGrenze.provinzID1==ss_provinz2&&aktGrenze.provinzID2==ss_provinz1)){
          is_grenze = true;
       }// end if
    }//end for
    
    return is_grenze;
  }
  
  /**
   * returns true if specific region is connected to a specific province
   * @param ss_region
   * @param ss_provinz
   * @return
   */
  public boolean is_regionProvinzGrenze(int ss_region,int ss_provinz){
    //ss_Grenze = "Region,Provinz"
    //ist Grenze von einer Region eine Grenze zu einer Provinz
      boolean is_grenze = false;
      RPGrenze aktGrenze;
      for(int i=0;i<v_RPGrenzen.size();i++){
         aktGrenze=(RPGrenze)v_RPGrenzen.elementAt(i);
      	if (aktGrenze.provinzID==ss_provinz&&aktGrenze.regionID==ss_region){
            is_grenze = true;
         }// end if
      }//end for
      return is_grenze;
  }
  
  /**
   * occupies a specific province by a specific player 
   * @param provinzid
   * @param spielernummer
   */
  public void besetze_ProvinzPS(int provinzid,int spielernummer){
    Provinz aktProvinz=this.getProvinzByIndex(provinzid);
     if(aktProvinz.getSpielernummer()!=spielernummer){
    	 aktProvinz.setZ_lager(0);
     }
     aktProvinz.setSpielernummer(spielernummer);
     this.setProvinzAusgewaehlt(provinzid,false);
     this.setChanged();
     this.notifyObservers(Konstanten.NOMSG);
     
  }
  
  /**
   * returns count o all provinces of a sepcific player in the neighborhood of a specific province
   * @param ss_provinz
   * @param ss_spieler
   * @return
   */
  public int get_angrenzendeProvinzenPS(int ss_provinz,int ss_spieler){
	  int anzahlProvinzen = 0;
	  boolean is_grenze=false;

     for(int j=0;j<Konstanten.PROVINZENANZAHL;j++){
        is_grenze=this.is_provinzenGrenze(j,ss_provinz);
        if(is_grenze){
           if(this.getSpielernummerVonProvinz(j)==ss_spieler){
              anzahlProvinzen++;
              is_grenze = false;
          }//end if
        }//end if
    }//end for
  return anzahlProvinzen;
  }
 
  /**
   * returns count of all fortresses belonging to a specific player in the neighborhood of a specific province 
   * @param ss_provinz
   * @param spielernummer
   * @return
   */
public int get_angrenzendeLagerPS(int provinzid,int spielernummer){
    int anzahlLager = 0;
     boolean is_grenze=false;

        for(int j=0;j<Konstanten.PROVINZENANZAHL;j++){
           is_grenze=this.is_provinzenGrenze(j,provinzid);
           if(is_grenze){
              if(this.get_legionslagerP(j)>0 && this.getSpielernummerVonProvinz(j)==spielernummer){
                 anzahlLager+=this.get_legionslagerP(j);
                 is_grenze = false;
             }//end if
           }//end if
       }//end for
  return anzahlLager;
}

public void setVesetzteAusgewaehlteProvinz(int ss_provinz){
	Provinz aktProvinz;
	for(int i=0;i<v_Provinzen.size();i++){
		aktProvinz=(Provinz)v_Provinzen.elementAt(i);
		if(aktProvinz.getSpielernummer()==Konstanten.NIXXOS&&i==ss_provinz){
			aktProvinz.setAusgewaehlt(true);
		}
		else if(aktProvinz.getSpielernummer()==Konstanten.NIXXOS){
			aktProvinz.setAusgewaehlt(false);
		}
	}
	this.setChanged();
	this.notifyObservers();
}

public int getAusgewaehlteProvinz(){
	int provinz=Konstanten.NIXXOS;
	Provinz aktProvinz;
	for(int i=0;i<v_Provinzen.size();i++){
		aktProvinz=(Provinz)v_Provinzen.elementAt(i);
		if(aktProvinz.isAusgewaehlt()){
			provinz=i;;
		}	
	}
	return provinz;
}

public int[] getA_Waren(int spieler){
	int[] a_waren =new int[Konstanten.MAX_WAREN];
	for(int i=0;i<a_waren.length;i++){
		a_waren[i]=0;
	}
	
	Provinz aktProvinz;
	for(int i=0;i<v_Provinzen.size();i++){
		aktProvinz=(Provinz)v_Provinzen.elementAt(i);
		if(aktProvinz.getSpielernummer()==spieler){
			if(!(aktProvinz.getWare()==Konstanten.NIXXOS)){
				a_waren[aktProvinz.getWare()]++;
			}
			
		}
	}
	return a_waren;
}

public int getProvinzWare(int provinz){
	return this.getProvinzByOid(provinz).getWare();
}

public boolean isProvinzBesetzt(int ss_provinz){
	Provinz aktProvinz=this.getProvinzByIndex(ss_provinz);
	if(aktProvinz.getSpielernummer()!=Konstanten.NIXXOS){
		return true;
	}
	else{
		return false;
	}
}

public int getSpieleranzahl() {
	return spieleranzahl;
}

public void setSpieleranzahl(int spieleranzahl) {
	this.spieleranzahl = spieleranzahl;
}

public void setProvinzAusgewaehlt(int ss_provinz,boolean isAusgewaehlt){
	Provinz aktProvinz=this.getProvinzByIndex(ss_provinz);
	aktProvinz.setAusgewaehlt(isAusgewaehlt);
}

/**
 * calculates absolute count of point for a single player,
 * identified by id, according to the gaming map (without special points)
 * @param spielernummer
 * @return
 */
public int getPunkteSpieler(int spielernummer){
	int punkte =0;
	Provinz aktProvinz;
	
	//points for provinces
	for(int i=0;i<v_Provinzen.size();i++){
		aktProvinz=this.getProvinzByIndex(i);
		if(aktProvinz.getSpielernummer() == spielernummer){
			punkte++;
			if(aktProvinz.getOid()==Konstanten.ROM){
				punkte += Konstanten.PUNKTE_ROM;
			}
			for(int j = i+ 1 ; j < Konstanten.PROVINZENANZAHL; j++){
				aktProvinz=this.getProvinzByIndex(j);
				if(aktProvinz.getSpielernummer() == spielernummer
						&& this.is_provinzenGrenze(i, j)){
					punkte++;
				}
			}
		}
	}

	// add points for trading goods
	int spieleranzahlPerWare = 0;
	int[] min_mid_max = new int[3];
	for(int j = 0 ; j < Konstanten.MAX_WAREN; j++){
		
		min_mid_max = getMaxAnzahlProWare(j);
		int warenanzahlSpieler = this.getA_Waren(spielernummer)[j];
		
		int platz = 0;
		for(int i = 0; i < min_mid_max.length; i++){
			spieleranzahlPerWare = getSpielerAnzahlProWareUndAnzahl(j, min_mid_max[i]);
			platz += (i + (spieleranzahlPerWare-1));
			
			if(warenanzahlSpieler > 0 && warenanzahlSpieler == min_mid_max[i]){
				if(platz < Konstanten.PUNKTE_WAREN.length){
					punkte += Konstanten.PUNKTE_WAREN[platz];
				}
			}
		}
		
		
	}
	
	return punkte;
}

/**
 * returns array with max, mid and min value for a trading good
 * @param ware
 * @return
 */
public int[] getMaxAnzahlProWare(int ware){
	
	int[] min_mid_max = new int[3];
	for(int i = 0 ;i < min_mid_max.length; i++){
		min_mid_max[i] = 0;
	}
	
	int[] a_anzahlWaren = new int[spieleranzahl];
	
	for(int i = 0 ;i < spieleranzahl; i++){
		a_anzahlWaren[i] = this.getA_Waren(i)[ware];
	}
	
	Arrays.sort(a_anzahlWaren);
	ArrayUtils.reverse(a_anzahlWaren);
	
	for(int i = 0 ;i < spieleranzahl; i++){
		if(a_anzahlWaren[i] > 0 && min_mid_max[0] == 0){
			min_mid_max[0] = a_anzahlWaren[i];
		}
		if(a_anzahlWaren[i] > 0 && a_anzahlWaren[i] < min_mid_max[0]){
			min_mid_max[1] = a_anzahlWaren[i];
		}
		if(a_anzahlWaren[i] > 0 && a_anzahlWaren[i] < min_mid_max[1]){
			min_mid_max[2] = a_anzahlWaren[i];
		}
	}
	return min_mid_max;
}

/**
 * returns count of players per trading good and amount of trading goods
 * @param ware
 * @param anzahl
 * @return
 */
public int getSpielerAnzahlProWareUndAnzahl(int ware, int anzahl){
	
	int spielerAnzahl = 0;
	for(int i = 0 ;i < spieleranzahl; i++){
		int aktAnzahl = this.getA_Waren(i)[ware];
		if(aktAnzahl == anzahl){
			spielerAnzahl ++;
		}
	}
	return spielerAnzahl;
}

public int getZBesetzteProvinzen(){
	int z_besetzt=0;
	Provinz aktProvinz;
	for(int i=0;i<v_Provinzen.size();i++){
		aktProvinz=this.getProvinzByIndex(i);
		if(aktProvinz.getSpielernummer()!=Konstanten.NIXXOS){
			z_besetzt++;
		}
	}
	return z_besetzt;
}

public Provinz getProvinzByOid(int id){
	Provinz aktProvinz;
	Provinz returnProvinz=null;
	for (int i=0;i<v_Provinzen.size();i++){
         aktProvinz=(Provinz)v_Provinzen.elementAt(i);
         if(aktProvinz.getOid()==id){
        	 returnProvinz=aktProvinz;
         }
      }
	return returnProvinz;
}

public Region getRegionByIndex(int index){
	return (Region)v_Regionen.elementAt(index);
}

public RPGrenze getRPGrenzeByIndex(int index){
	return (RPGrenze)v_RPGrenzen.elementAt(index);
}

public Vector<Provinz> getV_Provinzen() {
	return v_Provinzen;
}

public Vector<Region> getV_Regionen() {
	return v_Regionen;
}

public Vector<RPGrenze> getV_RPGrenzen() {
	return v_RPGrenzen;
}

public int getKostenRegion(int region){
	return this.getRegionByOid(region).getKosten();
}

public Region getRegionByOid(int id){
	Region aktRegion;
	Region returnRegion=null;
	for (int i=0;i<v_Regionen.size();i++){
         aktRegion=(Region)v_Regionen.elementAt(i);
         if(aktRegion.getOid()==id){
        	 returnRegion=aktRegion;
         }
      }
	return returnRegion;
}

public RPGrenze getRPGrenzeByOid(int id){
	RPGrenze aktRPGrenze;
	RPGrenze returnRPGrenze=null;
	for (int i=0;i<v_RPGrenzen.size();i++){
         aktRPGrenze=(RPGrenze)v_RPGrenzen.elementAt(i);
         if(aktRPGrenze.getOid()==id){
        	 returnRPGrenze=aktRPGrenze;
         }
      }
	return returnRPGrenze;
}

public int getZStaedteSpieler(int spielernummer){
	Provinz aktProvinz;
	int z_Staedte=0;
	for (int i=0;i<v_Provinzen.size();i++){
         aktProvinz=(Provinz)v_Provinzen.elementAt(i);
         if(aktProvinz.getSpielernummer()==spielernummer&&aktProvinz.isStadt()){
        	 z_Staedte++;
         }
      }
	return z_Staedte;
}

public String getNameProvinz(int provinzid){
	return this.getProvinzByOid(provinzid).getName();
}

public String getNameRegion(int regionid){
	return this.getRegionByOid(regionid).getName();
}

}//end class


