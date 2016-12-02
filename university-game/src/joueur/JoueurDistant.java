/**
 * JoueurDistant.java
 * 
 * Menu principale de l'application.
 * 
 * @author Thierry Maurigault, Si-Mohamed Lamraoui
 * @date 28.05.10
 */


package joueur;

import gui.InterfaceMenu;

import java.awt.Point;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.Timer;
import network.ProtocolTablut;
import network.client.*;
import moteur.Plateau;


public class JoueurDistant extends Joueur
{
	
	private static int 		port		= 15000; 		// port du serveur de jeu
	private static String 	host		= "127.0.0.1"; 	// adresse ip du serveur de jeu
	public static boolean 	ERROR_MSG 	= true; 		// message d'erreur console active
	public static boolean 	INFO_MSG 	= true; 		// message d'info console active
	
	private boolean admin = true;
	private Send send; // objet naicessaire a l'emission de packet au serveur de jeu
	private Receive receive; // objet naicessaire a la reception de packet au serveur de jeu
	private Socket sock;
	public boolean connectionProblem = false; // si vrai, alors il y a un probleme avec la connexion du serveur
	private InterfaceMenu mainwindow;
	private Timer time;
	
	
	
	// constructeur solo
	public JoueurDistant(String _pseudo, InterfaceMenu m)
	{
		pseudo = _pseudo;
		mainwindow = m;
		estJoueur = true;
		pionCourant = new Point(-1,-1);
		sock = null;
		try {
			sock = new Socket(host, port);
			send = new Send(this, sock);
			receive = new Receive(this, sock);
		} 
		catch(IOException e) { 
			JOptionPane.showMessageDialog(mainwindow ,"Serveur en maintenance. Reesayer plus tard.", "Erreur", JOptionPane.ERROR_MESSAGE);
			errorMessage("socket"); 
		}
		time = new Timer(ProtocolTablut.WAIT, new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				connectionProblem = true;
				JOptionPane.showMessageDialog(mainwindow ,"Vous avez ete deconnecte du serveur.", "Erreur", JOptionPane.ERROR_MESSAGE);
				 errorMessage("temps ecoule, probleme avec le serveur");
			} });
	}

	
	public Deplacement coupAJouer(Plateau P){
		return new Deplacement(new Point(0,0), new Point(0,0));
	}

	
	/**
	 * Demande le pseudo du joueur distant
	 * 
	 * @return un pseudo
	 */
	public String getPlayerPseudo() {
		int result = send.send(ProtocolTablut.GETPLAYERPSEUDO);
		if(result==ProtocolTablut.OK) {
			waitServer();
			String pseudo = receive.getBuffer();
			Scanner s = new Scanner(pseudo);
			if(s.next().equalsIgnoreCase(ProtocolTablut.PSEUDO))
				return s.next();
		}
		gestionErreur(ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_CON); // deconnexion
		errorMessage("getPlayerPseudo > deconnexion : "+result);
		return "";
	}
	
	
	/**
	 * Envoie un message (chat) au joueur distant
	 * (pas d'accuser de reception (ACK) pour CHAT) 
	 * 
	 * @param msg
	 * @return vrai si erreur
	 */
	public boolean sendMessage(String msg) {
		int result = send.send(ProtocolTablut.CHAT + " " + msg);
		if(result==ProtocolTablut.OK) {
			return true;
		} 
		gestionErreur(ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_CON); // deconnexion
		errorMessage("sendMessage > deconnexion : "+result);
		return false;
	}
	
	
	public boolean newPartie(boolean _camp, String attaques) {
		String camp;
		if (_camp==Joueur.MOSCOVITE)
			camp = "m";
		else
			camp = "s";
		int result = send.send(ProtocolTablut.NEW + " " + camp + " " + attaques);
		if(result==ProtocolTablut.OK) {
			waitServer();
			String packet = receive.getBuffer();
			Scanner s = new Scanner(packet);
			if(s.next().equalsIgnoreCase(ProtocolTablut.ACK))
				return true;
			else {
				gestionErreur(packet);
				infoMessage("newParty erreur");
				return false;
			}
		} 
		gestionErreur(ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_CON); // deconnexion
		errorMessage("newParty > deconnexion : "+result);
		return false;
	}
	
	
	public boolean joinPartie(int id){
		int result = send.send(ProtocolTablut.JOIN+" "+id);
		if(result==ProtocolTablut.OK) {
			waitServer();
			String packet = receive.getBuffer();
			Scanner s = new Scanner(packet);
			if(s.next().equalsIgnoreCase(ProtocolTablut.ACK))
				return true;
			else {
				gestionErreur(packet);
				infoMessage("abordParty erreur");
				return false;
			}
		}
		gestionErreur(ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_CON); // deconnexion
		errorMessage("joinParty > deconnexion : "+result);
		return false;
	}
	
	
	public boolean abandonPartie(){
		int result = send.send(ProtocolTablut.ABORD);
		if(result==ProtocolTablut.OK) {
			waitServer();
			String packet = receive.getBuffer();
			Scanner s = new Scanner(packet);
			if(s.next().equalsIgnoreCase(ProtocolTablut.ACK))
				return true;
			else {
				gestionErreur(packet);
				infoMessage("abordParty erreur");
				return false;
			}
		}
		gestionErreur(ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_CON); // deconnexion
		errorMessage("abordParty > deconnexion : "+result);
		return false;
	}
	
	
	public Vector<Party> getParty() { 
		Vector<Party> v = new Vector<Party>();
		String buffer = new String("");
		int result = send.send(ProtocolTablut.GETPARTY );
		if(result==ProtocolTablut.OK) {
			System.out.println("attente party");
			waitServer();
			buffer = receive.getBuffer();
			Scanner s = new Scanner(buffer);
			if(s.next().equalsIgnoreCase(ProtocolTablut.PARTY)){
				while(s.hasNext()){
					int id = Integer.valueOf(s.next());
					String user = s.next();
					int lvl = Integer.valueOf(s.next());
					String type = s.next().trim();
					String attaques = s.next().trim();
					Party p = new Party(id, user, lvl, type, attaques);
					v.add(p);
				}
				return v;
			}
		}
		errorMessage("getparty : "+result);
		return null;
	}


	public int DB_getExperience(String pseudo) {
		int result = send.send(ProtocolTablut.GETEXP + " " + pseudo);
		if(result==ProtocolTablut.OK) {
			waitServer();
			String exp = receive.getBuffer();
			Scanner s = new Scanner(exp);
			if(s.next().equalsIgnoreCase(ProtocolTablut.EXP))
				return Integer.valueOf(s.next());
		}
		gestionErreur(ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_CON); // deconnexion
		errorMessage("getexp > deconnexion : "+result);
		return -1;
	}

	
	public int DB_getNiveau(String pseudo) {
		int result = send.send(ProtocolTablut.GETLVL + " " + pseudo);
		if(result==ProtocolTablut.OK) {
			waitServer();
			String lvl = receive.getBuffer();
			Scanner s = new Scanner(lvl);
			if(s.next().equalsIgnoreCase("LVL"))
				return Integer.valueOf(s.next());
		}
		gestionErreur(ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_CON); // deconnexion
		errorMessage("getlvl > deconnexion : "+result);
		return -1;
	}


	public String DB_getPseudo() {
		int result = send.send(ProtocolTablut.GETPLAYERPSEUDO);
		if(result==ProtocolTablut.OK) {
			waitServer();
			String pseudo = receive.getBuffer();
			Scanner s = new Scanner(pseudo);
			if(s.next().equalsIgnoreCase(ProtocolTablut.PSEUDO))
				return s.next();
		}
		gestionErreur(ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_CON); // deconnexion
		errorMessage("getpseudo > deconnexion : "+result);
		return null;
	}


	public int DB_setExperience(int exp) {
		int result = send.send(ProtocolTablut.SETEXP+" "+exp);
		if(result==ProtocolTablut.OK) {
			waitServer();
			String packet = receive.getBuffer();
			if(packet.equalsIgnoreCase(ProtocolTablut.ACK)) {
				infoMessage("set exp ok");
				return result;
			} else {
				gestionErreur(packet);
				infoMessage("set exp erreur");
				return 0;
			}
		}  else {
			gestionErreur(ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_CON); // deconnexion
			errorMessage("setexp > deconnexion : "+result);
			return 0;
		}
	}


	public int DB_setNiveau(int lvl) {
		int result = send.send(ProtocolTablut.SETLVL+" "+lvl);
		if(result==ProtocolTablut.OK) {
				waitServer();
				String packet = receive.getBuffer();
				if(packet.equalsIgnoreCase(ProtocolTablut.ACK)) {
					infoMessage("set level ok");
					return result;
				} else {
					gestionErreur(packet);
					infoMessage("set level erreur");
					return 0;
				}
			}  else {
				gestionErreur(ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_CON); // deconnexion
				errorMessage("setlvl > deconnexion : "+result);
				return 0;
			}
	}
	

	public boolean DB_estAdmin(int i) {
		int result = send.send(ProtocolTablut.GETADMIN);
		if(result==ProtocolTablut.OK) {
			waitServer();
			String packet = receive.getBuffer();
			Scanner s = new Scanner(packet);
			if(s.next().equalsIgnoreCase(ProtocolTablut.ADMIN)) {
				if(s.next().equalsIgnoreCase("t")) {
					admin = true;
					return true;
				} else {
					admin = false;
					return false;
				}
			} else {
				gestionErreur(packet);
				errorMessage("getadmin : "+packet);
			}
		}
		gestionErreur(ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_CON); // deconnexion
		errorMessage("getadmin > deconnexion : "+result);
		return false;
	}
	
	
	/**
	 * Authentifie le joueur sur le serveur de jeu
	 * 
	 * @throws IOException 
	 */
	public boolean connect(String user, String pwd)
	{
		pwd = Md5.encode(pwd);
		int result = send.send(ProtocolTablut.AUTH +" "+ user +" "+ pwd);
		if(result==ProtocolTablut.OK) {
			System.out.println("attente connect");
			waitServer();
			String packet = getReceive().getBuffer();
			if(packet.equalsIgnoreCase(ProtocolTablut.ACK)) {
				infoMessage("authentification du joueur "+pseudo);
				return true;
			} else {
				gestionErreur(packet);
				infoMessage("mot de passe ou identifiant incorrectes");
				return false;
			}
		}  else {
			gestionErreur(ProtocolTablut.ERROR+" "+ProtocolTablut.ERROR_CON); // deconnexion
			errorMessage("connect > deconnexion : "+result);
		}
		return false;
	}
	
	
	/**
	 * Deconnecte le joueur du serveur de jeu
	 * 
	 */
	public void disconnect()
	{
		errorMessage("deconnexion");
		try {
			if(send.getOutput()!=null)
				send.getOutput().close(); // fermeture du flux d'emission
			if(sock!=null)
				sock.close(); // fermeture de la socket d'emission
			receive.kill(); // arret du thread et fermeture du flux/socket de reception
		} 
		catch (IOException e){ errorMessage("impossible de fermer les flux ou les sockets."); }
	}

		
	public void waitServer()
	{
		//time.start(); TODO a modif pour l'attente d'un joueur
		System.out.print("Waiting...");
		while(getReceive().bufferState()==false && connectionProblem==false) {
			//System.out.print(".");
			}
		System.out.println("");
		if(connectionProblem==true)
			System.out.print("waitServer : erreur de connexion");
		//time.stop();
	}
	
	
	public int sendAction(Point p)
	{
		return getSend().send(ProtocolTablut.ACTION + " " +p.x + " " + p.y);
	}
	
	
	public Receive getReceive() {
		return receive;
	}
	
	
	public Send getSend() {
		return send;
	}
	
	
	public boolean estJoueur(){
		return estJoueur;
	}
	
	
	public Boolean getCamp(){
		return camp;
	}
	
	
	public Point getPionCourant(){
		return pionCourant;
	}
	
	
	public void setPionCourant(int x, int y){
		pionCourant.x = x;
		pionCourant.y = y;
	}
	
	
	public void passageNiveau(){
		if (getNiveau() != 30){
			if (Math.pow(getNiveau(), 3) > getExperience()){
				setExperience(getExperience()-(int)Math.pow(getNiveau(), 3));
				setNiveau(getNiveau()+1);
			}
		}
	}
	
	
	/**
	 * Affiche une fenetre selon l'erreur passee en parametre 
	 * 
	 * @param packet
	 */
	public void gestionErreur(String packet)
	{
		Scanner s = new Scanner(packet);
		if(s.next().equalsIgnoreCase(ProtocolTablut.ERROR))
		{
			String message = "Erreur inconnue!";
			int error = s.nextInt();
			if(error==ProtocolTablut.ERROR_ABORD)
					message = "Votre ennemie a abandonne la partie. Vous avez gagne par forfait!";
			if(error==ProtocolTablut.ERROR_BD)
					message = "Le serveur est en maintenance, veuillez vous re-connecter plus tard.";
			if(error==ProtocolTablut.ERROR_CON)
					message = "Vous avez ete deconnecte du serveur!";
			if(error==ProtocolTablut.ERROR_NEW)
					message = "Impossible de creer une partie. Le serveur est propablement surcharge.\nReessayer plus tard.";
			if(error==ProtocolTablut.ERROR_PARTY)
					message = "Cette partie n'existe pas.";
			if(error==ProtocolTablut.ERROR_AUTH)
				message = "Mot de passe ou identifieant incorrecte.";
			JOptionPane.showMessageDialog(mainwindow ,message, "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}
		errorMessage("le gestionnaire d'erreur a ete apelle pour un packet ne contenant pas d'erreur");
	}
	
	
	public static void errorMessage(String msg) {
		if(ERROR_MSG==true)
			System.out.println("[erreur] "+msg);
	}
	
	
	public static void infoMessage(String msg) {
		if(INFO_MSG==true)
			System.out.println("[info] "+msg);
	}
	

}


