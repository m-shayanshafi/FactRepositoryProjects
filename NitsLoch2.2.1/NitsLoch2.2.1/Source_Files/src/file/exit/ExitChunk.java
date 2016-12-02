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

import java.util.ArrayList;

import src.Constants;
import src.file.chunk.Chunk;
import src.file.chunk.ChunkHeader;

/**
 * The file format for a collection of ExitEntries.
 * @author Jonathan Irons
 * date 2/5/2008
 *
 */
public class ExitChunk extends Chunk {
   
    /**
     * The ASCII sequence to identify an ExitChunk ('EXIT').
     */
    public static final int TAG = 0x45584954;

    private ArrayList<ExitEntry> entries =
	new ArrayList<ExitEntry>();
	
    /**
     * Constructor for the collection of ExitEntries.
     * @param h ChunkHeader : This Chunk's ChunkHeader
     */
    public ExitChunk(ChunkHeader h) {
	super(h);
    }

    /**
     * Accessor for a given ExitEntry.
     * @return ExitEntry : An individual ExitEntry from this chunk.
     */
    public ExitEntry getEntry(int i) {return this.entries.get(i);}

    /**
     * Set a given ExitEntry.
     * @param i int : Entry index.
     * @param e ExitEntry : This ExitEntry replaces whatever was at i.
     */
    public void setEntry(int i, ExitEntry e) {
	this.entries.set(i, e);
    }

    /**
     * Get number of entries present in this chunk.
     * @return int : The number of entries in the chunk.
     */
    public int numEntries(){return this.entries.size();}

    /**
     * Add an ExitEntry to the end of the list.
     * @param e ExitEntry : The ExitEntry that is pushed onto the ArrayList.
     */
    public void addEntry(ExitEntry e) {
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
	    this.header.getLength()/ExitEntry.DATA_LENGTH;
	for (int i=0; i<numEntries; i++) {
	    ExitEntry e = new ExitEntry();
	    e.load();
	    addEntry(e);
	    //	    Constants.debugPrint("\tExit "+i+": "+e);
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
	int l = numEntries() * ExitEntry.DATA_LENGTH;
	header.setLength(l);
	return l;
    }
}
