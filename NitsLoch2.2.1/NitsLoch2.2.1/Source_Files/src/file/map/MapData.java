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

import java.util.ArrayList;

import src.Constants;
import src.file.level.Level;
import src.file.level.LevelHeader;
import src.game.*;
import src.land.*;

/**
 * The underlying structure of the entire Map. Contains a set of headers
 * and a collection of levels.
 * @author Jonathan Irons
 * date 2/6/2008
 */
public class MapData {

    private MapHeader header;
    private ArrayList<Level> levels = new ArrayList<Level>();

    /**
     * Constructor. Initializes the MapHeader to some default values.
     */
    public MapData() {
	header = new MapHeader();
	header.setProgram(MapHeader.PROGRAM);
	header.setFormat(MapHeader.FORMAT);
	header.setNumberOfLevels(0);
	header.setDataLength(0);
    }

    /**
     * Accessor for the map's MapHeader.
     * @return MapHeader : The header for the entire map.
     */
    public MapHeader getHeader() {return header;}

    /**
     * Accessor for a given Level in the map.
     * @param i int : Get level index i.
     * @return Level : The Level at index i.
     */
    public Level getLevel(int i){return levels.get(i);}

    /**
     * Accessor for all levels in the map.
     * @return ArrayList<Level> : The ArrayList of Levels.
     */
    public ArrayList<Level> getLevels() {return levels;}

    /**
     * Fills the MapHeader and the LevelHeaders contained inside.
     */
    public void fillHeader() /*throws InvalidFormatException*/ {
	MapFile file = MapFile.getInstance();
	Constants.debugPrint("Filling map header.");
	int program = file.readInt();
	int format = file.readInt();
	if (program != MapHeader.PROGRAM ||
	    format != MapHeader.FORMAT) {
	    //throw new InvalidFormatException();
	} else {
	    Constants.debugPrint("Formats are OK.");
	    header.setProgram(program);
	    header.setFormat(format);
	    header.setNumberOfLevels(file.readInt());
	    header.setDataLength(file.readInt());
	    Constants.debugPrint(header);
	    for (int i=0; i<header.getNumberOfLevels(); i++) {
		LevelHeader lh = new LevelHeader();
		lh.setOffset(file.readInt());
		lh.setLength(file.readInt());
		lh.setDimensions
		    (file.readInt(),
		     file.readInt());
		byte[] bytes = new byte[LevelHeader.MAXIMUM_NAME_LENGTH];
		char[] chars = new char[LevelHeader.MAXIMUM_NAME_LENGTH];
		file.read(bytes);
		for (int j=0; j<bytes.length; j++) {
		    chars[j] = (char) bytes[j];
		}
		lh.setName(chars);
		header.addLevelHeader(lh);
	    }
	}
    }

    /**
     * Loads the levels from the MapFile. Resets the GameWorld before beginning.
     * @see src.game.GameWorld
     */
    public void loadLevels() {
	GameWorld g = GameWorld.getInstance();
	g.resetCities();
	for (int i=0; i<header.getNumberOfLevels(); i++) {
		try {
	    LevelHeader lh = header.getLevelHeader(i);
	    Level l = new Level(lh);
	    Constants.debugPrint(l);
	    g.newCity(lh.getName(), lh.getNumRows(), lh.getNumColumns());
	    l.load();
	    levels.add(l);
		} catch(src.exceptions.CityNameExistsException e) { }
	}
    }

    /**
     * Calculates a level's offset. Also calls calculateLength().
     * @param c int : Calculate the offset of the Level at index c.
     * @return int : The position in bytes of the Level's offset.
     * @see #calculateLength()
     */
    public int calcOffsetAt(int c) {
	calculateLength();
	int o = MapHeader.DATA_LENGTH +
	    LevelHeader.DATA_LENGTH * (header.getNumberOfLevels());
	if (c > 0) {
	    for (int i=0; i<c; i++) {
		Level l = levels.get(i);
		o+=l.getHeader().getLength();
	    }
	}
	Constants.debugPrint("Calculated level "+c+" offset = "+o);
	return o;
    }

    /**
     * Recursively calculates the lengths of everything in the map, updating
     * headers along the way.
     * @return int : The length in bytes of the map.
     */
    public int calculateLength() {
	int l = MapHeader.DATA_LENGTH;
	for (int i=0; i<header.getNumberOfLevels(); i++) {
	    l+=LevelHeader.DATA_LENGTH;
	    getLevel(i).calcLength();
	    l+=getLevel(i).getHeader().getLength();
	}
	return l;
    }

    /**
     * Writes the map's header and its Levels' headers.
     */
    public void writeHeaders() {
	MapFile file = MapFile.getInstance();
	file.writeInt(MapHeader.PROGRAM);
	file.writeInt(MapHeader.FORMAT);
	file.writeInt(levels.size());
	file.writeInt(calculateLength());
	for (int i=0; i<header.getNumberOfLevels(); i++) {
	    Level l = levels.get(i);
	    l.calcLength();
	    int o = calcOffsetAt(i);
	    Constants.debugPrint("writeHeaders got level "+i+" offset = "+o);
	    l.getHeader().setOffset(o);
	    l.getHeader().write();
	}
    }

    /**
     * Begins loading the headers and levels from the MapFile.
     */
    public void load() {
	Constants.debugPrint("Loading from file at "+MapFile.getInstance());
 	fillHeader();
 	loadLevels();
    }

    /**
     * Appends the levels from another MapData onto this MapData's levels.
     * @param data MapData : The MapData whose levels will be added.
     */
    public void append(MapData data) {
	ArrayList<Level> newLevels = data.getLevels();
	for (int i=0; i<newLevels.size(); i++) {
	    Level l = newLevels.get(i);
	    LevelHeader lh = l.getHeader();
	    levels.add(l);
	    header.addLevelHeader(lh);
	}
    }

    /**
     * Fills in all the data in the map. Used for creating a map from the
     * GameWorld.
     * @see src.game.GameWorld
     */
    public void fillData() {
	Constants.debugPrint("*Filling data from GameWorld...");
	GameWorld g = GameWorld.getInstance();
	int currentLevel = g.getCurrentLevel();
	ArrayList<Land[][]> cities = g.getCities();
	levels = new ArrayList<Level>();
	for (int i=0; i<cities.size(); i++) {
	    Constants.debugPrint("**Filling level "+i+" data...");
	    g.setCurrentLevel(i);
	    LevelHeader h = new LevelHeader();
	    header.addLevelHeader(h);
	    Land[][] grid = cities.get(i);
	    h.setName(g.getCityName(g.getCurrentLevel()));
	    h.setDimensions(grid.length, grid[0].length);
	    levels.add(new Level(h, grid));
	}
	header.setNumberOfLevels(levels.size());
	g.setCurrentLevel(currentLevel);
    }

    /**
     * Begins writing the map to a file.
     */
    public void write() {
	MapFile file = MapFile.getInstance();
	Constants.debugPrint("Writing...");
	fillData();
	writeHeaders();
	for (int i=0; i<header.getNumberOfLevels(); i++) {
	    Level l = levels.get(i);
	    l.write();
	}
	file.flush();
	file.closeOutput();
	Constants.debugPrint("Writing finished.");
    }
}
