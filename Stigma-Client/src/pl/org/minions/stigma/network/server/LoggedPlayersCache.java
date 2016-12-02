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
package pl.org.minions.stigma.network.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import pl.org.minions.stigma.databases.server.PlayerSaveDB;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.globals.GlobalTimer;
import pl.org.minions.stigma.network.messaging.system.SystemMessage;
import pl.org.minions.stigma.server.ServerConfig;
import pl.org.minions.stigma.server.managers.ActorManager;
import pl.org.minions.utils.logger.Log;

/**
 * Class containing info about current logged players. Used
 * for restoring broken connections, redirect existing
 * connections etc. Disconnected players are removed from
 * this "cache" using class own thread.
 */
public class LoggedPlayersCache
{
    private class CleaningTask extends TimerTask
    {
        private final int worldSaveInterval =
                ServerConfig.globalInstance().getWorldSaveInterval()
                    / ServerConfig.globalInstance().getReconnectGracePeriod();

        private List<Integer> toRemoveLoggedIn = new LinkedList<Integer>();
        private List<Integer> toRemoveLoggedOut = new LinkedList<Integer>();
        private int lastWorldSave = worldSaveInterval; // so it will not instantly save
        private int currentCycle;

        @Override
        public void run()
        {
            ++currentCycle;

            synchronized (recentlyLoggedOut)
            {
                for (Integer i : toRemoveLoggedOut)
                {
                    recentlyLoggedOut.remove(i);
                }

                toRemoveLoggedOut.clear();

                for (Integer i : recentlyLoggedOut)
                {
                    toRemoveLoggedOut.add(i);
                }
            }

            synchronized (connectionMap)
            {
                for (Integer i : toRemoveLoggedIn)
                {
                    ActorManager m = connectionMap.get(i);
                    if (m != null && !m.isWorking())
                    {
                        m.getWorldManager().removeActor(m);
                        connectionMap.remove(i);
                    }
                }

                toRemoveLoggedIn.clear();

                boolean performWorldSave = false;
                if (currentCycle - lastWorldSave >= worldSaveInterval)
                {
                    performWorldSave = true;
                    lastWorldSave = currentCycle;
                }

                Collection<ActorManager> loggedOn = connectionMap.values();
                Iterator<ActorManager> i = loggedOn.iterator();
                while (i.hasNext())
                {
                    final ActorManager manager = i.next();
                    if (!manager.isWorking())
                        toRemoveLoggedIn.add(manager.getActor().getId());
                    if (performWorldSave)
                        playerSaveDB.saveActor(new PlayerSaveDB.InfoReceiver()
                        {
                            @Override
                            public void saveFailed()
                            {
                                // nothing... 
                            }

                            @Override
                            public void saveSucceded()
                            {
                                manager.sendEvent(SystemMessage.SystemEventType.WORLD_SAVED);
                            }
                        },
                                               manager.getActor(),
                                               true);
                }
                if (performWorldSave)
                    Log.logger.info("World save performed");
            }
        }
    }

    private PlayerSaveDB playerSaveDB;
    private Map<Integer, ActorManager> connectionMap =
            new HashMap<Integer, ActorManager>();

    private Set<Integer> recentlyLoggedOut = new HashSet<Integer>();

    private TimerTask cleaningTask;

    /**
     * Creates empty cache and starts thread responsible for
     * cleaning it and performing 'world saves'.
     * @param playerSaveDB
     *            database for saving actors in 'world save'
     */
    public LoggedPlayersCache(PlayerSaveDB playerSaveDB)
    {
        this.playerSaveDB = playerSaveDB;

        final int cleaningInterval =
                ServerConfig.globalInstance().getReconnectGracePeriod() * 60 * 1000;

        cleaningTask = new CleaningTask();
        GlobalTimer.getTimer().scheduleAtFixedRate(cleaningTask,
                                                   cleaningInterval,
                                                   cleaningInterval);
    }

    /**
     * Adds new {@link ActorManager} to logged actors
     * database. If there was some previous of the same id
     * it is overwritten.
     * @param manager
     *            actor manager to be added
     */
    public void addActorManager(ActorManager manager)
    {
        synchronized (connectionMap)
        {
            connectionMap.put(manager.getActor().getId(), manager);
        }
    }

    /**
     * Returns {@link ActorManager} responsible for player
     * of given id or {@code null} if such actor is not
     * logged.
     * @param id
     *            id for which actor manager should be
     *            returned
     * @return actor manager responsible for logged player
     *         of given id or {@code null}
     */
    public ActorManager getActorManager(int id)
    {
        synchronized (connectionMap)
        {
            return connectionMap.get(id);
        }
    }

    /**
     * Returns {@code true} when actor of given id was
     * recently logged out. Such actor should not be allowed
     * to log in.
     * @param id
     *            id of actor to check
     * @return wheter or not actor was recently logged out
     */
    public boolean hasRecentlyLoggedOut(int id)
    {
        synchronized (recentlyLoggedOut)
        {
            return recentlyLoggedOut.contains(id);
        }
    }

    /**
     * Logs out player. Puts its id in collection of
     * recently logged out (until at least next cleaning
     * this actor should not be available) and sends actor
     * for saving. After successful saving connection is
     * closed.
     * @param manager
     *            actor's manager to log out
     */
    public void logoutActor(final ActorManager manager)
    {
        final Actor actor = manager.getActor();
        final Integer id = actor.getId();
        playerSaveDB.saveActor(new PlayerSaveDB.InfoReceiver()
        {
            @Override
            public void saveFailed()
            {
                Log.logger.error("Saving actor on logout failed for: "
                    + actor.getId() + " name: " + actor.getName());
                manager.disconnect();
            }

            @Override
            public void saveSucceded()
            {
                if (Log.isDebugEnabled())
                    Log.logger.debug("Saving actor succeeded for: "
                        + actor.getName());
                manager.sendEvent(SystemMessage.SystemEventType.LOGGED_OUT);
                manager.disconnect();
            }
        }, actor, false);
        synchronized (connectionMap)
        {
            connectionMap.remove(id);
        }
        synchronized (recentlyLoggedOut)
        {
            recentlyLoggedOut.add(id);
        }
    }

    /**
     * Stops cleaning thread.
     */
    public void stop()
    {
        cleaningTask.cancel();
        for (ActorManager m : connectionMap.values())
            m.disconnect();
    }
}
