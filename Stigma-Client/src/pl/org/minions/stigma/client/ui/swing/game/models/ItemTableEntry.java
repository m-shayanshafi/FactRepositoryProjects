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
package pl.org.minions.stigma.client.ui.swing.game.models;

import pl.org.minions.stigma.game.item.Item;

/**
 * Item table entry that contains information about recency
 * of the entry.
 * <p>
 * Note: this class has a natural ordering that is
 * inconsistent with equals
 */
public class ItemTableEntry implements Comparable<ItemTableEntry>
{
    private boolean recent = true;
    private int sequentialNumber;
    private Item item;

    /**
     * Constructor.
     * @param item
     *            item in this entry
     * @param seqentialNumber
     *            the ordering of this entry within table
     *            model
     */
    public ItemTableEntry(Item item, int seqentialNumber)
    {
        this.item = item;
        this.sequentialNumber = seqentialNumber;
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(ItemTableEntry o)
    {
        return sequentialNumber - o.sequentialNumber;
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
     * Returns recent.
     * @return recent
     */
    public boolean isRecent()
    {
        return recent;
    }

    /**
     * Sets new value of recent.
     * @param recent
     *            the recent to set
     */
    public void setRecent(boolean recent)
    {
        this.recent = recent;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Item)
            return obj.equals(item);
        else
            return super.equals(obj);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        return item.hashCode();
    }
}
