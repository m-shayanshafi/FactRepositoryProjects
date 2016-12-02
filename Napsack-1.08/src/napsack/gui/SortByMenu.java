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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButtonMenuItem;

import napsack.util.SongSorter;
import napsack.util.StringUtils;
import napsack.util.model.SongField;
import napsack.util.properties.Property;
import napsack.util.properties.SongSorterProperty;

public class SortByMenu extends NapsackMenu {
	private final static String SORT_BY_MENU = "Sort By";
	private final static char SORT_BY_MNEMONIC = 'B';

	public SortByMenu(final MainFrame mainFrame_) {
		super(mainFrame_, SORT_BY_MENU, SORT_BY_MNEMONIC);
	}

	protected void buildMenu() {
		final Property songSorterProperty_ = SongSorterProperty.getInstance();
		final SongField[] songFields_ = SongField.getValidSongFields();
		final SongField[] sortingFields_ = ((SongSorter) songSorterProperty_.getValue()).getSortingFields();
		final ButtonGroup buttonGroup_ = new ButtonGroup();
		final Map radioButtonMenuItems_ = new HashMap(sortingFields_.length, 1.0f);

		final ActionListener actionListener_ = new ActionListener() {
         public void actionPerformed(final ActionEvent actionEvent_) {
				final SongField selectedField_ = SongField.getSongField(((JRadioButtonMenuItem) actionEvent_.getSource()).getText());
				final SongSorter currentSorter_ = (SongSorter) songSorterProperty_.getValue();
				final SongField[] currentFields_ =  currentSorter_.getSortingFields();
				String sortingColumnFieldString_;

				if (currentFields_.length == 1 && currentFields_[0].equals(selectedField_) && currentSorter_.getOrder() == SongSorter.NATURAL_ORDER) {
					sortingColumnFieldString_ = SongSorterProperty.REVERSE_MODIFIER + selectedField_.getDescription();
				} else {
					sortingColumnFieldString_ = selectedField_.getDescription();
				}

				songSorterProperty_.setProperty(sortingColumnFieldString_);

				getMainFrame().getResultsPane().getResultsTable().getTableHeader().repaint();
         }
      };

		for (int i = 0; i < songFields_.length; ++i) {
			final String fieldDescription_ = songFields_[i].getDescription();
			final JRadioButtonMenuItem radioButtonMenuItem_ = new JRadioButtonMenuItem(fieldDescription_, sortingFields_.length == 1 && sortingFields_[0].equals(songFields_[i]));
			radioButtonMenuItem_.setMnemonic(songFields_[i].getMnemonic());
			radioButtonMenuItem_.addActionListener(actionListener_);
			radioButtonMenuItems_.put(fieldDescription_, radioButtonMenuItem_);

			buttonGroup_.add(radioButtonMenuItem_);
			add(radioButtonMenuItem_);
		}

		songSorterProperty_.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent propertyChangeEvent_) {
				final SongField[] newSortingFields_ = ((SongSorter) songSorterProperty_.getValue()).getSortingFields();

				if (newSortingFields_.length == 1) {
					((JRadioButtonMenuItem) radioButtonMenuItems_.get(newSortingFields_[0].getDescription())).setSelected(true);
				}
			}
		});
	}
}

