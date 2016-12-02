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
package sjrd.tricktakinggame.rules.whist.announces;

import java.util.*;

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.game.*;
import sjrd.tricktakinggame.remotable.*;
import sjrd.tricktakinggame.rules.whist.*;
import sjrd.tricktakinggame.rules.whist.contracts.*;

import static sjrd.tricktakinggame.rules.whist.WhistSuitComparator.*;

/**
 * Annonce Bouche-trou
 * @author sjrd
 */
public class StopgapAnnounce extends DuoContractAnnounce
{
	/**
	 * Indique si c'est un trou royal
	 */
	private boolean royal;
	
	/**
	 * Carte qui doit boucher le trou
	 */
	private Card stopgapCard;

	/**
	 * Crée l'annonce
	 * @param aPlayer Joueur qui fait l'annonce
	 * @param aPartner Partenaire
	 * @param aID ID de l'annonce
	 * @param aName Nom de l'annonce
	 */
	public StopgapAnnounce(Player aPlayer, Player aPartner, String aID,
		String aName, boolean aRoyal, Card aStopgapCard)
	{
		super(aPlayer, aPartner, aID, aName);
		
		royal = aRoyal;
		stopgapCard = aStopgapCard;
	}

	/**
	 * Crée l'annonce
	 * @param aPlayer Joueur qui fait l'annonce
	 * @param aPartner Partenaire
	 */
	public StopgapAnnounce(Player aPlayer, Player aPartner, boolean aRoyal,
		Card aStopgapCard)
	{
		this(aPlayer, aPartner, "Stopgap", "Bouche-trou", aRoyal, aStopgapCard);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getOrderValue()
	{
		return AnnounceValues.Gap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void selected(WhistAuction auction)
	{
		super.selected(auction);
		auction.setLastAnnounce(getPartner(), this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeAvailableAnnounces(WhistAuction auction, Player player,
	    List<WhistAnnounce> announces)
	{
		ContractSoloAnnounce.makeContractSoloAnnounces(auction, player,
			announces);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WhistContract makeContract(WhistAuction auction, WhistRules rules)
		throws CardGameException
	{
		Player player = getPlayer();
		TrumpAnnounce[] announces = new TrumpAnnounce[4];
		Suit[] orderedSuits = getOrderedSuits();
		
		for (int i = 0; i < 4; i++)
			announces[i] = new TrumpAnnounce(player, orderedSuits[i]);
		
		TrumpAnnounce chosen = player.chooseAnnounce(announces);
		chosen.selected(auction);
		auction.addAnnounce(chosen);
		
		Suit trump = chosen.getTrump();
		int trickCount;
		
		if (trump == stopgapCard.getSuit())
			trickCount = 8;
		else
			trickCount = 9;
		
		String contractName = String.format("%s et %s : Trou",
			player.getName(), getPartner().getName());
		if (royal)
			contractName += " royal";
		contractName += String.format(" en %s (%d plis)", trump.getName(),
			trickCount);
		
		rules.getGame().setActivePlayer(player);
		
		return new PackContract(rules, contractName, trump, trickCount,
			player, getPartner(), true);
	}
}
