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
package pl.org.minions.stigma.game.data;

import pl.org.minions.stigma.game.data.actor.ActorChangedSafeMap;
import pl.org.minions.stigma.game.data.actor.ActorCooldownChanged;
import pl.org.minions.stigma.game.data.info.ActorFastInfo;
import pl.org.minions.stigma.game.data.info.ActorInfo;
import pl.org.minions.stigma.game.data.info.ItemInfo;
import pl.org.minions.stigma.game.data.info.SegmentInfo;
import pl.org.minions.stigma.network.messaging.NetworkObjectCodec;

/**
 * Class listing all game data objects' types.
 */
public enum WorldDataType implements
                         NetworkObjectCodec.ObjectFactory<WorldData>
{
    ACTOR_CHANGED_SAFE_MAP
    {
        /** {@inheritDoc} */
        @Override
        public WorldData buildObject()
        {
            return ActorChangedSafeMap.create();
        }

        /** {@inheritDoc} */
        @Override
        public boolean isClientOnly()
        {
            return false;
        }
    },
    SEGMENT_INFO
    {
        /** {@inheritDoc} */
        @Override
        public WorldData buildObject()
        {
            return SegmentInfo.create();
        }

    },
    ACTOR_INFO
    {
        /** {@inheritDoc} */
        @Override
        public WorldData buildObject()
        {
            return new ActorInfo();
        }
    },
    ACTOR_FAST_INFO
    {
        /** {@inheritDoc} */
        @Override
        public WorldData buildObject()
        {
            return new ActorFastInfo();
        }
    },
    ITEM_INFO
    {
        /** {@inheritDoc} */
        @Override
        public WorldData buildObject()
        {
            return new ItemInfo();
        }
    },
    ACTOR_COOLDOWN_CHANGED
    {
        /** {@inheritDoc} */
        @Override
        public WorldData buildObject()
        {
            return ActorCooldownChanged.create();
        }

        /** {@inheritDoc} */
        @Override
        public boolean isClientOnly()
        {
            return false;
        }
    },
    ;

    /**
     * Returns {@code true} when data should be applied only
     * on client. Default implementation always return
     * {@code true}.
     * @return {@code true} when data should be applied only
     *         on client.
     */
    public boolean isClientOnly()
    {
        return true;
    }

}
