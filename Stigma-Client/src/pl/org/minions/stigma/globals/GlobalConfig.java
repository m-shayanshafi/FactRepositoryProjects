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
package pl.org.minions.stigma.globals;

import java.net.URI;
import java.net.URISyntaxException;

import pl.org.minions.utils.Properties;
import pl.org.minions.utils.logger.Log;

/**
 * Class representing configuration which is global for both
 * client and server.
 */
public class GlobalConfig
{
    public static final String RESOURCE_SET_FILE = "resourceset.stigmares";

    public static final String SPAMMER_INTERVAL = "spammer-interval";
    public static final String NETWORK_TIMEOUT = "network-timeout";
    public static final String MILLISECONDS_PER_TURN = "milliseconds-per-turn";
    public static final String RESOURCE_SET = "resource-set";
    public static final String CLIENT_RESOURCE_ROOT = "client-resource-root";
    public static final String MAX_PACKET_SIZE = "max-packet-size";

    public static final int DEFAULT_SERVER_PORT = 6969;
    public static final short DEFAULT_SPAMMER_INTERVAL = 100;
    public static final short DEFAULT_NETWORK_TIMEOUT = 20 * 1000; // 20s 
    public static final int DEFAULT_MILLISECONDS_PER_TURN = 100;
    public static final String DEFAULT_RESOURCE_SET = "default";
    public static final String DEFAULT_CLIENT_RESOURCE_ROOT =
            "http://some.stigma.server.com";
    public static final String CLIENT_RESOURCE_COMPRESSION =
            "client-resource-compression";
    public static final boolean DEFAULT_CLIENT_RESOURCE_COMPRESSION = true;
    public static final short DEFAULT_MAX_PACKET_SIZE = 4 * 1024; // 4kB

    private static GlobalConfig instance;

    private ResourceSetProperties resourceSetProperties;
    private String clientResourceRoot;
    private String resourceSet;
    private int spammerInterval;
    private int millisecondsPerTurn;
    private int networkTimeout;
    private boolean resourceCompression;
    private boolean clientResourceCompression;
    private short maxPacketSize;

    /**
     * Constructor for descendant classes.
     * @param resourceCompression
     *            sets "resourceCopression" parameter
     * @param setGlobalInstance
     *            whether or not this constructor should set
     *            global instance
     */
    protected GlobalConfig(boolean resourceCompression,
                           boolean setGlobalInstance)
    {
        this.resourceCompression = resourceCompression;
        if (setGlobalInstance)
            instance = this;
    }

    /**
     * Constructor. Should be used on client mostly.
     * @param properties
     *            see {@link #getResourceSetProperties()}
     * @param clientResourceRoot
     *            see {@link #getClientResourceRoot()}
     * @param resourceSet
     *            see {@link #getResourceSet()}
     * @param networkTimeout
     *            see {@link #getNetworkTimeout()}
     * @param millisecondsPerTurn
     *            see {@link #getMillisecondsPerTurn()}
     * @param resourceCompression
     *            see {@link #isResourceCompression()}
     */
    public GlobalConfig(ResourceSetProperties properties,
                        String clientResourceRoot,
                        String resourceSet,
                        int networkTimeout,
                        int millisecondsPerTurn,
                        boolean resourceCompression)
    {
        this(resourceCompression, true);
        this.clientResourceRoot = clientResourceRoot;
        this.resourceSet = resourceSet;
        this.resourceSetProperties = properties;
        this.networkTimeout = networkTimeout;
        this.millisecondsPerTurn = millisecondsPerTurn;
        this.maxPacketSize = Short.MAX_VALUE;
    }

    /**
     * Returns global instance.
     * @return global instance.
     */
    public static GlobalConfig globalInstance()
    {
        return instance;
    }

    /**
     * Returns if client should use compressed resources.
     * {@value #DEFAULT_CLIENT_RESOURCE_COMPRESSION}. Stored
     * in file as {@value #CLIENT_RESOURCE_COMPRESSION}.
     * @return {@code true} when client should use
     *         compressed resources.
     */
    public boolean getClientResourceCompression()
    {
        return clientResourceCompression;
    }

    /**
     * Returns root directory for all file configuration
     * sets for client. Default:
     * {@value #DEFAULT_CLIENT_RESOURCE_ROOT}. This must be
     * set, should be proper for {@link URI}. Stored in file
     * as {@value #CLIENT_RESOURCE_ROOT}.
     * @return root directory for file DBs for client
     */
    public final String getClientResourceRoot()
    {
        return clientResourceRoot;
    }

    /**
     * Returns URI (used for example by MapDB). It is
     * equivalent to {@code getClientResourceRoot() + "/" +
     * getResourceSet()}.
     * @return URI used by
     *         {@link pl.org.minions.stigma.databases.map.MapDB}
     *         (for example).
     */
    public URI getClientResourceUri()
    {
        try
        {
            return new URI(getClientResourceRoot() + "/" + getResourceSet());
        }
        catch (URISyntaxException e)
        {
            Log.logger.error(e);
            return URI.create("");
        }
    }

    /**
     * Returns time of turn in millisecond. Default
     * {@value #DEFAULT_MILLISECONDS_PER_TURN}. Stored in
     * file as {@value #MILLISECONDS_PER_TURN}.
     * @return time of turn in millisecond
     */
    public final int getMillisecondsPerTurn()
    {
        return millisecondsPerTurn;
    }

    /**
     * Returns timeout of network connection in millisecond.
     * Default {@value #DEFAULT_NETWORK_TIMEOUT}. Stored in
     * file as {@value #NETWORK_TIMEOUT}. Should be value
     * between {@code 0} and
     * {@value java.lang.Short#MAX_VALUE}.
     * @return timeout of network connection in millisecond
     */
    public final int getNetworkTimeout()
    {
        return networkTimeout;
    }

    /**
     * Returns chosen file configuration set. Default:
     * {@value #DEFAULT_RESOURCE_SET}. Stored in file as
     * {@value #RESOURCE_SET}
     * @return root directory for all resource configuration
     *         sets
     */
    public final String getResourceSet()
    {
        return resourceSet;
    }

    /**
     * Returns resource set properties.
     * @return resource set properties
     */
    public final ResourceSetProperties getResourceSetProperties()
    {
        return resourceSetProperties;
    }

    /**
     * Returns average interval between messages considered
     * to be spam. Default
     * {@value #DEFAULT_SPAMMER_INTERVAL}. Stored in file as
     * {@value #SPAMMER_INTERVAL}. Should be value between
     * {@code 0} and {@value java.lang.Short#MAX_VALUE}. On
     * client will always be {@code 0} to indicate
     * "no spammer detection".
     * @return average interval between messages considered
     *         to be spam
     */
    public final int getSpammerInterval()
    {
        return spammerInterval;
    }

    /**
     * Defines if application should use compressed
     * resources. By default it should be {@code false} for
     * server and on client it should depend on server
     * options.
     * @return whether or not application should use
     *         compressed resource files
     */
    public final boolean isResourceCompression()
    {
        return resourceCompression;
    }

    /**
     * Returns maximum packet size allowed to be received
     * from network. Default
     * {@value #DEFAULT_MAX_PACKET_SIZE}. Stored in file as
     * {@value #MAX_PACKET_SIZE}.
     * Should be value between {@code 0} and
     * {@value java.lang.Short#MAX_VALUE}. On client will
     * be always {@value java.lang.Short#MAX_VALUE} as
     * "no DoS detection".
     * @return maximum packet size allowed to be received
     *         from network
     */
    public final short getMaxPacketSize()
    {
        return maxPacketSize;
    }

    /**
     * Loads configuration from properties.
     * @param prop
     *            properties to load configuration from.
     */
    protected void load(Properties prop)
    {
        clientResourceRoot =
                prop.getProperty(CLIENT_RESOURCE_ROOT,
                                 DEFAULT_CLIENT_RESOURCE_ROOT);
        resourceSet = prop.getProperty(RESOURCE_SET, DEFAULT_RESOURCE_SET);

        millisecondsPerTurn =
                prop.getProperty(MILLISECONDS_PER_TURN,
                                 DEFAULT_MILLISECONDS_PER_TURN);

        spammerInterval =
                prop.getProperty(SPAMMER_INTERVAL, DEFAULT_SPAMMER_INTERVAL);

        networkTimeout =
                prop.getProperty(NETWORK_TIMEOUT, DEFAULT_NETWORK_TIMEOUT);

        clientResourceCompression =
                prop.getProperty(CLIENT_RESOURCE_COMPRESSION,
                                 DEFAULT_CLIENT_RESOURCE_COMPRESSION);

        maxPacketSize =
                prop.getProperty(MAX_PACKET_SIZE, DEFAULT_MAX_PACKET_SIZE);
    }

    /**
     * Sets new resource set properties.
     * @param properties
     *            new properties.
     */
    protected final void setResourceSetProperties(ResourceSetProperties properties)
    {
        this.resourceSetProperties = properties;
    }
}
