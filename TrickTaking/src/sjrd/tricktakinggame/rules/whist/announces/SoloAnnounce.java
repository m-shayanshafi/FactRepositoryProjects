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

/**
 * Annonce Solo
 * @author sjrd
 */
public abstract class SoloAnnounce extends ContractAnnounce
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
	 * Indique si le contrat est sur misère
	 */
	private boolean onMisery;
	
	/**
	 * Crée l'annonce
	 * @param aPlayer Joueur qui fait l'annonce
	 * @param aID ID de l'annonce
	 * @param aName Nom de l'annonce
	 * @param aTrump Atout
	 * @param aTrickCount Nombre de plis annoncés
	 * @param aOnMisery <tt>true</tt> pour une annonce sur misère
	 */
	public SoloAnnounce(Player aPlayer, String aID, String aName,
		Suit aTrump, int aTrickCount, boolean aOnMisery)
	{
		super(aPlayer, aID, aName);
		
		trump = aTrump;
		trickCount = aTrickCount;
		onMisery = aOnMisery;
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
	 * Indique si le contrat est sur misère
	 * @return <tt>true</tt> si le contrat est sur misère, <tt>false</tt> sinon
	 */
	public boolean isOnMisery()
	{
		return onMisery;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeAvailableAnnounces(WhistAuction auction, Player player,
		List<WhistAnnounce> announces)
	{
		if (auction.getLastAnnounce(player) instanceof SoloAnnounce)
		{
			SoloAnnounce previous =
				(SoloAnnounce) auction.getLastAnnounce(player);
			int trickCount = previous.getTrickCount()+1;
			Suit trump = previous.getTrump();
			
			// Annonces VarSolo
			for (; trickCount <= 8; trickCount++)
			{
				if (trickCount == 10)
					trickCount = 12;
				
				announces.add(new VarSoloAnnounce(player, trump, trickCount,
					false));
				if (trickCount >= 9)
					announces.add(new VarSoloAnnounce(player, trump, trickCount,
						true));
			}
			
			// Annonces ContractSolo
			for (; trickCount <= 13; trickCount++)
			{
				if (trickCount == 10)
					trickCount = 12;
				
				announces.add(new ContractSoloAnnounce(player, trump,
					trickCount, false, false));
				announces.add(new ContractSoloAnnounce(player, trump,
					trickCount, false, true));
			}
		}
		
		VarSoloAnnounce.makeVarSoloAnnounces(auction, player, announces);
		MiseryAnnounce.makeMiseryAnnounces(auction, player, announces);
		ContractSoloAnnounce.makeContractSoloAnnounces(auction, player,
			announces);
	}
}
