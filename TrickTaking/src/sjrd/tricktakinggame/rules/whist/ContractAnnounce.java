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

import sjrd.tricktakinggame.game.*;
import sjrd.tricktakinggame.remotable.*;

/**
 * Annonce qui peut résulter en un contrat
 * @author sjrd
 */
public abstract class ContractAnnounce extends WhistAnnounce
{
	/**
	 * Crée l'annonce
	 * @param aPlayer Joueur qui fait l'annonce
	 * @param aID ID de l'annonce
	 * @param aName Nom de l'annonce
	 */
	public ContractAnnounce(Player aPlayer, String aID, String aName)
	{
		super(aPlayer, aID, aName);
	}
	
	/**
	 * Nom du contrat simple, sans les noms des joueurs
	 * @return Nom du contratt simple
	 */
	protected String getContractName()
	{
		return getName();
	}
	
	/**
	 * Construit le nom du contrat correspondant
	 * @return Nom du contrat correspondant
	 * @see #getContractName()
	 */
	protected String makeContractName()
	{
		return String.format("%s : %s", getPlayer().getName(),
			getContractName());
	}
	
	/**
	 * Teste si cette annonce peut être faite au-dessus d'une autre
	 * <p>
	 * Pour toutes les annonces à contrat, cela est possible si et seulement si
	 * la valeur d'ordre de cette annonce est strictement supérieure à celle de
	 * la meilleure annonce.
	 * </p>
	 * @param bestAnnounce Meilleure annonce jusque là, annonce à battre
	 * @return <tt>true</tt> si cette annonce peut être faite, <tt>false</tt>
	 *         sinon
	 */
	@Override
	public final boolean canOverride(ContractAnnounce bestAnnounce)
	{
		return super.canOverride(bestAnnounce);
	}
	
	/**
	 * Construit les contres spéciaux disponibles sur cette annonce
	 * <p>
	 * Cette méthode peut ajouter des annonces possibles à la liste
	 * <tt>announces</tt> passée en paramètre.
	 * </p>
	 * <p>
	 * Elle ne peut être appelée que pour un joueur qui ne fait pas partie de
	 * cette annonce à contrat (comme annonçant ou comme partenaire).
	 * </p>
	 * @param auction Enchère
	 * @param player Joueur
	 * @param announces Liste des annonces
	 */
	public void makeSpecialCounters(WhistAuction auction,
		Player player, List<WhistAnnounce> announces)
	{
	}
	
	/**
	 * Construit le contrat associé
	 * @param auction Enchères
	 * @param rules Règles associées
	 * @return Contrat construit
	 */
	public abstract WhistContract makeContract(WhistAuction auction,
		WhistRules rules) throws CardGameException;
}
