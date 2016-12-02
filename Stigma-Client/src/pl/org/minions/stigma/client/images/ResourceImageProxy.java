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
package pl.org.minions.stigma.client.images;

/**
 * An ImageProxy that contains a resource name. Used for
 * images loaded from files.
 */
class ResourceImageProxy extends ImageProxy
{
    private String name;

    /**
     * Creates proxy for image with given name. Should be
     * used only by {@link ImageDB}.
     * @param name
     *            name of image to load
     */
    public ResourceImageProxy(String name)
    {
        this.name = name;
    }

    /**
     * Returns path/name of image. Is useful only for
     * {@link ImageDB}.
     * @return path/name of image
     */
    String getName()
    {
        return name;
    }
}
