package thaigo.utility;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Utility class for checking is the port free or not.
 * 
 * @author Nol Pasurapunya 5510546018
 * @version 2013.04.19
 *
 */
public class PortChecker { 

	/**
	 * Check whether is the port free or not.
	 * @param port port will be checked
	 * @return true if that port is free, otherwise false
	 */
	public static boolean checkAvailable(int port){
		try {  
			ServerSocket serverSok = new ServerSocket(port);
			serverSok.close(); 
			return true;
		} catch (Exception e) {  
			return false;
		}
	}

	/**
	 * Check whether is the port free or not with another host name.
	 * @param port port will be checked
	 * @param ip another host name
	 * @return true if that port is free, otherwise false
	 */
	public static boolean checkAvailable(int port, String ip){
		try {  
			Socket serverSok = new Socket(ip,port);
			serverSok.close(); 
			return true;
		} catch (Exception e) {  
			return false;
		}
	}
}
