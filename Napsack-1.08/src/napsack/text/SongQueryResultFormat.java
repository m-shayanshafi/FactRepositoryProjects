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

import napsack.servers.NapsterService;
import napsack.util.StringUtils;
import napsack.util.model.Song;
import napsack.util.model.SongField;

public class SongQueryResultFormat {
	private static String formatString(final String string_, final char formatChar_, final int width_) {
		String formattedField_ = null;

		switch (formatChar_) {
			case OutputFormat.LEFT:
			default:
				formattedField_ = StringUtils.leftJustify(string_, width_);
				break;
			case OutputFormat.RIGHT:
				formattedField_ = StringUtils.rightJustify(string_, width_);
				break;
			case OutputFormat.CENTER:
				formattedField_ = StringUtils.center(string_, width_);
				break;
			case OutputFormat.RIGHT_ZERO:
				formattedField_ = StringUtils.rightJustify(string_, width_, StringUtils.ZERO);
				break;
		}

		return formattedField_;
	}

	private final OutputFormat outputFormat;

	public SongQueryResultFormat(final OutputFormat outputFormat_) {
		outputFormat = outputFormat_;
	}

	public String format(final String napsterServiceString_, final Song song_) {
		final OutputFormat outputFormat_ = getOutputFormat();
		final String formatString_ = outputFormat_.getFormatString();
		final String[] fields_ = outputFormat_.getFormatFields();
		final int length_ = formatString_.length();
		final StringBuffer stringBuffer_ = new StringBuffer(length_);
		int fieldIndex_ = 0;

		for (int i = 0; i < length_;) {
			int j = formatString_.indexOf(OutputFormat.FORMAT_IDENTIFIER, i);

			if (j != -1) {
				char formatChar_ = '\u0000';	
				int width_ = 0;
				String fieldName_ = null;
				String formattedField_ = null;

				stringBuffer_.append(formatString_.substring(i, j));

				if (j < length_ - 1 && Arrays.binarySearch(OutputFormat.FORMAT_CHARS, (formatChar_ = formatString_.charAt(j + 1))) >= 0) {
					int k = j + 2;
					for (;k < length_ && formatString_.charAt(k) == formatChar_; ++k);
					width_ = k - j;
					i = k;
				} else {
					width_ = 1;
					i = j + 1;
				}

				if ((fieldName_ = fields_[fieldIndex_++]).equals(OutputFormat.NAPSTER_SERVICE)) {
					formattedField_ = formatString(napsterServiceString_, formatChar_, width_);
				} else {
					formattedField_ = formatString(SongField.getSongField(fieldName_).get(song_.getView()).toString(), formatChar_, width_);
				}

				stringBuffer_.append(formattedField_);
			} else {
				stringBuffer_.append(formatString_.substring(i));
				break;
			}
		}

		return stringBuffer_.toString();
	}

	public OutputFormat getOutputFormat() {
		return outputFormat;
	}
}

