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

import java.text.MessageFormat;

import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.client.PlayerController;
import pl.org.minions.stigma.client.PlayerController.PlayerRequest;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.command.request.Move;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.game.path.Path;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.globals.Distance;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.stigma.pathfinding.PathFinder;
import pl.org.minions.utils.logger.Log;

/**
 * A request that involves reaching a specific position on
 * map.
 * <p>
 * Can be extended to create "go to and ..." requests. The
 * extending class should just call
 * {@code super.getNextCommand(a, w, c)} to reach the
 * required position before taking any other actions. Also,
 * {@link #setTargetLocation(Position)} can be used to
 * update move destination when following a moving target.
 * <p>
 * Note, that {@link #getNextCommand(Actor, World, Command)}
 * returns <code>null</code> when either the move
 * destination is <code>null</code>, reached, or unreachable
 * (the actor can not make any steps that would bring
 * him/her closer to the move destination).
 */
public class MoveToRequest implements PlayerRequest
{

    private static final String PLAYER_TRIED_TO_MOVE_TO_AN_UNPASSABLE_POSITION =
            "Player tried to move to an unpassable position.";

    private static final String PATH_TO_DESTINATION_BLOCKED =
            "Path to destination blocked.";

    private static final String PLAYER_TRIED_TO_MOVE_FROM_WHILE_OVERLOADED =
            "Player tried to move from while overloaded.";

    // Goal
    private Position destination;

    // How far from target move should end
    private Distance proximity;

    // Plan
    private Path dynamicMovePath;
    private boolean forceRecalculatePath = true;

    /**
     * Create a request for the player actor to move to
     * proximity of a specified position.
     * @param destination
     *            position to move to
     * @param proximity
     *            how far from destination move should end
     */
    public MoveToRequest(Position destination, Distance proximity)
    {
        this.destination = destination;
        this.proximity = proximity;
    }

    /**
     * Create a request for the player actor to move to a
     * specified position.
     * @param destination
     *            position to move to
     */
    public MoveToRequest(Position destination)
    {
        this(destination, null);
    }

    /**
     * Creates a MoveToRequest with an empty destination.
     * <p>
     * For use by extending classes, if move destination is
     * not known at the time of creation, or complex
     * calculations are required to determine it.
     * <p>
     * The move destination can be provided later by
     * {@link #setTargetLocation(Position)}.
     */
    protected MoveToRequest()
    {
        this(null);
    }

    /** {@inheritDoc} */
    @Override
    public Command getNextCommand(Actor playerActor,
                                  World world,
                                  Command previousCommandResponse)
    {
        updatePath(playerActor, world);
        if (dynamicMovePath != null)
            return new Move(dynamicMovePath.getNextStepDirection());
        else
            return null;
    }

    private void updatePath(Actor playerActor, World world)
    {
        final MapInstance playerMap =
                world.getMap(playerActor.getMapId(),
                             playerActor.getMapInstanceNo());

        final Position playerPosition = playerActor.getPosition();

        if (playerMap == null || playerMap.getType() == null)
        {
            dynamicMovePath = null;
            return;
        }

        if (playerPosition == null)
        {
            dynamicMovePath = null;
            return;
        }

        if (destination == null)
        {
            dynamicMovePath = null;
            return;
        }

        final PlayerController playerController =
                Client.globalInstance().getPlayerController();
        final PathFinder dynamicPathFinder =
                playerController.getDynamicPathFinder();
        final PathFinder staticPathFinder =
                playerController.getStaticPathFinder();

        //If there is a path...
        if (dynamicMovePath != null)
        {
            if (forceRecalculatePath)
                //Will calculate the path again
                ;
            //Check if path is up to date
            else if ((proximity == null || proximity.getValue() == 0)
                && !dynamicMovePath.getEnd().equals(destination))
            {
                //Not up to date, path will be calculated again
                if (Log.logger.isDebugEnabled())
                    Log.logger.debug(MessageFormat.format("End of path {0} does not match move destination {1}.",
                                                          dynamicMovePath.getEnd(),
                                                          destination));

                //Will calculate the path again
            }
            else if (proximity != null
                && proximity.getValue() != proximity.getMetric()
                                                    .getDistance(dynamicMovePath.getEnd(),
                                                                 destination))
            {
                //Not up to date, path will be calculated again
                if (Log.logger.isDebugEnabled())
                    Log.logger.debug(MessageFormat.format("End of path {0} is not within desired distance {1} from move destination {2}.",
                                                          dynamicMovePath.getEnd(),
                                                          proximity,
                                                          destination));

                //Will calculate the path again
            }
            else if (dynamicMovePath.getStart().equals(playerPosition))
            {
                //Player still at the beginning of path (standing still)
                //Check if this is because the path is not passable anymore
                if (dynamicMovePath.isPassable(dynamicPathFinder.getPassable()))
                    //Passable, will try to move again
                    return;
                else
                    //Will calculate the path again
                    ;
            }
            else if (dynamicMovePath.getNextPosition().equals(playerPosition))
            {
                //Advancing along path...
                Log.logger.trace("Following path.");
                dynamicMovePath.advance();
                if (dynamicMovePath.isEmpty())
                {
                    dynamicMovePath = null;
                }
                return;
            }

            else
            {
                //Unexpected position (player was moved by a third party)
                Log.logger.debug("Unexpected position (" + playerPosition
                    + "), current path: " + dynamicMovePath + ". Halting.");
                dynamicMovePath = null;
                return;
            }
        }

        forceRecalculatePath = false;

        //If at the required destination
        if ((proximity == null || proximity.getValue() == 0)
            && destination.equals(playerPosition))
        {
            dynamicMovePath = null;
            return;
        }

        //If within desired range
        if (proximity != null
            && proximity.getValue() > 0
            && proximity.getValue() >= proximity.getMetric()
                                                .getDistance(playerPosition,
                                                             destination))
        {
            dynamicMovePath = null;
            return;
        }

        if (playerActor.isOverloaded())
        {
            dynamicMovePath = null;
            if (Log.logger.isDebugEnabled())
                Log.logger.debug(PLAYER_TRIED_TO_MOVE_FROM_WHILE_OVERLOADED);
            return;
        }

        if (!staticPathFinder.getPassable().isPassable(destination))
        {
            dynamicMovePath = null;
            if (Log.logger.isDebugEnabled())
                Log.logger.debug(PLAYER_TRIED_TO_MOVE_TO_AN_UNPASSABLE_POSITION);
            return;
        }

        //Calculate path

        final Path staticMovePath;

        if (destination.isNeighbor(playerPosition))
        {
            //If destination is close do not evoke path finding
            staticMovePath = new Path(playerPosition);
            dynamicMovePath = staticMovePath;
            staticMovePath.add(destination);
        }
        else
        {
            //Evoke path finding for other cases, overwriting current path if exist
            staticMovePath =
                    staticPathFinder.generatePath(playerPosition,
                                                  destination,
                                                  proximity);
            dynamicMovePath = staticMovePath;

            if (staticMovePath == null)
            {
                Log.logger.debug("No path to destination.");
                return;
            }
        }

        if (!staticMovePath.isPassable(dynamicPathFinder.getPassable())
            && staticMovePath.size() > 1)
        {
            //Map type-derived path is blocked, calculate a dynamic path

            forceRecalculatePath = true;
            dynamicMovePath =
                    dynamicPathFinder.generatePath(playerPosition,
                                                   destination,
                                                   proximity);
            if (dynamicMovePath != null)
                return;

            //No path to destination currently exists

            Log.logger.debug(PATH_TO_DESTINATION_BLOCKED);
            dynamicMovePath =
                    staticMovePath.getPassableSubPath(dynamicPathFinder.getPassable());

            if (!dynamicMovePath.isEmpty())
                return;

            //Cannot advance using map type-derived path (next step is blocked)

            dynamicMovePath = null;
            while (staticMovePath.size() > 1 && dynamicMovePath == null)
            {
                staticMovePath.advance();
                dynamicMovePath =
                        dynamicPathFinder.generatePath(playerPosition,
                                                       staticMovePath.getNextPosition());
            }

            if (dynamicMovePath == null)
            {
                Log.logger.debug("Can not advance towards move destination.");
            }

        }

    }

    /** {@inheritDoc} */
    @Override
    public Position getTargetLocation()
    {
        return destination;
    }

    /**
     * Changes the move destination.
     * <p>
     * As it is not synchronized, it should be called from
     * within the same thread as
     * {@link #getNextCommand(Actor, World, Command)}
     * method.
     * @param position
     *            new target position
     */
    protected void setTargetLocation(Position position)
    {
        this.destination = position;
        forceRecalculatePath = true;

    }

    /**
     * Returns proximity.
     * @return proximity
     */
    public Distance getProximity()
    {
        return proximity;
    }

    /**
     * Sets required distance to target.
     * <p>
     * As it is not synchronized, it should be called from
     * within the same thread as
     * {@link #getNextCommand(Actor, World, Command)}
     * method.
     * @param proximity
     *            the required distance to target
     */
    protected void setProximity(Distance proximity)
    {
        this.proximity = proximity;
        forceRecalculatePath = true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isInterruptible()
    {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "MoveTo("
            + (proximity != null ? "within " + proximity + " of " : "")
            + destination + ")";
    }
}
