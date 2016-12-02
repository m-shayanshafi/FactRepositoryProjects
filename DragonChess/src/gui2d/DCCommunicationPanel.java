/*
 * Classname			: DCCommunicationPanel 
 * Author 			: Koenraad Heijlen <vipie@ulyssis.org>
 * Author			: Christophe Hertigers <xof@pandora.be>
 * Author  			: TheBlackUnicorn <TheBlackUnicorn@softhome. net>
 * Creation Date 		: 2002-12-14
 * Last Updated 		: 2002-12-16
 * Description 			: A Panel to get communication between two players, it 
 * 				  also handels system messages.
 * Copyright    		: GNU GPL v2.
 * GPL disclaimer 		: 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; version 2 of the License.   This program is distributed
 * in the hope that it will be useful, but   WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY   or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License   for more details. You should
 * have received a copy of the GNU General   Public License along with this
 * program; if not, write to the Free Software   Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA  02111- 1307  USA
 */

package gui2d;

//import java.awt.BorderLayout;
import java.awt.Color;
//import java.awt.Dimension;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.text.*;

import main.DCConstants;

/**
 * @author Koenraad Heijlen (vipie@ulyssis.org)
 * @version DCCommunicationPanel.java,v 1.1 2002/12/14 23:49:48 vipie Exp
 *
 * @todo FIXME This panel should contain an option for sending private or public,  
 *  	 overriding for now
 */
public class DCCommunicationPanel extends JPanel {

	/** 
	 * The DC2dGUI in wich this panel is docked.
	 */
	DC2dGUI myDC2dGUI;
	/** 
	 * The gridbaglayout use to layout the elements.
	 */
	GridBagLayout gb;
	GridBagConstraints c;
	/** 
	 * The entry field, this is where we read the user's input.
	 */
	JTextField inputField;
	/** 
	 * Button pressed when the user tries to send a msg.
	 */
	JButton sendButton;
	/** 
	 * Button pressed when the user tries to clear the history in the textPane.
	 */
	JButton clearHistoryButton;

	/** 
	 * The pane containing the messages.
	 */
	JTextPane textPane;
	/** 
	 * The pane that scrolls the textPane.
	 */
	JScrollPane scrollPane;
	/** 
	 * The attributes used is the StyledDocument
	 */
	SimpleAttributeSet[] attrs;
	/** 
	 * The Styled Document used in the textPane
	 */
	DefaultStyledDocument lsd;
	/** 
	 * A string representing an newline, used for clarity.
	 */
	String newline= "\n";

	/** 
	 * The array containing the default prefixes, used when outputting a message,
	 * this is similar to the IRC 'nick' concept, the user has a unique 'nick'.
	 */
	String[] nameString= { "Player1", "Player2", "System", "Both Players", "Spectator"};
	
	/** 
	 * Should the spectators get this message (true means they'll get it).
	 */
	boolean publicBoolean = false;

	/** 
	 * The Action invoked when the user tries to send a new message.
	 */
	SendAction sendAction = new SendAction();
	/** 
	 * The Action invoked when the user tries to clear the history.
	 */
	ClearAction clearAction = new ClearAction();

	/*
	 * INNER CLASSES 
	 *
	 */
	
	/** 
	 * Action to Send a message.
	 *
	 * Used for the <code>inputField</code> and the <code>sendButton</code>.
	 */
	private class SendAction extends AbstractAction {
		public SendAction() {
			super ("Send!", null);
			putValue(SHORT_DESCRIPTION, "Send this message");
		}

		public void actionPerformed(ActionEvent e) {
			String input= "";
			try {
				input= inputField.getText();
			} catch (NullPointerException ex) {
				input= "";
			}

			if (!input.equals("") && !input.equals(" ")) {
				int player = myDC2dGUI.getPossibleActivePlayers();
				if (player == DCConstants.PLAYER_BOTH) {
					player = myDC2dGUI.getActivePlayer();
				}

				/*
				 * Send it only to the backend, it will return.
				 */
				 
				//SendMessage
				myDC2dGUI.sendOut(myDC2dGUI.getEncoder().chatMessage(player,
							publicBoolean, input));
				//clear message box
				inputField.setText("");
				inputField.requestFocus();
			}
		}
	}

	/** 
	 * Action to clear the history.
	 *
	 * Used for the <code>clearButton</code>.
	 */
	private class ClearAction extends AbstractAction {
		public ClearAction() {
			super("Clear All", null);
			putValue(SHORT_DESCRIPTION, "Clear all messages");
		}

		public void actionPerformed(ActionEvent e) {
			try {
				lsd.remove(0, lsd.getLength());
			} catch (BadLocationException ex) {}
		}
	}
		
	/**
	 * Constructs a new  DCCommunicationPanel without link to a DC2dGUI. 
	 *
	 */
	public DCCommunicationPanel() {
		super();

		gb = new GridBagLayout();
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		lsd= new DefaultStyledDocument();
		textPane= new JTextPane(lsd);
		textPane.setMargin(new Insets(2, 2, 2, 2));

		scrollPane= new JScrollPane(textPane);
		//scrollPane.setPreferredSize(new Dimension(100, 10));
		scrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		gb.setConstraints(scrollPane, c);
		
		inputField= new JTextField(40);
		c.weighty = 0.0;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		gb.setConstraints(inputField, c);
		inputField.setAction(sendAction);
		
		sendButton= new JButton(sendAction);
		c.weightx = 0.0;
		c.gridx = 2;
		gb.setConstraints(sendButton, c);
		
		clearHistoryButton= new JButton(clearAction);
		c.gridx = 0;
		gb.setConstraints(clearHistoryButton, c);

		this.setLayout(gb);
		this.add(scrollPane);
		this.add(sendButton);
		this.add(inputField);
		this.add(clearHistoryButton);

		initAtributes();
		this.systemMessage("Communication Panel loaded...");
		//this.setPreferredSize(new Dimension(400, 100));
		
		// set the gui to null.
		myDC2dGUI = null;
	}
	
	/**
	 * Method DCCommunicationPanel.
	 * @param myDC2dGUI the gui controlling this CommunicationPanel.
	 */
	DCCommunicationPanel(DC2dGUI myDC2dGUI) {
		this();
		this.myDC2dGUI = myDC2dGUI;
	}

	/**
	 * Method initAtributes.
	 *
	 * @post the attributes are initialised, ready for use.
	 */
	private void initAtributes() {

		attrs= new SimpleAttributeSet[5];

		attrs[0]= new SimpleAttributeSet();
		StyleConstants.setBold(attrs[0], true);

		attrs[1]= new SimpleAttributeSet();
		StyleConstants.setBackground(attrs[1], new Color(224, 244, 251));

		attrs[2]= new SimpleAttributeSet();
		StyleConstants.setBackground(attrs[2], new Color(165, 228, 153));
		
		attrs[3]= new SimpleAttributeSet(attrs[0]);
		StyleConstants.setForeground(attrs[3], Color.RED);
		
		attrs[4]= new SimpleAttributeSet();
		StyleConstants.setBackground(attrs[4], Color.gray);

	}

	/**
	 * Put the message on the textPane.
	 * @param publicBoolean true if it is public.
	 * @param playerName the String representing the player from which it came.
	 * @param message the message passed.
	 *
	 * @pre the DC2dGUI should be existing.
	 * @post the message is shown.
	 */
	public void playerMessage(boolean publicBoolean, String playerName, String message) {
	
		if (playerName.equals(this.myDC2dGUI.getGoldPlayerName())) {
			textAppend(playerName + ": ", attrs[0]);
			textAppend(message + newline, attrs[1]);
		} else if (playerName.equals(this.myDC2dGUI.getScarletPlayerName())) {
			textAppend(playerName + ": ", attrs[0]);
			textAppend(message + newline, attrs[2]);	
		} else {
			textAppend(playerName + ": ", attrs[0]);
			textAppend(message + newline, attrs[4]);
		}
	}

	/**
	 * Put the message from the system on the textPane.
	 * @param string the message.
	 *
	 * @post the message in shown.
	 */
	public void systemMessage(String string) {
		this.textAppend(nameString[2] + ": ", attrs[0]);
		this.textAppend(string + newline, attrs[3]);
	}

	/**
	 * Method textAppend.
	 * @param string the string to append to the StyleDocument.
	 * @param simpleAttributeSet the attribute used to print <string>.
	 *
	 * @post the string is appended.
	 * @post the scrollpane is scrolled to the end.
	 *
	 */
	private void textAppend(String string, SimpleAttributeSet simpleAttributeSet) {
		try {
			lsd.insertString(lsd.getLength(), string, simpleAttributeSet);
			scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
		} catch (BadLocationException e) {}

	}

	/**
	 * Enables or disables the communication system.
	 * @param enabled	true to enable chat, false to disable it.
	 *
	 */
	public void setChatEnabled(boolean enabled) {
		sendAction.setEnabled(enabled);
	}
	
	/**
	 * Get the status of the communication system.
	 * @return boolean true when enabled, false otherwise.
	 *
	 */
	public boolean getPublicBoolean()
	{
	    return publicBoolean;
	}
	
}
