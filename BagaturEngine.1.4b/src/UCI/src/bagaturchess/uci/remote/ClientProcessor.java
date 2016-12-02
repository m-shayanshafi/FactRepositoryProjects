package bagaturchess.uci.remote;

import java.net.Socket;
import java.io.*;

import bagaturchess.uci.api.ChannelManager;
import bagaturchess.uci.api.IChannel;
import bagaturchess.uci.impl.Channel_Remote;
import bagaturchess.uci.run.Boot;

/**
 *
 */
public class ClientProcessor implements Runnable {
	
	
	private static final int STRING_BUFFER_LENGTH = 255;
	
	
	private Socket mClientSocket;
	private BufferedReader mReader;
	private BufferedWriter mOutput;
	private StringBuffer mBuffer;
	
	private String mCanonicalHostName;
	
	
	public ClientProcessor(Socket aClientSocket, String aCanonicalHostName) {

		mClientSocket = aClientSocket;
		mCanonicalHostName = aCanonicalHostName;

		try {

			IChannel communicationChanel = new Channel_Remote(mClientSocket.getInputStream(), mClientSocket.getOutputStream());
			ChannelManager.setChannel(communicationChanel);

			mReader = communicationChanel.getIn();
			
			mOutput = communicationChanel.getOut();
			
			mBuffer = new StringBuffer(STRING_BUFFER_LENGTH);
			
		} catch (IOException ioe) {
			close(ioe, "Error while initializing client!");
		}
	}
	
	
	@Override
	public void run() {
		String[] args = new String[] {
				"bagaturchess.engines.base.cfg.UCIConfig_BaseImpl",
				"bagaturchess.search.impl.uci_adaptor.UCISearchAdaptorImpl_PonderingOpponentMove",
				"bagaturchess.engines.base.cfg.UCISearchAdaptorConfig_BaseImpl",
				"bagaturchess.search.impl.rootsearch.parallel.MTDParallelSearch",
				"bagaturchess.engines.base.cfg.RootSearchConfig_BaseImpl_SMP",
				"bagaturchess.search.impl.alg.impl0.SearchMTD0",
				"bagaturchess.engines.bagatur.cfg.search.SearchConfigImpl_MTD_SMP",
				"bagaturchess.engines.bagatur.cfg.board.BoardConfigImpl",
				"bagaturchess.engines.bagatur.cfg.eval.BagaturEvalConfigImpl_v2", };

		Boot.runStateManager(args, ChannelManager.getChannel());
	}
	
	
	private void close(Exception aException, String aMessage) {
		if (aException != null) {
			aException.printStackTrace();
		}
		System.out.println(aMessage);
		close();
	}
	
	
	public void close() {
		if (mReader != null) {
			try {
				mReader.close();
			} catch (IOException e) {
			}
		}
		if (mOutput != null) {
			try {
				mOutput.close();
			} catch (IOException e) {
			}
		}
		if (mClientSocket != null) {
			try {
				mClientSocket.close();
			} catch (IOException e) {
			}
		}
		mReader = null;
		mOutput = null;
		mClientSocket = null;
	}
}
