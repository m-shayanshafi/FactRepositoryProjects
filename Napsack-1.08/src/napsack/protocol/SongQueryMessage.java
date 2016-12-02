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

public class SongQueryMessage extends Message {
	private static String createQueryMessage(final String queryString_, final int maxResults_, final ConnectionConstraint connectionConstraint_, final BitRateConstraint bitRateConstraint_, final FrequencyConstraint frequencyConstraint_) {
		final StringBuffer searchMessage_ = new StringBuffer();

		searchMessage_.append("FILENAME CONTAINS \"");
		searchMessage_.append(queryString_);
		searchMessage_.append("\" ");

		searchMessage_.append("MAX_RESULTS ");
		searchMessage_.append(Integer.toString(maxResults_));

		if (connectionConstraint_ != null) {
			searchMessage_.append(" ");
			searchMessage_.append(connectionConstraint_.toString());
		}

		if (bitRateConstraint_ != null) {
			searchMessage_.append(" ");
			searchMessage_.append(bitRateConstraint_.toString());
		}

		if (frequencyConstraint_ != null) {
			searchMessage_.append(" ");
			searchMessage_.append(frequencyConstraint_.toString());
		}

		return searchMessage_.toString();
	}

	private final String queryString;

	public SongQueryMessage(String queryString_) {
		this(queryString_, 100, null, null, null);
	}

	public SongQueryMessage(final String queryString_, final int maxResults_, final ConnectionConstraint connectionConstraint_, final BitRateConstraint bitRateConstraint_, final FrequencyConstraint frequencyConstraint_) {
		super(MessageType.SEARCH_REQUEST, createQueryMessage(queryString_, maxResults_, connectionConstraint_, bitRateConstraint_, frequencyConstraint_));

		queryString = queryString_;
	}

	public String getQueryString() {
		return queryString;
	}
}

