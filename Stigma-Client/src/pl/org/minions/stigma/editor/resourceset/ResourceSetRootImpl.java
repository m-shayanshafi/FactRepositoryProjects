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

import javax.swing.Icon;

import pl.org.minions.stigma.editor.gui.GUIConstants;

/**
 * Basic implementation of the ResourceSetRoot.
 */
public class ResourceSetRootImpl extends ResourceSetRoot
{

    private ResourceSet resourceSet;

    private List<ResourceSetCategory<?>> resourceSetCategories;

    /**
     * Constructor.
     * @param resourceSetCategories
     *            available categories.
     */
    public ResourceSetRootImpl(List<ResourceSetCategory<?>> resourceSetCategories)
    {
        this.resourceSetCategories = resourceSetCategories;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ResourceSetCategory<?>> getCategories()
    {
        return resourceSetCategories;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription()
    {
        return resourceSet.getDescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Icon getIcon()
    {
        return GUIConstants.BUNNY_ICON;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return resourceSet.getName();
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSet getResourceSet()
    {
        return resourceSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(ResourceSet resourceSet)
    {
        this.resourceSet = resourceSet;
        for (ResourceSetCategory<?> resourceSetCategory : resourceSetCategories)
        {
            resourceSetCategory.init(resourceSet);
        }
    }

}
