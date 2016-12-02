package gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


import joueur.InterfaceJoueur;
import joueur.Joueur;

import pieces.Piece;
import pieces.PieceMoscovite;
import pieces.PieceRoi;
import pieces.PieceSuedois;
import pieces.PieceVide;

import moteur.Plateau;

@SuppressWarnings("serial")
public class AireDessin extends JPanel{
	public static int themeChoisi = 2;
	

	public static String pathImgMiniMap = "bin/gui/img/mini-map.PNG";
	
	
	 /*THEME 1*/
    public static String plateauVide = "bin/gui/img/plateau-iso.PNG";
    public static String masquePlateau = "bin/gui/img/plateau-iso-masque.PNG";
    public static String caseSelectionne = "bin/gui/img/case-selected.PNG";
    
    public static String imgRoiT1 = "bin/gui/img/plateau-iso-masque.PNG";
    public static String imgSuedoisT1 = "bin/gui/img/plateau-iso-masque.PNG";
    public static String imgMoscoviteT1 = "bin/gui/img/plateau-iso-masque.PNG";
    
    public static int largeur = 1082;
    public static int hauteur = 579;
    public static int offsetHauteur = 7;
    public static int largeurTile = 120;
    public static int hauteurTile = 60;
    
    /*THEME 2*/
    public static String plateauVideT2 = "bin/gui/img/theme2/plateau.png";
    public static String masquePlateauT2 = "bin/gui/img/theme2/plateau-masque.png";
    public static String caseSelectionneT2 = "bin/gui/img/theme2/case-selected.png";
    
    public static String imgRoiT2 = "bin/gui/img/theme2/viking.png";
    public static String imgSuedoisT2 = "bin/gui/img/theme2/viking.png";
    public static String imgMoscoviteT2 = "bin/gui/img/theme2/moscovite.png";

	
	public static int largeurT2 = 1080;
	public static int hauteurT2 = 640;
	public static int offsetHauteurT2 = 21;
	public static int offsetLargeurT2 = 23-22;
	public static int largeurTileT2 = 116;
	public static int hauteurTileT2 = 57;
	public static Color couleurFond = Color.gray;
	/**/
	private int largeurTheme;
	private int hauteurTheme;
	
	private ArrayList<Color> tableauCouleur;
	private BufferedImage imgPlateau;
	private BufferedImage imgMasquePlateau;
	private BufferedImage imgCaseSelectionne;
	private Image imgSuedois;
	private Image imgMoscovite;
	private BufferedImage imagePosition;
	private BufferedImage imgMiniMap;

	private Plateau plateau;
	private boolean isCaseSelect;
	private Point coordCaseSelect;
	private boolean isPlayerSelect;
	private Point coordPlayerSelect;
	private PlateauDessin plateauDessin;
	
	
	//TODO modif simo
	BufferedImage dimg;
	
	
	public AireDessin(Plateau p) 
	{
		this.plateau = p;
		isCaseSelect = false;
		isPlayerSelect = false;
		tableauCouleur = new ArrayList<Color>();
		plateauDessin = new PlateauDessin();
		coordCaseSelect = new Point();
		coordPlayerSelect = new Point();
		try {
			if(themeChoisi==1) {
				imgMasquePlateau = ImageIO.read(new File(masquePlateau));
				imgCaseSelectionne = this.makeColorTransparent(caseSelectionne, new Color(255,0,255));
				imgPlateau = this.makeColorTransparent(plateauVide, new Color(255,0,255));
				largeurTheme = largeur; hauteurTheme = hauteur;
			} else if (themeChoisi == 2) {
				imgMasquePlateau = ImageIO.read(new File(masquePlateau));
				imgPlateau = ImageIO.read(new File(plateauVideT2));
				//imgCaseSelectionne = this.makeColorTransparent(caseSelectionneT2, new Color(255,0,255));
				imgCaseSelectionne = ImageIO.read(new File(caseSelectionneT2));
				imgSuedois = ImageIO.read(new File(imgSuedoisT2));
				imgMoscovite = ImageIO.read(new File(imgMoscoviteT2));
				largeurTheme = largeurT2; hauteurTheme = hauteurT2;
				imgMiniMap=ImageIO.read(new File(pathImgMiniMap));
				imagePosition =  new BufferedImage(largeurTheme, hauteurTheme, BufferedImage.TYPE_INT_ARGB); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//--- initialisation du tableau des couleurs des tiles
		int v=255;
		for(int x=0;x<9;x++) {
			for(int y=0;y<9;y++) {
				tableauCouleur.add(new Color(0,v,0));
				v = v-2;
			}
		}
		
		
		//TODO modif simo
		dimg = new BufferedImage(imgMasquePlateau.getWidth(), imgMasquePlateau.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		
	}
	
	public void paintComponent(Graphics g)
	{
		this.setSize(largeurTheme, hauteurTheme);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);

		g2.setPaint(couleurFond);
		//dessin du plateau
		g2.fillRect(0, 0, largeurTheme, hauteurTheme);
		g2.setPaint(Color.black);
		g2.drawImage(imgPlateau, 0, 0, largeurTheme, hauteurTheme,this);

		//dessin de la case selectionn�
		if (isCaseSelect) {
			g2.drawImage(imgCaseSelectionne,coordCaseSelect.x, 
					coordCaseSelect.y, largeurTileT2, hauteurTileT2,this);
			isCaseSelect = false;
		}
		if(isPlayerSelect) {
			g2.drawImage(imgCaseSelectionne,coordPlayerSelect.x, 
					coordPlayerSelect.y, largeurTileT2, hauteurTileT2,this);
		}
		//dessin minimap - x =largeurTheme-largeurTheme/6
		g2.setPaint(Color.white);
		g2.drawImage(imgMiniMap,largeurTheme-180,5,165,163,this);
		g2.drawString("Tour :", largeurTheme-180, 178);
		//dessin des piece
		Piece n;
		for(int x=0;x<9;x++) {
			for(int y=0;y<9;y++) {
				n = plateau.getPlateauPieces(x, y);
				if(n.getClass() == PieceSuedois.class) {
					g2.drawImage(imgSuedois, plateauDessin.getCoordonneCase(x, y).x+27,
							plateauDessin.getCoordonneCase(x, y).y-20, 55, 63,this);
					g2.setPaint(Color.black);
					g2.fillOval(largeurTheme-180+x*18,5+18*y ,15,15);
				}else if(n.getClass() == PieceMoscovite.class) {
					g2.drawImage(imgMoscovite, plateauDessin.getCoordonneCase(x, y).x+27,
							plateauDessin.getCoordonneCase(x, y).y-20, 55, 63,this);
					g2.setPaint(Color.orange);
					g2.fillOval(largeurTheme-179+x*18,5+18*y ,15,15);
				}
			}
		}
		
		
	}
	
	public static BufferedImage makeColorTransparent(String ref, Color color) throws IOException 
	{   
		BufferedImage image = ImageIO.read(new File(ref)); 
		BufferedImage dimg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);  
		Graphics2D g = dimg.createGraphics();  
		g.setComposite(AlphaComposite.Src);  
		g.drawImage(image, null, 0, 0);  
		g.dispose();  
		for(int i = 0; i < dimg.getHeight(); i++) {  
			for(int j = 0; j < dimg.getWidth(); j++) {  
		        if(dimg.getRGB(j, i) == color.getRGB()) {  
		             dimg.setRGB(j, i, 0x8F1C1C);  
		        }  
		    }  
		}  
		return dimg;  
	}  
	
	public Color getColorPosition(int x, int y, String img) throws IOException
	{
		
		Graphics2D g = dimg.createGraphics();
		g.setComposite(AlphaComposite.Src);  
		g.drawImage(imgMasquePlateau, null, 0, 0);  
		Color n = new Color(dimg.getRGB(x, y));
		return n;
	}
	
	public Color getColorPosition(int x, int y, Image img) throws IOException
	{
		Color n = new Color(imgMasquePlateau.getRGB(x, y));
		return n;
	}
	
	/* fonction changer une case selectionne par un autre skin
	 * 
	 * */
	public void changeCase(Point p){
		if (p!=null) {	
			coordCaseSelect.x = plateauDessin.getCoordonneCase(p.x, p.y).x;
			coordCaseSelect.y = plateauDessin.getCoordonneCase(p.x, p.y).y;
			isCaseSelect = true;
			this.repaint();
		}
	}
	
	public void bougerPiece() {
		
	}
	/* retourne si une piece du joueur courant est présente
	 * 
	 */
	public boolean isPiece(int x, int y, InterfaceJoueur j) {
		if( plateau.getPlateauPieces(x, y).getClass() == PieceVide.class ) {
			return false;
		}
		if(j.getCamp() == Joueur.MOSCOVITE) {
			if( plateau.getPlateauPieces(x, y).getClass() == PieceMoscovite.class ) {
				return true;
			}
		} else {
			if( plateau.getPlateauPieces(x, y).getClass() == PieceSuedois.class 
					|| plateau.getPlateauPieces(x, y).getClass() == PieceRoi.class ) {
				return true;
			}
		}
		return false;
	}
	
	/*----------------------*/
	public int getLargeur()
	{
		return this.largeurT2;
	}
	
	public int getHauteur()
	{
		return this.hauteurT2;
	}

	
	public void setisPlayerSelect(boolean t)
	{
		this.isPlayerSelect = t;
	}
	
	public void setCoordPlayerSelect(Point p)
	{
		if (p!=null) {	
			coordPlayerSelect.x = plateauDessin.getCoordonneCase(p.x, p.y).x;
			coordPlayerSelect.y = plateauDessin.getCoordonneCase(p.x, p.y).y;
			this.setisPlayerSelect(true);
			this.repaint();
		}
	}
	
	public ArrayList<Color> getTableauCouleurPlateau()
	{
		return this.tableauCouleur;
	}
	
	public BufferedImage getImgMasquePlateau()
	{
		return imgMasquePlateau;
	}
	
	/************/
	public class PlateauDessin
	{
		private Point tblCentreCases[][];
		
		public PlateauDessin()
		{
			tblCentreCases = new Point[9][9];
			Point c=new Point();
			switch(themeChoisi) {
				case 1:
					c.x = largeur/2;
					c.y = hauteurTile/2+offsetHauteur;
					for(int y=0;y<9;y++) {
						for(int x=0;x<9;x++) {
							tblCentreCases[x][y]= new Point(c.x,c.y);
							c.y += hauteurTile/2;
							c.x += largeurTile/2;
						}
						c.y = tblCentreCases[1][y].y;
						c.x = tblCentreCases[1][y].x-largeurTile;
					}
					break;
				default:
					c.x = largeurT2/2+offsetLargeurT2;
					c.y = hauteurTileT2/2+offsetHauteurT2;
					for(int y=0;y<9;y++) {
						for(int x=0;x<9;x++) {
							tblCentreCases[x][y]= new Point(c.x,c.y);
							c.y += hauteurTileT2/2;
							c.x += largeurTileT2/2;
						}
						c.y = tblCentreCases[1][y].y;
						c.x = tblCentreCases[1][y].x-largeurTileT2;
					}
					break;
			}
			
		}
		
		/* fonction qui r�cup�re les coordonn� affich� de la case
		 * @param x : colonne de la case dans la matrice 
		 * @param y : ligne de la case dans la matrice 
		 * @return : point du coin haut gauche de l'image de la case
		 */
		public Point getCoordonneCase(int x, int y) 
		{
			Point r = new Point();
			switch(themeChoisi) {
				case 1:
					r.x = tblCentreCases[x][y].x-(largeurTile/2);
					r.y = tblCentreCases[x][y].y-(hauteurTile/2);
					break;
				default:
					r.x = tblCentreCases[x][y].x-(largeurTileT2/2);
					r.y = tblCentreCases[x][y].y-(hauteurTileT2/2);
					break;
			}
			return r;
		}
		//---
	}
	//---
}
