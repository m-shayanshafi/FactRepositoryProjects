/*
 * SpriteLoader.java
 *
 * Created on January 28, 2007, 1:20 PM
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
 */

package shoddybattleclient;
import java.awt.Image;
import java.io.IOException;

class SpriteLoaderException extends Exception {
    public SpriteLoaderException(Exception e) {
        super(e);
    }
}

/**
 *
 * @author Colin
 */
public abstract class SpriteLoader {
    
    private static SpriteLoader m_loader = null;
    
    /**
     * Return the number of pokemon that this sprite loader can load.
     */
    public static int getPokemonCount() {
        if (m_loader == null)
            return -1;
        return m_loader.getPokemonQuantity();
    }
    
    /**
     * Return the number of pokemon that this sprite loader can load.
     */
    public abstract int getPokemonQuantity();
    
    /**
     * Loads an image using the underlying sprite loader.
     */
    public abstract Image loadImage(String path, boolean front, boolean male, boolean shiny) throws IOException;
    
    /**
     * Load an image from the sprite loader set via setSpriteLoader().
     * Returns null if no sprite loader has been set.
     */
    public static Image getImage(String path, boolean front, boolean male, boolean shiny) throws IOException {
        if (m_loader == null) {
            return null;
        }
        return m_loader.loadImage(path, front, male, shiny);
    }
    
    /**
     * Set the class name from which we will load sprites. The class must
     * extend the SpriteLoader class.
     */
    public static void setSpriteLoader(String className) throws SpriteLoaderException {
        try {
            m_loader = (SpriteLoader)Class.forName(className).newInstance();
        } catch (Exception e) {
            throw new SpriteLoaderException(e);
        }
    }
    
}
