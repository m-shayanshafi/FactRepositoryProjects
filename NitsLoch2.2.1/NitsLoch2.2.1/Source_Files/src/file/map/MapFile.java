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

package src.file.map;

import java.io.*;

/**
 * A singleton layer between all of the Map hierarchy and I/O; mainly used to
 * avoid repeated try/catch statements.
 */
public class MapFile {

    private DataInputStream in;
    private DataOutputStream out;
    private MapData data;
    private static MapFile instance;
    private long position, out_position;

    /**
     * Private, empty constructor for singleton use.
     */
    private MapFile() {
    }

    /**
     * Gets the current instance of this MapFile in order to use its methods.
     * Creates a new instance if none exists.
     * @return MapFile : The internal instance of MapFile.
     */
    public static MapFile getInstance() {
	if (instance == null) {
	    instance = new MapFile();
	}
	return instance;
    }

    /**
     * Load a map from a file.
     * @param path String : The path to the desired file.
     */
    public void load(String path) {
	try {
	    in = new DataInputStream(new FileInputStream(path));
	    resetInputPosition();
	    data = new MapData();
	    data.load();
	} catch (FileNotFoundException f) {
	    System.out.println("File "+path+" not found.");
	}
    }

    /**
     * Load a map from a file.
     * @param stream InputStream : The InputStream from which to load the map.
     */
    public void load(InputStream stream) {
	try {
	    in = new DataInputStream(stream);
	    resetInputPosition();
	    data = new MapData();
	    data.load();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Save a map to a file.
     * @param path String : The path to the desired file.
     */
    public void save(String path) {
	openOutput(path);
	if (data == null)
	    data = new MapData();
	data.write();
    }

    /**
     * Combines two map files.
     * @param path String : The path of the MapFile to add to this one.
     */
    public void combine(String path) {
	MapData one = data;
	load(path);
	one.append(data);
	data = one;
    }

    /**
     * Closes the current input file.
     */
    public void closeInput() {
	try {in.close();}
	catch (Exception e) {e.printStackTrace();}
    }

    /**
     * Flushes contents of data stream to the file.
     */
    public void flush() {
	try {out.flush();}
	catch (Exception e) {e.printStackTrace();}
    }

    /**
     * Closes current output file.
     */
    public void closeOutput() {
	try {out.close();}
	catch (Exception e) {e.printStackTrace();}
    }

    /**
     * Opens a new output file.
     * @param path String : The path to the output file.
     */
    public void openOutput(String path) {
	try {
	    out = new DataOutputStream(new FileOutputStream(path));
	    out_position = 0;
	} catch (Exception e) {e.printStackTrace();}
    }

    /**
     * Reads from the input file into an array of bytes.
     * @param b byte[] : Reads b.length bytes into byte array b.
     */ 
    public void read(byte[] b) {
	try {
	    in.read(b);
	    position+=b.length;
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Writes to the output file from an array of bytes.
     * @param b byte[] : Write b.length bytes from array b.
     */
    public void write(byte[] b) {
	try {
	    out.write(b);
	    out_position+=b.length;
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Reads a byte from the input file.
     * @return byte : One byte from input. Is -1 if an exception occurs.
     */
    public byte readByte() {
	try {
	    position++;
	    return in.readByte();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return -1;
    }

    /**
     * Write a byte to the output file.
     * @param b byte : The byte written to the output.
     */
    public void writeByte(byte b) {
	try {
	    out.write(b);
	    out_position++;
	}
	catch (Exception e) { e.printStackTrace();}
    }

    /**
     * Reads a short from the input file.
     * @return short : One short from the input. Is -1 if an exception occurs.
     */
    public short readShort() {
	try {
	    position+=2;
	    return in.readShort();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return -1;
    }

    /**
     * Writes a short to the output file.
     * @param s short : The short written to the output.
     */
    public void writeShort(short s) {
	try {
	    out.writeShort(s);
	    out_position+=2;
	}
	catch (Exception e) {e.printStackTrace();}
    }

    /**
     * Reads an int from the input file.
     * @return int : One int from the input. Is -1 if an exception occurs.
     */
    public int readInt() {
	try {
	    position+=4;
	    return in.readInt();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return -1;
    }

    /**
     * Writes an int to the output file.
     * @param i int : The int written to the output.
     */
    public void writeInt(int i) {
	try {
	    out.writeInt(i);
	    out_position+=4;
	}
	catch (Exception e) {e.printStackTrace();}
    }

    /**
     * Accessor for the current position in the input.
     * @return long : The position in the input file (in bytes).
     */
    public long getInputPosition() {return position;}

    /**
     * Resets current position in the input file to zero.
     */
    public void resetInputPosition() {position = 0;}

    /**
     * Accessor for the current position in the output.
     * @return long : The position in the output file (in bytes).
     */
    public long getOutputPosition() {return out_position;}

    /**
     * Resets current position in the output file to zero.
     */
    public void resetOutputPosition() {out_position = 0;}
}
