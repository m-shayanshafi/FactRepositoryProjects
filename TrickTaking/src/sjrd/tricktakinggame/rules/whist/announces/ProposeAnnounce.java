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

import static sjrd.tricktakinggame.rules.whist.WhistSuitComparator.*;

/**
 * @author sjrd
 */
public class ProposeAnnounce extends WhistAnnounce
{
	/**
	 * Atout
	 */
	private Suit trump;
	
	/**
	 * Indique s'il s'agit d'une proposition maintenue
	 */
	boolean maintained;
	
	/**
	 * Crée l'annonce
	 * @param aPlayer Joueur qui fait l'annonce
	 * @param aID ID de l'annonce
	 * @param aName Nom de l'annonce
	 * @param aTrump Atout
	 * @param aMaintained <tt>true</tt> si proposition maintenue, <tt>false</tt>
	 *        sinon
	 */
	public ProposeAnnounce(Player aPlayer, String aID, String aName,
		Suit aTrump, boolean aMaintained)
	{
		super(aPlayer, aID, aName);
		
		trump = aTrump;
		maintained = aMaintained;
	}
	
	/**
	 * Crée l'annonce
	 * @param aPlayer Joueur qui fait l'annonce
	 * @param aTrump Atout
	 * @param aMaintained <tt>true</tt> si proposition maintenue, <tt>false</tt>
	 *        sinon
	 */
	public ProposeAnnounce(Player aPlayer, Suit aTrump, boolean aMaintained)
	{
		this(aPlayer, makeID(aTrump, aMaintained),
			makeName(aTrump, aMaintained), aTrump, aMaintained);
	}
	
	/**
	 * Construit l'ID d'une annonce Proposition
	 * @param trump Atout
	 * @param maintained <tt>true</tt> pour une proposition maintenue
	 * @return ID de l'annonce
	 */
	private static String makeID(Suit trump, boolean maintained)
	{
		String result = String.format("Propose-%s", trump.name());
		if (maintained)
			result += "-Maintained";
		return result;
	}
	
	/**
	 * Construit le nom d'une annonce Proposition
	 * @param trump Atout
	 * @param maintained <tt>true</tt> pour une proposition maintenue
	 * @return Nom de l'annonce
	 */
	private static String makeName(Suit trump, boolean maintained)
	{
		if (maintained)
			return "Maintiens " + trump.getName();
		else
			return "Propose " + trump.getName();
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
	 * Indique si c'est une proposition maintenue
	 * @return <tt>true</tt> si la proposition est maintenue, <tt>false</tt>
	 *         sinon
	 */
	public boolean isMaintained()
	{
		return maintained;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getOrderValue()
	{
		return AnnounceValues.Propose;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canOverride(ContractAnnounce bestAnnounce)
	{
		return bestAnnounce.getOrderValue() <= AnnounceValues.PackMax;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other)
	{
		if (!super.equals(other))
			return false;
		
		return getTrump() == ((ProposeAnnounce) other).getTrump();
	}

	/**
	 * Construit toutes les annonces de proposition possibles
	 * @param auction Enchères
	 * @param player Joueur qui doit faire l'annonce
	 * @param announces Liste des annonces
	 */
	public static void makeProposeAnnounces(WhistAuction auction, Player player,
		List<WhistAnnounce> announces)
	{
		if (auction.getBestAnnounce().getOrderValue() > AnnounceValues.PackMax)
			return;
		
		boolean[] available = new boolean[4];
		Arrays.fill(available, true);
		
		for (int i = 1; i < 4; i++)
		{
			WhistAnnounce announce = auction.getLastAnnounce(
				player.nextPlayer(i));

			// Eliminer les atouts qui ont déjà une proposition ou un emballage
			if (announce instanceof ProposeAnnounce)
			{
				Suit trump = ((ProposeAnnounce) announce).getTrump();
				available[getSuitOrderValue(trump)] = false;
			}
			else if (announce instanceof PackAnnounce)
			{
				Suit trump = ((PackAnnounce) announce).getTrump();
				available[getSuitOrderValue(trump)] = false;
			}
		}

		// Créer les annonces pour les atouts autorisés
		Suit[] orderedSuits = getOrderedSuits();
		for (int i = 0; i < 4; i++)
		{
			if (available[i])
			{
				Suit trump = orderedSuits[i];
				announces.add(new ProposeAnnounce(player, trump, false));
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
		// Si c'est une proposition maintenue, on ne peut plus faire qu'emballer
		if (isMaintained())
		{
			PackAnnounce.makePackAnnounces(auction, player, announces);
			return;
		}
		
		// Sinon, on peut tout faire
		announces.add(new ProposeAnnounce(player, getTrump(), true));
		PackAnnounce.makePackAnnounces(auction, player, announces);
		VarSoloAnnounce.makeVarSoloAnnounces(auction, player, announces);
		MiseryAnnounce.makeMiseryAnnounces(auction, player, announces);
		ContractSoloAnnounce.makeContractSoloAnnounces(auction, player,
			announces);
	}
}
