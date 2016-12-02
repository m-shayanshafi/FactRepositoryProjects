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

import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.actor.ActorCooldownObserver;
import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.command.CommandType;
import pl.org.minions.stigma.network.messaging.chat.ChatMessage;
import pl.org.minions.stigma.network.messaging.game.CommandMessage;
import pl.org.minions.stigma.network.messaging.system.SystemMessage.SystemEventType;
import pl.org.minions.stigma.server.ai.AiScript;
import pl.org.minions.stigma.server.collections.TurnSchedulable;
import pl.org.minions.stigma.server.collections.TurnScheduleQueue;

/**
 * Implementation of {@link ActorManager} for <i>AI
 * driven</i> NPC's. By actor's <i>brain</i> in such
 * situation is used given {@link AiScript} instance.
 */
public class AiManager extends ActorManager implements TurnSchedulable
{
    private NpcManager aiManager;
    private AiScript aiScript;
    private Command response;
    private int lastQueuedTurn = -1;

    /**
     * Creates new NPC manager.
     * @param worldManager
     *            world manager to which actor should be
     *            connected
     * @param chatManager
     *            chat manager to which actor should be
     *            connected
     * @param actor
     *            actor managed by this class
     * @param aiScript
     *            AI script which should be used by this
     *            manager to determine NPC's reactions
     */
    public AiManager(WorldManager worldManager,
                     ChatManager chatManager,
                     Actor actor,
                     AiScript aiScript)
    {
        super(worldManager, chatManager, false, actor);
        assert !actor.isPC();
        this.aiManager = worldManager.getAiManager();
        this.aiScript = aiScript;
        actor.addCooldownObserver(new ActorCooldownObserver()
        {
            @Override
            public void cooldownChanged(int oldValue, int newValue)
            {
                if (lastQueuedTurn != -1 && response != null)
                    aiManager.enqueueResponse(AiManager.this);
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    public void disconnect()
    {
        aiManager = null;
    }

    /**
     * Returns last turn for which this manager was queued
     * for response. May return {@code -1} if managers is
     * not in queue.
     * @return last turn for which this manager was queued
     *         for response.
     */
    public int getLastQueuedTurn()
    {
        return lastQueuedTurn;
    }

    /**
     * Returns type of response.
     * @return type of response.
     */
    CommandType getResponseType()
    {
        if (response != null)
            return response.getType();
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isWorking()
    {
        return aiManager != null;
    }

    /**
     * Uses instance's {@link AiScript} to determine
     * reaction for command, and if such enqueues it to send
     * after NPC's actor cooldown is over.
     * @param command
     *            command to react to
     */
    void react(Command command)
    {
        if (!isWorking())
            return;
        Command newResponse =
                aiScript.react(getActor(),
                               getWorldManager().getWorld(),
                               command);
        if (newResponse != null)
        {
            response = newResponse;
            aiManager.enqueueResponse(this);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void send(CommandMessage command)
    {
        aiManager.queueCommand(this, command.getCommand());
    }

    /**
     * Sends prepared earlier response. Should be called
     * when cooldown is over.
     */
    void sendResponse()
    {
        if (response == null)
            return;
        super.queueCommand(response);
        response = null;
        lastQueuedTurn = -1;
    }

    /**
     * Set new turn for which this managers is queued for
     * response. Should be called only by
     * {@link TurnScheduleQueue}
     * @param lastQueuedTurn
     *            new turn for which manager is queued
     */
    public void setLastQueuedTurn(int lastQueuedTurn)
    {
        this.lastQueuedTurn = lastQueuedTurn;
    }

    /** {@inheritDoc} */
    @Override
    public void sendEvent(SystemEventType eventType)
    {
        // currently we ignore those messages, 
        // maybe in future there will be something interesting for AI
    }

    /** {@inheritDoc} */
    @Override
    public void send(ChatMessage chat)
    {
        // received chat ignored for now   
    }
}
