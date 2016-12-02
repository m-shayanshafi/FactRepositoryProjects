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

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.jskat.data.GameSummary;
import org.jskat.gui.LayoutFactory;
import org.jskat.gui.action.JSkatAction;
import org.jskat.gui.img.JSkatGraphicRepository;
import org.jskat.util.Player;

class GameOverPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private GameResultPanel gameResultPanel;

	public GameOverPanel(ActionMap actions, JSkatGraphicRepository bitmaps) {

		initPanel(actions, bitmaps);
	}

	private void initPanel(ActionMap actions, JSkatGraphicRepository bitmaps) {

		this.setLayout(LayoutFactory.getMigLayout("fill", "fill", "fill")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		JPanel panel = new JPanel(LayoutFactory.getMigLayout("fill", "fill", "[grow][shrink]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		gameResultPanel = new GameResultPanel(bitmaps);
		panel.add(gameResultPanel, "grow, wrap"); //$NON-NLS-1$

		JPanel buttonPanel = new JPanel(LayoutFactory.getMigLayout("fill")); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		buttonPanel.add(new JButton(actions.get(JSkatAction.CONTINUE_LOCAL_SERIES)), "center, shrink"); //$NON-NLS-1$
		buttonPanel.setOpaque(false);
		panel.add(buttonPanel, "center"); //$NON-NLS-1$

		panel.setOpaque(false);
		this.add(panel, "center"); //$NON-NLS-1$

		setOpaque(false);
	}

	void setUserPosition(Player player) {

		gameResultPanel.setUserPosition(player);
	}

	void setGameSummary(GameSummary summary) {

		gameResultPanel.setGameSummary(summary);
	}

	public void resetPanel() {

		gameResultPanel.resetPanel();
	}
}
