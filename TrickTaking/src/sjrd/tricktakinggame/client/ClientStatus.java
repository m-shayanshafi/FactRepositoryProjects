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

import java.util.*;

/**
 * Statut d'un client
 * @author sjrd
 */
public class ClientStatus
{
	/**
	 * Type de statut
	 */
	ClientStatusKind statusKind;
	
	/**
	 * Informations sur la table
	 */
	TableInfo tableInfo;
	
	/**
	 * Position dans la table
	 */
	private int selfPosition = -1;
	
	/**
	 * Indique s'il veut commencer la partie
	 */
	private boolean wantToBeginGame = false;
	
	/**
	 * Indique s'il a des messages en attente
	 */
	private boolean pendingMessages = false;
	
	/**
	 * Nombre de ticks d'update
	 */
	private int updateTickCount = 0;
	
	/**
	 * Crée le statut
	 * @param kind Type de statut
	 * @param addInfo Infos additionnelles
	 */
	public ClientStatus(ClientStatusKind kind, String[] addInfo)
	{
		super();
		
		statusKind = kind;
		
		Map<String, String> info = new HashMap<String, String>(addInfo.length);
		
		for (String line: addInfo)
		{
			int pos = line.indexOf('=');
			info.put(line.substring(0, pos),
				line.substring(pos+1, line.length()));
		}
		
		if (info.containsKey("TableID"))
		{
			// Informations sur la table courante
			
			int tableID = Integer.parseInt(info.get("TableID"));
			
			String rulesName = info.get("RulesName");

			int awaitedPlayerCount =
				Integer.parseInt(info.get("AwaitedPlayerCount"));

			String[] playerNames = new String[Integer.parseInt(
				info.get("PlayerCount"))];
			for (int i = 0; i < playerNames.length; i++)
				playerNames[i] = info.get("PlayerName" + i);
			
			tableInfo = new TableInfo(tableID, rulesName, awaitedPlayerCount,
				playerNames);
		}
		else
		{
			// Pas de table
			tableInfo = null;
		}
		
		if (info.containsKey("SelfPosition"))
			selfPosition = Integer.parseInt(info.get("SelfPosition"));
		
		if (info.containsKey("WantToBeginGame"))
			wantToBeginGame =
				Integer.parseInt(info.get("WantToBeginGame")) != 0;
		
		if (info.containsKey("HasPendingMessages"))
			pendingMessages =
				Integer.parseInt(info.get("HasPendingMessages")) != 0;
		if (info.containsKey("UpdateTickCount"))
			updateTickCount = Integer.parseInt(info.get("UpdateTickCount"));
	}
	
	/**
	 * Crée le statut sans infos additionnelles
	 * @param kind Type de statut
	 */
	public ClientStatus(ClientStatusKind kind)
	{
		this(kind, new String[0]);
	}
	
	/**
	 * Type de statut
	 * @return Type de statut
	 */
	public ClientStatusKind getStatusKind()
	{
		return statusKind;
	}
	
	/**
	 * Informations sur la table courante
	 * @return Informations sur la table courante (peut être <tt>null</tt>)
	 */
	public TableInfo getTableInfo()
	{
		return tableInfo;
	}
	
	/**
	 * Position du joueur associé au client
	 * @return Position du joueur associé au client
	 */
	public int getSelfPosition()
	{
		return selfPosition;
	}
	
	/**
	 * Indique s'il veut commencer la partie
	 * @return <tt>true</tt> s'il veut commencer la partie, <tt>false</tt> sinon
	 */
	public boolean doesWantToBeginGame()
	{
		return wantToBeginGame;
	}
	
	/**
	 * Indique s'il a des messages en attente
	 * @return <tt>true</tt> s'il a des messages en attente, <tt>false</tt>
	 *         sinon
	 */
	public boolean hasPendingMessages()
	{
		return pendingMessages;
	}
	
	/**
	 * Nombre de ticks d'update
	 * @return Nombre de ticks d'update
	 */
	public int getUpdateTickCount()
	{
		return updateTickCount;
	}
}
