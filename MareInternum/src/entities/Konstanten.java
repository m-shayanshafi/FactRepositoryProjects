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

package entities;

/**
 * This class holds all general constant values of the game
 * @author johannes
 *
 */
public class Konstanten {
	
	//ids for panels
	public static final int PNL_KARTE=0;
	public static final int PNL_KLKARTE=1;
	public static final int PNL_INFO=2;
	public static final int PNL_AKTIONEN=3;
	public static final int PNL_CHAT=4;
	public static final int PNL_AKTIONENANZEIGE=5;
	public static final int PNL_ANGRIFF=6;
	public static final int PNL_EINHEITEN=7;
	public static final int PNL_GESCHENK=8;
	public static final int PNL_TRAINIEREN=9;
	public static final int PNL_VERTEIDIGEN=10;
	
	/**
	 * integer value for no value
	 */
	public static final int NIXXOS=9999;
	
	//title and version number
	public static final String NAME = "Mare Internum";
	public static final String TITEL = "Programmierung und Produktion:\nJohannes H\u00F6chst\u00E4dter";
	public static final String VERSION = "1.7.0";
	
	/**
	 * flag to toggle debug print outs on or off
	 * DEBUG > 0 : on
	 * DEBUG < 0 : off
	 */
	public static int DEBUG=-1;
	
	/**
	 * default port on which mi connects
	 */
	public static final int STANDARDPORT = 49154;
	
	//constants for observer pattern messages
	public static final String NOMSG="no";
	public static final String SPIELSTART="spielstart";
	public static final String SPIELENDE="spielende";
	public static final String CHATMSG="chat";
	
	//all graphic types
	public static final int PROVINZMARKERKLEIN =0;
	public static final int PROVINZMARKER =1;
	public static final int WARENMARKER = 2;
	public static final int ANGRIFFSMARKER = 3;
	public static final int EINHEITENMARKER = 4;
	public static final int KARTE= 5;
	public static final int STADT=6;
	public static final int ANGRIFFSMARKERKLEIN = 7;
	
	/**
	 * max count of troop graphics
	 */
	public static final int MAX_EINHEITENMARKER = 25;
	
	/**
	 * max count of players
	 */
	public static final int MAX_SPIELER = 5;
	
	/**
	 * amount of warlords on startup
	 */
	public static final int STARTZENTURIOS = 5;
	
	/**
	 * amount of money on startup
	 */
	public static final int STARTGELD = 10;
	
	/**
	 * /count of regions
	 */
	public static final int REGIONENANZAHL = 5;
	
	/**
	 * count of provinces
	 */
    public static final int PROVINZENANZAHL = 35;
    
    /**
     * costs for fortress
     */
    public static final int KOSTENLAGER=5;
    
    /**
     * costs for training
     */
    public static final int KOSTENTRAINING=1;
	
    /**
     * max trading good id
     */
	public static final int MAX_WAREN = 5;
	
    //trading goods
	public static final int WEIZEN = 0;
    public static final int WEIN = 1;
    public static final int STEIN = 2;
    public static final int TOEPFERWARE = 3;
    public static final int SCHAAF = 4;
    
    /**
     * id of province of rome
     */
    public static final int ROM = 21;
    
    /**
     * max count of additional troops from king
     */
    public static final int MAX_BONUS_EINHEITEN = 3;
    
    //constants for points
    /**
     * points for trading goods: max, mid, min
     */
    public static final int[] PUNKTE_WAREN = {4, 2, 1};
    
    /**
     * points for gifts
     */
    public static final int PUNKTE_BONUS = 2;
    
    /**
     * points for rome
     */
    public static final int PUNKTE_ROM = 2;
}
