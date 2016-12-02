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
package pl.org.minions.stigma.server.ai;

import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.world.World;

/**
 * Basic interface representing AI.
 */
public abstract class AiScript
{
    /**
     * Creates a new instance of AiScript of this type.
     * @return new AiScript implementation instance
     */
    public abstract AiScript create();

    /**
     * Initializes script using given description. Default
     * implementation is empty.
     * @param aiDescription
     *            description of AI (parameters for script)
     */
    public void initialize(AiDescription aiDescription)
    {

    }

    /**
     * Returns AI's reaction on given command or {@code
     * null} if no such is needed.
     * @param currentActor
     *            actor for which reaction is needed
     * @param world
     *            world in which NPC is "living"
     * @param command
     *            command to react on
     * @return reaction on given command
     */
    public abstract Command react(Actor currentActor,
                                  World world,
                                  Command command);

}
