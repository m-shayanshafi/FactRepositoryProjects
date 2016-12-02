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

package src.file.enemy;

import src.file.entry.Entry;
import src.file.map.MapFile;
import src.game.GameWorld;
import src.game.Enemy;
import src.enums.Enemies;

/**
 * The file format for an Enemy object.
 * @author Jonathan Irons
 * date 2/5/2008
 *
 */
public class EnemyEntry implements Entry {
   
    /**
     * The length of this entry's data in bytes
     */
    public static final byte DATA_LENGTH = 16;

    private int type;
    private int level;
    private int row, column;
	
    /**
     * Constructor for the file version of an enemy object.
     */
    public EnemyEntry() {
    }

    /**
     * Accessor for the enemy's type.
     * @return int : Enemy's type
     */
    public int getType() {return type;}

    /**
     * Mutator for the enemy's type. Types are based on enums.
     * @see src.enums.Enemies
     * @param t int : Change enemy to type t.
     */
    public void setType(int t) {type = t;}

    /**
     * Accessor for the enemy's level.
     * @return int : Enemy's level
     */
    public int getLevel(){return level;}

    /**
     * Mutator for the enemy's difficulty level.
     * @param l int : Change enemy's difficulty level to l.
     */
    public void setLevel(int l){level = l;}

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
	setLevel(mf.readInt());
	setRow(mf.readInt());
	setColumn(mf.readInt());
	Enemy e = new Enemy(Enemies.values()[type], getRow(),
			    getColumn(), getLevel());
	GameWorld.getInstance().getLandAt(getRow(), getColumn()).setEnemy(e);
    }

    /**
     * Writes an individual entry to the map file.
     */
    public void write() {
	MapFile mf = MapFile.getInstance();
	mf.writeInt(getType());
	mf.writeInt(getLevel());
	mf.writeInt(getRow());
	mf.writeInt(getColumn());
    }
}
