/*	
	This file is part of NitsLoch.

	Copyright (C) 2007-2008 Darren Watts

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

import java.io.File;
import java.util.Scanner;

import src.game.GameWorld;
import src.game.SoundThread;
import src.gui.SplashFrame;

/**
 * Main class for the game.  Calls a new CreatePlayerFrame.
 * @author Darren Watts
 * date 11/10/07
 *
 */
public class NitsLoch {

	/**
	 * Main program.  Prints the GPL message and then calls the CreatePlayerFrame.
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length > 0){
			if(args[0].equals("-debug")) src.Constants.SCENARIO_DEBUG = true;
		}
		String GPLmessage = 
			"NitsLoch v" + Constants.getVersion() + " Copyright (C) 2007-2008 Darren Watts and Jonathan Irons\n" +
			"This program comes with ABSOLUTELY NO WARRANTY;\n" +
			"This is free software, and you are welcome to redistribute it\n" +
			"under certain conditions; see the included COPYING.txt in the docs directory.\n";
		System.out.println(GPLmessage);
		
		try {
			Scanner scan = new Scanner(new File("ScenarioFile"));
			String scenarioFile = scan.nextLine();
			src.scenario.MiscScenarioData.SCENARIO_FILE = scenarioFile;
		} catch(Exception ex) {
			System.err.println("A file pointing to the current scenario is missing.\n" +
					"This should be named ScenarioFile and should have a line of text\n" +
					"with the path to your scenario .nits file.");
		}
		
		SplashFrame splash = new SplashFrame();
		new src.scenario.loader.ScenarioLoader(); // Read in scenario data
		GameWorld.getInstance().loadMapFromFile();
		splash.dispose();
		new src.gui.CreatePlayerFrame();
		SoundThread.getInstance().run(); // Start the sound thread
	}

}
