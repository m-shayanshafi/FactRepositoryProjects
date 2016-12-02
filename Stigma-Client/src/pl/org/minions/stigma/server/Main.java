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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipException;

import pl.org.minions.stigma.globals.ResourceSetProperties;
import pl.org.minions.stigma.license.LicenseInfo;
import pl.org.minions.stigma.network.Cipher;
import pl.org.minions.stigma.server.http.ServerHttpInterface;
import pl.org.minions.utils.Version;
import pl.org.minions.utils.collections.CollectionFactory;
import pl.org.minions.utils.collections.CollectionFactory.CollectionType;
import pl.org.minions.utils.i18n.AllClassesLoader;
import pl.org.minions.utils.i18n.Translator;
import pl.org.minions.utils.i18n.TranslatorException;
import pl.org.minions.utils.logger.Log;

/**
 * Main server class when running as stand-alone application
 * - starts server instance and administration modules.
 */
public final class Main
{
    private Main()
    {
    }

    /**
     * Main procedure of server application.
     * @param args
     *            currently ignored
     */
    public static void main(String[] args)
    {
        Version.initialize("Stigma-Server");

        Log.initialize("log_config.properties");
        try
        {
            AllClassesLoader.standardLoadAll();
        }
        catch (ZipException e)
        {
            Log.logger.fatal(e);
            System.exit(1);
        }
        catch (SecurityException e)
        {
            Log.logger.fatal(e);
            System.exit(1);
        }
        catch (TranslatorException e)
        {
            Log.logger.fatal(e);
            System.exit(1);
        }
        catch (IOException e)
        {
            Log.logger.fatal(e);
            System.exit(1);
        }

        if (args.length > 0)
        {
            try
            {
                Translator.processArgument(Version.getAppName(), args[0]);
            }
            catch (FileNotFoundException e)
            {
                Log.logger.fatal(e);
                System.exit(1);
            }
            catch (IOException e)
            {
                Log.logger.fatal(e);
                System.exit(1);
            }
        }

        Translator.globalInstance().translate();

        System.out.println(LicenseInfo.getLicenseText(LicenseInfo.Format.TEXT));

        CollectionFactory.init(CollectionType.Normal);

        ServerConfig config = new ServerConfig("./server_config.properties");
        ServerTray.create();

        ResourceSetProperties resProp = config.getResourceSetProperties();
        if (!resProp.isValidated())
        {
            Log.logger.fatal("Not validated resource set");
            System.exit(1);
        }

        Log.logger.info("Loaded resource set: '" + resProp.getName() + "' ("
            + config.getResourceSet() + ") last modified: "
            + resProp.getModifiedDate());

        Cipher.initServer();
        ServerHttpInterface.globalInstance().start();

        Server.globalInstance().start();
    }
}
