package moteur;

import gui.AireDessin;

import java.awt.Point;
import java.util.Scanner;

import joueur.Deplacement;
import joueur.InterfaceJoueur;
import joueur.Joueur;
import joueur.JoueurDistant;
import joueur.JoueurLocal;
import network.ProtocolTablut;
import pieces.Piece;
import pieces.PieceMoscovite;
import pieces.PieceRoi;
import pieces.PieceSuedois;
import pieces.PieceVide;
import cases.Case;


/*
 * L'un des moteurs aura en param�tres : new MoteurMultijoueur(DrawingArea d, GameWindow w, campQuiCommence, TRUE, pseudoLocal, pseudoDistant)
 * L'autre aura : new MoteurMultijoueur(DrawingArea d, GameWindow w, campQuiCommence, FALSE, pseudoLocal, pseudoDistant)
 * Histoire que les 2 joueurs aient des camps diff�rents ... Le dernier param�tre correspond au camp du joueur
 *local du moteur. Donc si une personne cr�e une partie en tant que moscovite, son moteur devra �tre cr��
 *avec FALSE, tandis que le moteur du joueur distant sera cr�� avec TRUE.
 * 
 * Rappel : Joueur.SUEDOIS = true
 * 			Joueur.MOSCOVITE = false
 * 
 * 
 * Ainsi, le moteur m1 aura pour joueur local les moscovites, et le moteur m2 les su�dois
 */

public class MoteurMultijoueur implements InterfaceMoteur {
	public static int PLVSIA = 1; // solo
	public static int PLVSPL = 2; // online

	private Plateau plateauJeu;

	private InterfaceJoueur joueurLocal, joueurDistant;
	private boolean tour; 
	private Point caseClic;
	private boolean clicAbandonne;
	
	private int tamponEntier;
	private String tamponString;

	private AireDessin interfaceDessin;
	//private GameWindow interfaceJeu;
	private boolean roiBouffe;

	public MoteurMultijoueur(AireDessin d,/* GameWindow w,*/ Boolean campJoueurLocal, boolean campQuiCommence, String pseudoLocal, InterfaceJoueur _joueurDistant)
	{
		tour = campQuiCommence;
		plateauJeu = new Plateau();
		interfaceDessin = d;
		//interfaceJeu = w;
		joueurLocal = new JoueurLocal(campJoueurLocal, pseudoLocal);
		joueurDistant = _joueurDistant;
		clicAbandonne = false;
		caseClic = new Point();
		roiBouffe = false;
		tamponEntier = 0;
		tamponString = new String("");
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

	// renvoie vrai si le d�placement est bien vertical ou horizontal, et qu'il n'y a aucun pion qui g�ne
	public Boolean deplacementCroix(Point p, InterfaceJoueur joueurCourant){

		// si le d�placement est bien vertical ou horizontal
		if (joueurCourant.getPionCourant().x == p.x || joueurCourant.getPionCourant().y == p.y){
			// si c'est un d�placement vers le haut
			if (joueurCourant.getPionCourant().x - p.x > 0){
				for(int i=joueurCourant.getPionCourant().x-1;i>=p.x;i--)
					if (plateauJeu.getPlateauPieces(i,joueurCourant.getPionCourant().y).type() != Piece.VIDE)
						return false;
			}
			// si c'est un d�placement vers le bas
			else if (joueurCourant.getPionCourant().x - p.x < 0){
				for(int j=joueurCourant.getPionCourant().x+1;j<=p.x;j++)
					if (plateauJeu.getPlateauPieces(j,joueurCourant.getPionCourant().y).type() != Piece.VIDE)
						return false;
			}
			// si c'est un d�placement vers la droite
			else if (joueurCourant.getPionCourant().y - p.y < 0){
				for(int k=joueurCourant.getPionCourant().y+1;k<=p.y;k++)
					if (plateauJeu.getPlateauPieces(joueurCourant.getPionCourant().x, k).type() != Piece.VIDE)
						return false;
			}
			// si c'est un d�placement vers la gauche
			else if (joueurCourant.getPionCourant().y - p.y > 0){
				for(int l=joueurCourant.getPionCourant().y-1;l>=p.y;l--)
					if (plateauJeu.getPlateauPieces(joueurCourant.getPionCourant().x, l).type() != Piece.VIDE)
						return false;
			}
			// si le point d'arriv�e est le konakis et que la pi�ce est un soldat, le d�placement n'est pas valide
			if (plateauJeu.getPlateauCases(p.x, p.y).type() == Case.KONAKIS && plateauJeu.getPlateauPieces(joueurCourant.getPionCourant().x, joueurCourant.getPionCourant().y).type() != Piece.ROI)
				return false;
			// si le point d'arriv�e est un angle et que la pi�ce est un soldat, invalide
			else if (plateauJeu.getPlateauCases(p.x, p.y).type() == Case.ANGLE && plateauJeu.getPlateauPieces(joueurCourant.getPionCourant().x, joueurCourant.getPionCourant().y).type() != Piece.ROI)
				return false;
			else
				return true;
		}
		return false;
	}

	// coup valide est appel� uniquement si estPionJoueur renvoie vrai
	public Boolean coupValide(Point p, InterfaceJoueur joueurCourant){
		if (deplacementCroix(p, joueurCourant)){
			//	si le pion est un roi, le d�placement est bon puisqu'il a le droit d'aller dans les angles
			// et sur le konakis
			if (plateauJeu.getPlateauPieces(joueurCourant.getPionCourant().x, joueurCourant.getPionCourant().y).type() == Piece.ROI)
				return true;
			//	si le pion est un soldat, il ne faut pas que la case d'arriv�e soit un angle ou un konakis
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
		j.setPionCourant(p.x, p.y);
	}

	public void deselectionner(InterfaceJoueur j){
		j.setPionCourant(-1, -1);
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
			return joueurLocal;
		else
			return joueurDistant;
	}

	// prend en param�tre la case o� a cliqu� le joueur
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
	
	    // r�seau => demande au serveur l'xp du joueur local et son niveau
	  	 if ((tamponEntier = joueurDistant.DB_getExperience(joueurLocal.getPseudo())) == -1){ 
	  		 System.out.println("erreur requete GETEXP");
	  		//retour au menu + disconnect
	  	 }
	    joueurLocal.setExperience(tamponEntier);
	    
	 	 if ((tamponEntier = joueurDistant.DB_getNiveau(joueurLocal.getPseudo())) == -1){
	 		System.out.println("erreur requete GETLVL");
	  		//message d'erreur + retour au menu + disconnect
	  	 }
	    joueurLocal.setNiveau(tamponEntier);
	   	 
	    
	 // r�seau => demande au serveur l'xp du joueur distant et son niveau
		if ((tamponString = joueurDistant.DB_getPseudo()) == null){
	    	System.out.println("erreur requete GETPSEUDO");
	  		//message d'erreur + retour au menu + disconnect
	  	 }
	    joueurDistant.setPseudo(tamponString);
	    
	  	 if ((tamponEntier = joueurLocal.DB_getExperience(joueurDistant.getPseudo())) == -1){
	  		 System.out.println("erreur requete GETEXP");
	  		//retour au menu + disconnect
	  	 }
		joueurDistant.setExperience(tamponEntier);

		if ((tamponEntier = joueurLocal.DB_getNiveau(joueurDistant.getPseudo())) == -1){
	 		System.out.println("erreur requete GETLVL");
	  		//message d'erreur + retour au menu + disconnect
	  	 }
		joueurDistant.setNiveau(tamponEntier);
		



		boolean aJoue;
		while(!partieFinie() && !clicAbandonne && !roiBouffe) {
			InterfaceJoueur joueurCourant;
			if (joueurDistant.getCamp() == tour){
				joueurCourant = joueurDistant;
				aJoue = false;
				while(aJoue == false){
					Point p = new Point(0,0);

					/*  
					 * 
					 * r�seau => si c'est le joueur distant qui doit jouer, il faut attendre puis recevoir le paquet du d�placement
					 * 
					 */
					while(joueurDistant.getReceive().isActionState() == false){
					}
					p = parseBufferAction(joueurDistant.getReceive().getActionBuffer());
					joueurDistant.getReceive().setActionState(false);

					// c'est le joueur distant qui a s�lectionn� un pion, donc il faut v�rifier si c'est un pion
					//ADVERSE, et sinon c'est un coup � jouer.
					if (estPionJoueur(p, joueurCourant)){
						// on s�lectionne forc�ment un pion du joueur online, puisque on est dans le parseur ACTION
						//uniquement quand on attendait un coup du joueur distant
						nouvelleSelection(p, joueurCourant);
					}
					else{
						// sinon pas besoin de tester si le coup est valide, �a a d�j� �t� fait par le joueur distant
						Deplacement d = new Deplacement(joueurCourant.getPionCourant(), p);
						jouerCoup(d, joueurCourant);
						deselectionner(joueurCourant);
						joueurCourant.setPionCourant(-1, -1);
						aJoue = true;
					}
				}
			}
			else{
				joueurCourant = joueurLocal;
				aJoue = false;
				while(aJoue == false){
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (estPionJoueur(caseClic, joueurCourant)){
						nouvelleSelection(caseClic, joueurCourant);
						/*
						 * r�seau => envoyer paquet de selection : d.getPointArrivee().x et d.d.getPointArrivee.y au joueur distant
						 * 
						 */

						if (joueurDistant.sendAction(caseClic) == false){
					  		//message d'erreur + retour au menu
					  }
					}
					else{
						if (joueurCourant.getPionCourant().x != -1 && joueurCourant.getPionCourant().y != -1){
							if (coupValide(caseClic, joueurCourant)){
								Deplacement d = new Deplacement(joueurCourant.getPionCourant(), caseClic);
								jouerCoup(d, joueurCourant);
								deselectionner(joueurCourant);
								aJoue = true;
								joueurCourant.setPionCourant(-1, -1);
								/*
								 * r�seau => envoyer paquet de destination : d.getPointArrivee().x et d.d.getPointArrivee.y au joueur distant
								 * 
								 */
								  if (joueurDistant.sendAction(d.getPointArrivee()) == false){
								  		//message d'erreur + retour au menu
								  }
								  
								 
							}
						}
						else{
							//	message .. ou pas, sur l'interface graphique
						}
					}	
				}
			}
			tour = !tour;
			interfaceDessin.repaint();
		}
		// si le joueur local a gagn�
		if(tour == joueurLocal.getCamp()) {
			//interfaceJeu.setStatusBar("Joueur 1 a gagn� !");

			// ajout de l'exp�rience au joueur due � la fin de partie
			if (joueurLocal.getNiveau()!=30){
				/* 
				 * 
				 * 
				 * 	r�seau => r�cup�rer le niveau du joueur distant, puisqu'il a pu �tre mis � jour deuis le d�but de la partie
				 * on la stocke dans la variable joueurOnline.niveau, en utilisant joueurOnline.setNiveau(int i)
				 */
				  if ((tamponEntier = joueurDistant.DB_getNiveau(joueurDistant.getPseudo())) == ProtocolTablut.ERROR_CON){
				  		//message d'erreur + retour au menu
				  	 }
				    joueurDistant.setNiveau(tamponEntier);
				  
				 
				joueurLocal.setExperience(joueurLocal.getExperience() + 30);
				//ajoute la diff�rence de niveau � l'xp du joueur courant
				joueurLocal.setExperience((joueurLocal.getNiveau() + joueurDistant.getNiveau())/3);
				//	v�rifier si il a pas pass� un niveau
				joueurLocal.passageNiveau();
				/* 
				 * 
				 * 
				 * 	r�seau => paquets � envoyer pour mettre � jour la base de donn�es, en cas de d�connexion : 
				 * joueurLocal.getExperience()
				 * joueurLocal.getNiveau()
				 */
				  	if (joueurDistant.DB_setNiveau(joueurLocal.getNiveau()) == ProtocolTablut.ERROR_CON){
				  		//message d'erreur + retour au menu
				  	 }
				  
				   if (joueurDistant.DB_setExperience(joueurLocal.getExperience()) == ProtocolTablut.ERROR_CON){
				  		//message d'erreur + retour au menu
				  	 }
				  
				 
			}
		} else {
			//interfaceJeu.setStatusBar("Joueur 2 a gagn� !");
		}
	}

	// appel�e en cas de clic sur le bouton abandonner
	public void abandonner(){
		if(tour) {
			//interfaceJeu.setStatusBar("Joueur 1 a gagn� !");
			//interfaceJeu.endingMsg(1, mode);
		} else {
			//interfaceJeu.setStatusBar("Joueur 2 a gagn� !");
			//interfaceJeu.endingMsg(2, mode);
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
					nbPrises++;
				}
				// si il a la possibilit� de prendre un roi
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
						roiBouffe = true;
						nbPrises++;
					}
				}
			}
			if (bas){
				if ((p.x!=plateauJeu.getColumn()-2 && plateauJeu.getPlateauPieces(p.x+1, p.y).type() == Piece.SUEDOIS
						&& plateauJeu.getPlateauPieces(p.x+2, p.y).type() == Piece.MOSCOVITE) || (p.x==plateauJeu.getColumn()-2 && plateauJeu.getPlateauPieces(p.x+1, p.y).type() == Piece.SUEDOIS)){
					plateauJeu.setPlateauPieces(p.x+1, p.y, new PieceVide());
					nbPrises++;
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
						roiBouffe = true;
						nbPrises++;
					}
				}
			}
			if (gauche){
				if ((p.y!=1 && plateauJeu.getPlateauPieces(p.x, p.y-1).type() == Piece.SUEDOIS
						&& plateauJeu.getPlateauPieces(p.x, p.y-2).type() == Piece.MOSCOVITE) ||( p.y==1 && plateauJeu.getPlateauPieces(p.x, p.y-1).type() == Piece.SUEDOIS)){
					plateauJeu.setPlateauPieces(p.x, p.y-1, new PieceVide());
					nbPrises++;
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
						roiBouffe = true;
						nbPrises++;
					}
				}
			}
			if (droite){
				if ((p.y!=plateauJeu.getLine()-2 && plateauJeu.getPlateauPieces(p.x, p.y+1).type() == Piece.SUEDOIS
						&& plateauJeu.getPlateauPieces(p.x, p.y+2).type() == Piece.MOSCOVITE) || (p.y==plateauJeu.getLine()-2 && plateauJeu.getPlateauPieces(p.x, p.y+1).type() == Piece.SUEDOIS)){
					plateauJeu.setPlateauPieces(p.x, p.y+1, new PieceVide());
					nbPrises++;
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
						roiBouffe = true;
						nbPrises++;
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
					nbPrises++;
				}
			}
			if (p.x <= plateauJeu.getLine()-2){
				if ((p.x!=plateauJeu.getLine()-2 && plateauJeu.getPlateauPieces(p.x+1, p.y).type() == Piece.MOSCOVITE
						&& (plateauJeu.getPlateauPieces(p.x+2, p.y).type() == Piece.SUEDOIS || plateauJeu.getPlateauPieces(p.x+2, p.y).type() == Piece.ROI)) || (p.x==plateauJeu.getLine()-2 && plateauJeu.getPlateauPieces(p.x+1, p.y).type() == Piece.MOSCOVITE)){
					plateauJeu.setPlateauPieces(p.x+1, p.y, new PieceVide());
					nbPrises++;
				}
			}
			if (p.y >= 1){
				if ((p.y!=1 && plateauJeu.getPlateauPieces(p.x, p.y-1).type() == Piece.MOSCOVITE
						&& (plateauJeu.getPlateauPieces(p.x, p.y-2).type() == Piece.SUEDOIS || plateauJeu.getPlateauPieces(p.x, p.y-2).type() == Piece.ROI))||(p.y==1 && plateauJeu.getPlateauPieces(p.x, p.y-1).type() == Piece.MOSCOVITE)){
					plateauJeu.setPlateauPieces(p.x, p.y-1, new PieceVide());
					nbPrises++;
				}
			}
			if (p.y <= plateauJeu.getColumn()-2){
				if ((p.y != plateauJeu.getColumn()-2 && plateauJeu.getPlateauPieces(p.x, p.y+1).type() == Piece.MOSCOVITE
						&& (plateauJeu.getPlateauPieces(p.x, p.y+2).type() == Piece.SUEDOIS || plateauJeu.getPlateauPieces(p.x, p.y+2).type() == Piece.ROI)) || (p.y == plateauJeu.getColumn()-2 && plateauJeu.getPlateauPieces(p.x, p.y+1).type() == Piece.MOSCOVITE)){
					plateauJeu.setPlateauPieces(p.x, p.y+1, new PieceVide());
					nbPrises++;
				}
			}
		}
		// on ajoute l'xp que si le coup qui vient d'�tre jou� par le moteur est le coup du joueur local
		if (nbPrises > 0 && joueurCourant == joueurLocal){
			// ajout de l'exp�rience au joueur due � la prise, si son niveau n'est pas 30
			/* 
			 * 
			 * 
			 * 	r�seau => r�cup�rer le niveau du joueur distant, puisqu'il a pu �tre mis � jour deuis le d�but de la partie
			 * on la stocke dans la variable joueurOnline.niveau, en utilisant joueurOnline.setNiveau(int i)
			 */
			  if ((tamponEntier = joueurDistant.DB_getNiveau(joueurDistant.getPseudo())) == ProtocolTablut.ERROR_CON){
			  		//message d'erreur + retour au menu
			  	 }
			    joueurDistant.setNiveau(tamponEntier);
			  
			 
			for(int i=0;i<nbPrises;i++){
				if (joueurLocal.getNiveau()!=30){
					joueurLocal.setExperience(joueurLocal.getExperience() + 10);
					//	ajoute la diff�rence de niveau � l'xp du joueur courant
					joueurLocal.setExperience((joueurLocal.getNiveau() + joueurDistant.getNiveau())/3);
					// 	v�rifier si il a pas pass� un niveau
					joueurLocal.passageNiveau();
				}
			}
			/* 
			 * 
			 * 
			 * 	r�seau => paquets � envoyer pour mettre � jour la base de donn�es, en cas de d�connexion : 
			 * joueurLocal.getExperience()
			 * joueurLocal.getNiveau()
			 */ 
			  if (joueurDistant.DB_setNiveau(joueurLocal.getNiveau()) == ProtocolTablut.ERROR_CON){ 
			  		//message d'erreur + retour au menu
			  	 }
			  
			   if (joueurDistant.DB_setExperience(joueurLocal.getExperience()) == ProtocolTablut.ERROR_CON){
			  		//message d'erreur + retour au menu
			  	 }
			  
		}
	}

	// fonction sp�cifique vu le traitement associ� au buffer 
	public Point parseBufferAction(String buffer){
		Scanner s = new Scanner(buffer);	
		String protocol = s.next();
		Point p = new Point(0,0);
		if(protocol.equalsIgnoreCase(ProtocolTablut.ACTION))
		{
			int l = s.nextInt(); 
			int c = s.nextInt(); 
			p = new Point(l,c);
		}
		return p;
	}

	@Override
	public void annuler() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refaire() {
		// TODO Auto-generated method stub
		
	}
	
	public void setAireDessin(AireDessin d){
		this.interfaceDessin = d;
	}
}




