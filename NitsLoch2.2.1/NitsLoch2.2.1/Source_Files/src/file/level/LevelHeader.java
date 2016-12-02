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

package src.file.level;

import src.file.map.*;

/**
 * A class to store all Level-related data in a header.
 * @author Jonathan Irons
 * date 2/6/2008
 */
public class LevelHeader {

    /**
     * The length of all data in the level header.
     */
    public static final byte DATA_LENGTH = 64;

    /**
     * The maximum length of a level's name.
     */
    public static final byte MAXIMUM_NAME_LENGTH = 48;

    private int offset;
    private int length;
    private int numRows, numColumns;
    private char[] name = new char[MAXIMUM_NAME_LENGTH];

    /**
     * Empty constructor.
     */
    public LevelHeader(){}

    /**
     * Accessor for the Level's offset.
     * @return int : Offset from the beginning of the file.
     */
    public int getOffset() {return offset;}

    /**
     * Mutator for the Level's offset.
     * @param o int : Change level offset to o.
     */
    public void setOffset(int o) {offset = o;}

    /**
     * Accessor for Level length.
     * @return int : Length of level in bytes.
     */
    public int getLength() {return length;}

    /**
     * Mutator for Level length.
     * @param l int : Change level length to l.
     */
    public void setLength(int l) {length = l;}

    /**
     * Accessor for number of rows in Level.
     * @return int : Number of rows in Level, measured
     * in "tiles" (one unit of Land).
     */
    public int getNumRows(){return numRows;}

    /**
     * Accessor for number of columns in Level.
     * @return int : Number of columns in Level, measured
     * in "tiles" (one unit of Land).
     */
    public int getNumColumns(){return numColumns;}

    /**
     * Mutator for both dimensions.
     * @param rows int : Change number of rows (measured in Land units).
     * @param cols int : Change number of columns.
     */
    public void setDimensions(int rows, int cols) {
	numRows = rows;
	numColumns = cols;
    }

    /**
     * Accessor for Level name.
     * @return String : Level name, as represented by a String.
     */
    public String getName() {
	String s  = "";
	for (int i=0; i<name.length; i++) {
	    if (name[i] != 0)
		s+=name[i];
	}
	return s;
    }

    /**
     * Mutator for Level name.
     * @see #MAXIMUM_NAME_LENGTH
     * @param n char[] : Change level name to an array of chars, n. Anything
     * longer than MAXIMUM_NAME_LENGTH will be truncated.
     */
    public void setName(char[] n) {name = n;}

    /**
     * Mutator for Level name.
     * @see #MAXIMUM_NAME_LENGTH
     * @param s String : Change level name to a String, s. Anything longer than
     * MAXIMUM_NAME_LENGTH will be truncated.
     */
    public void setName(String s) {
	name = new char[MAXIMUM_NAME_LENGTH];
	char[] oldString = s.toCharArray();
	if (oldString.length > name.length) {
	    for (int i=0; i<name.length; i++) {
		name[i] = oldString[i];
	    }
	}else{
	    for (int i=0; i<oldString.length; i++) {
		name[i] = oldString[i];
	    }
	}
    }

    /**
     * Converts the Level's name from chars into bytes.
     * @return byte[] : The byte array resulting from the conversion of the
     * name's char array.
     */
    public byte[] nameToByte() {
        byte[] b = new byte[name.length];
	for (int i=0; i< name.length; i++) {
	    b[i] = (byte) name[i];
	}
	return b;
    }

    /**
     * Writes the LevelHeader to a file.
     */
    public void write() {
	MapFile mf = MapFile.getInstance();
	mf.writeInt(getOffset());
	mf.writeInt(getLength());
	mf.writeInt(getNumRows());
	mf.writeInt(getNumColumns());
	mf.write(nameToByte());
    }	
}
