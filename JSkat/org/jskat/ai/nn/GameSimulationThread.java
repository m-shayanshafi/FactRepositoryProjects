/**
 * JSkat - A skat program written in Java
 * by Jan Schäfer and Markus J. Luzius
 *
 * Version 0.10.1
 * Copyright (C) 2012-03-25
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jskat.ai.nn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.NoOpLog;
import org.jskat.control.JSkatThread;
import org.jskat.control.SkatGame;
import org.jskat.data.GameAnnouncement;
import org.jskat.data.GameAnnouncement.GameAnnouncementFactory;
import org.jskat.data.SkatGameData.GameState;
import org.jskat.gui.NullView;
import org.jskat.util.CardDeck;
import org.jskat.util.CardList;
import org.jskat.util.GameType;
import org.jskat.util.GameVariant;
import org.jskat.util.Player;

/**
 * Helper class for simulating games
 */
class GameSimulationThread extends JSkatThread {

	private static Log log = LogFactory.getLog(GameSimulationThread.class);

	private final GameType gameType;
	private final Player position;
	private final CardList cards;

	private Long maxEpisodes;
	private Long maxTimestamp;

	private long simulatedGames;
	private long wonGames;

	private AIPlayerNN nnPlayer1;
	private AIPlayerNN nnPlayer2;
	private AIPlayerNN nnPlayer3;

	GameSimulationThread(GameType pGameType, Player playerPosition, CardList playerCards) {

		gameType = pGameType;
		position = playerPosition;
		cards = playerCards;

		nnPlayer1 = new AIPlayerNN();
		nnPlayer1.setIsLearning(false);
		nnPlayer1.setLogger(new NoOpLog());
		nnPlayer2 = new AIPlayerNN();
		nnPlayer2.setIsLearning(false);
		nnPlayer2.setLogger(new NoOpLog());
		nnPlayer3 = new AIPlayerNN();
		nnPlayer3.setIsLearning(false);
		nnPlayer3.setLogger(new NoOpLog());
	}

	void startSimulationWithMaxEpidodes(Long episodes) {
		maxEpisodes = episodes;
		start();
	}

	void startSimulationWithTimestamp(Long timestamp) {
		maxTimestamp = timestamp;
		start();
	}

	@Override
	public void run() {
		simulateGames();
	}

	private void simulateGames() {

		simulatedGames = 0;
		wonGames = 0;

		while (!isAllSimulationsDone()) {
			if (simulateGame()) {
				wonGames++;
			}
			simulatedGames++;
		}
	}

	private boolean isAllSimulationsDone() {

		if (maxEpisodes != null) {
			if (simulatedGames < maxEpisodes.longValue()) {
				return false;
			}
		} else if (maxTimestamp != null) {
			if (System.currentTimeMillis() < maxTimestamp.longValue()) {
				return false;
			}
		}
		return true;
	}

	private boolean simulateGame() {

		SkatGame game = new SkatGame("table", GameVariant.STANDARD, nnPlayer1, nnPlayer2, nnPlayer3);
		game.setView(new NullView());
		game.setLogger(new NoOpLog());

		CardDeck deck = CardDeckSimulator.simulateUnknownCards(position, cards);
		log.debug("Card deck: " + deck); //$NON-NLS-1$
		game.setCardDeck(deck);
		game.dealCards();

		game.setDeclarer(position);

		GameAnnouncementFactory factory = GameAnnouncement.getFactory();
		factory.setGameType(gameType);
		game.setGameAnnouncement(factory.getAnnouncement());

		game.setGameState(GameState.TRICK_PLAYING);

		game.start();
		try {
			game.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// FIXME (jansch 28.06.2011) have to call getGameResult() for result
		// calculation
		game.getGameResult();

		return game.isGameWon();
	}

	long getEpisodes() {
		return simulatedGames;
	}

	double getWonRate() {
		double wonDouble = wonGames;
		double gameCountDouble = simulatedGames;

		return wonDouble / gameCountDouble;
	}

	GameType getGameType() {
		return gameType;
	}
}
