/*
 * 
 * RESEAU : dans la fonction run() seulement
 * 
 * 
 * 
 */

package moteur;

import gui.AireDessin;
import gui.InterfaceJeu;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

import joueur.Deplacement;
import joueur.IAMoscovite;
import joueur.IASuedois;
import joueur.InterfaceJoueur;
import joueur.Joueur;
import joueur.JoueurLocal;
import pieces.Piece;
import pieces.PieceMoscovite;
import pieces.PieceRoi;
import pieces.PieceSuedois;
import pieces.PieceVide;
import cases.Case;


public class MoteurMonojoueur implements InterfaceMoteur {
	public static int PLVSIA = 1; // solo
	public static int PLVSPL = 2; // online

	private Plateau plateauJeu;
	private String difficulte;
	private InterfaceJoueur joueurHumain, joueurIA;
	private boolean tour; 
	private Point caseClic;
	private boolean clicAbandonne;
	private LinkedList<String> historique;
	private ListIterator<String> it;

	private AireDessin interfaceDessin;
	/*private InterfaceJeu interfaceJeu;*/
	private boolean roiBouffe;

	// constructeur solo
	public MoteurMonojoueur(String m, AireDessin d,/* InterfaceJeu w, */Boolean campJoueurHumain)
	{
		Random r = new Random();
		int i = r.nextInt(2);
		/*if (i == 0)
			tour = true;
		else
			tour = false;*/
		tour = true;
		plateauJeu = new Plateau();
		interfaceDessin = d;
		difficulte = m;
		//interfaceJeu = w;
		joueurHumain = new JoueurLocal(campJoueurHumain, "Joueur 1");
		if (campJoueurHumain == Joueur.SUEDOIS)
			joueurIA = new IAMoscovite(m);
		else
			joueurIA = new IASuedois(m);
		clicAbandonne = false;
		historique = new LinkedList<String>();
		it = historique.listIterator();
		caseClic = new Point();
		roiBouffe = false;
	}

	public boolean partieFinie()
	{
		if ((plateauJeu.getPlateauCases(0,0).type() == Case.ANGLE && plateauJeu.getPlateauPieces(0,0).type() == Piece.ROI)
				|| (plateauJeu.getPlateauCases(plateauJeu.getLine()-1,0).type() == Case.ANGLE && plateauJeu.getPlateauPieces(plateauJeu.getLine()-1,0).type() == Piece.ROI)
				|| (plateauJeu.getPlateauCases(plateauJeu.getLine()-1,plateauJeu.getColumn()-1).type() == Case.ANGLE && plateauJeu.getPlateauPieces(plateauJeu.getLine()-1,plateauJeu.getColumn()-1).type() == Piece.ROI)
				|| (plateauJeu.getPlateauCases(0,plateauJeu.getColumn()-1).type() == Case.ANGLE && plateauJeu.getPlateauPieces(0,plateauJeu.getColumn()-1).type() == Piece.ROI))
			return true ;
		for(int i=0;i<plateauJeu.getLine();i++)
			for(int j=0;j<plateauJeu.getColumn();j++)
				if (plateauJeu.getPlateauPieces(i,j).type() == Piece.MOSCOVITE)
					return false;
		return true;
	}

	public void jouerCoup(Deplacement d, InterfaceJoueur joueurCourant)
	{
		coupHistorique(plateauJeu.getPlateauPieces(d.getPointDepart().x, d.getPointDepart().y).type(), d);


		int typeCaseDepart = plateauJeu.getPlateauPieces(d.getPointDepart().x, d.getPointDepart().y).type();

		plateauJeu.setPlateauPieces(d.getPointDepart().x, d.getPointDepart().y, new PieceVide());
		if (typeCaseDepart == Piece.ROI){
			plateauJeu.setPlateauPieces(d.getPointArrivee().x,d.getPointArrivee().y, new PieceRoi());
		}
		else if (typeCaseDepart == Piece.MOSCOVITE){
			plateauJeu.setPlateauPieces(d.getPointArrivee().x, d.getPointArrivee().y, new PieceMoscovite());
		}
		else if (typeCaseDepart == Piece.SUEDOIS){
			plateauJeu.setPlateauPieces(d.getPointArrivee().x, d.getPointArrivee().y, new PieceSuedois());
		}

		// Envoyer coordonnï¿½e du point de depart et d'arrivï¿½ pour l'animation
		// Mettre une temporisation pour attendre le temps de l'animation
		prise(d.getPointArrivee(), joueurCourant);
	}

	// renvoie vrai si le pion sous la souris est bien au joueur actif
	public Boolean estPionJoueur(Point p, InterfaceJoueur joueurCourant){
		if (joueurCourant.getCamp() == Joueur.SUEDOIS){
			if (plateauJeu.getPlateauPieces(p.x, p.y).type() == Piece.ROI ||
					plateauJeu.getPlateauPieces(p.x, p.y).type() == Piece.SUEDOIS)
				return true;
		}
		else{
			if (plateauJeu.getPlateauPieces(p.x, p.y).type() == Piece.MOSCOVITE)
				return true;
		}

		return false;
	}

	// renvoie vrai si le dï¿½placement est bien vertical ou horizontal, et qu'il n'y a aucun pion qui gï¿½ne
	public Boolean deplacementCroix(Point p, InterfaceJoueur joueurCourant){

		// si le dï¿½placement est bien vertical ou horizontal
		if (joueurCourant.getPionCourant().x == p.x || joueurCourant.getPionCourant().y == p.y){
			// si c'est un dï¿½placement vers le haut
			if (joueurCourant.getPionCourant().x - p.x > 0){
				for(int i=joueurCourant.getPionCourant().x-1;i>=p.x;i--)
					if (plateauJeu.getPlateauPieces(i,joueurCourant.getPionCourant().y).type() != Piece.VIDE)
						return false;
			}
			// si c'est un dï¿½placement vers le bas
			else if (joueurCourant.getPionCourant().x - p.x < 0){
				for(int j=joueurCourant.getPionCourant().x+1;j<=p.x;j++)
					if (plateauJeu.getPlateauPieces(j,joueurCourant.getPionCourant().y).type() != Piece.VIDE)
						return false;
			}
			// si c'est un dï¿½placement vers la droite
			else if (joueurCourant.getPionCourant().y - p.y < 0){
				for(int k=joueurCourant.getPionCourant().y+1;k<=p.y;k++)
					if (plateauJeu.getPlateauPieces(joueurCourant.getPionCourant().x, k).type() != Piece.VIDE)
						return false;
			}
			// si c'est un dï¿½placement vers la gauche
			else if (joueurCourant.getPionCourant().y - p.y > 0){
				for(int l=joueurCourant.getPionCourant().y-1;l>=p.y;l--)
					if (plateauJeu.getPlateauPieces(joueurCourant.getPionCourant().x, l).type() != Piece.VIDE)
						return false;
			}
			// si le point d'arrivï¿½e est le konakis et que la piï¿½ce est un soldat, le dï¿½placement n'est pas valide
			if (plateauJeu.getPlateauCases(p.x, p.y).type() == Case.KONAKIS && plateauJeu.getPlateauPieces(joueurCourant.getPionCourant().x, joueurCourant.getPionCourant().y).type() != Piece.ROI)
				return false;
			// si le point d'arrivï¿½e est un angle et que la piï¿½ce est un soldat, invalide
			else if (plateauJeu.getPlateauCases(p.x, p.y).type() == Case.ANGLE && plateauJeu.getPlateauPieces(joueurCourant.getPionCourant().x, joueurCourant.getPionCourant().y).type() != Piece.ROI)
				return false;
			else
				return true;
		}
		return false;
	}

	// coup valide est appelï¿½ uniquement si estPionJoueur renvoie vrai
	public Boolean coupValide(Point p, InterfaceJoueur joueurCourant){
		if (deplacementCroix(p, joueurCourant)){
			//	si le pion est un roi, le dï¿½placement est bon puisqu'il a le droit d'aller dans les angles
			// et sur le konakis
			if (plateauJeu.getPlateauPieces(joueurCourant.getPionCourant().x, joueurCourant.getPionCourant().y).type() == Piece.ROI)
				return true;
			//	si le pion est un soldat, il ne faut pas que la case d'arrivï¿½e soit un angle ou un konakis
			else{
				if (plateauJeu.getPlateauCases(p.x, p.y).type() == Case.ANGLE 
						|| plateauJeu.getPlateauCases(p.x, p.y).type() == Case.KONAKIS)
					return false;
				else
					return true;
			}
		}
		return false;
	}

	public void nouvelleSelection(Point p, InterfaceJoueur j){
		j.getPionCourant().x = p.x;
		j.getPionCourant().y = p.y;
	}

	public void deselectionner(InterfaceJoueur j){
		j.getPionCourant().x = -1;
		j.getPionCourant().y = -1;
	}

	public Plateau getPlateau() {
		return plateauJeu;
	}

	public Plateau clonePlateau(){
		Plateau p = new Plateau();
		for(int i=0;i<p.getLine();i++)
			for(int j=0;j<p.getLine();j++){
				p.setPlateauCases(i, j, plateauJeu.getPlateauCases(i, j));
				p.setPlateauPieces(i, j, plateauJeu.getPlateauPieces(i, j));
			}
		return p;
	}

	public InterfaceJoueur getTour() {
		if(tour==true)
			return joueurHumain;
		else
			return joueurIA;
	}

	// prend en paramï¿½tre la case oï¿½ a cliquï¿½ le joueur
	synchronized public void setPoint(Point p){
		caseClic.x = p.x;
		caseClic.y = p.y;
		notifyAll();
		////////////////////////////////////////////////////////////////////////////////////////////
		// faire le notifyAll quand l'animation sera finie et non pas quand on clique //////////// 
		///////////////////////////////////////////////////////////////////////////////////////////
	}

	synchronized public void run() 
	{
		save();
		InterfaceJoueur joueurCourant;
		while(!partieFinie() && !clicAbandonne && !roiBouffe) {
			if (tour)
				joueurCourant = joueurHumain;
			else
				joueurCourant = joueurIA;
			boolean aJoue = false;
			// si c'est un humain
			if (joueurCourant.estJoueur()){
				while(aJoue == false){
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (estPionJoueur(caseClic, joueurCourant))
						nouvelleSelection(caseClic, joueurCourant);
					else{
						if (joueurCourant.getPionCourant().x != -1 && joueurCourant.getPionCourant().y != -1){
							if (coupValide(caseClic, joueurCourant)){
								Deplacement d = new Deplacement(joueurCourant.getPionCourant(), caseClic);
								jouerCoup(d, joueurCourant);
								deselectionner(joueurCourant);
								aJoue = true;
								joueurCourant.setPionCourant(-1, -1);
							}
						}
						else{
							//	message .. ou pas, sur l'interface graphique
						}
					}	
				}
			}
			// si  c'est une IA
			else{
				Deplacement d = joueurCourant.coupAJouer(plateauJeu);
				//jouerCoup(joueurCourant.coupAJouer(plateauJeu), joueurCourant);
				jouerCoup(d, joueurCourant);
				System.out.println("coup joué par l'ia => départ (" + d.getPointDepart().x + ", " + d.getPointDepart().y + ")   arrivée (" + d.getPointArrivee().x + ", " + d.getPointArrivee().y);
			}
			tour = !tour;
			interfaceDessin.repaint();
		}
		if(tour) {
			//interfaceJeu.setStatusBar(joueurLocal.getPseudo() + " a gagnï¿½ !");
		} else {
			//interfaceJeu.setStatusBar("L'ordinateur a gagnï¿½ !");
		}
	}

	// appelï¿½e en cas de clic sur le bouton abandonner
	public void abandonner(){
		if(tour) {
			//interfaceJeu.setStatusBar(joueurLocal.getPseudo() + " a gagnï¿½ !");
		} else {
			//interfaceJeu.setStatusBar("L'ordinateur a gagnï¿½ !");
		}
		clicAbandonne = true;
	}

	public void prise(Point p, InterfaceJoueur joueurCourant){
		// haut = true => le soldat qui vient de bouger n'est pas sur le bord haut, il faut donc tester si il prend un roi en haut
		boolean haut, bas, gauche, droite;
		haut = bas = gauche = droite = false;
		boolean hautRoi, basRoi, gaucheRoi, droiteRoi;
		hautRoi = basRoi = gaucheRoi = droiteRoi = false;
		int nbPrises = 0;
		String stringPrises = new String("");
		if (p.x >= 1)
			haut = true;
		if (p.x < plateauJeu.getLine()-1)
			bas = true;
		if (p.y >= 1)
			gauche = true;
		if (p.y < plateauJeu.getColumn()-1)
			droite = true;


		// cas de prise de soldat, si on vient de jouer un moscovite
		if (joueurCourant.getCamp() == Joueur.MOSCOVITE){
			if (haut){
				// si il prend un soldat
				if ((p.x!=1 && plateauJeu.getPlateauPieces(p.x-1, p.y).type() == Piece.SUEDOIS
						&& plateauJeu.getPlateauPieces(p.x-2, p.y).type() == Piece.MOSCOVITE)||(p.x==1 && plateauJeu.getPlateauPieces(p.x-1, p.y).type() == Piece.SUEDOIS)){
					plateauJeu.setPlateauPieces(p.x-1, p.y, new PieceVide());
					stringPrises = priseHistorique(Piece.SUEDOIS, new Point(p.x-1, p.y), stringPrises);
					nbPrises++;;
				}
				// si il a la possibilitï¿½ de prendre un roi
				else if (plateauJeu.getPlateauPieces(p.x-1, p.y).type() == Piece.ROI
						&& plateauJeu.getPlateauCases(p.x-1, p.y).type() != Case.KONAKIS){
					// hautRoi = true => le roi est sur le bord haut, on ne teste pas la case au dessus du roi
					// donc si hautRoi = true, il ne testera pas le reste de la ligne dans le "ou", donc pas d'exception de sortie de tableau
					hautRoi = basRoi = gaucheRoi = droiteRoi = false;
					if (p.x-1 == 0)
						hautRoi = true;
					else if (plateauJeu.getPlateauCases(p.x - 2, p.y).type() == Case.ANGLE)
						hautRoi = true;
					if (p.y == 0)
						gaucheRoi = true;
					else if (plateauJeu.getPlateauCases(p.x - 1, p.y - 1).type() == Case.ANGLE)
						gaucheRoi = true;
					if (p.y == plateauJeu.getColumn()-1)
						droiteRoi = true;
					else if (plateauJeu.getPlateauCases(p.x - 1, p.y + 1).type() == Case.ANGLE)
						droiteRoi = true;
					if ((hautRoi || plateauJeu.getPlateauPieces(p.x-2, p.y).type() == Piece.MOSCOVITE || plateauJeu.getPlateauCases(p.x-2, p.y).type() == Case.KONAKIS)
							&& (gaucheRoi || plateauJeu.getPlateauPieces(p.x-1, p.y-1).type() == Piece.MOSCOVITE || plateauJeu.getPlateauCases(p.x-1, p.y-1).type() == Case.KONAKIS)
							&& (droiteRoi || plateauJeu.getPlateauPieces(p.x-1, p.y+1).type() == Piece.MOSCOVITE || plateauJeu.getPlateauCases(p.x-1, p.y+1).type() == Case.KONAKIS)){
						plateauJeu.setPlateauPieces(p.x-1, p.y, new PieceVide());
						stringPrises = priseHistorique(Piece.ROI, new Point(p.x-1, p.y), stringPrises);
						roiBouffe = true;
						nbPrises++;;
					}
				}
			}
			if (bas){
				if ((p.x!=plateauJeu.getColumn()-2 && plateauJeu.getPlateauPieces(p.x+1, p.y).type() == Piece.SUEDOIS
						&& plateauJeu.getPlateauPieces(p.x+2, p.y).type() == Piece.MOSCOVITE) || (p.x==plateauJeu.getColumn()-2 && plateauJeu.getPlateauPieces(p.x+1, p.y).type() == Piece.SUEDOIS)){
					plateauJeu.setPlateauPieces(p.x+1, p.y, new PieceVide());
					stringPrises = priseHistorique(Piece.SUEDOIS, new Point(p.x+1, p.y), stringPrises);
					nbPrises++;;
				}
				else if (plateauJeu.getPlateauPieces(p.x+1, p.y).type() == Piece.ROI
						&& plateauJeu.getPlateauCases(p.x+1, p.y).type() != Case.KONAKIS){
					hautRoi = basRoi = gaucheRoi = droiteRoi = false;
					if (p.x+1 == plateauJeu.getLine()-1)
						basRoi = true;
					else if(plateauJeu.getPlateauCases(p.x + 2, p.y).type() == Case.ANGLE)
						basRoi = true;
					if (p.y == 0)
						gaucheRoi = true;
					else if (plateauJeu.getPlateauCases(p.x + 1, p.y - 1).type() == Case.ANGLE)
						gaucheRoi = true;
					if (p.y == plateauJeu.getColumn()-1)
						droiteRoi = true;
					else if (plateauJeu.getPlateauCases(p.x + 1, p.y + 1).type() == Case.ANGLE)
						droiteRoi = true;
					if ((basRoi || plateauJeu.getPlateauPieces(p.x+2, p.y).type() == Piece.MOSCOVITE || plateauJeu.getPlateauCases(p.x+2, p.y).type() == Case.KONAKIS)
							&& (gaucheRoi || plateauJeu.getPlateauPieces(p.x+1, p.y-1).type() == Piece.MOSCOVITE || plateauJeu.getPlateauCases(p.x+1, p.y-1).type() == Case.KONAKIS)
							&& (droiteRoi || plateauJeu.getPlateauPieces(p.x+1, p.y+1).type() == Piece.MOSCOVITE || plateauJeu.getPlateauCases(p.x+1, p.y+1).type() == Case.KONAKIS)){
						plateauJeu.setPlateauPieces(p.x+1, p.y, new PieceVide());
						stringPrises = priseHistorique(Piece.ROI, new Point(p.x+1, p.y), stringPrises);
						roiBouffe = true;
						nbPrises++;;
					}
				}
			}
			if (gauche){
				if ((p.y!=1 && plateauJeu.getPlateauPieces(p.x, p.y-1).type() == Piece.SUEDOIS
						&& plateauJeu.getPlateauPieces(p.x, p.y-2).type() == Piece.MOSCOVITE) ||( p.y==1 && plateauJeu.getPlateauPieces(p.x, p.y-1).type() == Piece.SUEDOIS)){
					plateauJeu.setPlateauPieces(p.x, p.y-1, new PieceVide());
					stringPrises = priseHistorique(Piece.SUEDOIS, new Point(p.x, p.y-1), stringPrises);
					nbPrises++;;
				}
				else if (plateauJeu.getPlateauPieces(p.x, p.y-1).type() == Piece.ROI
						&& plateauJeu.getPlateauCases(p.x, p.y-1).type() != Case.KONAKIS){
					hautRoi = basRoi = gaucheRoi = droiteRoi = false;
					if (p.x == plateauJeu.getLine()-1)
						basRoi = true;
					else if (plateauJeu.getPlateauCases(p.x + 1, p.y - 1).type() == Case.ANGLE)
						basRoi = true;
					if (p.y-1 == 0)
						gaucheRoi = true;
					else if (plateauJeu.getPlateauCases(p.x, p.y - 2).type() == Case.ANGLE)
						gaucheRoi = true;
					if (p.x == 0)
						hautRoi = true;
					else if (plateauJeu.getPlateauCases(p.x - 1, p.y - 1).type() == Case.ANGLE)
						hautRoi = true;
					if ((basRoi || plateauJeu.getPlateauPieces(p.x+1, p.y-1).type() == Piece.MOSCOVITE || plateauJeu.getPlateauCases(p.x+1, p.y-1).type() == Case.KONAKIS)
							&& (gaucheRoi || plateauJeu.getPlateauPieces(p.x, p.y-2).type() == Piece.MOSCOVITE || plateauJeu.getPlateauCases(p.x, p.y-2).type() == Case.KONAKIS)
							&& (hautRoi || plateauJeu.getPlateauPieces(p.x-1, p.y-1).type() == Piece.MOSCOVITE || plateauJeu.getPlateauCases(p.x-1, p.y-1).type() == Case.KONAKIS)){
						plateauJeu.setPlateauPieces(p.x, p.y-1, new PieceVide());
						stringPrises = priseHistorique(Piece.ROI, new Point(p.x, p.y-1), stringPrises);
						roiBouffe = true;
						nbPrises++;;
					}
				}
			}
			if (droite){
				if ((p.y!=plateauJeu.getLine()-2 && plateauJeu.getPlateauPieces(p.x, p.y+1).type() == Piece.SUEDOIS
						&& plateauJeu.getPlateauPieces(p.x, p.y+2).type() == Piece.MOSCOVITE) || (p.y==plateauJeu.getLine()-2 && plateauJeu.getPlateauPieces(p.x, p.y+1).type() == Piece.SUEDOIS)){
					plateauJeu.setPlateauPieces(p.x, p.y+1, new PieceVide());
					stringPrises = priseHistorique(Piece.SUEDOIS, new Point(p.x, p.y+1), stringPrises);
					nbPrises++;;
				}
				else if (plateauJeu.getPlateauPieces(p.x, p.y+1).type() == Piece.ROI
						&& plateauJeu.getPlateauCases(p.x, p.y+1).type() != Case.KONAKIS){
					if (p.x == plateauJeu.getLine()-1)
						basRoi = true;
					else if (plateauJeu.getPlateauCases(p.x + 1, p.y + 1).type() == Case.ANGLE)
						basRoi = true;
					if (p.y+1 == plateauJeu.getColumn()-1)
						droiteRoi = true;
					else if (plateauJeu.getPlateauCases(p.x, p.y + 2).type() == Case.ANGLE)
						droiteRoi = true;
					if (p.x == 0)
						hautRoi = true;
					else if (plateauJeu.getPlateauCases(p.x - 1, p.y + 1).type() == Case.ANGLE)
						hautRoi = true;
					if ((basRoi || plateauJeu.getPlateauPieces(p.x+1, p.y+1).type() == Piece.MOSCOVITE || plateauJeu.getPlateauCases(p.x+1, p.y+1).type() == Case.KONAKIS)
							&& (droiteRoi || plateauJeu.getPlateauPieces(p.x, p.y+2).type() == Piece.MOSCOVITE || plateauJeu.getPlateauCases(p.x, p.y+2).type() == Case.KONAKIS)
							&& (hautRoi || plateauJeu.getPlateauPieces(p.x-1, p.y+1).type() == Piece.MOSCOVITE || plateauJeu.getPlateauCases(p.x-1, p.y+1).type() == Case.KONAKIS)){
						plateauJeu.setPlateauPieces(p.x, p.y+1, new PieceVide());
						stringPrises = priseHistorique(Piece.ROI, new Point(p.x, p.y+1), stringPrises);
						roiBouffe = true;
						nbPrises++;;
					}
				}
			}
		}
		// cas de prise de soldat, si on vient de jouer suedois
		if (joueurCourant.getCamp() == Joueur.SUEDOIS){
			if (p.x >= 1){
				if ((p.x != 1 && plateauJeu.getPlateauPieces(p.x-1, p.y).type() == Piece.MOSCOVITE
						&& (plateauJeu.getPlateauPieces(p.x-2, p.y).type() == Piece.SUEDOIS || plateauJeu.getPlateauPieces(p.x-2, p.y).type() == Piece.ROI)) || (p.x == 1 && plateauJeu.getPlateauPieces(p.x-1, p.y).type() == Piece.MOSCOVITE)){
					plateauJeu.setPlateauPieces(p.x-1, p.y, new PieceVide());
					stringPrises = priseHistorique(Piece.MOSCOVITE, new Point(p.x-1, p.y), stringPrises);
					nbPrises++;;
				}
			}
			if (p.x <= plateauJeu.getLine()-2){
				if ((p.x!=plateauJeu.getLine()-2 && plateauJeu.getPlateauPieces(p.x+1, p.y).type() == Piece.MOSCOVITE
						&& (plateauJeu.getPlateauPieces(p.x+2, p.y).type() == Piece.SUEDOIS || plateauJeu.getPlateauPieces(p.x+2, p.y).type() == Piece.ROI)) || (p.x==plateauJeu.getLine()-2 && plateauJeu.getPlateauPieces(p.x+1, p.y).type() == Piece.MOSCOVITE)){
					plateauJeu.setPlateauPieces(p.x+1, p.y, new PieceVide());
					stringPrises = priseHistorique(Piece.MOSCOVITE, new Point(p.x+1, p.y), stringPrises);
					nbPrises++;;
				}
			}
			if (p.y >= 1){
				if ((p.y!=1 && plateauJeu.getPlateauPieces(p.x, p.y-1).type() == Piece.MOSCOVITE
						&& (plateauJeu.getPlateauPieces(p.x, p.y-2).type() == Piece.SUEDOIS || plateauJeu.getPlateauPieces(p.x, p.y-2).type() == Piece.ROI))||(p.y==1 && plateauJeu.getPlateauPieces(p.x, p.y-1).type() == Piece.MOSCOVITE)){
					plateauJeu.setPlateauPieces(p.x, p.y-1, new PieceVide());
					stringPrises = priseHistorique(Piece.MOSCOVITE, new Point(p.x, p.y-1), stringPrises);
					nbPrises++;;
				}
			}
			if (p.y <= plateauJeu.getColumn()-2){
				if ((p.y != plateauJeu.getColumn()-2 && plateauJeu.getPlateauPieces(p.x, p.y+1).type() == Piece.MOSCOVITE
						&& (plateauJeu.getPlateauPieces(p.x, p.y+2).type() == Piece.SUEDOIS || plateauJeu.getPlateauPieces(p.x, p.y+2).type() == Piece.ROI)) || (p.y == plateauJeu.getColumn()-2 && plateauJeu.getPlateauPieces(p.x, p.y+1).type() == Piece.MOSCOVITE)){
					plateauJeu.setPlateauPieces(p.x, p.y+1, new PieceVide());
					stringPrises = priseHistorique(Piece.MOSCOVITE, new Point(p.x, p.y+1), stringPrises);
					nbPrises++;;
				}
			}
		}

		if (nbPrises == 0){
			String s = historique.get(historique.size()-1);
			s = s.concat("*");
			historique.set(historique.size()-1, s);
		}
		else{
			String s = historique.get(historique.size()-1);
			s = s.concat(((Integer)nbPrises).toString());
			s = s.concat(stringPrises);
			historique.set(historique.size()-1, s);
		}
	}


	public void coupHistorique(int type, Deplacement d){
		String s = new String("");
		s = s.concat(((Integer)type).toString());
		s = s.concat(((Integer)d.getPointDepart().x).toString());
		s = s.concat(((Integer)d.getPointDepart().y).toString());
		s = s.concat(((Integer)d.getPointArrivee().x).toString());
		s = s.concat(((Integer)d.getPointArrivee().y).toString());
		it.add(s);
		while(it.hasNext()){
			it.next();
			it.remove();
		}
	}

	public String priseHistorique(int type, Point p, String s){
		s = s.concat("p");
		s = s.concat(((Integer)type).toString());
		s = s.concat(((Integer)p.x).toString());
		s = s.concat(((Integer)p.y).toString());
		return s;
	}

	// retourne 2 plateaux en arriï¿½re (action joueur + action IA)
	public void annuler(){
		if (it.hasPrevious() && historique.size()%2 == 0){

			for(int i=0;i<2;i++){
				it.previous();
				int nbPrises;
				String s = it.next();
				int typePion = (int)((s.charAt(0)-'0'));
				Point pionDepart = new Point((int)(s.charAt(1)-'0'),(int)(s.charAt(2)-'0'));
				Point pionArrivee = new Point((int)(s.charAt(3)-'0'),(int)(s.charAt(4)-'0'));
				if (typePion == Piece.ROI)
					plateauJeu.setPlateauPieces(pionDepart.x, pionDepart.y, new PieceRoi());
				if (typePion == Piece.MOSCOVITE){
					plateauJeu.setPlateauPieces(pionDepart.x, pionDepart.y, new PieceMoscovite());
				}
				if (typePion == Piece.SUEDOIS)
					plateauJeu.setPlateauPieces(pionDepart.x, pionDepart.y, new PieceSuedois());
				plateauJeu.setPlateauPieces(pionArrivee.x, pionArrivee.y, new PieceVide());
				if (s.charAt(5) != '*'){  // !='*' dans ce cas c'est un entier : le nombre de prises
					nbPrises = (int)((s.charAt(5))-'0');
					for(int j=6;j<6+(nbPrises*4);j+=4){
						int typePrise = (int)((s.charAt(j+1))-'0');  // i+1 car il faut sauter le 'p'
						Point prise = new Point((int)(s.charAt(j+2)-'0'),(int)(s.charAt(j+3)-'0'));
						if (typePrise == Piece.ROI)
							plateauJeu.setPlateauPieces(prise.x, prise.y, new PieceRoi());
						if (typePrise == Piece.MOSCOVITE)
							plateauJeu.setPlateauPieces(prise.x, prise.y, new PieceMoscovite());
						if (typePrise == Piece.SUEDOIS)
							plateauJeu.setPlateauPieces(prise.x, prise.y, new PieceSuedois());
					}
				}
				interfaceDessin.repaint();
				it.previous();
			}
		}
	}

	public void refaire(){
		if (it.hasNext() && historique.size()%2 == 0){
			for(int i=0;i<2;i++){
				it.next();
				int nbPrises;
				String s = it.previous();
				int typePion = (int)((s.charAt(0)-'0'));
				Point pionArrivee = new Point((int)(s.charAt(1)-'0'),(int)(s.charAt(2)-'0'));
				Point pionDepart = new Point((int)(s.charAt(3)-'0'),(int)(s.charAt(4)-'0'));
				if (typePion == Piece.ROI)
					plateauJeu.setPlateauPieces(pionDepart.x, pionDepart.y, new PieceRoi());
				if (typePion == Piece.MOSCOVITE){
					plateauJeu.setPlateauPieces(pionDepart.x, pionDepart.y, new PieceMoscovite());
				}
				if (typePion == Piece.SUEDOIS)
					plateauJeu.setPlateauPieces(pionDepart.x, pionDepart.y, new PieceSuedois());
				plateauJeu.setPlateauPieces(pionArrivee.x, pionArrivee.y, new PieceVide());

				if (s.charAt(5) != '*'){  // !='*' dans ce cas c'est un entier : le nombre de prises
					nbPrises = (int)((s.charAt(5))-'0');
					for(int j=6;j<6+(nbPrises*4);j+=4){
						// il faut sauter le 'p' et le type de piï¿½ce, qui ne sert ï¿½ rien ici
						Point prise = new Point((int)(s.charAt(j+2)-'0'),(int)(s.charAt(j+3)-'0'));
						plateauJeu.setPlateauPieces(prise.x, prise.y, new PieceVide());
					}
				}
				interfaceDessin.repaint();
				it.next();
			}
		}
	}

	/*public void save()
	{
		File directory=null;
		JFrame frame = new JFrame("SAVE");
		JFileChooser chooser = new JFileChooser();
		if (directory != null)
			chooser.setCurrentDirectory(directory);
		int result = chooser.showSaveDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			try {
				directory = chooser.getCurrentDirectory();
				PrintStream p;
				p = new PrintStream(chooser.getSelectedFile());
				if (difficulte == "facile")
					p.print(1 + " ");
				else if (difficulte == "normal")
					p.print(2 + " ");
				else if (difficulte == "difficile")
					p.print(3 + " ");
				else if (difficulte == "ultime")
					p.print(4 + " ");
				else if (tour)
					p.print("1 ");
				else
					p.print("2 ");
				p.print(historique.size()+" ");
				for ( int i=0; i<historique.size(); i++ ){
					String tmp = historique.get(i);
					p.print(tmp);
					p.println();
				}
				p.println(plateauJeu.getLine()+" "+plateauJeu.getColumn());
				for(int i=0;i<plateauJeu.getLine();i++){
					for(int j=0;j<plateauJeu.getColumn();j++){
						p.print(plateauJeu.getPlateauPieces(i,j).type());
					}
					p.println();
				}

			} catch (IOException e) {
				System.out.println("Impossible de sauver dans le fichier selectionne");
			}
		}
	}*/
	
	public void save()
	{
		DialogSave save = new DialogSave(/*interfaceJeu, */this);
	}
	
	public void setAireDessin(AireDessin d){
		this.interfaceDessin = d;
	}
	
	public String getDifficulte(){
		return difficulte;
	}
	
	public LinkedList<String> getHistorique(){
		return historique;
	}

    public InterfaceJoueur getJoueurHumain(){
    	return joueurHumain;
    }
    
    public void setJoueurHumain(InterfaceJoueur j){
    	joueurHumain = j;
    }
}


/*
Attaques : 



- tremblement de terre : le plateau tremble soit sur l'axe X soit sur l'axe Y. les pions qui ne sont pas gï¿½nï¿½s ont une chance sur 2 de bouger dans l'axe en question.






- dï¿½placer 2 piï¿½ces











- vous jouez 2 tours, puis l'adversaire joue 2 tours

(- faire revivre un pion)

- contrer l'effet adverse



niveau 1 : 
- bloquer un pion adverse pendant un tour  => on sait qu'un pion va bouger lï¿½, on l'en empï¿½che
- pousse un pion adverse adjacent d'une case, il ne peut pas se faire bouffer ainsi. A jouer en fin de tour
 => altï¿½re un peu la partie, et on ne peut donc pas bouger un pion et le prendre aprï¿½s

niveau 2 : 
- limite le dï¿½placement de la prochaine piï¿½ce adverse ï¿½ une case (l'adversaire ne sait pas que l'attaque a ï¿½tï¿½ lancï¿½e)
=> empï¿½che ï¿½ventuellement une prise et peut ammener ï¿½ une situation favorable ï¿½ la prise du pion adverse
- tï¿½lï¿½portation : bouge un de vos pion oï¿½ vous voulez, mais ce la ne doit pas engendrer de prise de pion. On ne peut pas se 
dï¿½placer ï¿½ cï¿½tï¿½ du roi.

niveau 3 : 
- saut : peut passer par dessus un pion. On ne peut pas aller ï¿½ cï¿½tï¿½ du roi => permet de bouffer un pion normalement inaccessible
- diagonale  : permet un dï¿½placement en diagonale. On ne peut pas aller ï¿½ cï¿½tï¿½ du roi

niveau 4 : 
- piï¿½ge : immobilisation => on peut mettre ï¿½a en arriï¿½re du roi pour le protï¿½ger, ralentissant l'ennemi pendant un tour 
=> (niveau 4 ?) 
- rend invincible un pion pendant 2 tours => ce qui est toujours mieux que de tuer un pion : le jouer a un plan et peut ainsi le 
continuer. Alors que tuer un pion : on ne connait pas l'intention de l'adversaire 

niveau 5 : 
- ressuscite un pion adverse, le bloque pendant 3 (5 ?) tours (le faisant donc chier). (Au bout de 3 tours l'adversaire reprend le contrï¿½le
du pion.) => met un obstacle et peut ralentir considï¿½rablement l'adversaire
- piï¿½ge : limite le dï¿½placement du pion ï¿½ 1 pendant 2 tours => ralentit encore plus la progression vers le roi, que le piï¿½ge d'immobilisation

niveau 6 : 
- truc : met une sort de virus sur un pion, qui jouera alï¿½atoirement tous les 10 tours (et remplaï¿½ant son coup (ou pas))
(l'adversaire ne sait pas quel pion c'est et ne sait pas quand l'attaque a ï¿½tï¿½ lancï¿½). Peut se propager sur d'autrs pions au contact.
- inverse le prochain dï¿½placement de l'adversaire (l'adversaire ne sait pas que l'attaque a ï¿½tï¿½ lancï¿½e)
=> on peut donc empï¿½cher une prise et tendre une embuscade pour au contraire prendre un pion adverse 

niveau 7 : 
- furie : quand un pion bouffe un pion, utilise l'attaque pour faire dï¿½placer le pion une autre fois
=> on peut donc bouffer 2 pions
- piï¿½ge : bombe (tue le pion qui va dessus) => mieux que les niveaux 3, car on a pas besoin d'essayer de prendre en tenaille un pion, 
il meurt plus facilement

niveau 8 : 
- Catacombes : bloque au hasard 2 angles du plateau, aucun pion ne peut y aller. Dure 2 tours. Utilisable uniquement par les mosco
- sacrifice : dï¿½truit un pion suï¿½dois ï¿½ cï¿½tï¿½ du roi pour rendre invincible le roi pendant 3 tours (un tour ce serait pas assez, vu que
si un suï¿½dois est ï¿½ cï¿½tï¿½ du roi, c'est que les mosco ne peuvent pas prendre le roi). Utilisable uniquement par les suï¿½dois.
- convertir un pion adverse => mieux que la furie : on fait directment une diffï¿½rence de 2 pions entre les joueurs, sans prï¿½voir
ï¿½ l'avance de pouvoir prendre un autre pion avec la furie














==============


niveau 1 : 
- piï¿½ge : immobilisation le tour prochain => assez dur ï¿½ utiliser correctement (immobilisation ciblï¿½e : pas tellement plus efficace, 
et moins marant ï¿½ utiliser), le piï¿½ge dure 2 tours (ou un, on verra) (peut ï¿½tre piï¿½ge persistant)
- annule un coup adverse

niveau 2 : 
- tï¿½lï¿½portation : bouge un de vos pion oï¿½ vous voulez, mais ce la ne doit pas engendrer de prise de pion. On ne peut pas se 
dï¿½placer ï¿½ cï¿½tï¿½ du roi. => empï¿½che une prise imminente => permet de rï¿½parer une erreur, par exemple
- bouge un pion adverse, pas le droit de l'amener ï¿½ cï¿½tï¿½ d'un pion amical.

niveau 3 : 
- modifie le dernier coup adverse : le pion bouge d'une ou deux cases en plus (permet de bouffer un pion peu ï¿½loignï¿½e, et 
c'est mieux que annuler le coup : lï¿½ on annule + permet de bouffer le pion)
- piï¿½ge : bombe (tue le pion qui va dessus) (restera pas bcp de temps)

niveau 4 : 
- saut : peut passer par dessus un pion. On ne peut pas aller ï¿½ cï¿½tï¿½ du roi => permet de bouffer un pion normalement inaccessible
- diagonale  : permet un dï¿½placement en diagonale. On ne peut pas aller ï¿½ cï¿½tï¿½ du roi

niveau 5 : 
- furie : quand un pion bouffe un pion, utilise l'attaque pour faire dï¿½placer le pion une autre fois => un peu mieux que diagonale
ou que saut : lï¿½ on peut bouffer un pion adverse normalement protï¿½gï¿½ par un pion qui boufferait le bouffant, mais du coup ici le bouffant
peut se retrancher. On peut bouffer 2 pions, aussi
- convertir un pion adverse => mieux que la furie : on fait directment une diffï¿½rence de 2 pions entre les joueurs, sans prï¿½voir
ï¿½ l'avance de pouvoir prendre un autre pion avec la furie
- catacombes : bloque 2 angles pendant 3 tours (oblige le roi ï¿½ rebrousser chemin) : rï¿½servï¿½ aux moscovites
- pousse les soldats ennemis autour du roi d'une case (ou deux ?) : reservï¿½ aux suï¿½dois

 => le niveau 5 aura 3 nouvelles attaques



 */