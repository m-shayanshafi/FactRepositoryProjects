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
import java.net.UnknownHostException;

import napsack.protocol.ClientInfo;
import napsack.protocol.Message;
import napsack.protocol.MessageType;
import napsack.servers.NapsterService;
import napsack.util.properties.ApplicationIdentifierProperty;

public class ConnectCommand extends LeafCommand {
	private final static MessageType[] INTERESTING_MESSAGES = {MessageType.LOGIN_ERROR, MessageType.LOGIN_ACK, MessageType.NICK_ALREADY_REGISTERED, MessageType.INVALID_NICK};
	private final static MessageType[] TERMINATING_MESSAGES = INTERESTING_MESSAGES; 

	final private Message loginMessage;

	public ConnectCommand(final ClientInfo clientInfo_) {
		loginMessage = new Message(MessageType.LOGIN, clientInfo_.getNick() + " " + clientInfo_.getPassword() + " " + clientInfo_.getPort() + " \"" + ApplicationIdentifierProperty.getInstance().getValue() + "\" " + clientInfo_.getConnection().getCode());
	}

	protected void doExecute(final NapsterService napsterService_) throws CommandException, IOException, UnknownHostException {
		napsterService_.connect();

		final Message[] returnedMessages_ = napsterService_.sendMessage(getLoginMessage(), INTERESTING_MESSAGES, TERMINATING_MESSAGES);

		if (returnedMessages_.length != 1 || !returnedMessages_[0].getType().equals(MessageType.LOGIN_ACK)) {
			setReturnedMessages(returnedMessages_);

			throw new CommandException();
		}
	}

	private Message getLoginMessage() {
		return loginMessage;
	}
}

