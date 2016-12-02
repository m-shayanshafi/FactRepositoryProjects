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

import napsack.protocol.BitRate;
import napsack.protocol.BitRateConstraint;
import napsack.protocol.Comparison;

public class BitRateConstraintProperty extends Property {
	private final static String NAME = "bit_rate";

   private static Property instance;

   public static Property getInstance() {
      if (instance == null) {
			synchronized (BitRateConstraintProperty.class) {
				if (instance == null) {
         		instance = new BitRateConstraintProperty();
				}
			}
      }

      return instance;
   }

   private BitRateConstraintProperty() {
   }

	public String getName() {
		return NAME;
	}

	protected Object parseProperty() {
		final String[] bitRateConstraintStrings_ = getStrings();
		BitRateConstraint bitRateConstraint_ = null;

		if (bitRateConstraintStrings_ != null) {
			bitRateConstraint_ = new BitRateConstraint(Comparison.getComparison(bitRateConstraintStrings_[0]), BitRate.getBitRateByDescription(bitRateConstraintStrings_[1]));
		}

		return bitRateConstraint_;
	}

	public void validate(final String property_) throws PropertyException {
      if (property_ != null) {
         final String[] bitRateStrings_ = getStrings(property_);

         if (bitRateStrings_.length != 2) {
            final String delimiter_ = (String) DelimiterProperty.getInstance().getValue();

            throw new PropertyException("Wrong number of values for " + getName() + "; exactly two values (separated by " + (delimiter_.length() > 1 ? "one of " : "") + delimiter_ + ") are required.");
         }

         validateComparison(bitRateStrings_[0]);
         validateBitRate(bitRateStrings_[1]);
      }
	}

   private void validateBitRate(final String bitRate_) throws PropertyException {
      if (bitRate_ != null && BitRate.getBitRateByDescription(bitRate_) == null) {
         throw new EnumerationException("Invalid value for " + getName() + "; valid bit-rate values are ", BitRate.getValidBitRates());
      }
   }
}

