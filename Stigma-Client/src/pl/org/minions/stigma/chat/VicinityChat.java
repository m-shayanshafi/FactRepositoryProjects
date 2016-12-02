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
package pl.org.minions.stigma.chat;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;

/**
 * Class representing one-to-all-that-can-hear chat.
 */
public class VicinityChat extends Chat
{
    private List<Integer> vicinity = new LinkedList<Integer>();

    /**
     * Constructor. This method will iterate on all actors
     * in neighborhood.
     * @param segment
     *            map segment on which sender stands
     * @param text
     *            chat message
     * @param senderId
     *            identifier of sender - to exclude him from
     *            vicinity
     */
    public VicinityChat(int senderId, MapInstance.Segment segment, String text)
    {
        super(ChatType.VICINITY, text);
        if (segment != null)
            for (MapInstance.Segment seg : segment.neighborhood())
                for (Actor a : seg.getActors())
                {
                    int id = a.getId();
                    if (id != senderId && !Actor.isNpc(id))
                        vicinity.add(id);
                }
    }

    /**
     * Factory method for decoding.
     * @return empty class instance
     */
    public static Chat create()
    {
        return new VicinityChat(0, null, null);
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeRecipients(Buffer buffer)
    {
        short size = buffer.decodeShort();

        for (int i = 0; i < size; ++i)
            vicinity.add(buffer.decodeInt());
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeRecipients(Buffer buffer, boolean toClient)
    {
        if (toClient)
        {
            buffer.encode((short) 0);
            return true;
        }

        buffer.encode((short) vicinity.size());
        for (int i : vicinity)
            buffer.encode(i);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public Collection<Integer> getRecipients()
    {
        return vicinity;
    }

    /** {@inheritDoc} */
    @Override
    protected int recipientsSize()
    {
        return SizeOf.SHORT + SizeOf.INT * vicinity.size();
    }

}
