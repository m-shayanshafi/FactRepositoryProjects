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

import napsack.commands.CommandCampaign;
import napsack.commands.NapsterCommand;
import napsack.commands.SongQueryCommand;
import napsack.commands.SongSearchCommand;
import napsack.event.ExceptionListener;
import napsack.event.ExceptionEvent;
import napsack.event.CommandAdapter;
import napsack.event.CommandEvent;
import napsack.event.CommandListener;
import napsack.protocol.Message;
import napsack.servers.NapsterService;
import napsack.text.OutputFormat;
import napsack.text.SongQueryResultFormat;
import napsack.util.SongSorter;
import napsack.util.StringUtils;
import napsack.util.model.SongQueryResults;
import napsack.util.properties.EmptyNickPropertyException;
import napsack.util.properties.EmptyPasswordPropertyException;
import napsack.util.properties.EmptyQueriesPropertyException;
import napsack.util.properties.ErrorMessagesProperty;
import napsack.util.properties.NickProperty;
import napsack.util.properties.NapsackProperties;
import napsack.util.properties.OutputFormatProperty;
import napsack.util.properties.PasswordProperty;
import napsack.util.properties.Property;
import napsack.util.properties.PropertyException;
import napsack.util.properties.QueriesProperty;
import napsack.util.properties.SongSorterProperty;

public class ClassicClient extends Client {
	private final static int MAX_ERROR_MESSAGE_LENGTH = 158;
	private final static String RETURNED_MESSAGE_DELIMITER = "; ";

	private static String createErrorMessage(final String intro_, final CommandEvent commandEvent_) {
		final StringBuffer messageBuffer_ = new StringBuffer(intro_);
		final Message[] returnedMessages_ = ((NapsterCommand) commandEvent_.getSource()).getReturnedMessages();
		final Exception exception_ = commandEvent_.getException();
		final String detailedMessage_ = exception_ == null ? null : exception_.getMessage();
		final boolean returnedMessagesNotEmpty_ = returnedMessages_ != null && returnedMessages_.length > 0;

		messageBuffer_.append(commandEvent_.getNapsterService().toLongString());

		if (returnedMessagesNotEmpty_ || detailedMessage_ != null) {
			messageBuffer_.append(": ");

			if (returnedMessagesNotEmpty_) {
				messageBuffer_.append(StringUtils.joinForSentence(returnedMessages_, RETURNED_MESSAGE_DELIMITER));
			} else {
				messageBuffer_.append(detailedMessage_);
			}
		}

		messageBuffer_.append(".");

		return StringUtils.crop(messageBuffer_.toString(), MAX_ERROR_MESSAGE_LENGTH);
	}

   private static void validateProperties() throws PropertyException, IOException {
      boolean missingInfoPromptShown = false;
      while (true) {
         try {
            NapsackProperties.getInstance().validate();
            break;
         } catch (EmptyNickPropertyException emptyNickPropertyException_) {
            if (!missingInfoPromptShown) {
               System.out.println("Required properties not found in file.  Prompting for required properties.");
               missingInfoPromptShown = true;
            }

            final Property nickProperty_ = NickProperty.getInstance();
            nickProperty_.setProperty(StringUtils.promptFor("nick?  "));
         } catch (EmptyPasswordPropertyException emptyPasswordPropertyException_) {
            if (!missingInfoPromptShown) {
               System.out.println("Required properties not found in file.  Prompting for required properties.");
               missingInfoPromptShown = true;
            }

            final Property passwordProperty_ = PasswordProperty.getInstance();
            passwordProperty_.setProperty(StringUtils.promptFor("password?  "));         } catch (EmptyQueriesPropertyException emptyQueriesPropertyException_) {
            final Property queryProperty_ = QueriesProperty.getInstance();
            System.out.println("Query not found on command-line or in properties file.  Prompting for query.");
            queryProperty_.setProperty(StringUtils.promptFor("queries?  "));
         }
      }
   }

	protected ClassicClient() {
	}

	public void initialize(final String[] args_) throws PropertyException, IOException {
      parseQueries(args_);

      validateProperties();

      final NapsackProperties napsackProperties_ = NapsackProperties.getInstance();
      final SongQueryResultFormat resultFormat_ = new SongQueryResultFormat(((OutputFormat) OutputFormatProperty.getInstance().getValue()));
		final SongSorter songSorter_ = (SongSorter) SongSorterProperty.getInstance().getValue();
		final SongSearchCommand songSearchCommand_ = new SongSearchCommand(napsackProperties_.getClientInfo(), napsackProperties_.getSongQueryMessages());
		final SongQueryCommand[] songQueryCommands_ = songSearchCommand_.getSongQueryCommands();
		final CommandCampaign songSearchCampaign_ = new CommandCampaign(songSearchCommand_);
		final CommandListener songQueryResultsListener_ = new CommandAdapter() {
			public synchronized void commandExecuted(final CommandEvent commandEvent_) {
				final SongQueryResults songQueryResults_ = new SongQueryResults(commandEvent_.getNapsterService(), (SongQueryCommand) commandEvent_.getSource());

				songQueryResults_.setSongSorter(songSorter_);

				songQueryResults_.print(resultFormat_);
			}
		};

		for (int i = 0; i < songQueryCommands_.length; ++i) {
			songQueryCommands_[i].addCommandListener(songQueryResultsListener_);
		}

		if (((Boolean) ErrorMessagesProperty.getInstance().getValue()).booleanValue()) {
			final CommandListener songQueryErrorListener_ = new CommandAdapter() {
				public void commandFailed(final CommandEvent commandEvent_) {
					System.err.println(createErrorMessage("Could not query ", commandEvent_));
				}
			};

			songSearchCommand_.getConnectCommand().addCommandListener(new CommandAdapter() {
				public void commandFailed(final CommandEvent commandEvent_) {
					System.err.println(createErrorMessage("Could not connect to ", commandEvent_));
				}
			});

			for (int i = 0; i < songQueryCommands_.length; ++i) {
				songQueryCommands_[i].addCommandListener(songQueryErrorListener_);
			}

			songSearchCommand_.getDisconnectCommand().addCommandListener(new CommandAdapter() {
				public void commandFailed(final CommandEvent commandEvent_) {
					System.err.println(createErrorMessage("Could not disconnect from ", commandEvent_));
				}
			});

			songSearchCampaign_.addExceptionListener(new ExceptionListener() {
            public void exceptionThrown(final ExceptionEvent exceptionEvent_) {
					System.err.println(StringUtils.crop("Error: " + exceptionEvent_.getException().getMessage() + ".", MAX_ERROR_MESSAGE_LENGTH));
            }
         });
		}

		songSearchCampaign_.start();
	}
}

