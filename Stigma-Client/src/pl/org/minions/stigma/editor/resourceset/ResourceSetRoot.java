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

import java.util.List;

/**
 * Base class for the root of the resource set.
 */
public abstract class ResourceSetRoot extends ResourceSetViewNode
{

    /**
     * Returns categories available within this resource
     * set.
     * @return categories available within this resource set
     */
    public abstract List<ResourceSetCategory<?>> getCategories();

    /**
     * Returns the resource set.
     * @return resource set
     */
    public abstract ResourceSet getResourceSet();

    /**
     * Inits this resource set root with a given resource
     * set.
     * @param resourceSet
     *            resource set
     */
    public abstract void init(ResourceSet resourceSet);
}
