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
package sjrd.tricktakinggame.remotable;

import java.util.*;

import sjrd.tricktakinggame.cards.*;

/**
 * Joueur remotable (auquel on peut accéder à distance)
 * @author sjrd
 */
public interface RemotablePlayer
{
	/**
	 * Jeu propriétaire
	 * @return Jeu propriétaire
	 */
	public RemotableGame getGame();

	/**
	 * Equipe de ce joueur
	 * @return Equipe de ce joueur
	 */
	public RemotableTeam getTeam();

	/**
	 * Nom de ce joueur
	 * @return Nom de ce joueur
	 */
	public String getName();

	/**
	 * Position du joueur
	 * @return Position du joueur
	 */
	public int getPosition();

	/**
	 * Xème joueur suivant
	 * @param count Nombre de joueurs à compter (0 = soi-même)
	 * @return Xème joueur suivant
	 */
	public RemotablePlayer nextPlayer(int count);

	/**
	 * Joueur suivant
	 * @return Joueur suivant
	 */
	public RemotablePlayer nextPlayer();

	/**
	 * Nombre de cartes en main
	 * @return Nombre de cartes en main
	 */
	public int getCardCount();

	/**
	 * Tableau zero-based des cartes en main
	 * @param index Index d'une carte
	 * @return Carte à l'index spécifié
	 */
	public Card getCards(int index);

	/**
	 * Renvoie un snapshot des cartes en main
	 * @return Snapshot des cartes en main
	 */
	public List<Card> getCardsSnapshot();
	
	/**
	 * Teste si le joueur a joué une carte dans le pli courant
	 * <p>
	 * Si oui, cette carte peut être identifiée <i>via</i> la méthode
	 * <tt>getPlayedCard()</tt>.
	 * </p>
	 * @return <tt>true</tt> si le joueur a joué une carte, <tt>false</tt> sinon
	 * @see #getPlayedCard()
	 */
	public boolean hasPlayedCard();
	
	/**
	 * Indique si la carte jouée par le joueur est cachée
	 * <p>
	 * Lorsqu'une carte jouée est cachée, elle est affichée face cachée, donc on
	 * ne peut pas savoir ce qu'elle est.
	 * </p>
	 * <p>
	 * Si le joueur n'a joué aucune carte (ce qui peut être testé avec
	 * <tt>hasPlayedCard()</tt>, le résultat de cette fonction est indéterminé.
	 * </p>
	 * @return <tt>true</tt> si la carte est jouée face cachée, <tt>false</tt>
	 *         sinon
	 * @see #getPlayedCard()
	 * @see #hasPlayedCard()
	 */
	public boolean isPlayedCardHidden();

	/**
	 * Carte jouée dans le pli courant
	 * <p>
	 * Pendant un pli, les joueurs jouent chacun une carte, qui est déposée sur
	 * le tapis en attendant que les autres jouent. C'est cette carte-là qui est
	 * désignée ici.
	 * </p>
	 * <p>
	 * Pour savoir si le joueur a effectivement joué une carte, utilisez la
	 * méthode <tt>hasPlayedCard()</tt>. Si cette méthode retourne
	 * <tt>false</tt>, le résultat de <tt>getPlayedCard()</tt> est indéterminé.
	 * </p>
	 * <p>
	 * Si la carte jouée est cachée (ce que détermine
	 * <tt>isPlayedCardHidden()</tt>), il est possible que cette méthode renvoie
	 * <tt>null</tt>, selon les informations disponibles.
	 * </p>
	 * @return Carte jouée dans le pli courant (peut être <tt>null</tt>)
	 * @see #hasPlayedCard()
	 * @see #isPlayedCardHidden()
	 */
	public Card getPlayedCard();

	/**
	 * Renvoie une liste snapshot des cartes ramassées
	 * @return Liste snapshot des cartes ramassées
	 */
	public List<Card> getCollectedCardsSnapshot();

	/**
	 * Nombre de plis ramassés
	 * <p>
	 * Ce nombre est calculé selon la formule (nombre de cartes collectées) /
	 * (nombre de joueurs). En effet, un pli contient autant de cartes que le
	 * nombre de joueurs (en principe).
	 * </p>
	 * @return Nombre de plis ramassés
	 */
	public int getCollectedTrickCount();
}
