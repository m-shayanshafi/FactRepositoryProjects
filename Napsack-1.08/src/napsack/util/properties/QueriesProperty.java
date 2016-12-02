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

public class QueriesProperty extends Property {
	private final static String NAME = "queries";
	private final static String[] DEFAULT_VALUE = new String[0];

   private static Property instance;

   public static Property getInstance() {
      if (instance == null) {
			synchronized (QueriesProperty.class) {
				if (instance == null) {
         		instance = new QueriesProperty();
				}
			}
      }

      return instance;
   }

   private QueriesProperty() {
   }

	public String getName() {
		return NAME;
	}

	protected Object parseProperty() {
		String[] queries_ = getStrings();

		if (queries_ == null) {
			queries_ = DEFAULT_VALUE;
		}

		return queries_;
	}

	public void validate(final String property_) throws PropertyException {
		final String[] queries_ = getStrings(property_);

		if (queries_ == null || queries_.length == 0) {
			throw new EmptyQueriesPropertyException("No query specified.");
		}

		for (int i = 0; i < queries_.length; ++i) {	
			if (queries_[i].trim().length() == 0) {
				throw new PropertyException("Empty query specified.");
			}

			if (queries_[i].indexOf('\"') != -1) {
				throw new PropertyException("Query \"" + queries_[i] + "\" contains an invalid \'\"\' character.");
			}
		}
	}
}

