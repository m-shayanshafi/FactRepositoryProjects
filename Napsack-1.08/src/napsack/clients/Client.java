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

package napsack.clients;

import java.io.IOException;

import napsack.util.StringUtils;
import napsack.util.properties.DelimiterProperty;
import napsack.util.properties.GuiProperty;
import napsack.util.properties.PropertyException;
import napsack.util.properties.QueriesProperty;

public abstract class Client {
	private static Client instance;

	public static Client getInstance() {
		if (instance == null) {
			if (((Boolean) GuiProperty.getInstance().getValue()).booleanValue()) {
				instance = new GuiClient();
			} else {
				instance = new ClassicClient();
			}
		}

		return instance;
	}

   protected static void parseQueries(final String[] argQueries_) {
      final String[] propertyQueries_ = (String[]) QueriesProperty.getInstance().getValue();
      String[] allQueries_ = propertyQueries_;

      if (argQueries_.length > 0) {
         allQueries_ = new String[propertyQueries_.length + argQueries_.length];         System.arraycopy(propertyQueries_, 0, allQueries_, 0, propertyQueries_.length);
         System.arraycopy(argQueries_, 0, allQueries_, propertyQueries_.length, argQueries_.length);
      }

      QueriesProperty.getInstance().setProperty(StringUtils.join(allQueries_, ((String) DelimiterProperty.getInstance().getValue()).substring(0, 1)));
   }

	protected Client() {
	}

   public abstract void initialize(String[] args_) throws PropertyException, IOException;
}

