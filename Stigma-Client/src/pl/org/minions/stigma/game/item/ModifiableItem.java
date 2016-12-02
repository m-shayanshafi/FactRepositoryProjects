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

import pl.org.minions.stigma.game.item.modifier.ItemModifier;

/**
 * Interface for all item that can be modified.
 * @param <T>
 *            type of modifier
 */
public interface ModifiableItem<T extends ItemModifier>
{
    /**
     * Adds modifier to item. If item contains modifier with
     * same category modifier is not added and false is
     * returned.
     * @param modifier
     *            to add
     * @return true if modifier was added successfully false
     *         otherwise
     */
    boolean addModifier(T modifier);

    /**
     * Removes modifier from item.
     * @param modifier
     *            to remove
     * @return true if modifier was removed successfully
     *         false otherwise
     */
    boolean removeModifier(T modifier);
}
