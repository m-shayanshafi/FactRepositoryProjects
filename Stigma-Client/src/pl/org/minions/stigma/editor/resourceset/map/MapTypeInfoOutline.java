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

import pl.org.minions.stigma.editor.resourceset.ResourceEditorOutline;
import pl.org.minions.stigma.editor.resourceset.ResourceSetDocument;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.utils.i18n.Translated;

/**
 * Properties outline of the map type editor.
 */
public class MapTypeInfoOutline extends ResourceEditorOutline
{

    private static final long serialVersionUID = 1L;

    @Translated
    private static String INFO_LABEL = "Info";

    private MapTypePropertiesPanel mapTypePropertiesPanel;

    /**
     * Default constructor.
     */
    public MapTypeInfoOutline()
    {
        mapTypePropertiesPanel = new MapTypePropertiesPanel();
        this.add(mapTypePropertiesPanel);
    }

    /** {@inheritDoc} */
    @Override
    public String getName()
    {
        return INFO_LABEL;
    }

    /**
     * Initializes info outline with a map type document.
     * @param mapTypeDocument
     *            map type document
     */
    public void init(ResourceSetDocument<MapType> mapTypeDocument)
    {
        mapTypePropertiesPanel.init(mapTypeDocument.getResource());
    }

}
