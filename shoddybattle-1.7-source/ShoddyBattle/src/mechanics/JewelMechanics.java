/*
 * JewelMechanics.java
 *
 * Created on June 26, 2007, 1:20 AM.
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

package mechanics;
import mechanics.moves.*;
import shoddybattle.*;
import java.util.*;
import java.io.*;

/**
 * Mechanics for the DS games (diamond/pearl), or the "Jewel Generation".
 * @author Colin
 */
public class JewelMechanics extends AdvanceMechanics {

    /**
     * Map indicating whether each move is special.
     */
    private static HashMap m_moves = new HashMap();
    
    public JewelMechanics(int bytes) {
        super(bytes);
    }
    
    /**
     * Load the move types from the given file.
     */
    public static void loadMoveTypes(File f) throws FileNotFoundException {
        m_moves.clear();
        String line;
        BufferedReader input = new BufferedReader(new FileReader(f));
        try {
            while ((line = input.readLine()) != null) {
                int space = line.lastIndexOf(' ');
                boolean special = (Integer.parseInt(line.substring(space + 1)) != 0);
                String move = line.substring(0, space);
                m_moves.put(move, new Boolean(special));
            }
        } catch (IOException e) {
            
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                
            }
        }
    }
    
    /**
     * Return whether a given move deals special damage.
     * In Diamond/Pearl, this is based on the move, not its type.
     */
    public boolean isMoveSpecial(PokemonMove move) {
        MoveListEntry entry = move.getMoveListEntry();
        if (entry == null) {
            return move.getType().isSpecial();
        }
        Boolean b = (Boolean)m_moves.get(entry.getName());
        if (b == null) {
            System.out.println("Warning: no move type entry for " + entry.getName() + "!");
            return move.getType().isSpecial();
        }
        return b.booleanValue();
    }

}
