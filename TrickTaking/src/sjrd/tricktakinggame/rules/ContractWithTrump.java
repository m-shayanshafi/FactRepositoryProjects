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
package sjrd.tricktakinggame.rules;

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.game.*;

/**
 * Contrat établi d'un jeu qui définit un atout
 * @author sjrd
 */
public class ContractWithTrump extends Contract
{
	/**
	 * Atout
	 */
	private Suit trump;
	
	/**
	 * Crée le contrat
	 * @param aName {@inheritDoc}
	 * @param aTrump Atout
	 */
	public ContractWithTrump(String aName, Suit aTrump)
	{
		super(aName);
		trump = aTrump;
	}
	
	/**
	 * Atout
	 * @return Atout
	 */
	public Suit getTrump()
	{
		return trump;
	}
	
	/**
	 * Modifie l'atout
	 * @param value Nouvel atout
	 */
	protected void setTrump(Suit value)
	{
		trump = value;
	}
}
