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

import java.util.*;

import sjrd.tricktakinggame.remotable.*;

/**
 * Enchères d'un jeu
 * @param <A> Type d'annonces de ces enchères
 * @param <C> Type de contrat produit par ces enchères
 * @author sjrd
 */
public abstract class Auction<A extends Announce, C extends Contract>
{
	/**
	 * Règles propriétaires
	 */
	private Rules rules;
	
	/**
	 * Jeu associé
	 */
	private Game game;
	
	/**
	 * Annonces faites dans ces enchères
	 */
	private List<A> announces = new ArrayList<A>();
	
	/**
	 * Crée les enchères
	 * @param aRules Règles propriétaires
	 */
	public Auction(Rules aRules)
	{
		super();
		
		rules = aRules;
		game = rules.getGame();
	}
	
	/**
	 * Règles propriétaires
	 * @return Règles propriétaires
	 */
	public Rules getRules()
	{
		return rules;
	}
	
	/**
	 * Jeu associé
	 * @return Jeu associé
	 */
	public Game getGame()
	{
		return game;
	}
	
	/**
	 * Nombre d'annonces faites
	 * @return Nombre d'annonces faites
	 */
	public int getAnnounceCount()
	{
		synchronized (announces)
		{
			return announces.size();
		}
	}
	
	/**
	 * Récupère une annonce
	 * @param index Index de l'annonce
	 * @return Annonce à l'index spécifié
	 */
	public A getAnnounces(int index)
	{
		synchronized (announces)
		{
			return announces.get(index);
		}
	}
	
	/**
	 * Récupère un instantané des annonces faites dans ces enchères
	 * @return Liste des annonces faites dans ces enchères
	 */
	public List<A> getAnnouncesSnapshot()
	{
		synchronized (announces)
		{
			return new ArrayList<A>(announces);
		}
	}
	
	/**
	 * Récupère la dernière annonce faite jusque là
	 * @return Dernière annonce faite, ou <tt>null</tt> si aucune jusque là
	 */
	public A getLastAnnounce()
	{
		synchronized (announces)
		{
			int announceCount = announces.size();
			
			if (announceCount == 0)
				return null;
			else
				return announces.get(announceCount-1);
		}
	}
	
	/**
	 * Ajoute une annonce à celles faites
	 * @param announce
	 */
	protected void addAnnounce(A announce)
	{
		synchronized (announces)
		{
			announces.add(announce);
		}
		
		game.notifyUpdate();
	}
	
	/**
	 * Effectue les enchères
	 * @return Contrat établi
	 */
	public abstract C doAuction() throws CardGameException;
}
