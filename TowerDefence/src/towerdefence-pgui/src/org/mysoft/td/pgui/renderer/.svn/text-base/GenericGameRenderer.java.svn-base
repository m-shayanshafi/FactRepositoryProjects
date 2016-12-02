package org.mysoft.td.pgui.renderer;

import org.mysoft.td.engine.action.GenericActionController;
import org.mysoft.td.engine.objects.GenericEnemy;
import org.mysoft.td.engine.objects.GenericTurrent;
import org.mysoft.td.engine.world.GenericWorld;

import processing.core.PGraphics;

public class GenericGameRenderer extends GraphicsRenderer {

	public final static int DEFAULT_BLOCK_SIZE = 10;
	
	protected RendererConfig config;
	protected RendererManager manager = new RendererManager();
	
	protected GenericActionController ctrl;
	
	
	public GenericGameRenderer(PGraphics g, GenericActionController ctrl, RendererConfig config) {
		super(g);
		this.ctrl = ctrl;
		this.config = config;
		init();
	}
	
	public void registerRenderers() {
		throw new RendererException("registerRenderers() is not overriden !");
	}
	
	private void init() {
		registerRenderers();
	}

	protected void renderEnemy(GenericEnemy e) {
		GenericObjectRenderer<GenericEnemy> r = manager.getObjectRenderer(e);
		if(r == null)
			throw new RendererException("No renderer for class " + e.getClass().getName());
		else
			manager.getObjectRenderer(e).render(e);
	}
	
	protected void renderTurrent(GenericTurrent t) {
		GenericObjectRenderer<GenericTurrent> r = manager.getObjectRenderer(t);
		if(r == null)
			throw new RendererException("No renderer for class " + t.getClass().getName());
		else
			manager.getObjectRenderer(t).render(t);
	}
	
	protected void renderWorld(GenericWorld w) {
		GenericWorldRenderer<GenericWorld> r = manager.getWorldRenderer(w);
		if(r == null)
			throw new RendererException("No renderer for class " + w.getClass().getName());
		else
			manager.getWorldRenderer(w).render(w);
	}
	
	private void internalRender() {
		g.beginDraw();
		g.background(0);

		renderWorld(ctrl.world);
		
		for(GenericTurrent t: ctrl.getTurrents()) {
			renderTurrent(t);
		}
		
		for(GenericEnemy e: ctrl.getEnemies()) {
			renderEnemy(e);
		}
		
		customRender();
		
		g.endDraw();		
	}
	
	@Override
	public final void render() {
		internalRender();
	}

	protected void customRender() {
		
	}
}
