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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pl.org.minions.stigma.chat.Chat;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.network.messaging.chat.ChatMessage;
import pl.org.minions.utils.logger.Log;

/**
 * Class for managing in game chat. Creates of managing
 * thread.
 */
public class ChatManager
{
    private class ChatThread implements Runnable
    {
        /** {@inheritDoc} */
        @Override
        public void run()
        {
            Log.logger.debug("Starting chat thread");
            while (!Thread.interrupted())
            {
                Chat chat;
                try
                {
                    chat = queue.take();
                }
                catch (InterruptedException e)
                {
                    break;
                }

                // add/remove only when somebody speaks something... otherwise it just doesn't matter

                synchronized (managersToRemove)
                {
                    for (ActorManager manager : managersToRemove)
                        managers.remove(manager.getActor().getId());
                    managersToRemove.clear();
                }

                synchronized (managersToAdd)
                {
                    for (ActorManager manager : managersToAdd)
                        managers.put(manager.getActor().getId(), manager);
                    managersToAdd.clear();
                }

                ChatMessage msg = new ChatMessage(chat);
                if (!Actor.isNpc(chat.getSenderId()))
                {
                    ActorManager sender = managers.get(chat.getSenderId());
                    if (sender == null)
                    {
                        Log.logger.debug("<null> sender: " + chat.getSenderId());
                        continue;
                    }
                    sender.send(msg); // returning msg
                }

                for (int id : chat.getRecipients())
                {
                    if (Actor.isNpc(id))
                    {
                        Log.logger.warn("Omitting NPC chat receiver: " + id);
                        continue;
                    }

                    ActorManager manager = managers.get(id);
                    if (manager == null)
                    {
                        Log.logger.debug("Omitting disconnected chat receiver: "
                            + id);
                        continue;
                    }
                    manager.send(msg);
                }
            }
            Log.logger.debug("Exiting chat thread");
        }
    }

    private BlockingQueue<Chat> queue = new LinkedBlockingQueue<Chat>();
    private List<ActorManager> managersToAdd = new LinkedList<ActorManager>();
    private List<ActorManager> managersToRemove =
            new LinkedList<ActorManager>();
    private Map<Integer, ActorManager> managers =
            new HashMap<Integer, ActorManager>();
    private Thread thread;

    /**
     * Constructor. Starts managing thread.
     */
    public ChatManager()
    {
        thread = new Thread(new ChatThread(), "chatThread");
        thread.start();
    }

    /**
     * Stops managing thread.
     */
    public void stop()
    {
        if (thread != null)
        {
            thread.interrupt();
            thread = null;
        }
    }

    /**
     * Enqueues chat for processing.
     * @param chat
     *            chat to process
     */
    public void chat(Chat chat)
    {
        try
        {
            queue.put(chat);
        }
        catch (InterruptedException e)
        {
            Log.logger.debug(e);
        }
    }

    /**
     * Adds actor to be available for chat.
     * @param manager
     *            actor to add
     */
    public void addActor(ActorManager manager)
    {
        synchronized (managersToAdd)
        {
            managersToAdd.add(manager);
        }
    }

    /**
     * Removes actor from manager's collection.
     * @param manager
     *            actor to be removed (no more chat will be
     *            sent to it)
     */
    public void removeActor(ActorManager manager)
    {
        synchronized (managersToRemove)
        {
            managersToRemove.add(manager);
        }
    }

}
