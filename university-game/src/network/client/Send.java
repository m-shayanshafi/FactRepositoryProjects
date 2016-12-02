/**
 * Send.java
 *
 *
 * @author Si-Mohamed Lamraoui
 * @date 28.05.10
 */

package network.client;

import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import javax.swing.Timer;
import joueur.JoueurDistant;
import network.ProtocolTablut;


public class Send
{

	public JoueurDistant player;
	private Socket sock;
	private PrintWriter output;
	private BufferedReader console;
	private boolean wait;
	private Timer timer;
	
	public Send(JoueurDistant p, Socket s)
	{
		player = p;
		sock = s;
		try {	
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())),true);
			console = new BufferedReader(new InputStreamReader(System.in)); // flux d'entree de la console
		} 
		catch (Exception e) { System.out.println("[erreur] connexion au serveur impossible."); }
		timer = new Timer(ProtocolTablut.WAIT, new AdaptatorTimer());
		wait = true;
	}

	

	/**
	 *
	 * Envoie le packet msg a au serveur de jeu.
	 *
	 */
	public int send(String msg)
	{
		try {
			if(output!=null) {
				output.println(msg);
				output.flush(); // envoi dans le flux de sortie
			}
		} 
		catch (Exception e) { 
			System.out.println("[erreur] terminaison du thread d'emission."); 
			return ProtocolTablut.ERROR_CON;
		}
		return ProtocolTablut.OK;
	}

	
	/**
	 * Implement l'action qui sera execute tout les x ms par le timer et
	 * d'arreter l'attente d'un packet.
	 */
	class AdaptatorTimer implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	    	wait = false;
	    }
	}
	
	
	
	public PrintWriter getOutput() {
		return output;
	}



}
