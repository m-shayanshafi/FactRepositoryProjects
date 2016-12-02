/* 
   This file is part of NitsLoch.
 	
   Copyright (C) 2007  Jonathan Irons

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

package src.file.map;

import java.util.ArrayList;

import src.file.level.LevelHeader;

/**
 * The header structure for a Map file. Also contains the LevelHeaders for all
 * Levels.
 */
public class MapHeader {

    /**
     * The length of all data in the MapHeader.
     */
    public static final byte DATA_LENGTH = 16;
    
    /**
     * The "Program" code for all NitsLoch file formats. In a big-endian file,
     * this is equivalent to ASCII 'NITS'.
     */
    public static final int PROGRAM = 0x4e495453;

    /**
     * The "Format" code for the Map format (as opposed to a Player file or
     * other future additions). In a big-endian file, this is equivalent to
     * ASCII 'LOCH'.
     */
    public static final int FORMAT = 0x4c4f4348;

    private int program_magic_number;
    private int format_magic_number;
    private int number_levels;
    private int data_length;
    private ArrayList<LevelHeader> level_headers;
    
    /**
     * Empty constructor; creates an ArrayList of LevelHeaders. Most situations
     * call for the program to set the member variables after creation.
     */
    public MapHeader() {
	level_headers = new ArrayList<LevelHeader>();
    }

    /**
     * Accessor for the file's "Program" code.
     * @return int : The integer form of the Program Code.
     */
    public int getProgram() {return program_magic_number;}

    /**
     * Mutator for the file's "Program" code.
     * @param p int : Change the code to int p.
     */
    public void setProgram(int p) {program_magic_number = p;}

    /**
     * Accessor for the file's Format Code.
     * @return int : The integer form of the Format Code.
     */
    public int getFormat() {return format_magic_number;}

    /**
     * Mutator for the file's Format Code.
     * @param f int : Change the code to int f.
     */
    public void setFormat(int f) {format_magic_number = f;}

    /**
     * Accessor for the number of levels in the file.
     * @return int : The number of levels in the file.
     */
    public int getNumberOfLevels() {return number_levels;}

    /**
     * Mutator for the number of levels in the file.
     * @param n int : Set the number of levels in the file to n.
     */
    public void setNumberOfLevels(int n) {number_levels = n;}

    /**
     * Accessor for the length of the file's data.
     * @return int : The length of the file's data, not counting the MapHeader,
     * in bytes.
     */
    public int getDataLength() {return data_length;}

    /**
     * Mutator for the length of the file's data.
     * @param l int : Set the length of the file's data, not counting the
     * MapHeader, to l bytes.
     */
    public void setDataLength(int l) {data_length = l;}

    /**
     * Retrieve a particular LevelHeader from the MapHeader.
     * @param i int : The level index i.
     * @return LevelHeader : The LevelHeader for level index i.
     */
    public LevelHeader getLevelHeader(int i) {
	return level_headers.get(i);
    }

    /**
     * Set a particular LevelHeader in the MapHeader.
     * @param i int : Set the LevelHeader for level index i.
     * @param h LevelHeader : The LevelHeader to set.
     * @return LevelHeader : The LevelHeader that was formerly at index i.
     */
    public LevelHeader setLevelHeader(int i, LevelHeader h) {
	LevelHeader l = getLevelHeader(i);
	level_headers.set(i, h);
	return l;
    }

    /**
     * Add a LevelHeader to the list in this MapHeader.
     * @param h LevelHeader : Add LevelHeader h to the list.
     */
    public void addLevelHeader(LevelHeader h) {
	level_headers.add(h);
    }
}
