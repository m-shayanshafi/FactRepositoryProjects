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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class StringUtils {
	public final static String ENCODING = "ISO-8859-1";
	public final static char SPACE = ' ';
	public final static char ZERO = '0';

	private final static String CROPPED_INDICATOR = "...";
	private final static BufferedReader IN = new BufferedReader(new InputStreamReader(System.in));

	public static String center(final String string_, final int width_) {
		return center(string_, width_, SPACE);
	}

	public static String center(final String string_, final int width_, final char paddingChar_) {
		String centered_ = null;
		final int length_ = string_.length();

		if (length_ > width_) {
			final int difference_ = length_ - width_;
			final int left_ = difference_ / 2;
			final int right_ = length_ - (difference_ - left_);

			centered_ = string_.substring(left_, right_);
		} else if (length_ == width_) {
			centered_ = string_;
		} else {
			final int difference_ = width_ - length_;
			final int left_ = difference_ / 2;
			final int right_ = difference_ - left_;
			final StringBuffer stringBuffer_ = new StringBuffer(width_);
			final char[] leftPadding_ = new char[left_];
			final char[] rightPadding_ = new char[right_];

			Arrays.fill(leftPadding_, paddingChar_);
			Arrays.fill(rightPadding_, paddingChar_);

			stringBuffer_.append(leftPadding_);
			stringBuffer_.append(string_);
			stringBuffer_.append(rightPadding_);

			centered_ = stringBuffer_.toString();
		}

		return centered_;
	}

	public static int countChars(final String string_, final char char_) {
		int beginChar_ = -1;
		int charCount_ = 0;

		while ((beginChar_ = string_.indexOf(char_, ++beginChar_)) != -1) {
			++charCount_;
		}

		return charCount_;
	}

	public static String crop(final String string_, final int croppedLength_) {
		String croppedString_;

		if (string_ == null || string_.length() <= croppedLength_) {
			croppedString_ =  string_;
		} else if (CROPPED_INDICATOR.length() > croppedLength_) {
			croppedString_ = CROPPED_INDICATOR.substring(0, croppedLength_);
		} else {
			final StringBuffer croppedBuffer_ = new StringBuffer(croppedLength_);

			croppedBuffer_.append(string_.substring(0, croppedLength_ - CROPPED_INDICATOR.length()));
			croppedBuffer_.append(CROPPED_INDICATOR);

			croppedString_ = croppedBuffer_.toString();
		}

		return croppedString_;
	}

	public static String join(final Object[] objects_) {
		return join(toStrings(objects_));
	}

	public static String join(final Object[] objects_, final Object delimiter_) {
		return join(toStrings(objects_), delimiter_.toString());
	}
		

	public static String join(final String[] strings_) {
		return join(strings_, "");
	}

	public static String join(final String[] strings_, final String delimiter_) {
		String string_ = "";

		if (strings_.length > 0) {
			final StringBuffer stringBuffer_ = new StringBuffer(strings_[0]);

			for (int i = 1; i < strings_.length; ++i) {
				stringBuffer_.append(delimiter_);
				stringBuffer_.append(strings_[i]);
			}

			string_ = stringBuffer_.toString();
		}
		
		return string_;
	}

	public static String joinForSentence(final Object[] objects_, final Object delimiter_) {
		return joinForSentence(toStrings(objects_), delimiter_.toString());
	}

	public static String joinForSentence(final String[] strings_, final String delimiter_) {
		String string_ = "";

		if (strings_.length > 0) {
			StringBuffer stringBuffer_ = new StringBuffer(strings_[0]);

			for (int i = 1; i < strings_.length; ++i) {
				stringBuffer_.append(i == strings_.length - 1 ? " and " : delimiter_);
				stringBuffer_.append(strings_[i].toString());
			}

			string_ = stringBuffer_.toString();
		}

		return string_;
	}

	public static String leftJustify(final String string_, final int width_) {
		return leftJustify(string_, width_, SPACE);
	}

	public static String leftJustify(final String string_, final int width_, final char paddingChar_) {
		String leftJustified_ = null;
		final int length_ = string_.length();

		if (length_ > width_) {
			leftJustified_ = string_.substring(0, width_);
		} else if (length_ == width_) {
			leftJustified_ = string_;
		} else {
			final StringBuffer stringBuffer_ = new StringBuffer(string_);
			final char[] padding_ = new char[width_ - length_];

			Arrays.fill(padding_, paddingChar_);
			stringBuffer_.append(padding_);

			leftJustified_ = stringBuffer_.toString();
		}

		return leftJustified_;
	}

	public static String promptFor(final String prompt_) throws IOException {
		System.out.print(prompt_);

		return IN.readLine();
	}

	public static String rightJustify(final String string_, final int width_) {
		return rightJustify(string_, width_, SPACE);
	}

	public static String rightJustify(final String string_, final int width_, final char paddingChar_) {
		String rightJustified_ = null;
		final int length_ = string_.length();

		if (length_ > width_) {
			rightJustified_ = string_.substring(length_ - width_);
		} else if (length_ == width_) {
			rightJustified_ = string_;
		} else {
			final StringBuffer stringBuffer_ = new StringBuffer(width_);
			final char[] padding_ = new char[width_ - length_];

			Arrays.fill(padding_, paddingChar_);
			stringBuffer_.append(padding_);
			stringBuffer_.append(string_);

			rightJustified_ = stringBuffer_.toString();
		}

		return rightJustified_;
	}

	public static String[] toStrings(final Object[] objects_) {
		final String[] strings_ = new String[objects_.length];

		for (int i = 0; i < objects_.length; ++i) {
			strings_[i] = objects_[i].toString();
		}

		return strings_;
	}
}

