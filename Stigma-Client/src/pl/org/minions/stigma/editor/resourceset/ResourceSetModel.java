/**
 *   Stigma - Multiplayer online RPG - http://stigma.sourceforge.net
 *   Copyright (C) 2005-2009 Minions Studio
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */
package pl.org.minions.stigma.editor.resourceset;

import java.io.File;
import java.net.URI;

/**
 * Singleton class for the resource set - there should be
 * only one in the editor.
 */
public final class ResourceSetModel
{

    private static ResourceSetModel INSTANCE;

    private ResourceSet resourceSet;

    private ResourceSetModel()
    {

    }

    /**
     * Returns the instance of the ResourceSetModel.
     * @return the instance of the ResourceSetModel
     */
    public static ResourceSetModel getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new ResourceSetModel();
        }
        return INSTANCE;
    }

    /**
     * Returns resource set.
     * @return resource set
     */
    public ResourceSet getResourceSet()
    {
        return resourceSet;
    }

    /**
     * Tells if the resource set was modified.
     * @return was modified
     */
    public boolean getWasModified()
    {
        return resourceSet.isModified();
    }

    /**
     * Loads the resource set from files.
     * @param path
     *            path to the root directory of the resource
     *            set.
     */
    public void loadResourceSet(String path)
    {
        loadResourceSet(new File(path).toURI());
    }

    /**
     * Loads the resource set from files.
     * @param path
     *            path to the root directory of the resource
     *            set.
     */
    public void loadResourceSet(URI path)
    {
        resourceSet = new ResourceSet(path);
    }

    /**
     * Creates a new resource set.
     * @param path
     *            path to the root directory of the resource
     *            set.
     */
    public void newResourceSet(String path)
    {

    }

    /**
     * Saves the resource set to files.
     */
    public void saveResourceSet()
    {
        resourceSet.save();
    }
}
