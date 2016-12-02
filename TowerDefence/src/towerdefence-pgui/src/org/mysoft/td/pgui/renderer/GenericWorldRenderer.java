package org.mysoft.td.pgui.renderer;

import org.mysoft.td.engine.world.GenericWorld;

import processing.core.PGraphics;

public abstract class GenericWorldRenderer<T extends GenericWorld> extends GraphicsRenderer {

	protected RendererConfig config;
	
	public GenericWorldRenderer(PGraphics g, RendererConfig config) {
		super(g);
		this.config = config;
	}

	@Override
	@Deprecated
	public final void render() {
		throw new RendererException("User render(world) instead");
	}

	public abstract void render(T world);
	
}
