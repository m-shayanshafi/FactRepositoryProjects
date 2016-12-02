package joueur;

import java.awt.Point;
import moteur.*;
import pieces.*;
import java.util.*;
import java.*;

import pieces.Piece;
import pieces.PieceMoscovite;
import pieces.PieceRoi;
import pieces.PieceSuedois;
import pieces.PieceVide;
import cases.Case;

class MonPetitMoteur {
	public boolean roiBouffe;
	public MonPetitMoteur (){
		roiBouffe = false;
	}
//======================================= coupValideGeorges ==============================================
//coup valide est appelï¿½ uniquement si estPionJoueur renvoie vrai
 public Boolean coupValideGeorges(Plateau plateauJeu,Point p, Piece depart){
	 if ( p.x < 0 || p.x>plateauJeu.getColumn()-1 || p.y<0 || p.y>plateauJeu.getLine()-1 )
			return false;
		if (depart.getLigne() == p.x && depart.getColonne() == p.y)
			return false;
	if (deplacementCroix(plateauJeu,p, depart)){
		//	si le pion est un roi, le dï¿½placement est bon puisqu'il a le droit d'aller dans les angles
		// et sur le konakis
		if (plateauJeu.getPlateauPieces(depart.getLigne(), depart.getColonne()).type() == Piece.ROI)
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
//======================================= deplacementCroix ==============================================
 public Boolean deplacementCroix(Plateau plateauJeu,Point p, Piece depart){
	//si deplacement est appelé pour un point hors bord du plateau, sère pour la fonc. etatDuRoi,...
	if ( p.x < 0 || p.x>plateauJeu.getColumn()-1 || p.y<0 || p.y>plateauJeu.getLine()-1 )
		return false;
	if (depart.getLigne() == p.x && depart.getColonne() == p.y)
		return false;
	// si le deplacement est bien vertical ou horizontal
	if (depart.getLigne() == p.x || depart.getColonne() == p.y){
		// si c'est un deplacement vers le haut
		if (depart.getLigne() - p.x > 0){
			for(int i=depart.getLigne()-1;i>=p.x;i--)
				if (plateauJeu.getPlateauPieces(i,depart.getColonne()).type() != Piece.VIDE)
					return false;
		}
		// si c'est un deplacement vers le bas
		else if (depart.getLigne() - p.x < 0){
			for(int j=depart.getLigne()+1;j<=p.x;j++)
				if (plateauJeu.getPlateauPieces(j,depart.getColonne()).type() != Piece.VIDE)
					return false;
		}
		// si c'est un deplacement vers la droite
		else if (depart.getColonne() - p.y < 0){
			for(int k=depart.getColonne()+1;k<=p.y;k++)
				if (plateauJeu.getPlateauPieces(depart.getLigne(), k).type() != Piece.VIDE)
					return false;
		}
		// si c'est un deplacement vers la gauche
		else if (depart.getColonne() - p.y > 0){
			for(int l=depart.getColonne()-1;l>=p.y;l--)
				if (plateauJeu.getPlateauPieces(depart.getLigne(), l).type() != Piece.VIDE)
					return false;
		}
		// si le point d'arrivï¿½e est le konakis et que la piï¿½ce est un soldat, le deplacement n'est pas valide
		if (plateauJeu.getPlateauCases(p.x, p.y).type() == Case.KONAKIS && plateauJeu.getPlateauPieces(depart.getLigne(), depart.getColonne()).type() != Piece.ROI)
			return false;
		// si le point d'arrivï¿½e est un angle et que la piï¿½ce est un soldat, invalide
		else if (plateauJeu.getPlateauCases(p.x, p.y).type() == Case.ANGLE && plateauJeu.getPlateauPieces(depart.getLigne(), depart.getColonne()).type() != Piece.ROI)
			return false;
		else
			return true;
	}
	return false;
}
//=========================================== jouerCoup ================================================
 public String jouerCoup(Plateau plateauJeu,EquipeSuedois suedois,EquipeMoscovite moscovite,Deplacement d, boolean type)
{
	// System.out.println("dans jouerCoup"+d.getPointDepart().x+","+d.getPointDepart().y);
	String s = new String("");
	s = coupHistorique(plateauJeu.getPlateauPieces(d.getPointDepart().x, d.getPointDepart().y).type(), d, s);
	int typeCaseDepart = plateauJeu.getPlateauPieces(d.getPointDepart().x, d.getPointDepart().y).type();

	plateauJeu.setPlateauPieces(d.getPointDepart().x, d.getPointDepart().y, new PieceVide());
	if (typeCaseDepart == Piece.ROI){
		plateauJeu.setPlateauPieces(d.getPointArrivee().x,d.getPointArrivee().y, new PieceRoi());
		suedois.set(0,new PieceRoi(d.getPointArrivee().x,d.getPointArrivee().y));
	}
	else if (typeCaseDepart == Piece.MOSCOVITE){
		plateauJeu.setPlateauPieces(d.getPointArrivee().x, d.getPointArrivee().y, new PieceMoscovite());
		moscovite.set(moscovite.find(d.getPointDepart()),new PieceMoscovite(d.getPointArrivee().x,d.getPointArrivee().y));
	}
	else if (typeCaseDepart == Piece.SUEDOIS){
		plateauJeu.setPlateauPieces(d.getPointArrivee().x, d.getPointArrivee().y, new PieceSuedois());
		suedois.set(suedois.find(d.getPointDepart()),new PieceSuedois(d.getPointArrivee().x,d.getPointArrivee().y));
		}
	
	// Envoyer coordonnée du point de depart et d'arrivé pour l'animation
	// Mettre une temporisation pour attendre le temps de l'animation
	s = prise(plateauJeu,suedois,moscovite,d.getPointArrivee(), type , s);
	//System.out.println("Coup joué, pion deplacé de de ("+d.getPointDepart().getX()+","+d.getPointDepart().y+") à la position ("+d.getPointArrivee().x+","+d.getPointArrivee().y+")");
	return s;
}
//=========================================== prise =========================================================
 public String prise(Plateau plateauJeu,EquipeSuedois suedois,EquipeMoscovite moscovite,Point p, boolean type, String s){
	// haut = true => le soldat qui vient de bouger n'est pas sur le bord haut, il faut donc tester si il prend un roi en haut
	boolean haut, bas, gauche, droite;
	haut = bas = gauche = droite = false;
	boolean hautRoi, basRoi, gaucheRoi, droiteRoi;
	hautRoi = basRoi = gaucheRoi = droiteRoi = false;
	int nbPrise = 0;
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
	if (type == Joueur.MOSCOVITE){
		if (haut){
			// si il prend un soldat
			if ((p.x!=1 && plateauJeu.getPlateauPieces(p.x-1, p.y).type() == Piece.SUEDOIS
					&& plateauJeu.getPlateauPieces(p.x-2, p.y).type() == Piece.MOSCOVITE)||(p.x==1 && plateauJeu.getPlateauPieces(p.x-1, p.y).type() == Piece.SUEDOIS)){
				plateauJeu.setPlateauPieces(p.x-1, p.y, new PieceVide());
				suedois.remove(suedois.find(new Point(p.x-1,p.y)));
				stringPrises = priseHistorique(Piece.SUEDOIS, new Point(p.x-1, p.y), stringPrises);
				nbPrise++;
			}
			// si il a la possibilite de prendre un roi
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
					suedois.remove(0);
					stringPrises = priseHistorique(Piece.ROI, new Point(p.x-1, p.y), stringPrises);
					roiBouffe = true;
					nbPrise++;
				}
			}
		}
		if (bas){
			if ((p.x!=plateauJeu.getColumn()-2 && plateauJeu.getPlateauPieces(p.x+1, p.y).type() == Piece.SUEDOIS
					&& plateauJeu.getPlateauPieces(p.x+2, p.y).type() == Piece.MOSCOVITE) || (p.x==plateauJeu.getColumn()-2 && plateauJeu.getPlateauPieces(p.x+1, p.y).type() == Piece.SUEDOIS)){
				plateauJeu.setPlateauPieces(p.x+1, p.y, new PieceVide());
				suedois.remove(suedois.find(new Point(p.x+1,p.y)));
				stringPrises = priseHistorique(Piece.SUEDOIS, new Point(p.x+1, p.y), stringPrises);
				nbPrise++;
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
					suedois.remove(0);
					stringPrises = priseHistorique(Piece.ROI, new Point(p.x+1, p.y), stringPrises);
					roiBouffe = true;
					nbPrise++;
				}
			}
		}
		if (gauche){
			if ((p.y!=1 && plateauJeu.getPlateauPieces(p.x, p.y-1).type() == Piece.SUEDOIS
					&& plateauJeu.getPlateauPieces(p.x, p.y-2).type() == Piece.MOSCOVITE) ||( p.y==1 && plateauJeu.getPlateauPieces(p.x, p.y-1).type() == Piece.SUEDOIS)){
				plateauJeu.setPlateauPieces(p.x, p.y-1, new PieceVide());
				suedois.remove(suedois.find(new Point(p.x,p.y-1)));
				stringPrises = priseHistorique(Piece.SUEDOIS, new Point(p.x, p.y-1), stringPrises);
				nbPrise++;
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
					suedois.remove(0);
					stringPrises = priseHistorique(Piece.ROI, new Point(p.x, p.y-1), stringPrises);
					roiBouffe = true;
					nbPrise++;
				}
			}
		}
		if (droite){
			if ((p.y!=plateauJeu.getLine()-2 && plateauJeu.getPlateauPieces(p.x, p.y+1).type() == Piece.SUEDOIS
					&& plateauJeu.getPlateauPieces(p.x, p.y+2).type() == Piece.MOSCOVITE) || (p.y==plateauJeu.getLine()-2 && plateauJeu.getPlateauPieces(p.x, p.y+1).type() == Piece.SUEDOIS)){
				plateauJeu.setPlateauPieces(p.x, p.y+1, new PieceVide());
				suedois.remove(suedois.find(new Point(p.x,p.y+1)));
				stringPrises = priseHistorique(Piece.SUEDOIS, new Point(p.x, p.y+1), stringPrises);
				nbPrise++;
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
					suedois.remove(0);
					stringPrises = priseHistorique(Piece.ROI, new Point(p.x, p.y+1), stringPrises);
					roiBouffe = true;
					nbPrise++;
				}
			}
		}
	}
	// cas de prise de soldat, si on vient de jouer suedois
	if (type == Joueur.SUEDOIS){
		if (p.x >= 1){
			if ((p.x != 1 && plateauJeu.getPlateauPieces(p.x-1, p.y).type() == Piece.MOSCOVITE
					&& (plateauJeu.getPlateauPieces(p.x-2, p.y).type() == Piece.SUEDOIS || plateauJeu.getPlateauPieces(p.x-2, p.y).type() == Piece.ROI)) || (p.x == 1 && plateauJeu.getPlateauPieces(p.x-1, p.y).type() == Piece.MOSCOVITE)){
				plateauJeu.setPlateauPieces(p.x-1, p.y, new PieceVide());
				moscovite.remove(moscovite.find(new Point(p.x-1,p.y)));
				stringPrises = priseHistorique(Piece.MOSCOVITE, new Point(p.x-1, p.y), stringPrises);
				nbPrise++;
			}
		}
		if (p.x <= plateauJeu.getLine()-2){
			if ((p.x!=plateauJeu.getLine()-2 && plateauJeu.getPlateauPieces(p.x+1, p.y).type() == Piece.MOSCOVITE
					&& (plateauJeu.getPlateauPieces(p.x+2, p.y).type() == Piece.SUEDOIS || plateauJeu.getPlateauPieces(p.x+2, p.y).type() == Piece.ROI)) || (p.x==plateauJeu.getLine()-2 && plateauJeu.getPlateauPieces(p.x+1, p.y).type() == Piece.MOSCOVITE)){
				plateauJeu.setPlateauPieces(p.x+1, p.y, new PieceVide());
				moscovite.remove(moscovite.find(new Point(p.x+1,p.y)));
				stringPrises = priseHistorique(Piece.MOSCOVITE, new Point(p.x+1, p.y), stringPrises);
				nbPrise++;
			}
		}
		if (p.y >= 1){
			if ((p.y!=1 && plateauJeu.getPlateauPieces(p.x, p.y-1).type() == Piece.MOSCOVITE
					&& (plateauJeu.getPlateauPieces(p.x, p.y-2).type() == Piece.SUEDOIS || plateauJeu.getPlateauPieces(p.x, p.y-2).type() == Piece.ROI))||(p.y==1 && plateauJeu.getPlateauPieces(p.x, p.y-1).type() == Piece.MOSCOVITE)){
				plateauJeu.setPlateauPieces(p.x, p.y-1, new PieceVide());
				moscovite.remove(moscovite.find(new Point(p.x,p.y-1)));
				stringPrises = priseHistorique(Piece.MOSCOVITE, new Point(p.x, p.y-1), stringPrises);
				nbPrise++;
			}
		}
		if (p.y <= plateauJeu.getColumn()-2){
			if ((p.y != plateauJeu.getColumn()-2 && plateauJeu.getPlateauPieces(p.x, p.y+1).type() == Piece.MOSCOVITE
					&& (plateauJeu.getPlateauPieces(p.x, p.y+2).type() == Piece.SUEDOIS || plateauJeu.getPlateauPieces(p.x, p.y+2).type() == Piece.ROI)) || (p.y == plateauJeu.getColumn()-2 && plateauJeu.getPlateauPieces(p.x, p.y+1).type() == Piece.MOSCOVITE)){
				plateauJeu.setPlateauPieces(p.x, p.y+1, new PieceVide());
				moscovite.remove(moscovite.find(new Point(p.x,p.y+1)));
				stringPrises = priseHistorique(Piece.MOSCOVITE, new Point(p.x, p.y+1), stringPrises);
				nbPrise++;
			}
		}
	}

	if (nbPrise == 0){
		s = priseHistorique(-1, null, s);
	}
	else{
		s = s.concat(((Integer)nbPrise).toString());
		s = s.concat(stringPrises);
	}
	
	return s;
 }
//=========================================== partieFinie ================================================
 public boolean partieFinie(Plateau plateauJeu)
{
	if (roiBouffe)
		return true;
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



//=========================================== coupHistorique ================================================
 public String coupHistorique(int type, Deplacement d, String sdf){
	String s = new String(sdf);
	s = s.concat(((Integer)type).toString());
	s = s.concat(((Integer)d.getPointDepart().x).toString());
	s = s.concat(((Integer)d.getPointDepart().y).toString());
	s = s.concat(((Integer)d.getPointArrivee().x).toString());
	s = s.concat(((Integer)d.getPointArrivee().y).toString());
	return s;
}
//=========================================== priseHistorique ================================================
 public String priseHistorique(int type, Point p, String sdf){
	String s = new String(sdf);
	if (type == -1){
		s = s.concat("*");
	}
	else{
		s = s.concat("p");
		s = s.concat(((Integer)type).toString());
		s = s.concat(((Integer)p.x).toString());
		s = s.concat(((Integer)p.y).toString());
	}
	return s;
}
//=========================================== annuler ================================================
// retourne 2 plateaux en arriï¿½re (action joueur + action IA)
 public void annuler(Plateau plateauJeu, EquipeSuedois suedois, EquipeMoscovite moscovite, String sdf){
	 int nbPrises;
	String s = new String(sdf);
	int typePion = (int)((s.charAt(0)-'0'));
	Point pionDepart = new Point((int)(s.charAt(1)-'0'),(int)(s.charAt(2)-'0'));
	Point pionArrivee = new Point((int)(s.charAt(3)-'0'),(int)(s.charAt(4)-'0'));
	if (typePion == Piece.ROI){
		plateauJeu.setPlateauPieces(pionDepart.x, pionDepart.y, new PieceRoi());
		suedois.set(0, new PieceRoi(pionDepart.x,pionDepart.y));
	}
	if (typePion == Piece.MOSCOVITE){
		plateauJeu.setPlateauPieces(pionDepart.x, pionDepart.y, new PieceMoscovite());
		moscovite.set(moscovite.find(new Point(pionArrivee.x, pionArrivee.y)), new PieceMoscovite(pionDepart.x,pionDepart.y));
	}
	if (typePion == Piece.SUEDOIS){
		plateauJeu.setPlateauPieces(pionDepart.x, pionDepart.y, new PieceSuedois());
		suedois.set(suedois.find(new Point(pionArrivee.x, pionArrivee.y)), new PieceSuedois(pionDepart.x,pionDepart.y));
		
	}
	plateauJeu.setPlateauPieces(pionArrivee.x, pionArrivee.y, new PieceVide());
	if (s.charAt(5) != '*'){  // !='*' dans ce cas c'est un entier : le nombre de prises
		nbPrises = (int)((s.charAt(5))-'0');
		for(int i=6;i<6+(nbPrises*4);i+=4){
			int typePrise = (int)((s.charAt(i+1))-'0');  // i+1 car il faut sauter le 'p'
			Point prise = new Point((int)(s.charAt(i+2)-'0'),(int)(s.charAt(i+3)-'0'));
			if (typePrise == Piece.ROI){
				plateauJeu.setPlateauPieces(prise.x, prise.y, new PieceRoi());
				suedois.add(0, new PieceRoi(prise.x, prise.y));
			}
			if (typePrise == Piece.MOSCOVITE){
				plateauJeu.setPlateauPieces(prise.x, prise.y, new PieceMoscovite());
				moscovite.add(new PieceMoscovite(prise.x, prise.y));
			}
			if (typePrise == Piece.SUEDOIS){
				plateauJeu.setPlateauPieces(prise.x, prise.y, new PieceSuedois());
				suedois.add(new PieceSuedois(prise.x, prise.y));
			}
		}
	}
	//System.out.println("Coup annulé, pion deplacé de de ("+pionArrivee.x+","+pionArrivee.y+") à la position ("+pionDepart.x+","+pionDepart.y+")");
}


/*
 * les anciens versions des fonctions
 * //renvoie vrai si le déplacement est bien vertical ou horizontal, et qu'il n'y a aucun pion qui gène
static public Boolean deplacementCroix(Plateau plateauJeu,Point p, Piece depart){
	//si deplacement est appelé pour un point hors bord du plateau, sère pour la fonc. etatDuRoi,...
	if ( p.x < 0 || p.x>plateauJeu.getColumn()-1 || p.y<0 || p.y>plateauJeu.getLine()-1 )
		return false;
	// si le point d'arrivée est le konakis et que la pièce est un soldat, le déplacement n'est pas valide
	if (plateauJeu.getPlateauCases(p.x, p.y).type() == Case.KONAKIS && plateauJeu.getPlateauPieces(p.x, p.y).type() != Piece.ROI)
		return false;
	// si le point d'arrivée est un angle et que la pièce est un soldat, invalide
	if (plateauJeu.getPlateauCases(p.x, p.y).type() == Case.ANGLE && plateauJeu.getPlateauPieces(p.x, p.y).type() != Piece.ROI)
		return false;
	// si le déplacement est bien vertical ou horizontal
	if (depart.getX() == p.x || depart.getY() == p.y){
		// si c'est un déplacement vers le haut
		if (depart.getX() - p.x < 0){
			for(int i=depart.getX()-1;i>=p.x;i--)
				if (plateauJeu.getPlateauPieces(i,depart.getY()).type() != Piece.VIDE)
					return false;
		}
		// si c'est un déplacement vers le bas
		if (depart.getX() - p.x > 0){
			for(int i=depart.getX()+1;i<=p.x;i++)
				if (plateauJeu.getPlateauPieces(i,depart.getY()).type() !=  Piece.VIDE)
					return false;
		}
		// si c'est un déplacement vers la droite
		if (depart.getY() - p.y < 0){
			for(int j=depart.getY()+1;j<=p.y;j++)
				if (plateauJeu.getPlateauPieces(depart.getX(), j).type() != Piece.VIDE)
					return false;
		}
		// si c'est un déplacement vers la gauche
		if (depart.getY() - p.y > 0){
			for(int j=depart.getY()-1;j>=p.y;j--)
				if (plateauJeu.getPlateauPieces(depart.getX(), j).type() != Piece.VIDE)
					return false;
		}
	}
	return true;
}


 */
/*
 * if (coupValide())
 * 	jouerCoup();
 * 
 * 
 */

}