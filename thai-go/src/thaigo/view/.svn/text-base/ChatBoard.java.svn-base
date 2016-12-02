package thaigo.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import thaigo.network.client.Client;
import thaigo.network.server.ClientHandler;
import thaigo.utility.ChatMessage;
import thaigo.utility.PropertyManager;

/**
 * ChatBoard is panel for chatting.
 * 
 * @author Poramate Homprakob 5510546077 & Nol 5510546018
 * @version 2013.4.21
 *
 */
public class ChatBoard extends JPanel {

	private ColorTextPane textarea;
	private JTextField input;
	private JButton submit,colorChooser;
	private JScrollPane scrollPane;

	private GameUI gameUI;

	private Client client;
	private ClientHandler server;

	/** Initializes the chat board. */
	public ChatBoard() {
		TitledBorder title = new TitledBorder("Chat Board");
		this.setBorder(title);
		this.setLayout(new GridBagLayout());

		textarea = new ColorTextPane();
		scrollPane = new JScrollPane(textarea);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
		scrollPane.setPreferredSize( new Dimension(450,150) );
		textarea.setEditable(false);
		textarea.setBorder(BorderFactory.createEtchedBorder());
		
		addComponent(scrollPane, 0, 0, 3, 1);

		input = new JTextField(25);
		input.addActionListener(new ChatListener());
		addComponent(input, 1, 1, 1, 1);
		
		colorChooser = new JButton("C");
		colorChooser.setToolTipText("Choose your Text Color");
		colorChooser.setPreferredSize( new Dimension(42,5) );
		colorChooser.addActionListener( new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color color = JColorChooser.showDialog(null, "Choose your text color", input.getForeground());
				if( color != null )
					input.setForeground(color);
			}
			
		});
		addComponent(colorChooser, 0, 1, 1, 1);
		
		
		submit = new JButton("Chat");
		submit.addActionListener(new ChatListener());
		addComponent(submit, 2, 1, 1, 1);
	}

	/** Adds components to this frame.
	 * 
	 * @param component Component to be added
	 * @param x X-Axis
	 * @param y Y-Axis
	 * @param width Width
	 * @param height Height
	 */
	private void addComponent(Component component, int x, int y, int width, int height) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(component, gbc);
	}

	/** Sets the client for the chat board.
	 * 
	 * @param client Client
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	/** Sets the clients handler for the chat board.
	 * 
	 * @param server ClientHandler
	 */
	public void setClientHandler(ClientHandler server) {
		this.server = server;
	}

	/** Adds the chat message to the chat board.
	 * 
	 * @param string Chat message
	 * @param color Color of message
	 */
	public void addChatMessage(String string , Color color) {
		textarea.addText(string , color);
	}
	
	/** Adds the name of the user before his/her message.
	 * 
	 * @param name User's name
	 * @param mode Color of the name depends on their mode
	 * @param chatText Message
	 * @param textColor Color of the message
	 */
	private void addNameBeforeChatMessage(String name, Color mode, String chatText, Color textColor){
		addChatMessage( String.format("%s  says :",name) , mode );
		addChatMessage( String.format("     %s", chatText), textColor);
	}
	
	/** Enable or disable the chat board.
	 * 
	 * @param bool True to enable the chat board
	 */
	public void setChatable(boolean bool){
		input.setEditable(bool);
		submit.setEnabled(bool);
		colorChooser.setEnabled(bool);
	}

	/** Listener for the <code>Send</code> button. */
	class ChatListener implements ActionListener {

		/** Sends the messages. */
		@Override
		public void actionPerformed(ActionEvent e) {
			String chatText = input.getText();
			if(chatText.equals("")) return;
			Color color = input.getForeground();
			String mode = PropertyManager.getProperty("mode");
			String name = PropertyManager.getProperty("player");
			input.setText("");
			textarea.setCaretPosition(textarea.getDocument().getLength());
			if( mode.equalsIgnoreCase("server") ){
				addNameBeforeChatMessage(name, Color.red, chatText, color);
				server.sendChatMessage(new ChatMessage(String.format("%s  says :",name), Color.red));
				server.sendChatMessage(new ChatMessage(String.format("     %s", chatText), color));
			}
			else if( mode.equalsIgnoreCase("client") ){
				addNameBeforeChatMessage(name, Color.blue, chatText, color);
				client.sendChatMessage(new ChatMessage(String.format("%s  says :",name), Color.blue));
				client.sendChatMessage(new ChatMessage(String.format("     %s", chatText), color));
			}
		}

	}
}
