package joueur;

import java.awt.Point;

import moteur.Plateau;

import cases.Case;
import pieces.*;

public class AlgoIA {
	//pour les testes
	public int nbrSuedois,nbrMoscovite,nbrBords,etatroi;
	public Piece ROI = new PieceRoi();
	//fin pour les testes
	public static final int MAX_VALUE=Integer.MAX_VALUE;	//l'infini positive
	public static final int MIN_VALUE=Integer.MIN_VALUE;	//l'infini negative
	//les constantes concernant la partie evaluation du plateau
	public final int VALEUR_MAX_ETAT=1000;
	public final int POIDS_MOSCOVITE=15;//le poids d un moscovite qui entour un pion
	public final int POIDS_SUEDOIS = 5;//le poids d un suedois qui entour un pion
	public final int POIDS_BORD=10;//le poids du fait qu'un pion soit au bord du plateau
	public final int POIDS_MENACE_MOSCOVITE=5;//le poids d un moscovite qui menace un pion
	public final int GAGNE = MAX_VALUE;//GAGNE de l'equipe joueuer
	public final int PERD = MAX_VALUE;//PERD de l'equipe joueuer
	public final int DEBUT_DU_JEU=4;//le nombre qui determine si on est toujours au debut du jeu
	public final int MILIEU_DU_JEU=4;//le nombre qui determine si on est au milieu du jeu
	public final int FIN_DU_JEU=5;//le nombre qui determine si on est � la fin du jeu
	public final int DIFF_CRITIQUE=10;
	public final int POIDS_MAJ_MOSCOVITE=25;
	//--------------------------------------------------------
	//================================== alphaBeta =========================================
	public Deplacement alphaBeta(Plateau P,EquipeSuedois suedois,EquipeMoscovite moscovite, int profondeur, 
			int alpha, int beta, Point pointPrec, boolean estSuedois, boolean MIN) {
		Deplacement resultat;
		Deplacement meilleur = new Deplacement();
		Equipe equipeJoueur;
		MonPetitMoteur moteur = new MonPetitMoteur();
		if ( profondeur == 0 || moteur.partieFinie(P) ){
			int evaluation;
			if (estSuedois)
				evaluation = etatDuJeuMoscovite(P,suedois,moscovite);
			else 
				evaluation = etatDuJeuSuedois(P,suedois,moscovite);	
			resultat = new Deplacement(null,pointPrec,evaluation);
			System.out.println("MIN : "+MIN+" l'evaluation de l'etat donne le score :"+resultat.getEtat());
			return resultat;
		}
		if ( estSuedois ){
			equipeJoueur = new EquipeSuedois(suedois);
		}
		else {
			equipeJoueur = new EquipeMoscovite(moscovite);
		}
		if ( MIN ){
			//System.out.println("=======================c est le MIN qui joue, profondeur "+profondeur);
			meilleur.setEtat(MAX_VALUE);
			//parcourir tous les pions et apeller Possibilites, le constructeur construit le tableau de possibilites
			for (int i=0; i<equipeJoueur.getTaille();i++){
				Possibilites poss = new Possibilites(P,equipeJoueur.get(i));
				//poss.Afficher();
				for (int j=0;j<poss.getNombre();j++){
					//System.out.println("possibilit� en cours de traitement a la position ("+poss.getPoss(j).x+","+poss.getPoss(j).y+")");
					Point dep = new Point(equipeJoueur.get(i).getLigne(),equipeJoueur.get(i).getColonne());
					Point arr = new Point(poss.getPoss(j).x,poss.getPoss(j).y);
					//System.out.println("dans alphaBeta "+dep.x+","+dep.y);
					Deplacement deplacement = new Deplacement(dep,arr);
					String s=new String("");
					s= moteur.jouerCoup(P,suedois,moscovite,deplacement,estSuedois);
					resultat = alphaBeta(P,suedois,moscovite,profondeur-1,alpha,beta,poss.getPoss(j),!estSuedois,!MIN);
					resultat.setPointDepart(dep);
					resultat.setPointArrivee(arr);
					//pas propre, � changer
					//resultat.setEtat(resultat.getEtat()-50);
					//
					moteur.annuler(P, suedois, moscovite, s);//annuler le coup jou�
					if (resultat.getEtat() < meilleur.getEtat()){
						meilleur.setEtat(resultat.getEtat());
						meilleur.setPointDepart(resultat.getPointDepart());
						meilleur.setPointArrivee(resultat.getPointArrivee());
					}
					if ( alpha >= meilleur.getEtat())
						return meilleur;
					if ( meilleur.getEtat() < beta )
						beta = meilleur.getEtat();
					
				}
			}
		}
		else { // MAX
			meilleur.setEtat(MIN_VALUE);
			//System.out.println("=======================c est le MAX qui joue, profondeur "+profondeur);
			//parcourir tous les pions et apeller Possibilites, le constructeur construit le tableau de possibilites
			for (int i=0; i<equipeJoueur.getTaille();i++){
				Possibilites poss = new Possibilites(P,equipeJoueur.get(i));
				//poss.Afficher();
				for (int j=0;j<poss.getNombre();j++){
					//System.out.println("possibilit� en cours de traitement a la position ("+poss.getPoss(j).x+","+poss.getPoss(j).y+")");
					Point dep = new Point(equipeJoueur.get(i).getLigne(),equipeJoueur.get(i).getColonne());
					Point arr = new Point(poss.getPoss(j).x,poss.getPoss(j).y);
					//System.out.println("dans alphaBeta "+dep.x+","+dep.y);
					Deplacement deplacement = new Deplacement(dep,arr);
					String s=new String("");
					s= moteur.jouerCoup(P,suedois,moscovite,deplacement,estSuedois);
					resultat = alphaBeta(P,suedois,moscovite,profondeur-1,-beta,-alpha,poss.getPoss(j),!estSuedois,!MIN);
					resultat.setPointDepart(dep);
					resultat.setPointArrivee(arr);
					//pas propre, � changer
					//resultat.setEtat(resultat.getEtat()-50);
					//
					moteur.annuler(P, suedois, moscovite, s);//annuler le coup jou�
					if (resultat.getEtat() > meilleur.getEtat()){
						meilleur.setEtat(resultat.getEtat());
						meilleur.setPointDepart(resultat.getPointDepart());
						meilleur.setPointArrivee(resultat.getPointArrivee());
					}
					if ( meilleur.getEtat() >= beta )
						return meilleur;
					if ( meilleur.getEtat() > alpha )
						alpha = meilleur.getEtat();
				}
			}
					
		}		
		return meilleur;
	}
	//=========================================== etatDuJeuSuedois ================================================
	public int etatDuJeuSuedois ( Plateau P, EquipeSuedois suedois, EquipeMoscovite moscovite ){
		/*==========================Remarques=========================:
		 * prend en compte la difference entre le nombre de pions dans les deux equipes
		 * si il reste pas trop de pions, on doit traiter la situation differement
		 * l etat du roi est la premiere chose qu on considere
		*/
		int diff,resultat,etatRoi;
		etatRoi = etatDuRoi(P,suedois,moscovite);
		//System.out.println("Etat du Roi est : "+etatRoi);
		if (etatRoi == PERD)//le roi est captur�
			return PERD;
		else if (etatRoi == GAGNE)//le roi est arriv� �un des buts
			return GAGNE;
		resultat = etatRoi*10 + suedois.getTaille()*5 - moscovite.getTaille()*5;//� verifier selon les testes
		//la difference entre le nombre de pions dans chaque �quipe, il sera 8 au debut de la partie(16-8)
		diff = moscovite.getTaille()-suedois.getTaille();
		if ( diff > DIFF_CRITIQUE ){
			//grande difference entre les moscovites et les suedois, on supprime POIDS_MAJ_MOSCOVITE points du score des suedois
			resultat-=POIDS_MAJ_MOSCOVITE;
		}
		return resultat;	//� modifier
	}
	//=========================================== etatDuJeuMoscovite ================================================
	public int etatDuJeuMoscovite ( Plateau P, EquipeSuedois suedois, EquipeMoscovite moscovite ){
		int resultat,etatRoi,diff;
		etatRoi = etatDuRoi(P,suedois,moscovite);
		if (etatRoi == GAGNE)//le roi est sur le but, du coup les moscovites ont perdus
			return PERD;
		else if (etatRoi == PERD )
			return GAGNE;
		resultat = -etatRoi*10 -suedois.getTaille()*5 +moscovite.getTaille()*8;//� verifier selon les testes
		//la difference entre le nombre de pions dans chaque �quipe, il sera 8 au debut de la partie(16-8)
		diff = moscovite.getTaille()-suedois.getTaille(); 
		if ( diff > DIFF_CRITIQUE ){
			//grande difference entre les moscovites et les suedois, on ajoute POIDS_MAJ_MOSCOVITE points du score des suedois
			resultat += POIDS_MAJ_MOSCOVITE;
		}
		return resultat;
	}
	//========================================== etatDuRoi ===============================================
	public int etatDuRoi ( Plateau P, EquipeSuedois suedois, EquipeMoscovite moscovite ){
		Piece roi = new PieceRoi(suedois.get(0));
		if (roi.getLigne() == 0 && roi.getColonne()==0 ){//le roi est sur l'angle 0,0
			return(GAGNE);
		}
		else if (roi.getLigne() == 8 && roi.getColonne()==0 ){//le roi est sur l'angle 0,0
			return(GAGNE);
		}
		else if (roi.getLigne() == 0 && roi.getColonne()==8 ){//le roi est sur l'angle 0,0
			return(GAGNE);
		}
		else if (roi.getLigne() == 8 && roi.getColonne()==8 ){//le roi est sur l'angle 0,0
			return(GAGNE);
		}
		int nombreMoscovite = 0;
		int nombreSuedois=0;
		int nombreBord = 0;
		int nombreMoscoviteMenace=0;
		MonPetitMoteur moteur = new MonPetitMoteur();
		//analyse de la position du roi
		//--------------les colonnes
		if (roi.getLigne()==P.getLine()-1 ){//le roi est sur le bord de droite
			nombreBord++;
			if ( P.getPlateauPieces(roi.getLigne()-1,roi.getColonne()).type() == Piece.MOSCOVITE )
				nombreMoscovite++;
			else if ( P.getPlateauPieces(roi.getLigne()-1,roi.getColonne()).type() == Piece.SUEDOIS )
				nombreSuedois++;
		}
		else if (roi.getLigne()==0 ){//le roi est sur le bord de gauche
			nombreBord++;
			if ( P.getPlateauPieces(roi.getLigne()+1,roi.getColonne()).type() == Piece.MOSCOVITE )
				nombreMoscovite++;
			else if ( P.getPlateauPieces(roi.getLigne()+1,roi.getColonne()).type() == Piece.SUEDOIS )
				nombreSuedois++;
		}
		else {//au milieu du tableau par rapport aux 2 colonnes ( sur aucun des deux bords verticales )
			if ( P.getPlateauPieces(roi.getLigne()+1,roi.getColonne()).type() == Piece.MOSCOVITE )
				nombreMoscovite++;
			else if ( P.getPlateauPieces(roi.getLigne()+1,roi.getColonne()).type() == Piece.SUEDOIS )
				nombreSuedois++;
			else if (P.getPlateauPieces(roi.getLigne()+1,roi.getColonne()).type() == Piece.VIDE && P.getPlateauCases(roi.getLigne()+1,roi.getColonne()).type() == Case.KONAKIS)
				nombreBord++;
			if ( P.getPlateauPieces(roi.getLigne()-1,roi.getColonne()).type() == Piece.MOSCOVITE )
				nombreMoscovite++;
			else if ( P.getPlateauPieces(roi.getLigne()-1,roi.getColonne()).type() == Piece.SUEDOIS )
				nombreSuedois++;
			else if (P.getPlateauPieces(roi.getLigne()-1,roi.getColonne()).type() == Piece.VIDE && P.getPlateauCases(roi.getLigne()-1,roi.getColonne()).type() == Case.KONAKIS)
				nombreBord++;
		}
		//------------les lignes
		if (  roi.getColonne()==P.getColumn()-1 ){//le roi est sur un des deux bord de lignes
			nombreBord++;
			if ( P.getPlateauPieces(roi.getLigne(),roi.getColonne()-1).type() == Piece.MOSCOVITE )
				nombreMoscovite++;
			else if ( P.getPlateauPieces(roi.getLigne(),roi.getColonne()-1).type() == Piece.SUEDOIS )
				nombreSuedois++;
		}
		else if (  roi.getColonne() == 0 ){
			nombreBord++;
			if ( P.getPlateauPieces(roi.getLigne(),roi.getColonne()+1).type() == Piece.MOSCOVITE )
				nombreMoscovite++;
			else if ( P.getPlateauPieces(roi.getLigne(),roi.getColonne()+1).type() == Piece.SUEDOIS )
				nombreSuedois++;
		}
		else {
			if ( P.getPlateauPieces(roi.getLigne(),roi.getColonne()-1).type() == Piece.MOSCOVITE )
				nombreMoscovite++;
			else if ( P.getPlateauPieces(roi.getLigne(),roi.getColonne()-1).type() == Piece.SUEDOIS )
				nombreSuedois++;
			else if (P.getPlateauPieces(roi.getLigne(),roi.getColonne()-1).type() == Piece.VIDE && P.getPlateauCases(roi.getLigne(),roi.getColonne()-1).type() == Case.KONAKIS)
				nombreBord++;
			if ( P.getPlateauPieces(roi.getLigne(),roi.getColonne()+1).type() == Piece.MOSCOVITE )
				nombreMoscovite++;
			else if ( P.getPlateauPieces(roi.getLigne(),roi.getColonne()+1).type() == Piece.SUEDOIS )
				nombreSuedois++;
			else if (P.getPlateauPieces(roi.getLigne(),roi.getColonne()+1).type() == Piece.VIDE && P.getPlateauCases(roi.getLigne(),roi.getColonne()+1).type() == Case.KONAKIS)
				nombreBord++;
		}
		/*
		//analyser les menaces possibles par les moscovites
		for (int i=0;i<moscovite.getTaille();i++){
			if (moteur.coupValideGeorges(P,new Point(roi.x+1,roi.y),moscovite.get(i)))
				nombreMoscoviteMenace++;
			if (moteur.coupValideGeorges(P,new Point(roi.x-1,roi.y),moscovite.get(i)))
				nombreMoscoviteMenace++;
			if (moteur.coupValideGeorges(P,new Point(roi.x,roi.y+1),moscovite.get(i)))
				nombreMoscoviteMenace++;
			if (moteur.coupValideGeorges(P,new Point(roi.x,roi.y-1),moscovite.get(i)))
				nombreMoscoviteMenace++;
		}
		*/
		//evaluation de l etat du roi selon l analyse precedent du terrain
		if (P.getPlateauCases(roi.getLigne(),roi.getColonne()).type() != Case.KONAKIS){//le roi est sur le KONAKIS, il pert pas
			if (nombreMoscovite == 4)
				return MIN_VALUE;//jeu fini, car le roi est entour� par 4 moscovites
			if ( nombreMoscovite == 3 && nombreBord >0 )
				return MIN_VALUE;//jeu fini, roi entour� par 3 moscovites et un bord(3 mosco. et konakis �galement)
			if ( nombreMoscovite == 2 && nombreBord>1 )
				return MIN_VALUE;//jeu fini, roi entour� par 2 moscovites et 2 bords(2 mosco. et konakis et bord
		}
		int resultat=0;//la situation est �valu� sur 100, toutes les menaces sont supprim�s de ce nombre
		
		//un truc � rajouter c'est l augmentation de la menace d un bord si il y a des moscovites autour du roi
		//et de diminuer ces menaces si le roi n'est pas entour� par les moscovites
		resultat -= nombreMoscovite*POIDS_MOSCOVITE;
		resultat += nombreSuedois*POIDS_SUEDOIS;
		resultat -= nombreBord*POIDS_BORD;
		resultat -= nombreMoscoviteMenace*POIDS_MENACE_MOSCOVITE;
		//pour les testes
		nbrSuedois=nombreSuedois;
		nbrMoscovite=nombreMoscovite;
		nbrBords=nombreBord;
		etatroi = resultat;
		ROI.setColonne(roi.getColonne());
		ROI.setLigne(roi.getLigne());
		//fin pour les testes
		return resultat;
	}
}
