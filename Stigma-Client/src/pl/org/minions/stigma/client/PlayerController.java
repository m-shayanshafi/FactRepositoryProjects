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
package pl.org.minions.stigma.client;

import java.awt.Dimension;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import pl.org.minions.stigma.client.MapLoader.MapLoadingObsrever;
import pl.org.minions.stigma.client.ui.event.listeners.CooldownChangedListener;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.command.CommandType;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.stigma.game.path.MapPassable;
import pl.org.minions.stigma.game.path.MapTypePassable;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.globals.GlobalConfig;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.stigma.network.client.ClientGlobalConnector;
import pl.org.minions.stigma.pathfinding.PathFinder;
import pl.org.minions.stigma.pathfinding.client.AStarPathFinder;
import pl.org.minions.stigma.pathfinding.client.ManhattanHeuristic;
import pl.org.minions.utils.logger.Log;

/**
 * Class that translate higher level player controls into
 * {@link Command Commands} and sends them to server.
 */
public class PlayerController implements
                             CooldownChangedListener,
                             MapLoadingObsrever
{
    private static final double COOLDOWN_GRACE_PERIOD = 0.1;

    private static final int POTENTIALY_VISIBLE_SEGMENTS = 2;

    private class ControllerThread implements Runnable
    {
        /** {@inheritDoc} */
        @Override
        public void run()
        {
            while (!Thread.interrupted())
            {
                try
                {
                    processRequests();
                    Thread.yield();
                }
                catch (InterruptedException e)
                {
                    return;
                }
            }
        }
    }

    /**
     * An interface for player requests, that represent a
     * goal the player wishes to achieve (ie. reaching a
     * specific position on map).
     * <p>
     * The current request is queried for next
     * {@link Command} (action to take) when it is issued
     * and every time the previous issued Command returns
     * from server (either executed successfully or not).
     */
    public interface PlayerRequest
    {
        /**
         * Returns the next {@link Command} that should be
         * taken to reach the requested goal.
         * <p>
         * Returning <code>null</code> command means that
         * either the requested goal is unreachable or it
         * was already reached. Either way, this request
         * will probably not be queried any more.
         * <p>
         * This method is called after a response to
         * previously sent command returns from server and
         * not necessarily after player actor cooldown
         * reaches <code>0</code>. The returned command is
         * then sent to server.
         * @param playerActor
         *            current player-controlled actor
         * @param world
         *            game world
         * @param previousCommandResponse
         *            response to previously issued command
         * @return next Command to send or <code>null</code>
         *         if no action should be taken
         */
        Command getNextCommand(Actor playerActor,
                               World world,
                               Command previousCommandResponse);

        /**
         * Returns the position of this request's target.
         * <p>
         * Can be <code>null</code>.
         * @return target location
         */
        Position getTargetLocation();

        /**
         * Determines if this request can be interrupted by
         * other requests before it is finished.
         * <p>
         * When this request is being executed and another
         * request is issued, if this request is
         * interruptible then the new requests replaces this
         * request in controller as soon as a reply to
         * previously sent command arrives.
         * @return is interruptible
         */
        boolean isInterruptible();
    }

    // Path finders for current map.
    private PathFinder dynamicPathFinder;
    private PathFinder staticPathFinder;

    private MapInstance lastPlayerMap;

    /**
     * Last sent command. Stored only until server replies.
     */
    private Command sentCommand;
    private Command commandResponse;

    // also guards sentCommand, commandResponse and currentRequest variables
    private Object waitingForResponseCondition = new Object();
    private Object waitingForCooldownCondition = new Object();

    private Thread controllerThread =
            new Thread(new ControllerThread(),
                       ControllerThread.class.getSimpleName());

    private PlayerRequest currentRequest;

    /**
     * Used to store the latest request if awaiting for
     * server response to currentRequest command.
     */
    private BlockingQueue<PlayerRequest> nextRequest =
            new ArrayBlockingQueue<PlayerRequest>(1);

    private long cooldownEndMs;

    private final long turnMs = GlobalConfig.globalInstance()
                                            .getMillisecondsPerTurn();

    private final long gracePeriodMs = Math.round(COOLDOWN_GRACE_PERIOD
        * turnMs);

    /**
     * Creates a new instance of player controller.
     */
    public PlayerController()
    {
        controllerThread.start();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void mapLoadingStateChanged()
    {
        final MapLoader mapLoader = Client.globalInstance().getMapLoader();
        if (mapLoader.isMapChanging())
        {
            Log.logger.debug("Map is changing.");
            halt();

            lastPlayerMap = null;
            staticPathFinder = null;
            dynamicPathFinder = null;
        }
        else if (mapLoader.getCurrentMapInstance() != lastPlayerMap)
        {
            Log.logger.debug("Map changed.");
            halt();

            lastPlayerMap = mapLoader.getCurrentMapInstance();
            staticPathFinder = null;
            dynamicPathFinder = null;
        }

        if (!mapLoader.mapNeedsType())
        {
            if (staticPathFinder == null)
            {
                staticPathFinder =
                        createStaticPathFinder(lastPlayerMap.getType());
                if (Log.isDebugEnabled())
                    Log.logger.debug("Static path finder created for map type: "
                        + lastPlayerMap.getType().getId());
            }
            if (dynamicPathFinder == null)
            {
                dynamicPathFinder = createDynamicPathFinder(lastPlayerMap);
                if (Log.isDebugEnabled())
                    Log.logger.debug("Static path finder created for map: "
                        + lastPlayerMap);
            }
        }
    }

    /**
     * Returns current request.
     * @return current request
     */
    public PlayerRequest getCurrentRequest()
    {
        synchronized (waitingForResponseCondition)
        {
            return currentRequest;
        }
    }

    /**
     * Returns the player actor position, or
     * <code>null</code> if player actor is not specified.
     * @return player actor position
     */
    public Position getPlayerPosition()
    {
        Actor playerActor = Client.globalInstance().getPlayerActor();
        return playerActor != null ? playerActor.getPosition() : null;
    }

    /**
     * Returns PathFinder for locating a dynamic path.
     * <p>
     * This PathFinder uses an instance of
     * {@link MapPassable} derived from current
     * {@link MapInstance}.
     * <p>
     * Path returned by the returned PathFinder takes into
     * account dynamic and static obstacles on map.
     * @return dynamic path finder
     */
    public final synchronized PathFinder getDynamicPathFinder()
    {
        return dynamicPathFinder;
    }

    /**
     * Returns PathFinder for locating a static path.
     * <p>
     * This {@link PathFinder} uses an instance of
     * {@link MapTypePassable} derived from current
     * {@link MapType}.
     * <p>
     * Path returned by the returned PathFinder does not
     * take into account any dynamic obstacles on map.
     * @return static path finder
     */
    public final synchronized PathFinder getStaticPathFinder()
    {
        return staticPathFinder;
    }

    private PathFinder createDynamicPathFinder(MapInstance map)
    {
        final MapType mapType = map.getType();
        final int maxDepth =
                POTENTIALY_VISIBLE_SEGMENTS
                    * Math.max(mapType.getSegmentSizeX(),
                               mapType.getSegmentSizeY());

        return new AStarPathFinder(new MapPassable(map),
                                   new Dimension(mapType.getSizeX(),
                                                 mapType.getSizeY()),
                                   new ManhattanHeuristic(),
                                   maxDepth);
    }

    private PathFinder createStaticPathFinder(MapType mapType)
    {
        final int maxDepth =
                POTENTIALY_VISIBLE_SEGMENTS
                    * Math.max(mapType.getSegmentSizeX(),
                               mapType.getSegmentSizeY());

        return new AStarPathFinder(new MapTypePassable(mapType),
                                   new Dimension(mapType.getSizeX(),
                                                 mapType.getSizeY()),
                                   new ManhattanHeuristic(),
                                   maxDepth);
    }

    /**
     * Request the player actor to perform a specific
     * {@link PlayerRequest}.
     * <p>
     * If another request is being currently executed the
     * new request will be interrupted as soon as possible.
     * @param request
     *            request to execute
     */
    public void playerRequest(PlayerRequest request)
    {
        if (Log.isDebugEnabled())
            Log.logger.debug("New request: " + request);

        synchronized (nextRequest)
        {
            nextRequest.clear();
            nextRequest.offer(request);
        }
    }

    /**
     * Stops player movement.
     */
    public void halt()
    {
        nextRequest.clear();
        synchronized (waitingForResponseCondition)
        {
            currentRequest = null;
            commandResponse = null;
        }
    }

    /**
     * A player command returned from server.
     * @param command
     *            a command from server with requester id
     *            equal to current player actor id
     */
    public void playerCommandResponse(Command command)
    {
        //This command type is sent by Client, not by the controller.
        CommandType type = command.getType();
        if (type == CommandType.ACTOR_DATA_REQUEST
            || type == CommandType.ITEM_DATA_REQUEST)
            return;

        assert sentCommand != null;
        assert sentCommand.equals(command);

        synchronized (waitingForResponseCondition)
        {
            sentCommand = null;
            commandResponse = command;
            waitingForResponseCondition.notify();
        }

        if (Log.logger.isDebugEnabled())
        {
            if (command.hasFailed())
                Log.logger.debug("Command failed: " + command.toString());
            else
                Log.logger.debug("Command succeeded: " + command.toString());
        }
    }

    /**
     * Stops any threads started by controller. After
     * calling this method controller is potentially
     * unusable.
     */
    public void stop()
    {
        controllerThread.interrupt();
    }

    /** {@inheritDoc} */
    @Override
    public void cooldownChanged(int cooldown)
    {
        synchronized (waitingForCooldownCondition)
        {
            cooldownEndMs =
                    System.currentTimeMillis() + cooldown * turnMs
                        - gracePeriodMs
                        - ClientGlobalConnector.globalInstance().getLag();
            if (Log.isTraceEnabled())
                Log.logger.trace("Cooldown changed: " + cooldown + " in ms: "
                    + cooldownEndMs);
            waitingForCooldownCondition.notify();
        }
    }

    private void processRequests() throws InterruptedException
    {
        // waiting for response
        synchronized (waitingForResponseCondition)
        {
            Log.logger.trace("Waiting for command response.");
            while (sentCommand != null && commandResponse == null)
                waitingForResponseCondition.wait();
        }

        // waiting for cooldown to end
        synchronized (waitingForCooldownCondition)
        {
            long curMs = System.currentTimeMillis();
            if (cooldownEndMs == 0)
                cooldownEndMs = curMs + turnMs / 2;
            Log.logger.trace("Waiting for cooldown to pass.");
            while ((curMs = System.currentTimeMillis()) < cooldownEndMs)
                waitingForCooldownCondition.wait(cooldownEndMs - curMs);
            cooldownEndMs = 0;
        }

        // local, thread-safe copy of some variables
        Command curResponse;
        PlayerRequest request;
        synchronized (waitingForResponseCondition)
        {
            curResponse = commandResponse;
            request = currentRequest;
            commandResponse = null;
        }

        // determining currentRequest
        if (request == null)
        {
            Log.logger.trace("Waiting for requests.");
            request = nextRequest.take();
            curResponse = null;
        }
        else if (request.isInterruptible())
        {
            PlayerRequest newReq = nextRequest.poll();
            if (newReq != null)
            {
                request = newReq;
                curResponse = null;
            }
        }

        assert request != null;

        // next command
        final Command commandToSend =
                request.getNextCommand(Client.globalInstance().getPlayerActor(),
                                       Client.globalInstance().getWorld(),
                                       curResponse);
        if (commandToSend != null)
        {
            if (commandToSend.getType() != CommandType.IDLE_COMMAND)
            {
                Client.globalInstance().sendCommand(commandToSend);
                if (Log.logger.isTraceEnabled())
                    Log.logger.trace("Sending command: "
                        + commandToSend.toString());

                synchronized (waitingForResponseCondition)
                {
                    commandResponse = null;
                }
            }
            else
            {
                if (Log.logger.isTraceEnabled())
                    Log.logger.trace("IdleCommand, skipping turn");

                synchronized (waitingForResponseCondition)
                {
                    commandResponse = commandToSend;
                }
            }

            synchronized (waitingForResponseCondition)
            {
                sentCommand = commandToSend;
                currentRequest = request;
            }
        }
        else
        {
            // command == null -> request finished
            synchronized (waitingForResponseCondition)
            {
                sentCommand = null;
                currentRequest = null;
                commandResponse = null;
            }
        }
    }
}
