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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ActionMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jskat.data.GameAnnouncement;
import org.jskat.data.GameAnnouncement.GameAnnouncementFactory;
import org.jskat.data.JSkatOptions;
import org.jskat.gui.AbstractI18NComboBoxRenderer;
import org.jskat.gui.LayoutFactory;
import org.jskat.gui.action.JSkatAction;
import org.jskat.util.CardList;
import org.jskat.util.GameType;
import org.jskat.util.JSkatResourceBundle;

/**
 * Holds widgets for announcing a game
 */
class GameAnnouncePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	static Log log = LogFactory.getLog(GameAnnouncePanel.class);

	JSkatResourceBundle strings;
	JSkatOptions options;

	JComboBox gameTypeList = null;
	JCheckBox handBox = null;
	JCheckBox ouvertBox = null;
	JCheckBox schneiderBox = null;
	JCheckBox schwarzBox = null;

	DiscardPanel discardPanel;
	JSkatUserPanel userPanel;

	/**
	 * Constructor
	 * 
	 * @param actions
	 *            Action map
	 * @param userPanel
	 *            User panel
	 * @param discardPanel
	 *            Discard panel
	 */
	GameAnnouncePanel(ActionMap actions, JSkatUserPanel userPanel, DiscardPanel discardPanel) {

		strings = JSkatResourceBundle.instance();
		this.userPanel = userPanel;
		this.discardPanel = discardPanel;

		initPanel(actions);
	}

	private void initPanel(final ActionMap actions) {

		this.setLayout(LayoutFactory.getMigLayout("fill")); //$NON-NLS-1$

		JPanel panel = new JPanel(LayoutFactory.getMigLayout("fill")); //$NON-NLS-1$

		this.gameTypeList = new JComboBox();
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		model.addElement(GameType.GRAND);
		model.addElement(GameType.CLUBS);
		model.addElement(GameType.SPADES);
		model.addElement(GameType.HEARTS);
		model.addElement(GameType.DIAMONDS);
		model.addElement(GameType.NULL);
		this.gameTypeList.setModel(model);
		gameTypeList.setRenderer(new GameTypeComboBoxRenderer());
		gameTypeList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// FIXME (jan 28.11.2010) send sorting game type to JSkatMaster
				// --> more view components can benefit from this
				GameType gameType = (GameType) gameTypeList.getSelectedItem();

				if (gameType != null) {
					userPanel.setSortGameType(gameType);
				}
			}
		});
		this.gameTypeList.setSelectedIndex(-1);

		handBox = new JCheckBox(strings.getString("hand")); //$NON-NLS-1$
		handBox.setEnabled(false);
		this.ouvertBox = new JCheckBox(strings.getString("ouvert")); //$NON-NLS-1$
		this.schneiderBox = new JCheckBox(strings.getString("schneider")); //$NON-NLS-1$
		this.schwarzBox = new JCheckBox(strings.getString("schwarz")); //$NON-NLS-1$

		panel.add(this.gameTypeList, "grow, wrap"); //$NON-NLS-1$
		panel.add(handBox, "wrap"); //$NON-NLS-1$
		panel.add(this.ouvertBox, "wrap"); //$NON-NLS-1$
		panel.add(this.schneiderBox, "wrap"); //$NON-NLS-1$
		panel.add(this.schwarzBox, "wrap"); //$NON-NLS-1$

		final JButton announceButton = new JButton(actions.get(JSkatAction.ANNOUNCE_GAME));
		announceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (gameTypeList.getSelectedItem() != null) {

					try {
						GameAnnouncement ann = getGameAnnouncement();
						if (ann == null)
							return;
						e.setSource(ann);
						// fire event again
						announceButton.dispatchEvent(e);
					} catch (IllegalArgumentException except) {
						log.error(except.getMessage());
					}
				}
			}

			private GameAnnouncement getGameAnnouncement() {
				GameAnnouncementFactory factory = GameAnnouncement.getFactory();
				GameType gameType = getGameTypeFromSelectedItem();
				factory.setGameType(gameType);

				if (discardPanel.isUserLookedIntoSkat()) {

					CardList discardedCards = discardPanel.getDiscardedCards();
					if (discardedCards.size() != 2) {
						JOptionPane.showMessageDialog(GameAnnouncePanel.this,
								strings.getString("invalid_number_of_cards_in_skat"), //$NON-NLS-1$
								strings.getString("invalid_number_of_cards_in_skat_title"), //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
						return null;
					}
					factory.setDiscardedCards(discardedCards);
					if (GameType.NULL.equals(gameType) && ouvertBox.isSelected()) {
						factory.setOuvert(true);
					}
				} else {

					if (handBox.isSelected()) {
						factory.setHand(Boolean.TRUE);
					}
					if (ouvertBox.isSelected()) {
						factory.setOuvert(Boolean.TRUE);
					}
					if (schneiderBox.isSelected()) {
						factory.setSchneider(Boolean.TRUE);
					}
					if (schwarzBox.isSelected()) {
						factory.setSchwarz(Boolean.TRUE);
					}
				}
				return factory.getAnnouncement();
			}

			private GameType getGameTypeFromSelectedItem() {
				Object selectedItem = gameTypeList.getSelectedItem();

				return (GameType) selectedItem;
			}
		});
		panel.add(announceButton);

		add(panel, "center"); //$NON-NLS-1$

		setOpaque(false);

		resetPanel();
	}

	void resetPanel() {

		this.gameTypeList.setSelectedIndex(-1);
		handBox.setSelected(true);
		this.ouvertBox.setSelected(false);
		this.schneiderBox.setSelected(false);
		this.schwarzBox.setSelected(false);
	}

	private class GameTypeComboBoxRenderer extends AbstractI18NComboBoxRenderer {

		private static final long serialVersionUID = 1L;

		GameTypeComboBoxRenderer() {
			super();
		}

		@Override
		public String getValueText(Object value) {

			String result = " "; //$NON-NLS-1$

			GameType gameType = (GameType) value;

			if (gameType != null) {
				result = strings.getGameType(gameType);
			}

			return result;
		}
	}

	void setUserPickedUpSkat(boolean isUserPickedUpSkat) {

		if (isUserPickedUpSkat) {
			handBox.setSelected(false);
		} else {
			handBox.setSelected(true);
		}
	}
}
