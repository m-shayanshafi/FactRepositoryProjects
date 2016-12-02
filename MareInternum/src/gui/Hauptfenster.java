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

import java.awt.Color;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JFrame;

import tools.Dateizugriff;
import entities.Konstanten;
import entities.KonstantenProtokoll;
import game.Spielverwaltung;
import game.VerwaltungClient;

public class Hauptfenster extends JFrame implements Runnable,ComponentListener,Observer {
	
	/**
	 * to satisfy java 5 requirements
	 */
	private static final long serialVersionUID = 1L;
	
	
	private final int HEIGHT_SUBTRAHENT = 50;
	private final int WIDTH_SUBTRAHENT = 10;
	private final int MARGIN = 10;
	
	private HashMap<String, JComponent> guiComponents;
	
	private boolean endeIntro;
	private Karte meineKarte;
	private Chatfenster meinChatfenster;
	private ControlPanel meinControlPanel;
	private AktionenAuswahl meineAktionenAuswahl;
	private Serversteuerung meineServersteuerung;
	private AnzeigeKartenauswahl meineAnzeigeKartenauswahl;

	private AngriffPanel meinAngriffPanel;
	private VerteidigenPanel meinVerteidigenPanel;
	private EinheitenVerwaltungsPanel meinEinheitenPanel;
	private GeschenkPanel meinGeschenkPanel;
	private TeilnehmenPanel meinTeilnehmenPanel;
	private TrainingPanel meinTrainingPanel;
	private IntroPanel meinIntroPanel;
	private KleineKartePanel meineKleineKarte;
	
	private Container cp;
	
	public Hauptfenster(Spielverwaltung ss_Verwaltung){
		
		guiComponents = new HashMap<String, JComponent>();
		
		this.init(ss_Verwaltung);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(Bilder.ICON)));
	}
	
	public void init(Spielverwaltung ss_Verwaltung){
		
		
		VerwaltungClient meineVerClient = ss_Verwaltung.getMeineVerClient();
		Spielverwaltung meineVerwaltung = ss_Verwaltung;
		meineVerwaltung.addObserver(this);
		meineVerClient.addObserver(this);

		endeIntro=false;
		
		cp=this.getContentPane();
		this.setTitle(Konstanten.NAME+" "+Konstanten.VERSION);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addComponentListener(this);
		cp.setLayout(null);	
		
		new Menue(meineVerwaltung,this);
		
		
		Abfragbar[] abfragbar=new Abfragbar[20];
		MeinActionListener actListener = new MeinActionListener(meineVerClient);
				
		meinControlPanel = new ControlPanel(meineVerClient,actListener);
		registerGuiComponent(meinControlPanel);
		
		meinTrainingPanel=new TrainingPanel(meineVerClient);
		registerGuiComponent(meinTrainingPanel);
		abfragbar[KonstantenProtokoll.TRAININEREN]=meinTrainingPanel;
		
		meineAktionenAuswahl = new AktionenAuswahl(meineVerClient);
		registerGuiComponent(meineAktionenAuswahl);
		abfragbar[KonstantenProtokoll.KARTENWAEHLEN]=meineAktionenAuswahl;
		
		meineKleineKarte=new KleineKartePanel(meineVerClient, this);
		registerGuiComponent(meineKleineKarte);
		
		meinAngriffPanel = new AngriffPanel(meineVerClient);
		registerGuiComponent(meinAngriffPanel);
		
		meinVerteidigenPanel = new VerteidigenPanel();
		registerGuiComponent(meinVerteidigenPanel);
		abfragbar[KonstantenProtokoll.VERTEIDIGEN]=meinVerteidigenPanel;
		
		meineServersteuerung = new Serversteuerung(meineVerwaltung);
		registerGuiComponent(meineServersteuerung);
		
		meineAnzeigeKartenauswahl = new AnzeigeKartenauswahl(meineVerClient);
		registerGuiComponent(meineAnzeigeKartenauswahl);
		
		meinEinheitenPanel = new EinheitenVerwaltungsPanel(meineVerClient,actListener);
		registerGuiComponent(meinEinheitenPanel);
		
		meinGeschenkPanel = new GeschenkPanel(actListener);
		registerGuiComponent(meinGeschenkPanel);
		
		meinTeilnehmenPanel = new TeilnehmenPanel(meineVerwaltung);
		registerGuiComponent(meinTeilnehmenPanel);
		actListener.setAbfragbar(abfragbar);
		
		meinIntroPanel = new IntroPanel();
		registerGuiComponent(meinIntroPanel);
		
		meinChatfenster = new Chatfenster(meineVerClient);
		
		meineKleineKarte.setVisible(false);
		meinEinheitenPanel.setVisible(false);
		meineAktionenAuswahl.setVisible(false);
		meinControlPanel.setVisible(false);
		meineAnzeigeKartenauswahl.setVisible(false);
		meinGeschenkPanel.setVisible(false);
		meinAngriffPanel.setVisible(false);
		meinVerteidigenPanel.setVisible(false);
		meinTeilnehmenPanel.setVisible(false);
		meineServersteuerung.setVisible(false);
		meinTrainingPanel.setVisible(false);
		meinChatfenster.setVisible(true);

		cp.add(meinChatfenster);
		cp.add(meineKleineKarte);
		cp.add(meineAktionenAuswahl);
		cp.add(meinTrainingPanel);
		cp.add(meinGeschenkPanel);
		cp.add(meinEinheitenPanel);
		cp.add(meineAnzeigeKartenauswahl);
		cp.add(meinAngriffPanel);
		cp.add(meinVerteidigenPanel);
		cp.add(meinControlPanel);
		cp.add(meineServersteuerung);
		cp.add(meinTeilnehmenPanel);
		cp.add(meinIntroPanel);
		cp.setBackground(Color.BLACK);

		
		meineKarte = new Karte(meineVerClient);
		meineKarte.addComponentListener(this);
		registerGuiComponent(meineKarte);
		
		this.setSize(1200,700);
		this.setVisible(true);
		this.setChatVisible(false);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.initGUIComponentLocations();
		
		//this.setIgnoreRepaint(false);	
	}
	
	
	/**
	 * prepares view for the new game
	 */
	public void start_neuesSpiel(){	
		endeIntro=true;
		initGUI();
	}
	
	public void setChatVisible(boolean ss_visible){

		meinChatfenster.setVisible(ss_visible);
	}

	public void componentResized(ComponentEvent arg0) {
		this.resize();
	}
	

	public void resize(){
		
		int meineHoehe = this.getHeight()-this.HEIGHT_SUBTRAHENT;
		int meineBreite= this.getWidth()-this.WIDTH_SUBTRAHENT;
		
		meinIntroPanel.setBounds((this.getWidth()/2) - (meinIntroPanel.getWidth()/2), this.getHeight()/3-meinIntroPanel.HEIGHT/3, meinIntroPanel.WIDTH, meinIntroPanel.HEIGHT);
		meineServersteuerung.setBounds(MARGIN, MARGIN,this.getWidth(), meineServersteuerung.HEIGHT);
		meinTeilnehmenPanel.setBounds(MARGIN, MARGIN, this.getWidth(), meinTeilnehmenPanel.HEIGHT);
		meinChatfenster.resize(meineBreite,meineHoehe);
		
		meineKleineKarte.setFocusSize(meineBreite/10, meineHoehe/10);
	
	}
	
	/**
	 * is called if jcomponent (map) was moved
	 * updates focus of small navigation map
	 */
	public void componentMoved(ComponentEvent event) {
		
		if(event.getSource() instanceof Karte){
			Karte karte = (Karte)event.getSource();
			meineKleineKarte.setRelativeFocusLocation(karte.getX(), karte.getY());
		}
		
	}
	
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void  update(Observable obs, Object ss_msg) {
		String msg=(String)ss_msg;
		Spielverwaltung spielverwaltung;
		VerwaltungClient verwClient;
		
		meinTeilnehmenPanel.setVisible(false);
		meineServersteuerung.setVisible(false);
		
		if(obs instanceof Spielverwaltung){
			spielverwaltung=(Spielverwaltung)obs;
			meineServersteuerung.setVisible(spielverwaltung.isSpielstartend());
			meineServersteuerung.updatePort();
			meinTeilnehmenPanel.setVisible(spielverwaltung.isTeilnehmen());
			if(msg != null && msg.equals(Konstanten.SPIELENDE)){
				this.init(spielverwaltung);
			}
		}
		else if(obs instanceof VerwaltungClient){
			verwClient=(VerwaltungClient)obs;
			if(msg.equals(Konstanten.SPIELSTART)){
				this.setAlleSpielPanelsVisible(false);
				this.start_neuesSpiel();
			}
			else if(msg.equals(Konstanten.SPIELENDE)){
				this.setAlleSpielPanelsVisible(false);
				cp.remove(meineKarte);
				meinControlPanel.setVisible(false);
			}
			else if(msg.equals(Konstanten.CHATMSG)){
				
			}
			else{
				this.setAlleSpielPanelsVisible(false);
				if(Konstanten.DEBUG>0){System.out.println("Aktion HptFenster:"+verwClient.getAktAktion());}
				if(verwClient.getAktAktion()==KonstantenProtokoll.KARTENWAEHLEN){
					meineAnzeigeKartenauswahl.setVisible(true);
					meineAktionenAuswahl.setVisible(true);
				}
				else if(verwClient.getAktAktion()==KonstantenProtokoll.GESCHENKWAEHLEN){
					meinGeschenkPanel.setVisible(!verwClient.isEinheitenGeschenk());
					meinEinheitenPanel.setVisible(verwClient.isEinheitenGeschenk());
				}
				else if(verwClient.getAktAktion()==KonstantenProtokoll.EINHEITENKAUFEN){
					meinEinheitenPanel.setVisible(true);
				}
				else if(verwClient.getAktAktion()==KonstantenProtokoll.ANGREIFEN){
					meinAngriffPanel.setVisible(true);
				}
				else if(verwClient.getAktAktion()==KonstantenProtokoll.VERTEIDIGEN){
					meinVerteidigenPanel.setVisible(true);
				}
				else if(verwClient.getAktAktion()==KonstantenProtokoll.TRAININEREN){
					meinTrainingPanel.setVisible(true);
				}
				
				
			}
			
		}
		
	}

	public void run() {
		int y = this.getHeight() - 380;
		while(!endeIntro){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			y--;
			meinIntroPanel.setBounds((this.getWidth()/2) - (meinIntroPanel.getWidth()/2), y, meinIntroPanel.WIDTH, meinIntroPanel.HEIGHT);
		}	
		
	}
	
	
	private void setAlleSpielPanelsVisible(boolean isVisible){
		
		meinEinheitenPanel.setVisible(isVisible);
		meineAktionenAuswahl.setVisible(isVisible);
		meinGeschenkPanel.setVisible(isVisible);
		meinAngriffPanel.setVisible(isVisible);
		meinVerteidigenPanel.setVisible(isVisible);
		meinTrainingPanel.setVisible(isVisible);
		
	}
	
	/**
	 * inits gui components (location)
	 * loads values from persistent stored object if possible
	 */
	private void initGUIComponentLocations(){
		JComponent c;
		
		HashMap guiProperties = loadPanelLocationsFromFile();
		
		int xValue = 0;
		int yValue = 0;
		
		if(guiProperties != null){
			
			Iterator iter = guiComponents.keySet().iterator();
			while(iter.hasNext()){
				c = this.getGuiComponent((String)iter.next());
				
				Object o = guiProperties.get(c.getClass().getName() + ".x");
				if(o != null){
					xValue = ((Integer)o).intValue();
				}
				
				o = guiProperties.get(c.getClass().getName() + ".y");
				if(o != null){
					yValue = ((Integer)o).intValue();
				}
				
				c.setLocation(xValue, yValue);
			}
			
		}
		else{
			
			meinControlPanel.setLocation( this.getWidth() - meinControlPanel.getWidth() - MARGIN,  MARGIN);			
			meineAnzeigeKartenauswahl.setLocation(meinTrainingPanel.getX() + meinTrainingPanel.getWidth() + MARGIN, MARGIN);
			meineKleineKarte.setLocation(this.getWidth()/2 - meineKleineKarte.getWidth()/2, this.getHeight()/2 - meineKleineKarte.getHeight()/2);
		}
		
		
	
	}
	
	/**
	 * toggles basic view components visible:
	 * map, control panel, chat
	 */
	private void initGUI(){
		meinIntroPanel.setVisible(false);
		cp.add(meineKarte);
		meinControlPanel.setVisible(true);
		this.setChatVisible(true);
	}
	
	/**
	 * set the visibility status of a specific component,
	 * specified by its class name
	 * @param classname
	 * @param b
	 */
	public void setGuiComponentVisible(String classname, boolean b){
		JComponent c = getGuiComponent(classname);
		
		if(c != null){
			c.setVisible(b);	
		}
	}
	
	/**
	 * sets location of gaming map
	 * @param x
	 * @param y
	 */
	public void centerLocationMap(int x, int y)
	{
		if(meineKarte != null){
			meineKarte.setBounds((x * (-1) )+ (this.getWidth()/2), (y * (-1)) + (this.getHeight()/2), meineKarte.getWidth(), meineKarte.getHeight());
		}
	}
	
	/**
	 * returns registered gui component by class name
	 * @param cn
	 * @return
	 */
	public JComponent getGuiComponent(String classname){
		if(guiComponents != null){
			return guiComponents.get(classname);
		}
		return null;
	}
	
	/**
	 * adds a component to collection of gui components
	 * @param c
	 */
	private void registerGuiComponent(JComponent c){
		
		if(guiComponents == null){
			guiComponents = new HashMap<String, JComponent>();
		}
		guiComponents.put(c.getClass().getName(), c);
	}
	
	/**
	 * stores all current panel locations from registered gui components into a file
	 */
	public void storePanelLocations(){
		HashMap locations = new HashMap();
		
		Iterator iter = guiComponents.keySet().iterator();
		while(iter.hasNext()){
			JComponent c = guiComponents.get(iter.next());
			locations.put(c.getClass().getName() + ".x", c.getX());
			locations.put(c.getClass().getName() + ".y", c.getY());
		}
		
		Dateizugriff.speicherObject(Dateizugriff.PATH_GUI_OBJECTS, locations);
		
	}
	
	/**
	 * loads persistent stored locations of gui panels
	 * @return
	 */
	public HashMap loadPanelLocationsFromFile(){
		Object o = Dateizugriff.ladeObject(Dateizugriff.PATH_GUI_OBJECTS);
		if(o instanceof HashMap){
			return (HashMap)o;
		}
		return null;		
	}

	/**
	 * returns instance of map
	 * @return
	 */
	public Karte getMeineKarte() {
		return meineKarte;
	}
	
}
