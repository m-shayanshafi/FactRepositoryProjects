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
package pl.org.minions.stigma.server.managers;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import pl.org.minions.stigma.databases.actor.ArchetypeDB;
import pl.org.minions.stigma.databases.actor.ProficiencyDB;
import pl.org.minions.stigma.databases.item.ItemTypeDB;
import pl.org.minions.stigma.databases.item.ModifierDB;
import pl.org.minions.stigma.databases.map.server.StaticItemsDB;
import pl.org.minions.stigma.databases.map.server.StaticNpcsDB;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.command.special.ApplyData;
import pl.org.minions.stigma.game.command.special.ApplyEvents;
import pl.org.minions.stigma.game.data.WorldData;
import pl.org.minions.stigma.game.data.info.SegmentInfo;
import pl.org.minions.stigma.game.event.actor.ActorAdded;
import pl.org.minions.stigma.game.event.actor.ActorRemoved;
import pl.org.minions.stigma.game.event.item.ItemAdded;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.game.map.MapInstance.Segment;
import pl.org.minions.stigma.game.world.ExtendedWorld;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.game.world.ExtendedWorld.MapCreationObserver;
import pl.org.minions.stigma.globals.Position;
import pl.org.minions.stigma.network.messaging.game.CommandMessage;
import pl.org.minions.stigma.server.ServerConfig;
import pl.org.minions.stigma.server.item.StaticItems;
import pl.org.minions.stigma.server.npc.StaticNpcs;
import pl.org.minions.utils.logger.Log;

/**
 * Class responsible for managing world. It executes
 * commands provided by {@link ActorManager ActorManagers}
 * and sends their results to all interested actors. World
 * is managed in <i>turns</i>. Every <i>turn</i> takes given
 * (configured via
 * {@link ServerConfig#getMillisecondsPerTurn()}) amount of
 * time. No more no less. During every <i>turn</i> new
 * actors are added, disconnected are removed,
 * "time effects" are checked. After this there are executed
 * as many commands as possible in given time, but no more
 * than one per actor (see {@link ActorManagerCollection}).
 */
public class WorldManager
{
    private class TimerThread implements Runnable
    {
        private final int sleepTime =
                ServerConfig.globalInstance().getMillisecondsPerTurn();

        /** {@inheritDoc} */
        @Override
        public void run()
        {
            while (!Thread.interrupted())
            {
                if (timerTick)
                {
                    Log.logger.warn("Double turn. Tweak server config.");
                }
                synchronized (notifier)
                {
                    timerTick = true;
                    world.incrementTurnNumber();
                    notifier.notify();
                }
                try
                {
                    Thread.sleep(sleepTime);
                }
                catch (InterruptedException e)
                {
                    break;
                }
            }
            Log.logger.debug("Timer thread interrupted");
        }
    }

    private class WorldManagerThread implements Runnable
    {
        /** {@inheritDoc} */
        @Override
        public void run()
        {
            while (!Thread.interrupted())
            {
                timerTick = false;

                synchronized (managersToRemove)
                {
                    for (ActorManager a : managersToRemove)
                        innerRemoveActor(a);
                    managersToRemove.clear();
                }
                if (timerTick)
                {
                    Log.logger.info("Turn done only to \"removing actors\"");
                    continue;
                }

                synchronized (managersToAdd)
                {
                    for (ActorManager a : managersToAdd)
                        innerAddActor(a);
                    managersToAdd.clear();
                }
                if (timerTick)
                {
                    Log.logger.info("Turn done only to \"adding actors\"");
                    continue;
                }

                synchronized (itemsToAdd)
                {
                    for (Item i : itemsToAdd)
                        innerAddItem(i);
                    itemsToAdd.clear();
                }
                if (timerTick)
                {
                    Log.logger.info("Turn done only to \"adding items\"");
                    continue;
                }

                actorManagerCollection.markPollStart();
                world.markTurn();

                if (timerTick)
                {
                    Log.logger.info("Turn done only to \"cooldown observers\"");
                    continue;
                }

                while (!timerTick)
                {
                    Command command = actorManagerCollection.poll();
                    if (command == null) // no one have something interested to say
                        break;
                    executeAndSend(command);
                }

                npcManager.pollResponses(world.getTurnNumber() + 1);

                synchronized (notifier)
                {
                    try
                    {
                        while (!timerTick)
                            notifier.wait();
                    }
                    catch (InterruptedException e)
                    {
                        break;
                    }
                }
            }
            Log.logger.debug("WorldManager thread interrupted");
        }
    }

    private Random random = new Random();
    private ExtendedWorld world;

    private StaticNpcsDB npcLayerDB;
    private StaticItemsDB itemsLayerDB;
    private ArchetypeDB archetypeDB;
    private ItemTypeDB itemTypeDB;
    private ModifierDB modifierDB;
    private ProficiencyDB proficiencyDB;

    private ActorManagerCollection actorManagerCollection =
            new ActorManagerCollection();

    private Thread managingThread;
    private Thread timerThread;

    private volatile boolean timerTick;
    private Object notifier = new Object();

    // Those are separated so synchronization is only on those, not on whole collection, so it may be faster    
    private List<ActorManager> managersToAdd = new LinkedList<ActorManager>();
    private List<ActorManager> managersToRemove =
            new LinkedList<ActorManager>();

    private List<Item> itemsToAdd = new LinkedList<Item>();

    private NpcManager npcManager = new NpcManager();
    private ItemManager itemManager = new ItemManager();

    /**
     * Creates world manager for given world. Starts
     * managing threads.
     * @param world
     *            world to manage
     * @param chatManager
     *            chat manager used by this world (not
     *            stored, just passed to NPCs...)
     * @param layerDB
     *            NPCs layers database
     * @param itemsDB
     *            item layers database
     * @param archetypeDB
     *            archetype database which should be used by
     *            this manager (for example for NPC
     *            creation)
     * @param itemTypeDB
     *            item type database which should be used by
     *            this manager (for example for item
     *            creation)
     * @param modifierDB
     *            item modifier database which should be
     *            used by this manager (for example for
     *            modifier creation)
     * @param proficiencyDB
     *            proficiency database which should be used
     *            by this manager
     */
    public WorldManager(ExtendedWorld world,
                        final ChatManager chatManager,
                        StaticNpcsDB layerDB,
                        StaticItemsDB itemsDB,
                        ArchetypeDB archetypeDB,
                        ItemTypeDB itemTypeDB,
                        ModifierDB modifierDB,
                        ProficiencyDB proficiencyDB)
    {
        this.world = world;
        this.npcLayerDB = layerDB;
        this.itemsLayerDB = itemsDB;
        this.archetypeDB = archetypeDB;
        this.itemTypeDB = itemTypeDB;
        this.modifierDB = modifierDB;
        this.proficiencyDB = proficiencyDB;

        world.addMapCreationObserver(new MapCreationObserver()
        {
            @Override
            public void notify(MapInstance newMap)
            {
                // static NPCs should have same id like map type
                short layerId = newMap.getType().getId();
                StaticNpcs npcLayer =
                        WorldManager.this.npcLayerDB.getById(layerId);
                if (npcLayer != null)
                    npcManager.createNpcs(WorldManager.this,
                                          chatManager,
                                          newMap,
                                          npcLayer);

                // static items should have same id like map type
                // and static NPCs layer 
                StaticItems itemsLayer =
                        WorldManager.this.itemsLayerDB.getById(layerId);
                if (itemsLayer != null)
                    itemManager.createItems(WorldManager.this,
                                            newMap,
                                            itemsLayer);
            }
        });

        managingThread =
                new Thread(new WorldManagerThread(), "worldManagingThread");
        managingThread.setPriority(Thread.MAX_PRIORITY);
        managingThread.start();
        timerThread = new Thread(new TimerThread(), "worldTimerThread");
        timerThread.setPriority(Thread.MAX_PRIORITY);
        timerThread.start();
    }

    /**
     * Schedules given actor (and it's manager) to add to
     * world for next turn.
     * @param a
     *            actor manager to add
     */
    public void addActor(ActorManager a)
    {
        synchronized (managersToAdd)
        {
            managersToAdd.add(a);
        }
    }

    /**
     * Schedules given item to add to world for next turn.
     * @param i
     *            item to add.
     */
    public void addItem(Item i)
    {
        synchronized (itemsToAdd)
        {
            itemsToAdd.add(i);
        }
    }

    private void executeAndSend(Command command)
    {
        if (command.getType().isStoppedByCooldown()
            && command.getRequesterId() != 0) // not an "inner" command
        {
            Actor a = world.getActor(command.getRequesterId());
            if (a != null && a.getCooldown() > world.getTurnNumber())
            {
                Log.logger.info(MessageFormat.format("Tried to execute {0} before end of cooldown (cooldown:{1}, turn:{2})",
                                                     command,
                                                     a.getCooldown(),
                                                     world.getTurnNumber()));
                returnToRequester(command);
                return;
            }
            if (Log.isTraceEnabled())
                Log.logger.trace("Received cooled down command for Actor id: "
                    + a.getId() + " turn no: " + world.getTurnNumber()
                    + " cooldown: " + a.getCooldown() + " diff: "
                    + (world.getTurnNumber() - a.getCooldown()));
        }

        if (!command.execute(world))
        {
            if (Log.isTraceEnabled())
                Log.logger.trace(MessageFormat.format("Execution of command: {0} failed",
                                                      command.toString()));
            returnToRequester(command);
            return;
        }
        else if (Log.isTraceEnabled())
        {
            Log.logger.trace(MessageFormat.format("Execution of command: {0} succeeded",
                                                  command.toString()));
        }

        if (!command.apply(world, null))
        {
            Log.logger.warn(MessageFormat.format("Applying of command: {0} failed",
                                                 command.toString()));
            return;
        }
        else if (Log.isTraceEnabled())
        {
            Log.logger.trace(MessageFormat.format("Applying of command: {0} succeeded",
                                                  command.toString()));
        }

        ActorManager manager;
        if (command.getRequesterId() != 0) // not an "inner" command
        {
            manager = actorManagerCollection.get(command.getRequesterId());
            if (manager == null)
            {
                Log.logger.warn("Null manager for requester: "
                    + command.getRequesterId());
            }
            else
                manager.send(new CommandMessage(command, true));
        }

        CommandMessage cmdForAll = new CommandMessage(command);
        for (Segment s : command.affectedSegments())
        {
            for (Actor a : s.getActors())
            {
                if (a.getId() != command.getRequesterId())
                {
                    manager = actorManagerCollection.get(a.getId());
                    if (manager == null)
                    {
                        Log.logger.warn("Null manager for actor: " + a.getId());
                        continue;
                    }

                    manager.send(cmdForAll);
                }
            }
        }
    }

    /**
     * Returns AI manager used by this manager.
     * @return AI manager used by this manager.
     */
    public NpcManager getAiManager()
    {
        return npcManager;
    }

    /**
     * Returns archetype database.
     * @return archetype database
     */
    public ArchetypeDB getArchetypeDB()
    {
        return archetypeDB;
    }

    /**
     * Returns item component database.
     * @return item component database.
     */
    public ItemTypeDB getItemTypeDB()
    {
        return itemTypeDB;
    }

    /**
     * Returns modifierDB.
     * @return modifierDB
     */
    public ModifierDB getModifierDB()
    {
        return modifierDB;
    }

    /**
     * Returns world managed by this manager.
     * @return world managed by this manager
     */
    public World getWorld()
    {
        return world;
    }

    private void innerAddActor(ActorManager a)
    {
        actorManagerCollection.add(a); // so actor may be disconnected

        int turnNumber = world.getTurnNumber();
        a.getActor().setFastTS(turnNumber);
        a.getActor().setSlowTS(turnNumber);

        a.getActor().firstRecalc(); // calculates characteristics

        MapInstance m;
        if (a.getActor().getPosition() == null) // logging in / re spawning
        {
            short mapType = a.getActor().getSafeMapId();
            m = world.findReadyInstance(mapType);
            if (m != null)
            {
                if (!m.getType().isSafeMap())
                {
                    Log.logger.error(MessageFormat.format("Actor {0} has ''safe'' map id which is not ''safe'': {1}",
                                                          a.getActor().getId(),
                                                          mapType));
                    a.disconnect();
                    return;
                }

                byte safeZoneId = m.getType().getSafeEntryZoneId();
                for (Position p : m.getType()
                                   .getEntryZoneMap()
                                   .get(safeZoneId)
                                   .getPositionsList())
                {
                    if (m.isPassable(p))
                    {
                        a.getActor().setPosition(p);
                        break;
                    }
                }
            }

            if (a.getActor().getPosition() == null) // still null - create new instance
            {
                m = world.createMap(mapType, world.findFreeInstanceNo(mapType));
                List<Position> positions =
                        m.getType()
                         .getEntryZoneMap()
                         .get(m.getType().getSafeEntryZoneId())
                         .getPositionsList();
                Position p;
                if (positions.size() == 1)
                    p = positions.get(0);
                else
                    p = positions.get(random.nextInt(positions.size()));
                a.getActor().setPosition(p);
            }

            a.getActor().setMapId(m.getType().getId());
        }
        else
        {
            short mapType = a.getActor().getMapId();
            m = world.getMap(mapType, a.getActor().getMapInstanceNo());
            if (m == null || !m.isReady()
                || !m.isPassable(a.getActor().getPosition()))
                m = world.findReadyInstance(mapType);

            if (m == null || !m.isPassable(a.getActor().getPosition()))
            {
                m = world.createMap(mapType, world.findFreeInstanceNo(mapType));

                if (!m.isPassable(a.getActor().getPosition()))
                {
                    Log.logger.info(MessageFormat.format("Some bad actor position [id: {0}, mapId: {1}, position: {2}]",
                                                         a.getActor().getId(),
                                                         mapType,
                                                         a.getActor()
                                                          .getPosition()));
                    a.disconnect();
                    return;
                }
            }
        }

        a.getActor().setMapInstanceNo(m.getInstanceNo());

        MapInstance.Segment segment =
                m.getSegmentForPosition(a.getActor().getPosition());

        // info about map before sending player 'actor added', so when it will be added map should be ready
        List<WorldData> data = new LinkedList<WorldData>();
        for (MapInstance.Segment s : segment.neighborhood())
        {
            data.add(new SegmentInfo(s));
        }
        ApplyData cmd = new ApplyData(data);
        cmd.dummyExecute();
        a.send(new CommandMessage(cmd, true));

        for (Item i : a.getActor().getAllItems())
        {
            world.addItem(i);
            i.setTS(world.getTurnNumber());
        }

        executeAndSend(new ApplyEvents(segment, new ActorAdded(a.getActor())));
    }

    private void innerRemoveActor(ActorManager a)
    {
        actorManagerCollection.remove(a);

        MapInstance m =
                world.getMap(a.getActor().getMapId(), a.getActor()
                                                       .getMapInstanceNo());
        if (m == null)
            return;

        MapInstance.Segment segment =
                m.getSegmentForPosition(a.getActor().getPosition());
        if (segment == null)
            return;

        for (Item i : a.getActor().getAllItems())
        {
            world.removeItem(i.getId());
        }

        executeAndSend(new ApplyEvents(segment, new ActorRemoved(a.getActor()
                                                                  .getId())));
    }

    private void innerAddItem(Item i)
    {
        int turnNumber = world.getTurnNumber();
        i.setTS(turnNumber);

        short mapType = i.getMapId();
        short mapInstanceNo = i.getMapInstanceNo();
        MapInstance m = world.getMap(mapType, mapInstanceNo);

        if (m == null)
        {
            Log.logger.info("Trying to put item on null map: " + mapType + ", "
                + mapInstanceNo);
            return;
        }

        MapInstance.Segment segment = m.getSegmentForPosition(i.getPosition());

        executeAndSend(new ApplyEvents(segment, new ItemAdded(i)));
    }

    /**
     * Schedules given actor (with it's manager) to remove
     * for next turn.
     * @param a
     *            actor manager to remove
     */
    public void removeActor(ActorManager a)
    {
        synchronized (managersToRemove)
        {
            managersToRemove.add(a);
        }
    }

    private void returnToRequester(Command command)
    {
        ActorManager m = actorManagerCollection.get(command.getRequesterId());
        if (m == null)
            return;
        if (Log.isTraceEnabled())
            Log.logger.trace("Failed command returned to sender: " + command);
        m.send(new CommandMessage(command));
    }

    /**
     * Stops all threads used by world manager.
     */
    public void stop()
    {
        if (managingThread != null)
        {
            timerThread.interrupt();
            timerThread = null;
            managingThread.interrupt();
            managingThread = null;
        }

        npcManager.stop();
        npcManager = null;

        itemManager.stop();
        itemManager = null;
    }

    /**
     * Returns proficiencyDB.
     * @return proficiencyDB
     */
    public ProficiencyDB getProficiencyDB()
    {
        return proficiencyDB;
    }
}
