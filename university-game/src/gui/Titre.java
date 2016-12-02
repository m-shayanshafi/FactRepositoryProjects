/**
 *  Titre.java
 *
 * Animation du titre dans une zone de dessin.
 *
 * @author Si-Mohamed Lamraoui
 * @date 20.05.10
 */

package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class Titre extends JComponent implements AdaptateurDessin
{

	private static final long serialVersionUID = 1L;
	private Animation anim;

	
	public Titre()
	{
		anim = new Animation(this, "/images/anim_titre2/titre", 8, 200, true);
		anim.start();
	}


	public void paintComponent(Graphics g)
	{
		Graphics2D draw = (Graphics2D) g;
		draw.drawImage(anim.get(), 0, 0, null);
	}  
}
