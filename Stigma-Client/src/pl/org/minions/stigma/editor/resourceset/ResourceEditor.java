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

import javax.swing.JPanel;

/**
 * Base class for a resource editor panel.
 * @param <T>
 *            type of entity that this editor works on
 */
public abstract class ResourceEditor<T> extends JPanel
{

    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object editor)
    {
        if (editor instanceof ResourceEditor<?>)
        {
            return this.getDocument()
                       .getName()
                       .equals(((ResourceEditor<?>) editor).getDocument()
                                                           .getName());
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns resource set document used by this editor.
     * @return resource set document used by this editor
     */
    public abstract ResourceSetDocument<T> getDocument();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract String getName();

    /**
     * Returns outlines connected to this editor.
     * @return set of outlines connected to this editor
     */
    public abstract List<? extends ResourceEditorOutline> getOutlines();

    /**
     * Notifies the editor about outline selection change.
     * @param outline
     *            outline to select
     */
    public abstract void outlineSelected(ResourceEditorOutline outline);

    /**
     * Returns resource used by this editor.
     * @return resource used by this editor
     */
    public abstract T getResource();

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return this.getDocument().getName().hashCode();

    }

    /**
     * Init this editor with a new resource.
     * @param resourceSetDocument
     *            resource to be inited with
     */
    public abstract void init(ResourceSetDocument<T> resourceSetDocument);
}
