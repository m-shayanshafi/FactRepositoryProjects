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

package src.file.obstruction;

import src.Constants;
import src.file.chunk.Chunk;
import src.file.chunk.ChunkHeader;

import java.util.ArrayList;

/**
 * The file format for a collection of ObstructionEntries.
 * @author Jonathan Irons
 * date 2/5/2008
 *
 */
public class ObstructionChunk extends Chunk {
    /**
     * The ASCII sequence to identify an ObstructionChunk ('OBST').
     */
    public static final int TAG = 0x4f425354;

    private ArrayList<ObstructionEntry> entries =
	new ArrayList<ObstructionEntry>(0);
	
    /**
     * Constructor for the collection of ObstructionEntries.
     * @param h ChunkHeader : This Chunk's ChunkHeader
     */
    public ObstructionChunk(ChunkHeader h) {
	super(h);
    }

    /**
     * Accessor for a given ObstructionEntry.
     * @return ObstructionEntry : An individual ObstructionEntry from this chunk.
     */
    public ObstructionEntry getEntry(int i) {return this.entries.get(i);}

    /**
     * Set a given ObstructionEntry.
     * @param i int : Entry index.
     * @param e ObstructionEntry : This ObstructionEntry replaces whatever was at i.
     */
    public void setEntry(int i, ObstructionEntry e) {
	this.entries.set(i, e);
    }

    /**
     * Get number of entries present in this chunk.
     * @return int : The number of entries in the chunk.
     */
    public int numEntries(){return this.entries.size();}

    /**
     * Add an ObstructionEntry to the end of the list.
     * @param e ObstructionEntry : The ObstructionEntry that is pushed onto the ArrayList.
     */
    public void addEntry(ObstructionEntry e) {
	this.entries.add(e);
    }

    /**
     * Begin loading (reading) this chunk from the map file. Takes the
     * DATA_LENGTH of the corresponding Entry type and uses that to split the
     * file data into Entries.
     */
    public void load() {
	Constants.debugPrint("Loading EXIT");
	super.load();
	int numEntries =
	    this.header.getLength()/ObstructionEntry.DATA_LENGTH;
	for (int i=0; i<numEntries; i++) {
	    ObstructionEntry e = new ObstructionEntry();
	    e.load();
	    addEntry(e);
	    //	    Constants.debugPrint("\tObstruction "+i+": "+e);
	}
    }

    /**
     * Begin writing this chunk to the map file.
     */
    public void write() {
	super.write();
	for (int i=0; i<this.entries.size(); i++) {
	    this.entries.get(i).write();
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
	int l = numEntries() * ObstructionEntry.DATA_LENGTH;
	this.header.setLength(l);
	return l;
    }
}
