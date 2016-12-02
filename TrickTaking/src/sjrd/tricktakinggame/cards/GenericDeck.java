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
 * Paquet de cartes générique
 * <p>
 * Un paquet de cartes retient l'ensemble des cartes qui y étaient contenues
 * initialement, donc celles qui appartiennent au jeu en cours.
 * </p>
 * <p>
 * Cela permet de retrouver une carte par son ID, qu'elle soit encore présente
 * ou non dans le paquet. Cela permet aussi de vérifier qu'une carte provient
 * bien d'un paquet donné.
 * </p>
 * <p>
 * En plus de cela, cette classe propose quelques opérations utilisées
 * naturellement avec un paquet de cartes, comme le mélanger.
 * </p>
 * @author sjrd
 */
public class GenericDeck<T extends Card> extends ArrayList<T>
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Cartes contenues initialement dans le paquet
	 */
	private List<T> originalCards;

	/**
	 * Crée un nouveau paquet
	 * @param cards Cartes présentes dans le paquet
	 */
	public GenericDeck(Collection<? extends T> cards)
	{
		super(cards);
		
		originalCards = new ArrayList<T>(size());
		originalCards.addAll(this);
	}

	/**
	 * Nombre de cartes initialement dans le paquet
	 * @return Nombre de cartes initialement dans le paquet
	 */
	public int getOriginalSize()
	{
		return originalCards.size();
	}

	/**
	 * Tableau zero-based des cartes initiales
	 * @param index Index d'une carte
	 * @return Carte à l'index spécifié
	 */
	public T getOriginalCards(int index)
	{
		return originalCards.get(index);
	}

	/**
	 * Itérateur des cartes initiales
	 * @return Itérateur sur les cartes initiales
	 */
	public Iterator<T> originalCardsIterator()
	{
		return originalCards.iterator();
	}

	/**
	 * Itérable des cartes initiales
	 * @return Itérable sur les cartes initiales
	 */
	public Iterable<T> originalCardsIterable()
	{
		return originalCards;
	}

	/**
	 * Indique si une carte appartient à ce paquet
	 * @param Carte à tester
	 * @return <tt>true</tt> si la carte appartient à ce paquet,
	 *         <tt>false</tt> sinon
	 */
	public boolean ownsCard(T card)
	{
		return originalCards.contains(card);
	}

	/**
	 * Réinitialise les cartes contenues à partir des cartes originales
	 */
	public void reset()
	{
		clear();
		addAll(originalCards);
	}

	/**
	 * Mélange les cartes actuellement dans le paquet
	 */
	public void shuffle()
	{
		Random rand = new Random();

		for (int i = size() - 1; i > 0; i--)
		{
			int newIndex = rand.nextInt(i + 1);
			T temp = get(newIndex);
			set(newIndex, get(i));
			set(i, temp);
		}
	}

	/**
	 * Ajoute une carte au sommet du paquet
	 * @param card Carte à ajouter
	 */
	public void push(T card)
	{
		add(card);
	}

	/**
	 * Prend la carte au sommet du paquet, et donc la retire du paquet
	 * @return Carte prise
	 */
	public T pop()
	{
		return remove(size() - 1);
	}
	
	/**
	 * Trouve une carte appartenant à l'origine à ce paquet d'après son ID
	 * @param cardID ID de la carte recherchée
	 * @return Carte correspondante
	 * @throws IllegalArgumentException Aucune carte ne porte l'ID donné
	 */
	public T findCardByID(String cardID)
	{
		for (T card: originalCards)
			if (card.getID().equals(cardID))
				return card;
		
		throw new IllegalArgumentException(cardID + " is not a valid card ID");
	}
	
	/**
	 * Trouve une carte de ce paquet d'après ses propriétés
	 * @param suit Couleur de la carte
	 * @param force Force de la carte
	 * @return Carte correspondante
	 * @throws IllegalArgumentException Aucune carte ne porte l'ID donné
	 */
	public T findCardByProps(Suit suit, int force)
	{
		for (T card: originalCards)
			if ((card.getSuit() == suit) && (card.getForce() == force))
				return card;
		
		throw new IllegalArgumentException("Can't find that card");
	}
}
