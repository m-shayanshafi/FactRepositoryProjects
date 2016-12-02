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

package napsack.util.model;

import napsack.commands.NapsterCommand;
import napsack.protocol.Message;
import napsack.servers.NapsterService;
import napsack.util.StringUtils;

public abstract class Error {
	private final static String RETURNED_MESSAGE_DELIMITER = "; ";

	private final String napsterServiceString;
	private final Exception exception;
	private final String errorMessage;

	protected Error(final NapsterService napsterService_, final NapsterCommand napsterCommand_, final Exception exception_, final String intro_) {
		napsterServiceString = napsterService_.toLongString();
		exception = exception_;
		errorMessage = createErrorMessage(napsterCommand_, intro_);
	}

	private String createErrorMessage(final NapsterCommand napsterCommand_, final String intro_) {
		final Exception exception_ = getException();
      final StringBuffer messageBuffer_ = new StringBuffer(intro_);
      final Message[] returnedMessages_ = napsterCommand_.getReturnedMessages();
      final String detailedMessage_ = exception_ == null ? null : exception_.getMessage();
      final boolean returnedMessagesNotEmpty_ = returnedMessages_ != null && returnedMessages_.length > 0;

      if (returnedMessagesNotEmpty_ || detailedMessage_ != null) {
         messageBuffer_.append(": ");

         if (returnedMessagesNotEmpty_) {
            messageBuffer_.append(StringUtils.joinForSentence(returnedMessages_, RETURNED_MESSAGE_DELIMITER));
         } else {
            messageBuffer_.append(detailedMessage_);
         }
		}

      messageBuffer_.append(".");

      return messageBuffer_.toString();
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public Exception getException() {
		return exception;
	}

	public String getNapsterServiceString() {
		return napsterServiceString;
	}
}

