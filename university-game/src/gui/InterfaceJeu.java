package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import joueur.InterfaceJoueur;

import moteur.MoteurMonojoueur;
import moteur.MoteurMultijoueur;
import moteur.Plateau;

public class InterfaceJeu implements Runnable{
	
	public static int largeur = 1200;
	public static int hauteur = 800;
	
	private String titre;
	private String P1;
	private String P2;
	
	private AireDessin aireDessin;
	private Plateau plateau;
	private JFrame fen;
	private JPanel container;
	
	
	
	private Point positionSouris;
	private MoteurMonojoueur myEngine;
	private MoteurMultijoueur myEngineOnNetwok;
	
	private boolean selection;
	private Point pieceSelectionne;
	private boolean deplacement;
	private boolean valide;
	
	
	/* @param titre : cheminement des titres de fenetre précédentes
	 * */
	public InterfaceJeu(String titre)
	{
		myEngine = new MoteurMonojoueur("facile",aireDessin, true);
		aireDessin = new AireDessin(myEngine.getPlateau());
		myEngine.setAireDessin(aireDessin);
		
		fen = new JFrame(titre);
		container = new JPanel();
		
		pieceSelectionne = new Point();
		this.titre = titre + "-"+P1+"vs"+P2;
			
		Thread T = new Thread(myEngine);
		T.start();
	}
	
	
	
	/////////////////// ADD
	public InterfaceJeu(String titre, boolean campJoueurLocal, String pseudoLocal,  InterfaceJoueur joueurDistant)
	{
		 myEngineOnNetwok = new MoteurMultijoueur(null, campJoueurLocal, true, pseudoLocal, joueurDistant);
		aireDessin = new AireDessin( myEngineOnNetwok.getPlateau());
		 myEngineOnNetwok.setAireDessin(aireDessin);
		
		fen = new JFrame(titre);
		container = new JPanel();
		
		pieceSelectionne = new Point();
		this.titre = titre + "-"+P1+"vs"+P2;
			
		Thread T = new Thread( myEngineOnNetwok);
		T.start();
	}
	
	
	public void run() 
	{	
		fen.setContentPane(container);
		fen.setUndecorated(true);
		fen.setForeground(aireDessin.couleurFond);
		
		container.setLayout(new BorderLayout());
		container.add(aireDessin,BorderLayout.WEST);
		
		Souris sourisMotionListener = new Souris(this);
		container.addMouseMotionListener(sourisMotionListener);
		container.addMouseListener(sourisMotionListener);
		//container.add(new JLabel("player"),BorderLayout.EAST);
		
		fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fen.setSize(aireDessin.largeurT2, hauteur);
		aireDessin.repaint();
		fen.setVisible(true);
	}
	
	
	/* Fonction qui r�cup�re la position de la case clique
	 * si une piece du joueur courant est présente la hit box est modifié
	 * @return : indice x et y de la case clique dans la matrice du plateau
	 * */
	public Point getCaseSouris() throws IOException
	{
		Color couleurCase = aireDessin.getColorPosition(positionSouris.x, 
														positionSouris.y, 
														aireDessin.getImgMasquePlateau());
		Color couleurIgnore = new Color(255,0,255);
		if (couleurCase.getRGB()==couleurIgnore.getRGB()) {
			aireDessin.repaint();
			return null;
		}
		if (positionSouris.x > aireDessin.largeurT2 || positionSouris.y > aireDessin.hauteurT2) {
			return null;
		}
		ArrayList<Color> tablCouleur = aireDessin.getTableauCouleurPlateau();
		int indexX = 0;
		int indexY = 0;
		int y=0; boolean ok=false;
		while(y<9 && !ok) {
			int x=0;
			while( x<9 && !ok) {
				if (tablCouleur.get(x+(y*9)).getGreen() == couleurCase.getGreen()) {
					indexX = x; indexY=y;
					ok = true;
				} x++;
			} y++;
		}
		return new Point(indexX,indexY);
	}
	
	/*------------------ ACCESSEUR ET MAIN*/
	
	public static void main(String args[])
	{
		SwingUtilities.invokeLater(new InterfaceJeu("try"));
	}
	
	public Point getPositionSouris()
	{
		return positionSouris;
	}
	public AireDessin getAireDessin()
	{
		return this.aireDessin;
	}
	
	public void setPositionSouris(Point s)
	{
		this.positionSouris = s;
	}
	
	public void setSelection(boolean b) 
	{
		this.selection = b;
	}
	
	public boolean getSelection() 
	{
		return this.selection;
	}
	
	public InterfaceJoueur getTour() 
	{
		return myEngine.getTour();
	}
	
	public void setPieceSelectionne(Point p)
	{
		this.pieceSelectionne.x = p.x;
		this.pieceSelectionne.y = p.y;
		aireDessin.setCoordPlayerSelect(p);
	}
	
	public Point getPieceSelectionne()
	{
		return this.pieceSelectionne;
	}
	
	public MoteurMonojoueur getEngyne()
	{
		return this.myEngine;
	}
}
