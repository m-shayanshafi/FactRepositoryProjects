package org.mysoft.td.pgui.renderer.sample;

import org.mysoft.td.engine.objects.sample.MachineGunTurrent;
import org.mysoft.td.pgui.renderer.AimingTurrentRenderer;
import org.mysoft.td.pgui.renderer.RendererConfig;

import processing.core.PGraphics;

public class MachineGunTurrentRenderer extends
		AimingTurrentRenderer<MachineGunTurrent> {

	public MachineGunTurrentRenderer(PGraphics g, RendererConfig config) {
		super(g, config);
	}

	@Override
	public void renderTop(MachineGunTurrent t) {
/*
		float cx = (float)t.getCenter().x * config.blockSize;
		float cy = (float)t.getCenter().y * config.blockSize;
		
		float tx = (float)t.getTarget().x * config.blockSize; 
		float ty = (float)t.getTarget().y * config.blockSize;
		
		if(t.state == ETurrentState.FIRE) {
			g.fill(g.color(255,0,0));
			g.line(cx, cy, tx, ty);
		}
*/
		g.stroke(0);
		g.fill(g.color(100,100,100));
		
		g.translate(-2.5f, -2.5f);
		g.rect(0, 0, 10, 5);
	}

	
	
}
