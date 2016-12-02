package org.mysoft.td.pgui.renderer.sample;

import org.mysoft.td.engine.objects.sample.ArtilleryTurrent;
import org.mysoft.td.pgui.renderer.AimingTurrentRenderer;
import org.mysoft.td.pgui.renderer.RendererConfig;

import processing.core.PGraphics;

public class ArtilleryTurrentRenderer extends
		AimingTurrentRenderer<ArtilleryTurrent> {

	public ArtilleryTurrentRenderer(PGraphics g, RendererConfig config) {
		super(g, config);
	}

	@Override
	public void renderBase(ArtilleryTurrent t) {
		super.renderBase(t);
	}
	
	@Override
	public void renderTop(ArtilleryTurrent t) {
		g.stroke(0);
		g.fill(g.color(100,200,100));
		
		g.translate(-2.5f, -4f);
		g.rect(0, 0, 15, 8);
	}

}
