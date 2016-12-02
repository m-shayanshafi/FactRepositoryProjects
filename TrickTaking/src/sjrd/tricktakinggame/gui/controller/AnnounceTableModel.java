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
package sjrd.tricktakinggame.gui.controller;

import java.util.*;

import javax.swing.table.*;

import sjrd.tricktakinggame.remotable.*;

/**
 * Modèle de la table des annonces
 * @author sjrd
 */
public class AnnounceTableModel extends AbstractTableModel
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Jeu maître dont afficher les annonces
	 */
	private RemotableGame game;
	
	/**
	 * Annonces à afficher
	 */
	private List<Announce> announces = null;

	/**
	 * Crée le modèle de table
	 * @param aGame Jeu maître dont afficher les scores
	 */
	public AnnounceTableModel(RemotableGame aGame)
	{
		super();
		
		game = aGame;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getColumnCount()
	{
		return game.getPlayerCount();
	}

	/**
	 * Nom d'une colonne
	 * <p>
	 * Le nom est le nom du joueur correspondant à la colonne
	 * </p>
	 * @param column  the column being queried
	 * @return a string containing the default name of <code>column</code>
	 */
	public String getColumnName(int column)
	{
		return game.getPlayers(column).getName();
	}

	/**
	 * {@inheritDoc}
	 */
    public Class<?> getColumnClass(int columnIndex)
    {
    	return String.class;
    }
	
	/**
	 * {@inheritDoc}
	 */
	public int getRowCount()
	{
		if (announces == null)
			return 0;
		else
			return announces.size() + 1;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getValueAt(int rowIndex, int columnIndex)
	{
		if (announces == null)
			return "";
		
		if (rowIndex == 0)
			return game.getPlayers(columnIndex).getName();
		rowIndex--;
		
		Announce announce = announces.get(rowIndex);
		
		if (announce.getPlayer().getPosition() != columnIndex)
			return "";
		else
			return announce.getName();
	}
	
	/**
	 * Met à jour l'affichage des scores
	 */
	public void updateDisplay()
	{
		announces = game.getAnnounces();
		fireTableDataChanged();
	}
}
