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

import java.util.LinkedList;
import java.util.List;

import pl.org.minions.stigma.databases.item.ItemFactory;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.item.PhysicalSlotType;
import pl.org.minions.stigma.game.item.type.ItemType.ItemKind;
import pl.org.minions.stigma.globals.SizeOf;
import pl.org.minions.stigma.network.Buffer;
import pl.org.minions.stigma.network.messaging.NetworkMessage;
import pl.org.minions.stigma.network.messaging.NetworkMessageType;

/**
 * Message carrying information about successful end of
 * login process.
 */
public class LoginCorrect extends NetworkMessage
{
    private static final int CONST_ITEM_SIZE = SizeOf.INT // id
        + SizeOf.BYTE // kind
        + SizeOf.SHORT // type
    ;

    private static final int CONST_ACTOR_SIZE = SizeOf.INT // id
        + SizeOf.SHORT // current load
    ;

    private int actorId;
    private short currentLoad;
    private List<Item> inventory;

    private LoginCorrect()
    {
        super(NetworkMessageType.LOGIN_CORRECT);
    }

    /**
     * Constructor.
     * @param actor
     *            chosen actor
     */
    public LoginCorrect(Actor actor)
    {
        super(NetworkMessageType.LOGIN_CORRECT);

        this.actorId = actor.getId();
        this.currentLoad = actor.getCurrentLoad();
        this.inventory = new LinkedList<Item>(actor.getInventory());
    }

    /**
     * Creates empty object (needed for parsing network
     * message).
     * @return empty object
     */
    public static LoginCorrect create()
    {
        return new LoginCorrect();
    }

    /** {@inheritDoc} */
    @Override
    protected boolean decodeParams(Buffer buffer)
    {
        this.actorId = buffer.decodeInt();
        this.currentLoad = buffer.decodeShort();
        this.inventory = new LinkedList<Item>();

        final int itemCnt = buffer.decodeShort();
        for (int itemIt = 0; itemIt < itemCnt; ++itemIt)
        {
            int id = buffer.decodeInt();
            ItemKind kind = buffer.decodeEnum(ItemKind.class);
            short type = buffer.decodeShort();
            Item i = ItemFactory.getInstance().getItem(id, type, kind);
            if (i == null)
                return false;
            inventory.add(i);
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean encodeParams(Buffer buffer)
    {
        buffer.encode(actorId);
        buffer.encode(currentLoad);
        buffer.encode((short) inventory.size());

        for (Item i : inventory)
        {
            buffer.encode(i.getId());
            buffer.encode(i.getKind());
            buffer.encode(i.getType().getId());
        }

        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected int innerGetParamsLength()
    {
        return CONST_ACTOR_SIZE + SizeOf.SHORT + CONST_ITEM_SIZE
            * inventory.size();
    }

    /**
     * Returns chosen actor id.
     * @return chosen actor id
     */
    public int getActorId()
    {
        return actorId;
    }

    /**
     * Applies additional information transported by this
     * message to existing actor.
     * @param playerActor
     *            actor instance to which information should
     *            be applied
     */
    public void applyToActor(Actor playerActor)
    {
        for (Item i : inventory)
            playerActor.addItem(i, PhysicalSlotType.INVENTORY);
        playerActor.setCurrentLoad(currentLoad);
    }
}
