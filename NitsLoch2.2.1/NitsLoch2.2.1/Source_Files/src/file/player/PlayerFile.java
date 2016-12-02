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

package src.file.player;

import java.io.*;
import src.game.Player;
import src.enums.Armor;
import src.enums.Weapon;
import src.scenario.MiscScenarioData;
import src.game.GameWorld;

/**
 * The file representation of a Player. This is similar to a hybrid
 * between MapFile and MapData. It is a singleton, just like MapFile.
 * @see src.file.map.MapFile
 * @see src.file.map.MapData
 */
public class PlayerFile {
    /**
     * The "Program" code for all NitsLoch file formats. In a big-endian file,
     * this is equivalent to ASCII 'NITS'.
     */
    public static final int PROGRAM = 0x4e495453;

    /**
     * The "Format" code for the Player format (as opposed to a Map file or
     * other future additions). In a big-endian file, this is equivalent to
     * ASCII 'PLA2'. PLA2 is the second version of the Player file format;
     * PLAY is the first version.
     */
    public static final int FORMAT = 0x504c4132;

    /* Stuff from src.game.Player */
    private int hitPoints;
    private int maxHitPoints;
    private int fightingAbility;
    private int marksmanAbility;
    private int martialArtsAbility;
    private int thievingAbility;
    private int nameLength;
    private String playerName;
    private int scenarioLength;
    private String fromScenario;

    private DataInputStream in;
    private DataOutputStream out;
    private long position, out_position;

    private static PlayerFile instance;

    /**
     * Private, empty constructor. Used for singleton's devious purposes.
     */
    private PlayerFile () {
    }

    /**
     * Returns the instance of the PlayerFile.
     * @return PlayerFile : The running instance of PlayerFile.
     */
    public static PlayerFile getInstance() {
	if (instance == null)
	    instance = new PlayerFile();
	return instance;
    }

    /**
     * Saves the PlayerFile to a file on disk.
     * @param path String : The path to the desired file.
     */
    public void save(String path) {
	Player p = GameWorld.getInstance().getLocalPlayer();
	openOutput(path);

	hitPoints = p.getHitPoints();
	maxHitPoints = p.getMaxHitPoints();
	fightingAbility = p.getFightingAbility();
	marksmanAbility = p.getMarksmanAbility();
	martialArtsAbility = p.getMartialArtsAbility();
	thievingAbility = p.getThievingAbility();
	playerName = p.getName();

	writeInt(PROGRAM);
	writeInt(FORMAT);
	writeInt(hitPoints);
	writeInt(maxHitPoints);
	writeInt(fightingAbility);
	writeInt(marksmanAbility);
	writeInt(martialArtsAbility);
	writeInt(thievingAbility);
	writeInt(playerName.length());
	write(playerName.getBytes());
	writeInt(MiscScenarioData.NAME.length());
	write(MiscScenarioData.NAME.getBytes());
	writeChinks();
	flush();
	closeOutput();
    }

    /**
     * Loads the PlayerFile from a file on disk.
     * @param path String : The path to the saved PlayerFile to be loaded.
     */ 
    public void load(String path) {
	try {
	    in = new DataInputStream(new FileInputStream(path));
	    position = 0;

	    int prog, form;
	    prog = readInt();
	    form = readInt();

	    if (prog != PROGRAM || form != FORMAT) {
		System.out.println("\""+path+"\" is not a valid Player File.");
	    } else {
		hitPoints = readInt();
		maxHitPoints = readInt();
		fightingAbility = readInt();
		marksmanAbility = readInt();
		martialArtsAbility = readInt();
		thievingAbility = readInt();
		nameLength = readInt();
		byte[] b = new byte[nameLength];
		read(b);
		playerName = new String(b);
		scenarioLength = readInt();
		b = new byte[scenarioLength];
		read(b);
		fromScenario = new String(b);
		setStats(GameWorld.getInstance().getLocalPlayer());
		readChinks();
	    }
	} catch (FileNotFoundException f) {
	    System.out.println("File "+path+" not found.");
	}
    }

    /**
     * Sets the statistics of a Player to those acquired from the PlayerFile.
     * @param p Player : The player whose stats to adjust.
     */
    public void setStats(Player p) {
	p.setName(playerName);
	p.setHP(hitPoints);
	p.setMaxHP(maxHitPoints);
	p.setFightingAbil(fightingAbility);
	p.setMarksmanshipAbil(marksmanAbility);
	p.setMartialArtsAbil(martialArtsAbility);
	p.setThievingAbil(thievingAbility);
    }

    /**
     * Close the PlayerFile's input stream.
     */
    public void closeInput() {
	try {in.close();}
	catch (Exception e) {e.printStackTrace();}
    }

    /**
     * Open a new input stream for the PlayerFile.
     * @param path String : The path to the desired input file.
     */
    public void openInput(String path) {
	try {
	    in = new DataInputStream(new FileInputStream(path));
	} catch (FileNotFoundException e) {
	    System.out.println("File "+path+" not found.");
	}
    }

    /**
     * Flush data in the output stream to its file.
     */
    public void flush() {
	try {out.flush();}
	catch (Exception e) {e.printStackTrace();}
    }

    /**
     * Close the PlayerFile's output stream.
     */
    public void closeOutput() {
	try {out.close();}
	catch (Exception e) {e.printStackTrace();}
    }

    /**
     * Open a new output stream for the PlayerFile.
     * @param path String : The path to the desired output file.
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

    /**
     * Reads the mini-chunks ("chinks") that are inside the PlayerFile.
     */
    public void readChinks() {
	boolean cont = true;
	while (cont) {
	    try {
		ChinkHeader ch =
		    new ChinkHeader(in.readByte(), in.readByte());
		Chink c = new Chink();
		c.setHeader(ch);
		Intry[] intries = new Intry[ch.getNumIntries()];
		for (int i=0; i<intries.length; i++) {
		    intries[i] =
			new Intry(in.readShort());
		}
		c.setIntries(intries);
		Player p = GameWorld.getInstance().getLocalPlayer();
		for (int i=0; i<intries.length; i++) {
		    short q = intries[i].getQuantity();
		    switch (ch.getType()) {
		    case 0: /* weapons */
			if (fromScenario.equals(MiscScenarioData.NAME)
			    && q > 0)
			    p.addWeapon(Weapon.values()[i], q);
			break;
		    case 1: /* armor */
			if (fromScenario.equals(MiscScenarioData.NAME)
			    && q > 0)
			    p.addArmor(Armor.values()[i]);
			break;
		    case 2: /* items */
			switch (i) {
			case 0:
			    p.addBandaid(q-p.getNumBandaids());
			    break;
			case 1:
			    p.addGrenade(q - p.getNumGrenades());
			    break;
			case 2:
			    p.addDynamite(q - p.getNumDynamite());
			    break;
			case 3:
			    p.addBullets(q - p.getNumBullets());
			    break;
			case 4:
			    p.addRockets(q - p.getNumRockets());
			    break;
			case 5:
			    p.addFlamePacks(q - p.getNumFlamePacks());
			    break;
			case 6:
			    p.addLadderUp(q - p.getNumLaddersUp());
			    break;
			case 7:
			    p.addLadderDown(q - p.getNumLaddersDown());
			    break;
			case 8:
			    p.addMapViewer(q - p.getNumMapViewers());
			    break;
			default: break;
			    //exports aren't counted.
			}
			break;
		    case 3: /* money */
			p.setMoney(q);
			break;
		    default: 
			break;
		    }
		}
	    } catch (EOFException eof) {
		cont = false;
	    } catch (Exception e) { e.printStackTrace(); }
	}
    }

    /**
     * Writes the mini-chunks ("chinks") to the Player file.
     */
    public void writeChinks() {
	try {
	    Player p = GameWorld.getInstance().getLocalPlayer();
	    int[] weaponLevels = p.getWeaponLevels();
	    boolean[] armor = p.getAvailableArmor();
	    //weapons
	    out.writeByte(0);
	    out.writeByte((byte) weaponLevels.length);
	    for (int i=0;i<weaponLevels.length;i++) {
		out.writeShort(weaponLevels[i]);
	    }
	    //armor
	    out.writeByte(1);
	    out.writeByte((byte) armor.length);
	    for (int i=0;i<armor.length;i++) {
		if (armor[i])
		    out.writeShort(1);
		else
		    out.writeShort(0);
	    }
	    //items
	    out.writeByte(2);
	    out.writeByte(9);
	    out.writeShort((short)p.getNumBandaids());
	    out.writeShort((short)p.getNumGrenades());
	    out.writeShort((short)p.getNumDynamite());
	    out.writeShort((short)p.getNumBullets());
	    out.writeShort((short)p.getNumRockets());
	    out.writeShort((short)p.getNumFlamePacks());
	    out.writeShort((short)p.getNumLaddersUp());
	    out.writeShort((short)p.getNumLaddersDown());
	    out.writeShort((short)p.getNumMapViewers());
	    //money
	    out.writeByte(3);
	    out.writeByte(1);
	    out.writeShort((short)p.getMoney());
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}

/**
 * A "chink" (pardon the terrible term...) is a tiny chunk whose header and
 * contents are meant to take up very little space.
 */
class Chink {
    private ChinkHeader header;
    protected Intry[] intries;
    public Chink () {}
    public ChinkHeader getHeader() { return header; }
    public Intry[] getIntries() { return intries; }
    public void setHeader(ChinkHeader h) { header = h; }
    public void setIntries(Intry[] i) { intries = i; }
}

/**
 * This is the header of a Chink: it is only two bytes long.
 */
class ChinkHeader{
    private byte type;
    private byte num_intries;
    public ChinkHeader(byte t, byte n) {
	type = t;
	num_intries = n;
    }
    public byte getType() { return type; }
    public byte getNumIntries() { return num_intries; }
    public void setType(byte t) { type = t; }
    public void setNumIntries(byte n) { num_intries = n; }
}

/**
 * This is a minature "Entry." Each Intry is a single short.
 */
class Intry {
    private short quantity; //NOTE: quantity = level for weapons.
    public Intry (short q) {
	quantity = q;
    }
    public short getQuantity() { return quantity; }
    public void setQuantity(short q) { quantity = q; }
}
