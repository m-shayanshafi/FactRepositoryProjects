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
 * Une propriété de l'application
 * @author sjrd
 */
public enum AppProperty implements ApplicationProperty
{
	ConsoleLogLevel("ConsoleLogLevel", Level.OFF),
	
	LogFileName("LogFileName"),
	LogFileLimit("LogFileLimit", 65536),
	LogFileCount("LogFileCount", 1),
	LogFileAppend("LogFileAppend", false),
	LogFileLevel("LogFileLevel", Level.FINE),
    
    RulesDirectory("RulesDirectory", "./rules/"),
	
	LoginChecker("LoginChecker", "None"),
	LoginFileName("LoginFileName"),
	LoginFileReloadOnChange("LoginFileReloadOnChange", false),
    
    AdminPasswordHash("AdminPasswordHash");
	
	/**
	 * Nom
	 */
	private String name;
	
	/**
	 * Valeur par défaut
	 */
	private String defaultValue;
	
	/**
	 * Crée une propriété
	 * @param aName Nom
	 * @param aDefaultValue Valeur par défaut
	 */
	AppProperty(String aName, String aDefaultValue)
	{
		name = aName;
		defaultValue = aDefaultValue;
	}
	
	/**
	 * Crée une propriété
	 * @param aName Nom
	 * @param aDefaultValue Valeur par défaut
	 */
	AppProperty(String aName, int aDefaultValue)
	{
		name = aName;
		defaultValue = String.valueOf(aDefaultValue);
	}
	
	/**
	 * Crée une propriété
	 * @param aName Nom
	 * @param aDefaultValue Valeur par défaut
	 */
	AppProperty(String aName, boolean aDefaultValue)
	{
		name = aName;
		defaultValue = String.valueOf(aDefaultValue);
	}
	
	/**
	 * Crée une propriété
	 * @param aName Nom
	 * @param aDefaultValue Valeur par défaut
	 */
	AppProperty(String aName, Level aDefaultValue)
	{
		name = aName;
		defaultValue = aDefaultValue.getName();
	}
	
	/**
	 * Crée une propriété sans valeur par défaut
	 * @param aName Nom
	 */
	AppProperty(String aName)
	{
		name = aName;
		defaultValue = null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getDefaultValue()
	{
		return defaultValue;
	}
}
