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
 * Annonce de misère (petite ou grande)
 * @author sjrd
 */
public class MiseryAnnounce extends ContractAnnounce
{
	/**
	 * Indique si c'est une petite misère
	 */
	private boolean small;
	
	/**
	 * Indique si le contrat est sur table
	 */
	private boolean onTable;

	/**
	 * Crée l'annonce
	 * @param aPlayer Joueur qui fait l'annonce
	 * @param aID ID de l'annonce
	 * @param aName Nom de l'annonce
	 * @param aSmall <tt>true</tt> si c'est une petite misère
	 * @param aOnTable <tt>true</tt> pour une annonce sur table
	 */
	public MiseryAnnounce(Player aPlayer, String aID, String aName,
		boolean aSmall, boolean aOnTable)
	{
		super(aPlayer, aID, aName);
		
		small = aSmall;
		onTable = aOnTable;
	}

	/**
	 * Crée l'annonce
	 * @param aPlayer Joueur qui fait l'annonce
	 * @param aSmall <tt>true</tt> si c'est une petite misère
	 * @param aOnTable <tt>true</tt> pour une annonce sur table
	 */
	public MiseryAnnounce(Player aPlayer, boolean aSmall, boolean aOnTable)
	{
		this(aPlayer, makeID(aSmall, aOnTable), makeName(aSmall, aOnTable),
			aSmall, aOnTable);
	}
	
	/**
	 * Construit l'ID d'une annonce Misère
	 * @param small <tt>true</tt> si c'est une petite misère
	 * @param onTable <tt>true</tt> pour une annonce sur table
	 * @return ID de l'annonce
	 */
	private static String makeID(boolean small, boolean onTable)
	{
		String result = (small ? "SmallMisery" : "GrandMisery");
		if (onTable)
			result += "-OnTable";
		return result;
	}
	
	/**
	 * Construit le nom d'une annonce Misère
	 * @param small <tt>true</tt> si c'est une petite misère
	 * @param onTable <tt>true</tt> pour une annonce sur table
	 * @return Nom de l'annonce
	 */
	private static String makeName(boolean small, boolean onTable)
	{
		String result = (small ? "Petite misère" : "Grande misère");
		if (onTable)
			result += " sur table";
		
		return result;
	}
	
	/**
	 * Indique si c'est une petite misère
	 * @return <tt>true</tt> si c'est une petite misère, <tt>false</tt> sinon
	 */
	public boolean isSmall()
	{
		return small;
	}
	
	/**
	 * Indique si le contrat est sur table
	 * @return <tt>true</tt> si le contrat est sur table, <tt>false</tt> sinon
	 */
	public boolean isOnTable()
	{
		return onTable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getOrderValue()
	{
		return small ? AnnounceValues.SmallMisery : AnnounceValues.GrandMisery;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other)
	{
		if (!super.equals(other))
			return false;
		
		MiseryAnnounce otherAnnounce = (MiseryAnnounce) other;
		
		return isOnTable() == otherAnnounce.isOnTable();
	}
	
	/**
	 * Ajoute toutes les annonces Misère possibles à une liste d'annonce
	 * @param auction Enchères
	 * @param player Joueur qui doit faire l'annonce
	 * @param announces Liste des annonces
	 */
	public static void makeMiseryAnnounces(WhistAuction auction, Player player,
		List<WhistAnnounce> announces)
	{
		ContractAnnounce bestAnnounce = auction.getBestAnnounce();

		if (bestAnnounce.getOrderValue() < AnnounceValues.GrandMisery)
		{
			if (bestAnnounce.getOrderValue() < AnnounceValues.SmallMisery)
				announces.add(new MiseryAnnounce(player, true, false));

			announces.add(new MiseryAnnounce(player, false, false));
			announces.add(new MiseryAnnounce(player, false, true));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeAvailableAnnounces(WhistAuction auction, Player player,
	    List<WhistAnnounce> announces)
	{
		makeMiseryAnnounces(auction, player, announces);
		VarSoloAnnounce.makeVarSoloAnnounces(auction, player, announces,
			8, null);
		ContractSoloAnnounce.makeContractSoloAnnounces(auction, player,
			announces);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeSpecialCounters(WhistAuction auction,
		Player player, List<WhistAnnounce> announces)
	{
		super.makeSpecialCounters(auction, player, announces);
		
		Suit[] orderedSuits = getOrderedSuits();
		
		if (small)
		{
			for (int i = 0; i < 4; i++)
				announces.add(new VarSoloAnnounce(player, orderedSuits[i], 8,
					true));
			
			announces.add(new DuoMiseryAnnounce(player, getPlayer()));
		}
		else
		{
			for (int i = 0; i < 4; i++)
			{
				announces.add(new ContractSoloAnnounce(player, orderedSuits[i],
					11, true, false));
				announces.add(new ContractSoloAnnounce(player, orderedSuits[i],
					11, true, true));
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public WhistContract makeContract(WhistAuction auction, WhistRules rules)
	{
		return new MiseryContract(rules, makeContractName(), small, onTable,
			getPlayer());
	}
}
