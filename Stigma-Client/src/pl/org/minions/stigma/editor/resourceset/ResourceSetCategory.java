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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Base category class for a parameterized object type.
 * @param <T>
 *            type of the object this category is for
 */
public abstract class ResourceSetCategory<T> extends ResourceSetViewNode
{

    private List<ResourceSetDocument<T>> elements =
            new ArrayList<ResourceSetDocument<T>>();

    /**
     * Returns proper document based on the given object.
     * @param object
     *            object to be wrapped by the proper
     *            document
     * @return proper document
     */
    public abstract ResourceSetDocument<T> getDocument(T object);

    /**
     * Returns a default {@code}ResourceEditor{/@code} for
     * type T.
     * @return default editor
     */
    public abstract ResourceEditor<T> getEditor();

    /**
     * Returns elements of this category.
     * @return elements of this category.
     */
    public List<? extends ResourceSetDocument<T>> getElements()
    {
        return elements;
    }

    /**
     * Returns new element wizard for type T.
     * @return new element wizard for type T.
     */
    public abstract NewElementWizard getNewElementWizard();

    /**
     * Returns collection of objects of type T from the
     * resource set.
     * @param resourceSet
     *            resource set
     * @return collection of objects of type T
     */
    public abstract Collection<T> getObjects(ResourceSet resourceSet);

    /**
     * Returns set of outlines available for type T.
     * @return outlines available for type T.
     */
    public abstract Set<ResourceEditorOutline> getOutlines();

    /**
     * Inits the category elements based on the resourceSet.
     * @param resourceSet
     *            resource set
     */
    public void init(ResourceSet resourceSet)
    {
        elements.clear();
        for (T object : this.getObjects(resourceSet))
        {
            elements.add(getDocument(object));
        }
    }

    /**
     * Reinits category with elements from resource set.
     * @param resourceSet
     *            resource set
     * @return collection of ResourceSetDocument's added to
     *         this this category
     */
    public Collection<? extends ResourceSetDocument<T>> reinit(ResourceSet resourceSet)
    {
        List<ResourceSetDocument<T>> addedElements =
                new ArrayList<ResourceSetDocument<T>>();
        for (T object : this.getObjects(resourceSet))
        {
            ResourceSetDocument<T> document = getDocument(object);
            if (!elements.contains(document))
            {
                elements.add(document);
                addedElements.add(document);
            }
        }
        return addedElements;
    }
}
