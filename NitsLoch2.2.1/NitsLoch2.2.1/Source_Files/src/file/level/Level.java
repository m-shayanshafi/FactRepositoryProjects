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

package src.file.level;

import src.file.map.MapFile;
import src.file.chunk.*;
import src.file.enemy.*;
import src.file.exit.*;
import src.file.item.*;
import src.file.npc.*;
import src.file.obstruction.*;
import src.file.shop.*;
import src.file.street.*;
import src.game.*;
import src.land.*;
import src.Constants;


/**
 * The structure of a Level (a.k.a. City).
 * @author Jonathan Irons
 * date 2/5/2008
 *
 */
public class Level {

    private LevelHeader header;
    
    private ObstructionChunk obstructions;
    private EnemyChunk enemies;
    private ItemChunk items;
    private ShopChunk shops;
    private StreetChunk streets;
    private ExitChunk exits;
    private NPCChunk npcs;

    /**
     * The basic Level constructor.
     * @param h LevelHeader : The Level's header.
     */
    public Level(LevelHeader h) {
	   header = h;
    }

    /**
     * This constructor converts a Land[][] array into its Level format.
     * @param h LevelHeader : The Level's header.
     * @param grid Land[][] : The two-dimensional array of Lands that
     * represents a city/level.
     */
    public Level (LevelHeader h, Land[][] grid) {
	header = h;
	//First, create all-new chunks of every type and their assoc. headers.

	ChunkHeader shh = new ChunkHeader(ShopChunk.TAG);
	shops = new ShopChunk(shh);

	ChunkHeader sth = new ChunkHeader(StreetChunk.TAG);
	streets = new StreetChunk(sth);

	ChunkHeader oh = new ChunkHeader(ObstructionChunk.TAG);
	obstructions = new ObstructionChunk(oh);

	ChunkHeader exh = new ChunkHeader(ExitChunk.TAG);
	exits = new ExitChunk(exh);

	ChunkHeader nh = new ChunkHeader(NPCChunk.TAG);
	npcs = new NPCChunk(nh);

	ChunkHeader eh = new ChunkHeader(EnemyChunk.TAG);
	enemies = new EnemyChunk(eh);

	ChunkHeader ih = new ChunkHeader(ItemChunk.TAG);
	items = new ItemChunk(ih);

	//traverse the Land grid, looking for various Land types
	for (int r=0; r<grid.length; r++) {
	    for (int c=0; c<grid[r].length; c++) {
		Land land = grid[r][c];
		if (land instanceof Street) {
		    Street street = (Street) land;
		    StreetEntry s = new StreetEntry();
		    s.setRow(r);
		    s.setColumn(c);
		    s.setType(street.getType().getType());
		    s.setPlayerIndex(street.hasPlayer());
		    streets.addEntry(s);
		    //Streets may contain the following types
		    Enemy e = street.getEnemy();
		    Item i = street.getItem();
		    NPC n = street.getNPC();
		    if (e != null) {
			EnemyEntry ee = new EnemyEntry();
			ee.setType(e.getType().getType());
			ee.setLevel(e.getAdvanced());
			ee.setRow(r);
			ee.setColumn(c);
			enemies.addEntry(ee);
		    }
		    if (i != null) {
			ItemEntry ie = new ItemEntry();
			ie.setType(i.getType().getType());
			ie.setData(i.getData());
			ie.setRow(r);
			ie.setColumn(c);
			items.addEntry(ie);
		    }
		    if (n != null) {
			NPCEntry ne = new NPCEntry();
			ne.setType(n.getType().getType());
			ne.setRow(r);
			ne.setColumn(c);
			npcs.addEntry(ne);
		    }
		} else if (land instanceof Obstruction) {
		    Obstruction obstruction = (Obstruction) land;
		    ObstructionEntry o = new ObstructionEntry();
		    o.setType(obstruction.getType().getType());
		    o.setCanBeDestroyed(obstruction.isDestroyable());
		    o.setRow(r);
		    o.setColumn(c);
		    obstructions.addEntry(o);
		} else if (land instanceof Exit) {
		    Exit exit = (Exit) land;
		    ExitEntry e = new ExitEntry();
		    e.setType(exit.getType().getType());
		    e.setNextCity(exit.getNextCity());
		    e.setDestinationRow(exit.getDestinationRow());
		    e.setDestinationColumn(exit.getDestinationCol());
		    e.setIsOpen(exit.getIsOpen());
		    e.setRow(r);
		    e.setColumn(c);
		    exits.addEntry(e);
		} else if (land instanceof Shop) {
		    Shop shop = (Shop) land;
		    ShopEntry s = new ShopEntry();
		    s.setType(shop.getType().getType());
		    s.setPermutation(shop.getPermutation());
		    s.setRow(r);
		    s.setColumn(c);
		    shops.addEntry(s);
		}
	    }
	}
    }

    /**
     * Accessor for the Level's header.
     * @return LevelHeader: The LevelHeader belonging to this Level.
     */
    public LevelHeader getHeader() {return header;}

    /**
     * Begin loading the level from a file.
     */
    public void load() {
	MapFile mf = MapFile.getInstance();
	while (mf.getInputPosition() <
	       header.getOffset() +
	       header.getLength()) {
	    int tag = mf.readInt();
	    int length = mf.readInt();
	    ChunkHeader h = new ChunkHeader();
	    h.setTag(tag);
	    h.setLength(length);
	    switch (tag) {
	    case EnemyChunk.TAG:
		Constants.debugPrint("ENEM");
		enemies = new EnemyChunk(h);
		enemies.load();
		break;
	    case NPCChunk.TAG:
		Constants.debugPrint("NPCS");
		npcs = new NPCChunk(h);
		npcs.load();
		break;
	    case ItemChunk.TAG:
		Constants.debugPrint("ITEM");
		items = new ItemChunk(h);
		items.load();
		break;
	    case ShopChunk.TAG:
		Constants.debugPrint("SHOP");
		shops = new ShopChunk(h);
		shops.load();
		break;
	    case StreetChunk.TAG:
		Constants.debugPrint("STRT");
		streets = new StreetChunk(h);
		streets.load();
		break;
	    case ObstructionChunk.TAG:
		Constants.debugPrint("OBST");
		obstructions = new ObstructionChunk(h);
		obstructions.load();
		break;
	    case ExitChunk.TAG:
		Constants.debugPrint("EXIT");
		exits = new ExitChunk(h);
		exits.load();
		break;
	    default:
		break;
	    }
	}
    }

    /**
     * Begin writing the Level to a file.
     */
    public void write() {
	calcLength();
	//write all designated chunk types
	obstructions.write();
	streets.write();
	exits.write();
	shops.write();
	enemies.write();
	npcs.write();
	items.write();
    }

    /**
     * Calculates the Level's length by going through the Level's Chunks,
     * Using the calcLength() method for each of those. Instead of returning
     * a number, like the other calcLength() methods, this sets the
     * LevelHeader's length equal to the result.
     * @see src.file.chunk.Chunk#calcLength()
     */
    public void calcLength() {
	int l=0;
	int hl = ChunkHeader.DATA_LENGTH;
	l+=obstructions.calcLength()+hl;
	l+=enemies.calcLength()+hl;
	l+=items.calcLength()+hl;
	l+=shops.calcLength()+hl;
	l+=streets.calcLength()+hl;
	l+=exits.calcLength()+hl;
	l+=npcs.calcLength()+hl;
	header.setLength(l);
    }

    public String toString() {
	return " begins @"+header.getOffset()+"; "+header.getLength()+" long"+
	    "; "+header.getNumRows()+" x "+header.getNumColumns()+"; "+
	    "'"+header.getName()+"'";
    }
}
