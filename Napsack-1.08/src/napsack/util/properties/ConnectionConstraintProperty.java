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
import napsack.protocol.Connection;
import napsack.protocol.ConnectionConstraint;
import napsack.util.properties.EnumerationException;

public class ConnectionConstraintProperty extends Property {
	private final static String NAME = "connection";

   private static Property instance;

   public static Property getInstance() {
		if (instance == null) {
			synchronized (ConnectionConstraintProperty.class) {
				if (instance == null) {
					instance = new ConnectionConstraintProperty();
				}
			}
      }

      return instance;
	}

   private ConnectionConstraintProperty() {
   }
   
	public String getName() {
		return NAME;
	}

	protected Object parseProperty() {
      final String[] connectionConstraintStrings_ = getStrings();
      ConnectionConstraint connectionConstraint_ = null;

      if (connectionConstraintStrings_ != null) {
         connectionConstraint_ = new ConnectionConstraint(Comparison.getComparison(connectionConstraintStrings_[0]), Connection.getConnectionByDescription(connectionConstraintStrings_[1]));
      }

      return connectionConstraint_;
	}

	public void validate(final String property_) throws PropertyException {
      if (property_ != null) {
			final String[] connectionStrings_ = getStrings(property_);

         if (connectionStrings_.length != 2) {
				final String delimiter_ = (String) DelimiterProperty.getInstance().getValue();

            throw new PropertyException("Wrong number of values for " + getName() + "; exactly two values (separated by " + (delimiter_.length() > 1 ? "one of " : "") + delimiter_ + ") are required.");
         }

         validateComparison(connectionStrings_[0]);
         validateConnection(connectionStrings_[1]);
      }
	}

	private void validateConnection(final String connection_) throws PropertyException {
      if (connection_ != null && Connection.getConnectionByDescription(connection_) == null) {
         throw new EnumerationException("Invalid value for " + getName() + "; valid connection values are ", Connection.getValidConnections());
      }
	}
}

