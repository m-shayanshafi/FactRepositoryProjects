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
package pl.org.minions.stigma.game.data.info;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.actor.Gender;
import pl.org.minions.stigma.game.command.data.sink.DataRequestSinks;
import pl.org.minions.stigma.game.data.WorldData;
import pl.org.minions.stigma.game.data.WorldDataType;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;
import pl.org.minions.utils.logger.Log;

/**
 * Class representing update of slowly changing actor
 * statistics. Updates multiple actors.
 */
public class ActorInfo extends WorldData
{
    private static class ActorInfoData
    {
        private static final int CONST_SIZE = SizeOf.INT // id 
            + SizeOf.BYTE // gender
            + SizeOf.BYTE // strength
            + SizeOf.BYTE // agility
            + SizeOf.BYTE // willpower
            + SizeOf.BYTE // finesse
            + SizeOf.INT; // slowTS
        private int actorId;
        private String name;
        private Gender gender;
        private byte strength;
        private byte agility;
        private byte willpower;
        private byte finesse;
        private int slowTS;

        public ActorInfoData(Actor a)
        {
            this.actorId = a.getId();
            this.name = a.getName();
            this.gender = a.getGender();
            this.strength = a.getStrength();
            this.agility = a.getAgility();
            this.willpower = a.getWillpower();
            this.finesse = a.getFinesse();
            this.slowTS = a.getSlowTS();
        }

        public ActorInfoData(Buffer buf)
        {
            this.actorId = buf.decodeInt();
            this.name = buf.decodeString();
            this.gender = Gender.getForOrdinal(buf.decodeByte());
            this.strength = buf.decodeByte();
            this.agility = buf.decodeByte();
            this.willpower = buf.decodeByte();
            this.finesse = buf.decodeByte();
            this.slowTS = buf.decodeInt();
        }

        public void encode(Buffer buf)
        {
            buf.encode(actorId);
            buf.encode(name);
            buf.encode((byte) gender.ordinal());
            buf.encode(strength);
            buf.encode(agility);
            buf.encode(willpower);
            buf.encode(finesse);
            buf.encode(slowTS);
        }

        public int getActorId()
        {
            return actorId;
        }

        public byte getAgility()
        {
            return agility;
        }

        public byte getFinesse()
        {
            return finesse;
        }

        public String getName()
        {
            return name;
        }

        public int getSlowTS()
        {
            return slowTS;
        }

        public byte getStrength()
        {
            return strength;
        }

        public byte getWillpower()
        {
            return willpower;
        }

        public Gender getGender()
        {
            return gender;
        }

        public int size()
        {
            return CONST_SIZE + Buffer.stringBytesCount(name);
        }
    }

    private List<ActorInfo.ActorInfoData> list =
            new LinkedList<ActorInfo.ActorInfoData>();

    /** Constructor. */
    public ActorInfo()
    {
        super(WorldDataType.ACTOR_INFO);
    }

    /**
     * Adds actor to list.
     * @param a
     *            actor which should be included in update
     */
    public void addActor(Actor a)
    {
        list.add(new ActorInfoData(a));
    }

    /** {@inheritDoc} */
    @Override
    public boolean apply(World world, DataRequestSinks dataRequestSink)
    {
        for (ActorInfo.ActorInfoData data : list)
        {
            Actor a = world.getActor(data.getActorId());
            if (a == null)
            {
                Log.logger.error("ActorInfo - Updating non existing actor: "
                    + data.getActorId() + " " + data.getName());
                return false;
            }

            if (Log.isTraceEnabled())
                Log.logger.trace("ActorInfo - Updating actor: "
                    + data.getName());
            a.setName(data.getName());
            a.setGender(data.getGender());
            a.setStrength(data.getStrength());
            a.setAgility(data.getAgility());
            a.setWillpower(data.getWillpower());
            a.setFinesse(data.getFinesse());
            a.setSlowTS(data.getSlowTS());

            a.fullRecalc();
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeParams(Buffer buffer)
    {
        byte size = buffer.decodeByte();
        list.clear();
        for (int i = 0; i < size; ++i)
            list.add(new ActorInfoData(buffer));
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        buffer.encode((byte) list.size());
        for (ActorInfo.ActorInfoData data : list)
            data.encode(buffer);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected int innerGetParamsLength()
    {
        int r = SizeOf.BYTE;
        for (ActorInfo.ActorInfoData data : list)
            r += data.size();
        return r;
    }

    /**
     * Returns {@code true} when no actor was added to this
     * delta.
     * @return {@code true} when actors info list is empty
     */
    public boolean isEmpty()
    {
        return list.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder();
        buf.append("ActorInfo: ");
        for (ActorInfo.ActorInfoData data : list)
            buf.append(MessageFormat.format("[actorId:{0}, name: {1}, gender{6}, strength:{2}, agility:{3}, willpower:{4}, finesse:{5}]",
                                            data.getActorId(),
                                            data.getName(),
                                            data.getStrength(),
                                            data.getAgility(),
                                            data.getWillpower(),
                                            data.getFinesse(),
                                            data.getGender()));
        return buf.toString();
    }

    /** {@inheritDoc} */
    @Override
    public Iterable<Integer> updatedIds()
    {
        return new Iterable<Integer>()
        {
            @Override
            public Iterator<Integer> iterator()
            {
                return new Iterator<Integer>()
                {
                    private Iterator<ActorInfoData> it = list.iterator();

                    @Override
                    public boolean hasNext()
                    {
                        return it.hasNext();
                    }

                    @Override
                    public Integer next()
                    {
                        return it.next().getActorId();
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

}
