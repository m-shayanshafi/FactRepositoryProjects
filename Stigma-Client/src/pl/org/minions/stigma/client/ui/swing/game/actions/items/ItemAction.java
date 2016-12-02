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
package pl.org.minions.stigma.client.ui.swing.game.actions.items;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import pl.org.minions.stigma.game.item.Item;

/**
 * Base class for swing {@link Action actions} that are used
 * to interact with items.
 */
public abstract class ItemAction extends AbstractAction
{
    private static final long serialVersionUID = 1L;

    private Item item;

    /**
     * Creates a new ItemAction with given name,
     * description, and icon.
     * @param name
     *            action name
     * @param description
     *            action short description
     * @param icon
     *            action icon
     */
    ItemAction(String name, String description, Icon icon)
    {
        super(name, icon);
        setItem(null);
        putValue(SHORT_DESCRIPTION, description);
    }

    /**
     * Creates a new ItemAction with given name and
     * description.
     * @param name
     *            action name
     * @param description
     *            action short description
     */
    ItemAction(String name, String description)
    {
        super(name);
        setItem(null);
        putValue(SHORT_DESCRIPTION, description);
    }

    /**
     * Sets new value of item.
     * @param item
     *            the item to set
     */
    public void setItem(Item item)
    {
        this.item = item;
        setEnabled(shouldBeEnabled());
    }

    /**
     * Returns item.
     * @return item
     */
    public Item getItem()
    {
        return item;
    }

    /**
     * Called after item changes to update enabled state.
     * <p>
     * Default implementation returns <code>false</code> for
     * <code>null</code> or non-complete items.
     * @return <code>true</code> if the action should be
     *         enabled, <code>false</code> otherwise
     */
    protected boolean shouldBeEnabled()
    {
        return getItem() != null && getItem().isComplete();
    }
}
