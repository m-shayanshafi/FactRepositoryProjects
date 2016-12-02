/*
 * PokemonNature.java
 *
 * Created on December 15, 2006, 12:20 PM
 *
 * This file is a part of Shoddy Battle.
 * Copyright (C) 2006  Colin Fitzpatrick
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
 * along with this program; if not, visit the Free Software Foundation, Inc.
 * online at http://gnu.org.
 */

package mechanics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import shoddybattle.Pokemon;
import java.io.*;

/**
 * This class represents the nature of a pokemon in the advance generation.
 *
 * @author Colin
 */
public class PokemonNature implements Serializable {
    
    private static final long serialVersionUID = -549059028197342801L;
    
    /*serializable*/ private int m_nature;
        
    private static final ArrayList m_natures = new ArrayList();
    
    public static final PokemonNature N_LONELY = new PokemonNature("Lonely", Pokemon.S_ATTACK, Pokemon.S_DEFENCE);
    public static final PokemonNature N_BRAVE = new PokemonNature("Brave", Pokemon.S_ATTACK, Pokemon.S_SPEED);
    public static final PokemonNature N_ADAMANT = new PokemonNature("Adamant", Pokemon.S_ATTACK, Pokemon.S_SPATTACK);
    public static final PokemonNature N_NAUGHTY = new PokemonNature("Naughty", Pokemon.S_ATTACK, Pokemon.S_SPDEFENCE);
    public static final PokemonNature N_BOLD = new PokemonNature("Bold", Pokemon.S_DEFENCE, Pokemon.S_ATTACK);
    public static final PokemonNature N_RELAXED = new PokemonNature("Relaxed", Pokemon.S_DEFENCE, Pokemon.S_SPEED);
    public static final PokemonNature N_IMPISH = new PokemonNature("Impish", Pokemon.S_DEFENCE, Pokemon.S_SPATTACK);
    public static final PokemonNature N_LAX = new PokemonNature("Lax", Pokemon.S_DEFENCE, Pokemon.S_SPDEFENCE);
    public static final PokemonNature N_TIMID = new PokemonNature("Timid", Pokemon.S_SPEED, Pokemon.S_ATTACK);
    public static final PokemonNature N_HASTY = new PokemonNature("Hasty", Pokemon.S_SPEED, Pokemon.S_DEFENCE);
    public static final PokemonNature N_JOLLY = new PokemonNature("Jolly", Pokemon.S_SPEED, Pokemon.S_SPATTACK);
    public static final PokemonNature N_NAIVE = new PokemonNature("Naive", Pokemon.S_SPEED, Pokemon.S_SPDEFENCE);
    public static final PokemonNature N_MODEST = new PokemonNature("Modest", Pokemon.S_SPATTACK, Pokemon.S_ATTACK);
    public static final PokemonNature N_MILD = new PokemonNature("Mild", Pokemon.S_SPATTACK, Pokemon.S_DEFENCE);
    public static final PokemonNature N_QUIET = new PokemonNature("Quiet", Pokemon.S_SPATTACK, Pokemon.S_SPEED);
    public static final PokemonNature N_RASH = new PokemonNature("Rash", Pokemon.S_SPATTACK, Pokemon.S_SPDEFENCE);
    public static final PokemonNature N_CALM = new PokemonNature("Calm", Pokemon.S_SPDEFENCE, Pokemon.S_ATTACK);
    public static final PokemonNature N_GENTLE = new PokemonNature("Gentle", Pokemon.S_SPDEFENCE, Pokemon.S_DEFENCE);
    public static final PokemonNature N_SASSY = new PokemonNature("Sassy", Pokemon.S_SPDEFENCE, Pokemon.S_SPEED);
    public static final PokemonNature N_CAREFUL = new PokemonNature("Careful", Pokemon.S_SPDEFENCE, Pokemon.S_SPATTACK);
    public static final PokemonNature N_QUIRKY = new PokemonNature("Quirky", -1, -1);
    public static final PokemonNature N_HARDY = new PokemonNature("Hardy", -1, -1);
    public static final PokemonNature N_SERIOUS = new PokemonNature("Serious", -1, -1);
    public static final PokemonNature N_BASHFUL = new PokemonNature("Bashful", -1, -1);
    public static final PokemonNature N_DOCILE = new PokemonNature("Docile", -1, -1);
    
    transient private String m_name;
    transient private int m_harms;
    transient private int m_benefits;
    
    /**
     * Get a nature by index.
     */
    public static PokemonNature getNature(int i) {
        if ((i < 0) || (i >= m_natures.size()))
            return new PokemonNature("Error", -1, -1);
        return (PokemonNature)m_natures.get(i);
    }
    
    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        PokemonNature nature = getNature(m_nature);
        m_name = nature.m_name;
        m_harms = nature.m_harms;
        m_benefits = nature.m_benefits;
    }
    
    /**
     * Creates a new instance of PokemonNature by arbitrary indices.
     */
    private PokemonNature(String name, int benefits, int harms) {
        m_name = name;
        m_benefits = benefits;
        m_harms = harms;
        m_nature = m_natures.size();
        m_natures.add(this);
    }
    
    /**
     * Initialise this nature by its name. Note that the first letter should
     * be capital, e.g., "Hardy", "Naive", etc.
     */
    private PokemonNature(String name) {
        Iterator i = m_natures.iterator();
        while (i.hasNext()) {
            PokemonNature nature = (PokemonNature)i.next();
            if (name.equals(name)) {
                m_name = name;
                m_benefits = nature.m_benefits;
                m_harms = nature.m_harms;
                break;
            }
        }
    }
    
    /**
     * Get the effect a nature has on a particular stat.
     * This will be 0.9, 1, or 1.1.
     *
     * @param i the index of the statistic
     */
    public double getEffect(int i) {
        return (i == m_benefits) ? 1.1 : ((i == m_harms) ? 0.9 : 1.0);
    }
    
    /**
     * Get a list of natures.
     */
    public static String[] getNatureNames() {
        String[] natures = new String[m_natures.size()];
        Iterator i = m_natures.iterator();
        int j = 0;
        while (i.hasNext()) {
            natures[j++] = ((PokemonNature)i.next()).getName();
        }
        return natures;
    }
    
    /**
     * Get a textual representation of the nature.
     */
    public String getName() {
        return m_name;
    }
    
    /**
     * Gets the stat that this nature benefits
     */
    public int getBenefits() {
        return m_benefits;
    }
    
    /**
     * Gets the stat that this nature hinders
     */
    public int getHarms() {
        return m_harms;
    }
    
}
