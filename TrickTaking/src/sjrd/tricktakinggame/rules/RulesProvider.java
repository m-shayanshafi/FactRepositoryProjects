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

import sjrd.tricktakinggame.game.*;

/**
 * Fournisseur de règles
 * @author sjrd
 */
public interface RulesProvider
{
	/**
	 * ID de ce fournisseur de règles
	 * @return ID
	 */
	public String getID();
	
	/**
	 * Nom du jeu
	 * @return Nom du jeu
	 */
	public String getName();
	
	/**
	 * Nombre minimum de joueurs
	 * @return Nombre minimum de joueurs
	 */
	public int getMinPlayerCount();
	
	/**
	 * Nombre maximum de joueurs
	 * @return Nombre maximum de joueurs
	 */
	public int getMaxPlayerCount();
	
	/**
	 * Crée un objet règles pour un nombre donné de joueurs
	 * @param playerCount Nombre de joueurs
	 * @return Règles
	 */
	public Rules newRules(int playerCount);
}
