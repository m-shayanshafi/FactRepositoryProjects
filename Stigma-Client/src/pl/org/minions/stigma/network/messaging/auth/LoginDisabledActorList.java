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
package pl.org.minions.stigma.network.messaging.auth;

import java.util.Collection;
import java.util.LinkedList;

import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;
import pl.org.minions.stigma.network.messaging.NetworkMessage;
import pl.org.minions.stigma.network.messaging.NetworkMessageType;

/**
 * Message carrying updated list of disabled player's
 * actors.
 */
public class LoginDisabledActorList extends NetworkMessage
{
    private Collection<Integer> disabledActors;

    private LoginDisabledActorList()
    {
        super(NetworkMessageType.LOGIN_DISABLED_ACTOR_LIST);
        this.disabledActors = new LinkedList<Integer>();
    }

    /**
     * Constructor.
     * @param disabledActors
     *            collection of currently disabled actors
     *            identifiers
     */
    public LoginDisabledActorList(Collection<Integer> disabledActors)
    {
        this();
        this.disabledActors = disabledActors;
    }

    /**
     * Creates empty object (needed for parsing network
     * message). Object state and information are unknown.
     * @return empty object
     */
    public static LoginDisabledActorList create()
    {
        return new LoginDisabledActorList();
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeParams(Buffer buffer)
    {
        byte size = buffer.decodeByte();
        disabledActors.clear();
        for (int i = 0; i < size; ++i)
            disabledActors.add(buffer.decodeInt());
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        buffer.encode((byte) disabledActors.size());
        for (int i : disabledActors)
            buffer.encode(i);
        return true;
    }

    /**
     * Returns collection of identifiers of actors which are
     * not currently available.
     * @return collection of identifiers of actors which are
     *         not currently available.
     */
    public Collection<Integer> getDisabledActors()
    {
        return disabledActors;
    }

    /** {@inheritDoc} */
    @Override
    protected int innerGetParamsLength()
    {
        return SizeOf.BYTE + disabledActors.size() * SizeOf.INT;
    }

}
