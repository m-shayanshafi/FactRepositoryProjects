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

public class Frequency implements Comparable {
	private final static Hashtable FREQUENCIES_BY_CODE = new Hashtable(4, 1.0f);
	private final static Hashtable FREQUENCIES_BY_DESCRIPTION = new Hashtable(4, 1.0f);
	private final static Frequency[] FREQUENCY_ARRAY = new Frequency[0];

	public final static Frequency _48000 = new Frequency(new Integer(48000));
	public final static Frequency _44100 = new Frequency(new Integer(44100));
	public final static Frequency _22050 = new Frequency(new Integer(22050));
	public final static Frequency _11025 = new Frequency(new Integer(11025));

	static {
		FREQUENCIES_BY_CODE.put(_48000.getCode(), _48000);
		FREQUENCIES_BY_CODE.put(_44100.getCode(), _44100);
		FREQUENCIES_BY_CODE.put(_22050.getCode(), _22050);
		FREQUENCIES_BY_CODE.put(_11025.getCode(), _11025);

		FREQUENCIES_BY_DESCRIPTION.put(_48000.getDescription(), _11025);
		FREQUENCIES_BY_DESCRIPTION.put(_44100.getDescription(), _44100);
		FREQUENCIES_BY_DESCRIPTION.put(_22050.getDescription(), _22050);
		FREQUENCIES_BY_DESCRIPTION.put(_11025.getDescription(), _11025);
	}

	public static Frequency createFrequencyByCode(final Integer code_) {
		Frequency frequency_ = getFrequencyByCode(code_);

		if (frequency_ == null) {
			frequency_ = new Frequency(code_);
		}

		return frequency_;
	}

	public static Frequency getFrequencyByCode(final Integer code_) {
		return (Frequency) FREQUENCIES_BY_CODE.get(code_);
	}

	public static Frequency getFrequencyByDescription(final String description_) {
		return (Frequency) FREQUENCIES_BY_DESCRIPTION.get(description_);
	}

	public static Frequency[] getValidFrequencies() {
		final Frequency[] validFrequencys_ = (Frequency[]) FREQUENCIES_BY_CODE.values().toArray(FREQUENCY_ARRAY);
		Arrays.sort(validFrequencys_);

		return validFrequencys_;
	}

	private final Integer code;
	private final String description;
	
	private Frequency(final Integer code_) {
		code = code_;
		description = code_ + " Hz";
	}

	public int compareTo(final Object object_) {
		if (this == object_) {
			return 0;
		}

		if (getClass() != object_.getClass()) {
			throw new ClassCastException();
		}

		return getCode().compareTo(((Frequency) object_).getCode());
	}

	public Integer getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public String toString() {
		return getDescription();
	}
}

