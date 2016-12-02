package thaigo.network.server;
import java.io.IOException;
import java.net.Socket;

import com.lloseng.ocsf.server.AbstractConnectionFactory;
import com.lloseng.ocsf.server.AbstractServer;
import com.lloseng.ocsf.server.ConnectionToClient;

/**
 * Create these connections when required.
 * 
 * @author TG_Dream_Team
 * @version 9/5/2013
 *
 */
public class ClientConnectionFactory extends AbstractConnectionFactory {
	
	/**
	 * Constructor for creation this class.
	 */
	public ClientConnectionFactory() {
		super();
	}

	/**
	 * @see com.lloseng.ocsf.server.AbstractConnectionFactory#createConnection(java.lang.ThreadGroup, java.net.Socket, com.lloseng.ocsf.server.AbstractServer)
	 */
	@Override
	protected ConnectionToClient createConnection(ThreadGroup group,
			Socket clientSocket, AbstractServer server) throws IOException {
		return new ClientHandler(group, clientSocket, server);
	}

}
