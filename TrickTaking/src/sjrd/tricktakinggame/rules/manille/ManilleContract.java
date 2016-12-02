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
package sjrd.tricktakinggame.rules.manille;

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.rules.*;

/**
 * Contrat de Manille
 * @author sjrd
 */
public class ManilleContract extends ContractWithTrump
{
	/**
	 * Indique si le contrat est contré
	 */
	private boolean countered;
	
	/**
	 * Indique si le contrat est surcontré
	 */
	private boolean reCountered;

	/**
	 * @param aName
	 * @param aTrump
	 * @param aCountered <tt>true</tt> si contré, <tt>false</tt> sinon
	 * @param aReCountered <tt>true</tt> si surcontré, <tt>false</tt> sinon
	 */
	public ManilleContract(Suit aTrump, boolean aCountered,
		boolean aReCountered)
	{
		super(makeName(aTrump, aCountered, aReCountered), aTrump);
		
		assert countered || !reCountered;
		
		countered = aCountered;
		reCountered = aReCountered;
	}
	
	/**
	 * Construit le nom du contrat
	 * @param trump Atout
	 * @param countered <tt>true</tt> si contré, <tt>false</tt> sinon
	 * @param reCountered <tt>true</tt> si surcontré, <tt>false</tt> sinon
	 * @return Nom du contrat
	 */
	private static String makeName(Suit trump, boolean countered,
		boolean reCountered)
	{
		String result;
		
		if (trump == Suit.None)
			result = "Sans-atout";
		else
			result = "Atout " + trump.getName();
		
		if (reCountered)
			result += " (surcontré)";
		else if (countered)
			result += " (contré)";
		
		return result;
	}

	/**
	 * Teste si le contrat est surcontré
	 * @return <tt>true</tt> si le contrat est contré, <tt>false</tt> sinon
	 */
	public boolean isCountered()
	{
		return countered;
	}
	
	/**
	 * Teste si le contrat est surcontré
	 * @return <tt>true</tt> si le contrat est surcontré, <tt>false</tt> sinon
	 */
	public boolean isReCountered()
	{
		return reCountered;
	}
}
