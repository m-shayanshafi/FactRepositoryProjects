package org.mysoft.td.pgui.renderer.sample;

import org.mysoft.td.engine.objects.GenericEnemy;
import org.mysoft.td.pgui.renderer.GenericObjectRenderer;
import org.mysoft.td.pgui.renderer.RendererConfig;

import processing.core.PGraphics;

public class SampleEnemyRenderer extends GenericObjectRenderer<GenericEnemy> {
	
	public SampleEnemyRenderer(PGraphics g, RendererConfig config) {
		super(g, config);
	}

	@Override
	public void render(GenericEnemy e) {
		
		float c = 1.0f * e.getHitPoints() / e.getMaxHitPoints() * 255.0f;
		
		g.fill(g.color(255, c, 0));
		g.stroke(0);
		
		int x = (int)Math.round(e.getRealPosition().x * config.blockSize + config.blockSize/2);
		int y = (int)Math.round(e.getRealPosition().y * config.blockSize + config.blockSize/2);
		
		g.ellipse(x, y, config.blockSize, config.blockSize);
	}
}
