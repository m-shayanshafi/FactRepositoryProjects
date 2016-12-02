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

package napsack;

import napsack.clients.Client;
import napsack.util.properties.GuiProperty;
import napsack.util.properties.NapsackProperties;

public class Napsack {
	public final static String APPLICATION_IDENTIFIER = "Napsack 1.08";

	public static void main(final String[] args_) {
		try {
      	NapsackProperties.createInstance();

			GuiProperty.getInstance().validate();

			Client.getInstance().initialize(args_);
		} catch (Exception exception_) {
			printException(exception_);
			System.err.println();
			printUsage();
			System.exit(1);
		}
	}

	private static void printException(final Exception exception_) {
		System.err.println("Exception encountered:  " + exception_.getMessage());
	}

	private static void printUsage() {
		System.err.println("Napsack usage:  java -jar napsack.jar [query] [query] ...");
	}
}

