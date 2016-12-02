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
package org.jskat.util.rule;

import org.jskat.data.SkatGameData;
import org.jskat.util.Card;
import org.jskat.util.CardList;
import org.jskat.util.GameType;
import org.jskat.util.SkatConstants;
import org.jskat.util.Suit;

/**
 * Implementation of skat rules for Null games
 * 
 */
public class NullRules extends AbstractSkatRules {

	/**
	 * @see BasicSkatRules#calcGameResult(SkatGameData)
	 */
	@Override
	public int calcGameResult(SkatGameData gameData) {

		int gameValue = SkatConstants.getGameBaseValue(gameData.getGameType(), gameData.isHand(), gameData.isOuvert());
		int multiplier = 1;

		if (gameData.isGameLost()) {

			// Lost game is always counted double
			multiplier = multiplier * -2;
		}

		return gameValue * multiplier;
	}

	@SuppressWarnings("unused")
	private int getWonTricksByDeclarer(SkatGameData data) {

		int result = 0;

		for (int i = 0; i < data.getTricks().size(); i++) {
			if (data.getTrickWinner(i).equals(data.getDeclarer())) {
				result++;
			}
		}

		return result;
	}

	/**
	 * @see BasicSkatRules#isCardBeatsCard(GameType, Card, Card)
	 */
	@Override
	public boolean isCardBeatsCard(GameType gameType, Card cardToBeat, Card card) {

		boolean result = false;

		if (cardToBeat.getSuit() == card.getSuit()) {

			if (cardToBeat.getNullOrder() < card.getNullOrder()) {

				result = true;
			}
		}

		return result;
	}

	/**
	 * @see BasicSkatRules#isCardAllowed(GameType, Card, CardList, Card)
	 */
	@Override
	public boolean isCardAllowed(GameType gameType, Card initialCard, CardList hand, Card card) {

		boolean result = false;

		if (initialCard == null) {
			// no intial card is given --> every card is allowed
			result = true;
		} else if (card.getSuit() == initialCard.getSuit()) {
			// card must serve same suit
			result = true;

		} else if (!hand.hasSuit(gameType, initialCard.getSuit())) {
			// hand has no card of same suit --> every card is allowed
			result = true;
		}

		return result;
	}

	/**
	 * @see BasicSkatRules#calcGameWon(SkatGameData)
	 */
	@Override
	public boolean calcGameWon(SkatGameData gameData) {

		boolean result = true;

		for (int i = 0; i < gameData.getTricks().size(); i++) {

			if (gameData.getTrickWinner(i).equals(gameData.getDeclarer())) {
				// the single player has won at least one trick
				result = false;
			}
		}

		return result;
	}

	/**
	 * @see BasicSkatRules#hasSuit(GameType, CardList, Suit)
	 */
	@Override
	public boolean hasSuit(GameType gameType, CardList hand, Suit suit) {

		boolean result = false;

		int index = 0;
		while (result == false && index < hand.size()) {

			if (hand.get(index).getSuit() == suit) {

				result = true;
			}

			index++;
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMultiplier(@SuppressWarnings("unused") SkatGameData gameData) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPlayWithJacks(@SuppressWarnings("unused") SkatGameData gameData) {
		return false;
	}
}
