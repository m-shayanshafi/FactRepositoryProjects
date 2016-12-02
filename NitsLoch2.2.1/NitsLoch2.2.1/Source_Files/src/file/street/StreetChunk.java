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

import java.util.ArrayList;

import src.Constants;
import src.file.chunk.Chunk;
import src.file.chunk.ChunkHeader;


/**
 * The file format for a collection of StreetEntries.
 * @author Jonathan Irons
 * date 2/5/2008
 *
 */
public class StreetChunk extends Chunk {
   
    /**
     * The ASCII sequence to identify an StreetChunk ('STRT').
     */
    public static final int TAG = 0x53545254;
    private ArrayList<StreetEntry> entries =
	new ArrayList<StreetEntry>();
	
    /**
     * Constructor for the collection of StreetEntries.
     * @param h ChunkHeader : This Chunk's ChunkHeader
     */
    public StreetChunk(ChunkHeader h) {
	super(h);
    }

    /**
     * Accessor for a given StreetEntry.
     * @return StreetEntry : An individual StreetEntry from this chunk.
     */
    public StreetEntry getEntry(int i) {return this.entries.get(i);}

    /**
     * Set a given StreetEntry.
     * @param i int : Entry index.
     * @param e StreetEntry : This StreetEntry replaces whatever was at i.
     */
    public void setEntry(int i, StreetEntry e) {
	this.entries.set(i, e);
    }

    /**
     * Add an StreetEntry to the end of the list.
     * @param e StreetEntry : The StreetEntry that is pushed onto the ArrayList.
     */
    public void addEntry(StreetEntry e) {
	this.entries.add(e);
    }

    /**
     * Get number of entries present in this chunk.
     * @return int : The number of entries in the chunk.
     */
    public int numEntries() {return this.entries.size();}

    /**
     * Begin loading (reading) this chunk from the map file. Takes the
     * DATA_LENGTH of the corresponding Entry type and uses that to split the
     * file data into Entries.
     */
    public void load() {
	Constants.debugPrint("Loading STRT");
	super.load();
	int numEntries =
	    this.header.getLength()/StreetEntry.DATA_LENGTH;
	for (int i=0; i<numEntries; i++) {
	    StreetEntry s = new StreetEntry();
	    s.load();
	    addEntry(s);
	    //	    Constants.debugPrint("\tStreet #"+i+" : "+s);
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
	int l = numEntries() * StreetEntry.DATA_LENGTH;
	this.header.setLength(l);
	return l;
    }
}
