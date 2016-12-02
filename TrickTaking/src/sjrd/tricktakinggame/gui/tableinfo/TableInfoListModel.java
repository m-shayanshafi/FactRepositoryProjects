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
package sjrd.tricktakinggame.gui.tableinfo;

import javax.swing.*;

import sjrd.tricktakinggame.client.*;

/**
 * Modèle de liste des infos sur une table
 * @author sjrd
 */
public class TableInfoListModel extends AbstractListModel
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Informations sur la table à afficher
	 */
	private TableInfo tableInfo = null;
	
	/**
	 * Informations affichées
	 * @return Informations affichées
	 */
	public TableInfo getTableInfo()
	{
		return tableInfo;
	}
	
	/**
	 * Indique que toute la liste a changé
	 * @param oldSize Ancienne taille de la liste
	 * @param newSize Nouvelle taille de la liste
	 */
	protected void fireAllChanged(int oldSize, int newSize)
	{
		if (newSize > oldSize)
		{
			fireIntervalAdded(this, oldSize, newSize-1);
			fireContentsChanged(this, 0, oldSize-1);
		}
		else if (newSize < oldSize)
		{
			fireIntervalRemoved(this, newSize, oldSize-1);
			fireContentsChanged(this, 0, newSize-1);
		}
		else
		{
			fireContentsChanged(this, 0, newSize-1);
		}
	}
	
	/**
	 * Modifie les informations à afficher
	 * @param value Nouvelles informations
	 */
	public void setTableInfo(TableInfo value)
	{
		int oldSize = getSize();
		tableInfo = value;
		int newSize = getSize();
		
		fireAllChanged(oldSize, newSize);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getSize()
	{
		if (tableInfo == null)
			return 0;
		else
		{
			String[] playerNames = tableInfo.getPlayerNames();
			if (playerNames == null)
				return 0;
			else
				return tableInfo.getPlayerNames().length + 1;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String getElementAt(int index)
	{
		if (tableInfo == null)
			return "";
		
		if (index == 0)
			return tableInfo.toString();
		index--;
		
		String[] playerNames = tableInfo.getPlayerNames();
		if ((playerNames == null) && (index < playerNames.length))
			return playerNames[index] == null ? "" : playerNames[index];
		
		return "";
	}
}
