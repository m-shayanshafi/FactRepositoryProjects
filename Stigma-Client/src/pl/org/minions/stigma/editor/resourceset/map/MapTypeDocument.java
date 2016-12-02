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
package pl.org.minions.stigma.editor.resourceset.map;

import javax.swing.Icon;

import pl.org.minions.stigma.editor.gui.GUIConstants;
import pl.org.minions.stigma.editor.resourceset.ResourceSetCategory;
import pl.org.minions.stigma.editor.resourceset.ResourceSetDocument;
import pl.org.minions.stigma.game.map.MapType;

/**
 * Document class wrapping {@code}MapType{/@code} object.
 */
public class MapTypeDocument extends ResourceSetDocument<MapType>
{

    /**
     * Constructor.
     * @param resource
     *            wrapped map
     * @param resourceSetCategory
     *            resource set category that this document
     *            belongs to
     */
    public MapTypeDocument(MapType resource,
                           ResourceSetCategory<MapType> resourceSetCategory)
    {
        super(resource, resourceSetCategory);
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription()
    {
        return getResource().getDescription();
    }

    /** {@inheritDoc} */
    @Override
    public Icon getIcon()
    {
        return GUIConstants.MAP_ICON_16PX;
    }

    /** {@inheritDoc} */
    @Override
    public String getName()
    {
        return getResource().getName();
    }

}