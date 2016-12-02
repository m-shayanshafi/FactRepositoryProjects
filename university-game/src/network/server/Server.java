/**
 * Server.java
 *
 * @author Si-Mohamed Lamraoui
 * @date 25.05.10
 */

package network.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Iterator;


public class Server
{	

	private static int port = 15000; // port d'ecoute/emission du serveur
	public static boolean ERROR_MSG = true; // message d'erreur console active
	public static boolean INFO_MSG = true; // message d'info console active
	
	private Hashtable<Integer, Client> clients = new Hashtable<Integer, Client>();
	private Hashtable<Integer, Party> partys = new Hashtable<Integer, Party>();
	private int clientKey = 0;
	private int partyKey = 0;
	private int nbPartys = 0;
	private int nbClients = 0;


	public static void main(String args[]) 
	{
		System.out.println("=================================================");
		System.out.println("= Bienvenue sur le serveur de jeu Tablut Online =");
		System.out.println("=================================================");
		ServerSocket ss = null;
		DataBase database = new DataBase();
		Server serv = new Server(); 
		new Control(serv); // execute le thread de controle
		try {
			ss = new ServerSocket(port); // ouverture d'un socket serveur sur le port
		} 
		catch(Exception e) { errorMessage("creation de la socket serveur impossible"); }
		try {		
			while (true) // attente en boucle de connexion (bloquant sur ss.accept)
	      	{
				Socket sock = ss.accept();
	        	new Client(sock, serv, database); // si un client se connect, on creer un thread
	      	}
		} 
		catch(Exception e) {errorMessage("creation du client impossible"); }
		
	}



	/**
	 * Creer une nouvelle partie
	 *
	 */
	synchronized public Party newParty(Client client, String type, boolean skill)
	{
		nbPartys++;
		partyKey++;
		Party p = new Party(partyKey, client, type, skill);
		partys.put(new Integer(partyKey), p);
		return p;
	}


	/**
	 * Supprime une partie
	 *
	 */
	synchronized public void deleteParty(int partyId)
	{
		nbPartys--;
		Integer key = new Integer(partyId);
		partys.remove(key);
	}

	
	/**
	 * Rejoindre une partie existante
	 *
	 */
	synchronized public Party joinParty(int partyId, Client client)
	{
		Party p;
		Integer key = new Integer(partyId);
		if((p = partys.get(key))!=null) {
			p.client2 = client;
			return p;
		}
		return null;		
	}

	/**
	 * Retourne un packet contenant les informations de  
	 * toutes les parties non commencees.
	 *
	 * PARTY id user lvl moscovite/suedois skill-active/skill-inactive id ...
	 */
	synchronized public String getPartys()
	{
		Party p;
		String s;
		String packet = "PARTY";
    		Iterator<Party> it = partys.values().iterator();
    		while(it.hasNext()) { // parcours de la table des parties
			p = it.next();
			if(p!=null) {
				if(p.skill) s = "a"; else s = "i";
				packet = packet +" "+ p.id +" "+ p.client1.user +" "+ p.client1.lvl +" "+ p.type +" "+ s;
			}
		}
		return packet;
	}


	/**
	 * Broadcast le packet msg a tous les clients.
	 *
	 */
	synchronized public void sendAll(String msg)
	{
		Client c;
    		Iterator<Client> it = clients.values().iterator();
    		while(it.hasNext()) { // parcours de la table des parties
			c = it.next();
			if(c!=null && c.output!=null) {
				c.output.println(msg);
				c.output.flush(); // envoi dans le flux de sortie
			}
		}
	}

	
	/**
	 * Envoie le packet msg a un client.
	 *
	 */
	synchronized public void send(int clientId, String msg)
	{
		Client c;
		Integer key = new Integer(clientId);
		if((c = clients.get(key))!=null) {
			if(c.output!=null) {
				c.output.println(msg);
				c.output.flush(); // envoi dans le flux de sortie
			}
		}
	}


	synchronized public void deleteClient(int id)
	{
		nbClients--;
		Integer key = new Integer(id);
		clients.remove(key);
	}


	synchronized public int addClient(Client c)
	{
		nbClients++;
		clientKey++; // TODO fix id inc
		clients.put(new Integer(clientKey), c);
		return clientKey;
	}


	synchronized public int getNbClients()
	{
		return nbClients;
	}


	synchronized public int getNbPartys()
	{
		return nbPartys;
	}
	
	
	
	public static void errorMessage(String msg) {
		if(ERROR_MSG==true)
			System.out.println("\n[erreur] "+msg);
	}
	
	public static void infoMessage(String msg) {
		if(INFO_MSG==true)
			System.out.println("\n[info] "+msg);
	}


}
