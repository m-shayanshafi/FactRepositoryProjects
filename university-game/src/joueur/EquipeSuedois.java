package joueur;

import java.awt.Point;
import java.util.LinkedList;
import java.util.ListIterator;

import moteur.*;
import pieces.*;

public class EquipeSuedois implements Equipe{
	private LinkedList <Piece> list;
	private int taille;
	//Le Roi est toujours à la case 0 de la list
	public EquipeSuedois () {
		taille = 9;
		list = new LinkedList<Piece>();
		Piece piece = new PieceRoi();
		piece.setLigne(4);
		piece.setColonne(4);
		list.add(0,piece);
		for (int i=0;i<8;i++){
			Piece p = new PieceSuedois();
			list.add(p);
		}
		(list.get(1)).setLigne(4);
		(list.get(1)).setColonne(2);
		(list.get(2)).setLigne(4);
		(list.get(2)).setColonne(3);
		(list.get(3)).setLigne(4);
		(list.get(3)).setColonne(5);
		(list.get(4)).setLigne(4);
		(list.get(4)).setColonne(6);
		(list.get(5)).setLigne(2);
		(list.get(5)).setColonne(4);
		(list.get(6)).setLigne(3);
		(list.get(6)).setColonne(4);
		(list.get(7)).setLigne(5);
		(list.get(7)).setColonne(4);
		(list.get(8)).setLigne(6);
		(list.get(8)).setColonne(4);
	}
	public EquipeSuedois (Plateau P) {
		taille = 0;
		list = new LinkedList<Piece>();
		for (int i=0;i<P.getLine();i++){
			for (int j=0;j<P.getColumn();j++){
				if ( P.getPlateauPieces(i,j).type() == Piece.SUEDOIS){
					list.add(new PieceSuedois(i,j));
					taille++;
				}
				else if (P.getPlateauPieces(i,j).type() == Piece.ROI ){
					list.add(0,new PieceRoi(i,j));
					taille++;
				}
			}
		}
	}
	public EquipeSuedois (EquipeSuedois equipe){
		taille = 0;
		list = new LinkedList<Piece>();
		for ( int i=0 ; i<equipe.getTaille(); i++){
			list.add(equipe.get(i));
			taille++;
		}
	}
	public int getTaille() {
		return taille;
	}
	public Piece get(int i){
		return list.get(i);
	}
	public void set(int i,Piece piece){
		list.set(i, piece);
	}
	public void add (Piece piece){
		list.add(piece);
		taille++;
	}
	public void add ( int i, Piece piece){
		list.add(i, piece);
		taille++;
	}
	public int find ( Point point){
		ListIterator <Piece>it = list.listIterator();
		while (it.hasNext()){
			int res = it.nextIndex();
			Piece p = it.next();
			if ( p.getLigne() == point.getX() && p.getColonne() == point.getY()){
				return res;
			}
		}
		return -1;
	}
	//=================================== Affichage ================================================
	public void Afficher (){
		//System.out.println("Le Roi est dans "+list.get(0).getX()+" "+list.get(0).getY());
		for ( int i=0 ; i<getTaille();i++){
			System.out.println("Suedois n."+i+" est à la position ("+list.get(i).getLigne()+","+list.get(i).getColonne()+")");
		}
	}
	public void remove (int i){
		list.remove(i);
		taille--;
	}
}
