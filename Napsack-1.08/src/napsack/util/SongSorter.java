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

package napsack.util;

import java.util.Arrays;
import java.util.Comparator;

import napsack.util.model.Song;
import napsack.util.model.SongField;

public class SongSorter implements Comparator {
	public final static int NATURAL_ORDER = 1;
	public final static int REVERSE_ORDER = -1;

	private final SongField[] sortingFields;
	private volatile int order;

	public SongSorter(final SongField[] sortingFields_) {
		this(sortingFields_, NATURAL_ORDER);
	}

	public SongSorter(final SongField[] sortingFields_, final int order_) {
		sortingFields = sortingFields_;
		order = order_;
	}

	public Object clone() {
		final SongField[] sortingFields_ = getSortingFields();
		final SongField[] newSortingFields_ = new SongField[sortingFields_.length];

		System.arraycopy(sortingFields, 0, newSortingFields_, 0, sortingFields_.length);

		return new SongSorter(newSortingFields_, getOrder());
	}

	public int compare(final Object object1_, final Object object2_) {
		if (!(object1_ instanceof Song) || object1_.getClass() != object2_.getClass()) {
			throw new ClassCastException();
		}

		final SongField[] sortingFields_ = getSortingFields();
		final Song song1_ = (Song) object1_;
		final Song song2_ = (Song) object2_;
		final int order_ = getOrder();
		int compare_ = 0;

		for (int i = 0; i < sortingFields_.length; ++i) {
			compare_ = ((Comparable) sortingFields_[i].get(song1_)).compareTo(sortingFields_[i].get(song2_));

			if (compare_ != 0) {
				break;
			}
		}

		return order_ * compare_;
	}

	public boolean equals(final Object object_) {
		if (object_ == null) {
			return false;
		}

		if (this == object_) {
			return true;
		}

		if (getClass() != object_.getClass()) {
			return false;
		}

		final SongSorter songSorter_ = (SongSorter) object_;
		
		return (getOrder() == songSorter_.getOrder() && Arrays.equals(getSortingFields(), songSorter_.getSortingFields()));
	}

	public void reverseOrder() {
		setOrder(getOrder() * -1);
	}

	public int getOrder() {
		return order;
	}

	public SongField[] getSortingFields() {
		return sortingFields;
	}

	public void setOrder(final int order_) {
		order = order_;
	}
}

