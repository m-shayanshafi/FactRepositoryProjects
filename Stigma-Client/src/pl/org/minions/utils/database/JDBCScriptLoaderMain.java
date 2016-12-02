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
import java.io.IOException;

import pl.org.minions.utils.Version;
import pl.org.minions.utils.logger.Log;

/**
 * Main script generator class.
 */
public final class JDBCScriptLoaderMain
{
    private JDBCScriptLoaderMain()
    {
    }

    /**
     * Starting method.
     * @param args
     *            arguments are not applicable
     */
    public static void main(String[] args)
    {
        Version.initialize("Stigma-Script-Loader");
        Log.initialize("log_config.properties");

        JDBCScriptLoaderConfig loaderConfig = null;
        JDBCScriptLoader loader = null;

        Log.logger.info("Started Script Loader");

        if (args == null || args.length == 0)
        {
            Log.logger.info("No arguments were found. Trying to open default properties file: "
                + JDBCScriptLoaderConfig.DEFAULT_CONFIGURATION_FILE);

            try
            {
                loaderConfig = new JDBCScriptLoaderConfig();
            }
            catch (IOException e)
            {
                Log.logger.fatal("Error while reading default configuration file: "
                                     + JDBCScriptLoaderConfig.DEFAULT_CONFIGURATION_FILE,
                                 e);
            }
        }
        else
        {
            Log.logger.info("Arguments were found. Trying to open file: "
                + args[0]);

            try
            {
                loaderConfig = new JDBCScriptLoaderConfig(new File(args[0]));
            }
            catch (IOException e)
            {
                Log.logger.fatal("Error while reading configuration file: "
                    + JDBCScriptLoaderConfig.DEFAULT_CONFIGURATION_FILE, e);
            }
        }

        Log.logger.info("Configuration loaded successfully.");
        loader = new JDBCScriptLoader(loaderConfig);

        Log.logger.info("Starting loading scripts...");
        loader.loadScripts();
        Log.logger.info("Scripts loaded successfully.");
    }
}
