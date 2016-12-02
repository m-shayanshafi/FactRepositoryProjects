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
package pl.org.minions.stigma.client.requests;

import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.command.request.Equip;
import pl.org.minions.stigma.game.command.request.PickUp;
import pl.org.minions.stigma.game.command.request.UnEquip;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.item.LogicalSlotType;
import pl.org.minions.stigma.game.item.PhysicalSlotType;
import pl.org.minions.stigma.game.item.type.ItemType;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.globals.Position;

/**
 * Request to equip an item.
 * <p>
 * The item can be in inventory, already equipped or laying
 * on the ground at the player actor's position.
 * <p>
 * In either case it will be equipped. Any items occupying
 * or blocking slots that this item requires will be
 * unequipped.
 */
public class EquipRequest extends UnInterruptibleRequest
{
    private int itemId;
    private PhysicalSlotType slot;

    /**
     * Equip the item with given id on the chosen slot.
     * @param itemId
     *            id of the item to equip
     * @param slot
     *            slot to equip the item on
     */
    public EquipRequest(int itemId, PhysicalSlotType slot)
    {
        this.itemId = itemId;
        this.slot = slot;
    }

    /**
     * Equip the item with given id.
     * <p>
     * One of the free {@link PhysicalSlotType physical
     * slots} among those supported by {@link ItemType type
     * of this item} will be used. If none is free, then one
     * of the occupied slots will be used.
     * @param itemId
     *            id of the item to equip
     */
    public EquipRequest(int itemId)
    {
        this(itemId, null);
    }

    /** {@inheritDoc} */
    @Override
    public Command getNextCommand(Actor playerActor,
                                  World world,
                                  Command previousCommandResponse)
    {
        final Item item = world.getItem(itemId);
        if (item == null)
            return null;

        if (slot == null)
        {
            final LogicalSlotType equipementSlot =
                    item.getType().getEquipementSlot();
            for (PhysicalSlotType slotType : equipementSlot.getAvailablePhysicalSlots())
            {
                if (slotType == PhysicalSlotType.INVENTORY)
                    continue;
                slot = slotType;
                if (playerActor.getEquipedItems().get(slotType) == null)
                    break;
            }
        }

        if (item.isOnGround())
        {
            if (item.getMapId() == playerActor.getMapId()
                && item.getMapInstanceNo() == playerActor.getMapInstanceNo()
                && item.getPosition().equals(playerActor.getPosition()))
                return new PickUp(item.getId());
            return null;
        }
        else if (!playerActor.hasItem(item))
        {
            //TODO: maybe items from other sources will be equipable, for now ignore them
            return null;
        }
        else
        {
            if (item.isEquipped())
            {
                if (item.getEquippedPosition() == slot)
                    return null;
                else
                    return new UnEquip(item.getEquippedPosition());
            }
            else
            {
                Item equippedItem = playerActor.getEquipedItems().get(slot);
                if (equippedItem != null)
                {
                    return new UnEquip(equippedItem.getEquippedPosition());
                }

                for (PhysicalSlotType slotType : item.getType()
                                                     .getEquipementSlot()
                                                     .getBlockedPhysicalSlots())
                {
                    equippedItem = playerActor.getEquipedItems().get(slotType);
                    if (equippedItem != null)
                        return new UnEquip(equippedItem.getEquippedPosition());
                }

                return new Equip(itemId, slot);
            }
        }

    }

    /** {@inheritDoc} */
    @Override
    public Position getTargetLocation()
    {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Equip(#" + itemId + "; " + slot + ")";

    }

}
