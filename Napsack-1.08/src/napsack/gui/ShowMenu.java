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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;

import napsack.gui.results.song.ResultsTable;
import napsack.gui.results.song.ResultsTableModel;
import napsack.util.StringUtils;
import napsack.util.model.SongField;
import napsack.util.properties.DelimiterProperty;
import napsack.util.properties.Property;
import napsack.util.properties.TableColumnsProperty;

public class ShowMenu extends NapsackMenu {
	private final static String SHOW_MENU = "Show";
	private final static char SHOW_MNEMONIC = 'S';

	public ShowMenu(final MainFrame mainFrame_) {
		super(mainFrame_, SHOW_MENU, SHOW_MNEMONIC);
	}

	protected void buildMenu() {
		final SongField[] songFields_ = SongField.getValidSongFields();
		final Property tableColumnsProperty_ = TableColumnsProperty.getInstance();
		final SongField[] columns_ = (SongField[]) tableColumnsProperty_.getValue();
		final SongField[] tempColumns_ = new SongField[columns_.length];

		System.arraycopy(columns_, 0, tempColumns_, 0, columns_.length);

		final ActionListener actionListener_ = new ActionListener() {
			public void actionPerformed(final ActionEvent actionEvent_) {
				final JCheckBoxMenuItem checkBoxMenuItem_ = (JCheckBoxMenuItem) actionEvent_.getSource();
				final boolean selected_ = checkBoxMenuItem_.isSelected();
				final SongField changedColumn_ = SongField.getSongField(checkBoxMenuItem_.getText());
				final SongField[] currentColumns_ = (SongField[]) tableColumnsProperty_.getValue();
				final List newColumns_ = new ArrayList(currentColumns_.length + (selected_ ? 1 : -1));

				if (selected_) {
					for (int i = 0; i < currentColumns_.length ; ++i) {
						newColumns_.add(currentColumns_[i]);
					}

					newColumns_.add(changedColumn_);
				} else {
					for (int i = 0; i < currentColumns_.length; ++i) {
						if (!currentColumns_[i].equals(changedColumn_)) {
							newColumns_.add(currentColumns_[i]);
						}
					}
				}

				tableColumnsProperty_.setProperty(StringUtils.join(newColumns_.toArray(), ((String) DelimiterProperty.getInstance().getValue()).substring(0, 1)));
			}
		};

		Arrays.sort(tempColumns_);

		for (int i = 0; i < songFields_.length; ++i) {
			final JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem(songFields_[i].getDescription(), Arrays.binarySearch(tempColumns_, songFields_[i]) >= 0);
			checkBoxMenuItem.setMnemonic(songFields_[i].getMnemonic());
			checkBoxMenuItem.addActionListener(actionListener_);

			add(checkBoxMenuItem);
		}
	}
}

