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

/**
 * Annonce dans une enchère
 * @author sjrd
 */
public class Announce
{
	/**
	 * Joueur qui fait l'annonce
	 */
	private RemotablePlayer player;
	
	/**
	 * ID de l'annonce
	 */
	private String id;
	
	/**
	 * Nom de l'annonce
	 */
	private String name;
	
	/**
	 * Crée l'annonce
	 * @param aPlayer Joueur qui fait l'annonce
	 * @param aID ID de l'annonce
	 * @param aName Nom de l'annonce
	 */
	public Announce(RemotablePlayer aPlayer, String aID, String aName)
	{
		super();
		
		player = aPlayer;
		id = aID;
		name = aName;
	}
	
	/**
	 * Joueur qui fait l'annonce
	 * @return Joueur qui fait l'annonce
	 */
	public RemotablePlayer getPlayer()
	{
		return player;
	}
	
	/**
	 * ID
	 * <p>
	 * L'ID d'une annonce est utilisée essentiellement par les modules
	 * d'intelligence artificielle.
	 * </p>
	 * @return ID
	 */
	public String getID()
	{
		return id;
	}
	
	/**
	 * Nom de l'annonce
	 * @return Nom de l'annonce
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
}
