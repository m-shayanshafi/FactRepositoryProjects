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
package sjrd.tricktakinggame.client;

/**
 * Informations sur une table ouverte sur le serveur
 * @author sjrd
 */
public class TableInfo
{
	/**
	 * ID de la table
	 */
	private int id;
	
	/**
	 * Règles associées
	 */
	private RulesInfo rules;
	
	/**
	 * Nom des règles
	 */
	private String rulesName;
	
	/**
	 * Nombre attendu de joueurs
	 */
	private int awaitedPlayerCount;
	
	/**
	 * Nombre de joueurs
	 */
	private int playerCount;
	
	/**
	 * Noms des joueurs
	 */
	private String[] playerNames;
	
	/**
	 * Crée de nouvelles informations sur une table
	 * @param aID ID de la table
	 * @param aRules Règles associées
	 * @param aRulesName Nom des règles associées
	 * @param aAwaitedPlayerCount Nombre attendu de joueurs
	 * @param aPlayerCount Nombre de joueurs
	 * @param aPlayerNames Noms des joueurs
	 */
	private TableInfo(int aID, RulesInfo aRules, String aRulesName,
		int aAwaitedPlayerCount, int aPlayerCount, String[] aPlayerNames)
	{
		super();
		
		assert aPlayerCount <= aAwaitedPlayerCount;
		
		id = aID;
		rules = aRules;
		rulesName = aRulesName;
		awaitedPlayerCount = aAwaitedPlayerCount;
		playerCount = aPlayerCount;
		
		if (aPlayerNames == null)
			playerNames = null;
		else
			playerNames = aPlayerNames.clone();
	}
	
	/**
	 * Crée de nouvelles informations sur une table
	 * @param aID ID de la table
	 * @param rulesInfo Informations sur les règles disponibles
	 * @param rulesID ID des règles de cette table
	 * @param aAwaitedPlayerCount Nombre attendu de joueurs
	 * @param aPlayerCount Nombre de joueurs
	 * @param aPlayerNames Noms des joueurs
	 */
	private TableInfo(int aID, RulesInfo[] rulesInfo, String rulesID,
		int aAwaitedPlayerCount, int aPlayerCount, String[] aPlayerNames)
	{
		super();
		
		id = aID;
		rules = RulesInfo.findRulesByID(rulesInfo, rulesID);
		if (rules == null)
			rulesName = rulesID;
		else
			rulesName = rules.getName();
		awaitedPlayerCount = aAwaitedPlayerCount;
		playerCount = aPlayerCount;
		
		if (aPlayerNames == null)
			playerNames = null;
		else
			playerNames = aPlayerNames.clone();
	}
	
	/**
	 * Crée de nouvelles informations sur une table
	 * @param aID ID de la table
	 * @param aRules Règles associées
	 * @param aAwaitedPlayerCount Nombre attendu de joueurs
	 * @param aPlayerNames Noms des joueurs
	 */
	public TableInfo(int aID, RulesInfo aRules, int aAwaitedPlayerCount,
		String[] aPlayerNames)
	{
		this(aID, aRules, aRules.getName(), aAwaitedPlayerCount,
			aPlayerNames.length, aPlayerNames);
	}
	
	/**
	 * Crée de nouvelles informations sur une table
	 * @param aID ID de la table
	 * @param aRules Règles associées
	 * @param aAwaitedPlayerCount Nombre attendu de joueurs
	 * @param aPlayerCount Nombre de joueurs
	 */
	public TableInfo(int aID, RulesInfo aRules, int aAwaitedPlayerCount,
		int aPlayerCount)
	{
		this(aID, aRules, aRules.getName(), aAwaitedPlayerCount,
			aPlayerCount, null);
	}
	
	/**
	 * Crée de nouvelles informations sur une table
	 * @param aID ID de la table
	 * @param rulesInfo Informations sur les règles disponibles
	 * @param rulesID ID des règles de cette table
	 * @param aAwaitedPlayerCount Nombre attendu de joueurs
	 * @param aPlayerNames Noms des joueurs
	 */
	public TableInfo(int aID, RulesInfo[] rulesInfo, String rulesID,
		int aAwaitedPlayerCount, String[] aPlayerNames)
	{
		this(aID, rulesInfo, rulesID, aAwaitedPlayerCount, aPlayerNames.length,
			aPlayerNames);
	}
	
	/**
	 * Crée de nouvelles informations sur une table
	 * @param aID ID de la table
	 * @param rulesInfo Informations sur les règles disponibles
	 * @param rulesID ID des règles de cette table
	 * @param aAwaitedPlayerCount Nombre attendu de joueurs
	 * @param aPlayerCount Nombre de joueurs
	 */
	public TableInfo(int aID, RulesInfo[] rulesInfo, String rulesID,
		int aAwaitedPlayerCount, int aPlayerCount)
	{
		this(aID, rulesInfo, rulesID, aAwaitedPlayerCount, aPlayerCount, null);
	}
	
	/**
	 * Crée de nouvelles informations sur une table
	 * @param aID ID de la table
	 * @param aRulesName Nom des règles
	 * @param aAwaitedPlayerCount Nombre attendu de joueurs
	 * @param aPlayerCount Nombre de joueurs
	 */
	public TableInfo(int aID, String aRulesName, int aAwaitedPlayerCount,
		int aPlayerCount)
	{
		this(aID, (RulesInfo) null, aRulesName, aAwaitedPlayerCount,
			aPlayerCount, null);
	}
	
	/**
	 * Crée de nouvelles informations sur une table
	 * @param aID ID de la table
	 * @param aRulesName Nom des règles
	 * @param aAwaitedPlayerCount Nombre attendu de joueurs
	 * @param aPlayerNames Noms des joueurs
	 */
	public TableInfo(int aID, String aRulesName, int aAwaitedPlayerCount,
		String[] aPlayerNames)
	{
		this(aID, (RulesInfo) null, aRulesName, aAwaitedPlayerCount,
			aPlayerNames.length, aPlayerNames);
	}
	
	/**
	 * ID de la table
	 * @return ID de la table
	 */
	public int getID()
	{
		return id;
	}
	
	/**
	 * Règles associées
	 * @return Règles associées
	 */
	public RulesInfo getRules()
	{
		return rules;
	}
	
	/**
	 * Nom des règles
	 * @return Nom des règles
	 */
	public String getRulesName()
	{
		return rulesName;
	}
	
	/**
	 * Nombre minimum de joueurs
	 * @return Nombre minimum de joueurs
	 */
	public int getAwaitedPlayerCount()
	{
		return awaitedPlayerCount;
	}
	
	/**
	 * Nombre maximum de joueurs
	 * @return Nombre maximum de joueurs
	 */
	public int getPlayerCount()
	{
		return playerCount;
	}
	
	/**
	 * Noms des joueurs
	 * @return Noms des joueurs, ou <tt>null</tt> si inconnus
	 */
	public String[] getPlayerNames()
	{
		if (playerNames == null)
			return null;
		else
			return playerNames.clone();
	}
	
	/**
	 * Teste si la table est pleine
	 * @return <tt>true</tt> si la table est pleine, <tt>false</tt> sinon
	 */
	public boolean isFull()
	{
		return playerCount == awaitedPlayerCount;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return String.format("%s (%d/%d)", rulesName, playerCount,
			awaitedPlayerCount);
	}
}
