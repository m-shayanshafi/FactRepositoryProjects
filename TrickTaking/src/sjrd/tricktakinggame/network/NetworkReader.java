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
import java.nio.channels.*;

import sjrd.util.*;
import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.remotable.*;

import static sjrd.util.ListUtils.*;

/**
 * Lecteur du réseau
 * @author sjrd
 */
public class NetworkReader extends BufferedReader
{
	/**
	 * Crée un nouveau lecteur du réseau
	 * @param in Flux d'entrée
	 */
	public NetworkReader(InputStream in)
	{
		super(new InputStreamReader(in, NetworkInfo.charset));
	}
	
	/**
	 * Remplit un buffer de caractères en lisant depuis l'entrée
	 * @param buffer Buffer de destination
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 */
	public void readChars(char[] buffer) throws IOException
	{
		int total = 0;
		
		while (total < buffer.length)
		{
			int charCount = read(buffer, total, buffer.length-total);
			
			if (charCount < 0)
				throw new ClosedChannelException();
			total += charCount;
		}
	}
	
	/**
	 * Lit un nombre donné de caractères depuis l'entrée
	 * @param length Nombre de caractères à lire
	 * @return Chaîne des caractères lus
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 */
	public String readChars(int length) throws IOException
	{
		char[] buffer = new char[length];
		readChars(buffer);
		return new String(buffer);
	}
	
	/**
	 * Lit une chaîne de caractères
	 * <p>
	 * Au contraire de <tt>readLine()</tt> qui renvoie <tt>null</tt> si le flux
	 * est terminé, cette méthode déclenche une exception.
	 * </p>
	 * @return Chaîne de caractères lue
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 */
	public String readString() throws IOException
	{
		String result = readLine();
		if (result == null)
			throw new ClosedChannelException();
		return result;
	}

	/**
	 * Lit une liste de chaînes de caractères depuis l'entrée
	 * @return Liste des chaînes lues
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 * @throws NetworkProtocolException N'a pas pu lire une liste de chaînes
	 */
	public String[] readStrings() throws IOException
	{
		String[] result = new String[readInteger()];
		for (int i = 0; i < result.length; i++)
			result[i] = readString();
		return result;
	}
	
	/**
	 * Lit une liste de chaînes de caractères depuis l'entrée
	 * @return Liste des chaînes lues
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 * @throws NetworkProtocolException N'a pas pu lire une liste de chaînes
	 */
	public List<String> readStringList() throws IOException
	{
		return Arrays.asList(readStrings());
	}
	
	/**
	 * Lit une chaîne de caractères nullable
	 * @return Chaîne de caractères lue (peut être <tt>null</tt>)
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 */
	public String readNullableString() throws IOException
	{
		String result = readString();
		if (result.equals(NetworkInfo.nullString))
			return null;
		else
			return result;
	}

	/**
	 * Lit une liste de chaînes de caractères nullables depuis l'entrée
	 * @return Liste des chaînes lues
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 * @throws NetworkProtocolException N'a pas pu lire une liste de chaînes
	 */
	public String[] readNullableStrings() throws IOException
	{
		String[] result = new String[readInteger()];
		for (int i = 0; i < result.length; i++)
			result[i] = readNullableString();
		return result;
	}
	
	/**
	 * Lit une liste de chaînes de caractères nullables depuis l'entrée
	 * @return Liste des chaînes lues
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 * @throws NetworkProtocolException N'a pas pu lire une liste de chaînes
	 */
	public List<String> readNullableStringList() throws IOException
	{
		return Arrays.asList(readNullableStrings());
	}
	
	/**
	 * List un entier sur une ligne depuis l'entrée
	 * @return Entier lu
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 * @throws NetworkProtocolException La ligne lue n'est pas un entier
	 */
	public int readInteger() throws IOException
	{
		String line = readString();
		
		try
		{
			return Integer.parseInt(line);
		}
		catch (NumberFormatException error)
		{
			throw new NetworkProtocolException(line + "is not an integer",
				error);
		}
	}

	/**
	 * Lit une liste d'entiers depuis l'entrée
	 * @return Liste des entiers lus
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 * @throws NetworkProtocolException N'a pas pu lire une liste d'entier
	 */
	public int[] readIntegers() throws IOException
	{
		String[] lines = readStrings();
		
		int[] result = new int[lines.length];
		
		for (int i = 0; i < result.length; i++)
		{
			try
			{
				result[i] = Integer.parseInt(lines[i]);
			}
			catch (NumberFormatException error)
			{
				throw new NetworkProtocolException(lines[i] +
					"is not an integer", error);
			}
		}
		
		return result;
	}

	/**
	 * Lit une liste d'entiers depuis l'entrée
	 * @return Liste des entiers lus
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 * @throws NetworkProtocolException N'a pas pu lire une liste d'entier
	 */
	public List<Integer> readIntegerList() throws IOException
	{
		int[] integers = readIntegers();
		List<Integer> result = new ArrayList<Integer>(integers.length);
		
		for (int i = 0; i < integers.length; i++)
			result.add(integers[i]);
		
		return result;
	}
	
	/**
	 * List un booléen sur une ligne depuis l'entrée
	 * @return Booléen lu
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 * @throws NetworkProtocolException La ligne lue n'est pas un booléen
	 */
	public boolean readBoolean() throws IOException
	{
		return readInteger() != 0;
	}

	/**
	 * Lit une liste de booléens depuis l'entrée
	 * @return Liste des booléens lus
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 * @throws NetworkProtocolException N'a pas pu lire une liste de booléens
	 */
	public boolean[] readBooleans() throws IOException
	{
		int[] integers = readIntegers();
		
		boolean[] result = new boolean[integers.length];
		
		for (int i = 0; i < result.length; i++)
			result[i] = (integers[i] != 0);
		
		return result;
	}

	/**
	 * Lit une liste de booléens depuis l'entrée
	 * @return Liste des booléens lus
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 * @throws NetworkProtocolException N'a pas pu lire une liste de booléens
	 */
	public List<Boolean> readBooleanList() throws IOException
	{
		return mapList(readIntegerList(),
			new Mapper<Integer, Boolean>()
			{
				public Boolean map(Integer value)
				{
					return value != 0;
				}
			});
	}

	/**
	 * Lit une réponse depuis l'entrée
	 * @return Réponse lue
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 * @throws NetworkProtocolException La ligne lue ne suit pas le protocole
	 * @throws NetworkProtocolException Un code d'erreur de protocole a été lu
	 * @throws NetworkException Un code d'erreur a été lu
	 */
	public Response readResponse() throws IOException
	{
		String line = readString();
		
		Response result = new Response(line);
		result.throwException();
		return result;
	}

	/**
	 * Lit une commande depuis l'entrée
	 * @return Tableau des commande et paramètres (commande en première
	 *         position), ou <tt>null</tt> si la fin du flux d'entrée est
	 *         atteinte.
	 * @throws IOException Erreur de communication
	 */
	public String[] readCommand() throws IOException
	{
		StringTokenizer tokenizer;
		
		do
		{
			String line = readLine();
			if (line == null)
				return null;

			tokenizer = new StringTokenizer(line, "\t");
		} while (tokenizer.countTokens() == 0);

		String[] result = new String[tokenizer.countTokens()];

		for (int i = 0; i < result.length; i++)
			result[i] = tokenizer.nextToken();

		return result;
	}
	
	/**
	 * Lit une couleur carte sur une ligne depuis l'entrée
	 * @return Couleur lue
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 * @throws NetworkProtocolException La ligne lue n'est pas une carte
	 */
	public Suit readSuit() throws IOException
	{
		String line = readString();
		
		try
		{
			if (line.equals(NetworkInfo.nullString))
				return null;
			else
				return Suit.valueOf(line);
		}
		catch (IllegalArgumentException error)
		{
			throw new NetworkProtocolException(line + "is not a valid suit");
		}
	}

	/**
	 * Lit une liste de couleurs de carte depuis l'entrée
	 * @return Liste des couleurs lues
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 * @throws NetworkProtocolException N'a pas pu lire une liste de couleurs
	 */
	public Suit[] readSuits() throws IOException
	{
		String[] lines = readStrings();
		
		Suit[] result = new Suit[lines.length];
		
		for (int i = 0; i < result.length; i++)
		{
			try
			{
				if (lines[i].equals(NetworkInfo.nullString))
					result[i] = null;
				else
					result[i] = Suit.valueOf(lines[i]);
			}
			catch (IllegalArgumentException error)
			{
				throw new NetworkProtocolException(lines[i] +
					"is not a valid suit");
			}
		}
		
		return result;
	}

	/**
	 * Lit une liste de couleurs de carte depuis l'entrée
	 * @return Liste des couleurs lues
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 * @throws NetworkProtocolException N'a pas pu lire une liste de couleurs
	 */
	public List<Suit> readSuitList() throws IOException
	{
		return Arrays.asList(readSuits());
	}
	
	/**
	 * Lit une carte sur une ligne depuis l'entrée
	 * @param deck Paquet de cartes
	 * @return Carte lue
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 * @throws NetworkProtocolException La ligne lue n'est pas une carte
	 */
	public Card readCard(Deck deck) throws IOException
	{
		String line = readString();

		try
		{
			if (line.equals(NetworkInfo.nullString))
				return null;
			else
				return deck.findCardByID(line);
		}
		catch (IllegalArgumentException error)
		{
			throw new NetworkProtocolException(line + "is not a valid card ID");
		}
	}

	/**
	 * Lit une liste de cartes depuis l'entrée
	 * @param deck Paquet de cartes
	 * @return Liste des cartes lues
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 * @throws NetworkProtocolException N'a pas pu lire une liste de cartes
	 */
	public Card[] readCards(Deck deck) throws IOException
	{
		String[] lines = readStrings();
		
		Card[] result = new Card[lines.length];
		
		for (int i = 0; i < result.length; i++)
		{
			try
			{
				if (lines[i].equals(NetworkInfo.nullString))
					result[i] = null;
				else
					result[i] = deck.findCardByID(lines[i]);
			}
			catch (IllegalArgumentException error)
			{
				throw new NetworkProtocolException(lines[i] +
					"is not a valid card ID");
			}
		}
		
		return result;
	}

	/**
	 * Lit une liste de cartes depuis l'entrée
	 * @param deck Paquet de cartes
	 * @return Liste des cartes lues
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 * @throws NetworkProtocolException N'a pas pu lire une liste de cartes
	 */
	public List<Card> readCardList(Deck deck) throws IOException
	{
		return Arrays.asList(readCards(deck));
	}

	/**
	 * Convertit une ligne lue sur l'entrée en annonce
	 * @param line Ligne lue
	 * @param game Jeu, pour récupérer le joueur
	 * @return Annonce correspondante
	 * @throws NetworkProtocolException La ligne ne correspond pas à une annonce
	 */
	protected Announce stringToAnnounce(String line, RemotableGame game)
		throws NetworkProtocolException
	{
		StringTokenizer tokenizer = new StringTokenizer(line, "\t");
		if (tokenizer.countTokens() != 3)
			throw new NetworkProtocolException("Bad announce line " + line);
		
		try
		{
			int playerPosition = Integer.parseInt(tokenizer.nextToken());
			String id = tokenizer.nextToken();
			String name = tokenizer.nextToken();
			
			if ((playerPosition < 0) ||
				(playerPosition >= game.getPlayerCount()))
				throw new NetworkProtocolException("Bad announce line " + line);
			
			return new Announce(game.getPlayers(playerPosition), id, name);
		}
		catch (NumberFormatException error)
		{
			throw new NetworkProtocolException("Bad announce line " + line);
		}
	}
	
	/**
	 * Lit une annonce depuis l'entrée
	 * @param game Jeu, pour récupérer le joueur
	 * @return Annonce lue
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 * @throws NetworkProtocolException La ligne lue n'est pas une annonce
	 */
	public Announce readAnnounce(RemotableGame game) throws IOException
	{
		return stringToAnnounce(readString(), game);
	}
	
	/**
	 * Lit une liste d'annonces depuis l'entrée
	 * @param game Jeu, pour récupérer les joueurs
	 * @return Tableau des annonces lues
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 * @throws NetworkProtocolException N'a pas pu lire une liste d'annonces
	 */
	public Announce[] readAnnounces(RemotableGame game) throws IOException
	{
		String[] lines = readStrings();
		
		Announce[] result = new Announce[lines.length];
		
		for (int i = 0; i < result.length; i++)
			result[i] = stringToAnnounce(lines[i], game);
		
		return result;
	}
	
	/**
	 * Lit une liste d'annonces depuis l'entrée
	 * @param game Jeu, pour récupérer les joueurs
	 * @return Liste des annonces lues
	 * @throws ClosedChannelException Connexion rompue
	 * @throws IOException Erreur de communication
	 * @throws NetworkProtocolException N'a pas pu lire une liste d'annonces
	 */
	public List<Announce> readAnnounceList(RemotableGame game)
		throws IOException
	{
		return Arrays.asList(readAnnounces(game));
	}
}
