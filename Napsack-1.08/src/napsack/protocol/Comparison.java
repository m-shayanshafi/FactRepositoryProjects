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

package napsack.protocol;

import java.util.Arrays;
import java.util.Hashtable;

public class Comparison implements Comparable {
	private final static Hashtable COMPARISONS = new Hashtable(3, 1.0f);
	private final static Comparison[] COMPARISON_ARRAY = new Comparison[0];

	public final static Comparison AT_LEAST = new Comparison("\"AT LEAST\"", "At Least");
	public final static Comparison AT_MOST = new Comparison("\"AT BEST\"", "At Most");
	public final static Comparison EQUAL_TO = new Comparison("\"EQUAL TO\"", "Equal To");

	public static Comparison getComparison(final String key_) {
		return (Comparison) COMPARISONS.get(key_);
	}

	public static Comparison[] getValidComparisons() {
		final Comparison[] validComparisons_ = (Comparison[]) COMPARISONS.values().toArray(COMPARISON_ARRAY);
		Arrays.sort(validComparisons_);

		return validComparisons_;
	}

	private String code;
	private String description;

	private Comparison(final String code_, final String description_) {
		code = code_;
		description = description_;

		COMPARISONS.put(description_, this);
	}

	public int compareTo(final Object object_) {
		if (this == object_) {
			return 0;
		}
		
		if (getClass() != object_.getClass()) {
			throw new ClassCastException();
		}

		final Comparison comparison_ = (Comparison) object_;
		final int compare_ = getDescription().compareTo(comparison_.getDescription());

		return compare_ == 0 ? getCode().compareTo(comparison_.getCode()) : compare_;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public String toString() {
		return getDescription();
	}
}

