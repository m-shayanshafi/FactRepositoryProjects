/*
 * BattleMechanics.java
 *
 * Created on December 15, 2006, 12:01 PM
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

import mechanics.moves.*;
import shoddybattle.*;
import java.io.*;
import java.util.Random;
import java.security.SecureRandom;
import java.nio.*;

/**
 * This class represents the mechanics for a battle in a particular generation
 * of pokemon. Derive classes from this class to implement a desired generation.
 *
 * @author Colin
 */
public abstract class BattleMechanics implements Serializable {
    
    /**
     * Constant version of this class.
     */
    private static final long serialVersionUID = 2907773868045621558L;
            
    /**
     * One universal random number generator is defined here for the whole
     * server so that there are not several identical streams of random
     * numbers kicking around.
     */
    private static final Random m_masterRandom;
    
    /**
     * A random number generator specific to this instance of the mechanics.
     */
    private final Random m_random;
    
    /**
     * Calculate the initial value of a stat from a pokemon's base stats and
     * hidden stats.
     *
     * @param p the pokemon whose stats to calculate
     * @param i the stat to calculate (use the constants Pokemon.S_HP, etc.)
     */
    abstract public int calculateStat(Pokemon p, int i) throws StatException;
    
    /**
     * Validate the hidden stats of a pokemon.
     */
    abstract public void validateHiddenStats(Pokemon p)
            throws ValidationException;
    
    /**
     * Randomly decide whether a move hits.
     */
    abstract public boolean attemptHit(PokemonMove move,
            Pokemon user,
            Pokemon target);
    
    /**
     * Calculate the damage done by a move.
     * Does not actually inflict damage to pokemon.
     * Optionally do not display any messages.
     */
    abstract public int calculateDamage(PokemonMove move,
            Pokemon attacker,
            Pokemon defender,
            boolean silent);
    
    /**
     * Calculate the damage done by a move.
     * Does not actually inflict damage to pokemon.
     */
    public int calculateDamage(PokemonMove move,
            Pokemon attacker,
            Pokemon defender) {
        return calculateDamage(move, attacker, defender, false);
    }
    
    /**
     * Return whether a given move deals special damage.
     */
    public abstract boolean isMoveSpecial(PokemonMove move);
    
    /**
     * Get an instance of the Random class.
     */
    public final Random getRandom() {
        return m_random;
    }
    
    /**
     * Initialise an instance of the mechanics.
     */
    public BattleMechanics(int bytes) {
        if (bytes == 4) {
            m_random = new Random();
        } else {
            m_random = getRandomSource(bytes);
        }
    }
    
    /**
     * Get random bytes using the Crypto API on Windows.
     */
    public native static byte[] getRandomBytes(int number);
    
    static {
        m_masterRandom = getRandomSource(25);
    }
    
    /**
     * Initialise the battle mechanics. Try to use the SecureRandom class for
     * the random number generator, with a seed from /dev/random. However, if
     * /dev/random is unavailable (e.g. if we are running on Windows) then an
     * instance of Random, seeded from the time, is used instead.
     *
     * For best results, use an operating system that supports /dev/random.
     */
    public static Random getRandomSource(int bytes) {
        try {
            /**File f = new File("/dev/urandom");
            byte[] seed;
            if (f.exists()) {
                InputStream input = new FileInputStream(f);
                seed = new byte[bytes];
                int read = 0;
                while (read < seed.length) {
                    read += input.read(seed, read, seed.length - read);
                }
                input.close();
            } else {
                System.out.println("Using Crypto API...");
                System.loadLibrary("ShoddyHelper");
                seed = getRandomBytes(bytes);
            }

            StringBuffer str = new StringBuffer();
            for (int i = 0; i < seed.length; ++i) {
                str.append(String.valueOf(seed[i]));
                str.append(" ");
            }
            System.out.println("SecureRandom seed: " + str.toString());
            **/return new SecureRandom(/*seed*/);
        } catch (Exception e) {
            System.out.println("Could not use /dev/random or Crypto API: " + e.getMessage());
            return new Random();
        }
    }
}
