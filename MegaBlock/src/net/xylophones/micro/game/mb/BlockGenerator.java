/*
 * BlockGenerator.java
 * 
 * Copyright 2007 William Robertson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package net.xylophones.micro.game.mb;

import java.util.Random;

/**
 * Class for generating a random tetrad
 * 
 * @todo - this shouldn't be completely random
 *
 * @author william@xylophones.net
 */
public class BlockGenerator {
    
	/**
	 * Used for generating rendom numbers
	 */
    Random random;
    
    /**
     * Integer array holding all possible tetrad 
     * identifiers - one of the {@code TETRAD_X} constants 
     * in the {@code Block} class 
     */
    int[] possibleTetrads = Block.getPossibleTetrads();

    /**
     * Creates a new instance of BlockGenerator
     */
    public BlockGenerator() {
        random = new Random( System.currentTimeMillis() );
    }
    
    /**
     * Get a random tetrad
     * 
     * Returns an integer identifier which corresponds to
     * one of the {@code TETRAD_X} constants in the {@code Block} class 
     *
     * @return identifier
     * 
     * @see net.xylophones.micro.game.mb.Block
     */
    public int randomTetrad() {
        int tetrad = random.nextInt(possibleTetrads.length);
        
        return possibleTetrads[tetrad];
    }
    
}
