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

package tools;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class is the localization tool. Type Localization.getInstance().getString(key)
 * to get a localized string from the property file in code.
 * The Locale of the os is the default locale. The bundle name is "messages".
 * @author johannes
 *
 */
public class Localization {

	/**
	 * instance of Localization
	 */
	private static Localization instance = new Localization();

	/**
	 * instance of the bundle
	 */
	private static ResourceBundle myBundle;
	
	/**
	 * private constructor, which initializes the default bundle
	 */
	private Localization(){
		myBundle = ResourceBundle.getBundle("messages");
	}
	
	/**
	 * get an the instance of the current localization bundle
	 * @return
	 */
	public static ResourceBundle getInstance(){
		return myBundle;
	}
	
	/**
	 * sets a new default locale
	 * @param loc
	 */
	public static void setLocale(Locale loc){
		Locale.setDefault(loc);
		myBundle = ResourceBundle.getBundle("messages");
	}

}
