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

package src.file.enemy;

import java.util.ArrayList;

import src.Constants;
import src.file.chunk.Chunk;
import src.file.chunk.ChunkHeader;

/**
 * The file format for a collection of EnemyEntries.
 * @author Jonathan Irons
 * date 2/5/2008
 *
 */
public class EnemyChunk extends Chunk {
   
    /**
     * The ASCII sequence to identify an EnemyChunk ('ENEM').
     */
    public static final int TAG = 0x454e454d;
    private ArrayList<EnemyEntry> entries =
	new ArrayList<EnemyEntry>();

	
    /**
     * Constructor for the collection of EnemyEntries.
     * @param h ChunkHeader : This Chunk's ChunkHeader
     */
    public EnemyChunk(ChunkHeader h) {
        super(h);
    }

    /**
     * Accessor for a given EnemyEntry.
     * @return EnemyEntry : An individual EnemyEntry from this chunk.
     */
    public EnemyEntry getEntry(int i) {return entries.get(i);}

    /**
     * Set a given EnemyEntry.
     * @param i int : Entry index.
     * @param e EnemyEntry : This EnemyEntry replaces whatever was at i.
     */
    public void setEntry(int i, EnemyEntry e) {
        entries.set(i, e);
    }

    /**
     * Get number of entries present in this chunk.
     * @return int : The number of entries in the chunk.
     */
    public int numEntries(){return entries.size();}

    /**
     * Add an EnemyEntry to the end of the list.
     * @param e EnemyEntry : The EnemyEntry that is pushed onto the ArrayList.
     */
    public void addEntry(EnemyEntry e) {
        entries.add(e);
    }

    /**
     * Begin loading (reading) this chunk from the map file. Takes the
     * DATA_LENGTH of the corresponding Entry type and uses that to split the
     * file data into Entries.
     */
    public void load() {
	Constants.debugPrint("Loading Enemy");
        super.load();
        int numEntries =
            header.getLength()/EnemyEntry.DATA_LENGTH;
        for (int i=0; i<numEntries; i++) {
            EnemyEntry e = new EnemyEntry();
            e.load();
            addEntry(e);
	    //	    Constants.debugPrint("\tEnemy "+i+": "+e);
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
	int l = numEntries() * EnemyEntry.DATA_LENGTH;
	header.setLength(l);
	return l;
    }
}
