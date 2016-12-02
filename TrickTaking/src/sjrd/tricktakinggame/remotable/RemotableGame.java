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
package sjrd.tricktakinggame.remotable;

import java.util.*;

/**
 * Jeu à plis remotable (auquel on peut accéder à distance)
 * @author sjrd
 */
public interface RemotableGame
{
	/**
	 * Nombre d'équipes
	 * @return Nombre d'équipes
	 */
	public int getTeamCount();

	/**
	 * Tableau zero-based des équipes
	 * @param index Index d'une équipe
	 * @return Equipe à l'index spécifié
	 */
	public RemotableTeam getTeams(int index);

	/**
	 * Tableau cyclique et zero-based des équipes
	 * @param index Index d'une équipe (sera normé dans les bornes par modulo)
	 * @return Equipe à l'index spécifié
	 */
	public RemotableTeam getCyclicTeams(int index);

	/**
	 * Itérateur sur les équipes
	 * @return Itérateur sur les équipes
	 */
	public Iterator<RemotableTeam> getTeamsIterator();

	/**
	 * Itérable sur les équipes
	 * @return Itérable sur les équipes
	 */
	public Iterable<RemotableTeam> getTeamsIterable();

	/**
	 * Nombre de joueurs
	 * @return Nombre de joueurs
	 */
	public int getPlayerCount();

	/**
	 * Tableau zero-based des joueurs
	 * @param index Index d'un joueur
	 * @return Joueur à l'index spécifié
	 */
	public RemotablePlayer getPlayers(int index);

	/**
	 * Tableau cyclique et zero-based des joueurs
	 * @param index Index d'un joueur (sera normé dans les bornes par modulo)
	 * @return Joueur à l'index spécifié
	 */
	public RemotablePlayer getCyclicPlayers(int index);

	/**
	 * Itérateur sur les joueurs
	 * @return Itérateur sur les joueurs
	 */
	public Iterator<RemotablePlayer> getPlayersIterator();

	/**
	 * Itérable sur les joueurs
	 * @return Itérable sur les joueurs
	 */
	public Iterable<RemotablePlayer> getPlayersIterable();
	
	/**
	 * Teste si une enchère est en cours
	 * @return <tt>true</tt> si une enchère est en cours, <tt>false</tt> sinon
	 */
	public boolean isAuctioning();

	/**
	 * Récupère les annonces faites
	 * <p>
	 * Une valeur retournée <tt>null</tt> signifie qu'aucune enchère n'est en
	 * cours.
	 * </p>
	 * @return Copie de la liste des annonces faites (peut être <tt>null</tt>)
	 */
	public List<Announce> getAnnounces();

	/**
	 * Nom du contrat courant
	 * @return Nom du contrat courant (peut être <tt>null</tt>)
	 */
	public String getCurrentContractName();

	/**
	 * Serveur
	 * @return Serveur
	 */
	public RemotablePlayer getDealer();

	/**
	 * Joueur actif, qui a la main
	 * @return Joueur actif, qui a la main
	 */
	public RemotablePlayer getActivePlayer();

	/**
	 * Nombre de parties jouées
	 * <p>
	 * Cette propriété reflète le nombre de fois que la méthode
	 * <tt>playGame</tt> a été appelée.
	 * </p>
	 * @return Nombre de parties jouées
	 */
	public int getPlayCount();

	/**
	 * Itérateur sur les équipes d'un <tt>RemotableGame</tt>
	 * @author sjrd
	 */
	public class TeamsIterator implements Iterator<RemotableTeam>
	{
		/**
		 * Jeu remotable
		 */
		private RemotableGame game;

		/**
		 * Index courant
		 */
		private int index = 0;

		/**
		 * Crée un nouvel itérateur sur les joueurs d'un jeu remotable
		 * @param aGame Jeu remotable
		 */
		public TeamsIterator(RemotableGame aGame)
		{
			super();
			game = aGame;
		}

		/**
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext()
		{
			return index < game.getTeamCount();
		}

		/**
		 * @see java.util.Iterator#next()
		 */
		public RemotableTeam next()
		{
			return game.getTeams(index++);
		}

		/**
		 * @see java.util.Iterator#remove()
		 */
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Itérable sur les équipess d'un <tt>RemotableGame</tt>
	 * @author sjrd
	 */
	public class TeamsIterable implements Iterable<RemotableTeam>
	{
		/**
		 * Jeu remotable
		 */
		private RemotableGame game;

		/**
		 * Crée un nouvel itérable sur les équipes d'un jeu remotable
		 * @param aGame Jeu remotable
		 */
		public TeamsIterable(RemotableGame aGame)
		{
			super();
			game = aGame;
		}

		/**
		 * @see java.lang.Iterable#iterator()
		 */
		public Iterator<RemotableTeam> iterator()
		{
			return game.getTeamsIterator();
		}
	}

	/**
	 * Itérateur sur les joueurs d'un <tt>RemotableGame</tt>
	 * @author sjrd
	 */
	public class PlayersIterator implements Iterator<RemotablePlayer>
	{
		/**
		 * Jeu remotable
		 */
		private RemotableGame game;

		/**
		 * Index courant
		 */
		private int index = 0;

		/**
		 * Crée un nouvel itérateur sur les joueurs d'un jeu remotable
		 * @param aGame Jeu remotable
		 */
		public PlayersIterator(RemotableGame aGame)
		{
			super();
			game = aGame;
		}

		/**
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext()
		{
			return index < game.getPlayerCount();
		}

		/**
		 * @see java.util.Iterator#next()
		 */
		public RemotablePlayer next()
		{
			return game.getPlayers(index++);
		}

		/**
		 * @see java.util.Iterator#remove()
		 */
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Itérable sur les joueurs d'un <tt>RemotableGame</tt>
	 * @author sjrd
	 */
	public class PlayersIterable implements Iterable<RemotablePlayer>
	{
		/**
		 * Jeu remotable
		 */
		private RemotableGame game;

		/**
		 * Crée un nouvel itérable sur les joueurs d'un jeu remotable
		 * @param aGame Jeu remotable
		 */
		public PlayersIterable(RemotableGame aGame)
		{
			super();
			game = aGame;
		}

		/**
		 * @see java.lang.Iterable#iterator()
		 */
		public Iterator<RemotablePlayer> iterator()
		{
			return game.getPlayersIterator();
		}
	}
}
