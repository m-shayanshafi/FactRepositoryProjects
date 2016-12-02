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
package pl.org.minions.stigma.game.command.data;

import java.util.List;

import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.command.CommandType;
import pl.org.minions.stigma.game.data.WorldData;
import pl.org.minions.stigma.game.data.info.ActorFastInfo;
import pl.org.minions.stigma.game.data.info.ActorInfo;
import pl.org.minions.stigma.game.world.ExtendedWorld;

/**
 * Request for actors data.
 */
public class ActorDataRequest extends GenericDataRequest<Actor>
{
    /** Constructor. */
    public ActorDataRequest()
    {
        super(CommandType.ACTOR_DATA_REQUEST);
    }

    /** {@inheritDoc} */
    @Override
    protected boolean fillDeltas(ExtendedWorld world,
                                 List<WorldData> deltasForSender,
                                 List<RequestDesc> list)
    {
        ActorFastInfo fastInfo = new ActorFastInfo();
        ActorInfo slowInfo = new ActorInfo();

        for (RequestDesc desc : list)
        {
            Actor a = world.getActor(desc.getObjectId());
            if (a == null)
                continue;
            if (desc.getObjectTS() < a.getFastTS())
                fastInfo.addActor(a);
            if (desc.getObjectTS() < a.getSlowTS())
                slowInfo.addActor(a);
        }

        if (!fastInfo.isEmpty())
            deltasForSender.add(fastInfo);
        if (!slowInfo.isEmpty())
            deltasForSender.add(slowInfo);
        return !fastInfo.isEmpty() || !slowInfo.isEmpty();
    }
}
