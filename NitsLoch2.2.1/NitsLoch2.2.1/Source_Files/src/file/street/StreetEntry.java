/* 
 	This file is part of NitsLoch.
 	
 	Copyright (C) 2007--2008  Jonathan Irons

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package src.file.street;

import src.file.entry.Entry;
import src.file.map.MapFile;
import src.game.GameWorld;
import src.land.Street;
import src.enums.StreetType;

/**
 * The file format for a Street object.
 * @author Jonathan Irons
 * date 2/5/2008
 *
 */
public class StreetEntry implements Entry {
   
    /**
     * The length of this entry's data in bytes
     */
    public static final byte DATA_LENGTH = 16;
    private int type;
    private int playerIndex;
    private int row, column;
	
    /**
     * Constructor for the file version of an enemy object.
     */
    public StreetEntry(){playerIndex = -1;}

    /**
     * Accessor for the street's type.
     * @return int : Street type.
     */
    public int getType(){return type;}

    /**
     * Mutator for the street's type. All types are enums.
     * @see src.enums.StreetType
     * @param t int : Change street type to t.
     */
    public void setType(int t){type = t;}

    /**
     * Accessor for contained player index.
     * @return int : The index of the contained player. -1 if no player is in
     * street.
     */
    public int getPlayerIndex() {return playerIndex;}

    /**
     * Mutator for contained player index.
     * @param p int : Change contained player index to p. Set to -1 for "none."
     */
    public void setPlayerIndex(int p) {playerIndex = p;}

    /**
     * Accessor for the enemy's row.
     * @return int : Enemy's row
     */
    public int getRow(){return row;}

    /**
     * Mutator for the enemy's row.
     * @param r int : Change enemy's row to r.
     */
    public void setRow(int r){row = r;}

    /**
     * Accessor for the enemy's column.
     * @return int : Enemy's column
     */
    public int getColumn(){return column;}

    /**
     * Mutator for the enemy's column.
     * @param c int : Change enemy's column to c.
     */
    public void setColumn(int c){column = c;}

    /**
     * Mutator to set both coordinates at once.
     * @param row int : Change row to row.
     * @param col int : Change column to col.
     */
    public void setPosition(int row, int col) {
	this.row = row;
	column = col;
    }

    /**
     * Loads (reads) an individual entry from the map file.
     */
    public void load() {
	MapFile mf = MapFile.getInstance();
	setType(mf.readInt());
	setPlayerIndex(mf.readInt());
	setRow(mf.readInt());
	setColumn(mf.readInt());
	Street s = new Street(StreetType.values()[type],
			      playerIndex, null, null, null);
	GameWorld.getInstance().setLandAt(row, column, s);
    }

    /**
     * Writes an individual entry to the map file.
     */
    public void write() {
	MapFile mf = MapFile.getInstance();
	mf.writeInt(getType());
	mf.writeInt(getPlayerIndex());
	mf.writeInt(getRow());
	mf.writeInt(getColumn());
    }
}
