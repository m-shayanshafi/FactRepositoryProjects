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

import java.util.Arrays;
import java.util.EnumSet;

import javax.swing.RowFilter;

import pl.org.minions.stigma.game.item.type.ItemType.ItemKind;

/**
 * Row filter to be used with {@link ItemTableModel} .
 * Filters based on {@link ItemKind}.
 */
public class ItemKindRowFilter extends RowFilter<ItemTableModel, Integer>
{
    /**
     * Filter for selecting only weapons.
     */
    public static final ItemKindRowFilter WEAPON_FILTER =
            new ItemKindRowFilter(ItemKind.WEAPON);
    /**
     * Filter for selecting only armors.
     */
    public static final ItemKindRowFilter ARMOR_FILTER =
            new ItemKindRowFilter(ItemKind.ARMOR);
    /**
     * Filter for selecting only other items.
     */
    public static final ItemKindRowFilter OTHER_FILTER =
            new ItemKindRowFilter(ItemKind.OTHER);

    /**
     * Filter for selecting equipment.
     */
    public static final ItemKindRowFilter EQUIPMENT_FILTER =
            new ItemKindRowFilter(ItemKind.ARMOR, ItemKind.WEAPON);

    private EnumSet<ItemKind> itemKinds;

    /**
     * Creates a filter including selected item kind.
     * @param itemKind
     *            item kind to include
     */
    public ItemKindRowFilter(ItemKind itemKind)
    {
        this(EnumSet.of(itemKind));
    }

    /**
     * Creates filter including selected item kinds.
     * @param itemKinds
     *            item kinds to include
     */
    public ItemKindRowFilter(ItemKind... itemKinds)
    {
        this(EnumSet.copyOf(Arrays.asList(itemKinds)));
    }

    /**
     * Creates filter including selected item kinds.
     * @param itemKinds
     *            item kinds to include
     */
    public ItemKindRowFilter(EnumSet<ItemKind> itemKinds)
    {
        super();
        this.itemKinds = itemKinds;
    }

    /** {@inheritDoc} */
    @Override
    public boolean include(RowFilter.Entry<? extends ItemTableModel, ? extends Integer> entry)
    {
        final ItemTableEntry itemEntry = (ItemTableEntry) entry.getValue(0);
        //ItemTableModel returns an ItemTableEntry for any column.
        return itemKinds.contains(itemEntry.getItem().getKind());
    }

    /**
     * Returns set of included item kinds.
     * @return copy of included item kinds
     */
    public EnumSet<ItemKind> getItemKinds()
    {
        return itemKinds.clone();
    }
}
