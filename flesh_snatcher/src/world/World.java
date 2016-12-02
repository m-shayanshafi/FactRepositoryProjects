//	Copyright 2009 Nicolas Devere
//
//	This file is part of FLESH SNATCHER.
//
//	FLESH SNATCHER is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
//
//	FLESH SNATCHER is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//
//	You should have received a copy of the GNU General Public License
//	along with FLESH SNATCHER; if not, write to the Free Software
//	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

package world;

import java.io.BufferedReader;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Observer;
import input.LoadHelper;


/**
 * Static game world class. It stores the map files list, the current Map object, 
 * and provides the levels sequence management.
 * 
 * @author Nicolas Devere
 *
 */
public final class World {
	
	private static Vector nameList;
	private static Vector picList;
	private static Vector lineList;
	private static Vector levelList;
	private static int numLevel;
	
	/**
	 * Ingame display mode.
	 */
	public static short INGAME = 0;
	
	/**
	 * Kinematic display mode.
	 */
	public static short KINEMATIC = 1;
	
	/**
	 * Current display mode.
	 */
	public static short mode = INGAME;
	
	/**
	 * Current map object.
	 */
	public static Map map = null;
	
	/**
	 * Current Kinematic object.
	 */
	public static Kinematic kinematic = null;
	
	
	
	/**
	 * Loads and stores the maps sequence.
	 * 
	 * @param codebase : the server base URL
	 * @param file : the maps summary file
	 * @throws Exception
	 */
	public static void loadMaps(String index) throws Exception {
		
		try {
			nameList = new Vector();
			picList = new Vector();
			lineList = new Vector();
			levelList = new Vector();
			numLevel = 0;
			
			BufferedReader br = LoadHelper.getBufferedReader(index);
			
			// Load maps
			int nbMaps = Integer.parseInt(br.readLine());
			for (int i=0; i<nbMaps; i++) {
				StringTokenizer st = new StringTokenizer(br.readLine());
				nameList.add(st.nextToken());
				picList.add(st.nextToken());
				lineList.add(new Integer(Integer.parseInt(st.nextToken())));
				levelList.add(st.nextToken());
			}
			
			br.close();
		}
		catch (Exception ex) {
			throw ex;
		}
	}
	
	
	/**
	 * Returns the maps names list.
	 * 
	 * @return the maps names list
	 */
	public static Vector getNameList() {
		return nameList;
	}
	
	
	/**
	 * Choose a map to load with its index.
	 * 
	 * @param mapIndex
	 */
	public static void setCurrentMap(int mapIndex) {
		
		if (mapIndex>=0 && mapIndex<levelList.size())
			numLevel = mapIndex;
			
	}
	
	
	/**
	 * Returns the current map index.
	 * @return the current map index (or -1 if no map is loaded)
	 */
	public static int getCurrentMap() {
		return numLevel - 1;
	}
	
	
	public static String getPic() {
		return (String)picList.get(numLevel);
	}
	
	public static int getLines() {
		return ((Integer)lineList.get(numLevel)).intValue();
	}
	
	public static String getName() {
		return (String)nameList.get(numLevel);
	}
	
	public static boolean isFinished() {
		return numLevel >= levelList.size();
	}
	
	
	/**
	 * Loads the next map in the maps summary. Returns true if a next map is loaded, 
	 * false otherwise (end of the game).
	 * 
	 * @return if a next map is loaded, or if it's end of the game
	 * @throws Exception
	 */
	public static boolean loadNext(Observer obs) throws Exception {
		
		try {
			
			map = null;
			
			if (numLevel<levelList.size()) {
				
				MapLoader loader = new MapLoader();
				loader.addObserver(obs);
				loader.loadMap((String)levelList.get(numLevel));
				loader.deleteObservers();
				
				numLevel++;
				return true;
			}
			return false;
		}
		catch (Exception ex) {
				throw ex;
		}
	}
	
	
	/**
	 * deletes current map, current music, and maps summary.
	 */
	public static void clear() {
		if (map!=null) map.clear();
		map = null;
		kinematic = null;
		mode = INGAME;
		numLevel = 0;
	}
	
	
}
