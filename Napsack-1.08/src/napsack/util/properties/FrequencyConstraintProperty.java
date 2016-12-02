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

import napsack.protocol.Comparison;
import napsack.protocol.Frequency;
import napsack.protocol.FrequencyConstraint;

public class FrequencyConstraintProperty extends Property {
	private final static String NAME = "frequency";

   private static Property instance;

   public static Property getInstance() {
      if (instance == null) {
			synchronized (FrequencyConstraintProperty.class) {
				if (instance == null) {
         		instance = new FrequencyConstraintProperty();
				}
			}
      }

      return instance;
   }

   private FrequencyConstraintProperty() {
   }

	public String getName() {
		return NAME;
	}

	protected Object parseProperty() {
		final String[] frequencyConstraintStrings_ = getStrings();
		FrequencyConstraint frequencyConstraint_ = null;

		if (frequencyConstraintStrings_ != null) {
			frequencyConstraint_ = new FrequencyConstraint(Comparison.getComparison(frequencyConstraintStrings_[0]), Frequency.getFrequencyByDescription(frequencyConstraintStrings_[1]));
		}

		return frequencyConstraint_;
	}

	public void validate(final String property_) throws PropertyException {
      if (property_ != null) {
         final String[] frequencyStrings_ = getStrings(property_);

         if (frequencyStrings_.length != 2) {
            final String delimiter_ = (String) DelimiterProperty.getInstance().getValue();

            throw new PropertyException("Wrong number of values for " + getName() + "; exactly two values (separated by " + (delimiter_.length() > 1 ? "one of " : "") + delimiter_ + ") are required.");
         }

         validateComparison(frequencyStrings_[0]);
         validateFrequency(frequencyStrings_[1]);
      }
	}

   private void validateFrequency(final String frequency_) throws PropertyException {
      if (frequency_ != null && Frequency.getFrequencyByDescription(frequency_) == null) {
         throw new EnumerationException("Invalid value for " + getName() + "; valid frequency values are ", Frequency.getValidFrequencies());
      }
   }
}

