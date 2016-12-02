package joueur;

import java.awt.Point;
import java.util.LinkedList;
import java.util.ListIterator;

import moteur.*;
import pieces.*;

public class EquipeMoscovite implements Equipe{
	private LinkedList <Piece> list;
	private int taille;
	public EquipeMoscovite () {
		
	}
	public EquipeMoscovite (Plateau P) {
		taille = 0;
		list = new LinkedList<Piece>();
		for (int i=0;i<P.getLine();i++){
			for (int j=0;j<P.getColumn();j++){
				if ( P.getPlateauPieces(i,j).type() == Piece.MOSCOVITE){
					list.add(new PieceMoscovite(i,j));
					taille++;
				}
			}
		}
	}
	public EquipeMoscovite (EquipeMoscovite equipe){
		taille = 0;
		list = new LinkedList<Piece>();
		for ( int i=0 ; i<equipe.getTaille(); i++){
			list.add(new PieceMoscovite(equipe.get(i)));
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
	public void Afficher (){
		for ( int i=0 ; i<getTaille();i++){
			System.out.println("Moscovite n."+i+" est à la position ("+list.get(i).getLigne()+","+list.get(i).getColonne()+")");
		}
	}
	public void remove (int i){
		list.remove(i);
		taille--;
	}
}
