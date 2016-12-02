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

import sjrd.tricktakinggame.cards.*;

/**
 * Contrôleur d'un joeuur
 * <p>
 * Les 3 méthodes de déroulement du jeu (<tt>chooseAnnounce</tt>,
 * <tt>playTurn</tt>, <tt>messageDialog</tt> et <tt>pause</tt>) peuvent être
 * mutuellement synchronisées.
 * </p>
 * <p>
 * Par contre, les autres méthodes doivent pouvoir fonctionner de manière
 * concurrente entre elles, et avec une des 3 premières.
 * </p>
 * @author sjrd
 */
public interface PlayerController
{
	/**
	 * Choisit une annonce parmi celles disponibles
	 * @param <A> Type d'annonce
	 * @param availableAnnounces Annonces disponibles
	 * @return Annonce choisie
	 */
	public <A extends Announce> A chooseAnnounce(A ... availableAnnounces)
		throws CardGameException;
	
	/**
	 * Joue un tour
	 * @return Carte jouée
	 */
	public Card playTurn() throws CardGameException;
	
	/**
	 * Pause le temps d'observer la situation
	 */
	public void pause() throws CardGameException;

	/**
	 * Notifie un changement dans le jeu
	 */
	public void notifyUpdate();

	/**
	 * Affiche un message asynchrone au joueur
	 * @param message Message à afficher
	 */
	public void showMessage(Message message);
}
