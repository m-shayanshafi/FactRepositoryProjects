package thaigo.network.client;
import java.awt.Color;
import java.io.IOException;

import thaigo.property.AbstractRuler;
import thaigo.property.Position;
import thaigo.state.UpdateTask;
import thaigo.utility.ChatMessage;
import thaigo.utility.PlayerInfoMessage;
import thaigo.utility.PositionMessage;
import thaigo.utility.PropertyManager;
import thaigo.utility.RemoveMessage;
import thaigo.view.GameUI;
import thaigo.view.SetupUI;

import com.lloseng.ocsf.client.AbstractClient;

/**
 * Handle events of client side player.
 * 
 * @author TG_Dream_Team
 * @version 9/5/2013
 *
 */
public class Client extends AbstractClient {

	/** Attribute of user interfaces of this program to be communicated with. */
	private SetupUI setupUI;
	private GameUI gameUI;

	/** Allow to create and show <code>GameUI</code> only one time. */
	private boolean isfirst = true;

	/** To make this class use singleton pattern. */
	private static Client instance;

	/**
	 * Private constructor only used by <code>getInstance(String host, int port, SetupUI ui, GameUI gameUI)</code>.
	 * @param host host location
	 * @param port port used to communicate to host
	 * @param setupUI <code>SetupUI</code>
	 * @param gameUI <code>GameUI</code>
	 */
	private Client(String host, int port, SetupUI setupUI, GameUI gameUI) {
		super(host, port);
		this.gameUI = gameUI;
		this.setupUI = setupUI;
	}

	/**
	 * Get <code>Client</code> object instead of creating every time. 
	 * @param host host location
	 * @param port port used to communicate to host
	 * @param setupUI <code>SetupUI</code>
	 * @param gameUI <code>GameUI</code>
	 * @return <code>Client</code> object
	 */
	public static Client getInstance(String host, int port, SetupUI setupUI, GameUI gameUI) {
		if (instance == null)
			instance = new Client(host, port, setupUI, gameUI);
		return instance;
	}

	/**
	 * Get <code>Client</code> object without parameters.
	 * Note that this method can be used after you have used
	 * <code>getInstance(String host, int port, SetupUI setupUI, GameUI gameUI)</code>
	 * at least one time before.
	 * @return <code>Client</code> object
	 */
	public static Client getInstance() {
		return instance;
	}
	
	/**
	 * Return <code>GameUI</code> handled in this class.
	 * @return <code>GameUI</code> handled in this class
	 */
	public GameUI getGameUI() {
		return gameUI;
	}

	/**
	 * Handle message from server.
	 * @param msg the object sent from server
	 */
	@Override
	protected void handleMessageFromServer(Object msg) {

		if (msg instanceof PlayerInfoMessage && isfirst) {
			isfirst = false;
			PlayerInfoMessage foe = (PlayerInfoMessage)msg;
			PropertyManager.setProperty("foe", foe.getFoeName());
			
			gameUI.startConnection();
			this.gameUI.getRuler().setClient(this);
			this.gameUI.getChatBoard().setClient(this);
			this.gameUI.getGameBoard().initPawn();
			UpdateTask.getInstance(gameUI.getRuler()).setToWaitingPhase();
			UpdateTask.setGameUI(gameUI);
			gameUI.highlightFoePanel();
			setupUI.close();

			gameUI.getRuler().setClient(this);
			
			try {
				PlayerInfoMessage info = new PlayerInfoMessage();
				sendToServer(info);
			} catch (IOException e) {
			}
		}
		else if (msg instanceof PositionMessage) {
			gameUI.movingSound(GameUI.PLAY);
			PositionMessage posMessage = (PositionMessage)msg;

			gameUI.getRuler().setClient(Client.getInstance());
			gameUI.getRuler().setFoeWalkingPosition(posMessage.getInvertedOldPos(), posMessage.getInvertedNewPos());
			UpdateTask.getInstance(gameUI.getRuler()).setToMainPhase();
			
			gameUI.highlightYourPanel();
			gameUI.addLog(posMessage.invertedToString(), GameUI.HOST);
		}
		else if (msg instanceof RemoveMessage) {
			gameUI.eatingSound(GameUI.PLAY);
			RemoveMessage removeCommand = (RemoveMessage)msg;
			gameUI.getRuler().removedAtPosition(removeCommand.getInversedRemovedPosition());
			gameUI.addLog(removeCommand.inversedToString(), GameUI.HOST);
		}
		else if (msg instanceof ChatMessage) {
			ChatMessage message = (ChatMessage)msg;
			gameUI.addChatMessage(message.toString() , message.getColor());
		}
	}

	/**
	 * Send position of pawn which is changed to server side.
	 */
	public void sendPawnPositionsToFoe() {
		gameUI.movingSound(GameUI.PLAY);
		try {
			PositionMessage x = new PositionMessage(gameUI.getRuler().getPawnPos(), gameUI.getRuler().getGopenelPos());
			sendToServer(x);
			gameUI.highlightFoePanel();
			gameUI.addLog(x.toString(), GameUI.CLIENT);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Send position of pawn which is removed to server side.
	 * @param removeMessage <code>RemoveMessage</code> object
	 */
	public void sendRemoveCommandToFoe(RemoveMessage removeMessage) {
		gameUI.eatingSound(GameUI.PLAY);
		try {
			sendToServer(removeMessage);
			gameUI.addLog(removeMessage.toString(), GameUI.CLIENT);
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
			sendToServer(chatMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}