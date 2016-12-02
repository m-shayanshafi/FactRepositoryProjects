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

import java.io.IOException;

import napsack.protocol.Message;
import napsack.protocol.MessageType;
import napsack.protocol.SongQueryMessage;
import napsack.servers.NapsterService;

public class SongQueryCommand extends LeafCommand {
	private final static MessageType[] INTERESTING_MESSAGES = {MessageType.SEARCH_RESPONSE, MessageType.ERROR};
	private final static MessageType[] TERMINATING_MESSAGES = {MessageType.END_OF_SEARCH_RESPONSE, MessageType.ERROR};

	private final SongQueryMessage queryMessage;

	public SongQueryCommand(final SongQueryMessage queryMessage_) { 
		queryMessage = queryMessage_;
	}

	public SongQueryMessage getQueryMessage() {
		return queryMessage;
	}

	protected void doExecute(final NapsterService napsterService_) throws CommandException, IOException {
		final SongQueryMessage queryMessage_ = getQueryMessage();
		final Message[] returnedMessages_ = napsterService_.sendMessage(queryMessage_, INTERESTING_MESSAGES, TERMINATING_MESSAGES);

		for (int i = 0; i < returnedMessages_.length; ++i) {
			if (returnedMessages_[i].getType().equals(MessageType.ERROR)) {
				setReturnedMessages(new Message[] {returnedMessages_[i]});

				throw new CommandException();
			}
		}

		setReturnedMessages(returnedMessages_);
	}
}

