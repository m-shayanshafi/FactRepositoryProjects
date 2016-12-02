/**
 * Client.java
 *
 * @author Si-Mohamed Lamraoui
 * @date 24.05.10
 */

package network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import network.ProtocolTablut;


public class Client implements Runnable
{

	public int id; // identifiant du joueur
	public String user;
	public int lvl;
	public boolean admin; // vrai si le client est un administrateur
	public String side; // camp du joueur (m:moscovite, s:suedois)
	public Party party = null; // partie : null si non cree, party.started = false si non demmarre 
	public Server server;
	public DataBase database;
	private Socket socket;
	public PrintWriter output; // flux de sortie sur la socket
  	private BufferedReader input; // flux d'entree sur la socket
  	private boolean run = true;

	public Client(Socket sock, Server serv, DataBase db)
	{
		socket = sock;
		server = serv;
		user = "pseudo";
    	try {
			output = new PrintWriter(socket.getOutputStream()); 
			input = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
			id = server.addClient(this);
	   		Thread t = new Thread(this);
			t.start();
		} catch (IOException e){ Server.errorMessage("flux"); }
		Server.infoMessage("le client "+id+" s'est connecte.");
		System.out.print("<serveur> ");
	}


	public void run()
	{
		int type;
		String msg = "";
		try {
			while(run==true && (msg = input.readLine())!=null) 
			{
				type = parsePacket(msg);
				System.out.print("<serveur> ");
				if(type==ProtocolTablut.P_FORWARD) {
					if(party!=null && party.started==true) { // forward le message de client a client
						if(id==party.client2.id)
							server.send(party.client1.id, msg);
						else
							server.send(party.client2.id, msg);
					}
				} else if(type==ProtocolTablut.P_DB) {
					// acces a la base de donnee
				}
			}
		} 
		catch (Exception e){ }
		finally // deconnexion du client
		{
			try {
				disconnect();
			} catch (IOException e){ }
		}
	}

	

	/**
	 * Traitement des packets selon le protocole tablut.
	 * (voir ProtocolTablut.java)
	 *		
	 */
	public int parsePacket(String packet)
	{
		Scanner s = new Scanner(packet);	
		String protocol = s.next();
		if(protocol.equalsIgnoreCase(ProtocolTablut.ACTION)) // fait une action en (ligne, colonne)
		{
			String l = s.next();  String c = s.next();
			if(party==null || party.started==false) {
				Server.errorMessage("partie inexistante ou non commencee.");
				server.send(id, "ERROR "+ProtocolTablut.ERROR_PARTY);
				return ProtocolTablut.P_ERROR;
			}
			Server.infoMessage("le joueur "+id+" fait une action en ("+l+","+c+").");
			server.send(id, ProtocolTablut.ACK); 
			return ProtocolTablut.P_FORWARD;
		}
		
		
		else if(protocol.equalsIgnoreCase(ProtocolTablut.JOIN)) // joindre une partie deja existante
		{
			int partyId = s.nextInt();
			party = server.joinParty(partyId, this);
			if(party==null) { 
				Server.errorMessage("la partie "+partyId+" n'existe pas.");
				server.send(id, "ERROR "+ProtocolTablut.ERROR_PARTY);
				return ProtocolTablut.P_ERROR;
			} 
			else if(party.started==true) { 
				Server.errorMessage("la partie a deja commencee.");
				server.send(id, ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_PARTY);
				return ProtocolTablut.P_ERROR;
			}
			Server.infoMessage("le joueur "+id+" rejoin la partie "+partyId);
			party.started = true; // la partie demarre
			Server.infoMessage("la partie "+party.id+" demmarre : client1="+party.client1.id+" client2="+party.client2.id);
			server.send(id, ProtocolTablut.ACK);
			return ProtocolTablut.P_FORWARD; //TODO prevenir l'autre client que la partie commence
		}
		
		
		else if(protocol.equalsIgnoreCase(ProtocolTablut.NEW)) // cree une partie m:moscovite/s:suedois
		{
			String partyType = s.next();  String skillType = s.next();
			String side;
			String skill;
			boolean skillState;
			if(skillType.equalsIgnoreCase("a")) {
				skillState = true; 
				skill = "activees";
			} else if(skillType.equalsIgnoreCase("i")) {
				skillState = false;
				skill = "inactivees";
			} else {
				Server.errorMessage("protocole NEW incorrecte");
				server.send(id, ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_NEW);
				return ProtocolTablut.P_ERROR;
			}
			if(partyType.equalsIgnoreCase("m")) {
				party = server.newParty(this, Party.MOSCOVITE_SIDE, skillState);
				side = "moscovite";
			} 
			else if(partyType.equalsIgnoreCase("s")) {
				party = server.newParty(this, Party.SUEDOIS_SIDE, skillState);
				side = "suedois";
			} else {
				Server.errorMessage("protocole NEW incorrecte");
				server.send(id, ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_NEW); 
				return ProtocolTablut.P_ERROR;
			}
			if(party==null) {
				Server.errorMessage("impossible de cree une partie : client-id <"+id+">");
				server.send(id, ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_NEW);
				return ProtocolTablut.P_ERROR;
			} 
			party.client1.user = user;
			party.client1.lvl = lvl;
			Server.infoMessage("le joueur "+id+" cree une partie "+party.id+" en tant que "+side+" avec les attaques "+skill);
			server.send(id, ProtocolTablut.ACK); 
			return ProtocolTablut.P_SERVER;
		}
		
		
		else if(protocol.equalsIgnoreCase(ProtocolTablut.AUTH)) // connexion avec user et mot de passe md5
		{ 
			String usr = s.next();  String pwd = s.next();
			user = usr.trim();
			
			///////////////// TODO //////////////////
			lvl = 999;
			server.send(id, ProtocolTablut.ACK);
			return ProtocolTablut.P_SERVER;
			////////////////////////////////////////
			
			
			/*admin = database.isAdmin(user);
			int dbid = database.getId(user);
			// verification du mot de passe et du pseudo sur la db
			if(database.getPassword(dbid).equalsIgnoreCase(pwd) && database.getPseudo(dbid).equalsIgnoreCase(user)) {
				Server.infoMessage(user+" s'est authentifie avec le mot de passe "+pwd);
				lvl = database.getLvl(user);
				server.send(id, ProtocolTablut.ACK); // Mot de passe et user correct
				return ProtocolTablut.P_DB;
			} 
			server.send(id, ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_AUTH); // Mot de passe ou user incorrect
			return ProtocolTablut.P_ERROR;*/
		}
		
		
		else if(protocol.equalsIgnoreCase(ProtocolTablut.ABORD)) { // abandon du client
			if(party!=null && party.started==true) { // on previent l'autre client que le joueur s'est deco
				if(id==party.client1.id)
					server.send(party.client2.id, ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_ABORD); // TODO a gere dans le moteur
				else
					server.send(party.client1.id, ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_ABORD);
			}
			if(party!=null) {
				server.deleteParty(party.id); // on supprime la partie de la liste
				Server.infoMessage("Le client "+id+" a abondonne la partie");
				server.send(id, ProtocolTablut.ACK);
				return ProtocolTablut.P_SERVER;
			}
			Server.errorMessage(" impossible d'abandonner une partie qui n'exsite pas");
			return ProtocolTablut.P_ERROR;
		}
		
		
		else if(protocol.equalsIgnoreCase(ProtocolTablut.GETPARTY)) { // envoie de toutes les parties non commencees
			String pack = server.getPartys();
			server.send(id, pack);
			Server.infoMessage("le joueur "+id+" demande la liste des parties");
			return ProtocolTablut.P_SERVER;
		}
		
		
		/*else if(protocol.equalsIgnoreCase(ProtocolTablut.GETSIDE)) {
			if(party!=null) {
				server.send(id, ProtocolTablut.SIDE+" "+side);
				Server.infoMessage("le joueur "+id+" demande son camp : "+side);
				return ProtocolTablut.P_DB;
			} else {
				server.send(id, ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_PARTY);
				Server.errorMessage("demande de camp impossible, pas de partie"); 
				return ProtocolTablut.P_ERROR;
			}
		}*/
		
		
		else if(protocol.equalsIgnoreCase(ProtocolTablut.SETEXP)) { 
			int exp = s.nextInt();
			/*if(setExp(user, exp)==true) { // modifie la db TODO
				server.send(id, ProtocolTablut.ACK);
				Server.infoMessage("le joueur "+id+" a modifier son experience : "+exp);
				return ProtocolTablut.P_DB;
			}*/
			server.send(id, ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_BD);
			Server.errorMessage("le joueur "+id+" n'a pas pu modifier son experience");
			return ProtocolTablut.P_ERROR;
		}
		
		
		else if(protocol.equalsIgnoreCase(ProtocolTablut.SETLVL)) { 
			int lvl = s.nextInt();
			/*if(setLvl(user, lvl)==true) { // modifie la db TODO
				server.send(id, ProtocolTablut.ACK);
				Server.infoMessage("le joueur "+id+" a modifier son lvl : "+lvl);
				return ProtocolTablut.P_DB;
			}*/
			server.send(id, ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_BD);
			Server.errorMessage("le joueur "+id+" n'a pas pu modifier son lvl");
			return ProtocolTablut.P_ERROR;
		}
		
		
		else if(protocol.equalsIgnoreCase(ProtocolTablut.CHAT)) { // envoie d'un message
			String message = s.nextLine();
			// TODO ajouter commande admin et les gerer dans le client
			if(admin==true) { //commandes administrateur
				Scanner s2 = new Scanner(message);
				String commande = s2.next();
				if(commande.equalsIgnoreCase("/ann")) { // envoie en broadcast le message
					String msg = s2.nextLine();
					server.sendAll(ProtocolTablut.CHAT+" <annoncement> "+msg);
					Server.infoMessage("l'admin "+id+" dit : "+msg);
				}
				//else if(commande.equalsIgnoreCase("/lvlup"))
				//else if(commande.equalsIgnoreCase("/setlvl"))
				else {
					Server.infoMessage("le joueur "+id+" dit : "+message);
					return ProtocolTablut.P_FORWARD;
				}
				return ProtocolTablut.P_SERVER;
			}
			Server.infoMessage("le joueur "+id+" dit : "+message);
			return ProtocolTablut.P_FORWARD;
		}
		
		
		else if(protocol.equalsIgnoreCase(ProtocolTablut.GETADMIN)) { // demande si le joueur est admin
			String result;
			if(admin==true)
				result = "t";
			else
				result = "f";
			server.send(id, ProtocolTablut.ADMIN+" "+result);
			Server.infoMessage("le joueur "+id+" demande si il est admin : "+result);
			return ProtocolTablut.P_SERVER;
		}
		
		
		else if(protocol.equalsIgnoreCase(ProtocolTablut.GETEXP)) {
			String pseudo = s.next();
			int exp = 999;
			/*if(!user.equalsIgnoreCase(pseudo) && (exp = database.getExp(user))!=0) { TODO
				Server.infoMessage("le joueur "+pseudo+" demande son exp : "+exp);
				server.send(id, ProtocolTablut.EXP+" "+exp);
				return ProtocolTablut.P_DB;
			}*/
			server.send(id, ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_BD);
			Server.errorMessage("le joueur "+id+" n'a pas pu demander son exp");
			return ProtocolTablut.P_ERROR;
		}
		
		
		else if(protocol.equalsIgnoreCase(ProtocolTablut.GETLVL)) {
			String pseudo = s.next();
			int lvl = 999;
			/*if(!user.equalsIgnoreCase(pseudo) && (lvl = database.getLvl(user))!=0) { TODO
				Server.infoMessage("le joueur "+pseudo+" demande son lvl : "+lvl);
				server.send(id, ProtocolTablut.LVL+" "+lvl);
				return ProtocolTablut.P_DB;
			}*/
			server.send(id, ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_BD);
			Server.errorMessage("le joueur "+id+" n'a pas pu demander son lvl");
			return ProtocolTablut.P_ERROR;
		}
		
		
		
		else if(protocol.equalsIgnoreCase(ProtocolTablut.GETPLAYERPSEUDO)) {
			String pseudo;
			if(party.client1.id==id) 
				pseudo = party.client2.user;
			else
				pseudo = party.client1.user;
			server.send(id, ProtocolTablut.PSEUDO+" "+pseudo);
			Server.infoMessage("le joueur "+id+" demande le pseudo de son adversaire: "+pseudo);
			return ProtocolTablut.P_DB;
		}		
		
			
		/////////////// ADD TODO gestion erreur
		else if(protocol.equalsIgnoreCase(ProtocolTablut.CLOSEPARTY)) { // termine une partie (normalement)
			if(party!=null) {
				Server.infoMessage("La partie "+party.id+" est terminee");
				run = false;
				return ProtocolTablut.P_SERVER;
			}
			Server.infoMessage("impossible de clore une partie inexistante");
			return ProtocolTablut.P_ERROR;
		}		
		
		
		Server.errorMessage("protocole incorrect");
		return 0;
	}

	
	
	/**
	 * Deconnecte le joueur du serveur de jeu
	 * 
	 */
	public void disconnect() throws IOException
	{
		Server.infoMessage("Le client "+id+" s'est deconnecte.");
		if(party!=null && party.started==true) { // on previent l'autre client que le joueur s'est deco
			/* TODO if(id==party.client1.id)
				server.send(party.client2.id, ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_ABORD);
					else
				server.send(party.client1.id, ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_ABORD);*/
		}
		if(party!=null)
			server.deleteParty(party.id); // on supprime la partie de la liste
		server.deleteClient(id); // on supprime le client de la liste
		if(socket!=null)
			socket.close(); // fermeture de la socket
		if(input!=null)
			input.close(); // fermeture du flux d'entree
		if(output!=null)
			output.close(); // fermeture du flux de sortie
		socket = null; input = null; output = null;
	}
	
	
}
