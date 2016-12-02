package org.mysoft.td.pgui.renderer;

import org.mysoft.td.engine.objects.GenericObject;

import processing.core.PGraphics;

public abstract class GenericObjectRenderer<T extends GenericObject> extends GraphicsRenderer {

	protected RendererConfig config;
	
	public GenericObjectRenderer(PGraphics g, RendererConfig config) {
		super(g);
		this.config = config;
	}

	@Deprecated
	public final void render() {
		throw new RendererException("User render(object) instead");
	}

	public abstract void render(T obj);
	
}
