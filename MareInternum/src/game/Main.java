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

import javax.swing.JOptionPane;

import tools.Localization;

import gui.DlgInfo;
import gui.DlgPreferences;
import gui.Hauptfenster;

public class Main {

	private Hauptfenster meinHauptfenster;
	private Spielverwaltung meineVerwaltung;
	private static Main me;
	
	/**
	 * main method of game
	 * @param args: no args
	 */
	public static void main(String[] args) {

		me = new Main();
		me.init();
	}
	
	/**
	 * method reloads main window and game manager
	 */
	public void init(){
		
		meineVerwaltung= new Spielverwaltung(this);
			
		if(meinHauptfenster != null){
			meinHauptfenster.dispose();
			meinHauptfenster = null;
		}
				
		meinHauptfenster = new Hauptfenster(meineVerwaltung);
		System.out.println("neues Spiel");
	}
	
	/**
	 * forces main window to store all locations of gui panels persistent
	 */
	public void storeGuiPanelLocations(){
		meinHauptfenster.storePanelLocations();	
	}
	
	/**
	 * generates input dialog and returns input from the dialog
	 * @param label
	 * @param value
	 * @return
	 */
	public static String showInputDialog(String label, String value){
		return (String)JOptionPane.showInputDialog(
				label,
				value);
	}
	
	/**
	 * shows info dialog
	 */
	public static void showInfoDialog() {
		DlgInfo dlg=new DlgInfo();
	}
	
	/**
	 * shows dialog with an error message
	 * @param msg
	 */
	public static void showErrorMessage(String msg){
		JOptionPane.showMessageDialog(null,
				msg,
				Localization.getInstance().getString("Error"),
		    JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * shows a simple message dialog
	 * @param msg
	 */
	public static void showPlainMessage(String msg){
		JOptionPane.showMessageDialog(null,
				msg,
				Localization.getInstance().getString("News"),
		    JOptionPane.PLAIN_MESSAGE);
	}
	
	/**
	 * shows preferences dialog
	 */
	public static void showDlgPreferences(){
		new DlgPreferences(me);
	}
}
