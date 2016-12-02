/**
 * JSkat - A skat program written in Java
 * by Jan Schäfer and Markus J. Luzius
 *
 * Version 0.10.1
 * Copyright (C) 2012-03-25
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jskat.control.iss;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jskat.data.SkatGameData;
import org.jskat.util.JSkatResourceBundle;

/**
 * Handles messages from ISS
 */
public class MessageHandler extends Thread {

	static Log log = LogFactory.getLog(MessageHandler.class);

	Connector connect;
	IssController issControl;

	JSkatResourceBundle strings;

	List<String> messageList;

	private final static int protocolVersion = 14;

	/**
	 * Constructor
	 * 
	 * @param conn
	 *            Connection to ISS
	 * @param controller
	 *            ISS controller for JSkat
	 */
	public MessageHandler(Connector conn, IssController controller) {

		connect = conn;
		issControl = controller;

		strings = JSkatResourceBundle.instance();

		messageList = new ArrayList<String>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		while (true) {
			if (messageList.size() > 0) {

				String message = getNextMessage();
				handleMessage(message);
			} else {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					log.warn("Thread.sleep() was interrupted");
				}
			}
		}
	}

	synchronized void addMessage(String newMessage) {

		messageList.add(newMessage);
	}

	private synchronized String getNextMessage() {

		return messageList.remove(0);
	}

	private void handleMessage(String message) {

		log.debug("ISS    |--> " + message); //$NON-NLS-1$

		if (message == null) {

			connect.closeConnection();
			issControl.closeIssPanels();
		} else {

			StringTokenizer tokenizer = new StringTokenizer(message); // get
																		// first
			// command
			String first = tokenizer.nextToken();
			// get all parameters
			List<String> params = new ArrayList<String>();
			while (tokenizer.hasMoreTokens()) {
				params.add(tokenizer.nextToken());
			}

			try {

				handleMessage(first, params);

			} catch (Exception except) {
				log.error("Error in parsing ISS protocoll", except); //$NON-NLS-1$
				issControl.showMessage(JOptionPane.ERROR_MESSAGE,
						strings.getString("iss_error_parsing_iss_protocol")); //$NON-NLS-1$
			}
		}
	}

	void handleMessage(String first, List<String> params) {

		MessageType type = MessageType.getByString(first);

		if (MessageType.UNKNOWN.equals(type)) {

			log.error("UNHANDLED MESSAGE: " + first + params.toString()); //$NON-NLS-1$ }
		} else {
			// FIXME (jansch 30.05.2011) put message into a queue
			try {
				handleMessageObsolete(type, params);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void handleMessageObsolete(MessageType type, List<String> params)
			throws Exception {

		switch (type) {
		case PASSWORD:
			handlePasswordMessage();
			break;
		case WELCOME:
			handleWelcomeMessage(params);
			break;
		case CLIENTS:
			handleClientListMessage(params);
			break;
		case TABLES:
			handleTableListMessage(params);
			break;
		case CREATE:
			handleTableCreateMessage(params);
			break;
		case INVITE:
			handleTableInvitationMessage(params);
			break;
		case TABLE:
			handleTableUpdateMessage(params);
			break;
		case DESTROY:
			handleTableDestroyMessage(params);
			break;
		case ERROR:
			handleErrorMessage(params);
			break;
		case TEXT:
			handleTextMessage(params);
			break;
		case YELL:
			handleLobbyChatMessage(params);
			break;
		}
	}

	void handleLobbyChatMessage(List<String> params) {

		issControl.addChatMessage(ChatMessageType.LOBBY, params);
	}

	void handlePasswordMessage() {

		connect.sendPassword();
	}

	void handleTextMessage(List<String> params) {
		// FIXME show it to the user
		log.error(params.toString());
	}

	void handleErrorMessage(List<String> params) {

		log.error(params.toString());
		// FIXME (jan 23.11.2010) i18n needed
		issControl.showMessage(JOptionPane.ERROR_MESSAGE,
				getI18ErrorString(getErrorString(params)));
	}

	private String getErrorString(List<String> params) {

		for (String param : params) {
			if (param.startsWith("_")) { //$NON-NLS-1$
				return param;
			}
		}

		return params.toString();
	}

	private String getI18ErrorString(String errorString) {

		if ("_id_pw_mismatch".equals(errorString)) { //$NON-NLS-1$
			return strings.getString("iss_login_password_wrong"); //$NON-NLS-1$
		} else if ("_not_your_turn".equals(errorString)) { //$NON-NLS-1$
			return strings.getString("iss_not_your_turn"); //$NON-NLS-1$
		} else if ("_invalid_move_colon".equals(errorString)) { //$NON-NLS-1$
			return strings.getString("iss_invalid_move_colon"); //$NON-NLS-1$
		}

		return errorString;
	}

	void handleTableCreateMessage(List<String> params) {

		log.debug("table creation message"); //$NON-NLS-1$

		String tableName = params.get(0);
		String creator = params.get(1);
		int seats = Integer.parseInt(params.get(2));
		issControl.createTable(tableName, creator, seats);
	}

	void handleTableDestroyMessage(List<String> params) {

		log.debug("table destroy message"); //$NON-NLS-1$

		String tableName = params.get(0);
		issControl.destroyTable(tableName);
	}

	void handleTableInvitationMessage(List<String> params) {
		log.debug("table destroy message"); //$NON-NLS-1$

		String invitor = params.get(0);
		String tableName = params.get(1);
		String invitationTicket = params.get(2);

		issControl.handleInvitation(invitor, tableName, invitationTicket);
	}

	/**
	 * table .1 bar state 3 bar xskat xskat:2 . bar . 0 0 0 0 0 0 1 0 xskat $ 0
	 * 0 0 0 0 0 1 1 xskat:2 $ 0 0 0 0 0 0 1 1 . . 0 0 0 0 0 0 0 0 false
	 */
	void handleTableUpdateMessage(List<String> params) {

		log.debug("table update message"); //$NON-NLS-1$

		String tableName = params.get(0);

		if (issControl.isTableJoined(tableName)) {

			// FIXME (jan 18.11.2010) is this the name of the creator or the
			// login name of the current player?
			String creator = params.get(1);
			String actionCommand = params.get(2);
			List<String> detailParams = params.subList(3, params.size());

			if (actionCommand.equals("error")) { //$NON-NLS-1$

				handleErrorMessage(params.subList(3, params.size()));

			} else if (actionCommand.equals("state")) { //$NON-NLS-1$

				issControl.updateISSTableState(tableName,
						MessageParser.getTableStatus(creator, detailParams));

			} else if (actionCommand.equals("start")) { //$NON-NLS-1$

				issControl
						.updateISSGame(tableName, MessageParser
								.getGameStartStatus(creator, detailParams));

			} else if (actionCommand.equals("go")) { //$NON-NLS-1$

				issControl.startGame(tableName);

			} else if (actionCommand.equals("play")) { //$NON-NLS-1$

				issControl.updateMove(tableName,
						MessageParser.getMoveInformation(detailParams));

			} else if (actionCommand.equals("tell")) { //$NON-NLS-1$

				issControl.updateISSTableChatMessage(tableName, MessageParser
						.getTableChatMessage(tableName, detailParams));

			} else if (actionCommand.equals("end")) { //$NON-NLS-1$

				issControl.endGame(tableName, getGameInformation(detailParams));

			} else {

				log.debug("unhandled action command: " + actionCommand + " for table " + tableName); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}

	private SkatGameData getGameInformation(List<String> params) {

		// first glue alle params back together
		String gameResult = glueParams(params);

		return MessageParser.parseGameSummary(gameResult);
	}

	private String glueParams(List<String> params) {

		String result = new String();
		Iterator<String> paramIterator = params.iterator();

		while (paramIterator.hasNext()) {

			if (result.length() > 0) {
				result += " "; //$NON-NLS-1$
			}

			result += paramIterator.next();
		}

		return result;
	}

	/**
	 * Handles a client list message
	 * 
	 * @param params
	 *            parameters
	 */
	void handleClientListMessage(List<String> params) {

		String plusMinus = params.remove(0);

		if (plusMinus.equals("+")) { //$NON-NLS-1$

			updateClientList(params);

		} else if (plusMinus.equals("-")) { //$NON-NLS-1$

			removeClientFromList(params);
		}
	}

	/**
	 * Adds or updates a client on the client list
	 * 
	 * @param params
	 *            Player information
	 */
	void updateClientList(List<String> params) {

		String playerName = params.get(0);
		String language = params.get(2);
		long gamesPlayed = Long.parseLong(params.get(3));
		double strength = Double.parseDouble(params.get(4));

		issControl.updateISSPlayerList(playerName, language, gamesPlayed,
				strength);
	}

	/**
	 * Removes a client from the client list
	 * 
	 * @param params
	 *            Player information
	 */
	void removeClientFromList(List<String> params) {

		issControl.removeISSPlayerFromList(params.get(0));
	}

	/**
	 * Handles the welcome message and checks the protocol version
	 * 
	 * @param params
	 *            Welcome information
	 */
	void handleWelcomeMessage(List<String> params) {

		double issProtocolVersion = Double
				.parseDouble(params.get(params.size() - 1));

		log.debug("iss version: " + issProtocolVersion); //$NON-NLS-1$
		log.debug("local version: " + protocolVersion); //$NON-NLS-1$

		if ((int) issProtocolVersion != protocolVersion) {
			// TODO handle this in JSkatMaster
			log.error("Wrong protocol version!!!"); //$NON-NLS-1$
		}
	}

	/**
	 * Handles a table list message
	 * 
	 * @param params Table information
	 */
	void handleTableListMessage(List<String> params) {

		String plusMinus = params.remove(0);

		if (plusMinus.equals("+")) { //$NON-NLS-1$

			updateTableList(params);

		} else if (plusMinus.equals("-")) { //$NON-NLS-1$

			removeTableFromList(params);
		}
	}

	/**
	 * Adds or updates a table on the table list
	 * 
	 * @param params
	 *            Table information
	 */
	void updateTableList(List<String> params) {

		String tableName = params.get(0);
		int maxPlayers = Integer.parseInt(params.get(1));
		long gamesPlayed = Long.parseLong(params.get(2));
		String player1 = params.get(3);
		String player2 = params.get(4);
		String player3 = params.get(5);

		issControl.updateISSTableList(tableName, maxPlayers, gamesPlayed,
				player1, player2, player3);
	}

	/**
	 * Removes a table from the table list
	 * 
	 * @param params
	 *            Table information
	 */
	void removeTableFromList(List<String> params) {

		issControl.removeISSTableFromList(params.get(0));
	}

}
