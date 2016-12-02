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

package napsack.text;

import java.util.Arrays;

public class OutputFormat {
	public final static char FORMAT_IDENTIFIER = '^';
	public final static String NAPSTER_SERVICE = "Service";
	public final static char LEFT = 'l';
	public final static char RIGHT = 'r';
	public final static char CENTER = 'c';
	public final static char RIGHT_ZERO = '0';
	public final static char[] FORMAT_CHARS = {LEFT, RIGHT, CENTER, RIGHT_ZERO};

	static {
		Arrays.sort(FORMAT_CHARS);
	}

	private final String formatString;
	private final String[] formatFields;

	public OutputFormat(final String formatString_, final String[] formatFields_) {
		formatString = formatString_;
		formatFields = formatFields_;
	}

	public String[] getFormatFields() {
		return formatFields;
	}

	public String getFormatString() {
		return formatString;
	}
}

