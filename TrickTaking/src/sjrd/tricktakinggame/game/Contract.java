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
package sjrd.tricktakinggame.game;

/**
 * Contrat établi d'un jeu
 * @author sjrd
 */
public class Contract
{
	/**
	 * Nom du contrat
	 */
	private String name;
	
	/**
	 * Crée le contrat
	 * @param aName Nom du contrat
	 */
	public Contract(String aName)
	{
		super();
		
		name = aName;
	}
	
	/**
	 * Nom du contrat
	 * @return Nom du contrat
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Modifie le nom du contrat
	 * @param value Nouveau nom
	 */
	protected void setName(String value)
	{
		name = value;
	}
}
