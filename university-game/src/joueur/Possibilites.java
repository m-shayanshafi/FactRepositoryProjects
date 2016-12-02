package joueur;

import java.util.List;
import java.util.LinkedList;
import java.awt.Point;
import moteur.*;
import pieces.*;


class Possibilites {
	private List <Point>list;
	private int nombre;
	//Remarque: pour l'instant cette fonction est naive, elle peut etre optimiser plus tard pour donner un ordre 
	//aux Points possibles qui pourrais optimiser le performance (amelioration avec IA)
	public Possibilites (Plateau P, Piece piece){
		list = new LinkedList<Point>();
		MonPetitMoteur miniMoteur = new MonPetitMoteur();
		int i;
		for (i = piece.getLigne();i<P.getColumn();i++){
			Point tmp = new Point(i,piece.getColonne());
			if ( miniMoteur.coupValideGeorges(P,tmp,piece)){
				//System.out.println("possibilité trouvé à la position "+i+" "+piece.getY());
				list.add(tmp);
				nombre++;
			}
			else {
				//System.out.println("point "+tmp.x+" "+tmp.y+" pas possible");
			}
		}
		for (i = piece.getLigne();i>=0;i--){
			Point tmp = new Point(i,piece.getColonne());
			if ( miniMoteur.coupValideGeorges(P,tmp,piece)){
				//System.out.println("possibilité trouvé à la position "+i+" "+piece.getY());
				list.add(tmp);
				nombre++;
			}
			else {
				//System.out.println("point "+tmp.x+" "+tmp.y+" pas possible");
			}
		}
		for (i = piece.getColonne();i<P.getLine();i++){
			Point tmp = new Point(piece.getLigne(),i);
			if ( miniMoteur.coupValideGeorges(P,tmp,piece)){
				//System.out.println("possibilité trouvé à la position "+piece.getX()+" "+i);
				list.add(tmp);
				nombre++;
			}
			else {
				//System.out.println("point "+tmp.x+" "+tmp.y+" pas possible");
			}
		}
		for (i = piece.getColonne();i>=0;i--){
			Point tmp = new Point(piece.getLigne(),i);
			if ( miniMoteur.coupValideGeorges(P,tmp,piece)){
				//System.out.println("possibilité trouvé à la position "+piece.getX()+" "+i);
				list.add(tmp);
				nombre++;
			}
			else {
				//System.out.println("point "+tmp.x+" "+tmp.y+" pas possible");
			}
		}		
	}
	
	public int getNombre (){
		return nombre;
	}
	public void Afficher() {
		if ( nombre==0){
			System.out.println("table de possibilités VIDE");
		}
		for (int i=0;i<nombre;i++){
			System.out.println("Possibilité n."+i+" a la position ("+list.get(i).x+","+list.get(i).y+")");
		}
	}
	
	public Point getPoss (int i){
		return list.get(i);
	}


}
















