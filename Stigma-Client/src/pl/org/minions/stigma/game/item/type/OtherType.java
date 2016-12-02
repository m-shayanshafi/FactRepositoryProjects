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
package pl.org.minions.stigma.game.item.type;

import pl.org.minions.stigma.game.item.LogicalSlotType;

/**
 * Other items.
 */
public class OtherType extends ItemType
{
    /**
     * Default constructor for itemType, protected because
     * ItemType is abstract.
     * @param id
     *            id of item type
     * @param weight
     *            base weight of item
     * @param value
     *            base value of item
     * @param name
     *            base name of item
     * @param description
     *            base description of item type
     * @param onGroundIcon
     *            path to icon representing item laying on
     *            ground
     * @param inventoryIcon
     *            path to icon representing item in
     *            inventory
     */
    public OtherType(short id,
                     short weight,
                     int value,
                     String name,
                     String description,
                     String onGroundIcon,
                     String inventoryIcon)
    {
        super(id,
              weight,
              value,
              name,
              description,
              onGroundIcon,
              inventoryIcon,
              LogicalSlotType.NO_SLOT_TYPE);

        // TODO Features of non-wearable items, stocking maybe?
    }

    /** {@inheritDoc} */
    @Override
    public ItemKind getKind()
    {
        return ItemKind.OTHER;
    }

}
