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
package sjrd.tricktakinggame.cards;

import java.io.*;
import java.util.*;

import static sjrd.util.ArrayUtils.*;

/**
 * Couleur de carte
 * <p>
 * Aucune couleur (sans-atout), coeur, carreau, trèfle ou pique
 * </p>
 * @author sjrd
 */
public enum Suit implements Serializable
{
	None("", ""), Heart("Coeur", "Hearts"),
	Diamond("Carreau", "Diamonds"), Club("Trèfle", "Clubs"),
	Spade("Pique", "Spades");

	/**
	 * Nom de la couleur
	 */
	private String name;
	
	/**
	 * ID de dessin
	 */
	private String drawID;

	/**
	 * Les quatre couleurs existantes
	 */
	public static final Suit[] allSuits = {Heart, Diamond, Club, Spade};

	/**
	 * Initialise une couleur
	 * @param aName Nom de la couleur
	 * @param aDrawID ID de dessin
	 */
	Suit(String aName, String aDrawID)
	{
		name = aName;
		drawID = aDrawID;
	}

	/**
	 * Nom de la couleur
	 * @param noneName Nom donné à la couleur <tt>ccNone</tt>
	 * @return Nom de la couleur, ou <tt>noneName</tt> si c'est
	 *         <tt>ccNone</tt>
	 */
	public String getName(String noneName)
	{
		if (this == None)
			return noneName;
		else
			return name;
	}

	/**
	 * Nom de la couleur
	 * @return Nom de la couleur (chaîne vide pour <tt>ccNone</tt>)
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return getName();
	}
	
	/**
	 * ID de dessin
	 * @return ID de dessin
	 */
	public String getDrawID()
	{
		return drawID;
	}
	
	/**
	 * Comparateur à utiliser pour trier des couleurs selon un ordre visuel
	 * <p>
	 * Ce comparateur arrange les couleurs de façon à alterner rouge et noir.
	 * </p>
	 * @author sjrd
	 */
	public static class VisualHelpingComparator implements Comparator<Suit>
	{
		/**
		 * Couleurs triées
		 */
		protected static final Suit[] orderedSuits = new Suit[]
			{Spade, Diamond, Club, Heart};
		
		/**
		 * {@inheritDoc}
		 */
		public int compare(Suit left, Suit right)
		{
			int leftIndex = arrayIndexOf(orderedSuits, left);
			int rightIndex = arrayIndexOf(orderedSuits, right);
			return leftIndex - rightIndex;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj)
		{
			return obj.getClass() == this.getClass();
		}
	}
}
