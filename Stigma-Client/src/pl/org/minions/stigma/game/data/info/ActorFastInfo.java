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
import pl.org.minions.stigma.game.command.data.sink.DataRequestSinks;
import pl.org.minions.stigma.game.data.WorldData;
import pl.org.minions.stigma.game.data.WorldDataType;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;

/**
 * Class representing update of rapidly changing actor
 * statistics. Updates multiple actors.
 */
public class ActorFastInfo extends WorldData
{
    private static class ActorFastInfoData
    {
        private static final int SIZE =
                SizeOf.INT + SizeOf.SHORT + SizeOf.SHORT + SizeOf.INT;

        private int actorId;
        private short currentStamina;
        private short currentHealth;
        private int fastTS;

        public ActorFastInfoData(Actor a)
        {
            this.actorId = a.getId();
            this.currentStamina = a.getCurrentStamina();
            this.currentHealth = a.getCurrentHealth();
            this.fastTS = a.getFastTS();
        }

        public ActorFastInfoData(Buffer buf)
        {
            this.actorId = buf.decodeInt();
            this.currentStamina = buf.decodeShort();
            this.currentHealth = buf.decodeShort();
            this.fastTS = buf.decodeInt();
        }

        public static int sizeOf()
        {
            return SIZE;
        }

        public void encode(Buffer buf)
        {
            buf.encode(actorId);
            buf.encode(currentStamina);
            buf.encode(currentHealth);
            buf.encode(fastTS);
        }

        public int getActorId()
        {
            return actorId;
        }

        public short getCurrentHealth()
        {
            return currentHealth;
        }

        public short getCurrentStamina()
        {
            return currentStamina;
        }

        public int getFastTS()
        {
            return fastTS;
        }
    }

    private List<ActorFastInfo.ActorFastInfoData> list =
            new LinkedList<ActorFastInfo.ActorFastInfoData>();

    /** Constructor. */
    public ActorFastInfo()
    {
        super(WorldDataType.ACTOR_FAST_INFO);
    }

    /**
     * Adds actor info to delta.
     * @param a
     *            actor which info should be added
     */
    public void addActor(Actor a)
    {
        list.add(new ActorFastInfoData(a));
    }

    /** {@inheritDoc} */
    @Override
    public boolean apply(World world, DataRequestSinks dataRequestSink)
    {
        for (ActorFastInfo.ActorFastInfoData data : list)
        {
            Actor a = world.getActor(data.getActorId());
            if (a == null)
                return false;
            a.setCurrentHealth(data.getCurrentHealth());
            a.setCurrentStamina(data.getCurrentStamina());
            a.setFastTS(data.getFastTS());
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
            list.add(new ActorFastInfoData(buffer));
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        buffer.encode((byte) list.size());
        for (ActorFastInfo.ActorFastInfoData data : list)
            data.encode(buffer);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected int innerGetParamsLength()
    {
        return SizeOf.BYTE + list.size() * ActorFastInfoData.sizeOf();
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
        buf.append("ActorFastInfo: ");
        for (ActorFastInfo.ActorFastInfoData data : list)
            buf.append(MessageFormat.format("[actorId:{0}, actualStamina: {1}, actualHealth: {2}, fastTS:{3}],",
                                            data.getActorId(),
                                            data.getCurrentStamina(),
                                            data.getCurrentHealth(),
                                            data.getFastTS()));

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
                    private Iterator<ActorFastInfoData> it = list.iterator();

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
