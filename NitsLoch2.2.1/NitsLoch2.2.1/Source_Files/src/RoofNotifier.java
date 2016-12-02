/*	
	This file is part of NitsLoch.

	Copyright (C) 2007 Darren Watts

    NitsLoch is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    NitsLoch is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with NitsLoch.  If not, see <http://www.gnu.org/licenses/>.
 */

package src;

import src.game.TheEditor;

/**
 * Main class for the map editor.
 * @author Darren Watts
 * date 1/21/08
 *
 */
public class RoofNotifier {
	
	/**
	 * Starts the editor
	 * @param args Command line arguments.  None are used.
	 */
	public static void main(String args[]){
		src.Constants.EDITOR = true;
		new src.scenario.loader.ScenarioLoader(); // Read in scenario data
		TheEditor e = new TheEditor();
		e.run();
	}
}
