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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

import napsack.util.SongSorter;
import napsack.util.StringUtils;
import napsack.util.model.SongField;
import napsack.util.model.SongQueryResults;
import napsack.util.properties.DelimiterProperty;
import napsack.util.properties.Property;
import napsack.util.properties.SongSorterProperty;
import napsack.util.properties.TableColumnsProperty;

public class ResultsTableModel extends AbstractTableModel {
	private SongField[] columns;
	private SongQueryResults results;
	private SongSorter songSorter;

	public ResultsTableModel() {
		final Property tableColumnsProperty_ = TableColumnsProperty.getInstance();
		final Property songSorterProperty_ = SongSorterProperty.getInstance();

		tableColumnsProperty_.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent propertyChangeEvent_) {
				final SongField[] newColumns_ = (SongField[]) tableColumnsProperty_.getValue();
				final SongField[] currentColumns_ = getColumns();

				if (newColumns_.length != currentColumns_.length) {
					setColumns(newColumns_);
				} else {
					final SongField[] tempNewColumns_ = new SongField[newColumns_.length];
					final SongField[] tempCurrentColumns_ = new SongField[currentColumns_.length];
					
					System.arraycopy(newColumns_, 0, tempNewColumns_, 0, newColumns_.length);
					System.arraycopy(currentColumns_, 0, tempCurrentColumns_, 0, currentColumns_.length);

					Arrays.sort(tempNewColumns_);
					Arrays.sort(tempCurrentColumns_);

					if (!(Arrays.equals(tempNewColumns_, tempCurrentColumns_))) {
						setColumns(newColumns_);
					}
				}
			}
		});

		songSorterProperty_.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent propertyChangeEvent_) {
				setSongSorter((SongSorter) songSorterProperty_.getValue());
			}
		});
	}

	public Class getColumnClass(final int columnIndex_) {
		final SongField[] columns_ = getColumns();

		return columns_ == null ? null : columns_[columnIndex_].getColumnClass();
	}

	public int getColumnCount() {
		final SongField[] columns_ = getColumns();

		return columns_ == null ? 0 : columns_.length;
	}

	public String getColumnName(final int columnIndex_) {
		final SongField[] columns_ = getColumns();

		return columns_ == null ? "" : columns_[columnIndex_].toString();
	}

	public SongField[] getColumns() {
		return columns;
	}

	public SongQueryResults getResults() {
		return results;
	}

	public int getRowCount() {
		final SongQueryResults results_ = getResults();

		return results_ == null ? 0 : results_.getSongs().length;
	}

	public SongSorter getSongSorter() {
		return songSorter;
	}

	public Object getValueAt(final int rowIndex_, final int columnIndex_) {
		final SongQueryResults results_ = getResults();
		final SongField[] columns_ = getColumns();
		Object object_ = null;

		if (results_ != null && columns_ != null) {
			object_ = columns_[columnIndex_].get(results_.getSongs()[rowIndex_].getView());
		}

		return object_;
	}

	public void setColumns(final SongField[] columns_) {
		columns = columns_;

		fireTableChanged(new TableModelEvent(this, TableModelEvent.HEADER_ROW));
	}

	public void setResults(final SongQueryResults results_) {
		if (results_ != null) {
			final SongSorter songSorter_ = getSongSorter();
			if (songSorter_ != null) {
				results_.setSongSorter((SongSorter) songSorter_.clone());
			}
		}

		results = results_;
		fireTableChanged(new TableModelEvent(this));
	}

	public void setSongSorter(final SongSorter songSorter_) {
		if (songSorter_ != null) {
			final SongQueryResults results_ = getResults();

			if (results_ != null) {
				results_.setSongSorter((SongSorter) songSorter_.clone());
				fireTableChanged(new TableModelEvent(this, 0, results_.getSongs().length));
			}
		}

		songSorter = songSorter_;
	}
}

