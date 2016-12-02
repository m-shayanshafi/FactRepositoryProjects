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
    GNU General Public License for more etails.

   You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package src.file.obstruction;

import src.file.entry.Entry;
import src.file.map.MapFile;
import src.game.GameWorld;
import src.land.Obstruction;
import src.enums.ObstructionLandType;


/**
 * The file format for an Obstruction object.
 * @author Jonathan Irons
 * date 2/5/2008
 *
 */
public class ObstructionEntry implements Entry {

    public static final byte DATA_LENGTH = 13;

    int type;
    int row, column;
    byte canBeDestroyed;
	
    /**
     * Constructor for the file version of an Item object.
     */
    public ObstructionEntry(){}

    /**
     * Accessor for Obstruction's type.
     * @return int : Obstruction type
     */
    public int getType() {return type;}

    /**
     * Mutator for Obstruction's type. Types are enums.
     * @see src.enums.ObstructionLandType
     * @param t int : Change type to to.
     */
    public void setType(int t) {type = t;}

    /**
     * Accessor for the row.
     * @return int : The Obstruction's row.
     */
    public int getRow() {return row;}

    /**
     * Mutator for destination row.
     * @param r int : Change row to r.
     */
    public void setRow(int r) {row = r;}

    /**
     * Accessor for column.
     * @return int : Column.
     */
    public int getColumn() {return column;}

    /**
     * Mutator for column.
     * @param c int : Change column to c.
     */
    public void setColumn(int c) {column = c;}

    /**
     * Mutator to set both coordinates at once.
     * @param row int : Change row to row.
     * @param col int : Change column to col.
     */
    public void setCoordinates(int row, int col) {
	this.row = row;
	column = col;
    }

    /**
     * Checks whether or not this Obstruction is open.
     * @return boolean : State of exit openness.
     */
    public boolean getCanBeDestroyed() {
	if (canBeDestroyed != 0)
	    return true;
	return false;
    }

    /**
     * Set whether or not this Obstruction is open.
     * @param o boolean : 'true' to open exit, 'false' to close it.
     */
    public void setCanBeDestroyed(boolean o) {
	canBeDestroyed = 0;
	if (o)
	    canBeDestroyed = 1;
    }

    /**
     * Loads (reads) an individual entry from the map file.
     */
    public void load() {
	MapFile mf = MapFile.getInstance();
	setType(mf.readInt());
	setRow(mf.readInt());
	setColumn(mf.readInt());
	setCanBeDestroyed(mf.readByte() != 0);
	Obstruction o =
	    new Obstruction(ObstructionLandType.values()[type],
			    getCanBeDestroyed());
	GameWorld.getInstance().setLandAt(row, column, o);
    }

    /**
     * Writes an individual entry to the map file.
     */
    public void write() {
	MapFile mf = MapFile.getInstance();
	mf.writeInt(getType());
	mf.writeInt(getRow());
	mf.writeInt(getColumn());
	mf.writeByte(canBeDestroyed);
    }
}
