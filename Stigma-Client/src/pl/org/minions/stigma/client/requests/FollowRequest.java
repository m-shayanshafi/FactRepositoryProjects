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
package pl.org.minions.stigma.client.requests;

import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.command.special.IdleCommand;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.globals.Distance;
import pl.org.minions.stigma.globals.StandardMetrics;

/**
 * A request to follow an actor around, trying to be within
 * a given distance as it moves.
 */
public class FollowRequest extends MoveToRequest
{

    private static final Distance ONE_STEP_AWAY =
            StandardMetrics.STEPS.createDistance(1);

    private Actor target;

    /**
     * Creates a new request to approach the target actor at
     * the given distance.
     * @param target
     *            target to follow
     * @param proximity
     *            approach distance
     */
    public FollowRequest(Actor target, Distance proximity)
    {
        super(target.getPosition(), proximity);
        this.target = target;
    }

    /**
     * Creates a new request to approach the target actor at
     * a distance of one step.
     * @param target
     *            target to follow
     */
    public FollowRequest(Actor target)
    {
        this(target, ONE_STEP_AWAY);
    }

    /** {@inheritDoc} */
    @Override
    public Command getNextCommand(Actor playerActor,
                                  World world,
                                  Command previousCommandResponse)
    {
        if (target.getPosition() != getTargetLocation())
            setTargetLocation(target.getPosition());

        Command nextCommand =
                super.getNextCommand(playerActor,
                                     world,
                                     previousCommandResponse);
        if (nextCommand == null)
            nextCommand = new IdleCommand();

        return nextCommand;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Follow("
            + (getProximity() != null ? "within " + getProximity() + " of #"
                                     : "#") + target.getId() + ")";
    }
}
