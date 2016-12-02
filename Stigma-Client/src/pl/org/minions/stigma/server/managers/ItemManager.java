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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.server.item.ItemGenerator;
import pl.org.minions.utils.logger.Log;

/**
 * Class that manages items. It starts its own thread to
 * perform actions on items.
 */
public class ItemManager
{
    private class ItemThread implements Runnable
    {
        /** {@inheritDoc} */
        @Override
        public void run()
        {
            while (!Thread.interrupted())
            {
                Request req;
                try
                {
                    req = requestQueue.take();
                }
                catch (InterruptedException e)
                {
                    break;
                }

                req.execute();
            }
            Log.logger.debug("Item thread interrupted");
        }
    }

    private interface Request
    {
        void execute();
    }

    private static class CreateItemsRequest implements Request
    {
        private WorldManager worldManager;
        private MapInstance map;
        private ItemGenerator generator;

        public CreateItemsRequest(WorldManager worldManager,
                                  MapInstance map,
                                  ItemGenerator generator)
        {
            this.worldManager = worldManager;
            this.map = map;
            this.generator = generator;
        }

        /** {@inheritDoc} */
        @Override
        public void execute()
        {
            if (!generator.putItems(worldManager, map))
            {
                Log.logger.info("ItemGenerator#putItems failed");
            }
        }
    }

    private Thread itemThread;
    private BlockingQueue<Request> requestQueue =
            new LinkedBlockingQueue<Request>();

    /**
     * Constructor. Starts thread.
     */
    public ItemManager()
    {
        itemThread = new Thread(new ItemThread(), "itemThread");
        itemThread.start();
    }

    private void enqueueRequest(Request req)
    {
        try
        {
            requestQueue.put(req);
        }
        catch (InterruptedException e)
        {
            return;
        }
    }

    /**
     * Enqueues request for creation of items.
     * @param worldManager
     *            world manager in which items should be
     *            created
     * @param map
     *            map on which items should be put
     * @param generator
     *            generator of static items
     */
    public void createItems(WorldManager worldManager,
                            MapInstance map,
                            ItemGenerator generator)
    {
        enqueueRequest(new CreateItemsRequest(worldManager, map, generator));
    }

    /**
     * Stops thread.
     */
    public void stop()
    {
        if (itemThread != null)
        {
            itemThread.interrupt();
            itemThread = null;
        }
        requestQueue.clear();
    }
}
