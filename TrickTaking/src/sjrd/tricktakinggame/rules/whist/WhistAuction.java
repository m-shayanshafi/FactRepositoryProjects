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
package sjrd.tricktakinggame.rules.whist;

import java.util.*;

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.game.*;
import sjrd.tricktakinggame.remotable.*;
import sjrd.tricktakinggame.rules.whist.announces.*;

/**
 * Enchères d'une partie de Whist
 * @author sjrd
 */
public class WhistAuction extends Auction<WhistAnnounce, WhistContract>
{
	/**
	 * Comparateur de couleurs
	 */
	public static final Comparator<Suit> suitComparator =
		new WhistSuitComparator();
	
	/**
	 * Règles associées
	 */
	private WhistRules rules;
	
	/**
	 * Dernière annonce faite par chaque joueur
	 */
	private WhistAnnounce[] lastAnnounces;
	
	/**
	 * Meilleure annonce jusque là
	 */
	private ContractAnnounce bestAnnounce;
	
	/**
	 * Crée les enchères
	 * @param aRules Règles associées
	 */
	public WhistAuction(WhistRules aRules)
	{
		super(aRules);
		
		rules = aRules;
		Game game = getGame();
		
		lastAnnounces = new WhistAnnounce[4];
		for (int i = 0; i < 4; i++)
			lastAnnounces[i] = new NoAnnounce(game.getPlayers(i));
		
		bestAnnounce = new NoAnnounce(game.getDealer());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public WhistRules getRules()
	{
		return rules;
	}
	
	/**
	 * Ajoute une annonce à celles faites
	 * <p>
	 * La dernière annonce faite par le joueur qui fait cette annonce est mise
	 * à jour également.
	 * </p>
	 * <p>
	 * De plus, si l'annonce faite est une annonce à contrat, alors celle-ci
	 * devient la meilleure annonce faite. En effet, la liste des annonces
	 * disponibles ne peut contenir que des annonces à contrat qui sont
	 * meilleures que l'actuelle meilleure annonce faite.
	 * Cette condition est garantie par le caractère final de
	 * <tt>ContractAnnounce.canOverride(ContractAnnounce)</tt>.
	 * </p>
	 * @param announce Annonce à ajouter
	 * @see ContractAnnounce#canOverride(ContractAnnounce)
	 */
	@Override
	public void addAnnounce(WhistAnnounce announce)
	{
		super.addAnnounce(announce);
		
		if (announce instanceof ContractAnnounce)
			bestAnnounce = (ContractAnnounce) announce;
	}
	
	/**
	 * Dernière annonce d'un joueur
	 * @param player Joueur
	 * @return Dernière annonce faite par ce joueur
	 */
	public WhistAnnounce getLastAnnounce(Player player)
	{
		return lastAnnounces[player.getPosition()];
	}
	
	/**
	 * Modifie la dernière annonce d'un joueur
	 * @param player Joueur
	 * @param value Nouvelle dernière annonce faite par ce joueur
	 */
	public void setLastAnnounce(Player player, WhistAnnounce value)
	{
		lastAnnounces[player.getPosition()].replaced(this, player, value);
		lastAnnounces[player.getPosition()] = value;
	}
	
	/**
	 * Teste si un joueur a passé
	 * @param player Joueur
	 * @return <tt>true</tt> si ce joueur a passé, <tt>false</tt> sinon
	 */
	public boolean hasPassed(Player player)
	{
		return getLastAnnounce(player) instanceof PassAnnounce;
	}
	
	/**
	 * Meilleure annonce faite jusque là
	 * @return Meilleure annonce faite jusque là
	 */
	public ContractAnnounce getBestAnnounce()
	{
		return bestAnnounce;
	}

	/**
	 * Effectue les enchères
	 * <p>
	 * Dans cette implémentation, tous les joueurs commencent avec une annonce
	 * fictive "Pas d'annonce", et la meilleure annonce est aussi une telle
	 * annonce fictive.
	 * </p>
	 * <p>
	 * Ensuite, à partir du joueur actif, chaque joueur, à son tour, peut faire
	 * une annonce. Ceci est fait en appelant la méthode
	 * <tt>doAnnounce(Player)</tt>.
	 * </p>
	 * <p>
	 * Lorsque cette méthode renvoie <tt>false</tt>, le joueur n'a rien dû
	 * annoncer. Dès que les 4 joueurs n'ont rien annoncé d'affilée, les
	 * enchères sont terminées.
	 * </p>
	 * <p>
	 * Lorsque les enchères sont terminée, le contrat est créé en fonction de la
	 * meilleure annonce faite jusque là, ce que renseigne la propriété
	 * <tt>bestAnnounce</tt>.
	 * </p>
	 */
	@Override
	public WhistContract doAuction() throws CardGameException
	{
		Game game = getGame();
		
		// Détecter les trous
		
		GapAnnounce gapAnnounce = GapAnnounce.checkForGap(this);
		if (gapAnnounce != null)
		{
			gapAnnounce.selected(this);
			addAnnounce(gapAnnounce);
			
			Card stopgapCard = gapAnnounce.getStopgapCard();
			for (Player player: game.playersIterable())
			{
				if (player.hasCard(stopgapCard))
				{
					StopgapAnnounce stopgapAnnounce =
						new StopgapAnnounce(player, gapAnnounce.getPlayer(),
							gapAnnounce.isRoyal(), stopgapCard);

					stopgapAnnounce.selected(this);
					addAnnounce(stopgapAnnounce);
					
					break;
				}
			}
		}
		
		// Enchères
		
		int noAnnounceCount = 0;
		Player currentPlayer = game.getActivePlayer();
		
		while (noAnnounceCount < 4)
		{
			if (doAnnounce(currentPlayer))
				noAnnounceCount = 0;
			else
				noAnnounceCount++;
			
			currentPlayer = currentPlayer.nextPlayer();
		}
		
		return bestAnnounce.makeContract(this, getRules());
	}
	
	/**
	 * Fait faire une annonce à un joueur
	 * @param player Player qui doit faire une annonce
	 * @return <tt>true</tt> si le joueur a fait une annonce, <tt>false</tt>
	 *         sinon
	 * @see #makeAvailableAnnounces(Player)
	 */
	protected boolean doAnnounce(Player player) throws CardGameException
	{
		// Etablir la liste des annonces possibles
		WhistAnnounce[] availableAnnounces = makeAvailableAnnounces(player);
		
		// Si null, alors le joueur ne doit/peut rien annoncer maintenant
		if (availableAnnounces == null)
			return false;
		
		// Demander au joueur l'annonce qu'il veut faire, et la rajouter
		WhistAnnounce announce = player.chooseAnnounce(availableAnnounces);
		announce.selected(this);
		addAnnounce(announce);
		
		return true;
	}
	
	/**
	 * Construit la liste des annonces disponibles pour un joueur
	 * <p>
	 * Si cette méthode renvoie <tt>null</tt>, cela signifie que ce joueur ne
	 * doit/peut rien annoncer (pas même "Passe").
	 * </p>
	 * <p>
	 * Deux situations exemple dans lesquelles cela peut arriver :
	 * </p>
	 * <ul>
	 *   <li>Ce joueur fait partie de la meilleure annonce faite jusqu'à
	 *   présent ;</li>
	 *   <li>Ce joueur a déjà passé, tout simplement.</li>
	 * </ul>
	 * <p>
	 * Les annonces disponibles pour un joueur sont déterminées par les contres
	 * spéciaux proposés par la dernière meilleure annonce, ainsi que par la
	 * dernière annonce qu'a faite ce joueur, dans cet ordre.
	 * </p>
	 * @param player Joueur qui doit faire l'annonce
	 * @return Liste des annonces que peut faire le joueur (ou <tt>null</tt>)
	 * @see ContractAnnounce#makeSpecialCounters(WhistAuction, Player, List)
	 * @see WhistAnnounce#makeAvailableAnnounces(WhistAuction, Player, List)
	 * @see #cleanAvailableAnnounces(Player, List)
	 */
	protected WhistAnnounce[] makeAvailableAnnounces(Player player)
	{
		// Si le joueur a déjà passé, il ne peut plus rien dire
		if (hasPassed(player))
			return null;
		
		// Si le joueur est compris dans la meilleure annonce, ne rien faire
		// Sauf si cette meilleure annonce est en faite la pseudo NoAnnounce
		if (!(bestAnnounce instanceof NoAnnounce))
		{
			if (bestAnnounce.getPlayer() == player)
				return null;
			if (bestAnnounce.getPartner() == player)
				return null;
		}
		
		// Sinon, on continue

		WhistAnnounce lastAnnounce = getLastAnnounce(player);
		List<WhistAnnounce> result = new LinkedList<WhistAnnounce>();
		
		result.add(new PassAnnounce(player));
		bestAnnounce.makeSpecialCounters(this, player, result);
		lastAnnounce.makeAvailableAnnounces(this, player, result);
		cleanAvailableAnnounces(player, result);
		
		// Résultat
		
		if (result.size() == 0)
			return null;
		else
			return result.toArray(new WhistAnnounce[result.size()]);
	}
	
	/**
	 * Nettoie les annonces disponibles
	 * <p>
	 * Cette dernière opération supprime toutes les annonces qui ne peuvent pas
	 * être faites au-dessus de la meilleure annonce courante.
	 * </p>
	 * <p>
	 * Elle supprime également tous les doublons, en conservant la première
	 * occurence.
	 * </p>
	 * @param player Joueur qui doit faire l'annonce
	 * @param announces Liste des annonces que peut faire le joueur
	 * @see WhistAnnounce#canOverride(ContractAnnounce)
	 * @see WhistAnnounce#equals(Object)
	 */
	protected void cleanAvailableAnnounces(Player player,
		List<WhistAnnounce> announces)
	{
		// Supprimer les annonces invalides
		
		Iterator<WhistAnnounce> iterator = announces.iterator();
		while (iterator.hasNext())
		{
			WhistAnnounce announce = iterator.next();
			if (!announce.canOverride(bestAnnounce))
				iterator.remove();
		}
		
		// Supprimer les doublons
		
		for (int i = 0; i < announces.size(); i++)
		{
			WhistAnnounce announce = announces.get(i);
			
			for (int j = i+1; j < announces.size(); j++)
			{
				WhistAnnounce other = announces.get(j);
				if (other.equals(announce))
				{
					announces.remove(j);
					j--;
				}
			}
		}
	}
}
