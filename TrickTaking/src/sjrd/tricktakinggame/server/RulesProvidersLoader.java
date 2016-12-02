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
import java.util.jar.*;
import java.io.*;
import java.net.*;

import sjrd.tricktakinggame.rules.*;

import static sjrd.util.ArrayUtils.*;

/**
 * Loader pour les fournisseurs de règles en plug-in
 * @author sjrd
 */
public class RulesProvidersLoader
{
	/**
	 * Charge tous les fournisseurs de règles dans un dossier
	 * @param directory Dossier des plug-in
	 * @return Tableau des fournisseurs de règles chargés
	 */
	public static RulesProvider[] loadRulesProviers(File directory)
	{
		assert directory.isDirectory();
		
		// Lister les fichiers "*.jar"
		File[] files = directory.listFiles(new FilenameFilter()
		{
			public boolean accept(File dir, String name)
			{
				return (name.length() > 4) &&
					(name.substring(name.length()-4).equalsIgnoreCase(".jar"));
			}
		});
		
		// Charger les fournisseurs de règles de chaque jar
		RulesProvider[][] result = new RulesProvider[files.length][];
		for (int i = 0; i < files.length; i++)
		{
			try
			{
				result[i] = loadJarFile(files[i]);
			}
			catch (IOException error)
			{
				result[i] = new RulesProvider[0];
			}
		}
		
		// Renvoyer l'ensemble des fournisseurs de règles
		return concatArrays(RulesProvider.class, result);
	}
	
	/**
	 * Charge les fournisseurs de règles contenus dans un fichier jar
	 * @param file Fichier jar
	 * @return Tableau des fournisseurs de règles chargés
	 * @throws IOException Erreur I/O au chargement du jar
	 */
	private static RulesProvider[] loadJarFile(File file)
		throws IOException
	{
		// Créer l'URLClassLoader
		URLClassLoader loader;
		try
		{
			loader = new URLClassLoader(new URL[] {file.toURI().toURL()});
		}
		catch (MalformedURLException error)
		{
			error.printStackTrace();
			return new RulesProvider[0];
		}
		
		// Créer le JarFile et récupérer ses entrées
		JarFile jarFile = new JarFile(file);
		Enumeration<JarEntry> entries = jarFile.entries();
		
		List<RulesProvider> result = new LinkedList<RulesProvider>();
		
		// Boucle sur toutes les entrées du jar
		while (entries.hasMoreElements())
		{
			JarEntry entry = entries.nextElement();
			String name = entry.toString();
			
			// Ne pas traiter les fichiers qui ne sont pas des *.class
			if (name.length() <= 6)
				continue;
			if (!name.substring(name.length()-6).equalsIgnoreCase(".class"))
				continue;
			
			// Déterminer le nom de la classe
			String className = name.substring(0, name.length()-6);
			className = className.replace('/', '.');
			
			try
			{
				/*
				 * Si la classe implémente RulesProvider, on en crée une
				 * instance, et on rajoute celle-ci à la liste des fournisseurs
				 * qui seront renvoyés
				 */
				Class clazz = Class.forName(className, true, loader);
				for (Class intf: clazz.getInterfaces())
				{
					if (intf.equals(RulesProvider.class))
					{
						result.add((RulesProvider) clazz.newInstance());
						break;
					}
				}
			}
			catch (InstantiationException error)
			{
				error.printStackTrace();
			}
			catch (IllegalAccessException error)
			{
				error.printStackTrace();
			}
			catch (ClassNotFoundException error)
			{
				error.printStackTrace();
			}
		}
		
		return result.toArray(new RulesProvider[result.size()]);
	}
}
