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
import java.util.Hashtable;

public class Switch implements Comparable {
	private final static Hashtable SWITCHES = new Hashtable(2, 1.0f);
	private static Switch[] SWITCH_ARRAY = new Switch[0];

	public final static Switch ON = new Switch("On");
	public final static Switch OFF = new Switch("Off");

	public static Switch getSwitch(String value_) {
		return (Switch) SWITCHES.get(value_);
	}

	public static Switch[] getValidSwitches() {
		final Switch[] validSwitches_ = (Switch[]) SWITCHES.values().toArray(SWITCH_ARRAY);
		Arrays.sort(validSwitches_);

		return validSwitches_;
	}

	private final String value;

	private Switch(final String value_) {
		value = value_;

		SWITCHES.put(value, this);
	}

	public int compareTo(final Object object_) {
		if (this == object_) {
			return 0;
		}

		if (getClass() != object_.getClass()) {
			throw new ClassCastException();
		}

		return -1 * getValue().compareTo(((Switch) object_).getValue());
	}

	public String getValue() {
		return value;
	}

	public boolean isOn() {
		return equals(ON);
	}

	public String toString() {
		return getValue();
	}
}

