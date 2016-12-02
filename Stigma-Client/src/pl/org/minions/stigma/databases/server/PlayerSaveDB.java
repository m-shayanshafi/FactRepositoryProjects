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
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pl.org.minions.stigma.databases.actor.ArchetypeDB;
import pl.org.minions.stigma.databases.sql.SqlAsyncDB;
import pl.org.minions.stigma.databases.sql.SqlRequest;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.actor.Archetype;
import pl.org.minions.stigma.game.actor.ArchetypeBuilder;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.utils.logger.Log;

/**
 * Class using for saving player actors. Players data is
 * saved in SQL database.
 * @see SqlAsyncDB
 */
public class PlayerSaveDB
{
    /**
     * Interface used for callback. Requester must provide
     * object with this interface. This object will be
     * informed using proper functions about success.
     */
    public static interface InfoReceiver
    {
        /**
         * Called when save failed.
         */
        void saveFailed();

        /**
         * Called when save succeeded.
         */
        void saveSucceded();
    }

    private class SaveActor extends SqlRequest
    {
        private InfoReceiver receiver;
        private Actor actor;

        public SaveActor(InfoReceiver receiver, Actor actor, boolean hardCopy)
        {
            if (hardCopy)
                this.actor = actor.deepCopy();
            else
                this.actor = actor;
            this.receiver = receiver;
        }

        /** {@inheritDoc} */
        @Override
        protected boolean execute()
        {
            if (!removeArchetypes())
                return operationFailed();

            if (!saveArchetypes())
                return operationFailed();

            if (!removeItems())
                return operationFailed();

            if (!saveItems())
                return operationFailed();

            if (!saveActor())
                return operationFailed();

            if (receiver != null)
                receiver.saveSucceded();
            return true;
        }

        private boolean operationFailed()
        {
            if (receiver != null)
                receiver.saveFailed();
            return false;
        }

        private boolean saveItems()
        {
            //save inventory
            int itemId = 0;
            for (Item item : actor.getAllItems())
            {
                if (!executeStatement(itemsSaveStatement,
                                      actor.getId(),
                                      itemId,
                                      item.getType().getId(),
                                      item.getKind(),
                                      item.getEquippedPosition().ordinal()))
                {
                    Log.logger.error("Error while executing save item query.");
                    return false;
                }
                Set<Short> modifiers = item.getModifierMap().keySet();

                for (Short modifier : modifiers)
                {
                    if (!executeStatement(modifierSaveStatement,
                                          actor.getId(),
                                          itemId,
                                          modifier.shortValue()))
                    {
                        Log.logger.error("Error while executing save modifiers query: "
                            + modifierSaveStatement);
                        return false;
                    }
                }

                List<Short> effects = item.getEffectIdList();

                for (Short effect : effects)
                {
                    if (!executeStatement(effectsSaveStatement,
                                          actor.getId(),
                                          itemId,
                                          effect.shortValue()))
                    {
                        Log.logger.error("Error while executing save effects query: "
                            + effectsSaveStatement);
                        return false;
                    }
                }
                itemId++;
            }

            return true;
        }

        private boolean removeItems()
        {
            if (!executeStatement(itemsDeleteStatement, actor.getId()))
            {
                Log.logger.error("Error while deleting items for actor: "
                    + actor.getId());
                return false;
            }
            //this statement is executed because we may not have delete cascade in database
            if (!executeStatement(modifiersDeleteStatement, actor.getId()))
            {
                Log.logger.error("Error while deleting item modifiers for actor: "
                    + actor.getId());
                return false;
            }
            //this statement is also executed because we may not have delete cascade in database
            if (!executeStatement(effectsDeleteStatement, actor.getId()))
            {
                Log.logger.error("Error while deleting item effects for actor: "
                    + actor.getId());
                return false;
            }
            return true;
        }

        private boolean removeArchetypes()
        {
            return executeStatement(archetypeDeleteStatement, actor.getId());
        }

        private boolean saveActor()
        {
            return executeStatement(actorSaveStatement,
                                    actor.getLevel(),
                                    actor.getExperience(),
                                    actor.getSafeMapId(),
                                    (byte) actor.getGender().ordinal(),
                                    actor.getStrength(),
                                    actor.getWillpower(),
                                    actor.getAgility(),
                                    actor.getFinesse(),
                                    actor.getMoney(),
                                    actor.getId());
        }

        private boolean saveArchetypes()
        {
            List<Archetype> archetypes = new LinkedList<Archetype>();
            for (Short s : actor.getPersistenArchetypes())
            {
                Archetype a = archetypeDB.getArchetype(s);
                if (a == null)
                    return false;
                archetypes.add(a);
                if (!executeStatement(archetypeSaveStatement, actor.getId(), s))
                    return false;
            }

            ArchetypeBuilder.divide(actor, archetypes);
            return true;
        }

        /** {@inheritDoc} */
        @Override
        protected boolean shouldBeFlushed()
        {
            return true;
        }
    }

    private PreparedStatement archetypeDeleteStatement;
    private PreparedStatement archetypeSaveStatement;
    private PreparedStatement actorSaveStatement;
    private PreparedStatement itemsDeleteStatement;
    private PreparedStatement modifiersDeleteStatement;
    private PreparedStatement effectsDeleteStatement;
    private PreparedStatement itemsSaveStatement;
    private PreparedStatement modifierSaveStatement;
    private PreparedStatement effectsSaveStatement;

    private ArchetypeDB archetypeDB;

    /**
     * Constructor. Global instance of {@link SqlAsyncDB}
     * must exists. Allocates some some resources (like
     * prepared statements).
     * @param archetypeDB
     *            database used to load archetypes
     */
    public PlayerSaveDB(ArchetypeDB archetypeDB)
    {
        this.archetypeDB = archetypeDB;
        SqlAsyncDB db = SqlAsyncDB.globalInstance();
        assert db != null;

        archetypeDeleteStatement =
                db.prepareStatement("DELETE FROM Archetypes WHERE Avatar_id=?");

        archetypeSaveStatement =
                db.prepareStatement("INSERT INTO Archetypes (Avatar_id,Archetype_id) VALUES(?,?)");

        actorSaveStatement =
                db.prepareStatement("UPDATE Avatars SET Level=?, Experience=?, Safe_Map_Id=?, Gender=?, Strength=?, Willpower=?, Agility=?, Finesse=?, Money=? WHERE Avatar_id=?");

        itemsDeleteStatement =
                db.prepareStatement("DELETE FROM Inventory WHERE Avatar_id=?");

        itemsSaveStatement =
                db.prepareStatement("INSERT INTO Inventory (Avatar_id,Item_id, Item_type, Item_kind, Item_position) VALUES(?,?,?,?,?)");

        modifierSaveStatement =
                db.prepareStatement("INSERT INTO Item_Modifiers (Avatar_id,Item_id, Modifier_id) VALUES(?,?,?)");

        effectsSaveStatement =
                db.prepareStatement("INSERT INTO Item_Effects (Avatar_id,Item_id, Effect_id) VALUES(?,?,?)");

        modifiersDeleteStatement =
                db.prepareStatement("DELETE FROM Item_Modifiers WHERE Avatar_id=?");

        effectsDeleteStatement =
                db.prepareStatement("DELETE FROM Item_Effects WHERE Avatar_id=?");

        Log.logger.debug("PlayerSaveDB created");
    }

    /**
     * Frees resources allocated by this object. After
     * calling this function this object is no more usable.
     */
    public void freeResources()
    {
        if (archetypeDeleteStatement == null)
            return;

        try
        {
            if (archetypeDeleteStatement != null)
                archetypeDeleteStatement.close();
            if (archetypeSaveStatement != null)
                archetypeSaveStatement.close();
            if (actorSaveStatement != null)
                actorSaveStatement.close();
            if (itemsDeleteStatement != null)
                itemsDeleteStatement.close();
            if (itemsSaveStatement != null)
                itemsSaveStatement.close();
            if (modifierSaveStatement != null)
                modifierSaveStatement.close();
            if (effectsSaveStatement != null)
                effectsSaveStatement.close();
        }
        catch (SQLException e)
        {
            Log.logger.error(e);
        }

        archetypeDeleteStatement = null;
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
        return archetypeDeleteStatement != null
            && archetypeSaveStatement != null && actorSaveStatement != null
            && itemsDeleteStatement != null && itemsSaveStatement != null;
    }

    /**
     * Puts actor for save in SQL database.
     * @param receiver
     *            optional receiver which will be informed
     *            about effect of operation
     * @param actor
     *            actor to save
     * @param hardCopy
     *            when {@code true} a copy of actor will be
     *            created before enqueue it
     */
    public void saveActor(InfoReceiver receiver, Actor actor, boolean hardCopy)
    {
        SqlAsyncDB db = SqlAsyncDB.globalInstance();
        assert db != null;
        db.executeSQL(new SaveActor(receiver, actor, hardCopy));
    }
}
