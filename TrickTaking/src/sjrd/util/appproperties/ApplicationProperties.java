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
package sjrd.util.appproperties;

import java.util.*;

/**
 * Propriétés de l'application
 * @author sjrd
 */
public class ApplicationProperties extends Properties
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Propriétés disponibles
	 */
	private ApplicationProperty[] availableProperties =
		new ApplicationProperty[0];
	
	/**
	 * Construit les propriétés par défauts
	 * @param availableProperties Propriétés par défaut
	 * @return Propriétés par défaut
	 */
	private static Properties makeDefaultProperties(
		ApplicationProperty[] availableProperties)
	{
		Properties result = new Properties();
		
		for (ApplicationProperty property: availableProperties)
			if (property.getDefaultValue() != null)
				result.setProperty(property.getName(),
					property.getDefaultValue());
		
		return result;
	}
	
	/**
	 * Crée les propriétés
	 * @param defaults Propriétés par défaut
	 */
	public ApplicationProperties(Properties defaults)
	{
		super(defaults);
	}
	
	/**
	 * Crée les propriétés
	 * @param aAvailableProperties Propriétés disponibles
	 */
	public ApplicationProperties(ApplicationProperty[] aAvailableProperties)
	{
		this(makeDefaultProperties(aAvailableProperties));
		
		availableProperties = aAvailableProperties;
	}
	
	/**
	 * Crée les propriétés
	 */
	public ApplicationProperties()
	{
		super();
	}
	
	/**
	 * Teste si une propriété est renseignée
	 * @param property Propriété
	 * @return <tt>true</tt> si la propriété est renseignées, <tt>false</tt>
	 *         sinon
	 */
	public boolean isPropertySet(ApplicationProperty property)
	{
		return getProperty(property.getName()) != null;
	}
	
	/**
	 * Lit la valeur d'une propriété
	 * @param property Propriété
	 * @return Valeur de la propriété
	 * @throws PropertyNotFoundException Propriété non trouvée
	 * @see #setProperty(ApplicationProperty, String)
	 * @see #getProperty(String)
	 */
	public String getProperty(ApplicationProperty property)
		throws PropertyNotFoundException
	{
		String result = getProperty(property.getName());
		
		if (result == null)
			throw new PropertyNotFoundException(String.format(
				"La propriété %s n'a pas pu être trouvée", property.getName()));
		
		return result;
	}
	
	/**
	 * Modifie la valeur d'une propriété
	 * @param property Propriété
	 * @param value Nouvelle valeur de la propriété
	 * @see #getProperty(ApplicationProperty)
	 * @see #setProperty(String, String)
	 */
	public void setProperty(ApplicationProperty property, String value)
	{
		setProperty(property.getName(), value);
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
	public int getIntegerProperty(ApplicationProperty property)
		throws PropertyException
	{
		try
		{
			return Integer.parseInt(getProperty(property));
		}
		catch (NumberFormatException error)
		{
			throw new PropertyFormatException(
				String.format("La propriété %s n'est pas un nombre entier",
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
	public void setIntegerProperty(ApplicationProperty property, int value)
	{
		setProperty(property, String.valueOf(value));
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
	public boolean getBooleanProperty(ApplicationProperty property)
		throws PropertyException
	{
		try
		{
			String value = getProperty(property);
			
			if (value.equalsIgnoreCase("true"))
				return true;
			else if (value.equalsIgnoreCase("false"))
				return false;
			else
				return Integer.parseInt(getProperty(property)) != 0;
		}
		catch (NumberFormatException error)
		{
			throw new PropertyFormatException(
				String.format("La propriété %s n'est pas un booléen",
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
	public void setBooleanProperty(ApplicationProperty property, boolean value)
	{
		setProperty(property, String.valueOf(value));
	}
	
	/**
	 * Parse la ligne de commande et ajoute les paramètres lus
	 * @param args Paramètres en ligne de commande
	 * @param availableProperties Propriétés disponibles
	 */
	public void parseCommandLine(String[] args,
		ApplicationProperty[] availableProperties)
	{
		// TODO A implémenter
	}
	
	/**
	 * Parse la ligne de commande et ajoute les paramètres lus
	 * @param args
	 */
	public void parseCommandLine(String[] args)
	{
		parseCommandLine(args, availableProperties);
	}
}
