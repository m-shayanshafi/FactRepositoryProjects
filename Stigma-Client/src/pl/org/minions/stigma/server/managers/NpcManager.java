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
package pl.org.minions.stigma.server.managers;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.server.ai.AiScript;
import pl.org.minions.stigma.server.collections.TurnScheduleQueue;
import pl.org.minions.stigma.server.npc.NpcGenerator;
import pl.org.minions.utils.logger.Log;

/**
 * Class that manages actors running on {@link AiScript AI
 * Scripts}. It starts its own thread to manage NPCs
 * reaction.
 */
public class NpcManager
{
    private class AiThread implements Runnable
    {
        @Override
        public void run()
        {
            Log.logger.debug("AiThread started");
            while (!Thread.interrupted())
            {
                Request req;
                try
                {
                    req = requestQueue.take();
                }
                catch (InterruptedException e)
                {
                    break;
                }

                req.execute();
            }
            Log.logger.debug("AiThread interrupted");
        }
    }

    private static class CreateNpcsRequest implements Request
    {
        private WorldManager worldManager;
        private ChatManager chatManager;

        private NpcGenerator layer;
        private MapInstance map;

        public CreateNpcsRequest(WorldManager worldManager,
                                 ChatManager chatManager,
                                 NpcGenerator layer,
                                 MapInstance map)
        {
            this.worldManager = worldManager;
            this.chatManager = chatManager;
            this.layer = layer;
            this.map = map;
        }

        /** {@inheritDoc} */
        @Override
        public void execute()
        {
            if (!layer.putNpcs(worldManager, chatManager, map))
            {
                Log.logger.warn("Putting NPCs on map id:"
                    + map.getType().getId() + " instance: "
                    + map.getInstanceNo() + " failed");
            }

        }

    }

    private class PollRequest implements Request
    {
        private int turnNumber;

        public PollRequest(int turnNumber)
        {
            this.turnNumber = turnNumber;
        }

        @Override
        public void execute()
        {
            Collection<AiManager> managers = npcQueue.poll(turnNumber);
            if (managers == null)
                return;
            for (AiManager m : managers)
                m.sendResponse();
        }
    }

    private static class ReactRequest implements Request
    {
        private AiManager npcManager;
        private Command command;

        public ReactRequest(AiManager npcManager, Command command)
        {
            this.npcManager = npcManager;
            this.command = command;
        }

        @Override
        public void execute()
        {
            npcManager.react(command);
        }
    }

    private static interface Request
    {
        void execute();
    }

    private BlockingQueue<Request> requestQueue =
            new LinkedBlockingQueue<Request>();
    private Thread aiThread;
    private TurnScheduleQueue<AiManager> npcQueue =
            new TurnScheduleQueue<AiManager>();

    /**
     * Creates new manager and starts its thread.
     */
    public NpcManager()
    {
        aiThread = new Thread(new AiThread(), "AiThread");
        aiThread.start();
    }

    /**
     * Enqueues request for creation NPC for given map.
     * @param worldManager
     *            world manager to which NPCs should be
     *            connected
     *@param chatManager
     *            chat manager to which NPCs should be
     *            connected
     * @param map
     *            map for which NPCs should be created
     * @param npcGenerator
     *            layer witch should be responsible for
     *            creating NPC for given map
     */
    public void createNpcs(WorldManager worldManager,
                           ChatManager chatManager,
                           MapInstance map,
                           NpcGenerator npcGenerator)
    {
        enqueueRequest(new CreateNpcsRequest(worldManager,
                                             chatManager,
                                             npcGenerator,
                                             map));
    }

    private void enqueueRequest(Request req)
    {
        try
        {
            requestQueue.put(req);
        }
        catch (InterruptedException e)
        {
            Log.logger.info("queueCommand interrupted");
        }
    }

    /**
     * Adds given {@link AiManager} to queue using it
     * cooldown. When cooldown is not active manager is
     * instantly requested to send response. If manager was
     * already queued it will be moved to new position in
     * queue.
     * @param npcManager
     *            manager to enqueue
     */
    void enqueueResponse(AiManager npcManager)
    {
        int cooldown;
        if (npcManager.getResponseType().isStoppedByCooldown())
            cooldown = npcManager.getActor().getCooldown();
        else
            cooldown =
                    npcManager.getWorldManager().getWorld().getTurnNumber() - 1; // so it for sure be lower than current turn number
        // we cannot send it even if cooldown < turnNumber, because of some races - better enqueue it, actor cooldown can change
        npcQueue.enqueue(cooldown, npcManager);
    }

    /**
     * Enqueues request in AI thread to poll NPC managers
     * queue for those which have some responses to send
     * before or in given turn. Polled managers will be
     * asked to send their responses (see
     * {@link AiManager#sendResponse()}).
     * @param currentTurnNumber
     *            turn number to which all NPC managers
     *            should be polled
     */
    public void pollResponses(int currentTurnNumber)
    {
        enqueueRequest(new PollRequest(currentTurnNumber));
    }

    /**
     * Queues command for NPC to react to.
     * @param npcManager
     *            NPC manager which should respond to given
     *            command
     * @param command
     *            command for which NPC should react
     */
    void queueCommand(AiManager npcManager, Command command)
    {
        enqueueRequest(new ReactRequest(npcManager, command));
    }

    /**
     * Stops manager thread.
     */
    public void stop()
    {
        if (aiThread != null)
        {
            aiThread.interrupt();
            aiThread = null;
        }
    }
}
