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

package napsack.gui.results.song;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import napsack.util.SongSorter;
import napsack.util.model.SongField;
import napsack.util.properties.SongSorterProperty;

public class ResultsTableHeaderRenderer implements TableCellRenderer {
	private final TableCellRenderer tableCellRenderer;
	public ResultsTableHeaderRenderer(final TableCellRenderer tableCellRenderer_) {
		tableCellRenderer = tableCellRenderer_;
	}

	private TableCellRenderer getTableCellRenderer() {
		return tableCellRenderer;
	}

	public Component getTableCellRendererComponent(JTable table_, Object value_, boolean isSelected_, boolean hasFocus_, int rowIndex_, int columnIndex_) {
		final Component component_ = getTableCellRenderer().getTableCellRendererComponent(table_, value_, isSelected_, hasFocus_, rowIndex_, columnIndex_);

		if (rowIndex_ == -1) {
			try {
			final SongSorter currentSongSorter_ = (SongSorter) SongSorterProperty.getInstance().getValue();
			final SongField[] currentSortingFields_ = currentSongSorter_.getSortingFields();

			if (currentSortingFields_.length == 1 && currentSortingFields_[0].equals(SongField.getSongField(((JLabel) component_).getText()))) {
				final Font currentFont_ = component_.getFont();

				if (currentSongSorter_.getOrder() == SongSorter.NATURAL_ORDER) {
					component_.setFont(new Font(currentFont_.getName(), Font.BOLD, currentFont_.getSize()));
				} else {
					component_.setFont(new Font(currentFont_.getName(), Font.BOLD | Font.ITALIC, currentFont_.getSize()));
				}
			}
			} catch (ClassCastException classCastException_) {
			}
		}

		return component_;
	}
}

