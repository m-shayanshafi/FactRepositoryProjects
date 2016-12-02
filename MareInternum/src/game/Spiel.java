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

import java.util.Vector;

import tools.Localization;

import entities.Konstanten;
import entities.KonstantenProtokoll;

public class Spiel {

	//constant values for each 
    public final int P_GEHALT = 0;
    public final int P_PROVINZWAEHLEN = 1;
    public final int P_KARTENWAEHLEN = 2;
    public final int P_AKTION = 3;
    
    private Spielbrett spielbrett;
    private int spieleranzahl;
    private Spieler[] spieler;
    private int startspieler;
    private int aktuellerSpieler;
    private int aktuelleProvinz;
    private int phase;
    private int karte;
    private int wuerfel;
    private int verteidiger;
    private int angriffsRegion;
    private int verteidigungsRegion;
    private int kampfProvinz;
    
    /**
     * response from server
     */
    private String antwort;
    
    /**
     * part of response which says what happened
     */
    private String waspassierte;

	/**
	 * constructor
	 * @param ss_spieler
	 * @param ss_spielbrett
	 */
	public Spiel(Vector<Spieler> ss_spieler,Spielbrett ss_spielbrett){
        spielbrett = ss_spielbrett;
        spieleranzahl = ss_spieler.size();
        spieler = new Spieler[spieleranzahl];
        Spieler aktSpieler;
        
        for(int i=0;i<ss_spieler.size();i++){
        	aktSpieler = ss_spieler.elementAt(i);
        	aktSpieler.set_Spielernummer(i);
        	spieler[i]=aktSpieler;
        }
        
        aktuelleProvinz = 0;
        aktuellerSpieler =0;
        verteidiger= Konstanten.NIXXOS;
        angriffsRegion = Konstanten.NIXXOS;
        verteidigungsRegion = Konstanten.NIXXOS;
        kampfProvinz = Konstanten.NIXXOS;
        phase =1;
        karte =0;
        antwort = "";
        waspassierte = "";
        verteidiger = Konstanten.NIXXOS;
}
	
	/**
	 * returns the number of the player, by his session id
	 * @param id
	 * @return
	 */
	private int getSpielernummerById(int id){
		for(int i=0;i<spieler.length;i++){
			if(spieler[i].get_id()==id){
				return spieler[i].get_Spielernummer();
			}
        }
		return Konstanten.NIXXOS;
		
	}

    /**
     * game logic of server
     * creates a response from a request array
     * @param ss_aktSpieler: current player
     * @param ss_spielzug: request
     * @return
     */
    public String verarbeite_Spielzug(int ss_aktSpieler,String[] ss_spielzug){
        String nachricht = "";
        //Eingang: Spieler/Aktion/Parameter/Parameter
        //Antwort:"Option/wer ist dran/mit was/was ist passiert"
        antwort = "03"+KonstantenProtokoll.SEPARATOR1+"Es ist ein Fehler aufgetreten";
        String[] spielzug=ss_spielzug;
        
        int aktSpielerFromRequest = this.getSpielernummerById(ss_aktSpieler);
        
        if(aktSpielerFromRequest==aktuellerSpieler || aktSpielerFromRequest==verteidiger){
        	
          switch(Integer.parseInt(spielzug[0])){
  //hat Provinz ausgewählt
          case KonstantenProtokoll.A_PROVINZGEWAEHLT:if(phase==P_PROVINZWAEHLEN){
                       antwort = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.PROVINZWAEHLEN+KonstantenProtokoll.SEPARATOR1+Konstanten.NIXXOS+KonstantenProtokoll.SEPARATOR1+"Bitte nochmal.";
                       int wohin = Integer.parseInt(spielzug[1]);
                       if (!spielbrett.isProvinzBesetzt(wohin)){
                          aktuelleProvinz=wohin;
                          phase = P_KARTENWAEHLEN;
                          aktuellerSpieler=startspieler;
                          int aktuelleWare=spielbrett.get_provinzWare(aktuelleProvinz);
                          spielbrett.besetze_ProvinzPS(aktuelleProvinz,aktuellerSpieler);
                          waspassierte=KonstantenProtokoll.WP_PROVINZERSTEIGERT+KonstantenProtokoll.SEPARATORSUBJEKT+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+aktuelleProvinz+KonstantenProtokoll.SEPARATORSUBJEKT+aktuelleWare;
                          antwort = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.KARTENWAEHLEN+KonstantenProtokoll.SEPARATORSUBJEKT+startspieler+KonstantenProtokoll.SEPARATOR1+waspassierte;
                          if(spielbrett.getZBesetzteProvinzen()==Konstanten.PROVINZENANZAHL){
                     		 this.spielende();
                     	}
                       }//end if
                      }//end if
                      break;
 // hat Karten ausgewählt
             case KonstantenProtokoll.A_KARTENGEWAEHLT:if(phase==P_KARTENWAEHLEN){
                      spieler[aktuellerSpieler].set_karten(Integer.parseInt(spielzug[1]),Integer.parseInt(spielzug[2]));
                      waspassierte = KonstantenProtokoll.WP_KARTENGEWAEHLT+KonstantenProtokoll.SEPARATORSUBJEKT+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+spielzug[1]+KonstantenProtokoll.SEPARATORSUBJEKT+spielzug[2];
                      this.antwortNaechsterSpieler();
                      }
                      break;
            case KonstantenProtokoll.A_ANGEGRIFFEN:if(phase==P_AKTION){
                    int wohin = Integer.parseInt(spielzug[2]);
                    int woher = Integer.parseInt(spielzug[1]);
                    if(spielbrett.getSpielernummerVonProvinz(wohin)!=aktuellerSpieler&&woher!=Konstanten.NIXXOS&&wohin!=Konstanten.NIXXOS&&spielbrett.is_regionProvinzGrenze(woher,wohin)){
                    	waspassierte=KonstantenProtokoll.WP_ANGEGRIFFEN+KonstantenProtokoll.SEPARATORSUBJEKT+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+spielzug[1]+KonstantenProtokoll.SEPARATORSUBJEKT+spielzug[2];
                        verteidiger =spielbrett.getSpielernummerVonProvinz(wohin);
                        if((verteidiger!=Konstanten.NIXXOS)&&(spielbrett.get_einheitenRS(woher,aktuellerSpieler)>0)&&(spieler[aktuellerSpieler].get_zenturios()>0)){
                            angriffsRegion = woher;
                            kampfProvinz = wohin;
                            if(spieler[verteidiger].get_zenturios()<1){
                            	spieler[aktuellerSpieler].set_zenturiodazu(-1);
                                spielbrett.besetze_ProvinzPS(kampfProvinz,aktuellerSpieler);  
                                waspassierte = this.getKampfWaspassierte(KonstantenProtokoll.MSG_MUSSTEKAPITULIEREN,aktuellerSpieler);
                                this.antwortNaechsterSpieler();
                                verteidiger=Konstanten.NIXXOS;
                            }
                            else{
                            	 antwort = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+verteidiger+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.VERTEIDIGEN+KonstantenProtokoll.SEPARATORSUBJEKT+startspieler+KonstantenProtokoll.SEPARATOR1+waspassierte;
                            }
                           
                        }//end if
                        else{
                        	waspassierte=KonstantenProtokoll.WP_NIX+KonstantenProtokoll.SEPARATORSUBJEKT+Konstanten.NOMSG;
                        	this.antwortNaechsterSpieler();
                        }//end else
                    }
                    else{
                    	waspassierte=KonstantenProtokoll.WP_NIX+KonstantenProtokoll.SEPARATORSUBJEKT+Konstanten.NOMSG;
                    	this.antwortNaechsterSpieler();
                    }
                    
                }//end if
                break;
            case KonstantenProtokoll.A_VERTEIDIGT:if(phase==P_AKTION){
                   verteidigungsRegion =  angriffsRegion;
                            this.kampf();
                            this.antwortNaechsterSpieler();
                            verteidiger=Konstanten.NIXXOS;
                }
                break;
            case KonstantenProtokoll.A_KAPITULIERT:if(phase==P_AKTION){
                verteidigungsRegion =  angriffsRegion;
                spieler[aktuellerSpieler].set_zenturiodazu(-1);
                spielbrett.set_einheitendazuRSE(angriffsRegion,aktuellerSpieler,-1);
                spielbrett.besetze_ProvinzPS(kampfProvinz,aktuellerSpieler);      
                waspassierte = this.getKampfWaspassierte(KonstantenProtokoll.MSG_HATKAPITULIERT,aktuellerSpieler);
                this.antwortNaechsterSpieler();
                verteidiger=Konstanten.NIXXOS;
             }
             break;
            case KonstantenProtokoll.A_TRAININERT:if(phase==P_AKTION){
                int wieviel = Integer.parseInt(spielzug[1]);
                if(spieler[aktuellerSpieler].getGeld()>wieviel*Konstanten.KOSTENTRAINING){
                	spieler[aktuellerSpieler].setTrainingsFaktorDazu((float)wieviel/10.0f);
                	spieler[aktuellerSpieler].set_gelddazu(-wieviel*Konstanten.KOSTENTRAINING);
                }
                else{
                	spieler[aktuellerSpieler].setTrainingsFaktorDazu((float)spieler[aktuellerSpieler].getGeld()/Konstanten.KOSTENTRAINING/10);
                	spieler[aktuellerSpieler].set_gelddazu(-spieler[aktuellerSpieler].getGeld()/Konstanten.KOSTENTRAINING);
                }
                waspassierte = KonstantenProtokoll.WP_TRAINIERT+KonstantenProtokoll.SEPARATORSUBJEKT+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+spieler[aktuellerSpieler].getGeld()+KonstantenProtokoll.SEPARATORSUBJEKT+spieler[aktuellerSpieler].getTrainingsFaktor();
                
                this.antwortNaechsterSpieler();
             }
             break;
            case KonstantenProtokoll.A_EINHEITENVERSCHOBEN:if(phase==P_AKTION){
                   int woher = Integer.parseInt(spielzug[1]);
                   int wohin = Integer.parseInt(spielzug[2]);
                   int wieviel = Integer.parseInt(spielzug[3]);
                   waspassierte = KonstantenProtokoll.WP_NIX+KonstantenProtokoll.SEPARATORSUBJEKT+Konstanten.NOMSG;
                      if(spielbrett.is_regionenGrenze(Integer.parseInt(spielzug[1]),Integer.parseInt(spielzug[2]))){
                        if(wieviel<=spielbrett.get_einheitenRS(woher,aktuellerSpieler)){
                           spielbrett.set_einheitendazuRSE(wohin,aktuellerSpieler,wieviel);
                           spielbrett.set_einheitendazuRSE(woher,aktuellerSpieler,(-1)*wieviel);
                           waspassierte = KonstantenProtokoll.WP_EINHEITENVERSCHOBEN +KonstantenProtokoll.SEPARATORSUBJEKT+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+spielzug[1]+KonstantenProtokoll.SEPARATORSUBJEKT+spielbrett.get_einheitenRS(woher,aktuellerSpieler)+KonstantenProtokoll.SEPARATORSUBJEKT+spielzug[2]+spielbrett.get_einheitenRS(wohin,aktuellerSpieler);
                           this.antwortNaechsterSpieler();
                        }//end if
                        else{
                          antwort = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.EINHEITENKAUFEN+KonstantenProtokoll.SEPARATORSUBJEKT+startspieler+KonstantenProtokoll.SEPARATOR1+KonstantenProtokoll.WP_FEHLER+KonstantenProtokoll.SEPARATORSUBJEKT+Localization.getInstance().getString("NotEnoughTroops");
                     }//end else
                     }//end if
                     else{
                          antwort = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.EINHEITENKAUFEN+KonstantenProtokoll.SEPARATORSUBJEKT+startspieler+KonstantenProtokoll.SEPARATOR1+KonstantenProtokoll.WP_FEHLER+KonstantenProtokoll.SEPARATORSUBJEKT+Localization.getInstance().getString("NoBorderingRegion");
                     }
                  }//end if
                  break;
                  //hat nichts gemacht
            case KonstantenProtokoll.A_NIX:if(phase==P_AKTION){
                     switch(karte){
                     
                     //Botschafter
                     case KonstantenProtokoll.K_ENDE:
                    	 this.beendeSpiel();
                         break;
                        case KonstantenProtokoll.K_BOTSCHAFTER:
                            waspassierte = KonstantenProtokoll.WP_NIX+KonstantenProtokoll.SEPARATORSUBJEKT+Konstanten.NOMSG;
                            if(spieler[aktuellerSpieler].get_is_kartevorhanden(KonstantenProtokoll.K_BOTSCHAFTER)){
                               spieler[aktuellerSpieler].setBotschafter(spieler[aktuellerSpieler].getBotschafter()+1);
                               waspassierte = KonstantenProtokoll.WP_BOTSCHAFTERGESENDET+KonstantenProtokoll.SEPARATORSUBJEKT+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+spieler[aktuellerSpieler].getBotschafter();
                            }//end if
                            this.antwortNaechsterSpieler();
                            break;
                        //Zenturio
                        case KonstantenProtokoll.K_ZENTURIO:
                            if(spieler[aktuellerSpieler].get_is_kartevorhanden(KonstantenProtokoll.K_ZENTURIO)){
                              spieler[aktuellerSpieler].set_zenturiodazu(1);
                              waspassierte = KonstantenProtokoll.WP_ZENTURIODAZU+KonstantenProtokoll.SEPARATORSUBJEKT+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+spieler[aktuellerSpieler].get_zenturios();
                            }//end if
                            else{
                              waspassierte =KonstantenProtokoll.WP_NIX+KonstantenProtokoll.SEPARATORSUBJEKT+Konstanten.NOMSG;
                            }//end else
                            this.antwortNaechsterSpieler();
                            break;
                        //angriff
                        case KonstantenProtokoll.K_ANGREIFEN:
                           waspassierte = KonstantenProtokoll.WP_NIX+KonstantenProtokoll.SEPARATORSUBJEKT+Konstanten.NOMSG;
                           this.antwortNaechsterSpieler();
                           break;
                        //Einheiten kaufen
                        case KonstantenProtokoll.K_EINHEITENKAUFEN:
                            waspassierte = KonstantenProtokoll.WP_NIX+KonstantenProtokoll.SEPARATORSUBJEKT+Konstanten.NOMSG;
                            this.antwortNaechsterSpieler();
                            break;
                         //Lager bauen
                        case KonstantenProtokoll.K_LAGERBAUEN:
                            waspassierte = KonstantenProtokoll.WP_NIX+KonstantenProtokoll.SEPARATORSUBJEKT+Konstanten.NOMSG;
                            this.antwortNaechsterSpieler();
                            break;
                        case KonstantenProtokoll.K_TRAINIEREN:
                            waspassierte = KonstantenProtokoll.WP_NIX+KonstantenProtokoll.SEPARATORSUBJEKT+Konstanten.NOMSG;
                            this.antwortNaechsterSpieler();
                            break;
                        }//end switch
                      }//end if
                      else if(phase==P_GEHALT){
                        
                    	  waspassierte = KonstantenProtokoll.WP_GEHALT+KonstantenProtokoll.SEPARATORSUBJEKT;
                    	  boolean isZenturio= this.gehalt_wuerfeln();
                         for(int i=0;i<spieleranzahl;i++){
                           waspassierte += spieler[i].getGeld()+KonstantenProtokoll.SEPARATORSUBJEKT;
                           
                         }
                         for(int i=0;i<spieleranzahl;i++){
                        	 waspassierte += spieler[i].get_gehalt()+KonstantenProtokoll.SEPARATORSUBJEKT; 
                         }
                         if(isZenturio){
                        	 waspassierte+=KonstantenProtokoll.MSG_GEHALT_FUERZENTURIOS;
                         }
                         else{
                        	 waspassierte+=KonstantenProtokoll.MSG_GEHALT_NORMAL;
                         }
                         this.antwortNaechsterSpieler();
                      }//end if
                       break;
   //hat Einheiten gekauft
            case KonstantenProtokoll.A_EINHEITENGEKAUFT:
                int kosten=0; 
            	int wieviel;
                int region;
            	for(int i=1;i<spielzug.length;i++){
            		region=i-1;
        			wieviel=Integer.parseInt(spielzug[i]);
                    kosten = kosten+(spielbrett.get_einheitenKosten(region)*wieviel);
            	}
            	if(kosten<=spieler[aktuellerSpieler].getGeld()){
            		waspassierte = KonstantenProtokoll.WP_EINHEITENGEKAUFT+KonstantenProtokoll.SEPARATORSUBJEKT+aktuellerSpieler;
            		for(int i=1;i<spielzug.length;i++){
            			region=i-1;
            			wieviel=Integer.parseInt(spielzug[i]);
                        kosten = spielbrett.get_einheitenKosten(region);
                        spielbrett.set_einheitendazuRSE(region,aktuellerSpieler,wieviel);
                        spieler[aktuellerSpieler].set_gelddazu(wieviel *kosten*(-1));
                        waspassierte = waspassierte+KonstantenProtokoll.SEPARATORSUBJEKT+spielbrett.get_einheitenRS(region,aktuellerSpieler);
                       
                        
            		}
            		 waspassierte+=KonstantenProtokoll.SEPARATORSUBJEKT+spieler[aktuellerSpieler].getGeld();
            		this.antwortNaechsterSpieler();
            	}
            	else{
            		waspassierte=KonstantenProtokoll.WP_NIX+KonstantenProtokoll.SEPARATORSUBJEKT+Konstanten.NOMSG;
            		antwort = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.EINHEITENKAUFEN+KonstantenProtokoll.SEPARATORSUBJEKT+startspieler+KonstantenProtokoll.SEPARATOR1+waspassierte;  
            	}
                break;
            case KonstantenProtokoll.A_LAGERGEBAUT:
                         int provinz = Integer.parseInt(spielzug[1]);
                         waspassierte = KonstantenProtokoll.WP_NIX+KonstantenProtokoll.SEPARATORSUBJEKT+Konstanten.NOMSG;
                         if(provinz!=Konstanten.NIXXOS&&spielbrett.getSpielernummerVonProvinz(provinz)==aktuellerSpieler&&spielbrett.get_legionslagerP(provinz)==0){
                            if(spieler[aktuellerSpieler].getGeld()>=Konstanten.KOSTENLAGER&&spieler[aktuellerSpieler].get_zenturios()>=1){
                            	spieler[aktuellerSpieler].set_gelddazu(-Konstanten.KOSTENLAGER);
                            	spieler[aktuellerSpieler].set_zenturiodazu(-1);
                            	spielbrett.set_LagerAbsolutPSL(provinz,aktuellerSpieler,1);
                            	waspassierte = KonstantenProtokoll.WP_LAGERGEBAUT+KonstantenProtokoll.SEPARATORSUBJEKT+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+provinz+KonstantenProtokoll.SEPARATORSUBJEKT+1+KonstantenProtokoll.SEPARATORSUBJEKT+spieler[aktuellerSpieler].getGeld()+KonstantenProtokoll.SEPARATORSUBJEKT+spieler[aktuellerSpieler].get_zenturios();
                            	
                            }
                            else{
                            	if(spieler[aktuellerSpieler].getGeld()<Konstanten.KOSTENLAGER){
                            		waspassierte = KonstantenProtokoll.WP_NIX+KonstantenProtokoll.SEPARATORSUBJEKT+Konstanten.NOMSG;
                            	}
                            	if(spieler[aktuellerSpieler].get_zenturios()<1){
                            		waspassierte = KonstantenProtokoll.WP_NIX+KonstantenProtokoll.SEPARATORSUBJEKT+Konstanten.NOMSG;
                            	}
                            	if(spieler[aktuellerSpieler].getGeld()<5&&spieler[aktuellerSpieler].get_zenturios()<1){
                            		waspassierte = KonstantenProtokoll.WP_NIX+KonstantenProtokoll.SEPARATORSUBJEKT+Konstanten.NOMSG;
                            	}
                            }
                            this.antwortNaechsterSpieler();
                         }
                         else {
                        	 this.antwortNaechsterSpieler();
                         }
                         break;
           
            case KonstantenProtokoll.A_BONUSPUNKTEGEWAEHLT:
            	spieler[aktuellerSpieler].setBoniDazu(Konstanten.PUNKTE_BONUS);
            	waspassierte=KonstantenProtokoll.WP_BONUSPUNKTE+KonstantenProtokoll.SEPARATORSUBJEKT+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+spieler[aktuellerSpieler].getBoni();
            	this.antwortNaechsterSpieler();
            	break;
            case KonstantenProtokoll.A_BONUSEINHEITENGEWAEHLT:
            	waspassierte=KonstantenProtokoll.WP_BONUSEINHEITEN+KonstantenProtokoll.SEPARATORSUBJEKT+aktuellerSpieler+"";
            	int zielregion;
            	for(int i=1;i<spielzug.length;i++){
            		zielregion=i-1;
            		int wieviele=Integer.parseInt(spielzug[i]);
            		spielbrett.set_einheitendazuRSE(zielregion,aktuellerSpieler,wieviele);
            		waspassierte=waspassierte+KonstantenProtokoll.SEPARATORSUBJEKT+spielbrett.get_einheitenRS(zielregion,aktuellerSpieler);
            	}
            	this.antwortNaechsterSpieler();
            	break;
            }//end switch

            }// end if
            nachricht = antwort;
            return nachricht;
        }

	/**
	 * generates the income of the players and sets it for the player instances
	 * returns if income was from taxes or from warlords
	 * @return
	 */
	private boolean gehalt_wuerfeln(){
    	boolean isZenturio=false;
    	wuerfel = (((int)(Math.random()*100))%6)+1;
    	int stadtbonus=0;
       
    	for(int i=0;i<spieleranzahl;i++){
        	  stadtbonus=0;
        	  stadtbonus=spielbrett.getZStaedteSpieler(i);
        	  if (wuerfel == 1 || wuerfel == 6){
        		  spieler[i].set_gelddazu(spieler[i].get_zenturios()+stadtbonus);
        		  isZenturio=true;
        	  }
        	  else{
        		  spieler[i].set_gelddazu(wuerfel+stadtbonus);
        	  }
    	}//end for
    	return isZenturio;
    }
	
	/**
	 * looks which player comes next and generates the response String
	 */
    private void antwortNaechsterSpieler(){  	
    	
    	switch(phase){
        case P_AKTION:
            switch(karte){
            case KonstantenProtokoll.K_BOTSCHAFTER:
                antwort =KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.WARTEN+KonstantenProtokoll.SEPARATORSUBJEKT+startspieler+KonstantenProtokoll.SEPARATOR1+waspassierte;
               
                karte=KonstantenProtokoll.K_ZENTURIO;
                   if(spieler[aktuellerSpieler].get_is_kartevorhanden(KonstantenProtokoll.K_BOTSCHAFTER)){
                      if(spieler[aktuellerSpieler].getBotschafter()==3){
                         spieler[aktuellerSpieler].setBotschafter(0);
                         antwort = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.GESCHENKWAEHLEN+KonstantenProtokoll.SEPARATORSUBJEKT+startspieler+KonstantenProtokoll.SEPARATOR1+waspassierte;//player can choose
                         karte=KonstantenProtokoll.K_BOTSCHAFTER;
                      }//end if
                   }//end if
                break;
            case KonstantenProtokoll.K_ZENTURIO:
                antwort = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.WARTEN+KonstantenProtokoll.SEPARATORSUBJEKT+startspieler+KonstantenProtokoll.SEPARATOR1+waspassierte;
                    karte=KonstantenProtokoll.K_ANGREIFEN;
                    if(spieler[aktuellerSpieler].get_is_kartevorhanden(KonstantenProtokoll.K_ANGREIFEN)){
                       antwort = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.ANGREIFEN+KonstantenProtokoll.SEPARATORSUBJEKT+startspieler+KonstantenProtokoll.SEPARATOR1+waspassierte;
                       }//end if
                break;
            case KonstantenProtokoll.K_LAGERBAUEN:
                antwort = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.WARTEN+KonstantenProtokoll.SEPARATORSUBJEKT+startspieler+KonstantenProtokoll.SEPARATOR1+waspassierte;
                   karte=KonstantenProtokoll.K_EINHEITENKAUFEN;
                   if(spieler[aktuellerSpieler].get_is_kartevorhanden(KonstantenProtokoll.K_EINHEITENKAUFEN)){
                      antwort = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.EINHEITENKAUFEN+KonstantenProtokoll.SEPARATORSUBJEKT+startspieler+KonstantenProtokoll.SEPARATOR1+waspassierte;
                   }

                break;
            case KonstantenProtokoll.K_ANGREIFEN:
                antwort = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.WARTEN+KonstantenProtokoll.SEPARATORSUBJEKT+startspieler+KonstantenProtokoll.SEPARATOR1+waspassierte;
                   karte=KonstantenProtokoll.K_LAGERBAUEN;
                   if(spieler[aktuellerSpieler].get_is_kartevorhanden(KonstantenProtokoll.K_LAGERBAUEN)){
                      antwort = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.LAGERBAUEN+KonstantenProtokoll.SEPARATORSUBJEKT+startspieler+KonstantenProtokoll.SEPARATOR1+waspassierte;
                   }//end if
                break;
            case KonstantenProtokoll.K_EINHEITENKAUFEN:
                
                
                
                	karte=KonstantenProtokoll.K_TRAINIEREN; 
                	if(spieler[aktuellerSpieler].get_is_kartevorhanden(KonstantenProtokoll.K_TRAINIEREN)){
                		 antwort = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.TRAININEREN+KonstantenProtokoll.SEPARATORSUBJEKT+startspieler+KonstantenProtokoll.SEPARATOR1+waspassierte;
                	}
                	else{
                		antwort = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.WARTEN+KonstantenProtokoll.SEPARATORSUBJEKT+startspieler+KonstantenProtokoll.SEPARATOR1+waspassierte;
                	}
                
                
                break;
            case KonstantenProtokoll.K_TRAINIEREN:
            	aktuellerSpieler=(aktuellerSpieler+1)%spieleranzahl;
            	if(aktuellerSpieler==startspieler){
            		phase=P_GEHALT;
                    karte=KonstantenProtokoll.K_BOTSCHAFTER;
                    antwort = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.WARTEN+KonstantenProtokoll.SEPARATORSUBJEKT+startspieler+KonstantenProtokoll.SEPARATOR1+waspassierte;
            	}
            	else{
                	antwort = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.KARTENWAEHLEN+KonstantenProtokoll.SEPARATORSUBJEKT+startspieler+KonstantenProtokoll.SEPARATOR1+waspassierte;
                	phase = P_KARTENWAEHLEN;
                }
            	break;
        
               
            }
            break;
        case P_GEHALT:
        	phase = P_PROVINZWAEHLEN;
            startspieler = (startspieler+1)%spieleranzahl;
            aktuellerSpieler = startspieler;
            antwort = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.PROVINZWAEHLEN+KonstantenProtokoll.SEPARATORSUBJEKT+startspieler+KonstantenProtokoll.SEPARATOR1+waspassierte;
            break;
        case P_KARTENWAEHLEN:
            antwort = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.WARTEN+KonstantenProtokoll.SEPARATORSUBJEKT+startspieler+KonstantenProtokoll.SEPARATOR1+waspassierte;
                phase=P_AKTION;;
                karte = KonstantenProtokoll.K_BOTSCHAFTER;
                if(spieler[aktuellerSpieler].get_is_kartevorhanden(KonstantenProtokoll.K_BOTSCHAFTER)){
                     if(spieler[aktuellerSpieler].getBotschafter()==3){
                        spieler[aktuellerSpieler].setBotschafter(0);
                        antwort = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.GESCHENKWAEHLEN+KonstantenProtokoll.SEPARATORSUBJEKT+startspieler+KonstantenProtokoll.SEPARATOR1+waspassierte;//player can choose
                     }//end if
                }//end if
            break;
        }//end switch
    	
    	
    }
 
	/**
	 * calculates the situation of fight
	 * and creates a response
	 */
	private void kampf(){
		
		int grenzeZufallszahl=7;
		int gewinner = Konstanten.NIXXOS;
		int v_einheiten = spielbrett.get_einheitenRS(verteidigungsRegion,verteidiger);
		int a_einheiten = spielbrett.get_einheitenRS(angriffsRegion,aktuellerSpieler);
		int v_provinzen = spielbrett.get_angrenzendeProvinzenPS(kampfProvinz,verteidiger);
		int a_provinzen = spielbrett.get_angrenzendeProvinzenPS(kampfProvinz,aktuellerSpieler);
		int v_lagerK = spielbrett.get_legionslagerP(kampfProvinz)*3;
		int v_lager = spielbrett.get_angrenzendeLagerPS(kampfProvinz,verteidiger)*1;
		int a_lager = spielbrett.get_angrenzendeLagerPS(kampfProvinz,aktuellerSpieler)*1;
		
		float a_training=spieler[aktuellerSpieler].getTrainingsFaktor();
		float v_training=spieler[verteidiger].getTrainingsFaktor();
		
		int v_zufallszahl = (((int)(Math.random()*10))%(grenzeZufallszahl-1))+1;
		int a_zufallszahl = (((int)(Math.random()*10))%(grenzeZufallszahl-1))+1;
		
		int v_staerkeEinheiten=(int)((v_einheiten/2)+(v_einheiten*v_training));
		int a_staerkeEinheiten=(int)((a_einheiten/2)+(a_einheiten*a_training));
			
		int a_kampfpunkte = a_zufallszahl+a_lager+a_staerkeEinheiten+a_provinzen;
		int v_kampfpunkte = v_zufallszahl+v_lager+v_lagerK+v_staerkeEinheiten+v_provinzen;
		
		int a_verlust=a_zufallszahl/2;
		if(a_verlust<1){
			a_verlust=1;
		}
		a_verlust=a_verlust*-1;
		
		int v_verlust=v_zufallszahl/2;
		if(v_verlust<1){
			v_verlust=1;
		}
		v_verlust=v_verlust*-1;
		
		if(a_kampfpunkte> v_kampfpunkte){
		  spieler[aktuellerSpieler].set_zenturiodazu(-1);
		  spieler[verteidiger].set_zenturiodazu(-1);
		  spielbrett.besetze_ProvinzPS(kampfProvinz,aktuellerSpieler);
		  spielbrett.set_einheitendazuRSE(angriffsRegion,verteidiger,(v_zufallszahl-grenzeZufallszahl));
		  spielbrett.set_einheitendazuRSE(angriffsRegion,aktuellerSpieler,a_verlust);
		  gewinner = aktuellerSpieler;
		}
		else{
		  spieler[aktuellerSpieler].set_zenturiodazu(-1);
		  spielbrett.set_einheitendazuRSE(verteidigungsRegion,aktuellerSpieler,(v_zufallszahl-grenzeZufallszahl));
		  spielbrett.set_einheitendazuRSE(angriffsRegion,verteidiger,(v_verlust));
		  gewinner = verteidiger;
		}
			waspassierte = this.getKampfWaspassierte(
					a_kampfpunkte+KonstantenProtokoll.SEPARATORSUBJEKT+a_zufallszahl+KonstantenProtokoll.SEPARATORSUBJEKT+a_lager+KonstantenProtokoll.SEPARATORSUBJEKT+a_staerkeEinheiten+KonstantenProtokoll.SEPARATORSUBJEKT+a_training+KonstantenProtokoll.SEPARATORSUBJEKT+a_provinzen+KonstantenProtokoll.SEPARATORSUBJEKT+
					v_kampfpunkte+KonstantenProtokoll.SEPARATORSUBJEKT+v_zufallszahl+KonstantenProtokoll.SEPARATORSUBJEKT+(v_lager+v_lagerK)+KonstantenProtokoll.SEPARATORSUBJEKT+v_staerkeEinheiten+KonstantenProtokoll.SEPARATORSUBJEKT+v_training+KonstantenProtokoll.SEPARATORSUBJEKT+v_provinzen, gewinner);
		    //aggressor 0:points+ 1:random number+ 2:fortresses+ 3:strength troups + 4:training + 5:count of provinces
			//defender  6:points+ 7:random number+ 8:fortresses+ 9:strength troups + 10:training + 11:count of provinces
	
	}

	/**
	 * Method writes all information about a single fight into a string
	 * 0:fight is over+1:winner+2:province+3:region
	 * +4:aggressor+5:troups of aggressor+6:warlords of aggressor  
	 * +7:region+8:defender+9:troups of defender+10:warlords of defender
	 * +11:additional message
	 */
	private String getKampfWaspassierte(String msg,int gewinner){
		String wp=KonstantenProtokoll.WP_KAMPFVORBEI+KonstantenProtokoll.SEPARATORSUBJEKT+gewinner+KonstantenProtokoll.SEPARATORSUBJEKT+kampfProvinz+KonstantenProtokoll.SEPARATORSUBJEKT+angriffsRegion+KonstantenProtokoll.SEPARATORSUBJEKT+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+spielbrett.get_einheitenRS(angriffsRegion,aktuellerSpieler)+KonstantenProtokoll.SEPARATORSUBJEKT+spieler[aktuellerSpieler].get_zenturios()+KonstantenProtokoll.SEPARATORSUBJEKT+
	    verteidigungsRegion+KonstantenProtokoll.SEPARATORSUBJEKT+verteidiger+KonstantenProtokoll.SEPARATORSUBJEKT+spielbrett.get_einheitenRS(verteidigungsRegion,verteidiger)+KonstantenProtokoll.SEPARATORSUBJEKT+spieler[verteidiger].get_zenturios()+KonstantenProtokoll.SEPARATORSUBJEKT+msg;
		return wp;
	}
	
	/**
	 * returns first player in current turn
	 * @return
	 */
	public int getStartspieler(){
		return startspieler;
	}

	/**
	 * generates response for final points calculation
	 */
	private void beendeSpiel(){
		int punkte=0;
		int gewinner=0;
		
		int[] spielerpunkte=new int[spielbrett.getSpieleranzahl()];
		 waspassierte=KonstantenProtokoll.WP_SPIELENDE+"";
		
		 for(int i=0;i<spielbrett.getSpieleranzahl();i++){
			spielerpunkte[i]=(spielbrett.getPunkteSpieler(i)+spieler[i].getBoni());
			if(punkte<spielerpunkte[i]){
				punkte=spielerpunkte[i];
				gewinner=i;
			}
		}
		waspassierte+=KonstantenProtokoll.SEPARATORSUBJEKT+gewinner;
		for(int i=0;i<spielbrett.getSpieleranzahl();i++){
			waspassierte+=KonstantenProtokoll.SEPARATORSUBJEKT+spielerpunkte[i];
		}
		antwort =KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+gewinner+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.STOP+KonstantenProtokoll.SEPARATORSUBJEKT+gewinner+KonstantenProtokoll.SEPARATOR1+waspassierte;
	}

	/**
	 * generates message for end of game 
	 */
	private void spielende(){		
		antwort =KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+aktuellerSpieler+KonstantenProtokoll.SEPARATORSUBJEKT+KonstantenProtokoll.WARTEN+KonstantenProtokoll.SEPARATORSUBJEKT+startspieler+KonstantenProtokoll.SEPARATOR1+waspassierte;
	  	karte=KonstantenProtokoll.K_ENDE;		
		phase=P_AKTION;
	}

}//end class




