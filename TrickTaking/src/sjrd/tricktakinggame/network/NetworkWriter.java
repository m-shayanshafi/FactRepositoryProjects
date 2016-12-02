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
package sjrd.tricktakinggame.network;

import java.util.*;
import java.io.*;

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.remotable.*;

/**
 * Ecrivain du réseau
 * @author sjrd
 */
public class NetworkWriter extends PrintWriter
{
	/**
	 * Crée un écrivain du réseau
	 * @param out Buffer de sortie
	 */
	public NetworkWriter(OutputStream out)
	{
		super(new OutputStreamWriter(out, NetworkInfo.charset), true);
	}

	/**
	 * Ecrit une réponse
	 * @param response Réponse à écrire
	 */
	public void writeResponse(Response response)
	{
		println(response);
	}

	/**
	 * Ecrit une réponse
	 * @param code Code de la réponse
	 * @param msg Message de la réponse
	 */
	public void writeResponse(ResponseCode code, String msg)
	{
		writeResponse(new Response(code, msg));
	}

	/**
	 * Ecrit une réponse avec le message par défaut
	 * @param code Code de la réponse
	 */
	public void writeResponse(ResponseCode code)
	{
		writeResponse(new Response(code));
	}
	
	/**
	 * Ecrit une commande
	 * @param command Nom de la commande
	 * @param params Paramètres de la commande
	 */
	public void writeCommand(String command, String ... params)
	{
		String line = command;
		for (String param: params)
			line += "\t" + param;
		println(line);
	}
	
	/**
	 * Ecrit une commande
	 * @param command Nom de la commande
	 * @param params Paramètres de la commande
	 */
	public void writeCommand(String command, Object ... params)
	{
		String[] strParams = new String[params.length];
		for (int i = 0; i < params.length; i++)
			strParams[i] = params[i].toString();
		writeCommand(command, strParams);
	}
	
	/**
	 * Ecrit une chaîne de caractères
	 * <p>
	 * Cette méthode est inclue pour complétude ; elle est équivalente à
	 * <tt>println(String)</tt>.
	 * </p>
	 * @param string Chaîne à écrire
	 * @see #println(String)
	 */
	public void writeString(String string)
	{
		println(string);
	}
	
	/**
	 * Ecrit une liste de chaînes de caractères
	 * @param strings Liste de chaînes à écrire
	 */
	public void writeStrings(String[] strings)
	{
		println(strings.length);
		for (String string: strings)
			println(string);
	}
	
	/**
	 * Ecrit une liste de chaînes de caractères
	 * @param strings Liste de chaînes à écrire
	 */
	public void writeStrings(Collection<String> strings)
	{
		println(strings.size());
		for (String string: strings)
			println(string);
	}
	
	/**
	 * Ecrit une chaîne "nullable" (qui peut être <tt>null</tt>)
	 * @param string Chaîne à écrire
	 */
	public void writeNullableString(String string)
	{
		if (string == null)
			println(NetworkInfo.nullString);
		else
			println(string);
	}
	
	/**
	 * Ecrit une liste de chaînes de caractères nullables
	 * @param cards Liste de chaînes à écrire
	 */
	public void writeNullableStrings(String[] strings)
	{
		println(strings.length);
		for (String string: strings)
			writeNullableString(string);
	}
	
	/**
	 * Ecrit une liste de chaînes de caractères nullables
	 * @param cards Liste de chaînes à écrire
	 */
	public void writeNullableStrings(Collection<String> strings)
	{
		println(strings.size());
		for (String string: strings)
			writeNullableString(string);
	}
	
	/**
	 * Ecrit un entier
	 * <p>
	 * Cette méthode est inclue pour complétude ; elle est équivalente à
	 * <tt>println(int)</tt>.
	 * </p>
	 * @param integer Entier à écrire
	 * @see #println(int)
	 */
	public void writeInteger(int integer)
	{
		println(integer);
	}
	
	/**
	 * Ecrit une liste d'entiers
	 * @param integers Liste d'entiers à écrire
	 */
	public void writeIntegers(int[] integers)
	{
		println(integers.length);
		for (int integer: integers)
			println(integer);
	}
	
	/**
	 * Ecrit une liste d'entiers
	 * @param integers Liste d'entiers à écrire
	 */
	public void writeIntegers(Collection<Integer> integers)
	{
		println(integers.size());
		for (int integer: integers)
			println(integer);
	}
	
	/**
	 * Ecrit un booléen
	 * @param bool Booléen à écrire
	 */
	public void writeBoolean(boolean bool)
	{
		println(bool ? 1 : 0);
	}
	
	/**
	 * Ecrit une liste de booléens
	 * @param bools Liste de booléens à écrire
	 */
	public void writeBooleans(boolean[] bools)
	{
		println(bools.length);
		for (boolean bool: bools)
			println(bool ? 1 : 0);
	}
	
	/**
	 * Ecrit une liste de booléens
	 * @param bools Liste de booléens à écrire
	 */
	public void writeBooleans(Collection<Boolean> bools)
	{
		println(bools.size());
		for (boolean bool: bools)
			println(bool ? 1 : 0);
	}
	
	/**
	 * Ecrit une couleur de carte
	 * @param suit Couleur à écrire
	 */
	public void writeSuit(Suit suit)
	{
		if (suit == null)
			println(NetworkInfo.nullString);
		else
			println(suit.name());
	}
	
	/**
	 * Ecrit une liste de couleurs de carte
	 * @param cards Liste de couleurs à écrire
	 */
	public void writeSuits(Suit[] suits)
	{
		println(suits.length);
		for (Suit suit: suits)
			writeSuit(suit);
	}
	
	/**
	 * Ecrit une liste de cartes
	 * @param cards Liste de cartes à écrire
	 */
	public void writeSuits(Collection<? extends Suit> suits)
	{
		println(suits.size());
		for (Suit suit: suits)
			writeSuit(suit);
	}
	
	/**
	 * Ecrit une carte
	 * @param card Carte à écrire
	 */
	public void writeCard(Card card)
	{
		if (card == null)
			println(NetworkInfo.nullString);
		else
			println(card.getID());
	}
	
	/**
	 * Ecrit une liste de cartes
	 * @param cards Liste de cartes à écrire
	 */
	public void writeCards(Card[] cards)
	{
		println(cards.length);
		for (Card card: cards)
			writeCard(card);
	}
	
	/**
	 * Ecrit une liste de cartes
	 * @param cards Liste de cartes à écrire
	 */
	public void writeCards(Collection<? extends Card> cards)
	{
		println(cards.size());
		for (Card card: cards)
			writeCard(card);
	}

	/**
	 * Ecrit une annonce
	 * @param announce Annonce à écrire
	 */
	public void writeAnnounce(Announce announce)
	{
		println(announce.getPlayer().getPosition() + "\t" + announce.getID() +
			"\t" + announce.getName());
	}
	
	/**
	 * Ecrit une liste d'annonces
	 * @param announces Liste d'annonces à écrire
	 */
	public void writeAnnounces(Announce[] announces)
	{
		println(announces.length);
		for (Announce announce: announces)
			writeAnnounce(announce);
	}
	
	/**
	 * Ecrit une liste d'annonces
	 * @param announces Liste d'annonces à écrire
	 */
	public void writeAnnounces(Collection<? extends Announce> announces)
	{
		println(announces.size());
		for (Announce announce: announces)
			writeAnnounce(announce);
	}
}
