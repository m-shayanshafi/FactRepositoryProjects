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

import pl.org.minions.stigma.network.messaging.NetworkMessageType;
import pl.org.minions.stigma.network.messaging.base.IdCarryingMessage;

/**
 * Class representing message sent by client to choose actor
 * for player. Contains chosen actor id.
 */
public class LoginActorChosen extends IdCarryingMessage
{
    private LoginActorChosen()
    {
        super(NetworkMessageType.LOGIN_ACTOR_CHOSEN);
    }

    /**
     * Constructor.
     * @param id
     *            id of chosen actor
     */
    public LoginActorChosen(int id)
    {
        super(NetworkMessageType.LOGIN_ACTOR_CHOSEN, id);
    }

    /**
     * Creates empty object (needed for parsing network
     * message) Object state and information are unknown.
     * @return empty object
     */
    public static LoginActorChosen create()
    {
        return new LoginActorChosen();
    }

}
