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

import sjrd.util.appproperties.*;

/**
 * Propriétés de l'application
 * @author sjrd
 */
public class ServerAppProperties extends ApplicationProperties
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Nom du fichier de configuration par défaut
	 */
	public static final String defaultConfFileName = "./server.conf";
	
	/**
	 * Crée les propriétés
	 */
	public ServerAppProperties()
	{
		super(AppProperty.values());
	}
	
	/**
	 * Lit la valeur d'une propriété entière
	 * @param property Propriété
	 * @return Valeur de la propriété
	 * @throws PropertyNotFoundException Propriété non trouvée
	 * @throws PropertyFormatException La propriété n'a pas le bon format
	 * @see #setProperty(ApplicationProperty, String)
	 * @see #getProperty(String)
	 */
	public Level getLevelProperty(ApplicationProperty property)
		throws PropertyException
	{
		try
		{
			return Level.parse(getProperty(property));
		}
		catch (IllegalArgumentException error)
		{
			throw new PropertyFormatException(
				String.format("La propriété %s n'est pas un niveau connu",
					property.getName()), error);
		}
	}
	
	/**
	 * Modifie la valeur d'une propriété entière
	 * @param property Propriété
	 * @param value Nouvelle valeur de la propriété
	 * @see #getProperty(ApplicationProperty)
	 * @see #setProperty(String, String)
	 */
	public void setLevelProperty(ApplicationProperty property, Level value)
	{
		setProperty(property, value.getName());
	}
}
