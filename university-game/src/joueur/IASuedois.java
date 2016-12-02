package joueur;

import java.awt.Point;
import cases.Case;
import moteur.*;
import pieces.*;

public class IASuedois extends Joueur {
	private int niveau;
	
	//=================================== constructeur solo ===================================
	public IASuedois(String niv)
	{
		estJoueur = false;
		camp = Joueur.SUEDOIS;
		pseudo = "Ordinateur";
		if ( niv == "facile")
			niveau = 0;
		else if ( niv == "normal")
			niveau = 1;
		else if ( niv == "difficile")
			niveau = 2;
		else 
			niveau = 3;	//niveau de difficulte le plus difficile possible, profondeur de l'arbre de recherche illimité
	}
	//=================================== Getteurs  ===============================================
	public boolean estJoueur(){
		return estJoueur;
	}
	
	public int getNiveau(){
		return niveau;
	}
	
	public int getExperience(){
		return experience;
	}
	public String getPseudo(){
		return pseudo;
	}
	
	public Boolean getCamp(){
		return camp;
	}
	
	public Point getPionCourant(){
		return pionCourant;
	}
	//=================================== Setteurs  ===============================================
	
	public void setNiveau(int i){
		niveau = i;
	}
	
	public void setExperience(int i){
		experience = i;
	}
	
	public void setPionCourant(int x, int y){
		pionCourant.x = x;
		pionCourant.y = y;
	}
	
	//================================== coupAJouer ========================================	
	public Deplacement coupAJouer(Plateau P){
		EquipeSuedois suedois = new EquipeSuedois(P);
		EquipeMoscovite moscovite = new EquipeMoscovite(P);
		AlgoIA algo = new AlgoIA();
		return algo.alphaBeta((Plateau)P.clone(),suedois,moscovite,niveau+3,algo.MIN_VALUE,algo.MAX_VALUE,null,true,false); //ça doit etre -infini et +infini
		//la profondeur de l'arbre de recherche à verifier selon la performance de l'algo
		//dans le niveau ultime la profondeur de l'arbre doit etre assez grande
	}
}















