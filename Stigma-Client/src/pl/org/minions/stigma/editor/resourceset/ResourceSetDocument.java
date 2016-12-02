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

/**
 * Base document class for a parameterized object type.
 * @param <T>
 *            type of the object this document is for
 */
public abstract class ResourceSetDocument<T> extends ResourceSetViewNode
{

    private T resource;
    private ResourceSetCategory<T> resourceSetCategory;

    /**
     * Constructor.
     * @param resource
     *            resource
     * @param resourceSetCategory
     *            category of the resource
     */
    public ResourceSetDocument(T resource,
                               ResourceSetCategory<T> resourceSetCategory)
    {
        this.resource = resource;
        this.resourceSetCategory = resourceSetCategory;
    }

    /**
     * Returns the category of this document.
     * @return the category of this document
     */
    public ResourceSetCategory<T> getCategory()
    {
        return resourceSetCategory;
    }

    /**
     * Returns an initialized document editor.
     * @return editor
     */
    public ResourceEditor<T> getInitedEditor()
    {
        ResourceEditor<T> editor = getCategory().getEditor();
        editor.init(this);
        return editor;
    }

    /**
     * Returns the resource that this document wraps.
     * @return the resource
     */
    public T getResource()
    {
        return resource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof ResourceSetDocument<?>))
        {
            return false;
        }
        return getResource().equals(((ResourceSetDocument<?>) o).getResource());
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        // just for checkstyle sake
        return super.hashCode();
    }
}
