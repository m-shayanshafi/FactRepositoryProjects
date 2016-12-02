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

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.command.request.Move;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.globals.Direction;
import pl.org.minions.stigma.globals.Position;

/**
 * Class implementing default (testing) AI behavior.
 */
public class DefaultAiScript extends AiScript
{
    private enum State
    {
        IDLE, MOVING
    };

    private State state = State.IDLE;
    private Random randomGen = new Random();
    private Command lastCommand;

    private Command move(Actor currentActor, World world)
    {
        MapInstance m =
                world.getMap(currentActor.getMapId(),
                             currentActor.getMapInstanceNo());

        Position p = currentActor.getPosition();
        List<Direction> dirList = new LinkedList<Direction>();
        for (Direction d : Direction.getValuesArray())
        {
            if (d != Direction.NONE && m.isPassable(p.newPosition(d)))
                dirList.add(d);
        }

        if (dirList.isEmpty())
        {
            state = State.IDLE;
            return null;
        }

        Direction dir;
        if (dirList.size() == 1)
            dir = dirList.get(0);
        else
            dir = dirList.get(randomGen.nextInt(dirList.size()));
        state = State.MOVING;
        return new Move(dir);
    }

    /** {@inheritDoc} */
    @Override
    public Command react(Actor currentActor, World world, Command command)
    {
        switch (state)
        {
            case IDLE:
                return lastCommand = move(currentActor, world);
            case MOVING:
                // command is back (failed or not)
                if (lastCommand == command)
                {
                    return lastCommand = move(currentActor, world);
                }
                break;
            default:
                assert "This shouldn't happened".equals(null);
                return null;
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public AiScript create()
    {
        return new DefaultAiScript();
    }
}
