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

package src.file.chunk;
import src.file.map.MapFile;


/**
 * The file format for a Chunk's header.
 * @author Jonathan Irons
 * date 2/5/2008
 *
 */
public class ChunkHeader {

    /**
     * The length of all data contained in the ChunkHeader.
     */
    public static final byte DATA_LENGTH = 8;
    private int tag;
    private int length;

    /**
     * Constructor that sets nothing initially.
     */
    public ChunkHeader() {}

    /**
     * Constructor the sets the tag.
     * param t int : Set ChunkHeader tag to t.
     */
    public ChunkHeader(int t) {
	tag = t;
    }

    /**
     * Accessor for the ChunkHeader's stored tag.
     * @return int : Stored tag.
     */
    public int getTag() {return tag;}

    /**
     * Mutator for the ChunkHeader's stored tag.
     * @param t int : Set stored tag to t.
     */
    public void setTag(int t) {tag = t;}

    /**
     * Accessor for the ChunkHeader's stored length.
     * @return int : Stored length.
     */
    public int getLength() {return length;}

    /**
     * Mutator for the ChunkHeader's stored length.
     * @param l int : Set stored length to l.
     */
    public void setLength(int l) {length = l;}

    /**
     * Write the ChunkHeader to the current file.
     */
    public void write() {
	MapFile mf = MapFile.getInstance();
	mf.writeInt(getTag());
	mf.writeInt(getLength());
    }
}
