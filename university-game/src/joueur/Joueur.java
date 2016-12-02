package joueur;

import java.awt.Point;

import network.client.Receive;
import network.client.Send;
import moteur.Plateau;

public class Joueur implements InterfaceJoueur
{
	protected boolean estJoueur;
	protected Boolean camp;
	protected int niveau;
	protected int experience;
	protected String pseudo;
	protected Point pionCourant; // d�signe le pion s�lectionn�, pour que l'interface graphique affiche
				// les coups possibles � partir du pion
				// (-1,-1) => aucun pion s�lectionn�

	public static Boolean MOSCOVITE = false;
	public static Boolean SUEDOIS = true;
	
	/*
	 * fonctionnement de l'exp�rience : 
	 * 
	 * il y aura 30 niveaux. Aux niveaux 0, 6, 12, 18, 24, 30, le joueur obtient les attaques de niveau 1, 2, 3, 4, 5, 6.
	 *Au d�but de la partie, il y a un random sur une attaque de chaque niveau disponible : au niveau 12 on a donc 3 attaques � utiliser.
	 *
	 * xp n�cessaire pour chaque Niveau : xp = N�
	 * 
	 * - Chaque prise donne de l'xp : suivant la diff�rence de niveau, �a peut donner un bonus d'xp � la prise(pas un malus) 10 xp par prise peut �tre une base
	 * - Diff�rence entre les niveaux : (N1 + N2)/3 (donc au pire �a rajoute 10 points d'xp entre un niveau 1 et 30, ce qui
	 * double seulement l'xp normalement gagn�e � chaque prise)
	 *
	 * En fin de partie, on gagne l'xp de 3 prises normales (donc 30) + la diff de niveau (et pas divis� par 3, l�)
	 * 
	 * 
	 * 
	 * PENSER � ne pas afficher l'exp�rience en cas de niveau 30 : la valeur ne veut alors rien dire. 
	 */

	public boolean estJoueur(){
		return estJoueur;
	}
	
	public int getNiveau(){
		return niveau;
	}

	public void setNiveau(int i){
		niveau = i;
	}
	
	public int getExperience(){
		return experience;
	}

	public void setExperience(int i){
		experience = i;
	}
	
	public String getPseudo(){
		return pseudo;
	}
	
	@Override
	public void setPseudo(String s) {
		pseudo = s;
		
	}
	
	public Boolean getCamp(){
		return camp;
	}
	
	public void setCamp(boolean b){
		camp = b;
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

	public Deplacement coupAJouer(Plateau P){return new Deplacement(new Point(0,0), new Point(0,0));}

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
	public boolean DB_estAdmin(int i) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int DB_getExperience(String s) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int DB_getNiveau(String s) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String DB_getPseudo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int DB_setExperience(int exp) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int DB_setNiveau(int lvl) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int sendAction(Point p) {
		// TODO Auto-generated method stub
		return 0;
	}

}