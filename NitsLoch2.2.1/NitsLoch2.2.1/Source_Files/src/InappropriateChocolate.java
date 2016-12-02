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

import java.util.ArrayList;

import src.enums.ObstructionLandType;
import src.file.map.MapFile;
import src.game.Enemy;
import src.game.GameWorld;
import src.game.Item;
import src.game.NPC;
import src.land.Exit;
import src.land.Land;
import src.land.Obstruction;
import src.land.Shop;
import src.land.Street;

public class InappropriateChocolate {

	public static void main(String[] args) {
		src.Constants.EDITOR = true;
		
		if(args.length != 4) {
			System.out.format("Usage: java -jar InappropriateChocolate.jar OPTION <arg1> <arg2> <arg2>" +
					"\nWhere OPTION can be -p or -c.\n\t" +
					"-p: Patch a file. <original map> <patch file> <map to create>\n\t" +
					"-c: Create a patch. <original map> <new map> <patch file to create>\n");
			System.exit(1);
		}
		
		if(args[0].equals("-c")) {
			createPatchFile(args[1], args[2], args[3]);
		}
		else if(args[0].equals("-p")) {
			patch(args[1], args[2], args[3]);
		}
	}
	
	private static void createPatchFile(String originalMap, String newMap, String patchMap) {
		try {
			GameWorld originalGW = GameWorld.getInstance().getTempInstance();
			GameWorld newGW = GameWorld.getInstance().getTempInstance();
			GameWorld patchGW = GameWorld.getInstance().getTempInstance();

			MapFile.getInstance().load(originalMap);
			originalGW.setCities(GameWorld.getInstance().getCities());
			originalGW.setCityNames(GameWorld.getInstance().getCityNames());
			GameWorld.setInstance(GameWorld.getInstance().getTempInstance());
			
			MapFile.getInstance().load(newMap);
			newGW.setCities(GameWorld.getInstance().getCities());
			newGW.setCityNames(GameWorld.getInstance().getCityNames());
			GameWorld.setInstance(GameWorld.getInstance().getTempInstance());
			
			validateWorlds(originalGW, newGW);
			
			clearPatchedWorld(patchGW, originalGW.getCities().get(0).length, originalGW.getCities().get(0)[0].length);
			
			updatePatchedWorld(originalGW, newGW, patchGW);
			
			GameWorld.setInstance(patchGW);
			MapFile.getInstance().save(patchMap);
			
		} catch(Exception ex) {
			System.out.println("Problem creating patch file: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	private static void patch(String originalMap, String patchMap, String newMap) {
		try {
			GameWorld patchGW = GameWorld.getInstance().getTempInstance();

			MapFile.getInstance().load(patchMap);
			patchGW.setCities(GameWorld.getInstance().getCities());
			patchGW.setCityNames(GameWorld.getInstance().getCityNames());
			GameWorld.setInstance(GameWorld.getInstance().getTempInstance());

			MapFile.getInstance().load(originalMap);

			validateWorlds(GameWorld.getInstance(), patchGW);

			applyPatch(patchGW);
			
			MapFile.getInstance().save(newMap);
		} catch(Exception ex) {
			System.out.println("Problem applying patch file: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	private static void applyPatch(GameWorld patchGW) {
		Land[][] originalLand = GameWorld.getInstance().getCities().get(0);
		Land[][] patchLand = patchGW.getCities().get(0);
		
		for(int row = 0; row < originalLand.length; row++) {
			for(int col = 0; col < originalLand[0].length; col++) {
				if(patchLand[row][col] instanceof Obstruction && 
						((Obstruction)patchLand[row][col]).getType() == ObstructionLandType.OBS_INVALID)
					continue;
				else
					originalLand[row][col] = patchLand[row][col];
			}
		}
	}
	
	private static void validateWorlds(GameWorld originalGW, GameWorld newGW) throws Exception {
		if(originalGW.getCities().get(0).length != newGW.getCities().get(0).length ||
				originalGW.getCities().get(0)[0].length != newGW.getCities().get(0)[0].length)
			throw new Exception ("World sizes do not match.");
	}
	
	private static void clearPatchedWorld(GameWorld patchGW, int rows, int cols) {
		Land[][] city = new Land[rows][cols];
		for(int row = 0; row < city.length; row++) {
			for(int col = 0; col < city[0].length; col++) {
				city[row][col] = new Obstruction(ObstructionLandType.OBS_INVALID, false);
			}
		}
		ArrayList<Land[][]> cities = new ArrayList<Land[][]>();
		cities.add(city);
		patchGW.setCities(cities);
	}
	
	private static void updatePatchedWorld(GameWorld originalGW, GameWorld newGW, GameWorld patchGW) {
		Land[][] originalLand = originalGW.getCities().get(0);
		Land[][] newLand = newGW.getCities().get(0);
		Land[][] patchLand = patchGW.getCities().get(0);
		
		for(int row = 0; row < originalLand.length; row++) {
			for(int col = 0; col < originalLand[0].length; col++) {
				if(different(originalLand[row][col], newLand[row][col])) {
					patchLand[row][col] = newLand[row][col];
				}
			}
		}
		
		ArrayList<Land[][]> patchedCities = new ArrayList<Land[][]>();
		patchedCities.add(patchLand);
		patchGW.setCities(patchedCities);
		patchGW.setCityNames(originalGW.getCityNames());
	}
	
	private static boolean different(Land land1, Land land2) {
		if(land1 instanceof Obstruction && !(land2 instanceof Obstruction))
			return true;
		if(land1 instanceof Exit && !(land2 instanceof Exit))
			return true;
		if(land1 instanceof Shop && !(land2 instanceof Shop))
			return true;
		if(land1 instanceof Street && !(land2 instanceof Street))
			return true;
		
		if(land1 instanceof Obstruction) {
			Obstruction obs1 = (Obstruction)land1;
			Obstruction obs2 = (Obstruction)land2;
			
			if(obs1.getType() != obs2.getType() || obs1.isDestroyable() != obs2.isDestroyable())
				return true;
		}
		
		if(land1 instanceof Exit) {
			Exit ex1 = (Exit)land1;
			Exit ex2 = (Exit)land2;
			
			if(ex1.getType() != ex2.getType() || ex1.getIsOpen() != ex2.getIsOpen() ||
					ex1.getDestinationCol() != ex2.getDestinationCol() ||
					ex1.getDestinationRow() != ex2.getDestinationRow() ||
					ex1.getNextCity() != ex2.getNextCity())
				return true;
		}
		
		if(land1 instanceof Shop) {
			Shop shop1 = (Shop)land1;
			Shop shop2 = (Shop)land2;
			
			if(shop1.getType() != shop2.getType() || shop1.getPermutation() != shop2.getPermutation())
				return true;
		}
		
		if(land1 instanceof Street) {
			Street st1 = (Street)land1;
			Street st2 = (Street)land2;
			
			if(st1.getType() != st2.getType())
				return true;
			
			if(!objectsSame(st1, st2))
				return true;
		}
		
		return false;
	}
	
	private static boolean objectsSame(Street st1, Street st2) {
		Enemy enemy1 = st1.getEnemy();
		Enemy enemy2 = st2.getEnemy();
		
		Item item1 = st1.getItem();
		Item item2 = st2.getItem();
		
		NPC npc1 = st1.getNPC();
		NPC npc2 = st2.getNPC();
		
		if(enemy1 != null && enemy2 == null || enemy1 == null && enemy2 != null)
			return false;
		
		if(item1 != null && item2 == null || item1 == null && item2 != null)
			return false;
		
		if(npc1 != null && npc2 == null || npc1 == null && npc2 != null)
			return false;
		
		if(enemy1 != null) {
			if(enemy1.getType() != enemy2.getType() || enemy1.getAdvanced() != enemy2.getAdvanced())
				return false;
		}
		
		if(item1 != null) {
			if(item1.getType() != item2.getType() || item1.getData() != item2.getData())
				return false;
		}
		
		if(npc1 != null) {
			if(npc1.getType() != npc2.getType())
				return false;
		}
		
		if(st1.getPlayerIndex() != st1.getPlayerIndex())
			return false;
		
		return true;
	}
}
