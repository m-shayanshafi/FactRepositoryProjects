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

package src.file.item;

import src.file.entry.Entry;
import src.file.map.MapFile;
import src.game.GameWorld;
import src.game.Item;
import src.enums.GroundItems;

/**
 * The file format for a GroundItem object.
 * @author Jonathan Irons
 * date 2/5/2008
 *
 */
public class ItemEntry implements Entry {
   
    /**
     * The length of this entry's data in bytes
     */
    public static final byte DATA_LENGTH = 16;
    private int type;
    private int data;
    private int row, column;
	
    /**
     * Constructor for the file version of an Item object.
     */
    public ItemEntry(){}

    /**
     * Accessor for the Item's type.
     * @return int : Item's type
     */
    public int getType() {return type;}

    /**
     * Mutator for the Item's type. Types are based on enums.
     * @see src.enums.GroundItems
     * @param t int : Change Item's type to t.
     */
    public void setType(int t) {type = t;}

    /**
     * Accessor for the Item's data.
     * @return int : Item's data
     */
    public int getData() {return data;}

    /**
     * Mutator for the Item's data. Data is usually the amount of money in a
     * briefcase or time left in an explosive; see src.game.GroundItems.
     * @param d int : Change the item's data to d.
     */
    public void setData(int d) {data = d;}

    /**
     * Accessor for the Item's row.
     * @return int : Item's row
     */
    public int getRow() {return row;}

    /**
     * Mutator for the Item's row.
     * @param r int : Change the item's row to r.
     */
    public void setRow(int r) {row = r;}

    /**
     * Accessor for the Item's column.
     * @return int : Item's column
     */
    public int getColumn() {return column;}

    /**
     * Mutator for the Item's column.
     * @param c int : Change the item's column to c.
     */
    public void setColumn(int c) {column = c;}

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
	setData(mf.readInt());
	setRow(mf.readInt());
	setColumn(mf.readInt());
	Item i = new Item(GroundItems.values()[type], data);
	GameWorld.getInstance().getLandAt(row, column).setItem(i);
    }

    /**
     * Writes an individual entry to the map file.
     */
    public void write() {
	MapFile mf = MapFile.getInstance();
	mf.writeInt(getType());
	mf.writeInt(getData());
	mf.writeInt(getRow());
	mf.writeInt(getColumn());
    }
}
