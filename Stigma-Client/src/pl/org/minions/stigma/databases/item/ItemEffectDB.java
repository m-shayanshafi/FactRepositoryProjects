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
package pl.org.minions.stigma.databases.item;

import pl.org.minions.stigma.game.item.effect.ItemEffect;

/**
 * Database containing items effects.
 */
public interface ItemEffectDB
{
    /**
     * Name of items database root directory.
     */
    String MAIN_DB_DIR = "items";
    /**
     * Name of effects database sub-directory.
     */
    String EFFECT_DB_DIR = "effects";

    String EFFECT_FILE_PREFIX = "effect";

    /**
     * Returns item effect with selected id.
     * @param id
     *            id of item effect
     * @return item effect
     */
    ItemEffect getEffect(short id);
}
