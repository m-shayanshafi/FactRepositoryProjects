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

import javax.swing.*;

import sjrd.tricktakinggame.remotable.*;

/**
 * @author sjrd
 */
public class TeamScoreListModel extends AbstractListModel
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Jeu maître dont afficher les scores
	 */
	private RemotableGame game;

	/**
	 * Crée le modèle de liste
	 * @param aGame Jeu maître dont afficher les scores
	 */
	public TeamScoreListModel(RemotableGame aGame)
	{
		super();
		
		game = aGame;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getSize()
	{
		return game.getTeamCount();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getElementAt(int index)
	{
		RemotableTeam team = game.getTeams(index);
		
		return String.format("%s : %d points", team.getName(), team.getScore());
	}
	
	/**
	 * Met à jour l'affichage des scores
	 */
	public void updateDisplay()
	{
		fireContentsChanged(this, 0, getSize()-1);
	}
}
