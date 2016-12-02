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
package pl.org.minions.utils.database;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Configuration loader and keeper for JDBC Script Loader
 * application.
 */
public class JDBCScriptLoaderConfig
{
    public static final String DEFAULT_CONFIGURATION_FILE =
            "script_loader.properties";

    private static final String DB_DRIVER_KEY = "db-driver";
    private static final String DB_URL = "db-url";
    private static final String DB_USER = "db-user";
    private static final String DB_PASSWORD = "db-password";

    private static final String PRE_CREATE_SCRIPT_URL = "pre-create-script-url";
    private static final String CREATE_SCRIPT_URL = "create-script-url";
    private static final String POST_CREATE_SCRIPT_URL =
            "post-create-script-url";

    private static final String PRE_LOAD_SCRIPT_URL = "pre-load-script-url";
    private static final String LOAD_SCRIPT_URL = "load-script-url";
    private static final String POST_LOAD_SCRIPT_URL = "post-load-script-url";

    private Map<String, Map<String, String>> databaseProperties;

    /**
     * Default constructor.
     * @throws IOException
     *             error while reading file
     */
    public JDBCScriptLoaderConfig() throws IOException
    {
        this(new File(DEFAULT_CONFIGURATION_FILE));
    }

    /**
     * Constructor with properties file argument.
     * @param propertiesFile
     *            properties file
     * @throws IOException
     *             error while reading file
     */
    public JDBCScriptLoaderConfig(File propertiesFile) throws IOException
    {
        this.databaseProperties = new HashMap<String, Map<String, String>>();

        Properties properties = new Properties();

        properties.load(new FileReader(propertiesFile));

        for (Object o : properties.keySet())
        {
            String key = o.toString();

            Map<String, String> oneBaseProps =
                    this.databaseProperties.get(key.substring(0,
                                                              key.indexOf('.')));
            if (oneBaseProps == null)
            {
                oneBaseProps = new HashMap<String, String>();
                databaseProperties.put(key.substring(0, key.indexOf('.')),
                                       oneBaseProps);
            }

            oneBaseProps.put(key.substring(key.indexOf('.') + 1),
                             properties.get(o).toString());
        }
    }

    /**
     * Returns driver for selected database.
     * @param database
     *            database which driver will be returned
     * @return String representing database driver
     */
    public String getDBDriver(String database)
    {
        return databaseProperties.get(database).get(DB_DRIVER_KEY);
    }

    /**
     * Returns URL for selected database.
     * @param database
     *            database URL
     * @return String representing database URL
     */
    public String getDBURL(String database)
    {
        return databaseProperties.get(database).get(DB_URL);
    }

    /**
     * Returns URL to file containing script executed before
     * main create script.
     * @param database
     *            database script concerns
     * @return URL to file containing script executed before
     *         main create script
     */
    public String getPreCreateScriptURL(String database)
    {
        return databaseProperties.get(database).get(PRE_CREATE_SCRIPT_URL);
    }

    /**
     * Returns URL to file containing main create script.
     * @param database
     *            database script concerns
     * @return URL to file containing main create script
     */
    public String getCreateScriptURL(String database)
    {
        return databaseProperties.get(database).get(CREATE_SCRIPT_URL);
    }

    /**
     * Returns URL to file containing script executed after
     * main create script.
     * @param database
     *            database script concerns
     * @return URL to file containing script executed after
     *         main create script
     */
    public String getPostCreateScriptURL(String database)
    {
        return databaseProperties.get(database).get(POST_CREATE_SCRIPT_URL);
    }

    /**
     * Returns URL to file containing script executed before
     * main load script.
     * @param database
     *            database script concerns
     * @return URL to file containing script executed before
     *         main load script
     */
    public String getPreLoadScriptURL(String database)
    {
        return databaseProperties.get(database).get(PRE_LOAD_SCRIPT_URL);
    }

    /**
     * Returns URL to file containing main load script.
     * @param database
     *            database script concerns
     * @return URL to file containing main load script
     */
    public String getLoadScriptURL(String database)
    {
        return databaseProperties.get(database).get(LOAD_SCRIPT_URL);
    }

    /**
     * Returns URL to file containing script executed after
     * main load script.
     * @param database
     *            database script concerns
     * @return URL to file containing script executed after
     *         main load script
     */
    public String getPostLoadScriptURL(String database)
    {
        return databaseProperties.get(database).get(POST_LOAD_SCRIPT_URL);
    }

    /**
     * Returns user name with which database connection will
     * be established.
     * @param database
     *            database to which database connection will
     *            be established.
     * @return user name
     */
    public String getDBUser(String database)
    {
        return databaseProperties.get(database).get(DB_USER);
    }

    /**
     * Returns password to database for selected user.
     * @param database
     *            database to which password will be
     *            returned
     * @return password
     */
    public String getDBPassword(String database)
    {
        return databaseProperties.get(database).get(DB_PASSWORD);
    }

    /**
     * Lists databases which are configured for
     * ScriptLoader.
     * @return collection containing database names
     */
    public Collection<String> getDatabasesList()
    {
        return databaseProperties.keySet();
    }
}
