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

import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jskat.data.GameSummary;
import org.jskat.data.Trick;
import org.jskat.gui.LayoutFactory;
import org.jskat.gui.img.JSkatGraphicRepository;
import org.jskat.util.Player;

/**
 * Holds all informations about a game at the end
 */
class GameResultPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(GameResultPanel.class);

	private Player userPosition;
	private List<TrickPanel> trickPanelList;

	/**
	 * Game result panel
	 */
	GameResultPanel(JSkatGraphicRepository bitmaps) {

		initPanel(bitmaps);
	}

	private void initPanel(JSkatGraphicRepository bitmaps) {

		setLayout(LayoutFactory.getMigLayout("fill", "fill", "fill")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		trickPanelList = new ArrayList<TrickPanel>();
		for (int i = 0; i < 10; i++) {

			trickPanelList.add(new TrickPanel(1.0, false));
		}

		JPanel trickPanel = new JPanel(LayoutFactory.getMigLayout("fill", "fill", "fill")); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		for (TrickPanel panel : trickPanelList) {
			trickPanel.add(panel);
		}
		trickPanel.setOpaque(false);

		add(trickPanel, "grow"); //$NON-NLS-1$

		setOpaque(false);
	}

	public void setGameSummary(GameSummary summary) {

		List<Trick> tricks = summary.getTricks();

		log.debug("Trick size: " + tricks.size()); //$NON-NLS-1$

		for (int i = 0; i < 10; i++) {

			TrickPanel trickPanel = trickPanelList.get(i);
			trickPanel.clearCards();

			if (i < tricks.size()) {
				Trick trick = tricks.get(i);
				if (trick != null) {
					trickPanel.setUserPosition(userPosition);
					trickPanel.addCard(trick.getForeHand(), trick.getFirstCard());
					trickPanel.addCard(trick.getForeHand().getLeftNeighbor(), trick.getSecondCard());
					trickPanel.addCard(trick.getForeHand().getRightNeighbor(), trick.getThirdCard());
				}
			}
		}
	}

	public void setUserPosition(Player newUserPosition) {

		userPosition = newUserPosition;
	}

	public void resetPanel() {

		for (TrickPanel panel : trickPanelList) {

			panel.clearCards();
		}
	}
}
