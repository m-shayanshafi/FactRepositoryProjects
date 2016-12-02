/**
 * AireDessin.java
 * 
 * Classe de test animation.
 * 
 * @author Si-Mohamed Lamraoui
 * @date 27.05.10
 */

package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class TestAireDessin extends JComponent implements AdaptateurDessin
{

	private static final long serialVersionUID = 1L;
	private Animation anim;
	private Animation anim2;

	public TestAireDessin()
	{
		float t = (float) 0.05;
		anim = new Animation(this, "images/anim_arrow/arrow", 4, 200, false);
		anim2 = new Animation(this, "images/anim_exp/exp", 20, 100, true, t);
		anim.start();
		anim2.start();
	}
	public void paintComponent(Graphics g)
	{
		Graphics2D draw = (Graphics2D) g;
	
		draw.setPaint(Color.white);
		draw.fillRect(0, 0, this.getSize().width, this.getSize().height);
	
		draw.drawImage(anim.get(), 0, 0, null);
		draw.drawImage(anim.get(), 60, 0, null);
	}  
}
