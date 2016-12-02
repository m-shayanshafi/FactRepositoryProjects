package org.mysoft.td.pgui.renderer.sample;

import org.mysoft.td.engine.world.EWorldObjectType;
import org.mysoft.td.engine.world.sample.SampleWorld;
import org.mysoft.td.pgui.renderer.GenericWorldRenderer;
import org.mysoft.td.pgui.renderer.RendererConfig;

import processing.core.PGraphics;

public class SampleWorldRenderer extends GenericWorldRenderer<SampleWorld> {

	public SampleWorldRenderer(PGraphics g, RendererConfig config) {
		super(g, config);
	}

	@Override
	public void render(SampleWorld w) {
		g.noStroke();
		for(int i=0; i<w.getWidth(); i++)
			for(int j=0; j<w.getHeight(); j++) {
				EWorldObjectType t = w.getObjectTypeAt(i, j);
				if(t == EWorldObjectType.WALL) {
					g.fill(g.color(0, 0, 0));
					g.rect(i*config.blockSize, j*config.blockSize, config.blockSize, config.blockSize);
				} else if(t == EWorldObjectType.EMPTY) {
					int d = ((SampleWorld)w).getIndex().getTurrentsInRange(i, j).size();
					d *= 20;
					g.fill(g.color(255, 255 - d, 255 - d));
					g.rect(i*config.blockSize, j*config.blockSize, config.blockSize, config.blockSize);
				} else {
					g.fill(g.color(255, 255, 255));
					g.rect(i*config.blockSize, j*config.blockSize, config.blockSize, config.blockSize);
				}
			}
	}
	
}
