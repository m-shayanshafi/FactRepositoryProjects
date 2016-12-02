package joueur;

import java.awt.Point;

import network.client.Receive;
import network.client.Send;
import moteur.*;

public class IAMoscovite extends Joueur {
	public static final int MAX_VALUE=Integer.MAX_VALUE;	//l'infini positive
	public static final int MIN_VALUE=Integer.MIN_VALUE;	//l'infini negative
	//les constantes concernant la partie évaluation du plateau
	public final int VALEUR_MAX_ETAT=1000;
	public final int POIDS_MOSCOVITE=20;//le poids d un moscovite qui entour un pion
	public final int POIDS_BORD=10;//le poids du fait qu'un pion soit au bord du plateau
	public final int POIDS_MENACE_MOSCOVITE=5;//le poids d un moscovite qui menace un pion
	public final int DEBUT_DU_JEU=3;//le nombre qui determine si on est toujours au debut du jeu
	public final int MILIEU_DU_JEU=4;//le nombre qui determine si on est au milieu du jeu
	public final int FIN_DU_JEU=5;//le nombre qui determine si on est à la fin du jeu
	public final int DIFF_CRITIQUE=10;
	public final int POIDS_MAJ_MOSCOVITE=25;
	//--------------------------------------------------------
	private int niveau;
	
	//=================================== constructeur solo ===================================
	public IAMoscovite(String  niv)
	{
		estJoueur = false;
		camp = Joueur.MOSCOVITE;
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
	//================================ La Partie IA ================================================

	public Deplacement coupAJouer(Plateau P){
		EquipeSuedois suedois = new EquipeSuedois(P);
		EquipeMoscovite moscovite = new EquipeMoscovite(P);
		AlgoIA algo = new AlgoIA();
		return algo.alphaBeta((Plateau)P.clone(),suedois,moscovite,niveau+3,algo.MIN_VALUE,algo.MAX_VALUE,null,false,false);
	}
	@Override
	public Receive getReceive() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Send getSend() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void passageNiveau() {
		// TODO Auto-generated method stub
		
	}
}





















