package moteur;

import java.awt.Point;
import gui.AireDessin;
import joueur.InterfaceJoueur;

public interface InterfaceMoteur extends Runnable{
	public void run();
	
	public void annuler();
	public void refaire();
	public void setPoint(Point p);
	public void setAireDessin(AireDessin d);
	public InterfaceJoueur getTour();
	public Plateau getPlateau();
}
