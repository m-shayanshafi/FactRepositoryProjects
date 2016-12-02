package ChessGameKenai;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * Chat class is extends JPanel and is the part of the chess game where the two
 * players may communicate with each other. The chat includes basic message
 * communication which works with the Packet class to send the messages to each
 * other. This chat also includes smilies which the users can send to each
 * other. The user can also chat certain font setting like bold/italic and the
 * font color. The users can also save the conversation by typing *save in the
 * text field and clear the text area with *clear.
 * 
 * @author Mario Bruno
 * @version 2.0
 */
public class Chat extends JPanel {

	final private JTextPane chatA;
	final private JPanel chatBox, allChatPanel;
	final private JTextField chatF;
	final private JButton btnSend, btnFont;
	private Font font;
	private ChatImage chatImage = new ChatImage();
	final private ChessBoardView view;
	private SimpleAttributeSet smpSet, smpSetUnderline;
	private Color color = Color.ORANGE;
	final private Chess_Data chessData;

	/**
	 * Overloaded constructor which receives references to other classes to be
	 * able to pass messages between them. It gets the ChessBoardView class only
	 * for setting the location of the Font chooser dialog. It also receives the
	 * Chess_Data class for getting the player names.
	 * 
	 * @param view
	 *            as a ChessBoardView object
	 * @param data
	 *            as a Chess_Data object
	 */
	public Chat(ChessBoardView view, Chess_Data data) {

		this.view = view;
		this.chessData = data;

		smpSet = new SimpleAttributeSet();
		smpSetUnderline = new SimpleAttributeSet();
		StyleConstants.setUnderline(smpSetUnderline, true);

		chatBox = new JPanel(new BorderLayout()) {

			/**
			 * The method painComponent of chatBox is used here to paint the
			 * JPanel as we want
			 * 
			 * @param graphics
			 *            Graphics object used to paint this object
			 */
			@Override
			public void paintComponent(Graphics graphics) {
				super.paintComponent(graphics);
				int width = chatBox.getWidth();
				int height = chatBox.getHeight();

				URL url = chatA.getClass().getResource("Icons/background.jpg");
				Toolkit toolkit = this.getToolkit();
				Image image = toolkit.getImage(url);
				graphics.drawImage(image, 0, 0, width, height, chatBox);
			}
		};

		chatBox.setOpaque(false);

		chatA = new JTextPane() {

			/**
			 * The method painComponent of chatA is used here to paint the
			 * JTexchatA as we want
			 * 
			 * @param graphics
			 *            Graphics object used to paint this object
			 */
			@Override
			public void paintComponent(Graphics graphics) {
				int width = chatA.getWidth();
				int height = chatA.getHeight();

				URL url = chatA.getClass().getResource("Icons/background.jpg");
				Toolkit toolkit = this.getToolkit();
				Image image = toolkit.getImage(url);
				graphics.drawImage(image, 0, 0, width, height, chatA);
				super.paintComponent(graphics);
			}
		};

		chatA.setFont(new Font("Dialog", Font.PLAIN, 16));
		appendStr("Chess chat", smpSetUnderline, Color.BLACK);

		btnFont = new JButton("Font");

		btnFont.addActionListener(new ActionListener() {

			/**
			 * The method actionPerformed needs to be overridden in our class
			 * 
			 * @param actionEvent
			 *            ActionEvent object that is generated when listener
			 *            detects an action
			 */
			public void actionPerformed(ActionEvent actionEvent) {
				new FontDialog();
			}
		});

		chatA.setOpaque(false);
		chatA.setEditable(false);

		JScrollPane jScroll = new JScrollPane(chatA,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		TitledBorder titleBorder = new TitledBorder("Chat");
		titleBorder.setTitleFont(font);
		titleBorder.setTitleColor(Color.WHITE);
		chatBox.setBorder(titleBorder);
		chatBox.add(jScroll);

		chatF = new JTextField(10);

		JPanel sendPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));

		btnSend = new JButton("Send");

		btnSend.addActionListener(new ActionListener() {

			/**
			 * The method actionPerformed needs to be overridden in our class
			 * 
			 * @param actionEvent
			 *            ActionEvent object that is generated when listener
			 *            detects action
			 */
			public void actionPerformed(ActionEvent actionEvent) {
				if (!chatF.getText().trim().equals("")
						|| chatImage.getImgPath() != null) {
					if (chatF.getText().trim().equals("*save")) {
						saveChat();
						chatF.setText("");
					} else if (chatF.getText().trim().equals("*clear")) {
						chatA.setText("");
						appendStr("Chess chat", smpSetUnderline, Color.BLACK);
						chatF.setText("");
					} else {
						try {
							sendMsg();
							chatA.setCaretPosition(chatA.getDocument()
									.getLength());
						} catch (IOException ex) {
							Logger.getLogger(Chat.class.getName()).log(
									Level.SEVERE, null, ex);
						}
					}
				}
			}
		});

		sendPnl.add(chatF);
		sendPnl.add(btnSend);
		sendPnl.add(btnFont);
		sendPnl.setOpaque(false);

		ActionListener enterActionListener = new ActionListener() {

			/**
			 * The method actionPerformed needs to be overridden in our class
			 * 
			 * @param actionEvent
			 *            ActionEvent object that is generated when listener
			 *            detects an action
			 */
			public void actionPerformed(ActionEvent actionEvent) {
				if (!chatF.getText().trim().equals("")
						|| chatImage.getImgPath() != null) {
					if (chatF.getText().trim().equals("*save")) {
						saveChat();
						chatF.setText("");
					} else if (chatF.getText().trim().equals("*clear")) {
						chatA.setText("");
						appendStr("Chess chat", smpSetUnderline, Color.BLACK);
						chatF.setText("");
					} else {
						try {
							sendMsg();
							chatA.setCaretPosition(chatA.getDocument()
									.getLength());
						} catch (IOException ex) {
							Logger.getLogger(Chat.class.getName()).log(
									Level.SEVERE, null, ex);
						}
					}
				}
			}
		};

		chatF.addActionListener(enterActionListener);

		allChatPanel = new JPanel(new BorderLayout()) {

			/**
			 * The method painComponent of allChatPanel is used here to paint
			 * the JPanel as we want
			 * 
			 * @param graphics
			 *            Graphics object used to paint this object
			 */
			@Override
			public void paintComponent(Graphics graphics) {
				super.paintComponent(graphics);
				int width = allChatPanel.getWidth();
				int height = allChatPanel.getHeight();

				URL url = chatA.getClass().getResource("Icons/background.jpg");
				Toolkit toolkit = this.getToolkit();
				Image image = toolkit.getImage(url);
				graphics.drawImage(image, 0, 0, width, height, allChatPanel);
			}
		};

		setButtons(false);

		allChatPanel.add(chatBox, BorderLayout.CENTER);
		allChatPanel.add(sendPnl, BorderLayout.SOUTH);
		allChatPanel.setPreferredSize(new Dimension(300, 520));

		this.add(allChatPanel);
		allChatPanel.setPreferredSize(new Dimension(300, 520));
		this.setVisible(true);

	}

	/**
	 * saveChat method is used to save the conversation to a text file. The user
	 * is given the choice where to save the file with a JFileChooser. It also
	 * contains a file filter and automatically saves the file as a .txt file
	 */
	public void saveChat() {
		String path = null;
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
		Date dateNow = new Date();

		JFileChooser saveDialog = new JFileChooser(".");

		saveDialog.setFileFilter(new javax.swing.filechooser.FileFilter() {

			public boolean accept(File file) {
				return file.isDirectory()
						|| file.getName().toLowerCase().endsWith(".txt");
			}

			public String getDescription() {
				return "Text Document (*txt files)";
			}
		});

		saveDialog.setAcceptAllFileFilterUsed(false);

		int returnVal = saveDialog.showDialog(null, "save");

		if (returnVal != JFileChooser.APPROVE_OPTION) {
			return;
		} else {
			File file = saveDialog.getSelectedFile();
			path = file.getPath();

			if (!path.endsWith(".txt")) {
				path = path + ".txt";
			}
		}

		try {

			PrintWriter printerWriter = new PrintWriter(new BufferedWriter(
					new FileWriter(path)));

			printerWriter.println("*****" + dateFormat.format(dateNow)
					+ "*****");
			printerWriter.print(chatA.getText());
			printerWriter.flush();
			printerWriter.close();

		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, ioe.getMessage());
		}
	}

	/**
	 * setChatImage is used to set where the images are on the harddrive
	 * 
	 * @param chatImage
	 *            the path of where the images are stored
	 */
	public void setChatImage(ChatImage chatImg) {
		this.chatImage = chatImg;
	}

	/**
	 * getChatImage is used to simply get the ChatImage chatImage so it can be
	 * used in any other class
	 * 
	 * @return chatImage as a String
	 */
	public ChatImage getChatImage() {
		return chatImage;
	}

	/**
	 * getTxtxPane is used to get the JTextPane so it can be access in any other
	 * class
	 * 
	 * @return chatA as a JTextPane
	 */
	public JTextPane getTxtPane() {
		return chatA;
	}

	/**
	 * getTxtField is used to get the JTextField so it can be access in any
	 * other class
	 * 
	 * @return chatF as a JTextField
	 */
	public JTextField getTxtField() {
		return chatF;
	}

	/**
	 * appendStr is used to append messages
	 * 
	 * @param string
	 *            The message you want to send
	 * @param simpleAttribSet
	 *            the SimpleAttributeSet used
	 * @param color
	 *            Color of the String
	 */
	public final void appendStr(String string,
			SimpleAttributeSet simpleAttribSet, Color color) {
		try {
			Document doc = chatA.getDocument();
			StyleConstants.setForeground(simpleAttribSet, color);
			doc.insertString(doc.getLength(), string, simpleAttribSet);

		} catch (BadLocationException ex) {
			System.err.println(ex.getMessage());
		}
	}

	/**
	 * replaceStr is used to check for a certain string and can replace that
	 * string with another string. ( Used for when smileys )
	 * 
	 * @param original
	 *            The entire entered message
	 * @param find
	 *            The String in which to replace
	 * @param replacement
	 *            The replacement String
	 * @return The new message with the replaced String
	 */
	public String replaceStr(String original, String find, String replacement) {
		int indexOfFind = original.indexOf(find);
		if (indexOfFind < 0) {
			return original; 
		} else {
			String partBefore = original.substring(0, indexOfFind);
			String partAfter = original.substring(indexOfFind + find.length());
			return partBefore + replacement + partAfter;
		}

	}

	/**
	 * sendMsg method is used to send the message to the protocol so the other
	 * user can receive it. It first gets the outputstream, then checks if the
	 * user has entered a smiley, if there is a smiley the word :image is
	 * removed from the message. It then sends the font style,color,message to
	 * the protocol. It also checks if the image path is null or not, if it is
	 * not null that means the user has entered a smiley and it sends the smiley
	 * to the protocol. Finally it gets the users users name and inputs his own
	 * message to his JTextPane.
	 * 
	 * @throws IOException
	 */
	public void sendMsg() throws IOException {
		ObjectOutputStream objectOutputStream = ChessBoardView
				.getConnectionInstance().getOutputStream();

		if (chatImage.getImgPath() != null
				&& chatF.getText().indexOf(" :image ") >= 0) {
			chatF.setText(replaceStr(chatF.getText(), chatImage.getImageSet(),
					""));
		}
		Packet packet = new Packet();
		packet.setSmpSet(smpSet);
		packet.setColor(color);
		packet.setMessage(chatF.getText().trim());
		if (chatImage.getImgPath() != null) {
			packet.setImgPath(chatImage.getImgPath());
		}
		objectOutputStream.writeObject(packet);
		objectOutputStream.flush();

		String name = "";
		if (chessData.isServer()) {
			name = chessData.getPlayers().get(0).getName();
		} else {
			name = chessData.getPlayers().get(1).getName();
		}
		if (!chatF.getText().trim().equals("")
				|| chatImage.getImgPath() != null) {
			appendStr("\n" + name + ": " + chatF.getText(), smpSet, color);
			if (chatImage.getImgPath() != null) {
				chatA.insertIcon(new ImageIcon(getClass().getResource(
						chatImage.getImgPath())));
			}
		}
		chatF.setText("");
		chatImage.setImgPath(null);
	}

	/**
	 * setButtons method is used to set up the buttons settings. When the chat
	 * starts the buttons are disabled. Once a connection has been made with
	 * another user, the buttons become enabled.
	 * 
	 * @param isOn
	 *            boolean
	 */
	public final void setButtons(boolean isOn) {
		if (isOn == true) {
			chatF.setEditable(true);
			chatF.setEnabled(true);
			btnSend.setEnabled(true);
			btnFont.setEnabled(true);

		} else {
			chatF.setEditable(false);
			chatF.setEnabled(false);
			btnSend.setEnabled(false);
			btnFont.setEnabled(false);
		}
	}

	public String getClientName() {
		String name = "";
		if (!chessData.isServer()) {
			name = chessData.getPlayers().get(0).getName();
		} else {
			name = chessData.getPlayers().get(1).getName();
		}
		return name;
	}

	/**
	 * FontDialog class extends JDialog and is used so the user can change their
	 * font settings. It allows to change the font properties.
	 */
	public class FontDialog extends JDialog {

		private JCheckBox chkItalic, chkBold;
		private JButton btnOk, btnColor;

		private SimpleAttributeSet smpSet1;

		public FontDialog() {

			chkItalic = new JCheckBox("Italic");
			chkBold = new JCheckBox("Bold");

			btnOk = new JButton("Ok");
			btnColor = new JButton("Change Color");

			btnColor.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent actionEvent) {
					color = JColorChooser.showDialog(view, "Choose Color",
							Color.BLUE);
					if (color != null) {
						StyleConstants.setForeground(smpSet, color);
					} else {
						color = Color.BLUE;
					}
				}
			});

			btnOk.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent actionEvent) {
					smpSet1 = new SimpleAttributeSet();
					if (!chkBold.isSelected() && chkItalic.isSelected()) {
						StyleConstants.setItalic(smpSet1, true);
						StyleConstants.setForeground(smpSet1, color);
						Chat.this.smpSet = smpSet1;
					} else if (chkBold.isSelected() && chkItalic.isSelected()) {
						StyleConstants.setItalic(smpSet1, true);
						StyleConstants.setBold(smpSet1, true);
						StyleConstants.setForeground(smpSet1, color);
						Chat.this.smpSet = smpSet1;
					}
					if (chkBold.isSelected() && !chkItalic.isSelected()) {
						StyleConstants.setBold(smpSet1, true);
						StyleConstants.setForeground(smpSet1, color);
						Chat.this.smpSet = smpSet1;
					} else if (!chkBold.isSelected() && !chkItalic.isSelected()) {
						Chat.this.smpSet = smpSet1;
					}
					FontDialog.this.dispose();
				}
			});

			this.add(chkItalic);
			this.add(chkBold);
			this.add(btnColor);
			this.add(btnOk);

			this.setLayout(new FlowLayout());
			this.setTitle("Change Font");
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setSize(200, 100);
			this.setResizable(false);
			this.setLocationRelativeTo(view);
			this.setModal(true);
			this.setVisible(true);
		}
	}
}
