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
package pl.org.minions.stigma.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import pl.org.minions.stigma.databases.map.MapDB;
import pl.org.minions.stigma.globals.GlobalConfig;
import pl.org.minions.stigma.globals.ResourceSetProperties;
import pl.org.minions.utils.Properties;
import pl.org.minions.utils.logger.Log;

/**
 * Class containing server configuration for access SQL and
 * XML databases. Configuration is stored in standard Java
 * Properties file. It is partially singleton, only once
 * constructor may be called.
 */
public class ServerConfig extends GlobalConfig
{
    public static final String SERVER_PORT = "server-port";
    public static final String AUTH_REQUEST_TIMEOUT = "auth-request-timeout";
    public static final String AUTH_REQUEST_POOL = "auth-request-pool";
    public static final String SPAMMER_COMMANDS_COUNT =
            "spammer-commands-count";
    public static final String COMMAND_QUEUE_SIZE = "command-queue-size";
    public static final String RESOURCE_ROOT = "resource-root";
    public static final String HTTP_PORT = "http-port";
    public static final String SQL_DRIVER = "sql-driver";
    public static final String SQL_PASSWORD = "sql-password";
    public static final String SQL_LOGIN = "sql-login";
    public static final String SQL_URL = "sql-url";
    public static final String RECONNECT_GRACE_PERIOD =
            "reconnect-grace-period";
    public static final String WORLD_SAVE_INTERVAL = "world-save-interval";
    public static final String RESOURCE_CACHE_USE_LIMIT =
            "resource-cache-use-limit";
    public static final String RESOURCE_CACHE_VALIDATION_PERIOD =
            "resource-cache-validation-period";
    public static final String RESOURCE_HTTP_SERVER_ON =
            "resource-http-server-on";
    public static final String MAX_MAP_INSTANCES = "max-map-instances";

    public static final int DEFAULT_AUTH_REQUEST_TIMEOUT = 15 * 60;
    public static final int DEFAULT_AUTH_REQUEST_POOL = 100;
    public static final int DEFAULT_COMMANDS_QUEUE_SIZE = 50;
    public static final int DEFAULT_SPAMMER_COMMANDS_COUNT = 5;
    public static final int DEFAULT_HTTP_PORT = 6980;
    public static final String DEFAULT_RESOURCE_ROOT = "../res";
    public static final String DEFAULT_SQL_DRIVER = "org.sqlite.JDBC";
    public static final String DEFAULT_SQL_PASSWORD = "";
    public static final String DEFAULT_SQL_LOGIN = "";
    public static final String DEFAULT_SQL_URL = "jdbc:sqlite:Stigma.db";
    public static final int DEFAULT_RECONNECT_GRACE_PERIOD = 3;
    public static final int DEFAULT_WORLD_SAVE_INTERVAL = 15;
    public static final int DEFAULT_RESOURCE_CACHE_USE_LIMIT = -1;
    public static final int DEFAULT_RESOURCE_CACHE_VALIDATION_PERIOD = -1;
    public static final boolean DEFAULT_RESOURCE_HTTP_SERVER_ON = false;
    public static final int DEFAULT_MAX_MAP_INSTANCES = 20000;

    private static ServerConfig instance;

    private String sqlUrl;
    private String sqlLogin;
    private String sqlPassword;
    private String sqlDriver;
    private String resourceRoot;
    private int serverPort;
    private int authRequestTimeout;
    private int authRequestPool;
    private int commandQueueSize;
    private int spammerCommandsCount;
    private int httpPort;
    private int reconnectGracePeriod;
    private int worldSaveInterval;
    private int resourceCacheUseLimit;
    private int resourceCacheValidationPeriod;
    private boolean resourceHttpServerOn;
    private int maxMapInstances;

    /**
     * Constructor. Sets global instance. May be called only
     * once. Loads also resource set properties.
     * @param configFile
     *            configuration file name, from which
     *            configuration should be read
     */
    public ServerConfig(String configFile)
    {
        super(false, true);

        assert instance == null;
        instance = this;
        Properties prop = new Properties();
        try
        {
            InputStream in = new FileInputStream(configFile);
            prop.load(in);
            in.close();
        }
        catch (FileNotFoundException e)
        {
            Log.logger.fatal("Server configuration file not found");
            if (storeDefaultConfig(configFile))
                Log.logger.info("Configuration file with default values created, you should verify options");
            System.exit(-1);
        }
        catch (IOException e)
        {
            Log.logger.fatal("Server configuration file IO error: "
                + e.getMessage());
            System.exit(-1);
        }

        super.load(prop);

        sqlUrl = prop.getProperty(SQL_URL, DEFAULT_SQL_URL);
        sqlLogin = prop.getProperty(SQL_LOGIN, DEFAULT_SQL_LOGIN);
        sqlPassword = prop.getProperty(SQL_PASSWORD, DEFAULT_SQL_PASSWORD);
        sqlDriver = prop.getProperty(SQL_DRIVER, DEFAULT_SQL_DRIVER);
        resourceRoot = prop.getProperty(RESOURCE_ROOT, DEFAULT_RESOURCE_ROOT);
        serverPort = prop.getProperty(SERVER_PORT, DEFAULT_SERVER_PORT);
        authRequestTimeout =
                prop.getProperty(AUTH_REQUEST_TIMEOUT,
                                 DEFAULT_AUTH_REQUEST_TIMEOUT);
        authRequestPool =
                prop.getProperty(AUTH_REQUEST_POOL, DEFAULT_AUTH_REQUEST_POOL);
        commandQueueSize =
                prop.getProperty(COMMAND_QUEUE_SIZE,
                                 DEFAULT_COMMANDS_QUEUE_SIZE);
        spammerCommandsCount =
                prop.getProperty(SPAMMER_COMMANDS_COUNT,
                                 DEFAULT_SPAMMER_COMMANDS_COUNT);
        httpPort = prop.getProperty(HTTP_PORT, DEFAULT_HTTP_PORT);
        reconnectGracePeriod =
                prop.getProperty(RECONNECT_GRACE_PERIOD,
                                 DEFAULT_RECONNECT_GRACE_PERIOD);
        worldSaveInterval =
                prop.getProperty(WORLD_SAVE_INTERVAL,
                                 DEFAULT_WORLD_SAVE_INTERVAL);
        resourceCacheUseLimit =
                prop.getProperty(RESOURCE_CACHE_USE_LIMIT,
                                 DEFAULT_RESOURCE_CACHE_USE_LIMIT);
        resourceCacheUseLimit =
                prop.getProperty(RESOURCE_CACHE_VALIDATION_PERIOD,
                                 DEFAULT_RESOURCE_CACHE_VALIDATION_PERIOD);
        resourceHttpServerOn =
                prop.getProperty(RESOURCE_HTTP_SERVER_ON,
                                 DEFAULT_RESOURCE_HTTP_SERVER_ON);

        maxMapInstances =
                prop.getProperty(MAX_MAP_INSTANCES, DEFAULT_MAX_MAP_INSTANCES);

        setResourceSetProperties(new ResourceSetProperties(getResourceUri().getPath()
            + File.separator + RESOURCE_SET_FILE));
    }

    /**
     * Return global instance.
     * @return global instance
     */
    public static ServerConfig globalInstance()
    {
        assert instance != null;
        return instance;
    }

    /**
     * Stores default properties values in file of given
     * name.
     * @param fileName
     *            file in which default properties should be
     *            saved
     * @return {@code true} when file was created
     *         successfully
     */
    public static boolean storeDefaultConfig(String fileName)
    {
        Properties prop = new Properties();
        prop.setProperty(SQL_URL, DEFAULT_SQL_URL);
        prop.setProperty(SQL_LOGIN, DEFAULT_SQL_LOGIN);
        prop.setProperty(SQL_PASSWORD, DEFAULT_SQL_PASSWORD);
        prop.setProperty(SQL_DRIVER, DEFAULT_SQL_DRIVER);
        prop.setProperty(RESOURCE_ROOT, DEFAULT_RESOURCE_ROOT);
        prop.setProperty(CLIENT_RESOURCE_ROOT, DEFAULT_CLIENT_RESOURCE_ROOT);
        prop.setProperty(CLIENT_RESOURCE_COMPRESSION,
                         DEFAULT_CLIENT_RESOURCE_COMPRESSION);
        prop.setProperty(RESOURCE_SET, DEFAULT_RESOURCE_SET);
        prop.setProperty(SERVER_PORT, DEFAULT_SERVER_PORT);
        prop.setProperty(NETWORK_TIMEOUT, DEFAULT_NETWORK_TIMEOUT);
        prop.setProperty(SPAMMER_INTERVAL, DEFAULT_SPAMMER_INTERVAL);
        prop.setProperty(AUTH_REQUEST_TIMEOUT, DEFAULT_AUTH_REQUEST_TIMEOUT);
        prop.setProperty(AUTH_REQUEST_POOL, DEFAULT_AUTH_REQUEST_POOL);
        prop.setProperty(COMMAND_QUEUE_SIZE, DEFAULT_COMMANDS_QUEUE_SIZE);
        prop.setProperty(SPAMMER_COMMANDS_COUNT, DEFAULT_SPAMMER_COMMANDS_COUNT);
        prop.setProperty(MILLISECONDS_PER_TURN, DEFAULT_MILLISECONDS_PER_TURN);
        prop.setProperty(HTTP_PORT, DEFAULT_HTTP_PORT);
        prop.setProperty(RECONNECT_GRACE_PERIOD, DEFAULT_RECONNECT_GRACE_PERIOD);
        prop.setProperty(WORLD_SAVE_INTERVAL, DEFAULT_WORLD_SAVE_INTERVAL);
        prop.setProperty(RESOURCE_CACHE_USE_LIMIT,
                         DEFAULT_RESOURCE_CACHE_USE_LIMIT);
        prop.setProperty(RESOURCE_CACHE_VALIDATION_PERIOD,
                         DEFAULT_RESOURCE_CACHE_VALIDATION_PERIOD);
        prop.setProperty(RESOURCE_HTTP_SERVER_ON,
                         DEFAULT_RESOURCE_HTTP_SERVER_ON);
        prop.setProperty(MAX_PACKET_SIZE, DEFAULT_MAX_PACKET_SIZE);
        prop.setProperty(MAX_MAP_INSTANCES, DEFAULT_MAX_MAP_INSTANCES);

        try
        {
            OutputStream out = new FileOutputStream(fileName);
            prop.store(out,
                       "This is generated file with default values, you can change it as you wish.");
            out.close();
        }
        catch (FileNotFoundException e)
        {
            Log.logger.info("File not found");
            return false;
        }
        catch (IOException e)
        {
            Log.logger.info("File IO error: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Returns amount of concurrent unauthorized connection
     * that can be made to server. If this limit is reached,
     * new connections will be refused unless oldest
     * connections did 'time-out'. Default:
     * {@value #DEFAULT_AUTH_REQUEST_POOL}. Stored in file
     * as {@value #AUTH_REQUEST_POOL}
     * @return authRequestPool
     */
    public final int getAuthRequestPool()
    {
        return authRequestPool;
    }

    /**
     * Returns time in seconds in which player should log in
     * and choose actor or else will be considered
     * 'timed-out' and may be removed if 'authorization
     * request poll' will be full. Default:
     * {@value #DEFAULT_AUTH_REQUEST_TIMEOUT}. Stored in
     * file as {@value #AUTH_REQUEST_TIMEOUT}
     * @return authRequestTimeout
     */
    public final int getAuthRequestTimeout()
    {
        return authRequestTimeout;
    }

    /**
     * Returns actor's command queue size. Default:
     * {@value #DEFAULT_COMMANDS_QUEUE_SIZE}. Stored in file
     * as {@code #COMMAND_QUEUE_SIZE}.
     * @return actor's command queue size
     */
    public final int getCommandQueueSize()
    {
        return commandQueueSize;
    }

    /**
     * Returns port number to use for HTTP interface. If
     * equals zero HTTP interface will not be created.
     * Default: {@value #DEFAULT_HTTP_PORT}. Stored in file
     * as {@value #HTTP_PORT}.
     * @return port number for HTTP interface
     */
    public final int getHttpPort()
    {
        return httpPort;
    }

    /**
     * Returns grace period (in minutes) for which
     * disconnected player actors will be cached and ready
     * for reconnection. Default:
     * {@value #DEFAULT_RECONNECT_GRACE_PERIOD}. Stored in
     * file as {@value #RECONNECT_GRACE_PERIOD}.
     * @return grace period for caching disconnected player
     *         actors
     */
    public final int getReconnectGracePeriod()
    {
        return reconnectGracePeriod;
    }

    /**
     * Returns limit of resource cache uses. If resource
     * cache item is used less times than stated in this
     * field during resource cleaning period, item is
     * removed from cache. For limit lower than 0 items are
     * never removed from cache. Default:
     * {@value #DEFAULT_RESOURCE_CACHE_USE_LIMIT}. Stored in
     * file as {@value #RESOURCE_CACHE_USE_LIMIT}
     * @return resourceCacheUseLimit
     */
    public final int getResourceCacheUseLimit()
    {
        return resourceCacheUseLimit;
    }

    /**
     * Returns int representing time number of milliseconds
     * after which cache resource cache will be cleaned. If
     * number is lower than 0 cache is never cleaned.
     * Default:
     * {@value #DEFAULT_RESOURCE_CACHE_VALIDATION_PERIOD}.
     * Stored in file as
     * {@value #RESOURCE_CACHE_VALIDATION_PERIOD}
     * @return resourceCacheValidationPeriod
     */
    public final int getResourceCacheValidationPeriod()
    {
        return resourceCacheValidationPeriod;
    }

    /**
     * Returns root directory for all file configuration
     * sets. Default: {@value #DEFAULT_RESOURCE_ROOT}.
     * Stored in file as {@value #RESOURCE_ROOT}.
     * @return root directory for all file configuration
     *         sets
     */
    public final String getResourceRoot()
    {
        return resourceRoot;
    }

    /**
     * Returns URI (used for example by {@link MapDB}). It
     * is equivalent to {@code getResourceRoot() + "/" +
     * getResourceSet()}.
     * @return URI used by {@link MapDB} (for example).
     */
    public final URI getResourceUri()
    {
        return new File(getResourceRoot() + File.separator + getResourceSet()).toURI();
    }

    /**
     * Returns server port number. Default:
     * {@value pl.org.minions.stigma.globals.GlobalConfig#DEFAULT_SERVER_PORT}
     * . Stored in file as {@value #SERVER_PORT}
     * @return server port number
     */
    public final int getServerPort()
    {
        return serverPort;
    }

    /**
     * Returns maximum amount of commands that didn't fit in
     * queue before player is treated as spammer. Default:
     * {@value #DEFAULT_SPAMMER_COMMANDS_COUNT}. Stored in
     * file as {@value #SPAMMER_COMMANDS_COUNT}.
     * @return maximum amount of commands that doen't fit in
     *         queue
     */
    public final int getSpammerCommandsCount()
    {
        return spammerCommandsCount;
    }

    /**
     * Returns driver name for accessing SQL database.
     * Default: {@value #DEFAULT_SQL_DRIVER}. Stored in file
     * as {@value #SQL_DRIVER}.
     * @return driver name for accessing SQL database
     */
    public final String getSqlDriver()
    {
        return sqlDriver;
    }

    /**
     * Returns user name for login to SQL database. Default:
     * {@value #DEFAULT_SQL_LOGIN}. Stored in file as
     * {@value #SQL_LOGIN}.
     * @return user name for login to SQL database
     */
    public final String getSqlLogin()
    {
        return sqlLogin;
    }

    /**
     * Returns password for login to SQL database. Default:
     * {@value #DEFAULT_SQL_PASSWORD}. Stored in file as
     * {@value #SQL_PASSWORD}.
     * @return password for login to SQL database
     */
    public final String getSqlPassword()
    {
        return sqlPassword;
    }

    /**
     * Returns URL to SQL database. Default:
     * {@value #DEFAULT_SQL_URL}. Stored in file as
     * {@value #SQL_URL}
     * @return URL to SQL database
     */
    public final String getSqlUrl()
    {
        return sqlUrl;
    }

    /**
     * Returns interval between subsequent 'world saves'.
     * Should be multiplication of
     * {@link #getReconnectGracePeriod()}. Default:
     * {@value #DEFAULT_WORLD_SAVE_INTERVAL}. Stored in file
     * as {@value #WORLD_SAVE_INTERVAL}.
     * @return interval between 'world saves'.
     */
    public final int getWorldSaveInterval()
    {
        return worldSaveInterval;
    }

    /**
     * Flag used to configure whether resources are
     * available via HTTP. This flag works only when HTTP
     * server port is greater than 0. Default:
     * {@value #DEFAULT_RESOURCE_HTTP_SERVER_ON}. Stored in
     * file as {@value #RESOURCE_HTTP_SERVER_ON}
     * @return resourceHttpServerOn
     */
    public final boolean isResourceHttpServerOn()
    {
        return resourceHttpServerOn;
    }

    /**
     * Returns maximum amount of map instances server should
     * support. Default {@value #DEFAULT_MAX_MAP_INSTANCES}.
     * Stored in file as {@value #MAX_MAP_INSTANCES}.
     * @return maximum amount of map instances server should
     *         support
     */
    public final int getMaxMapInstances()
    {
        return maxMapInstances;
    }

}
