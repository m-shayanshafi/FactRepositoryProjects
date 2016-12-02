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

package src.file.exit;

import src.file.entry.Entry;
import src.file.map.MapFile;
import src.game.GameWorld;
import src.land.Exit;
import src.enums.ExitType;

/**
 * The file format for an Exit object.
 * @author Jonathan Irons
 * date 2/5/2008
 *
 */
public class ExitEntry implements Entry {

    /**
     * The length of this entry's data in bytes.
     */
    public static final byte DATA_LENGTH = 25;
    private int type;
    private int nextCity;
    private int destinationRow;
    private int destinationColumn;
    private byte isOpen;
    private int row, column;
	
    /**
     * Constructor for the file version of an Exit object.
     */
    public ExitEntry() {
    }

    /**
     * Accessor for Exit's type.
     * @return int : Exit type
     */
    public int getType() {return type;}

    /**
     * Mutator for Exit's type. Types are enums.
     * @see src.enums.ExitType
     * @param t int : Change type to to.
     */
    public void setType(int t) {type = t;}

    /**
     * Accessor for the destination city.
     * @return int : Destination city index.
     */
    public int getNextCity() {return nextCity;}

    /**
     * Mutator for destination city.
     * @param c int : Change destination city to index c.
     */
    public void setNextCity(int c) {nextCity = c;}

    /**
     * Accessor for the destination row.
     * @return int : Destination row.
     */
    public int getDestinationRow() {return destinationRow;}

    /**
     * Mutator for destination row.
     * @param r int : Change destination row to r.
     */
    public void setDestinationRow(int r) {destinationRow = r;}

    /**
     * Accessor for destination column.
     * @return int : Destination column.
     */
    public int getDestinationColumn() {return destinationColumn;}

    /**
     * Mutator for desination column.
     * @param c int : Change desination column to c.
     */
    public void setDestinationColumn(int c) {destinationColumn = c;}

    /**
     * Mutator to set both coordinates at once.
     * @param row int : Change desination row to row.
     * @param col int : Change destination column to col.
     */
    public void setDestination(int row, int col) {
	destinationRow = row;
	destinationColumn = col;
    }

    /**
     * Checks whether or not this Exit is open.
     * @return boolean : State of exit openness.
     */
    public boolean getIsOpen() {
	if (isOpen != 0)
	    return true;
	return false;
    }

    /**
     * Set whether or not this Exit is open.
     * @param o boolean : 'true' to open exit, 'false' to close it.
     */
    public void setIsOpen(boolean o) {
	isOpen = 0;
	if (o)
	    isOpen = 1;
    }

    /**
     * Accessor for the Exit's row.
     * @return int : the row where this Exit belongs.
     */
    public int getRow() { return row; }

    /**
     * Mutator for the Exit's row.
     * @param r int : change the row to r.
     */
    public void setRow(int r) { row = r; }

    /**
     * Accessor for the Exit's column.
     * @return int : the column where this Exit belongs.
     */
    public int getColumn() { return column; }

    /**
     * Mutator for the Exit's column.
     * @param c int : change the column to c.
     */
    public void setColumn(int c) { column = c; }

    /**
     * Loads (reads) an individual entry from the map file.
     */
    public void load() {
	MapFile mf = MapFile.getInstance();
	setType(mf.readInt());
	setNextCity(mf.readInt());
	setDestinationRow(mf.readInt());
	setDestinationColumn(mf.readInt());
	setIsOpen(mf.readByte() != 0);
	setRow(mf.readInt());
	setColumn(mf.readInt());
	Exit e = new Exit(ExitType.values()[type], nextCity, destinationRow,
			  destinationColumn, getIsOpen());
	GameWorld.getInstance().setLandAt(row, column, e);
    }

    /**
     * Writes an individual entry to the map file.
     */
    public void write() {
	MapFile mf = MapFile.getInstance();
	mf.writeInt(getType());
	mf.writeInt(getNextCity());
	mf.writeInt(getDestinationRow());
	mf.writeInt(getDestinationColumn());
	mf.writeByte(isOpen);
	mf.writeInt(row);
	mf.writeInt(column);
    }
}
