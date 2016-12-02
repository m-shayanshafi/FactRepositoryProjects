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
package pl.org.minions.stigma.chat.client;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import pl.org.minions.stigma.chat.Chat;
import pl.org.minions.stigma.chat.ChatType;
import pl.org.minions.stigma.client.ui.event.UiEventRegistry;
import pl.org.minions.stigma.client.ui.event.listeners.ChatListener;
import pl.org.minions.stigma.game.actor.Actor;

/**
 * Chat processor. Filters chat messages, stores all
 * possible chat targets etc. Supports listeners for
 * filtered messages.
 */
public class ChatProcessor
{
    private static final int INITIAL_CAPACITY = 10;
    private Vector<ChatTarget> targets =
            new Vector<ChatTarget>(INITIAL_CAPACITY);
    private int currentTarget;
    private List<ProcessedChatListener> listeners =
            new LinkedList<ProcessedChatListener>();
    private List<ChatTargetChangedListener> targetListeners =
            new LinkedList<ChatTargetChangedListener>();

    /**
     * Constructor. Register for chat messages, which will
     * be automatically filtered.
     * @param reg
     *            event register
     */
    public ChatProcessor(UiEventRegistry reg)
    {
        currentTarget = 0; // vicinity
        targets.add(new VicinityTarget(currentTarget));

        reg.addChatListener(new ChatListener()
        {
            @Override
            public void chatReceived(Chat chat)
            {
                ChatTarget target = matchTarget(chat);
                if (target == null)
                {
                    int idx = addTarget(chat);
                    target = targets.get(idx);
                }
                if (!target.isSilent())
                    for (ProcessedChatListener l : listeners)
                        l.chatReceived(target, chat);
            }
        });
    }

    /**
     * Sets current (default) chat target.
     * @param id
     *            index of desired chat target
     */
    public void setCurrentTarget(int id)
    {
        assert id >= 0 && id < targets.size();
        currentTarget = id;
        ChatTarget target = targets.get(id);
        target.setSilent(false);
        for (ChatTargetChangedListener l : targetListeners)
            l.chatTargetChoosen(target);
    }

    /**
     * Returns current selected chat target index.
     * @return current selected chat target index.
     */
    public int getCurrentTargetIdx()
    {
        return currentTarget;
    }

    /**
     * Returns current selected chat target.
     * @return current selected chat target.
     */
    public ChatTarget getCurrentTarget()
    {
        return targets.get(currentTarget);
    }

    /**
     * Creates new chat message, using selected chat target.
     * @param msg
     *            message to be sent in chat
     * @return new chat message
     */
    public Chat create(String msg)
    {
        return targets.get(currentTarget).build(msg);
    }

    private int addTarget(Chat chat)
    {
        assert chat.getType() == ChatType.WHISPER;
        int idx = targets.size();
        targets.add(new WhisperTarget(idx, chat.getSenderId()));
        return idx;
    }

    private ChatTarget matchTarget(Chat msg)
    {
        for (ChatTarget target : targets)
            if (msg.getType() == target.getType() && target.match(msg))
                return target;
        return null;
    }

    /**
     * Returns object to iterate over all remembered
     * targets.
     * @return object to iterate over all remembered
     *         targets.
     */
    public Iterable<ChatTarget> targets()
    {
        return targets;
    }

    /**
     * Adds listeners for processed chat messages. Listener
     * will be informed about positively filtered messages.
     * @param listener
     *            listener to add
     */
    public void addListener(ProcessedChatListener listener)
    {
        listeners.add(listener);
    }

    /**
     * Adds listener for changes in currently selected chat
     * target.
     * @param listener
     *            listener to add
     */
    public void addListener(ChatTargetChangedListener listener)
    {
        targetListeners.add(listener);
    }

    /**
     * Removes chat listener.
     * @param listener
     *            listener to remove
     */
    public void removeListener(ProcessedChatListener listener)
    {
        listeners.remove(listener);
    }

    /**
     * Returns index of chat target representing whisper to
     * given actor. If needed - adds new target.
     * @param actorId
     *            identifier of actor to which whisper
     *            should be targeted
     * @return index of whisper chat target for requested
     *         actor or {@code -1} if such actor is
     *         unavailable
     */
    public int getWhisperTarget(int actorId)
    {
        if (Actor.isNpc(actorId))
            return -1;

        WhisperTarget t = new WhisperTarget(-1, actorId);
        int idx = targets.indexOf(t);
        if (idx == -1)
        {
            idx = targets.size();
            targets.add(new WhisperTarget(idx, actorId));
        }
        return idx;
    }
}
