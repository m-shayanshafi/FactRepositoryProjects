/*	
	This file is part of NitsLoch.

	Copyright (C) 2007 Darren Watts

    NitsLoch is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    NitsLoch is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with NitsLoch.  If not, see <http://www.gnu.org/licenses/>.
 */

package src.game;

/**
 * Singleton class used for sending messages to the gui and displaying them.
 * @author Darren Watts
 * date 11/11/07
 *
 */
public class Messages {
	private String[] messages;
	private static Messages instance = null;

	private Messages() {
		messages = new String[src.Constants.NUM_MESSAGES];
		for(int i = 0; i < src.Constants.NUM_MESSAGES; i++){
			messages[i] = "";
		}
	}

	/**
	 * Gets an instance of this class.
	 * @return Messages : instance
	 */
	public static Messages getInstance(){
		if(instance == null)
			instance = new Messages();
		return instance;
	}

	/**
	 * Sets the instance to null.
	 */
	public void clearInstance(){
		instance = null;
	}

	/**
	 * Adds a line of text to the messages string.
	 * @param str String : message
	 */
	public void addMessage(String str){
		String fullStr = str;
		String nextStr = "";
		boolean needNextStr = false;
		for(int i = 0; i < str.length(); i++){
			if(str.charAt(i) == '\n'){
				str = str.substring(0, i);
				nextStr = fullStr.substring(i+1);
				needNextStr = true;
			}
		}
		
		if(str.length() > src.Constants.MESSAGE_LINE_LEN){
			addLongMessage(str);
			if(needNextStr)
				addMessage(nextStr);
			return;
		}
		int addIndex = 0;
		if(messages[0].equals("")){
			for(int i = 0; i < src.Constants.NUM_MESSAGES; i++){
				if(messages[i].equals("")){ addIndex = i; break; }
			}
			messages[addIndex] = str;
		}
		else{
			for(int i = src.Constants.NUM_MESSAGES-1; i > 0; i--){
				messages[i] = messages[i-1];
			}
			messages[0] = str;
		}
		if(needNextStr)
			addMessage(nextStr);
	}

	/**
	 * Adds a message that is too long to fit on one line.  It
	 * will split the line up based on the whitespace and call
	 * the addMessage method for each line after it is split up.
	 * @param str String : message to add
	 */
	private void addLongMessage(String str){
		String talkString = str;

		while(talkString.length() > src.Constants.MESSAGE_LINE_LEN){
			try {
				int count = 1;
				String partialString = talkString.substring(0, src.Constants.MESSAGE_LINE_LEN-count);
				while(!Character.isWhitespace(partialString.charAt(partialString.length()-1)) &&
						count < src.Constants.MESSAGE_LINE_LEN){
					count++;
					partialString = talkString.substring(0, src.Constants.MESSAGE_LINE_LEN-count);
				}

				// If the word is too long to fit on the line, break it up.
				if(count >= src.Constants.MESSAGE_LINE_LEN) {
					count = 1;
					partialString = talkString.substring(0, src.Constants.MESSAGE_LINE_LEN-count);
				}

				addMessage(partialString);
				talkString = talkString.substring(src.Constants.MESSAGE_LINE_LEN-count);
			} catch(Exception ex) {

			}
		}
		addMessage(talkString);
	}

	/**
	 * Returns the messages as a string for the GUI to display.
	 * @return String : The messages
	 */
	public String getMessages(){
		String str = "";
		for(int i = src.Constants.NUM_MESSAGES-1; i >= 0; i--){
			str += messages[i] + "\n";
		}
		return str;
	}

}
