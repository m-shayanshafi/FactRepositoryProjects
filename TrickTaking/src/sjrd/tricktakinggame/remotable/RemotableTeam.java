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

import sjrd.tricktakinggame.cards.*;

/**
 * Equipe remotable (à laquelle on peut accéder à distance)
 * @author sjrd
 */
public interface RemotableTeam
{
	/**
	 * Jeu propriétaire
	 * @return Jeu propriétaire
	 */
	public RemotableGame getGame();

	/**
	 * Index de l'équipe
	 * @return Index de l'équipe
	 */
	public int getIndex();
	
	/**
	 * Nom de l'équipe
	 * @return Nom de l'équipe
	 */
	public String getName();
	
	/**
	 * Indique si le nom de l'équipe est un pluriel ou non
	 * @return <tt>true</tt> si le nom de l'équipe est un pluriel,
	 *         <tt>false</tt> sinon
	 */
	public boolean isNamePlural();

	/**
	 * Nombre de joueurs dans cette équipe
	 * @return Nombre de joueurs dans cette équipe
	 */
	public int getPlayerCount();

	/**
	 * Tableau zero-based des joueurs dans cette équipe
	 * @param index Index d'un joueur
	 * @return Joueur à l'index spécifié
	 */
	public RemotablePlayer getPlayers(int index);

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
	 * Renvoie une liste snapshot des cartes ramassées
	 * @return Liste snapshot des cartes ramassées
	 */
	public List<Card> getCollectedCardsSnapshot();

	/**
	 * Nombre de plis ramassés
	 * <p>
	 * Ce nombre est calculé selon la formule (nombre de cartes collectées) /
	 * (nombre de joueurs). En effet, un pli contient autant de cartes que le
	 * nombre de joueurs (en principe).
	 * </p>
	 * @return Nombre de plis ramassés
	 */
	public int getCollectedTrickCount();

	/**
	 * Score de l'équipe
	 * @return Score de l'équipe
	 */
	public int getScore();

	/**
	 * Itérateur sur les joueurs d'un <tt>RemotableGame</tt>
	 * @author sjrd
	 */
	public class PlayersIterator implements Iterator<RemotablePlayer>
	{
		/**
		 * Equipe remotable
		 */
		private RemotableTeam team;

		/**
		 * Index courant
		 */
		private int index = 0;

		/**
		 * Crée un nouvel itérateur sur les joueurs d'une équipe remotable
		 * @param aTeam Equipe remotable
		 */
		public PlayersIterator(RemotableTeam aTeam)
		{
			super();
			team = aTeam;
		}

		/**
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext()
		{
			return index < team.getPlayerCount();
		}

		/**
		 * @see java.util.Iterator#next()
		 */
		public RemotablePlayer next()
		{
			return team.getPlayers(index++);
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
	 * Itérable sur les joueurs d'une <tt>RemotableTeam</tt>
	 * @author sjrd
	 */
	public class PlayersIterable implements Iterable<RemotablePlayer>
	{
		/**
		 * Equipe remotable
		 */
		private RemotableTeam team;

		/**
		 * Crée un nouvel itérable sur les joueurs d'une équipe remotable
		 * @param aTeam Equipe remotable
		 */
		public PlayersIterable(RemotableTeam aTeam)
		{
			super();
			team = aTeam;
		}

		/**
		 * @see java.lang.Iterable#iterator()
		 */
		public Iterator<RemotablePlayer> iterator()
		{
			return team.getPlayersIterator();
		}
	}
}
