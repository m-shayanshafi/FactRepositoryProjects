package org.mysoft.td.pgui.renderer.sample;

import org.mysoft.td.engine.objects.GenericTurrent;
import org.mysoft.td.pgui.renderer.GenericObjectRenderer;
import org.mysoft.td.pgui.renderer.RendererConfig;

import processing.core.PGraphics;

public class SampleTurrentRenderer extends GenericObjectRenderer<GenericTurrent>  {

	public SampleTurrentRenderer(PGraphics g, RendererConfig config) {
		super(g, config);
	}

	@Override
	public void render(GenericTurrent t) {
		g.stroke(0);
		g.fill(100);
		g.rect(t.getPosition().x * config.blockSize, t.getPosition().y * config.blockSize, t.getWidth() * config.blockSize, t.getHeight() * config.blockSize);
	}
	
}
