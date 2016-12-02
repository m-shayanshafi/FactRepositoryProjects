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
package pl.org.minions.stigma.server.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.TimerTask;
import java.util.zip.GZIPOutputStream;

import pl.org.minions.stigma.globals.GlobalTimer;
import pl.org.minions.stigma.server.Server;
import pl.org.minions.stigma.server.ServerConfig;
import pl.org.minions.stigma.server.ServerObserver;
import pl.org.minions.utils.logger.Log;

/**
 * Class used to manage resources. It is able to return
 * resource data both form cache and persistent memory and
 * has ability to manage both compressed and not compressed
 * resource data.
 */
public class ResourceManager
{
    /**
     * Observer used to create directory with compressed
     * files on server startup and delete it while server is
     * shutting down.
     */
    private class ResourceCompressionObserver implements ServerObserver
    {
        private static final int COMPRESSION_BUFFER_SIZE = 1024;
        private TimerTask cleaningTask;

        private void cleanDirectory(String directory)
        {
            File dir = new File(directory);

            if (!dir.isDirectory())
            {
                Log.logger.error("Trying to delete file "
                    + dir.getAbsolutePath());
                return;
            }

            for (String d : dir.list())
            {
                File dirOrFile =
                        new File(dir.getAbsolutePath() + File.separator + d);

                if (dirOrFile.isDirectory())
                {
                    cleanDirectory(dirOrFile.getAbsolutePath());
                }
                else
                {
                    if (dirOrFile.getName().endsWith(COMPRESSED_FILE_SUFFIX))
                    {
                        boolean success = dirOrFile.delete();
                        if (!success)
                        {
                            Log.logger.error("Unable to delete compressed file "
                                + dirOrFile.getAbsolutePath());
                        }
                    }
                }
            }

        }

        private void compressDirectory(String directoryName)
        {
            File dir = new File(directoryName);

            if (dir.isDirectory())
            {
                for (String d : dir.list())
                {
                    File dirOrFile =
                            new File(dir.getAbsolutePath() + File.separator + d);

                    if (dirOrFile.isDirectory()
                        && !dirOrFile.getName().startsWith("."))
                    {
                        compressDirectory(directoryName + File.separator + d);
                    }
                    else if (!dirOrFile.isDirectory()
                        && dirOrFile.getName()
                                    .endsWith(COMPRESSABLE_FILE_SUFFIX))
                    {
                        try
                        {
                            File compressedFile =
                                    new File(dirOrFile.getAbsolutePath()
                                        + COMPRESSED_FILE_SUFFIX);
                            compressedFile.createNewFile();
                            compressedFile.setWritable(true);
                            FileInputStream fis =
                                    new FileInputStream(dirOrFile);
                            GZIPOutputStream gzipos =
                                    new GZIPOutputStream(new FileOutputStream(compressedFile));

                            int len = 0;
                            byte[] buf = new byte[COMPRESSION_BUFFER_SIZE];
                            while ((len = fis.read(buf)) > 0)
                            {
                                gzipos.write(buf, 0, len);
                            }

                            gzipos.close();
                            fis.close();
                        }
                        catch (IOException e)
                        {
                            Log.logger.fatal("Error while compressing data. File: "
                                                 + dirOrFile.getAbsolutePath(),
                                             e);
                            System.exit(1);
                        }
                    }
                }
            }
            else
            {
                Log.logger.fatal("FATAL: trying to list file!");
                System.exit(1);
            }
        }

        /** {@inheritDoc} */
        @Override
        public void stateChanged()
        {
            if (Server.globalInstance().getState().equals(Server.State.BOOTING))
            {
                Log.logger.info("Compressing files.");

                compressDirectory(ServerConfig.globalInstance()
                                              .getResourceRoot()
                    + File.separator
                    + ServerConfig.globalInstance().getResourceSet());

                int time =
                        ServerConfig.globalInstance()
                                    .getResourceCacheValidationPeriod();
                if (time > 0)
                {
                    cleaningTask = new TimerTask()
                    {
                        @Override
                        public void run()
                        {
                            cleanCache();
                        }
                    };
                    GlobalTimer.getTimer().schedule(cleaningTask, time, time);
                }
            }
            else if (Server.globalInstance()
                           .getState()
                           .equals(Server.State.SHUTTING))
            {
                Log.logger.info("Deleting compressed files.");

                if (cleaningTask != null)
                    cleaningTask.cancel();

                //delete all elements in compressed directory
                cleanDirectory(ServerConfig.globalInstance().getResourceRoot()
                    + File.separator
                    + ServerConfig.globalInstance().getResourceSet());
            }
        }

    }

    private static final String COMPRESSABLE_FILE_SUFFIX = ".xml";

    /** Suffix added to name of compressed file */
    private static final String COMPRESSED_FILE_SUFFIX = ".gz";

    private ResourceCache cache;

    /**
     * Default constructor.
     */
    public ResourceManager()
    {
        this.cache = ResourceCache.getInstance();

        // Adds observer to create and delete compressed files
        if (ServerConfig.globalInstance().getClientResourceCompression())
        {
            Server.globalInstance()
                  .addObserver(new ResourceCompressionObserver());
        }
        else
        {
            Log.logger.debug("Started resource manager without compression.");
        }
    }

    private static byte[] readFile(File file) throws IOException
    {
        FileInputStream is = new FileInputStream(file);

        long length = file.length();
        byte[] bytes = new byte[(int) length];

        // Read bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
            && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)
        {
            offset += numRead;
        }

        // Check whether whole file has been read
        if (offset < bytes.length)
        {
            throw new IOException("Could not completely read file "
                + file.getName());
        }

        is.close();
        return bytes;
    }

    /**
     * Cleans cache.
     */
    public void cleanCache()
    {
        cache.cleanCache();
    }

    /**
     * Returns byte array with represents resource data.
     * Most important method in this class.
     * @param path
     *            path to resource
     * @param compressed
     *            indicates whether manager should return
     *            compressed resources
     * @return byte array with resource data
     */
    public byte[] getResourceData(String path, boolean compressed)
    {
        byte[] result = null;

        result = cache.getResource(path);

        // if data is not in cache read it from file and put to cache
        if (result == null)
        {
            String resourcePath = null;

            // calculate path of resource
            // add COMPRESSED_TEMPORARY_DIRECTORY_SUFFIX to directory name if resources are compressed
            // and add COMPRESSED_FILE_SUFFIX to file name also if resources are compressed
            resourcePath =
                    ServerConfig.globalInstance().getResourceRoot()
                        + File.separator + path;

            try
            {
                File file = new File(resourcePath);
                if (file.exists() && !file.isDirectory())
                    result = readFile(file);
                else
                    Log.logger.debug("Resource " + resourcePath
                        + " not found or is not a file.");
            }
            catch (IOException e)
            {
                Log.logger.error("Error while reading file " + resourcePath, e);
                return null;
            }

            //add resource to cache
            cache.addResource(path, result);
        }

        return result;
    }

}
