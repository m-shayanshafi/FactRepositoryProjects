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
package sjrd.tricktakinggame.rules.whist.contracts;

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.game.*;
import sjrd.tricktakinggame.remotable.*;
import sjrd.tricktakinggame.rules.whist.*;

/**
 * Contrat utilisé lorsqu'aucun contrat n'a pu être établi
 * @author sjrd
 */
public class NoContract extends WhistContract
{
	/**
	 * Crée le contrat
	 * @param aRules Règles associées
	 */
	public NoContract(WhistRules aRules, Player aPlayer)
	{
		super(aRules, "Aucun contrat", Suit.None, aPlayer);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void playTricks() throws CardGameException
	{
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeScores() throws CardGameException
	{
	}
}
