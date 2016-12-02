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


/**
 * The generic structure of a Chunk.
 * @author Jonathan Irons
 * date 2/5/2008
 *
 */
public class Chunk {

    protected ChunkHeader header;

    /**
     * Empty constructor.
     */
    public Chunk(){}

    /**
     * Constructor.
     * @param h ChunkHeader : Set member ChunkHeader to h.
     */
    public Chunk(ChunkHeader h) {
	header = h;
    }

    /**
     * Accessor for the member ChunkHeader.
     * @return ChunkHeader : Member Chunkheader.
     */
    public ChunkHeader getHeader() {return header;}

    /**
     * Mutator for the member ChunkHeader.
     * @param h ChunkHeader : Set member ChunkHeader to h.
     */
    public void setHeader(ChunkHeader h) {header = h;}

    /**
     * Dummy method to begin loading a chunk.
     */
    public void load(){}

    /**
     * Method to write a chunk; takes care of writing the header.
     */
    public void write(){
	header.write();
    }

    /**
     * Dummy method for calculating the Chunk's length.
     */
    public int calcLength(){return 0;}
   
}
