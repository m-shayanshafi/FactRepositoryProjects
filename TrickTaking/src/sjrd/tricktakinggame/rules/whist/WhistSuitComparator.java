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
package sjrd.tricktakinggame.rules.whist;

import java.util.*;

import sjrd.tricktakinggame.cards.*;

import static sjrd.util.ArrayUtils.*;

/**
 * Comparateur de couleurs selon leurs forces au Whist
 * @author sjrd
 */
public class WhistSuitComparator implements Comparator<Suit>
{
	/**
	 * Couleurs ordonnées selon leur force
	 */
	private static final Suit[] orderedSuits =
		{Suit.Spade, Suit.Club, Suit.Diamond, Suit.Heart};
	
	/**
	 * Couleurs ordonnées selon leur force
	 * @return Tableau des 4 couleurs ordonnées selon leur force
	 */
	public static Suit[] getOrderedSuits()
	{
		return orderedSuits.clone();
	}
	
	/**
	 * Récupère la valeur numérique d'ordre d'une couleur
	 * @param suit Couleur
	 * @return Valeur numérique d'ordre
	 */
	public static int getSuitOrderValue(Suit suit)
	{
		return arrayIndexOf(orderedSuits, suit);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int compare(Suit left, Suit right)
	{
		return getSuitOrderValue(left) - getSuitOrderValue(right);
	}
}
