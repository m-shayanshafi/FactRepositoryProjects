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
package pl.org.minions.stigma.databases.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import pl.org.minions.stigma.databases.actor.ArchetypeDB;
import pl.org.minions.stigma.databases.item.ItemFactory;
import pl.org.minions.stigma.databases.sql.SqlAsyncDB;
import pl.org.minions.stigma.databases.sql.SqlRequest;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.actor.Archetype;
import pl.org.minions.stigma.game.actor.ArchetypeBuilder;
import pl.org.minions.stigma.game.actor.Gender;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.item.PhysicalSlotType;
import pl.org.minions.stigma.game.item.type.ItemType.ItemKind;
import pl.org.minions.stigma.server.item.ItemIdRegistry;
import pl.org.minions.utils.logger.Log;

/**
 * Class using for logging in players and loading their
 * actors. Players data is loaded from SQL database.
 * @see SqlAsyncDB
 */
public class PlayerLoadDB
{
    /**
     * Interface used for callback. Requester must provide
     * object with this interface. This object will be
     * informed using proper functions about success (or
     * not) of logging in.
     */
    public static interface InfoReceiver
    {
        /**
         * Will be called when player's data is incorrect
         * (login and/or password).
         */
        void badLogin();

        /**
         * Will be called when player can be logged in.
         * @param availableActors
         *            actors of logged player
         */
        void loginSucceded(List<Actor> availableActors);
    }

    private class ReadActorList extends SqlRequest
    {
        private InfoReceiver receiver;
        private String login;
        private String pass;

        public ReadActorList(String login, String pass, InfoReceiver receiver)
        {
            this.login = login;
            this.pass = pass;
            this.receiver = receiver;
        }

        /** {@inheritDoc} */
        @Override
        protected boolean execute()
        {
            List<Actor> actors = readActors();
            if (actors == null)
            {
                receiver.badLogin();
                return false;
            }

            for (Actor a : actors)
            {
                if (!readArchetypes(a))
                {
                    receiver.badLogin();
                    return false;
                }
                if (!readItems(a))
                {
                    receiver.badLogin();
                    return false;
                }
            }
            receiver.loginSucceded(actors);
            return true;
        }

        private List<Actor> readActors()
        {
            if (!executeStatement(actorReadingStatement,
                                  login,
                                  PasswordHasher.hash(pass)))
                return null;

            ResultSet resultSet = aquireResultSet(actorReadingStatement);

            try
            {
                if (resultSet == null || !resultSet.next())
                {
                    return null;
                }
                List<Actor> list = new LinkedList<Actor>();
                do
                {
                    Actor actor =
                            new Actor(resultSet.getInt("Avatar_id"),
                                      resultSet.getString("Name"));
                    actor.setGender(Gender.getForOrdinal(resultSet.getByte("Gender")));
                    actor.setSafeMapId(resultSet.getShort("Safe_Map_Id"));
                    actor.setLevel(resultSet.getByte("Level"));
                    actor.setExperience(resultSet.getInt("Experience"));
                    actor.setStrength(resultSet.getByte("Strength"));
                    actor.setWillpower(resultSet.getByte("Willpower"));
                    actor.setAgility(resultSet.getByte("Agility"));
                    actor.setFinesse(resultSet.getByte("Finesse"));
                    actor.setMoney(resultSet.getInt("Money"));
                    actor.setPosition(null); // clears position - will be set while adding
                    list.add(actor);
                } while (resultSet.next());
                resultSet.close();
                return list;
            }
            catch (SQLException e)
            {
                Log.logger.error(e);
                return null;
            }
        }

        private boolean readArchetypes(Actor a)
        {
            if (!executeStatement(archetypeReadingStatement, a.getId()))
                return false;

            ResultSet resultSet = aquireResultSet(archetypeReadingStatement);
            if (resultSet == null)
                return false;
            try
            {
                if (!resultSet.next())
                {
                    resultSet.close();
                    return true; // empty - it's ok <- no archetypes for this actor
                }
                List<Archetype> list = new LinkedList<Archetype>();
                do
                {
                    short id = resultSet.getShort("Archetype_id");
                    Archetype archetype = archetypeDB.getArchetype(id);
                    if (archetype == null)
                    {
                        Log.logger.warn("Skipping null archetype: " + id);
                        continue;
                    }
                    a.getPersistenArchetypes().add(id);
                    list.add(archetype);
                } while (resultSet.next());
                ArchetypeBuilder.accumulate(a, list);
            }
            catch (SQLException e)
            {
                Log.logger.error(e);
                return false;
            }
            return true;
        }

        private boolean readItems(Actor a)
        {
            if (!executeStatement(itemReadingStatement, a.getId()))
                return false;

            ResultSet itemResultSet = aquireResultSet(itemReadingStatement);
            if (itemResultSet == null)
                return false;
            try
            {
                if (!itemResultSet.next())
                {
                    itemResultSet.close();
                    return true; // empty - it's ok <- no items for this actor
                }

                do
                {
                    short itemId = itemResultSet.getShort("Item_id");
                    short itemTypeId = itemResultSet.getShort("Item_type");
                    int itemKind = itemResultSet.getInt("Item_kind");

                    if (itemKind < 0 || itemKind > ItemKind.valuesCount())
                    {
                        Log.logger.error("Item kind out of range: " + itemKind
                            + " item: " + itemId + " actor: " + a.getId());
                        continue;
                    }

                    short itemPosition =
                            itemResultSet.getShort("Item_position");

                    //add position
                    if (!(itemPosition >= 0 && itemPosition < PhysicalSlotType.valuesCount()))
                    {
                        Log.logger.warn("Item position out of range: "
                            + itemPosition + " found for item: " + itemId
                            + " and actor: " + a.getId());
                        itemPosition =
                                (short) PhysicalSlotType.INVENTORY.ordinal();
                    }

                    //create item
                    ItemKind kind = ItemKind.getForOridinal(itemKind);
                    Item i = null;
                    if (kind.equals(ItemKind.ARMOR)
                        || kind.equals(ItemKind.WEAPON))
                    {
                        //read dynamic modifiers
                        if (!executeStatement(dynamicItemModifiersReadingStatement,
                                              a.getId(),
                                              itemId))
                            return false;
                        ResultSet modifierResultSet =
                                aquireResultSet(dynamicItemModifiersReadingStatement);
                        List<Short> dynamicModifiers = new LinkedList<Short>();
                        if (modifierResultSet != null)
                        {
                            if (!modifierResultSet.next())
                            {
                                modifierResultSet.close();
                            }
                            else
                            {
                                do
                                {
                                    dynamicModifiers.add(new Short(modifierResultSet.getShort("Modifier_id")));
                                } while (modifierResultSet.next());
                                modifierResultSet.close();
                            }
                        }

                        i =
                                ItemFactory.getInstance()
                                           .getItem(ItemIdRegistry.globalInstance()
                                                                  .nextId(),
                                                    itemTypeId,
                                                    kind,
                                                    dynamicModifiers);
                        if (i == null)
                        {
                            Log.logger.warn("Null item returned from factory - killing request");
                            return false;
                        }

                        //read dynamic effects
                        if (!executeStatement(dynamicItemEffectsReadingStatement,
                                              a.getId(),
                                              itemId))
                            return false;
                        ResultSet effectsResultSet =
                                aquireResultSet(dynamicItemEffectsReadingStatement);
                        if (effectsResultSet != null)
                        {
                            if (!effectsResultSet.next())
                            {
                                effectsResultSet.close();
                            }
                            else
                            {
                                do
                                {
                                    i.getEffectIdList()
                                     .add(new Short(effectsResultSet.getShort("Effect_id")));
                                } while (effectsResultSet.next());
                                effectsResultSet.close();
                            }
                        }
                    }
                    else
                    {
                        i =
                                ItemFactory.getInstance()
                                           .getItem(ItemIdRegistry.globalInstance()
                                                                  .nextId(),
                                                    itemTypeId,
                                                    kind);
                    }

                    a.addItem(i, PhysicalSlotType.getForOridinal(itemPosition));

                } while (itemResultSet.next());

                itemResultSet.close();
            }
            catch (SQLException e)
            {
                Log.logger.error(e);
                return false;
            }
            return true;
        }

        /** {@inheritDoc} */
        @Override
        protected boolean shouldBeFlushed()
        {
            return false;
        }
    }

    private PreparedStatement actorReadingStatement;
    private PreparedStatement archetypeReadingStatement;
    private PreparedStatement itemReadingStatement;
    private PreparedStatement dynamicItemModifiersReadingStatement;
    private PreparedStatement dynamicItemEffectsReadingStatement;

    private ArchetypeDB archetypeDB;

    /**
     * Constructor. Global instance of {@link SqlAsyncDB}
     * must exists. Allocates some some resources (like
     * prepared statements).
     * @param archetypeDB
     *            database component used to load archetypes
     */
    public PlayerLoadDB(ArchetypeDB archetypeDB)
    {
        this.archetypeDB = archetypeDB;
        SqlAsyncDB db = SqlAsyncDB.globalInstance();
        assert db != null;

        actorReadingStatement =
                db.prepareStatement("SELECT av.* FROM Avatars av, Accounts ac WHERE av.Account_id=ac.Account_id AND ac.E_mail=? AND ac.password=?");

        archetypeReadingStatement =
                db.prepareStatement("SELECT arch.Archetype_id FROM Archetypes arch WHERE arch.Avatar_id=?");

        itemReadingStatement =
                db.prepareStatement("SELECT  i.Item_id, i.Item_type, i.Item_kind, i.Item_position FROM Inventory i WHERE i.Avatar_id=? ORDER BY i.Item_id");

        dynamicItemModifiersReadingStatement =
                db.prepareStatement("SELECT im.Modifier_id FROM Item_Modifiers im WHERE im.Avatar_id=? AND im.Item_id=?");

        dynamicItemEffectsReadingStatement =
                db.prepareStatement("SELECT ie.Effect_id FROM Item_Effects ie WHERE ie.Avatar_id=? AND ie.Item_id=?");
    }

    /**
     * Frees resources allocated by this object. After
     * calling this function this object is no more usable.
     */
    public void freeResources()
    {
        if (actorReadingStatement == null)
            return;

        try
        {
            if (actorReadingStatement != null)
                actorReadingStatement.close();
            if (archetypeReadingStatement != null)
                archetypeReadingStatement.close();
            if (itemReadingStatement != null)
                itemReadingStatement.close();
        }
        catch (SQLException e)
        {
            Log.logger.error(e);
        }

        actorReadingStatement = null;
    }

    /**
     * Checks if database was initialized properly. Returns
     * {@code false} when, for example, some tables are
     * missing.
     * @return {@code true} when database was initialized
     *         properly and can be used
     */
    public boolean isOk()
    {
        return actorReadingStatement != null
            && archetypeReadingStatement != null
            && itemReadingStatement != null;
    }

    /**
     * Requests list of actors for given player's login
     * (authenticated given password).
     * @param login
     *            login which should be used
     * @param pass
     *            password for login
     * @param receiver
     *            object with callback interface, this
     *            object will be informed about result of
     *            operation
     */
    public void requestActorList(String login,
                                 String pass,
                                 InfoReceiver receiver)
    {
        SqlAsyncDB db = SqlAsyncDB.globalInstance();
        assert db != null;
        db.executeSQL(new ReadActorList(login, pass, receiver));
    }
}
