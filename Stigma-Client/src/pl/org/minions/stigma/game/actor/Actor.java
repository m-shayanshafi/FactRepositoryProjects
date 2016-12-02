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
package pl.org.minions.stigma.game.actor;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.org.minions.stigma.game.MechanicInternals;
import pl.org.minions.stigma.game.TimestampedObject;
import pl.org.minions.stigma.game.item.Equipment;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.item.PhysicalSlotType;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.utils.collections.SuperSet;
import pl.org.minions.utils.logger.Log;

/**
 * Main class representing game various characters -
 * players, NPCs, enemies. Contains character statistics and
 * it's position in game
 */
public final class Actor extends ArchetypeBase implements
                                              TimestampedObject<Actor>
{
    private static final int MOVE_SPEED = 3;

    // Identification
    private int id;
    private List<Short> persistenArchetypesList = new LinkedList<Short>();

    // Time-stamps
    private int fastTS;
    private int slowTS;

    // Position
    private Position position = new Position((short) -1, (short) -1);
    private short mapId = -1;
    private short safeMapId = -1;
    private short mapInstanceNo;

    // Current status
    private short currentHealth;
    private short currentStamina;
    private short currentLoad;
    private int cooldown;

    // Experience
    private byte level;
    private int experience;

    // Characteristics
    private short maxHealth;
    private short maxStamina;
    private short maxLoad;
    private short attack;
    private short defense;
    private short spellPower;
    private short spellImmunity;

    //items
    private List<Item> inventory;
    private EnumMap<PhysicalSlotType, Item> equipedItems;
    private int money;

    // Other - implementation specific
    private List<ActorCooldownObserver> observers;

    /**
     * Constructor.
     * @param id
     *            global identifier of actor - must be
     *            unique
     * @param name
     *            name of actor
     */
    public Actor(int id, String name)
    {
        super(name);
        this.id = id;
        this.currentLoad = 0;
        this.inventory = new LinkedList<Item>();
        this.equipedItems =
                new EnumMap<PhysicalSlotType, Item>(PhysicalSlotType.class);
        if (Log.isTraceEnabled())
        {
            Log.logger.trace("CREATED: Actor with id: " + getId() + " name: "
                + getName());
        }

    }

    /**
     * Checks whether given identifier is NPC's or not.
     * @param id
     *            id to check
     * @return {@code true} when given identifier belongs to
     *         NPC
     */
    public static boolean isNpc(int id)
    {
        return id < 0;
    }

    /**
     * Adds observer. Currently it observes only cooldown
     * changes.
     * @param observer
     *            new observer
     */
    public void addCooldownObserver(ActorCooldownObserver observer)
    {
        if (observers == null)
            observers = new LinkedList<ActorCooldownObserver>();
        observers.add(observer);
    }

    /** {@inheritDoc} */
    @Override
    public boolean compareTS(Actor other)
    {
        return this.getFastTS() < other.getFastTS()
            || this.getSlowTS() < other.getSlowTS();
    }

    /**
     * Returns deep copy of this class. IMPORTANT: copies
     * only STATIC information (other can be calculated from
     * it).
     * @return deep copy of this class.
     */
    public Actor deepCopy()
    {
        Actor copy = new Actor(this.id, getName());
        copy.experience = this.experience;
        copy.level = this.level;
        copy.mapId = this.mapId;
        copy.safeMapId = this.safeMapId;
        copy.mapInstanceNo = this.mapInstanceNo;
        copy.position = this.position.deepCopy();
        copy.setAgility(this.getAgility());
        copy.setStrength(this.getStrength());
        copy.setWillpower(this.getWillpower());
        copy.setFinesse(this.getFinesse());
        copy.setGender(this.getGender());

        EnumMap<DamageType, Resistance> copyMap =
                new EnumMap<DamageType, Resistance>(DamageType.class);
        for (Map.Entry<DamageType, Resistance> entry : this.getResistanceMap()
                                                           .entrySet())
        {
            copyMap.put(entry.getKey(), entry.getValue().deepCopy());
        }
        copy.setResistanceMap(copyMap);

        copy.persistenArchetypesList.addAll(persistenArchetypesList);
        copy.getProficiencies().addAll(getProficiencies());

        return copy;
    }

    /**
     * Returns actor's current health level.
     * @return actor's current health level
     */
    public short getCurrentHealth()
    {
        return currentHealth;
    }

    /**
     * Returns actor's current stamina level.
     * @return actor's current stamina level
     */
    public short getCurrentStamina()
    {
        return currentStamina;
    }

    /**
     * Returns current load of actor.
     * @return current load of actor
     */
    public short getCurrentLoad()
    {
        return currentLoad;
    }

    /**
     * Returns attack.
     * @return attack
     */
    public short getAttack()
    {
        return attack;
    }

    /**
     * Returns max load of actor.
     * @return max load of actor
     */
    public short getMaxLoad()
    {
        return maxLoad;
    }

    /**
     * Returns cooldown for this actor. This may be either
     * relative (client) or absolute (server) amount of
     * turns till actor will be able to perform any
     * "command".
     * @return cooldown of this actor
     */
    public int getCooldown()
    {
        return cooldown;
    }

    /**
     * Returns defense.
     * @return defense
     */
    public short getDefense()
    {
        return defense;
    }

    /**
     * Returns actor's experience.
     * @return actor's experience
     */
    public int getExperience()
    {
        return experience;
    }

    /**
     * Returns total amount of experience this actor needs
     * to accumulate to advance to the next level.
     * <p>
     * The amount is counted from the start of the current
     * level, so current {@link #getExperience() experience}
     * does not influence returned value.
     * @return amount of XP to advance
     */
    public int getExperienceToNextLevel()
    {
        return MechanicInternals.getNextLevelXPRequirement(level);
    }

    /**
     * Returns rapidly changing statistics time-stamp.
     * @return rapidly changing statistics time-stamp.
     */
    public int getFastTS()
    {
        return fastTS;
    }

    /**
     * Returns unique id of this actor.
     * @return unique id of this actor.
     */
    @Override
    public int getId()
    {
        return id;
    }

    /**
     * Returns actor's level.
     * @return actor's level
     */
    public byte getLevel()
    {
        return level;
    }

    /**
     * Returns id of map where actor is placed.
     * @return id of map where actor is placed
     */
    public short getMapId()
    {
        return mapId;
    }

    /**
     * Returns current map instance number.
     * @return current map instance number.
     */
    public short getMapInstanceNo()
    {
        return mapInstanceNo;
    }

    /**
     * Returns maxHealth.
     * @return maxHealth
     */
    public short getMaxHealth()
    {
        return maxHealth;
    }

    /**
     * Returns maxStamina.
     * @return maxStamina
     */
    public short getMaxStamina()
    {
        return maxStamina;
    }

    /**
     * Returns time (in turns) of one step of this actor.
     * @return time (in turns) of one step of this actor.
     */
    public short getMoveSpeed()
    {
        return MOVE_SPEED;
    }

    /**
     * Equivalent to MAX(getSlowTS(),getFastTS()).
     * @return MAX(getSlowTS(),getFastTS()).
     */
    @Override
    public int getNewestTS()
    {
        return Math.max(slowTS, fastTS);
    }

    /**
     * Return list of persistent archetypes of this actor.
     * @return list of persistent archetypes of this actor.
     */
    public List<Short> getPersistenArchetypes()
    {
        return persistenArchetypesList;
    }

    /**
     * Returns current position of actor on map.
     * @return current position of actor on map
     */
    public Position getPosition()
    {
        return position;
    }

    /**
     * Returns id of 'safe' map. This is id of map on which
     * actor will spawn (log in etc).
     * @return id of 'safe' map
     */
    public short getSafeMapId()
    {
        return safeMapId;
    }

    /**
     * Returns slowly changing statistics time-stamp.
     * @return slowly changing statistics time-stamp.
     */
    public int getSlowTS()
    {
        return slowTS;
    }

    /**
     * Returns spellImmunity.
     * @return spellImmunity
     */
    public short getSpellImmunity()
    {
        return spellImmunity;
    }

    /**
     * Returns spellPower.
     * @return spellPower
     */
    public short getSpellPower()
    {
        return spellPower;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        return id;
    }

    /**
     * Returns {@code true} if actor is playable character
     * (PC).
     * @return {@code true} if actor is playable character
     *         (PC)
     */
    public boolean isPC()
    {
        return !isNpc(getId());
    }

    /**
     * Removes observer.
     * @param observer
     *            observer to remove
     */
    public void removeObserver(ActorCooldownObserver observer)
    {
        if (observer == null)
            return;
        observers.remove(observer);
        if (observers.isEmpty())
            observers = null;
    }

    /**
     * Sets new current health level. Does not check if it's
     * &lt; 0 or &gt; maximum health.
     * @param currentHealth
     *            new current health level
     */
    public void setCurrentHealth(short currentHealth)
    {
        this.currentHealth = currentHealth;
    }

    /**
     * Sets new current stamina level. Does not check if
     * it's &lt; 0 or &gt; maximum stamina.
     * @param currentStamina
     *            new current health level
     */
    public void setCurrentStamina(short currentStamina)
    {
        this.currentStamina = currentStamina;
    }

    /**
     * Sets new value of currentLoad.
     * @param currentLoad
     *            the currentLoad to set
     */
    public void setCurrentLoad(short currentLoad)
    {
        this.currentLoad = currentLoad;
    }

    /**
     * Sets new value of attack.
     * @param attack
     *            the attack to set
     */
    public void setAttack(short attack)
    {
        this.attack = attack;
    }

    /**
     * Sets new value of maxLoad.
     * @param maxLoad
     *            the maxLoad to set
     */
    public void setMaxLoad(short maxLoad)
    {
        this.maxLoad = maxLoad;
    }

    /**
     * Sets new cooldown for this actor.
     * @param cooldown
     *            new cooldown to set
     */
    public void setCooldown(int cooldown)
    {
        if (observers != null)
            for (ActorCooldownObserver observer : observers)
                observer.cooldownChanged(this.cooldown, cooldown);
        this.cooldown = cooldown;
    }

    /**
     * Sets new value of defense.
     * @param defense
     *            the defense to set
     */
    public void setDefense(short defense)
    {
        this.defense = defense;
    }

    /**
     * Sets new experience.
     * @param experience
     *            new experience
     */
    public void setExperience(int experience)
    {
        this.experience = experience;
    }

    /**
     * Sets new rapidly changing statistics time-stamp.
     * @param fastTS
     *            new rapidly changing statistics time-stamp
     */
    public void setFastTS(int fastTS)
    {
        this.fastTS = fastTS;
    }

    /**
     * Sets id of this actor.
     * @param id
     *            new id
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Sets new level.
     * @param level
     *            new level
     */
    public void setLevel(byte level)
    {
        this.level = level;
    }

    /**
     * Sets new map id.
     * @param mapId
     *            new map id
     */
    public void setMapId(short mapId)
    {
        this.mapId = mapId;
    }

    /**
     * Sets new map instance number.
     * @param mapInstanceNo
     *            new map instance number
     */
    public void setMapInstanceNo(short mapInstanceNo)
    {
        this.mapInstanceNo = mapInstanceNo;
    }

    /**
     * Sets new value of maxHealth.
     * @param maxHealth
     *            the maxHealth to set
     */
    public void setMaxHealth(short maxHealth)
    {
        this.maxHealth = maxHealth;
    }

    /**
     * Sets new value of maxStamina.
     * @param maxStamina
     *            the maxStamina to set
     */
    public void setMaxStamina(short maxStamina)
    {
        this.maxStamina = maxStamina;
    }

    /**
     * Sets new position of actor on map - move actor.
     * @param p
     *            new actors position
     */
    public void setPosition(Position p)
    {
        this.position = p;
    }

    /**
     * Sets new value of 'safe' map id.
     * @param safeMapId
     *            new value of 'safe' map id
     */
    public void setSafeMapId(short safeMapId)
    {
        this.safeMapId = safeMapId;
    }

    /**
     * Sets new slowly changing statistics time-stamp.
     * @param slowTS
     *            new slowly changing statistics time-stamp
     */
    public void setSlowTS(int slowTS)
    {
        this.slowTS = slowTS;
    }

    /**
     * Sets new value of spellDefense.
     * @param spellDefense
     *            the spellDefense to set
     */
    public void setSpellDefense(short spellDefense)
    {
        this.spellImmunity = spellDefense;
    }

    /**
     * Sets new value of spellPower.
     * @param spellPower
     *            the spellPower to set
     */
    public void setSpellPower(short spellPower)
    {
        this.spellPower = spellPower;
    }

    /**
     * Returns items actor has in his inventory.
     * @return inventory list of items that are in actor's
     *         inventory
     */
    public List<Item> getInventory()
    {
        return inventory;
    }

    /**
     * Returns items actor wears.
     * @return equipedItems map with equipped items, key is
     *         a position of item, value is an item
     */
    public Map<PhysicalSlotType, Item> getEquipedItems()
    {
        return equipedItems;
    }

    /**
     * Returns collection which contains all items actor
     * owns (both from inventory and equipment).
     * @return unmodifiable collection which contains all
     *         items actor owns (both from inventory and
     *         equipment).
     */
    public Collection<Item> getAllItems()
    {
        Collection<Item> items = new SuperSet<Item>(inventory);
        items.addAll(equipedItems.values());

        return Collections.unmodifiableCollection(items);
    }

    /**
     * Adds item to actor. Item is placed according to it's
     * position, can be added to inventory list or to
     * equipped items map.
     * @param item
     *            item to add
     * @param position
     *            position to where add item
     */
    public void addItem(Item item, PhysicalSlotType position)
    {
        if (position == PhysicalSlotType.INVENTORY)
        {
            inventory.add(item);
            item.setEquippedPosition(position);
        }
        else
        {
            equipItem(item, position);
        }

        currentLoad += item.getWeight();
    }

    /**
     * Equips item. Removes item from inventory.
     * @param item
     *            item to equip
     * @param position
     *            position where to add item
     * @return true if item was equipped
     */
    public boolean equipItem(Item item, PhysicalSlotType position)
    {
        //if item cannot be equipped due to actors statistics
        if (!canEquip(item, position))
        {
            if (Log.isTraceEnabled())
                Log.logger.trace("Actor " + id + " cannot equip item: "
                    + item.getId() + "of type: " + item.getType().getId());
            return false;
        }

        for (PhysicalSlotType pst : item.getType()
                                        .getEquipementSlot()
                                        .getBlockedPhysicalSlots())
        {
            //add item to all positions blocked by item
            equipedItems.put(pst, item);
        }

        equipedItems.put(position, item);

        inventory.remove(item);

        //add position information to item
        item.setEquippedPosition(position);

        recalculateStatistics();

        return true;
    }

    /**
     * Moves item from equipped items to inventory.
     * @param position
     *            position from which item is taken
     * @return unequipped item or {@code null} when failed
     */
    public Item unEquipItem(PhysicalSlotType position)
    {
        Item item = equipedItems.get(position);

        if (item == null)
            return null;

        for (PhysicalSlotType pst : item.getType()
                                        .getEquipementSlot()
                                        .getBlockedPhysicalSlots())
            equipedItems.remove(pst);

        equipedItems.remove(position);
        inventory.add(item);

        //remove position from item
        item.setEquippedPosition(PhysicalSlotType.INVENTORY);

        recalculateStatistics();

        return item;
    }

    /**
     * Removes item from inventory.
     * @param item
     *            item to remove
     * @return {@code true} when item was in possession of
     *         this actor
     */
    public boolean removeItem(Item item)
    {
        if (item.getEquippedPosition() != PhysicalSlotType.INVENTORY)
            unEquipItem(item.getEquippedPosition());

        if (!inventory.remove(item))
        {
            Log.logger.warn("Unable to remove item: " + item.getId()
                + "from actor: " + id);
            return false;
        }
        currentLoad -= item.getWeight();
        return true;
    }

    /**
     * Checks if this actor can equip given item on given
     * position.
     * @param item
     *            item to be checked
     * @param position
     *            position for item
     * @return {@code true} when requirements of item are
     *         met.
     */
    public boolean canEquip(Item item, PhysicalSlotType position)
    {
        if (!item.isComplete())
            return false;

        if (position == PhysicalSlotType.INVENTORY)
        {
            Log.logger.warn("Trying to equip item on INVENTORY slot");
            return false;
        }

        // check whether item can by equipped on this slot
        if (!item.getType()
                 .getEquipementSlot()
                 .getAvailablePhysicalSlots()
                 .contains(position))
        {
            Log.logger.warn("Trying to equip item: " + item.getType().getId()
                + " on slot " + position.name());
            return false;
        }

        // check if all blocked slots are empty
        for (PhysicalSlotType pst : item.getType()
                                        .getEquipementSlot()
                                        .getBlockedPhysicalSlots())
        {
            if (equipedItems.containsKey(pst))
                return false;
        }

        return canEquip(item);
    }

    /**
     * Check if actor meets requirements for given item.
     * Does not check if proper equipment slots are empty.
     * @param item
     *            item to check
     * @return {@code true} when actor can equip given item
     */
    public boolean canEquip(Item item)
    {
        if (!item.isComplete())
            return false;

        Equipment equipement = null;
        // check item type
        switch (item.getKind())
        {
            case ARMOR:
            case WEAPON:
                equipement = (Equipment) item;
                break;
            default:
            case OTHER:
                //if item is not an equipment do not allow to equip it
                return false;
        }

        if (this.getStrength() < equipement.getRequiredStrength()
            || this.getAgility() < equipement.getRequiredAgility()
            || this.getWillpower() < equipement.getRequiredWillpower()
            || this.getFinesse() < equipement.getRequiredFinesse())
        {
            if (Log.isTraceEnabled())
                Log.logger.trace("Equipment statistics requirements not met. Actor: "
                    + this.getId() + ", item: " + item.getId());
            return false;
        }

        for (short s : equipement.getRequiredProficiency())
        {
            if (!this.getProficiencies().contains(s))
            {
                if (Log.isTraceEnabled())
                    Log.logger.trace("Equipment proficiency requirements not met. Actor: "
                        + this.getId() + " item: " + item.getId());
                return false;
            }
        }

        return true;
    }

    /**
     * Calculates all characteristics.
     */
    public void fullRecalc()
    {
        // TODO some real calculations
        this.setMaxHealth(MechanicInternals.getBaseMaxHealth(this));
        this.setMaxStamina(MechanicInternals.getBaseMaxStamina(this));
        this.setMaxLoad(MechanicInternals.getBaseMaxLoad(this));
        this.setAttack((short) 1);
        this.setDefense((short) 1);
        this.setSpellPower((short) 1);
        this.setSpellDefense((short) 1);
        //TODO: include effects??
    }

    /**
     * Calls {@link #fullRecalc()} and assigns 'max' values
     * to 'current' values. For example: health.
     */
    public void firstRecalc()
    {
        fullRecalc();
        currentHealth = maxHealth;
        currentStamina = maxStamina;
        // set "actual" values to max values
    }

    private void recalculateStatistics()
    {
        //TODO after discussion on minions forum: we will recalculate statistics here
        // NOTE: not exactly... I believe this method name is bad, but yes, let's keep it here until we will get into equipping items etc
        // maybe even 'fullRecalc' should be used when equipping items... or this should be part of fullRecalc - for future investigation
    }

    /**
     * Checks whether or not this actor is in possession of
     * given item.
     * @param i
     *            item to check
     * @return {@code true} when item is in this actors
     *         possession
     */
    public boolean hasItem(Item i)
    {
        if (i.isOnGround())
            return false;

        PhysicalSlotType slot = i.getEquippedPosition();

        if (slot == PhysicalSlotType.INVENTORY)
            return inventory.contains(i);

        return equipedItems.get(slot) == i;
    }

    /**
     * Returns money.
     * @return money
     */
    public int getMoney()
    {
        return money;
    }

    /**
     * Sets new value of money.
     * @param money
     *            the money to set
     */
    public void setMoney(int money)
    {
        this.money = money;
    }

    /**
     * Checks if actor is overloaded.
     * @return {@code true} when actor is overloaded and
     *         shouldn't move
     */
    public boolean isOverloaded()
    {
        return currentLoad > maxLoad;
    }
}
