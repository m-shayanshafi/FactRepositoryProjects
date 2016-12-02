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

public class BitRate implements Comparable {
	private final static Hashtable BIT_RATES_BY_CODE = new Hashtable(13, 1.0f);
	private final static Hashtable BIT_RATES_BY_DESCRIPTION = new Hashtable(13, 1.0f);
	private final static BitRate[] BIT_RATE_ARRAY = new BitRate[0];

	public final static BitRate _20 = new BitRate(new Integer(20));
	public final static BitRate _24 = new BitRate(new Integer(24));
	public final static BitRate _32 = new BitRate(new Integer(32));
	public final static BitRate _48 = new BitRate(new Integer(48));
	public final static BitRate _56 = new BitRate(new Integer(56));
	public final static BitRate _64 = new BitRate(new Integer(64));
	public final static BitRate _80 = new BitRate(new Integer(80));
	public final static BitRate _96 = new BitRate(new Integer(96));
	public final static BitRate _112 = new BitRate(new Integer(112));
	public final static BitRate _128 = new BitRate(new Integer(128));
	public final static BitRate _160 = new BitRate(new Integer(160));
	public final static BitRate _192 = new BitRate(new Integer(192));
	public final static BitRate _256 = new BitRate(new Integer(256));

	static {
		BIT_RATES_BY_CODE.put(_20.getCode(), _20);
		BIT_RATES_BY_CODE.put(_24.getCode(), _24);
		BIT_RATES_BY_CODE.put(_32.getCode(), _32);
		BIT_RATES_BY_CODE.put(_48.getCode(), _48);
		BIT_RATES_BY_CODE.put(_56.getCode(), _56);
		BIT_RATES_BY_CODE.put(_64.getCode(), _64);
		BIT_RATES_BY_CODE.put(_80.getCode(), _80);
		BIT_RATES_BY_CODE.put(_96.getCode(), _96);
		BIT_RATES_BY_CODE.put(_112.getCode(), _112);
		BIT_RATES_BY_CODE.put(_128.getCode(), _128);
		BIT_RATES_BY_CODE.put(_160.getCode(), _160);
		BIT_RATES_BY_CODE.put(_192.getCode(), _192);
		BIT_RATES_BY_CODE.put(_256.getCode(), _256);

		BIT_RATES_BY_DESCRIPTION.put(_20.getDescription(), _20);
		BIT_RATES_BY_DESCRIPTION.put(_24.getDescription(), _24);
		BIT_RATES_BY_DESCRIPTION.put(_32.getDescription(), _32);
		BIT_RATES_BY_DESCRIPTION.put(_48.getDescription(), _48);
		BIT_RATES_BY_DESCRIPTION.put(_56.getDescription(), _56);
		BIT_RATES_BY_DESCRIPTION.put(_64.getDescription(), _64);
		BIT_RATES_BY_DESCRIPTION.put(_80.getDescription(), _80);
		BIT_RATES_BY_DESCRIPTION.put(_96.getDescription(), _96);
		BIT_RATES_BY_DESCRIPTION.put(_112.getDescription(), _112);
		BIT_RATES_BY_DESCRIPTION.put(_128.getDescription(), _128);
		BIT_RATES_BY_DESCRIPTION.put(_160.getDescription(), _160);
		BIT_RATES_BY_DESCRIPTION.put(_192.getDescription(), _192);
		BIT_RATES_BY_DESCRIPTION.put(_256.getDescription(), _256);
	}

	public static BitRate createBitRateByCode(final Integer code_) {
		BitRate bitRate_ = (BitRate) BIT_RATES_BY_CODE.get(code_);

		if (bitRate_ == null) {
			bitRate_ = new BitRate(code_);
		}

		return bitRate_;
	}

	public static BitRate getBitRateByCode(final Integer code_) {
		return (BitRate) BIT_RATES_BY_CODE.get(code_);
	}

	public static BitRate getBitRateByDescription(final String description_) {
		return (BitRate) BIT_RATES_BY_DESCRIPTION.get(description_);
	}

	public static BitRate[] getValidBitRates() {
		final BitRate[] validBitRates_ = (BitRate[]) BIT_RATES_BY_CODE.values().toArray(BIT_RATE_ARRAY);
		Arrays.sort(validBitRates_);

		return validBitRates_;
	}

	private final Integer code;
	private final String description;

	private BitRate(final Integer code_) {
		code = code_;
		description = code_ + " Kbps";
	}

	public int compareTo(final Object object_) {
		if (this == object_) {
			return 0;
		}

		if (getClass() != object_.getClass()) {
			throw new ClassCastException();
		}

		return getCode().compareTo(((BitRate) object_).getCode());
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

