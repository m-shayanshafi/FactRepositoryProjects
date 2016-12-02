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
import pl.org.minions.stigma.chat.VicinityChat;
import pl.org.minions.stigma.client.Client;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.utils.i18n.Translated;
import pl.org.minions.utils.logger.Log;

/**
 * Class representing 'vicinity chat target'.
 */
public class VicinityTarget extends ChatTarget
{
    @Translated
    private static String LABEL = "Vicinity";

    /**
     * Constructor. Package-wide, because it is utility
     * class which should be used only by
     * {@link ChatProcessor}.
     * @param idx
     *            index of this target in
     *            {@link ChatProcessor}.
     */
    VicinityTarget(int idx)
    {
        super(idx);
    }

    /** {@inheritDoc} */
    @Override
    public Chat build(String msg)
    {
        MapInstance.Segment s = Client.globalInstance().getPlayerSegment();
        if (s == null)
        {
            Log.logger.warn("Trying to say to vicinity when no player segment available");
            return null;
        }
        return new VicinityChat(Client.globalInstance()
                                      .getPlayerActor()
                                      .getId(), s, msg);
    }

    /** {@inheritDoc} */
    @Override
    public String getLabel()
    {
        return LABEL;
    }

    /** {@inheritDoc} */
    @Override
    public ChatType getType()
    {
        return ChatType.VICINITY;
    }

    /** {@inheritDoc} */
    @Override
    public boolean match(Chat msg)
    {
        assert msg.getType() == ChatType.VICINITY;
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o)
    {
        if (o == this)
            return true;
        if (o instanceof VicinityTarget)
            return true;
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        // just to remove checkstyle warnings 
        return super.hashCode();
    }
}
