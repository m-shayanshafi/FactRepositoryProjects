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

package napsack.util.properties;

import napsack.text.OutputFormat;
import napsack.util.StringUtils;
import napsack.util.model.SongField;

public class OutputFormatProperty extends Property {
	private final static String NAME = "output_format";
	private final static OutputFormat DEFAULT_VALUE = new OutputFormat("^llllllllllllllllllll ^llllllllllllllllllllllllllllllllllllllllllllll ^llll ^rr", new String[] {OutputFormat.NAPSTER_SERVICE, SongField.FILE_NAME.toString(), SongField.LENGTH.toString(), SongField.BIT_RATE.toString()});

	private static Property instance;

   public static Property getInstance() {
      if (instance == null) {
			synchronized (OutputFormatProperty.class) {
				if (instance == null) {
         		instance = new OutputFormatProperty();
				}
			}
      }

      return instance;
   }

   private OutputFormatProperty() {
   }

	public String getName() {
		return NAME;
	}
	
	protected Object parseProperty() {
		final String[] outputFormatStrings_ = getStrings();
		OutputFormat outputFormat_ = null;

		if (outputFormatStrings_ != null) {
			final String formatString_ = outputFormatStrings_[0];
			final int length_ = outputFormatStrings_.length - 1;
			final String[] formatFields_ = new String[length_];

			System.arraycopy(outputFormatStrings_, 1, formatFields_, 0, length_);
			
			outputFormat_ = new OutputFormat(formatString_, formatFields_);
		} else {
			outputFormat_ = DEFAULT_VALUE;
		}

		return outputFormat_;
	}

	public void validate(final String property_) throws PropertyException {
		if (property_ != null) {
			final String[] formatStrings_ = getStrings(property_);

			if (formatStrings_.length < 1) {
				throw new PropertyException("Empty " + getName() + " property specified.");
			}

			if (StringUtils.countChars(formatStrings_[0], OutputFormat.FORMAT_IDENTIFIER) != formatStrings_.length - 1) {
				throw new PropertyException("Invalid value for " + getName() + "; the number of " + OutputFormat.FORMAT_IDENTIFIER + "s must equal the number of fields specified.");
			}

			for (int i = 1; i < formatStrings_.length; ++i) {
				if (!formatStrings_[i].equals(OutputFormat.NAPSTER_SERVICE) && SongField.getSongField(formatStrings_[i]) == null) {
					final SongField[] almostAllValidFormatFields_ = SongField.getValidSongFields();
					final Object[] validFormatFields_ = new Object[almostAllValidFormatFields_.length + 1];

					validFormatFields_[0] = OutputFormat.NAPSTER_SERVICE;
					System.arraycopy(almostAllValidFormatFields_, 0, validFormatFields_, 1, almostAllValidFormatFields_.length);

					throw new EnumerationException("Invalid output field for " + getName() + "; valid values are", validFormatFields_);
				}
			}
		}
	}
}

