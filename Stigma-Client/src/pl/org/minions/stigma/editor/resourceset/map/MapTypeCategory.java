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

import java.util.Collection;
import java.util.Set;

import javax.swing.Icon;

import pl.org.minions.stigma.editor.gui.GUIConstants;
import pl.org.minions.stigma.editor.resourceset.NewElementWizard;
import pl.org.minions.stigma.editor.resourceset.ResourceEditor;
import pl.org.minions.stigma.editor.resourceset.ResourceEditorOutline;
import pl.org.minions.stigma.editor.resourceset.ResourceSet;
import pl.org.minions.stigma.editor.resourceset.ResourceSetCategory;
import pl.org.minions.stigma.editor.resourceset.ResourceSetDocument;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.utils.i18n.Translated;

/**
 * Category class for {@code}MapType{/@code} objects.
 */
public class MapTypeCategory extends ResourceSetCategory<MapType>
{

    @Translated
    private static String DESCRIPTION_LABEL = "Map types";
    @Translated
    private static String CATEGORY_NAME_LABEL = "Maps";

    /** {@inheritDoc} */
    @Override
    public String getDescription()
    {
        return DESCRIPTION_LABEL;
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSetDocument<MapType> getDocument(MapType object)
    {
        return new MapTypeDocument(object, this);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceEditor<MapType> getEditor()
    {
        return new MapTypeEditor();
    }

    /** {@inheritDoc} */
    @Override
    public Icon getIcon()
    {
        return GUIConstants.MAP_ICON;
    }

    /** {@inheritDoc} */
    @Override
    public String getName()
    {
        return CATEGORY_NAME_LABEL;
    }

    /** {@inheritDoc} */
    @Override
    public NewElementWizard getNewElementWizard()
    {
        return new MapTypeWizard();
    }

    /** {@inheritDoc} */
    @Override
    public Collection<MapType> getObjects(ResourceSet resourceSet)
    {
        return resourceSet.getMapTypes();
    }

    /** {@inheritDoc} */
    @Override
    public Set<ResourceEditorOutline> getOutlines()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
