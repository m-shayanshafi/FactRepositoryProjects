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

import java.text.MessageFormat;
import java.util.ArrayDeque;
import java.util.Queue;

import pl.org.minions.stigma.chat.Chat;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.command.CommandType;
import pl.org.minions.stigma.network.messaging.chat.ChatMessage;
import pl.org.minions.stigma.network.messaging.game.CommandMessage;
import pl.org.minions.stigma.network.messaging.system.SystemMessage;
import pl.org.minions.stigma.server.ServerConfig;
import pl.org.minions.utils.logger.Log;

/**
 * Abstract class representing connection between actor's
 * <i>brain</i> and world manager. It queues commands made
 * by <i>brain</i> and can be used to send effects of
 * commands to "brain". Actor's <i>brain</i> it something
 * that "makes decisions" - produces commands.
 */
public abstract class ActorManager
{
    private static final int QUEUE_SIZE =
            ServerConfig.globalInstance().getCommandQueueSize();
    private static final int SPAMMER_SIZE =
            ServerConfig.globalInstance().getSpammerCommandsCount();
    private Queue<Command> lowPriorityQueue =
            new ArrayDeque<Command>(QUEUE_SIZE);
    private Queue<Command> highPriorityQueue =
            new ArrayDeque<Command>(QUEUE_SIZE);;
    private int spamCount;
    private WorldManager worldManager;
    private ChatManager chatManager;
    private Actor actor;

    /**
     * Creates actor manager for given actor, connected with
     * given world manager. Registers actor in
     * {@link WorldManager} and {@link ChatManager}.
     * @param worldManager
     *            world manager to which actor should be
     *            connected
     * @param chatManager
     *            chat manager to which actor should be
     *            connected (can be {@code null})
     * @param registerInChatManager
     *            {@code true} when managed actor should
     *            receive chat
     * @param actor
     *            actor managed by this class
     */
    protected ActorManager(WorldManager worldManager,
                           ChatManager chatManager,
                           boolean registerInChatManager,
                           Actor actor)
    {
        this.worldManager = worldManager;
        this.chatManager = chatManager;
        this.actor = actor;

        worldManager.addActor(this);
        if (registerInChatManager)
            chatManager.addActor(this);
    }

    /**
     * Sends disconnect request to actor's <i>brain</i>.
     */
    public abstract void disconnect();

    /**
     * Returns actor managed by this class.
     * @return actor managed by this class.
     */
    public Actor getActor()
    {
        return actor;
    }

    /**
     * Returns world manager to which actor is connected.
     * @return world manager to which actor is connected.
     */
    public WorldManager getWorldManager()
    {
        return worldManager;
    }

    /**
     * Returns chat manager to which actor is connected.
     * @return chat manager to which actor is connected.
     */
    public ChatManager getChatManager()
    {
        return chatManager;
    }

    /**
     * Checks if current manager is still connected with any
     * <i>brain</i>.
     * @return {@code true} when actor's <i>brain</i> is
     *         still connected
     */
    public abstract boolean isWorking();

    /**
     * Takes first available command queue in this manager.
     * If command is of high priority (see
     * {@link CommandType#isHighPriority()}) low priority
     * commands are removed from queue. Returns {@code null}
     * if no commands are available from actor managed by
     * this class.
     * @return next command queued by this manager or
     *         {@code null} if no such available
     */
    public Command poll()
    {
        synchronized (lowPriorityQueue)
        {
            Command ret = highPriorityQueue.poll();
            if (ret != null)
                lowPriorityQueue.clear();
            else
                ret = lowPriorityQueue.poll();
            return ret;
        }
    }

    /**
     * Queues command in this manager. This function sets
     * "requester id" in command. Also it checks if actor is
     * spammer (too long queue) and disconnects it if
     * necessary.
     * @param cmd
     *            command to queue
     */
    protected void queueCommand(Command cmd)
    {
        synchronized (lowPriorityQueue)
        {
            Queue<Command> targetQueue =
                    cmd.getType().isHighPriority() ? highPriorityQueue
                                                  : lowPriorityQueue;

            cmd.setRequesterId(getActor().getId());

            if (targetQueue.size() < QUEUE_SIZE)
            {
                targetQueue.add(cmd);
                spamCount = 0;
            }
            else
            {
                ++spamCount;
                if (spamCount >= SPAMMER_SIZE)
                {
                    if (!getActor().isPC())
                        Log.logger.warn(MessageFormat.format("NPC spammer... somebody should tweak server option and/or AI: [id:{0}]",
                                                             getActor().getId()));
                    else
                        Log.logger.info(MessageFormat.format("Spammer found, disconnecting [id:{0}]",
                                                             getActor().getId()));
                    disconnect();
                }
                else
                {
                    // "returned to sender" failure
                    if (Log.isTraceEnabled())
                        Log.logger.trace("Command returned to sender (to big queue): "
                            + cmd);
                    if (Log.isDebugEnabled())
                        Log.logger.debug("Queue full, returning to sender, spamCount="
                            + spamCount);
                    send(new CommandMessage(cmd));
                }
            }
        }
    }

    /**
     * Enqueues chat for processing.
     * @param chat
     *            message to enqueue
     */
    protected void queueChat(Chat chat)
    {
        chat.setSenderId(getActor().getId());
        getChatManager().chat(chat);
    }

    /**
     * Method used to send to <i>brain</i> information about
     * executed command.
     * @param command
     *            message containing executed command
     */
    public abstract void send(CommandMessage command);

    /**
     * Sends information about given event to <i>brain</i>.
     * @param eventType
     *            type of event that occurred
     */
    public abstract void sendEvent(SystemMessage.SystemEventType eventType);

    /**
     * Method used to send to <i>brain</i> chat messages.
     * @param chat
     *            chat message
     */
    public abstract void send(ChatMessage chat);
}
