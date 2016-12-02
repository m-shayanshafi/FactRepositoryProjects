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

public class Connection implements Comparable {
	private final static Hashtable CONNECTIONS_BY_CODE = new Hashtable(11, 1.0f);
	private final static Hashtable CONNECTIONS_BY_DESCRIPTION = new Hashtable(11, 1.0f);
	private final static Connection[] CONNECTION_ARRAY = new Connection[0];

	public final static Connection UNKNOWN = new Connection(new Integer(0), "Unknown");
	public final static Connection _14_4 = new Connection(new Integer(1), "14.4 Kbps");
	public final static Connection _28_8 = new Connection(new Integer(2), "28.8 Kbps");
	public final static Connection _33_6 = new Connection(new Integer(3), "33.6 Kbps");
	public final static Connection _56_7 = new Connection(new Integer(4), "56.7 Kbps");
	public final static Connection _64 = new Connection(new Integer(5), "64 Kbps");
	public final static Connection _128 = new Connection(new Integer(6), "128 Kbps");
	public final static Connection CABLE = new Connection(new Integer(7), "Cable");
	public final static Connection DSL = new Connection(new Integer(8), "DSL");
	public final static Connection T1 = new Connection(new Integer(9), "T1");
	public final static Connection T3_PLUS = new Connection(new Integer(10), "T3+");

	static {
		CONNECTIONS_BY_CODE.put(UNKNOWN.getCode(), UNKNOWN);
		CONNECTIONS_BY_CODE.put(_14_4.getCode(), _14_4);
		CONNECTIONS_BY_CODE.put(_28_8.getCode(), _28_8);
		CONNECTIONS_BY_CODE.put(_33_6.getCode(), _33_6);
		CONNECTIONS_BY_CODE.put(_56_7.getCode(), _56_7);
		CONNECTIONS_BY_CODE.put(_64.getCode(), _64);
		CONNECTIONS_BY_CODE.put(_128.getCode(), _128);
		CONNECTIONS_BY_CODE.put(CABLE.getCode(), CABLE);
		CONNECTIONS_BY_CODE.put(DSL.getCode(), DSL);
		CONNECTIONS_BY_CODE.put(T1.getCode(), T1);
		CONNECTIONS_BY_CODE.put(T3_PLUS.getCode(), T3_PLUS);

		CONNECTIONS_BY_DESCRIPTION.put(UNKNOWN.getDescription(), UNKNOWN);
		CONNECTIONS_BY_DESCRIPTION.put(_14_4.getDescription(), _14_4);
		CONNECTIONS_BY_DESCRIPTION.put(_28_8.getDescription(), _28_8);
		CONNECTIONS_BY_DESCRIPTION.put(_33_6.getDescription(), _33_6);
		CONNECTIONS_BY_DESCRIPTION.put(_56_7.getDescription(), _56_7);
		CONNECTIONS_BY_DESCRIPTION.put(_64.getDescription(), _64);
		CONNECTIONS_BY_DESCRIPTION.put(_128.getDescription(), _128);
		CONNECTIONS_BY_DESCRIPTION.put(CABLE.getDescription(), CABLE);
		CONNECTIONS_BY_DESCRIPTION.put(DSL.getDescription(), DSL);
		CONNECTIONS_BY_DESCRIPTION.put(T1.getDescription(), T1);
		CONNECTIONS_BY_DESCRIPTION.put(T3_PLUS.getDescription(), T3_PLUS);
	}

	public static Connection getConnectionByCode(final Integer code_) {
		return (Connection) CONNECTIONS_BY_CODE.get(code_);
	}

	public static Connection getConnectionByDescription(final String description_) {
		return (Connection) CONNECTIONS_BY_DESCRIPTION.get(description_);
	}

	public static Connection[] getValidConnections() {
		final Connection[] validConnections_ = (Connection[]) CONNECTIONS_BY_CODE.values().toArray(CONNECTION_ARRAY);
		Arrays.sort(validConnections_);

		return validConnections_;
	}

	private final Integer code;
	private final String description;

	private Connection(final Integer code_, final String description_) {
		code = code_;
		description = description_;
	}

	public int compareTo(final Object object_) {
		if (this == object_) {
			return 0;
		}

		if (getClass() != object_.getClass()) {
			throw new ClassCastException();
		}

		return getCode().compareTo(((Connection) object_).getCode());
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

