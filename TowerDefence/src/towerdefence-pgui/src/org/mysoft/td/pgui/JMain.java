package org.mysoft.td.pgui;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import processing.core.PApplet;
import processing.core.PGraphics;

public class JMain {

	public static void test(PGraphics img) {
		img.beginDraw();
		img.lights();
		img.background(0);

		// Change height of the camera with mouseY
		img.camera(30.0f, 0, 220.0f, // eyeX, eyeY, eyeZ
				0.0f, 0.0f, 0.0f, // centerX, centerY, centerZ
				0.0f, 1.0f, 0.0f); // upX, upY, upZ

		img.noStroke();
		img.box(90);
		img.stroke(255);
		img.line(-100, 0, 0, 100, 0, 0);
		img.line(0, -100, 0, 0, 100, 0);
		img.line(0, 0, -100, 0, 0, 100);		
		
		img.endDraw();
		

	}
	
	public static void main(String[] args) {

		JFrame f = new JFrame("A JFrame");
		f.setSize(640, 360);
		f.setLocation(300, 200);
		
		PApplet a = new PApplet();
		
		PGraphics img = a.createGraphics(640, 480, a.P3D);
		
		test(img);
		
		JPanel p = new JPanel(); 
		
		f.getContentPane().add(p);
		
		f.setVisible(true);
		
		Graphics g = p.getGraphics();
		
		if(g==null) 
			System.out.println("null");
		else
			g.drawImage(img.getImage(), 0, 0, null);
		
		

	}

}
