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

package src.game;

import src.enums.DungeonWalls;
import src.enums.Facing;
import src.enums.ObstructionLandType;
import src.land.Land;
import src.land.Obstruction;

/**
 * Class to communicate with the GameWorld to see which land objects
 * are within view of the player while he is in a dungeon.
 * @author Darren Watts
 * date 12/15/07
 */
public class DungeonView {

	/**
	 * Get the visible area in view.  It will assign land objects to a land array
	 * that holds what is in view.
	 * @return Land[] : land in view
	 */
	public static Land[] getVisibleArea(){
		int plrRow = GameWorld.getInstance().getLocalPlayer().getRow();
		int plrCol = GameWorld.getInstance().getLocalPlayer().getCol();
		Facing face = GameWorld.getInstance().getLocalPlayer().getFacing();
		Land[] land = new Land[12];

		if(face == Facing.UP){
			land[DungeonWalls.FRONT_WALL4.getType()] = getLand(plrRow-4, plrCol);
			land[DungeonWalls.FRONT_WALL3.getType()] = getLand(plrRow-3, plrCol);
			land[DungeonWalls.FRONT_WALL2.getType()] = getLand(plrRow-2, plrCol);
			land[DungeonWalls.FRONT_WALL.getType()] = getLand(plrRow-1, plrCol);

			land[DungeonWalls.LEFT_WALL4.getType()] = getLand(plrRow-3, plrCol-1);
			land[DungeonWalls.LEFT_WALL3.getType()] = getLand(plrRow-2, plrCol-1);
			land[DungeonWalls.LEFT_WALL2.getType()] = getLand(plrRow-1, plrCol-1);
			land[DungeonWalls.LEFT_WALL.getType()] = getLand(plrRow, plrCol-1);

			land[DungeonWalls.RIGHT_WALL4.getType()] = getLand(plrRow-3, plrCol+1);
			land[DungeonWalls.RIGHT_WALL3.getType()] = getLand(plrRow-2, plrCol+1);
			land[DungeonWalls.RIGHT_WALL2.getType()] = getLand(plrRow-1, plrCol+1);
			land[DungeonWalls.RIGHT_WALL.getType()] = getLand(plrRow, plrCol+1);
		}
		else if(face == Facing.RIGHT){
			land[DungeonWalls.FRONT_WALL4.getType()] = getLand(plrRow, plrCol+4);
			land[DungeonWalls.FRONT_WALL3.getType()] = getLand(plrRow, plrCol+3);
			land[DungeonWalls.FRONT_WALL2.getType()] = getLand(plrRow, plrCol+2);
			land[DungeonWalls.FRONT_WALL.getType()] = getLand(plrRow, plrCol+1);

			land[DungeonWalls.LEFT_WALL4.getType()] = getLand(plrRow-1, plrCol+3);
			land[DungeonWalls.LEFT_WALL3.getType()] = getLand(plrRow-1, plrCol+2);
			land[DungeonWalls.LEFT_WALL2.getType()] = getLand(plrRow-1, plrCol+1);
			land[DungeonWalls.LEFT_WALL.getType()] = getLand(plrRow-1, plrCol);

			land[DungeonWalls.RIGHT_WALL4.getType()] = getLand(plrRow+1, plrCol+3);
			land[DungeonWalls.RIGHT_WALL3.getType()] = getLand(plrRow+1, plrCol+2);
			land[DungeonWalls.RIGHT_WALL2.getType()] = getLand(plrRow+1, plrCol+1);
			land[DungeonWalls.RIGHT_WALL.getType()] = getLand(plrRow+1, plrCol);
		}
		else if(face == Facing.DOWN){
			land[DungeonWalls.FRONT_WALL4.getType()] = getLand(plrRow+4, plrCol);
			land[DungeonWalls.FRONT_WALL3.getType()] = getLand(plrRow+3, plrCol);
			land[DungeonWalls.FRONT_WALL2.getType()] = getLand(plrRow+2, plrCol);
			land[DungeonWalls.FRONT_WALL.getType()] = getLand(plrRow+1, plrCol);

			land[DungeonWalls.LEFT_WALL4.getType()] = getLand(plrRow+3, plrCol+1);
			land[DungeonWalls.LEFT_WALL3.getType()] = getLand(plrRow+2, plrCol+1);
			land[DungeonWalls.LEFT_WALL2.getType()] = getLand(plrRow+1, plrCol+1);
			land[DungeonWalls.LEFT_WALL.getType()] = getLand(plrRow, plrCol+1);

			land[DungeonWalls.RIGHT_WALL4.getType()] = getLand(plrRow+3, plrCol-1);
			land[DungeonWalls.RIGHT_WALL3.getType()] = getLand(plrRow+2, plrCol-1);
			land[DungeonWalls.RIGHT_WALL2.getType()] = getLand(plrRow+1, plrCol-1);
			land[DungeonWalls.RIGHT_WALL.getType()] = getLand(plrRow, plrCol-1);
		}
		else{
			land[DungeonWalls.FRONT_WALL4.getType()] = getLand(plrRow, plrCol-4);
			land[DungeonWalls.FRONT_WALL3.getType()] = getLand(plrRow, plrCol-3);
			land[DungeonWalls.FRONT_WALL2.getType()] = getLand(plrRow, plrCol-2);
			land[DungeonWalls.FRONT_WALL.getType()] = getLand(plrRow, plrCol-1);

			land[DungeonWalls.LEFT_WALL4.getType()] = getLand(plrRow+1, plrCol-3);
			land[DungeonWalls.LEFT_WALL3.getType()] = getLand(plrRow+1, plrCol-2);
			land[DungeonWalls.LEFT_WALL2.getType()] = getLand(plrRow+1, plrCol-1);
			land[DungeonWalls.LEFT_WALL.getType()] = getLand(plrRow+1, plrCol);

			land[DungeonWalls.RIGHT_WALL4.getType()] = getLand(plrRow-1, plrCol-3);
			land[DungeonWalls.RIGHT_WALL3.getType()] = getLand(plrRow-1, plrCol-2);
			land[DungeonWalls.RIGHT_WALL2.getType()] = getLand(plrRow-1, plrCol-1);
			land[DungeonWalls.RIGHT_WALL.getType()] = getLand(plrRow-1, plrCol);
		}
		return land;
	}

	/**
	 * Gets a land object at the specified row and column
	 * @param row int : row
	 * @param col int : column
	 * @return Land : land object
	 */
	private static Land getLand(int row, int col){
		Land wall = new Obstruction(ObstructionLandType.OBS_001, false);
		Land[][] wholeLand = GameWorld.getInstance().getCities().get(
				GameWorld.getInstance().getCurrentLevel());
		try {
			return wholeLand[row][col];
		} catch(Exception ex){
			return wall;
		}
	}
}
