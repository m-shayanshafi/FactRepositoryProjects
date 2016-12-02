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
package pl.org.minions.stigma.editor.resourceset.terrainset;

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
import pl.org.minions.stigma.game.map.TerrainSet;

/**
 * Category class for {@code}TerrainSet{/@code} objects.
 */
public class TerrainSetCategory extends ResourceSetCategory<TerrainSet>
{

    /** {@inheritDoc} */
    @Override
    public String getDescription()
    {
        //TODO: add NLS
        return "Terrain sets description";
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSetDocument<TerrainSet> getDocument(TerrainSet object)
    {
        return new TerrainSetDocument(object, this);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceEditor<TerrainSet> getEditor()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Icon getIcon()
    {
        return GUIConstants.BUNNY_ICON;
    }

    /** {@inheritDoc} */
    @Override
    public String getName()
    {
        //TODO: add NLS
        return "Terrain sets";
    }

    /** {@inheritDoc} */
    @Override
    public NewElementWizard getNewElementWizard()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Collection<TerrainSet> getObjects(ResourceSet resourceSet)
    {
        return resourceSet.getTerrainSets();
    }

    /** {@inheritDoc} */
    @Override
    public Set<ResourceEditorOutline> getOutlines()
    {
        // TODO Auto-generated method stub
        return null;
    }

}