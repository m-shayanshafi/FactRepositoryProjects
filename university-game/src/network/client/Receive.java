/**
 * Receive.java
 *
 *
 * @author Si-Mohamed Lamraoui
 * @date 26.05.10
 */

package network.client;

import gui.Chat;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import joueur.JoueurDistant;

import network.ProtocolTablut;


public class Receive implements Runnable
{

	private Socket sock;
	private BufferedReader input; // flux d'entree
	private String buffer = ""; // tampon de reception
	private String actionBuffer = "";
	private JoueurDistant player;
	private boolean actionState = false; // vrai si on a recu un packet ACTION
	private boolean run = true;
	private boolean bufferReady = false;
	private Chat chat;


	public Receive(JoueurDistant p, Socket s)
	{
		player = p;
		sock = s;
		try {
			input = new BufferedReader( new InputStreamReader(sock.getInputStream()) );
			Thread t = new Thread(this);
			t.start();
		} catch (Exception e) {  
			System.out.println("erreur de connexion"); 
		}
	}

	public void run()
	{
		Scanner s;
		String msg = "";
		try {
			while((msg = input.readLine()) != null && run==true) 
			{
				s = new Scanner(msg);
				String protocol = s.next();
				if(protocol.equalsIgnoreCase(ProtocolTablut.ACTION)) {
					setActionState(true);
				} else {
					setActionState(false);
				}
				if(protocol.equalsIgnoreCase(ProtocolTablut.CHAT)) {
					if(chat!=null)
						chat.addMessage(chat.pseudoDistant, s.nextLine());
				} else {
					setBuffer(msg);
				}
				System.out.println("Message recu: "+msg);
				System.out.print("<client> ");
				System.out.flush(); // on affiche tout ce qui est en attente dans le flux
			}
		} 
		catch(IOException e) { System.out.println("[erreur] <socket:recieve>"); }
		finally // deconnexion
		{
			player.disconnect();
		}

	}


	synchronized public String getBuffer() {
		System.out.println("get");
		System.out.flush();
		bufferReady = false;
		return buffer;
	} 

	synchronized public boolean bufferState()
	{
		return bufferReady;
	}

	synchronized public void setBuffer(String newBuffer) {
		
		System.out.println("set");
		System.out.flush();
		buffer = newBuffer;
		bufferReady = true;
	} 
	
	synchronized public void setActionBuffer(String newBuffer) {
		actionBuffer = newBuffer;
	} 
	
	synchronized public String getActionBuffer() {
		return actionBuffer;
	} 

	synchronized public boolean isActionState() {
		return actionState;
	} 


	synchronized public void setActionState(boolean newState) {
		actionState = newState;
	} 

	
	synchronized public void kill() {
		run = false;
	}
	
	public BufferedReader getInput() {
		return input;
	}
	
	public void setChat(Chat c) {
		chat = c;
	}
	
}
