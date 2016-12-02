// Written by Chris Redekop <waveform@users.sourceforge.net>
// Released under the conditions of the GPL (See below).
//
// Thanks to Travis Whitton for the Gnap Fetch code used by Napsack.
//
// Also thanks to Radovan Garabik for the pynap code used by Napsack.
//
// Napsack - a specialized client for launching cross-server Napster queries.
// Copyright (C) 2000-2002 Chris Redekop
// 
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 2
// of the License, or (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA

package napsack.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import napsack.util.Switch;
import napsack.util.properties.ErrorMessagesProperty;
import napsack.util.properties.FlipResultsTreeProperty;
import napsack.util.properties.Property;

public class ViewMenu extends NapsackMenu {
	private final static String VIEW_MENU = "View";
	private final static String FLIP_ITEM = "Flip Results Tree";
	private final static String PROBLEMS_ITEM = "Problems...";

	private final static char VIEW_MNEMONIC = 'V';
	private final static char FLIP_MNEMONIC	 = 'F';
	private final static char PROBLEMS_MNEMONIC = 'P';

	private ProblemsDialog problemsDialog;

	public ViewMenu(final MainFrame mainFrame_) {
		super(mainFrame_, VIEW_MENU, VIEW_MNEMONIC);
	}

	protected void buildMenu() {
		final JMenuItem problems_ = new JMenuItem(PROBLEMS_ITEM);
		final JMenuItem flipResultsTree_ = new JCheckBoxMenuItem(FLIP_ITEM);
		final Property flipResultsTreeProperty_ = FlipResultsTreeProperty.getInstance();

		flipResultsTree_.setMnemonic(FLIP_MNEMONIC);
		problems_.setMnemonic(PROBLEMS_MNEMONIC);

		add(flipResultsTree_);
		add(new ShowMenu(getMainFrame()));
		add(new SortByMenu(getMainFrame()));
		addSeparator();
		add(problems_);

		flipResultsTree_.setSelected(((Boolean) flipResultsTreeProperty_.getValue()).booleanValue());

		flipResultsTree_.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent actionEvent_) {
				flipResultsTreeProperty_.setProperty((((JCheckBoxMenuItem) actionEvent_.getSource()).isSelected() ? Switch.ON : Switch.OFF).toString());
			}
		});

		problems_.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent actionEvent_) {
				getProblemsDialog().setVisible(true);
			}
		});
	}

	public ProblemsDialog getProblemsDialog() {
		if (problemsDialog == null) {
			problemsDialog = new ProblemsDialog(getMainFrame());
		}

		return problemsDialog;
	}
}

