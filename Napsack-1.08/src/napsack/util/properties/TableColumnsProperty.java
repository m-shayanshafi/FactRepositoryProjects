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

package napsack.util.properties;

import napsack.util.model.SongField;

public class TableColumnsProperty extends Property {
	private final static String NAME = "table_columns";
	private final static SongField[] DEFAULT_VALUE = {SongField.FILE_NAME, SongField.CONNECTION, SongField.USER, SongField.SIZE, SongField.LENGTH, SongField.BIT_RATE};

	private static Property instance;

   public static Property getInstance() {
      if (instance == null) {
			synchronized (TableColumnsProperty.class) {
				if (instance == null) {
         		instance = new TableColumnsProperty();
				}
			}
      }

      return instance;
   }

   private TableColumnsProperty() {
   }

	public String getName() {
		return NAME;
	}
	
	protected Object parseProperty() {
		final String[] tableColumnStrings_ = getStrings();
		SongField[] tableColumns_ = null;

		if (tableColumnStrings_ != null) {
			tableColumns_ = new SongField[tableColumnStrings_.length];
			
			for (int i = 0; i < tableColumnStrings_.length; ++i) {
				tableColumns_[i] = SongField.getSongField(tableColumnStrings_[i]);
			}
		} else {
			tableColumns_ = DEFAULT_VALUE;
		}

		return tableColumns_;
	}

	public void validate(final String property_) throws PropertyException {
		if (property_ != null) {
			final String[] tableColumnStrings_ = getStrings(property_);

			for (int i = 0; i < tableColumnStrings_.length; ++i) {
				if (SongField.getSongField(tableColumnStrings_[i]) == null) {
					throw new EnumerationException("Invalid column value for " + getName() + "; valid values are", SongField.getValidSongFields());
				}
			}
		}
	}
}

