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

package napsack.commands;

import java.util.List;

import napsack.protocol.ClientInfo;
import napsack.protocol.SongQueryMessage;
import napsack.servers.NapsterService;

public class SongSearchCommand extends CompositeCommand {
	private final static SongQueryCommand[] SONG_QUERY_COMMAND_ARRAY = new SongQueryCommand[0];

	public SongSearchCommand(final ClientInfo clientInfo_, final SongQueryMessage[] queryMessages_) {
		addNapsterCommand(new ConnectCommand(clientInfo_));

		for (int i = 0; i < queryMessages_.length; ++i) {
			addNapsterCommand(new SongQueryCommand(queryMessages_[i]));
		}

		addNapsterCommand(new DisconnectCommand());
	}

	public ConnectCommand getConnectCommand() {
		return (ConnectCommand) getNapsterCommands().get(0);
	}

	public DisconnectCommand getDisconnectCommand() {
		final List napsterCommands_ = getNapsterCommands();

		return (DisconnectCommand) napsterCommands_.get(napsterCommands_.size() - 1);
	}

	public SongQueryCommand[] getSongQueryCommands() {
		final List napsterCommands_ = getNapsterCommands();

		return (SongQueryCommand[]) napsterCommands_.subList(1, napsterCommands_.size() - 1).toArray(SONG_QUERY_COMMAND_ARRAY);
	}
}

