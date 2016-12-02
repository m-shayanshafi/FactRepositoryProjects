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

import entities.Einheit;
import entities.Konstanten;
import entities.Provinz;
import entities.RPGrenze;
import entities.Region;
import game.Spielbrett;
import game.VerwaltungClient;

import java.awt.Color;
import java.util.Vector;

import javax.swing.JComponent;


/**
 * class represents the big gaming map 
 * @author johannes
 *
 */
public class Karte extends JComponent{
	
	
	/**
	 * width of the map
	 */
	public static final int WIDTH = 1589;
	/**
	 * height of the map
	 */
	public static final int HEIGHT = 1229;
	
	private Vector<Grafik> gebietsmarker;
	private Vector<Grafik> warenmarker;
	private Vector<GrafikEinheiten> einheitenmarker;
	private Vector<Grafik> angriffsmarker;
	private Vector<Grafik> staedte;
	
	private Grafik karte;

	public Karte(VerwaltungClient ss_verClient){
		
		MeinMouseListener aktMouseListener;
		Grafik aktGrafik;
		GrafikEinheiten aktGrafikEinheiten;
		
		String s_einheit;
		gebietsmarker = new Vector<Grafik>();
		warenmarker = new Vector<Grafik>();
		einheitenmarker = new Vector<GrafikEinheiten>();
		angriffsmarker = new Vector<Grafik>();
		staedte=new Vector<Grafik>();
		
		Spielbrett meinBrett=ss_verClient.getMeinSpielbrett();
		
		Provinz aktProvinz;
		Region aktRegion;
		Einheit aktEinheit;
		RPGrenze aktRPGrenze;
		
		int spieler; 
		
		this.setLayout(null);
		this.setSize(WIDTH, HEIGHT);
		this.setBackground(Color.BLACK);
		
		karte = new Grafik(Bilder.KARTE);
		karte.setType(Konstanten.KARTE);
		karte.setBounds(0,0, WIDTH, HEIGHT);
		
		//Gebietsmarker initialisieren
		for (int i = 0;i<Konstanten.PROVINZENANZAHL;i++){
			//URL bildURL = getClass().getResource("Bilder/SPQR_weich.gif");
			gebietsmarker.addElement(new Grafik(Bilder.SPQR));
			warenmarker.addElement(new Grafik(Bilder.WEIZEN));
			staedte.addElement(new Grafik(Bilder.ROM));

		}
		
		//Gebietsmarker auf der karte platzieren
		for (int i = 0; i < gebietsmarker.size(); i++){
			aktProvinz = meinBrett.getProvinzByIndex(i);
			aktGrafik = gebietsmarker.elementAt(i);
			aktGrafik.setBounds(aktProvinz.getX_pMarker(),aktProvinz.getY_pMarker(),30,50);
			aktGrafik.set_offset(aktProvinz.getX_pMarker(),aktProvinz.getY_pMarker());
			aktGrafik.setType(Konstanten.PROVINZMARKER);
			aktGrafik.setMeinObject(aktProvinz);
			aktGrafik.setObservable(ss_verClient.getMeinSpielbrett());
			aktGrafik.addMouseListener(new MeinMouseListener(aktGrafik,Konstanten.PROVINZMARKER,ss_verClient));
			this.add(aktGrafik);
			
			aktGrafik = warenmarker.elementAt(i);
			aktGrafik.setBounds(aktProvinz.getX_wMarker(),aktProvinz.getY_wMarker(),30,30);
			aktGrafik.set_offset(aktProvinz.getX_wMarker(),aktProvinz.getY_wMarker());
			aktGrafik.setType(Konstanten.WARENMARKER);
			aktGrafik.setMeinObject(aktProvinz);
			aktGrafik.setObservable(ss_verClient.getMeinSpielbrett());
			aktGrafik.addMouseListener(new MeinMouseListener(aktGrafik,Konstanten.WARENMARKER,ss_verClient));
			this.add(aktGrafik);
			
			if(aktProvinz.isStadt){
				aktGrafik = staedte.elementAt(i);
				aktGrafik.setBounds(aktProvinz.getX_Stadt(),aktProvinz.getY_Stadt(),30,30);
				aktGrafik.set_offset(aktProvinz.getX_Stadt(),aktProvinz.getY_Stadt());
				aktGrafik.setType(Konstanten.STADT);
				aktGrafik.setMeinObject(aktProvinz);
				this.add(aktGrafik);
			}
			
		}
		
		//		Einheitenmarker initialisieren
		for (int i = 0; i < Konstanten.MAX_EINHEITENMARKER; i++){
			aktRegion=meinBrett.getRegionByIndex((int)(i/5));
			spieler = i%5;
			if(aktRegion.getTopologie().equals("wasser")){
				s_einheit = SpielerEinheiten.WASSEREINHEITEN[spieler];
				einheitenmarker.addElement(new GrafikEinheiten(ss_verClient,s_einheit,Color.GRAY,1));
			}
			else{
				s_einheit = SpielerEinheiten.LANDEINHEITEN[spieler];
				einheitenmarker.addElement(new GrafikEinheiten(ss_verClient,s_einheit,Color.BLACK,1));
			}
			
		}
		
//		Einheitenmarker auf der karte platzieren
		for (int i=0;i<einheitenmarker.size();i++){
			aktRegion=meinBrett.getRegionByIndex((int)(i/5));
			aktEinheit=aktRegion.getV_Einheiten().elementAt(i%5);
			aktGrafikEinheiten = einheitenmarker.elementAt(i);
			aktGrafikEinheiten.setBounds(aktEinheit.getX(),aktEinheit.getY(),37,37);
			aktGrafikEinheiten.set_offset(aktEinheit.getX(),aktEinheit.getY());
			aktGrafikEinheiten.set_id(aktEinheit.getOid());
			aktGrafikEinheiten.setToolTipText(aktRegion.getName());
			aktMouseListener=new MeinMouseListener(aktGrafikEinheiten,Konstanten.EINHEITENMARKER,ss_verClient);
			aktGrafikEinheiten.addMouseListener(aktMouseListener);
			
			this.add(aktGrafikEinheiten);
		}
		
		Vector<RPGrenze> grenzen=meinBrett.getV_RPGrenzen();
		for(int i = 0; i < grenzen.size(); i++){
			aktRPGrenze=grenzen.elementAt(i);
			aktGrafik = new Grafik(Bilder.PFEIL_N[0]);
			aktGrafik.setType(Konstanten.ANGRIFFSMARKER);
			aktGrafik.setMeinObject(aktRPGrenze);
			aktGrafik.setObservable(ss_verClient);
			this.add(aktGrafik);
			angriffsmarker.addElement(aktGrafik);
		}

		this.add(karte);
		
		MeinGUICtrlVerschieber meinVerschieber = new MeinGUICtrlVerschieber(this, Konstanten.PNL_KARTE, false);
		this.addMouseListener(meinVerschieber);
		this.addMouseMotionListener(meinVerschieber);
		
		karte.setVisible(true);
	}
	
	public int get_kartenbreite(){
		return WIDTH;
	}
	
	public int get_kartenhoehe(){
		return HEIGHT;
	}
	
	public Grafik get_grafik(){
		return karte;
	}
	
	public Vector<Grafik> get_gebietsmarker(){
		return gebietsmarker;
	}
	
	public Vector<Grafik> get_warenmarker(){
		return warenmarker;
	}
	public Vector<GrafikEinheiten> get_einheitenmarker(){
		return einheitenmarker;
	}
	public Vector<Grafik> get_angriffsmarker(){
		return angriffsmarker;
	}

}
