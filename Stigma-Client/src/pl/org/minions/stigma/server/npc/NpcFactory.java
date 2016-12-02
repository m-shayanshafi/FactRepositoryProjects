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

import java.util.LinkedList;
import java.util.List;

import pl.org.minions.stigma.databases.actor.ArchetypeDB;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.actor.Archetype;
import pl.org.minions.stigma.game.actor.ArchetypeBuilder;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.stigma.server.ai.AiScript;
import pl.org.minions.stigma.server.ai.AiScriptFactory;
import pl.org.minions.stigma.server.managers.ActorManager;
import pl.org.minions.stigma.server.managers.AiManager;
import pl.org.minions.stigma.server.managers.ChatManager;
import pl.org.minions.stigma.server.managers.WorldManager;

/**
 * Utility class for building {@link ActorManager} instances
 * for NPCs.
 */
public final class NpcFactory
{
    private NpcFactory()
    {
    }

    /**
     * Build manager for NPC (also creates its {@link Actor}
     * ). Takes every parameter that must be set for actor
     * to be properly placed on map.
     * @param worldManager
     *            world manager to which actor manager
     *            should be connected
     * @param chatManager
     *            chat manager to which actor manager
     *            should be connected
     * @param npcDescription
     *            description of NPC
     * @param mapId
     *            id of map on which NPC will/should be
     *            placed
     * @param mapInstanceNo
     *            instance number of map on which NPC
     *            will/should be placed
     * @param position
     *            position of NPC on map
     * @return new instance of {@link ActorManager}
     */
    public static ActorManager buildNpc(WorldManager worldManager,
                                        ChatManager chatManager,
                                        NpcDescription npcDescription,
                                        short mapId,
                                        short mapInstanceNo,
                                        Position position)
    {
        Actor a =
                new Actor(NpcIdRegistry.globalInstance().nextId(),
                          npcDescription.getName());
        a.setMapId(mapId);
        a.setMapInstanceNo(mapInstanceNo);
        a.setPosition(position);

        List<Archetype> archetypes = new LinkedList<Archetype>();
        ArchetypeDB archetypeDB = worldManager.getArchetypeDB();
        for (short s : npcDescription.getArchetypes())
        {
            Archetype arch = archetypeDB.getArchetype(s);
            if (arch != null)
                archetypes.add(arch);
        }
        ArchetypeBuilder.accumulate(a, archetypes);

        AiScript script =
                AiScriptFactory.globalInstance()
                               .buildAiScript(npcDescription.getAiDescription());
        ActorManager m = new AiManager(worldManager, chatManager, a, script);
        return m;
    }
}
