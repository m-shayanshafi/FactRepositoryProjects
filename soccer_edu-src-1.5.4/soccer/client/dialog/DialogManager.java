/* DialogManager.java
   This class stores all the client dialogs of TOS

   Copyright (C) 2004  Yu Zhang

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the
   Free Software Foundation, Inc.,
   59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


	Modifications by Vadim Kyrylov 
							January 2006
							
*/

package soccer.client.dialog;

import soccer.client.SoccerMaster;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class DialogManager 
{
	private SoccerMaster m_client;
	// We can hold on to any dialogs that are capable of resetting
	// themselves on each call.
	ViewDialog 		m_view;
	PlayDialog 		m_play;
	ServerDialog 	m_server;
	AIDialog 		m_ai;
	SituationDialog m_situation;

	ArrayList m_alVisibleDialogs;
	public DialogManager(SoccerMaster c) {
		m_client = c;
		m_alVisibleDialogs = new ArrayList();
	}

	protected DialogManager() {
		this(null);
	}

	private class DialogInvoker implements Runnable {
		private JDialog m_dialog;
		public DialogInvoker(JDialog d) {
			m_dialog = d;
		}
		public void run() {
			m_dialog.pack();
			m_dialog.setLocationRelativeTo(m_client);
			m_dialog.setVisible(true);
		}
	}
	private class DialogCloser implements Runnable {
		private JDialog m_dialog;
		public DialogCloser(JDialog d) {
			m_dialog = d;
		}
		public void run() {
			m_dialog.setVisible(false);
		}
	}
	/**
	 * Packs and opens the specified dialog in the AWT event thread.
	 * Remembers that the dialog is open so that a call to
	 * {@link #closeAllDialogs()} will hide the dialog. You should
	 * use {@link #closeDialog(JDialog)} to pop the dialog back down
	 * programmatically (in response to an event e.g. a button click)
	 */
	void showDialog(JDialog d) {
		// Remember the dialog.
		m_alVisibleDialogs.add(d);
		// Pack & display
		SwingUtilities.invokeLater(new DialogInvoker(d));
	}
	/**
	 * Hides the specified dialog and forgets about it for the purposes
	 * of {@link #closeAllDialogs()}.
	 */
	void hideDialog(JDialog d) {
		m_alVisibleDialogs.remove(d);
		SwingUtilities.invokeLater(new DialogCloser(d));
	}
	/**
	 * Closes all dialogs that have been opened using {@link #openDialog(JDialog)}
	 * but not yet closed with {@link #closeDialog(JDialog)}.
	 */
	public void hideAllDialogs() {
		for (int i = 0; i < m_alVisibleDialogs.size(); i++) {
			JDialog d = (JDialog) m_alVisibleDialogs.get(i);
			SwingUtilities.invokeLater(new DialogCloser(d));
		}
	}

	public ViewDialog getViewDialog() {
		if (m_view == null) {
			m_view = new ViewDialog(this, m_client);
		}
		return m_view;
	}

	public PlayDialog getPlayDialog() {
		if (m_play == null) {
			m_play = new PlayDialog(this, m_client);
		}
		return m_play;
	}

	public ServerDialog getServerDialog() {
		if (m_server == null) {
			m_server = new ServerDialog(this, m_client);
		}
		return m_server;
	}

	public AIDialog getAIDialog() {
		if (m_ai == null) {
			m_ai = new AIDialog(this, m_client);
		}
		return m_ai;
	}

	public SituationDialog getSituationDialog( File file ) {
			// always create new instance, as the file couold be differnt
		m_situation = new SituationDialog(this, m_client, file );
		return m_situation;
	}
	
	class MessageDialogRunnable implements Runnable {
		private String m_title, m_message;
		public MessageDialogRunnable(String title, String message) {
			m_title = title;
			m_message = message;
		}
		public void run() {
			JOptionPane.showMessageDialog(
				m_client,
				m_title,
				m_message,
				JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Display a message dialog, and return immediately
	 *
	 * @param message the message to display in the dialog
	 */
	public void showMessageDialogNonBlocking(String message) {
		showMessageDialogNonBlocking(SoccerMaster.APP_NAME, message);
	}

	/**
	 * Display a message dialog. Block until the user dismisses the dialog
	 *
	 * @param message the message to display
	 */
	public void showMessageDialog(String message) {
		showMessageDialogBlocking(SoccerMaster.APP_NAME, message);
	}

	/**
	 * @deprecated use showMessageDialogNonBlocking( String )
	 */
	public void showMessageDialogNonBlocking(String title, String message) {
		SwingUtilities.invokeLater(new MessageDialogRunnable(title, message));
	}
	/**
	 * @deprecated use showMessageDialog( String )
	 */
	public void showMessageDialogBlocking(String title, String message) {
		JOptionPane.showMessageDialog(
			m_client,
			message,
			title,
			JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Display a yes/no confirmation dialog.  Blocking, of course.
	 */
	public int showConfirmationDialog(String message) {
		return JOptionPane.showConfirmDialog(
			m_client,
			message,
			SoccerMaster.APP_NAME,
			JOptionPane.YES_NO_OPTION);
	}

	/**
	 * Display dialog asking for the user to type something in.
	 */
	public String showInputDialog(
		String message,
		String initialSelectionValue) {
		JOptionPane pane =
			new JOptionPane(
				message,
				JOptionPane.QUESTION_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION);
		pane.setWantsInput(true);
		pane.setInitialSelectionValue(initialSelectionValue);

		JDialog dialog = pane.createDialog(m_client, SoccerMaster.APP_NAME);
		dialog.show();

		return (String) pane.getInputValue();
	}
}
