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
 * Annonce Solo variable
 * @author sjrd
 */
public class VarSoloAnnounce extends SoloAnnounce
{
	/**
	 * Crée l'annonce
	 * @param aPlayer Joueur qui fait l'annonce
	 * @param aID ID de l'annonce
	 * @param aName Nom de l'annonce
	 * @param aTrump Atout
	 * @param aTrickCount Nombre de plis annoncés
	 * @param aOnMisery <tt>true</tt> pour une annonce sur misère
	 */
	public VarSoloAnnounce(Player aPlayer, String aID, String aName,
	    Suit aTrump, int aTrickCount, boolean aOnMisery)
	{
		super(aPlayer, aID, aName, aTrump, aTrickCount, aOnMisery);
	}

	/**
	 * Crée l'annonce
	 * @param aPlayer Joueur qui fait l'annonce
	 * @param aTrump Atout
	 * @param aTrickCount Nombre de plis annoncés
	 * @param aOnTable <tt>true</tt> pour une annonce sur table
	 */
	public VarSoloAnnounce(Player aPlayer, Suit aTrump, int aTrickCount,
	    boolean aOnMisery)
	{
		this(aPlayer, makeID(aTrump, aTrickCount, aOnMisery),
			makeName(aTrump, aTrickCount, aOnMisery),
			aTrump, aTrickCount, aOnMisery);
	}
	
	/**
	 * Construit l'ID d'une annonce Solo
	 * @param trump Atout
	 * @param trickCount Nombre de plis annoncés
	 * @param onTable <tt>true</tt> pour une annonce sur table
	 * @return ID de l'annonce
	 */
	private static String makeID(Suit trump, int trickCount, boolean onMisery)
	{
		String result = String.format("Solo-%s-%d", trump.name(), trickCount);
		if (onMisery)
			result += "-OnMisery";
		return result;
	}
	
	/**
	 * Construit le nom d'une annonce Solo
	 * @param trump Atout
	 * @param trickCount Nombre de plis annoncés
	 * @param onTable <tt>true</tt> pour une annonce sur table
	 * @return Nom de l'annonce
	 */
	private static String makeName(Suit trump, int trickCount, boolean onMisery)
	{
		String result = trickCount + " " + trump.getName();
		if (onMisery)
			result += " sur misère";
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getOrderValue()
	{
		return AnnounceValues.VarSoloBase +
			AnnounceValues.TrickCountCoeff * (getTrickCount()-6) +
			getSuitOrderValue(getTrump());
	}
	
	/**
	 * Ajoute toutes les annonces VarSolo possibles à une liste d'annonce
	 * <p>
	 * Les paramètres <tt>fromTrickCount</tt> et <tt>fromTrump</tt> indiquent la
	 * première annonce valide (la plus faible). Ils peuvent valoir
	 * respectivement 0 et <tt>null</tt>, auquel cas toutes les annonces VarSolo
	 * possibles sont ajoutées.
	 * </p>
	 * @param auction Enchères
	 * @param player Joueur qui doit faire l'annonce
	 * @param announces Liste des annonces
	 * @param fromTrickCount Nombre de plis minimum
	 * @param fromTrump Atout minimum
	 */
	public static void makeVarSoloAnnounces(WhistAuction auction, Player player,
		List<WhistAnnounce> announces, int fromTrickCount, Suit fromTrump)
	{
		int trickCount = fromTrickCount < 6 ? 6 : fromTrickCount;
		int trumpIndex = getSuitOrderValue(fromTrump);
		if (trumpIndex < 0)
			trumpIndex = 0;
		
		Suit[] orderedSuits = getOrderedSuits();
		
		while (trickCount <= 8)
		{
			// Ajouter l'annonce
			Suit trump = orderedSuits[trumpIndex];
			announces.add(new VarSoloAnnounce(player, trump, trickCount,
				false));
			
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
	 * Ajoute toutes les annonces VarSolo possibles à une liste d'annonce
	 * @param auction Enchères
	 * @param player Joueur qui doit faire l'annonce
	 * @param announces Liste des annonces
	 */
	public static void makeVarSoloAnnounces(WhistAuction auction, Player player,
		List<WhistAnnounce> announces)
	{
		makeVarSoloAnnounces(auction, player, announces, 0, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WhistContract makeContract(WhistAuction auction, WhistRules rules)
	{
		return new SoloContract(rules, makeContractName(), getTrump(),
			getTrickCount(), false, getPlayer());
	}
}
