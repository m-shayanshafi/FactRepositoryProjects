/*
 * SpriteClassLoader.java
 *
 * Created on June 25, 2007, 9:27 PM
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

package shoddybattleclient;

/**
 *
 * @author Colin
 */
public class SpriteClassLoader extends ClassLoader {

    private static final SpriteClassLoader m_instance = new SpriteClassLoader();
    
    /**
     * Return the unique instance of the sprite class loader.
     * @return singleton instance of SpriteClassLoader
     */
    public static SpriteClassLoader getSpriteClassLoader() {
        return m_instance;
    }
    
    /**
     * Load the specified class from the client's mod data folder.
     */
    protected Class findClass(String name) throws ClassNotFoundException {
        int slash = name.lastIndexOf('/');
        String folder = name.substring(slash + 1);
        
        return null;
    }
    
    /** Creates a new instance of SpriteClassLoader */
    public SpriteClassLoader() {
    }

}
