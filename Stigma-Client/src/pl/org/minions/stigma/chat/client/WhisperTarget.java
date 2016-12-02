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

import pl.org.minions.stigma.chat.Chat;
import pl.org.minions.stigma.chat.ChatType;
import pl.org.minions.stigma.chat.WhisperChat;
import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.game.actor.Actor;

/**
 * Class representing 'whisper chat target', with given
 * actor as whisper receiver.
 */
public class WhisperTarget extends ChatTarget
{
    private int id;
    private boolean echo;

    /**
     * Constructor. Package-wide, because it is utility
     * class which should be used only by
     * {@link ChatProcessor}.
     * @param idx
     *            index of this target in
     *            {@link ChatProcessor}.
     * @param id
     *            of actor which should be whisper's target
     */
    WhisperTarget(int idx, int id)
    {
        super(idx);
        this.id = id;
        echo = Client.globalInstance().getPlayerActor().getId() == id;
    }

    /** {@inheritDoc} */
    @Override
    public Chat build(String msg)
    {
        return new WhisperChat(id, msg);
    }

    /** {@inheritDoc} */
    @Override
    public String getLabel()
    {
        Actor a = Client.globalInstance().getWorld().getActor(id);
        if (a == null || a.getName() == null || a.getName().isEmpty())
            return String.valueOf(id);
        return a.getName();
    }

    /** {@inheritDoc} */
    @Override
    public ChatType getType()
    {
        return ChatType.WHISPER;
    }

    /** {@inheritDoc} */
    @Override
    public boolean match(Chat msg)
    {
        assert msg.getType() == ChatType.WHISPER;
        return msg.getSenderId() == id;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        // just to remove checkstyle warnings 
        return super.hashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o)
    {
        if (o == this)
            return true;
        if (!(o instanceof WhisperTarget))
            return false;
        WhisperTarget t = (WhisperTarget) o;
        return t.id == this.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEcho()
    {
        return echo;
    }
}
