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

import java.util.*;

/**
 * Paquet de cartes
 * @author sjrd
 */
public class Deck extends GenericDeck<Card>
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Construit un ensemble de cartes initial
	 * @param suits Couleurs des cartes utilisées
	 * @param names Noms des cartes dans chaque couleur, ordonnées par force
	 * @param namesDrawIDs ID de dessin des noms de cartes, même ordre
	 * @return Collection contenant toutes les cartes demandées
	 */
	protected static Collection<Card> makeInitialCards(Suit[] suits,
		String[] names, String[] namesDrawIDs)
	{
		assert names.length == namesDrawIDs.length;

		Collection<Card> result = new ArrayList<Card>(
			suits.length * names.length);

		for (int force = 0; force < names.length; force++)
			for (Suit suit: suits)
				result.add(new Card(suit, force, names[force],
					suit.getDrawID() + "-" + namesDrawIDs[force]));
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Deck(Collection<Card> cards)
	{
		super(cards);
	}

	/**
	 * Crée un nouveau paquet
	 * @param suits Couleurs des cartes utilisées
	 * @param names Noms des cartes dans chaque couleur, ordonnées par force
	 * @param namesDrawIDs ID de dessin des noms de cartes, même ordre
	 */
	public Deck(Suit[] suits, String[] names, String[] namesDrawIDs)
	{
		this(makeInitialCards(suits, names, namesDrawIDs));
	}

	/**
	 * Crée un paquet avec les quatre couleurs de cartes
	 * @param names Noms des cartes dans chaque couleur, ordonnées par force
	 * @param namesDrawIDs ID de dessin des noms de cartes, même ordre
	 */
	public Deck(String[] names, String[] namesDrawIDs)
	{
		this(makeInitialCards(Suit.allSuits, names, namesDrawIDs));
	}
}
