package joueur;

import java.awt.Point;

import pieces.Piece;

interface Equipe {
	public int getTaille();
	public Piece get(int i);
	public void set(int i,Piece piece);
	public void add (Piece piece);
	public void add ( int i, Piece piece);
	public int find ( Point point);
	public void Afficher ();
	public void remove (int i);
}