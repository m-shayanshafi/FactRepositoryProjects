package main;

import connectivity.*;

public interface DCFrontEnd {

	public void sendMessage(DCMessage message);
	public void registerConnection(DCLocalConnection connection);
	public void connectionFailed(int reason, String address);
	public void backendConnectionBroken(int reason);

}
