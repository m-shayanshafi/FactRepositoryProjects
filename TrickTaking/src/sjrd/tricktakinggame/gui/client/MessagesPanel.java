/*
 * TrickTakingGame - Trick-taking games platform on-line
 * Copyright (C) 2008  Sébastien Doeraene
 * All Rights Reserved
 *
 * This file is part of TrickTakingGame.
 *
 * TrickTakingGame is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * TrickTakingGame is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * TrickTakingGame.  If not, see <http://www.gnu.org/licenses/>.
 */
package sjrd.tricktakinggame.gui.client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import sjrd.tricktakinggame.gui.networkactions.*;
import sjrd.tricktakinggame.network.client.*;
import sjrd.tricktakinggame.remotable.*;

/**
 * Panel des messages
 * @author sjrd
 */
public class MessagesPanel extends JPanel implements MessageProvider
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Panel propriétaire
	 */
	private Client client;

	/**
	 * Check box permettant de choisir si on accepte les messages de la salle
	 */
	private JCheckBox acceptRoomCheckBox;

	/**
	 * Modèle de la liste des messages reçus
	 */
	private MessageListModel messageListModel;

	/**
	 * Liste des messages reçus
	 */
	private JList messagesList;

	/**
	 * Panneau de défilement de la liste des messages reçus
	 */
	private JScrollPane messagesScrollPane;

	/**
	 * Check box permettant d'envoyer à la salle entière
	 */
	private JCheckBox sendToRoomCheckBox;

	/**
	 * Champ message
	 */
	private JTextField messageField;

	/**
	 * Action d'envoi de message de chat
	 */
	private SendChatMessageAction sendChatMessageAction;

	/**
	 * Bouton d'envoi de message de chat
	 */
	private JButton sendChatMessageButton;

	/**
	 * Crée le panel
	 */
	public MessagesPanel(Client aClient)
	{
		super(new BorderLayout());

		client = aClient;

		build();

		client.addMessageListener(new MessageReceivedListener());
	}

	/**
	 * Construit le panel
	 */
	private void build()
	{
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

		// Check boxes pour filtrer les messages entrant

		JPanel checkBoxesPanel = new JPanel(new FlowLayout());

		acceptRoomCheckBox = new JCheckBox("Recevoir depuis la salle", true);
		checkBoxesPanel.add(acceptRoomCheckBox);

		add(checkBoxesPanel, BorderLayout.NORTH);

		// Liste des messages

		messageListModel = new MessageListModel();
		messagesList = new JList(messageListModel);
		messagesList.setCellRenderer(new MessageCellRenderer());
		messagesScrollPane = new JScrollPane(messagesList);
		messagesScrollPane.setPreferredSize(new Dimension(
			Integer.MAX_VALUE, 100));
		add(messagesScrollPane, BorderLayout.CENTER);

		// Envoi de messages

		sendChatMessageAction = new SendChatMessageAction(client, this,
		"Envoyer");

		JPanel bottomPanel = new JPanel(new BorderLayout());

		sendToRoomCheckBox = new JCheckBox("Pour la salle", true);
		bottomPanel.add(sendToRoomCheckBox, BorderLayout.WEST);

		messageField = new JTextField();
		messageField.addFocusListener(new MessageFieldFocusListener());
		bottomPanel.add(messageField, BorderLayout.CENTER);

		sendChatMessageButton = new JButton(sendChatMessageAction);
		bottomPanel.add(sendChatMessageButton, BorderLayout.EAST);

		add(bottomPanel, BorderLayout.SOUTH);
	}

	/**
	 * {@inheritDoc}
	 */
	public Message fetchMessage()
	{
		String message = messageField.getText().trim();
		messageField.setText("");

		if (message.length() == 0)
			return null;

		MessageSource source;
		if (sendToRoomCheckBox.isSelected())
			source = MessageSource.Room;
		else
			source = MessageSource.Table;

		return new Message(source, message);
	}

	/**
	 * Teste si un message doit être affiché
	 * @param source Source du message
	 * @return <tt>true</tt> si le message doit être accepté, <tt>false</tt>
	 *         sinon
	 */
	public boolean acceptMessage(MessageSource source)
	{
		if (source == MessageSource.Room)
			return acceptRoomCheckBox.isSelected();
		else
			return true;
	}

	/**
	 * Listener du focus du champ message
	 * @author sjrd
	 */
	private class MessageFieldFocusListener implements FocusListener
	{
		/**
		 * {@inheritDoc}
		 */
		public void focusGained(FocusEvent event)
		{
			getRootPane().setDefaultButton(sendChatMessageButton);
		}

		/**
		 * {@inheritDoc}
		 */
		public void focusLost(FocusEvent event)
		{
			getRootPane().setDefaultButton(null);
		}
	}

	/**
	 * Listener pour la réception de messages du serveur
	 * @author sjrd
	 */
	private class MessageReceivedListener implements MessageListener
	{
		/**
		 * {@inheritDoc}
		 */
		public void messageReceived(final Message message)
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					if (!acceptMessage(message.getSource()))
						return;

					messageListModel.add(message);

					/*
					 * Il faut que l'événement de mise à jour des bornes de la
					 * scroll-bar soit effectué avant de faire l'auto-scroll.
					 * Sinon, les nouveaux messages ne sont pas pris en compte,
					 * et on ne peut pas les voir.
					 */
					SwingUtilities.invokeLater(new AutoScroll());
				}
			});
		}

		/**
		 * Runnable qui fait l'auto-scroll
		 * @author sjrd
		 */
		private class AutoScroll implements Runnable
		{
			/**
			 * {@inheritDoc}
			 */
			public void run()
			{
				BoundedRangeModel model =
					messagesScrollPane.getVerticalScrollBar().getModel();
				model.setValue(model.getMaximum()-model.getExtent());
			}
		}
	}

	/**
	 * Renderer pour la liste des messages reçus
	 * @author sjrd
	 */
	private class MessageCellRenderer extends JLabel implements ListCellRenderer
	{
		/**
		 * ID de sérialisation
		 */
		private static final long serialVersionUID = 1;

		/**
		 * {@inheritDoc}
		 */
		public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus)
		{
			MessageSource source;
			String message;
			
			if (value instanceof Message)
			{
				Message msgValue = (Message) value;
				source = msgValue.getSource();
				message = msgValue.getMessage();
			}
			else
			{
				source = MessageSource.Admin;
				message = value.toString();
			}
			
			setText(message);
			
			Color background;
			Color foreground;
			
			if (isSelected)
			{
				background = list.getSelectionBackground();
				foreground = list.getSelectionForeground();
			}
			else
			{
				background = list.getBackground();
				foreground = list.getForeground();
			}
			
			if ((source == MessageSource.Admin) ||
				(source == MessageSource.RulesError))
				foreground = Color.red;
			
			setBackground(background);
			setForeground(foreground);
			setEnabled(list.isEnabled());
			setFont(list.getFont());
			setOpaque(true);
			
			return this;
		}
	}
}
