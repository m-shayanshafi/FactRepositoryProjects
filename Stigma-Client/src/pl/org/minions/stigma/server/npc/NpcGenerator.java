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
package pl.org.minions.stigma.server.npc;

import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.server.managers.ChatManager;
import pl.org.minions.stigma.server.managers.WorldManager;

/**
 * Base class representing NPCs "layer" over map. Children
 * of this class should provide various ways to put NPCs on
 * given map.
 */
public interface NpcGenerator
{
    /**
     * Puts NPCs on given {@link MapInstance} instance.
     * @param worldManager
     *            world manager to which NPC should connect
     * @param chatManager
     *            chat manager to which NPC should connect
     * @param m
     *            map on which NPCs should be put
     * @return {@code true} when everything went properly
     */
    boolean putNpcs(WorldManager worldManager,
                    ChatManager chatManager,
                    MapInstance m);

}
