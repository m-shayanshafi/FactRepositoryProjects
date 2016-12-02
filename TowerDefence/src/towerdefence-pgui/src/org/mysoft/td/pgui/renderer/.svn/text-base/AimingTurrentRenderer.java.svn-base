package org.mysoft.td.pgui.renderer;

import org.mysoft.td.engine.objects.AimingTurrent;
import org.mysoft.td.engine.objects.ETurrentState;
import org.mysoft.td.engine.objects.GenericBullet;

import processing.core.PGraphics;

public class AimingTurrentRenderer<T extends AimingTurrent> extends GenericObjectRenderer<T> {

	public AimingTurrentRenderer(PGraphics g, RendererConfig config) {
		super(g, config);
	}

	@Override
	public final void render(T t) {
		float cx = (float)t.getCenter().x * config.blockSize;
		float cy = (float)t.getCenter().y * config.blockSize;
		
		float tx = (float)t.getTarget().x * config.blockSize; 
		float ty = (float)t.getTarget().y * config.blockSize;
			
		float dx = tx - cx;
		float dy = ty - cy;
		
		g.pushMatrix();
		
		g.translate(t.getPosition().x * config.blockSize, t.getPosition().y * config.blockSize);
		
		renderBase(t);

		
		g.popMatrix();

		for(GenericBullet b: t.getBullets())
			renderBullet(b);
		
		g.pushMatrix();
		
		g.translate(cx, cy);
		
		g.rotate((float)Math.atan2(dy, dx));

		renderTop(t);
		
		g.popMatrix();	

		
		if(t.state == ETurrentState.FIRE) {
			g.fill(g.color(255,0,0));
			g.line(cx, cy, tx, ty);
		}
		
		

		
	}

	
	
	public void renderBase(T t) {
		g.stroke(100);
		g.fill(g.color(200, 200, 200));
		g.rect(0, 0, t.getWidth() * config.blockSize, t.getHeight() * config.blockSize);
	}
	
	public void renderTop(T t) {
		g.stroke(0);
		g.fill(g.color(100,100,100));
		g.translate(-2.5f, -2.5f);
		g.rect(0, 0, 10, 5);
	}
	
	public void renderBullet(GenericBullet bullet) {
		g.stroke(0);
		g.fill(255);
		g.ellipse((float)bullet.getPosition().x *config.blockSize - 2, (float)bullet.getPosition().y * config.blockSize - 2, 4, 4);
	}
	
}
