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

import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.command.data.ActorDataRequest;
import pl.org.minions.stigma.game.command.data.ItemDataRequest;
import pl.org.minions.stigma.game.command.data.sink.DataRequestSinks;

/**
 * Implementation of request sink for client. Uses sinks
 * implemented as {@link Command commands}, so they may be
 * send to server as data requests.
 */
public class ClientDataRequestSink implements DataRequestSinks
{
    private ActorDataRequest actorDataSink;
    private ItemDataRequest itemDataSink;

    /**
     * Constructor. Creates empty sink.
     */
    public ClientDataRequestSink()
    {
        resetActorSink();
        resetItemSink();
    }

    /** {@inheritDoc} */
    @Override
    public ActorDataRequest getActorSink()
    {
        return actorDataSink;
    }

    /** {@inheritDoc} */
    @Override
    public ItemDataRequest getItemSink()
    {
        return itemDataSink;
    }

    /**
     * Clears actors sink. Should be called after sending
     * sink.
     */
    public void resetActorSink()
    {
        actorDataSink = new ActorDataRequest();
    }

    /**
     * Clears items sink. Should be called after sending
     * sink.
     */
    public void resetItemSink()
    {
        itemDataSink = new ItemDataRequest();
    }

}
