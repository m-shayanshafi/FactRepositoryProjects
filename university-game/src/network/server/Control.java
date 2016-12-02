/**
 * Control.java
 *
 * @author Si-Mohamed Lamraoui
 * @date 20.05.10
 */

package network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import network.ProtocolTablut;


public class Control implements Runnable
{

	private Server server;
	private BufferedReader input;

	public Control(Server serv)
	{
		server = serv;
    		input = new BufferedReader(new InputStreamReader(System.in)); // flux d'entree de la console
    		Thread t = new Thread(this);
    		t.start();
	}


	public void run()
	{
		try {
		String cmd = "";
		boolean done = true;
		while(done)
		{	
			System.out.print("<serveur> ");
			cmd = input.readLine();
			if (cmd.equalsIgnoreCase("quit")) { // commande quitter
				server.sendAll("ERROR "+ProtocolTablut.ERROR_CON); // deconnecte tous les clients
				System.exit(0);
			}
			else if(cmd.equalsIgnoreCase("total")) { // commande affiche nb client et nb partie en cour
				System.out.println("Nombre de connectes : "+server.getNbClients());
				System.out.println("Nombre de partie : "+server.getNbPartys());
			}
			else {
				System.out.println("commandes :");
				System.out.println("\tQuitter : \"quit\"");
				System.out.println("\tNombre de connectes : \"total\"");
				System.out.println("");
			}
			System.out.flush(); // on affiche tout ce qui est en attente dans le flux
		}
		}catch (IOException e) {}
	}


}


