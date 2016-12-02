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
package sjrd.tricktakinggame.game;

import sjrd.tricktakinggame.remotable.*;

/**
 * Equipe
 * @author sjrd
 */
public class Team extends BaseRemotableTeam<Game, Player>
{
	/**
	 * Crée une nouvelle équipe
	 * @param aGame Jeu propriétaire
	 */
	public Team(Game aGame)
	{
		super(aGame);

		getGame().addTeam(this);
	}

	/**
	 * Ajoute un joueur
	 * @param player Joueur à ajouter
	 */
	protected void addPlayer(Player player)
	{
		super.addPlayer(player);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setScore(int value)
	{
		if (value == getScore())
			return;
		
		super.setScore(value);
		getGame().notifyUpdate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addToScore(int value)
	{
		if (value == 0)
			return;
		
		super.addToScore(value);
		getGame().notifyUpdate();
	}
}
