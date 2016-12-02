package joueur;
import java.awt.Point;

public class Deplacement {
	private Point pointDepart;
	private Point pointArrivee;
	private int etat;
	
	public Deplacement(Point _pointDepart, Point _pointArrivee){
		pointDepart = _pointDepart;
		pointArrivee = _pointArrivee;
		etat = 0;
	}
	
	public Deplacement(Point _pointDepart, Point _pointArrivee, int e){
		pointDepart = _pointDepart;
		pointArrivee = _pointArrivee;
		etat = e;
	}
	
	public Deplacement(){
		pointDepart = new Point(0,0);
		pointArrivee = new Point(0,0);
	}
	
	public Point getPointDepart(){
		return pointDepart;
	}
	
	public void setPointDepart(Point p){
		pointDepart = p;
	}
	
	public Point getPointArrivee(){
		return pointArrivee;
	}
	
	public void setPointArrivee(Point p){
		pointArrivee = p;
	}
	
	public int getEtat(){
		return etat;
	}
	
	public void setEtat(int e){
		etat = e;
	}
}
