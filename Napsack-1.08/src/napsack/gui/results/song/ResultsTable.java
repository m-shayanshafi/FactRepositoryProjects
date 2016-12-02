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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.table.JTableHeader;

import napsack.util.SongSorter;
import napsack.util.StringUtils;
import napsack.util.model.SongField;
import napsack.util.properties.DelimiterProperty;
import napsack.util.properties.Property;
import napsack.util.properties.SongSorterProperty;
import napsack.util.properties.TableColumnsProperty;

public class ResultsTable extends JTable {
	private final ResultsTableModel resultsTableModel;

	public ResultsTable() {
		super();
		resultsTableModel = new ResultsTableModel();

		final JTableHeader tableHeader_ = getTableHeader();

		setModel(resultsTableModel);
		setAutoResizeMode(AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		setShowGrid(false);

		// Comment out the lines between <comment> and </comment> tags for
      // Java 1.2.2.  This will prevent Napsack from bolding column headers
		// to indicate sorting order.

      // <comment>
		tableHeader_.setDefaultRenderer(new ResultsTableHeaderRenderer(tableHeader_.getDefaultRenderer()));
		// </comment>

      tableHeader_.addMouseListener(new MouseAdapter() {
         public void mouseClicked(final MouseEvent mouseEvent_) {
            if (mouseEvent_.getClickCount() == 1) {
					final int columnIndex_ = getColumnModel().getColumnIndexAtX(mouseEvent_.getX());

					if (columnIndex_ >= 0) {
						final SongField selectedField_ = SongField.getSongField(getColumnName(columnIndex_));
						final Property songSorterProperty_ = SongSorterProperty.getInstance();
            		final SongSorter currentSorter_ = (SongSorter) songSorterProperty_.getValue();
            		final SongField[] currentFields_ =  currentSorter_.getSortingFields();
            		String sortingColumnFieldString_;

            		if (currentFields_.length == 1 && currentFields_[0].equals(selectedField_) && currentSorter_.getOrder() == SongSorter.NATURAL_ORDER) {
               		sortingColumnFieldString_ = SongSorterProperty.REVERSE_MODIFIER + selectedField_.getDescription();
            		} else {
               		sortingColumnFieldString_ = selectedField_.getDescription();
            		}

            		songSorterProperty_.setProperty(sortingColumnFieldString_);
            	}
				}
         }
      });
	}

	public void columnMoved(final TableColumnModelEvent tableColumnModelEvent_) {
		super.columnMoved(tableColumnModelEvent_);

		TableColumnsProperty.getInstance().setProperty(StringUtils.join(getColumns(), ((String) DelimiterProperty.getInstance().getValue()).substring(0, 1)));
	};

	public SongField[] getColumns() {
		final SongField[] columns_ = new SongField[getColumnCount()];

		for (int i = 0; i < columns_.length; ++i) {
			columns_[i] = SongField.getSongField(getColumnName(i));
		}

		return columns_;
	}

	public ResultsTableModel getResultsTableModel() {
		return resultsTableModel;
	}

	public void reset() {
		getResultsTableModel().setResults(null);
	}
}

