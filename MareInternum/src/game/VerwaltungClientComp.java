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
import entities.KonstantenProtokoll;
import entities.Provinz;

import java.util.Vector;

import network.Client;
import network.Netzwerkable;

import org.apache.mina.transport.socket.nio.SocketConnector;

public class VerwaltungClientComp implements Netzwerkable{

	private SocketConnector connector;
	private String[] a_spielernamen;
	private Spieler[] a_spieler;
	private Client meinClient;
	private int aktAktion;
	private int[] einheitenCache;
	private final int T_SLEEP=1000;
	private boolean[] aktionsMoeglichkeiten;
	private String[] namen={"Alf","Buxtehude","Clown","Domingo","Erl","Rudi","Hermann","Gerald","Edwald","Bischof","Luise","Leguan","Knallhard","Eberhard","Schoenfeld"};
	private String[] sprueche={"Alea jacta est","Erare humanum est","Veni vidi vici"};
	private int angriffsProvinz;
	private int angriffsRegion;
	
	private boolean isEinheitenGeschenk;
	private Spielbrett meinSpielbrett;
	private int spielerID;
	private int spielerNummer;
	private int spieleranzahl;
	
	public VerwaltungClientComp(){
		connector = new SocketConnector();
		spielerID = 99;
		aktAktion=KonstantenProtokoll.WARTEN;
		meinSpielbrett = new Spielbrett();
		einheitenCache= new int[Konstanten.REGIONENANZAHL];

		isEinheitenGeschenk=false;
		angriffsRegion=1;
		angriffsProvinz=1;
		
	}
	
	/**
	 * connects computer client to server
	 * returns true if client was connected successfully
	 * @param ip
	 * @param port
	 * @return
	 */
	public boolean nehmeAnSpielTeil(String ip,int port){
		meinClient = new Client(this, connector);
		if(!meinClient.connect(ip, port)){
			return false;
		}
		this.nachrichtAnServer(KonstantenProtokoll.ANMELDUNG+KonstantenProtokoll.SEPARATORHEADER+namen[zufallszahl(namen.length,0)]);
		return true;
	}
	
	public void resetEinheitenCache(){
		for(int i=0;i<einheitenCache.length;i++){
			einheitenCache[i]=0;
		}
	}	
	
	public synchronized void nachrichtVonServer(String ss_nachricht){
			
		int t_sleep=0;
		String nachricht="";
		String[] a_header = ss_nachricht.split(KonstantenProtokoll.SEPARATORHEADER);
		String[] a_infos = a_header[1].split(KonstantenProtokoll.SEPARATOR1);
		String[] a_subjekt;
		String meinName="";
		if(Konstanten.DEBUG>0){System.out.println("CompCleintverwaltung empfängt: "+ss_nachricht);}
		if(spielerID==99&&a_header[0].equals(""+KonstantenProtokoll.ID)){
			spielerID=Integer.parseInt(a_header[1]);
		}			
		else if(a_header[0].equals(Integer.toString(KonstantenProtokoll.CHAT))){
			/*int z=this.zufallszahl(100,1);
			if(z>z_geantwortet){
				z_geantwortet=98;
				z=this.zufallszahl(sprueche.length,0);
				this.nachrichtAnServer(Konstanten.CHAT+Konstanten.SEPERATORHEADER+sprueche[z]);
			}
			if(z<z_geantwortet){
				z_geantwortet--;
			}*/
			
		}
		else if(a_header[0].equals(Integer.toString(KonstantenProtokoll.INITIALISIERUNG))){
			//1=waren;2=Spielernummern;3=Namen
			
			String[] a_spielerids=a_infos[1].split(KonstantenProtokoll.SEPARATORSUBJEKT);
			a_spielernamen = a_infos[2].split(KonstantenProtokoll.SEPARATORSUBJEKT);
			a_spieler=new Spieler[a_spielerids.length];
			for(int i=0;i<a_spieler.length;i++){
				a_spieler[i]=new Spieler(Integer.parseInt(a_spielerids[i]),a_spielernamen[i],Konstanten.STARTGELD,Konstanten.STARTZENTURIOS);
				a_spieler[i].set_Spielernummer(i);
				if(Integer.parseInt(a_spielerids[i])==spielerID){
					spielerNummer = i;
				}
			}

			spieleranzahl=a_spielerids.length;
			
			meinSpielbrett.setSpieleranzahl(spieleranzahl);

			meinSpielbrett.waren_setzen(a_infos[0].split(KonstantenProtokoll.SEPARATORSUBJEKT));
			meinSpielbrett.spieler_setzen(a_infos[3].split(KonstantenProtokoll.SEPARATORSUBJEKT));
		}
		else if(a_header[0].equals(Integer.toString(KonstantenProtokoll.EINSPIELER))){
			
//			was ist passierte...
			a_subjekt = a_infos[1].split(KonstantenProtokoll.SEPARATORSUBJEKT);
			switch(Integer.parseInt(a_subjekt[0])){
			case KonstantenProtokoll.WP_PROVINZAUSGEWAEHLT:
				meinSpielbrett.setVesetzteAusgewaehlteProvinz(Integer.parseInt(a_subjekt[1]));
				this.resetKarten();
				break;
			case KonstantenProtokoll.WP_KARTENGEWAEHLT:
				
				for(int i=0;i<a_spieler.length;i++){
					if(a_spieler[i].get_Spielernummer()==Integer.parseInt(a_subjekt[1])){
						a_spieler[i].set_karten(Integer.parseInt(a_subjekt[2]),Integer.parseInt(a_subjekt[3]));
					}
				}
				break;
			case KonstantenProtokoll.WP_BOTSCHAFTERGESENDET:
				for(int i=0;i<a_spieler.length;i++){
					if(a_spieler[i].get_Spielernummer()==Integer.parseInt(a_subjekt[1])){
						a_spieler[i].setBotschafter(Integer.parseInt(a_subjekt[2]));
					}
				}
				break;
			case KonstantenProtokoll.WP_ZENTURIODAZU:
				for(int i=0;i<a_spieler.length;i++){
					if(a_spieler[i].get_Spielernummer()==Integer.parseInt(a_subjekt[1])){
						a_spieler[i].set_zenturios(Integer.parseInt(a_subjekt[2]));
					}
				}
				break;
			case KonstantenProtokoll.WP_GEHALT:
				int i=this.getMeinSpielerByID(spielerID).get_Spielernummer()+1;		
				this.getMeinSpielerByID(spielerID).setGeld(Integer.parseInt(a_subjekt[i]));
				break;

			
			case KonstantenProtokoll.WP_TRAINIERT:
				
				for(int j=0;j<a_spieler.length;j++){
					if(a_spieler[j].get_Spielernummer()==Integer.parseInt(a_subjekt[1])){
						a_spieler[j].setGeld(Integer.parseInt(a_subjekt[2]));
						a_spieler[j].setTrainingsFaktor(Float.parseFloat(a_subjekt[3]));
					}
				}
				break;
			case KonstantenProtokoll.WP_ANGEGRIFFEN:
				angriffsProvinz=Integer.parseInt(a_subjekt[3]);
				angriffsRegion=Integer.parseInt(a_subjekt[2]);
				for(int j=0;j<a_spieler.length;j++){
					if(a_spieler[j].get_Spielernummer()==Integer.parseInt(a_subjekt[1])){
						a_spieler[j].setAngriff(a_subjekt[2]+","+a_subjekt[3]);
					}
					else{
						a_spieler[j].setAngriff("");
					}
				}
				break;
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
				angriffsProvinz=Konstanten.NIXXOS;
				angriffsRegion=Konstanten.NIXXOS;
				break;
			case KonstantenProtokoll.WP_PROVINZERSTEIGERT:
				
				/*for(int k=5;k<10;k++){
					einheitenCache[k-5]=Integer.parseInt(a_subjekt[k]);
					meinSpielbrett.set_einheitenAbsolutES(einheitenCache,meinMapping.getIdBySpielernummer(Integer.parseInt(a_subjekt[1])));
				}*/
				this.resetEinheitenCache();
				meinSpielbrett.besetze_ProvinzPS(Integer.parseInt(a_subjekt[2]),Integer.parseInt(a_subjekt[1]));				
				break;
			case KonstantenProtokoll.WP_EINHEITENGEKAUFT:
				this.resetEinheitenCache();
				for(int j=2;j<a_subjekt.length-1;j++){
					einheitenCache[j-2]=Integer.parseInt(a_subjekt[j]);
					meinSpielbrett.set_einheitenAbsolutES(einheitenCache,Integer.parseInt(a_subjekt[1]));
				}
				if(a_subjekt[1].equals(""+this.getMeinSpielerByID(spielerID).get_Spielernummer())){
					this.getMeinSpielerByID(spielerID).setGeld(Integer.parseInt(a_subjekt[a_subjekt.length-1]));
				}
				this.resetEinheitenCache();
				break;
			case KonstantenProtokoll.WP_BONUSPUNKTE:
				for(int j=0;j<a_spieler.length;j++){
					if(a_spieler[j].get_Spielernummer()==Integer.parseInt(a_subjekt[1])){
						a_spieler[j].setBoni(Integer.parseInt(a_subjekt[2]));
					}
				}
				break;
			case KonstantenProtokoll.WP_BONUSEINHEITEN:
				this.resetEinheitenCache();
				for(int j=2;j<a_subjekt.length;j++){
					einheitenCache[j-2]=Integer.parseInt(a_subjekt[j]);
					meinSpielbrett.set_einheitenAbsolutES(einheitenCache,Integer.parseInt(a_subjekt[1]));
				}
				break;
			case KonstantenProtokoll.WP_LAGERGEBAUT:
				meinSpielbrett.set_LagerAbsolutPSL(Integer.parseInt(a_subjekt[2]),Integer.parseInt(a_subjekt[1]),1);
				for(int j=0;j<a_spieler.length;j++){
					if(a_spieler[j].get_Spielernummer()==Integer.parseInt(a_subjekt[1])){
						a_spieler[j].set_zenturios(Integer.parseInt(a_subjekt[5]));
					}
				}
				if(Integer.parseInt(a_subjekt[1])==this.getMeinSpielerByID(spielerID).get_Spielernummer()){
					this.getMeinSpielerByID(spielerID).setGeld(Integer.parseInt(a_subjekt[4]));
				}
			}
			
			
			//was ist dran
			a_subjekt = a_infos[0].split(KonstantenProtokoll.SEPARATORSUBJEKT);
			
			for(int i=0;i<a_spieler.length;i++){
				if(a_spieler[i].get_Spielernummer()==Integer.parseInt(a_subjekt[0])){
					a_spieler[i].setAktuellerSpieler(true);
				}
				else{
					a_spieler[i].setAktuellerSpieler(false);
				}
				if(i==Integer.parseInt(a_subjekt[2])){
					a_spieler[i].setStartspieler(true);
				}
				else{
					a_spieler[i].setStartspieler(false);
				}
			}
			
			switch(Integer.parseInt(a_subjekt[1])){
			case KonstantenProtokoll.PROVINZWAEHLEN:
				
				if(a_subjekt[0].equals(""+this.getMeinSpielerByID(spielerID).get_Spielernummer())){
					this.aktionProvinzWaehlen();
					t_sleep=T_SLEEP;
				}
				break;
			case KonstantenProtokoll.KARTENWAEHLEN:
				
				if(a_subjekt[0].equals(""+this.getMeinSpielerByID(spielerID).get_Spielernummer())){
					this.aktionKartenWaehlen();
					t_sleep=T_SLEEP;
				}
				break;
			case KonstantenProtokoll.WARTEN:	
				if(a_subjekt[0].equals(""+this.getMeinSpielerByID(spielerID).get_Spielernummer())){
					nachricht = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+
					KonstantenProtokoll.A_NIX+KonstantenProtokoll.SEPARATORSUBJEKT+"gewartet";
					this.nachrichtAnServer(nachricht);
				}
				break;

			case KonstantenProtokoll.GESCHENKWAEHLEN:
				isEinheitenGeschenk=false;
				if(a_subjekt[0].equals(""+this.getMeinSpielerByID(spielerID).get_Spielernummer())){
					this.aktionGeschenkWaehlen();
					t_sleep=T_SLEEP;
				}
				break;
			case KonstantenProtokoll.LAGERBAUEN:
				
				if(a_subjekt[0].equals(""+this.getMeinSpielerByID(spielerID).get_Spielernummer())){
					this.aktionLagerBauenDef();
					t_sleep=T_SLEEP;
				}
				break;
				case KonstantenProtokoll.TRAININEREN:
				
				if(a_subjekt[0].equals(""+this.getMeinSpielerByID(spielerID).get_Spielernummer())){
					this.aktionTrainieren();
					t_sleep=T_SLEEP;
				}
				break;
			case KonstantenProtokoll.ANGREIFEN:
				
				if(a_subjekt[0].equals(""+this.getMeinSpielerByID(spielerID).get_Spielernummer())){
					this.aktionAngreifen();
					t_sleep=T_SLEEP;
									
				}
				break;
			case KonstantenProtokoll.VERTEIDIGEN:
				
				if(a_subjekt[0].equals(""+this.getMeinSpielerByID(spielerID).get_Spielernummer())){
					aktAktion=KonstantenProtokoll.VERTEIDIGEN;
					if(this.calcKannVerteidigen(this.getMeinSpielerByID(spielerID).get_Spielernummer())){
						nachricht=KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER;
						nachricht+=KonstantenProtokoll.A_VERTEIDIGT;
					}
					else{
						nachricht=KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER;
						nachricht+=KonstantenProtokoll.A_KAPITULIERT;
					}
					t_sleep=T_SLEEP;
					this.nachrichtAnServer(nachricht);
					
				}
				break;
			case KonstantenProtokoll.EINHEITENKAUFEN:
				this.resetEinheitenCache();
				if(a_subjekt[0].equals(""+this.getMeinSpielerByID(spielerID).get_Spielernummer())){
					this.aktionEinheitenKaufen();	
					t_sleep=T_SLEEP;
				}
				
				break;
			}

		}
		try {
			Thread.sleep(t_sleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * sends a message to the server
	 * 
	 * @param ss_nachricht
	 */
	public synchronized void nachrichtAnServer(String nachricht){
		if(meinClient!=null){
			try {
				meinClient.sendeNachricht(nachricht);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void stoppeClient() throws Exception{
		
		if(meinClient!=null){
			meinClient.stoppeClient();
			meinClient = null;
		}
	}
	
	
	public Spieler getMeinSpielerByID(int spielerID){
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

	
	

	public void calcAktionsMoeglichkeiten(){
		boolean[] moeglichkeiten=new boolean[KonstantenProtokoll.K_MAX];
		Spieler spieler=this.getMeinSpielerByID(spielerID);
		Vector v_provinzen=meinSpielbrett.getV_Provinzen();
		int z_provinzen=0;
		int z_provinzenOhneLager=0;
		
		for(int i=0;i<moeglichkeiten.length;i++){
			moeglichkeiten[i]=true;
		}
		
		for(int i=0;i<v_provinzen.size();i++){
			
			if(((Provinz)v_provinzen.elementAt(i)).getSpielernummer()==spielerNummer&&((Provinz)v_provinzen.elementAt(i)).getZ_lager()==0){
				z_provinzenOhneLager++;
			}
		}
		
		if(spieler.get_zenturios()<1||spieler.getGeld()<5||z_provinzenOhneLager==0){
			moeglichkeiten[KonstantenProtokoll.K_LAGERBAUEN]=false;
		}
		if(spieler.getGeld()<2){
			moeglichkeiten[KonstantenProtokoll.K_EINHEITENKAUFEN]=false;
		}
		
		moeglichkeiten[KonstantenProtokoll.K_TRAINIEREN]=false;
		
		for(int i=0;i<Konstanten.PROVINZENANZAHL;i++){
			if(meinSpielbrett.getSpielernummerVonProvinz(i)==spielerNummer&&meinSpielbrett.get_legionslagerP(i)==0){
				moeglichkeiten[KonstantenProtokoll.K_TRAINIEREN]=true;
			}
		}
		
		if(meinSpielbrett.get_legionslagerS(spielerNummer)<1||spieler.getGeld()<1){
			moeglichkeiten[KonstantenProtokoll.K_TRAINIEREN]=false;
			
		}
		
		
		moeglichkeiten[KonstantenProtokoll.K_ANGREIFEN]=false;
		for(int i=0;i<Konstanten.PROVINZENANZAHL;i++){
			for(int j=0;j<Konstanten.REGIONENANZAHL;j++){
				if(meinSpielbrett.get_einheitenRS(j,spielerNummer)>0){
					if(meinSpielbrett.getSpielernummerVonProvinz(i)!=spielerNummer&&
							meinSpielbrett.getSpielernummerVonProvinz(i)!=Konstanten.NIXXOS){
						if(meinSpielbrett.is_regionProvinzGrenze(j,i)){		
							moeglichkeiten[KonstantenProtokoll.K_ANGREIFEN]=true;
						}
					}
				}
			}
		}
		aktionsMoeglichkeiten=moeglichkeiten;
	}

	

	public void setEinheitenGeschenk(boolean isEinheitenGeschenk) {
		this.isEinheitenGeschenk = isEinheitenGeschenk;
	}
	public String getAngriffsInfo(){
		String info="x,x,x";
		info=angriffsRegion+KonstantenProtokoll.SEPARATORSUBJEKT+angriffsProvinz+KonstantenProtokoll.SEPARATORSUBJEKT+meinSpielbrett.is_regionProvinzGrenze(angriffsRegion,angriffsProvinz);
		return info;
	}
	public void resetKarten(){
		for(int i=0;i<a_spieler.length;i++){
			a_spieler[i].set_karten(Konstanten.NIXXOS,Konstanten.NIXXOS);
		}
	}
	public void aktionKartenWaehlen(){
		int[] karten=new int[2];
		int[] prioritaeten=new int[6];
		int maxPrioritaet=0;

		int[] a_z_karten;
		String nachricht="";
		Spieler spieler= this.getMeinSpielerByID(spielerID);
		int geld=spieler.getGeld();
		int[] einheitenDefizite;
		this.calcAktionsMoeglichkeiten();
		
		for(int i=0;i<prioritaeten.length;i++){
			prioritaeten[i]=-1;
		}
		
		
			
		a_z_karten=new int[KonstantenProtokoll.K_MAX];
		
		
		
				
		for(int i=0;i<a_spieler.length;i++){
			if(a_spieler[i].getA_Karten()[0]!=Konstanten.NIXXOS){
				a_z_karten[a_spieler[i].getA_Karten()[0]]++;
			}
			if(a_spieler[i].getA_Karten()[1]!=Konstanten.NIXXOS){
				a_z_karten[a_spieler[i].getA_Karten()[1]]++;
			}
		}
		
		prioritaeten[KonstantenProtokoll.K_LAGERBAUEN]=10;
		prioritaeten[KonstantenProtokoll.K_EINHEITENKAUFEN]=10;
		prioritaeten[KonstantenProtokoll.K_ANGREIFEN]=10;
		prioritaeten[KonstantenProtokoll.K_TRAINIEREN]=10;
		
		if(geld<15){
			prioritaeten[KonstantenProtokoll.K_LAGERBAUEN]=10;
			prioritaeten[KonstantenProtokoll.K_EINHEITENKAUFEN]=this.zufallszahl(10,1);
			prioritaeten[KonstantenProtokoll.K_ANGREIFEN]=5;
			prioritaeten[KonstantenProtokoll.K_TRAINIEREN]=10;
		}
		if(geld<10){
			prioritaeten[KonstantenProtokoll.K_LAGERBAUEN]=this.zufallszahl(10,1);;
			prioritaeten[KonstantenProtokoll.K_EINHEITENKAUFEN]=this.zufallszahl(10,1);
			prioritaeten[KonstantenProtokoll.K_ANGREIFEN]=this.zufallszahl(8,1);
			prioritaeten[KonstantenProtokoll.K_TRAINIEREN]=10;
		}
		if(geld<7){
			prioritaeten[KonstantenProtokoll.K_ZENTURIO]=5;
			prioritaeten[KonstantenProtokoll.K_BOTSCHAFTER]=this.zufallszahl(20,7);
			prioritaeten[KonstantenProtokoll.K_LAGERBAUEN]=this.zufallszahl(10,1);
			prioritaeten[KonstantenProtokoll.K_EINHEITENKAUFEN]=0;
			prioritaeten[KonstantenProtokoll.K_ANGREIFEN]=this.zufallszahl(10,1);
			prioritaeten[KonstantenProtokoll.K_TRAINIEREN]=10;
		}
		if(geld<4){
			prioritaeten[KonstantenProtokoll.K_ZENTURIO]=10;
			prioritaeten[KonstantenProtokoll.K_BOTSCHAFTER]=14;
			prioritaeten[KonstantenProtokoll.K_LAGERBAUEN]=0;
			prioritaeten[KonstantenProtokoll.K_EINHEITENKAUFEN]=0;
			prioritaeten[KonstantenProtokoll.K_ANGREIFEN]=10;
			prioritaeten[KonstantenProtokoll.K_TRAINIEREN]=0;
		}
		einheitenDefizite=this.calcEinheitenDefizite();
		for(int i=0;i<einheitenDefizite.length;i++){
			if(einheitenDefizite[i]>4){
				prioritaeten[KonstantenProtokoll.K_EINHEITENKAUFEN]+=5;
			}
			if(einheitenDefizite[i]<=0&&meinSpielbrett.getEinheitenRS(i,spielerNummer)>3){
				prioritaeten[KonstantenProtokoll.K_ANGREIFEN]+=10;
			}
		}
		
		if(this.getMeinSpielerByID(spielerID).get_zenturios()<10){
			prioritaeten[KonstantenProtokoll.K_ZENTURIO]+=2;
			if(this.getMeinSpielerByID(spielerID).get_zenturios()<spieleranzahl){
				prioritaeten[KonstantenProtokoll.K_ZENTURIO]+=5;
				if(this.getMeinSpielerByID(spielerID).get_zenturios()<2){
					prioritaeten[KonstantenProtokoll.K_ZENTURIO]+=10;
				}
			}
		}
		
		
		
		if(this.kaufeEinheiten(geld,false,true)<geld){
			prioritaeten[KonstantenProtokoll.K_EINHEITENKAUFEN]+=2;
			geld=this.getMeinSpielerByID(spielerID).getGeld();
			if(this.kaufeEinheiten(geld,false,false)<geld){
				prioritaeten[KonstantenProtokoll.K_EINHEITENKAUFEN]+=3;
			}
		}
		for(int i=0;i<aktionsMoeglichkeiten.length;i++){
			if(!aktionsMoeglichkeiten[i]){
				prioritaeten[i]=-1;
			}
		}
		
		for(int i=0;i<karten.length;i++){
			maxPrioritaet=-1;
			for(int j=0;j<prioritaeten.length;j++){
				if(maxPrioritaet<prioritaeten[j]){
					maxPrioritaet=prioritaeten[j];
					prioritaeten[j]=-1;
					karten[i]=j;
				}
			}
		}
		
		nachricht = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+
		KonstantenProtokoll.A_KARTENGEWAEHLT+KonstantenProtokoll.SEPARATORSUBJEKT+
		karten[0]+KonstantenProtokoll.SEPARATORSUBJEKT+karten[1];
		this.nachrichtAnServer(nachricht);
		
		
		
	}
	public int[] calcAngriffsProvinzen(){
		int[] angriffsProvinzen=new int[Konstanten.PROVINZENANZAHL];
		int einheiten=0;
		int[] a_waren;
		for(int i=0;i<angriffsProvinzen.length;i++){
			angriffsProvinzen[i]=0;
		}
		
		for(int i=0;i<Konstanten.PROVINZENANZAHL;i++){
			if(meinSpielbrett.getSpielernummerVonProvinz(i)==spielerNummer){
				for(int j=0;j<Konstanten.PROVINZENANZAHL;j++){		
					if(meinSpielbrett.getSpielernummerVonProvinz(j)!=Konstanten.NIXXOS){
						if(meinSpielbrett.is_provinzenGrenze(i,j)){
							angriffsProvinzen[j]+=2;
						}
						if(this.calcPerfWareNeg(spielerNummer)[meinSpielbrett.get_provinzWare(j)]){
							angriffsProvinzen[j]+=5;
						}
						if(meinSpielbrett.get_angrenzendeLagerPS(j,spielerNummer)>0){
							angriffsProvinzen[j]+=meinSpielbrett.get_angrenzendeLagerPS(j,spielerNummer)*2;
						}
						if(meinSpielbrett.get_angrenzendeLagerPS(j,meinSpielbrett.getSpielernummerVonProvinz(j))>0){
							angriffsProvinzen[j]-=meinSpielbrett.get_angrenzendeLagerPS(j,spielerNummer)*2;
						}
						for(int k=0;k<Konstanten.REGIONENANZAHL;k++){
							if(meinSpielbrett.is_regionProvinzGrenze(k,j)){
								angriffsProvinzen[j]+=meinSpielbrett.getEinheitenRS(k,spielerNummer);
								angriffsProvinzen[j]-=meinSpielbrett.getEinheitenRS(k,meinSpielbrett.getSpielernummerVonProvinz(j));
								
							}
						}
					}
				}
			}
		}
		return angriffsProvinzen;
	}
	public boolean[] calcPerfWareNeg(int spielernummer){
		int[] prefWaren=new int[Konstanten.MAX_SPIELER];
		int[] a_waren=new int[Konstanten.MAX_WAREN];
		boolean[] warenPrefs=new boolean[Konstanten.MAX_WAREN];
		for(int i=0;i<warenPrefs.length;i++){
			warenPrefs[i]=false;
		}
		a_waren=meinSpielbrett.getA_Waren(spielernummer);
		for(int i=0;i<a_waren.length;i++){
			if(a_waren[i]<4){
				warenPrefs[i]=true;
			}
		}
		return warenPrefs;
	}
	public boolean[] calcPerfWarePos(int spielernummer){
		
		int[] prefWaren=new int[Konstanten.MAX_SPIELER];
		int[] a_waren=new int[Konstanten.MAX_WAREN];
		boolean[] warenPrefs=new boolean[Konstanten.MAX_WAREN];
		for(int i=0;i<warenPrefs.length;i++){
			warenPrefs[i]=false;
		}
		
		a_waren=meinSpielbrett.getA_Waren(spielernummer);
		for(int i=0;i<a_waren.length;i++){
			if(a_waren[i]>=4){
				warenPrefs[i]=true;
			}
		}
		return warenPrefs;
	}
	public boolean[] calcPrefProvinz(int spielernummer){
		boolean[] provinzPrefs=new boolean[Konstanten.PROVINZENANZAHL];
		for(int i=0;i<provinzPrefs.length;i++){
			provinzPrefs[i]=false;
		}
		
		for(int i=0;i<Konstanten.PROVINZENANZAHL;i++){
			if(meinSpielbrett.getSpielernummerVonProvinz(i)!=spielernummer){
				for(int j=0;j<Konstanten.PROVINZENANZAHL;j++){		
					if(meinSpielbrett.getSpielernummerVonProvinz(j)==spielernummer&&meinSpielbrett.is_provinzenGrenze(i,j)){
						provinzPrefs[i]=true;
					}
				}
			}
		}
		return provinzPrefs;
	}
	public int[] calcEinflussProvinz(int spielernummer){
		int[] provinzEinfluss=new int[Konstanten.PROVINZENANZAHL];
		for(int i=0;i<provinzEinfluss.length;i++){
			provinzEinfluss[i]=0;
		}
	
		for(int i=0;i<Konstanten.PROVINZENANZAHL;i++){
				for(int j=0;j<Konstanten.PROVINZENANZAHL;j++){		
					if(meinSpielbrett.getSpielernummerVonProvinz(j)==spielernummer&&meinSpielbrett.is_provinzenGrenze(i,j)){
						provinzEinfluss[i]++;
					}
				}
		}
		return provinzEinfluss;
	}
	public void aktionProvinzWaehlen(){
		int provinz=Konstanten.NIXXOS;
		String nachricht="";
		boolean notWahl=false;
		boolean[] provinzPrefs=this.calcPrefProvinz(spielerNummer);
		boolean[] warenPrefs=this.calcPerfWareNeg(spielerNummer);
		for(int i=0;i<Konstanten.PROVINZENANZAHL;i++){
			if(meinSpielbrett.getSpielernummerVonProvinz(i)==Konstanten.NIXXOS&&i!=Konstanten.ROM){
				if(provinz==Konstanten.NIXXOS&&!notWahl){
					provinz=i;
					notWahl=true;
				}
				if(provinzPrefs[i]&&warenPrefs[meinSpielbrett.getProvinzWare(i)]){
					provinz=i;
					notWahl=false;
				}
				else if(warenPrefs[meinSpielbrett.getProvinzWare(i)]&&notWahl){
					provinz=i;
				}
				else if(provinzPrefs[i]&&notWahl){
					provinz=i;
					notWahl=false;
				}
			}
			else if(meinSpielbrett.getSpielernummerVonProvinz(i)==Konstanten.NIXXOS&&i==Konstanten.ROM){
				provinz=i;
				notWahl=true;
			}
		}
		if (provinz!=Konstanten.NIXXOS){
			nachricht=KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+
			KonstantenProtokoll.A_PROVINZGEWAEHLT+KonstantenProtokoll.SEPARATORSUBJEKT+provinz;
			aktAktion=KonstantenProtokoll.WARTEN;
			this.nachrichtAnServer(nachricht);
			
		}
		
	}
	
	public void aktionLagerBauenDef(){
		int[] a_einfluss=this.calcProvinzenWerte(spielerNummer);
		int maxEinfluss=-1;
		int provinz=Konstanten.NIXXOS;
		String nachricht="";
		for(int i=0;i<a_einfluss.length;i++){
			if(meinSpielbrett.get_legionslagerP(i)==0&&a_einfluss[i]>maxEinfluss&&meinSpielbrett.getSpielernummerVonProvinz(i)==spielerNummer){
				maxEinfluss=a_einfluss[i];
				provinz=i;
			}
		}
		if(provinz==Konstanten.NIXXOS){
			for(int i=0;i<Konstanten.PROVINZENANZAHL;i++){
				if(meinSpielbrett.get_legionslagerP(i)==0&&meinSpielbrett.getSpielernummerVonProvinz(i)==spielerNummer){
					provinz=i;
				}
			}
		}
		
		nachricht=KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+
		KonstantenProtokoll.A_LAGERGEBAUT+KonstantenProtokoll.SEPARATORSUBJEKT+provinz;
		this.nachrichtAnServer(nachricht);
		
	}
	public int[] calcProvinzenWerte(int spielernummer){
		int[] a_werte =new int[Konstanten.PROVINZENANZAHL];
		for(int i=0;i<Konstanten.PROVINZENANZAHL;i++){
			a_werte[i]=0;
			if(meinSpielbrett.getSpielernummerVonProvinz(i)==spielernummer){
				a_werte[i]++;
			}
			for(int j=0;j<Konstanten.PROVINZENANZAHL;j++){
				
				if(meinSpielbrett.is_provinzenGrenze(j,i)){
					if(meinSpielbrett.getSpielernummerVonProvinz(j)==spielernummer){
						a_werte[i]++;
					}
				}
			}
		}
		return a_werte;
	}
	public void aktionEinheitenKaufen(){
		
		this.resetEinheitenCache();
		String nachricht="";
		int[] a_karten=this.getMeinSpielerByID(spielerID).getA_Karten();
		
		int geld=this.getMeinSpielerByID(spielerID).getGeld();
		
		for(int i=0;i<a_karten.length;i++){
			if(a_karten[i]==KonstantenProtokoll.K_LAGERBAUEN){
				geld-=5;
			}
			if(a_karten[i]==KonstantenProtokoll.K_TRAINIEREN){
				geld-=1;
			}
		}
		//geld=this.kaufeEinheiten(geld,false,false);
		geld=this.kaufeEinheiten(geld,false,true);
		//geld=this.kaufeEinheiten(geld,true,false);
		
		nachricht = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+
		KonstantenProtokoll.A_EINHEITENGEKAUFT+KonstantenProtokoll.SEPARATORSUBJEKT;
		for(int i=0;i<einheitenCache.length;i++){
			nachricht=nachricht+einheitenCache[i]+KonstantenProtokoll.SEPARATORSUBJEKT;
		}
		this.nachrichtAnServer(nachricht);
		
	}
	public int kaufeEinheiten(int geld,boolean ss_prevProvinzen,boolean ss_prevWaren){
		int[] defizite=this.calcEinheitenDefizite();
		boolean[] prefsProvinzen =this.calcPrefProvinz(spielerNummer);
		boolean[] prefsWaren=this.calcPerfWareNeg(spielerNummer);
		boolean[] isBearbeitet=new boolean[Konstanten.REGIONENANZAHL];
		
		for(int i=0;i<Konstanten.PROVINZENANZAHL;i++){
			isBearbeitet=this.setBooleanArray(isBearbeitet,false);
			if((prefsProvinzen[i]||ss_prevProvinzen)&&(i!=Konstanten.ROM&&prefsWaren[meinSpielbrett.get_provinzWare(i)]||ss_prevWaren)){
				while(this.getIsFalse(isBearbeitet)){
					int j=(int)(Math.random()*100)%Konstanten.REGIONENANZAHL;
					if(!isBearbeitet[j]){
						isBearbeitet[j]=true;
						if(meinSpielbrett.is_regionProvinzGrenze(j,i)){
							//if((defizite[j]>0)||meinSpielbrett.get_einheitenRS(j,spielerID)<3){
								if(geld>meinSpielbrett.getKostenRegion(j)){
									einheitenCache[j]+=geld/meinSpielbrett.getKostenRegion(j);
									geld-=meinSpielbrett.getKostenRegion(j)*einheitenCache[j];
								}
							//}
						}
					}
					
				}
			}
		}
		
		return geld;
	}
	public boolean getIsFalse(boolean[] array){
		boolean isFalse=false;
		for(int i=0;i<array.length;i++){
			if(!array[i]){
				isFalse=true;
			}
		}
		return isFalse;
	}
	public boolean[] setBooleanArray(boolean[] array,boolean value){
		for(int i=0;i<array.length;i++){
			array[i]=value;
		}
		return array;
	}
	public boolean calcKannVerteidigen(int spielernummer){
		boolean ja=false;
		Spieler s=a_spieler[spielernummer];
		if(s.get_zenturios()>0){
			//if(meinSpielbrett.get_einheitenRS(angriffsRegion,ss_spielerID)>0){
				ja=true;
			//}
		}
		
		return ja;
	}
	public void aktionGeschenkWaehlen(){
		String nachricht;
		int einheiten=3;
		int region=((int)(100*Math.random()))%Konstanten.REGIONENANZAHL;
		this.resetEinheitenCache();
		boolean[] prefProvinzen=this.calcPrefProvinz(spielerNummer);
		int[] defizite=this.calcEinheitenDefizite();
		for(int i=0;i<defizite.length;i++){
			einheiten+=defizite[i];
		}
		if(einheiten>5){
			nachricht = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+
			KonstantenProtokoll.A_BONUSPUNKTEGEWAEHLT;
		}else{
			nachricht = KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+
			KonstantenProtokoll.A_BONUSEINHEITENGEWAEHLT;
			for(int i=0;i<einheitenCache.length;i++){
				nachricht=nachricht+KonstantenProtokoll.SEPARATORSUBJEKT+einheitenCache[i];
			}
		}
		
		this.nachrichtAnServer(nachricht);
		
	}
	public void aktionAngreifen(){
		String nachricht;
		boolean nichtOk=true;
		boolean[] prefProvinzen=this.calcPrefProvinz(spielerNummer);
		int[] defizit=this.calcEinheitenDefizite();
		int mindefizit=99999;
		angriffsProvinz=Konstanten.NIXXOS;
		angriffsRegion=Konstanten.NIXXOS;
		boolean[] isBearbeitet=new boolean[Konstanten.PROVINZENANZAHL];
		this.setBooleanArray(isBearbeitet,false);
		
		int z=0;	
		while(!this.getIsFalse(isBearbeitet)){
			z=this.zufallszahl(Konstanten.PROVINZENANZAHL,0);
			while(isBearbeitet[z]){
				z=this.zufallszahl(Konstanten.PROVINZENANZAHL,0);
			}
			isBearbeitet[z]=true;
			if(prefProvinzen[z]){
				if(meinSpielbrett.getSpielernummerVonProvinz(z)!=Konstanten.NIXXOS){
					for(int j=0;j<Konstanten.REGIONENANZAHL;j++){
						if(meinSpielbrett.is_regionProvinzGrenze(j,z)){
							if(meinSpielbrett.getEinheitenRS(j,spielerNummer)>0){
								if(mindefizit>defizit[j]){
									// TODO einheitenabgleich?
									mindefizit=defizit[j];
									angriffsProvinz=z;
									angriffsRegion=j;
								}
							}
						}
					}
				}
			}
		}
		if(angriffsProvinz==Konstanten.NIXXOS){
			for(int j=0;j<Konstanten.REGIONENANZAHL;j++){
				if(meinSpielbrett.getEinheitenRS(j,spielerNummer)>0){
					for(int i=0;i<Konstanten.PROVINZENANZAHL;i++){
						if(meinSpielbrett.getSpielernummerVonProvinz(i)!=Konstanten.NIXXOS&&meinSpielbrett.getSpielernummerVonProvinz(i)!=spielerNummer&&meinSpielbrett.is_regionProvinzGrenze(j,i)){
							if(mindefizit>defizit[j]){
								mindefizit=defizit[j];
								angriffsProvinz=i;
								angriffsRegion=j;
							}
						}
					}
				}
				
			}
		}
		
		
		nachricht=KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+
		KonstantenProtokoll.A_ANGEGRIFFEN+KonstantenProtokoll.SEPARATORSUBJEKT+angriffsRegion+KonstantenProtokoll.SEPARATORSUBJEKT+angriffsProvinz;
		aktAktion=KonstantenProtokoll.WARTEN;
		this.nachrichtAnServer(nachricht);
	}
	public void aktionTrainieren(){
		String nachricht;
		int lager=meinSpielbrett.get_legionslagerS(spielerNummer);
		int training=0;
		int geld=this.getMeinSpielerByID(spielerID).getGeld();
		if(geld>lager){
			training=lager;
		}
		else{
			training=geld;
		}
		nachricht=KonstantenProtokoll.EINSPIELER+KonstantenProtokoll.SEPARATORHEADER+
		KonstantenProtokoll.A_TRAININERT+KonstantenProtokoll.SEPARATORSUBJEKT+lager;
		aktAktion=KonstantenProtokoll.WARTEN;
		this.nachrichtAnServer(nachricht);
	}
	public int[] calcEinheitenDefizite(){
		int[] einheitenDefizite=new int[Konstanten.REGIONENANZAHL];
		int[] einfluesse=this.calcEinflussProvinz(spielerNummer);
		for(int i=0;i<einheitenDefizite.length;i++){
			einheitenDefizite[i]=0;
		}
		
		int meineEinheiten=0;
		for(int i=0;i<Konstanten.REGIONENANZAHL;i++){
			meineEinheiten=meinSpielbrett.getEinheitenRS(i,spielerNummer);
			meineEinheiten+=(int)(meineEinheiten*this.getMeinSpielerByID(spielerID).getTrainingsFaktor());
			for(int j=0;j<spieleranzahl;j++){
				if(einheitenDefizite[i]<(-1*meineEinheiten+meinSpielbrett.getEinheitenRS(i,j))+einfluesse[i]){
					einheitenDefizite[i]=(-1*meineEinheiten+meinSpielbrett.getEinheitenRS(i,j))+einfluesse[i];
				}
			}
		}
		return einheitenDefizite;
	}
	
	
public int zufallszahl(int maximum, int minimum){
	return (((int)(Math.random()*1000000))%maximum)+minimum;
}

public void beendeClient() {
	try {
		meinClient.stoppeClient();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

public void clientBeendet() {
	// TODO Auto-generated method stub
	
}
		
}
