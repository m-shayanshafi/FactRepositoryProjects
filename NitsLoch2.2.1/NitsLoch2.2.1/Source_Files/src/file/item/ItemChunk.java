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

import java.util.ArrayList;

import src.Constants;
import src.file.chunk.Chunk;
import src.file.chunk.ChunkHeader;


/**
 * The file format for a collection of ItemEntries.
 * @author Jonathan Irons
 * date 2/5/2008
 *
 */
public class ItemChunk extends Chunk {
   
    /**
     * The ASCII sequence to identify an ItemChunk ('ITEM').
     */
    public static final int TAG = 0x4954454d;

    private ArrayList<ItemEntry> entries =
	new ArrayList<ItemEntry>();

	
    /**
     * Constructor for a collection of ItemEntries.
     * @param h ChunkHeader : This Chunk's ChunkHeader
     */
    public ItemChunk(ChunkHeader h) {
	super(h);
    }

    /**
     * Accessor for a given ItemEntry.
     * @return ItemEntry : An individual ItemEntry from this chunk.
     */
    public ItemEntry getEntry(int i) {return entries.get(i);}

    /**
     * Set a given ItemEntry.
     * @param i int : ItemEntry index
     * @param e ItemEntry : Replace present ItemEntry in list with e.
     */
    public void setEntry(int i, ItemEntry e) {
	entries.set(i, e);
    }

    /**
     * Add an ItemEntry to the end of the list.
     * @param e ItemEntry : The ItemEntry that is pushed onto the ArrayList.
     */
    public void addEntry(ItemEntry e) {
	entries.add(e);
    }

    /**
     * Get number of entries present in this chunk.
     * @return int : The number of entries in the chunk.
     */
    public int numEntries(){return entries.size();}

    /**
     * Begin loading (reading) this chunk from the map file. Takes the
     * DATA_LENGTH of the corresponding Entry type and uses that to split the
     * file data into Entries.
     */
    public void load() {
	super.load();
	Constants.debugPrint("Loading ITEM");
	int numEntries =
	    header.getLength() / ItemEntry.DATA_LENGTH;
	for (int i=0; i<numEntries; i++) {
	    ItemEntry ie = new ItemEntry();
	    ie.load();
	    addEntry(ie);
	    //	    Constants.debugPrint("\tItem "+i+": "+ie);
	}
    }

    /**
     * Begin writing this chunk to the map file.
     */
    public void write() {
	super.write();
	for (int i=0; i<entries.size(); i++) {
	    entries.get(i).write();
	}
    }

    /**
     * Calculates Chunk's length for the benefit of all the file's various
     * headers, which are heavily dependent on specified lengths. Usually called
     * recursively, starting from the Level and proceeding through the Level's
     * Chunks, just before the File is written.
     */
    public int calcLength() {
	super.calcLength();
	int l = numEntries() * ItemEntry.DATA_LENGTH;
	header.setLength(l);
	return l;
    }
}
