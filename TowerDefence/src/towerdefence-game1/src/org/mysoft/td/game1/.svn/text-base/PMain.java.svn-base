package org.mysoft.td.game1;

import java.util.Date;
import java.util.Random;

import org.mysoft.td.engine.action.GenericActionController;
import org.mysoft.td.engine.action.sample.SampleActionController;
import org.mysoft.td.engine.objects.GenericTurrent;
import org.mysoft.td.engine.objects.sample.ArtilleryTurrent;
import org.mysoft.td.engine.objects.sample.MachineGunTurrent;
import org.mysoft.td.engine.objects.sample.SampleEnemy;
import org.mysoft.td.engine.world.sample.SampleWorld;
import org.mysoft.td.pgui.renderer.ProcessingRenderer;
import org.mysoft.td.pgui.renderer.sample.SampleGameRenderer;

import processing.core.PApplet;
import processing.core.PGraphics;

public class PMain extends PApplet {

	PGraphics img;
	ProcessingRenderer r;
	
	GenericActionController ctrl;
	
	public void setup() {
		size(640, 480);

		img = createGraphics(640, 480, PGraphics.P2D);
				
		frameRate(35);
		
		initGame();
		
		r = new SampleGameRenderer(img, ctrl);
		
	}

	public void initGame() {
		SampleWorld w = new SampleWorld();
		
		ctrl = new SampleActionController();
		ctrl.setWorld(w);
		
		Random rnd = new Random((new Date()).getTime());
		
		for(int i=4; i<62; i+=2)
			for(int j=1; j<46; j+=2) {
				int r = rnd.nextInt(128); 
				
				GenericTurrent t = null;
				
				if(r < 10) {
					t = new MachineGunTurrent(w);
					t.setLevel(0);
				} else if(r < 14) {
					t = new ArtilleryTurrent(w);
					t.setLevel(0);
				}
				
				if(t != null) {
					t.setPosition(i, j);
					ctrl.addObject(t);
				}
			}
		
	}
	
	int count = 0;
	int delay = 6;

	
	public void draw() {
		ctrl.calculateTurn();
		
		r.render();
		
		background(0);
		image(img, 0, 0);

		if(count++ % delay == 0) {
			SampleEnemy e = new SampleEnemy(ctrl.world, "test", -1, ctrl.world.getHeight()/2);
			//e.mass = 6.0f;
			//e.maxSpeed = 0.3f;
			ctrl.addObject(e);	
		}
		
	}

	static public void main(String args[]) {
		PApplet.main(new String[] { "--bgcolor=#D4D0C8",
				"org.mysoft.td.game1.PMain" });
	}

}
