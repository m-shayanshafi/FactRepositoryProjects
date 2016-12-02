/*
 * TrickTakingGame - Trick-taking games platform on-line
 * Copyright (C) 2008  Sébastien Doeraene
 * All Rights Reserved
 *
 * This file is part of TrickTakingGame.
 *
 * TrickTakingGame is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * TrickTakingGame is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * TrickTakingGame.  If not, see <http://www.gnu.org/licenses/>.
 */
package sjrd.tricktakinggame.server;

import java.util.logging.*;
import java.io.*;

import sjrd.util.appproperties.*;
import sjrd.tricktakinggame.network.server.*;
import sjrd.tricktakinggame.rules.*;

import static sjrd.util.HashUtils.*;
import static sjrd.tricktakinggame.server.AppProperty.*;

/**
 * Serveur de jeu à plis
 * @author sjrd
 */
public class TrickTakingGameServer
{
	/**
	 * Méthode principale
	 * @param args Paramètres en ligne de commande
	 */
	public static void main(String[] args) throws IOException
	{
		// Chemin du fichier de configuration
		
        String confFileName;
		if (args.length >= 1)
			confFileName = args[0];
        else
            confFileName = ServerAppProperties.defaultConfFileName;
		
        // Chargement du fichier de configuration
        
        ServerAppProperties appProperties = new ServerAppProperties();
        appProperties.load(new FileInputStream(confFileName));
        
		try
		{
			// Logging

			Handler consoleHandler = new ConsoleHandler();
			consoleHandler.setLevel(appProperties.getLevelProperty(
				ConsoleLogLevel));
			Server.addLogHandler(consoleHandler);
			
			if (appProperties.isPropertySet(LogFileName))
			{
				String fileName = appProperties.getProperty(LogFileName);
				int limit = appProperties.getIntegerProperty(LogFileLimit);
				int count = appProperties.getIntegerProperty(LogFileCount);
				boolean append = appProperties.getBooleanProperty(
					LogFileAppend);
				
				Handler fileHandler = new FileHandler(fileName, limit, count,
					append);
				fileHandler.setLevel(appProperties.getLevelProperty(
					LogFileLevel));
				fileHandler.setFormatter(new SimpleFormatter());
				Server.addLogHandler(fileHandler);
			}
			
			// Login checker
			
			LoginChecker loginChecker = null;
			
			if (appProperties.getProperty(LoginChecker).equals("CSV"))
			{
				String fileName = appProperties.getProperty(LoginFileName);
				boolean reloadOnChange =
					appProperties.getBooleanProperty(LoginFileReloadOnChange);
				
				loginChecker = new CSVFileLoginChecker(new File(fileName),
					reloadOnChange);
			}
            
            // Admin checker
            
            AdminChecker adminChecker = null;
            
            if (appProperties.isPropertySet(AdminPasswordHash))
            {
                final String adminPasswordHash =
                    appProperties.getProperty(AdminPasswordHash);
                
                adminChecker = new AdminChecker()
                {
                    public boolean checkPassword(String passwordHash)
                    {
                        return md5String(passwordHash).equals(
                            adminPasswordHash);
                    }
                };
            }

			// Load rules providers

			File directory = new File(appProperties.getProperty(
                RulesDirectory));
			RulesProvider[] providers =
				RulesProvidersLoader.loadRulesProviers(directory);
		
			// Server execution

			Server server = new Server();
			server.setLoginChecker(loginChecker);
            server.setAdminChecker(adminChecker);

			for (RulesProvider provider: providers)
				server.addRulesProvider(provider);
		}
		catch (PropertyException error)
		{
			System.err.println(error.getMessage());
			return;
		}
	}
}
