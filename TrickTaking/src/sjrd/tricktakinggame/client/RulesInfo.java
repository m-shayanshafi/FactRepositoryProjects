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
 * Informations sur des règles disponibles sur le serveur
 * @author sjrd
 */
public class RulesInfo
{
	/**
	 * ID du fournisseur de règles
	 */
	private String id;
	
	/**
	 * Nom des règles
	 */
	private String name;
	
	/**
	 * Nombre minimum de joueurs
	 */
	private int minPlayerCount;
	
	/**
	 * Nombre maximum de joueurs
	 */
	private int maxPlayerCount;
	
	/**
	 * Trouve dans un tableau de règles celle qui a l'ID demandé
	 * @param rulesInfo Talbeau d'informations sur des règles
	 * @param rulesID ID des règles recherchées
	 * @return Règles correspondantes, ou <tt>null</tt> si non trouvé
	 */
	public static RulesInfo findRulesByID(RulesInfo[] rulesInfo, String rulesID)
	{
		for (RulesInfo info: rulesInfo)
			if (info.getID().equals(rulesID))
				return info;
		
		return null;
	}
	
	/**
	 * Crée de nouvelles informations sur une règle
	 * @param aID ID du fournisseur de règles
	 * @param aName Nom des règles
	 * @param aMinPlayerCount Nombre minimum de joueurs
	 * @param aMaxPlayerCount Nombre maximum de joueurs
	 */
	public RulesInfo(String aID, String aName, int aMinPlayerCount,
		int aMaxPlayerCount)
	{
		super();
		
		assert aMinPlayerCount <= aMaxPlayerCount;
		
		id = aID;
		name = aName;
		minPlayerCount = aMinPlayerCount;
		maxPlayerCount = aMaxPlayerCount;
	}
	
	/**
	 * ID du fournisseur de règles
	 * @return ID du fournisseur de règles
	 */
	public String getID()
	{
		return id;
	}
	
	/**
	 * Nom des règles
	 * @return Nom des règles
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Nombre minimum de joueurs
	 * @return Nombre minimum de joueurs
	 */
	public int getMinPlayerCount()
	{
		return minPlayerCount;
	}
	
	/**
	 * Nombre maximum de joueurs
	 * @return Nombre maximum de joueurs
	 */
	public int getMaxPlayerCount()
	{
		return maxPlayerCount;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return getName();
	}
}
