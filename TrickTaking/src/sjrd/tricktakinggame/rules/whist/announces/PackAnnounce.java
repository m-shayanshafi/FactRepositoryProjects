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
import sjrd.tricktakinggame.rules.whist.*;
import sjrd.tricktakinggame.rules.whist.contracts.*;

import static sjrd.tricktakinggame.rules.whist.WhistSuitComparator.*;

/**
 * Annonce d'emballage
 * @author sjrd
 */
public class PackAnnounce extends DuoContractAnnounce
{
	/**
	 * Atout
	 */
	private Suit trump;
	
	/**
	 * Nombre de plis annoncés
	 */
	private int trickCount;

	/**
	 * Crée l'annonce
	 * @param aPlayer Joueur fait l'annonce
	 * @param aPartner Partenaire
	 * @param aID ID de l'annonce
	 * @param aName Nom de l'annonce
	 * @param aTrump Atout
	 * @param aTrickCount Nombre de plis
	 */
	public PackAnnounce(Player aPlayer, Player aPartner, String aID,
		String aName, Suit aTrump, int aTrickCount)
	{
		super(aPlayer, aPartner, aID, aName);
		
		trump = aTrump;
		trickCount = aTrickCount;
	}

	/**
	 * Crée l'annonce
	 * @param aPlayer Joueur fait l'annonce
	 * @param aPartner Partenaire
	 * @param aTrump Atout
	 * @param aTrickCount Nombre de plis
	 */
	public PackAnnounce(Player aPlayer, Player aPartner, Suit aTrump,
		int aTrickCount)
	{
		this(aPlayer, aPartner, makeID(aTrump, aTrickCount),
			makeName(aTrump, aTrickCount), aTrump, aTrickCount);
	}
	
	/**
	 * Construit l'ID d'une annonce Emballage
	 * @param trump Atout
	 * @param trickCount Nombre de plis annoncés
	 * @return ID de l'annonce
	 */
	private static String makeID(Suit trump, int trickCount)
	{
		return String.format("Pack-%s-%d", trump.name(), trickCount);
	}
	
	/**
	 * Construit le nom d'une annonce Emballage
	 * @param trump Atout
	 * @param trickCount Nombre de plis annoncés
	 * @return Nom de l'annonce
	 */
	private static String makeName(Suit trump, int trickCount)
	{
		return String.format("Emballe %s pour %d", trump.getName(), trickCount);
	}
	
	/**
	 * Atout
	 * @return Atout
	 */
	public Suit getTrump()
	{
		return trump;
	}
	
	/**
	 * Nombre de plis annoncés
	 * @return Nombre de plis annoncés
	 */
	public int getTrickCount()
	{
		return trickCount;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getOrderValue()
	{
		return AnnounceValues.PackBase +
			AnnounceValues.TrickCountCoeff * (trickCount-8) +
			getSuitOrderValue(getTrump());
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
	public void replaced(WhistAuction auction, Player player,
		WhistAnnounce announce)
	{
		super.replaced(auction, player, announce);
		
		// Libérer le partenaire de son engagement si ce n'est plus un emballage
		if (!(announce instanceof PackAnnounce) && (player == getPlayer()))
		{
			Player partner = getPartner();
			
			if (auction.getLastAnnounce(partner) instanceof PackAnnounce)
				auction.setLastAnnounce(partner,
					new NoAnnounce(partner, false));
		}
	}
	
	/**
	 * Construit toutes les annonces d'emballage possibles
	 * @param auction Enchères
	 * @param player Joueur qui doit faire l'annonce
	 * @param announces Liste des annonces
	 */
	public static void makePackAnnounces(WhistAuction auction, Player player,
		List<WhistAnnounce> announces)
	{
		for (int i = 1; i < 4; i++)
		{
			Player partner = player.nextPlayer(i);
			WhistAnnounce lastAnnounce = auction.getLastAnnounce(partner);
			
			if (lastAnnounce instanceof ProposeAnnounce)
			{
				ProposeAnnounce proposeAnnounce =
					(ProposeAnnounce) lastAnnounce;
				
				for (int trickCount = 8; trickCount <= 13; trickCount++)
					announces.add(new PackAnnounce(player, partner,
						proposeAnnounce.getTrump(), trickCount));
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeAvailableAnnounces(WhistAuction auction, Player player,
	    List<WhistAnnounce> announces)
	{
		if (auction.getBestAnnounce() instanceof PackAnnounce)
		{
			// Monter contre un autre emballage
			
			// Seul celui qui a emballé a droit de parole
			if (player != getPlayer())
			{
				announces.clear();
				return;
			}
			
			for (int trickCount = getTrickCount()+1; trickCount <= 13;
				trickCount++)
			{
				announces.add(new PackAnnounce(player, getPartner(), getTrump(),
					trickCount));
			}
		}
		else
		{
			// Emballage cassé, on peut tout faire à partir du solo
			VarSoloAnnounce.makeVarSoloAnnounces(auction, player, announces);
			MiseryAnnounce.makeMiseryAnnounces(auction, player, announces);
			ContractSoloAnnounce.makeContractSoloAnnounces(auction, player,
				announces);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getContractName()
	{
		return String.format("Emballage %d %s", trickCount, trump.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WhistContract makeContract(WhistAuction auction, WhistRules rules)
	{
		return new PackContract(rules, makeContractName(), getTrump(),
			getTrickCount(), getPlayer(), getPartner());
	}
}
