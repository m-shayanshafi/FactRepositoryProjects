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
package pl.org.minions.stigma.client.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;

import pl.org.minions.stigma.game.item.type.ItemType.ItemKind;
import pl.org.minions.stigma.game.map.TerrainType;
import pl.org.minions.stigma.globals.GlobalConfig;
import pl.org.minions.utils.logger.Log;

/**
 * Class used for asynchronous loading of images from
 * "client-resource-root" location.
 */
public final class ImageDB
{
    private class ReadingThread implements Runnable
    {
        /** {@inheritDoc} */
        @Override
        public void run()
        {
            while (!Thread.interrupted())
            {
                ResourceImageProxy proxy;
                try
                {
                    proxy = queue.take();
                }
                catch (InterruptedException e)
                {
                    break;
                }

                BufferedImage img = read(proxy.getName());

                if (img == null)
                    proxy.setState(ImageProxy.LoadingState.FAILED);

                else if (proxy.setImage(img))
                {
                    proxy.setState(ImageProxy.LoadingState.LOADED);
                }
                else
                    proxy.setState(ImageProxy.LoadingState.FAILED);
            }
            Log.logger.debug("Image reading thread interrupted");
        }
    }

    public static final String DEFAULT_IMAGE_FILE_EXTENSION = ".png";

    public static final String DEFAULT_IMAGE_FILE_DIRECTORY = "images";

    private static ImageDB instance;

    private BlockingQueue<ResourceImageProxy> queue =
            new LinkedBlockingQueue<ResourceImageProxy>();

    private Map<TerrainType, TerrainImageProxy> terrainImages =
            new HashMap<TerrainType, TerrainImageProxy>();
    private Map<String, ResourceImageProxy> cache =
            new HashMap<String, ResourceImageProxy>();

    private Thread readingThread;

    private ImageDB()
    {
        readingThread = new Thread(new ReadingThread(), "imageReading");
        readingThread.setDaemon(true);
        readingThread.start();
    }

    /**
     * Builds image name from given name and category
     * (directory). Adds file extension if needed (
     * {@value #DEFAULT_IMAGE_FILE_EXTENSION}).
     * @param category
     *            directory to which image belongs
     * @param name
     *            name of exact image
     * @return full name which can be combined with URI
     */
    public static String getImageName(String category, String name)
    {
        if (name == null || category == null)
            return null;

        if (!name.endsWith(DEFAULT_IMAGE_FILE_EXTENSION))
            name += DEFAULT_IMAGE_FILE_EXTENSION;
        return DEFAULT_IMAGE_FILE_DIRECTORY + '/' + category + '/' + name;
    }

    /**
     * Returns singleton global instance.
     * @return singleton global instance
     */
    public static ImageDB globalInstance()
    {
        if (instance == null)
            instance = new ImageDB();
        return instance;
    }

    /**
     * Clears all cached requests.
     */
    public void clear()
    {
        synchronized (terrainImages)
        {
            terrainImages.clear();
        }

        queue.clear();
    }

    /**
     * Removes cached item. Next call to
     * {@link #getTerrainImage(TerrainTypeImageId)} will
     * enforce reloading of image.
     * @param type
     *            type of terrain to clear cache for
     */
    public void clearCachedTerrain(TerrainType type)
    {
        synchronized (terrainImages)
        {
            ImageProxy proxy = terrainImages.remove(type);
            if (proxy != null)
                queue.remove(proxy);
        }
    }

    private void enqueueProxy(ResourceImageProxy proxy)
    {
        try
        {
            queue.put(proxy);
        }
        catch (InterruptedException e)
        {
            Log.logger.debug("Put interrupted");
        }
    }

    /**
     * Returns image proxy for requested terrain image id.
     * <p>
     * Proxied image is scheduled for loading and cached
     * within ImageDB.
     * @param id
     *            terrain element id
     * @return proxy for requested image
     */
    public ImageProxy getTerrainImage(TerrainTypeImageId id)
    {
        synchronized (terrainImages)
        {
            TerrainImageProxy terrainProxy =
                    terrainImages.get(id.getTerrainType());
            if (terrainProxy == null)
            {
                terrainProxy = new TerrainImageProxy(id.getTerrainType());
                enqueueProxy(terrainProxy);
                terrainImages.put(id.getTerrainType(), terrainProxy);
            }

            return terrainProxy.getSubProxy(id.getImageId());
        }
    }

    /**
     * Returns image proxy for given item kind and icon
     * name. Proxied image is scheduled for loading and
     * cached within ImageDB.
     * @param kind
     *            kind of item image is for
     * @param name
     *            name of icon
     * @return proxy for requested image
     */
    public ImageProxy getItemIcon(ItemKind kind, String name)
    {
        switch (kind)
        {
            case ARMOR:
                name = "armors/" + name;
                break;
            case WEAPON:
                name = "weapons/" + name;
                break;
            case OTHER:
            default:
                break;
        }

        name = getImageName("items", name);
        synchronized (cache)
        {
            ResourceImageProxy proxy = cache.get(name);
            if (proxy != null)
                return proxy;
            proxy = new ResourceImageProxy(name);
            enqueueProxy(proxy);
            cache.put(name, proxy);
            return proxy;
        }
    }

    private BufferedImage read(String name)
    {
        if (name == null)
            return null;

        URI base = GlobalConfig.globalInstance().getClientResourceUri();
        URI file;
        try
        {
            file =
                    new URI(base.getScheme(),
                            base.getUserInfo(),
                            base.getHost(),
                            base.getPort(),
                            base.getPath() + '/' + name,
                            base.getQuery(),
                            base.getFragment());
        }
        catch (URISyntaxException e)
        {
            Log.logger.error("URI creation failed!", e);
            return null;
        }

        URL url;
        if (file.isAbsolute())
            try
            {
                url = file.toURL();
            }
            catch (MalformedURLException e)
            {
                Log.logger.error("URI -> URL failed: " + e);
                return null;
            }
        else
        {
            try
            {
                url = new File(file.getPath()).toURI().toURL();
            }
            catch (MalformedURLException e)
            {
                Log.logger.error("relative URI -> URL failed: " + e);
                return null;
            }
        }

        BufferedImage img;
        try
        {
            img = ImageIO.read(url);
        }
        catch (IOException e)
        {
            Log.logger.error("Reading image '" + name + "' failed: " + e);
            return null;
        }

        return img;
    }
}
