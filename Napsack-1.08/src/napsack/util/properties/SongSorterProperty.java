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

import napsack.util.SongSorter;
import napsack.util.model.SongField;

public class SongSorterProperty extends Property {
	public final static String REVERSE_MODIFIER = "-";
	private final static String NAME = "sort_by";
	private final static SongField[] DEFAULT_VALUE = new SongField[0];

   private static Property instance;

   public static Property getInstance() {
      if (instance == null) {
			synchronized (SongSorterProperty.class) {
				if (instance == null) {
         		instance = new SongSorterProperty();
				}
			}
      }

      return instance;
   }

   private SongSorterProperty() {
   }

	public String getName() {
		return NAME;
	}

	protected Object parseProperty() {
		final String[] fieldNames_ = getStrings();
		SongField[] sortingFields_ = null;
		int order_ = SongSorter.NATURAL_ORDER;

		if (fieldNames_ != null) {
			final int reverseLength_ = REVERSE_MODIFIER.length();
			sortingFields_ = new SongField[fieldNames_.length];

			if (fieldNames_.length == 1 && fieldNames_[0].length() >= reverseLength_ && fieldNames_[0].substring(0, reverseLength_).equals(REVERSE_MODIFIER)) {
				order_ = SongSorter.REVERSE_ORDER;
				fieldNames_[0] = fieldNames_[0].substring(reverseLength_);
			}

			for (int i = 0; i < sortingFields_.length; ++i) {
				sortingFields_[i] = SongField.getSongField(fieldNames_[i]);
			}
		} else {
			sortingFields_ = DEFAULT_VALUE;
		}

		return new SongSorter(sortingFields_, order_);
	}

	public void validate(final String property_) throws PropertyException {
		final String[] sortingFields_ = getStrings(property_);
		final int reverseLength_ = REVERSE_MODIFIER.length();

		if (sortingFields_ != null) {
			if (sortingFields_.length == 1 && sortingFields_[0].length() >= reverseLength_ && sortingFields_[0].substring(0, reverseLength_).equals(REVERSE_MODIFIER)) {
				sortingFields_[0] = sortingFields_[0].substring(reverseLength_);
			}

			for (int i = 0; i < sortingFields_.length; ++i) {	
				if (SongField.getSongField(sortingFields_[i]) == null) {
					throw new EnumerationException("Invalid value for " + getName() + "; valid sort-by values are ", SongField.getValidSongFields());
				}
			}
		}
	}
}

