/** Animation.java
 *
 * @author Si-Mohamed Lamraoui
 * @date 20.05.10
 */

package gui;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.Timer;

public class Animation
{

	public Vector<BufferedImage> clip; // tableau d'images
	public int delay; // delay entre deux images
	public int indiceImage; // indice courant dans le tableau d'images (clip)
	public boolean loop; // vrai si l'animation tourne en boucle, sinon faux
	public Timer timer;
	public AdaptateurDessin draw;

	/**
	 * Cree une animation avec des images formees de la facon suivante : 
	 *    path1.png, path2.png, path3.png, ect.
	 * Avec n, le nombre d'image de l'animation, d le temps entre deux images, 
	 * et l le booleen qui indique si l'animation tourne en boucle ou pas.
	 */
	public Animation(AdaptateurDessin a, String path, int n, int d, boolean l)
	{
		loop = l;
		draw = a;
		delay = d;
		indiceImage = 0;
		timer = new Timer(delay, new AdaptatorTimer(this));
		clip = new Vector<BufferedImage>();
		for(int i=0;i<n;i++)
			addImage(path+(i+1)+".png");
	}

	/**
	 * Avec transparence : t le parametre de transparence.
	 */
	public Animation(AdaptateurDessin a, String path, int n, int d, boolean l, float t)
	{
		loop = l;
		draw = a;
		delay = d;
		indiceImage = 0;
		timer = new Timer(delay, new AdaptatorTimer(this));
		clip = new Vector<BufferedImage>();
		for(int i=0;i<n;i++)
			addTranslucentImage(path+(i+1)+".png", i*t);
	}


	public Animation(AdaptateurDessin a, int d, boolean l)
	{
		loop = l;
		draw = a;
		delay = d;
		indiceImage = 0;
		clip = new Vector<BufferedImage>();
		timer = new Timer(delay, new AdaptatorTimer(this));
	}


	/**
	 * Retourne l'image courant de l'animation.
	 * (a utiliser dans l'aire de dessin pour afficher l'animation)
	 */
	public BufferedImage get()
	{
		return (BufferedImage) clip.elementAt(indiceImage);
	}


	/**
	 * Demarre l'animation.
	 */
	public void start()
	{
		timer.start();
	}


	/**
	 * Met en pause l'animation.
	 */
	public void stop()
	{
		timer.stop();
	}


	/**
	 * Met a zero l'animation.
	 */
	public void reset()
	{
		indiceImage = 0;
	}


	/**
	 * Ajoute une image a l'animation.
	 */
	public void addImage(String path)
	{
		try {
		BufferedImage img = ImageIO.read(getImage(path));
		clip.addElement(img);
		} 
		catch(IOException e) { System.out.println("Impossible d'ouvrir l'image <"+path+">"); }
	}


	/**
	 * Ajoute une image transparente a l'animation.
	 */
	public void addTranslucentImage(String path, float transperancy) 
	{  
		BufferedImage img = null;
		try {
		img = ImageIO.read(getImage(path));
		BufferedImage aimg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TRANSLUCENT);    
		Graphics2D g = aimg.createGraphics();  
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transperancy));  
		g.drawImage(img, null, 0, 0);  
		g.dispose();  
		clip.addElement(aimg); 
		} catch(IOException e) { System.out.println("Impossible d'ouvrir l'image <"+path+">"); }   
       }  


	/**
	 * Implement l'action qui sera execute tout les x ms par le timer et
	 * permettra de faire deffiler les images de l'animation.
	 */
	class AdaptatorTimer implements ActionListener {
		private Animation anim;
		public AdaptatorTimer(Animation a) {
			anim = a;
		}
	    public void actionPerformed(ActionEvent e) {
		anim.indiceImage++;
		if(anim.indiceImage==anim.clip.size() && anim.loop==true) {
			anim.indiceImage = 0;
		}
		else if(anim.indiceImage==anim.clip.size() && anim.loop==false) {
			anim.indiceImage--;
			anim.stop();
			return;
		}
		anim.draw.repaint();
	    }
	}

 	private URL getImage(String nom) {
        	URL cl = getClass().getResource(nom);
        	return cl;
    }
 	

}

