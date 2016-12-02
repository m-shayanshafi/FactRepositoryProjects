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

import network.Client;
import network.Netzwerkable;

import org.apache.mina.transport.socket.nio.SocketConnector;

import tools.Localization;
import tools.Parser;

import entities.Konstanten;
import entities.KonstantenProtokoll;
import entities.RPGrenze;

import gui.DlgGewinner;

import java.util.Observable;
import java.util.ResourceBundle;

/**
 * This class manages a human client. It interprets all messages which are coming from the server,
 * and create a response from the user actions which is send back to the server. 
 * 
 * In addition it holds all user specific data.
 */
public class VerwaltungClient extends Observable implements Netzwerkable{

	/**
	 * An array with all names from the players in the game
	 */
	private String[] a_spielernamen;
	
	/**
	 * An array with all players in the game
	 */
	private Spieler[] a_spieler;
	
	/**
	 * network connection
	 */
	private Client meinClient;

	/**
	 * the id of the current action which the user has to achieve 
	 */
	private int aktAktion;
	
	/**
	 * cache for troops which are set into a region, or removed from there
	 */
	private int[] einheitenCache;
	
	/**
	 * id of province where the player wants to build a new fortress
	 */
	private int neuesLager;
	
	/**
	 * array with true or false for each action id
	 * true=action can be performed by this player
	 * false=action can not performed by this player
	 * 
	 * currently not active
	 */
	private boolean[] aktionsMoeglichkeiten;
	
	/**
	 * true if game already is started
	 */
	private boolean isSpielgestartet;
	
	/**
	 * is it the players turn at the moment
	 */
	private boolean isSpieleraktiv;
	
	/**
	 * chat message, which is received from the server
	 */
	private String s_chat;
	
	/**
	 * game instructions, which are displayed to inform
	 * the player about the current game status
	 */
	private String s_msg;
	
	/**
	 * id of the province which the player wants to attack
	 */
	private int angriffsProvinz;
	
	/**
	 * id of the region, where the player wants to attack from
	 */
	private int angriffsRegion;
	
	/**
	 * array with all points from all players at the end of the game
	 */
	private int[] a_spielEndpunkte;
	
	/**
	 * set to true if the player decides to take the troops from the gift of caesar
	 */
	private boolean isEinheitenGeschenk;
	
	/**
	 * reference to a gaming board instance. This instance holds all game board
	 * specific data like the troops of the player in specific region, or something
	 */
	private Spielbrett meinSpielbrett;
	
	/**
	 * id of the player him or herself
	 */
	private int spielerID;
	
	/**
	 * count of all players
	 */
	int spieleranzahl;
	
	private SocketConnector connector;
	
	public VerwaltungClient(){
		connector = new SocketConnector();
		isSpielgestartet=false;
		spielerID = Konstanten.NIXXOS;
		aktAktion=KonstantenProtokoll.WARTEN;
		meinSpielbrett = new Spielbrett();
		einheitenCache= new int[Konstanten.REGIONENANZAHL];
		s_msg="--";
		s_chat="--";
		isEinheitenGeschenk=false;
		angriffsRegion=1;
		angriffsProvinz=1;
	}
	

	/**
	 * connects client to game server
	 * returns true if connect procedure was successful
	 * 
	 * @param ip
	 * @param port
	 * @return
	 * @throws Exception
	 */
	public boolean nehmeAnSpielTeil(String ip,int port)
	throws Exception{
			
		meinClient = new Client(this, connector);
		if(!meinClient.connect(ip, port)){
			return false;
		}
	
		String name="";
		name = Main.showInputDialog(Localization.getInstance().getString("YourName"),"");
			
		if(name == null || name.trim().equals("")){
			name = "Atomfred II";
		} 
			
		this.nachrichtAnServer(KonstantenProtokoll.ANMELDUNG+KonstantenProtokoll.SEPARATORHEADER+name);
			
		return true;
	}
	
	
	/**
	 * select a province
	 * @param ss_provinz
	 */
	public void provinzAusgewaehlt(int ss_provinz){
		if(aktAktion==KonstantenProtokoll.PROVINZWAEHLEN){
			meinSpielbrett.setVesetzteAusgewaehlteProvinz(ss_provinz);
		}
		if(aktAktion==KonstantenProtokoll.LAGERBAUEN){
			if(meinSpielbrett.getSpielernummerVonProvinz(ss_provinz)==this.getMeinSpielerByID().get_Spielernummer()&&
					meinSpielbrett.get_legionslagerP(ss_provinz)==0){
				neuesLager=ss_provinz;
				meinSpielbrett.set_LagerAbsolutPSL(ss_provinz,this.getMeinSpielerByID().get_Spielernummer(),-1);
			}
			else{
				neuesLager=Konstanten.NIXXOS;
				meinSpielbrett.resetLegionsLager();
			}
			
		}
		if(aktAktion==KonstantenProtokoll.ANGREIFEN){
			if(meinSpielbrett.getSpielernummerVonProvinz(ss_provinz)!=this.getMeinSpielerByID().get_Spielernummer()){
				angriffsProvinz=ss_provinz;
				this.setChanged();
				this.notifyObservers(Konstanten.NOMSG);
			}
			
		}
		
	}
	
	/**
	 * clear cache for troops
	 */
	public void resetEinheitenCache(){
		for(int i=0;i<einheitenCache.length;i++){
			einheitenCache[i]=0;
		}
	}
	
	/**
	 * clears all temporary data from an attack
	 */
	public void resetAngriff(){
		for(int j=0;j<a_spieler.length;j++){
			a_spieler[j].setAngriff(Konstanten.NOMSG);
		}
	}
	
	/**
	 * resets the choosen troops
	 */
	public void resetGewaehlt(){
		if(aktAktion==KonstantenProtokoll.EINHEITENKAUFEN){
			meinSpielbrett.set_einheitenArrayDazuES(einheitenCache,this.getMeinSpielerByID().get_Spielernummer(),-1);
			this.resetEinheitenCache();
		}
		else if(aktAktion==KonstantenProtokoll.GESCHENKWAEHLEN){
			meinSpielbrett.set_einheitenArrayDazuES(einheitenCache,this.getMeinSpielerByID().get_Spielernummer(),-1);
			this.resetEinheitenCache();
		}
		this.setChanged();
		this.notifyObservers(Konstanten.NOMSG);
	}
	
	/**
	 * handler for choosing a region
	 * troops are set or removed, or the region is set as region for the next attack
	 * @param ss_region
	 * @param ss_MouseButton
	 */
	public void einheitenAusgewaehlt(int ss_region,String ss_MouseButton){
		int aktProvinz=meinSpielbrett.getAusgewaehlteProvinz();
		int einheitenSumme=0;
		if(aktAktion==KonstantenProtokoll.GESCHENKWAEHLEN && isEinheitenGeschenk){
			einheitenSumme=0;
			for(int i=0; i < einheitenCache.length;i ++){
				einheitenSumme+=einheitenCache[i];
			}
			if(ss_MouseButton.equals("links")){
				if(einheitenSumme >= Konstanten.MAX_BONUS_EINHEITEN){
					//nix
				}
				else{
					einheitenCache[ss_region] ++;
					meinSpielbrett.set_einheitendazuRSE(ss_region, this.getMeinSpielerByID().get_Spielernummer(), 1);
				}
			}
			else if(ss_MouseButton.equals("rechts") && einheitenCache[ss_region] > 0){
				if(einheitenSumme < 1){
					//nix
					}
				else{
					einheitenCache[ss_region]--;
					meinSpielbrett.set_einheitendazuRSE(ss_region,this.getMeinSpielerByID().get_Spielernummer(), -1);
					
				}
			}	
				
		}
		else if(aktAktion==KonstantenProtokoll.ANGREIFEN){
			angriffsRegion=ss_region;
		}
		else if(aktAktion==KonstantenProtokoll.EINHEITENKAUFEN){
			if(ss_MouseButton.equals("links")){
				einheitenCache[ss_region]++;
				meinSpielbrett.set_einheitendazuRSE(ss_region,this.getMeinSpielerByID().get_Spielernummer(),1);
			}
			else if(ss_MouseButton.equals("rechts")){
				if(einheitenCache[ss_region]>0){
					einheitenCache[ss_region]--;
					meinSpielbrett.set_einheitendazuRSE(ss_region,this.getMeinSpielerByID().get_Spielernummer(),-1);
				}
				if(einheitenCache[ss_region]<=0){
					
				}
			}
		}
		this.setChanged();
		this.notifyObservers(Konstanten.NOMSG);
	}
	/**
	 * handler for choosing a gift from the caesar
	 * @param ss_geschenk
	 */
	public void geschenkGewaehlt(int ss_geschenk){
		String nachricht="";
		isEinheitenGeschenk=false;
		
		//points chosen
		if(ss_geschenk==KonstantenProtokoll.A_BONUSPUNKTEGEWAEHLT){	
			aktAktion=KonstantenProtokoll.WARTEN;
			nachricht=KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+
			KonstantenProtokoll.A_BONUSPUNKTEGEWAEHLT;
			this.nachrichtAnServer(nachricht);
		}
		
		//troops chosen
		else if(ss_geschenk==KonstantenProtokoll.A_BONUSEINHEITENGEWAEHLT){
			isEinheitenGeschenk=true;
			this.resetEinheitenCache();
			aktAktion=KonstantenProtokoll.GESCHENKWAEHLEN;
			this.setChanged();
			this.notifyObservers(Konstanten.NOMSG);
			
			
		}
		
	}
	
	/**
	 * handler for end turn button pressed
	 * @param msg
	 */
	public void weiterButtonGedrueckt(String msg){
		
		int provinz;
		String nachricht=msg;
		if(isSpielgestartet){
			
		//which was the current action?
		switch(aktAktion){
		
		//player had to choose a province
		case KonstantenProtokoll.PROVINZWAEHLEN:
			provinz=meinSpielbrett.getAusgewaehlteProvinz();
			if (provinz!=Konstanten.NIXXOS){
				nachricht=KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+
				KonstantenProtokoll.A_PROVINZGEWAEHLT+KonstantenProtokoll.SEPARATORSUBJEKT+provinz;
				aktAktion=KonstantenProtokoll.WARTEN;
				this.nachrichtAnServer(nachricht);
				
			}
			else{
				s_msg+="\n[!] "+Localization.getInstance().getString("ChooseProvinceWhichYouWantToAnnex");
				this.setChanged();
				this.notifyObservers(Konstanten.NOMSG);
			}
			break;
			
		//player had to wait
		case KonstantenProtokoll.WARTEN:
			break;
			
		//player had to choose actions
		case KonstantenProtokoll.KARTENWAEHLEN:
			if(nachricht.split(KonstantenProtokoll.SEPARATORSUBJEKT).length !=2){
				s_msg+="\n[!] "+Localization.getInstance().getString("ChooseExactlyTwoActions");
				this.setChanged();
				this.notifyObservers(Konstanten.NOMSG);
			}
			else{
				String[] s=nachricht.split(KonstantenProtokoll.SEPARATORSUBJEKT);
				if(this.checkKartenwahl(s)){
					nachricht = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+
					KonstantenProtokoll.A_KARTENGEWAEHLT+KonstantenProtokoll.SEPARATORSUBJEKT+nachricht;
					aktAktion=KonstantenProtokoll.WARTEN;
					this.nachrichtAnServer(nachricht);
				}
				else{
					this.setChanged();
					this.notifyObservers(Konstanten.NOMSG);
				}
				
				//aktAktion=Konstanten.NIXXOS;
			}
			break;
		
		//player had to choose a gift
		case KonstantenProtokoll.GESCHENKWAEHLEN:
			if(isEinheitenGeschenk){
				nachricht = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+
				KonstantenProtokoll.A_BONUSEINHEITENGEWAEHLT;
				for(int i=0;i<einheitenCache.length;i++){
					nachricht=nachricht+KonstantenProtokoll.SEPARATORSUBJEKT+einheitenCache[i];
				}
			}else{
				nachricht = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+
				KonstantenProtokoll.A_BONUSPUNKTEGEWAEHLT;
			}
			
			aktAktion = KonstantenProtokoll.WARTEN;
			this.nachrichtAnServer(nachricht);
			
			break;
			
		//player wanted to build a fortress
		case KonstantenProtokoll.LAGERBAUEN:
			provinz=neuesLager;
			if (true){
				nachricht=KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+
				KonstantenProtokoll.A_LAGERGEBAUT+KonstantenProtokoll.SEPARATORSUBJEKT+provinz;
				aktAktion=KonstantenProtokoll.WARTEN;
				this.nachrichtAnServer(nachricht);
			}
			else{
				s_msg+="\n[!] "+Localization.getInstance().getString("ChooseAProvinceForBuildingAFortress");
				this.setChanged();
				this.notifyObservers(Konstanten.NOMSG);
			}
			break;
		
		//player had to select an attack
		case KonstantenProtokoll.ANGREIFEN:
			nachricht=KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+
			KonstantenProtokoll.A_ANGEGRIFFEN+KonstantenProtokoll.SEPARATORSUBJEKT+angriffsRegion+KonstantenProtokoll.SEPARATORSUBJEKT+angriffsProvinz;
			aktAktion=KonstantenProtokoll.WARTEN;
			this.nachrichtAnServer(nachricht);
				
			break;
		
		//player wanted to train his or her troops
		case KonstantenProtokoll.TRAININEREN:
			nachricht=KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+
			KonstantenProtokoll.A_TRAININERT+KonstantenProtokoll.SEPARATORSUBJEKT+nachricht;
			aktAktion=KonstantenProtokoll.WARTEN;
			this.nachrichtAnServer(nachricht);
			break;
			
		//player was attacked and was asked to defend
		case KonstantenProtokoll.VERTEIDIGEN:
			
			if(Boolean.parseBoolean(nachricht)){
				nachricht=KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER;
				nachricht+=KonstantenProtokoll.A_VERTEIDIGT;
			}
			else{
				nachricht=KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER;
				nachricht+=KonstantenProtokoll.A_KAPITULIERT;
			}
			aktAktion=KonstantenProtokoll.WARTEN;
			this.nachrichtAnServer(nachricht);
			break;
		
		//player wanted to buy troops
		case KonstantenProtokoll.EINHEITENKAUFEN:
			nachricht = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+
			KonstantenProtokoll.A_EINHEITENGEKAUFT+KonstantenProtokoll.SEPARATORSUBJEKT;
			for(int i=0;i<einheitenCache.length;i++){
				nachricht=nachricht+einheitenCache[i]+KonstantenProtokoll.SEPARATORSUBJEKT;
			}
			this.resetGewaehlt();
			aktAktion = KonstantenProtokoll.WARTEN;
			this.nachrichtAnServer(nachricht);
			break;
			}
		}
		
		
		
	}

	/**
	 * handler if the client received a message from the server
	 */
public synchronized void nachrichtVonServer(String ss_nachricht){
		
	
		Parser prs=new Parser();
		
		ResourceBundle loc = Localization.getInstance();
		
		String nachricht="";
		
		//split header of message from body with information data 
		//the header describes the type of information data 
		String[] a_header = ss_nachricht.split(KonstantenProtokoll.SEPARATORHEADER);
		
		//split body into sub packages
		//there used to be two sub packages
		//package 1 is for data which describes what happened
		//package 0 is for the data which describes what happens next
		String[] a_infos = a_header[1].split(KonstantenProtokoll.SEPARATOR1);
		
		String[] a_subjekt;
		
		//***
		//interpretation of data
		//
		//switch on header data
		//-- switch on package 1 
		//-- switch on package 0
		//
		//***
		
		//player id is send
		if(a_header[0].equals(""+KonstantenProtokoll.ID)){
			if(spielerID==Konstanten.NIXXOS){
				spielerID=Integer.parseInt(a_header[1]);
				}
			
		}
		
		//login
		else if(a_header[0].equals(Integer.toString(KonstantenProtokoll.ANMELDUNG))){
			//meinDlg = new DlgAnmelden(this);
		}
		
		//chat message
		else if(a_header[0].equals(Integer.toString(KonstantenProtokoll.CHAT))){

			nachricht=prs.decode(a_header[1]);
			s_chat+="\n"+nachricht;
			this.setChanged();
			this.notifyObservers(Konstanten.CHATMSG);
		}
		
		//start of the game
		else if(a_header[0].equals(Integer.toString(KonstantenProtokoll.INITIALISIERUNG))){
			
			//1=waren;2=Spielernummern;3=Namen
			String[] a_spielerids=a_infos[1].split(KonstantenProtokoll.SEPARATORSUBJEKT);
			a_spielernamen = a_infos[2].split(KonstantenProtokoll.SEPARATORSUBJEKT);
			a_spieler=new Spieler[a_spielerids.length];
			for(int i=0;i<a_spieler.length;i++){
				a_spieler[i]=new Spieler(Integer.parseInt(a_spielerids[i]),a_spielernamen[i],Konstanten.STARTGELD,Konstanten.STARTZENTURIOS);
				a_spieler[i].set_Spielernummer(i);
				a_spieler[i].setAngriff(Konstanten.NOMSG);
			}
			spieleranzahl=a_spielerids.length;
			
			meinSpielbrett.setSpieleranzahl(spieleranzahl);
			
			isSpielgestartet=true;
			meinSpielbrett.waren_setzen(a_infos[0].split(KonstantenProtokoll.SEPARATORSUBJEKT));
			meinSpielbrett.spieler_setzen(a_infos[3].split(KonstantenProtokoll.SEPARATORSUBJEKT));
			this.setChanged();
			this.notifyObservers(Konstanten.SPIELSTART);
		}
		
		//players turn
		else if(a_header[0].equals(Integer.toString(KonstantenProtokoll.EINSPIELER))){
			
			//***
			//what happened
			//data package 1
			//***
			
			a_subjekt = a_infos[1].split(KonstantenProtokoll.SEPARATORSUBJEKT);
			
			switch(Integer.parseInt(a_subjekt[0])){
			
			//end of game
			case KonstantenProtokoll.WP_SPIELENDE:
				a_spielEndpunkte=new int[meinSpielbrett.getSpieleranzahl()];
				for(int i=2;i<a_subjekt.length;i++){
					a_spielEndpunkte[i-2]=Integer.parseInt(a_subjekt[i]);
				}
				break;
				
			//province was selected
			case KonstantenProtokoll.WP_PROVINZAUSGEWAEHLT:
				meinSpielbrett.setVesetzteAusgewaehlteProvinz(Integer.parseInt(a_subjekt[1]));
				
				break;
			
			//actions were chosen
			case KonstantenProtokoll.WP_KARTENGEWAEHLT:
				
				for(int i=0;i<a_spieler.length;i++){
					if(a_spieler[i].get_Spielernummer()==Integer.parseInt(a_subjekt[1])){
						a_spieler[i].set_karten(Integer.parseInt(a_subjekt[2]),Integer.parseInt(a_subjekt[3]));
					}
				}
				break;
			
			//troops were trained
			case KonstantenProtokoll.WP_TRAINIERT:
				
				for(int i=0;i<a_spieler.length;i++){
					if(a_spieler[i].get_Spielernummer()==Integer.parseInt(a_subjekt[1])){
						a_spieler[i].setGeld(Integer.parseInt(a_subjekt[2]));
						a_spieler[i].setTrainingsFaktor(Float.parseFloat(a_subjekt[3]));
					}
				}
				break;
			
			//ambassador was send
			case KonstantenProtokoll.WP_BOTSCHAFTERGESENDET:
				for(int i=0;i<a_spieler.length;i++){
					if(a_spieler[i].get_Spielernummer()==Integer.parseInt(a_subjekt[1])){
						a_spieler[i].setBotschafter(Integer.parseInt(a_subjekt[2]));
					}
				}
				break;
			
			//warlord was chosen
			case KonstantenProtokoll.WP_ZENTURIODAZU:
				for(int i=0;i<a_spieler.length;i++){
					if(a_spieler[i].get_Spielernummer()==Integer.parseInt(a_subjekt[1])){
						a_spieler[i].set_zenturios(Integer.parseInt(a_subjekt[2]));
					}
				}
				break;
			
			//taxes
			case KonstantenProtokoll.WP_GEHALT:
				
				int i=this.getMeinSpielerByID().get_Spielernummer()+1;		
				
				this.getMeinSpielerByID().setGeld(Integer.parseInt(a_subjekt[i]));
				
				s_msg+="\n[+] "+loc.getString("Taxes")+": "+ a_subjekt[i+spieleranzahl]+" "+loc.getString("Denarii") +" ("+loc.getString(a_subjekt[a_subjekt.length-1])+")";
				
				break;
			
			//attack happened
			case KonstantenProtokoll.WP_ANGEGRIFFEN:
				angriffsProvinz=Integer.parseInt(a_subjekt[3]);
				angriffsRegion=Integer.parseInt(a_subjekt[2]);
				s_msg+="\n"+loc.getString("AttackFrom")+" "+a_spielernamen[Integer.parseInt(a_subjekt[1])]+" "+loc.getString("In")+" \""+meinSpielbrett.getNameProvinz(angriffsProvinz)+"\" "+ loc.getString("From")+" \""+meinSpielbrett.getNameRegion(angriffsRegion)+"\"";
				for(int j=0;j<a_spieler.length;j++){
					if(a_spieler[j].get_Spielernummer()==Integer.parseInt(a_subjekt[1])){
						a_spieler[j].setAngriff(a_subjekt[2]+","+a_subjekt[3]);
					}
					else{
						a_spieler[j].setAngriff("");
					}
				}
				break;
			
			//fight is over / result
			case KonstantenProtokoll.WP_KAMPFVORBEI:
				int gewinner =Integer.parseInt(a_subjekt[1]);
				angriffsProvinz=Integer.parseInt(a_subjekt[2]);
				angriffsRegion=Integer.parseInt(a_subjekt[3]);
				for(int j=0;j<a_spieler.length;j++){
					if(a_spieler[j].get_Spielernummer()==Integer.parseInt(a_subjekt[4])){
						a_spieler[j].set_zenturios(Integer.parseInt(a_subjekt[6]));
					}
					if(a_spieler[j].get_Spielernummer()==Integer.parseInt(a_subjekt[8])){
						a_spieler[j].set_zenturios(Integer.parseInt(a_subjekt[10]));
					}
				}
				meinSpielbrett.besetze_ProvinzPS(angriffsProvinz,gewinner);

				meinSpielbrett.set_einheitenAbsolutRSE(angriffsRegion,Integer.parseInt(a_subjekt[4]),Integer.parseInt(a_subjekt[5]));
				meinSpielbrett.set_einheitenAbsolutRSE(angriffsRegion,Integer.parseInt(a_subjekt[8]),Integer.parseInt(a_subjekt[9]));

				if(a_subjekt[11].equals(KonstantenProtokoll.MSG_HATKAPITULIERT)
						||a_subjekt[11].equals(KonstantenProtokoll.MSG_MUSSTEKAPITULIEREN)){
					s_msg+="\n"+a_spielernamen[Integer.parseInt(a_subjekt[8])]+" "+loc.getString(a_subjekt[11]);
				}
				else{
					s_msg+="\n"+a_spielernamen[Integer.parseInt(a_subjekt[4])]+": "+ a_subjekt[11]+" = "+loc.getString("Attack") +" "+a_subjekt[12]+" + "+loc.getString("Fortresses")+" "+a_subjekt[13]+" + "+loc.getString("Strength")+ " "+ a_subjekt[14] + " ("+loc.getString("ForceLevel")+" "+a_subjekt[15]+") + "+loc.getString("Assistance")+" "+a_subjekt[16];
					s_msg+="\n"+a_spielernamen[Integer.parseInt(a_subjekt[8])]+": "+ a_subjekt[17]+" = "+loc.getString("Defence") +" "+a_subjekt[18]+" + "+loc.getString("Fortresses")+" "+a_subjekt[19]+" + "+loc.getString("Strength")+ " "+ a_subjekt[20] + " ("+loc.getString("ForceLevel")+" "+a_subjekt[21]+") + "+loc.getString("Assistance")+" "+a_subjekt[22];
				}
				
				angriffsProvinz=Konstanten.NIXXOS;
				angriffsRegion=Konstanten.NIXXOS;
				this.resetAngriff();
				break;
			
			//player got a new province
			case KonstantenProtokoll.WP_PROVINZERSTEIGERT:

				this.resetEinheitenCache();
				meinSpielbrett.besetze_ProvinzPS(Integer.parseInt(a_subjekt[2]),Integer.parseInt(a_subjekt[1]));				
				break;
			
			//some troops are hired
			case KonstantenProtokoll.WP_EINHEITENGEKAUFT:
				
						
				for(int j=2;j<a_subjekt.length-1;j++){
					meinSpielbrett.set_einheitenAbsolutRSE(j-2,Integer.parseInt(a_subjekt[1]),Integer.parseInt(a_subjekt[j]));
				}
				if(a_subjekt[1].equals(""+this.getMeinSpielerByID().get_Spielernummer())){
					this.getMeinSpielerByID().setGeld(Integer.parseInt(a_subjekt[a_subjekt.length-1]));
				}
				
				break;
			
			//points were chosen from a gift
			case KonstantenProtokoll.WP_BONUSPUNKTE:
				for(int j=0;j<a_spieler.length;j++){
					if(a_spieler[j].get_Spielernummer()==Integer.parseInt(a_subjekt[1])){
						a_spieler[j].setBoni(Integer.parseInt(a_subjekt[2]));
					}
				}
				s_msg+="\n[!] "+a_spielernamen[Integer.parseInt(a_subjekt[1])]+" "+loc.getString("ChosePoints");
				break;
			
			//troops were chosen from a gift
			case KonstantenProtokoll.WP_BONUSEINHEITEN:
				this.resetEinheitenCache();
				for(int j=2;j<a_subjekt.length;j++){
					einheitenCache[j-2]=Integer.parseInt(a_subjekt[j]);
					meinSpielbrett.set_einheitenAbsolutES(einheitenCache,Integer.parseInt(a_subjekt[1]));
				}
				break;
			
			//new fortress was build
			case KonstantenProtokoll.WP_LAGERGEBAUT:
				meinSpielbrett.resetLegionsLager();
				meinSpielbrett.set_LagerAbsolutPSL(Integer.parseInt(a_subjekt[2]),Integer.parseInt(a_subjekt[1]),1);
				for(int j=0;j<a_spieler.length;j++){
					if(a_spieler[j].get_Spielernummer()==Integer.parseInt(a_subjekt[1])){
						a_spieler[j].set_zenturios(Integer.parseInt(a_subjekt[5]));
					}
				}
				if(Integer.parseInt(a_subjekt[1])==this.getMeinSpielerByID().get_Spielernummer()){
					this.getMeinSpielerByID().setGeld(Integer.parseInt(a_subjekt[4]));
				}
				break;
			
			//nothing happened
			case KonstantenProtokoll.WP_NIX:
				meinSpielbrett.resetLegionsLager();
				if(!a_subjekt[1].equals(Konstanten.NOMSG)){
					s_msg+="\n[!] "+a_subjekt[1];
				}
				
				break;
			}
			
			//aktAktion=Konstanten.NIXXOS;
			isSpieleraktiv=false;	
			
			
			//***
			//what happens next
			//data package 0
			//*** 
			
			a_subjekt = a_infos[0].split(KonstantenProtokoll.SEPARATORSUBJEKT);
			
			//ask if it is the players turn himself
			if(a_subjekt[0].equals(""+this.getMeinSpielerByID().get_Spielernummer())){
				isSpieleraktiv=true;
			}
			
			//set first player of round and current player
			//in array of players
			for(int i=0;i<a_spieler.length;i++){
				if(a_spieler[i].get_Spielernummer()==Integer.parseInt(a_subjekt[0])){
					a_spieler[i].setAktuellerSpieler(true);
				}
				else{
					a_spieler[i].setAktuellerSpieler(false);
				}
				if(a_spieler[i].get_Spielernummer()==Integer.parseInt(a_subjekt[2])){
					a_spieler[i].setStartspieler(true);
				}
				else{
					a_spieler[i].setStartspieler(false);
				}
			}
			
			switch(Integer.parseInt(a_subjekt[1])){
			
			//chose a province
			case KonstantenProtokoll.PROVINZWAEHLEN:
				
				this.resetKarten();

				if(a_subjekt[0].equals(""+this.getMeinSpielerByID().get_Spielernummer())){
					aktAktion=KonstantenProtokoll.PROVINZWAEHLEN;
				}
				break;
				
			//chose actions
			case KonstantenProtokoll.KARTENWAEHLEN:
				//this.calcAktionsMoeglichkeiten();
				if(a_subjekt[0].equals(""+this.getMeinSpielerByID().get_Spielernummer())){
					aktAktion=KonstantenProtokoll.KARTENWAEHLEN;
				}
				break;
				
			//end of game
			case KonstantenProtokoll.STOP:
				DlgGewinner dlg=new DlgGewinner(this);
				isSpielgestartet=false;
				break;
			
			//wait / do nothing
			case KonstantenProtokoll.WARTEN:	
				if(a_subjekt[0].equals(""+this.getMeinSpielerByID().get_Spielernummer())){
					nachricht = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+
					KonstantenProtokoll.A_NIX+KonstantenProtokoll.SEPARATORSUBJEKT+"gewartet";
					aktAktion=KonstantenProtokoll.WARTEN;
					this.nachrichtAnServer(nachricht);
				}
				break;
			
			//train troops
			case KonstantenProtokoll.TRAININEREN:
				
				if(a_subjekt[0].equals(""+this.getMeinSpielerByID().get_Spielernummer())){
					aktAktion=KonstantenProtokoll.TRAININEREN;
				}
				break;
			
			//chose gift
			case KonstantenProtokoll.GESCHENKWAEHLEN:
				isEinheitenGeschenk=false;
				if(a_subjekt[0].equals(""+this.getMeinSpielerByID().get_Spielernummer())){
					aktAktion=KonstantenProtokoll.GESCHENKWAEHLEN;
				}
				break;
			
			//build fortress
			case KonstantenProtokoll.LAGERBAUEN:
				
				neuesLager=Konstanten.NIXXOS;
				if(a_subjekt[0].equals(""+this.getMeinSpielerByID().get_Spielernummer())){
					aktAktion=KonstantenProtokoll.LAGERBAUEN;
				}
				break;
			
			//attack
			case KonstantenProtokoll.ANGREIFEN:
				
				if(a_subjekt[0].equals(""+this.getMeinSpielerByID().get_Spielernummer())){
					aktAktion=KonstantenProtokoll.ANGREIFEN;
									
				}
				break;
			
			//defend
			case KonstantenProtokoll.VERTEIDIGEN:
				
				if(a_subjekt[0].equals(""+this.getMeinSpielerByID().get_Spielernummer())){
					aktAktion=KonstantenProtokoll.VERTEIDIGEN;
								
				}
				break;
			
			//hire troops
			case KonstantenProtokoll.EINHEITENKAUFEN:
				
				if(a_subjekt[0].equals(""+this.getMeinSpielerByID().get_Spielernummer())){
					this.resetEinheitenCache();
					aktAktion=KonstantenProtokoll.EINHEITENKAUFEN;
					
				}
				
				break;
			}
			
			if(Konstanten.DEBUG>0){System.out.println("Aktion Client:"+aktAktion);}
			this.setChanged();
			this.notifyObservers(Konstanten.NOMSG);
		}
		
	}

/**
 * sends a message to the server
 * @param ss_nachricht
 */
	public synchronized void nachrichtAnServer(String ss_nachricht){
		if(meinClient!=null){
			try{
				meinClient.sendeNachricht(ss_nachricht);
			}
			catch(Exception e){
				s_msg+="\n[!] "+Localization.getInstance().getString("ErrorOnClientConnect");
				this.setChanged();
				this.notifyObservers(Konstanten.NOMSG);
			}
			
		}
	}
	
	/**
	 * closes network connection
	 */
	public void beendeClient(){
		
		if(meinClient!=null){
			try {
				meinClient.stoppeClient();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			meinClient = null;
		}
		this.setChanged();
		this.notifyObservers(Konstanten.SPIELENDE);
		
	}

	public boolean isSpielgestartet() {
		return isSpielgestartet;
	}

	public Spielbrett getMeinSpielbrett() {
		return meinSpielbrett;
	}

	public Spieler[] getA_spieler() {
		return a_spieler;
	}
	public String[] getA_Spielernamen(){
		return a_spielernamen;
	}

	public boolean isSpieleraktiv() {
		return isSpieleraktiv;
	}

	public int getAktAktion() {
		return aktAktion;
	}
	
	/**
	 * returns all trading goods from one specific player
	 * @param ss_spieler
	 * @return
	 */
	public int[] getA_Waren(int ss_spieler){
		return meinSpielbrett.getA_Waren(ss_spieler);
	}
	
	/**
	 * returns player himself from array of players
	 * @return
	 */
	public Spieler getMeinSpielerByID(){
		Spieler spieler=null;
		for(int i=0;i<a_spieler.length;i++){
			if(a_spieler[i].get_id()==spielerID){
				spieler=a_spieler[i];
			}
		}
		return spieler;
	}

	public int getSpielerID() {
		return spielerID;
	}

	public int[] getEinheitenCache() {
		return einheitenCache;
	}

	public void setEinheitenCache(int[] einheitenCache) {
		this.einheitenCache = einheitenCache;
	}

	/*public void calcAktionsMoeglichkeiten(){
		boolean[] moeglichkeiten=new boolean[Konstanten.K_MAX];
		Spieler spieler=this.getMeinSpielerByID();
		Provinz[] a_provinzen=meinSpielbrett.getA_Provinzen();
		Region[] a_regionen=meinSpielbrett.getA_Regionen();
		int z_provinzen=0;
		int z_provinzenOhneLager=0;
		
		for(int i=0;i<moeglichkeiten.length;i++){
			moeglichkeiten[i]=true;
		}
		
		for(int i=0;i<a_provinzen.length;i++){
			if(a_provinzen[i].getSpieler()==spielerID){
				z_provinzen++;
			}
			if(a_provinzen[i].getSpieler()==spielerID&&a_provinzen[i].getLegionslager()==0){
				z_provinzenOhneLager++;
			}
		}
		
		if(spieler.get_zenturios()<1||spieler.getGeld()<5||z_provinzenOhneLager==0){
			moeglichkeiten[Konstanten.K_LAGERBAUEN]=false;
		}
		if(spieler.getGeld()<2){
			moeglichkeiten[Konstanten.K_EINHEITENKAUFEN]=false;
		}
		
		
		moeglichkeiten[Konstanten.K_ANGREIFEN]=false;
		for(int i=0;i<a_provinzen.length;i++){
			for(int j=0;j<a_regionen.length;j++){
				if(meinSpielbrett.get_einheitenRS(j,spielerID)>0){
					if(meinSpielbrett.getSpielerIDVonProvinz(i)!=spielerID&&
							meinSpielbrett.getSpielerIDVonProvinz(i)!=Konstanten.NIXXOS){
						if(meinSpielbrett.is_regionProvinzGrenze(j+","+i)){		
							moeglichkeiten[Konstanten.K_ANGREIFEN]=true;
						}
					}
				}
			}
		}
		aktionsMoeglichkeiten=moeglichkeiten;
	}*/

	public boolean[] getAktionsMoeglichkeiten() {
		return aktionsMoeglichkeiten;
	}

	

	public String getS_chat() {
		return s_chat;
	}

	public String getS_msg() {
		return s_msg;
	}

	public boolean isEinheitenGeschenk() {
		return isEinheitenGeschenk;
	}

	public void setEinheitenGeschenk(boolean isEinheitenGeschenk) {
		this.isEinheitenGeschenk = isEinheitenGeschenk;
	}
	
	/**
	 * returns information about current attack
	 * format: region+SEPERATOR+province+SEPERATOR+isValid
	 * @return
	 */
	public String getAngriffsInfo(){
		String info="";
		if(angriffsRegion!=Konstanten.NIXXOS){
			info+=meinSpielbrett.getRegionByOid(angriffsRegion).getName();
		}
		else{
			info+="-";
		}
		info+=KonstantenProtokoll.SEPARATORSUBJEKT;
		if(angriffsProvinz!=Konstanten.NIXXOS){
			info+=meinSpielbrett.getProvinzByOid(angriffsProvinz).getName();
		}
		else{
			info+="-";
		}
		if(meinSpielbrett.getSpielernummerVonProvinz(angriffsProvinz)!=this.getMeinSpielerByID().get_Spielernummer()&&meinSpielbrett.getSpielernummerVonProvinz(angriffsProvinz)!=Konstanten.NIXXOS&&meinSpielbrett.getEinheitenRS(angriffsRegion,this.getMeinSpielerByID().get_Spielernummer())>0&&meinSpielbrett.is_regionProvinzGrenze(angriffsRegion,angriffsProvinz)){
			info+=KonstantenProtokoll.SEPARATORSUBJEKT+true;
		}
		else{
			info+=KonstantenProtokoll.SEPARATORSUBJEKT+false;
		}
		
		
		return info;
	}
	
	/**
	 * resets chosen actions
	 */
	public void resetKarten(){
		for(int i=0;i<a_spieler.length;i++){
			a_spieler[i].set_karten(-1,-1);
		}
	}
	
	/**
	 * returns, if the player has the possibility to defend an attack
	 * @param spielernummer
	 * @return
	 */
	public boolean getKannVerteidigen(int spielernummer){
		boolean ja=false;
		Spieler s=a_spieler[spielernummer];
		if(s.get_zenturios()>0){
			if(meinSpielbrett.get_einheitenRS(angriffsRegion,spielernummer)>0){
				ja=true;
			}
		}
		
		return ja;
	}

	public int getAngriffsProvinz() {
		return angriffsProvinz;
	}

	public int getAngriffsRegion() {
		return angriffsRegion;
	}
	
	/**
	 * returns the id of the player who is attacking
	 * @return
	 */
	public int getAngreifer(){
		int spieler=Konstanten.NIXXOS;
		for(int j=0;j<a_spieler.length;j++){
			if(a_spieler[j].getAngriff().equals(angriffsRegion+","+angriffsProvinz)){
				spieler=a_spieler[j].get_Spielernummer();
			}
		}
		return spieler;
	}
	
	/**
	 * returns points of a single player by the id of the player
	 * @param spielernummer
	 * @return
	 */
	public int getPunkte(int spielernummer){
		int punkte=0;
		for(int i=0;i<a_spieler.length;i++){
			if(a_spieler[i].get_Spielernummer()==spielernummer){
				punkte += a_spieler[i].getBoni();
				punkte += meinSpielbrett.getPunkteSpieler(spielernummer);		
			}
			
		}
		
		return punkte;
		
	}

	public int[] getA_spielEndpunkte() {
		return a_spielEndpunkte;
	}
	public int getLegionslager(){
		return meinSpielbrett.get_legionslagerS(this.getMeinSpielerByID().get_Spielernummer());
	}
	public RPGrenze getRPGrenze(int id){
		return meinSpielbrett.getRPGrenzeByOid(id);
	}
	public int getSpieleranzahl() {
		return spieleranzahl;
	}

	public void setSpieleranzahl(int spieleranzahl) {
		this.spieleranzahl = spieleranzahl;
	}
	
	/**
	 * checks if the chosen actions are valid
	 * @param karte String array with actions
	 * @return
	 */
	public boolean checkKartenwahl(String[] karte){
		boolean isOK=true;
		int karte1=Integer.parseInt(karte[0]);
		int karte2=Integer.parseInt(karte[1]);
		
		//has player enough money for a fortress
		if((karte1==KonstantenProtokoll.K_LAGERBAUEN||karte2==KonstantenProtokoll.K_LAGERBAUEN)&&this.getMeinSpielerByID().getGeld()<5){
			isOK=false;
			s_msg+="\n[!] "+Localization.getInstance().getString("YouHaveGotNotEnoughMoneyForBuildingAForress");
		}
		
		//has player enough warlords for a fortress
		if((karte1==KonstantenProtokoll.K_LAGERBAUEN||karte2==KonstantenProtokoll.K_LAGERBAUEN)&&this.getMeinSpielerByID().get_zenturios()<1){
			if((karte1!=KonstantenProtokoll.K_ZENTURIO||karte2==KonstantenProtokoll.K_ZENTURIO)){
				isOK=false;
				s_msg+="\n[!] "+Localization.getInstance().getString("YouHaveGotNotEnoughWarlordsForBuildingAFortress");
			}
		}
		
		//has player enough money to hire troops
		if((karte1==KonstantenProtokoll.K_EINHEITENKAUFEN||karte2==KonstantenProtokoll.K_EINHEITENKAUFEN)&&this.getMeinSpielerByID().getGeld()<2){
			isOK=false;
			s_msg+="\n[!] "+Localization.getInstance().getString("YouHaveGotNotEnoughMoneyForNewTroops");
		}
		
		//has player enough money to train troops
		if((karte1==KonstantenProtokoll.K_TRAINIEREN||karte2==KonstantenProtokoll.K_TRAINIEREN)&&this.getMeinSpielerByID().getGeld()<1){
			isOK=false;
			s_msg+="\n[!] "+Localization.getInstance().getString("YouHaveGotNotEnoughMoneyToTrainYourTroops");
		}
		
		//has player enough warlords for an attack
		if((karte1==KonstantenProtokoll.K_ANGREIFEN||karte2==KonstantenProtokoll.K_ANGREIFEN)&&this.getMeinSpielerByID().get_zenturios()<1){
			if((karte1!=KonstantenProtokoll.K_ZENTURIO||karte2==KonstantenProtokoll.K_ZENTURIO)){
				isOK=false;
				s_msg+="\n[!] "+Localization.getInstance().getString("YouHaveGotNotEnoughWarlordsForAnAttack");
			}
		}
		return isOK;
	}

/**
 * returns count of independent provinces
 * @return
 */
public int getZFreieProvinzen(){
	return Konstanten.PROVINZENANZAHL-meinSpielbrett.getZBesetzteProvinzen();
}

public void clientBeendet() {
	this.setChanged();
	this.notifyObservers(Konstanten.SPIELENDE);
	
}
}


