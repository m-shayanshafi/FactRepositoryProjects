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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;

import napsack.Napsack;
import napsack.protocol.BitRateConstraint;
import napsack.protocol.ClientInfo;
import napsack.protocol.Comparison;
import napsack.protocol.ConnectionConstraint;
import napsack.protocol.FrequencyConstraint;
import napsack.protocol.SongQueryMessage;
import napsack.servers.ServiceStatistics;
import napsack.text.OutputFormat;
import napsack.util.StringUtils;

public class NapsackProperties extends Properties {
	public final static File PROPERTIES_FILE = new File(System.getProperty("user.home") + System.getProperty("file.separator") + ".napsack");
	private final static String HEADER = "Generated by " + Napsack.APPLICATION_IDENTIFIER;

	private final static Property[] VALID_PROPERTIES = {
		DelimiterProperty.getInstance(),
		GuiProperty.getInstance(),
		ServersFileProperty.getInstance(),
		NickProperty.getInstance(),
		PasswordProperty.getInstance(),
		QueriesProperty.getInstance(),
		MinFilesProperty.getInstance(),
		MinGigsProperty.getInstance(),
		MinUsersProperty.getInstance(),
		ExcludedNetworksProperty.getInstance(),
		BitRateConstraintProperty.getInstance(),
		FrequencyConstraintProperty.getInstance(),
		ConnectionConstraintProperty.getInstance(),
		MaxResultsProperty.getInstance(),
		MaxThreadsProperty.getInstance(),
		ErrorMessagesProperty.getInstance(),
		SongSorterProperty.getInstance(),
		OutputFormatProperty.getInstance(),
		TableColumnsProperty.getInstance(),
		SaveOnExitProperty.getInstance(),
		CommandAttemptsProperty.getInstance(),
		CommandAttemptIntervalProperty.getInstance(),
		ApplicationIdentifierProperty.getInstance(),
		FlipResultsTreeProperty.getInstance()
	};

	private static NapsackProperties instance;

	public static NapsackProperties createInstance() throws IOException {
		if (instance == null) {
			instance = new NapsackProperties();
		}

		return instance;
	}

	public static NapsackProperties getInstance() {
		if (instance == null) {
			throw new NullPointerException("Create NapsackProperties with createInstance before calling getInstance.");
		}

		return instance;
	}

	private NapsackProperties() throws IOException {
		if (PROPERTIES_FILE.exists()) {
			final BufferedInputStream bufferedInputStream_ = new BufferedInputStream(new FileInputStream(PROPERTIES_FILE));

			load(bufferedInputStream_);
			bufferedInputStream_.close();
		}
	}

   public ClientInfo getClientInfo() {
      return new ClientInfo((String) NickProperty.getInstance().getValue(), (String) PasswordProperty.getInstance().getValue());
   }

	public ServiceStatistics getMinStats() {
		return new ServiceStatistics((Integer) MinFilesProperty.getInstance().getValue(), (Integer) MinGigsProperty.getInstance().getValue(), (Integer) MinUsersProperty.getInstance().getValue());
	}

   public SongQueryMessage[] getSongQueryMessages() {
      final String[] queries_ = (String[]) QueriesProperty.getInstance().getValue();
      SongQueryMessage[] songQueryMessages_ = null;

      if (queries_ != null) {
         songQueryMessages_ = new SongQueryMessage[queries_.length];
         final int maxResults_ = ((Integer) MaxResultsProperty.getInstance().getValue()).intValue();
         final ConnectionConstraint connectionConstraint_ = (ConnectionConstraint) ConnectionConstraintProperty.getInstance().getValue();
         final BitRateConstraint bitRateConstraint_ = (BitRateConstraint) BitRateConstraintProperty.getInstance().getValue();
         final FrequencyConstraint frequencyConstraint_ = (FrequencyConstraint) FrequencyConstraintProperty.getInstance().getValue();

         for (int i = 0; i < queries_.length; ++i) {
            songQueryMessages_[i] = new SongQueryMessage(queries_[i], maxResults_, connectionConstraint_, bitRateConstraint_, frequencyConstraint_);
         }
      } else {
         songQueryMessages_ = new SongQueryMessage[0];
      }

      return songQueryMessages_;
   }

	public synchronized void storeProperties() throws IOException {
		final BufferedOutputStream bufferedOutputStream_ = new BufferedOutputStream(new FileOutputStream(PROPERTIES_FILE));

		store(bufferedOutputStream_, HEADER);
		bufferedOutputStream_.close();
	}

	public synchronized void storeSaveOnExit() throws IOException {
		final NapsackProperties napsackProperties_ = new NapsackProperties();
		final Property saveOnExitProperty_ = SaveOnExitProperty.getInstance();

		napsackProperties_.setProperty(saveOnExitProperty_.getName(), saveOnExitProperty_.getProperty());
		napsackProperties_.storeProperties();
	}

	public void validate() throws PropertyException {
		validateForInvalidProperties();

		for (int i = 0; i < VALID_PROPERTIES.length; ++i) {
			VALID_PROPERTIES[i].validate();
		}
	}

	private void validateForInvalidProperties() throws PropertyException {
		OUTER: for (Enumeration keys_ = keys(); keys_.hasMoreElements();) {
			String key_ = (String) keys_.nextElement();

			for (int i = 0; i < VALID_PROPERTIES.length; ++i) {
				if (key_.equals(VALID_PROPERTIES[i].getName())) {
					continue OUTER;
				}
			}

			throw new PropertyException("Unrecognized option, \"" + key_ + "\", encountered.");
		}
	}
}

