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

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ActionMap;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jskat.data.GameAnnouncement;
import org.jskat.data.GameSummary;
import org.jskat.data.SkatGameData.GameState;
import org.jskat.data.SkatSeriesData.SeriesState;
import org.jskat.data.Trick;
import org.jskat.gui.AbstractTabPanel;
import org.jskat.gui.LayoutFactory;
import org.jskat.gui.action.JSkatAction;
import org.jskat.gui.action.main.StartSkatSeriesAction;
import org.jskat.util.Card;
import org.jskat.util.CardList;
import org.jskat.util.GameType;
import org.jskat.util.Player;

/**
 * Panel for a skat table
 */
public class SkatTablePanel extends AbstractTabPanel {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(SkatTablePanel.class);

	protected Map<String, Player> playerNamesAndPositions;

	// FIXME (jan 14.11.2010) looks wrong to me, was made static to avoid
	// NullPointerException during ISS table creation
	protected static Map<Player, Boolean> playerPassed = new HashMap<Player, Boolean>();
	// declarer player on the table
	protected Player declarer;

	protected AbstractHandPanel foreHand;
	protected AbstractHandPanel middleHand;
	protected AbstractHandPanel rearHand;
	protected OpponentPanel leftOpponentPanel;
	protected OpponentPanel rightOpponentPanel;
	protected JSkatUserPanel userPanel;
	protected GameInformationPanel gameInfoPanel;
	protected JPanel gameContextPanel;
	protected Map<ContextPanelType, JPanel> contextPanels;
	protected TrickPanel trickPanel;
	protected TrickPanel lastTrickPanel;
	protected GameOverPanel gameOverPanel;
	/**
	 * Table model for skat list
	 */
	protected SkatListTableModel skatListTableModel;
	protected JTable scoreListTable;
	protected JScrollPane scoreListScrollPane;
	protected BiddingContextPanel biddingPanel;
	protected DeclaringContextPanel declaringPanel;
	protected SchieberamschContextPanel schieberamschPanel;

	protected boolean ramsch = false;

	/**
	 * @see AbstractTabPanel#AbstractTabPanel(String, ActionMap)
	 */
	public SkatTablePanel(String newTableName, ActionMap actions) {

		super(newTableName, actions);

		log.debug("SkatTablePanel: name: " + newTableName); //$NON-NLS-1$
	}

	/**
	 * @see AbstractTabPanel#initPanel()
	 */
	@Override
	protected void initPanel() {

		setLayout(LayoutFactory.getMigLayout("fill", "fill", "fill")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		playerNamesAndPositions = new HashMap<String, Player>();

		contextPanels = new HashMap<ContextPanelType, JPanel>();

		getActionMap().get(JSkatAction.INVITE_ISS_PLAYER).setEnabled(true);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, getScoreListPanel(), getPlayGroundPanel());
		add(splitPane, "grow"); //$NON-NLS-1$
	}

	private JPanel getScoreListPanel() {

		JPanel panel = new JPanel(LayoutFactory.getMigLayout("fill", "fill", "[shrink][grow]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		JLabel skatListLabel = new JLabel(strings.getString("score_sheet")); //$NON-NLS-1$
		skatListLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
		panel.add(skatListLabel, "wrap, growx, shrinky"); //$NON-NLS-1$

		skatListTableModel = new SkatListTableModel();
		scoreListTable = new JTable(skatListTableModel);

		for (int i = 0; i < scoreListTable.getColumnModel().getColumnCount(); i++) {

			if (i == 3) {

				// game colum is bigger
				scoreListTable.getColumnModel().getColumn(i).setPreferredWidth(40);
			} else {

				scoreListTable.getColumnModel().getColumn(i).setPreferredWidth(20);
			}
		}

		scoreListTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

		scoreListScrollPane = new JScrollPane(scoreListTable);
		scoreListScrollPane.setMinimumSize(new Dimension(150, 100));
		scoreListScrollPane.setPreferredSize(new Dimension(300, 100));
		scoreListScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		panel.add(scoreListScrollPane, "growy"); //$NON-NLS-1$

		return panel;
	}

	/**
	 * Builds the play ground panel
	 * 
	 * @return Play ground panel
	 */
	protected JPanel getPlayGroundPanel() {

		gameInfoPanel = getGameInfoPanel();
		leftOpponentPanel = getOpponentPanel();
		rightOpponentPanel = getOpponentPanel();
		userPanel = createPlayerPanel();
		createGameContextPanel();

		return new PlayGroundPanel(gameInfoPanel, leftOpponentPanel, rightOpponentPanel, gameContextPanel, userPanel);
	}

	private GameInformationPanel getGameInfoPanel() {

		return new GameInformationPanel();
	}

	protected OpponentPanel getOpponentPanel() {

		return new OpponentPanel(getActionMap(), 12, false);
	}

	protected JSkatUserPanel createPlayerPanel() {

		return new JSkatUserPanel(getActionMap(), 12, false);
	}

	protected void addContextPanel(ContextPanelType panelType, JPanel panel) {

		if (contextPanels.containsKey(panelType)) {
			// remove existing panel first
			gameContextPanel.remove(contextPanels.get(panelType));
			contextPanels.remove(panelType);
		}

		contextPanels.put(panelType, panel);
		gameContextPanel.add(panel, panelType.toString());
	}

	private void createGameContextPanel() {

		gameContextPanel = new JPanel();
		gameContextPanel.setOpaque(false);
		gameContextPanel.setLayout(new CardLayout());

		addContextPanel(ContextPanelType.START,
				new StartContextPanel((StartSkatSeriesAction) getActionMap().get(JSkatAction.START_LOCAL_SERIES)));

		biddingPanel = new BiddingContextPanel(getActionMap(), bitmaps, userPanel);
		addContextPanel(ContextPanelType.BIDDING, biddingPanel);

		declaringPanel = new DeclaringContextPanel(getActionMap(), bitmaps, userPanel, 4);
		addContextPanel(ContextPanelType.DECLARING, declaringPanel);

		schieberamschPanel = new SchieberamschContextPanel(getActionMap(), userPanel, 4);
		addContextPanel(ContextPanelType.SCHIEBERAMSCH, schieberamschPanel);

		JPanel trickHoldingPanel = new JPanel(LayoutFactory.getMigLayout("fill", "[shrink][grow][shrink]", //$NON-NLS-1$ //$NON-NLS-2$
				"fill")); //$NON-NLS-1$
		lastTrickPanel = new TrickPanel(0.6, false);
		trickHoldingPanel.add(lastTrickPanel, "width 25%"); //$NON-NLS-1$
		trickPanel = new TrickPanel(0.8, true);
		trickHoldingPanel.add(trickPanel, "grow"); //$NON-NLS-1$

		trickHoldingPanel.add(getRightPanelForTrickPanel(), "width 25%"); //$NON-NLS-1$
		trickHoldingPanel.setOpaque(false);
		addContextPanel(ContextPanelType.TRICK_PLAYING, trickHoldingPanel);

		gameOverPanel = new GameOverPanel(getActionMap(), bitmaps);
		addContextPanel(ContextPanelType.GAME_OVER, gameOverPanel);
	}

	private AbstractHandPanel getPlayerPanel(Player player) {

		AbstractHandPanel result = getHandPanel(player);

		return result;
	}

	protected JPanel getRightPanelForTrickPanel() {
		JPanel blankPanel = new JPanel();
		blankPanel.setOpaque(false);
		return blankPanel;
	}

	/**
	 * Sets player positions
	 * 
	 * @param leftPosition
	 *            Upper left position
	 * @param rightPosition
	 *            Upper right position
	 * @param playerPosition
	 *            Player position
	 */
	public void setPositions(Player leftPosition, Player rightPosition, Player playerPosition) {

		leftOpponentPanel.setPosition(leftPosition);
		rightOpponentPanel.setPosition(rightPosition);
		userPanel.setPosition(playerPosition);

		biddingPanel.setUserPosition(playerPosition);
		trickPanel.setUserPosition(playerPosition);
		lastTrickPanel.setUserPosition(playerPosition);
		gameOverPanel.setUserPosition(playerPosition);

		// FIXME (jansch 09.11.2010) code duplication with
		// BiddingPanel.setPlayerPositions()
		switch (playerPosition) {
		case FOREHAND:
			foreHand = userPanel;
			middleHand = leftOpponentPanel;
			rearHand = rightOpponentPanel;
			break;
		case MIDDLEHAND:
			foreHand = rightOpponentPanel;
			middleHand = userPanel;
			rearHand = leftOpponentPanel;
			break;
		case REARHAND:
			foreHand = leftOpponentPanel;
			middleHand = rightOpponentPanel;
			rearHand = userPanel;
			break;
		}
	}

	/**
	 * Adds a card to a player
	 * 
	 * @param player
	 *            Player
	 * @param card
	 *            Card
	 */
	public void addCard(Player player, Card card) {

		getPlayerPanel(player).addCard(card);
	}

	/**
	 * Adds cards to a player
	 * 
	 * @param player
	 *            Player
	 * @param cards
	 *            Cards
	 */
	public void addCards(Player player, Collection<Card> cards) {

		getPlayerPanel(player).addCards(cards);
	}

	/**
	 * Sets a card played in a trick
	 * 
	 * @param player
	 *            Player position
	 * @param card
	 *            Card
	 */
	public void setTrickCard(Player player, Card card) {

		trickPanel.addCard(player, card);
	}

	/**
	 * Clears trick cards
	 */
	public void clearTrickCards() {

		trickPanel.clearCards();
	}

	/**
	 * Clears last trick cards
	 */
	void clearLastTrickCards() {

		lastTrickPanel.clearCards();
	}

	/**
	 * Removes a card from a player
	 * 
	 * @param player
	 *            Player
	 * @param card
	 *            Card
	 */
	public void removeCard(Player player, Card card) {

		switch (player) {
		case FOREHAND:
			foreHand.removeCard(card);
			break;
		case MIDDLEHAND:
			middleHand.removeCard(card);
			break;
		case REARHAND:
			rearHand.removeCard(card);
			break;
		}
	}

	/**
	 * Removes all cards from a player
	 * 
	 * @param player
	 *            Player
	 */
	public void removeAllCards(Player player) {
		switch (player) {
		case FOREHAND:
			foreHand.removeAllCards();
			break;
		case MIDDLEHAND:
			middleHand.removeAllCards();
			break;
		case REARHAND:
			rearHand.removeAllCards();
			break;
		}
	}

	/**
	 * Clears the hand of a player
	 * 
	 * @param player
	 *            Player
	 */
	public void clearHand(Player player) {

		getPlayerPanel(player).clearHandPanel();
	}

	/**
	 * Sets the game announcement
	 * 
	 * @param player
	 *            Player
	 * @param gameAnnouncement
	 *            Game announcement
	 */
	public void setGameAnnouncement(Player player, GameAnnouncement gameAnnouncement) {

		if (gameAnnouncement.getGameType() == GameType.RAMSCH)
			ramsch = true;

		gameInfoPanel.setGameAnnouncement(gameAnnouncement);

		leftOpponentPanel.setSortGameType(gameAnnouncement.getGameType());
		rightOpponentPanel.setSortGameType(gameAnnouncement.getGameType());
		userPanel.setSortGameType(gameAnnouncement.getGameType());

		if (gameAnnouncement.getGameType() != GameType.PASSED_IN && gameAnnouncement.getGameType() != GameType.RAMSCH) {
			getPlayerPanel(player).setDeclarer(true);
		}

		if (gameAnnouncement.isOuvert()) {
			getPlayerPanel(player).showCards();
		}
	}

	/**
	 * Sets the game state
	 * 
	 * @param state
	 *            Game state
	 */
	public void setGameState(GameState state) {

		log.debug(".setGameState(" + state + ")"); //$NON-NLS-1$ //$NON-NLS-2$

		gameInfoPanel.setGameState(state);

		switch (state) {
		case GAME_START:
			setContextPanel(ContextPanelType.START);
			resetGameData();
			break;
		case DEALING:
			setContextPanel(ContextPanelType.START);
			break;
		case BIDDING:
			setContextPanel(ContextPanelType.BIDDING);
			getActionMap().get(JSkatAction.ANNOUNCE_GAME).setEnabled(false);
			break;
		case RAMSCH_GRAND_HAND_ANNOUNCING:
			setContextPanel(ContextPanelType.SCHIEBERAMSCH);
			userPanel.setGameState(state);
			getActionMap().get(JSkatAction.PLAY_GRAND_HAND).setEnabled(true);
			getActionMap().get(JSkatAction.PLAY_SCHIEBERAMSCH).setEnabled(true);
			getActionMap().get(JSkatAction.SCHIEBEN).setEnabled(false);
			getActionMap().get(JSkatAction.PICK_UP_SKAT).setEnabled(false);
			break;
		case SCHIEBERAMSCH:
			setContextPanel(ContextPanelType.SCHIEBERAMSCH);
			userPanel.setGameState(state);
			getActionMap().get(JSkatAction.PLAY_GRAND_HAND).setEnabled(false);
			getActionMap().get(JSkatAction.PLAY_SCHIEBERAMSCH).setEnabled(false);
			getActionMap().get(JSkatAction.SCHIEBEN).setEnabled(true);
			getActionMap().get(JSkatAction.PICK_UP_SKAT).setEnabled(true);
			break;
		case PICKING_UP_SKAT:
			if (userPanel.getPosition().equals(declarer)) {
				setContextPanel(ContextPanelType.DECLARING);
				userPanel.setGameState(state);
				getActionMap().get(JSkatAction.PICK_UP_SKAT).setEnabled(true);
				getActionMap().get(JSkatAction.ANNOUNCE_GAME).setEnabled(true);
			}
			break;
		case DISCARDING:
			if (userPanel.getPosition().equals(declarer)) {
				userPanel.setGameState(state);
			}
			break;
		case DECLARING:
			if (userPanel.getPosition().equals(declarer)) {
				setContextPanel(ContextPanelType.DECLARING);
			}
			break;
		case TRICK_PLAYING:
			setContextPanel(ContextPanelType.TRICK_PLAYING);
			userPanel.setGameState(state);
			break;
		case CALCULATING_GAME_VALUE:
		case PRELIMINARY_GAME_END:
		case GAME_OVER:
			setContextPanel(ContextPanelType.GAME_OVER);
			break;
		}
	}

	private void resetGameData() {

		playerPassed.put(Player.FOREHAND, Boolean.FALSE);
		playerPassed.put(Player.MIDDLEHAND, Boolean.FALSE);
		playerPassed.put(Player.REARHAND, Boolean.FALSE);
		ramsch = false;
		declarer = null;
	}

	/**
	 * Sets the context panel
	 * 
	 * @param panelType
	 *            Panel type
	 */
	void setContextPanel(ContextPanelType panelType) {

		((CardLayout) gameContextPanel.getLayout()).show(gameContextPanel, panelType.toString());
	}

	/**
	 * Adds a new game result
	 * 
	 * @param gameData
	 *            Game data
	 */
	public void addGameResult(GameSummary summary) {

		gameOverPanel.setGameSummary(summary);

		skatListTableModel.addResult(leftOpponentPanel.getPosition(), rightOpponentPanel.getPosition(),
				userPanel.getPosition(), summary.getDeclarer(), summary);
		scrollSkatListToTheEnd();

		gameInfoPanel.setGameSummary(summary);
	}

	private void scrollSkatListToTheEnd() {
		// scroll skat list if the new result is out of scope
		Rectangle bounds = scoreListTable.getCellRect(skatListTableModel.getRowCount() - 1, 0, true);
		Point loc = bounds.getLocation();
		loc.move(loc.x, loc.y + bounds.height);
		scoreListScrollPane.getViewport().setViewPosition(loc);
	}

	Player getHumanPosition() {

		return userPanel.getPosition();
	}

	/**
	 * Clears the skat table
	 */
	public void clearTable() {

		gameInfoPanel.clear();
		biddingPanel.resetPanel();
		declaringPanel.resetPanel();
		gameOverPanel.resetPanel();
		schieberamschPanel.resetPanel();
		clearHand(Player.FOREHAND);
		clearHand(Player.MIDDLEHAND);
		clearHand(Player.REARHAND);
		clearTrickCards();
		clearLastTrickCards();
		// default sorting is grand sorting
		leftOpponentPanel.setSortGameType(GameType.GRAND);
		rightOpponentPanel.setSortGameType(GameType.GRAND);
		userPanel.setSortGameType(GameType.GRAND);

		resetGameData();
	}

	/**
	 * Sets the fore hand player for the trick
	 * 
	 * @param trickForeHand
	 *            Fore hand player for the trick
	 */
	public void setTrickForeHand(Player trickForeHand) {

		setActivePlayer(trickForeHand);
	}

	/**
	 * Sets the bid value for a player
	 * 
	 * @param player
	 *            Player
	 * @param bidValue
	 *            Bid value
	 * @param madeBid
	 *            TRUE, if the player made the bid<br>
	 *            FALSE, if the player hold the bid
	 */
	public void setBid(Player player, int bidValue, boolean madeBid) {

		log.debug(player + " " + (madeBid ? "bids" : "holds") + ": " + bidValue); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		biddingPanel.setBid(player, bidValue);
		getPlayerPanel(player).setBidValue(bidValue);

		switch (player) {
		case FOREHAND:
			if (playerPassed.get(Player.MIDDLEHAND).booleanValue()) {
				setActivePlayer(Player.REARHAND);
			} else {
				setActivePlayer(Player.MIDDLEHAND);
			}
			break;
		case MIDDLEHAND:
			if (madeBid) {
				setActivePlayer(Player.FOREHAND);
			} else {
				setActivePlayer(Player.REARHAND);
			}
			break;
		case REARHAND:
			if (playerPassed.get(Player.FOREHAND).booleanValue()) {
				setActivePlayer(Player.MIDDLEHAND);
			} else {
				setActivePlayer(Player.FOREHAND);
			}
			break;
		}
	}

	/**
	 * Starts a game
	 */
	public void startGame() {

		clearTable();
	}

	/**
	 * Sets the skat
	 * 
	 * @param skat
	 *            Skat
	 */
	public void setSkat(CardList skat) {

		if (ramsch) {
			schieberamschPanel.setSkat(skat);
		} else {
			declaringPanel.setSkat(skat);
		}
	}

	/**
	 * Takes a card from skat to user panel
	 * 
	 * @param card
	 *            Card
	 */
	public void takeCardFromSkat(Card card) {
		takeCardFromSkat(userPanel, card);
	}

	/**
	 * Takes a card from skat
	 * 
	 * @param player
	 *            Player
	 * @param card
	 *            Card
	 */
	public void takeCardFromSkat(Player player, Card card) {
		takeCardFromSkat(getPlayerPanel(player), card);
	}

	private void takeCardFromSkat(AbstractHandPanel panel, Card card) {

		if (!panel.isHandFull()) {

			declaringPanel.removeCard(card);
			schieberamschPanel.removeCard(card);
			panel.addCard(card);

		} else {

			log.debug("Player panel full!!!"); //$NON-NLS-1$
		}
	}

	/**
	 * Puts a card from the user panel to the skat
	 * 
	 * @param card
	 *            Card
	 */
	public void putCardIntoSkat(Card card) {
		putCardIntoSkat(userPanel, card);
	}

	/**
	 * Puts a card into the skat
	 * 
	 * @param player
	 *            Player
	 * @param card
	 *            Card
	 */
	public void putCardIntoSkat(Player player, Card card) {
		putCardIntoSkat(getPlayerPanel(player), card);
	}

	private void putCardIntoSkat(AbstractHandPanel panel, Card card) {

		if (!declaringPanel.isHandFull()) {

			panel.removeCard(card);
			declaringPanel.addCard(card);
			schieberamschPanel.addCard(card);

		} else {

			log.debug("Discard panel full!!!"); //$NON-NLS-1$
		}
	}

	/**
	 * Clears the skat list
	 */
	public void clearSkatList() {

		skatListTableModel.clearList();
	}

	/**
	 * Sets maximum number of players
	 * 
	 * @param maxPlayers
	 *            Maximum number of players
	 */
	protected void setMaxPlayers(int maxPlayers) {

		skatListTableModel.setPlayerCount(maxPlayers);
	}

	/**
	 * Sets player name
	 * 
	 * @param player
	 *            Player position
	 * @param name
	 *            Player name
	 */
	public void setPlayerName(Player player, String name) {

		playerNamesAndPositions.put(name, player);
		AbstractHandPanel panel = getHandPanel(player);

		if (panel != null) {
			if (name != null) {
				panel.setPlayerName(name);
			}
		}
	}

	/**
	 * Sets player time
	 * 
	 * @param player
	 *            Player position
	 * @param time
	 *            Player time
	 */
	public void setPlayerTime(Player player, double time) {

		AbstractHandPanel panel = getHandPanel(player);

		if (panel != null) {
			panel.setPlayerTime(time);
		}
	}

	/**
	 * Sets player flag for chat enabled yes/no
	 * 
	 * @param playerName
	 *            Player name
	 * @param isChatEnabled
	 *            Flag for chat enabled yes/no
	 */
	public void setPlayerChatEnabled(String playerName, boolean isChatEnabled) {

		AbstractHandPanel panel = getHandPanel(playerName);

		if (panel != null) {
			panel.setChatEnabled(isChatEnabled);
		}
	}

	/**
	 * Sets player flag for ready to play yes/no
	 * 
	 * @param playerName
	 *            Player name
	 * @param isReadyToPlay
	 *            Flag for ready to play yes/no
	 */
	public void setPlayerReadyToPlay(String playerName, boolean isReadyToPlay) {

		AbstractHandPanel panel = getHandPanel(playerName);

		if (panel != null) {
			panel.setReadyToPlay(isReadyToPlay);
		}
	}

	/**
	 * Sets player flag for resign
	 * 
	 * @param player
	 *            Player
	 */
	public void setResign(Player player) {

		AbstractHandPanel panel = getHandPanel(player);

		if (panel != null) {
			panel.setResign(true);
		}
	}

	private AbstractHandPanel getHandPanel(String playerName) {

		AbstractHandPanel panel = null;

		if (playerName.equals(userPanel.getPlayerName())) {
			panel = userPanel;
		} else if (playerName.equals(leftOpponentPanel.getPlayerName())) {
			panel = leftOpponentPanel;
		} else if (playerName.equals(rightOpponentPanel.getPlayerName())) {
			panel = rightOpponentPanel;
		}

		return panel;
	}

	private AbstractHandPanel getHandPanel(Player player) {
		AbstractHandPanel panel = null;

		switch (player) {
		case FOREHAND:
			panel = foreHand;
			break;
		case MIDDLEHAND:
			panel = middleHand;
			break;
		case REARHAND:
			panel = rearHand;
			break;
		}
		return panel;
	}

	/**
	 * Sets the last trick
	 * 
	 * @param trickForeHand
	 * @param foreHandCard
	 * @param middleHandCard
	 * @param rearHandCard
	 */
	public void setLastTrick(Trick trick) {

		lastTrickPanel.clearCards();
		Player trickForeHand = trick.getForeHand();
		lastTrickPanel.addCard(trickForeHand, trick.getFirstCard());
		lastTrickPanel.addCard(trickForeHand.getLeftNeighbor(), trick.getSecondCard());
		lastTrickPanel.addCard(trickForeHand.getRightNeighbor(), trick.getThirdCard());
	}

	/**
	 * Sets the active player
	 * 
	 * @param player
	 *            Active player
	 */
	public void setActivePlayer(Player player) {
		switch (player) {
		case FOREHAND:
			foreHand.setActivePlayer(true);
			middleHand.setActivePlayer(false);
			rearHand.setActivePlayer(false);
			break;
		case MIDDLEHAND:
			foreHand.setActivePlayer(false);
			middleHand.setActivePlayer(true);
			rearHand.setActivePlayer(false);
			break;
		case REARHAND:
			foreHand.setActivePlayer(false);
			middleHand.setActivePlayer(false);
			rearHand.setActivePlayer(true);
			break;
		}
	}

	/**
	 * Sets passing of a player
	 * 
	 * @param player
	 *            Player
	 */
	public void setPass(Player player) {

		log.debug(player + " passes"); //$NON-NLS-1$

		playerPassed.put(player, Boolean.TRUE);

		getPlayerPanel(player).setPass(true);
		biddingPanel.setPass(player);

		switch (player) {
		case FOREHAND:
		case MIDDLEHAND:
			setActivePlayer(Player.REARHAND);
			break;
		case REARHAND:
			if (playerPassed.get(Player.FOREHAND).booleanValue()) {
				setActivePlayer(Player.MIDDLEHAND);
			} else {
				setActivePlayer(Player.FOREHAND);
			}
			break;
		}
	}

	/**
	 * Sets the series state
	 * 
	 * @param state
	 *            Series state
	 */
	public void setSeriesState(SeriesState state) {

		if (SeriesState.SERIES_FINISHED.equals(state)) {

			setContextPanel(ContextPanelType.START);
		}
	}

	/**
	 * Sets the bid value to make
	 * 
	 * @param bidValue
	 *            Bid value
	 */
	public void setBidValueToMake(int bidValue) {

		biddingPanel.setBidValueToMake(bidValue);
	}

	/**
	 * Sets the bid value to hold
	 * 
	 * @param bidValue
	 *            Bid value
	 */
	public void setBidValueToHold(int bidValue) {

		biddingPanel.setBidValueToHold(bidValue);
	}

	@Override
	protected void setFocus() {
		// FIXME (jan 20.11.2010) set active/inactive actions

	}

	/**
	 * Sets the trick number
	 * 
	 * @param trickNumber
	 *            Trick number
	 */
	public void setTrickNumber(int trickNumber) {

		gameInfoPanel.setTrickNumber(trickNumber);
	}

	/**
	 * Sets the game number
	 * 
	 * @param gameNumber
	 *            Game number
	 */
	public void setGameNumber(int gameNumber) {

		gameInfoPanel.setGameNumber(gameNumber);
	}

	/**
	 * Sets the player names
	 * 
	 * @param upperLeftPlayerName
	 * @param upperRightPlayerName
	 * @param lowerPlayerName
	 */
	public void setPlayerNames(String upperLeftPlayerName, String upperRightPlayerName, String lowerPlayerName) {
		// FIXME (jan 26.01.2011) possible code duplication with
		// setPlayerInformation()
		leftOpponentPanel.setPlayerName(upperLeftPlayerName);
		rightOpponentPanel.setPlayerName(upperRightPlayerName);
		userPanel.setPlayerName(lowerPlayerName);
		skatListTableModel.setPlayerNames(upperLeftPlayerName, upperRightPlayerName, lowerPlayerName);
	}

	/**
	 * Gets the declarer player for the table
	 * 
	 * @return Declarer player
	 */
	public Player getDeclarer() {
		return declarer;
	}

	/**
	 * Sets the declarer player for the table
	 * 
	 * @param declarer
	 *            Declarer player
	 */
	public void setDeclarer(Player declarer) {
		this.declarer = declarer;
	}

	/**
	 * Shows the cards of a player
	 * 
	 * @param player
	 *            Player
	 */
	public void showCards(Player player) {

		getPlayerPanel(player).showCards();
	}

	/**
	 * Hides the cards of a player
	 * 
	 * @param player
	 *            Player
	 */
	public void hideCards(Player player) {

		getPlayerPanel(player).hideCards();
	}

	/**
	 * Sets the schieben of a player
	 * 
	 * @param player
	 *            Player position
	 */
	public void setGeschoben(Player player) {
		getPlayerPanel(player).setGeschoben();
	}

	/**
	 * Sets the discarded skat
	 * 
	 * @param player
	 *            Player
	 * @param skatBefore
	 *            Skat before discarding
	 * @param discardedSkat
	 *            Skat after discarding
	 */
	public void setDiscardedSkat(Player player, CardList skatBefore, CardList discardedSkat) {
		AbstractHandPanel playerPanel = getPlayerPanel(player);

		for (int i = 0; i < 2; i++) {
			Card skatCard = skatBefore.get(i);
			takeCardFromSkat(player, skatCard);
		}
		for (int i = 0; i < 2; i++) {
			Card skatCard = discardedSkat.get(i);
			putCardIntoSkat(player, skatCard);
		}
	}
}
