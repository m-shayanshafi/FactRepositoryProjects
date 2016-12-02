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
package org.jskat.ai.nn.train;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.NoOpLog;
import org.jskat.ai.nn.AIPlayerNN;
import org.jskat.control.JSkatMaster;
import org.jskat.control.JSkatThread;
import org.jskat.control.SkatGame;
import org.jskat.data.GameAnnouncement;
import org.jskat.data.GameAnnouncement.GameAnnouncementFactory;
import org.jskat.data.GameSummary;
import org.jskat.data.SkatGameData.GameState;
import org.jskat.gui.NullView;
import org.jskat.util.CardDeck;
import org.jskat.util.GameType;
import org.jskat.util.GameVariant;
import org.jskat.util.Player;

/**
 * Trains the neural networks
 */
public class NNTrainer extends JSkatThread {

	private static Log log = LogFactory.getLog(NNTrainer.class);

	private JSkatMaster jskat;

	private Random rand;
	private List<StringBuffer> nullGames;

	private GameType gameType;

	/**
	 * Constructor
	 */
	public NNTrainer() {

		jskat = JSkatMaster.instance();

		rand = new Random();
		nullGames = new ArrayList<StringBuffer>();

		initLearningPatterns();
	}

	private void initLearningPatterns() {

		// test a perfect null game
		StringBuffer buffer = new StringBuffer();
		buffer.append("CA CT CK "); // 3 cards fore hand //$NON-NLS-1$
		buffer.append("C9 ST SK "); // 3 cards middle hand //$NON-NLS-1$
		buffer.append("H7 DA DT "); // 3 cards hind hand //$NON-NLS-1$
		buffer.append("SA HA "); // skat //$NON-NLS-1$
		buffer.append("CQ CJ C8 C7 "); // 4 cards fore hand //$NON-NLS-1$
		buffer.append("SQ SJ HK HQ "); // 4 cards middle hand //$NON-NLS-1$
		buffer.append("DK DQ D9 D8 "); // 4 cards hind hand //$NON-NLS-1$
		buffer.append("S9 S8 S7 "); // 3 cards fore hand //$NON-NLS-1$
		buffer.append("HJ H9 H8 "); // 3 cards middle hand //$NON-NLS-1$
		buffer.append("D7 HT DJ"); // 3 cards hind hand //$NON-NLS-1$
		nullGames.add(buffer);

		buffer = new StringBuffer();
		buffer.append("CA CT CK "); // 3 cards fore hand //$NON-NLS-1$
		buffer.append("S9 ST SK "); // 3 cards middle hand //$NON-NLS-1$
		buffer.append("H7 DA DT "); // 3 cards hind hand //$NON-NLS-1$
		buffer.append("SA HA "); // skat //$NON-NLS-1$
		buffer.append("CQ CJ C8 C7 "); // 4 cards fore hand //$NON-NLS-1$
		buffer.append("SQ SJ HK HQ "); // 4 cards middle hand //$NON-NLS-1$
		buffer.append("DK DQ D9 D8 "); // 4 cards hind hand //$NON-NLS-1$
		buffer.append("C9 S8 S7 "); // 3 cards fore hand //$NON-NLS-1$
		buffer.append("HJ H9 H8 "); // 3 cards middle hand //$NON-NLS-1$
		buffer.append("D7 HT DJ"); // 3 cards hind hand //$NON-NLS-1$
		nullGames.add(buffer);

		buffer = new StringBuffer();
		buffer.append("C9 C8 C7 "); // 3 cards fore hand //$NON-NLS-1$
		buffer.append("CA CT CK "); // 3 cards middle hand //$NON-NLS-1$
		buffer.append("CQ CJ SA "); // 3 cards hind hand //$NON-NLS-1$
		buffer.append("SJ SK "); // skat //$NON-NLS-1$
		buffer.append("ST S9 S8 S7 "); // 4 cards fore hand //$NON-NLS-1$
		buffer.append("SQ HA HT HK "); // 4 cards middle hand //$NON-NLS-1$
		buffer.append("HQ DA DT DK "); // 4 cards hind hand //$NON-NLS-1$
		buffer.append("H9 H8 H7 "); // 3 cards fore hand //$NON-NLS-1$
		buffer.append("DQ DJ D9 "); // 3 cards middle hand //$NON-NLS-1$
		buffer.append("D8 D7 HJ"); // 3 cards hind hand //$NON-NLS-1$
		nullGames.add(buffer);

		buffer = new StringBuffer();
		buffer.append("SA ST SK "); // 3 cards fore hand //$NON-NLS-1$
		buffer.append("S9 CT CK "); // 3 cards middle hand //$NON-NLS-1$
		buffer.append("H7 DA DT "); // 3 cards hind hand //$NON-NLS-1$
		buffer.append("CA HA "); // skat //$NON-NLS-1$
		buffer.append("SQ SJ S8 S7 "); // 4 cards fore hand //$NON-NLS-1$
		buffer.append("CQ CJ HK HQ "); // 4 cards middle hand //$NON-NLS-1$
		buffer.append("DK DQ D9 D8 "); // 4 cards hind hand //$NON-NLS-1$
		buffer.append("C9 C8 C7 "); // 3 cards fore hand //$NON-NLS-1$
		buffer.append("HJ H9 H8 "); // 3 cards middle hand //$NON-NLS-1$
		buffer.append("D7 HT DJ"); // 3 cards hind hand //$NON-NLS-1$
		nullGames.add(buffer);

		buffer = new StringBuffer();
		buffer.append("HA HT HK "); // 3 cards fore hand //$NON-NLS-1$
		buffer.append("H9 CT CK "); // 3 cards middle hand //$NON-NLS-1$
		buffer.append("S7 DA DT "); // 3 cards hind hand //$NON-NLS-1$
		buffer.append("CA SA "); // skat //$NON-NLS-1$
		buffer.append("HQ HJ H8 H7 "); // 4 cards fore hand //$NON-NLS-1$
		buffer.append("CQ CJ SK SQ "); // 4 cards middle hand //$NON-NLS-1$
		buffer.append("DK DQ D9 D8 "); // 4 cards hind hand //$NON-NLS-1$
		buffer.append("S9 S8 S7 "); // 3 cards fore hand //$NON-NLS-1$
		buffer.append("SJ S9 S8 "); // 3 cards middle hand //$NON-NLS-1$
		buffer.append("D7 ST DJ"); // 3 cards hind hand //$NON-NLS-1$
		nullGames.add(buffer);
	}

	/**
	 * Sets the game type to learn
	 * 
	 * @param newGameType
	 *            Game type
	 */
	public void setGameType(GameType newGameType) {

		gameType = newGameType;
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {

		trainNets();
	}

	/**
	 * Trains the neural networks
	 */
	private void trainNets() {

		AIPlayerNN nnPlayer1 = new AIPlayerNN();
		nnPlayer1.setIsLearning(true);
		nnPlayer1.setLogger(new NoOpLog());
		AIPlayerNN nnPlayer2 = new AIPlayerNN();
		nnPlayer2.setIsLearning(true);
		nnPlayer2.setLogger(new NoOpLog());
		AIPlayerNN nnPlayer3 = new AIPlayerNN();
		nnPlayer3.setIsLearning(true);
		nnPlayer3.setLogger(new NoOpLog());

		long episodes = 0;
		long episodesWonGames = 0;
		long totalWonGames = 0;
		long totalGames = 0;
		int episodeSteps = 100;

		while (true) {

			if (episodes > 0 && episodes % episodeSteps == 0) {

				log.debug(gameType + ": Episode " + episodes + " won games " //$NON-NLS-1$ //$NON-NLS-2$
						+ episodesWonGames + " (" + 100 * episodesWonGames //$NON-NLS-1$
						/ (episodeSteps * 3) + " %)" + " total won games " //$NON-NLS-1$ //$NON-NLS-2$
						+ totalWonGames + " (" + 100 * totalWonGames //$NON-NLS-1$
						/ totalGames + " %)"); //$NON-NLS-1$
				//				log.debug("        Declarer net: " //$NON-NLS-1$
				// + declarerLearning.getCurrentIteration()
				//						+ " iterations " //$NON-NLS-1$
				// + declarerLearning.getTotalNetworkError()
				//						+ " total network error"); //$NON-NLS-1$
				//				log.debug("        Opponent net: " //$NON-NLS-1$
				// + opponentLearning.getCurrentIteration()
				//						+ " iterations " //$NON-NLS-1$
				// + opponentLearning.getTotalNetworkError()
				//						+ " total network error"); //$NON-NLS-1$

				jskat.addTrainingResult(gameType, episodes, totalWonGames, episodesWonGames, 0.0);

				episodesWonGames = 0;
			}

			for (Player currPlayer : Player.values()) {

				nnPlayer1.newGame(Player.FOREHAND);
				nnPlayer2.newGame(Player.MIDDLEHAND);
				nnPlayer3.newGame(Player.REARHAND);
				SkatGame game = new SkatGame("table", GameVariant.STANDARD, nnPlayer1, nnPlayer2, nnPlayer3);
				game.setView(new NullView());
				game.setLogger(new NoOpLog());

				CardDeck deck = new CardDeck();
				deck.shuffle();
				log.debug("Card deck: " + deck); //$NON-NLS-1$
				game.setCardDeck(deck);
				game.dealCards();

				if (!GameType.RAMSCH.equals(gameType)) {
					game.setDeclarer(currPlayer);
				}

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

				// FIXME (jansch 28.06.2011) have to call getGameResult() to get
				// the result
				game.getGameResult();

				boolean gameWon = false;
				if (gameType.equals(GameType.RAMSCH)) {
					gameWon = isRamschGameWon(game.getGameSummary(), currPlayer);
				} else {
					gameWon = game.isGameWon();
				}

				if (gameWon) {
					episodesWonGames++;
					totalWonGames++;
				}

				totalGames++;
			}

			episodes += 3;

			checkWaitCondition();
		}
	}

	// FIXME (jan 10.03.2012) code duplication with AIPlayerNN
	private static boolean isRamschGameWon(GameSummary gameSummary, Player currPlayer) {

		boolean ramschGameWon = false;
		int playerPoints = gameSummary.getPlayerPoints(currPlayer);
		int highestPlayerPoints = 0;
		for (Player player : Player.values()) {
			int currPlayerPoints = gameSummary.getPlayerPoints(player);

			if (currPlayerPoints > highestPlayerPoints) {
				highestPlayerPoints = currPlayerPoints;
			}
		}

		if (highestPlayerPoints > playerPoints) {
			ramschGameWon = true;
		}

		return ramschGameWon;
	}
}
