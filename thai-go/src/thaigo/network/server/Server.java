package thaigo.network.server;
import java.io.IOException;

import thaigo.utility.PlayerInfoMessage;
import thaigo.utility.PropertyManager;
import thaigo.view.GameUI;
import thaigo.view.SetupUI;

import com.lloseng.ocsf.server.AbstractServer;
import com.lloseng.ocsf.server.ConnectionToClient;
public class Server extends AbstractServer {
	/** Attribute of user interfaces of this program to be communicated with. */
	private SetupUI setupUI;
	private GameUI gameUI;
	
	private static ConnectionToClient conToClient;
	
	/** To make this class use singleton pattern. */
	private static Server instance;

	/**
	 * Private constructor only used by <code>getInstance(int port, SetupUI ui, GameUI gameUI)</code>.
	 * @param port port used to communicate to client
	 * @param setupUI <code>SetupUI</code>
	 * @param gameUI <code>GameUI</code>
	 */
	private Server(int port, SetupUI ui, GameUI gameUI) {
		super(port);
		this.gameUI = gameUI;
		this.setupUI = ui;
	}
	
	/**
	 * Get <code>Server</code> object instead of creating every time. 
	 * @param port port used to communicate to client
	 * @param setupUI <code>SetupUI</code>
	 * @param gameUI <code>GameUI</code>
	 * @return <code>Server</code> object
	 */
	public static Server getInstance(int port, SetupUI ui, GameUI gameUI) {
		if (instance == null)
			return new Server(port, ui, gameUI);
		return instance;
	}
	
	/**
	 * Handle message from client.
	 * @param msg the object sent from client
	 * @param client client side
	 */	
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		setupUI.addServerStatus(String.format(" %-8.8s > %s", "Client", msg));
	}
	public void talk(String text) throws IOException {
		super.sendToAllClients(text);
	}

	/**
	 * Perform when server is started.
	 */
	protected void serverStarted() {
		setupUI.addServerStatus("Server listening on port " + super.getPort());
	}

	/**
	 * Perform when server is stopped.
	 */
	protected void serverStopped() {
		setupUI.addServerStatus("Server has stopped listening.");
	}

	/**
	 * Perform when there is client connected.
	 */
	@Override
	protected void clientConnected(ConnectionToClient client) {
		// change status to "Connected"
		setupUI.addServerStatus(String.format("New connection from %s", client.getInetAddress().getHostAddress()));
		setupUI.addServerStatus(String.format("Network : Connected to client %s", client.getInetAddress().getHostAddress()));

		PlayerInfoMessage info = new PlayerInfoMessage();
		super.sendToAllClients(info);
	}

	/**
	 * Perform when there is client disconnected.
	 */
	@Override
	protected synchronized void clientDisconnected(ConnectionToClient client) {
		String user = (String)client.getInfo("user");
		setupUI.addServerStatus( String.format("Disconnect %s @ %s", user, client.getInetAddress().getHostAddress()));
	}

	/**
	 * Set <code>ConnectionToClient</code> to this class.
	 * @param client <code>ConnectionToClient</code>
	 */
	public void setConnectionToClient(ConnectionToClient client) {
		this.conToClient = client;
		((ClientHandler)client).setGameUI(gameUI);
	}

	/**
	 * Return <code>SetupUI</code>.
	 * @return <code>SetupUI</code>
	 */
	public SetupUI getSetupUI() {
		return setupUI;
	}
}