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
package pl.org.minions.stigma.game.item;

import pl.org.minions.stigma.game.item.type.OtherType;
import pl.org.minions.stigma.game.item.type.ItemType.ItemKind;

/**
 * Main class which represents non-wearable items in game.
 */
public class OtherItem extends Item
{
    /**
     * Default constructor.
     * @param id
     *            id of item
     * @param type
     *            item type
     */
    public OtherItem(int id, OtherType type)
    {
        this(id, type, null);
    }

    /**
     * Default constructor.
     * @param id
     *            id of item
     * @param name
     *            name of item
     * @param type
     *            id of item type
     */
    public OtherItem(int id, OtherType type, String name)
    {
        super(id, type, name);
        if (!type.isStub())
            assignType(type);
    }

    /** {@inheritDoc} */
    @Override
    public OtherType getType()
    {
        return (OtherType) super.getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemKind getKind()
    {
        return ItemKind.OTHER;
    }

    /**
     * Assigns real type. Should be called when type arrived
     * and should replace stub.
     * <p>
     * Note to implementers: always remember to call {@code
     * super.assignType(type)} in subclasses.
     * @param type
     *            type to assign
     */
    public final void assignType(OtherType type)
    {
        super.assignType(type);
    }

}
