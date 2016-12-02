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

import java.util.*;
import java.io.*;

import sjrd.tricktakinggame.network.server.*;

import static sjrd.util.HashUtils.*;

/**
 * @author sjrd
 */
public class CSVFileLoginChecker implements LoginChecker
{
	/**
	 * Fichier CSV
	 */
	private File file;
	
	/**
	 * Dernière date de modif du fichier chargé
	 */
	private long lastModified;
	
	/**
	 * Indique s'il faut recharger le fichier quand sa date de modif change
	 */
	private boolean reloadOnChange;
	
	/**
	 * Map des login et de leurs mots de passe hashés
	 */
	private Map<String, String> loginMap;
	
	/**
	 * Crée le vérificateur de login
	 * @param aFile Fichier CSV à lire
	 * @param aReloadOnChange <tt>true</tt> pour recharger automatiquement
	 * @throws IOException Erreur I/O à la lecture du fichier
	 */
	public CSVFileLoginChecker(File aFile, boolean aReloadOnChange)
		throws IOException
	{
		super();
		
		file = aFile;
		reloadOnChange = aReloadOnChange;
		
		load();
	}
	
	/**
	 * Charge le fichier
	 * @throws IOException Erreur au chargement du fichier
	 */
	private void load() throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		Map<String, String> newMap = new HashMap<String, String>();
		
		String line;
		while ((line = reader.readLine()) != null)
		{
			line = line.trim();
			if (line.length() == 0)
				continue;
			
			StringTokenizer tokenizer = new StringTokenizer(line, ";");
			String loginName = tokenizer.nextToken();
			String passwordHash = tokenizer.nextToken();
			
			newMap.put(loginName, passwordHash);
		}
		
		loginMap = newMap;
		lastModified = file.lastModified();
	}
	
	/**
	 * Recharge si nécessaire
	 */
	private void checkReload()
	{
		if (!reloadOnChange)
			return;
		if (lastModified >= file.lastModified())
			return;
		
		try
		{
			load();
		}
		catch (IOException ignore)
		{
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public LoggedClient checkLogin(String loginName, String passwordHash)
	{
		checkReload();
		
		if (!loginMap.get(loginName).equals(md5String(passwordHash)))
			return null;
		else
			return new DefaultLoggedClient(loginName);
	}
}
