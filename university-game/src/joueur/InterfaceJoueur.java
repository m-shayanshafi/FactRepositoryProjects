package joueur;

import java.awt.Point;
import java.util.Scanner;

import network.ProtocolTablut;
import network.client.Receive;
import network.client.Send;

import moteur.Plateau;


public interface InterfaceJoueur {
	
	public boolean estJoueur();
	
	public int getNiveau();
	
	public Boolean getCamp();
	
	public void setCamp(boolean b);
	
	public int getExperience();
	
	public Point getPionCourant();
	
	public String getPseudo();
	
	public void setExperience(int exp);

	public void setNiveau(int n);

	public void setPseudo(String s);
	
	public void setPionCourant(int x, int y);
	
	public void passageNiveau();
	
	public Receive getReceive();
	
	//public int send(String msg); // TODO  deplace dans Send
	public Send getSend();

	public Deplacement coupAJouer(Plateau P);

	public int DB_getExperience(String pseudo);

	public int DB_setExperience(int exp);

	public int DB_getNiveau(String pseudo);

	public int DB_setNiveau(int lvl);
	
	public String DB_getPseudo();

	public boolean DB_estAdmin(int i);

	public int sendAction(Point p);


}
