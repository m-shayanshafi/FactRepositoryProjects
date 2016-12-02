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
 * @author sjrd
 */
public class ContractSoloAnnounce extends SoloAnnounce
{	
	/**
	 * Indique si le contrat est sur table
	 */
	private boolean onTable;

	/**
	 * Crée l'annonce
	 * @param aPlayer Joueur qui fait l'annonce
	 * @param aID ID de l'annonce
	 * @param aName Nom de l'annonce
	 * @param aTrump Atout
	 * @param aTrickCount Nombre de plis annoncés
	 * @param aOnMisery <tt>true</tt> pour une annonce sur misère
	 * @param aOnTable <tt>true</tt> pour une annonce sur table
	 */
	public ContractSoloAnnounce(Player aPlayer, String aID, String aName,
	    Suit aTrump, int aTrickCount, boolean aOnTable, boolean aOnMisery)
	{
		super(aPlayer, aID, aName, aTrump, aTrickCount, aOnMisery);

		onTable = aOnTable;
	}

	/**
	 * Crée l'annonce
	 * @param aPlayer Joueur qui fait l'annonce
	 * @param aTrump Atout
	 * @param aTrickCount Nombre de plis annoncés
	 * @param aOnMisery <tt>true</tt> pour une annonce sur misère
	 * @param aOnTable <tt>true</tt> pour une annonce sur table
	 */
	public ContractSoloAnnounce(Player aPlayer, Suit aTrump, int aTrickCount,
	    boolean aOnMisery, boolean aOnTable)
	{
		this(aPlayer, makeID(aTrump, aTrickCount, aOnMisery, aOnTable),
			makeName(aTrump, aTrickCount, aOnMisery, aOnTable), aTrump,
			aTrickCount, aOnTable, aOnMisery);
	}
	
	/**
	 * Construit l'ID d'une annonce Solo
	 * @param trump Atout
	 * @param trickCount Nombre de plis annoncés
	 * @param onMisery <tt>true</tt> pour une annonce sur misère
	 * @param onTable <tt>true</tt> pour une annonce sur table
	 * @return ID de l'annonce
	 */
	private static String makeID(Suit trump, int trickCount, boolean onMisery,
		boolean onTable)
	{
		String result = String.format("ContractSolo-%s-%d", trump.name(),
			trickCount);
		if (onMisery)
			result += "-OnMisery";
		if (onTable)
			result += "-OnTable";
		return result;
	}
	
	/**
	 * Construit le nom d'une annonce Solo
	 * @param trump Atout
	 * @param trickCount Nombre de plis annoncés
	 * @param onMisery <tt>true</tt> pour une annonce sur misère
	 * @param onTable <tt>true</tt> pour une annonce sur table
	 * @return Nom de l'annonce
	 */
	private static String makeName(Suit trump, int trickCount, boolean onMisery,
		boolean onTable)
	{
		String result;
		
		switch (trickCount)
		{
			case 9:
				result = "Abondance";
				break;
			case 12:
				result = "Petit Shelem";
				break;
			case 13:
				result = "Grand Shelem";
				break;
			default:
				result = String.valueOf(trickCount);
		}
		
		result += " " + trump.getName();
		if (onMisery)
			result += " sur misère";
		if (onTable)
			result += " sur table";
		
		return result;
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
		int suitValue = getSuitOrderValue(getTrump());
		
		switch (getTrickCount())
		{
			case 9:
				return suitValue + AnnounceValues.Abondance;
			case 11:
				return suitValue + AnnounceValues.ElevenOnMisery;
			case 12:
				return suitValue + AnnounceValues.SmallShelem;
			case 13:
				return suitValue + AnnounceValues.GrandShelem;
			default:
				return 0;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other)
	{
		if (!super.equals(other))
			return false;
		
		ContractSoloAnnounce otherAnnounce = (ContractSoloAnnounce) other;
		
		return isOnTable() == otherAnnounce.isOnTable();
	}
	
	/**
	 * Ajoute toutes les annonces ContractSolo possibles à une liste d'annonce
	 * <p>
	 * Les paramètres <tt>fromTrickCount</tt> et <tt>fromTrump</tt> indiquent la
	 * première annonce valide (la plus faible). Ils peuvent valoir
	 * respectivement 0 et <tt>null</tt>, auquel cas toutes les annonces
	 * ContractSolo possibles sont ajoutées.
	 * </p>
	 * @param auction Enchères
	 * @param player Joueur qui doit faire l'annonce
	 * @param announces Liste des annonces
	 * @param fromTrickCount Nombre de plis minimum
	 * @param fromTrump Atout minimum
	 */
	public static void makeContractSoloAnnounces(WhistAuction auction,
		Player player, List<WhistAnnounce> announces, int fromTrickCount,
		Suit fromTrump)
	{
		int trickCount = fromTrickCount < 9 ? 9 : fromTrickCount;
		int trumpIndex = getSuitOrderValue(fromTrump);
		if (trumpIndex < 0)
			trumpIndex = 0;
		
		Suit[] orderedSuits = getOrderedSuits();
		
		while (trickCount <= 13)
		{
			// Sauter les 10 et 11
			if (trickCount == 10)
				trickCount = 12;

			// Ajouter les annonces
			Suit trump = orderedSuits[trumpIndex];
			
			announces.add(new ContractSoloAnnounce(player, trump, trickCount,
				false, false));
			announces.add(new ContractSoloAnnounce(player, trump, trickCount,
				false, true));
			
			// Passer au tour suivant
			trumpIndex++;
			if (trumpIndex >= 4)
			{
				trumpIndex = 0;
				trickCount++;
			}
		}
	}
	
	/**
	 * Ajoute toutes les annonces ContractSolo possibles à une liste d'annonce
	 * @param auction Enchères
	 * @param player Joueur qui doit faire l'annonce
	 * @param announces Liste des annonces
	 */
	public static void makeContractSoloAnnounces(WhistAuction auction,
		Player player, List<WhistAnnounce> announces)
	{
		makeContractSoloAnnounces(auction, player, announces, 0, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WhistContract makeContract(WhistAuction auction, WhistRules rules)
	{
		return new SoloContract(rules, makeContractName(), getTrump(),
			getTrickCount(), isOnTable(), getPlayer());
	}
}
