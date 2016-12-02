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

import java.util.Arrays;

public class NickProperty extends Property {
	private final static String NAME = "nick";
   private final static String VALID_NICK_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_[]{}-@^!$";
   private final static char[] VALID_NICK_CHARS = new char[VALID_NICK_STRING.length()];

   private static Property instance;

	static {
      VALID_NICK_STRING.getChars(0, VALID_NICK_STRING.length(), VALID_NICK_CHARS, 0);
      Arrays.sort(VALID_NICK_CHARS);
	}

   public static Property getInstance() {
      if (instance == null) {
			synchronized (NickProperty.class) {
				if (instance == null) {
         		instance = new NickProperty();
				}
			}
      }

      return instance;
   }

   private NickProperty() {
   }

	public String getName() {
		return NAME;
	}

	protected Object parseProperty() {
		return getProperty();
	}
      
	public void validate(final String property_) throws PropertyException {
      if (property_ == null || property_.trim().length() == 0) {
         throw new EmptyNickPropertyException("No nick specified.");
      }

      final int nickLength_ = property_.length();

      for (int i = 0; i < nickLength_; ++i) {
         char char_ = property_.charAt(i);

         if (Arrays.binarySearch(VALID_NICK_CHARS, char_) < 0) {
            throw new PropertyException("Nick contains invalid character: \'" + char_ + "\'.");
         }
      }
	}
}

