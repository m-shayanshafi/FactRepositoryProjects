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

package gui;

import entities.Konstanten;
import entities.Provinz;
import entities.RPGrenze;
import game.Spielbrett;
import game.VerwaltungClient;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

public class Grafik extends JComponent implements Observer,Abfragbar{
	
	private String aktBild;
	private String alternativBild;
	private String bild;
	private int offsetX;
	private int offsetY;
	private int id;
	private int type;
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public Grafik(String ss_bild){
		this.setIgnoreRepaint(false);
		bild=ss_bild;
		aktBild = ss_bild;
	}
	@Override
	public void paintComponent(Graphics g){
		Image i = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(aktBild));
		g.drawImage(i,0,0,this);
	}
	
	public void change(String ss_bild){
		bild = ss_bild;
		aktBild = bild;
		this.repaint();
	}	
	
	public void showAlternative(String ss_bild){
		alternativBild =ss_bild;
		aktBild = alternativBild;
		this.repaint();
	}
	
	public void reset(boolean ss_ignoremuenze){
		if(!ss_ignoremuenze){
			aktBild = bild;
		}
		else{
			if(!alternativBild.equals(Bilder.MUENZE)){
				aktBild = bild;
			}
		}
		this.repaint();
	}
	
	public void set_offset(int ss_x,int ss_y){
		offsetX = ss_x;
		offsetY = ss_y;
	}
	
	public String getBild(){
		return aktBild;
	}
	
	public void set_id(int ss_id){
		id =ss_id;
	}
	
	public int get_offsetX(){
		return offsetX;
	}
	
	public int get_offsetY(){
		return offsetY;
	}
	
	public void update(Observable obs, Object arg1) {

		String msg=(String)arg1;
		RPGrenze aktRPGrenze;
		Provinz provinz;
		VerwaltungClient verClient;
		if(obs instanceof Spielbrett){
			if(type==Konstanten.PROVINZMARKER){
				provinz=((Spielbrett)obs).getProvinzByOid(id);
				if(provinz.isAusgewaehlt()){
					this.change(Bilder.MUENZE);
				}
				else if(provinz.getSpielernummer()!=Konstanten.NIXXOS){
					if(provinz.getZ_lager()!=0){
						this.change(SpielerEinheiten.ZELTE[provinz.getSpielernummer()]);
					}
					else{
						this.change(SpielerEinheiten.FAHNEN[provinz.getSpielernummer()]);
					}
				}
				else{
					this.change(Bilder.SPQR);
				}
			}
			else if(type==Konstanten.WARENMARKER){
				provinz=((Spielbrett)obs).getProvinzByOid(id);
				this.setWare(provinz.getWare());
				
			}
			else if(type==Konstanten.PROVINZMARKERKLEIN){
				provinz=((Spielbrett)obs).getProvinzByOid(id);
				if(provinz.getSpielernummer()!=Konstanten.NIXXOS){
					this.change(SpielerEinheiten.PUNKTE[provinz.getSpielernummer()]);
				}
			}
		}
		if(obs instanceof VerwaltungClient&&(msg.equals(Konstanten.NOMSG)||msg.equals(Konstanten.SPIELSTART))){
			if(type==Konstanten.ANGRIFFSMARKER){
				verClient=(VerwaltungClient)obs;
				aktRPGrenze=verClient.getRPGrenze(id);
				this.setVisible(false);
				if(verClient.getAngreifer()!=Konstanten.NIXXOS){
					if(aktRPGrenze.regionID==verClient.getAngriffsRegion()&&aktRPGrenze.provinzID==verClient.getAngriffsProvinz()){
						if(aktRPGrenze.getRichtung().equals("N")){
							this.showAlternative(Bilder.PFEIL_N[verClient.getAngreifer()]);
						}
						if(aktRPGrenze.getRichtung().equals("O")){
							this.showAlternative(Bilder.PFEIL_O[verClient.getAngreifer()]);
						}
						if(aktRPGrenze.getRichtung().equals("S")){
							this.showAlternative(Bilder.PFEIL_S[verClient.getAngreifer()]);
						}
						if(aktRPGrenze.getRichtung().equals("NW")){
							this.showAlternative(Bilder.PFEIL_NW[verClient.getAngreifer()]);
						}
						if(aktRPGrenze.getRichtung().equals("NO")){
							this.showAlternative(Bilder.PFEIL_NO[verClient.getAngreifer()]);
						}
						if(aktRPGrenze.getRichtung().equals("W")){
							this.showAlternative(Bilder.PFEIL_W[verClient.getAngreifer()]);
						}
						this.setVisible(true);
					}
				}
			}
			if(type==Konstanten.ANGRIFFSMARKERKLEIN){
				verClient=(VerwaltungClient)obs;
				aktRPGrenze=verClient.getRPGrenze(id);
				this.setVisible(false);
				if(verClient.getAngreifer()!=Konstanten.NIXXOS){
					if(aktRPGrenze.regionID==verClient.getAngriffsRegion()&&aktRPGrenze.provinzID==verClient.getAngriffsProvinz()){
						if(aktRPGrenze.getRichtung().equals("N")){
							this.showAlternative(Bilder.PFEILKLEIN[verClient.getAngreifer()]);
						}
						if(aktRPGrenze.getRichtung().equals("O")){
							this.showAlternative(Bilder.PFEILKLEIN[verClient.getAngreifer()]);
						}
						if(aktRPGrenze.getRichtung().equals("S")){
							this.showAlternative(Bilder.PFEILKLEIN[verClient.getAngreifer()]);
						}
						if(aktRPGrenze.getRichtung().equals("NW")){
							this.showAlternative(Bilder.PFEILKLEIN[verClient.getAngreifer()]);
						}
						if(aktRPGrenze.getRichtung().equals("NO")){
							this.showAlternative(Bilder.PFEILKLEIN[verClient.getAngreifer()]);
						}
						if(aktRPGrenze.getRichtung().equals("W")){
							this.showAlternative(Bilder.PFEILKLEIN[verClient.getAngreifer()]);
						}
						this.setVisible(true);
					}
				}
			}
			
		}
		
	}
	
	public void setObservable(Observable obs){
		obs.addObserver(this);
	}
	public void setWare(int ware){
		switch(ware){
		case Konstanten.WEIN:this.change(Bilder.WEIN);break;
		case Konstanten.WEIZEN:this.change(Bilder.WEIZEN);break;
		case Konstanten.TOEPFERWARE:this.change(Bilder.TON);break;
		case Konstanten.SCHAAF:this.change(Bilder.SCHAF);break;
		case Konstanten.STEIN:this.change(Bilder.STEIN);break;
		default:this.change(Bilder.NOK);
		}
	}
	public String getData() {
		
		return null;
	}
	public int getID() {
		
		return id;
	}
	
	
	public void setMeinObject(Object meinObject) {
		Class c=meinObject.getClass();
		RPGrenze aktRPGrenze;
		Provinz aktProvinz;
		if(type==Konstanten.ANGRIFFSMARKER){
			aktRPGrenze=(RPGrenze)meinObject;
			this.set_id(aktRPGrenze.getOid());
			this.set_offset(aktRPGrenze.getX(),aktRPGrenze.getY());
			this.setBounds(aktRPGrenze.getX(),aktRPGrenze.getY(),50,50);
			
		}
		if(type==Konstanten.PROVINZMARKER||type==Konstanten.WARENMARKER){
			aktProvinz=(Provinz)meinObject;
			this.set_id(aktProvinz.getOid());
			if(type!=Konstanten.STADT){
				this.setName(aktProvinz.getName());
				this.setToolTipText(aktProvinz.getName());
			}
			
		}
		
	}
	
}
	

