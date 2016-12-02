package thaigo.network.server;
import java.io.IOException;
import java.net.Socket;

import thaigo.state.UpdateTask;
import thaigo.utility.ChatMessage;
import thaigo.utility.PlayerInfoMessage;
import thaigo.utility.PositionMessage;
import thaigo.utility.PropertyManager;
import thaigo.utility.RemoveMessage;
import thaigo.view.GameUI;
import thaigo.view.SetupUI;

import com.lloseng.ocsf.server.AbstractServer;
import com.lloseng.ocsf.server.ConnectionToClient;

/**
 * Handle events of server side player.
 * 
 * @author TG_Dream_Team
 * @version 9/5/2013
 *
 */
public class ClientHandler extends ConnectionToClient {

	/** <code>Server</code> object. */
	private Server server;
	
	/** Attribute of user interfaces of this program to be communicated with. */
	private SetupUI setupUI;
	private GameUI gameUI;

	/** To make this class use singleton pattern. */
	private static ClientHandler clientHandler;

	/**
	 * Constructor used by <code>ClientConnectionFactory</code> class.
	 * @param group <code>ThreadGroup</code>
	 * @param clientSocket <code>Socket</code>
	 * @param server <code>AbstractServer</code>
	 * @throws IOException
	 */
	protected ClientHandler(ThreadGroup group, Socket clientSocket, AbstractServer server) throws IOException {
		super(group, clientSocket, server);
		super.setInfo("login", "unknown");

		this.setupUI = ((Server) server).getSetupUI();
		this.server = (Server)server;

		this.server.setConnectionToClient(this);
		super.sendToClient(new PlayerInfoMessage());
		this.clientHandler = this;
	}

	/**
	 * Get <code>ClientHandler</code> object instead of creating every time. 
	 * @return <code>ClientHandler</code> object
	 */
	public static ClientHandler getInstance() {
		return clientHandler;
	}
	
	/**
	 * Set <code>GameUI</code> to this class.
	 * @param gameUI <code>GameUI</code> object to be set to this class
	 */
	public void setGameUI(GameUI gameUI) {
		this.gameUI = gameUI;
	}
	
	/**
	 * Return <code>GameUI</code> handled in this class.
	 * @return <code>GameUI</code> handled in this class
	 */
	public GameUI getGameUI() {
		return gameUI;
	}

	/**
	 * Handle message from client.
	 * @param msg the object sent from client
	 */
	protected boolean handleMessageFromClient(Object msg) {
		if (msg instanceof PlayerInfoMessage) {
			PlayerInfoMessage foe = (PlayerInfoMessage)msg;
			PropertyManager.setProperty("foe", foe.getFoeName());
			
			gameUI.startConnection();
			this.gameUI.getRuler().setClientHandler(this);
			this.gameUI.getChatBoard().setClientHandler(this);
			this.gameUI.getGameBoard().initPawn();
			UpdateTask.getInstance(gameUI.getRuler()).setToMainPhase();
			UpdateTask.setGameUI(gameUI);
			gameUI.highlightYourPanel();
			setupUI.dispose();
		}
		else if (msg instanceof PositionMessage) {
			gameUI.movingSound(GameUI.PLAY);
			PositionMessage posMessage = (PositionMessage)msg;
			
			gameUI.getRuler().setFoeWalkingPosition(posMessage.getInvertedOldPos(), posMessage.getInvertedNewPos());
			UpdateTask.getInstance(gameUI.getRuler()).setToMainPhase();
			
			gameUI.highlightYourPanel();
			gameUI.addLog(posMessage.invertedToString(), GameUI.CLIENT);
		}
		else if (msg instanceof RemoveMessage) {
			gameUI.eatingSound(GameUI.PLAY);
			RemoveMessage removeCommand = (RemoveMessage)msg;
			gameUI.getRuler().removedAtPosition(removeCommand.getInversedRemovedPosition());
			gameUI.addLog(removeCommand.inversedToString(), GameUI.CLIENT);
		}
		else if(msg instanceof ChatMessage){
			ChatMessage message = (ChatMessage)msg;
			gameUI.addChatMessage(message.toString(), message.getColor());
		}
		
		return true;
	}
	
	/**
	 * Send position of pawn which is changed to client side.
	 */
	public void sendPawnPositionsToFoe() {
		gameUI.movingSound(GameUI.PLAY);
		try {

			PositionMessage x = new PositionMessage(gameUI.getRuler().getPawnPos(), gameUI.getRuler().getGopenelPos());
			super.sendToClient(x);
			gameUI.highlightFoePanel();
			gameUI.addLog(x.toString(), GameUI.HOST);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * Send position of pawn which is removed to client side.
	 * @param removeMessage <code>RemoveMessage</code> object
	 */
	public void sendRemoveCommandToFoe(RemoveMessage removeMessage) {
		gameUI.eatingSound(GameUI.PLAY);
		try {
			super.sendToClient(removeMessage);
			gameUI.addLog(removeMessage.toString(), GameUI.HOST);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Send chat message of player to server side.
	 * @param chatMessage <code>ChatMessage</code> object
	 */
	public void sendChatMessage(ChatMessage chatMessage) {
		try {
			super.sendToClient(chatMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
