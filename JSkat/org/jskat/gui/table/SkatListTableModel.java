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
package org.jskat.gui.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jskat.data.GameSummary;
import org.jskat.util.JSkatResourceBundle;
import org.jskat.util.Player;
import org.jskat.util.SkatConstants;
import org.jskat.util.SkatListMode;

/**
 * Provides a model for the skat list table
 */
class SkatListTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(SkatListTableModel.class);

	private JSkatResourceBundle strings;

	private SkatListMode mode = SkatListMode.NORMAL;

	private int playerCount = 3;
	private List<Player> declarers;
	private List<List<Integer>> playerResults;
	private List<GameSummary> gameResults;
	private List<String> columns;
	private List<List<Integer>> displayValues;

	/**
	 * Constructor
	 */
	public SkatListTableModel() {

		strings = JSkatResourceBundle.instance();

		declarers = new ArrayList<Player>();
		playerResults = new ArrayList<List<Integer>>();
		gameResults = new ArrayList<GameSummary>();
		displayValues = new ArrayList<List<Integer>>();
		columns = new ArrayList<String>();
		setColumns();
	}

	/**
	 * @see AbstractTableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {

		return columns.size();
	}

	/**
	 * @see AbstractTableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {

		return declarers.size();
	}

	/**
	 * @see AbstractTableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		Object result = null;

		if (displayValues.get(rowIndex).get(columnIndex) != null) {
			result = displayValues.get(rowIndex).get(columnIndex);
		} else {
			result = "-"; //$NON-NLS-1$
		}

		return result;
	}

	Integer getPlayerValue(int playerColumn, int gameRow) {

		Integer result = null;

		return result;
	}

	/**
	 * @see AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int col) {

		return columns.get(col);
	}

	/**
	 * @see AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int col) {

		return Integer.class;
	}

	/**
	 * Sets the skat list mode
	 * 
	 * @param newMode
	 */
	void setSkatListMode(SkatListMode newMode) {

		mode = newMode;

		calculateDisplayValues();

		fireTableDataChanged();
	}

	void calculateDisplayValues() {

		int currResult = 0;
		List<Integer> playerResultsSoFar = new ArrayList<Integer>();
		for (int i = 0; i < playerCount; i++) {
			playerResultsSoFar.add(new Integer(0));
		}

		displayValues.clear();

		for (int game = 0; game < gameResults.size(); game++) {

			displayValues.add(new ArrayList<Integer>());

			// add player values
			for (int player = 0; player < playerCount; player++) {

				int previousResult = 0;
				currResult = 0;

				if (declarers.get(game) != null) {

					// get previous result for player values
					previousResult = playerResultsSoFar.get(player).intValue();

					// get player results from current game
					switch (mode) {
					case NORMAL:
						currResult = playerResults.get(player).get(game);
						break;
					case TOURNAMENT:
						boolean isDeclarer = (playerResults.get(player).get(game) != 0);
						currResult = SkatConstants.getTournamentGameValue(isDeclarer, gameResults.get(game)
								.getGameValue(), playerCount);
						break;
					case BIERLACHS:
						// FIXME jan 31.05.2010 add bierlachs value
						break;
					}
				}

				if (currResult != 0) {

					Integer newResult = new Integer(previousResult + currResult);
					displayValues.get(game).add(newResult);
					playerResultsSoFar.set(player, newResult);

				} else {

					displayValues.get(game).add(null);
				}
			}

			// get game result
			switch (mode) {
			case NORMAL:
			case BIERLACHS:
				currResult = gameResults.get(game).getGameValue();
				break;
			case TOURNAMENT:
				currResult = SkatConstants.getTournamentGameValue(true, gameResults.get(game).getGameValue(),
						playerCount);
				break;
			}
			displayValues.get(game).add(currResult);
		}
	}

	/**
	 * Adds a game result to the model
	 * 
	 * @param leftOpponent
	 *            Position of the upper left opponent
	 * @param rightOpponent
	 *            Position of the upper right opponent
	 * @param user
	 *            Position of the player
	 * @param declarer
	 *            Position of the game declarer
	 * @param gameSummary
	 *            Game summary
	 */
	void addResult(Player leftOpponent, Player rightOpponent, Player user, Player declarer, GameSummary gameSummary) {

		// FIXME works only on 3 player series
		// FIXME (jansch 21.03.2011) provide only one method for addResult()
		declarers.add(declarer);
		gameResults.add(gameSummary);

		int declarerColumn = getDeclarerColumn(leftOpponent, rightOpponent, user, declarer);

		if (declarer != null) {
			playerResults.get(declarerColumn).add(Integer.valueOf(gameSummary.getGameValue()));
			playerResults.get((declarerColumn + 1) % 3).add(Integer.valueOf(0));
			playerResults.get((declarerColumn + 2) % 3).add(Integer.valueOf(0));
		} else {
			// game was passed in
			for (int i = 0; i < playerCount; i++) {
				playerResults.get(i).add(0);
			}
		}
		calculateDisplayValues();

		fireTableDataChanged();
	}

	static int getDeclarerColumn(Player leftOpponent, Player rightOpponent, Player player, Player declarer) {

		int result = -1;

		if (declarer == leftOpponent) {
			result = 0;
		} else if (declarer == rightOpponent) {
			result = 1;
		} else if (declarer == player) {
			result = 2;
		}

		return result;
	}

	/**
	 * Clears the complete list
	 */
	void clearList() {

		declarers.clear();
		for (List<Integer> currList : playerResults) {

			currList.clear();
		}
		gameResults.clear();
		displayValues.clear();

		fireTableDataChanged();
	}

	public void setPlayerCount(int newPlayerCount) {

		declarers.clear();
		gameResults.clear();

		playerCount = newPlayerCount;

		setColumns();

		fireTableStructureChanged();
		fireTableDataChanged();
	}

	void setColumns() {

		playerResults.clear();
		displayValues.clear();
		columns.clear();

		for (int i = 0; i < playerCount; i++) {
			// FIXME (jan 14.12.2010) get player names
			columns.add("P" + i);
			playerResults.add(new ArrayList<Integer>());
			displayValues.add(new ArrayList<Integer>());
		}
		columns.add(strings.getString("games")); //$NON-NLS-1$
		displayValues.add(new ArrayList<Integer>());
	}

	void setPlayerNames(String upperLeftPlayer, String upperRightPlayer, String lowerPlayer) {

		columns.set(0, upperLeftPlayer);
		columns.set(1, upperRightPlayer);
		columns.set(2, lowerPlayer);
		fireTableStructureChanged();
	}
}
