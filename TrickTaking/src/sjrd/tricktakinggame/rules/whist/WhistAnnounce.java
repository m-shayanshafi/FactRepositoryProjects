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
 * Annonce d'une partie de Whist
 * @author sjrd
 */
public abstract class WhistAnnounce extends Announce
{
	/**
	 * Joueur qui fait l'annonce
	 */
	private Player player;
	
	/**
	 * Crée l'annonce
	 * @param aPlayer Joueur qui fait l'annonce
	 * @param aID ID de l'annonce
	 * @param aName Nom de l'annonce
	 */
	public WhistAnnounce(Player aPlayer, String aID, String aName)
	{
		super(aPlayer, aID, aName);
		
		player = aPlayer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Player getPlayer()
	{
		return player;
	}
	
	/**
	 * Joueur partenaire pour cette annonce
	 * @return Joueur partenaire pour cette annonce (peut être <tt>null</tt>)
	 */
	public Player getPartner()
	{
		return null;
	}
	
	/**
	 * Valeur d'ordre
	 * @return Valeur d'ordre
	 */
	public abstract int getOrderValue();
	
	/**
	 * Teste si cette annonce peut être faite au-dessus d'une autre
	 * <p>
	 * Par défaut, c'est possible uniquement si la valeur d'ordre de cette
	 * annonce est strictement supérieure à celle de la meilleure annonce.
	 * </p>
	 * @param bestAnnounce Meilleure annonce jusque là, annonce à battre
	 * @return <tt>true</tt> si cette annonce peut être faite, <tt>false</tt>
	 *         sinon
	 * @see #getOrderValue()
	 */
	public boolean canOverride(ContractAnnounce bestAnnounce)
	{
		return getOrderValue() > bestAnnounce.getOrderValue();
	}
	
	/**
	 * Teste si deux annonces sont équivalentes
	 * <p>
	 * Dans <tt>WhistAnnounce</tt>, cette méthode teste si les classes sont
	 * égales, si les valeurs entières d'ordre sont égales, et si les joueurs
	 * déclarant et partenaire sont égaux.
	 * </p>
	 * <p>
	 * Vous devez surcharger cette méthode dans toutes les sous-classes de
	 * <tt>WhistAnnounce</tt> si l'égalité de valeur d'ordre ne suffit pas à
	 * garantir l'équivalence.
	 * </p>
	 * @param other Annonce avec laquelle comparer
	 * @return <tt>true</tt> si les annonces sont équivalentes, <tt>false</tt>
	 *         sinon
	 * @see #getPlayer()
	 * @see #getPartner()
	 * @see #getOrderValue()
	 */
	@Override
	public boolean equals(Object other)
	{
		if (other.getClass() != getClass())
			return false;
		
		WhistAnnounce otherAnnounce = (WhistAnnounce) other;
		
		return (getPlayer() == otherAnnounce.getPlayer()) &&
			(getPartner() == otherAnnounce.getPartner()) &&
			(getOrderValue() == otherAnnounce.getOrderValue());
	}
	
	/**
	 * Appelé lorsque cette annonce est sélectionnée par son joueur
	 * @param auction Enchères
	 */
	public void selected(WhistAuction auction)
	{
		auction.setLastAnnounce(getPlayer(), this);
	}
	
	/**
	 * Appelé lorsque cette annonce est remplacée par une autre pour un joueur
	 * @param auction Enchères
	 * @param player Joueur
	 * @param announce Annonce qui va remplacer celle-ci
	 */
	public void replaced(WhistAuction auction, Player player,
		WhistAnnounce announce)
	{
	}
	
	/**
	 * Construit les annonces disponibles après cette annonce
	 * <p>
	 * Cette méthode peut ajouter et/ou retirer des annonces possibles à la
	 * liste <tt>announces</tt> passée en paramètre.
	 * </p>
	 * @param auction Enchère
	 * @param player Joueur
	 * @param announces Liste des annonces
	 */
	public abstract void makeAvailableAnnounces(WhistAuction auction,
		Player player, List<WhistAnnounce> announces);
}
