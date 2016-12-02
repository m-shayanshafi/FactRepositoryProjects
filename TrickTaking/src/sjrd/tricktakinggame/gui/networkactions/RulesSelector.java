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
package sjrd.tricktakinggame.gui.networkactions;

import sjrd.tricktakinggame.client.*;

/**
 * Sélectionneur de règles
 * @author sjrd
 */
public interface RulesSelector
{
	/**
	 * Règles sélectionnées
	 * @return Règles sélectionnées (peut être <tt>null</tt>)
	 */
	public RulesInfo getSelectedRules();
	
	/**
	 * Nombre de joueurs sélectionné
	 * @return Nombre de joueurs sélectionné (-1 si nombre invalide)
	 */
	public int getSelectedPlayerCount();
}
