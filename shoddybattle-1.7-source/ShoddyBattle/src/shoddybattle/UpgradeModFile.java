/*
 * UpgradeModFile.java
 *
 * Created on July 6, 2007, 3:20 PM
 *
 * This file is a part of Shoddy Battle.
 * Copyright (C) 2007  Colin Fitzpatrick
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * The Free Software Foundation may be visited online at http://www.fsf.org.
 */

package shoddybattle;

import java.io.*;
import java.util.*;

/**
 * <p>This class upgrades a mod file written for the NetBattle program to one
 * usable with Shoddy Battle. The class does not perform any sophisticated
 * redundancy eliminations.
 * 
 * <p>The class is directly executable in the following manner:
 * <pre>java -cp dist/ShoddyBattle.jar shoddybattle.UpgradeModFile SPECIES [FILE [DEST]]</pre>
 * <p>where SPECIES is a species database, FILE is the file to upgrade,
 * and DEST is the output file name. If the destination file is omitted then
 * the program writes to the standard output. If both the destination and the
 * file are omitted then the program reads from the standard input as well
 * as writing to the standard output. Error messages are always written to the
 * standard error stream.
 * 
 * <p>Note that the class depends on the species file in order to load
 * the pokemon database. This database is required because the
 * NetBattle syntax for abilities requires knowledge of the original
 * abilities. Therefore a species database (e.g. dpspecies.db) must be
 * available in order to use this class.
 * 
 * <p>Note also that abilities will probably not be converted correctly because
 * NetBattle's syntax assumes that they are listed in a particular order in
 * the database. It is likely that for many pokemon this order will not
 * be the same in Shoddy Battle. Adding a second ability to a pokemon that had
 * only one was the most common use of NetBattle's ability feature and
 * a statement doing that will upgrade correctly. Statements <em>changing</em>
 * abilities may not work as expected, however.
 * 
 * @see ModData
 * @author Colin
 */
public class UpgradeModFile {

    /**
     * A NetBattle mod file is not sorted by pokemon, so it must be built
     * internally before being written. This HashMap maps each pokemon to the
     * mods applied to them, written as a list of comma-separated elements
     * comprising a valid Shoddy Battle mod expression.
     */
    private HashMap m_map = new HashMap();
    
    /**
     * Upgrade a NetBattle mod file to a Shoddy Battle patch file.
     * See class description for an overview of the parameters.
     * 
     * @param args arguments passed on the command line
     */
    public static void main(String[] args) {
        if ((args.length < 1) && (args.length > 3)) {
            System.out.println("upgrademod SPECIES [FILE [DEST]]");
            return;
        }
        File species = new File(args[0]);

        try {
            OutputStreamWriter writer;
            InputStreamReader reader;
            if (args.length == 3) {
                writer = new FileWriter(new File(args[2]));
            } else {
                writer = new OutputStreamWriter(System.out);
            }
            if (args.length != 1) {
                reader = new FileReader(new File(args[1]));
            } else {
                reader = new InputStreamReader(System.in);
            }
            
            PokemonSpeciesData data = new PokemonSpeciesData();
            data.loadSpeciesDatabase(species, false);
            new UpgradeModFile(data, reader).saveFile(writer);
            if (args.length == 3) {
                writer.close();
            }
            if (args.length != 1) {
                reader.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Save the mod file to disc.
     * 
     * @param stream stream to save to
     * @throws IOException if an error occurred while writing to the stream
     */
    public void saveFile(OutputStreamWriter stream) throws IOException {
        PrintWriter writer = new PrintWriter(stream);
        Iterator i = m_map.keySet().iterator();
        while (i.hasNext()) {
            String pokemon = (String)i.next();
            String mod = (String)m_map.get(pokemon);
            writer.println(pokemon + ":" + mod + ";\n");
        }
        writer.flush();
    }
    
    /**
     * <p>Handle a line from the NetBattle mod file.
     * 
     * <p>The syntax for a valid line is as follows:
     * <pre>Class, Pokemon, Argument 1 [, Argument 2, ...]</pre>
     * <p>where <b>Class</b> is one of <ul>
     * <li>Move - which takes one argument, the move to add to a pokemon
     * <li>Trait - which takes the ability to add and the slot to put it in
     * <li>Illegal - which mandates that the moves specified cannot occur together
     * </ul>
     * 
     * <p>Note that the "slot number" required for the <em>Trait</em> statement
     * class is arbitrary. Shoddy Battle's database does not duplicate the
     * database of NetBattle exactly so the meaning of all <em>Trait</em>
     * statements will not be the same as it was in NetBattle. This is unlikely
     * to have a significant effect on most mod files. In particular, if a
     * pokemon normally has only one ability, or if both abilitiy slots are set,
     * then statements of the <em>Trait</em> class are guaranteed to do the same
     * thing they did in NetBattle.
     * 
     * @param data species data to get abilities from
     * @param line line to handle
     */
    public void handleLine(PokemonSpeciesData data, String line) {
        if (line.length() == 0) {
            return;
        }
        String[] parts = line.split(" *, *");
        String pokemon = parts[1];
        
        int idx = data.getPokemonByName(pokemon);
        if (idx == -1) {
            System.err.println("Warning: No pokemon by the name of "
                    + pokemon + " found in the database specified.");
        }
        
        String element = (String)m_map.get(pokemon);
        if (element == null) {
            element = "";
        }
        StringBuffer buffer = new StringBuffer(element);
        buffer.append("\t");
        
        String command = parts[0].toLowerCase();
        if (command.equals("move")) {
            buffer.append("+");
            buffer.append(parts[2]);
            
        } else if (command.equals("trait")) {
            if (idx != -1) {
                int position = Integer.parseInt(parts[3]);
                String[] abilities = data.getAbilityNames(pokemon);
                if ((abilities != null) && (abilities.length >= position)) {
                    buffer.append("a-");
                    buffer.append(abilities[position - 1]);
                    buffer.append(",\n\t");
                }
                buffer.append("a+");
                buffer.append(parts[2]);
            }
                    
        } else if (command.equals("illegal")) {
            if (parts.length == 3) {
                buffer.append("-");
                buffer.append(parts[2]);
            } else {
                buffer.append("~ ");
                int i = 2;
                do {
                    buffer.append(parts[i]);
                    if (++i >= parts.length) {
                        break;
                    }
                    buffer.append(" + ");
                } while (true);
            }
            
        } else {
            System.err.println("Unknown statement class: " + command);
            return;
        }
        
        buffer.append(",\n");
        m_map.put(pokemon, new String(buffer));
    }
    
    /**
     * Read a NetBattle mod file and build an internal representation of it.
     * 
     * @param data species database (used for abilities)
     * @param stream the stream to read from
     * @throws FileNotFoundException if the file does not exist
     * @throws IOException if an error occurs while reading from the file 
     */
    public UpgradeModFile(PokemonSpeciesData data, InputStreamReader stream)
            throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(stream);
        String line;
        while ((line = reader.readLine()) != null) {
            try {
                handleLine(data, line.trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
