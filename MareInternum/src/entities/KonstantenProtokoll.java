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
 * All constants for server-client and client-server communication 
 * 
 * @author johannes
 *
 */
public class KonstantenProtokoll {

	//all protocol types
	public static final int ID=132;
	public static final int INITIALISIERUNG = 0;
	public static final int ANMELDUNG = 4;
	public static final int CHAT = 3;
	public static final int EINSPIELER = 1;
	public static final int ABMELDEN = 8;
	
	// all actions
	public static final int K_BOTSCHAFTER = 0;
	public static final int K_ZENTURIO = 1;
	public static final int K_ANGREIFEN = 2;
	public static final int K_EINHEITENKAUFEN = 4;
	public static final int K_LAGERBAUEN = 3;
	public static final int K_TRAINIEREN=5;
	public static final int K_MAX=6;
	public static final int K_ENDE = 7;
	
	//constants for responses
	//what to do
	public static final int WARTEN = 0;
	public static final int PROVINZWAEHLEN = 1;
	public static final int KARTENWAEHLEN = 2;
	public static final int ANGREIFEN = 3;
	public static final int VERTEIDIGEN = 4;
	public static final int GESCHENKWAEHLEN = 6;
	public static final int EINHEITENKAUFEN = 8;
	public static final int LAGERBAUEN = 9;
	public static final int TRAININEREN = 11;
	public static final int STOP=99;
	
	//constants for responses
	//what happened
	public static final int WP_GEHALT=1;
	public static final int WP_PROVINZAUSGEWAEHLT=2;
	public static final int WP_KARTENGEWAEHLT=3;
	public static final int WP_BOTSCHAFTERGESENDET=4;
	public static final int WP_PROVINZGEWECHSELT=5;
	public static final int WP_LAGERGEBAUT=6;
	public static final int WP_PROVINZERSTEIGERT=9;
	public static final int WP_EINHEITENGEKAUFT = 10;
	public static final int WP_EINHEITENVERSCHOBEN = 14;// not used yet
	public static final int WP_ZENTURIODAZU = 11;
	public static final int WP_ANGEGRIFFEN=12;
	public static final int WP_KAMPFVORBEI = 13;
	public static final int WP_FEHLER = 16;
	public static final int WP_BONUSPUNKTE=17;
	public static final int WP_BONUSEINHEITEN=18;
	public static final int WP_TRAINIERT=19;
	public static final int WP_NIX = 99;
	public static final int WP_SPIELENDE=100;
	
	//all message strings have to be localized
	public static final String MSG_GEHALT_FUERZENTURIOS="ForWarlords";
	public static final String MSG_GEHALT_NORMAL="Normal";
	public static final String MSG_HATKAPITULIERT="Capitulated";
	public static final String MSG_MUSSTEKAPITULIEREN="HadToCapitulate";
	
	// constants for requests
	public static final int A_NIX = 0;
	public static final int A_PROVINZGEWAEHLT = 1;
	public static final int A_KARTENGEWAEHLT = 2;
	public static final int A_ANGEGRIFFEN = 3;
	public static final int A_VERTEIDIGT = 9;
	public static final int A_EINHEITENGEKAUFT = 4;
	public static final int A_EINHEITENVERSCHOBEN = 5; // not used yet
	public static final int A_LAGERGEBAUT = 8;
	public static final int A_BONUSPUNKTEGEWAEHLT=10;
	public static final int A_BONUSEINHEITENGEWAEHLT=11;
	public static final int A_KAPITULIERT=12;
	public static final int A_TRAININERT = 13;
	
	//separators in protocol to make a distinction between header, body ,...
	public static final String SEPARATORHEADER ="&";
	public static final String SEPARATOR1 = "/";
	public static final String SEPARATORSUBJEKT = ",";
	
}
