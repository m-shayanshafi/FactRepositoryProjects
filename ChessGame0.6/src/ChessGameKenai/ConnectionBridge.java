package ChessGameKenai;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.text.SimpleAttributeSet;

/**
 * The ConnectionBridge class is the connection between two clients if the game
 * is played online using sockets. If the game is played online this class has
 * responsibility to read data from the socket and to establish a connection
 * between clients in order for them to work as one. This class is an observer
 * so each time any change happens to our data class Chess_Data this class is
 * notified by executing its update method data sends a list of two value
 * through notifyObservers method we extract them and send to the client that is
 * connected when the client is connected when the client receives a list he
 * calls the move method with two arguments of the list that is sent so changes
 * happen that way
 * 
 * @author Dimitri Pankov
 * @see Observer
 * @version 1.5
 */
public class ConnectionBridge implements Observer {
	private ServerSocket serverSocket;
	private Socket socket;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectIputStream;
	private boolean isServer = true;
	private InetAddress ipAddress;
	private ChessBoardView chessBoardView;
	private ReadData readData;
	private Chess_Data chessData;
	private Color color = Color.ORANGE;
	private SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
	private Chat chat;

	/**
	 * The overloaded constructor of the class creates server or client as well
	 * as creates both stream ObjectInputStream and ObjectOutputStream and
	 * starts the inner class thread
	 * 
	 * @param data
	 *            as Chess_Data
	 * @param view
	 *            as ChessBoardView
	 * @param isServer
	 *            as a boolean
	 * @param ipAddress
	 *            as an InetAddress
	 * @param chat
	 *            as a Chat
	 */
	public ConnectionBridge(Chess_Data data, ChessBoardView view,
			boolean isServer, InetAddress ipAddress, Chat chat) {
		this.chessBoardView = view;
		this.isServer = isServer;
		this.ipAddress = ipAddress;
		this.chessData = data;
		this.chat = chat;
		try {
			if (isServer) {
				chat.appendStr("\nSERVER ON LINE WAITING FOR CONNECTION\n",
						simpleAttributeSet, color);
				serverSocket = new ServerSocket(8888);
				socket = serverSocket.accept();
				chat.appendStr("CONNECTION IS ESTABLISHED\n",
						simpleAttributeSet, color);
				chat.appendStr("GAME HAS BEEN STARTED!\n", simpleAttributeSet,
						color);
				chat.setButtons(true);
				view.startTimer();
				data.isServer(true);
			} else {
				socket = new Socket(ipAddress, 8888);
				chat.appendStr("\nCONNECTED TO SERVER\n", simpleAttributeSet,
						color);
				chat.appendStr("GAME HAS BEEN STARTED!\n", simpleAttributeSet,
						color);
				chat.setButtons(true);
				view.startTimer();
				data.isServer(false);
				view.flipClientBoard();
			}
			objectOutputStream = new ObjectOutputStream(
					socket.getOutputStream());
			objectOutputStream.flush();
			readData = new ReadData();
			readData.start();
		} catch (ConnectException connectException) {
			chat.appendStr("\nSERVER NOT STARTED\n", simpleAttributeSet, color);
			view.reEnableMenuItems(true);
			data.isGameOnLine(false);
		} catch (IOException ioException) {
			JOptionPane.showMessageDialog(null, ioException.toString());
		}
	}

	/**
	 * The update method of the class is inherited from the Observer Interface
	 * Each time any change happens to the data class Chess_Data this method is
	 * executed in this case the update method receives Object that we use to
	 * extract the data from The Chess_Data each time the move method is
	 * executed when the piece is moved it sends the ArrayList of two elements
	 * first element is original position of the piece and the second is the
	 * destination. Update method sends the list to the other client who upon
	 * receiving the list calls its move method with the arguments in the list
	 * 
	 * @param observable
	 *            as an Observable object
	 * @param object
	 *            as an Object we send a list using this argument
	 */
	public void update(Observable observable, Object object) {
		if (object != null) {
			try {
				objectOutputStream.writeObject((ArrayList) object);
				objectOutputStream.flush();
			} catch (IOException exception) {
				chat.appendStr(exception.getMessage(), simpleAttributeSet,
						color);
			}
		}
	}

	/**
	 * The ReadData is a Thread object which reads data from the socket then
	 * passes the read object to the analyzeAndExecute when the start method is
	 * called on this object the run method is executed in our case it is inside
	 * the loop so the run method will be executed until the boolean value is
	 * changed to false which will force the run method to terminate thus
	 * killing the thread
	 */
	public class ReadData extends Thread {

		private volatile boolean isAlive = true;

		public ReadData() throws IOException {
			objectIputStream = new ObjectInputStream(socket.getInputStream());
		}

		/**
		 * The run method of the class reads data from the socket it is inside
		 * the loop so reads data until the run method terminates
		 */
		@Override
		public void run() {
			try {
				while (isAlive) {
					Object object = objectIputStream.readObject();
					this.analyzeAndExecute(object);
				}
			} catch (ClassNotFoundException classNotFoundException) {
				chat.appendStr("\n" + chat.getClientName()
						+ " Has left the game", simpleAttributeSet, color);
				chat.setButtons(false);
				chessData.isGameOnLine(false);
				chessBoardView.reEnableMenuItems(true);
				try {
					socket.close();
				} catch (IOException ioException) {
					chat.appendStr(ioException.toString(), simpleAttributeSet,
							color);
				}
			} catch (IOException ioException) {
				chat.appendStr(ioException.toString(), simpleAttributeSet,
						color);
			}
		}

		/**
		 * The analyzeAndExecute method receives an object that the ReadData
		 * class has just read from the socket this method analyzes it and calls
		 * appropriate methods to update the view like change icon, display
		 * message, restart game or list of two integers that the move method
		 * will be called with
		 * 
		 * @param object
		 *            as an Object
		 */
		public void analyzeAndExecute(Object object) {
			if (object instanceof Packet) {
				Packet packet = (Packet) object;
				if (packet.getMessage() != null) {
					chat.appendStr(
							"\n" + chat.getClientName() + ": "
									+ packet.getMessage(), packet.getSmpSet(),
							packet.getColor());
					chat.getTxtPane().setCaretPosition(
							chat.getTxtPane().getDocument().getLength());
				}
				if (packet.getImgPath() != null) {
					chat.getTxtPane().insertIcon(
							new ImageIcon(getClass().getResource(
									((Packet) object).getImgPath())));
					chat.getTxtPane().setCaretPosition(
							chat.getTxtPane().getDocument().getLength());
				}
				if (packet.getPlayerIconPath() != null) {
					ConnectionBridge.this.setPlayerIconPath(object);
				}
				if (packet.getGuestName() != null) {
					ConnectionBridge.this.setGuestName(object);
				}
				if (packet.getRestartGame() != null) {
					ConnectionBridge.this.restartGame();
				}
				if (packet.getConfirmRestart() != null) {
					chessBoardView.restartClientGame();
				}
			} else if (object instanceof ArrayList) {
				ArrayList list = (ArrayList) object;
				chessData.move((Integer) list.get(0), (Integer) list.get(1));
			}
		}
	}

	public ObjectOutputStream getOutputStream() {
		return objectOutputStream;
	}

	public void restartGame() {
		int returnValue = JOptionPane.showConfirmDialog(chessBoardView,
				chat.getClientName() + "would you like to restart the game",
				"Confirmation Message", JOptionPane.YES_NO_OPTION);
		if (returnValue == JOptionPane.YES_OPTION) {
			Packet packet = new Packet();
			packet.setConfirmRestart("restart game");
			try {
				this.objectOutputStream.writeObject(packet);
				this.objectOutputStream.flush();
			} catch (IOException ex) {
				chat.appendStr(ex.toString(), simpleAttributeSet, color);
			}
			chessBoardView.restartClientGame();
		}
	}

	/**
	 * The method killThread simply sets the boolean value isAlive to false this
	 * forces the run method to exit thus killing the thread
	 */
	public void killThread() {
		readData.isAlive = false;
	}

	/**
	 * The method setPlayericonPath simply sets the iconPath of the player image
	 * this method is used to update the view of the client when a client
	 * changes his image the path to the image is send to the other client that
	 * is connected so he would also see that image changed
	 * 
	 * @param object
	 *            as an Object
	 */
	public void setPlayerIconPath(Object object) {
		Packet packet = (Packet) object;
		if (chessData.isServer()) {
			chessData.getPlayers().get(1)
					.setImagePath(packet.getPlayerIconPath());
		} else {
			chessData.getPlayers().get(0)
					.setImagePath(packet.getPlayerIconPath());
		}
		chessData.notifyView();
	}

	public void setGuestName(Object object) {
		Packet packet = (Packet) object;
		if (chessData.isServer()) {
			chessData.getPlayers().get(1).setName(packet.getGuestName());
		} else {
			chessData.getPlayers().get(0).setName(packet.getGuestName());
		}
		chessData.notifyView();
	}
}
